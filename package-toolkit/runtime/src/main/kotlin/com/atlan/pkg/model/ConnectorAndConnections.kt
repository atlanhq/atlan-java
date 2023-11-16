/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.model

import com.atlan.model.enums.AtlanConnectorType
import com.fasterxml.jackson.annotation.JsonAutoDetect

/**
 * Captures the selected connector type and all connections that exist in a tenant
 * with that type, as returned by the ConnectorTypeSelector widget.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class ConnectorAndConnections(
    val source: AtlanConnectorType,
    val connections: List<String>,
)
