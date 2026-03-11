/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AssetGroupingTrackedCategories;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * User-created collection of assets derived from a grouping strategy.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class AssetGroupingCollection extends Asset
        implements IAssetGroupingCollection, IAssetGrouping, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AssetGroupingCollection";

    /** Fixed typeName for AssetGroupingCollections. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Qualified name of the grouping strategy from which this collection was created. */
    @Attribute
    String assetGroupingCollectionStrategyQualifiedName;

    /** Metadata categories the user explicitly selected for tracking. */
    @Attribute
    @Singular
    SortedSet<AssetGroupingTrackedCategories> assetGroupingCollectionTrackedCategories;

    /** Grouping strategy from which this collection was created. */
    @Attribute
    IAssetGroupingStrategy assetGroupingStrategy;

    /** Tasks to which this asset provides input. */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> inputToAirflowTasks;

    /** Processes to which this asset provides input. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISparkJob> inputToSparkJobs;

    /** Attributes implemented by this asset. */
    @Attribute
    @Singular
    SortedSet<IModelAttribute> modelImplementedAttributes;

    /** Entities implemented by this asset. */
    @Attribute
    @Singular
    SortedSet<IModelEntity> modelImplementedEntities;

    /** Tasks from which this asset is output. */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> outputFromAirflowTasks;

    /** Processes from which this asset is produced as output. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISparkJob> outputFromSparkJobs;

    /** Partial fields contained in the asset. */
    @Attribute
    @Singular
    SortedSet<IPartialField> partialChildFields;

    /** Partial objects contained in the asset. */
    @Attribute
    @Singular
    SortedSet<IPartialObject> partialChildObjects;

    /**
     * Builds the minimal object necessary to create a relationship to a AssetGroupingCollection, from a potentially
     * more-complete AssetGroupingCollection object.
     *
     * @return the minimal object necessary to relate to the AssetGroupingCollection
     * @throws InvalidRequestException if any of the minimal set of required properties for a AssetGroupingCollection relationship are not found in the initial object
     */
    @Override
    public AssetGroupingCollection trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all AssetGroupingCollection assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) AssetGroupingCollection assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all AssetGroupingCollection assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all AssetGroupingCollection assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) AssetGroupingCollections will be included
     * @return a fluent search that includes all AssetGroupingCollection assets
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
     * Reference to a AssetGroupingCollection by GUID. Use this to create a relationship to this AssetGroupingCollection,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the AssetGroupingCollection to reference
     * @return reference to a AssetGroupingCollection that can be used for defining a relationship to a AssetGroupingCollection
     */
    public static AssetGroupingCollection refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AssetGroupingCollection by GUID. Use this to create a relationship to this AssetGroupingCollection,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the AssetGroupingCollection to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AssetGroupingCollection that can be used for defining a relationship to a AssetGroupingCollection
     */
    public static AssetGroupingCollection refByGuid(String guid, Reference.SaveSemantic semantic) {
        return AssetGroupingCollection._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a AssetGroupingCollection by qualifiedName. Use this to create a relationship to this AssetGroupingCollection,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the AssetGroupingCollection to reference
     * @return reference to a AssetGroupingCollection that can be used for defining a relationship to a AssetGroupingCollection
     */
    public static AssetGroupingCollection refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AssetGroupingCollection by qualifiedName. Use this to create a relationship to this AssetGroupingCollection,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the AssetGroupingCollection to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AssetGroupingCollection that can be used for defining a relationship to a AssetGroupingCollection
     */
    public static AssetGroupingCollection refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return AssetGroupingCollection._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a AssetGroupingCollection by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AssetGroupingCollection to retrieve, either its GUID or its full qualifiedName
     * @return the requested full AssetGroupingCollection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AssetGroupingCollection does not exist or the provided GUID is not a AssetGroupingCollection
     */
    @JsonIgnore
    public static AssetGroupingCollection get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a AssetGroupingCollection by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AssetGroupingCollection to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full AssetGroupingCollection, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AssetGroupingCollection does not exist or the provided GUID is not a AssetGroupingCollection
     */
    @JsonIgnore
    public static AssetGroupingCollection get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof AssetGroupingCollection) {
                return (AssetGroupingCollection) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof AssetGroupingCollection) {
                return (AssetGroupingCollection) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a AssetGroupingCollection by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AssetGroupingCollection to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AssetGroupingCollection, including any relationships
     * @return the requested AssetGroupingCollection, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AssetGroupingCollection does not exist or the provided GUID is not a AssetGroupingCollection
     */
    @JsonIgnore
    public static AssetGroupingCollection get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a AssetGroupingCollection by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AssetGroupingCollection to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AssetGroupingCollection, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the AssetGroupingCollection
     * @return the requested AssetGroupingCollection, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AssetGroupingCollection does not exist or the provided GUID is not a AssetGroupingCollection
     */
    @JsonIgnore
    public static AssetGroupingCollection get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = AssetGroupingCollection.select(client)
                    .where(AssetGroupingCollection.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof AssetGroupingCollection) {
                return (AssetGroupingCollection) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = AssetGroupingCollection.select(client)
                    .where(AssetGroupingCollection.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof AssetGroupingCollection) {
                return (AssetGroupingCollection) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) AssetGroupingCollection to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the AssetGroupingCollection
     * @return true if the AssetGroupingCollection is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a AssetGroupingCollection.
     *
     * @param qualifiedName of the AssetGroupingCollection
     * @param name of the AssetGroupingCollection
     * @return the minimal request necessary to update the AssetGroupingCollection, as a builder
     */
    public static AssetGroupingCollectionBuilder<?, ?> updater(String qualifiedName, String name) {
        return AssetGroupingCollection._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AssetGroupingCollection,
     * from a potentially more-complete AssetGroupingCollection object.
     *
     * @return the minimal object necessary to update the AssetGroupingCollection, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a AssetGroupingCollection are not present in the initial object
     */
    @Override
    public AssetGroupingCollectionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class AssetGroupingCollectionBuilder<
                    C extends AssetGroupingCollection, B extends AssetGroupingCollectionBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a AssetGroupingCollection.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AssetGroupingCollection
     * @param name of the AssetGroupingCollection
     * @return the updated AssetGroupingCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AssetGroupingCollection removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AssetGroupingCollection) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a AssetGroupingCollection.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AssetGroupingCollection
     * @param name of the AssetGroupingCollection
     * @return the updated AssetGroupingCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AssetGroupingCollection removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AssetGroupingCollection) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a AssetGroupingCollection.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AssetGroupingCollection's owners
     * @param qualifiedName of the AssetGroupingCollection
     * @param name of the AssetGroupingCollection
     * @return the updated AssetGroupingCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AssetGroupingCollection removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AssetGroupingCollection) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a AssetGroupingCollection.
     *
     * @param client connectivity to the Atlan tenant on which to update the AssetGroupingCollection's certificate
     * @param qualifiedName of the AssetGroupingCollection
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AssetGroupingCollection, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AssetGroupingCollection updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (AssetGroupingCollection)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a AssetGroupingCollection.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AssetGroupingCollection's certificate
     * @param qualifiedName of the AssetGroupingCollection
     * @param name of the AssetGroupingCollection
     * @return the updated AssetGroupingCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AssetGroupingCollection removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AssetGroupingCollection) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a AssetGroupingCollection.
     *
     * @param client connectivity to the Atlan tenant on which to update the AssetGroupingCollection's announcement
     * @param qualifiedName of the AssetGroupingCollection
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AssetGroupingCollection updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (AssetGroupingCollection)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a AssetGroupingCollection.
     *
     * @param client connectivity to the Atlan client from which to remove the AssetGroupingCollection's announcement
     * @param qualifiedName of the AssetGroupingCollection
     * @param name of the AssetGroupingCollection
     * @return the updated AssetGroupingCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AssetGroupingCollection removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AssetGroupingCollection) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the AssetGroupingCollection.
     *
     * @param client connectivity to the Atlan tenant on which to replace the AssetGroupingCollection's assigned terms
     * @param qualifiedName for the AssetGroupingCollection
     * @param name human-readable name of the AssetGroupingCollection
     * @param terms the list of terms to replace on the AssetGroupingCollection, or null to remove all terms from the AssetGroupingCollection
     * @return the AssetGroupingCollection that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static AssetGroupingCollection replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (AssetGroupingCollection) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the AssetGroupingCollection, without replacing existing terms linked to the AssetGroupingCollection.
     * Note: this operation must make two API calls — one to retrieve the AssetGroupingCollection's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the AssetGroupingCollection
     * @param qualifiedName for the AssetGroupingCollection
     * @param terms the list of terms to append to the AssetGroupingCollection
     * @return the AssetGroupingCollection that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static AssetGroupingCollection appendTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (AssetGroupingCollection) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a AssetGroupingCollection, without replacing all existing terms linked to the AssetGroupingCollection.
     * Note: this operation must make two API calls — one to retrieve the AssetGroupingCollection's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the AssetGroupingCollection
     * @param qualifiedName for the AssetGroupingCollection
     * @param terms the list of terms to remove from the AssetGroupingCollection, which must be referenced by GUID
     * @return the AssetGroupingCollection that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static AssetGroupingCollection removeTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (AssetGroupingCollection) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a AssetGroupingCollection, without replacing existing Atlan tags linked to the AssetGroupingCollection.
     * Note: this operation must make two API calls — one to retrieve the AssetGroupingCollection's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AssetGroupingCollection
     * @param qualifiedName of the AssetGroupingCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated AssetGroupingCollection
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static AssetGroupingCollection appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (AssetGroupingCollection) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AssetGroupingCollection, without replacing existing Atlan tags linked to the AssetGroupingCollection.
     * Note: this operation must make two API calls — one to retrieve the AssetGroupingCollection's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AssetGroupingCollection
     * @param qualifiedName of the AssetGroupingCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated AssetGroupingCollection
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static AssetGroupingCollection appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (AssetGroupingCollection) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a AssetGroupingCollection.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a AssetGroupingCollection
     * @param qualifiedName of the AssetGroupingCollection
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the AssetGroupingCollection
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
