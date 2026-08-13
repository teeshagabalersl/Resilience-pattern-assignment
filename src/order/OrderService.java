package order;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import order.resilience.Bulkhead;
import order.resilience.CircuitBreaker;
import order.resilience.RetryBackoff;
import order.resilience.Timeout;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class OrderService {

    private static final HttpClient httpClient =
            HttpClient.newHttpClient();

    private static final String SLOW_SERVICE_URL =
            System.getenv()
                    .getOrDefault(
                            "SLOW_SERVICE_URL",
                            "http://localhost:8081"
                    );

    private static final String UNSTABLE_SERVICE_URL =
            System.getenv()
                    .getOrDefault(
                            "UNSTABLE_SERVICE_URL",
                            "http://localhost:8082"
                    );

    private static final String LIMITED_CAPACITY_SERVICE_URL =
            System.getenv()
                    .getOrDefault(
                            "LIMITED_CAPACITY_SERVICE_URL",
                            "http://localhost:8083"
                    );

    private static final String FLAPPING_SERVICE_URL =
            System.getenv()
                    .getOrDefault(
                            "FLAPPING_SERVICE_URL",
                            "http://localhost:8084"
                    );

    public static void main(String[] args)
            throws Exception {

        HttpServer server =
                HttpServer.create(
                        new InetSocketAddress(8080),
                        0
                );
        server.createContext(
                "/order",
                OrderService::handleOrder
        );

        /*
         * Allow multiple requests to be processed
         * concurrently.
         */
        server.setExecutor(
                Executors.newCachedThreadPool()
        );

        server.start();

        System.out.println(
                "Order Service running on port 8080"
        );
    }

    private static void handleOrder(
            HttpExchange exchange)
            throws IOException {

        String query =
                exchange.getRequestURI()
                        .getQuery();

        String service =
                getService(query);

        System.out.println(
                "\nReceived order request"
        );

        System.out.println(
                "Service = " + service
        );

        try {

            String result;

            switch (service) {

                case "slow":

                    result =
                            callSlowService();

                    break;

                case "unstable":

                    result =
                            callUnstableService();

                    break;

                case "limited":

                    result =
                            callLimitedCapacityService();

                    break;

                case "flapping":

                    result =
                            callFlappingService();

                    break;

                default:

                    sendResponse(
                            exchange,
                            400,
                            "Invalid service. Use: "
                                    + "slow, unstable, limited, flapping"
                    );

                    return;
            }

            sendResponse(
                    exchange,
                    200,
                    "Order successful: "
                            + result
            );

        } catch (Exception e) {

            System.out.println(
                    "Order failed: "
                            + e.getMessage()
            );

            sendResponse(
                    exchange,
                    500,
                    "Order failed: "
                            + e.getMessage()
            );
        }
    }

    /*
     * TODO:
     * Identify the problem with this downstream service
     * and implement an appropriate resilience strategy.
     */
    private static String callSlowService()
            throws Exception {

        return callService(
                SLOW_SERVICE_URL + "/data"
        );
    }

    /*
     * TODO:
     * Identify the problem with this downstream service
     * and implement an appropriate resilience strategy.
     */
    private static String callUnstableService()
            throws Exception {

        return callService(
                UNSTABLE_SERVICE_URL + "/data"
        );
    }

    /*
     * TODO:
     * Identify the problem with this downstream service
     * and implement an appropriate resilience strategy.
     */
    private static String callLimitedCapacityService()
            throws Exception {

        return callService(
                LIMITED_CAPACITY_SERVICE_URL + "/data"
        );
    }

    /*
     * TODO:
     * Identify the problem with this downstream service
     * and implement an appropriate resilience strategy.
     */
    private static String callFlappingService()
            throws Exception {

        return callService(
                FLAPPING_SERVICE_URL + "/data"
        );
    }

    /*
     * Common HTTP call.
     * Do not modify unless required by your implementation.
     */
    private static String callService(
            String url)
            throws Exception {

        System.out.println(
                "HTTP GET → " + url
        );

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        System.out.println(
                "Response status: "
                        + response.statusCode()
        );

        if (response.statusCode() < 200
                || response.statusCode() >= 300) {

            throw new IOException(
                    "Downstream service returned HTTP "
                            + response.statusCode()
                            + ": "
                            + response.body()
            );
        }

        return response.body();
    }

    /*
     * Extracts ?service=... from the request URL.
     */
    private static String getService(
            String query) {

        if (query == null) {
            return "none";
        }

        for (String parameter :
                query.split("&")) {

            if (parameter.startsWith(
                    "service=")) {

                return parameter.substring(
                        "service=".length()
                );
            }
        }

        return "none";
    }

    private static void sendResponse(
            HttpExchange exchange,
            int statusCode,
            String response)
            throws IOException {

        byte[] bytes =
                response.getBytes();

        exchange.sendResponseHeaders(
                statusCode,
                bytes.length
        );

        try (OutputStream output =
                     exchange.getResponseBody()) {

            output.write(bytes);
        }
    }
}
