## Security

> Follow these security guidelines for every change to the Atlan Java SDK.

### Contact

- **Security Team:** #bu-security-and-it on Slack

### Quickstart for Agents

atlan-java is Atlan's Java SDK (multi-module Gradle, Kotlin DSL). Modules:

- `sdk/` — Core Java SDK client (`AtlanClient`, `HttpClient`); API key auth via `Authorization: Bearer` header; `HttpURLConnectionClient` for HTTP transport; WireMock for unit tests.
- `package-toolkit/runtime/` — Utilities for custom packages including S3 (`S3Utils`), GCS, ADLS connectors, CSV/Excel processing.
- `package-toolkit/config/` — Pkl language support for package configuration.
- `integration-tests/` — E2E tests against live Atlan environment using `ATLAN_BASE_URL` and `ATLAN_API_KEY` env vars.

Review every change for:

- **API key logging** — `AtlanClient` holds the API key and sets it as `Authorization: Bearer <key>` on every HTTP request; the key must never appear in log output, SLF4J messages, or exception `getMessage()` results; when logging `HttpClient` errors, strip the `Authorization` header value from the logged request details; `ATLAN_API_KEY` read from environment for integration tests must similarly not be logged.
- **TLS certificate verification** — `HttpURLConnectionClient` must not disable hostname verification (`HttpsURLConnection.setDefaultHostnameVerifier(...)` with an always-true lambda) or bypass certificate validation via a trust-all `SSLContext`; if custom CA bundles are needed for corporate proxies, accept the CA certificate path, not a bypass flag.
- **Package-toolkit S3/GCS/ADLS credential logging** — `S3Utils` and equivalent GCS/ADLS utilities accept access keys, secret keys, and service account credentials; these must not be logged; use `@ToString(exclude=...)` Lombok annotations or equivalent to exclude credential fields from `toString()`.
- **Pkl config secrets** — `package-toolkit/config/` uses Pkl to define package configuration schemas; if any Pkl config field represents a secret (password, API key, token), mark it as sensitive and ensure it is excluded from serialization outputs used for logging or UI display.
- **Dependency version pinning** — all direct dependencies in `sdk/build.gradle.kts` and `package-toolkit/*/build.gradle.kts` must use explicit version strings; avoid dynamic version ranges (`+`, `latest.release`) which allow supply-chain substitution.

### Security Invariants

- **[MUST]** API key (`ATLAN_API_KEY`, `apiKey`) must never appear in log output or exception messages.
- **[MUST]** TLS certificate verification and hostname verification must not be disabled.
- **[MUST]** S3/GCS/ADLS credential values must not be logged — use `@ToString(exclude=...)`.
- **[MUST]** All direct dependencies must use explicit version pins — no dynamic version ranges.

### Data Classification

- **CONFIDENTIAL:** `ATLAN_API_KEY`, S3 secret access key, GCS service account JSON, ADLS client secret
- **INTERNAL:** `ATLAN_BASE_URL`, workspace IDs, asset GUIDs, object storage bucket names
- **PUBLIC:** SDK version, asset type names, API endpoint names

### Review Checklist

- [ ] `ATLAN_API_KEY` / `apiKey` absent from all log output and exception messages
- [ ] No `trustAllCerts`, `setDefaultHostnameVerifier(always-true)`, or bypass `SSLContext`
- [ ] S3/GCS/ADLS credential fields excluded from `toString()` via Lombok or equivalent
- [ ] Pkl config secret fields marked sensitive and excluded from log serialization
- [ ] All `build.gradle.kts` dependencies use explicit version pins (no `+` or `latest.release`)
