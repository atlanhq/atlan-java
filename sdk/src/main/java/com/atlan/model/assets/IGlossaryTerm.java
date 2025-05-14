/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanIcon;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.enums.AtlasGlossaryTermType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.SourceCostUnitType;
import com.atlan.model.fields.KeywordField;
import com.atlan.model.fields.RelationField;
import com.atlan.model.fields.TextField;
import com.atlan.model.relations.RelationshipAttributes;
import com.atlan.model.relations.UniqueAttributes;
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
 * Instance of a term in Atlan. Terms define concepts in natural language that can be associated with other assets to provide meaning.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@JsonSerialize(using = AssetSerializer.class)
@JsonDeserialize(using = AssetDeserializer.class)
public interface IGlossaryTerm {

    public static final String TYPE_NAME = "AtlasGlossaryTerm";

    /** TBC */
    TextField ABBREVIATION = new TextField("abbreviation", "abbreviation");

    /** TBC */
    KeywordField ADDITIONAL_ATTRIBUTES = new KeywordField("additionalAttributes", "additionalAttributes");

    /** TBC */
    RelationField ANTONYMS = new RelationField("antonyms");

    /** TBC */
    RelationField ASSIGNED_ENTITIES = new RelationField("assignedEntities");

    /** TBC */
    RelationField CLASSIFIES = new RelationField("classifies");

    /** TBC */
    TextField EXAMPLES = new TextField("examples", "examples");

    /** TBC */
    RelationField IS_A = new RelationField("isA");

    /** TBC */
    TextField LONG_DESCRIPTION = new TextField("longDescription", "longDescription");

    /** TBC */
    RelationField PREFERRED_TERMS = new RelationField("preferredTerms");

    /** TBC */
    RelationField PREFERRED_TO_TERMS = new RelationField("preferredToTerms");

    /** TBC */
    RelationField REPLACED_BY = new RelationField("replacedBy");

    /** TBC */
    RelationField REPLACEMENT_TERMS = new RelationField("replacementTerms");

    /** TBC */
    RelationField SEE_ALSO = new RelationField("seeAlso");

    /** TBC */
    TextField SHORT_DESCRIPTION = new TextField("shortDescription", "shortDescription");

    /** TBC */
    RelationField SYNONYMS = new RelationField("synonyms");

    /** TBC */
    KeywordField TERM_TYPE = new KeywordField("termType", "termType");

    /** TBC */
    RelationField TRANSLATED_TERMS = new RelationField("translatedTerms");

    /** TBC */
    RelationField TRANSLATION_TERMS = new RelationField("translationTerms");

    /** TBC */
    TextField USAGE = new TextField("usage", "usage");

    /** TBC */
    RelationField VALID_VALUES = new RelationField("validValues");

    /** TBC */
    RelationField VALID_VALUES_FOR = new RelationField("validValuesFor");

    /** Glossary in which the term is contained, searchable by the qualifiedName of the glossary. */
    KeywordField ANCHOR = new KeywordField("anchor", "__glossary");

    /** Categories in which the term is organized, searchable by the qualifiedName of the category. */
    KeywordField CATEGORIES = new KeywordField("categories", "__categories");

    /** TBC */
    String getAbbreviation();

    /** TBC */
    Map<String, String> getAdditionalAttributes();

    /** TBC */
    SortedSet<String> getAdminGroups();

    /** TBC */
    SortedSet<String> getAdminRoles();

    /** TBC */
    SortedSet<String> getAdminUsers();

    /** TBC */
    IGlossary getAnchor();

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
    SortedSet<IAnomaloCheck> getAnomaloChecks();

    /** TBC */
    SortedSet<IGlossaryTerm> getAntonyms();

    /** TBC */
    IApplication getApplication();

    /** TBC */
    IApplicationField getApplicationField();

    /** TBC */
    String getApplicationFieldQualifiedName();

    /** TBC */
    String getApplicationQualifiedName();

    /** TBC */
    SortedSet<String> getAssetAnomaloAppliedCheckTypes();

    /** TBC */
    Long getAssetAnomaloCheckCount();

    /** TBC */
    String getAssetAnomaloCheckStatuses();

    /** TBC */
    String getAssetAnomaloDQStatus();

    /** TBC */
    Long getAssetAnomaloFailedCheckCount();

    /** TBC */
    SortedSet<String> getAssetAnomaloFailedCheckTypes();

    /** TBC */
    Long getAssetAnomaloLastCheckRunAt();

    /** TBC */
    String getAssetAnomaloSourceUrl();

    /** TBC */
    String getAssetCoverImage();

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
    String getAssetDbtWorkflowLastUpdated();

    /** TBC */
    AtlanIcon getAssetIcon();

    /** TBC */
    SortedSet<String> getAssetMcAlertQualifiedNames();

    /** TBC */
    SortedSet<String> getAssetMcIncidentNames();

    /** TBC */
    SortedSet<String> getAssetMcIncidentPriorities();

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
    Boolean getAssetMcIsMonitored();

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
    Long getAssetPoliciesCount();

    /** TBC */
    SortedSet<String> getAssetPolicyGUIDs();

    /** TBC */
    SortedSet<String> getAssetRedirectGUIDs();

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

    /** TBC */
    String getAssetThemeHex();

    /** TBC */
    SortedSet<IAsset> getAssignedEntities();

    /** TBC */
    SortedSet<IGlossaryTerm> getAssignedTerms();

    /** TBC */
    SortedSet<IGlossaryCategory> getCategories();

    /** TBC */
    CertificateStatus getCertificateStatus();

    /** TBC */
    String getCertificateStatusMessage();

    /** TBC */
    Long getCertificateUpdatedAt();

    /** TBC */
    String getCertificateUpdatedBy();

    /** TBC */
    SortedSet<IGlossaryTerm> getClassifies();

    /** TBC */
    String getConnectionName();

    /** TBC */
    String getConnectionQualifiedName();

    /** TBC */
    String getConnectorName();

    /** TBC */
    IDataContract getDataContractLatest();

    /** TBC */
    IDataContract getDataContractLatestCertified();

    /** TBC */
    String getDbtQualifiedName();

    /** TBC */
    String getDescription();

    /** TBC */
    String getDisplayName();

    /** TBC */
    SortedSet<String> getDomainGUIDs();

    /** TBC */
    SortedSet<String> getExamples();

    /** TBC */
    SortedSet<IFile> getFiles();

    /** TBC */
    Boolean getHasContract();

    /** TBC */
    Boolean getHasLineage();

    /** TBC */
    SortedSet<IDataProduct> getInputPortDataProducts();

    /** TBC */
    SortedSet<IGlossaryTerm> getIsA();

    /** TBC */
    Boolean getIsAIGenerated();

    /** TBC */
    Boolean getIsDiscoverable();

    /** TBC */
    Boolean getIsEditable();

    /** TBC */
    Boolean getIsPartial();

    /** TBC */
    Long getLastRowChangedAt();

    /** TBC */
    String getLastSyncRun();

    /** TBC */
    Long getLastSyncRunAt();

    /** TBC */
    String getLastSyncWorkflowName();

    /** TBC */
    String getLexicographicalSortOrder();

    /** TBC */
    SortedSet<ILink> getLinks();

    /** TBC */
    String getLongDescription();

    /** TBC */
    SortedSet<IMCIncident> getMcIncidents();

    /** TBC */
    SortedSet<IMCMonitor> getMcMonitors();

    /** TBC */
    SortedSet<IMetric> getMetrics();

    /** TBC */
    String getName();

    /** TBC */
    SortedSet<String> getNonCompliantAssetPolicyGUIDs();

    /** TBC */
    SortedSet<IDataProduct> getOutputPortDataProducts();

    /** TBC */
    SortedSet<String> getOutputProductGUIDs();

    /** TBC */
    SortedSet<String> getOwnerGroups();

    /** TBC */
    SortedSet<String> getOwnerUsers();

    /** TBC */
    Double getPopularityScore();

    /** TBC */
    SortedSet<IGlossaryTerm> getPreferredTerms();

    /** TBC */
    SortedSet<IGlossaryTerm> getPreferredToTerms();

    /** TBC */
    SortedSet<String> getProductGUIDs();

    /** TBC */
    String getQualifiedName();

    /** TBC */
    IReadme getReadme();

    /** TBC */
    SortedSet<IGlossaryTerm> getReplacedBy();

    /** TBC */
    SortedSet<IGlossaryTerm> getReplacementTerms();

    /** TBC */
    String getSampleDataUrl();

    /** TBC */
    SortedSet<ISchemaRegistrySubject> getSchemaRegistrySubjects();

    /** TBC */
    SortedSet<IGlossaryTerm> getSeeAlso();

    /** TBC */
    String getShortDescription();

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
    SortedSet<IGlossaryTerm> getSynonyms();

    /** TBC */
    String getTenantId();

    /** TBC */
    AtlasGlossaryTermType getTermType();

    /** TBC */
    SortedSet<IGlossaryTerm> getTranslatedTerms();

    /** TBC */
    SortedSet<IGlossaryTerm> getTranslationTerms();

    /** TBC */
    String getUsage();

    /** TBC */
    SortedSet<IAsset> getUserDefRelationshipFroms();

    /** TBC */
    SortedSet<IAsset> getUserDefRelationshipTos();

    /** TBC */
    String getUserDescription();

    /** TBC */
    SortedSet<IGlossaryTerm> getValidValues();

    /** TBC */
    SortedSet<IGlossaryTerm> getValidValuesFor();

    /** TBC */
    Double getViewScore();

    /** TBC */
    SortedSet<String> getViewerGroups();

    /** TBC */
    SortedSet<String> getViewerUsers();

    /** URL of an icon to use for this asset. (Only applies to CustomEntity and Fivetran Catalog assets, currently.) */
    String getIconUrl();

    /** Built-in connector type through which this asset is accessible. */
    AtlanConnectorType getConnectorType();

    /** Custom connector type through which this asset is accessible. */
    String getCustomConnectorType();

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
    RelationshipAttributes getRelationshipAttributes();

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
