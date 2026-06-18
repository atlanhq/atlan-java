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
import com.atlan.model.enums.SnowflakeListingDistribution;
import com.atlan.model.enums.SnowflakeListingState;
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
 * Instance of a Snowflake listing in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class SnowflakeListing extends Asset
        implements ISnowflakeListing, ISnowflake, ICatalog, IAsset, IReferenceable, ISQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SnowflakeListing";

    /** Fixed typeName for SnowflakeListings. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Simple name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewName;

    /** Unique name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewQualifiedName;

    /** Unique identifier of the dataset this asset belongs to. */
    @Attribute
    String catalogDatasetGuid;

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

    /** Application package name when this listing wraps a Native App. */
    @Attribute
    String snowflakeListingApplicationPackage;

    /** Auto-fulfillment configuration for the listing. */
    @Attribute
    String snowflakeListingAutoFulfillment;

    /** Discovery categories assigned to the listing. */
    @Attribute
    @Singular
    SortedSet<String> snowflakeListingCategories;

    /** Data properties of the listing (refresh rate, history, freshness window) as a JSON blob emitted by Snowflake. */
    @Attribute
    String snowflakeListingDataAttributes;

    /** Distribution scope of the listing (organization-internal vs external marketplace/exchange). */
    @Attribute
    SnowflakeListingDistribution snowflakeListingDistribution;

    /** Whether this listing wraps a Snowflake Native App (true) or not (false). */
    @Attribute
    Boolean snowflakeListingIsApplication;

    /** Whether this listing wraps a data share (true) or not (false). */
    @Attribute
    Boolean snowflakeListingIsShare;

    /** External Snowflake provider profile attached to the listing. */
    @Attribute
    String snowflakeListingProfile;

    /** Resharing configuration for the listing. */
    @Attribute
    String snowflakeListingResharing;

    /** Publication state of the listing. */
    @Attribute
    SnowflakeListingState snowflakeListingState;

    /** Marketplace subtitle of the listing. */
    @Attribute
    String snowflakeListingSubtitle;

    /** Contact info for the listing. */
    @Attribute
    String snowflakeListingSupportContact;

    /** Distribution targets of the listing (accounts, regions) as a JSON blob emitted by Snowflake. */
    @Attribute
    String snowflakeListingTargets;

    /** Terms of service for the listing. */
    @Attribute
    String snowflakeListingTerms;

    /** Snowflake's source-truthful title for the listing. Distinct from `name` (the non-human-readable Snowflake identifier). */
    @Attribute
    String snowflakeListingTitle;

    /** Uniform Listing Locator (ULL) of the listing. */
    @Attribute
    String snowflakeListingUniformListingLocator;

    /** Semantic logical tables that reference this physical table or view. */
    @Attribute
    @Singular
    SortedSet<ISnowflakeSemanticLogicalTable> snowflakeSemanticLogicalTables;

    /** Snowflake shares wrapped by this listing. */
    @Attribute
    @Singular
    SortedSet<ISnowflakeShare> snowflakeShares;

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

    /** Business question insights for this SQL asset. */
    @Attribute
    @Singular
    SortedSet<ISqlInsightBusinessQuestion> sqlInsightBusinessQuestions;

    /** Join insights where this asset is the joined dataset. */
    @Attribute
    @Singular
    SortedSet<ISqlInsightJoin> sqlInsightIncomingJoins;

    /** Join insights where this asset is the source dataset. */
    @Attribute
    @Singular
    SortedSet<ISqlInsightJoin> sqlInsightOutgoingJoins;

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
     * Builds the minimal object necessary to create a relationship to a SnowflakeListing, from a potentially
     * more-complete SnowflakeListing object.
     *
     * @return the minimal object necessary to relate to the SnowflakeListing
     * @throws InvalidRequestException if any of the minimal set of required properties for a SnowflakeListing relationship are not found in the initial object
     */
    @Override
    public SnowflakeListing trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SnowflakeListing assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SnowflakeListing assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SnowflakeListing assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SnowflakeListing assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SnowflakeListings will be included
     * @return a fluent search that includes all SnowflakeListing assets
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
     * Reference to a SnowflakeListing by GUID. Use this to create a relationship to this SnowflakeListing,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SnowflakeListing to reference
     * @return reference to a SnowflakeListing that can be used for defining a relationship to a SnowflakeListing
     */
    public static SnowflakeListing refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SnowflakeListing by GUID. Use this to create a relationship to this SnowflakeListing,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SnowflakeListing to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SnowflakeListing that can be used for defining a relationship to a SnowflakeListing
     */
    public static SnowflakeListing refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SnowflakeListing._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SnowflakeListing by qualifiedName. Use this to create a relationship to this SnowflakeListing,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SnowflakeListing to reference
     * @return reference to a SnowflakeListing that can be used for defining a relationship to a SnowflakeListing
     */
    public static SnowflakeListing refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SnowflakeListing by qualifiedName. Use this to create a relationship to this SnowflakeListing,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SnowflakeListing to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SnowflakeListing that can be used for defining a relationship to a SnowflakeListing
     */
    public static SnowflakeListing refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SnowflakeListing._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SnowflakeListing by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SnowflakeListing to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SnowflakeListing, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakeListing does not exist or the provided GUID is not a SnowflakeListing
     */
    @JsonIgnore
    public static SnowflakeListing get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SnowflakeListing by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SnowflakeListing to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SnowflakeListing, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakeListing does not exist or the provided GUID is not a SnowflakeListing
     */
    @JsonIgnore
    public static SnowflakeListing get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SnowflakeListing) {
                return (SnowflakeListing) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof SnowflakeListing) {
                return (SnowflakeListing) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SnowflakeListing by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SnowflakeListing to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SnowflakeListing, including any relationships
     * @return the requested SnowflakeListing, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakeListing does not exist or the provided GUID is not a SnowflakeListing
     */
    @JsonIgnore
    public static SnowflakeListing get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a SnowflakeListing by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SnowflakeListing to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SnowflakeListing, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the SnowflakeListing
     * @return the requested SnowflakeListing, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakeListing does not exist or the provided GUID is not a SnowflakeListing
     */
    @JsonIgnore
    public static SnowflakeListing get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = SnowflakeListing.select(client)
                    .where(SnowflakeListing.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof SnowflakeListing) {
                return (SnowflakeListing) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = SnowflakeListing.select(client)
                    .where(SnowflakeListing.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof SnowflakeListing) {
                return (SnowflakeListing) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SnowflakeListing to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SnowflakeListing
     * @return true if the SnowflakeListing is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SnowflakeListing.
     *
     * @param qualifiedName of the SnowflakeListing
     * @param name of the SnowflakeListing
     * @return the minimal request necessary to update the SnowflakeListing, as a builder
     */
    public static SnowflakeListingBuilder<?, ?> updater(String qualifiedName, String name) {
        return SnowflakeListing._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SnowflakeListing,
     * from a potentially more-complete SnowflakeListing object.
     *
     * @return the minimal object necessary to update the SnowflakeListing, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a SnowflakeListing are not present in the initial object
     */
    @Override
    public SnowflakeListingBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class SnowflakeListingBuilder<
                    C extends SnowflakeListing, B extends SnowflakeListingBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a SnowflakeListing.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SnowflakeListing
     * @param name of the SnowflakeListing
     * @return the updated SnowflakeListing, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeListing removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SnowflakeListing) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SnowflakeListing.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SnowflakeListing
     * @param name of the SnowflakeListing
     * @return the updated SnowflakeListing, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeListing removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SnowflakeListing) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SnowflakeListing.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SnowflakeListing's owners
     * @param qualifiedName of the SnowflakeListing
     * @param name of the SnowflakeListing
     * @return the updated SnowflakeListing, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeListing removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SnowflakeListing) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SnowflakeListing.
     *
     * @param client connectivity to the Atlan tenant on which to update the SnowflakeListing's certificate
     * @param qualifiedName of the SnowflakeListing
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SnowflakeListing, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeListing updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SnowflakeListing)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SnowflakeListing.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SnowflakeListing's certificate
     * @param qualifiedName of the SnowflakeListing
     * @param name of the SnowflakeListing
     * @return the updated SnowflakeListing, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeListing removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SnowflakeListing) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SnowflakeListing.
     *
     * @param client connectivity to the Atlan tenant on which to update the SnowflakeListing's announcement
     * @param qualifiedName of the SnowflakeListing
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeListing updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SnowflakeListing)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SnowflakeListing.
     *
     * @param client connectivity to the Atlan client from which to remove the SnowflakeListing's announcement
     * @param qualifiedName of the SnowflakeListing
     * @param name of the SnowflakeListing
     * @return the updated SnowflakeListing, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeListing removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SnowflakeListing) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SnowflakeListing.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SnowflakeListing's assigned terms
     * @param qualifiedName for the SnowflakeListing
     * @param name human-readable name of the SnowflakeListing
     * @param terms the list of terms to replace on the SnowflakeListing, or null to remove all terms from the SnowflakeListing
     * @return the SnowflakeListing that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SnowflakeListing replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SnowflakeListing) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SnowflakeListing, without replacing existing terms linked to the SnowflakeListing.
     * Note: this operation must make two API calls — one to retrieve the SnowflakeListing's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SnowflakeListing
     * @param qualifiedName for the SnowflakeListing
     * @param terms the list of terms to append to the SnowflakeListing
     * @return the SnowflakeListing that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SnowflakeListing appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SnowflakeListing) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SnowflakeListing, without replacing all existing terms linked to the SnowflakeListing.
     * Note: this operation must make two API calls — one to retrieve the SnowflakeListing's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SnowflakeListing
     * @param qualifiedName for the SnowflakeListing
     * @param terms the list of terms to remove from the SnowflakeListing, which must be referenced by GUID
     * @return the SnowflakeListing that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SnowflakeListing removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SnowflakeListing) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SnowflakeListing, without replacing existing Atlan tags linked to the SnowflakeListing.
     * Note: this operation must make two API calls — one to retrieve the SnowflakeListing's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SnowflakeListing
     * @param qualifiedName of the SnowflakeListing
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SnowflakeListing
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static SnowflakeListing appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SnowflakeListing) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SnowflakeListing, without replacing existing Atlan tags linked to the SnowflakeListing.
     * Note: this operation must make two API calls — one to retrieve the SnowflakeListing's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SnowflakeListing
     * @param qualifiedName of the SnowflakeListing
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SnowflakeListing
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static SnowflakeListing appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SnowflakeListing) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SnowflakeListing.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SnowflakeListing
     * @param qualifiedName of the SnowflakeListing
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SnowflakeListing
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
