package downstream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ThreadLocalRandom;

/*
 * This service responds after a random delay.
 * The delay can range from 1 to 10 seconds.
 * Requests may complete quickly or take longer to respond.
 */
public class SlowService {

    private static final int PORT = 8081;

    private static final int MIN_DELAY = 1000;
    private static final int MAX_DELAY = 10000;

    public static void main(String[] args)
            throws Exception {

        HttpServer server =
                HttpServer.create(
                        new InetSocketAddress(PORT),
                        0
                );

        server.createContext(
                "/data",
                SlowService::handleRequest
        );

        server.start();

        System.out.println(
                "Slow Service running on port "
                        + PORT
        );

        System.out.println(
                "Random delay: 1-10 seconds"
        );
    }

    private static void handleRequest(
            HttpExchange exchange)
            throws IOException {

        int delay =
                ThreadLocalRandom.current()
                        .nextInt(
                                MIN_DELAY,
                                MAX_DELAY + 1
                        );

        System.out.println(
                "Slow Service: request received"
        );

        System.out.println(
                "Sleeping for "
                        + delay
                        + " ms..."
        );

        try {

            Thread.sleep(delay);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            sendResponse(
                    exchange,
                    500,
                    "Request interrupted"
            );

            return;
        }

        System.out.println(
                "Slow Service: sending response"
        );

        sendResponse(
                exchange,
                200,
                "Slow service response after "
                        + delay
                        + " ms"
        );
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