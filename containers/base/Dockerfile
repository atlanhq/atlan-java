# SPDX-License-Identifier: Apache-2.0
# Copyright 2023 Atlan Pte. Ltd.
FROM eclipse-temurin:21-jre-ubi9-minimal

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

# Block the eMudhra intermediates (CVE-2026-91403) until the base image ships a fixed Temurin 17.
# security/blocked.certs is generated from openjdk/jdk@22bc7357ce ("8395142: CA Enhancements") with
# that commit's BlockedCertsConverter. Merged with the JDK's own file so its entries are kept.
COPY security/blocked.certs /tmp/blocked.certs
RUN set -e; f="$JAVA_HOME/lib/security/blocked.certs"; test -f "$f"; \
      head -1 "$f" | grep -qx 'Algorithm=SHA-256'; \
      { echo "Algorithm=SHA-256"; grep -hvx 'Algorithm=SHA-256' "$f" /tmp/blocked.certs | sort -u; } > /tmp/merged; \
      mv /tmp/merged "$f"; chmod 644 "$f"; rm /tmp/blocked.certs

COPY $TARGETARCH/assembly /opt/jars

ENV CLASSPATH="/opt/jars/*:/opt/app/*"

ENTRYPOINT ["/dumb-init", "--", "java"]
