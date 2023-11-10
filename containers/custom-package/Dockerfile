# SPDX-License-Identifier: Apache-2.0
# Copyright 2023 Atlan Pte. Ltd.
ARG VERSION
ARG PKG_NAME

FROM ghcr.io/atlanhq/atlan-java:$VERSION

LABEL org.opencontainers.image.vendor="Atlan Pte. Ltd." \
      org.opencontainers.image.source="https://github.com/atlanhq/atlan-java" \
      org.opencontainers.image.description="Atlan image for $PKG_NAME custom package." \
      org.opencontainers.image.licenses=Apache-2

COPY assembly /opt/jars
