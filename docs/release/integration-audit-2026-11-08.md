# RedFish integration audit — 2026-11-08

## Scope and safety

- Working branch: `integration/redfish-final-2026-11-08`, created from `Develop`.
- Protected branches `main`, `Develop`, and `Qa` have not been modified directly.
- The user's existing first-cut project and Docker database volume are to be preserved until the new release is locally verified.
- The previously confirmed SQL backup is `C:\Users\LUIS FERNANDO\redfish-backup-20261108.sql`.

## Implemented in this integration branch

### API and persistence

- Products and customers: existing REST create/list/get endpoints with JPA persistence.
- Vehicles: create/list/get REST API, unique plate validation, JPA persistence, and vehicle-active validation before dispatch.
- Production: ponds create/list/get and feeding-record create/list endpoints, with MySQL JPA persistence.
- Inventory: initial stock registration, current-stock listing, stock entries/exits and movement history, with MySQL persistence.
- Orders: create/list/get, line items and totals, lifecycle transitions, JPA persistence, and transactional stock reservation. Cancelling an order restores its reserved quantities.
- Dispatches: schedule/list/get and lifecycle endpoints. Scheduling validates that the order is in preparation and the vehicle is active. Moving a dispatch into transit synchronizes the order to DISPATCHED.
- Security: users stored in MySQL, BCrypt password hashes, HTTP Basic authentication, ADMIN/OPERATOR roles, and bootstrap administrator configured through environment variables.
- Browser UI: responsive static dashboard served by Spring Boot at `/`, with forms and tables for products, customers, vehicles, ponds, feeding, inventory, orders and dispatches.
- OpenAPI YAML expanded to document the operational modules and HTTP Basic security.

### Build and deployment

- Java 17 / Gradle / Spring Boot.
- Dockerfile runs the application as a non-root user and installs curl for the health check.
- GitHub Actions CI includes Gradle tests, JaCoCo report generation, Docker image build, CodeQL analysis and Trivy filesystem scanning.
- Compose environment examples include bootstrap admin variables. Replace every example password before use.

## Important limitations still open

- Feeding records currently track food type and quantity but do not automatically deduct an inventory product; the UI states this limitation. The data model must link a feeding record to an inventory product before consumption can be applied safely.
- Vehicles and ponds currently support create/list/get; edit/deactivate workflows are not complete.
- Users can be created by an ADMIN through the API, but there is no user-administration screen in the UI.
- Reporting has event-consumer/persistence foundations but no dedicated reporting dashboard or endpoints.
- HTTP Basic is suitable for this academic MVP only when used over HTTPS outside localhost. It is not a substitute for a production identity provider.
- No final downloadable release archive has been prepared yet.

## Validation status

- An earlier CI run on the integration branch passed Gradle tests, Docker image build, CodeQL and Trivy before the later module additions.
- The current integration branch has database-backed tests for MySQL persistence, stock reservation/restoration, dispatch/order lifecycle, dashboard delivery and authentication. The latest CI run must finish successfully before these new tests are considered verified.
- A green contract-test run alone does not prove the full application is deployable; the full RedFish CI workflow and local Docker Compose smoke test are required.

## Release plan

1. Finish and repair any failures from the latest CI run.
2. Verify the current branch's database-backed tests and complete Docker image build.
3. Review the API/UI workflows, especially authentication, stock movement, order cancellation and dispatch transitions.
4. Prepare a clean release archive and exact Windows startup instructions.
5. Have the user run the new copy on port 8080 and confirm it works before considering replacement of the old project.
6. Do not merge this draft PR or delete the old project/database volume until the user has verified the new version.
