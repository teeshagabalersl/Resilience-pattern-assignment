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

}