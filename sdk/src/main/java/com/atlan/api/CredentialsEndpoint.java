/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.Credential;
import com.atlan.model.admin.CredentialResponse;
import com.atlan.model.admin.CredentialTestResponse;
import com.atlan.model.workflow.*;
import com.atlan.net.ApiResource;
import com.atlan.net.RequestOptions;

/**
 * API endpoints for operating on Atlan's workflows.
 */
public class CredentialsEndpoint extends HeraclesEndpoint {

    private static final String endpoint = "/credentials";
    private static final String test_endpoint = endpoint + "/test";

    public CredentialsEndpoint(AtlanClient client) {
        super(client);
    }

    /**
     * Retrieve a credential. Note that this will never contain sensitive information in
     * the credential, such as usernames, passwords or client secrets or keys.
     *
     * @param guid unique identifier (GUID) of the credential
     * @return non-sensitive information about the credential
     * @throws AtlanException on any API communication issue
     */
    public CredentialResponse get(String guid) throws AtlanException {
        return get(guid, null);
    }

    /**
     * Retrieve a credential. Note that this will never contain sensitive information in
     * the credential, such as usernames, passwords or client secrets or keys.
     *
     * @param guid unique identifier (GUID) of the credential
     * @param options to override default client settings
     * @return non-sensitive information about the credential
     * @throws AtlanException on any API communication issue
     */
    public CredentialResponse get(String guid, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s/%s", getBaseUrl(), endpoint, guid);
        return ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", CredentialResponse.class, options);
    }

    /**
     * Test the provided credential, to confirm it works.
     *
     * @param credential details of the credential to test
     * @return the test results
     * @throws AtlanException on any API communication issue
     */
    public CredentialTestResponse test(Credential credential) throws AtlanException {
        return test(credential, null);
    }

    /**
     * Test the provided credential, to confirm it works.
     *
     * @param credential details of the credential to test
     * @param options to override default client settings
     * @return the test results
     * @throws AtlanException on any API communication issue
     */
    public CredentialTestResponse test(Credential credential, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), test_endpoint);
        return ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, credential, CredentialTestResponse.class, options);
    }

    /**
     * Update the provided credential in Atlan. Note that this will NOT test the
     * credential first, so ensure it is valid before calling this method.
     *
     * @param guid unique identifier of the credential to update
     * @param credential details of the credential to include in the update
     * @return details of the updated credential
     * @throws AtlanException on any API communication issue
     */
    public CredentialResponse update(String guid, Credential credential) throws AtlanException {
        return update(guid, credential, null);
    }

    /**
     * Update the provided credential in Atlan. Note that this will NOT test the
     * credential first, so ensure it is valid before calling this method.
     *
     * @param guid unique identifier of the credential to update
     * @param credential details of the credential to include in the update
     * @param options to override default client settings
     * @return details of the updated credential
     * @throws AtlanException on any API communication issue
     */
    public CredentialResponse update(String guid, Credential credential, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s/%s", getBaseUrl(), endpoint, guid);
        return ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, credential, CredentialResponse.class, options);
    }
}
