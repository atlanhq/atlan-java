/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.events;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.*;
import com.atlan.model.enums.AtlanTagHandling;
import com.atlan.model.enums.CustomMetadataHandling;
import com.atlan.model.events.AtlanEvent;
import com.atlan.model.events.AtlanEventPayload;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.util.AssetBatch;
import java.io.IOException;
import java.util.*;
import org.slf4j.Logger;

public interface AtlanEventHandler {

    /** Retrieve connectivity to the Atlan client for this handler. */
    AtlanClient getClient();

    /**
     * Validate the prerequisites expected by the event handler. These should generally run before
     * trying to do any other actions.
     * This default implementation will only confirm that an event has been received and there are
     * details of an asset embedded within the event.
     *
     * @param event the event to be processed
     * @param log a logger to log anything you want
     * @return true if the prerequisites are met, otherwise false
     * @throws AtlanException optionally throw any errors on unexpected problems
     */
    default boolean validatePrerequisites(AtlanEvent event, Logger log) throws AtlanException {
        return event != null && event.getPayload() != null && event.getPayload().getAsset() != null;
    }

    /**
     * Retrieve the current state of the asset, with minimal required info to handle any logic
     * the event handler requires to make its decisions.
     * This default implementation will only really check that the asset still exists in Atlan.
     *
     * @param client connectivity to Atlan
     * @param fromEvent the asset from the event (which could be stale at this point)
     * @param log a logger to log anything you want
     * @return the current state of the asset, as retrieved from Atlan
     * @throws AtlanException if there are any problems retrieving the current state of the asset from Atlan
     */
    default Asset getCurrentState(AtlanClient client, Asset fromEvent, Logger log) throws AtlanException {
        return getCurrentViewOfAsset(client, fromEvent, null, false, false);
    }

    /**
     * Calculate any changes to apply to assets, and return a collection of the minimally-updated form of the assets
     * with those changes applied (in-memory). Typically, you will want to call {@link Asset#trimToRequired()}
     * on the currentView of each asset before making any changes, to ensure a minimal set of changes are applied to the
     * asset (minimizing the risk of accidentally clobbering any other changes someone may make to the asset
     * between this in-memory set of changes and the subsequent application of those changes to Atlan itself).
     * Also, you should call your {@link #hasChanges(Asset, Asset, Logger)} method for each asset
     * to determine whether it actually has any changes to include before returning it from this method.
     * NOTE: The returned assets from this method should be ONLY those assets on which updates are actually being
     * applied, or you will risk an infinite loop of events triggering changes, more events, more changes, etc.
     *
     * @param currentView the current view / state of the asset in Atlan, as the starting point for any changes
     * @param log a logger to log anything you want
     * @return a collection of only those assets that have changes to send to Atlan (empty, if there are no changes to send)
     * @throws AtlanException if there are any problems calculating or applying changes (in-memory) to the asset
     */
    Collection<Asset> calculateChanges(Asset currentView, Logger log) throws AtlanException;

    /**
     * Check the key information this event processing is meant to handle between the original asset and the
     * in-memory-modified asset. Only return true if there is actually a change to be applied to this asset in
     * Atlan - this ensures idempotency, and avoids an infinite loop of making changes repeatedly in Atlan,
     * which triggers a new event, a new change, a new event, and so on.
     * This default implementation only blindly checks for equality. It is likely you would want to check
     * specific attributes' values, rather than the entire object, for equality when determining whether a relevant
     * change has been made (or not) to the asset.
     *
     * @param current the current view / state of the asset in Atlan, that was the starting point for any change calculations
     * @param modified the in-memory-modified asset against which to check if any changes actually need to be sent to Atlan
     * @param log a logger to log anything you want
     * @return true if the modified asset should be sent on to (updated in) Atlan, or false if there are no actual changes to apply
     */
    default boolean hasChanges(Asset current, Asset modified, Logger log) {
        return !Objects.equals(current, modified);
    }

    /**
     * Actually send the changed assets to Atlan so that they are persisted.
     *
     * @param client connectivity to Atlan
     * @param changedAssets the in-memory-modified assets to send to Atlan
     * @param log a logger to log anything you want
     * @throws AtlanException if there are any problems actually updating the asset in Atlan
     */
    default void saveChanges(AtlanClient client, Collection<Asset> changedAssets, Logger log) throws AtlanException {
        AssetBatch batch = new AssetBatch(client, 20, AtlanTagHandling.IGNORE, CustomMetadataHandling.MERGE);
        for (Asset one : changedAssets) {
            batch.add(one);
        }
        batch.flush();
    }

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
     * @param client connectivity to Atlan
     * @param data the JSON payload
     * @return an Atlan event object representation of the payload
     * @throws IOException on any problems deserializing the event details
     */
    static AtlanEvent getAtlanEvent(AtlanClient client, String data) throws IOException {
        return client.readValue(data, AtlanEvent.class);
    }

    /**
     * Translate the JSON payload into an Atlan event object.
     *
     * @param client connectivity to Atlan
     * @param data the JSON payload
     * @return an Atlan event object representation of the payload
     * @throws IOException on any problems deserializing the event details
     */
    static AtlanEvent getAtlanEvent(AtlanClient client, byte[] data) throws IOException {
        return client.readValue(data, AtlanEvent.class);
    }

    /**
     * Retrieve the full asset from Atlan, to ensure we have the latest information about it.
     * Note: this will be slower than getCurrentViewOfAsset, but does not rely on the eventual
     * consistency of the search index so will have the absolute latest information about an
     * asset.
     *
     * @param client connectivity to Atlan
     * @param event containing details about an asset
     * @return the current information about the asset in Atlan, in its entirety
     * @throws AtlanException on any issues communicating with the API
     */
    static Asset getCurrentFullAsset(AtlanClient client, AtlanEvent event) throws AtlanException {
        AtlanEventPayload payload = event.getPayload();
        if (payload != null && payload.getAsset() != null) {
            return Asset.get(client, payload.getAsset().getGuid(), true);
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
     * @param client connectivity to Atlan
     * @param event containing details about an asset
     * @param limitedToAttributes the limited set of attributes to retrieve about the asset
     * @param includeMeanings if true, include any assigned terms
     * @param includeAtlanTags if true, include any assigned Atlan tags
     * @return the current information about the asset in Atlan, limited to what was requested
     * @throws AtlanException on any issues communicating with the API
     */
    static Asset getCurrentViewOfAsset(
            AtlanClient client,
            AtlanEvent event,
            Collection<String> limitedToAttributes,
            boolean includeMeanings,
            boolean includeAtlanTags)
            throws AtlanException {
        AtlanEventPayload payload = event.getPayload();
        if (payload != null && payload.getAsset() != null) {
            return getCurrentViewOfAsset(
                    client, payload.getAsset(), limitedToAttributes, includeMeanings, includeAtlanTags);
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
     * @param client connectivity to Atlan
     * @param fromEvent details of the asset in the event
     * @param limitedToAttributes the limited set of attributes to retrieve about the asset
     * @param includeMeanings if true, include any assigned terms
     * @param includeAtlanTags if true, include any assigned Atlan tags
     * @return the current information about the asset in Atlan, limited to what was requested
     * @throws AtlanException on any issues communicating with the API
     */
    static Asset getCurrentViewOfAsset(
            AtlanClient client,
            Asset fromEvent,
            Collection<String> limitedToAttributes,
            boolean includeMeanings,
            boolean includeAtlanTags)
            throws AtlanException {
        IndexSearchRequest request = client.assets
                .select()
                .where(Asset.TYPE_NAME.eq(fromEvent.getTypeName()))
                .where(Asset.QUALIFIED_NAME.eq(fromEvent.getQualifiedName()))
                // Include attributes that are mandatory for updates, for some asset types
                .includeOnResults(IGlossaryTerm.ANCHOR)
                .includeOnResults(IAWS.AWS_ARN)
                .includeOnRelations(IReferenceable.GUID)
                .includeOnRelations(Asset.NAME)
                .includeOnRelations(Asset.DESCRIPTION)
                .toRequestBuilder()
                .excludeAtlanTags(!includeAtlanTags)
                .excludeMeanings(!includeMeanings)
                .allowDeletedRelations(false)
                .attributes(limitedToAttributes == null ? Collections.emptySet() : limitedToAttributes)
                .build();
        IndexSearchResponse response = request.search(client);
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
        if (description == null || description.isEmpty()) {
            description = asset.getDescription();
        }
        return description != null && !description.isEmpty();
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

    /**
     * Check if the asset has a README assigned (with content).
     *
     * @param asset to check for the presence of a README
     * @return true if the asset has a README, and the README is not empty
     */
    static boolean hasReadme(Asset asset) {
        return asset.getReadme() != null
                && asset.getReadme().getDescription() != null
                && !asset.getReadme().getDescription().isEmpty();
    }
}
