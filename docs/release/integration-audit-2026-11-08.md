# RedFish integration audit — 2026-11-08

## Scope and safety

- Working branch: `integration/redfish-final-2026-11-08`, created from `Develop`.
- Protected branches `main`, `Develop`, and `Qa` have not been modified directly.
- Preserve the existing project and MySQL volume until the integrated version has passed local Docker Compose smoke tests.
- Never use `docker compose down -v` during validation; it deletes the database volume.

## Implemented in this integration branch

### API and persistence

- Products and customers: REST create/list/get, update and logical deactivation with JPA persistence.
- Vehicles and ponds: REST create/list/get, update and logical deactivation with JPA persistence.
- Production: ponds and feeding-record endpoints with MySQL JPA persistence. Feeding records reference an active inventory input product; consumption and the stock-exit movement are recorded transactionally.
- Inventory: initial stock registration, current-stock listing/get, stock entries/exits and movement history with MySQL persistence.
- Orders: create/list/get, line items and totals, lifecycle transitions, JPA persistence and transactional stock reservation. Cancelling a pending or in-preparation order restores its reserved quantities.
- Dispatches: schedule/list/get and lifecycle endpoints. Scheduling validates the order state and active vehicle; transit synchronizes the order state.
- Security: users stored in MySQL, BCrypt password hashes, HTTP Basic authentication, role-based ADMIN, PRODUCTION, OPERATIONS and COMMERCIAL accounts, plus legacy OPERATOR compatibility. Bootstrap accounts for the four areas are configured independently through environment variables.
- Browser UI: responsive static application served by Spring Boot at `/`. The home page presents four separate area entry points (Producción, Operaciones, Comercial and Administración); each requires username/password authentication and validates that the authenticated account has the selected role. The dashboard menu is filtered by role. Forms and tables cover products, customers, vehicles, ponds, feeding, inventory, orders and dispatches. Catalog editing and logical deactivation are exposed for products, customers, vehicles and ponds.
- OpenAPI YAML documents the operational modules and HTTP Basic security.

### Build and deployment

- Java 17 / Gradle / Spring Boot.
- Dockerfile runs the application as a non-root user and installs curl for the health check.
- GitHub Actions CI includes Gradle tests, JaCoCo report generation, Docker image build, CodeQL analysis and Trivy filesystem scanning.
- Compose environment examples include separate bootstrap credentials for the four areas. Replace every example password with a unique secret before use; do not publish real credentials.

## Known scope limitations

- There is no user-administration screen yet; the four area accounts are provisioned on first startup from the corresponding bootstrap environment variables. ADMIN user creation is available through the API.
- Feeding records and inventory movements are traceability records. They should not be edited or physically deleted without a compensating stock transaction and audit trail; that workflow is not implemented in the UI.
- Orders and dispatches expose lifecycle transitions rather than unrestricted edit/delete. Their state transitions must preserve stock and delivery history.
- There is no dedicated reporting dashboard/endpoints yet.
- HTTP Basic is suitable for this academic MVP only over HTTPS outside localhost; it is not a production identity provider.
- GitHub CI passing does not replace the final local Docker Compose smoke test on Windows.

## Validation status

- The inventory reservation aggregation fix in commit `0f4ddb18ba94c424f592b231388d0708941cc9f3` passed Contract tests and full RedFish CI.
- The four-area landing page, bootstrap account provisioning, role-specific API authorization and new role tests are now committed on this branch. Their latest GitHub Actions runs are pending/in progress and must pass before release.
- Local Docker Compose end-to-end validation on Windows is still required; no local-machine test is claimed.

## Release gate

1. Continue reviewing API/UI request and response compatibility and operational state transitions.
2. Run the latest image with Docker Compose and perform smoke tests for login, CRUD, stock, feeding, order cancellation and dispatch delivery.
3. Prepare a release archive and exact Windows startup instructions only after the local smoke test.
4. Do not merge this draft PR or delete the old project/database volume until the new version has been locally verified.
