/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.auth;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;

/**
 * Manage API tokens.
 */
public class APITokenManager extends TokenManager {

    public static final String ENVIRONMENT_VARIABLE = "ATLAN_API_KEY";

    public APITokenManager(char[] token) {
        super(token);
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
        throw new InvalidRequestException(ErrorCode.CANNOT_REFRESH_API_TOKEN);
    }
}
