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
 * An operation that uses at least one field-level asset as input and produces at least one field-level asset as output.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class FlowV02FieldOperation extends Asset
        implements IFlowV02FieldOperation, IFlowV02, IColumnProcess, IAsset, IReferenceable, ILineageProcess {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "FlowV02FieldOperation";

    /** Fixed typeName for FlowV02FieldOperations. */
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

    /** Code that ran within the process. */
    @Attribute
    String code;

    /** Processes that detail column-level lineage for this process. */
    @Attribute
    @Singular
    SortedSet<IColumnProcess> columnProcesses;

    /** Control flow that contains this process representing a data flow between data assets. */
    @Attribute
    IETLControlFlow etlControlFlow;

    /** Data flow operations that are contained in this process. */
    @Attribute
    @Singular
    SortedSet<IETLDataFlowOperation> etlDataFlowOperations;

    /** Executable units that result in creating this process. */
    @Attribute
    @Singular
    SortedSet<IETLExecutableUnit> etlExecutableUnits;

    /** fivetranConnector in which this process exists. */
    @Attribute
    IFivetranConnector fivetranConnector;

    /** Grouping of data flows (processes) that contains this individual data flow (process). */
    @Attribute
    IFlowProcessGrouping flowGrouping;

    /** Optional error message of the flow run. */
    @Attribute
    String flowV02ErrorMessage;

    /** Date and time at which this point in the data processing or orchestration finished. */
    @Attribute
    @Date
    Long flowV02FinishedAt;

    /** Simple name of the folder in which this asset is contained. */
    @Attribute
    String flowV02FolderName;

    /** Unique name of the folder in which this asset is contained. */
    @Attribute
    String flowV02FolderQualifiedName;

    /** Grouping of data flows (processes) that contains this individual data flow (process). */
    @Attribute
    IFlowV02ProcessGrouping flowV02Grouping;

    /** Unique ID for this flow asset, which will remain constant throughout the lifecycle of the asset. */
    @Attribute
    String flowV02Id;

    /** Simple name of the project in which this asset is contained. */
    @Attribute
    String flowV02ProjectName;

    /** Unique name of the project in which this asset is contained. */
    @Attribute
    String flowV02ProjectQualifiedName;

    /** Unique ID of the flow run, which could change on subsequent runs of the same flow. */
    @Attribute
    String flowV02RunId;

    /** Schedule for this point in the data processing or orchestration. */
    @Attribute
    String flowV02Schedule;

    /** Date and time at which this point in the data processing or orchestration started. */
    @Attribute
    @Date
    Long flowV02StartedAt;

    /** Overall status of this point in the data processing or orchestration. */
    @Attribute
    String flowV02Status;

    /** Grouping of data flows (processes) that contains this individual data flow (process). */
    @Attribute
    IFlowV03ProcessGrouping flowV03Grouping;

    /** Orchestrated control operation that ran these data flows (process). */
    @Attribute
    IFlowV03ControlOperation flowV03OrchestratedBy;

    /** Assets that are inputs to this process. */
    @Attribute
    @Singular
    SortedSet<ICatalog> inputs;

    /** Matillion component that contains the logic for this lineage process. */
    @Attribute
    IMatillionComponent matillionComponent;

    /** Assets that are outputs from this process. */
    @Attribute
    @Singular
    SortedSet<ICatalog> outputs;

    /** TBC */
    @Attribute
    @Singular
    @JsonProperty("parentConnectionProcessQualifiedName")
    SortedSet<String> parentConnectionProcessQualifiedNames;

    /** PowerBI Dataflow that is associated with this lineage process. */
    @Attribute
    IPowerBIDataflow powerBIDataflow;

    /** Process in which this task exists. */
    @Attribute
    ILineageProcess process;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISparkJob> sparkJobs;

    /** SQL query that ran to produce the outputs. */
    @Attribute
    String sql;

    /**
     * Builds the minimal object necessary to create a relationship to a FlowV02FieldOperation, from a potentially
     * more-complete FlowV02FieldOperation object.
     *
     * @return the minimal object necessary to relate to the FlowV02FieldOperation
     * @throws InvalidRequestException if any of the minimal set of required properties for a FlowV02FieldOperation relationship are not found in the initial object
     */
    @Override
    public FlowV02FieldOperation trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all FlowV02FieldOperation assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) FlowV02FieldOperation assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all FlowV02FieldOperation assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all FlowV02FieldOperation assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) FlowV02FieldOperations will be included
     * @return a fluent search that includes all FlowV02FieldOperation assets
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
     * Reference to a FlowV02FieldOperation by GUID. Use this to create a relationship to this FlowV02FieldOperation,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the FlowV02FieldOperation to reference
     * @return reference to a FlowV02FieldOperation that can be used for defining a relationship to a FlowV02FieldOperation
     */
    public static FlowV02FieldOperation refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a FlowV02FieldOperation by GUID. Use this to create a relationship to this FlowV02FieldOperation,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the FlowV02FieldOperation to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a FlowV02FieldOperation that can be used for defining a relationship to a FlowV02FieldOperation
     */
    public static FlowV02FieldOperation refByGuid(String guid, Reference.SaveSemantic semantic) {
        return FlowV02FieldOperation._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a FlowV02FieldOperation by qualifiedName. Use this to create a relationship to this FlowV02FieldOperation,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the FlowV02FieldOperation to reference
     * @return reference to a FlowV02FieldOperation that can be used for defining a relationship to a FlowV02FieldOperation
     */
    public static FlowV02FieldOperation refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a FlowV02FieldOperation by qualifiedName. Use this to create a relationship to this FlowV02FieldOperation,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the FlowV02FieldOperation to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a FlowV02FieldOperation that can be used for defining a relationship to a FlowV02FieldOperation
     */
    public static FlowV02FieldOperation refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return FlowV02FieldOperation._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a FlowV02FieldOperation by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the FlowV02FieldOperation to retrieve, either its GUID or its full qualifiedName
     * @return the requested full FlowV02FieldOperation, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the FlowV02FieldOperation does not exist or the provided GUID is not a FlowV02FieldOperation
     */
    @JsonIgnore
    public static FlowV02FieldOperation get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a FlowV02FieldOperation by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the FlowV02FieldOperation to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full FlowV02FieldOperation, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the FlowV02FieldOperation does not exist or the provided GUID is not a FlowV02FieldOperation
     */
    @JsonIgnore
    public static FlowV02FieldOperation get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof FlowV02FieldOperation) {
                return (FlowV02FieldOperation) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof FlowV02FieldOperation) {
                return (FlowV02FieldOperation) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a FlowV02FieldOperation by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the FlowV02FieldOperation to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the FlowV02FieldOperation, including any relationships
     * @return the requested FlowV02FieldOperation, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the FlowV02FieldOperation does not exist or the provided GUID is not a FlowV02FieldOperation
     */
    @JsonIgnore
    public static FlowV02FieldOperation get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a FlowV02FieldOperation by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the FlowV02FieldOperation to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the FlowV02FieldOperation, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the FlowV02FieldOperation
     * @return the requested FlowV02FieldOperation, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the FlowV02FieldOperation does not exist or the provided GUID is not a FlowV02FieldOperation
     */
    @JsonIgnore
    public static FlowV02FieldOperation get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = FlowV02FieldOperation.select(client)
                    .where(FlowV02FieldOperation.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof FlowV02FieldOperation) {
                return (FlowV02FieldOperation) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = FlowV02FieldOperation.select(client)
                    .where(FlowV02FieldOperation.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof FlowV02FieldOperation) {
                return (FlowV02FieldOperation) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) FlowV02FieldOperation to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the FlowV02FieldOperation
     * @return true if the FlowV02FieldOperation is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a FlowV02FieldOperation.
     *
     * @param qualifiedName of the FlowV02FieldOperation
     * @param name of the FlowV02FieldOperation
     * @return the minimal request necessary to update the FlowV02FieldOperation, as a builder
     */
    public static FlowV02FieldOperationBuilder<?, ?> updater(String qualifiedName, String name) {
        return FlowV02FieldOperation._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a FlowV02FieldOperation, from a potentially
     * more-complete FlowV02FieldOperation object.
     *
     * @return the minimal object necessary to update the FlowV02FieldOperation, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for FlowV02FieldOperation are not found in the initial object
     */
    @Override
    public FlowV02FieldOperationBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class FlowV02FieldOperationBuilder<
                    C extends FlowV02FieldOperation, B extends FlowV02FieldOperationBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a FlowV02FieldOperation.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the FlowV02FieldOperation
     * @param name of the FlowV02FieldOperation
     * @return the updated FlowV02FieldOperation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static FlowV02FieldOperation removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (FlowV02FieldOperation) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a FlowV02FieldOperation.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the FlowV02FieldOperation
     * @param name of the FlowV02FieldOperation
     * @return the updated FlowV02FieldOperation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static FlowV02FieldOperation removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (FlowV02FieldOperation) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a FlowV02FieldOperation.
     *
     * @param client connectivity to the Atlan tenant from which to remove the FlowV02FieldOperation's owners
     * @param qualifiedName of the FlowV02FieldOperation
     * @param name of the FlowV02FieldOperation
     * @return the updated FlowV02FieldOperation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static FlowV02FieldOperation removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (FlowV02FieldOperation) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a FlowV02FieldOperation.
     *
     * @param client connectivity to the Atlan tenant on which to update the FlowV02FieldOperation's certificate
     * @param qualifiedName of the FlowV02FieldOperation
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated FlowV02FieldOperation, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static FlowV02FieldOperation updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (FlowV02FieldOperation)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a FlowV02FieldOperation.
     *
     * @param client connectivity to the Atlan tenant from which to remove the FlowV02FieldOperation's certificate
     * @param qualifiedName of the FlowV02FieldOperation
     * @param name of the FlowV02FieldOperation
     * @return the updated FlowV02FieldOperation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static FlowV02FieldOperation removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (FlowV02FieldOperation) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a FlowV02FieldOperation.
     *
     * @param client connectivity to the Atlan tenant on which to update the FlowV02FieldOperation's announcement
     * @param qualifiedName of the FlowV02FieldOperation
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static FlowV02FieldOperation updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (FlowV02FieldOperation)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a FlowV02FieldOperation.
     *
     * @param client connectivity to the Atlan client from which to remove the FlowV02FieldOperation's announcement
     * @param qualifiedName of the FlowV02FieldOperation
     * @param name of the FlowV02FieldOperation
     * @return the updated FlowV02FieldOperation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static FlowV02FieldOperation removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (FlowV02FieldOperation) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the FlowV02FieldOperation.
     *
     * @param client connectivity to the Atlan tenant on which to replace the FlowV02FieldOperation's assigned terms
     * @param qualifiedName for the FlowV02FieldOperation
     * @param name human-readable name of the FlowV02FieldOperation
     * @param terms the list of terms to replace on the FlowV02FieldOperation, or null to remove all terms from the FlowV02FieldOperation
     * @return the FlowV02FieldOperation that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static FlowV02FieldOperation replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (FlowV02FieldOperation) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the FlowV02FieldOperation, without replacing existing terms linked to the FlowV02FieldOperation.
     * Note: this operation must make two API calls — one to retrieve the FlowV02FieldOperation's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the FlowV02FieldOperation
     * @param qualifiedName for the FlowV02FieldOperation
     * @param terms the list of terms to append to the FlowV02FieldOperation
     * @return the FlowV02FieldOperation that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static FlowV02FieldOperation appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (FlowV02FieldOperation) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a FlowV02FieldOperation, without replacing all existing terms linked to the FlowV02FieldOperation.
     * Note: this operation must make two API calls — one to retrieve the FlowV02FieldOperation's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the FlowV02FieldOperation
     * @param qualifiedName for the FlowV02FieldOperation
     * @param terms the list of terms to remove from the FlowV02FieldOperation, which must be referenced by GUID
     * @return the FlowV02FieldOperation that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static FlowV02FieldOperation removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (FlowV02FieldOperation) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a FlowV02FieldOperation, without replacing existing Atlan tags linked to the FlowV02FieldOperation.
     * Note: this operation must make two API calls — one to retrieve the FlowV02FieldOperation's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the FlowV02FieldOperation
     * @param qualifiedName of the FlowV02FieldOperation
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated FlowV02FieldOperation
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static FlowV02FieldOperation appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (FlowV02FieldOperation) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a FlowV02FieldOperation, without replacing existing Atlan tags linked to the FlowV02FieldOperation.
     * Note: this operation must make two API calls — one to retrieve the FlowV02FieldOperation's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the FlowV02FieldOperation
     * @param qualifiedName of the FlowV02FieldOperation
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated FlowV02FieldOperation
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static FlowV02FieldOperation appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (FlowV02FieldOperation) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a FlowV02FieldOperation.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a FlowV02FieldOperation
     * @param qualifiedName of the FlowV02FieldOperation
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the FlowV02FieldOperation
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
