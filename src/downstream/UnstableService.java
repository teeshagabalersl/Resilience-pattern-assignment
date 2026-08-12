package downstream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Random;

/*
 * This service randomly succeeds or fails.
 * Each request has a chance of returning a successful response
 * or an error response.
 */
public class UnstableService {

    private static final int PORT = 8082;

    private static final Random random =
            new Random();

    public static void main(String[] args)
            throws Exception {

        HttpServer server =
                HttpServer.create(
                        new InetSocketAddress(PORT),
                        0
                );

        server.createContext(
                "/data",
                UnstableService::handleRequest
        );

        server.start();

        System.out.println(
                "Unstable Service running on port "
                        + PORT
        );
    }

    private static void handleRequest(
            HttpExchange exchange)
            throws IOException {

        boolean shouldFail =
                random.nextBoolean();

        if (shouldFail) {

            System.out.println(
                    "Unstable Service: FAIL"
            );

            sendResponse(
                    exchange,
                    500,
                    "Temporary failure"
            );

        } else {

            System.out.println(
                    "Unstable Service: SUCCESS"
            );

            sendResponse(
                    exchange,
                    200,
                    "Unstable service response"
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