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
 * Instance of a database table partition in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class TablePartition extends Asset implements ITablePartition, ISQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TablePartition";

    /** Fixed typeName for TablePartitions. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Alias for this partition. */
    @Attribute
    String alias;

    /** Simple name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewName;

    /** Unique name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewQualifiedName;

    /** Partitions that exist within this partition. */
    @Attribute
    @Singular
    SortedSet<ITablePartition> childTablePartitions;

    /** Number of columns in this partition. */
    @Attribute
    Long columnCount;

    /** Columns that exist within this table partition. */
    @Attribute
    @Singular
    SortedSet<IColumn> columns;

    /** Constraint that defines this table partition. */
    @Attribute
    String constraint;

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

    /** External location of this partition, for example: an S3 object location. */
    @Attribute
    String externalLocation;

    /** Format of the external location of this partition, for example: JSON, CSV, PARQUET, etc. */
    @Attribute
    String externalLocationFormat;

    /** Region of the external location of this partition, for example: S3 region. */
    @Attribute
    String externalLocationRegion;

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

    /** Whether this partition is further partitioned (true) or not (false). */
    @Attribute
    Boolean isPartitioned;

    /** Whether this asset has been profiled (true) or not (false). */
    @Attribute
    Boolean isProfiled;

    /** Whether preview queries for this partition are allowed (true) or not (false). */
    @Attribute
    Boolean isQueryPreview;

    /** Whether this partition is temporary (true) or not (false). */
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

    /** Table in which this partition exists. */
    @Attribute
    ITable parentTable;

    /** Partition in which this partition exists. */
    @Attribute
    ITablePartition parentTablePartition;

    /** Number of sub-partitions of this partition. */
    @Attribute
    Long partitionCount;

    /** List of sub-partitions in this partition. */
    @Attribute
    String partitionList;

    /** Partition strategy of this partition. */
    @Attribute
    String partitionStrategy;

    /** Number of times this asset has been queried. */
    @Attribute
    Long queryCount;

    /** Time (epoch) at which the query count was last updated, in milliseconds. */
    @Attribute
    @Date
    Long queryCountUpdatedAt;

    /** Configuration for the preview queries. */
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

    /** Number of rows in this partition. */
    @Attribute
    Long rowCount;

    /** Simple name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaName;

    /** Unique name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaQualifiedName;

    /** Size of this partition, in bytes. */
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
     * Builds the minimal object necessary to create a relationship to a TablePartition, from a potentially
     * more-complete TablePartition object.
     *
     * @return the minimal object necessary to relate to the TablePartition
     * @throws InvalidRequestException if any of the minimal set of required properties for a TablePartition relationship are not found in the initial object
     */
    @Override
    public TablePartition trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all TablePartition assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) TablePartition assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all TablePartition assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all TablePartition assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) TablePartitions will be included
     * @return a fluent search that includes all TablePartition assets
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
     * Reference to a TablePartition by GUID. Use this to create a relationship to this TablePartition,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the TablePartition to reference
     * @return reference to a TablePartition that can be used for defining a relationship to a TablePartition
     */
    public static TablePartition refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a TablePartition by GUID. Use this to create a relationship to this TablePartition,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the TablePartition to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a TablePartition that can be used for defining a relationship to a TablePartition
     */
    public static TablePartition refByGuid(String guid, Reference.SaveSemantic semantic) {
        return TablePartition._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a TablePartition by qualifiedName. Use this to create a relationship to this TablePartition,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the TablePartition to reference
     * @return reference to a TablePartition that can be used for defining a relationship to a TablePartition
     */
    public static TablePartition refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a TablePartition by qualifiedName. Use this to create a relationship to this TablePartition,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the TablePartition to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a TablePartition that can be used for defining a relationship to a TablePartition
     */
    public static TablePartition refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return TablePartition._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a TablePartition by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the TablePartition to retrieve, either its GUID or its full qualifiedName
     * @return the requested full TablePartition, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TablePartition does not exist or the provided GUID is not a TablePartition
     */
    @JsonIgnore
    public static TablePartition get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a TablePartition by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the TablePartition to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full TablePartition, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TablePartition does not exist or the provided GUID is not a TablePartition
     */
    @JsonIgnore
    public static TablePartition get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof TablePartition) {
                return (TablePartition) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof TablePartition) {
                return (TablePartition) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a TablePartition by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the TablePartition to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the TablePartition, including any relationships
     * @return the requested TablePartition, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TablePartition does not exist or the provided GUID is not a TablePartition
     */
    @JsonIgnore
    public static TablePartition get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a TablePartition by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the TablePartition to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the TablePartition, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the TablePartition
     * @return the requested TablePartition, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TablePartition does not exist or the provided GUID is not a TablePartition
     */
    @JsonIgnore
    public static TablePartition get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = TablePartition.select(client)
                    .where(TablePartition.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof TablePartition) {
                return (TablePartition) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = TablePartition.select(client)
                    .where(TablePartition.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof TablePartition) {
                return (TablePartition) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) TablePartition to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the TablePartition
     * @return true if the TablePartition is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a table partition.
     *
     * @param name of the table partition
     * @param table in which the partition should be created, which must have at least
     *              a qualifiedName
     * @return the minimal request necessary to create the table partition, as a builder
     * @throws InvalidRequestException if the table provided is without a qualifiedName
     */
    public static TablePartitionBuilder<?, ?> creator(String name, Table table) throws InvalidRequestException {
        if (table.getQualifiedName() == null || table.getQualifiedName().isEmpty()) {
            throw new InvalidRequestException(ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, "Table", "qualifiedName");
        }
        return creator(name, table.getQualifiedName()).parentTable(table.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a table partition.
     *
     * @param name of the table partition
     * @param tableQualifiedName unique name of the table in which this table partition exists
     * @return the minimal request necessary to create the table partition, as a builder
     */
    public static TablePartitionBuilder<?, ?> creator(String name, String tableQualifiedName) {
        String tableName = StringUtils.getNameFromQualifiedName(tableQualifiedName);
        String schemaQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(tableQualifiedName);
        String schemaName = StringUtils.getNameFromQualifiedName(schemaQualifiedName);
        String databaseQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(schemaQualifiedName);
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        return creator(
                name,
                connectionQualifiedName,
                databaseName,
                databaseQualifiedName,
                schemaName,
                schemaQualifiedName,
                tableName,
                tableQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a TablePartition.
     *
     * @param name of the TablePartition
     * @param connectionQualifiedName unique name of the connection in which to create the TablePartition
     * @param databaseName simple name of the Database in which to create the TablePartition
     * @param databaseQualifiedName unique name of the Database in which to create the TablePartition
     * @param schemaName simple name of the Schema in which to create the TablePartition
     * @param schemaQualifiedName unique name of the Schema in which to create the TablePartition
     * @param tableName simple name of the Table in which to create the TablePartition
     * @param tableQualifiedName unique name of the Table in which to create the TablePartition
     * @return the minimal request necessary to create the TablePartition, as a builder
     */
    public static TablePartitionBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String databaseName,
            String databaseQualifiedName,
            String schemaName,
            String schemaQualifiedName,
            String tableName,
            String tableQualifiedName) {
        return TablePartition._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, schemaQualifiedName))
                .parentTable(Table.refByQualifiedName(tableQualifiedName))
                .schemaName(schemaName)
                .schemaQualifiedName(schemaQualifiedName)
                .databaseName(databaseName)
                .databaseQualifiedName(databaseQualifiedName)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Generate a unique table partition name.
     *
     * @param name of the table partition
     * @param schemaQualifiedName unique name of the schema in which this partition exists
     * @return a unique name for the table partition
     */
    public static String generateQualifiedName(String name, String schemaQualifiedName) {
        return schemaQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a TablePartition.
     *
     * @param qualifiedName of the TablePartition
     * @param name of the TablePartition
     * @return the minimal request necessary to update the TablePartition, as a builder
     */
    public static TablePartitionBuilder<?, ?> updater(String qualifiedName, String name) {
        return TablePartition._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TablePartition, from a potentially
     * more-complete TablePartition object.
     *
     * @return the minimal object necessary to update the TablePartition, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TablePartition are not found in the initial object
     */
    @Override
    public TablePartitionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class TablePartitionBuilder<C extends TablePartition, B extends TablePartitionBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a TablePartition.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the TablePartition
     * @param name of the TablePartition
     * @return the updated TablePartition, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TablePartition) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a TablePartition.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the TablePartition
     * @param name of the TablePartition
     * @return the updated TablePartition, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TablePartition) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a TablePartition.
     *
     * @param client connectivity to the Atlan tenant from which to remove the TablePartition's owners
     * @param qualifiedName of the TablePartition
     * @param name of the TablePartition
     * @return the updated TablePartition, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TablePartition) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a TablePartition.
     *
     * @param client connectivity to the Atlan tenant on which to update the TablePartition's certificate
     * @param qualifiedName of the TablePartition
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TablePartition, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (TablePartition)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a TablePartition.
     *
     * @param client connectivity to the Atlan tenant from which to remove the TablePartition's certificate
     * @param qualifiedName of the TablePartition
     * @param name of the TablePartition
     * @return the updated TablePartition, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TablePartition) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a TablePartition.
     *
     * @param client connectivity to the Atlan tenant on which to update the TablePartition's announcement
     * @param qualifiedName of the TablePartition
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (TablePartition)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a TablePartition.
     *
     * @param client connectivity to the Atlan client from which to remove the TablePartition's announcement
     * @param qualifiedName of the TablePartition
     * @param name of the TablePartition
     * @return the updated TablePartition, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TablePartition) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the TablePartition.
     *
     * @param client connectivity to the Atlan tenant on which to replace the TablePartition's assigned terms
     * @param qualifiedName for the TablePartition
     * @param name human-readable name of the TablePartition
     * @param terms the list of terms to replace on the TablePartition, or null to remove all terms from the TablePartition
     * @return the TablePartition that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TablePartition replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (TablePartition) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the TablePartition, without replacing existing terms linked to the TablePartition.
     * Note: this operation must make two API calls — one to retrieve the TablePartition's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the TablePartition
     * @param qualifiedName for the TablePartition
     * @param terms the list of terms to append to the TablePartition
     * @return the TablePartition that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static TablePartition appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (TablePartition) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a TablePartition, without replacing all existing terms linked to the TablePartition.
     * Note: this operation must make two API calls — one to retrieve the TablePartition's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the TablePartition
     * @param qualifiedName for the TablePartition
     * @param terms the list of terms to remove from the TablePartition, which must be referenced by GUID
     * @return the TablePartition that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static TablePartition removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (TablePartition) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a TablePartition, without replacing existing Atlan tags linked to the TablePartition.
     * Note: this operation must make two API calls — one to retrieve the TablePartition's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the TablePartition
     * @param qualifiedName of the TablePartition
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated TablePartition
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static TablePartition appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (TablePartition) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TablePartition, without replacing existing Atlan tags linked to the TablePartition.
     * Note: this operation must make two API calls — one to retrieve the TablePartition's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the TablePartition
     * @param qualifiedName of the TablePartition
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated TablePartition
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static TablePartition appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (TablePartition) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a TablePartition.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a TablePartition
     * @param qualifiedName of the TablePartition
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the TablePartition
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
