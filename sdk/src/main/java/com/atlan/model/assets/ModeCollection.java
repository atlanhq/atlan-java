/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
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
 * Instance of a Mode collection in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class ModeCollection extends Asset implements IModeCollection, IMode, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ModeCollection";

    /** Fixed typeName for ModeCollections. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

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

    /** State of this collection. */
    @Attribute
    String modeCollectionState;

    /** Type of this collection. */
    @Attribute
    String modeCollectionType;

    /** TBC */
    @Attribute
    String modeId;

    /** TBC */
    @Attribute
    String modeQueryName;

    /** TBC */
    @Attribute
    String modeQueryQualifiedName;

    /** TBC */
    @Attribute
    String modeReportName;

    /** TBC */
    @Attribute
    String modeReportQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IModeReport> modeReports;

    /** TBC */
    @Attribute
    String modeToken;

    /** Workspace in which this collection exists. */
    @Attribute
    IModeWorkspace modeWorkspace;

    /** TBC */
    @Attribute
    String modeWorkspaceName;

    /** TBC */
    @Attribute
    String modeWorkspaceQualifiedName;

    /** TBC */
    @Attribute
    String modeWorkspaceUsername;

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

    /**
     * Builds the minimal object necessary to create a relationship to a ModeCollection, from a potentially
     * more-complete ModeCollection object.
     *
     * @return the minimal object necessary to relate to the ModeCollection
     * @throws InvalidRequestException if any of the minimal set of required properties for a ModeCollection relationship are not found in the initial object
     */
    @Override
    public ModeCollection trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all ModeCollection assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ModeCollection assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all ModeCollection assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all ModeCollection assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ModeCollections will be included
     * @return a fluent search that includes all ModeCollection assets
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
     * Reference to a ModeCollection by GUID. Use this to create a relationship to this ModeCollection,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the ModeCollection to reference
     * @return reference to a ModeCollection that can be used for defining a relationship to a ModeCollection
     */
    public static ModeCollection refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ModeCollection by GUID. Use this to create a relationship to this ModeCollection,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the ModeCollection to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ModeCollection that can be used for defining a relationship to a ModeCollection
     */
    public static ModeCollection refByGuid(String guid, Reference.SaveSemantic semantic) {
        return ModeCollection._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a ModeCollection by qualifiedName. Use this to create a relationship to this ModeCollection,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the ModeCollection to reference
     * @return reference to a ModeCollection that can be used for defining a relationship to a ModeCollection
     */
    public static ModeCollection refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ModeCollection by qualifiedName. Use this to create a relationship to this ModeCollection,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the ModeCollection to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ModeCollection that can be used for defining a relationship to a ModeCollection
     */
    public static ModeCollection refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return ModeCollection._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a ModeCollection by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModeCollection to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ModeCollection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeCollection does not exist or the provided GUID is not a ModeCollection
     */
    @JsonIgnore
    public static ModeCollection get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a ModeCollection by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModeCollection to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full ModeCollection, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeCollection does not exist or the provided GUID is not a ModeCollection
     */
    @JsonIgnore
    public static ModeCollection get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof ModeCollection) {
                return (ModeCollection) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof ModeCollection) {
                return (ModeCollection) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a ModeCollection by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModeCollection to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the ModeCollection, including any relationships
     * @return the requested ModeCollection, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeCollection does not exist or the provided GUID is not a ModeCollection
     */
    @JsonIgnore
    public static ModeCollection get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a ModeCollection by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModeCollection to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the ModeCollection, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the ModeCollection
     * @return the requested ModeCollection, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeCollection does not exist or the provided GUID is not a ModeCollection
     */
    @JsonIgnore
    public static ModeCollection get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = ModeCollection.select(client)
                    .where(ModeCollection.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof ModeCollection) {
                return (ModeCollection) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = ModeCollection.select(client)
                    .where(ModeCollection.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof ModeCollection) {
                return (ModeCollection) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) ModeCollection to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the ModeCollection
     * @return true if the ModeCollection is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a ModeCollection.
     *
     * @param qualifiedName of the ModeCollection
     * @param name of the ModeCollection
     * @return the minimal request necessary to update the ModeCollection, as a builder
     */
    public static ModeCollectionBuilder<?, ?> updater(String qualifiedName, String name) {
        return ModeCollection._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ModeCollection, from a potentially
     * more-complete ModeCollection object.
     *
     * @return the minimal object necessary to update the ModeCollection, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ModeCollection are not found in the initial object
     */
    @Override
    public ModeCollectionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ModeCollectionBuilder<C extends ModeCollection, B extends ModeCollectionBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a ModeCollection.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ModeCollection
     * @param name of the ModeCollection
     * @return the updated ModeCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModeCollection) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ModeCollection.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ModeCollection
     * @param name of the ModeCollection
     * @return the updated ModeCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModeCollection) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ModeCollection.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ModeCollection's owners
     * @param qualifiedName of the ModeCollection
     * @param name of the ModeCollection
     * @return the updated ModeCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModeCollection) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ModeCollection.
     *
     * @param client connectivity to the Atlan tenant on which to update the ModeCollection's certificate
     * @param qualifiedName of the ModeCollection
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ModeCollection, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (ModeCollection)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ModeCollection.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ModeCollection's certificate
     * @param qualifiedName of the ModeCollection
     * @param name of the ModeCollection
     * @return the updated ModeCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModeCollection) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ModeCollection.
     *
     * @param client connectivity to the Atlan tenant on which to update the ModeCollection's announcement
     * @param qualifiedName of the ModeCollection
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (ModeCollection)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ModeCollection.
     *
     * @param client connectivity to the Atlan client from which to remove the ModeCollection's announcement
     * @param qualifiedName of the ModeCollection
     * @param name of the ModeCollection
     * @return the updated ModeCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModeCollection) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the ModeCollection.
     *
     * @param client connectivity to the Atlan tenant on which to replace the ModeCollection's assigned terms
     * @param qualifiedName for the ModeCollection
     * @param name human-readable name of the ModeCollection
     * @param terms the list of terms to replace on the ModeCollection, or null to remove all terms from the ModeCollection
     * @return the ModeCollection that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ModeCollection replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (ModeCollection) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ModeCollection, without replacing existing terms linked to the ModeCollection.
     * Note: this operation must make two API calls — one to retrieve the ModeCollection's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the ModeCollection
     * @param qualifiedName for the ModeCollection
     * @param terms the list of terms to append to the ModeCollection
     * @return the ModeCollection that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static ModeCollection appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ModeCollection) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ModeCollection, without replacing all existing terms linked to the ModeCollection.
     * Note: this operation must make two API calls — one to retrieve the ModeCollection's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the ModeCollection
     * @param qualifiedName for the ModeCollection
     * @param terms the list of terms to remove from the ModeCollection, which must be referenced by GUID
     * @return the ModeCollection that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static ModeCollection removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ModeCollection) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a ModeCollection, without replacing existing Atlan tags linked to the ModeCollection.
     * Note: this operation must make two API calls — one to retrieve the ModeCollection's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ModeCollection
     * @param qualifiedName of the ModeCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ModeCollection
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static ModeCollection appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (ModeCollection) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ModeCollection, without replacing existing Atlan tags linked to the ModeCollection.
     * Note: this operation must make two API calls — one to retrieve the ModeCollection's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ModeCollection
     * @param qualifiedName of the ModeCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ModeCollection
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static ModeCollection appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (ModeCollection) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a ModeCollection.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a ModeCollection
     * @param qualifiedName of the ModeCollection
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ModeCollection
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
