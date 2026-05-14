/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AIDatasetType;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
 * Column-level lineage process for SAP. Subtype of ColumnProcess used by all SAP connectors so SAP-specific column-lineage attributes stay scoped to the SAP namespace.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class SAPColumnProcess extends Asset
        implements ISAPColumnProcess, IColumnProcess, ISAP, ILineageProcess, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SAPColumnProcess";

    /** Fixed typeName for SAPColumnProcesss. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Additional Context of the ETL pipeline/notebook which creates the process. */
    @Attribute
    String additionalEtlContext;

    /** ADF Activity that is associated with this lineage process. */
    @Attribute
    IAdfActivity adfActivity;

    /** Dataset type for AI Model - dataset process. */
    @Attribute
    AIDatasetType aiDatasetType;

    /** Tasks that exist within this process. */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> airflowTasks;

    /** Parsed AST of the code or SQL statements that describe the logic of this process. */
    @Attribute
    String ast;

    /** Unique identifier of the dataset this asset belongs to. */
    @Attribute
    String catalogDatasetGuid;

    /** Code that ran within the process. */
    @Attribute
    String code;

    /** Processes that detail column-level lineage for this process. */
    @Attribute
    @Singular
    SortedSet<IColumnProcess> columnProcesses;

    /** Individual Fabric activities contained in the process. */
    @Attribute
    @Singular
    SortedSet<IFabricActivity> fabricActivities;

    /** fivetranConnector in which this process exists. */
    @Attribute
    IFivetranConnector fivetranConnector;

    /** Orchestrated control operation that ran these data flows (process). */
    @Attribute
    IFlowControlOperation flowOrchestratedBy;

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

    /** Assets that are inputs to this process. */
    @Attribute
    @Singular
    SortedSet<ICatalog> inputs;

    /** Whether this process represents a pass-through data flow where data is moved without transformation, as opposed to a flow where data is actively modified. */
    @Attribute
    Boolean isPassThrough;

    /** Matillion component that contains the logic for this lineage process. */
    @Attribute
    IMatillionComponent matillionComponent;

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

    /** Assets that are outputs from this process. */
    @Attribute
    @Singular
    SortedSet<ICatalog> outputs;

    /** TBC */
    @Attribute
    @Singular
    @JsonProperty("parentConnectionProcessQualifiedName")
    SortedSet<String> parentConnectionProcessQualifiedNames;

    /** Partial fields contained in the asset. */
    @Attribute
    @Singular
    SortedSet<IPartialField> partialChildFields;

    /** Partial objects contained in the asset. */
    @Attribute
    @Singular
    SortedSet<IPartialObject> partialChildObjects;

    /** PowerBI Dataflow that is associated with this lineage process. */
    @Attribute
    IPowerBIDataflow powerBIDataflow;

    /** Parent process that contains this column-level process. */
    @Attribute
    ILineageProcess process;

    /** SAP BW Transformation whose mapping logic this column-level process represents (populated as a single-element list by the connector). */
    @Attribute
    @Singular
    SortedSet<ISAPBWTransformation> sapBwTransformations;

    /** Name of the SAP component, representing a specific functional area in SAP. */
    @Attribute
    String sapComponentName;

    /** SAP-specific data types. */
    @Attribute
    String sapDataType;

    /** Represents the total number of fields, columns, or child assets present in a given SAP asset. */
    @Attribute
    Long sapFieldCount;

    /** Indicates the sequential position of a field, column, or child asset within its parent SAP asset, starting from 1. */
    @Attribute
    Integer sapFieldOrder;

    /** Logical, business-friendly identifier for SAP data objects, aligned with business terminology and concepts. */
    @Attribute
    String sapLogicalName;

    /** Name of the SAP package, representing a logical grouping of related SAP data objects. */
    @Attribute
    String sapPackageName;

    /** Technical identifier for SAP data objects, used for integration and internal reference. */
    @Attribute
    String sapTechnicalName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISparkJob> sparkJobs;

    /** SQL query that ran to produce the outputs. */
    @Attribute
    String sql;

    /** Functions used by this process. */
    @Attribute
    @Singular
    SortedSet<IFunction> sqlFunctions;

    /** Procedures used by this process. */
    @Attribute
    @Singular
    SortedSet<IProcedure> sqlProcedures;

    /**
     * Builds the minimal object necessary to create a relationship to a SAPColumnProcess, from a potentially
     * more-complete SAPColumnProcess object.
     *
     * @return the minimal object necessary to relate to the SAPColumnProcess
     * @throws InvalidRequestException if any of the minimal set of required properties for a SAPColumnProcess relationship are not found in the initial object
     */
    @Override
    public SAPColumnProcess trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SAPColumnProcess assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SAPColumnProcess assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SAPColumnProcess assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SAPColumnProcess assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SAPColumnProcesss will be included
     * @return a fluent search that includes all SAPColumnProcess assets
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
     * Reference to a SAPColumnProcess by GUID. Use this to create a relationship to this SAPColumnProcess,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SAPColumnProcess to reference
     * @return reference to a SAPColumnProcess that can be used for defining a relationship to a SAPColumnProcess
     */
    public static SAPColumnProcess refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SAPColumnProcess by GUID. Use this to create a relationship to this SAPColumnProcess,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SAPColumnProcess to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SAPColumnProcess that can be used for defining a relationship to a SAPColumnProcess
     */
    public static SAPColumnProcess refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SAPColumnProcess._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SAPColumnProcess by qualifiedName. Use this to create a relationship to this SAPColumnProcess,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SAPColumnProcess to reference
     * @return reference to a SAPColumnProcess that can be used for defining a relationship to a SAPColumnProcess
     */
    public static SAPColumnProcess refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SAPColumnProcess by qualifiedName. Use this to create a relationship to this SAPColumnProcess,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SAPColumnProcess to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SAPColumnProcess that can be used for defining a relationship to a SAPColumnProcess
     */
    public static SAPColumnProcess refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SAPColumnProcess._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SAPColumnProcess by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SAPColumnProcess to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SAPColumnProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SAPColumnProcess does not exist or the provided GUID is not a SAPColumnProcess
     */
    @JsonIgnore
    public static SAPColumnProcess get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SAPColumnProcess by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SAPColumnProcess to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SAPColumnProcess, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SAPColumnProcess does not exist or the provided GUID is not a SAPColumnProcess
     */
    @JsonIgnore
    public static SAPColumnProcess get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SAPColumnProcess) {
                return (SAPColumnProcess) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof SAPColumnProcess) {
                return (SAPColumnProcess) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SAPColumnProcess by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SAPColumnProcess to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SAPColumnProcess, including any relationships
     * @return the requested SAPColumnProcess, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SAPColumnProcess does not exist or the provided GUID is not a SAPColumnProcess
     */
    @JsonIgnore
    public static SAPColumnProcess get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a SAPColumnProcess by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SAPColumnProcess to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SAPColumnProcess, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the SAPColumnProcess
     * @return the requested SAPColumnProcess, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SAPColumnProcess does not exist or the provided GUID is not a SAPColumnProcess
     */
    @JsonIgnore
    public static SAPColumnProcess get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = SAPColumnProcess.select(client)
                    .where(SAPColumnProcess.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof SAPColumnProcess) {
                return (SAPColumnProcess) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = SAPColumnProcess.select(client)
                    .where(SAPColumnProcess.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof SAPColumnProcess) {
                return (SAPColumnProcess) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SAPColumnProcess to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SAPColumnProcess
     * @return true if the SAPColumnProcess is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SAPColumnProcess.
     *
     * @param qualifiedName of the SAPColumnProcess
     * @param name of the SAPColumnProcess
     * @return the minimal request necessary to update the SAPColumnProcess, as a builder
     */
    public static SAPColumnProcessBuilder<?, ?> updater(String qualifiedName, String name) {
        return SAPColumnProcess._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SAPColumnProcess,
     * from a potentially more-complete SAPColumnProcess object.
     *
     * @return the minimal object necessary to update the SAPColumnProcess, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a SAPColumnProcess are not present in the initial object
     */
    @Override
    public SAPColumnProcessBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class SAPColumnProcessBuilder<
                    C extends SAPColumnProcess, B extends SAPColumnProcessBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a SAPColumnProcess.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SAPColumnProcess
     * @param name of the SAPColumnProcess
     * @return the updated SAPColumnProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SAPColumnProcess removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SAPColumnProcess) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SAPColumnProcess.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SAPColumnProcess
     * @param name of the SAPColumnProcess
     * @return the updated SAPColumnProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SAPColumnProcess removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SAPColumnProcess) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SAPColumnProcess.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SAPColumnProcess's owners
     * @param qualifiedName of the SAPColumnProcess
     * @param name of the SAPColumnProcess
     * @return the updated SAPColumnProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SAPColumnProcess removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SAPColumnProcess) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SAPColumnProcess.
     *
     * @param client connectivity to the Atlan tenant on which to update the SAPColumnProcess's certificate
     * @param qualifiedName of the SAPColumnProcess
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SAPColumnProcess, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SAPColumnProcess updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SAPColumnProcess)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SAPColumnProcess.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SAPColumnProcess's certificate
     * @param qualifiedName of the SAPColumnProcess
     * @param name of the SAPColumnProcess
     * @return the updated SAPColumnProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SAPColumnProcess removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SAPColumnProcess) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SAPColumnProcess.
     *
     * @param client connectivity to the Atlan tenant on which to update the SAPColumnProcess's announcement
     * @param qualifiedName of the SAPColumnProcess
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SAPColumnProcess updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SAPColumnProcess)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SAPColumnProcess.
     *
     * @param client connectivity to the Atlan client from which to remove the SAPColumnProcess's announcement
     * @param qualifiedName of the SAPColumnProcess
     * @param name of the SAPColumnProcess
     * @return the updated SAPColumnProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SAPColumnProcess removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SAPColumnProcess) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SAPColumnProcess.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SAPColumnProcess's assigned terms
     * @param qualifiedName for the SAPColumnProcess
     * @param name human-readable name of the SAPColumnProcess
     * @param terms the list of terms to replace on the SAPColumnProcess, or null to remove all terms from the SAPColumnProcess
     * @return the SAPColumnProcess that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SAPColumnProcess replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SAPColumnProcess) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SAPColumnProcess, without replacing existing terms linked to the SAPColumnProcess.
     * Note: this operation must make two API calls — one to retrieve the SAPColumnProcess's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SAPColumnProcess
     * @param qualifiedName for the SAPColumnProcess
     * @param terms the list of terms to append to the SAPColumnProcess
     * @return the SAPColumnProcess that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SAPColumnProcess appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SAPColumnProcess) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SAPColumnProcess, without replacing all existing terms linked to the SAPColumnProcess.
     * Note: this operation must make two API calls — one to retrieve the SAPColumnProcess's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SAPColumnProcess
     * @param qualifiedName for the SAPColumnProcess
     * @param terms the list of terms to remove from the SAPColumnProcess, which must be referenced by GUID
     * @return the SAPColumnProcess that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SAPColumnProcess removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SAPColumnProcess) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SAPColumnProcess, without replacing existing Atlan tags linked to the SAPColumnProcess.
     * Note: this operation must make two API calls — one to retrieve the SAPColumnProcess's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SAPColumnProcess
     * @param qualifiedName of the SAPColumnProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SAPColumnProcess
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static SAPColumnProcess appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SAPColumnProcess) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SAPColumnProcess, without replacing existing Atlan tags linked to the SAPColumnProcess.
     * Note: this operation must make two API calls — one to retrieve the SAPColumnProcess's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SAPColumnProcess
     * @param qualifiedName of the SAPColumnProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SAPColumnProcess
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static SAPColumnProcess appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SAPColumnProcess) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SAPColumnProcess.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SAPColumnProcess
     * @param qualifiedName of the SAPColumnProcess
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SAPColumnProcess
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
