package order.resilience;

import java.util.concurrent.Callable;

public class RetryBackoff {

    // TODO: Implement Retry with exponential Backoff.
    // - Retry failed operations up to the maximum number of attempts.
    // - Wait between retry attempts.
    // - Increase the delay after each failed attempt.
    //
    // Example:
    //
    // RetryBackoff retryBackoff =
    //         new RetryBackoff();
    //
    // retryBackoff.execute(
    //         () -> callDownstreamService(),
    //         3,
    //         1000
    // );
    //
    // Expected output:
    //
    // When the request succeeds on the first attempt:
    //
    // Received order request
    // Service = <serviceName>
    // Using Retry + Backoff for <serviceName>
    // Attempt 1
    // HTTP GET → http://localhost:<portNumber>/data
    // Response status: 200
    //
    // When a retry is required:
    //
    // Received order request
    // Service = <serviceName>
    // Using Retry + Backoff for <serviceName>
    // Attempt 1
    // HTTP GET → http://localhost:<portNumber>/data
    // Response status: 500
    // Attempt 1 failed: <error message>
    // Waiting 1000 ms before retry
    // Attempt 2
    // HTTP GET → http://localhost:<portNumber>/data
    // Response status: 200
    //
    // If all attempts fail:
    //
    // Attempt 3
    // HTTP GET → http://localhost:<portNumber>/data
    // Response status: 500
    // Attempt 3 failed: <error message>
    // Maximum retry attempts reached

}