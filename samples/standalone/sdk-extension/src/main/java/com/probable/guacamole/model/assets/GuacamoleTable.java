/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.probable.guacamole.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.Attribute;
import com.atlan.model.assets.Connection;
import com.atlan.model.assets.Date;
import com.atlan.model.assets.IAirflowTask;
import com.atlan.model.assets.IAsset;
import com.atlan.model.assets.IAtlanQuery;
import com.atlan.model.assets.ICatalog;
import com.atlan.model.assets.IColumn;
import com.atlan.model.assets.IDbtModel;
import com.atlan.model.assets.IDbtSource;
import com.atlan.model.assets.IDbtTest;
import com.atlan.model.assets.IGlossaryTerm;
import com.atlan.model.assets.ILineageProcess;
import com.atlan.model.assets.IReferenceable;
import com.atlan.model.assets.ISQL;
import com.atlan.model.assets.ISchema;
import com.atlan.model.assets.ITable;
import com.atlan.model.assets.ITablePartition;
import com.atlan.model.assets.Schema;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.probable.guacamole.model.enums.GuacamoleTemperature;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Specialized form of a table specific to Guacamole.
 */
@Generated(value = "com.probable.guacamole.generators.POJOGenerator")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class GuacamoleTable extends Asset implements IGuacamoleTable, ITable, ISQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "GuacamoleTable";

    /** Fixed typeName for GuacamoleTables. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Alias for this table. */
    @Attribute
    String alias;

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

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtModel> dbtModels;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtSource> dbtSources;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtTest> dbtTests;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ITable> dimensions;

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

    /** Whether this table is currently archived (true) or not (false). */
    @Attribute
    Boolean guacamoleArchived;

    /** Specialized columns contained within this specialized table. */
    @Attribute
    @Singular
    SortedSet<IGuacamoleColumn> guacamoleColumns;

    /** Consolidated quantification metric spanning number of columns, rows, and sparsity of population. */
    @Attribute
    Long guacamoleSize;

    /** Rough measure of the IOPS allocated to the table's processing. */
    @Attribute
    GuacamoleTemperature guacamoleTemperature;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> inputToAirflowTasks;

    /** Processes to which this asset provides input. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** Whether this table is partitioned (true) or not (false). */
    @Attribute
    Boolean isPartitioned;

    /** Whether this asset has been profiled (true) or not (false). */
    @Attribute
    Boolean isProfiled;

    /** Whether preview queries are allowed for this table (true) or not (false). */
    @Attribute
    Boolean isQueryPreview;

    /** Whether this table is temporary (true) or not (false). */
    @Attribute
    Boolean isTemporary;

    /** Time (epoch) at which this asset was last profiled, in milliseconds. */
    @Attribute
    @Date
    Long lastProfiledAt;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> outputFromAirflowTasks;

    /** Processes from which this asset is produced as output. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

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

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtSource> sqlDBTSources;

    /** TBC */
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
     * Builds the minimal object necessary to create a relationship to a GuacamoleTable, from a potentially
     * more-complete GuacamoleTable object.
     *
     * @return the minimal object necessary to relate to the GuacamoleTable
     * @throws InvalidRequestException if any of the minimal set of required properties for a GuacamoleTable relationship are not found in the initial object
     */
    @Override
    public GuacamoleTable trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all GuacamoleTable assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) GuacamoleTable assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all GuacamoleTable assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all GuacamoleTable assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) GuacamoleTables will be included
     * @return a fluent search that includes all GuacamoleTable assets
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
     * Reference to a GuacamoleTable by GUID. Use this to create a relationship to this GuacamoleTable,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the GuacamoleTable to reference
     * @return reference to a GuacamoleTable that can be used for defining a relationship to a GuacamoleTable
     */
    public static GuacamoleTable refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a GuacamoleTable by GUID. Use this to create a relationship to this GuacamoleTable,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the GuacamoleTable to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a GuacamoleTable that can be used for defining a relationship to a GuacamoleTable
     */
    public static GuacamoleTable refByGuid(String guid, Reference.SaveSemantic semantic) {
        return GuacamoleTable._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a GuacamoleTable by qualifiedName. Use this to create a relationship to this GuacamoleTable,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the GuacamoleTable to reference
     * @return reference to a GuacamoleTable that can be used for defining a relationship to a GuacamoleTable
     */
    public static GuacamoleTable refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a GuacamoleTable by qualifiedName. Use this to create a relationship to this GuacamoleTable,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the GuacamoleTable to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a GuacamoleTable that can be used for defining a relationship to a GuacamoleTable
     */
    public static GuacamoleTable refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return GuacamoleTable._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a GuacamoleTable by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the GuacamoleTable to retrieve, either its GUID or its full qualifiedName
     * @return the requested full GuacamoleTable, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GuacamoleTable does not exist or the provided GUID is not a GuacamoleTable
     */
    @JsonIgnore
    public static GuacamoleTable get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a GuacamoleTable by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the GuacamoleTable to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full GuacamoleTable, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GuacamoleTable does not exist or the provided GUID is not a GuacamoleTable
     */
    @JsonIgnore
    public static GuacamoleTable get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof GuacamoleTable) {
                return (GuacamoleTable) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof GuacamoleTable) {
                return (GuacamoleTable) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a GuacamoleTable by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the GuacamoleTable to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the GuacamoleTable, including any relationships
     * @return the requested GuacamoleTable, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GuacamoleTable does not exist or the provided GUID is not a GuacamoleTable
     */
    @JsonIgnore
    public static GuacamoleTable get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a GuacamoleTable by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the GuacamoleTable to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the GuacamoleTable, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the GuacamoleTable
     * @return the requested GuacamoleTable, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GuacamoleTable does not exist or the provided GUID is not a GuacamoleTable
     */
    @JsonIgnore
    public static GuacamoleTable get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = GuacamoleTable.select(client)
                    .where(GuacamoleTable.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof GuacamoleTable) {
                return (GuacamoleTable) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = GuacamoleTable.select(client)
                    .where(GuacamoleTable.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof GuacamoleTable) {
                return (GuacamoleTable) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) GuacamoleTable to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the GuacamoleTable
     * @return true if the GuacamoleTable is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Guacamole table.
     *
     * @param name of the Guacamole table
     * @param schemaQualifiedName unique name of the schema in which this Guacamole table exists
     * @return the minimal request necessary to create the Guacamole table, as a builder
     */
    public static GuacamoleTableBuilder<?, ?> creator(String name, String schemaQualifiedName) {
        String[] tokens = schemaQualifiedName.split("/");
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(tokens);
        String schemaName = StringUtils.getNameFromQualifiedName(schemaQualifiedName);
        String databaseQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(schemaQualifiedName);
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        return GuacamoleTable._internal()
                .name(name)
                .qualifiedName(generateQualifiedName(name, schemaQualifiedName))
                .connectorType(connectorType)
                .schemaName(schemaName)
                .schemaQualifiedName(schemaQualifiedName)
                .schema(Schema.refByQualifiedName(schemaQualifiedName))
                .databaseName(databaseName)
                .databaseQualifiedName(databaseQualifiedName)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a GuacamoleTable.
     *
     * @param qualifiedName of the GuacamoleTable
     * @param name of the GuacamoleTable
     * @return the minimal request necessary to update the GuacamoleTable, as a builder
     */
    public static GuacamoleTableBuilder<?, ?> updater(String qualifiedName, String name) {
        return GuacamoleTable._internal().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Generate a unique Guacamole table name.
     *
     * @param name of the Guacamole table
     * @param schemaQualifiedName unique name of the schema in which this Guacamole table exists
     * @return a unique name for the Guacamole table
     */
    public static String generateQualifiedName(String name, String schemaQualifiedName) {
        return schemaQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to apply an update to a GuacamoleTable, from a potentially
     * more-complete GuacamoleTable object.
     *
     * @return the minimal object necessary to update the GuacamoleTable, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for GuacamoleTable are not found in the initial object
     */
    @Override
    public GuacamoleTableBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, TYPE_NAME, String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a GuacamoleTable.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the GuacamoleTable
     * @param name of the GuacamoleTable
     * @return the updated GuacamoleTable, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GuacamoleTable removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (GuacamoleTable) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a GuacamoleTable.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the GuacamoleTable
     * @param name of the GuacamoleTable
     * @return the updated GuacamoleTable, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GuacamoleTable removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (GuacamoleTable) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a GuacamoleTable.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GuacamoleTable's owners
     * @param qualifiedName of the GuacamoleTable
     * @param name of the GuacamoleTable
     * @return the updated GuacamoleTable, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GuacamoleTable removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (GuacamoleTable) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a GuacamoleTable.
     *
     * @param client connectivity to the Atlan tenant on which to update the GuacamoleTable's certificate
     * @param qualifiedName of the GuacamoleTable
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated GuacamoleTable, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GuacamoleTable updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (GuacamoleTable)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a GuacamoleTable.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GuacamoleTable's certificate
     * @param qualifiedName of the GuacamoleTable
     * @param name of the GuacamoleTable
     * @return the updated GuacamoleTable, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GuacamoleTable removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (GuacamoleTable) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a GuacamoleTable.
     *
     * @param client connectivity to the Atlan tenant on which to update the GuacamoleTable's announcement
     * @param qualifiedName of the GuacamoleTable
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GuacamoleTable updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (GuacamoleTable)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a GuacamoleTable.
     *
     * @param client connectivity to the Atlan client from which to remove the GuacamoleTable's announcement
     * @param qualifiedName of the GuacamoleTable
     * @param name of the GuacamoleTable
     * @return the updated GuacamoleTable, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GuacamoleTable removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (GuacamoleTable) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the GuacamoleTable.
     *
     * @param client connectivity to the Atlan tenant on which to replace the GuacamoleTable's assigned terms
     * @param qualifiedName for the GuacamoleTable
     * @param name human-readable name of the GuacamoleTable
     * @param terms the list of terms to replace on the GuacamoleTable, or null to remove all terms from the GuacamoleTable
     * @return the GuacamoleTable that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static GuacamoleTable replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (GuacamoleTable) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the GuacamoleTable, without replacing existing terms linked to the GuacamoleTable.
     * Note: this operation must make two API calls — one to retrieve the GuacamoleTable's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the GuacamoleTable
     * @param qualifiedName for the GuacamoleTable
     * @param terms the list of terms to append to the GuacamoleTable
     * @return the GuacamoleTable that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static GuacamoleTable appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (GuacamoleTable) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a GuacamoleTable, without replacing all existing terms linked to the GuacamoleTable.
     * Note: this operation must make two API calls — one to retrieve the GuacamoleTable's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the GuacamoleTable
     * @param qualifiedName for the GuacamoleTable
     * @param terms the list of terms to remove from the GuacamoleTable, which must be referenced by GUID
     * @return the GuacamoleTable that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static GuacamoleTable removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (GuacamoleTable) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a GuacamoleTable, without replacing existing Atlan tags linked to the GuacamoleTable.
     * Note: this operation must make two API calls — one to retrieve the GuacamoleTable's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the GuacamoleTable
     * @param qualifiedName of the GuacamoleTable
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated GuacamoleTable
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static GuacamoleTable appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (GuacamoleTable) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a GuacamoleTable, without replacing existing Atlan tags linked to the GuacamoleTable.
     * Note: this operation must make two API calls — one to retrieve the GuacamoleTable's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the GuacamoleTable
     * @param qualifiedName of the GuacamoleTable
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated GuacamoleTable
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static GuacamoleTable appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (GuacamoleTable) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a GuacamoleTable.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a GuacamoleTable
     * @param qualifiedName of the GuacamoleTable
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the GuacamoleTable
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
