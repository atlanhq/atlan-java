/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AIModelStatus;
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
 * Instance of an ai model in databricks.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class DatabricksAIModelContext extends Asset
        implements IDatabricksAIModelContext, IAIModel, IDatabricks, IAI, ICatalog, IAsset, IReferenceable, ISQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DatabricksAIModelContext";

    /** Fixed typeName for DatabricksAIModelContexts. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Search DSL used to define which assets/datasets are part of the AI model. */
    @Attribute
    String aiModelDatasetsDSL;

    /** Status of the AI model. */
    @Attribute
    AIModelStatus aiModelStatus;

    /** Version of the AI model. */
    @Attribute
    String aiModelVersion;

    /** Versions contained within the model. */
    @Attribute
    @Singular("modelVersion")
    SortedSet<IAIModelVersion> aiModelVersions;

    /** AI applications that are created using this AI model. */
    @Attribute
    @Singular("aiApplication")
    SortedSet<IAIApplication> applications;

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

    /** The id of the model, common across versions. */
    @Attribute
    String databricksAIModelContextMetastoreId;

    /** Schema containing the context. */
    @Attribute
    ISchema databricksAIModelSchema;

    /** Versions contained within the context. */
    @Attribute
    @Singular
    SortedSet<IDatabricksAIModelVersion> databricksAIModelVersions;

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
     * Builds the minimal object necessary to create a relationship to a DatabricksAIModelContext, from a potentially
     * more-complete DatabricksAIModelContext object.
     *
     * @return the minimal object necessary to relate to the DatabricksAIModelContext
     * @throws InvalidRequestException if any of the minimal set of required properties for a DatabricksAIModelContext relationship are not found in the initial object
     */
    @Override
    public DatabricksAIModelContext trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all DatabricksAIModelContext assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DatabricksAIModelContext assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all DatabricksAIModelContext assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all DatabricksAIModelContext assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DatabricksAIModelContexts will be included
     * @return a fluent search that includes all DatabricksAIModelContext assets
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
     * Reference to a DatabricksAIModelContext by GUID. Use this to create a relationship to this DatabricksAIModelContext,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the DatabricksAIModelContext to reference
     * @return reference to a DatabricksAIModelContext that can be used for defining a relationship to a DatabricksAIModelContext
     */
    public static DatabricksAIModelContext refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DatabricksAIModelContext by GUID. Use this to create a relationship to this DatabricksAIModelContext,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the DatabricksAIModelContext to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DatabricksAIModelContext that can be used for defining a relationship to a DatabricksAIModelContext
     */
    public static DatabricksAIModelContext refByGuid(String guid, Reference.SaveSemantic semantic) {
        return DatabricksAIModelContext._internal()
                .guid(guid)
                .semantic(semantic)
                .build();
    }

    /**
     * Reference to a DatabricksAIModelContext by qualifiedName. Use this to create a relationship to this DatabricksAIModelContext,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the DatabricksAIModelContext to reference
     * @return reference to a DatabricksAIModelContext that can be used for defining a relationship to a DatabricksAIModelContext
     */
    public static DatabricksAIModelContext refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DatabricksAIModelContext by qualifiedName. Use this to create a relationship to this DatabricksAIModelContext,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the DatabricksAIModelContext to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DatabricksAIModelContext that can be used for defining a relationship to a DatabricksAIModelContext
     */
    public static DatabricksAIModelContext refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return DatabricksAIModelContext._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a DatabricksAIModelContext by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DatabricksAIModelContext to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DatabricksAIModelContext, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DatabricksAIModelContext does not exist or the provided GUID is not a DatabricksAIModelContext
     */
    @JsonIgnore
    public static DatabricksAIModelContext get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a DatabricksAIModelContext by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DatabricksAIModelContext to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full DatabricksAIModelContext, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DatabricksAIModelContext does not exist or the provided GUID is not a DatabricksAIModelContext
     */
    @JsonIgnore
    public static DatabricksAIModelContext get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof DatabricksAIModelContext) {
                return (DatabricksAIModelContext) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof DatabricksAIModelContext) {
                return (DatabricksAIModelContext) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a DatabricksAIModelContext by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DatabricksAIModelContext to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DatabricksAIModelContext, including any relationships
     * @return the requested DatabricksAIModelContext, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DatabricksAIModelContext does not exist or the provided GUID is not a DatabricksAIModelContext
     */
    @JsonIgnore
    public static DatabricksAIModelContext get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a DatabricksAIModelContext by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DatabricksAIModelContext to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DatabricksAIModelContext, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the DatabricksAIModelContext
     * @return the requested DatabricksAIModelContext, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DatabricksAIModelContext does not exist or the provided GUID is not a DatabricksAIModelContext
     */
    @JsonIgnore
    public static DatabricksAIModelContext get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = DatabricksAIModelContext.select(client)
                    .where(DatabricksAIModelContext.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof DatabricksAIModelContext) {
                return (DatabricksAIModelContext) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = DatabricksAIModelContext.select(client)
                    .where(DatabricksAIModelContext.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof DatabricksAIModelContext) {
                return (DatabricksAIModelContext) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) DatabricksAIModelContext to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DatabricksAIModelContext
     * @return true if the DatabricksAIModelContext is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a DatabricksAIModelContext.
     *
     * @param qualifiedName of the DatabricksAIModelContext
     * @param name of the DatabricksAIModelContext
     * @return the minimal request necessary to update the DatabricksAIModelContext, as a builder
     */
    public static DatabricksAIModelContextBuilder<?, ?> updater(String qualifiedName, String name) {
        return DatabricksAIModelContext._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DatabricksAIModelContext, from a potentially
     * more-complete DatabricksAIModelContext object.
     *
     * @return the minimal object necessary to update the DatabricksAIModelContext, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DatabricksAIModelContext are not found in the initial object
     */
    @Override
    public DatabricksAIModelContextBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class DatabricksAIModelContextBuilder<
                    C extends DatabricksAIModelContext, B extends DatabricksAIModelContextBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a DatabricksAIModelContext.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DatabricksAIModelContext
     * @param name of the DatabricksAIModelContext
     * @return the updated DatabricksAIModelContext, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksAIModelContext removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DatabricksAIModelContext) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DatabricksAIModelContext.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DatabricksAIModelContext
     * @param name of the DatabricksAIModelContext
     * @return the updated DatabricksAIModelContext, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksAIModelContext removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DatabricksAIModelContext) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DatabricksAIModelContext.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DatabricksAIModelContext's owners
     * @param qualifiedName of the DatabricksAIModelContext
     * @param name of the DatabricksAIModelContext
     * @return the updated DatabricksAIModelContext, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksAIModelContext removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DatabricksAIModelContext) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DatabricksAIModelContext.
     *
     * @param client connectivity to the Atlan tenant on which to update the DatabricksAIModelContext's certificate
     * @param qualifiedName of the DatabricksAIModelContext
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DatabricksAIModelContext, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksAIModelContext updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DatabricksAIModelContext)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DatabricksAIModelContext.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DatabricksAIModelContext's certificate
     * @param qualifiedName of the DatabricksAIModelContext
     * @param name of the DatabricksAIModelContext
     * @return the updated DatabricksAIModelContext, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksAIModelContext removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DatabricksAIModelContext) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DatabricksAIModelContext.
     *
     * @param client connectivity to the Atlan tenant on which to update the DatabricksAIModelContext's announcement
     * @param qualifiedName of the DatabricksAIModelContext
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksAIModelContext updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (DatabricksAIModelContext)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DatabricksAIModelContext.
     *
     * @param client connectivity to the Atlan client from which to remove the DatabricksAIModelContext's announcement
     * @param qualifiedName of the DatabricksAIModelContext
     * @param name of the DatabricksAIModelContext
     * @return the updated DatabricksAIModelContext, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksAIModelContext removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DatabricksAIModelContext) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DatabricksAIModelContext.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DatabricksAIModelContext's assigned terms
     * @param qualifiedName for the DatabricksAIModelContext
     * @param name human-readable name of the DatabricksAIModelContext
     * @param terms the list of terms to replace on the DatabricksAIModelContext, or null to remove all terms from the DatabricksAIModelContext
     * @return the DatabricksAIModelContext that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DatabricksAIModelContext replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (DatabricksAIModelContext) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DatabricksAIModelContext, without replacing existing terms linked to the DatabricksAIModelContext.
     * Note: this operation must make two API calls — one to retrieve the DatabricksAIModelContext's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DatabricksAIModelContext
     * @param qualifiedName for the DatabricksAIModelContext
     * @param terms the list of terms to append to the DatabricksAIModelContext
     * @return the DatabricksAIModelContext that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DatabricksAIModelContext appendTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (DatabricksAIModelContext) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DatabricksAIModelContext, without replacing all existing terms linked to the DatabricksAIModelContext.
     * Note: this operation must make two API calls — one to retrieve the DatabricksAIModelContext's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DatabricksAIModelContext
     * @param qualifiedName for the DatabricksAIModelContext
     * @param terms the list of terms to remove from the DatabricksAIModelContext, which must be referenced by GUID
     * @return the DatabricksAIModelContext that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DatabricksAIModelContext removeTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (DatabricksAIModelContext) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DatabricksAIModelContext, without replacing existing Atlan tags linked to the DatabricksAIModelContext.
     * Note: this operation must make two API calls — one to retrieve the DatabricksAIModelContext's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DatabricksAIModelContext
     * @param qualifiedName of the DatabricksAIModelContext
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DatabricksAIModelContext
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static DatabricksAIModelContext appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (DatabricksAIModelContext) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DatabricksAIModelContext, without replacing existing Atlan tags linked to the DatabricksAIModelContext.
     * Note: this operation must make two API calls — one to retrieve the DatabricksAIModelContext's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DatabricksAIModelContext
     * @param qualifiedName of the DatabricksAIModelContext
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DatabricksAIModelContext
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static DatabricksAIModelContext appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DatabricksAIModelContext) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a DatabricksAIModelContext.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DatabricksAIModelContext
     * @param qualifiedName of the DatabricksAIModelContext
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DatabricksAIModelContext
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
