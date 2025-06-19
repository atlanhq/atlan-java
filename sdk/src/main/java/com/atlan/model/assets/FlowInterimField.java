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
 * A single field of data within a broader ephemeral dataset.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class FlowInterimField extends Asset implements IFlowInterimField, ICatalog, IFlow, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "FlowInterimField";

    /** Fixed typeName for FlowInterimFields. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Interim dataset that contains these fields. */
    @Attribute
    IFlowInterimDataset flowDataset;

    /** Optional error message of the flow run. */
    @Attribute
    String flowErrorMessage;

    /** Date and time at which this point in the data processing or orchestration finished. */
    @Attribute
    @Date
    Long flowFinishedAt;

    /** Folder in which this asset is contained. */
    @Attribute
    IFlowFolder flowFolder;

    /** Simple name of the folder in which this asset is contained. */
    @Attribute
    String flowFolderName;

    /** Unique name of the folder in which this asset is contained. */
    @Attribute
    String flowFolderQualifiedName;

    /** Unique ID for this flow asset, which will remain constant throughout the lifecycle of the asset. */
    @Attribute
    String flowId;

    /** Project, workspace or namespace in which this asset is contained. */
    @Attribute
    IFlowProject flowProject;

    /** Simple name of the project in which this asset is contained. */
    @Attribute
    String flowProjectName;

    /** Unique name of the project in which this asset is contained. */
    @Attribute
    String flowProjectQualifiedName;

    /** Unique ID of the flow run, which could change on subsequent runs of the same flow. */
    @Attribute
    String flowRunId;

    /** Schedule for this point in the data processing or orchestration. */
    @Attribute
    String flowSchedule;

    /** Date and time at which this point in the data processing or orchestration started. */
    @Attribute
    @Date
    Long flowStartedAt;

    /** Overall status of this point in the data processing or orchestration. */
    @Attribute
    String flowStatus;

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

    /**
     * Builds the minimal object necessary to create a relationship to a FlowInterimField, from a potentially
     * more-complete FlowInterimField object.
     *
     * @return the minimal object necessary to relate to the FlowInterimField
     * @throws InvalidRequestException if any of the minimal set of required properties for a FlowInterimField relationship are not found in the initial object
     */
    @Override
    public FlowInterimField trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all FlowInterimField assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) FlowInterimField assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all FlowInterimField assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all FlowInterimField assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) FlowInterimFields will be included
     * @return a fluent search that includes all FlowInterimField assets
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
     * Reference to a FlowInterimField by GUID. Use this to create a relationship to this FlowInterimField,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the FlowInterimField to reference
     * @return reference to a FlowInterimField that can be used for defining a relationship to a FlowInterimField
     */
    public static FlowInterimField refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a FlowInterimField by GUID. Use this to create a relationship to this FlowInterimField,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the FlowInterimField to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a FlowInterimField that can be used for defining a relationship to a FlowInterimField
     */
    public static FlowInterimField refByGuid(String guid, Reference.SaveSemantic semantic) {
        return FlowInterimField._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a FlowInterimField by qualifiedName. Use this to create a relationship to this FlowInterimField,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the FlowInterimField to reference
     * @return reference to a FlowInterimField that can be used for defining a relationship to a FlowInterimField
     */
    public static FlowInterimField refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a FlowInterimField by qualifiedName. Use this to create a relationship to this FlowInterimField,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the FlowInterimField to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a FlowInterimField that can be used for defining a relationship to a FlowInterimField
     */
    public static FlowInterimField refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return FlowInterimField._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a FlowInterimField by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the FlowInterimField to retrieve, either its GUID or its full qualifiedName
     * @return the requested full FlowInterimField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the FlowInterimField does not exist or the provided GUID is not a FlowInterimField
     */
    @JsonIgnore
    public static FlowInterimField get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a FlowInterimField by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the FlowInterimField to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full FlowInterimField, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the FlowInterimField does not exist or the provided GUID is not a FlowInterimField
     */
    @JsonIgnore
    public static FlowInterimField get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof FlowInterimField) {
                return (FlowInterimField) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof FlowInterimField) {
                return (FlowInterimField) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a FlowInterimField by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the FlowInterimField to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the FlowInterimField, including any relationships
     * @return the requested FlowInterimField, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the FlowInterimField does not exist or the provided GUID is not a FlowInterimField
     */
    @JsonIgnore
    public static FlowInterimField get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a FlowInterimField by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the FlowInterimField to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the FlowInterimField, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the FlowInterimField
     * @return the requested FlowInterimField, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the FlowInterimField does not exist or the provided GUID is not a FlowInterimField
     */
    @JsonIgnore
    public static FlowInterimField get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = FlowInterimField.select(client)
                    .where(FlowInterimField.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof FlowInterimField) {
                return (FlowInterimField) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = FlowInterimField.select(client)
                    .where(FlowInterimField.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof FlowInterimField) {
                return (FlowInterimField) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) FlowInterimField to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the FlowInterimField
     * @return true if the FlowInterimField is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a FlowInterimField.
     *
     * @param qualifiedName of the FlowInterimField
     * @param name of the FlowInterimField
     * @return the minimal request necessary to update the FlowInterimField, as a builder
     */
    public static FlowInterimFieldBuilder<?, ?> updater(String qualifiedName, String name) {
        return FlowInterimField._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a FlowInterimField, from a potentially
     * more-complete FlowInterimField object.
     *
     * @return the minimal object necessary to update the FlowInterimField, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for FlowInterimField are not found in the initial object
     */
    @Override
    public FlowInterimFieldBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class FlowInterimFieldBuilder<
                    C extends FlowInterimField, B extends FlowInterimFieldBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a FlowInterimField.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the FlowInterimField
     * @param name of the FlowInterimField
     * @return the updated FlowInterimField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static FlowInterimField removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (FlowInterimField) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a FlowInterimField.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the FlowInterimField
     * @param name of the FlowInterimField
     * @return the updated FlowInterimField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static FlowInterimField removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (FlowInterimField) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a FlowInterimField.
     *
     * @param client connectivity to the Atlan tenant from which to remove the FlowInterimField's owners
     * @param qualifiedName of the FlowInterimField
     * @param name of the FlowInterimField
     * @return the updated FlowInterimField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static FlowInterimField removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (FlowInterimField) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a FlowInterimField.
     *
     * @param client connectivity to the Atlan tenant on which to update the FlowInterimField's certificate
     * @param qualifiedName of the FlowInterimField
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated FlowInterimField, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static FlowInterimField updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (FlowInterimField)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a FlowInterimField.
     *
     * @param client connectivity to the Atlan tenant from which to remove the FlowInterimField's certificate
     * @param qualifiedName of the FlowInterimField
     * @param name of the FlowInterimField
     * @return the updated FlowInterimField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static FlowInterimField removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (FlowInterimField) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a FlowInterimField.
     *
     * @param client connectivity to the Atlan tenant on which to update the FlowInterimField's announcement
     * @param qualifiedName of the FlowInterimField
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static FlowInterimField updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (FlowInterimField)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a FlowInterimField.
     *
     * @param client connectivity to the Atlan client from which to remove the FlowInterimField's announcement
     * @param qualifiedName of the FlowInterimField
     * @param name of the FlowInterimField
     * @return the updated FlowInterimField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static FlowInterimField removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (FlowInterimField) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the FlowInterimField.
     *
     * @param client connectivity to the Atlan tenant on which to replace the FlowInterimField's assigned terms
     * @param qualifiedName for the FlowInterimField
     * @param name human-readable name of the FlowInterimField
     * @param terms the list of terms to replace on the FlowInterimField, or null to remove all terms from the FlowInterimField
     * @return the FlowInterimField that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static FlowInterimField replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (FlowInterimField) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the FlowInterimField, without replacing existing terms linked to the FlowInterimField.
     * Note: this operation must make two API calls — one to retrieve the FlowInterimField's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the FlowInterimField
     * @param qualifiedName for the FlowInterimField
     * @param terms the list of terms to append to the FlowInterimField
     * @return the FlowInterimField that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static FlowInterimField appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (FlowInterimField) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a FlowInterimField, without replacing all existing terms linked to the FlowInterimField.
     * Note: this operation must make two API calls — one to retrieve the FlowInterimField's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the FlowInterimField
     * @param qualifiedName for the FlowInterimField
     * @param terms the list of terms to remove from the FlowInterimField, which must be referenced by GUID
     * @return the FlowInterimField that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static FlowInterimField removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (FlowInterimField) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a FlowInterimField, without replacing existing Atlan tags linked to the FlowInterimField.
     * Note: this operation must make two API calls — one to retrieve the FlowInterimField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the FlowInterimField
     * @param qualifiedName of the FlowInterimField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated FlowInterimField
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static FlowInterimField appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (FlowInterimField) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a FlowInterimField, without replacing existing Atlan tags linked to the FlowInterimField.
     * Note: this operation must make two API calls — one to retrieve the FlowInterimField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the FlowInterimField
     * @param qualifiedName of the FlowInterimField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated FlowInterimField
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static FlowInterimField appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (FlowInterimField) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a FlowInterimField.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a FlowInterimField
     * @param qualifiedName of the FlowInterimField
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the FlowInterimField
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
