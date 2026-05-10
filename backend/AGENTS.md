# AGENTS — How to be productive in this repository

This file collects the minimal, actionable knowledge an AI coding agent needs to work effectively with the backend service.

Summary (big picture)
- Language / framework: Java 21, Spring Boot 3.2.x, Spring Web, Spring Data JPA, Spring Security.
- Persistence: MySQL in development (configured in `src/main/resources/application.yaml`); H2 is used for tests.
- Auth: JWT (jjwt library). There is a JwtService and a JwtAuthenticationFilter; security is configured stateless and `/api/auth/**` is allowed without authentication.

Where to look first (key files)
- `pom.xml` — Java version, dependencies and the maven-compiler-plugin configuration (annotation processors). Important to run builds.
- `src/main/resources/application.yaml` — local MySQL URL, credentials, server port (8080) and hibernate ddl-auto=update.
- `src/main/java/.../config/SecurityConfig.java` — SecurityFilterChain, PasswordEncoder bean (BCrypt) and permitted routes.
- `src/main/java/.../config/CorsConfig.java` — explicit allowed origins (localhost:3000, 3010, 5173) and headers/methods.
- `src/main/java/.../service/JwtService.java` — JWT generation/validation, SECRET_KEY constant and expiration.
- `src/main/java/.../config/JwtAuthenticationFilter.java` — filter that extracts the Bearer token and authenticates the SecurityContext.
- `src/main/java/.../controller/AuthController.java` — registration endpoint that hashes passwords. Note login currently returns a placeholder string.
- `src/main/java/.../controller/TransactionController.java` and `service/TransactionService.java` — main domain flows (pagination, filtering, dispute action).
- `src/main/java/.../repository/TransactionRepository.java` — JPA query methods used by the service.
- `src/main/java/.../mapper/TransactionMapper.java` — mapping DTO + masked-card logic (keeps last 4 digits only).

Important architectural and project-specific patterns
- Stateless JWT auth: Security config sets SessionCreationPolicy.STATELESS and permits `/api/auth/**`. Expect callers to provide `Authorization: Bearer <token>` for other endpoints. Example: controller `TransactionController` expects authenticated access.
- JWT implementation details: `JwtService` uses a hard-coded SECRET_KEY string in-source. This is a discoverable security risk — change to environment variable before production.
- CORS is explicitly configured via `CorsConfig` and some controllers also declare `@CrossOrigin`. The project expects frontends on localhost ports (3000/3010/5173/3009).
- Money representation: `Transaction.amount` uses BigDecimal (precision/scale in the column) — the code intentionally avoids using double for money.
- Mapping: DTOs are simple POJOs; mapping logic lives in `TransactionMapper` (for masking card number and assembling DTOs). Follow this single-responsibility convention when adding DTO transformations.
- Repository pagination: service uses Spring Data `Pageable`/`PageRequest` and repository methods named `findByUserId` / `findByUserIdAndDisputed`.

Developer workflows (commands)
- Build (recommended wrapper):

  ./mvnw clean package

- Run (development):

  ./mvnw spring-boot:run

  - If you need to run the produced jar:

  java -jar target/backend-1.0.0.jar

- Run tests (uses H2 in-memory DB):

  ./mvnw test

Notes:
- Java 21 is required (see `pom.xml` property `<java.version>`). Ensure your local JDK matches.
- The maven-compiler-plugin may include annotation processors when needed. Do not add processors unless you also add a matching dependency.

Integration points and runtime dependencies
- MySQL: configured in `application.yaml` (jdbc:mysql://localhost:3306/transactions). Local dev DB must exist or change `spring.datasource.url` or use an alternative profile.
- JWT: jjwt (io.jsonwebtoken:jjwt). Tokens are signed with the in-source SECRET_KEY.
- Frontend clients: CORS lists `http://localhost:3000`, `http://localhost:3010`, `http://localhost:5173` and several controllers include `@CrossOrigin` for particular ports (e.g. `AuthController` uses 3009). Expect the frontend to call `/api/*` endpoints.

API surface, examples and gotchas
- Registration: POST /api/auth/register
  - Request body: `RegisterRequest` DTO (see `dto/RegisterRequest.java`). Passwords are BCrypt-hashed via the injected `PasswordEncoder`.

- Login: POST /api/auth/login
  - Currently returns a placeholder string (`"Login successful"`). There is NO code that returns a JWT token in the current controller — an agent adding JWT-based login should call `JwtService.generateToken(UserDetails)` and return the token.

- Get transactions: GET /api/transactions?userId=<id>&page=0&size=10&disputed=true|false
  - `userId` is optional now; if omitted, the endpoint returns all transactions so the frontend can render without JWT-derived user context.
  - Response: Page<TransactionDTO>. Masked card number is provided in `maskedCardNumber` (TransactionMapper masks last 4 digits).

- Dispute transaction: PUT /api/transactions/{id}/dispute
  - Sets `disputed` true and `status` to "DISPUTED". Service throws RuntimeException for not found / already disputed cases.

Common debugging hints
- Lombok/build errors: If you see Lombok-related compile errors, ensure annotation processing is enabled in the IDE and the plugin config is present. This project does not require Lombok by default.
- Database problems: If the application fails to start with a JDBC connection error, either bring up a local MySQL instance with credentials from `application.yaml` or temporarily change `spring.datasource` to an in-memory DB profile.
- JWT/Authentication: The `JwtAuthenticationFilter` class exists and validates tokens, but the current `SecurityConfig` does not explicitly add it to the filter chain. If authentication is not happening as expected, register the filter with `http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)` inside `SecurityConfig`.

If you modify code, run these quick checks
- ./mvnw -q -DskipTests=true package  (fast compilation)
- ./mvnw test  (unit/integration tests; tests rely on H2 DB)

Files worth editing when adding features
- `SecurityConfig.java` — add custom filters or change permitted routes.
- `JwtService.java` — central place for token format and secret (move secret to env var).
- `TransactionService.java` / `TransactionRepository.java` — add business logic and queries.
- `TransactionMapper.java` — place DTO transformation logic here (card masking is an example).

Limitations (what agents should be careful about)
- Secrets in code: `JwtService.SECRET_KEY` is hard-coded. Do not commit production secrets; use environment variables or Spring configuration.
- Login does not produce JWTs yet. Any frontend expecting a token must be updated or the controller extended.
- `transaction` and `user` are used as table names — these are reserved words in some DBs; be cautious in migration scripts or raw SQL.

If you need more context
- Read `pom.xml` and `application.yaml` first. Then jump to `SecurityConfig`, `JwtService`, `JwtAuthenticationFilter`, `AuthController`, and `TransactionService`.

----
Generated by an automated analysis. Use this as the starting point for change tasks: add login->JWT, improve error types, and externalize secrets.


