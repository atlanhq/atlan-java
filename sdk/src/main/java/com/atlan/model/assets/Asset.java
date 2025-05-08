/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.ApiException;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.AssetDeletionResponse;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.core.AssetResponse;
import com.atlan.model.core.AtlanTag;
import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanDeleteType;
import com.atlan.model.enums.AtlanIcon;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.SourceCostUnitType;
import com.atlan.model.lineage.FluentLineage;
import com.atlan.model.relations.Reference;
import com.atlan.model.structs.PopularityInsights;
import com.atlan.model.structs.StarredDetails;
import com.atlan.net.HttpClient;
import com.atlan.serde.AssetDeserializer;
import com.atlan.serde.AssetSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@JsonSerialize(using = AssetSerializer.class)
@JsonDeserialize(using = AssetDeserializer.class)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "typeName",
        defaultImpl = IndistinctAsset.class)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public abstract class Asset extends Reference implements IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    /** List of groups who administer this asset. (This is only used for certain asset types.) */
    @Attribute
    @Singular
    SortedSet<String> adminGroups;

    /** List of roles who administer this asset. (This is only used for Connection assets.) */
    @Attribute
    @Singular
    SortedSet<String> adminRoles;

    /** List of users who administer this asset. (This is only used for certain asset types.) */
    @Attribute
    @Singular
    SortedSet<String> adminUsers;

    /** Detailed message to include in the announcement on this asset. */
    @Attribute
    String announcementMessage;

    /** Brief title for the announcement on this asset. Required when announcementType is specified. */
    @Attribute
    String announcementTitle;

    /** Type of announcement on this asset. */
    @Attribute
    AtlanAnnouncementType announcementType;

    /** Time (epoch) at which the announcement was last updated, in milliseconds. */
    @Attribute
    @Date
    Long announcementUpdatedAt;

    /** Name of the user who last updated the announcement. */
    @Attribute
    String announcementUpdatedBy;

    /** Checks that run on this asset. */
    @Attribute
    @Singular
    SortedSet<IAnomaloCheck> anomaloChecks;

    /** Application asset containing this Asset. */
    @Attribute
    IApplication application;

    /** ApplicationField asset containing this Asset. */
    @Attribute
    IApplicationField applicationField;

    /** Qualified name of the ApplicationField that contains this asset. */
    @Attribute
    String applicationFieldQualifiedName;

    /** Qualified name of the Application that contains this asset. */
    @Attribute
    String applicationQualifiedName;

    /** All associated Anomalo check types. */
    @Attribute
    @Singular
    SortedSet<String> assetAnomaloAppliedCheckTypes;

    /** Total number of checks present in Anomalo for this asset. */
    @Attribute
    Long assetAnomaloCheckCount;

    /** Stringified JSON object containing status of all Anomalo checks associated to this asset. */
    @Attribute
    String assetAnomaloCheckStatuses;

    /** Status of data quality from Anomalo. */
    @Attribute
    String assetAnomaloDQStatus;

    /** Total number of checks failed in Anomalo for this asset. */
    @Attribute
    Long assetAnomaloFailedCheckCount;

    /** All associated Anomalo failed check types. */
    @Attribute
    @Singular
    SortedSet<String> assetAnomaloFailedCheckTypes;

    /** Time (epoch) at which the last check was run via Anomalo. */
    @Attribute
    @Date
    Long assetAnomaloLastCheckRunAt;

    /** URL of the source in Anomalo. */
    @Attribute
    String assetAnomaloSourceUrl;

    /** TBC */
    @Attribute
    String assetCoverImage;

    /** Name of the account in which this asset exists in dbt. */
    @Attribute
    String assetDbtAccountName;

    /** Alias of this asset in dbt. */
    @Attribute
    String assetDbtAlias;

    /** Version of the environment in which this asset is materialized in dbt. */
    @Attribute
    String assetDbtEnvironmentDbtVersion;

    /** Name of the environment in which this asset is materialized in dbt. */
    @Attribute
    String assetDbtEnvironmentName;

    /** Time (epoch) at which the job that materialized this asset in dbt last ran, in milliseconds. */
    @Attribute
    @Date
    Long assetDbtJobLastRun;

    /** Path in S3 to the artifacts saved from the last run of the job that materialized this asset in dbt. */
    @Attribute
    String assetDbtJobLastRunArtifactS3Path;

    /** Whether artifacts were saved from the last run of the job that materialized this asset in dbt (true) or not (false). */
    @Attribute
    Boolean assetDbtJobLastRunArtifactsSaved;

    /** Time (epoch) at which the job that materialized this asset in dbt was last created, in milliseconds. */
    @Attribute
    @Date
    Long assetDbtJobLastRunCreatedAt;

    /** Time (epoch) at which the job that materialized this asset in dbt was dequeued, in milliseconds. */
    @Attribute
    @Date
    Long assetDbtJobLastRunDequedAt;

    /** Thread ID of the user who executed the last run of the job that materialized this asset in dbt. */
    @Attribute
    String assetDbtJobLastRunExecutedByThreadId;

    /** Branch in git from which the last run of the job that materialized this asset in dbt ran. */
    @Attribute
    String assetDbtJobLastRunGitBranch;

    /** SHA hash in git for the last run of the job that materialized this asset in dbt. */
    @Attribute
    String assetDbtJobLastRunGitSha;

    /** Whether docs were generated from the last run of the job that materialized this asset in dbt (true) or not (false). */
    @Attribute
    Boolean assetDbtJobLastRunHasDocsGenerated;

    /** Whether sources were generated from the last run of the job that materialized this asset in dbt (true) or not (false). */
    @Attribute
    Boolean assetDbtJobLastRunHasSourcesGenerated;

    /** Whether notifications were sent from the last run of the job that materialized this asset in dbt (true) or not (false). */
    @Attribute
    Boolean assetDbtJobLastRunNotificationsSent;

    /** Thread ID of the owner of the last run of the job that materialized this asset in dbt. */
    @Attribute
    String assetDbtJobLastRunOwnerThreadId;

    /** Total duration the job that materialized this asset in dbt spent being queued. */
    @Attribute
    String assetDbtJobLastRunQueuedDuration;

    /** Human-readable total duration of the last run of the job that materialized this asset in dbt spend being queued. */
    @Attribute
    String assetDbtJobLastRunQueuedDurationHumanized;

    /** Run duration of the last run of the job that materialized this asset in dbt. */
    @Attribute
    String assetDbtJobLastRunRunDuration;

    /** Human-readable run duration of the last run of the job that materialized this asset in dbt. */
    @Attribute
    String assetDbtJobLastRunRunDurationHumanized;

    /** Time (epoch) at which the job that materialized this asset in dbt was started running, in milliseconds. */
    @Attribute
    @Date
    Long assetDbtJobLastRunStartedAt;

    /** Status message of the last run of the job that materialized this asset in dbt. */
    @Attribute
    String assetDbtJobLastRunStatusMessage;

    /** Total duration of the last run of the job that materialized this asset in dbt. */
    @Attribute
    String assetDbtJobLastRunTotalDuration;

    /** Human-readable total duration of the last run of the job that materialized this asset in dbt. */
    @Attribute
    String assetDbtJobLastRunTotalDurationHumanized;

    /** Time (epoch) at which the job that materialized this asset in dbt was last updated, in milliseconds. */
    @Attribute
    @Date
    Long assetDbtJobLastRunUpdatedAt;

    /** URL of the last run of the job that materialized this asset in dbt. */
    @Attribute
    String assetDbtJobLastRunUrl;

    /** Name of the job that materialized this asset in dbt. */
    @Attribute
    String assetDbtJobName;

    /** Time (epoch) when the next run of the job that materializes this asset in dbt is scheduled. */
    @Attribute
    @Date
    Long assetDbtJobNextRun;

    /** Human-readable time when the next run of the job that materializes this asset in dbt is scheduled. */
    @Attribute
    String assetDbtJobNextRunHumanized;

    /** Schedule of the job that materialized this asset in dbt. */
    @Attribute
    String assetDbtJobSchedule;

    /** Human-readable cron schedule of the job that materialized this asset in dbt. */
    @Attribute
    String assetDbtJobScheduleCronHumanized;

    /** Status of the job that materialized this asset in dbt. */
    @Attribute
    String assetDbtJobStatus;

    /** Metadata for this asset in dbt, specifically everything under the 'meta' key in the dbt object. */
    @Attribute
    String assetDbtMeta;

    /** Name of the package in which this asset exists in dbt. */
    @Attribute
    String assetDbtPackageName;

    /** Name of the project in which this asset exists in dbt. */
    @Attribute
    String assetDbtProjectName;

    /** URL of the semantic layer proxy for this asset in dbt. */
    @Attribute
    String assetDbtSemanticLayerProxyUrl;

    /** Freshness criteria for the source of this asset in dbt. */
    @Attribute
    String assetDbtSourceFreshnessCriteria;

    /** List of tags attached to this asset in dbt. */
    @Attribute
    @Singular
    SortedSet<String> assetDbtTags;

    /** All associated dbt test statuses. */
    @Attribute
    String assetDbtTestStatus;

    /** Unique identifier of this asset in dbt. */
    @Attribute
    String assetDbtUniqueId;

    /** Name of the DBT workflow in Atlan that last updated the asset. */
    @Attribute
    String assetDbtWorkflowLastUpdated;

    /** Name of the icon to use for this asset. (Only applies to glossaries, currently.) */
    @Attribute
    AtlanIcon assetIcon;

    /** List of unique Monte Carlo alert names attached to this asset. */
    @Attribute
    @Singular
    SortedSet<String> assetMcAlertQualifiedNames;

    /** List of Monte Carlo incident names attached to this asset. */
    @Attribute
    @Singular
    SortedSet<String> assetMcIncidentNames;

    /** List of Monte Carlo incident priorities associated with this asset. */
    @Attribute
    @Singular
    SortedSet<String> assetMcIncidentPriorities;

    /** List of unique Monte Carlo incident names attached to this asset. */
    @Attribute
    @Singular
    SortedSet<String> assetMcIncidentQualifiedNames;

    /** List of Monte Carlo incident severities associated with this asset. */
    @Attribute
    @Singular
    SortedSet<String> assetMcIncidentSeverities;

    /** List of Monte Carlo incident states associated with this asset. */
    @Attribute
    @Singular
    SortedSet<String> assetMcIncidentStates;

    /** List of Monte Carlo incident sub-types associated with this asset. */
    @Attribute
    @Singular
    SortedSet<String> assetMcIncidentSubTypes;

    /** List of Monte Carlo incident types associated with this asset. */
    @Attribute
    @Singular
    SortedSet<String> assetMcIncidentTypes;

    /** Tracks whether this asset is monitored by MC or not */
    @Attribute
    Boolean assetMcIsMonitored;

    /** Time (epoch) at which this asset was last synced from Monte Carlo. */
    @Attribute
    @Date
    Long assetMcLastSyncRunAt;

    /** List of Monte Carlo monitor names attached to this asset. */
    @Attribute
    @Singular
    SortedSet<String> assetMcMonitorNames;

    /** List of unique Monte Carlo monitor names attached to this asset. */
    @Attribute
    @Singular
    SortedSet<String> assetMcMonitorQualifiedNames;

    /** Schedules of all associated Monte Carlo monitors. */
    @Attribute
    @Singular
    SortedSet<String> assetMcMonitorScheduleTypes;

    /** Statuses of all associated Monte Carlo monitors. */
    @Attribute
    @Singular
    SortedSet<String> assetMcMonitorStatuses;

    /** Types of all associated Monte Carlo monitors. */
    @Attribute
    @Singular
    SortedSet<String> assetMcMonitorTypes;

    /** Count of policies inside the asset */
    @Attribute
    Long assetPoliciesCount;

    /** Array of policy ids governing this asset */
    @Attribute
    @Singular
    SortedSet<String> assetPolicyGUIDs;

    /** Array of asset ids that equivalent to this asset. */
    @Attribute
    @Singular
    SortedSet<String> assetRedirectGUIDs;

    /** Number of checks done via Soda. */
    @Attribute
    Long assetSodaCheckCount;

    /** All associated Soda check statuses. */
    @Attribute
    String assetSodaCheckStatuses;

    /** Status of data quality from Soda. */
    @Attribute
    String assetSodaDQStatus;

    /** TBC */
    @Attribute
    @Date
    Long assetSodaLastScanAt;

    /** TBC */
    @Attribute
    @Date
    Long assetSodaLastSyncRunAt;

    /** TBC */
    @Attribute
    String assetSodaSourceURL;

    /** List of tags attached to this asset. */
    @Attribute
    @Singular
    SortedSet<String> assetTags;

    /** Color (in hexadecimal RGB) to use to represent this asset. */
    @Attribute
    String assetThemeHex;

    /** Glossary terms that are linked to this asset. */
    @Attribute
    @Singular
    @JsonProperty("meanings")
    SortedSet<IGlossaryTerm> assignedTerms;

    /** Status of this asset's certification. */
    @Attribute
    CertificateStatus certificateStatus;

    /** Human-readable descriptive message used to provide further detail to certificateStatus. */
    @Attribute
    String certificateStatusMessage;

    /** Time (epoch) at which the certification was last updated, in milliseconds. */
    @Attribute
    @Date
    Long certificateUpdatedAt;

    /** Name of the user who last updated the certification of this asset. */
    @Attribute
    String certificateUpdatedBy;

    /** Simple name of the connection through which this asset is accessible. */
    @Attribute
    String connectionName;

    /** Unique name of the connection through which this asset is accessible. */
    @Attribute
    String connectionQualifiedName;

    /** Type of the connector through which this asset is accessible. */
    @Attribute
    @JsonProperty("connectorName")
    AtlanConnectorType connectorType;

    /** Latest version of the data contract (in any status) for this asset. */
    @Attribute
    IDataContract dataContractLatest;

    /** Latest certified version of the data contract for this asset. */
    @Attribute
    IDataContract dataContractLatestCertified;

    /** Unique name of this asset in dbt. */
    @Attribute
    String dbtQualifiedName;

    /** Description of this asset, for example as crawled from a source. Fallback for display purposes, if userDescription is empty. */
    @Attribute
    String description;

    /** Human-readable name of this asset used for display purposes (in user interface). */
    @Attribute
    String displayName;

    /** Array of domain guids linked to this asset */
    @Attribute
    @Singular
    SortedSet<String> domainGUIDs;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IFile> files;

    /** Whether this asset has contract (true) or not (false). */
    @Attribute
    Boolean hasContract;

    /** Whether this asset has lineage (true) or not (false). */
    @Attribute
    @JsonProperty("__hasLineage")
    Boolean hasLineage;

    /** Data products for which this asset is an input port. */
    @Attribute
    @Singular
    SortedSet<IDataProduct> inputPortDataProducts;

    /** TBC */
    @Attribute
    Boolean isAIGenerated;

    /** Whether this asset is discoverable through the UI (true) or not (false). */
    @Attribute
    Boolean isDiscoverable;

    /** Whether this asset can be edited in the UI (true) or not (false). */
    @Attribute
    Boolean isEditable;

    /** TBC */
    @Attribute
    Boolean isPartial;

    /** Time (epoch) of the last operation that inserted, updated, or deleted rows, in milliseconds. */
    @Attribute
    @Date
    Long lastRowChangedAt;

    /** Name of the last run of the crawler that last synchronized this asset. */
    @Attribute
    String lastSyncRun;

    /** Time (epoch) at which this asset was last crawled, in milliseconds. */
    @Attribute
    @Date
    Long lastSyncRunAt;

    /** Name of the crawler that last synchronized this asset. */
    @Attribute
    String lastSyncWorkflowName;

    /** Custom order for sorting purpose, managed by client */
    @Attribute
    String lexicographicalSortOrder;

    /** Links that are attached to this asset. */
    @Attribute
    @Singular
    SortedSet<ILink> links;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IMCIncident> mcIncidents;

    /** Monitors that observe this asset. */
    @Attribute
    @Singular
    SortedSet<IMCMonitor> mcMonitors;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IMetric> metrics;

    /** Name of this asset. Fallback for display purposes, if displayName is empty. */
    @Attribute
    String name;

    /** Array of policy ids non-compliant to this asset */
    @Attribute
    @Singular
    SortedSet<String> nonCompliantAssetPolicyGUIDs;

    /** Data products for which this asset is an output port. */
    @Attribute
    @Singular
    SortedSet<IDataProduct> outputPortDataProducts;

    /** Array of product guids which have this asset as outputPort */
    @Attribute
    @Singular
    SortedSet<String> outputProductGUIDs;

    /** List of groups who own this asset. */
    @Attribute
    @Singular
    SortedSet<String> ownerGroups;

    /** List of users who own this asset. */
    @Attribute
    @Singular
    SortedSet<String> ownerUsers;

    /** Popularity score for this asset. */
    @Attribute
    Double popularityScore;

    /** Array of product guids linked to this asset */
    @Attribute
    @Singular
    SortedSet<String> productGUIDs;

    /** Unique name for this asset. This is typically a concatenation of the asset's name onto its parent's qualifiedName. This must be unique across all assets of the same type. */
    @Attribute
    String qualifiedName;

    /** README that is linked to this asset. */
    @Attribute
    IReadme readme;

    /** URL for sample data for this asset. */
    @Attribute
    String sampleDataUrl;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISchemaRegistrySubject> schemaRegistrySubjects;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISodaCheck> sodaChecks;

    /** The unit of measure for sourceTotalCost. */
    @Attribute
    SourceCostUnitType sourceCostUnit;

    /** Time (epoch) at which this asset was created in the source system, in milliseconds. */
    @Attribute
    @Date
    Long sourceCreatedAt;

    /** Name of the user who created this asset, in the source system. */
    @Attribute
    String sourceCreatedBy;

    /** URL to create an embed for a resource (for example, an image of a dashboard) within Atlan. */
    @Attribute
    String sourceEmbedURL;

    /** Timestamp of most recent read operation. */
    @Attribute
    @Date
    Long sourceLastReadAt;

    /** List of owners of this asset, in the source system. */
    @Attribute
    String sourceOwners;

    /** List of most expensive warehouses with extra insights. */
    @Attribute
    @Singular
    @JsonProperty("sourceQueryComputeCostRecordList")
    List<PopularityInsights> sourceQueryComputeCostRecords;

    /** List of most expensive warehouse names. */
    @Attribute
    @Singular
    @JsonProperty("sourceQueryComputeCostList")
    SortedSet<String> sourceQueryComputeCosts;

    /** Total count of all read operations at source. */
    @Attribute
    Long sourceReadCount;

    /** List of the most expensive queries that accessed this asset. */
    @Attribute
    @Singular
    @JsonProperty("sourceReadExpensiveQueryRecordList")
    List<PopularityInsights> sourceReadExpensiveQueryRecords;

    /** List of the most popular queries that accessed this asset. */
    @Attribute
    @Singular
    @JsonProperty("sourceReadPopularQueryRecordList")
    List<PopularityInsights> sourceReadPopularQueryRecords;

    /** Total cost of read queries at source. */
    @Attribute
    Double sourceReadQueryCost;

    /** List of usernames with extra insights for the most recent users who read this asset. */
    @Attribute
    @Singular
    @JsonProperty("sourceReadRecentUserRecordList")
    List<PopularityInsights> sourceReadRecentUserRecords;

    /** List of usernames of the most recent users who read this asset. */
    @Attribute
    @Singular
    @JsonProperty("sourceReadRecentUserList")
    SortedSet<String> sourceReadRecentUsers;

    /** List of the slowest queries that accessed this asset. */
    @Attribute
    @Singular
    @JsonProperty("sourceReadSlowQueryRecordList")
    List<PopularityInsights> sourceReadSlowQueryRecords;

    /** List of usernames with extra insights for the users who read this asset the most. */
    @Attribute
    @Singular
    @JsonProperty("sourceReadTopUserRecordList")
    List<PopularityInsights> sourceReadTopUserRecords;

    /** List of usernames of the users who read this asset the most. */
    @Attribute
    @Singular
    @JsonProperty("sourceReadTopUserList")
    SortedSet<String> sourceReadTopUsers;

    /** Total number of unique users that read data from asset. */
    @Attribute
    Long sourceReadUserCount;

    /** Total cost of all operations at source. */
    @Attribute
    Double sourceTotalCost;

    /** URL to the resource within the source application, used to create a button to view this asset in the source application. */
    @Attribute
    String sourceURL;

    /** Time (epoch) at which this asset was last updated in the source system, in milliseconds. */
    @Attribute
    @Date
    Long sourceUpdatedAt;

    /** Name of the user who last updated this asset, in the source system. */
    @Attribute
    String sourceUpdatedBy;

    /** Users who have starred this asset. */
    @Attribute
    @Singular("addStarredBy")
    SortedSet<String> starredBy;

    /** Number of users who have starred this asset. */
    @Attribute
    Integer starredCount;

    /** List of usernames with extra information of the users who have starred an asset. */
    @Attribute
    @Singular
    @JsonProperty("starredDetailsList")
    List<StarredDetails> starredDetails;

    /** Subtype of this asset. */
    @Attribute
    String subType;

    /** Name of the Atlan workspace in which this asset exists. */
    @Attribute
    String tenantId;

    /** TBC */
    @Attribute
    @Singular
    @JsonProperty("userDefRelationshipFrom")
    SortedSet<IAsset> userDefRelationshipFroms;

    /** TBC */
    @Attribute
    @Singular
    @JsonProperty("userDefRelationshipTo")
    SortedSet<IAsset> userDefRelationshipTos;

    /** Description of this asset, as provided by a user. If present, this will be used for the description in user interface. */
    @Attribute
    String userDescription;

    /** View score for this asset. */
    @Attribute
    Double viewScore;

    /** List of groups who can view assets contained in a collection. (This is only used for certain asset types.) */
    @Attribute
    @Singular
    SortedSet<String> viewerGroups;

    /** List of users who can view assets contained in a collection. (This is only used for certain asset types.) */
    @Attribute
    @Singular
    SortedSet<String> viewerUsers;

    /** URL of an icon to use for this asset. (Only applies to CustomEntity and Fivetran Catalog assets, currently.) */
    transient String iconUrl;

    /** Custom connection type name. */
    transient String customConnectorType;

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
    @Date
    final Long createTime;

    /** Time (epoch) at which the asset was last updated, in milliseconds. */
    @Date
    final Long updateTime;

    /** Details on the handler used for deletion of the asset. */
    final String deleteHandler;

    /**
     * Depth of this asset within lineage.
     * Note: this will only be available in assets retrieved via lineage, and will vary even for
     * the same asset depending on the starting point of the lineage requested.
     */
    final Long depth;

    /**
     * Reference details about the asset(s) that are immediately upstream of this asset within lineage.
     * Note: this will only be available in assets retrieved via lineage when {@code immediateNeighbors} is true,
     * and could vary even for the same asset depending on the starting point and depth of the lineage requested.
     */
    final List<LineageRef> immediateUpstream;

    /**
     * Reference details about the asset(s) that are immediately downstream of this asset within lineage.
     * Note: this will only be available in assets retrieved via lineage when {@code immediateNeighbors} is true,
     * and could vary even for the same asset depending on the starting point and depth of the lineage requested.
     */
    final List<LineageRef> immediateDownstream;

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

    /** {@inheritDoc} */
    @Override
    public String getQualifiedName() {
        return qualifiedName != null
                ? qualifiedName
                : (getUniqueAttributes() != null ? getUniqueAttributes().getQualifiedName() : null);
    }

    /**
     * Retrieve the value of the custom metadata attribute from this asset.
     * Note: returns null in all cases where the custom metadata does not exist, is not available on this asset,
     * or simply is not assigned any value on this asset.
     *
     * @param setName the name of the custom metadata set from which to retrieve the attribute's value
     * @param attrName the name of the custom metadata attribute for which to retrieve the value
     * @return the value of that custom metadata attribute on this asset, or null if there is no value
     */
    @JsonIgnore
    public Object getCustomMetadata(String setName, String attrName) {
        if (customMetadataSets == null) {
            return null;
        } else if (!customMetadataSets.containsKey(setName)) {
            return null;
        } else {
            Map<String, Object> attrs = customMetadataSets.get(setName).getAttributes();
            if (!attrs.containsKey(attrName)) {
                return null;
            } else {
                return attrs.get(attrName);
            }
        }
    }

    /**
     * Reduce the asset to the minimum set of properties required to update it.
     *
     * @return a builder containing the minimal set of properties required to update this asset
     * @throws InvalidRequestException if any of the minimal set of required properties are not found in the initial object
     */
    public AssetBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(getTypeName(), map);
        return IndistinctAsset.updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Reduce the asset to the minimum set of properties required to relate to it.
     *
     * @return an asset containing the minimal set of properties required to relate to this asset
     * @throws InvalidRequestException if any of the minimal set of required properties are not found in the initial object
     */
    public Asset trimToReference() throws InvalidRequestException {
        if (this.getGuid() != null && !this.getGuid().isEmpty()) {
            return IndistinctAsset.refByGuid(this.getGuid());
        }
        if (this.getQualifiedName() != null && !this.getQualifiedName().isEmpty()) {
            return IndistinctAsset.refByQualifiedName(this.getQualifiedName());
        }
        if (this.getUniqueAttributes() != null
                && this.getUniqueAttributes().getQualifiedName() != null
                && !this.getUniqueAttributes().getQualifiedName().isEmpty()) {
            return IndistinctAsset.refByQualifiedName(this.getUniqueAttributes().getQualifiedName());
        }
        throw new InvalidRequestException(
                ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, "Asset", "guid, qualifiedName");
    }

    /**
     * If an asset with the same qualifiedName exists, updates the existing asset. Otherwise, creates the asset.
     * No Atlan tags or custom metadata will be changed if updating an existing asset, irrespective of what
     * is included in the asset itself when the method is called.
     *
     * @param client connectivity to the Atlan tenant on which to save the asset
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     */
    public AssetMutationResponse save(AtlanClient client) throws AtlanException {
        return client.assets.save(this);
    }

    /**
     * If no asset exists, has the same behavior as the {@link #save(AtlanClient)} method.
     * If an asset does exist, optionally overwrites any Atlan tags. Custom metadata will always
     * be entirely ignored using this method.
     *
     * @param client connectivity to the Atlan tenant on which to save this asset
     * @param replaceAtlanTags whether to replace Atlan tags during an update (true) or not (false)
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     * @deprecated see {@link #save(AtlanClient)}
     */
    @Deprecated
    public AssetMutationResponse save(AtlanClient client, boolean replaceAtlanTags) throws AtlanException {
        return client.assets.save(this, replaceAtlanTags);
    }

    /**
     * If no asset exists, has the same behavior as the {@link #save(AtlanClient)} method, while also setting
     * any custom metadata provided.
     * Will merge any provided custom metadata with any custom metadata that already exists on the asset.
     *
     * @param client connectivity to the Atlan tenant where this asset should be saved
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     */
    public AssetMutationResponse saveMergingCM(AtlanClient client) throws AtlanException {
        return client.assets.saveMergingCM(List.of(this));
    }

    /**
     * If no asset exists, has the same behavior as the {@link #save(AtlanClient)} method, while also setting
     * any custom metadata provided.
     * If an asset does exist, optionally overwrites any Atlan tags.
     * Will merge any provided custom metadata with any custom metadata that already exists on the asset.
     *
     * @param client connectivity to the Atlan tenant where this asset should be saved
     * @param replaceAtlanTags whether to replace AtlanTags during an update (true) or not (false)
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     * @deprecated see {@link #saveMergingCM(AtlanClient)}
     */
    @Deprecated
    public AssetMutationResponse saveMergingCM(AtlanClient client, boolean replaceAtlanTags) throws AtlanException {
        return client.assets.saveMergingCM(List.of(this), replaceAtlanTags);
    }

    /**
     * If no asset exists, has the same behavior as the {@link #save(AtlanClient)} method, while also setting
     * any custom metadata provided.
     * Will overwrite all custom metadata on any existing asset with only the custom metadata provided
     * (wiping out any other custom metadata on an existing asset that is not provided in the request).
     *
     * @param client connectivity to the Atlan tenant where this asset should be saved
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     */
    public AssetMutationResponse saveReplacingCM(AtlanClient client) throws AtlanException {
        return client.assets.saveReplacingCM(List.of(this));
    }

    /**
     * If no asset exists, has the same behavior as the {@link #save(AtlanClient)} method, while also setting
     * any custom metadata provided.
     * If an asset does exist, optionally overwrites any Atlan tags.
     * Will overwrite all custom metadata on any existing asset with only the custom metadata provided
     * (wiping out any other custom metadata on an existing asset that is not provided in the request).
     *
     * @param client connectivity to the Atlan tenant where this asset should be saved
     * @param replaceAtlanTags whether to replace Atlan tags during an update (true) or not (false)
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     * @deprecated see {@link #saveReplacingCM(AtlanClient)}
     */
    @Deprecated
    public AssetMutationResponse saveReplacingCM(AtlanClient client, boolean replaceAtlanTags) throws AtlanException {
        return client.assets.saveReplacingCM(List.of(this), replaceAtlanTags);
    }

    /**
     * If no asset exists, fails with a NotFoundException.
     * Will merge any provided custom metadata with any custom metadata that already exists on the asset.
     *
     * @param client connectivity to the Atlan tenant where this asset should be saved
     * @return details of the updated asset
     * @throws AtlanException on any error during the API invocation
     * @throws com.atlan.exception.NotFoundException if the asset does not exist (will not create it)
     */
    public AssetMutationResponse updateMergingCM(AtlanClient client) throws AtlanException {
        // Attempt to retrieve the asset first, and allow this to throw a NotFoundException if it does not exist
        get(client, this.getTypeName(), this.getQualifiedName(), false);
        // Otherwise, attempt the update
        return saveReplacingCM(client);
    }

    /**
     * If no asset exists, fails with a NotFoundException.
     * Will merge any provided custom metadata with any custom metadata that already exists on the asset.
     * If an asset does exist, optionally overwrites any Atlan tags.
     *
     * @param client connectivity to the Atlan tenant where this asset should be saved
     * @param replaceAtlanTags whether to replace AtlanTags during an update (true) or not (false)
     * @return details of the updated asset
     * @throws AtlanException on any error during the API invocation
     * @throws com.atlan.exception.NotFoundException if the asset does not exist (will not create it)
     * @deprecated see {@link #updateMergingCM(AtlanClient)}
     */
    @Deprecated
    public AssetMutationResponse updateMergingCM(AtlanClient client, boolean replaceAtlanTags) throws AtlanException {
        // Attempt to retrieve the asset first, and allow this to throw a NotFoundException if it does not exist
        get(client, this.getTypeName(), this.getQualifiedName(), false);
        // Otherwise, attempt the update
        return saveReplacingCM(client, replaceAtlanTags);
    }

    /**
     * If no asset exists, fails with a NotFoundException.
     * Will overwrite all custom metadata on any existing asset with only the custom metadata provided
     * (wiping out any other custom metadata on an existing asset that is not provided in the request).
     *
     * @param client connectivity to the Atlan tenant where this asset should be saved
     * @return details of the updated asset
     * @throws AtlanException on any error during the API invocation
     * @throws com.atlan.exception.NotFoundException if the asset does not exist (will not create it)
     */
    public AssetMutationResponse updateReplacingCM(AtlanClient client) throws AtlanException {
        // Attempt to retrieve the asset first, and allow this to throw a NotFoundException if it does not exist
        get(client, this.getTypeName(), this.getQualifiedName(), false);
        // Otherwise, attempt the update
        return saveReplacingCM(client);
    }

    /**
     * If no asset exists, fails with a NotFoundException.
     * Will overwrite all custom metadata on any existing asset with only the custom metadata provided
     * (wiping out any other custom metadata on an existing asset that is not provided in the request).
     * If an asset does exist, optionally overwrites any Atlan tags.
     *
     * @param client connectivity to the Atlan tenant where this asset should be saved
     * @param replaceAtlanTags whether to replace Atlan tags during an update (true) or not (false)
     * @return details of the updated asset
     * @throws AtlanException on any error during the API invocation
     * @throws com.atlan.exception.NotFoundException if the asset does not exist (will not create it)
     * @deprecated see {@link #updateReplacingCM(AtlanClient)}
     */
    @Deprecated
    public AssetMutationResponse updateReplacingCM(AtlanClient client, boolean replaceAtlanTags) throws AtlanException {
        // Attempt to retrieve the asset first, and allow this to throw a NotFoundException if it does not exist
        get(client, this.getTypeName(), this.getQualifiedName(), false);
        // Otherwise, attempt the update
        return saveReplacingCM(client, replaceAtlanTags);
    }

    /**
     * Start a fluent lineage request that will return all active downstream assets.
     * Additional conditions can be chained onto the returned builder before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) assets will be included.
     * (To change the default direction of downstream, chain a .direction() call.)
     *
     * @param client connectivity to Atlan tenant
     * @return a fluent lineage request that includes all active downstream assets
     */
    public FluentLineage.FluentLineageBuilder requestLineage(AtlanClient client) {
        return Asset.lineage(client, getGuid());
    }

    /**
     * Add the API token configured for the default client as an admin to this object.
     *
     * @param client connectivity to Atlan tenant
     * @param assetGuid unique identifier (GUID) of the asset to which we should add this API token as an admin
     * @param impersonationToken a bearer token for an actual user who is already an admin for the object, NOT an API token
     * @throws AtlanException on any error during API invocation
     */
    protected static AssetMutationResponse addApiTokenAsAdmin(
            AtlanClient client, final String assetGuid, final String impersonationToken) throws AtlanException {
        String username = client.users.getCurrentUser().getUsername();
        AssetMutationResponse response = null;
        try (AtlanClient tmp = new AtlanClient(client.getBaseUrl(), impersonationToken)) {
            // Look for the asset as the impersonated user, ensuring we include the admin users
            // in the results (so we avoid clobbering any existing admin users)
            Optional<Asset> found =
                    tmp.assets.select().where(GUID.eq(assetGuid)).includeOnResults(ADMIN_USERS).pageSize(1).stream()
                            .findFirst();
            if (found.isPresent()) {
                Asset asset = found.get();
                Set<String> existingAdmins = asset.getAdminUsers();
                response = asset.trimToRequired()
                        .adminUsers(existingAdmins)
                        .adminUser(username)
                        .build()
                        .save(tmp);
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, assetGuid);
            }
        } catch (Exception e) {
            log.warn("Unable to remove temporary client using impersonationToken.", e);
        }
        return response;
    }

    /**
     * Retrieves an asset by its GUID, optionally complete with all of its relationships.
     * The type of the asset will only be determined at runtime.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the asset to retrieve
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full asset, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link com.atlan.exception.NotFoundException} if the asset does not exist
     */
    @JsonIgnore
    public static Asset get(AtlanClient client, String guid, boolean includeAllRelationships) throws AtlanException {
        AssetResponse response = client.assets.get(guid, !includeAllRelationships, !includeAllRelationships);
        Asset asset = response.getAsset();
        if (asset != null && includeAllRelationships) {
            asset.setCompleteObject();
        }
        return asset;
    }

    /**
     * Retrieves an asset by its qualifiedName, optionally complete with all of its relationships.
     * The type of the asset will only be determined at runtime.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param typeName the type of the asset to retrieve
     * @param qualifiedName the unique name of the asset to retrieve
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full asset, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link com.atlan.exception.NotFoundException} if the asset does not exist
     */
    @JsonIgnore
    public static Asset get(AtlanClient client, String typeName, String qualifiedName, boolean includeAllRelationships)
            throws AtlanException {
        AssetResponse response =
                client.assets.get(typeName, qualifiedName, !includeAllRelationships, !includeAllRelationships);
        Asset asset = response.getAsset();
        if (asset != null && includeAllRelationships) {
            asset.setCompleteObject();
        }
        return asset;
    }

    /**
     * Soft-deletes an asset by its GUID. This operation can be reversed by updating the asset and changing
     * its {@link #status} to {@code ACTIVE}.
     *
     * @param client connectivity to the Atlan tenant from which to delete the asset
     * @param guid of the asset to soft-delete
     * @return details of the soft-deleted asset
     * @throws AtlanException on any error during the API invocation
     */
    public static AssetDeletionResponse delete(AtlanClient client, String guid) throws AtlanException {
        return client.assets.delete(guid, AtlanDeleteType.SOFT);
    }

    /**
     * Hard-deletes (purges) an asset by its GUID. This operation is irreversible.
     *
     * @param client connectivity to the Atlan tenant from which to delete the asset
     * @param guid of the asset to hard-delete
     * @return details of the hard-deleted asset
     * @throws AtlanException on any error during the API invocation
     */
    public static AssetDeletionResponse purge(AtlanClient client, String guid) throws AtlanException {
        return client.assets.delete(guid, AtlanDeleteType.PURGE);
    }

    /**
     * Start a fluent lineage request that will return all active downstream assets.
     * Additional conditions can be chained onto the returned builder before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) assets will be included.
     * (To change the default direction of downstream, chain a .direction() call.)
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param guid unique identifier (GUID) for the starting point of lineage
     * @return a fluent lineage request that includes all active downstream assets
     */
    public static FluentLineage.FluentLineageBuilder lineage(AtlanClient client, String guid) {
        return lineage(client, guid, false);
    }

    /**
     * Start a fluent lineage request that will return all downstream assets.
     * Additional conditions can be chained onto the returned builder before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. (To change the default direction of downstream, chain a
     * .direction() call.)
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the lineage
     * @param guid unique identifier (GUID) for the starting point of lineage
     * @param includeArchived when true, archived (soft-deleted) assets in lineage will be included
     * @return a fluent search that includes all downstream assets
     */
    public static FluentLineage.FluentLineageBuilder lineage(AtlanClient client, String guid, boolean includeArchived) {
        FluentLineage.FluentLineageBuilder builder = FluentLineage.builder(client, guid);
        if (!includeArchived) {
            builder.whereAsset(FluentLineage.ACTIVE)
                    .whereRelationship(FluentLineage.ACTIVE)
                    .includeInResults(FluentLineage.ACTIVE);
        }
        return builder;
    }

    /**
     * Update only the provided custom metadata attributes on the asset. This will leave all other custom metadata
     * attributes, even within the same named custom metadata, unchanged.
     *
     * @param client connectivity to the Atlan tenant on which to update the asset's custom metadata attributes
     * @param guid unique identifier of the asset
     * @param cmName human-readable name of the custom metadata to update
     * @param attributes the values of the custom metadata attributes to change
     * @throws AtlanException on any API problems, or if the custom metadata is not defined in Atlan
     */
    public static void updateCustomMetadataAttributes(
            AtlanClient client, String guid, String cmName, CustomMetadataAttributes attributes) throws AtlanException {
        client.assets.updateCustomMetadataAttributes(guid, cmName, attributes);
    }

    /**
     * Replace specific custom metadata on the asset. This will replace everything within the named custom metadata,
     * but will not change any of the other named custom metadata on the asset.
     *
     * @param client connectivity to the Atlan tenant on which to replace the asset's custom metadata
     * @param guid unique identifier of the asset
     * @param cmName human-readable name of the custom metadata to replace
     * @param attributes the values of the attributes to replace for the custom metadata
     * @throws AtlanException on any API problems, or if the custom metadata is not defined in Atlan
     */
    public static void replaceCustomMetadata(
            AtlanClient client, String guid, String cmName, CustomMetadataAttributes attributes) throws AtlanException {
        client.assets.replaceCustomMetadata(guid, cmName, attributes);
    }

    /**
     * Remove specific custom metadata from an asset.
     *
     * @param client connectivity to the Atlan tenant from which to remove the asset's custom metadata
     * @param guid unique identifier of the asset
     * @param cmName human-readable name of the custom metadata to remove
     * @throws AtlanException on any API problems, or if the custom metadata is not defined in Atlan
     */
    public static void removeCustomMetadata(AtlanClient client, String guid, String cmName) throws AtlanException {
        client.assets.removeCustomMetadata(guid, cmName);
    }

    /**
     * Add Atlan tags to an asset, without replacing existing Atlan tags linked to the asset.
     * Note: this operation must make two API calls — one to retrieve the asset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the asset
     * @param typeName type of the asset
     * @param qualifiedName of the asset
     * @param atlanTagNames human-readable names of the Atlan tags to append
     * @return the asset that was updated
     * @throws AtlanException on any API problems
     * @deprecated see {@link AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    protected static Asset appendAtlanTags(
            AtlanClient client, String typeName, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(client, typeName, qualifiedName, atlanTagNames, true, true, false);
    }

    /**
     * Add Atlan tags to an asset, without replacing existing Atlan tags linked to the asset.
     * Note: this operation must make two API calls — one to retrieve the asset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to add the Atlan tags to the asset
     * @param typeName type of the asset
     * @param qualifiedName of the asset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @return the asset that was updated
     * @throws AtlanException on any API problems
     * @deprecated see {@link AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    protected static Asset appendAtlanTags(
            AtlanClient client,
            String typeName,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {

        Asset existing = get(client, typeName, qualifiedName, true);
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
            return replaceAtlanTags(
                    client, minimal.atlanTags(replacementAtlanTags).build());
        }
        return null;
    }

    /**
     * Remove an Atlan tag from an asset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the Atlan tag from the asset
     * @param typeName type of the asset
     * @param qualifiedName of the asset
     * @param atlanTagName human-readable name of the Atlan tags to remove
     * @throws AtlanException on any API problems, or if any of the Atlan tag does not exist on the asset
     * @deprecated see {@link AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    protected static void removeAtlanTag(AtlanClient client, String typeName, String qualifiedName, String atlanTagName)
            throws AtlanException {
        client.assets.removeAtlanTag(typeName, qualifiedName, atlanTagName, true);
    }

    /**
     * Update the certificate on an asset.
     *
     * @param client connectivity to the Atlan tenant on which to update the asset's certificate
     * @param builder the builder to use for updating the certificate
     * @param certificate certificate to set
     * @param message (optional) message to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    protected static Asset updateCertificate(
            AtlanClient client, AssetBuilder<?, ?> builder, CertificateStatus certificate, String message)
            throws AtlanException {
        builder.certificateStatus(certificate);
        if (message != null && message.length() > 1) {
            builder.certificateStatusMessage(message);
        }
        return updateAttributes(client, builder.build());
    }

    /**
     * Remove the certificate on an asset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's certificate
     * @param builder the builder to use for removing the certificate
     * @return the result of the removal, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    protected static Asset removeCertificate(AtlanClient client, AssetBuilder<?, ?> builder) throws AtlanException {
        Asset asset = builder.removeCertificate().build();
        AssetMutationResponse response = asset.save(client);
        if (response != null && !response.getUpdatedAssets().isEmpty()) {
            return response.getUpdatedAssets().get(0);
        } else {
            return null;
        }
    }

    /**
     * Update the announcement on an asset.
     *
     * @param client connectivity to the Atlan tenant on which to update the asset's announcement
     * @param builder the builder to use for updating the announcement
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    protected static Asset updateAnnouncement(
            AtlanClient client, AssetBuilder<?, ?> builder, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        builder.announcementType(type);
        if (title != null && title.length() > 1) {
            builder.announcementTitle(title);
        }
        if (message != null && message.length() > 1) {
            builder.announcementMessage(message);
        }
        return updateAttributes(client, builder.build());
    }

    /**
     * Remove the announcement on an asset.
     *
     * @param client connectivity to the Atlan tenant from which to remove the asset's announcement
     * @param builder the builder to use for removing the announcement
     * @return the result of the removal, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    protected static Asset removeAnnouncement(AtlanClient client, AssetBuilder<?, ?> builder) throws AtlanException {
        Asset asset = builder.removeAnnouncement().build();
        AssetMutationResponse response = asset.save(client);
        if (response != null && !response.getUpdatedAssets().isEmpty()) {
            return response.getUpdatedAssets().get(0);
        } else {
            return null;
        }
    }

    /**
     * Remove the system description from an asset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param builder the builder to use for removing the description
     * @return the result of the removal, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    protected static Asset removeDescription(AtlanClient client, AssetBuilder<?, ?> builder) throws AtlanException {
        Asset asset = builder.removeDescription().build();
        AssetMutationResponse response = asset.save(client);
        if (response != null && !response.getUpdatedAssets().isEmpty()) {
            return response.getUpdatedAssets().get(0);
        } else {
            return null;
        }
    }

    /**
     * Remove the user-provided description from an asset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param builder the builder to use for removing the description
     * @return the result of the removal, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    protected static Asset removeUserDescription(AtlanClient client, AssetBuilder<?, ?> builder) throws AtlanException {
        Asset asset = builder.removeUserDescription().build();
        AssetMutationResponse response = asset.save(client);
        if (response != null && !response.getUpdatedAssets().isEmpty()) {
            return response.getUpdatedAssets().get(0);
        } else {
            return null;
        }
    }

    /**
     * Remove the owners from an asset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's owners
     * @param builder the builder to use for removing the owners
     * @return the result of the removal, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    protected static Asset removeOwners(AtlanClient client, AssetBuilder<?, ?> builder) throws AtlanException {
        Asset asset = builder.removeOwners().build();
        AssetMutationResponse response = asset.save(client);
        if (response != null && !response.getUpdatedAssets().isEmpty()) {
            return response.getUpdatedAssets().get(0);
        } else {
            return null;
        }
    }

    private static Asset updateAttributes(AtlanClient client, Asset asset) throws AtlanException {
        AssetMutationResponse response = client.assets.saveNoTagsNoCM(List.of(asset), null);
        if (response != null && !response.getUpdatedAssets().isEmpty()) {
            return response.getUpdatedAssets().get(0);
        }
        return null;
    }

    @Deprecated
    private static Asset replaceAtlanTags(AtlanClient client, Asset asset) throws AtlanException {
        AssetMutationResponse response = client.assets.save(asset, true);
        if (response != null && !response.getUpdatedAssets().isEmpty()) {
            return response.getUpdatedAssets().get(0);
        }
        return null;
    }

    /**
     * Update the certificate on an asset.
     *
     * @param client connectivity to the Atlan tenant on which to update the asset's certificate
     * @param builder the builder to use for updating the certificate
     * @param typeName type of the asset
     * @param qualifiedName for the asset
     * @param certificate certificate to set
     * @param message (optional) message to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    protected static Asset updateCertificate(
            AtlanClient client,
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
        return updateAttributes(client, typeName, qualifiedName, builder.build());
    }

    /**
     * Update the announcement on an asset.
     *
     * @param client connectivity to the Atlan tenant on which to update the asset's announcement
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
            AtlanClient client,
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
        return updateAttributes(client, typeName, qualifiedName, builder.build());
    }

    private static Asset updateAttributes(AtlanClient client, String typeName, String qualifiedName, Asset asset)
            throws AtlanException {
        AssetMutationResponse response = client.assets.updateAttributes(typeName, qualifiedName, asset);
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
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param typeName type of the asset to restore
     * @param qualifiedName of the asset to restore
     * @return true if the asset is now restored, or false if not
     * @throws AtlanException on any API problems
     */
    protected static boolean restore(AtlanClient client, String typeName, String qualifiedName) throws AtlanException {
        try {
            return restore(client, typeName, qualifiedName, 0);
        } catch (InterruptedException e) {
            throw new ApiException(ErrorCode.RETRIES_INTERRUPTED, e);
        }
    }

    /**
     * Restore an archived (soft-deleted) asset to active, retrying in case it is found to
     * already be active (since the delete handlers run asynchronously).
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param typeName type of the asset to restore
     * @param qualifiedName of the asset to restore
     * @param retryCount number of retries we have already attempted
     * @return true if the asset is now restored, or false if not
     * @throws AtlanException on any API problems
     * @throws InterruptedException if the retry cycle sleeps are interrupted
     */
    private static boolean restore(AtlanClient client, String typeName, String qualifiedName, int retryCount)
            throws AtlanException, InterruptedException {
        Asset existing = get(client, typeName, qualifiedName, true);
        if (existing == null) {
            // Nothing to restore, so cannot be restored
            return false;
        } else if (existing.getStatus() == AtlanStatus.ACTIVE) {
            // Already active, but this could be due to the async nature of the delete handlers
            if (retryCount < client.getMaxNetworkRetries()) {
                // So continue to retry up to the maximum number of allowed retries
                log.debug(
                        "Attempted to restore an active asset, retrying status check for async delete handling (attempt: {}).",
                        retryCount + 1);
                Thread.sleep(HttpClient.waitTime(retryCount).toMillis());
                return restore(client, typeName, qualifiedName, retryCount + 1);
            } else {
                // If we have exhausted the retries, though, then we should just short-circuit
                return true;
            }
        } else {
            Optional<String> guidRestored = restore(client, existing);
            return guidRestored.isPresent() && guidRestored.get().equals(existing.getGuid());
        }
    }

    /**
     * Replace the terms linked to an asset.
     *
     * @param client connectivity to the Atlan tenant on which to replace the asset's terms
     * @param builder the builder to use for updating the terms
     * @param terms the list of terms to replace on the asset, or null to remove all terms from an asset
     * @return the asset that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    protected static Asset replaceTerms(AtlanClient client, AssetBuilder<?, ?> builder, List<IGlossaryTerm> terms)
            throws AtlanException {
        if (terms == null || terms.isEmpty()) {
            Asset asset = builder.removeAssignedTerms().build();
            return updateRelationships(client, asset);
        } else {
            return updateRelationships(
                    client, builder.assignedTerms(getTermRefs(terms)).build());
        }
    }

    /**
     * Link additional terms to an asset, without replacing existing terms linked to the asset.
     * Note: this operation must make two API calls — one to retrieve the asset's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the asset
     * @param typeName type of the asset
     * @param qualifiedName for the asset
     * @param terms the list of terms to append to the asset
     * @return the asset that was updated (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    protected static Asset appendTerms(
            AtlanClient client, String typeName, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        Asset existing = get(client, typeName, qualifiedName, true);
        if (terms == null) {
            return existing;
        } else if (existing != null) {
            Set<IGlossaryTerm> replacementTerms = new TreeSet<>();
            Set<IGlossaryTerm> existingTerms = existing.getAssignedTerms();
            if (existingTerms != null) {
                for (IGlossaryTerm term : existingTerms) {
                    if (term.getRelationshipStatus() != AtlanStatus.DELETED) {
                        // Only re-include the terms that are not already deleted
                        replacementTerms.add(term);
                    }
                }
            }
            replacementTerms.addAll(terms);
            AssetBuilder<?, ?> minimal = existing.trimToRequired();
            return updateRelationships(
                    client, minimal.assignedTerms(getTermRefs(replacementTerms)).build());
        }
        return null;
    }

    /**
     * Remove terms from an asset, without replacing all existing terms linked to the asset.
     * Note: this operation must make two API calls — one to retrieve the asset's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant on which to remove terms from the asset
     * @param typeName type of the asset
     * @param qualifiedName for the asset
     * @param terms the list of terms to remove from the asset (note: these must be references by GUID
     *              in order to efficiently remove any existing terms)
     * @return the asset that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @throws InvalidRequestException if any of the passed terms are not valid references by GUID to a term
     * @deprecated see {@link AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    protected static Asset removeTerms(
            AtlanClient client, String typeName, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        Asset existing = get(client, typeName, qualifiedName, true);
        if (existing != null) {
            Set<IGlossaryTerm> replacementTerms = new TreeSet<>();
            Set<IGlossaryTerm> existingTerms = existing.getAssignedTerms();
            Set<String> removeGuids = new HashSet<>();
            for (IGlossaryTerm term : terms) {
                if (term.isValidReferenceByGuid()) {
                    removeGuids.add(term.getGuid());
                } else {
                    throw new InvalidRequestException(ErrorCode.MISSING_TERM_GUID);
                }
            }
            for (IGlossaryTerm term : existingTerms) {
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
            return updateRelationships(client, update);
        }
        return null;
    }

    private static Collection<IGlossaryTerm> getTermRefs(Collection<IGlossaryTerm> terms) {
        if (terms != null && !terms.isEmpty()) {
            Set<IGlossaryTerm> termRefs = new TreeSet<>();
            for (IGlossaryTerm term : terms) {
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

    private static Asset updateRelationships(AtlanClient client, Asset asset) throws AtlanException {
        String typeNameToUpdate = asset.getTypeName();
        AssetMutationResponse response = client.assets.saveNoTagsNoCM(List.of(asset), null);
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

    private static Optional<String> restore(AtlanClient client, Asset asset) throws AtlanException {
        AssetMutationResponse response = client.assets.restore(asset);
        if (response != null && !response.getGuidAssignments().isEmpty()) {
            return response.getGuidAssignments().values().stream().findFirst();
        }
        return Optional.empty();
    }

    public abstract static class AssetBuilder<C extends Asset, B extends Asset.AssetBuilder<C, B>>
            extends Reference.ReferenceBuilder<C, B> {
        /** Set both the connection qualified name of the asset, and its connector type. */
        public B connectionQualifiedName(String qualifiedName) {
            AtlanConnectorType ct = Connection.getConnectorTypeFromQualifiedName(qualifiedName);
            if (ct != AtlanConnectorType.UNKNOWN_CUSTOM) {
                connectorType(ct);
            } else {
                String[] tokens = qualifiedName.split("/");
                if (tokens.length > 1) {
                    customConnectorType(tokens[1]);
                }
            }
            return self();
        }

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
            // true
            clearAtlanTags();
            clearAtlanTagNames();
            nullField("atlanTags");
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

        /**
         * Remove the specified term (only) from the asset, idempotently, avoiding impacting any other existing terms.
         * @param term the term to be removed from the asset
         * @return the builder with the term to be removed
         */
        public B removeAssignedTerm(GlossaryTerm term) {
            assignedTerm(term.toBuilder().semantic(SaveSemantic.REMOVE).build());
            return self();
        }

        /**
         * Append the specified term to the asset, idempotently, avoiding impacting any other existing terms.
         * @param term the term to be appended to the asset
         * @return the builder with the term appended
         */
        public B appendAssignedTerm(GlossaryTerm term) {
            assignedTerm(term.toBuilder().semantic(SaveSemantic.APPEND).build());
            return self();
        }

        /**
         * Append the specified tags to the asset idempotently, avoiding replacing any existing tags.
         * @param atlanTagNames human-readable names of the tags to append
         * @return the builder with the tags appended
         */
        public B appendAtlanTags(List<String> atlanTagNames) {
            appendAtlanTags(atlanTagNames, false, false, false, false);
            return self();
        }

        /**
         * Append the specified tags to the asset idempotently, avoiding replacing any existing tags.
         * @param atlanTagNames human-readable names of the tags to append
         * @param propagate whether to propagate the Atlan tag (true) or not (false)
         * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
         * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
         * @param restrictHierarchyPropagation whether to avoid propagating through the hierarchy (true) or do propagate through the hierarchy (false)
         * @return the builder with the tags appended
         */
        public B appendAtlanTags(
                List<String> atlanTagNames,
                boolean propagate,
                boolean removePropagationsOnDelete,
                boolean restrictLineagePropagation,
                boolean restrictHierarchyPropagation) {
            atlanTagNames.forEach(tagName -> {
                appendAtlanTag(
                        tagName,
                        propagate,
                        removePropagationsOnDelete,
                        restrictLineagePropagation,
                        restrictHierarchyPropagation);
            });
            return self();
        }

        /**
         * Append the specified tag to the asset idempotently, avoiding replacing any existing tag.
         * @param tagName human-readable name of the tag to append
         * @return the builder with the tag appended
         */
        public B appendAtlanTag(String tagName) {
            appendAtlanTag(tagName, false, false, false, false);
            return self();
        }

        /**
         * Append the specified tag to the asset idempotently, avoiding replacing any existing tag.
         * @param tagName human-readable name of the tag to append
         * @param propagate whether to propagate the Atlan tag (true) or not (false)
         * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
         * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
         * @param restrictHierarchyPropagation whether to avoid propagating through the hierarchy (true) or do propagate through the hierarchy (false)
         * @return the builder with the tag appended
         */
        public B appendAtlanTag(
                String tagName,
                boolean propagate,
                boolean removePropagationsOnDelete,
                boolean restrictLineagePropagation,
                boolean restrictHierarchyPropagation) {
            atlanTag(AtlanTag.builder()
                    .typeName(tagName)
                    .semantic(SaveSemantic.APPEND)
                    .propagate(propagate)
                    .removePropagationsOnEntityDelete(removePropagationsOnDelete)
                    .restrictPropagationThroughLineage(restrictLineagePropagation)
                    .restrictPropagationThroughHierarchy(restrictHierarchyPropagation)
                    .build());
            return self();
        }

        /**
         * Remove the specified tags from the asset idempotently, avoiding removing any other existing tag.
         * @param tagNames human-readable names of the tags to remove
         * @return the builder with the tag removed
         */
        public B removeAtlanTags(List<String> tagNames) {
            tagNames.forEach(tagName -> {
                removeAtlanTag(tagName);
            });
            return self();
        }

        /**
         * Remove the specified tag from the asset idempotently, avoiding removing any other existing tag.
         * @param tagName human-readable name of the tag to remove
         * @return the builder with the tag removed
         */
        public B removeAtlanTag(String tagName) {
            atlanTag(AtlanTag.builder()
                    .typeName(tagName)
                    .semantic(SaveSemantic.REMOVE)
                    .build());
            return self();
        }
    }
}
