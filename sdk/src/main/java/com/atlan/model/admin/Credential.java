/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.core.AtlanObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings({"cast", "serial"})
public class Credential extends AtlanObject {

    private static final long serialVersionUID = 2L;

    /** Unique identifier (GUID) of the credential. */
    String id;

    /** Name of the credential. */
    String name;

    /** Hostname the for which connectivity is defined by the credential. */
    String host;

    /** Port number on which connectivity should be done. */
    Integer port;

    /** Authentication mechanism represented by the credential. */
    String authType;

    /** Type of connector used by the credential. */
    String connectorType;

    /**
     * Less sensitive portion of the credential, typically used for a username for basic
     * authentication or client IDs for other forms of authentication.
     */
    String username;

    /**
     * More sensitive portion of the credential, typically used for a password for basic
     * authentication or client secrets for other forms of authentication.
     */
    String password;

    /**
     * Additional details about the credential. This can capture, for example, a secondary
     * secret for particular forms of authentication and / or additional details about the
     * scope of the connectivity (a specific database, role, warehouse, etc).
     */
    @JsonInclude(JsonInclude.Include.ALWAYS)
    @JsonProperty("extra")
    @Singular
    Map<String, String> extras;

    /** Name of the connector configuration responsible for managing the credential. */
    String connectorConfigName;

    /**
     * Retrieves a credential by its unique identifier (GUID).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the credential
     * @param guid unique identifier (GUID) of the credential to retrieve
     * @return the credential, or null if none exists
     * @throws AtlanException on any error during API invocation
     */
    public static CredentialResponse get(AtlanClient client, String guid) throws AtlanException {
        return client.credentials.get(guid);
    }

    /**
     * Sends this credential to Atlan to update it in Atlan, after first testing it
     * to confirm it works.
     *
     * @param client connectivity to the Atlan tenant on which to update the credential
     * @return the updated credential
     * @throws InvalidRequestException if the provided credentials cannot be validated successfully
     * @throws AtlanException on any error during API invocation
     */
    public CredentialResponse update(AtlanClient client) throws AtlanException {
        if (this.id == null || this.id.isEmpty()) {
            throw new InvalidRequestException(ErrorCode.MISSING_TOKEN_ID);
        }
        CredentialTestResponse test = client.credentials.test(this);
        if (test.isSuccessful()) {
            return client.credentials.update(this.id, this);
        } else {
            throw new InvalidRequestException(ErrorCode.INVALID_CREDENTIALS, test.getMessage());
        }
    }

    /**
     * Convert this credential into a map, for embedding within a package (crawler).
     *
     * @return map representation of the credential
     */
    public Map<String, Object> toMap() {
        Map<String, Object> credential = new HashMap<>();
        if (name != null) {
            credential.put("name", name);
        }
        if (host != null) {
            credential.put("host", host);
        }
        if (port != null) {
            credential.put("port", port);
        }
        if (authType != null) {
            credential.put("authType", authType);
        }
        if (connectorType != null) {
            credential.put("connectorType", connectorType);
        }
        if (username != null) {
            credential.put("username", username);
        }
        if (password != null) {
            credential.put("password", password);
        }
        if (extras != null) {
            credential.put("extra", extras);
        }
        if (connectorConfigName != null) {
            credential.put("connectorConfigName", connectorConfigName);
        }
        return credential;
    }
}
