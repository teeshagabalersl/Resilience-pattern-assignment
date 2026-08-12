## Testing Resilience Patterns

After implementing the resilience patterns, start all four downstream services and the Order Service from IntelliJ.

### Step 1: Start the Services

Start the following services:

| Service | Port |
|---|---:|
| Slow Service | 8081 |
| Unstable Service | 8082 |
| Limited Capacity Service | 8083 |
| Flapping Service | 8084 |
| Order Service | 8080 |

Start the Downstream Services

Run each downstream service and order service separately in IntelliJ.

For each file:

- Open the Java file.
- Find the main() method.
- Click the Run ▶ button.
- Keep the service running.
- Repeat for the remaining services.

---

### Step 2: Test Slow Service

Open the following URL in a browser:

http://127.0.0.1:8080/order?service=slow

Observe the Order Service console.

Verify that:

- The request is sent to the Slow Service.
- The configured timeout is applied.
- The request fails when the timeout is exceeded.
- The Order Service does not wait indefinitely.

Take a screenshot of the console output showing the timeout behavior.

---

### Step 3: Test Unstable Service

Open the following URL:

http://127.0.0.1:8080/order?service=unstable

Run the request multiple times because the service can fail intermittently.

Observe the Order Service console.

Verify that:

- Failed requests are retried according to the configured retry policy.
- The maximum number of attempts is respected.
- A delay is introduced between retry attempts.
- The delay increases according to the configured backoff strategy.

Take a screenshot or video showing the retry attempts and backoff delays.

---

### Step 4: Test Limited Capacity Service

Open the following URL:

http://127.0.0.1:8080/order?service=limited

To test concurrent requests, open the URL in multiple browser tabs/windows or use a tool such as `curl` to send multiple requests at the same time.

For example:

```bash
for i in {1..10}; do
  curl "http://127.0.0.1:8080/order?service=limited" &
done
wait
```

### Step 4: Test Flapping Service

Open the following URL in a browser:

http://127.0.0.1:8080/order?service=flapping

Observe the Order Service console and send the request multiple times.

Verify that:
- The Flapping Service initially responds successfully and then starts failing.
- The Circuit Breaker records the failures.
- The Circuit Breaker opens after the configured failure threshold is reached.
- Once the circuit is open, further requests are rejected without calling the Flapping Service.
- After the configured wait duration, the Circuit Breaker allows a test request to check whether the service has recovered.

### Check the Logs

Each running service has its own Run console in IntelliJ.

Use the OrderService console to observe:

- Incoming requests
- Downstream service calls
- Timeout behavior
- Retry attempts
- Backoff delays
- Circuit Breaker state changes
- Bulkhead behavior

You can switch between the service consoles from the bottom of IntelliJ.

> Important: Do not stop the downstream services while testing. They need to remain running so that the Order Service can communicate with them.
