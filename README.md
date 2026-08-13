# Testing Resilience Patterns

The application consists of one **Order Service** and four **downstream services**. Docker Compose is provided to run the services in isolated containers.

Each resilience pattern can be tested independently by starting only the Order Service and the downstream service required for that pattern.

## Services

| Service | Port | Resilience Pattern |
|----------|------|--------------------|
| Order Service | 8080 | Applies resilience patterns |
| Slow Service | 8081 | Timeout |
| Unstable Service | 8082 | Retry + Backoff |
| Limited Capacity Service | 8083 | Bulkhead |
| Flapping Service | 8084 | Circuit Breaker |

---

## Prerequisites

The assignment uses Docker to build and run the services.

Before starting, install Docker Desktop:

- [Download Docker Desktop](https://www.docker.com/products/docker-desktop/)

After installation, start Docker Desktop and verify the installation:

```bash
docker --version
docker compose version
```

---

## Start the Services

From the project root directory, run:

```bash
docker compose up --build
```

This starts all services.

You should see logs similar to:

```text
order-service-1              | Order Service running on port 8080
slow-service-1               | Slow Service running on port 8081
unstable-service-1           | Unstable Service running on port 8082
limited-capacity-service-1   | Limited Capacity Service running on port 8083
flapping-service-1           | Flapping Service running on port 8084
```

Check running containers:

```bash
docker compose ps
```

---

## Stop the Services

To stop and remove containers:

```bash
docker compose down
```

---

## Rebuilding Images

Use `--build`:

- The first time you run the application
- After modifying Java source code
- After changing the Dockerfile
- After changing Docker Compose configuration

Example:

```bash
docker compose up --build
```

If no changes were made, you can start services without rebuilding:

```bash
docker compose up
```

---

# Testing Individual Resilience Patterns

For easier log analysis, start only the services required for a specific pattern.

> **Note:** You only need to use `--build` the first time you start the
> services, or when you make changes to the Java source code, `Dockerfile`, or
> Docker Compose configuration. For subsequent runs without any changes, use
> `docker compose up` without `--build`.

> **Note:** Before testing a new resilience pattern, make sure any previously
> running services are stopped. This avoids confusion between logs from
> different tests.

### Stop Running Services

If the services are running in the terminal, press:

```text
Ctrl + C
```
or use:

```bash
docker compose down
```

---

## Timeout

Start the required services:

```bash
docker compose up --build order-service slow-service
```

Open:

```text
http://127.0.0.1:8080/order?service=slow
```

Or use:

```bash
curl "http://127.0.0.1:8080/order?service=slow"
```

### Observe the Logs

The logs for the running services are displayed directly in the terminal where
`docker compose up` is running.

Observe the logs to verify that:

- The Order Service receives the request.
- The Order Service calls the Slow Service.
- The configured timeout is applied.
- The request fails when the timeout is exceeded.
- The Order Service does not wait indefinitely.

Take a screenshot of the terminal output showing the timeout behavior.

---

## Retry + Backoff

Start:

```bash
docker compose up --build order-service unstable-service
```

Open:

```text
http://127.0.0.1:8080/order?service=unstable
```

Or:

```bash
curl "http://127.0.0.1:8080/order?service=unstable"
```

### Observe the Logs

The logs for the Order Service and Unstable Service are displayed directly in
the terminal where `docker compose up` is running.

Observe the logs to verify that:

- Failed requests are retried.
- The maximum retry attempts are respected.
- A delay is introduced between retry attempts.
- The configured backoff strategy is applied.

Take a screenshot or recording showing the retry attempts and backoff delays.

---

## Bulkhead

Start:

```bash
docker compose up --build order-service limited-capacity-service
```

Send multiple concurrent requests:

```bash
for i in {1..10}; do
  curl "http://127.0.0.1:8080/order?service=limited" &
done
wait
```

### Observe the Logs

The logs for the Order Service and Limited Capacity Service are displayed
directly in the terminal where `docker compose up` is running.

Observe the logs to verify that:

- Multiple requests are processed concurrently.
- The Bulkhead limits concurrent requests.
- Requests exceeding the configured capacity are rejected or handled
  according to the implementation.

Take a screenshot of the terminal output showing the Bulkhead behavior.

---

## Circuit Breaker

Start:

```bash
docker compose up --build order-service flapping-service
```

Open:

```text
http://127.0.0.1:8080/order?service=flapping
```

Or:

```bash
curl "http://127.0.0.1:8080/order?service=flapping"
```

Run the request multiple times.

### Observe the Logs

The logs for the Order Service and Flapping Service are displayed directly in
the terminal where `docker compose up` is running.

Observe the logs to verify that:

- The Flapping Service changes between UP and DOWN states.
- The Circuit Breaker records failures.
- The Circuit Breaker opens after reaching the configured failure threshold.
- Requests are rejected while the circuit is OPEN.
- After the configured wait duration, the Circuit Breaker allows a test request.

Take a screenshot or recording showing the Circuit Breaker state changes.

---

## Viewing Logs

### Order Service

```bash
docker compose logs -f order-service
```

### Slow Service

```bash
docker compose logs -f slow-service
```

### Unstable Service

```bash
docker compose logs -f unstable-service
```

### Limited Capacity Service

```bash
docker compose logs -f limited-capacity-service
```

### Flapping Service

```bash
docker compose logs -f flapping-service
```

View logs for multiple services:

```bash
docker compose logs -f order-service flapping-service
```

---

## Useful Commands

### List running containers

```bash
docker compose ps
```

### Stop services

```bash
docker compose down
```

### Remove containers and rebuild

```bash
docker compose up --build
```

---

## Running from IntelliJ

The application can also be run directly from IntelliJ.

When running locally, the Order Service uses:

```text
http://localhost:8081
http://localhost:8082
http://localhost:8083
http://localhost:8084
```

When running in Docker, service URLs are provided through environment variables:

```text
http://slow-service:8081
http://unstable-service:8082
http://limited-capacity-service:8083
http://flapping-service:8084
```

This allows the same codebase to run both locally and inside Docker.