/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.AssetFilter;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.CompoundQuery;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.QueryFactory;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a MongoDB collection in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class MongoDBCollection extends Asset
        implements IMongoDBCollection, ITable, IMongoDB, ISQL, ICatalog, IAsset, IReferenceable, INoSQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MongoDBCollection";

    /** Fixed typeName for MongoDBCollections. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String alias;

    /** TBC */
    @Attribute
    Long columnCount;

    /** Columns that exist within this table. */
    @Attribute
    @Singular
    SortedSet<IColumn> columns;

    /** TBC */
    @Attribute
    String databaseName;

    /** TBC */
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

    /** TBC */
    @Attribute
    String externalLocation;

    /** TBC */
    @Attribute
    String externalLocationFormat;

    /** TBC */
    @Attribute
    String externalLocationRegion;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ITable> facts;

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
    Boolean isPartitioned;

    /** TBC */
    @Attribute
    Boolean isProfiled;

    /** TBC */
    @Attribute
    Boolean isQueryPreview;

    /** TBC */
    @Attribute
    Boolean isTemporary;

    /** TBC */
    @Attribute
    Long lastProfiledAt;

    /** Average size of an object in the collection. */
    @Attribute
    Long mongoDBCollectionAverageObjectSize;

    /** Seconds after which documents in a time series collection or clustered collection expire. */
    @Attribute
    Long mongoDBCollectionExpireAfterSeconds;

    /** Whether the collection is capped (true) or not (false). */
    @Attribute
    Boolean mongoDBCollectionIsCapped;

    /** Maximum size allowed in a capped collection. */
    @Attribute
    Long mongoDBCollectionMaxSize;

    /** Maximum number of documents allowed in a capped collection. */
    @Attribute
    Long mongoDBCollectionMaximumDocumentCount;

    /** Number of indexes on the collection. */
    @Attribute
    Long mongoDBCollectionNumIndexes;

    /** Number of orphaned documents in the collection. */
    @Attribute
    Long mongoDBCollectionNumOrphanDocs;

    /** Definition of the schema applicable for the collection. */
    @Attribute
    String mongoDBCollectionSchemaDefinition;

    /** Subtype of a MongoDB collection, for example: Capped, Time Series, etc. */
    @Attribute
    String mongoDBCollectionSubtype;

    /** Name of the field containing the date in each time series document. */
    @Attribute
    String mongoDBCollectionTimeField;

    /** Closest match to the time span between consecutive incoming measurements. */
    @Attribute
    String mongoDBCollectionTimeGranularity;

    /** Total size of all indexes. */
    @Attribute
    Long mongoDBCollectionTotalIndexSize;

    /** Database in which the collection exists. */
    @Attribute
    IMongoDBDatabase mongoDBDatabase;

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
    Long partitionCount;

    /** TBC */
    @Attribute
    String partitionList;

    /** TBC */
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

    /** TBC */
    @Attribute
    Long queryCount;

    /** TBC */
    @Attribute
    Long queryCountUpdatedAt;

    /** TBC */
    @Attribute
    @Singular("putQueryPreviewConfig")
    Map<String, String> queryPreviewConfig;

    /** TBC */
    @Attribute
    Long queryUserCount;

    /** TBC */
    @Attribute
    @Singular("putQueryUserMap")
    Map<String, Long> queryUserMap;

    /** TBC */
    @Attribute
    Long rowCount;

    /** Schema in which this table exists. */
    @Attribute
    @JsonProperty("atlanSchema")
    ISchema schema;

    /** TBC */
    @Attribute
    String schemaName;

    /** TBC */
    @Attribute
    String schemaQualifiedName;

    /** TBC */
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

    /** TBC */
    @Attribute
    String tableName;

    /** TBC */
    @Attribute
    String tableQualifiedName;

    /** TBC */
    @Attribute
    String viewName;

    /** TBC */
    @Attribute
    String viewQualifiedName;

    /**
     * Builds the minimal object necessary to create a relationship to a MongoDBCollection, from a potentially
     * more-complete MongoDBCollection object.
     *
     * @return the minimal object necessary to relate to the MongoDBCollection
     * @throws InvalidRequestException if any of the minimal set of required properties for a MongoDBCollection relationship are not found in the initial object
     */
    @Override
    public MongoDBCollection trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all MongoDBCollection assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) MongoDBCollection assets will be included.
     *
     * @return a fluent search that includes all MongoDBCollection assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all MongoDBCollection assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) MongoDBCollection assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all MongoDBCollection assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all MongoDBCollection assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) MongoDBCollections will be included
     * @return a fluent search that includes all MongoDBCollection assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all MongoDBCollection assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) MongoDBCollections will be included
     * @return a fluent search that includes all MongoDBCollection assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client, boolean includeArchived) {
        FluentSearch.FluentSearchBuilder<?, ?> builder =
                FluentSearch.builder(client).where(CompoundQuery.assetType(TYPE_NAME));
        if (!includeArchived) {
            builder.where(CompoundQuery.ACTIVE);
        }
        return builder;
    }

    /**
     * Start an asset filter that will return all MongoDBCollection assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) MongoDBCollection assets will be included.
     *
     * @return an asset filter that includes all MongoDBCollection assets
     * @deprecated replaced by {@link #select()}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all MongoDBCollection assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) MongoDBCollection assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all MongoDBCollection assets
     * @deprecated replaced by {@link #select(AtlanClient)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all MongoDBCollection assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) MongoDBCollections will be included
     * @return an asset filter that includes all MongoDBCollection assets
     * @deprecated replaced by {@link #select(boolean)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all MongoDBCollection assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) MongoDBCollections will be included
     * @return an asset filter that includes all MongoDBCollection assets
     * @deprecated replaced by {@link #select(AtlanClient, boolean)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client, boolean includeArchived) {
        AssetFilter.AssetFilterBuilder builder =
                AssetFilter.builder().client(client).filter(QueryFactory.type(TYPE_NAME));
        if (!includeArchived) {
            builder.filter(QueryFactory.active());
        }
        return builder;
    }

    /**
     * Reference to a MongoDBCollection by GUID.
     *
     * @param guid the GUID of the MongoDBCollection to reference
     * @return reference to a MongoDBCollection that can be used for defining a relationship to a MongoDBCollection
     */
    public static MongoDBCollection refByGuid(String guid) {
        return MongoDBCollection._internal().guid(guid).build();
    }

    /**
     * Reference to a MongoDBCollection by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the MongoDBCollection to reference
     * @return reference to a MongoDBCollection that can be used for defining a relationship to a MongoDBCollection
     */
    public static MongoDBCollection refByQualifiedName(String qualifiedName) {
        return MongoDBCollection._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a MongoDBCollection by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the MongoDBCollection to retrieve, either its GUID or its full qualifiedName
     * @return the requested full MongoDBCollection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MongoDBCollection does not exist or the provided GUID is not a MongoDBCollection
     */
    @JsonIgnore
    public static MongoDBCollection get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a MongoDBCollection by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the MongoDBCollection to retrieve, either its GUID or its full qualifiedName
     * @return the requested full MongoDBCollection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MongoDBCollection does not exist or the provided GUID is not a MongoDBCollection
     */
    @JsonIgnore
    public static MongoDBCollection get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a MongoDBCollection by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the MongoDBCollection to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full MongoDBCollection, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MongoDBCollection does not exist or the provided GUID is not a MongoDBCollection
     */
    @JsonIgnore
    public static MongoDBCollection get(AtlanClient client, String id, boolean includeRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof MongoDBCollection) {
                return (MongoDBCollection) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof MongoDBCollection) {
                return (MongoDBCollection) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a MongoDBCollection by its GUID, complete with all of its relationships.
     *
     * @param guid of the MongoDBCollection to retrieve
     * @return the requested full MongoDBCollection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MongoDBCollection does not exist or the provided GUID is not a MongoDBCollection
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static MongoDBCollection retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a MongoDBCollection by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the MongoDBCollection to retrieve
     * @return the requested full MongoDBCollection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MongoDBCollection does not exist or the provided GUID is not a MongoDBCollection
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static MongoDBCollection retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a MongoDBCollection by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the MongoDBCollection to retrieve
     * @return the requested full MongoDBCollection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MongoDBCollection does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static MongoDBCollection retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a MongoDBCollection by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the MongoDBCollection to retrieve
     * @return the requested full MongoDBCollection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MongoDBCollection does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static MongoDBCollection retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) MongoDBCollection to active.
     *
     * @param qualifiedName for the MongoDBCollection
     * @return true if the MongoDBCollection is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) MongoDBCollection to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the MongoDBCollection
     * @return true if the MongoDBCollection is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a MongoDBCollection.
     *
     * @param qualifiedName of the MongoDBCollection
     * @param name of the MongoDBCollection
     * @return the minimal request necessary to update the MongoDBCollection, as a builder
     */
    public static MongoDBCollectionBuilder<?, ?> updater(String qualifiedName, String name) {
        return MongoDBCollection._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a MongoDBCollection, from a potentially
     * more-complete MongoDBCollection object.
     *
     * @return the minimal object necessary to update the MongoDBCollection, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for MongoDBCollection are not found in the initial object
     */
    @Override
    public MongoDBCollectionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "MongoDBCollection", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a MongoDBCollection.
     *
     * @param qualifiedName of the MongoDBCollection
     * @param name of the MongoDBCollection
     * @return the updated MongoDBCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MongoDBCollection removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a MongoDBCollection.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MongoDBCollection
     * @param name of the MongoDBCollection
     * @return the updated MongoDBCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MongoDBCollection removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MongoDBCollection) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a MongoDBCollection.
     *
     * @param qualifiedName of the MongoDBCollection
     * @param name of the MongoDBCollection
     * @return the updated MongoDBCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MongoDBCollection removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a MongoDBCollection.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MongoDBCollection
     * @param name of the MongoDBCollection
     * @return the updated MongoDBCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MongoDBCollection removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MongoDBCollection) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a MongoDBCollection.
     *
     * @param qualifiedName of the MongoDBCollection
     * @param name of the MongoDBCollection
     * @return the updated MongoDBCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MongoDBCollection removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a MongoDBCollection.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MongoDBCollection's owners
     * @param qualifiedName of the MongoDBCollection
     * @param name of the MongoDBCollection
     * @return the updated MongoDBCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MongoDBCollection removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MongoDBCollection) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a MongoDBCollection.
     *
     * @param qualifiedName of the MongoDBCollection
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MongoDBCollection, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MongoDBCollection updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a MongoDBCollection.
     *
     * @param client connectivity to the Atlan tenant on which to update the MongoDBCollection's certificate
     * @param qualifiedName of the MongoDBCollection
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MongoDBCollection, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MongoDBCollection updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (MongoDBCollection)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MongoDBCollection.
     *
     * @param qualifiedName of the MongoDBCollection
     * @param name of the MongoDBCollection
     * @return the updated MongoDBCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MongoDBCollection removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a MongoDBCollection.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MongoDBCollection's certificate
     * @param qualifiedName of the MongoDBCollection
     * @param name of the MongoDBCollection
     * @return the updated MongoDBCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MongoDBCollection removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MongoDBCollection) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a MongoDBCollection.
     *
     * @param qualifiedName of the MongoDBCollection
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MongoDBCollection updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a MongoDBCollection.
     *
     * @param client connectivity to the Atlan tenant on which to update the MongoDBCollection's announcement
     * @param qualifiedName of the MongoDBCollection
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MongoDBCollection updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (MongoDBCollection)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MongoDBCollection.
     *
     * @param qualifiedName of the MongoDBCollection
     * @param name of the MongoDBCollection
     * @return the updated MongoDBCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MongoDBCollection removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a MongoDBCollection.
     *
     * @param client connectivity to the Atlan client from which to remove the MongoDBCollection's announcement
     * @param qualifiedName of the MongoDBCollection
     * @param name of the MongoDBCollection
     * @return the updated MongoDBCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MongoDBCollection removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MongoDBCollection) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the MongoDBCollection.
     *
     * @param qualifiedName for the MongoDBCollection
     * @param name human-readable name of the MongoDBCollection
     * @param terms the list of terms to replace on the MongoDBCollection, or null to remove all terms from the MongoDBCollection
     * @return the MongoDBCollection that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MongoDBCollection replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the MongoDBCollection.
     *
     * @param client connectivity to the Atlan tenant on which to replace the MongoDBCollection's assigned terms
     * @param qualifiedName for the MongoDBCollection
     * @param name human-readable name of the MongoDBCollection
     * @param terms the list of terms to replace on the MongoDBCollection, or null to remove all terms from the MongoDBCollection
     * @return the MongoDBCollection that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MongoDBCollection replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (MongoDBCollection) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MongoDBCollection, without replacing existing terms linked to the MongoDBCollection.
     * Note: this operation must make two API calls — one to retrieve the MongoDBCollection's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the MongoDBCollection
     * @param terms the list of terms to append to the MongoDBCollection
     * @return the MongoDBCollection that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MongoDBCollection appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the MongoDBCollection, without replacing existing terms linked to the MongoDBCollection.
     * Note: this operation must make two API calls — one to retrieve the MongoDBCollection's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the MongoDBCollection
     * @param qualifiedName for the MongoDBCollection
     * @param terms the list of terms to append to the MongoDBCollection
     * @return the MongoDBCollection that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MongoDBCollection appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MongoDBCollection) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MongoDBCollection, without replacing all existing terms linked to the MongoDBCollection.
     * Note: this operation must make two API calls — one to retrieve the MongoDBCollection's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the MongoDBCollection
     * @param terms the list of terms to remove from the MongoDBCollection, which must be referenced by GUID
     * @return the MongoDBCollection that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MongoDBCollection removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a MongoDBCollection, without replacing all existing terms linked to the MongoDBCollection.
     * Note: this operation must make two API calls — one to retrieve the MongoDBCollection's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the MongoDBCollection
     * @param qualifiedName for the MongoDBCollection
     * @param terms the list of terms to remove from the MongoDBCollection, which must be referenced by GUID
     * @return the MongoDBCollection that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MongoDBCollection removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MongoDBCollection) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a MongoDBCollection, without replacing existing Atlan tags linked to the MongoDBCollection.
     * Note: this operation must make two API calls — one to retrieve the MongoDBCollection's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MongoDBCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MongoDBCollection
     */
    public static MongoDBCollection appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MongoDBCollection, without replacing existing Atlan tags linked to the MongoDBCollection.
     * Note: this operation must make two API calls — one to retrieve the MongoDBCollection's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MongoDBCollection
     * @param qualifiedName of the MongoDBCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MongoDBCollection
     */
    public static MongoDBCollection appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (MongoDBCollection) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MongoDBCollection, without replacing existing Atlan tags linked to the MongoDBCollection.
     * Note: this operation must make two API calls — one to retrieve the MongoDBCollection's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MongoDBCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MongoDBCollection
     */
    public static MongoDBCollection appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return appendAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a MongoDBCollection, without replacing existing Atlan tags linked to the MongoDBCollection.
     * Note: this operation must make two API calls — one to retrieve the MongoDBCollection's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MongoDBCollection
     * @param qualifiedName of the MongoDBCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MongoDBCollection
     */
    public static MongoDBCollection appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (MongoDBCollection) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a MongoDBCollection.
     *
     * @param qualifiedName of the MongoDBCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MongoDBCollection
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MongoDBCollection.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the MongoDBCollection
     * @param qualifiedName of the MongoDBCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MongoDBCollection
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MongoDBCollection.
     *
     * @param qualifiedName of the MongoDBCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MongoDBCollection
     * @deprecated see {@link #appendAtlanTags(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    public static void addAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        addAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a MongoDBCollection.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the MongoDBCollection
     * @param qualifiedName of the MongoDBCollection
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MongoDBCollection
     * @deprecated see {@link #appendAtlanTags(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    public static void addAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a MongoDBCollection.
     *
     * @param qualifiedName of the MongoDBCollection
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MongoDBCollection
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a MongoDBCollection.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a MongoDBCollection
     * @param qualifiedName of the MongoDBCollection
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MongoDBCollection
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
