package order.resilience;

import java.util.concurrent.Callable;

public class CircuitBreaker {

    // TODO: Implement Circuit Breaker.
    // - Maintain CLOSED, OPEN, and HALF_OPEN states.
    // - Track failures and open the circuit after a threshold.
    // - Reject requests while OPEN.
    // - After a recovery period, allow a test request in HALF_OPEN.
    // - Close the circuit if the test succeeds; reopen if it fails.
    //
    // Example:
    // CircuitBreaker circuitBreaker = new CircuitBreaker(3, 10000);
    //
    // circuitBreaker.execute(() -> callDownstreamService());
    //
    // Expected output:
    //
    // When the downstream service is available:
    //
    // Received order request
    // Service = <serviceName>
    // HTTP GET → http://localhost:<portNumber>/data
    // Response status: 200
    //
    // When the failure threshold is reached:
    //
    // Received order request
    // Service = <serviceName>
    // HTTP GET → http://localhost:<portNumber>/data
    // Response status: 503
    // Circuit failure count = 3
    // Circuit OPEN
    //
    // While the circuit is OPEN:
    //
    // Received order request
    // Service = <serviceName>
    // Circuit is OPEN - request rejected
    //
    // After the recovery period:
    //
    // Circuit changed to HALF_OPEN
    //
    // If the test request succeeds:
    //
    // HTTP GET → http://localhost:<portNumber>/data
    // Response status: 200
    // Circuit CLOSED
    //
    // If the test request fails:
    //
    // HTTP GET → http://localhost:<portNumber>/data
    // Response status: 503
    // Circuit OPEN

}