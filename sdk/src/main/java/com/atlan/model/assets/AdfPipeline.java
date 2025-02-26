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
 * Base class for ADF Pipelines. It is a logical grouping of activities that together perform a specific data processing task.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class AdfPipeline extends Asset implements IAdfPipeline, IADF, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AdfPipeline";

    /** Fixed typeName for AdfPipelines. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** ADF Pipeline that is associated with these ADF Activities. */
    @Attribute
    @Singular
    SortedSet<IAdfActivity> adfActivities;

    /** Defines the folder path in which this ADF asset exists. */
    @Attribute
    String adfAssetFolderPath;

    /** ADF pipelines that is associated with this ADF Dataflos. */
    @Attribute
    @Singular
    SortedSet<IAdfDataflow> adfDataflows;

    /** ADF pipelines that is associated with this ADF Datasets. */
    @Attribute
    @Singular
    SortedSet<IAdfDataset> adfDatasets;

    /** Defines the name of the factory in which this asset exists. */
    @Attribute
    String adfFactoryName;

    /** ADF pipelines that is associated with this ADF Linkedservices. */
    @Attribute
    @Singular
    SortedSet<IAdfLinkedservice> adfLinkedservices;

    /** Defines the count of activities in the pipline. */
    @Attribute
    Integer adfPipelineActivityCount;

    /** The list of annotation assigned to a pipeline. */
    @Attribute
    @Singular
    SortedSet<String> adfPipelineAnnotations;

    /** List of objects of pipeline runs for a particular pipeline. */
    @Attribute
    @Singular
    List<Map<String, String>> adfPipelineRuns;

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
     * Builds the minimal object necessary to create a relationship to a AdfPipeline, from a potentially
     * more-complete AdfPipeline object.
     *
     * @return the minimal object necessary to relate to the AdfPipeline
     * @throws InvalidRequestException if any of the minimal set of required properties for a AdfPipeline relationship are not found in the initial object
     */
    @Override
    public AdfPipeline trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all AdfPipeline assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) AdfPipeline assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all AdfPipeline assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all AdfPipeline assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) AdfPipelines will be included
     * @return a fluent search that includes all AdfPipeline assets
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
     * Reference to a AdfPipeline by GUID. Use this to create a relationship to this AdfPipeline,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the AdfPipeline to reference
     * @return reference to a AdfPipeline that can be used for defining a relationship to a AdfPipeline
     */
    public static AdfPipeline refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AdfPipeline by GUID. Use this to create a relationship to this AdfPipeline,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the AdfPipeline to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AdfPipeline that can be used for defining a relationship to a AdfPipeline
     */
    public static AdfPipeline refByGuid(String guid, Reference.SaveSemantic semantic) {
        return AdfPipeline._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a AdfPipeline by qualifiedName. Use this to create a relationship to this AdfPipeline,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the AdfPipeline to reference
     * @return reference to a AdfPipeline that can be used for defining a relationship to a AdfPipeline
     */
    public static AdfPipeline refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AdfPipeline by qualifiedName. Use this to create a relationship to this AdfPipeline,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the AdfPipeline to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AdfPipeline that can be used for defining a relationship to a AdfPipeline
     */
    public static AdfPipeline refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return AdfPipeline._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a AdfPipeline by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AdfPipeline to retrieve, either its GUID or its full qualifiedName
     * @return the requested full AdfPipeline, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AdfPipeline does not exist or the provided GUID is not a AdfPipeline
     */
    @JsonIgnore
    public static AdfPipeline get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a AdfPipeline by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AdfPipeline to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full AdfPipeline, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AdfPipeline does not exist or the provided GUID is not a AdfPipeline
     */
    @JsonIgnore
    public static AdfPipeline get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof AdfPipeline) {
                return (AdfPipeline) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof AdfPipeline) {
                return (AdfPipeline) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a AdfPipeline by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AdfPipeline to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AdfPipeline, including any relationships
     * @return the requested AdfPipeline, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AdfPipeline does not exist or the provided GUID is not a AdfPipeline
     */
    @JsonIgnore
    public static AdfPipeline get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a AdfPipeline by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AdfPipeline to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AdfPipeline, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the AdfPipeline
     * @return the requested AdfPipeline, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AdfPipeline does not exist or the provided GUID is not a AdfPipeline
     */
    @JsonIgnore
    public static AdfPipeline get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = AdfPipeline.select(client)
                    .where(AdfPipeline.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof AdfPipeline) {
                return (AdfPipeline) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = AdfPipeline.select(client)
                    .where(AdfPipeline.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof AdfPipeline) {
                return (AdfPipeline) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) AdfPipeline to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the AdfPipeline
     * @return true if the AdfPipeline is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a AdfPipeline.
     *
     * @param qualifiedName of the AdfPipeline
     * @param name of the AdfPipeline
     * @return the minimal request necessary to update the AdfPipeline, as a builder
     */
    public static AdfPipelineBuilder<?, ?> updater(String qualifiedName, String name) {
        return AdfPipeline._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AdfPipeline, from a potentially
     * more-complete AdfPipeline object.
     *
     * @return the minimal object necessary to update the AdfPipeline, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AdfPipeline are not found in the initial object
     */
    @Override
    public AdfPipelineBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a AdfPipeline.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AdfPipeline
     * @param name of the AdfPipeline
     * @return the updated AdfPipeline, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AdfPipeline removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AdfPipeline) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a AdfPipeline.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AdfPipeline
     * @param name of the AdfPipeline
     * @return the updated AdfPipeline, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AdfPipeline removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AdfPipeline) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a AdfPipeline.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AdfPipeline's owners
     * @param qualifiedName of the AdfPipeline
     * @param name of the AdfPipeline
     * @return the updated AdfPipeline, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AdfPipeline removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AdfPipeline) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a AdfPipeline.
     *
     * @param client connectivity to the Atlan tenant on which to update the AdfPipeline's certificate
     * @param qualifiedName of the AdfPipeline
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AdfPipeline, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AdfPipeline updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (AdfPipeline)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a AdfPipeline.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AdfPipeline's certificate
     * @param qualifiedName of the AdfPipeline
     * @param name of the AdfPipeline
     * @return the updated AdfPipeline, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AdfPipeline removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AdfPipeline) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a AdfPipeline.
     *
     * @param client connectivity to the Atlan tenant on which to update the AdfPipeline's announcement
     * @param qualifiedName of the AdfPipeline
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AdfPipeline updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (AdfPipeline)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a AdfPipeline.
     *
     * @param client connectivity to the Atlan client from which to remove the AdfPipeline's announcement
     * @param qualifiedName of the AdfPipeline
     * @param name of the AdfPipeline
     * @return the updated AdfPipeline, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AdfPipeline removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AdfPipeline) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the AdfPipeline.
     *
     * @param client connectivity to the Atlan tenant on which to replace the AdfPipeline's assigned terms
     * @param qualifiedName for the AdfPipeline
     * @param name human-readable name of the AdfPipeline
     * @param terms the list of terms to replace on the AdfPipeline, or null to remove all terms from the AdfPipeline
     * @return the AdfPipeline that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static AdfPipeline replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (AdfPipeline) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the AdfPipeline, without replacing existing terms linked to the AdfPipeline.
     * Note: this operation must make two API calls — one to retrieve the AdfPipeline's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the AdfPipeline
     * @param qualifiedName for the AdfPipeline
     * @param terms the list of terms to append to the AdfPipeline
     * @return the AdfPipeline that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static AdfPipeline appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AdfPipeline) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a AdfPipeline, without replacing all existing terms linked to the AdfPipeline.
     * Note: this operation must make two API calls — one to retrieve the AdfPipeline's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the AdfPipeline
     * @param qualifiedName for the AdfPipeline
     * @param terms the list of terms to remove from the AdfPipeline, which must be referenced by GUID
     * @return the AdfPipeline that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static AdfPipeline removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AdfPipeline) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a AdfPipeline, without replacing existing Atlan tags linked to the AdfPipeline.
     * Note: this operation must make two API calls — one to retrieve the AdfPipeline's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AdfPipeline
     * @param qualifiedName of the AdfPipeline
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated AdfPipeline
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static AdfPipeline appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (AdfPipeline) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AdfPipeline, without replacing existing Atlan tags linked to the AdfPipeline.
     * Note: this operation must make two API calls — one to retrieve the AdfPipeline's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AdfPipeline
     * @param qualifiedName of the AdfPipeline
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated AdfPipeline
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static AdfPipeline appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (AdfPipeline) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a AdfPipeline.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a AdfPipeline
     * @param qualifiedName of the AdfPipeline
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the AdfPipeline
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
