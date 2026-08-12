package downstream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Semaphore;

/*
 * This service can process only a limited number of requests concurrently.
 * Additional requests are rejected when the service reaches its capacity.
 * Each accepted request takes several seconds to complete.
 */
public class LimitedCapacityService {

    private static final int PORT = 8083;

    private static final int MAX_CONCURRENT_REQUESTS = 2;

    private static final int PROCESSING_TIME = 10000;

    private static final Semaphore semaphore =
            new Semaphore(
                    MAX_CONCURRENT_REQUESTS
            );

    public static void main(String[] args)
            throws Exception {

        HttpServer server =
                HttpServer.create(
                        new InetSocketAddress(PORT),
                        0
                );

        server.createContext(
                "/data",
                LimitedCapacityService::handleRequest
        );

        server.start();

        System.out.println(
                "Limited Capacity Service running on port "
                        + PORT
        );

        System.out.println(
                "Maximum concurrent requests: "
                        + MAX_CONCURRENT_REQUESTS
        );
    }

    private static void handleRequest(
            HttpExchange exchange)
            throws IOException {

        boolean acquired =
                semaphore.tryAcquire();

        if (!acquired) {

            System.out.println(
                    "Limited Capacity Service: "
                            + "CAPACITY FULL - REJECTING"
            );

            sendResponse(
                    exchange,
                    503,
                    "Service capacity exceeded"
            );

            return;
        }

        try {

            System.out.println(
                    "Limited Capacity Service: "
                            + "request accepted"
            );

            System.out.println(
                    "Available permits: "
                            + semaphore.availablePermits()
            );

            Thread.sleep(PROCESSING_TIME);

            sendResponse(
                    exchange,
                    200,
                    "Limited capacity service response"
            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            sendResponse(
                    exchange,
                    500,
                    "Request interrupted"
            );

        } finally {

            semaphore.release();

            System.out.println(
                    "Limited Capacity Service: "
                            + "permit released"
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