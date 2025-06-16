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
 * Instance of a Looker field in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class LookerField extends Asset implements ILookerField, ILooker, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "LookerField";

    /** Fixed typeName for LookerFields. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Dashboard in which this field is used. */
    @Attribute
    ILookerDashboard dashboard;

    /** Explore in which this field exists. */
    @Attribute
    ILookerExplore explore;

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

    /** Look in which this field is used. */
    @Attribute
    ILookerLook look;

    /** Unique name of the dashboard in which this field is used. */
    @Attribute
    String lookerDashboardQualifiedName;

    /** Unique name of the Explore in which this field exists. */
    @Attribute
    String lookerExploreQualifiedName;

    /** Deprecated. */
    @Attribute
    String lookerFieldDataType;

    /** Whether the looker field asset is coming from a refinement */
    @Attribute
    Boolean lookerFieldIsRefined;

    /** Absolute path of the file where the refinement of the field is declared. */
    @Attribute
    String lookerFieldRefinementFilePath;

    /** Line number in the lookerFieldRefinementFilePath where this refinement of the field is declared. */
    @Attribute
    String lookerFieldRefinementLineNumber;

    /** Unique name of the look in which this field is used. */
    @Attribute
    String lookerLookQualifiedName;

    /** An alpha-numeric slug for the underlying Looker asset that can be used to uniquely identify it */
    @Attribute
    String lookerSlug;

    /** Unique name of the tile in which this field is used. */
    @Attribute
    String lookerTileQualifiedName;

    /** Deprecated. */
    @Attribute
    Integer lookerTimesUsed;

    /** Unique name of the view in which this field exists. */
    @Attribute
    String lookerViewQualifiedName;

    /** Model in which this field exists. */
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

    /** Name of the model in which this field exists. */
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

    /** Project in which this field exists. */
    @Attribute
    ILookerProject project;

    /** Name of the project in which this field exists. */
    @Attribute
    String projectName;

    /** Deprecated. */
    @Attribute
    String sourceDefinition;

    /** Tile in which this field is used. */
    @Attribute
    ILookerTile tile;

    /** View in which this field exists. */
    @Attribute
    ILookerView view;

    /**
     * Builds the minimal object necessary to create a relationship to a LookerField, from a potentially
     * more-complete LookerField object.
     *
     * @return the minimal object necessary to relate to the LookerField
     * @throws InvalidRequestException if any of the minimal set of required properties for a LookerField relationship are not found in the initial object
     */
    @Override
    public LookerField trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all LookerField assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) LookerField assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all LookerField assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all LookerField assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) LookerFields will be included
     * @return a fluent search that includes all LookerField assets
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
     * Reference to a LookerField by GUID. Use this to create a relationship to this LookerField,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the LookerField to reference
     * @return reference to a LookerField that can be used for defining a relationship to a LookerField
     */
    public static LookerField refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a LookerField by GUID. Use this to create a relationship to this LookerField,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the LookerField to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a LookerField that can be used for defining a relationship to a LookerField
     */
    public static LookerField refByGuid(String guid, Reference.SaveSemantic semantic) {
        return LookerField._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a LookerField by qualifiedName. Use this to create a relationship to this LookerField,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the LookerField to reference
     * @return reference to a LookerField that can be used for defining a relationship to a LookerField
     */
    public static LookerField refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a LookerField by qualifiedName. Use this to create a relationship to this LookerField,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the LookerField to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a LookerField that can be used for defining a relationship to a LookerField
     */
    public static LookerField refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return LookerField._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a LookerField by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the LookerField to retrieve, either its GUID or its full qualifiedName
     * @return the requested full LookerField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerField does not exist or the provided GUID is not a LookerField
     */
    @JsonIgnore
    public static LookerField get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a LookerField by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the LookerField to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full LookerField, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerField does not exist or the provided GUID is not a LookerField
     */
    @JsonIgnore
    public static LookerField get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof LookerField) {
                return (LookerField) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof LookerField) {
                return (LookerField) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a LookerField by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the LookerField to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the LookerField, including any relationships
     * @return the requested LookerField, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerField does not exist or the provided GUID is not a LookerField
     */
    @JsonIgnore
    public static LookerField get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a LookerField by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the LookerField to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the LookerField, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the LookerField
     * @return the requested LookerField, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerField does not exist or the provided GUID is not a LookerField
     */
    @JsonIgnore
    public static LookerField get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = LookerField.select(client)
                    .where(LookerField.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof LookerField) {
                return (LookerField) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = LookerField.select(client)
                    .where(LookerField.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof LookerField) {
                return (LookerField) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) LookerField to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the LookerField
     * @return true if the LookerField is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a LookerField.
     *
     * @param qualifiedName of the LookerField
     * @param name of the LookerField
     * @return the minimal request necessary to update the LookerField, as a builder
     */
    public static LookerFieldBuilder<?, ?> updater(String qualifiedName, String name) {
        return LookerField._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a LookerField, from a potentially
     * more-complete LookerField object.
     *
     * @return the minimal object necessary to update the LookerField, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for LookerField are not found in the initial object
     */
    @Override
    public LookerFieldBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class LookerFieldBuilder<C extends LookerField, B extends LookerFieldBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a LookerField.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the LookerField
     * @param name of the LookerField
     * @return the updated LookerField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerField removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LookerField) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a LookerField.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the LookerField
     * @param name of the LookerField
     * @return the updated LookerField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerField removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LookerField) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a LookerField.
     *
     * @param client connectivity to the Atlan tenant from which to remove the LookerField's owners
     * @param qualifiedName of the LookerField
     * @param name of the LookerField
     * @return the updated LookerField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerField removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LookerField) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a LookerField.
     *
     * @param client connectivity to the Atlan tenant on which to update the LookerField's certificate
     * @param qualifiedName of the LookerField
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated LookerField, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerField updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (LookerField)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a LookerField.
     *
     * @param client connectivity to the Atlan tenant from which to remove the LookerField's certificate
     * @param qualifiedName of the LookerField
     * @param name of the LookerField
     * @return the updated LookerField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerField removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LookerField) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a LookerField.
     *
     * @param client connectivity to the Atlan tenant on which to update the LookerField's announcement
     * @param qualifiedName of the LookerField
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerField updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (LookerField)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a LookerField.
     *
     * @param client connectivity to the Atlan client from which to remove the LookerField's announcement
     * @param qualifiedName of the LookerField
     * @param name of the LookerField
     * @return the updated LookerField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerField removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LookerField) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the LookerField.
     *
     * @param client connectivity to the Atlan tenant on which to replace the LookerField's assigned terms
     * @param qualifiedName for the LookerField
     * @param name human-readable name of the LookerField
     * @param terms the list of terms to replace on the LookerField, or null to remove all terms from the LookerField
     * @return the LookerField that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static LookerField replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (LookerField) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the LookerField, without replacing existing terms linked to the LookerField.
     * Note: this operation must make two API calls — one to retrieve the LookerField's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the LookerField
     * @param qualifiedName for the LookerField
     * @param terms the list of terms to append to the LookerField
     * @return the LookerField that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static LookerField appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (LookerField) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a LookerField, without replacing all existing terms linked to the LookerField.
     * Note: this operation must make two API calls — one to retrieve the LookerField's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the LookerField
     * @param qualifiedName for the LookerField
     * @param terms the list of terms to remove from the LookerField, which must be referenced by GUID
     * @return the LookerField that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static LookerField removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (LookerField) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a LookerField, without replacing existing Atlan tags linked to the LookerField.
     * Note: this operation must make two API calls — one to retrieve the LookerField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the LookerField
     * @param qualifiedName of the LookerField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated LookerField
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static LookerField appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (LookerField) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a LookerField, without replacing existing Atlan tags linked to the LookerField.
     * Note: this operation must make two API calls — one to retrieve the LookerField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the LookerField
     * @param qualifiedName of the LookerField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated LookerField
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static LookerField appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (LookerField) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a LookerField.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a LookerField
     * @param qualifiedName of the LookerField
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the LookerField
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
