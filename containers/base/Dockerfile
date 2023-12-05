# SPDX-License-Identifier: Apache-2.0
# Copyright 2023 Atlan Pte. Ltd.
FROM eclipse-temurin:17 as jre-build

RUN $JAVA_HOME/bin/jlink \
		--add-modules java.base,java.compiler,java.desktop,java.instrument,java.logging,java.management,java.naming,java.net.http,java.rmi,java.scripting,java.xml,java.xml.crypto,java.security.jgss,java.security.sasl,java.sql,jdk.httpserver,jdk.compiler,jdk.jfr,jdk.unsupported \
		--strip-debug \
		--no-man-pages \
		--no-header-files \
		--compress=2 \
		--output /javaruntime

RUN wget -O /dumb-init https://github.com/Yelp/dumb-init/releases/download/v1.2.5/dumb-init_1.2.5_x86_64 \
        && chmod +x /dumb-init

COPY assembly /opt/jars

FROM debian:buster-slim

LABEL org.opencontainers.image.vendor="Atlan Pte. Ltd." \
      org.opencontainers.image.source="https://github.com/atlanhq/atlan-java" \
      org.opencontainers.image.description="Atlan Java SDK bundled together for layering on any additional Java code." \
      org.opencontainers.image.licenses=Apache-2

RUN apt-get update && apt-get install -y libfreetype6 && rm -rf /var/lib/apt/lists/*

ENV JAVA_HOME=/opt/java/openjdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"
ENV CLASSPATH "/opt/jars/*:/opt/app/*"
COPY --from=jre-build /javaruntime $JAVA_HOME
COPY --from=jre-build /opt/jars /opt/jars
COPY --from=jre-build /dumb-init /dumb-init

ENTRYPOINT ["/dumb-init", "--", "java"]
