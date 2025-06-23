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
 * An operation that uses at least one data asset as input and produces at least one data asset as output.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class ETLDataFlowOperation extends Asset
        implements IETLDataFlowOperation, ILineageProcess, IETL, IAsset, IReferenceable, ICatalog {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ETLDataFlowOperation";

    /** Fixed typeName for ETLDataFlowOperations. */
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

    /** Child data flow operations that show internal details of this data flow operation. */
    @Attribute
    @Singular
    SortedSet<IETLDataFlowOperation> etlDetailedDataFlowOperations;

    /** Executable units that result in creating this process. */
    @Attribute
    @Singular
    SortedSet<IETLExecutableUnit> etlExecutableUnits;

    /** Date and time at which this point in the data processing or orchestration finished. */
    @Attribute
    @Date
    Long etlFinishedAt;

    /** Parent data flow operation that contains this data flow operation. */
    @Attribute
    IETLDataFlowOperation etlParentDataFlowOperation;

    /** Process that contains this data flow operation. */
    @Attribute
    ILineageProcess etlParentProcess;

    /** Schedule for this point in the data processing or orchestration. */
    @Attribute
    String etlSchedule;

    /** Date and time at which this point in the data processing or orchestration started. */
    @Attribute
    @Date
    Long etlStartedAt;

    /** Overall status of this point in the data processing or orchestration. */
    @Attribute
    String etlStatus;

    /** fivetranConnector in which this process exists. */
    @Attribute
    IFivetranConnector fivetranConnector;

    /** Grouping of data flows (processes) that contains this individual data flow (process). */
    @Attribute
    IFlowProcessGrouping flowGrouping;

    /** Grouping of data flows (processes) that contains this individual data flow (process). */
    @Attribute
    IFlowV02ProcessGrouping flowV02Grouping;

    /** Grouping of data flows (processes) that contains this individual data flow (process). */
    @Attribute
    IFlowV03ProcessGrouping flowV03Grouping;

    /** Orchestrated control operation that ran these data flows (process). */
    @Attribute
    IFlowV03ControlOperation flowV03OrchestratedBy;

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

    /** Assets that are inputs to this task. */
    @Attribute
    @Singular
    SortedSet<ICatalog> inputs;

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

    /** Assets that are outputs from this task. */
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

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISparkJob> sparkJobs;

    /** SQL query that ran to produce the outputs. */
    @Attribute
    String sql;

    /**
     * Builds the minimal object necessary to create a relationship to a ETLDataFlowOperation, from a potentially
     * more-complete ETLDataFlowOperation object.
     *
     * @return the minimal object necessary to relate to the ETLDataFlowOperation
     * @throws InvalidRequestException if any of the minimal set of required properties for a ETLDataFlowOperation relationship are not found in the initial object
     */
    @Override
    public ETLDataFlowOperation trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all ETLDataFlowOperation assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ETLDataFlowOperation assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all ETLDataFlowOperation assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all ETLDataFlowOperation assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ETLDataFlowOperations will be included
     * @return a fluent search that includes all ETLDataFlowOperation assets
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
     * Reference to a ETLDataFlowOperation by GUID. Use this to create a relationship to this ETLDataFlowOperation,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the ETLDataFlowOperation to reference
     * @return reference to a ETLDataFlowOperation that can be used for defining a relationship to a ETLDataFlowOperation
     */
    public static ETLDataFlowOperation refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ETLDataFlowOperation by GUID. Use this to create a relationship to this ETLDataFlowOperation,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the ETLDataFlowOperation to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ETLDataFlowOperation that can be used for defining a relationship to a ETLDataFlowOperation
     */
    public static ETLDataFlowOperation refByGuid(String guid, Reference.SaveSemantic semantic) {
        return ETLDataFlowOperation._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a ETLDataFlowOperation by qualifiedName. Use this to create a relationship to this ETLDataFlowOperation,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the ETLDataFlowOperation to reference
     * @return reference to a ETLDataFlowOperation that can be used for defining a relationship to a ETLDataFlowOperation
     */
    public static ETLDataFlowOperation refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ETLDataFlowOperation by qualifiedName. Use this to create a relationship to this ETLDataFlowOperation,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the ETLDataFlowOperation to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ETLDataFlowOperation that can be used for defining a relationship to a ETLDataFlowOperation
     */
    public static ETLDataFlowOperation refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return ETLDataFlowOperation._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a ETLDataFlowOperation by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ETLDataFlowOperation to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ETLDataFlowOperation, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ETLDataFlowOperation does not exist or the provided GUID is not a ETLDataFlowOperation
     */
    @JsonIgnore
    public static ETLDataFlowOperation get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a ETLDataFlowOperation by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ETLDataFlowOperation to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full ETLDataFlowOperation, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ETLDataFlowOperation does not exist or the provided GUID is not a ETLDataFlowOperation
     */
    @JsonIgnore
    public static ETLDataFlowOperation get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof ETLDataFlowOperation) {
                return (ETLDataFlowOperation) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof ETLDataFlowOperation) {
                return (ETLDataFlowOperation) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a ETLDataFlowOperation by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ETLDataFlowOperation to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the ETLDataFlowOperation, including any relationships
     * @return the requested ETLDataFlowOperation, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ETLDataFlowOperation does not exist or the provided GUID is not a ETLDataFlowOperation
     */
    @JsonIgnore
    public static ETLDataFlowOperation get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a ETLDataFlowOperation by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ETLDataFlowOperation to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the ETLDataFlowOperation, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the ETLDataFlowOperation
     * @return the requested ETLDataFlowOperation, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ETLDataFlowOperation does not exist or the provided GUID is not a ETLDataFlowOperation
     */
    @JsonIgnore
    public static ETLDataFlowOperation get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = ETLDataFlowOperation.select(client)
                    .where(ETLDataFlowOperation.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof ETLDataFlowOperation) {
                return (ETLDataFlowOperation) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = ETLDataFlowOperation.select(client)
                    .where(ETLDataFlowOperation.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof ETLDataFlowOperation) {
                return (ETLDataFlowOperation) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) ETLDataFlowOperation to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the ETLDataFlowOperation
     * @return true if the ETLDataFlowOperation is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a ETLDataFlowOperation.
     *
     * @param qualifiedName of the ETLDataFlowOperation
     * @param name of the ETLDataFlowOperation
     * @return the minimal request necessary to update the ETLDataFlowOperation, as a builder
     */
    public static ETLDataFlowOperationBuilder<?, ?> updater(String qualifiedName, String name) {
        return ETLDataFlowOperation._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ETLDataFlowOperation, from a potentially
     * more-complete ETLDataFlowOperation object.
     *
     * @return the minimal object necessary to update the ETLDataFlowOperation, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ETLDataFlowOperation are not found in the initial object
     */
    @Override
    public ETLDataFlowOperationBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ETLDataFlowOperationBuilder<
                    C extends ETLDataFlowOperation, B extends ETLDataFlowOperationBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a ETLDataFlowOperation.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ETLDataFlowOperation
     * @param name of the ETLDataFlowOperation
     * @return the updated ETLDataFlowOperation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ETLDataFlowOperation removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ETLDataFlowOperation) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ETLDataFlowOperation.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ETLDataFlowOperation
     * @param name of the ETLDataFlowOperation
     * @return the updated ETLDataFlowOperation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ETLDataFlowOperation removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ETLDataFlowOperation) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ETLDataFlowOperation.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ETLDataFlowOperation's owners
     * @param qualifiedName of the ETLDataFlowOperation
     * @param name of the ETLDataFlowOperation
     * @return the updated ETLDataFlowOperation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ETLDataFlowOperation removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ETLDataFlowOperation) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ETLDataFlowOperation.
     *
     * @param client connectivity to the Atlan tenant on which to update the ETLDataFlowOperation's certificate
     * @param qualifiedName of the ETLDataFlowOperation
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ETLDataFlowOperation, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ETLDataFlowOperation updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (ETLDataFlowOperation)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ETLDataFlowOperation.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ETLDataFlowOperation's certificate
     * @param qualifiedName of the ETLDataFlowOperation
     * @param name of the ETLDataFlowOperation
     * @return the updated ETLDataFlowOperation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ETLDataFlowOperation removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ETLDataFlowOperation) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ETLDataFlowOperation.
     *
     * @param client connectivity to the Atlan tenant on which to update the ETLDataFlowOperation's announcement
     * @param qualifiedName of the ETLDataFlowOperation
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ETLDataFlowOperation updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (ETLDataFlowOperation)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ETLDataFlowOperation.
     *
     * @param client connectivity to the Atlan client from which to remove the ETLDataFlowOperation's announcement
     * @param qualifiedName of the ETLDataFlowOperation
     * @param name of the ETLDataFlowOperation
     * @return the updated ETLDataFlowOperation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ETLDataFlowOperation removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ETLDataFlowOperation) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the ETLDataFlowOperation.
     *
     * @param client connectivity to the Atlan tenant on which to replace the ETLDataFlowOperation's assigned terms
     * @param qualifiedName for the ETLDataFlowOperation
     * @param name human-readable name of the ETLDataFlowOperation
     * @param terms the list of terms to replace on the ETLDataFlowOperation, or null to remove all terms from the ETLDataFlowOperation
     * @return the ETLDataFlowOperation that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ETLDataFlowOperation replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (ETLDataFlowOperation) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ETLDataFlowOperation, without replacing existing terms linked to the ETLDataFlowOperation.
     * Note: this operation must make two API calls — one to retrieve the ETLDataFlowOperation's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the ETLDataFlowOperation
     * @param qualifiedName for the ETLDataFlowOperation
     * @param terms the list of terms to append to the ETLDataFlowOperation
     * @return the ETLDataFlowOperation that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static ETLDataFlowOperation appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ETLDataFlowOperation) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ETLDataFlowOperation, without replacing all existing terms linked to the ETLDataFlowOperation.
     * Note: this operation must make two API calls — one to retrieve the ETLDataFlowOperation's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the ETLDataFlowOperation
     * @param qualifiedName for the ETLDataFlowOperation
     * @param terms the list of terms to remove from the ETLDataFlowOperation, which must be referenced by GUID
     * @return the ETLDataFlowOperation that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static ETLDataFlowOperation removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ETLDataFlowOperation) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a ETLDataFlowOperation, without replacing existing Atlan tags linked to the ETLDataFlowOperation.
     * Note: this operation must make two API calls — one to retrieve the ETLDataFlowOperation's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ETLDataFlowOperation
     * @param qualifiedName of the ETLDataFlowOperation
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ETLDataFlowOperation
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static ETLDataFlowOperation appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (ETLDataFlowOperation) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ETLDataFlowOperation, without replacing existing Atlan tags linked to the ETLDataFlowOperation.
     * Note: this operation must make two API calls — one to retrieve the ETLDataFlowOperation's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ETLDataFlowOperation
     * @param qualifiedName of the ETLDataFlowOperation
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ETLDataFlowOperation
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static ETLDataFlowOperation appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (ETLDataFlowOperation) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a ETLDataFlowOperation.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a ETLDataFlowOperation
     * @param qualifiedName of the ETLDataFlowOperation
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ETLDataFlowOperation
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
