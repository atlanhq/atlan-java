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
import com.atlan.model.enums.EthicalAIAccountabilityConfig;
import com.atlan.model.enums.EthicalAIBiasMitigationConfig;
import com.atlan.model.enums.EthicalAIEnvironmentalConsciousnessConfig;
import com.atlan.model.enums.EthicalAIFairnessConfig;
import com.atlan.model.enums.EthicalAIPrivacyConfig;
import com.atlan.model.enums.EthicalAIReliabilityAndSafetyConfig;
import com.atlan.model.enums.EthicalAITransparencyConfig;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.model.structs.DatabricksAIModelVersionMetric;
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
 * Instance of an ai model version in databricks.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class DatabricksAIModelVersion extends Asset
        implements IDatabricksAIModelVersion,
                IAIModelVersion,
                IDatabricks,
                IAI,
                ICatalog,
                IAsset,
                IReferenceable,
                ISQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DatabricksAIModelVersion";

    /** Fixed typeName for DatabricksAIModelVersions. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Model containing the versions. */
    @Attribute
    IAIModel aiModel;

    /** Simple name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewName;

    /** Unique name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewQualifiedName;

    /** Simple name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    @Attribute
    String databaseName;

    /** Unique name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    @Attribute
    String databaseQualifiedName;

    /** Context containing the version. */
    @Attribute
    IDatabricksAIModelContext databricksAIModelContext;

    /** The aliases of the model. */
    @Attribute
    @Singular
    SortedSet<String> databricksAIModelVersionAliases;

    /** Artifact uri for the model. */
    @Attribute
    String databricksAIModelVersionArtifactUri;

    /** Number of datasets. */
    @Attribute
    Long databricksAIModelVersionDatasetCount;

    /** The id of the model, unique to every version. */
    @Attribute
    Long databricksAIModelVersionId;

    /** Metrics for an individual experiment. */
    @Attribute
    @Singular
    List<DatabricksAIModelVersionMetric> databricksAIModelVersionMetrics;

    /** Params with key mapped to value for an individual experiment. */
    @Attribute
    @Singular
    Map<String, String> databricksAIModelVersionParams;

    /** The run end time of the model. */
    @Attribute
    @Date
    Long databricksAIModelVersionRunEndTime;

    /** The run id of the model. */
    @Attribute
    String databricksAIModelVersionRunId;

    /** The run name of the model. */
    @Attribute
    String databricksAIModelVersionRunName;

    /** The run start time of the model. */
    @Attribute
    @Date
    Long databricksAIModelVersionRunStartTime;

    /** Source artifact link for the model. */
    @Attribute
    String databricksAIModelVersionSource;

    /** The status of the model. */
    @Attribute
    String databricksAIModelVersionStatus;

    /** (Deprecated) Model containing the assets. */
    @Attribute
    @Singular
    SortedSet<IDbtModel> dbtModels;

    /** DBT seeds that materialize the SQL asset. */
    @Attribute
    @Singular
    SortedSet<IDbtSeed> dbtSeedAssets;

    /** Source containing the assets. */
    @Attribute
    @Singular
    SortedSet<IDbtSource> dbtSources;

    /** Tests related to this asset. */
    @Attribute
    @Singular
    SortedSet<IDbtTest> dbtTests;

    /** Accountability configuration for ensuring the ethical use of an AI asset */
    @Attribute
    EthicalAIAccountabilityConfig ethicalAIAccountabilityConfig;

    /** Bias mitigation configuration for ensuring the ethical use of an AI asset */
    @Attribute
    EthicalAIBiasMitigationConfig ethicalAIBiasMitigationConfig;

    /** Environmental consciousness configuration for ensuring the ethical use of an AI asset */
    @Attribute
    EthicalAIEnvironmentalConsciousnessConfig ethicalAIEnvironmentalConsciousnessConfig;

    /** Fairness configuration for ensuring the ethical use of an AI asset */
    @Attribute
    EthicalAIFairnessConfig ethicalAIFairnessConfig;

    /** Privacy configuration for ensuring the ethical use of an AI asset */
    @Attribute
    EthicalAIPrivacyConfig ethicalAIPrivacyConfig;

    /** Reliability and safety configuration for ensuring the ethical use of an AI asset */
    @Attribute
    EthicalAIReliabilityAndSafetyConfig ethicalAIReliabilityAndSafetyConfig;

    /** Transparency configuration for ensuring the ethical use of an AI asset */
    @Attribute
    EthicalAITransparencyConfig ethicalAITransparencyConfig;

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

    /** Whether this asset has been profiled (true) or not (false). */
    @Attribute
    Boolean isProfiled;

    /** Time (epoch) at which this asset was last profiled, in milliseconds. */
    @Attribute
    @Date
    Long lastProfiledAt;

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

    /** Number of times this asset has been queried. */
    @Attribute
    Long queryCount;

    /** Time (epoch) at which the query count was last updated, in milliseconds. */
    @Attribute
    @Date
    Long queryCountUpdatedAt;

    /** Number of unique users who have queried this asset. */
    @Attribute
    Long queryUserCount;

    /** Map of unique users who have queried this asset to the number of times they have queried it. */
    @Attribute
    @Singular("putQueryUserMap")
    Map<String, Long> queryUserMap;

    /** Simple name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaName;

    /** Unique name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaQualifiedName;

    /** Unique name of the context in which the model versions exist, or empty if it does not exist within an AI model context. */
    @Attribute
    String sqlAIModelContextQualifiedName;

    /** Sources related to this asset. */
    @Attribute
    @Singular
    SortedSet<IDbtSource> sqlDBTSources;

    /** Assets related to the model. */
    @Attribute
    @Singular
    SortedSet<IDbtModel> sqlDbtModels;

    /** Whether this asset is secure (true) or not (false). */
    @Attribute
    Boolean sqlIsSecure;

    /** Simple name of the table in which this SQL asset exists, or empty if it does not exist within a table. */
    @Attribute
    String tableName;

    /** Unique name of the table in which this SQL asset exists, or empty if it does not exist within a table. */
    @Attribute
    String tableQualifiedName;

    /** Simple name of the view in which this SQL asset exists, or empty if it does not exist within a view. */
    @Attribute
    String viewName;

    /** Unique name of the view in which this SQL asset exists, or empty if it does not exist within a view. */
    @Attribute
    String viewQualifiedName;

    /**
     * Builds the minimal object necessary to create a relationship to a DatabricksAIModelVersion, from a potentially
     * more-complete DatabricksAIModelVersion object.
     *
     * @return the minimal object necessary to relate to the DatabricksAIModelVersion
     * @throws InvalidRequestException if any of the minimal set of required properties for a DatabricksAIModelVersion relationship are not found in the initial object
     */
    @Override
    public DatabricksAIModelVersion trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all DatabricksAIModelVersion assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DatabricksAIModelVersion assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all DatabricksAIModelVersion assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all DatabricksAIModelVersion assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DatabricksAIModelVersions will be included
     * @return a fluent search that includes all DatabricksAIModelVersion assets
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
     * Reference to a DatabricksAIModelVersion by GUID. Use this to create a relationship to this DatabricksAIModelVersion,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the DatabricksAIModelVersion to reference
     * @return reference to a DatabricksAIModelVersion that can be used for defining a relationship to a DatabricksAIModelVersion
     */
    public static DatabricksAIModelVersion refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DatabricksAIModelVersion by GUID. Use this to create a relationship to this DatabricksAIModelVersion,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the DatabricksAIModelVersion to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DatabricksAIModelVersion that can be used for defining a relationship to a DatabricksAIModelVersion
     */
    public static DatabricksAIModelVersion refByGuid(String guid, Reference.SaveSemantic semantic) {
        return DatabricksAIModelVersion._internal()
                .guid(guid)
                .semantic(semantic)
                .build();
    }

    /**
     * Reference to a DatabricksAIModelVersion by qualifiedName. Use this to create a relationship to this DatabricksAIModelVersion,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the DatabricksAIModelVersion to reference
     * @return reference to a DatabricksAIModelVersion that can be used for defining a relationship to a DatabricksAIModelVersion
     */
    public static DatabricksAIModelVersion refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DatabricksAIModelVersion by qualifiedName. Use this to create a relationship to this DatabricksAIModelVersion,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the DatabricksAIModelVersion to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DatabricksAIModelVersion that can be used for defining a relationship to a DatabricksAIModelVersion
     */
    public static DatabricksAIModelVersion refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return DatabricksAIModelVersion._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a DatabricksAIModelVersion by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DatabricksAIModelVersion to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DatabricksAIModelVersion, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DatabricksAIModelVersion does not exist or the provided GUID is not a DatabricksAIModelVersion
     */
    @JsonIgnore
    public static DatabricksAIModelVersion get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a DatabricksAIModelVersion by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DatabricksAIModelVersion to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full DatabricksAIModelVersion, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DatabricksAIModelVersion does not exist or the provided GUID is not a DatabricksAIModelVersion
     */
    @JsonIgnore
    public static DatabricksAIModelVersion get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof DatabricksAIModelVersion) {
                return (DatabricksAIModelVersion) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof DatabricksAIModelVersion) {
                return (DatabricksAIModelVersion) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a DatabricksAIModelVersion by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DatabricksAIModelVersion to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DatabricksAIModelVersion, including any relationships
     * @return the requested DatabricksAIModelVersion, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DatabricksAIModelVersion does not exist or the provided GUID is not a DatabricksAIModelVersion
     */
    @JsonIgnore
    public static DatabricksAIModelVersion get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a DatabricksAIModelVersion by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DatabricksAIModelVersion to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DatabricksAIModelVersion, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the DatabricksAIModelVersion
     * @return the requested DatabricksAIModelVersion, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DatabricksAIModelVersion does not exist or the provided GUID is not a DatabricksAIModelVersion
     */
    @JsonIgnore
    public static DatabricksAIModelVersion get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = DatabricksAIModelVersion.select(client)
                    .where(DatabricksAIModelVersion.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof DatabricksAIModelVersion) {
                return (DatabricksAIModelVersion) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = DatabricksAIModelVersion.select(client)
                    .where(DatabricksAIModelVersion.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof DatabricksAIModelVersion) {
                return (DatabricksAIModelVersion) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) DatabricksAIModelVersion to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DatabricksAIModelVersion
     * @return true if the DatabricksAIModelVersion is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a DatabricksAIModelVersion.
     *
     * @param qualifiedName of the DatabricksAIModelVersion
     * @param name of the DatabricksAIModelVersion
     * @return the minimal request necessary to update the DatabricksAIModelVersion, as a builder
     */
    public static DatabricksAIModelVersionBuilder<?, ?> updater(String qualifiedName, String name) {
        return DatabricksAIModelVersion._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DatabricksAIModelVersion, from a potentially
     * more-complete DatabricksAIModelVersion object.
     *
     * @return the minimal object necessary to update the DatabricksAIModelVersion, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DatabricksAIModelVersion are not found in the initial object
     */
    @Override
    public DatabricksAIModelVersionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class DatabricksAIModelVersionBuilder<
                    C extends DatabricksAIModelVersion, B extends DatabricksAIModelVersionBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a DatabricksAIModelVersion.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DatabricksAIModelVersion
     * @param name of the DatabricksAIModelVersion
     * @return the updated DatabricksAIModelVersion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksAIModelVersion removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DatabricksAIModelVersion) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DatabricksAIModelVersion.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DatabricksAIModelVersion
     * @param name of the DatabricksAIModelVersion
     * @return the updated DatabricksAIModelVersion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksAIModelVersion removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DatabricksAIModelVersion) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DatabricksAIModelVersion.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DatabricksAIModelVersion's owners
     * @param qualifiedName of the DatabricksAIModelVersion
     * @param name of the DatabricksAIModelVersion
     * @return the updated DatabricksAIModelVersion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksAIModelVersion removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DatabricksAIModelVersion) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DatabricksAIModelVersion.
     *
     * @param client connectivity to the Atlan tenant on which to update the DatabricksAIModelVersion's certificate
     * @param qualifiedName of the DatabricksAIModelVersion
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DatabricksAIModelVersion, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksAIModelVersion updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DatabricksAIModelVersion)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DatabricksAIModelVersion.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DatabricksAIModelVersion's certificate
     * @param qualifiedName of the DatabricksAIModelVersion
     * @param name of the DatabricksAIModelVersion
     * @return the updated DatabricksAIModelVersion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksAIModelVersion removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DatabricksAIModelVersion) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DatabricksAIModelVersion.
     *
     * @param client connectivity to the Atlan tenant on which to update the DatabricksAIModelVersion's announcement
     * @param qualifiedName of the DatabricksAIModelVersion
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksAIModelVersion updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (DatabricksAIModelVersion)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DatabricksAIModelVersion.
     *
     * @param client connectivity to the Atlan client from which to remove the DatabricksAIModelVersion's announcement
     * @param qualifiedName of the DatabricksAIModelVersion
     * @param name of the DatabricksAIModelVersion
     * @return the updated DatabricksAIModelVersion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksAIModelVersion removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DatabricksAIModelVersion) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DatabricksAIModelVersion.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DatabricksAIModelVersion's assigned terms
     * @param qualifiedName for the DatabricksAIModelVersion
     * @param name human-readable name of the DatabricksAIModelVersion
     * @param terms the list of terms to replace on the DatabricksAIModelVersion, or null to remove all terms from the DatabricksAIModelVersion
     * @return the DatabricksAIModelVersion that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DatabricksAIModelVersion replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (DatabricksAIModelVersion) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DatabricksAIModelVersion, without replacing existing terms linked to the DatabricksAIModelVersion.
     * Note: this operation must make two API calls — one to retrieve the DatabricksAIModelVersion's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DatabricksAIModelVersion
     * @param qualifiedName for the DatabricksAIModelVersion
     * @param terms the list of terms to append to the DatabricksAIModelVersion
     * @return the DatabricksAIModelVersion that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DatabricksAIModelVersion appendTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (DatabricksAIModelVersion) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DatabricksAIModelVersion, without replacing all existing terms linked to the DatabricksAIModelVersion.
     * Note: this operation must make two API calls — one to retrieve the DatabricksAIModelVersion's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DatabricksAIModelVersion
     * @param qualifiedName for the DatabricksAIModelVersion
     * @param terms the list of terms to remove from the DatabricksAIModelVersion, which must be referenced by GUID
     * @return the DatabricksAIModelVersion that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DatabricksAIModelVersion removeTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (DatabricksAIModelVersion) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DatabricksAIModelVersion, without replacing existing Atlan tags linked to the DatabricksAIModelVersion.
     * Note: this operation must make two API calls — one to retrieve the DatabricksAIModelVersion's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DatabricksAIModelVersion
     * @param qualifiedName of the DatabricksAIModelVersion
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DatabricksAIModelVersion
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static DatabricksAIModelVersion appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (DatabricksAIModelVersion) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DatabricksAIModelVersion, without replacing existing Atlan tags linked to the DatabricksAIModelVersion.
     * Note: this operation must make two API calls — one to retrieve the DatabricksAIModelVersion's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DatabricksAIModelVersion
     * @param qualifiedName of the DatabricksAIModelVersion
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DatabricksAIModelVersion
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static DatabricksAIModelVersion appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DatabricksAIModelVersion) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a DatabricksAIModelVersion.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DatabricksAIModelVersion
     * @param qualifiedName of the DatabricksAIModelVersion
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DatabricksAIModelVersion
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
