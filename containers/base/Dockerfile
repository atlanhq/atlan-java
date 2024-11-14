# SPDX-License-Identifier: Apache-2.0
# Copyright 2023 Atlan Pte. Ltd.
FROM eclipse-temurin:17-jre-ubi9-minimal

LABEL org.opencontainers.image.vendor="Atlan Pte. Ltd." \
      org.opencontainers.image.source="https://github.com/atlanhq/atlan-java" \
      org.opencontainers.image.description="Atlan Java SDK bundled together for layering on any additional Java code." \
      org.opencontainers.image.licenses=Apache-2

RUN wget -O /dumb-init https://github.com/Yelp/dumb-init/releases/download/v1.2.5/dumb-init_1.2.5_x86_64 \
        && chmod +x /dumb-init

COPY assembly /opt/jars

ENV CLASSPATH="/opt/jars/*:/opt/app/*"

ENTRYPOINT ["/dumb-init", "--", "java"]
