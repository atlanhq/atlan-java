/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.core.AsyncCreationResponse;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.IconType;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a query collection in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class AtlanCollection extends Asset implements IAtlanCollection, INamespace, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Collection";

    /** Fixed typeName for AtlanCollections. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IFolder> childrenFolders;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IAtlanQuery> childrenQueries;

    /** TBC */
    @Attribute
    String icon;

    /** TBC */
    @Attribute
    IconType iconType;

    /**
     * Builds the minimal object necessary to create a relationship to a AtlanCollection, from a potentially
     * more-complete AtlanCollection object.
     *
     * @return the minimal object necessary to relate to the AtlanCollection
     * @throws InvalidRequestException if any of the minimal set of required properties for a AtlanCollection relationship are not found in the initial object
     */
    @Override
    public AtlanCollection trimToReference() throws InvalidRequestException {
        if (this.getGuid() != null && !this.getGuid().isEmpty()) {
            return refByGuid(this.getGuid());
        }
        if (this.getQualifiedName() != null && !this.getQualifiedName().isEmpty()) {
            return refByQualifiedName(this.getQualifiedName());
        }
        if (this.getUniqueAttributes() != null
                && this.getUniqueAttributes().getQualifiedName() != null
                && !this.getUniqueAttributes().getQualifiedName().isEmpty()) {
            return refByQualifiedName(this.getUniqueAttributes().getQualifiedName());
        }
        throw new InvalidRequestException(
                ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, TYPE_NAME, "guid, qualifiedName");
    }

    /**
     * Start a fluent search that will return all AtlanCollection assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) AtlanCollection assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all AtlanCollection assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all AtlanCollection assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) AtlanCollections will be included
     * @return a fluent search that includes all AtlanCollection assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client, boolean includeArchived) {
        FluentSearch.FluentSearchBuilder<?, ?> builder =
                FluentSearch.builder(client).where(Asset.TYPE_NAME.eq(TYPE_NAME));
        if (!includeArchived) {
            builder.active();
        }
        return builder;
    }

    /**
     * Reference to a AtlanCollection by GUID. Use this to create a relationship to this AtlanCollection,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the AtlanCollection to reference
     * @return reference to a AtlanCollection that can be used for defining a relationship to a AtlanCollection
     */
    public static AtlanCollection refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AtlanCollection by GUID. Use this to create a relationship to this AtlanCollection,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the AtlanCollection to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AtlanCollection that can be used for defining a relationship to a AtlanCollection
     */
    public static AtlanCollection refByGuid(String guid, Reference.SaveSemantic semantic) {
        return AtlanCollection._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a AtlanCollection by qualifiedName. Use this to create a relationship to this AtlanCollection,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the AtlanCollection to reference
     * @return reference to a AtlanCollection that can be used for defining a relationship to a AtlanCollection
     */
    public static AtlanCollection refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AtlanCollection by qualifiedName. Use this to create a relationship to this AtlanCollection,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the AtlanCollection to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AtlanCollection that can be used for defining a relationship to a AtlanCollection
     */
    public static AtlanCollection refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return AtlanCollection._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a AtlanCollection by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AtlanCollection to retrieve, either its GUID or its full qualifiedName
     * @return the requested full AtlanCollection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AtlanCollection does not exist or the provided GUID is not a AtlanCollection
     */
    @JsonIgnore
    public static AtlanCollection get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a AtlanCollection by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AtlanCollection to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full AtlanCollection, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AtlanCollection does not exist or the provided GUID is not a AtlanCollection
     */
    @JsonIgnore
    public static AtlanCollection get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof AtlanCollection) {
                return (AtlanCollection) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof AtlanCollection) {
                return (AtlanCollection) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a AtlanCollection by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AtlanCollection to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AtlanCollection, including any relationships
     * @return the requested AtlanCollection, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AtlanCollection does not exist or the provided GUID is not a AtlanCollection
     */
    @JsonIgnore
    public static AtlanCollection get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a AtlanCollection by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AtlanCollection to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AtlanCollection, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the AtlanCollection
     * @return the requested AtlanCollection, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AtlanCollection does not exist or the provided GUID is not a AtlanCollection
     */
    @JsonIgnore
    public static AtlanCollection get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = AtlanCollection.select(client)
                    .where(AtlanCollection.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof AtlanCollection) {
                return (AtlanCollection) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = AtlanCollection.select(client)
                    .where(AtlanCollection.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof AtlanCollection) {
                return (AtlanCollection) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) AtlanCollection to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the AtlanCollection
     * @return true if the AtlanCollection is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Add the API token configured for the default client as an admin for this AtlanCollection.
     * This is necessary to allow the API token to manage the collection itself or any queries within it.
     *
     * @param client connectivity to the Atlan tenant
     * @param impersonationToken a bearer token for an actual user who is already an admin for the AtlanCollection, NOT an API token
     * @throws AtlanException on any error during API invocation
     */
    public AssetMutationResponse addApiTokenAsAdmin(AtlanClient client, final String impersonationToken)
            throws AtlanException {
        return Asset.addApiTokenAsAdmin(client, getGuid(), impersonationToken);
    }

    /**
     * Add the API token configured for the default client as a viewer for this AtlanCollection.
     * This is necessary to allow the API token to view or run queries within the collection, but not make any
     * changes to them.
     *
     * @param client connectivity to Atlan tenant
     * @param impersonationToken a bearer token for an actual user who is already an admin for the AtlanCollection, NOT an API token
     * @throws AtlanException on any error during API invocation
     */
    public AssetMutationResponse addApiTokenAsViewer(AtlanClient client, final String impersonationToken)
            throws AtlanException {
        String username = client.users.getCurrentUser().getUsername();
        AssetMutationResponse response = null;
        try (AtlanClient tmp = new AtlanClient(client.getBaseUrl(), impersonationToken)) {
            // Look for the asset as the impersonated user, ensuring we include the viewer users
            // in the results (so we avoid clobbering any existing viewer users)
            Optional<Asset> found =
                    tmp.assets.select().where(GUID.eq(getGuid())).includeOnResults(VIEWER_USERS).stream()
                            .findFirst();
            response = null;
            if (found.isPresent()) {
                Asset asset = found.get();
                Set<String> existingViewers = asset.getViewerUsers();
                response = asset.trimToRequired()
                        .viewerUsers(existingViewers)
                        .viewerUser(username)
                        .build()
                        .save(tmp);
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, getGuid());
            }
        } catch (Exception e) {
            log.warn("Unable to remove temporary client using impersonationToken.", e);
        }
        return response;
    }

    /**
     * Builds the minimal object necessary to create an AltanCollection.
     *
     * @param client connectivity to the Atlan tenant
     * @param name of the AtlanCollection as the user who will own the AtlanCollection
     * @return the minimal request necessary to create the AtlanCollection, as a builder
     */
    public static AtlanCollectionBuilder<?, ?> creator(AtlanClient client, String name) {
        return AtlanCollection._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(client));
    }

    /**
     * Generate a unique AltanCollection name.
     *
     * @param client connectivity to the Atlan tenant as the user who will own the AtlanCollection
     * @return a unique name for the AltanCollection
     */
    public static String generateQualifiedName(AtlanClient client) {
        try {
            String username = client.users.getCurrentUser().getUsername();
            return "default/collection/" + username + "/" + UUID.randomUUID();
        } catch (AtlanException e) {
            log.error("Unable to determine the current user.", e);
        }
        return null;
    }

    /**
     * If an asset with the same qualifiedName exists, updates the existing asset. Otherwise, creates the asset.
     * No Atlan tags or custom metadata will be changed if updating an existing asset, irrespective of what
     * is included in the asset itself when the method is called.
     *
     * @param client connectivity to the Atlan tenant where this collection should be saved
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     */
    @Override
    public AsyncCreationResponse save(AtlanClient client) throws AtlanException {
        return client.assets.save(this);
    }

    /**
     * If no asset exists, has the same behavior as the {@link #save(AtlanClient)} method.
     * If an asset does exist, optionally overwrites any Atlan tags. Custom metadata will always
     * be entirely ignored using this method.
     *
     * @param client connectivity to the Atlan tenant where this collection should be saved
     * @param replaceAtlanTags whether to replace Atlan tags during an update (true) or not (false)
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     * @deprecated see {@link #save(AtlanClient)}
     */
    @Deprecated
    @Override
    public AsyncCreationResponse save(AtlanClient client, boolean replaceAtlanTags) throws AtlanException {
        return client.assets.save(this, replaceAtlanTags);
    }

    /**
     * Builds the minimal object necessary to update a AtlanCollection.
     *
     * @param qualifiedName of the AtlanCollection
     * @param name of the AtlanCollection
     * @return the minimal request necessary to update the AtlanCollection, as a builder
     */
    public static AtlanCollectionBuilder<?, ?> updater(String qualifiedName, String name) {
        return AtlanCollection._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AtlanCollection, from a potentially
     * more-complete AtlanCollection object.
     *
     * @return the minimal object necessary to update the AtlanCollection, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AtlanCollection are not found in the initial object
     */
    @Override
    public AtlanCollectionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Find a collection by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the collection, if found.
     *
     * @param client connectivity to the Atlan tenant in which to search for the collection
     * @param name of the collection
     * @return all collections with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the collection does not exist
     */
    public static List<AtlanCollection> findByName(AtlanClient client, String name) throws AtlanException {
        return findByName(client, name, (List<AtlanField>) null);
    }

    /**
     * Find a collection by its human-readable name.
     *
     * @param client connectivity to the Atlan tenant in which to search for the collection
     * @param name of the collection
     * @param attributes an optional collection of attributes to retrieve for the collection
     * @return all collections with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the collection does not exist
     */
    public static List<AtlanCollection> findByName(AtlanClient client, String name, Collection<String> attributes)
            throws AtlanException {
        List<AtlanCollection> results = new ArrayList<>();
        AtlanCollection.select(client)
                .where(NAME.eq(name))
                ._includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof AtlanCollection)
                .forEach(c -> results.add((AtlanCollection) c));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.COLLECTION_NOT_FOUND_BY_NAME, name);
        }
        return results;
    }

    /**
     * Find a collection by its human-readable name.
     *
     * @param client connectivity to the Atlan tenant in which to search for the collection
     * @param name of the collection
     * @param attributes an optional collection of attributes (checked) to retrieve for the collection
     * @return all collections with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the collection does not exist
     */
    public static List<AtlanCollection> findByName(AtlanClient client, String name, List<AtlanField> attributes)
            throws AtlanException {
        List<AtlanCollection> results = new ArrayList<>();
        AtlanCollection.select(client)
                .where(NAME.eq(name))
                .includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof AtlanCollection)
                .forEach(c -> results.add((AtlanCollection) c));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.COLLECTION_NOT_FOUND_BY_NAME, name);
        }
        return results;
    }

    public abstract static class AtlanCollectionBuilder<
                    C extends AtlanCollection, B extends AtlanCollectionBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a AtlanCollection.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AtlanCollection
     * @param name of the AtlanCollection
     * @return the updated AtlanCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanCollection removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AtlanCollection) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a AtlanCollection.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AtlanCollection
     * @param name of the AtlanCollection
     * @return the updated AtlanCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanCollection removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AtlanCollection) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a AtlanCollection.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AtlanCollection's owners
     * @param qualifiedName of the AtlanCollection
     * @param name of the AtlanCollection
     * @return the updated AtlanCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanCollection removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AtlanCollection) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a AtlanCollection.
     *
     * @param client connectivity to the Atlan tenant on which to update the AtlanCollection's certificate
     * @param qualifiedName of the AtlanCollection
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AtlanCollection, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AtlanCollection updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (AtlanCollection)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a AtlanCollection.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AtlanCollection's certificate
     * @param qualifiedName of the AtlanCollection
     * @param name of the AtlanCollection
     * @return the updated AtlanCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanCollection removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AtlanCollection) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a AtlanCollection.
     *
     * @param client connectivity to the Atlan tenant on which to update the AtlanCollection's announcement
     * @param qualifiedName of the AtlanCollection
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AtlanCollection updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (AtlanCollection)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a AtlanCollection.
     *
     * @param client connectivity to the Atlan client from which to remove the AtlanCollection's announcement
     * @param qualifiedName of the AtlanCollection
     * @param name of the AtlanCollection
     * @return the updated AtlanCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanCollection removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AtlanCollection) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the AtlanCollection.
     *
     * @param client connectivity to the Atlan tenant on which to replace the AtlanCollection's assigned terms
     * @param qualifiedName for the AtlanCollection
     * @param name human-readable name of the AtlanCollection
     * @param terms the list of terms to replace on the AtlanCollection, or null to remove all terms from the AtlanCollection
     * @return the AtlanCollection that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static AtlanCollection replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (AtlanCollection) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the AtlanCollection, without replacing existing terms linked to the AtlanCollection.
     * Note: this operation must make two API calls — one to retrieve the AtlanCollection's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the AtlanCollection
     * @param qualifiedName for the AtlanCollection
     * @param terms the list of terms to append to the AtlanCollection
     * @return the AtlanCollection that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static AtlanCollection appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AtlanCollection) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a AtlanCollection, without replacing all existing terms linked to the AtlanCollection.
     * Note: this operation must make two API calls — one to retrieve the AtlanCollection's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the AtlanCollection
     * @param qualifiedName for the AtlanCollection
     * @param terms the list of terms to remove from the AtlanCollection, which must be referenced by GUID
     * @return the AtlanCollection that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static AtlanCollection removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AtlanCollection) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a AtlanCollection, without replacing existing Atlan tags linked to the AtlanCollection.
     * Note: this operation must make two API calls — one to retrieve the AtlanCollection's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AtlanCollection
     * @param qualifiedName of the AtlanCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated AtlanCollection
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static AtlanCollection appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (AtlanCollection) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AtlanCollection, without replacing existing Atlan tags linked to the AtlanCollection.
     * Note: this operation must make two API calls — one to retrieve the AtlanCollection's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AtlanCollection
     * @param qualifiedName of the AtlanCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated AtlanCollection
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static AtlanCollection appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (AtlanCollection) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a AtlanCollection.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a AtlanCollection
     * @param qualifiedName of the AtlanCollection
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the AtlanCollection
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
