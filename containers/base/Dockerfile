# SPDX-License-Identifier: Apache-2.0
# Copyright 2023 Atlan Pte. Ltd.
FROM eclipse-temurin:17-jre-ubi9-minimal

ARG TARGETARCH

LABEL org.opencontainers.image.vendor="Atlan Pte. Ltd." \
      org.opencontainers.image.source="https://github.com/atlanhq/atlan-java" \
      org.opencontainers.image.description="Atlan Java SDK bundled together for layering on any additional Java code." \
      org.opencontainers.image.licenses=Apache-2

RUN set -eux; \
      if [ "$TARGETARCH" = "amd64" ]; then DUMB_INIT_ARCH="x86_64"; \
      elif [ "$TARGETARCH" = "arm64" ]; then DUMB_INIT_ARCH="aarch64"; \
      else echo "Unsupported architecture for dumb-init download: $TARGETARCH"; exit 1; fi; \
      wget -O /dumb-init https://github.com/Yelp/dumb-init/releases/download/v1.2.5/dumb-init_1.2.5_${DUMB_INIT_ARCH} \
      && chmod +x /dumb-init

COPY $TARGETARCH/assembly /opt/jars

ENV CLASSPATH="/opt/jars/*:/opt/app/*"

ENTRYPOINT ["/dumb-init", "--", "java"]
