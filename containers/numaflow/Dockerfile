# SPDX-License-Identifier: Apache-2.0
# Copyright $YEAR Atlan Pte. Ltd.
FROM eclipse-temurin:17 as jre-build

RUN $JAVA_HOME/bin/jlink \
		--add-modules java.base,java.logging,jdk.unsupported \
		--strip-debug \
		--no-man-pages \
		--no-header-files \
		--compress=2 \
		--output /javaruntime

COPY assembly /opt/jars

FROM debian:buster-slim

LABEL org.opencontainers.image.vendor="Atlan Pte. Ltd." \
      org.opencontainers.image.source="https://github.com/atlanhq/atlan-java" \
      org.opencontainers.image.description="Atlan Java SDK and Numaflow bundled together for layering on Java UDFs." \
      org.opencontainers.image.licenses=Apache-2

ENV JAVA_HOME=/opt/java/openjdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"
ENV CLASSPATH "/opt/jars/*:/opt/app/*"
COPY --from=jre-build /javaruntime $JAVA_HOME
COPY --from=jre-build /opt/jars /opt/jars

ENTRYPOINT ["java"]
