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
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
 * Instance of an Iceberg namespace in Atlan. Supports nested namespaces with dot-separated paths.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class IcebergNamespace extends Asset implements IIcebergNamespace, ISchema, IIceberg, ICatalog, IAsset, IReferenceable, ISQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "IcebergNamespace";

    /** Fixed typeName for IcebergNamespaces. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Simple name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewName;

    /** Unique name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewQualifiedName;

    /** Calculation views that exist within this schema. */
    @Attribute
    @Singular
    SortedSet<ICalculationView> calculationViews;

    /** Database in which this schema exists. */
    @Attribute
    IDatabase database;

    /** Simple name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    @Attribute
    String databaseName;

    /** Unique name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    @Attribute
    String databaseQualifiedName;

    /** Contexts contained within the schema. */
    @Attribute
    @Singular
    SortedSet<IDatabricksAIModelContext> databricksAIModelContexts;

    /** Volume contained within the schema. */
    @Attribute
    @Singular
    SortedSet<IDatabricksVolume> databricksVolumes;

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

    /** Functions that exist within this schema. */
    @Attribute
    @Singular
    SortedSet<IFunction> functions;

    /** Ordered array of namespace assets with qualified name and name representing the complete namespace hierarchy path for this asset, from immediate parent to root namespace. */
    @Attribute
    @Singular("addIcebergNamespaceHierarchy")
    List<Map<String, String>> icebergNamespaceHierarchy;

    /** Parent Iceberg Namespace containing the sub-namespaces. */
    @Attribute
    IIcebergNamespace icebergParentNamespace;

    /** Unique name of the immediate parent namespace in which this asset exists. */
    @Attribute
    String icebergParentNamespaceQualifiedName;

    /** Child namespaces nested within the parent Iceberg Namespace. */
    @Attribute
    @Singular
    SortedSet<IIcebergNamespace> icebergSubNamespaces;

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

    /** Unique name of the Linked Schema on which this Schema is dependent. This concept is mostly applicable for linked datasets/datasource in Google BigQuery via Analytics Hub Listing */
    @Attribute
    String linkedSchemaQualifiedName;

    /** Materialized views that exist within this schema. */
    @Attribute
    @Singular
    @JsonProperty("materialisedViews")
    SortedSet<IMaterializedView> materializedViews;

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

    /** Stored procedures that exist within this schema. */
    @Attribute
    @Singular
    SortedSet<IProcedure> procedures;

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

    /** External location of this schema, for example: an S3 object location. */
    @Attribute
    String schemaExternalLocation;

    /** Simple name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaName;

    /** Unique name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaQualifiedName;

    /** Contexts contained within the schema. */
    @Attribute
    @Singular
    SortedSet<ISnowflakeAIModelContext> snowflakeAIModelContexts;

    /** Snowflake dynamic tables that exist within this schema. */
    @Attribute
    @Singular
    SortedSet<ISnowflakeDynamicTable> snowflakeDynamicTables;

    /** Snowflake pipes that exist within this schema. */
    @Attribute
    @Singular
    SortedSet<ISnowflakePipe> snowflakePipes;

    /** Snowflake semantic views contained in the schema. */
    @Attribute
    @Singular
    SortedSet<ISnowflakeSemanticView> snowflakeSemanticViews;

    /** Collection of Snowflake stages that are defined and contained within this schema, representing staging areas for data loading and unloading operations. */
    @Attribute
    @Singular
    SortedSet<ISnowflakeStage> snowflakeStages;

    /** Snowflake streams that exist within this schema. */
    @Attribute
    @Singular
    SortedSet<ISnowflakeStream> snowflakeStreams;

    /** Snowflake tags that exist within this schema. */
    @Attribute
    @Singular
    SortedSet<ISnowflakeTag> snowflakeTags;

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

    /** Number of tables in this schema. */
    @Attribute
    Integer tableCount;

    /** Simple name of the table in which this SQL asset exists, or empty if it does not exist within a table. */
    @Attribute
    String tableName;

    /** Unique name of the table in which this SQL asset exists, or empty if it does not exist within a table. */
    @Attribute
    String tableQualifiedName;

    /** Tables that exist within this schema. */
    @Attribute
    @Singular
    SortedSet<ITable> tables;

    /** Number of views in this schema. */
    @Attribute
    @JsonProperty("viewsCount")
    Integer viewCount;

    /** Simple name of the view in which this SQL asset exists, or empty if it does not exist within a view. */
    @Attribute
    String viewName;

    /** Unique name of the view in which this SQL asset exists, or empty if it does not exist within a view. */
    @Attribute
    String viewQualifiedName;

    /** Views that exist within this schema. */
    @Attribute
    @Singular
    SortedSet<IView> views;

    /**
     * Builds the minimal object necessary to create a relationship to a IcebergNamespace, from a potentially
     * more-complete IcebergNamespace object.
     *
     * @return the minimal object necessary to relate to the IcebergNamespace
     * @throws InvalidRequestException if any of the minimal set of required properties for a IcebergNamespace relationship are not found in the initial object
     */
    @Override
    public IcebergNamespace trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all IcebergNamespace assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) IcebergNamespace assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all IcebergNamespace assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all IcebergNamespace assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) IcebergNamespaces will be included
     * @return a fluent search that includes all IcebergNamespace assets
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
     * Reference to a IcebergNamespace by GUID. Use this to create a relationship to this IcebergNamespace,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the IcebergNamespace to reference
     * @return reference to a IcebergNamespace that can be used for defining a relationship to a IcebergNamespace
     */
    public static IcebergNamespace refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a IcebergNamespace by GUID. Use this to create a relationship to this IcebergNamespace,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the IcebergNamespace to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a IcebergNamespace that can be used for defining a relationship to a IcebergNamespace
     */
    public static IcebergNamespace refByGuid(String guid, Reference.SaveSemantic semantic) {
        return IcebergNamespace._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a IcebergNamespace by qualifiedName. Use this to create a relationship to this IcebergNamespace,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the IcebergNamespace to reference
     * @return reference to a IcebergNamespace that can be used for defining a relationship to a IcebergNamespace
     */
    public static IcebergNamespace refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a IcebergNamespace by qualifiedName. Use this to create a relationship to this IcebergNamespace,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the IcebergNamespace to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a IcebergNamespace that can be used for defining a relationship to a IcebergNamespace
     */
    public static IcebergNamespace refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return IcebergNamespace._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a IcebergNamespace by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the IcebergNamespace to retrieve, either its GUID or its full qualifiedName
     * @return the requested full IcebergNamespace, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the IcebergNamespace does not exist or the provided GUID is not a IcebergNamespace
     */
    @JsonIgnore
    public static IcebergNamespace get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a IcebergNamespace by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the IcebergNamespace to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full IcebergNamespace, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the IcebergNamespace does not exist or the provided GUID is not a IcebergNamespace
     */
    @JsonIgnore
    public static IcebergNamespace get(AtlanClient client, String id, boolean includeAllRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof IcebergNamespace) {
                return (IcebergNamespace) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof IcebergNamespace) {
                return (IcebergNamespace) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a IcebergNamespace by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the IcebergNamespace to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the IcebergNamespace, including any relationships
     * @return the requested IcebergNamespace, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the IcebergNamespace does not exist or the provided GUID is not a IcebergNamespace
     */
    @JsonIgnore
    public static IcebergNamespace get(AtlanClient client, String id, Collection<AtlanField> attributes) throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a IcebergNamespace by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the IcebergNamespace to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the IcebergNamespace, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the IcebergNamespace
     * @return the requested IcebergNamespace, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the IcebergNamespace does not exist or the provided GUID is not a IcebergNamespace
     */
    @JsonIgnore
    public static IcebergNamespace get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = IcebergNamespace.select(client)
                    .where(IcebergNamespace.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof IcebergNamespace) {
                return (IcebergNamespace) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = IcebergNamespace.select(client)
                    .where(IcebergNamespace.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof IcebergNamespace) {
                return (IcebergNamespace) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) IcebergNamespace to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the IcebergNamespace
     * @return true if the IcebergNamespace is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a IcebergNamespace.
     *
     * @param qualifiedName of the IcebergNamespace
     * @param name of the IcebergNamespace
     * @return the minimal request necessary to update the IcebergNamespace, as a builder
     */
    public static IcebergNamespaceBuilder<?, ?> updater(String qualifiedName, String name) {
        return IcebergNamespace._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a IcebergNamespace,
     * from a potentially more-complete IcebergNamespace object.
     *
     * @return the minimal object necessary to update the IcebergNamespace, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a IcebergNamespace are not present in the initial object
     */
    @Override
    public IcebergNamespaceBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class IcebergNamespaceBuilder<C extends IcebergNamespace, B extends IcebergNamespaceBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a IcebergNamespace.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the IcebergNamespace
     * @param name of the IcebergNamespace
     * @return the updated IcebergNamespace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static IcebergNamespace removeDescription(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (IcebergNamespace) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a IcebergNamespace.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the IcebergNamespace
     * @param name of the IcebergNamespace
     * @return the updated IcebergNamespace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static IcebergNamespace removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (IcebergNamespace) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a IcebergNamespace.
     *
     * @param client connectivity to the Atlan tenant from which to remove the IcebergNamespace's owners
     * @param qualifiedName of the IcebergNamespace
     * @param name of the IcebergNamespace
     * @return the updated IcebergNamespace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static IcebergNamespace removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (IcebergNamespace) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a IcebergNamespace.
     *
     * @param client connectivity to the Atlan tenant on which to update the IcebergNamespace's certificate
     * @param qualifiedName of the IcebergNamespace
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated IcebergNamespace, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static IcebergNamespace updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (IcebergNamespace) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a IcebergNamespace.
     *
     * @param client connectivity to the Atlan tenant from which to remove the IcebergNamespace's certificate
     * @param qualifiedName of the IcebergNamespace
     * @param name of the IcebergNamespace
     * @return the updated IcebergNamespace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static IcebergNamespace removeCertificate(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (IcebergNamespace) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a IcebergNamespace.
     *
     * @param client connectivity to the Atlan tenant on which to update the IcebergNamespace's announcement
     * @param qualifiedName of the IcebergNamespace
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static IcebergNamespace updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (IcebergNamespace) Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a IcebergNamespace.
     *
     * @param client connectivity to the Atlan client from which to remove the IcebergNamespace's announcement
     * @param qualifiedName of the IcebergNamespace
     * @param name of the IcebergNamespace
     * @return the updated IcebergNamespace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static IcebergNamespace removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (IcebergNamespace) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the IcebergNamespace.
     *
     * @param client connectivity to the Atlan tenant on which to replace the IcebergNamespace's assigned terms
     * @param qualifiedName for the IcebergNamespace
     * @param name human-readable name of the IcebergNamespace
     * @param terms the list of terms to replace on the IcebergNamespace, or null to remove all terms from the IcebergNamespace
     * @return the IcebergNamespace that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static IcebergNamespace replaceTerms(AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (IcebergNamespace) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the IcebergNamespace, without replacing existing terms linked to the IcebergNamespace.
     * Note: this operation must make two API calls — one to retrieve the IcebergNamespace's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the IcebergNamespace
     * @param qualifiedName for the IcebergNamespace
     * @param terms the list of terms to append to the IcebergNamespace
     * @return the IcebergNamespace that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static IcebergNamespace appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (IcebergNamespace) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a IcebergNamespace, without replacing all existing terms linked to the IcebergNamespace.
     * Note: this operation must make two API calls — one to retrieve the IcebergNamespace's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the IcebergNamespace
     * @param qualifiedName for the IcebergNamespace
     * @param terms the list of terms to remove from the IcebergNamespace, which must be referenced by GUID
     * @return the IcebergNamespace that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static IcebergNamespace removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (IcebergNamespace) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a IcebergNamespace, without replacing existing Atlan tags linked to the IcebergNamespace.
     * Note: this operation must make two API calls — one to retrieve the IcebergNamespace's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the IcebergNamespace
     * @param qualifiedName of the IcebergNamespace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated IcebergNamespace
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static IcebergNamespace appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (IcebergNamespace) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a IcebergNamespace, without replacing existing Atlan tags linked to the IcebergNamespace.
     * Note: this operation must make two API calls — one to retrieve the IcebergNamespace's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the IcebergNamespace
     * @param qualifiedName of the IcebergNamespace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated IcebergNamespace
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static IcebergNamespace appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (IcebergNamespace) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a IcebergNamespace.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a IcebergNamespace
     * @param qualifiedName of the IcebergNamespace
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the IcebergNamespace
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}