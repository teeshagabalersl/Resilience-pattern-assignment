package downstream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/*
 * This service alternates between UP and DOWN states.
 * It remains UP for 30 seconds and DOWN for 30 seconds.
 * Requests return 200 when UP and 503 when DOWN.
 */
public class FlappingService {

    private static final int PORT = 8084;

    private static final long UP_TIME =
            30_000;

    private static final long DOWN_TIME =
            30_000;

    private static long stateStartTime;

    private static boolean isUp = true;

    public static void main(String[] args)
            throws Exception {

        stateStartTime =
                System.currentTimeMillis();

        HttpServer server =
                HttpServer.create(
                        new InetSocketAddress(PORT),
                        0
                );

        server.createContext(
                "/data",
                FlappingService::handleRequest
        );

        server.start();

        System.out.println(
                "Flapping Service running on port "
                        + PORT
        );

        System.out.println(
                "Starting in UP state"
        );

        System.out.println(
                "UP for "
                        + UP_TIME / 1000
                        + " seconds"
        );

        System.out.println(
                "DOWN for "
                        + DOWN_TIME / 1000
                        + " seconds"
        );

        startStateManager();
    }

    private static void startStateManager() {

        Thread stateManager =
                new Thread(() -> {

                    while (true) {

                        long elapsed =
                                System.currentTimeMillis()
                                        - stateStartTime;

                        long currentDuration =
                                isUp
                                        ? UP_TIME
                                        : DOWN_TIME;

                        if (elapsed >=
                                currentDuration) {

                            isUp = !isUp;

                            stateStartTime =
                                    System.currentTimeMillis();

                            System.out.println(
                                    "Flapping Service changed state to: "
                                            + (isUp
                                            ? "UP"
                                            : "DOWN")
                            );
                        }

                        try {

                            Thread.sleep(100);

                        } catch (
                                InterruptedException e) {

                            Thread.currentThread()
                                    .interrupt();

                            break;
                        }
                    }

                });

        stateManager.setDaemon(true);

        stateManager.start();
    }

    private static void handleRequest(
            HttpExchange exchange)
            throws IOException {

        if (isUp) {

            System.out.println(
                    "Flapping Service: UP → SUCCESS"
            );

            sendResponse(
                    exchange,
                    200,
                    "Flapping service is UP"
            );

        } else {

            System.out.println(
                    "Flapping Service: DOWN → FAIL"
            );

            sendResponse(
                    exchange,
                    503,
                    "Flapping service is DOWN"
            );
        }
    }

    private static void sendResponse(
            HttpExchange exchange,
            int statusCode,
            String response)
            throws IOException {

        byte[] responseBytes =
                response.getBytes();

        exchange.sendResponseHeaders(
                statusCode,
                responseBytes.length
        );

        try (OutputStream output =
                     exchange.getResponseBody()) {

            output.write(responseBytes);
        }
    }
}