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
 * Instance of a Cosmos MongoDB database in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class CosmosMongoDBDatabase extends Asset
        implements ICosmosMongoDBDatabase,
                ICosmosMongoDB,
                IMongoDBDatabase,
                INoSQL,
                ICatalog,
                IAsset,
                IReferenceable,
                IDatabase,
                IMongoDB,
                ISQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "CosmosMongoDBDatabase";

    /** Fixed typeName for CosmosMongoDBDatabases. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Simple name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewName;

    /** Unique name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewQualifiedName;

    /** Account in which the database exists. */
    @Attribute
    ICosmosMongoDBAccount cosmosMongoDBAccount;

    /** Unique name of the account in which this database exists. */
    @Attribute
    String cosmosMongoDBAccountQualifiedName;

    /** Collections that exist within this database. */
    @Attribute
    @Singular
    SortedSet<ICosmosMongoDBCollection> cosmosMongoDBCollections;

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

    /** Collections that exist within this database. */
    @Attribute
    @Singular
    SortedSet<IMongoDBCollection> mongoDBCollections;

    /** Number of collections in the database. */
    @Attribute
    Integer mongoDBDatabaseCollectionCount;

    /** Represents attributes for describing the key schema for the table and indexes. */
    @Attribute
    String noSQLSchemaDefinition;

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

    /** Number of schemas in this database. */
    @Attribute
    Integer schemaCount;

    /** Simple name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaName;

    /** Unique name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaQualifiedName;

    /** Schemas that exist within this database. */
    @Attribute
    @Singular
    SortedSet<ISchema> schemas;

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
     * Builds the minimal object necessary to create a relationship to a CosmosMongoDBDatabase, from a potentially
     * more-complete CosmosMongoDBDatabase object.
     *
     * @return the minimal object necessary to relate to the CosmosMongoDBDatabase
     * @throws InvalidRequestException if any of the minimal set of required properties for a CosmosMongoDBDatabase relationship are not found in the initial object
     */
    @Override
    public CosmosMongoDBDatabase trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all CosmosMongoDBDatabase assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) CosmosMongoDBDatabase assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all CosmosMongoDBDatabase assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all CosmosMongoDBDatabase assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) CosmosMongoDBDatabases will be included
     * @return a fluent search that includes all CosmosMongoDBDatabase assets
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
     * Reference to a CosmosMongoDBDatabase by GUID. Use this to create a relationship to this CosmosMongoDBDatabase,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the CosmosMongoDBDatabase to reference
     * @return reference to a CosmosMongoDBDatabase that can be used for defining a relationship to a CosmosMongoDBDatabase
     */
    public static CosmosMongoDBDatabase refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a CosmosMongoDBDatabase by GUID. Use this to create a relationship to this CosmosMongoDBDatabase,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the CosmosMongoDBDatabase to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a CosmosMongoDBDatabase that can be used for defining a relationship to a CosmosMongoDBDatabase
     */
    public static CosmosMongoDBDatabase refByGuid(String guid, Reference.SaveSemantic semantic) {
        return CosmosMongoDBDatabase._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a CosmosMongoDBDatabase by qualifiedName. Use this to create a relationship to this CosmosMongoDBDatabase,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the CosmosMongoDBDatabase to reference
     * @return reference to a CosmosMongoDBDatabase that can be used for defining a relationship to a CosmosMongoDBDatabase
     */
    public static CosmosMongoDBDatabase refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a CosmosMongoDBDatabase by qualifiedName. Use this to create a relationship to this CosmosMongoDBDatabase,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the CosmosMongoDBDatabase to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a CosmosMongoDBDatabase that can be used for defining a relationship to a CosmosMongoDBDatabase
     */
    public static CosmosMongoDBDatabase refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return CosmosMongoDBDatabase._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a CosmosMongoDBDatabase by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CosmosMongoDBDatabase to retrieve, either its GUID or its full qualifiedName
     * @return the requested full CosmosMongoDBDatabase, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CosmosMongoDBDatabase does not exist or the provided GUID is not a CosmosMongoDBDatabase
     */
    @JsonIgnore
    public static CosmosMongoDBDatabase get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a CosmosMongoDBDatabase by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CosmosMongoDBDatabase to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full CosmosMongoDBDatabase, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CosmosMongoDBDatabase does not exist or the provided GUID is not a CosmosMongoDBDatabase
     */
    @JsonIgnore
    public static CosmosMongoDBDatabase get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof CosmosMongoDBDatabase) {
                return (CosmosMongoDBDatabase) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof CosmosMongoDBDatabase) {
                return (CosmosMongoDBDatabase) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a CosmosMongoDBDatabase by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CosmosMongoDBDatabase to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the CosmosMongoDBDatabase, including any relationships
     * @return the requested CosmosMongoDBDatabase, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CosmosMongoDBDatabase does not exist or the provided GUID is not a CosmosMongoDBDatabase
     */
    @JsonIgnore
    public static CosmosMongoDBDatabase get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a CosmosMongoDBDatabase by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CosmosMongoDBDatabase to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the CosmosMongoDBDatabase, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the CosmosMongoDBDatabase
     * @return the requested CosmosMongoDBDatabase, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CosmosMongoDBDatabase does not exist or the provided GUID is not a CosmosMongoDBDatabase
     */
    @JsonIgnore
    public static CosmosMongoDBDatabase get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = CosmosMongoDBDatabase.select(client)
                    .where(CosmosMongoDBDatabase.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof CosmosMongoDBDatabase) {
                return (CosmosMongoDBDatabase) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = CosmosMongoDBDatabase.select(client)
                    .where(CosmosMongoDBDatabase.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof CosmosMongoDBDatabase) {
                return (CosmosMongoDBDatabase) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) CosmosMongoDBDatabase to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the CosmosMongoDBDatabase
     * @return true if the CosmosMongoDBDatabase is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a CosmosMongoDBDatabase.
     *
     * @param qualifiedName of the CosmosMongoDBDatabase
     * @param name of the CosmosMongoDBDatabase
     * @return the minimal request necessary to update the CosmosMongoDBDatabase, as a builder
     */
    public static CosmosMongoDBDatabaseBuilder<?, ?> updater(String qualifiedName, String name) {
        return CosmosMongoDBDatabase._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a CosmosMongoDBDatabase, from a potentially
     * more-complete CosmosMongoDBDatabase object.
     *
     * @return the minimal object necessary to update the CosmosMongoDBDatabase, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for CosmosMongoDBDatabase are not found in the initial object
     */
    @Override
    public CosmosMongoDBDatabaseBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class CosmosMongoDBDatabaseBuilder<
                    C extends CosmosMongoDBDatabase, B extends CosmosMongoDBDatabaseBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a CosmosMongoDBDatabase.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the CosmosMongoDBDatabase
     * @param name of the CosmosMongoDBDatabase
     * @return the updated CosmosMongoDBDatabase, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBDatabase removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CosmosMongoDBDatabase) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a CosmosMongoDBDatabase.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the CosmosMongoDBDatabase
     * @param name of the CosmosMongoDBDatabase
     * @return the updated CosmosMongoDBDatabase, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBDatabase removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CosmosMongoDBDatabase) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a CosmosMongoDBDatabase.
     *
     * @param client connectivity to the Atlan tenant from which to remove the CosmosMongoDBDatabase's owners
     * @param qualifiedName of the CosmosMongoDBDatabase
     * @param name of the CosmosMongoDBDatabase
     * @return the updated CosmosMongoDBDatabase, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBDatabase removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CosmosMongoDBDatabase) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a CosmosMongoDBDatabase.
     *
     * @param client connectivity to the Atlan tenant on which to update the CosmosMongoDBDatabase's certificate
     * @param qualifiedName of the CosmosMongoDBDatabase
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated CosmosMongoDBDatabase, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBDatabase updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (CosmosMongoDBDatabase)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a CosmosMongoDBDatabase.
     *
     * @param client connectivity to the Atlan tenant from which to remove the CosmosMongoDBDatabase's certificate
     * @param qualifiedName of the CosmosMongoDBDatabase
     * @param name of the CosmosMongoDBDatabase
     * @return the updated CosmosMongoDBDatabase, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBDatabase removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CosmosMongoDBDatabase) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a CosmosMongoDBDatabase.
     *
     * @param client connectivity to the Atlan tenant on which to update the CosmosMongoDBDatabase's announcement
     * @param qualifiedName of the CosmosMongoDBDatabase
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBDatabase updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (CosmosMongoDBDatabase)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a CosmosMongoDBDatabase.
     *
     * @param client connectivity to the Atlan client from which to remove the CosmosMongoDBDatabase's announcement
     * @param qualifiedName of the CosmosMongoDBDatabase
     * @param name of the CosmosMongoDBDatabase
     * @return the updated CosmosMongoDBDatabase, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBDatabase removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CosmosMongoDBDatabase) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the CosmosMongoDBDatabase.
     *
     * @param client connectivity to the Atlan tenant on which to replace the CosmosMongoDBDatabase's assigned terms
     * @param qualifiedName for the CosmosMongoDBDatabase
     * @param name human-readable name of the CosmosMongoDBDatabase
     * @param terms the list of terms to replace on the CosmosMongoDBDatabase, or null to remove all terms from the CosmosMongoDBDatabase
     * @return the CosmosMongoDBDatabase that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBDatabase replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (CosmosMongoDBDatabase) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the CosmosMongoDBDatabase, without replacing existing terms linked to the CosmosMongoDBDatabase.
     * Note: this operation must make two API calls — one to retrieve the CosmosMongoDBDatabase's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the CosmosMongoDBDatabase
     * @param qualifiedName for the CosmosMongoDBDatabase
     * @param terms the list of terms to append to the CosmosMongoDBDatabase
     * @return the CosmosMongoDBDatabase that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static CosmosMongoDBDatabase appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (CosmosMongoDBDatabase) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a CosmosMongoDBDatabase, without replacing all existing terms linked to the CosmosMongoDBDatabase.
     * Note: this operation must make two API calls — one to retrieve the CosmosMongoDBDatabase's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the CosmosMongoDBDatabase
     * @param qualifiedName for the CosmosMongoDBDatabase
     * @param terms the list of terms to remove from the CosmosMongoDBDatabase, which must be referenced by GUID
     * @return the CosmosMongoDBDatabase that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static CosmosMongoDBDatabase removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (CosmosMongoDBDatabase) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a CosmosMongoDBDatabase, without replacing existing Atlan tags linked to the CosmosMongoDBDatabase.
     * Note: this operation must make two API calls — one to retrieve the CosmosMongoDBDatabase's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the CosmosMongoDBDatabase
     * @param qualifiedName of the CosmosMongoDBDatabase
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated CosmosMongoDBDatabase
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static CosmosMongoDBDatabase appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (CosmosMongoDBDatabase) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a CosmosMongoDBDatabase, without replacing existing Atlan tags linked to the CosmosMongoDBDatabase.
     * Note: this operation must make two API calls — one to retrieve the CosmosMongoDBDatabase's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the CosmosMongoDBDatabase
     * @param qualifiedName of the CosmosMongoDBDatabase
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated CosmosMongoDBDatabase
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static CosmosMongoDBDatabase appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (CosmosMongoDBDatabase) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a CosmosMongoDBDatabase.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a CosmosMongoDBDatabase
     * @param qualifiedName of the CosmosMongoDBDatabase
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the CosmosMongoDBDatabase
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
