/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AdfActivityState;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for ADF Activities. It is a processing or transformation step that performs a specific task within a pipeline to manipulate or move data
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class AdfActivity extends Asset implements IAdfActivity, IADF, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AdfActivity";

    /** Fixed typeName for AdfActivitys. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Defines the batch count of activity to runs in ForEach activity. */
    @Attribute
    Integer adfActivityBatchCount;

    /** Indicates whether to import only first row only or not in Lookup activity. */
    @Attribute
    Boolean adfActivityFirstRowOnly;

    /** Indicates whether the activity processing is sequential or not inside the ForEach activity. */
    @Attribute
    Boolean adfActivityIsSequential;

    /** Defines the main class of the databricks spark activity. */
    @Attribute
    String adfActivityMainClassName;

    /** Defines the path of the notebook in the databricks notebook activity. */
    @Attribute
    String adfActivityNotebookPath;

    /** The retry interval in seconds for the ADF activity. */
    @Attribute
    Integer adfActivityPolictRetryInterval;

    /** The timout defined for the ADF activity. */
    @Attribute
    String adfActivityPolicyTimeout;

    /** The list of ADF activities on which this ADF activity depends on. */
    @Attribute
    @Singular
    @JsonProperty("adfActivityPrecedingDependency")
    SortedSet<String> adfActivityPrecedingDependencies;

    /** Defines the python file path for databricks python activity. */
    @Attribute
    String adfActivityPythonFilePath;

    /** Defines the dataflow that is to be used in dataflow activity. */
    @Attribute
    String adfActivityReferenceDataflow;

    /** List of objects of activity runs for a particular activity. */
    @Attribute
    @Singular
    List<Map<String, String>> adfActivityRuns;

    /** Defines the type of the sink of the ADF activtity. */
    @Attribute
    String adfActivitySinkType;

    /** The list of names of sinks for the ADF activity. */
    @Attribute
    @Singular
    SortedSet<String> adfActivitySinks;

    /** Defines the type of the source of the ADF activtity. */
    @Attribute
    String adfActivitySourceType;

    /** The list of names of sources for the ADF activity. */
    @Attribute
    @Singular
    SortedSet<String> adfActivitySources;

    /** Defines the state (Active or Inactive) of an ADF activity whether it is active or not. */
    @Attribute
    AdfActivityState adfActivityState;

    /** The list of activities to be run inside a ForEach activity. */
    @Attribute
    @Singular
    SortedSet<String> adfActivitySubActivities;

    /** The type of the ADF activity. */
    @Attribute
    String adfActivityType;

    /** Defines the folder path in which this ADF asset exists. */
    @Attribute
    String adfAssetFolderPath;

    /** ADF activities that are associated with this ADF Dataflow. */
    @Attribute
    IAdfDataflow adfDataflow;

    /** ADF activities that are associated with this ADF Dataset. */
    @Attribute
    @Singular
    SortedSet<IAdfDataset> adfDatasets;

    /** Defines the name of the factory in which this asset exists. */
    @Attribute
    String adfFactoryName;

    /** ADF activities that are associated with this ADF Linkedservice. */
    @Attribute
    @Singular
    SortedSet<IAdfLinkedservice> adfLinkedservices;

    /** ADF Activity that is associated with this ADF Pipeline. */
    @Attribute
    IAdfPipeline adfPipeline;

    /** Unique name of the pipeline in which this activity exists. */
    @Attribute
    String adfPipelineQualifiedName;

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

    /** Entities implemented by this asset. */
    @Attribute
    @Singular
    @JsonProperty("modelEntityImplemented")
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

    /** Lineage process that associates this ADF Activity. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> processes;

    /**
     * Builds the minimal object necessary to create a relationship to a AdfActivity, from a potentially
     * more-complete AdfActivity object.
     *
     * @return the minimal object necessary to relate to the AdfActivity
     * @throws InvalidRequestException if any of the minimal set of required properties for a AdfActivity relationship are not found in the initial object
     */
    @Override
    public AdfActivity trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all AdfActivity assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) AdfActivity assets will be included.
     *
     * @return a fluent search that includes all AdfActivity assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all AdfActivity assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) AdfActivity assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all AdfActivity assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all AdfActivity assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) AdfActivitys will be included
     * @return a fluent search that includes all AdfActivity assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all AdfActivity assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) AdfActivitys will be included
     * @return a fluent search that includes all AdfActivity assets
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
     * Reference to a AdfActivity by GUID. Use this to create a relationship to this AdfActivity,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the AdfActivity to reference
     * @return reference to a AdfActivity that can be used for defining a relationship to a AdfActivity
     */
    public static AdfActivity refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AdfActivity by GUID. Use this to create a relationship to this AdfActivity,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the AdfActivity to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AdfActivity that can be used for defining a relationship to a AdfActivity
     */
    public static AdfActivity refByGuid(String guid, Reference.SaveSemantic semantic) {
        return AdfActivity._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a AdfActivity by qualifiedName. Use this to create a relationship to this AdfActivity,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the AdfActivity to reference
     * @return reference to a AdfActivity that can be used for defining a relationship to a AdfActivity
     */
    public static AdfActivity refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AdfActivity by qualifiedName. Use this to create a relationship to this AdfActivity,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the AdfActivity to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AdfActivity that can be used for defining a relationship to a AdfActivity
     */
    public static AdfActivity refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return AdfActivity._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a AdfActivity by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the AdfActivity to retrieve, either its GUID or its full qualifiedName
     * @return the requested full AdfActivity, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AdfActivity does not exist or the provided GUID is not a AdfActivity
     */
    @JsonIgnore
    public static AdfActivity get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a AdfActivity by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AdfActivity to retrieve, either its GUID or its full qualifiedName
     * @return the requested full AdfActivity, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AdfActivity does not exist or the provided GUID is not a AdfActivity
     */
    @JsonIgnore
    public static AdfActivity get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a AdfActivity by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AdfActivity to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full AdfActivity, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AdfActivity does not exist or the provided GUID is not a AdfActivity
     */
    @JsonIgnore
    public static AdfActivity get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof AdfActivity) {
                return (AdfActivity) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof AdfActivity) {
                return (AdfActivity) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) AdfActivity to active.
     *
     * @param qualifiedName for the AdfActivity
     * @return true if the AdfActivity is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) AdfActivity to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the AdfActivity
     * @return true if the AdfActivity is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a AdfActivity.
     *
     * @param qualifiedName of the AdfActivity
     * @param name of the AdfActivity
     * @return the minimal request necessary to update the AdfActivity, as a builder
     */
    public static AdfActivityBuilder<?, ?> updater(String qualifiedName, String name) {
        return AdfActivity._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AdfActivity, from a potentially
     * more-complete AdfActivity object.
     *
     * @return the minimal object necessary to update the AdfActivity, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AdfActivity are not found in the initial object
     */
    @Override
    public AdfActivityBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a AdfActivity.
     *
     * @param qualifiedName of the AdfActivity
     * @param name of the AdfActivity
     * @return the updated AdfActivity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AdfActivity removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a AdfActivity.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AdfActivity
     * @param name of the AdfActivity
     * @return the updated AdfActivity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AdfActivity removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AdfActivity) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a AdfActivity.
     *
     * @param qualifiedName of the AdfActivity
     * @param name of the AdfActivity
     * @return the updated AdfActivity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AdfActivity removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a AdfActivity.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AdfActivity
     * @param name of the AdfActivity
     * @return the updated AdfActivity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AdfActivity removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AdfActivity) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a AdfActivity.
     *
     * @param qualifiedName of the AdfActivity
     * @param name of the AdfActivity
     * @return the updated AdfActivity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AdfActivity removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a AdfActivity.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AdfActivity's owners
     * @param qualifiedName of the AdfActivity
     * @param name of the AdfActivity
     * @return the updated AdfActivity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AdfActivity removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AdfActivity) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a AdfActivity.
     *
     * @param qualifiedName of the AdfActivity
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AdfActivity, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AdfActivity updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a AdfActivity.
     *
     * @param client connectivity to the Atlan tenant on which to update the AdfActivity's certificate
     * @param qualifiedName of the AdfActivity
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AdfActivity, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AdfActivity updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (AdfActivity)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a AdfActivity.
     *
     * @param qualifiedName of the AdfActivity
     * @param name of the AdfActivity
     * @return the updated AdfActivity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AdfActivity removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a AdfActivity.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AdfActivity's certificate
     * @param qualifiedName of the AdfActivity
     * @param name of the AdfActivity
     * @return the updated AdfActivity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AdfActivity removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AdfActivity) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a AdfActivity.
     *
     * @param qualifiedName of the AdfActivity
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AdfActivity updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a AdfActivity.
     *
     * @param client connectivity to the Atlan tenant on which to update the AdfActivity's announcement
     * @param qualifiedName of the AdfActivity
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AdfActivity updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (AdfActivity)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a AdfActivity.
     *
     * @param qualifiedName of the AdfActivity
     * @param name of the AdfActivity
     * @return the updated AdfActivity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AdfActivity removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a AdfActivity.
     *
     * @param client connectivity to the Atlan client from which to remove the AdfActivity's announcement
     * @param qualifiedName of the AdfActivity
     * @param name of the AdfActivity
     * @return the updated AdfActivity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AdfActivity removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AdfActivity) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the AdfActivity.
     *
     * @param qualifiedName for the AdfActivity
     * @param name human-readable name of the AdfActivity
     * @param terms the list of terms to replace on the AdfActivity, or null to remove all terms from the AdfActivity
     * @return the AdfActivity that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static AdfActivity replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the AdfActivity.
     *
     * @param client connectivity to the Atlan tenant on which to replace the AdfActivity's assigned terms
     * @param qualifiedName for the AdfActivity
     * @param name human-readable name of the AdfActivity
     * @param terms the list of terms to replace on the AdfActivity, or null to remove all terms from the AdfActivity
     * @return the AdfActivity that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static AdfActivity replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (AdfActivity) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the AdfActivity, without replacing existing terms linked to the AdfActivity.
     * Note: this operation must make two API calls — one to retrieve the AdfActivity's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the AdfActivity
     * @param terms the list of terms to append to the AdfActivity
     * @return the AdfActivity that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static AdfActivity appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the AdfActivity, without replacing existing terms linked to the AdfActivity.
     * Note: this operation must make two API calls — one to retrieve the AdfActivity's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the AdfActivity
     * @param qualifiedName for the AdfActivity
     * @param terms the list of terms to append to the AdfActivity
     * @return the AdfActivity that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static AdfActivity appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AdfActivity) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a AdfActivity, without replacing all existing terms linked to the AdfActivity.
     * Note: this operation must make two API calls — one to retrieve the AdfActivity's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the AdfActivity
     * @param terms the list of terms to remove from the AdfActivity, which must be referenced by GUID
     * @return the AdfActivity that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static AdfActivity removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a AdfActivity, without replacing all existing terms linked to the AdfActivity.
     * Note: this operation must make two API calls — one to retrieve the AdfActivity's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the AdfActivity
     * @param qualifiedName for the AdfActivity
     * @param terms the list of terms to remove from the AdfActivity, which must be referenced by GUID
     * @return the AdfActivity that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static AdfActivity removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AdfActivity) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a AdfActivity, without replacing existing Atlan tags linked to the AdfActivity.
     * Note: this operation must make two API calls — one to retrieve the AdfActivity's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the AdfActivity
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated AdfActivity
     */
    public static AdfActivity appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AdfActivity, without replacing existing Atlan tags linked to the AdfActivity.
     * Note: this operation must make two API calls — one to retrieve the AdfActivity's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AdfActivity
     * @param qualifiedName of the AdfActivity
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated AdfActivity
     */
    public static AdfActivity appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (AdfActivity) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AdfActivity, without replacing existing Atlan tags linked to the AdfActivity.
     * Note: this operation must make two API calls — one to retrieve the AdfActivity's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the AdfActivity
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated AdfActivity
     */
    public static AdfActivity appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return appendAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a AdfActivity, without replacing existing Atlan tags linked to the AdfActivity.
     * Note: this operation must make two API calls — one to retrieve the AdfActivity's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AdfActivity
     * @param qualifiedName of the AdfActivity
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated AdfActivity
     */
    public static AdfActivity appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (AdfActivity) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a AdfActivity.
     *
     * @param qualifiedName of the AdfActivity
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the AdfActivity
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a AdfActivity.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a AdfActivity
     * @param qualifiedName of the AdfActivity
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the AdfActivity
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}