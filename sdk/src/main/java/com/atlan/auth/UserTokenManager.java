/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.auth;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;

/**
 * Manage user-allocated bearer tokens.
 */
public class UserTokenManager extends TokenManager {

    public static final String ENVIRONMENT_VARIABLE = "ATLAN_USER_ID";

    private final String userId;

    public UserTokenManager(String userId) {
        super(null);
        this.userId = userId;
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
        String tmp = client.impersonate.user(userId);
        if (tmp != null) {
            this.token = tmp.toCharArray();
            return true;
        }
        return false;
    }
}
