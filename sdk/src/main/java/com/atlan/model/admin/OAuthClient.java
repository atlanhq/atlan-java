/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.core.AtlanObject;
import java.util.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class OAuthClient extends AtlanObject {

    private static final long serialVersionUID = 2L;
    public static final String OAUTH_CLIENT_PREFIX = "oauth-client-";

    /** Unique identifier (GUID) of the OAuth credential. */
    String id;

    /** Unique client identifier (prefixed GUID) of the OAuth client. */
    String clientId;

    /** Unique secret used to authorize exchanging for bearer tokens. */
    String clientSecret;

    /** Human-readable name provided when creating the client. */
    String displayName;

    /** Epoch time, in milliseconds, at which the OAuth client was created. */
    String createdAt;

    /** User who created the OAuth client. */
    String createdBy;

    /** Explanation of the OAuth client. */
    String description;

    /** Unique names (qualifiedNames) of personas associated with the OAuth client. */
    @Singular
    List<String> personaQNs;

    /** Name of the workspace role to which the OAuth client is bound. */
    String role;

    /** Lifespan of each exchanged token for the OAuth client. */
    Long tokenExpirySeconds;

    /** Epoch time, in milliseconds, at which the OAuth client was last updated. */
    String updatedAt;

    /** User who last updated the OAuth client. */
    String updatedBy;

    /**
     * Create a new OAuth client that is not assigned to any personas.
     *
     * @param client connectivity to the Atlan tenant on which to create the new OAuth client
     * @param displayName readable name for the OAuth client
     * @param role workspace role the client should be bound to
     * @return the OAuth client details
     * @throws AtlanException on any API communication issues
     */
    public static OAuthClient create(AtlanClient client, String displayName, String role) throws AtlanException {
        return create(client, displayName, null, null, role);
    }

    /**
     * Create a new OAuth client with the provided details.
     *
     * @param client connectivity to the Atlan tenant on which to create the new OAuth client
     * @param displayName readable name for the OAuth client
     * @param description explanation of the OAuth client
     * @param personas unique names (qualifiedNames) of personas that should be linked to the client
     * @param role workspace role the client should be bound to
     * @return the OAuth client details
     * @throws AtlanException on any API communication issues
     */
    public static OAuthClient create(
            AtlanClient client, String displayName, String description, Set<String> personas, String role)
            throws AtlanException {
        return client.oauthClients.create(displayName, description, personas, role);
    }

    /**
     * Retrieves the OAuth client with a name that exactly matches the provided string.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the OAuth client
     * @param displayName name (as it appears in the UI) by which to retrieve the OAuth client
     * @return the OAuth client whose name (in the UI) contains the provided string, or null if there is none
     * @throws AtlanException on any error during API invocation
     */
    public static OAuthClient retrieveByName(AtlanClient client, String displayName) throws AtlanException {
        return client.oauthClients.get(displayName);
    }

    /**
     * Sends this OAuth client to Atlan to update it in Atlan.
     * Note: only the displayName and description can currently be updated (for anything else,
     * you need to create a new OAuth client).
     *
     * @param client connectivity to the Atlan tenant on which to update the OAuth client
     * @return the updated OAuth client
     * @throws AtlanException on any error during API invocation
     */
    public OAuthClient update(AtlanClient client) throws AtlanException {
        if (this.clientId == null || this.clientId.isEmpty()) {
            throw new InvalidRequestException(ErrorCode.MISSING_CLIENT_ID);
        }
        if (this.displayName == null || this.displayName.isEmpty()) {
            throw new InvalidRequestException(ErrorCode.MISSING_CLIENT_NAME);
        }
        return client.oauthClients.update(this.clientId, this.displayName, this.description);
    }

    /**
     * Delete (purge) the OAuth client with the provided clientId.
     *
     * @param client connectivity to the Atlan tenant from which to remove the OAuth client
     * @param clientId unique identifier of the OAuth client to hard-delete
     * @throws AtlanException on any API communication issues
     */
    public static void delete(AtlanClient client, String clientId) throws AtlanException {
        client.oauthClients.purge(clientId);
    }
}
