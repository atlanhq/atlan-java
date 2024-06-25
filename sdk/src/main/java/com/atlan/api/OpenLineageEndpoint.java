/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.AuthenticationException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.lineage.OpenLineageEvent;
import com.atlan.net.ApiResource;
import com.atlan.net.RequestOptions;

/**
 * API endpoints for interacting with OpenLineage.
 */
public class OpenLineageEndpoint extends ChronosEndpoint {

    private static final String endpoint = "api/v1/lineage";

    public OpenLineageEndpoint(AtlanClient client) {
        super(client);
    }

    /**
     * Sends the OpenLineage event to Atlan to be consumed.
     *
     * @param request OpenLineage event to send
     * @param connectorType of the connection that should receive the OpenLineage event
     * @throws AtlanException on any issues with API communication
     */
    public void send(OpenLineageEvent request, AtlanConnectorType connectorType) throws AtlanException {
        send(request, connectorType, null);
    }

    /**
     * Sends the OpenLineage event to Atlan to be consumed.
     *
     * @param request OpenLineage event to send
     * @param connectorType of the connection that should receive the OpenLineage event
     * @param options to override default client settings
     * @throws AtlanException on any issues with API communication
     */
    public void send(OpenLineageEvent request, AtlanConnectorType connectorType, RequestOptions options)
            throws AtlanException {
        String url = String.format("%s/%s/%s", getBaseUrl(), connectorType.getValue(), endpoint);
        try {
            ApiResource.requestPlainText(client, ApiResource.RequestMethod.POST, url, request, options);
        } catch (AuthenticationException e) {
            if (e.getAtlanError() != null
                    && e.getAtlanError().getErrorMessage() != null
                    && e.getAtlanError()
                            .getErrorMessage()
                            .startsWith("Unauthorized: url path not configured to receive data, urlPath:")) {
                throw new InvalidRequestException(ErrorCode.OPENLINEAGE_NOT_CONFIGURED, e, connectorType.getValue());
            } else {
                throw e;
            }
        }
    }
}
