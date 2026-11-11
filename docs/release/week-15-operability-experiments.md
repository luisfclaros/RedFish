# Week 15 — Load and controlled resilience experiments

**Target:** RedFish modular monolith (Spring Boot + MySQL + Docker Compose)  
**Status:** Experiment instructions prepared; no load or chaos result is claimed by this document.

## 1. Load test with k6

The script at `RedFish/performance/k6-load.js` performs read-only requests against:
- `GET /actuator/health` (public health endpoint).
- `GET /api/v1/products` (only when Basic Auth credentials are supplied).

It uses a short ramp profile (5 then 10 virtual users) and provisional, configurable thresholds:
- p95 request duration below `SLO_P95_MS` (default 500 ms).
- failed request rate below `SLO_ERROR_RATE` (default 1%).
- checks success above 99%.

These are **provisional test targets**, not an approved production SLO. Confirm the target with the instructor/team before treating it as a release gate. Run first against a local or isolated QA environment, never against production.

### Run

Install k6, start RedFish in the isolated test environment, then from the repository root:

```bash
k6 run RedFish/performance/k6-load.js
```

To include the authenticated products endpoint, provide a dedicated low-privilege test account. Do not put credentials in the script or commit them:

PowerShell:
```powershell
$env:K6_USERNAME = "test-user"
$env:K6_PASSWORD = "use-a-local-test-password"
$env:BASE_URL = "http://localhost:8080"
k6 run .\RedFish\performance\k6-load.js
```

Linux/macOS:
```bash
K6_USERNAME='test-user' K6_PASSWORD='use-a-local-test-password' BASE_URL='http://localhost:8080' k6 run RedFish/performance/k6-load.js
```

Save the terminal summary and exact tested commit SHA with the evidence. Record environment, duration, VUs, p95, error rate and whether thresholds passed. Do not infer production capacity from this small local profile.

## 2. Controlled dependency-failure experiment (MySQL)

This is a manual, destructive-to-availability experiment. It must run only in a disposable local/QA environment with no real users or valuable data. It intentionally interrupts the database connection. It does not delete the persistent volume.

### Preconditions
- Confirm the Compose project and environment file point to the isolated test database.
- Confirm no one else is using this environment.
- Capture the current commit SHA and health response.
- Ensure you know how to access Docker logs.
- Never run `docker compose down -v`; never remove the MySQL volume.

### Procedure

From the directory containing the Compose file and the correct test `.env`:

```bash
docker compose ps
curl --fail http://localhost:8080/actuator/health
docker compose stop mysql
```

During the outage, issue a small number of read-only requests to `/actuator/health` and (with test credentials) `/api/v1/products`. Record status codes, response times, and application logs. Verify the application does not expose secrets or stack traces to clients.

Restore the dependency immediately:

```bash
docker compose start mysql
docker compose ps
curl --retry 12 --retry-delay 5 --retry-connrefused --fail http://localhost:8080/actuator/health
```

If the application does not recover automatically after MySQL returns, capture logs and perform a controlled application restart only after recording the failure:

```bash
docker compose restart app
curl --retry 12 --retry-delay 5 --retry-connrefused --fail http://localhost:8080/actuator/health
```

### PowerShell / Windows commands

For Windows with Docker Desktop, use `curl.exe` explicitly (not the PowerShell `curl` alias). Run these commands only after confirming the active Compose project and `.env` point to a disposable test database:

```powershell
docker compose ps
curl.exe --fail http://localhost:8080/actuator/health
docker compose stop mysql

# During the outage: capture the response and timestamp
curl.exe --include http://localhost:8080/actuator/health

# Restore MySQL and wait for the application health endpoint
docker compose start mysql
docker compose ps
curl.exe --retry 12 --retry-delay 5 --retry-connrefused --fail http://localhost:8080/actuator/health

# Capture logs before any optional application restart
docker compose logs --since 10m app mysql
```

If automatic recovery fails, record the failure and logs first, then use `docker compose restart app` and repeat the health check. Do not use `docker compose down -v` or remove the named volume.

### Evidence and acceptance

Record:
- outage start/end timestamps and exact commands;
- health/API status and latency before, during and after;
- `docker compose logs --since 10m app mysql` output with secrets/PII redacted;
- whether the app recovered automatically or required restart;
- any failed/in-flight operation and whether persisted data remained consistent;
- exact branch and commit SHA.

**Pass criteria:** the failure is detected; clients receive controlled errors (not sensitive internals); the database is restored; the app returns healthy; data integrity is verified. If any criterion fails, record it as a defect and do not mark chaos/resilience as passed.

## 3. Limitations

- This test exercises a real external dependency of the current monolith. It does not claim inter-microservice resilience.
- Stopping MySQL is not a zero-downtime deployment or rollback test.
- The local Compose health endpoint reports application health; it does not provide a full metrics/traces/alerting stack.
- A separate rollback drill and operational observability validation remain required for the release checklist.
