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
 * Instance of an ai model version in snowflake.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class SnowflakeAIModelVersion extends Asset
        implements ISnowflakeAIModelVersion, IAIModelVersion, ISnowflake, IAI, ICatalog, IAsset, IReferenceable, ISQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SnowflakeAIModelVersion";

    /** Fixed typeName for SnowflakeAIModelVersions. */
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

    /** Context containing the version. */
    @Attribute
    ISnowflakeAIModelContext snowflakeAIModelContext;

    /** The aliases for the model version. */
    @Attribute
    @Singular
    SortedSet<String> snowflakeAIModelVersionAliases;

    /** Functions used in the model version. */
    @Attribute
    @Singular
    SortedSet<String> snowflakeAIModelVersionFunctions;

    /** Metrics for an individual experiment. */
    @Attribute
    @Singular
    Map<String, String> snowflakeAIModelVersionMetrics;

    /** Version part of the model name. */
    @Attribute
    String snowflakeAIModelVersionName;

    /** The type of the model version. */
    @Attribute
    String snowflakeAIModelVersionType;

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
     * Builds the minimal object necessary to create a relationship to a SnowflakeAIModelVersion, from a potentially
     * more-complete SnowflakeAIModelVersion object.
     *
     * @return the minimal object necessary to relate to the SnowflakeAIModelVersion
     * @throws InvalidRequestException if any of the minimal set of required properties for a SnowflakeAIModelVersion relationship are not found in the initial object
     */
    @Override
    public SnowflakeAIModelVersion trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SnowflakeAIModelVersion assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SnowflakeAIModelVersion assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SnowflakeAIModelVersion assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SnowflakeAIModelVersion assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SnowflakeAIModelVersions will be included
     * @return a fluent search that includes all SnowflakeAIModelVersion assets
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
     * Reference to a SnowflakeAIModelVersion by GUID. Use this to create a relationship to this SnowflakeAIModelVersion,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SnowflakeAIModelVersion to reference
     * @return reference to a SnowflakeAIModelVersion that can be used for defining a relationship to a SnowflakeAIModelVersion
     */
    public static SnowflakeAIModelVersion refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SnowflakeAIModelVersion by GUID. Use this to create a relationship to this SnowflakeAIModelVersion,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SnowflakeAIModelVersion to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SnowflakeAIModelVersion that can be used for defining a relationship to a SnowflakeAIModelVersion
     */
    public static SnowflakeAIModelVersion refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SnowflakeAIModelVersion._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SnowflakeAIModelVersion by qualifiedName. Use this to create a relationship to this SnowflakeAIModelVersion,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SnowflakeAIModelVersion to reference
     * @return reference to a SnowflakeAIModelVersion that can be used for defining a relationship to a SnowflakeAIModelVersion
     */
    public static SnowflakeAIModelVersion refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SnowflakeAIModelVersion by qualifiedName. Use this to create a relationship to this SnowflakeAIModelVersion,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SnowflakeAIModelVersion to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SnowflakeAIModelVersion that can be used for defining a relationship to a SnowflakeAIModelVersion
     */
    public static SnowflakeAIModelVersion refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SnowflakeAIModelVersion._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SnowflakeAIModelVersion by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SnowflakeAIModelVersion to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SnowflakeAIModelVersion, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakeAIModelVersion does not exist or the provided GUID is not a SnowflakeAIModelVersion
     */
    @JsonIgnore
    public static SnowflakeAIModelVersion get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SnowflakeAIModelVersion by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SnowflakeAIModelVersion to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SnowflakeAIModelVersion, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakeAIModelVersion does not exist or the provided GUID is not a SnowflakeAIModelVersion
     */
    @JsonIgnore
    public static SnowflakeAIModelVersion get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SnowflakeAIModelVersion) {
                return (SnowflakeAIModelVersion) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof SnowflakeAIModelVersion) {
                return (SnowflakeAIModelVersion) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SnowflakeAIModelVersion by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SnowflakeAIModelVersion to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SnowflakeAIModelVersion, including any relationships
     * @return the requested SnowflakeAIModelVersion, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakeAIModelVersion does not exist or the provided GUID is not a SnowflakeAIModelVersion
     */
    @JsonIgnore
    public static SnowflakeAIModelVersion get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a SnowflakeAIModelVersion by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SnowflakeAIModelVersion to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SnowflakeAIModelVersion, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the SnowflakeAIModelVersion
     * @return the requested SnowflakeAIModelVersion, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakeAIModelVersion does not exist or the provided GUID is not a SnowflakeAIModelVersion
     */
    @JsonIgnore
    public static SnowflakeAIModelVersion get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = SnowflakeAIModelVersion.select(client)
                    .where(SnowflakeAIModelVersion.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof SnowflakeAIModelVersion) {
                return (SnowflakeAIModelVersion) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = SnowflakeAIModelVersion.select(client)
                    .where(SnowflakeAIModelVersion.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof SnowflakeAIModelVersion) {
                return (SnowflakeAIModelVersion) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SnowflakeAIModelVersion to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SnowflakeAIModelVersion
     * @return true if the SnowflakeAIModelVersion is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SnowflakeAIModelVersion.
     *
     * @param qualifiedName of the SnowflakeAIModelVersion
     * @param name of the SnowflakeAIModelVersion
     * @return the minimal request necessary to update the SnowflakeAIModelVersion, as a builder
     */
    public static SnowflakeAIModelVersionBuilder<?, ?> updater(String qualifiedName, String name) {
        return SnowflakeAIModelVersion._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SnowflakeAIModelVersion, from a potentially
     * more-complete SnowflakeAIModelVersion object.
     *
     * @return the minimal object necessary to update the SnowflakeAIModelVersion, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SnowflakeAIModelVersion are not found in the initial object
     */
    @Override
    public SnowflakeAIModelVersionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class SnowflakeAIModelVersionBuilder<
                    C extends SnowflakeAIModelVersion, B extends SnowflakeAIModelVersionBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a SnowflakeAIModelVersion.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SnowflakeAIModelVersion
     * @param name of the SnowflakeAIModelVersion
     * @return the updated SnowflakeAIModelVersion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeAIModelVersion removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SnowflakeAIModelVersion) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SnowflakeAIModelVersion.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SnowflakeAIModelVersion
     * @param name of the SnowflakeAIModelVersion
     * @return the updated SnowflakeAIModelVersion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeAIModelVersion removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SnowflakeAIModelVersion) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SnowflakeAIModelVersion.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SnowflakeAIModelVersion's owners
     * @param qualifiedName of the SnowflakeAIModelVersion
     * @param name of the SnowflakeAIModelVersion
     * @return the updated SnowflakeAIModelVersion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeAIModelVersion removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SnowflakeAIModelVersion) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SnowflakeAIModelVersion.
     *
     * @param client connectivity to the Atlan tenant on which to update the SnowflakeAIModelVersion's certificate
     * @param qualifiedName of the SnowflakeAIModelVersion
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SnowflakeAIModelVersion, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeAIModelVersion updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SnowflakeAIModelVersion)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SnowflakeAIModelVersion.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SnowflakeAIModelVersion's certificate
     * @param qualifiedName of the SnowflakeAIModelVersion
     * @param name of the SnowflakeAIModelVersion
     * @return the updated SnowflakeAIModelVersion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeAIModelVersion removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SnowflakeAIModelVersion) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SnowflakeAIModelVersion.
     *
     * @param client connectivity to the Atlan tenant on which to update the SnowflakeAIModelVersion's announcement
     * @param qualifiedName of the SnowflakeAIModelVersion
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeAIModelVersion updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SnowflakeAIModelVersion)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SnowflakeAIModelVersion.
     *
     * @param client connectivity to the Atlan client from which to remove the SnowflakeAIModelVersion's announcement
     * @param qualifiedName of the SnowflakeAIModelVersion
     * @param name of the SnowflakeAIModelVersion
     * @return the updated SnowflakeAIModelVersion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeAIModelVersion removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SnowflakeAIModelVersion) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SnowflakeAIModelVersion.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SnowflakeAIModelVersion's assigned terms
     * @param qualifiedName for the SnowflakeAIModelVersion
     * @param name human-readable name of the SnowflakeAIModelVersion
     * @param terms the list of terms to replace on the SnowflakeAIModelVersion, or null to remove all terms from the SnowflakeAIModelVersion
     * @return the SnowflakeAIModelVersion that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SnowflakeAIModelVersion replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SnowflakeAIModelVersion) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SnowflakeAIModelVersion, without replacing existing terms linked to the SnowflakeAIModelVersion.
     * Note: this operation must make two API calls — one to retrieve the SnowflakeAIModelVersion's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SnowflakeAIModelVersion
     * @param qualifiedName for the SnowflakeAIModelVersion
     * @param terms the list of terms to append to the SnowflakeAIModelVersion
     * @return the SnowflakeAIModelVersion that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SnowflakeAIModelVersion appendTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (SnowflakeAIModelVersion) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SnowflakeAIModelVersion, without replacing all existing terms linked to the SnowflakeAIModelVersion.
     * Note: this operation must make two API calls — one to retrieve the SnowflakeAIModelVersion's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SnowflakeAIModelVersion
     * @param qualifiedName for the SnowflakeAIModelVersion
     * @param terms the list of terms to remove from the SnowflakeAIModelVersion, which must be referenced by GUID
     * @return the SnowflakeAIModelVersion that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SnowflakeAIModelVersion removeTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (SnowflakeAIModelVersion) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SnowflakeAIModelVersion, without replacing existing Atlan tags linked to the SnowflakeAIModelVersion.
     * Note: this operation must make two API calls — one to retrieve the SnowflakeAIModelVersion's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SnowflakeAIModelVersion
     * @param qualifiedName of the SnowflakeAIModelVersion
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SnowflakeAIModelVersion
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static SnowflakeAIModelVersion appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (SnowflakeAIModelVersion) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SnowflakeAIModelVersion, without replacing existing Atlan tags linked to the SnowflakeAIModelVersion.
     * Note: this operation must make two API calls — one to retrieve the SnowflakeAIModelVersion's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SnowflakeAIModelVersion
     * @param qualifiedName of the SnowflakeAIModelVersion
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SnowflakeAIModelVersion
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static SnowflakeAIModelVersion appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SnowflakeAIModelVersion) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SnowflakeAIModelVersion.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SnowflakeAIModelVersion
     * @param qualifiedName of the SnowflakeAIModelVersion
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SnowflakeAIModelVersion
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
