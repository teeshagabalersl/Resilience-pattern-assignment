package order.resilience;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Timeout {

    // TODO: Implement Timeout resilience pattern.
    // - Execute the operation with a configured timeout.
    // - Fail the operation if it exceeds the timeout.
    //
    // Example:
    //
    // Timeout.execute(
    //         () -> callDownstreamService(),
    //         5,
    //         TimeUnit.SECONDS
    // );

}