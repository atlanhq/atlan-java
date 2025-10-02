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
import com.atlan.model.enums.TableType;
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
 * Instance of a Dremio Physical Dataset (Table) in Atlan. Represents actual data files or database tables that can be queried directly and serve as the foundation for virtual datasets.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class DremioPhysicalDataset extends Asset
        implements IDremioPhysicalDataset, ITable, IDremio, ISQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DremioPhysicalDataset";

    /** Fixed typeName for DremioPhysicalDatasets. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Alias for this table. */
    @Attribute
    String alias;

    /** Simple name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewName;

    /** Unique name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewQualifiedName;

    /** Number of columns in this table. */
    @Attribute
    Long columnCount;

    /** Columns that exist within this table. */
    @Attribute
    @Singular
    SortedSet<IColumn> columns;

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

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ITable> dimensions;

    /** Dremio Folder that contains the physical datasets (tables). */
    @Attribute
    IDremioFolder dremioFolder;

    /** Ordered array of folder assets with qualified name and name representing the complete folder hierarchy path for this asset, from immediate parent to root folder. */
    @Attribute
    @Singular("addDremioFolderHierarchy")
    List<Map<String, String>> dremioFolderHierarchy;

    /** Source ID of this asset in Dremio. */
    @Attribute
    String dremioId;

    /** Unique qualified name of the immediate parent folder containing this asset. */
    @Attribute
    String dremioParentFolderQualifiedName;

    /** Dremio Source that contains the physical datasets (tables). */
    @Attribute
    IDremioSource dremioSource;

    /** Simple name of the Dremio Source containing this asset. */
    @Attribute
    String dremioSourceName;

    /** Unique qualified name of the Dremio Source containing this asset. */
    @Attribute
    String dremioSourceQualifiedName;

    /** Simple name of the Dremio Space containing this asset. */
    @Attribute
    String dremioSpaceName;

    /** Unique qualified name of the Dremio Space containing this asset. */
    @Attribute
    String dremioSpaceQualifiedName;

    /** External location of this table, for example: an S3 object location. */
    @Attribute
    String externalLocation;

    /** Format of the external location of this table, for example: JSON, CSV, PARQUET, etc. */
    @Attribute
    String externalLocationFormat;

    /** Region of the external location of this table, for example: S3 region. */
    @Attribute
    String externalLocationRegion;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ITable> facts;

    /** Iceberg table catalog name (can be any user defined name) */
    @Attribute
    String icebergCatalogName;

    /** Iceberg table catalog type (glue, polaris, snowflake) */
    @Attribute
    String icebergCatalogSource;

    /** Catalog table name (actual table name on the catalog side). */
    @Attribute
    String icebergCatalogTableName;

    /** Catalog table namespace (actual database name on the catalog side). */
    @Attribute
    String icebergCatalogTableNamespace;

    /** Iceberg table base location inside the external volume. */
    @Attribute
    String icebergTableBaseLocation;

    /** Iceberg table type (managed vs unmanaged) */
    @Attribute
    String icebergTableType;

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

    /** Whether this table is partitioned (true) or not (false). */
    @Attribute
    Boolean isPartitioned;

    /** Whether this asset has been profiled (true) or not (false). */
    @Attribute
    Boolean isProfiled;

    /** Whether preview queries are allowed for this table (true) or not (false). */
    @Attribute
    Boolean isQueryPreview;

    /** Whether this table is a sharded table (true) or not (false). */
    @Attribute
    Boolean isSharded;

    /** Whether this table is temporary (true) or not (false). */
    @Attribute
    Boolean isTemporary;

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

    /** Number of partitions in this table. */
    @Attribute
    Long partitionCount;

    /** List of partitions in this table. */
    @Attribute
    String partitionList;

    /** Partition strategy for this table. */
    @Attribute
    String partitionStrategy;

    /** Partitions that exist within this table. */
    @Attribute
    @Singular
    SortedSet<ITablePartition> partitions;

    /** Queries that access this table. */
    @Attribute
    @Singular
    SortedSet<IAtlanQuery> queries;

    /** Number of times this asset has been queried. */
    @Attribute
    Long queryCount;

    /** Time (epoch) at which the query count was last updated, in milliseconds. */
    @Attribute
    @Date
    Long queryCountUpdatedAt;

    /** Configuration for preview queries. */
    @Attribute
    @Singular("putQueryPreviewConfig")
    Map<String, String> queryPreviewConfig;

    /** Number of unique users who have queried this asset. */
    @Attribute
    Long queryUserCount;

    /** Map of unique users who have queried this asset to the number of times they have queried it. */
    @Attribute
    @Singular("putQueryUserMap")
    Map<String, Long> queryUserMap;

    /** Number of rows in this table. */
    @Attribute
    Long rowCount;

    /** Schema in which this table exists. */
    @Attribute
    @JsonProperty("atlanSchema")
    ISchema schema;

    /** Simple name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaName;

    /** Unique name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaQualifiedName;

    /** Size of this table, in bytes. */
    @Attribute
    Long sizeBytes;

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

    /** Definition of the table. */
    @Attribute
    String tableDefinition;

    /** External volume name for the table. */
    @Attribute
    String tableExternalVolumeName;

    /** Extra attributes for Impala */
    @Attribute
    @Singular
    Map<String, String> tableImpalaParameters;

    /** Simple name of the table in which this SQL asset exists, or empty if it does not exist within a table. */
    @Attribute
    String tableName;

    /** Number of objects in this table. */
    @Attribute
    Long tableObjectCount;

    /** Unique name of the table in which this SQL asset exists, or empty if it does not exist within a table. */
    @Attribute
    String tableQualifiedName;

    /** Data retention time in days. */
    @Attribute
    Long tableRetentionTime;

    /** Type of the table. */
    @Attribute
    TableType tableType;

    /** Simple name of the view in which this SQL asset exists, or empty if it does not exist within a view. */
    @Attribute
    String viewName;

    /** Unique name of the view in which this SQL asset exists, or empty if it does not exist within a view. */
    @Attribute
    String viewQualifiedName;

    /**
     * Builds the minimal object necessary to create a relationship to a DremioPhysicalDataset, from a potentially
     * more-complete DremioPhysicalDataset object.
     *
     * @return the minimal object necessary to relate to the DremioPhysicalDataset
     * @throws InvalidRequestException if any of the minimal set of required properties for a DremioPhysicalDataset relationship are not found in the initial object
     */
    @Override
    public DremioPhysicalDataset trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all DremioPhysicalDataset assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DremioPhysicalDataset assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all DremioPhysicalDataset assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all DremioPhysicalDataset assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DremioPhysicalDatasets will be included
     * @return a fluent search that includes all DremioPhysicalDataset assets
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
     * Reference to a DremioPhysicalDataset by GUID. Use this to create a relationship to this DremioPhysicalDataset,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the DremioPhysicalDataset to reference
     * @return reference to a DremioPhysicalDataset that can be used for defining a relationship to a DremioPhysicalDataset
     */
    public static DremioPhysicalDataset refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DremioPhysicalDataset by GUID. Use this to create a relationship to this DremioPhysicalDataset,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the DremioPhysicalDataset to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DremioPhysicalDataset that can be used for defining a relationship to a DremioPhysicalDataset
     */
    public static DremioPhysicalDataset refByGuid(String guid, Reference.SaveSemantic semantic) {
        return DremioPhysicalDataset._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a DremioPhysicalDataset by qualifiedName. Use this to create a relationship to this DremioPhysicalDataset,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the DremioPhysicalDataset to reference
     * @return reference to a DremioPhysicalDataset that can be used for defining a relationship to a DremioPhysicalDataset
     */
    public static DremioPhysicalDataset refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DremioPhysicalDataset by qualifiedName. Use this to create a relationship to this DremioPhysicalDataset,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the DremioPhysicalDataset to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DremioPhysicalDataset that can be used for defining a relationship to a DremioPhysicalDataset
     */
    public static DremioPhysicalDataset refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return DremioPhysicalDataset._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a DremioPhysicalDataset by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DremioPhysicalDataset to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DremioPhysicalDataset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DremioPhysicalDataset does not exist or the provided GUID is not a DremioPhysicalDataset
     */
    @JsonIgnore
    public static DremioPhysicalDataset get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a DremioPhysicalDataset by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DremioPhysicalDataset to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full DremioPhysicalDataset, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DremioPhysicalDataset does not exist or the provided GUID is not a DremioPhysicalDataset
     */
    @JsonIgnore
    public static DremioPhysicalDataset get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof DremioPhysicalDataset) {
                return (DremioPhysicalDataset) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof DremioPhysicalDataset) {
                return (DremioPhysicalDataset) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a DremioPhysicalDataset by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DremioPhysicalDataset to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DremioPhysicalDataset, including any relationships
     * @return the requested DremioPhysicalDataset, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DremioPhysicalDataset does not exist or the provided GUID is not a DremioPhysicalDataset
     */
    @JsonIgnore
    public static DremioPhysicalDataset get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a DremioPhysicalDataset by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DremioPhysicalDataset to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DremioPhysicalDataset, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the DremioPhysicalDataset
     * @return the requested DremioPhysicalDataset, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DremioPhysicalDataset does not exist or the provided GUID is not a DremioPhysicalDataset
     */
    @JsonIgnore
    public static DremioPhysicalDataset get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = DremioPhysicalDataset.select(client)
                    .where(DremioPhysicalDataset.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof DremioPhysicalDataset) {
                return (DremioPhysicalDataset) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = DremioPhysicalDataset.select(client)
                    .where(DremioPhysicalDataset.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof DremioPhysicalDataset) {
                return (DremioPhysicalDataset) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) DremioPhysicalDataset to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DremioPhysicalDataset
     * @return true if the DremioPhysicalDataset is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a DremioPhysicalDataset.
     *
     * @param qualifiedName of the DremioPhysicalDataset
     * @param name of the DremioPhysicalDataset
     * @return the minimal request necessary to update the DremioPhysicalDataset, as a builder
     */
    public static DremioPhysicalDatasetBuilder<?, ?> updater(String qualifiedName, String name) {
        return DremioPhysicalDataset._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DremioPhysicalDataset, from a potentially
     * more-complete DremioPhysicalDataset object.
     *
     * @return the minimal object necessary to update the DremioPhysicalDataset, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DremioPhysicalDataset are not found in the initial object
     */
    @Override
    public DremioPhysicalDatasetBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class DremioPhysicalDatasetBuilder<
                    C extends DremioPhysicalDataset, B extends DremioPhysicalDatasetBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a DremioPhysicalDataset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DremioPhysicalDataset
     * @param name of the DremioPhysicalDataset
     * @return the updated DremioPhysicalDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DremioPhysicalDataset removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DremioPhysicalDataset) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DremioPhysicalDataset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DremioPhysicalDataset
     * @param name of the DremioPhysicalDataset
     * @return the updated DremioPhysicalDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DremioPhysicalDataset removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DremioPhysicalDataset) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DremioPhysicalDataset.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DremioPhysicalDataset's owners
     * @param qualifiedName of the DremioPhysicalDataset
     * @param name of the DremioPhysicalDataset
     * @return the updated DremioPhysicalDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DremioPhysicalDataset removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DremioPhysicalDataset) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DremioPhysicalDataset.
     *
     * @param client connectivity to the Atlan tenant on which to update the DremioPhysicalDataset's certificate
     * @param qualifiedName of the DremioPhysicalDataset
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DremioPhysicalDataset, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DremioPhysicalDataset updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DremioPhysicalDataset)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DremioPhysicalDataset.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DremioPhysicalDataset's certificate
     * @param qualifiedName of the DremioPhysicalDataset
     * @param name of the DremioPhysicalDataset
     * @return the updated DremioPhysicalDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DremioPhysicalDataset removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DremioPhysicalDataset) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DremioPhysicalDataset.
     *
     * @param client connectivity to the Atlan tenant on which to update the DremioPhysicalDataset's announcement
     * @param qualifiedName of the DremioPhysicalDataset
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DremioPhysicalDataset updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (DremioPhysicalDataset)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DremioPhysicalDataset.
     *
     * @param client connectivity to the Atlan client from which to remove the DremioPhysicalDataset's announcement
     * @param qualifiedName of the DremioPhysicalDataset
     * @param name of the DremioPhysicalDataset
     * @return the updated DremioPhysicalDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DremioPhysicalDataset removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DremioPhysicalDataset) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DremioPhysicalDataset.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DremioPhysicalDataset's assigned terms
     * @param qualifiedName for the DremioPhysicalDataset
     * @param name human-readable name of the DremioPhysicalDataset
     * @param terms the list of terms to replace on the DremioPhysicalDataset, or null to remove all terms from the DremioPhysicalDataset
     * @return the DremioPhysicalDataset that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DremioPhysicalDataset replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (DremioPhysicalDataset) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DremioPhysicalDataset, without replacing existing terms linked to the DremioPhysicalDataset.
     * Note: this operation must make two API calls — one to retrieve the DremioPhysicalDataset's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DremioPhysicalDataset
     * @param qualifiedName for the DremioPhysicalDataset
     * @param terms the list of terms to append to the DremioPhysicalDataset
     * @return the DremioPhysicalDataset that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DremioPhysicalDataset appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DremioPhysicalDataset) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DremioPhysicalDataset, without replacing all existing terms linked to the DremioPhysicalDataset.
     * Note: this operation must make two API calls — one to retrieve the DremioPhysicalDataset's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DremioPhysicalDataset
     * @param qualifiedName for the DremioPhysicalDataset
     * @param terms the list of terms to remove from the DremioPhysicalDataset, which must be referenced by GUID
     * @return the DremioPhysicalDataset that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DremioPhysicalDataset removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DremioPhysicalDataset) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DremioPhysicalDataset, without replacing existing Atlan tags linked to the DremioPhysicalDataset.
     * Note: this operation must make two API calls — one to retrieve the DremioPhysicalDataset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DremioPhysicalDataset
     * @param qualifiedName of the DremioPhysicalDataset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DremioPhysicalDataset
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static DremioPhysicalDataset appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (DremioPhysicalDataset) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DremioPhysicalDataset, without replacing existing Atlan tags linked to the DremioPhysicalDataset.
     * Note: this operation must make two API calls — one to retrieve the DremioPhysicalDataset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DremioPhysicalDataset
     * @param qualifiedName of the DremioPhysicalDataset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DremioPhysicalDataset
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static DremioPhysicalDataset appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DremioPhysicalDataset) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a DremioPhysicalDataset.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DremioPhysicalDataset
     * @param qualifiedName of the DremioPhysicalDataset
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DremioPhysicalDataset
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
