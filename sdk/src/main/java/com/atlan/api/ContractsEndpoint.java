/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Asset;
import com.atlan.model.core.AtlanObject;
import com.atlan.net.ApiResource;
import com.atlan.net.RequestOptions;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * API endpoints for data contract-specific operations.
 */
public class ContractsEndpoint extends HeraclesEndpoint {

    private static final String endpoint = "/contracts";

    private static final String endpoint_init = endpoint + "/init";

    public ContractsEndpoint(AtlanClient client) {
        super(client);
    }

    /**
     * Generate an initial contract spec for the provided asset.
     * The asset must have at least its qualifiedName (and type) populated.
     *
     * @param asset for which to generate the initial contract spec
     * @return the YAML for the initial contract spec for the provided asset
     * @throws AtlanException on any issue interacting with the API
     */
    public String generateInitialSpec(Asset asset) throws AtlanException {
        return generateInitialSpec(asset, null);
    }

    /**
     * Generate an initial contract spec for the provided asset.
     * The asset must have at least its qualifiedName (and type) populated.
     *
     * @param asset for which to generate the initial contract spec
     * @param options to override default client settings
     * @return the YAML for the initial contract spec for the provided asset
     * @throws AtlanException on any issue interacting with the API
     */
    public String generateInitialSpec(Asset asset, RequestOptions options) throws AtlanException {
        InitRequest request = new InitRequest(asset);
        String url = String.format("%s%s", getBaseUrl(), endpoint_init);
        InitResponse response =
                ApiResource.request(client, ApiResource.RequestMethod.POST, url, request, InitResponse.class, options);
        return response.getContract();
    }

    @Getter
    @EqualsAndHashCode(callSuper = false)
    private static final class InitRequest extends AtlanObject {
        private static final long serialVersionUID = 2L;
        String assetType;
        String assetQualifiedName;

        public InitRequest(String assetType, String assetQualifiedName) {
            this.assetType = assetType;
            this.assetQualifiedName = assetQualifiedName;
        }

        public InitRequest(Asset asset) {
            this(asset.getTypeName(), asset.getQualifiedName());
        }
    }

    @Getter
    @EqualsAndHashCode(callSuper = false)
    private static final class InitResponse extends ApiResource {
        private static final long serialVersionUID = 2L;
        String contract;
    }
}
