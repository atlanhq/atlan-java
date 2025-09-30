/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.auth;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Manage authorization against a locally-running setup.
 */
public class LocalTokenManager extends TokenManager {

    public LocalTokenManager(String basicAuth) {
        super(
                basicAuth == null
                        ? null
                        : Base64.getEncoder()
                                .encodeToString(basicAuth.getBytes(StandardCharsets.UTF_8))
                                .toCharArray());
    }

    /** {@inheritDoc} */
    @Override
    protected String getAuthHeader() {
        return token == null ? null : String.format("Basic %s", new String(token));
    }

    /**
     * Refresh the API token.
     *
     * @param client through which to refresh the token
     * @throws AtlanException on any API communication issue
     */
    @Override
    protected boolean refreshToken(AtlanClient client) throws AtlanException {
        throw new InvalidRequestException(ErrorCode.CANNOT_REFRESH_LOCAL_CREDS);
    }
}
