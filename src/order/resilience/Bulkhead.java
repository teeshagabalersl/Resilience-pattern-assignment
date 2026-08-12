package order.resilience;

import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

public class Bulkhead {

    // TODO: Implement Bulkhead resilience pattern.
    //
    // Requirements:
    // 1. Limit the number of concurrent requests.
    // 2. Use Semaphore to control access to the limited resource.
    // 3. Acquire a permit before executing the operation.
    // 4. Release the permit after the operation completes,
    //    including when an exception occurs.
    // 5. If no permit is available, fail the request appropriately.
    //
    // Example:
    // Bulkhead bulkhead = new Bulkhead(2);
    //
    // bulkhead.execute(() -> callDownstreamService());

}