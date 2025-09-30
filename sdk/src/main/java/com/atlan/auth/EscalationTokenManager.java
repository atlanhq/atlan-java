/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.auth;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;

/**
 * Manage tokenless escalation (only possible in-tenant with appropriate config).
 */
public class EscalationTokenManager extends TokenManager {

    public EscalationTokenManager() {
        super(null);
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
        String tmp = client.impersonate.escalate();
        if (tmp != null) {
            this.token = tmp.toCharArray();
            return true;
        }
        return false;
    }
}
