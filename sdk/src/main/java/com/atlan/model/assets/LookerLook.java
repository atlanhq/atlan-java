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
 * Instance of a Looker Look in Atlan. Looks are saved visualizations used to understand and analyze data. They can be shared and reused in multiple dashboards.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class LookerLook extends Asset implements ILookerLook, ILooker, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "LookerLook";

    /** Fixed typeName for LookerLooks. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Dashboard in which this Look is used. */
    @Attribute
    ILookerDashboard dashboard;

    /** Fields that are used in this look. */
    @Attribute
    @Singular
    SortedSet<ILookerField> fields;

    /** Folder in which this Look exists. */
    @Attribute
    ILookerFolder folder;

    /** Name of the folder in which the Look is organized. */
    @Attribute
    String folderName;

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

    /** An alpha-numeric slug for the underlying Looker asset that can be used to uniquely identify it */
    @Attribute
    String lookerSlug;

    /** Identifier of the query for the Look, from Looker. */
    @Attribute
    String lookerSourceQueryId;

    /** Model in which this Look exists. */
    @Attribute
    ILookerModel model;

    /** Attributes implemented by this asset. */
    @Attribute
    @Singular
    SortedSet<IModelAttribute> modelImplementedAttributes;

    /** Entities implemented by this asset. */
    @Attribute
    @Singular
    SortedSet<IModelEntity> modelImplementedEntities;

    /** Name of the model in which this Look exists. */
    @Attribute
    String modelName;

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

    /** Deprecated. */
    @Attribute
    ILookerQuery query;

    /** Identifier of the Look's content metadata, from Looker. */
    @Attribute
    Integer sourceContentMetadataId;

    /** Time (epoch) when the Look was last accessed by a user, in milliseconds. */
    @Attribute
    @Date
    Long sourceLastAccessedAt;

    /** Time (epoch) when the Look was last viewed by a user, in milliseconds. */
    @Attribute
    @Date
    Long sourceLastViewedAt;

    /** (Deprecated) Please use lookerSourceQueryId instead. */
    @Attribute
    Integer sourceQueryId;

    /** Identifier of the user who created the Look, from Looker. */
    @Attribute
    Integer sourceUserId;

    /** Number of times the look has been viewed in the Looker web UI. */
    @Attribute
    Integer sourceViewCount;

    /** Identifier of the user that last updated the Look, from Looker. */
    @Attribute
    Integer sourcelastUpdaterId;

    /** Tiles that exist within this Look. */
    @Attribute
    ILookerTile tile;

    /**
     * Builds the minimal object necessary to create a relationship to a LookerLook, from a potentially
     * more-complete LookerLook object.
     *
     * @return the minimal object necessary to relate to the LookerLook
     * @throws InvalidRequestException if any of the minimal set of required properties for a LookerLook relationship are not found in the initial object
     */
    @Override
    public LookerLook trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all LookerLook assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) LookerLook assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all LookerLook assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all LookerLook assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) LookerLooks will be included
     * @return a fluent search that includes all LookerLook assets
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
     * Reference to a LookerLook by GUID. Use this to create a relationship to this LookerLook,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the LookerLook to reference
     * @return reference to a LookerLook that can be used for defining a relationship to a LookerLook
     */
    public static LookerLook refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a LookerLook by GUID. Use this to create a relationship to this LookerLook,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the LookerLook to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a LookerLook that can be used for defining a relationship to a LookerLook
     */
    public static LookerLook refByGuid(String guid, Reference.SaveSemantic semantic) {
        return LookerLook._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a LookerLook by qualifiedName. Use this to create a relationship to this LookerLook,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the LookerLook to reference
     * @return reference to a LookerLook that can be used for defining a relationship to a LookerLook
     */
    public static LookerLook refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a LookerLook by qualifiedName. Use this to create a relationship to this LookerLook,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the LookerLook to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a LookerLook that can be used for defining a relationship to a LookerLook
     */
    public static LookerLook refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return LookerLook._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a LookerLook by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the LookerLook to retrieve, either its GUID or its full qualifiedName
     * @return the requested full LookerLook, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerLook does not exist or the provided GUID is not a LookerLook
     */
    @JsonIgnore
    public static LookerLook get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a LookerLook by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the LookerLook to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full LookerLook, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerLook does not exist or the provided GUID is not a LookerLook
     */
    @JsonIgnore
    public static LookerLook get(AtlanClient client, String id, boolean includeAllRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof LookerLook) {
                return (LookerLook) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof LookerLook) {
                return (LookerLook) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a LookerLook by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the LookerLook to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the LookerLook, including any relationships
     * @return the requested LookerLook, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerLook does not exist or the provided GUID is not a LookerLook
     */
    @JsonIgnore
    public static LookerLook get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a LookerLook by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the LookerLook to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the LookerLook, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the LookerLook
     * @return the requested LookerLook, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerLook does not exist or the provided GUID is not a LookerLook
     */
    @JsonIgnore
    public static LookerLook get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = LookerLook.select(client)
                    .where(LookerLook.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof LookerLook) {
                return (LookerLook) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = LookerLook.select(client)
                    .where(LookerLook.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof LookerLook) {
                return (LookerLook) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) LookerLook to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the LookerLook
     * @return true if the LookerLook is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a LookerLook.
     *
     * @param qualifiedName of the LookerLook
     * @param name of the LookerLook
     * @return the minimal request necessary to update the LookerLook, as a builder
     */
    public static LookerLookBuilder<?, ?> updater(String qualifiedName, String name) {
        return LookerLook._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a LookerLook, from a potentially
     * more-complete LookerLook object.
     *
     * @return the minimal object necessary to update the LookerLook, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for LookerLook are not found in the initial object
     */
    @Override
    public LookerLookBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a LookerLook.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the LookerLook
     * @param name of the LookerLook
     * @return the updated LookerLook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LookerLook) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a LookerLook.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the LookerLook
     * @param name of the LookerLook
     * @return the updated LookerLook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LookerLook) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a LookerLook.
     *
     * @param client connectivity to the Atlan tenant from which to remove the LookerLook's owners
     * @param qualifiedName of the LookerLook
     * @param name of the LookerLook
     * @return the updated LookerLook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (LookerLook) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a LookerLook.
     *
     * @param client connectivity to the Atlan tenant on which to update the LookerLook's certificate
     * @param qualifiedName of the LookerLook
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated LookerLook, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (LookerLook)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a LookerLook.
     *
     * @param client connectivity to the Atlan tenant from which to remove the LookerLook's certificate
     * @param qualifiedName of the LookerLook
     * @param name of the LookerLook
     * @return the updated LookerLook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LookerLook) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a LookerLook.
     *
     * @param client connectivity to the Atlan tenant on which to update the LookerLook's announcement
     * @param qualifiedName of the LookerLook
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (LookerLook)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a LookerLook.
     *
     * @param client connectivity to the Atlan client from which to remove the LookerLook's announcement
     * @param qualifiedName of the LookerLook
     * @param name of the LookerLook
     * @return the updated LookerLook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LookerLook) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the LookerLook.
     *
     * @param client connectivity to the Atlan tenant on which to replace the LookerLook's assigned terms
     * @param qualifiedName for the LookerLook
     * @param name human-readable name of the LookerLook
     * @param terms the list of terms to replace on the LookerLook, or null to remove all terms from the LookerLook
     * @return the LookerLook that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static LookerLook replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (LookerLook) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the LookerLook, without replacing existing terms linked to the LookerLook.
     * Note: this operation must make two API calls — one to retrieve the LookerLook's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the LookerLook
     * @param qualifiedName for the LookerLook
     * @param terms the list of terms to append to the LookerLook
     * @return the LookerLook that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static LookerLook appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (LookerLook) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a LookerLook, without replacing all existing terms linked to the LookerLook.
     * Note: this operation must make two API calls — one to retrieve the LookerLook's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the LookerLook
     * @param qualifiedName for the LookerLook
     * @param terms the list of terms to remove from the LookerLook, which must be referenced by GUID
     * @return the LookerLook that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static LookerLook removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (LookerLook) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a LookerLook, without replacing existing Atlan tags linked to the LookerLook.
     * Note: this operation must make two API calls — one to retrieve the LookerLook's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the LookerLook
     * @param qualifiedName of the LookerLook
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated LookerLook
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static LookerLook appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (LookerLook) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a LookerLook, without replacing existing Atlan tags linked to the LookerLook.
     * Note: this operation must make two API calls — one to retrieve the LookerLook's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the LookerLook
     * @param qualifiedName of the LookerLook
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated LookerLook
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static LookerLook appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (LookerLook) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a LookerLook.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a LookerLook
     * @param qualifiedName of the LookerLook
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the LookerLook
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
