/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.auth;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.OAuthExchangeResponse;

/**
 * Manage OAuth client authorization.
 */
public class OAuthClientManager extends TokenManager {

    public static final String ENV_CLIENT_ID = "ATLAN_OAUTH_CLIENT_ID";
    public static final String ENV_SECRET = "ATLAN_OAUTH_CLIENT_SECRET";

    private final String clientId;
    private final char[] clientSecret;

    public OAuthClientManager(final String clientId, final char[] clientSecret) {
        super(null);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    /** {@inheritDoc} */
    @Override
    protected String getAuthHeader() {
        return token == null ? null : String.format("Bearer %s", new String(token));
    }

    /**
     * Refresh the API token.
     *
     * @param client through which to refresh the token
     * @throws AtlanException on any API communication issue
     */
    @Override
    protected boolean refreshToken(AtlanClient client) throws AtlanException {
        OAuthExchangeResponse response = client.oauthClients.exchange(clientId, new String(clientSecret));
        String accessToken = response.getAccessToken();
        if (accessToken != null && !accessToken.isEmpty()) {
            this.token = response.getAccessToken().toCharArray();
            return true;
        }
        return false;
    }
}
