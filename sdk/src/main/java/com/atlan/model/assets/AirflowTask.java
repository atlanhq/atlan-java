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
import com.atlan.model.enums.OpenLineageRunState;
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
 * Instance of an Airflow task in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class AirflowTask extends Asset implements IAirflowTask, IAirflow, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AirflowTask";

    /** Fixed typeName for AirflowTasks. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** DAG in which this task exists. */
    @Attribute
    IAirflowDag airflowDag;

    /** Simple name of the DAG this task is contained within. */
    @Attribute
    String airflowDagName;

    /** Unique name of the DAG this task is contained within. */
    @Attribute
    String airflowDagQualifiedName;

    /** End time of the run. */
    @Attribute
    @Date
    Long airflowRunEndTime;

    /** Name of the run. */
    @Attribute
    String airflowRunName;

    /** State of the run in OpenLineage. */
    @Attribute
    OpenLineageRunState airflowRunOpenLineageState;

    /** Version of the run in OpenLineage. */
    @Attribute
    String airflowRunOpenLineageVersion;

    /** Start time of the run. */
    @Attribute
    @Date
    Long airflowRunStartTime;

    /** Type of the run. */
    @Attribute
    String airflowRunType;

    /** Version of the run in Airflow. */
    @Attribute
    String airflowRunVersion;

    /** Tags assigned to the asset in Airflow. */
    @Attribute
    @Singular
    SortedSet<String> airflowTags;

    /** Identifier for the connection this task accesses. */
    @Attribute
    String airflowTaskConnectionId;

    /** Group name for the task. */
    @Attribute
    String airflowTaskGroupName;

    /** Class name for the operator this task uses. */
    @Attribute
    String airflowTaskOperatorClass;

    /** Pool on which this run happened. */
    @Attribute
    String airflowTaskPool;

    /** Pool slots used for the run. */
    @Attribute
    Long airflowTaskPoolSlots;

    /** Priority of the run. */
    @Attribute
    Long airflowTaskPriorityWeight;

    /** Queue on which this run happened. */
    @Attribute
    String airflowTaskQueue;

    /** Retry count for this task running. */
    @Attribute
    Long airflowTaskRetryNumber;

    /** SQL code that executes through this task. */
    @Attribute
    String airflowTaskSql;

    /** Trigger for the run. */
    @Attribute
    String airflowTaskTriggerRule;

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

    /** Process in which this task exists. */
    @Attribute
    ILineageProcess process;

    /** Spark assets that are executed by this airflow asset. */
    @Attribute
    @Singular
    SortedSet<ISpark> sparkOrchestratedAssets;

    /**
     * Builds the minimal object necessary to create a relationship to a AirflowTask, from a potentially
     * more-complete AirflowTask object.
     *
     * @return the minimal object necessary to relate to the AirflowTask
     * @throws InvalidRequestException if any of the minimal set of required properties for a AirflowTask relationship are not found in the initial object
     */
    @Override
    public AirflowTask trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all AirflowTask assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) AirflowTask assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all AirflowTask assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all AirflowTask assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) AirflowTasks will be included
     * @return a fluent search that includes all AirflowTask assets
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
     * Reference to a AirflowTask by GUID. Use this to create a relationship to this AirflowTask,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the AirflowTask to reference
     * @return reference to a AirflowTask that can be used for defining a relationship to a AirflowTask
     */
    public static AirflowTask refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AirflowTask by GUID. Use this to create a relationship to this AirflowTask,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the AirflowTask to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AirflowTask that can be used for defining a relationship to a AirflowTask
     */
    public static AirflowTask refByGuid(String guid, Reference.SaveSemantic semantic) {
        return AirflowTask._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a AirflowTask by qualifiedName. Use this to create a relationship to this AirflowTask,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the AirflowTask to reference
     * @return reference to a AirflowTask that can be used for defining a relationship to a AirflowTask
     */
    public static AirflowTask refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AirflowTask by qualifiedName. Use this to create a relationship to this AirflowTask,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the AirflowTask to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AirflowTask that can be used for defining a relationship to a AirflowTask
     */
    public static AirflowTask refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return AirflowTask._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a AirflowTask by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AirflowTask to retrieve, either its GUID or its full qualifiedName
     * @return the requested full AirflowTask, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AirflowTask does not exist or the provided GUID is not a AirflowTask
     */
    @JsonIgnore
    public static AirflowTask get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a AirflowTask by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AirflowTask to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full AirflowTask, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AirflowTask does not exist or the provided GUID is not a AirflowTask
     */
    @JsonIgnore
    public static AirflowTask get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof AirflowTask) {
                return (AirflowTask) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof AirflowTask) {
                return (AirflowTask) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a AirflowTask by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AirflowTask to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AirflowTask, including any relationships
     * @return the requested AirflowTask, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AirflowTask does not exist or the provided GUID is not a AirflowTask
     */
    @JsonIgnore
    public static AirflowTask get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a AirflowTask by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AirflowTask to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AirflowTask, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the AirflowTask
     * @return the requested AirflowTask, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AirflowTask does not exist or the provided GUID is not a AirflowTask
     */
    @JsonIgnore
    public static AirflowTask get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = AirflowTask.select(client)
                    .where(AirflowTask.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof AirflowTask) {
                return (AirflowTask) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = AirflowTask.select(client)
                    .where(AirflowTask.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof AirflowTask) {
                return (AirflowTask) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) AirflowTask to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the AirflowTask
     * @return true if the AirflowTask is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create an AirflowTask.
     *
     * @param name of the AirflowTask
     * @param airflowDag in which the AirflowTask should be created, which must have at least
     *                   a qualifiedName
     * @return the minimal request necessary to create the AirflowTask, as a builder
     * @throws InvalidRequestException if the AirflowDag provided is without a qualifiedName
     */
    public static AirflowTaskBuilder<?, ?> creator(String name, AirflowDag airflowDag) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", airflowDag.getQualifiedName());
        validateRelationship(AirflowDag.TYPE_NAME, map);
        return creator(name, airflowDag.getQualifiedName()).airflowDag(airflowDag.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create an AirflowTask.
     *
     * @param name of the AirflowTask
     * @param airflowDagQualifiedName unique name of the DAG through which the task is accessible
     * @return the minimal object necessary to create the AirflowTask, as a builder
     */
    public static AirflowTaskBuilder<?, ?> creator(String name, String airflowDagQualifiedName) {
        String[] tokens = airflowDagQualifiedName.split("/");
        String airflowDagName = StringUtils.getNameFromQualifiedName(airflowDagQualifiedName);
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(airflowDagQualifiedName);
        return AirflowTask._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(airflowDagQualifiedName + "/" + name)
                .name(name)
                .airflowDagQualifiedName(airflowDagQualifiedName)
                .airflowDagName(airflowDagName)
                .airflowDag(AirflowDag.refByQualifiedName(airflowDagQualifiedName))
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a AirflowTask.
     *
     * @param qualifiedName of the AirflowTask
     * @param name of the AirflowTask
     * @return the minimal request necessary to update the AirflowTask, as a builder
     */
    public static AirflowTaskBuilder<?, ?> updater(String qualifiedName, String name) {
        return AirflowTask._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AirflowTask, from a potentially
     * more-complete AirflowTask object.
     *
     * @return the minimal object necessary to update the AirflowTask, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AirflowTask are not found in the initial object
     */
    @Override
    public AirflowTaskBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class AirflowTaskBuilder<C extends AirflowTask, B extends AirflowTaskBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a AirflowTask.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AirflowTask
     * @param name of the AirflowTask
     * @return the updated AirflowTask, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AirflowTask removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AirflowTask) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a AirflowTask.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AirflowTask
     * @param name of the AirflowTask
     * @return the updated AirflowTask, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AirflowTask removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AirflowTask) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a AirflowTask.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AirflowTask's owners
     * @param qualifiedName of the AirflowTask
     * @param name of the AirflowTask
     * @return the updated AirflowTask, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AirflowTask removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AirflowTask) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a AirflowTask.
     *
     * @param client connectivity to the Atlan tenant on which to update the AirflowTask's certificate
     * @param qualifiedName of the AirflowTask
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AirflowTask, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AirflowTask updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (AirflowTask)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a AirflowTask.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AirflowTask's certificate
     * @param qualifiedName of the AirflowTask
     * @param name of the AirflowTask
     * @return the updated AirflowTask, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AirflowTask removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AirflowTask) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a AirflowTask.
     *
     * @param client connectivity to the Atlan tenant on which to update the AirflowTask's announcement
     * @param qualifiedName of the AirflowTask
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AirflowTask updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (AirflowTask)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a AirflowTask.
     *
     * @param client connectivity to the Atlan client from which to remove the AirflowTask's announcement
     * @param qualifiedName of the AirflowTask
     * @param name of the AirflowTask
     * @return the updated AirflowTask, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AirflowTask removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AirflowTask) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the AirflowTask.
     *
     * @param client connectivity to the Atlan tenant on which to replace the AirflowTask's assigned terms
     * @param qualifiedName for the AirflowTask
     * @param name human-readable name of the AirflowTask
     * @param terms the list of terms to replace on the AirflowTask, or null to remove all terms from the AirflowTask
     * @return the AirflowTask that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static AirflowTask replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (AirflowTask) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the AirflowTask, without replacing existing terms linked to the AirflowTask.
     * Note: this operation must make two API calls — one to retrieve the AirflowTask's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the AirflowTask
     * @param qualifiedName for the AirflowTask
     * @param terms the list of terms to append to the AirflowTask
     * @return the AirflowTask that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static AirflowTask appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AirflowTask) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a AirflowTask, without replacing all existing terms linked to the AirflowTask.
     * Note: this operation must make two API calls — one to retrieve the AirflowTask's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the AirflowTask
     * @param qualifiedName for the AirflowTask
     * @param terms the list of terms to remove from the AirflowTask, which must be referenced by GUID
     * @return the AirflowTask that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static AirflowTask removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AirflowTask) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a AirflowTask, without replacing existing Atlan tags linked to the AirflowTask.
     * Note: this operation must make two API calls — one to retrieve the AirflowTask's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AirflowTask
     * @param qualifiedName of the AirflowTask
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated AirflowTask
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static AirflowTask appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (AirflowTask) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AirflowTask, without replacing existing Atlan tags linked to the AirflowTask.
     * Note: this operation must make two API calls — one to retrieve the AirflowTask's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AirflowTask
     * @param qualifiedName of the AirflowTask
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated AirflowTask
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static AirflowTask appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (AirflowTask) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a AirflowTask.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a AirflowTask
     * @param qualifiedName of the AirflowTask
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the AirflowTask
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
