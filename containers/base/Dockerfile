# SPDX-License-Identifier: Apache-2.0
# Copyright 2023 Atlan Pte. Ltd.
FROM eclipse-temurin:17-jre

LABEL org.opencontainers.image.vendor="Atlan Pte. Ltd." \
      org.opencontainers.image.source="https://github.com/atlanhq/atlan-java" \
      org.opencontainers.image.description="Atlan Java SDK bundled together for layering on any additional Java code." \
      org.opencontainers.image.licenses=Apache-2

RUN wget -O /dumb-init https://github.com/Yelp/dumb-init/releases/download/v1.2.5/dumb-init_1.2.5_x86_64 \
        && chmod +x /dumb-init

COPY assembly /opt/jars

ENV CLASSPATH "/opt/jars/*:/opt/app/*"
ENV JAVA_OPTS "--add-exports=java.base/jdk.internal.ref=ALL-UNNAMED \
               --add-exports=java.base/sun.nio.ch=ALL-UNNAMED \
               --add-exports=jdk.unsupported/sun.misc=ALL-UNNAMED \
               --add-exports=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED \
               --add-opens=jdk.compiler/com.sun.tools.javac=ALL-UNNAMED \
               --add-opens=java.base/java.lang=ALL-UNNAMED \
               --add-opens=java.base/java.lang.reflect=ALL-UNNAMED \
               --add-opens=java.base/java.io=ALL-UNNAMED \
               --add-opens=java.base/java.util=ALL-UNNAMED"

ENTRYPOINT ["/dumb-init", "--", "java"]
