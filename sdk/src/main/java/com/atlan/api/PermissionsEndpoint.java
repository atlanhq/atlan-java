/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.ApiToken;
import com.atlan.model.admin.ApiTokenResponse;
import com.atlan.model.admin.PermissionsResponse;
import com.atlan.net.ApiResource;
import com.atlan.net.RequestOptions;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

/**
 * API endpoints for retrieving evaluated permissions.
 */
public class PermissionsEndpoint extends HeraclesEndpoint {

    private static final String endpoint = "/whoami";

    public PermissionsEndpoint(AtlanClient client) {
        super(client);
    }

    /**
     * Retrieves the current authenticated user's (or client's) evaluated permissions.
     *
     * @return evaluated permissions of the current context
     * @throws AtlanException on any API communication issue
     */
    public PermissionsResponse get() throws AtlanException {
        return get(null);
    }

    /**
     * Retrieves the current authenticated user's (or client's) evaluated permissions.
     *
     * @param options to override default client settings
     * @return evaluated permissions of the current context
     * @throws AtlanException on any API communication issue
     */
    public PermissionsResponse get(RequestOptions options) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), endpoint);
        return ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", PermissionsResponse.class, options);
    }
}
