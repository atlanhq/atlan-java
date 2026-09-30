/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AgentType;
import com.atlan.model.enums.AgenticLifecycleStatus;
import com.atlan.model.enums.AgenticSource;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
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
 * Instance of a Databricks Genie space in Atlan. A Genie space is a curated natural-language interface over a set of Databricks tables, published here as an agent asset for governance and discovery.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class DatabricksGenieAgent extends Asset
        implements IDatabricksGenieAgent, IAgent, IDatabricks, IAgentic, ICatalog, IAsset, IReferenceable, ISQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DatabricksGenieAgent";

    /** Fixed typeName for DatabricksGenieAgents. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** JSON-serialized LLMConfig (model, temperature, maxTokens, maxTurns, baseUrl). */
    @Attribute
    String agentLlmConfig;

    /** JSON list of MCPServerConfig entries (name, url, headers, enabled). */
    @Attribute
    String agentMcpServers;

    /** JSON-serialized agent schedule configuration, including kickoff message, cron expression, timezone, version policy, status, and Temporal schedule identifier. */
    @Attribute
    String agentSchedules;

    /** Denormalized list of names of the skills bound to this agent version. */
    @Attribute
    @Singular
    SortedSet<String> agentSkillNames;

    /** Denormalized list of qualifiedNames of the skills bound to this agent version. */
    @Attribute
    @Singular
    SortedSet<String> agentSkillQualifiedNames;

    /** Skills bound to this agent. */
    @Attribute
    @Singular
    SortedSet<ISkill> agentSkills;

    /** URL-safe unique identifier for this agent (for example, my-data-agent). */
    @Attribute
    String agentSlug;

    /** Lifecycle status of this agent version (draft or published). */
    @Attribute
    AgenticLifecycleStatus agentStatus;

    /** System prompt for this agent version. */
    @Attribute
    String agentSystemPrompt;

    /** Origin type of this agent — system-provided or custom user-created. */
    @Attribute
    AgentType agentType;

    /** Product surface this agentic asset was created from, so agents and skills can be attributed to their originating surface without slug pattern matching (AUT-1074). Mirrors AtlanAppWorkflow.source, which does the same for workflows (AUT-1028). */
    @Attribute
    AgenticSource agenticSource;

    /** Version of this agentic asset as an epoch-millisecond timestamp. One Atlan entity per (slug, version) tuple. */
    @Attribute
    Long agenticVersion;

    /** Simple name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewName;

    /** Unique name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewQualifiedName;

    /** Unique identifier of the dataset this asset belongs to. */
    @Attribute
    String catalogDatasetGuid;

    /** Context repository that produced this agent. */
    @Attribute
    IContextRepository contextSourceRepository;

    /** Simple name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    @Attribute
    String databaseName;

    /** Unique name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    @Attribute
    String databaseQualifiedName;

    /** Entity tag used as a change token for the Genie space. It is populated only by an enabled serialized-detail read, so it is null when that read is disabled, denied, or omitted by the source. */
    @Attribute
    String databricksGenieAgentEtag;

    /** Workspace folder path containing the Genie space. It is descriptive only and creates no containment or hierarchy edge. */
    @Attribute
    String databricksGenieAgentParentPath;

    /** Identifier of the SQL warehouse backing the Genie space. */
    @Attribute
    String databricksGenieAgentWarehouseId;

    /** Identifier of the workspace containing the Genie space. */
    @Attribute
    String databricksGenieAgentWorkspaceId;

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

    /** Partial fields contained in the asset. */
    @Attribute
    @Singular
    SortedSet<IPartialField> partialChildFields;

    /** Partial objects contained in the asset. */
    @Attribute
    @Singular
    SortedSet<IPartialObject> partialChildObjects;

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

    /** Time (epoch) at which this asset was last analyzed for AI insights, in milliseconds. */
    @Attribute
    @Date
    Long sqlAiInsightsLastAnalyzedAt;

    /** Number of popular business questions associated with this asset. */
    @Attribute
    Integer sqlAiInsightsPopularBusinessQuestionCount;

    /** Number of popular filter patterns associated with this asset. */
    @Attribute
    Integer sqlAiInsightsPopularFilterCount;

    /** Number of popular join patterns associated with this asset. */
    @Attribute
    Integer sqlAiInsightsPopularJoinCount;

    /** Number of relationship insights associated with this asset. */
    @Attribute
    Integer sqlAiInsightsRelationshipCount;

    /** Identifier of the Coalesce environment. */
    @Attribute
    String sqlCoalesceEnvironmentId;

    /** Name of the Coalesce environment. */
    @Attribute
    String sqlCoalesceEnvironmentName;

    /** Time (epoch) at which the Coalesce node that materialized this asset last ran, in milliseconds. */
    @Attribute
    @Date
    Long sqlCoalesceLastRunAt;

    /** Status of the Coalesce run. One of: success, failure, cancelled, or skipped. */
    @Attribute
    String sqlCoalesceLastRunStatus;

    /** Status of the Coalesce node for a given run. */
    @Attribute
    String sqlCoalesceNodeStatus;

    /** Type of the Coalesce node. */
    @Attribute
    String sqlCoalesceNodeType;

    /** Identifier of the Coalesce project. */
    @Attribute
    String sqlCoalesceProjectId;

    /** Name of the Coalesce project. */
    @Attribute
    String sqlCoalesceProjectName;

    /** Sources related to this asset. */
    @Attribute
    @Singular
    SortedSet<IDbtSource> sqlDBTSources;

    /** Assets related to the model. */
    @Attribute
    @Singular
    SortedSet<IDbtModel> sqlDbtModels;

    /** Whether this asset has any AI insights data available. */
    @Attribute
    Boolean sqlHasAiInsights;

    /** Whether this asset is secure (true) or not (false). */
    @Attribute
    Boolean sqlIsSecure;

    /** Qualified names of data shares this asset is granted to. */
    @Attribute
    @Singular
    SortedSet<String> sqlShareQualifiedNames;

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
     * Builds the minimal object necessary to create a relationship to a DatabricksGenieAgent, from a potentially
     * more-complete DatabricksGenieAgent object.
     *
     * @return the minimal object necessary to relate to the DatabricksGenieAgent
     * @throws InvalidRequestException if any of the minimal set of required properties for a DatabricksGenieAgent relationship are not found in the initial object
     */
    @Override
    public DatabricksGenieAgent trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all DatabricksGenieAgent assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DatabricksGenieAgent assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all DatabricksGenieAgent assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all DatabricksGenieAgent assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DatabricksGenieAgents will be included
     * @return a fluent search that includes all DatabricksGenieAgent assets
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
     * Reference to a DatabricksGenieAgent by GUID. Use this to create a relationship to this DatabricksGenieAgent,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the DatabricksGenieAgent to reference
     * @return reference to a DatabricksGenieAgent that can be used for defining a relationship to a DatabricksGenieAgent
     */
    public static DatabricksGenieAgent refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DatabricksGenieAgent by GUID. Use this to create a relationship to this DatabricksGenieAgent,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the DatabricksGenieAgent to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DatabricksGenieAgent that can be used for defining a relationship to a DatabricksGenieAgent
     */
    public static DatabricksGenieAgent refByGuid(String guid, Reference.SaveSemantic semantic) {
        return DatabricksGenieAgent._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a DatabricksGenieAgent by qualifiedName. Use this to create a relationship to this DatabricksGenieAgent,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the DatabricksGenieAgent to reference
     * @return reference to a DatabricksGenieAgent that can be used for defining a relationship to a DatabricksGenieAgent
     */
    public static DatabricksGenieAgent refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DatabricksGenieAgent by qualifiedName. Use this to create a relationship to this DatabricksGenieAgent,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the DatabricksGenieAgent to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DatabricksGenieAgent that can be used for defining a relationship to a DatabricksGenieAgent
     */
    public static DatabricksGenieAgent refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return DatabricksGenieAgent._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a DatabricksGenieAgent by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DatabricksGenieAgent to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DatabricksGenieAgent, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DatabricksGenieAgent does not exist or the provided GUID is not a DatabricksGenieAgent
     */
    @JsonIgnore
    public static DatabricksGenieAgent get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a DatabricksGenieAgent by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DatabricksGenieAgent to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full DatabricksGenieAgent, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DatabricksGenieAgent does not exist or the provided GUID is not a DatabricksGenieAgent
     */
    @JsonIgnore
    public static DatabricksGenieAgent get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof DatabricksGenieAgent) {
                return (DatabricksGenieAgent) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof DatabricksGenieAgent) {
                return (DatabricksGenieAgent) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a DatabricksGenieAgent by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DatabricksGenieAgent to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DatabricksGenieAgent, including any relationships
     * @return the requested DatabricksGenieAgent, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DatabricksGenieAgent does not exist or the provided GUID is not a DatabricksGenieAgent
     */
    @JsonIgnore
    public static DatabricksGenieAgent get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a DatabricksGenieAgent by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DatabricksGenieAgent to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DatabricksGenieAgent, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the DatabricksGenieAgent
     * @return the requested DatabricksGenieAgent, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DatabricksGenieAgent does not exist or the provided GUID is not a DatabricksGenieAgent
     */
    @JsonIgnore
    public static DatabricksGenieAgent get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = DatabricksGenieAgent.select(client)
                    .where(DatabricksGenieAgent.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof DatabricksGenieAgent) {
                return (DatabricksGenieAgent) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = DatabricksGenieAgent.select(client)
                    .where(DatabricksGenieAgent.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof DatabricksGenieAgent) {
                return (DatabricksGenieAgent) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) DatabricksGenieAgent to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DatabricksGenieAgent
     * @return true if the DatabricksGenieAgent is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a DatabricksGenieAgent.
     *
     * @param qualifiedName of the DatabricksGenieAgent
     * @param name of the DatabricksGenieAgent
     * @return the minimal request necessary to update the DatabricksGenieAgent, as a builder
     */
    public static DatabricksGenieAgentBuilder<?, ?> updater(String qualifiedName, String name) {
        return DatabricksGenieAgent._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DatabricksGenieAgent,
     * from a potentially more-complete DatabricksGenieAgent object.
     *
     * @return the minimal object necessary to update the DatabricksGenieAgent, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a DatabricksGenieAgent are not present in the initial object
     */
    @Override
    public DatabricksGenieAgentBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class DatabricksGenieAgentBuilder<
                    C extends DatabricksGenieAgent, B extends DatabricksGenieAgentBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a DatabricksGenieAgent.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DatabricksGenieAgent
     * @param name of the DatabricksGenieAgent
     * @return the updated DatabricksGenieAgent, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksGenieAgent removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DatabricksGenieAgent) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DatabricksGenieAgent.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DatabricksGenieAgent
     * @param name of the DatabricksGenieAgent
     * @return the updated DatabricksGenieAgent, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksGenieAgent removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DatabricksGenieAgent) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DatabricksGenieAgent.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DatabricksGenieAgent's owners
     * @param qualifiedName of the DatabricksGenieAgent
     * @param name of the DatabricksGenieAgent
     * @return the updated DatabricksGenieAgent, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksGenieAgent removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DatabricksGenieAgent) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DatabricksGenieAgent.
     *
     * @param client connectivity to the Atlan tenant on which to update the DatabricksGenieAgent's certificate
     * @param qualifiedName of the DatabricksGenieAgent
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DatabricksGenieAgent, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksGenieAgent updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DatabricksGenieAgent)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DatabricksGenieAgent.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DatabricksGenieAgent's certificate
     * @param qualifiedName of the DatabricksGenieAgent
     * @param name of the DatabricksGenieAgent
     * @return the updated DatabricksGenieAgent, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksGenieAgent removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DatabricksGenieAgent) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DatabricksGenieAgent.
     *
     * @param client connectivity to the Atlan tenant on which to update the DatabricksGenieAgent's announcement
     * @param qualifiedName of the DatabricksGenieAgent
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksGenieAgent updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (DatabricksGenieAgent)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DatabricksGenieAgent.
     *
     * @param client connectivity to the Atlan client from which to remove the DatabricksGenieAgent's announcement
     * @param qualifiedName of the DatabricksGenieAgent
     * @param name of the DatabricksGenieAgent
     * @return the updated DatabricksGenieAgent, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksGenieAgent removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DatabricksGenieAgent) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DatabricksGenieAgent.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DatabricksGenieAgent's assigned terms
     * @param qualifiedName for the DatabricksGenieAgent
     * @param name human-readable name of the DatabricksGenieAgent
     * @param terms the list of terms to replace on the DatabricksGenieAgent, or null to remove all terms from the DatabricksGenieAgent
     * @return the DatabricksGenieAgent that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DatabricksGenieAgent replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (DatabricksGenieAgent) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DatabricksGenieAgent, without replacing existing terms linked to the DatabricksGenieAgent.
     * Note: this operation must make two API calls — one to retrieve the DatabricksGenieAgent's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DatabricksGenieAgent
     * @param qualifiedName for the DatabricksGenieAgent
     * @param terms the list of terms to append to the DatabricksGenieAgent
     * @return the DatabricksGenieAgent that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DatabricksGenieAgent appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DatabricksGenieAgent) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DatabricksGenieAgent, without replacing all existing terms linked to the DatabricksGenieAgent.
     * Note: this operation must make two API calls — one to retrieve the DatabricksGenieAgent's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DatabricksGenieAgent
     * @param qualifiedName for the DatabricksGenieAgent
     * @param terms the list of terms to remove from the DatabricksGenieAgent, which must be referenced by GUID
     * @return the DatabricksGenieAgent that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DatabricksGenieAgent removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DatabricksGenieAgent) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DatabricksGenieAgent, without replacing existing Atlan tags linked to the DatabricksGenieAgent.
     * Note: this operation must make two API calls — one to retrieve the DatabricksGenieAgent's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DatabricksGenieAgent
     * @param qualifiedName of the DatabricksGenieAgent
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DatabricksGenieAgent
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static DatabricksGenieAgent appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (DatabricksGenieAgent) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DatabricksGenieAgent, without replacing existing Atlan tags linked to the DatabricksGenieAgent.
     * Note: this operation must make two API calls — one to retrieve the DatabricksGenieAgent's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DatabricksGenieAgent
     * @param qualifiedName of the DatabricksGenieAgent
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DatabricksGenieAgent
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static DatabricksGenieAgent appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DatabricksGenieAgent) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a DatabricksGenieAgent.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DatabricksGenieAgent
     * @param qualifiedName of the DatabricksGenieAgent
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DatabricksGenieAgent
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
