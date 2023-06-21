/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.events;

import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.ICatalog;
import com.atlan.model.assets.ILineageProcess;
import com.atlan.model.enums.KeywordFields;
import com.atlan.model.events.AtlanEvent;
import com.atlan.model.events.AtlanEventPayload;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.serde.Serde;
import com.atlan.util.QueryFactory;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public interface AtlanEventHandler {

    String WEBHOOK_VALIDATION_REQUEST =
            "{\"atlan-webhook\": \"Hello, humans of data! It worked. Excited to see what you build!\"}";

    static boolean isValidationRequest(String data) {
        return WEBHOOK_VALIDATION_REQUEST.equals(data);
    }

    /**
     * Validate the signing secret provided with a request matches the expected signing secret.
     *
     * @param expectedSignature signature that must be found for a valid request
     * @param headers that were sent with the request
     * @return true if and only if the headers contain a signing secret that matches the expected signature
     */
    static boolean validSignature(String expectedSignature, Map<String, String> headers) {
        if (headers == null) {
            return false;
        } else {
            String found = headers.getOrDefault("x-atlan-signing-secret", null);
            return found != null && found.equals(expectedSignature);
        }
    }

    /**
     * Translate the JSON payload into an Atlan event object.
     *
     * @param data the JSON payload
     * @return an Atlan event object representation of the payload
     * @throws IOException on any problems deserializing the event details
     */
    static AtlanEvent getAtlanEvent(String data) throws IOException {
        return Serde.mapper.readValue(data, AtlanEvent.class);
    }

    /**
     * Translate the JSON payload into an Atlan event object.
     *
     * @param data the JSON payload
     * @return an Atlan event object representation of the payload
     * @throws IOException on any problems deserializing the event details
     */
    static AtlanEvent getAtlanEvent(byte[] data) throws IOException {
        return Serde.mapper.readValue(data, AtlanEvent.class);
    }

    /**
     * Translate the JSON payload directly into the Atlan asset nested in the payload.
     *
     * @param data the JSON payload
     * @return the nested asset object in the event, or null if there is none
     * @throws IOException on any problems deserializing the event details
     */
    static Asset getAssetFromEvent(String data) throws IOException {
        return getAssetFromEvent(getAtlanEvent(data));
    }

    /**
     * Translate the JSON payload directly into the Atlan asset nested in the payload.
     *
     * @param data the JSON payload
     * @return the nested asset object in the event, or null if there is none
     * @throws IOException on any problems deserializing the event details
     */
    static Asset getAssetFromEvent(byte[] data) throws IOException {
        return getAssetFromEvent(getAtlanEvent(data));
    }

    /**
     * Retrieve the asset nested within the Atlan event.
     *
     * @param event containing asset information
     * @return the nested asset object in the event, or null if there is none
     */
    static Asset getAssetFromEvent(AtlanEvent event) {
        if (event == null || event.getPayload() == null || event.getPayload().getAsset() == null) {
            return null;
        }
        return event.getPayload().getAsset();
    }

    /**
     * Retrieve the full asset from Atlan, to ensure we have the latest information about it.
     * Note: this will be slower than getCurrentViewOfAsset, but does not rely on the eventual
     * consistency of the search index so will have the absolute latest information about an
     * asset.
     *
     * @param event containing details about an asset
     * @return the current information about the asset in Atlan, in its entirety
     * @throws AtlanException on any issues communicating with the API
     */
    static Asset getCurrentFullAsset(AtlanEvent event) throws AtlanException {
        AtlanEventPayload payload = event.getPayload();
        if (payload != null && payload.getAsset() != null) {
            return Asset.retrieveFull(payload.getAsset().getGuid());
        }
        return null;
    }

    /**
     * Retrieve a limited set of information about the asset in Atlan,
     * as up-to-date as is available in the search index, to ensure we have
     * reasonably up-to-date information about it.
     * Note: this will be faster than getCurrentFullAsset, but relies on the eventual
     * consistency of the search index so may not have the absolute latest information about
     * an asset.
     *
     * @param event containing details about an asset
     * @param limitedToAttributes the limited set of attributes to retrieve about the asset
     * @param includeMeanings if true, include any assigned terms
     * @param includeAtlanTags if true, include any assigned Atlan tags
     * @return the current information about the asset in Atlan, limited to what was requested
     * @throws AtlanException on any issues communicating with the API
     */
    static Asset getCurrentViewOfAsset(
            AtlanEvent event, Collection<String> limitedToAttributes, boolean includeMeanings, boolean includeAtlanTags)
            throws AtlanException {
        AtlanEventPayload payload = event.getPayload();
        if (payload != null && payload.getAsset() != null) {
            return getCurrentViewOfAsset(payload.getAsset(), limitedToAttributes, includeMeanings, includeAtlanTags);
        }
        return null;
    }

    /**
     * Retrieve a limited set of information about the asset in Atlan,
     * as up-to-date as is available in the search index, to ensure we have
     * reasonably up-to-date information about it.
     * Note: this will be faster than getCurrentFullAsset, but relies on the eventual
     * consistency of the search index so may not have the absolute latest information about
     * an asset.
     *
     * @param fromEvent details of the asset in the event
     * @param limitedToAttributes the limited set of attributes to retrieve about the asset
     * @param includeMeanings if true, include any assigned terms
     * @param includeAtlanTags if true, include any assigned Atlan tags
     * @return the current information about the asset in Atlan, limited to what was requested
     * @throws AtlanException on any issues communicating with the API
     */
    static Asset getCurrentViewOfAsset(
            Asset fromEvent, Collection<String> limitedToAttributes, boolean includeMeanings, boolean includeAtlanTags)
            throws AtlanException {
        IndexSearchRequest request = IndexSearchRequest.builder()
                .dsl(IndexSearchDSL.builder()
                        .query(QueryFactory.CompoundQuery.builder()
                                .must(QueryFactory.beActive())
                                .must(QueryFactory.beOfType(fromEvent.getTypeName()))
                                .must(QueryFactory.have(KeywordFields.QUALIFIED_NAME)
                                        .eq(fromEvent.getQualifiedName()))
                                .build()
                                ._toQuery())
                        .build())
                .excludeAtlanTags(!includeAtlanTags)
                .excludeMeanings(!includeMeanings)
                .attributes(limitedToAttributes == null ? Collections.emptySet() : limitedToAttributes)
                // Include attributes that are mandatory for updates, for some asset types
                .attribute("anchor")
                .attribute("awsArn")
                .relationAttribute("guid")
                .build();
        IndexSearchResponse response = request.search();
        if (response != null && response.getAssets() != null) {
            if (!response.getAssets().isEmpty()) {
                return response.getAssets().get(0);
            }
        }
        return null;
    }

    /**
     * Check if the asset has either a user-provided or system-provided description.
     *
     * @param asset to check for the presence of a description
     * @return true if there is either a user-provided or system-provided description
     */
    static boolean hasDescription(Asset asset) {
        String description = asset.getUserDescription();
        if (description == null || description.length() == 0) {
            description = asset.getDescription();
        }
        return description != null && description.length() > 0;
    }

    /**
     * Check if the asset has any individual or group owners.
     *
     * @param asset to check for the presence of an owner
     * @return true if there is at least one individual or group owner
     */
    static boolean hasOwner(Asset asset) {
        Set<String> ownerUsers = asset.getOwnerUsers();
        Set<String> ownerGroups = asset.getOwnerGroups();
        return (ownerUsers != null && !ownerUsers.isEmpty()) || (ownerGroups != null && !ownerGroups.isEmpty());
    }

    /**
     * Check if the asset has any assigned terms.
     *
     * @param asset to check for the presence of an assigned term
     * @return true if there is at least one assigned term
     */
    static boolean hasAssignedTerms(Asset asset) {
        return (asset.getAssignedTerms() != null && !asset.getAssignedTerms().isEmpty());
    }

    /**
     * Check if the asset has any Atlan tags.
     *
     * @param asset to check for the presence of an Atlan tag
     * @return true if there is at least one assigned Atlan tag
     */
    static boolean hasAtlanTags(Asset asset) {
        return asset.getAtlanTags() != null && !asset.getAtlanTags().isEmpty();
    }

    /**
     * Check if the asset has any lineage.
     *
     * @param asset to check for the presence of lineage
     * @return true if the asset is input to or output from at least one process
     */
    static boolean hasLineage(Asset asset) {
        if (asset instanceof ICatalog) {
            // If possible, look directly on inputs and outputs rather than the __hasLineage flag
            ICatalog details = (ICatalog) asset;
            Set<ILineageProcess> downstream = details.getInputToProcesses();
            Set<ILineageProcess> upstream = details.getOutputFromProcesses();
            return (downstream != null && !downstream.isEmpty()) || (upstream != null && !upstream.isEmpty());
        } else {
            return asset.getHasLineage();
        }
    }
}
