/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.*;
import com.atlan.net.ApiResource;
import com.atlan.net.RequestOptions;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

/**
 * API endpoints for impersonating users as part of Atlan automations (if desired).
 * Note: this will only work when run as part of Atlan's packaged workflow ecosystem
 * (running in the cluster back-end).
 */
public class ImpersonationEndpoint extends AbstractEndpoint {

    private static final String SERVICE = "http://keycloak-http.keycloak.svc.cluster.local";
    private static final String endpoint = "/auth/realms/default/protocol/openid-connect/token";

    public ImpersonationEndpoint(AtlanClient client) {
        super(client);
    }

    protected String getBaseUrl() throws ApiConnectionException {
        if (!client.isInternal()) {
            throw new ApiConnectionException(ErrorCode.INTERNAL_ONLY);
        }
        return SERVICE;
    }

    /**
     * Escalate to a privileged user on a short-term basis.
     * Note: this is only possible from within the Atlan tenant, and only when given the appropriate credentials.
     *
     * @return a short-lived bearer token with escalated privileges
     * @throws AtlanException on any API communication or permission issue
     */
    public String escalate() throws AtlanException {
        return escalate(null);
    }

    /**
     * Escalate to a privileged user on a short-term basis.
     * Note: this is only possible from within the Atlan tenant, and only when given the appropriate credentials.
     *
     * @param options to override default client settings
     * @return a short-lived bearer token with escalated privileges
     * @throws AtlanException on any API communication or permission issue
     */
    public String escalate(RequestOptions options) throws AtlanException {
        String tokenUrl = String.format("%s%s", getBaseUrl(), endpoint);
        String clientId = System.getenv("CLIENT_ID");
        String clientSecret = System.getenv("CLIENT_SECRET");
        if (clientId == null || clientId.isEmpty() || clientSecret == null || clientSecret.isEmpty()) {
            throw new InvalidRequestException(ErrorCode.MISSING_CREDENTIALS);
        }
        Map<String, Object> argoMap = Map.of(
                "grant_type",
                "client_credentials",
                "client_id",
                clientId,
                "client_secret",
                clientSecret,
                "scope",
                "openid");
        try {
            AccessTokenResponse clientToken = ApiResource.request(
                    client, ApiResource.RequestMethod.POST, tokenUrl, argoMap, AccessTokenResponse.class, options);
            return clientToken.getAccessToken();
        } catch (AtlanException e) {
            throw new PermissionException(ErrorCode.UNABLE_TO_ESCALATE, e);
        }
    }

    /**
     * Retrieves a bearer token that impersonates the provided user.
     *
     * @param userId unique identifier of the user to impersonate
     * @return a bearer token that impersonates the provided user
     * @throws AtlanException on any API communication issue
     */
    public String user(String userId) throws AtlanException {
        return user(userId, null);
    }

    /**
     * Retrieves a bearer token that impersonates the provided user.
     *
     * @param userId unique identifier of the user to impersonate
     * @param options to override default client settings
     * @return a bearer token that impersonates the provided user
     * @throws AtlanException on any API communication issue
     */
    public String user(String userId, RequestOptions options) throws AtlanException {
        String tokenUrl = String.format("%s/auth/realms/default/protocol/openid-connect/token", getBaseUrl());
        String clientId = System.getenv("CLIENT_ID");
        String clientSecret = System.getenv("CLIENT_SECRET");
        if (clientId == null || clientId.isEmpty() || clientSecret == null || clientSecret.isEmpty()) {
            throw new InvalidRequestException(ErrorCode.MISSING_CREDENTIALS);
        }
        Map<String, Object> argoMap = Map.of(
                "grant_type", "client_credentials",
                "client_id", clientId,
                "client_secret", clientSecret);
        String argoToken;
        try {
            AccessTokenResponse clientToken = ApiResource.request(
                    client, ApiResource.RequestMethod.POST, tokenUrl, argoMap, AccessTokenResponse.class, options);
            argoToken = clientToken.getAccessToken();
        } catch (AtlanException e) {
            throw new PermissionException(ErrorCode.UNABLE_TO_ESCALATE, e);
        }
        Map<String, Object> userMap = Map.of(
                "grant_type", "urn:ietf:params:oauth:grant-type:token-exchange",
                "client_id", clientId,
                "client_secret", clientSecret,
                "subject_token", argoToken,
                "requested_subject", userId);
        try {
            AccessTokenResponse userToken = ApiResource.request(
                    client, ApiResource.RequestMethod.POST, tokenUrl, userMap, AccessTokenResponse.class, options);
            return userToken.getAccessToken();
        } catch (AtlanException e) {
            throw new PermissionException(ErrorCode.UNABLE_TO_IMPERSONATE, e);
        }
    }

    @Getter
    @Jacksonized
    @Builder(toBuilder = true)
    private static final class AccessTokenResponse extends ApiResource {
        private static final long serialVersionUID = 2L;

        /** Bearer token that can be used to impersonate a user. */
        @JsonProperty("access_token")
        String accessToken;

        /** When the bearer token will expire (in seconds). */
        @JsonProperty("expires_in")
        Long expiresIn;

        /** When the ability to refresh the token will expire (in seconds). */
        @JsonProperty("refresh_expires_in")
        Long refreshExpiresIn;

        /** Token that can be used to refresh the bearer token. */
        @JsonProperty("refresh_token")
        String refreshToken;

        /** Type of token (should be "Bearer"). */
        @JsonProperty("token_type")
        String tokenType;

        /** TBC */
        @JsonProperty("not-before-policy")
        Long notBeforePolicy;

        /** TBC */
        @JsonProperty("session_state")
        String sessionState;

        /** TBC */
        String scope;
    }
}
