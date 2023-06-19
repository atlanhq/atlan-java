/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.api.EntityBulkEndpoint;
import com.atlan.api.EntityGuidEndpoint;
import com.atlan.api.EntityUniqueAttributesEndpoint;
import com.atlan.exception.ApiException;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.core.AssetDeletionResponse;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.core.AssetResponse;
import com.atlan.model.core.AtlanTag;
import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanDeleteType;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.SourceCostUnitType;
import com.atlan.model.relations.Reference;
import com.atlan.model.structs.PopularityInsights;
import com.atlan.net.HttpClient;
import com.atlan.serde.AssetDeserializer;
import com.atlan.serde.AssetSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for all assets.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSerialize(using = AssetSerializer.class)
@JsonDeserialize(using = AssetDeserializer.class)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "typeName",
        defaultImpl = IndistinctAsset.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Connection.class, name = Connection.TYPE_NAME),
    @JsonSubTypes.Type(value = LineageProcess.class, name = LineageProcess.TYPE_NAME),
    @JsonSubTypes.Type(value = GlossaryCategory.class, name = GlossaryCategory.TYPE_NAME),
    @JsonSubTypes.Type(value = Badge.class, name = Badge.TYPE_NAME),
    @JsonSubTypes.Type(value = AccessControl.class, name = AccessControl.TYPE_NAME),
    @JsonSubTypes.Type(value = Namespace.class, name = Namespace.TYPE_NAME),
    @JsonSubTypes.Type(value = Catalog.class, name = Catalog.TYPE_NAME),
    @JsonSubTypes.Type(value = Glossary.class, name = Glossary.TYPE_NAME),
    @JsonSubTypes.Type(value = AuthPolicy.class, name = AuthPolicy.TYPE_NAME),
    @JsonSubTypes.Type(value = GlossaryTerm.class, name = GlossaryTerm.TYPE_NAME),
    @JsonSubTypes.Type(value = AuthService.class, name = AuthService.TYPE_NAME),
    @JsonSubTypes.Type(value = Cloud.class, name = Cloud.TYPE_NAME),
})
@Slf4j
@SuppressWarnings("cast")
public abstract class Asset extends Reference {

    public static final String TYPE_NAME = "Asset";

    /** Human-readable name of the asset. */
    @Attribute
    String name;

    /** Name used for display purposes (in user interfaces). */
    @Attribute
    String displayName;

    /** Description of the asset, as crawled from a source. */
    @Attribute
    String description;

    /** Description of the asset, as provided by a user. If present, this will be used for the description in user interfaces. If not present, the description will be used. */
    @Attribute
    String userDescription;

    /** Name of the Atlan workspace in which the asset exists. */
    @Attribute
    String tenantId;

    /** Status of the asset's certification. */
    @Attribute
    CertificateStatus certificateStatus;

    /** Human-readable descriptive message that can optionally be submitted when the certificateStatus is changed. */
    @Attribute
    String certificateStatusMessage;

    /** Name of the user who last updated the certification of the asset. */
    @Attribute
    String certificateUpdatedBy;

    /** Time (epoch) at which the certification was last updated, in milliseconds. */
    @Attribute
    Long certificateUpdatedAt;

    /** Brief title for the announcement on this asset. Required when announcementType is specified. */
    @Attribute
    String announcementTitle;

    /** Detailed message to include in the announcement on this asset. */
    @Attribute
    String announcementMessage;

    /** Type of announcement on the asset. */
    @Attribute
    AtlanAnnouncementType announcementType;

    /** Time (epoch) at which the announcement was last updated, in milliseconds. */
    @Attribute
    Long announcementUpdatedAt;

    /** Name of the user who last updated the announcement. */
    @Attribute
    String announcementUpdatedBy;

    /** List of users who own the asset. */
    @Attribute
    @Singular
    SortedSet<String> ownerUsers;

    /** List of groups who own the asset. */
    @Attribute
    @Singular
    SortedSet<String> ownerGroups;

    /** List of users who administer the asset. (This is only used for Connection assets.) */
    @Attribute
    @Singular
    SortedSet<String> adminUsers;

    /** List of groups who administer the asset. (This is only used for Connection assets.) */
    @Attribute
    @Singular
    SortedSet<String> adminGroups;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> viewerUsers;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> viewerGroups;

    /** Type of the connector through which this asset is accessible. */
    @Attribute
    @JsonProperty("connectorName")
    AtlanConnectorType connectorType;

    /** TBC */
    @Attribute
    String connectionName;

    /** Unique name of the connection through which this asset is accessible. */
    @Attribute
    String connectionQualifiedName;

    /** Indicates whether this asset has lineage (true) or not. */
    @Attribute
    @JsonProperty("__hasLineage")
    Boolean hasLineage;

    /** TBC */
    @Attribute
    Boolean isDiscoverable;

    /** TBC */
    @Attribute
    Boolean isEditable;

    /** TBC */
    @Attribute
    String subType;

    /** TBC */
    @Attribute
    Double viewScore;

    /** TBC */
    @Attribute
    Double popularityScore;

    /** TBC */
    @Attribute
    String sourceOwners;

    /** Who created the asset, in the source system. */
    @Attribute
    String sourceCreatedBy;

    /** Time (epoch) at which the asset was created in the source system, in milliseconds. */
    @Attribute
    Long sourceCreatedAt;

    /** Time (epoch) at which the asset was last updated in the source system, in milliseconds. */
    @Attribute
    Long sourceUpdatedAt;

    /** Who last updated the asset in the source system. */
    @Attribute
    String sourceUpdatedBy;

    /** URL to the resource within the source application. */
    @Attribute
    String sourceURL;

    /** URL to create an embed for a resource (for example, an image of a dashboard) within Atlan. */
    @Attribute
    String sourceEmbedURL;

    /** Name of the crawler that last synchronized this asset. */
    @Attribute
    String lastSyncWorkflowName;

    /** Time (epoch) at which the asset was last crawled, in milliseconds. */
    @Attribute
    Long lastSyncRunAt;

    /** Name of the last run of the crawler that last synchronized this asset. */
    @Attribute
    String lastSyncRun;

    /** List of roles who administer the asset. (This is only used for Connection assets.) */
    @Attribute
    @Singular
    SortedSet<String> adminRoles;

    /** Total count of all read operations at source. */
    @Attribute
    Long sourceReadCount;

    /** Total number of unique users that read data from asset. */
    @Attribute
    Long sourceReadUserCount;

    /** Timestamp of most recent read operation. */
    @Attribute
    Long sourceLastReadAt;

    /** Timestamp of last operation that inserted, updated, or deleted rows. */
    @Attribute
    Long lastRowChangedAt;

    /** Total cost of all operations at source. */
    @Attribute
    Double sourceTotalCost;

    /** The unit of measure for sourceTotalCost. */
    @Attribute
    SourceCostUnitType sourceCostUnit;

    /** Total cost of read queries at source. */
    @Attribute
    Double sourceReadQueryCost;

    /** List of usernames of the most recent users who read the asset. */
    @Attribute
    @Singular
    @JsonProperty("sourceReadRecentUserList")
    SortedSet<String> sourceReadRecentUsers;

    /** List of usernames with extra insights for the most recent users who read the asset. */
    @Attribute
    @Singular
    @JsonProperty("sourceReadRecentUserRecordList")
    List<PopularityInsights> sourceReadRecentUserRecords;

    /** List of usernames of the users who read the asset the most. */
    @Attribute
    @Singular
    @JsonProperty("sourceReadTopUserList")
    SortedSet<String> sourceReadTopUsers;

    /** List of usernames with extra insights for the users who read the asset the most. */
    @Attribute
    @Singular
    @JsonProperty("sourceReadTopUserRecordList")
    List<PopularityInsights> sourceReadTopUserRecords;

    /** List of the most popular queries that accessed this asset. */
    @Attribute
    @Singular
    @JsonProperty("sourceReadPopularQueryRecordList")
    List<PopularityInsights> sourceReadPopularQueryRecords;

    /** List of the most expensive queries that accessed this asset. */
    @Attribute
    @Singular
    @JsonProperty("sourceReadExpensiveQueryRecordList")
    List<PopularityInsights> sourceReadExpensiveQueryRecords;

    /** List of the slowest queries that accessed this asset. */
    @Attribute
    @Singular
    @JsonProperty("sourceReadSlowQueryRecordList")
    List<PopularityInsights> sourceReadSlowQueryRecords;

    /** List of most expensive warehouse names. */
    @Attribute
    @Singular
    @JsonProperty("sourceQueryComputeCostList")
    SortedSet<String> sourceQueryComputeCosts;

    /** List of most expensive warehouses with extra insights. */
    @Attribute
    @Singular
    @JsonProperty("sourceQueryComputeCostRecordList")
    List<PopularityInsights> sourceQueryComputeCostRecords;

    /** TBC */
    @Attribute
    String dbtQualifiedName;

    /** TBC */
    @Attribute
    String assetDbtAlias;

    /** TBC */
    @Attribute
    String assetDbtMeta;

    /** TBC */
    @Attribute
    String assetDbtUniqueId;

    /** TBC */
    @Attribute
    String assetDbtAccountName;

    /** TBC */
    @Attribute
    String assetDbtProjectName;

    /** TBC */
    @Attribute
    String assetDbtPackageName;

    /** TBC */
    @Attribute
    String assetDbtJobName;

    /** TBC */
    @Attribute
    String assetDbtJobSchedule;

    /** TBC */
    @Attribute
    String assetDbtJobStatus;

    /** TBC */
    @Attribute
    String assetDbtJobScheduleCronHumanized;

    /** TBC */
    @Attribute
    Long assetDbtJobLastRun;

    /** TBC */
    @Attribute
    String assetDbtJobLastRunUrl;

    /** TBC */
    @Attribute
    Long assetDbtJobLastRunCreatedAt;

    /** TBC */
    @Attribute
    Long assetDbtJobLastRunUpdatedAt;

    /** TBC */
    @Attribute
    Long assetDbtJobLastRunDequedAt;

    /** TBC */
    @Attribute
    Long assetDbtJobLastRunStartedAt;

    /** TBC */
    @Attribute
    String assetDbtJobLastRunTotalDuration;

    /** TBC */
    @Attribute
    String assetDbtJobLastRunTotalDurationHumanized;

    /** TBC */
    @Attribute
    String assetDbtJobLastRunQueuedDuration;

    /** TBC */
    @Attribute
    String assetDbtJobLastRunQueuedDurationHumanized;

    /** TBC */
    @Attribute
    String assetDbtJobLastRunRunDuration;

    /** TBC */
    @Attribute
    String assetDbtJobLastRunRunDurationHumanized;

    /** TBC */
    @Attribute
    String assetDbtJobLastRunGitBranch;

    /** TBC */
    @Attribute
    String assetDbtJobLastRunGitSha;

    /** TBC */
    @Attribute
    String assetDbtJobLastRunStatusMessage;

    /** TBC */
    @Attribute
    String assetDbtJobLastRunOwnerThreadId;

    /** TBC */
    @Attribute
    String assetDbtJobLastRunExecutedByThreadId;

    /** TBC */
    @Attribute
    Boolean assetDbtJobLastRunArtifactsSaved;

    /** TBC */
    @Attribute
    String assetDbtJobLastRunArtifactS3Path;

    /** TBC */
    @Attribute
    Boolean assetDbtJobLastRunHasDocsGenerated;

    /** TBC */
    @Attribute
    Boolean assetDbtJobLastRunHasSourcesGenerated;

    /** TBC */
    @Attribute
    Boolean assetDbtJobLastRunNotificationsSent;

    /** TBC */
    @Attribute
    Long assetDbtJobNextRun;

    /** TBC */
    @Attribute
    String assetDbtJobNextRunHumanized;

    /** TBC */
    @Attribute
    String assetDbtEnvironmentName;

    /** TBC */
    @Attribute
    String assetDbtEnvironmentDbtVersion;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> assetDbtTags;

    /** TBC */
    @Attribute
    String assetDbtSemanticLayerProxyUrl;

    /** TBC */
    @Attribute
    String assetDbtSourceFreshnessCriteria;

    /** TBC */
    @Attribute
    String sampleDataUrl;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> assetTags;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> assetMcIncidentNames;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> assetMcIncidentQualifiedNames;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> assetMcMonitorNames;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> assetMcMonitorQualifiedNames;

    /** Statuses of all associated Monte Carlo monitors. */
    @Attribute
    @Singular
    SortedSet<String> assetMcMonitorStatuses;

    /** Types of all associated Monte Carlo monitors. */
    @Attribute
    @Singular
    SortedSet<String> assetMcMonitorTypes;

    /** Schedules of all associated Monte Carlo monitors. */
    @Attribute
    @Singular
    SortedSet<String> assetMcMonitorScheduleTypes;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> assetMcIncidentTypes;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> assetMcIncidentSubTypes;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> assetMcIncidentSeverities;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> assetMcIncidentStates;

    /** TBC */
    @Attribute
    Long assetMcLastSyncRunAt;

    /** TBC */
    @Attribute
    @Singular("addStarredBy")
    SortedSet<String> starredBy;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<MCMonitor> mcMonitors;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<File> files;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<MCIncident> mcIncidents;

    /** Resources that are linked to this asset. */
    @Attribute
    @Singular
    SortedSet<Link> links;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<Metric> metrics;

    /** README that is linked to this asset. */
    @Attribute
    Readme readme;

    /** Glossary terms that are linked to this asset. */
    @Attribute
    @Singular
    @JsonProperty("meanings")
    SortedSet<GlossaryTerm> assignedTerms;

    /**
     * Unique name for this asset. This is typically a concatenation of the asset's name onto its
     * parent's qualifiedName.
     */
    @Attribute
    String qualifiedName;

    /** Internal tracking of fields that should be serialized with null values. */
    @JsonIgnore
    @Singular
    transient Set<String> nullFields;

    /** Retrieve the list of fields to be serialized with null values. */
    @JsonIgnore
    public Set<String> getNullFields() {
        if (nullFields == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(nullFields);
    }

    /** Atlan tags assigned to the asset. */
    @Singular
    @JsonProperty("classifications")
    SortedSet<AtlanTag> atlanTags;

    /**
     * Map of custom metadata attributes and values defined on the asset. The map is keyed by the human-readable
     * name of the custom metadata set, and the values are a further mapping from human-readable attribute name
     * to the value for that attribute on this asset.
     */
    @Singular("customMetadata")
    Map<String, CustomMetadataAttributes> customMetadataSets;

    /** Status of the asset. */
    AtlanStatus status;

    /** User or account that created the asset. */
    final String createdBy;

    /** User or account that last updated the asset. */
    final String updatedBy;

    /** Time (epoch) at which the asset was created, in milliseconds. */
    final Long createTime;

    /** Time (epoch) at which the asset was last updated, in milliseconds. */
    final Long updateTime;

    /** Details on the handler used for deletion of the asset. */
    final String deleteHandler;

    /**
     * The names of the Atlan tags that exist on the asset. This is not always returned, even by
     * full retrieval operations. It is better to depend on the detailed values in the Atlan tags
     * property.
     * @see #atlanTags
     */
    @Deprecated
    @Singular
    @JsonProperty("classificationNames")
    SortedSet<String> atlanTagNames;

    /** Unused. */
    Boolean isIncomplete;

    /** Names of terms that have been linked to this asset. */
    @Singular
    SortedSet<String> meaningNames;

    /**
     * Details of terms that have been linked to this asset. This is not set by all API endpoints, so cannot
     * be relied upon in general, even when there are terms assigned to an asset.
     * @deprecated see {@link #assignedTerms} instead
     */
    @Singular
    @Deprecated
    SortedSet<Meaning> meanings;

    /** Unique identifiers (GUIDs) for any background tasks that are yet to operate on this asset. */
    @Singular
    final SortedSet<String> pendingTasks;

    /**
     * Reduce the asset to the minimum set of properties required to update it.
     *
     * @return a builder containing the minimal set of properties required to update this asset
     * @throws InvalidRequestException if any of the minimal set of required properties are not found in the initial object
     */
    public abstract AssetBuilder<?, ?> trimToRequired() throws InvalidRequestException;

    /**
     * If an asset with the same qualifiedName exists, updates the existing asset. Otherwise, creates the asset.
     * No Atlan tags or custom metadata will be changed if updating an existing asset, irrespective of what
     * is included in the asset itself when the method is called.
     *
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     */
    public AssetMutationResponse upsert() throws AtlanException {
        return EntityBulkEndpoint.upsert(this, false);
    }

    /**
     * If no asset exists, has the same behavior as the {@link #upsert()} method.
     * If an asset does exist, optionally overwrites any Atlan tags. Custom metadata will always
     * be entirely ignored using this method.
     *
     * @param replaceAtlanTags whether to replace Atlan tags during an update (true) or not (false)
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     */
    public AssetMutationResponse upsert(boolean replaceAtlanTags) throws AtlanException {
        return EntityBulkEndpoint.upsert(this, replaceAtlanTags);
    }

    /**
     * If no asset exists, has the same behavior as the {@link #upsert()} method, while also setting
     * any custom metadata provided.
     * If an asset does exist, optionally overwrites any Atlan tags.
     * Will merge any provided custom metadata with any custom metadata that already exists on the asset.
     *
     * @param replaceAtlanTags whether to replace AtlanTags during an update (true) or not (false)
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     */
    public AssetMutationResponse upsertMergingCM(boolean replaceAtlanTags) throws AtlanException {
        return EntityBulkEndpoint.upsertMergingCM(List.of(this), replaceAtlanTags);
    }

    /**
     * If no asset exists, has the same behavior as the {@link #upsert()} method, while also setting
     * any custom metadata provided.
     * If an asset does exist, optionally overwrites any Atlan tags.
     * Will overwrite all custom metadata on any existing asset with only the custom metadata provided
     * (wiping out any other custom metadata on an existing asset that is not provided in the request).
     *
     * @param replaceAtlanTags whether to replace Atlan tags during an update (true) or not (false)
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     */
    public AssetMutationResponse upsertReplacingCM(boolean replaceAtlanTags) throws AtlanException {
        return EntityBulkEndpoint.upsertReplacingCM(List.of(this), replaceAtlanTags);
    }

    /**
     * Retrieves an asset by its GUID, complete with all of its relationships.
     * The type of the asset will only be determined at runtime.
     *
     * @param guid of the asset to retrieve
     * @return the requested full asset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link com.atlan.exception.NotFoundException} if the asset does not exist
     */
    public static Asset retrieveFull(String guid) throws AtlanException {
        AssetResponse response = EntityGuidEndpoint.retrieve(guid, false, false);
        Asset asset = response.getAsset();
        if (asset != null) {
            asset.setCompleteObject();
        }
        return asset;
    }

    /**
     * Retrieves a minimal asset by its GUID, without its relationships.
     * The type of the asset will only be determined at runtime.
     *
     * @param guid of the asset to retrieve
     * @return the requested minimal asset, without its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link com.atlan.exception.NotFoundException} if the asset does not exist
     */
    public static Asset retrieveMinimal(String guid) throws AtlanException {
        AssetResponse response = EntityGuidEndpoint.retrieve(guid, true, true);
        return response.getAsset();
    }

    /**
     * Retrieves an asset by its qualifiedName, complete with all of its relationships.
     * The type of the asset will only be determined at runtime.
     *
     * @param typeName the type of the asset to retrieve
     * @param qualifiedName the unique name of the asset to retrieve
     * @return the requested full asset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link com.atlan.exception.NotFoundException} if the asset does not exist
     */
    protected static Asset retrieveFull(String typeName, String qualifiedName) throws AtlanException {
        AssetResponse response = EntityUniqueAttributesEndpoint.retrieve(typeName, qualifiedName, false, false);
        Asset asset = response.getAsset();
        if (asset != null) {
            asset.setCompleteObject();
        }
        return asset;
    }

    /**
     * Retrieves an asset by its qualifiedName, without its relationships.
     * The type of the asset will only be determined at runtime.
     *
     * @param typeName the type of the asset to retrieve
     * @param qualifiedName the unique name of the asset to retrieve
     * @return the requested minimal asset, without its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link com.atlan.exception.NotFoundException} if the asset does not exist
     */
    public static Asset retrieveMinimal(String typeName, String qualifiedName) throws AtlanException {
        AssetResponse response = EntityUniqueAttributesEndpoint.retrieve(typeName, qualifiedName, true, true);
        return response.getAsset();
    }

    /**
     * Soft-deletes an asset by its GUID. This operation can be reversed by updating the asset and changing
     * its {@link #status} to {@code ACTIVE}.
     *
     * @param guid of the asset to soft-delete
     * @return details of the soft-deleted asset
     * @throws AtlanException on any error during the API invocation
     */
    public static AssetDeletionResponse delete(String guid) throws AtlanException {
        return EntityBulkEndpoint.delete(guid, AtlanDeleteType.SOFT);
    }

    /**
     * Hard-deletes (purges) an asset by its GUID. This operation is irreversible.
     *
     * @param guid of the asset to hard-delete
     * @return details of the hard-deleted asset
     * @throws AtlanException on any error during the API invocation
     */
    public static AssetDeletionResponse purge(String guid) throws AtlanException {
        return EntityBulkEndpoint.delete(guid, AtlanDeleteType.PURGE);
    }

    /**
     * Update only the provided custom metadata attributes on the asset. This will leave all other custom metadata
     * attributes, even within the same named custom metadata, unchanged.
     *
     * @param guid unique identifier of the asset
     * @param cmName human-readable name of the custom metadata to update
     * @param attributes the values of the custom metadata attributes to change
     * @throws AtlanException on any API problems, or if the custom metadata is not defined in Atlan
     */
    public static void updateCustomMetadataAttributes(String guid, String cmName, CustomMetadataAttributes attributes)
            throws AtlanException {
        EntityGuidEndpoint.updateCustomMetadataAttributes(guid, cmName, attributes);
    }

    /**
     * Replace specific custom metadata on the asset. This will replace everything within the named custom metadata,
     * but will not change any of the other named custom metadata on the asset.
     *
     * @param guid unique identifier of the asset
     * @param cmName human-readable name of the custom metadata to replace
     * @param attributes the values of the attributes to replace for the custom metadata
     * @throws AtlanException on any API problems, or if the custom metadata is not defined in Atlan
     */
    public static void replaceCustomMetadata(String guid, String cmName, CustomMetadataAttributes attributes)
            throws AtlanException {
        EntityGuidEndpoint.replaceCustomMetadata(guid, cmName, attributes);
    }

    /**
     * Remove specific custom metadata from an asset.
     *
     * @param guid unique identifier of the asset
     * @param cmName human-readable name of the custom metadata to remove
     * @throws AtlanException on any API problems, or if the custom metadata is not defined in Atlan
     */
    public static void removeCustomMetadata(String guid, String cmName) throws AtlanException {
        EntityGuidEndpoint.removeCustomMetadata(guid, cmName);
    }

    /**
     * Add Atlan tags to an asset, without replacing existing Atlan tags linked to the asset.
     * Note: this operation must make two API calls — one to retrieve the asset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param typeName type of the asset
     * @param qualifiedName of the asset
     * @param atlanTagNames human-readable names of the Atlan tags to append
     * @return the asset that was updated
     * @throws AtlanException on any API problems
     */
    protected static Asset appendAtlanTags(String typeName, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(typeName, qualifiedName, atlanTagNames, true, true, false);
    }

    /**
     * Add Atlan tags to an asset, without replacing existing Atlan tags linked to the asset.
     * Note: this operation must make two API calls — one to retrieve the asset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param typeName type of the asset
     * @param qualifiedName of the asset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @return the asset that was updated
     * @throws AtlanException on any API problems
     */
    protected static Asset appendAtlanTags(
            String typeName,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {

        Asset existing = retrieveFull(typeName, qualifiedName);
        if (atlanTagNames == null) {
            return existing;
        } else if (existing != null) {
            Set<AtlanTag> replacementAtlanTags = new TreeSet<>();
            Set<AtlanTag> existingAtlanTags = existing.getAtlanTags();
            if (existingAtlanTags != null) {
                for (AtlanTag atlanTag : existingAtlanTags) {
                    if (existing.getGuid().equals(atlanTag.getEntityGuid())) {
                        // Only re-include Atlan tags that are directly assigned, and whose
                        // propagation settings are not being overridden by this update
                        // (Propagation overrides will be handled by the loop further below)
                        if (!atlanTagNames.contains(atlanTag.getTypeName())) {
                            replacementAtlanTags.add(atlanTag);
                        }
                    }
                }
            }
            // Append all the extra Atlan tags (including any propagation overrides)
            for (String atlanTagName : atlanTagNames) {
                replacementAtlanTags.add(AtlanTag.builder()
                        .typeName(atlanTagName)
                        .propagate(propagate)
                        .removePropagationsOnEntityDelete(removePropagationsOnDelete)
                        .restrictPropagationThroughLineage(restrictLineagePropagation)
                        .build());
            }
            AssetBuilder<?, ?> minimal = existing.trimToRequired();
            return replaceAtlanTags(minimal.atlanTags(replacementAtlanTags).build());
        }
        return null;
    }

    /**
     * Add Atlan tags to an asset.
     *
     * @param typeName type of the asset
     * @param qualifiedName of the asset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the asset
     * @deprecated see {@link #appendAtlanTags(String, String, List)} instead
     */
    @Deprecated
    protected static void addAtlanTags(String typeName, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        EntityUniqueAttributesEndpoint.addAtlanTags(typeName, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to an asset.
     *
     * @param typeName type of the asset
     * @param qualifiedName of the asset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the asset
     * @deprecated see {@link #appendAtlanTags(String, String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    protected static void addAtlanTags(
            String typeName,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        EntityUniqueAttributesEndpoint.addAtlanTags(
                typeName,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from an asset.
     *
     * @param typeName type of the asset
     * @param qualifiedName of the asset
     * @param atlanTagName human-readable name of the Atlan tags to remove
     * @throws AtlanException on any API problems, or if any of the Atlan tag does not exist on the asset
     */
    protected static void removeAtlanTag(String typeName, String qualifiedName, String atlanTagName)
            throws AtlanException {
        EntityUniqueAttributesEndpoint.removeAtlanTag(typeName, qualifiedName, atlanTagName, true);
    }

    /**
     * Update the certificate on an asset.
     *
     * @param builder the builder to use for updating the certificate
     * @param certificate certificate to set
     * @param message (optional) message to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    protected static Asset updateCertificate(AssetBuilder<?, ?> builder, CertificateStatus certificate, String message)
            throws AtlanException {
        builder.certificateStatus(certificate);
        if (message != null && message.length() > 1) {
            builder.certificateStatusMessage(message);
        }
        return updateAttributes(builder.build());
    }

    /**
     * Remove the certificate on an asset.
     *
     * @param builder the builder to use for removing the certificate
     * @return the result of the removal, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    protected static Asset removeCertificate(AssetBuilder<?, ?> builder) throws AtlanException {
        Asset asset = builder.removeCertificate().build();
        AssetMutationResponse response = asset.upsert();
        if (response != null && !response.getUpdatedAssets().isEmpty()) {
            return response.getUpdatedAssets().get(0);
        } else {
            return null;
        }
    }

    /**
     * Update the announcement on an asset.
     *
     * @param builder the builder to use for updating the announcement
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    protected static Asset updateAnnouncement(
            AssetBuilder<?, ?> builder, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        builder.announcementType(type);
        if (title != null && title.length() > 1) {
            builder.announcementTitle(title);
        }
        if (message != null && message.length() > 1) {
            builder.announcementMessage(message);
        }
        return updateAttributes(builder.build());
    }

    /**
     * Remove the announcement on an asset.
     *
     * @param builder the builder to use for removing the announcement
     * @return the result of the removal, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    protected static Asset removeAnnouncement(AssetBuilder<?, ?> builder) throws AtlanException {
        Asset asset = builder.removeAnnouncement().build();
        AssetMutationResponse response = asset.upsert();
        if (response != null && !response.getUpdatedAssets().isEmpty()) {
            return response.getUpdatedAssets().get(0);
        } else {
            return null;
        }
    }

    /**
     * Remove the system description from an asset.
     *
     * @param builder the builder to use for removing the description
     * @return the result of the removal, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    protected static Asset removeDescription(AssetBuilder<?, ?> builder) throws AtlanException {
        Asset asset = builder.removeDescription().build();
        AssetMutationResponse response = asset.upsert();
        if (response != null && !response.getUpdatedAssets().isEmpty()) {
            return response.getUpdatedAssets().get(0);
        } else {
            return null;
        }
    }

    /**
     * Remove the user-provided description from an asset.
     *
     * @param builder the builder to use for removing the description
     * @return the result of the removal, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    protected static Asset removeUserDescription(AssetBuilder<?, ?> builder) throws AtlanException {
        Asset asset = builder.removeUserDescription().build();
        AssetMutationResponse response = asset.upsert();
        if (response != null && !response.getUpdatedAssets().isEmpty()) {
            return response.getUpdatedAssets().get(0);
        } else {
            return null;
        }
    }

    /**
     * Remove the owners from an asset.
     *
     * @param builder the builder to use for removing the owners
     * @return the result of the removal, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    protected static Asset removeOwners(AssetBuilder<?, ?> builder) throws AtlanException {
        Asset asset = builder.removeOwners().build();
        AssetMutationResponse response = asset.upsert();
        if (response != null && !response.getUpdatedAssets().isEmpty()) {
            return response.getUpdatedAssets().get(0);
        } else {
            return null;
        }
    }

    private static Asset updateAttributes(Asset asset) throws AtlanException {
        AssetMutationResponse response = EntityBulkEndpoint.upsert(asset, false);
        if (response != null && !response.getUpdatedAssets().isEmpty()) {
            return response.getUpdatedAssets().get(0);
        }
        return null;
    }

    private static Asset replaceAtlanTags(Asset asset) throws AtlanException {
        AssetMutationResponse response = EntityBulkEndpoint.upsert(asset, true);
        if (response != null && !response.getUpdatedAssets().isEmpty()) {
            return response.getUpdatedAssets().get(0);
        }
        return null;
    }

    /**
     * Update the certificate on an asset.
     *
     * @param builder the builder to use for updating the certificate
     * @param typeName type of the asset
     * @param qualifiedName for the asset
     * @param certificate certificate to set
     * @param message (optional) message to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    protected static Asset updateCertificate(
            AssetBuilder<?, ?> builder,
            String typeName,
            String qualifiedName,
            CertificateStatus certificate,
            String message)
            throws AtlanException {
        builder.qualifiedName(qualifiedName).certificateStatus(certificate);
        if (message != null && message.length() > 1) {
            builder.certificateStatusMessage(message);
        }
        return updateAttributes(typeName, qualifiedName, builder.build());
    }

    /**
     * Update the announcement on an asset.
     *
     * @param builder the builder to use for updating the announcement
     * @param typeName type of the asset
     * @param qualifiedName for the asset
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    protected static Asset updateAnnouncement(
            AssetBuilder<?, ?> builder,
            String typeName,
            String qualifiedName,
            AtlanAnnouncementType type,
            String title,
            String message)
            throws AtlanException {
        builder.qualifiedName(qualifiedName).announcementType(type);
        if (title != null && title.length() > 1) {
            builder.announcementTitle(title);
        }
        if (message != null && message.length() > 1) {
            builder.announcementMessage(message);
        }
        return updateAttributes(typeName, qualifiedName, builder.build());
    }

    private static Asset updateAttributes(String typeName, String qualifiedName, Asset asset) throws AtlanException {
        AssetMutationResponse response =
                EntityUniqueAttributesEndpoint.updateAttributes(typeName, qualifiedName, asset);
        if (response != null && !response.getPartiallyUpdatedAssets().isEmpty()) {
            return response.getPartiallyUpdatedAssets().get(0);
        }
        if (response != null && !response.getUpdatedAssets().isEmpty()) {
            return response.getUpdatedAssets().get(0);
        }
        return null;
    }

    /**
     * Restore an archived (soft-deleted) asset to active.
     *
     * @param typeName type of the asset to restore
     * @param qualifiedName of the asset to restore
     * @return true if the asset is now restored, or false if not
     * @throws AtlanException on any API problems
     */
    protected static boolean restore(String typeName, String qualifiedName) throws AtlanException {
        try {
            return restore(typeName, qualifiedName, 0);
        } catch (InterruptedException e) {
            throw new ApiException(ErrorCode.RETRIES_INTERRUPTED, e);
        }
    }

    /**
     * Restore an archived (soft-deleted) asset to active, retrying in case it is found to
     * already be active (since the delete handlers run asynchronously).
     *
     * @param typeName type of the asset to restore
     * @param qualifiedName of the asset to restore
     * @param retryCount number of retries we have already attempted
     * @return true if the asset is now restored, or false if not
     * @throws AtlanException on any API problems
     * @throws InterruptedException if the retry cycle sleeps are interrupted
     */
    private static boolean restore(String typeName, String qualifiedName, int retryCount)
            throws AtlanException, InterruptedException {
        Asset existing = retrieveFull(typeName, qualifiedName);
        if (existing == null) {
            // Nothing to restore, so cannot be restored
            return false;
        } else if (existing.getStatus() == AtlanStatus.ACTIVE) {
            // Already active, but this could be due to the async nature of the delete handlers
            if (retryCount < Atlan.getMaxNetworkRetries()) {
                // So continue to retry up to the maximum number of allowed retries
                log.debug(
                        "Attempted to restore an active asset, retrying status check for async delete handling (attempt: {}).",
                        retryCount + 1);
                Thread.sleep(HttpClient.waitTime(retryCount).toMillis());
                return restore(typeName, qualifiedName, retryCount + 1);
            } else {
                // If we have exhausted the retries, though, then we should just short-circuit
                return true;
            }
        } else {
            Optional<String> guidRestored = restore(existing);
            return guidRestored.isPresent() && guidRestored.get().equals(existing.getGuid());
        }
    }

    /**
     * Replace the terms linked to an asset.
     *
     * @param builder the builder to use for updating the terms
     * @param terms the list of terms to replace on the asset, or null to remove all terms from an asset
     * @return the asset that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    protected static Asset replaceTerms(AssetBuilder<?, ?> builder, List<GlossaryTerm> terms) throws AtlanException {
        if (terms == null || terms.isEmpty()) {
            Asset asset = builder.removeAssignedTerms().build();
            return updateRelationships(asset);
        } else {
            return updateRelationships(builder.assignedTerms(getTermRefs(terms)).build());
        }
    }

    /**
     * Link additional terms to an asset, without replacing existing terms linked to the asset.
     * Note: this operation must make two API calls — one to retrieve the asset's existing terms,
     * and a second to append the new terms.
     *
     * @param typeName type of the asset
     * @param qualifiedName for the asset
     * @param terms the list of terms to append to the asset
     * @return the asset that was updated (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    protected static Asset appendTerms(String typeName, String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        Asset existing = retrieveFull(typeName, qualifiedName);
        if (terms == null) {
            return existing;
        } else if (existing != null) {
            Set<GlossaryTerm> replacementTerms = new TreeSet<>();
            Set<GlossaryTerm> existingTerms = existing.getAssignedTerms();
            if (existingTerms != null) {
                for (GlossaryTerm term : existingTerms) {
                    if (term.getRelationshipStatus() != AtlanStatus.DELETED) {
                        // Only re-include the terms that are not already deleted
                        replacementTerms.add(term);
                    }
                }
            }
            replacementTerms.addAll(terms);
            AssetBuilder<?, ?> minimal = existing.trimToRequired();
            return updateRelationships(
                    minimal.assignedTerms(getTermRefs(replacementTerms)).build());
        }
        return null;
    }

    /**
     * Remove terms from an asset, without replacing all existing terms linked to the asset.
     * Note: this operation must make two API calls — one to retrieve the asset's existing terms,
     * and a second to remove the provided terms.
     *
     * @param typeName type of the asset
     * @param qualifiedName for the asset
     * @param terms the list of terms to remove from the asset (note: these must be references by GUID
     *              in order to efficiently remove any existing terms)
     * @return the asset that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @throws InvalidRequestException if any of the passed terms are not valid references by GUID to a term
     */
    protected static Asset removeTerms(String typeName, String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        Asset existing = retrieveFull(typeName, qualifiedName);
        if (existing != null) {
            Set<GlossaryTerm> replacementTerms = new TreeSet<>();
            Set<GlossaryTerm> existingTerms = existing.getAssignedTerms();
            Set<String> removeGuids = new HashSet<>();
            for (GlossaryTerm term : terms) {
                if (term.isValidReferenceByGuid()) {
                    removeGuids.add(term.getGuid());
                } else {
                    throw new InvalidRequestException(ErrorCode.MISSING_TERM_GUID);
                }
            }
            for (GlossaryTerm term : existingTerms) {
                String existingTermGuid = term.getGuid();
                if (!removeGuids.contains(existingTermGuid) && term.getRelationshipStatus() != AtlanStatus.DELETED) {
                    // Only re-include the terms that we are not removing and that are not already deleted
                    replacementTerms.add(term);
                }
            }
            AssetBuilder<?, ?> minimal = existing.trimToRequired();
            Asset update;
            if (replacementTerms.isEmpty()) {
                // If there are no terms left after the removal, we need to do the same as removing all terms
                update = minimal.removeAssignedTerms().build();
            } else {
                // Otherwise we should do the update with the difference
                update = minimal.assignedTerms(getTermRefs(replacementTerms)).build();
            }
            return updateRelationships(update);
        }
        return null;
    }

    private static Collection<GlossaryTerm> getTermRefs(Collection<GlossaryTerm> terms) {
        if (terms != null && !terms.isEmpty()) {
            Set<GlossaryTerm> termRefs = new TreeSet<>();
            for (GlossaryTerm term : terms) {
                if (term.getGuid() != null) {
                    termRefs.add(GlossaryTerm.refByGuid(term.getGuid()));
                } else if (term.getQualifiedName() != null) {
                    termRefs.add(GlossaryTerm.refByQualifiedName(term.getQualifiedName()));
                }
            }
            return termRefs;
        } else {
            return Collections.emptySet();
        }
    }

    private static Asset updateRelationships(Asset asset) throws AtlanException {
        String typeNameToUpdate = asset.getTypeName();
        AssetMutationResponse response = EntityBulkEndpoint.upsert(asset, false);
        if (response != null && !response.getUpdatedAssets().isEmpty()) {
            for (Asset result : response.getUpdatedAssets()) {
                if (result.getTypeName().equals(typeNameToUpdate)) {
                    String foundQN = result.getQualifiedName();
                    if (foundQN != null && foundQN.equals(asset.getQualifiedName())) {
                        // Return the first result that matches both the type that we attempted to update
                        // and the qualifiedName of the asset we attempted to update. Irrespective of
                        // the kind of relationship, this should uniquely identify the asset that we
                        // attempted to update
                        return result;
                    }
                }
            }
        }
        return null;
    }

    private static Optional<String> restore(Asset asset) throws AtlanException {
        AssetMutationResponse response = EntityBulkEndpoint.restore(asset);
        if (response != null && !response.getGuidAssignments().isEmpty()) {
            return response.getGuidAssignments().values().stream().findFirst();
        }
        return Optional.empty();
    }

    public abstract static class AssetBuilder<C extends Asset, B extends Asset.AssetBuilder<C, B>>
            extends Reference.ReferenceBuilder<C, B> {
        /** Remove the announcement from the asset, if any is set on the asset. */
        public B removeAnnouncement() {
            nullField("announcementType");
            nullField("announcementTitle");
            nullField("announcementMessage");
            return self();
        }

        /** Remove all custom metadata from the asset, if any is set on the asset. */
        public B removeCustomMetadata() {
            // It is sufficient to simply exclude businessAttributes from a request in order
            // for them to be removed, as long as the "replaceBusinessAttributes" flag is set
            // to true (which it must be for any update to work to businessAttributes anyway)
            clearCustomMetadataSets();
            return self();
        }

        /** Remove the Atlan tags from the asset, if the asset is classified with any. */
        public B removeAtlanTags() {
            // It is sufficient to simply exclude Atlan tags from a request in order
            // for them to be removed, as long as the "replaceAtlanTags" flag is set to
            // true (which it must be for any update to work to Atlan tags anyway)
            clearAtlanTags();
            clearAtlanTagNames();
            return self();
        }

        /** Remove the system description from the asset, if any is set on the asset. */
        public B removeDescription() {
            nullField("description");
            return self();
        }

        /** Remove the user's description from the asset, if any is set on the asset. */
        public B removeUserDescription() {
            nullField("userDescription");
            return self();
        }

        /** Remove the owners from the asset, if any are set on the asset. */
        public B removeOwners() {
            nullField("ownerUsers");
            nullField("ownerGroups");
            return self();
        }

        /** Remove the certificate from the asset, if any is set on the asset. */
        public B removeCertificate() {
            nullField("certificateStatus");
            nullField("certificateStatusMessage");
            return self();
        }

        /** Remove the linked terms from the asset, if any are set on the asset. */
        public B removeAssignedTerms() {
            nullField("assignedTerms");
            return self();
        }
    }
}
