/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanIcon;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.SourceCostUnitType;
import com.atlan.model.fields.KeywordField;
import com.atlan.model.fields.KeywordTextField;
import com.atlan.model.fields.NumericField;
import com.atlan.model.fields.RelationField;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.structs.GoogleLabel;
import com.atlan.model.structs.GoogleTag;
import com.atlan.model.structs.PopularityInsights;
import com.atlan.model.structs.StarredDetails;
import com.atlan.serde.AssetDeserializer;
import com.atlan.serde.AssetSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.processing.Generated;

/**
 * Instance of a Google Cloud Storage object in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@JsonSerialize(using = AssetSerializer.class)
@JsonDeserialize(using = AssetDeserializer.class)
public interface IGCSObject {

    public static final String TYPE_NAME = "GCSObject";

    /** GCS bucket in which the object exists. */
    RelationField GCS_BUCKET = new RelationField("gcsBucket");

    /** Simple name of the bucket in which this object exists. */
    KeywordTextField GCS_BUCKET_NAME = new KeywordTextField("gcsBucketName", "gcsBucketName.keyword", "gcsBucketName");

    /** Unique name of the bucket in which this object exists. */
    KeywordTextField GCS_BUCKET_QUALIFIED_NAME =
            new KeywordTextField("gcsBucketQualifiedName", "gcsBucketQualifiedName", "gcsBucketQualifiedName.text");

    /** CRC32C hash of this object. */
    KeywordField GCS_OBJECT_CRC32C_HASH = new KeywordField("gcsObjectCRC32CHash", "gcsObjectCRC32CHash");

    /** Information about how this object's content should be presented. */
    KeywordField GCS_OBJECT_CONTENT_DISPOSITION =
            new KeywordField("gcsObjectContentDisposition", "gcsObjectContentDisposition");

    /** Content encoding of this object. */
    KeywordField GCS_OBJECT_CONTENT_ENCODING = new KeywordField("gcsObjectContentEncoding", "gcsObjectContentEncoding");

    /** Language of this object's contents. */
    KeywordField GCS_OBJECT_CONTENT_LANGUAGE = new KeywordField("gcsObjectContentLanguage", "gcsObjectContentLanguage");

    /** Type of content in this object. */
    KeywordField GCS_OBJECT_CONTENT_TYPE = new KeywordField("gcsObjectContentType", "gcsObjectContentType");

    /** Time (epoch) at which this object's data was last modified, in milliseconds. */
    NumericField GCS_OBJECT_DATA_LAST_MODIFIED_TIME =
            new NumericField("gcsObjectDataLastModifiedTime", "gcsObjectDataLastModifiedTime");

    /** Generation ID of this object. */
    NumericField GCS_OBJECT_GENERATION_ID = new NumericField("gcsObjectGenerationId", "gcsObjectGenerationId");

    /** Type of hold on this object. */
    KeywordField GCS_OBJECT_HOLD_TYPE = new KeywordField("gcsObjectHoldType", "gcsObjectHoldType");

    /** Key of this object, in GCS. */
    KeywordTextField GCS_OBJECT_KEY = new KeywordTextField("gcsObjectKey", "gcsObjectKey", "gcsObjectKey.text");

    /** MD5 hash of this object. */
    KeywordField GCS_OBJECT_MD5HASH = new KeywordField("gcsObjectMD5Hash", "gcsObjectMD5Hash");

    /** Media link to this object. */
    KeywordTextField GCS_OBJECT_MEDIA_LINK =
            new KeywordTextField("gcsObjectMediaLink", "gcsObjectMediaLink", "gcsObjectMediaLink.text");

    /** Retention expiration date of this object. */
    NumericField GCS_OBJECT_RETENTION_EXPIRATION_DATE =
            new NumericField("gcsObjectRetentionExpirationDate", "gcsObjectRetentionExpirationDate");

    /** Object size in bytes. */
    NumericField GCS_OBJECT_SIZE = new NumericField("gcsObjectSize", "gcsObjectSize");

    /** TBC */
    SortedSet<String> getAdminGroups();

    /** TBC */
    SortedSet<String> getAdminRoles();

    /** TBC */
    SortedSet<String> getAdminUsers();

    /** TBC */
    String getAnnouncementMessage();

    /** TBC */
    String getAnnouncementTitle();

    /** TBC */
    AtlanAnnouncementType getAnnouncementType();

    /** TBC */
    Long getAnnouncementUpdatedAt();

    /** TBC */
    String getAnnouncementUpdatedBy();

    /** TBC */
    String getAssetDbtAccountName();

    /** TBC */
    String getAssetDbtAlias();

    /** TBC */
    String getAssetDbtEnvironmentDbtVersion();

    /** TBC */
    String getAssetDbtEnvironmentName();

    /** TBC */
    Long getAssetDbtJobLastRun();

    /** TBC */
    String getAssetDbtJobLastRunArtifactS3Path();

    /** TBC */
    Boolean getAssetDbtJobLastRunArtifactsSaved();

    /** TBC */
    Long getAssetDbtJobLastRunCreatedAt();

    /** TBC */
    Long getAssetDbtJobLastRunDequedAt();

    /** TBC */
    String getAssetDbtJobLastRunExecutedByThreadId();

    /** TBC */
    String getAssetDbtJobLastRunGitBranch();

    /** TBC */
    String getAssetDbtJobLastRunGitSha();

    /** TBC */
    Boolean getAssetDbtJobLastRunHasDocsGenerated();

    /** TBC */
    Boolean getAssetDbtJobLastRunHasSourcesGenerated();

    /** TBC */
    Boolean getAssetDbtJobLastRunNotificationsSent();

    /** TBC */
    String getAssetDbtJobLastRunOwnerThreadId();

    /** TBC */
    String getAssetDbtJobLastRunQueuedDuration();

    /** TBC */
    String getAssetDbtJobLastRunQueuedDurationHumanized();

    /** TBC */
    String getAssetDbtJobLastRunRunDuration();

    /** TBC */
    String getAssetDbtJobLastRunRunDurationHumanized();

    /** TBC */
    Long getAssetDbtJobLastRunStartedAt();

    /** TBC */
    String getAssetDbtJobLastRunStatusMessage();

    /** TBC */
    String getAssetDbtJobLastRunTotalDuration();

    /** TBC */
    String getAssetDbtJobLastRunTotalDurationHumanized();

    /** TBC */
    Long getAssetDbtJobLastRunUpdatedAt();

    /** TBC */
    String getAssetDbtJobLastRunUrl();

    /** TBC */
    String getAssetDbtJobName();

    /** TBC */
    Long getAssetDbtJobNextRun();

    /** TBC */
    String getAssetDbtJobNextRunHumanized();

    /** TBC */
    String getAssetDbtJobSchedule();

    /** TBC */
    String getAssetDbtJobScheduleCronHumanized();

    /** TBC */
    String getAssetDbtJobStatus();

    /** TBC */
    String getAssetDbtMeta();

    /** TBC */
    String getAssetDbtPackageName();

    /** TBC */
    String getAssetDbtProjectName();

    /** TBC */
    String getAssetDbtSemanticLayerProxyUrl();

    /** TBC */
    String getAssetDbtSourceFreshnessCriteria();

    /** TBC */
    SortedSet<String> getAssetDbtTags();

    /** TBC */
    String getAssetDbtTestStatus();

    /** TBC */
    String getAssetDbtUniqueId();

    /** TBC */
    AtlanIcon getAssetIcon();

    /** TBC */
    SortedSet<String> getAssetMcIncidentNames();

    /** TBC */
    SortedSet<String> getAssetMcIncidentQualifiedNames();

    /** TBC */
    SortedSet<String> getAssetMcIncidentSeverities();

    /** TBC */
    SortedSet<String> getAssetMcIncidentStates();

    /** TBC */
    SortedSet<String> getAssetMcIncidentSubTypes();

    /** TBC */
    SortedSet<String> getAssetMcIncidentTypes();

    /** TBC */
    Long getAssetMcLastSyncRunAt();

    /** TBC */
    SortedSet<String> getAssetMcMonitorNames();

    /** TBC */
    SortedSet<String> getAssetMcMonitorQualifiedNames();

    /** TBC */
    SortedSet<String> getAssetMcMonitorScheduleTypes();

    /** TBC */
    SortedSet<String> getAssetMcMonitorStatuses();

    /** TBC */
    SortedSet<String> getAssetMcMonitorTypes();

    /** TBC */
    Long getAssetSodaCheckCount();

    /** TBC */
    String getAssetSodaCheckStatuses();

    /** TBC */
    String getAssetSodaDQStatus();

    /** TBC */
    Long getAssetSodaLastScanAt();

    /** TBC */
    Long getAssetSodaLastSyncRunAt();

    /** TBC */
    String getAssetSodaSourceURL();

    /** TBC */
    SortedSet<String> getAssetTags();

    /** Glossary terms that are linked to this asset. */
    SortedSet<IGlossaryTerm> getAssignedTerms();

    /** TBC */
    CertificateStatus getCertificateStatus();

    /** TBC */
    String getCertificateStatusMessage();

    /** TBC */
    Long getCertificateUpdatedAt();

    /** TBC */
    String getCertificateUpdatedBy();

    /** TBC */
    String getConnectionName();

    /** TBC */
    String getConnectionQualifiedName();

    /** TBC */
    AtlanConnectorType getConnectorType();

    /** TBC */
    String getDbtQualifiedName();

    /** TBC */
    String getDescription();

    /** TBC */
    String getDisplayName();

    /** TBC */
    SortedSet<IFile> getFiles();

    /** TBC */
    String getGcsAccessControl();

    /** GCS bucket in which the object exists. */
    IGCSBucket getGcsBucket();

    /** Simple name of the bucket in which this object exists. */
    String getGcsBucketName();

    /** Unique name of the bucket in which this object exists. */
    String getGcsBucketQualifiedName();

    /** TBC */
    String getGcsETag();

    /** TBC */
    String getGcsEncryptionType();

    /** TBC */
    Long getGcsMetaGenerationId();

    /** CRC32C hash of this object. */
    String getGcsObjectCRC32CHash();

    /** Information about how this object's content should be presented. */
    String getGcsObjectContentDisposition();

    /** Content encoding of this object. */
    String getGcsObjectContentEncoding();

    /** Language of this object's contents. */
    String getGcsObjectContentLanguage();

    /** Type of content in this object. */
    String getGcsObjectContentType();

    /** Time (epoch) at which this object's data was last modified, in milliseconds. */
    Long getGcsObjectDataLastModifiedTime();

    /** Generation ID of this object. */
    Long getGcsObjectGenerationId();

    /** Type of hold on this object. */
    String getGcsObjectHoldType();

    /** Key of this object, in GCS. */
    String getGcsObjectKey();

    /** MD5 hash of this object. */
    String getGcsObjectMD5Hash();

    /** Media link to this object. */
    String getGcsObjectMediaLink();

    /** Retention expiration date of this object. */
    Long getGcsObjectRetentionExpirationDate();

    /** Object size in bytes. */
    Long getGcsObjectSize();

    /** TBC */
    Boolean getGcsRequesterPays();

    /** TBC */
    String getGcsStorageClass();

    /** TBC */
    List<GoogleLabel> getGoogleLabels();

    /** TBC */
    String getGoogleLocation();

    /** TBC */
    String getGoogleLocationType();

    /** TBC */
    String getGoogleProjectId();

    /** TBC */
    String getGoogleProjectName();

    /** TBC */
    Long getGoogleProjectNumber();

    /** TBC */
    String getGoogleService();

    /** TBC */
    List<GoogleTag> getGoogleTags();

    /** TBC */
    Boolean getHasLineage();

    /** TBC */
    SortedSet<IAirflowTask> getInputToAirflowTasks();

    /** Processes to which this asset provides input. */
    SortedSet<ILineageProcess> getInputToProcesses();

    /** TBC */
    Boolean getIsAIGenerated();

    /** TBC */
    Boolean getIsDiscoverable();

    /** TBC */
    Boolean getIsEditable();

    /** TBC */
    Long getLastRowChangedAt();

    /** TBC */
    String getLastSyncRun();

    /** TBC */
    Long getLastSyncRunAt();

    /** TBC */
    String getLastSyncWorkflowName();

    /** Links that are attached to this asset. */
    SortedSet<ILink> getLinks();

    /** TBC */
    SortedSet<IMCIncident> getMcIncidents();

    /** Monitors that observe this asset. */
    SortedSet<IMCMonitor> getMcMonitors();

    /** TBC */
    SortedSet<IMetric> getMetrics();

    /** TBC */
    String getName();

    /** TBC */
    SortedSet<IAirflowTask> getOutputFromAirflowTasks();

    /** Processes from which this asset is produced as output. */
    SortedSet<ILineageProcess> getOutputFromProcesses();

    /** TBC */
    SortedSet<String> getOwnerGroups();

    /** TBC */
    SortedSet<String> getOwnerUsers();

    /** TBC */
    Double getPopularityScore();

    /** TBC */
    String getQualifiedName();

    /** README that is linked to this asset. */
    IReadme getReadme();

    /** TBC */
    String getSampleDataUrl();

    /** TBC */
    SortedSet<ISchemaRegistrySubject> getSchemaRegistrySubjects();

    /** TBC */
    SortedSet<ISodaCheck> getSodaChecks();

    /** TBC */
    SourceCostUnitType getSourceCostUnit();

    /** TBC */
    Long getSourceCreatedAt();

    /** TBC */
    String getSourceCreatedBy();

    /** TBC */
    String getSourceEmbedURL();

    /** TBC */
    Long getSourceLastReadAt();

    /** TBC */
    String getSourceOwners();

    /** TBC */
    List<PopularityInsights> getSourceQueryComputeCostRecords();

    /** TBC */
    SortedSet<String> getSourceQueryComputeCosts();

    /** TBC */
    Long getSourceReadCount();

    /** TBC */
    List<PopularityInsights> getSourceReadExpensiveQueryRecords();

    /** TBC */
    List<PopularityInsights> getSourceReadPopularQueryRecords();

    /** TBC */
    Double getSourceReadQueryCost();

    /** TBC */
    List<PopularityInsights> getSourceReadRecentUserRecords();

    /** TBC */
    SortedSet<String> getSourceReadRecentUsers();

    /** TBC */
    List<PopularityInsights> getSourceReadSlowQueryRecords();

    /** TBC */
    List<PopularityInsights> getSourceReadTopUserRecords();

    /** TBC */
    SortedSet<String> getSourceReadTopUsers();

    /** TBC */
    Long getSourceReadUserCount();

    /** TBC */
    Double getSourceTotalCost();

    /** TBC */
    String getSourceURL();

    /** TBC */
    Long getSourceUpdatedAt();

    /** TBC */
    String getSourceUpdatedBy();

    /** TBC */
    SortedSet<String> getStarredBy();

    /** TBC */
    Integer getStarredCount();

    /** TBC */
    List<StarredDetails> getStarredDetails();

    /** TBC */
    String getSubType();

    /** TBC */
    String getTenantId();

    /** TBC */
    String getUserDescription();

    /** TBC */
    Double getViewScore();

    /** TBC */
    SortedSet<String> getViewerGroups();

    /** TBC */
    SortedSet<String> getViewerUsers();

    /** Name of the type that defines the asset. */
    String getTypeName();

    /** Globally-unique identifier for the asset. */
    String getGuid();

    /** Human-readable name of the asset. */
    String getDisplayText();

    /** Status of the asset (if this is a related asset). */
    String getEntityStatus();

    /** Type of the relationship (if this is a related asset). */
    String getRelationshipType();

    /** Unique identifier of the relationship (when this is a related asset). */
    String getRelationshipGuid();

    /** Status of the relationship (when this is a related asset). */
    AtlanStatus getRelationshipStatus();

    /** Attributes specific to the relationship (unused). */
    Map<String, Object> getRelationshipAttributes();

    /**
     * Attribute(s) that uniquely identify the asset (when this is a related asset).
     * If the guid is not provided, these must be provided.
     */
    UniqueAttributes getUniqueAttributes();

    /**
     * When true, indicates that this object represents a complete view of the entity.
     * When false, this object is only a reference or some partial view of the entity.
     */
    boolean isComplete();

    /**
     * Indicates whether this object can be used as a valid reference by GUID.
     * @return true if it is a valid GUID reference, false otherwise
     */
    boolean isValidReferenceByGuid();

    /**
     * Indicates whether this object can be used as a valid reference by qualifiedName.
     * @return true if it is a valid qualifiedName reference, false otherwise
     */
    boolean isValidReferenceByQualifiedName();
}
