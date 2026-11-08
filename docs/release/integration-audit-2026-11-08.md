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
- Production: ponds create/list/get and feeding-record create/list/get endpoints, with MySQL JPA persistence. Each feeding record references an inventory input product; the service validates the product type, deducts stock and records the exit movement in the same transaction.
- Inventory: initial stock registration, current-stock listing/get, stock entries/exits and movement history, with MySQL persistence.
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

- Feeding records now require an inventory product of type INSUMO and deduct the consumed quantity automatically. The user must register the initial stock first.
- Vehicles and ponds currently support create/list/get; edit/deactivate workflows are not complete.
- Users can be created by an ADMIN through the API, but there is no user-administration screen in the UI.
- Reporting has event-consumer/persistence foundations but no dedicated reporting dashboard or endpoints.
- HTTP Basic is suitable for this academic MVP only when used over HTTPS outside localhost. It is not a substitute for a production identity provider.
- No final downloadable release archive has been prepared yet.

## Validation status

- The latest full CI workflow for commit `0329a871744c555dd01be0e0c0792cb11ee7bb0a` completed successfully: Gradle tests, Docker image build, JaCoCo report generation, CodeQL analysis and Trivy scan all passed.
- The automated suite includes database-backed tests for MySQL persistence, feeding consumption, stock reservation/restoration, dispatch/order lifecycle, dashboard delivery and authentication.
- The GitHub Docker image build passing does not replace the final local Docker Compose smoke test on the user's Windows computer.

## Release plan

1. Review the API/UI workflows, especially authentication, stock movement, feeding consumption, order cancellation and dispatch transitions.
2. Prepare a clean release archive and exact Windows startup instructions.
3. Have the user run the new copy on port 8080 and confirm it works before considering replacement of the old project.
4. Do not merge this draft PR or delete the old project/database volume until the user has verified the new version.
