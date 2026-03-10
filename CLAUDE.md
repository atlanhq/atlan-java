# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build the SDK and all modules
./gradlew assemble shadowJar

# Code formatting (required before committing)
./gradlew spotlessApply

# Unit tests (no external dependencies, uses WireMock)
./gradlew test

# Integration tests (requires ATLAN_BASE_URL and ATLAN_API_KEY env vars)
./gradlew test -PintegrationTests

# Package-specific integration tests
./gradlew test -PpackageTests

# Run a single test class
./gradlew :sdk:test --tests "com.atlan.model.assets.ColumnTest"
```

## Project Structure

This is a multi-module Gradle project (Kotlin DSL) for the Atlan Java SDK:

- **`sdk/`** - Core Java SDK client library (~1,300+ asset type models). Published to Maven Central as `com.atlan:atlan-java`
- **`package-toolkit/`** - Framework for building custom Atlan packages
  - `config/` - Pkl configuration language support for package definitions
  - `runtime/` - Runtime utilities (S3, GCS, ADLS connectors, CSV, Excel, etc.)
  - `testing/` - Testing utilities for package development
- **`integration-tests/`** - E2E tests requiring a live Atlan environment
- **`mocks/`** - Shared WireMock definitions used across unit tests
- **`samples/packages/`** - Example custom packages (lineage-builder, asset-import, etc.)
- **`samples/standalone/`** - Standalone SDK extension examples
- **`buildSrc/`** - Gradle convention plugins (`com.atlan.java`, `com.atlan.kotlin`, `com.atlan.kotlin-custom-package`)

## Architecture

### SDK Client Design (based on Stripe Java SDK patterns)
- **`AtlanClient`** - Multi-tenant client handling auth, retries, rate limiting. Primary entry point for API calls
- **`Atlan`** - Static configuration utility
- **`ApiResource`** - Base class for API responses
- Token management via `TokenManager` implementations (API tokens, OAuth, escalation)

### Data Models
- Lombok-generated POJOs using `@SuperBuilder` pattern
- Base class: `AtlanObject` (serializable)
- Asset types inherit from `Asset` interface hierarchy
- Jackson for JSON/YAML serialization with custom `Serde` mapper

### Testing Infrastructure
- **TestNG** for test framework
- **WireMock** (port 8765) auto-started/stopped around tests
- Shared mock mappings in `mocks/src/main/resources/wiremock/`
- Per-module overrides in `src/test/resources/wiremock/`

## Code Style

- **Java**: Palantir Java Format, 4-space indentation
- **Kotlin**: ktlint, 4-space indentation
- **Lombok config**: `lombok.getter.noIsPrefix = true` (getters don't use `is` prefix for booleans)
- **License headers**: SPDX headers required on all source files (auto-applied by Spotless)
- **Error Prone**: Static analysis enabled with some checks disabled for generated code

## Contributing

All commits require DCO sign-off:
```bash
git commit -s -m "Your message"
```

## Environment Setup for Integration Tests

```bash
cp .env.example .env
# Edit .env with your ATLAN_BASE_URL and ATLAN_API_KEY
export $(cat .env | xargs)
```
