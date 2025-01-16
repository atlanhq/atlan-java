<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright 2022 Atlan Pte. Ltd. -->

[![Build](https://github.com/atlanhq/atlan-java/workflows/Merge/badge.svg)](https://github.com/atlanhq/atlan-java/actions/workflows/merge.yml?query=workflow%3AMerge)
[![JavaDocs](https://img.shields.io/badge/javadocs-passing-success)](https://atlanhq.github.io/atlan-java/)
[![Release](https://img.shields.io/maven-central/v/com.atlan/atlan-java?label=release)](https://s01.oss.sonatype.org/content/repositories/releases/com/atlan/atlan-java/)
[![Development](https://img.shields.io/nexus/s/com.atlan/atlan-java?label=development&server=https%3A%2F%2Fs01.oss.sonatype.org)](https://s01.oss.sonatype.org/content/repositories/snapshots/com/atlan/atlan-java/)
<!--[![CodeQL](https://github.com/atlanhq/atlan-java/workflows/CodeQL/badge.svg)](https://github.com/atlanhq/atlan-java/actions/workflows/codeql-analysis.yml) -->

# Atlan Java

This repository houses the Java-based utilities for interacting with [Atlan](https://atlan.com):

- `sdk` the Atlan Java SDK (client library), that ultimately calls through to Atlan's REST APIs
- `package-toolkit` for configuring, running and testing custom packages
- `integration-tests` for automated regression-testing
- `generate` to generate components of the SDK that are model-driven
- `samples` illustrating how the other components can be used for real automations and integrations
- `mocks` bundle wire mocks that are reused in unit tests across different projects

These additional directories serve other purposes:

- `containers` defines the container image that bundles the SDK and custom package toolkits, and container images for each custom package sample
- `gradle` contains Gradle build bootstrapping and dependency version catalog
- `buildSrc` contains code for the Gradle multi-project build process (in particular, convention plugins)

## [Documentation](https://developer.atlan.com/getting-started/java-sdk/)

For guides on actually using the various utilities housed in this repository, see: [https://developer.atlan.com/getting-started/java-sdk/](https://developer.atlan.com/getting-started/java-sdk/)

## Installing for Development

### Initial Setup
To get started developing the SDK:

1. Clone the repository:
   ```shell
   git clone ..
   ```

2. Ensure you have the Java 17 SDK installed. For example, on macOS:
   ```shell
   brew install openjdk@..
   ```

### Building the SDK
To build the SDK, from within the root of the cloned repository:
```shell
./gradlew assemble shadowJar
```
Note that we use the Gradle build tool. This accelerates subsequent rebuilds through things like caching, and thus only needing to rebuild paths within the project that have changed from one build to another.



### Code Formatting
Before committing code, ensure it adheres to the repository's formatting guidelines. You can apply the required formatting using:

```shell
./gradlew spotlessApply
```

## Testing the SDK
### Environment Setup
For running integration tests, you'll need to configure your environment:

1. Copy the example environment file:
   ```shell
   cp .env.example .env
   ```
2. Update the `.env` file with your Atlan API key and base URL.
3. Export the environment variables:
    - For macOS/Linux:
      ```shell
      export $(cat .env | xargs)
      ```
    - For Windows (PowerShell):
      ```shell
      Get-Content .env | ForEach-Object {
          if ($_ -match '^(.*?)=(.*)$') {
              $env:($matches[1]) = $matches[2]
          }
      }
      ```

### Running Unit Tests
You can run the SDK's unit tests **without needing access to an Atlan environment**:

```shell
./gradlew test
```

### Running Integration Tests
Once the environment is set up, you can run integration tests:

- Base SDK integration tests:
  ```shell
  ./gradlew test -PintegrationTests
  ```
- Package-specific integration tests:
  ```shell
  ./gradlew test -PpackageTests
  ```


## Attribution

Portions of the SDK are based on original work from https://github.com/stripe/stripe-java. Those classes that derive from this original work have an extra heading comment as follows:

```java
/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
```

The original MIT license under which these were used is included here:

```text
The MIT License

Copyright (c) 2011- Stripe, Inc. (https://stripe.com)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright 2022 Atlan Pte. Ltd.
