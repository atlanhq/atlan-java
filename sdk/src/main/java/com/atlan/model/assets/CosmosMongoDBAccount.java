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
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.CompoundQuery;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.QueryFactory;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Cosmos MongoDB account in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class CosmosMongoDBAccount extends Asset
        implements ICosmosMongoDBAccount, ICosmosMongoDB, INoSQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "CosmosMongoDBAccount";

    /** Fixed typeName for CosmosMongoDBAccounts. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Application that is implemented by this asset. */
    @Attribute
    IAppApplication appApplicationImplemented;

    /** Application component that is implemented by this asset. */
    @Attribute
    IAppComponent appComponentImplemented;

    /** The consistency policy configured for the Cosmos MongoDB account. */
    @Attribute
    String cosmosMongoDBAccountConsistencyPolicy;

    /** The Document Endpoint URL for the Cosmos MongoDB account. */
    @Attribute
    String cosmosMongoDBAccountDocumentEndpoint;

    /** Indicates whether automatic failover is enabled for the Cosmos MongoDB account. */
    @Attribute
    Boolean cosmosMongoDBAccountEnableAutomaticFailover;

    /** Indicates whether multiple write locations are enabled for the Cosmos MongoDB account. */
    @Attribute
    Boolean cosmosMongoDBAccountEnableMultipleWriteLocations;

    /** Indicates whether partition key monitoring is enabled for the Cosmos MongoDB account. */
    @Attribute
    Boolean cosmosMongoDBAccountEnablePartitionKeyMonitor;

    /** The unique identifier for the Cosmos MongoDB account. */
    @Attribute
    String cosmosMongoDBAccountInstanceId;

    /** Indicates whether the virtual network filter is enabled for the Cosmos MongoDB account. */
    @Attribute
    Boolean cosmosMongoDBAccountIsVirtualNetworkFilterEnabled;

    /** The locations where the Cosmos MongoDB account is available. */
    @Attribute
    @Singular
    SortedSet<String> cosmosMongoDBAccountLocations;

    /** The MongoDB connection endpoint for the Cosmos MongoDB account. */
    @Attribute
    String cosmosMongoDBAccountMongoEndpoint;

    /** The status of public network access for the Cosmos MongoDB account. */
    @Attribute
    String cosmosMongoDBAccountPublicNetworkAccess;

    /** The read locations configured for the Cosmos MongoDB account. */
    @Attribute
    @Singular
    SortedSet<String> cosmosMongoDBAccountReadLocations;

    /** The resource group that contains the Cosmos MongoDB account. */
    @Attribute
    String cosmosMongoDBAccountResourceGroup;

    /** The ID of the subscription to which the Cosmos MongoDB account belongs. */
    @Attribute
    String cosmosMongoDBAccountSubscriptionId;

    /** The type of the Cosmos MongoDB account, such as RU or VCORE. */
    @Attribute
    String cosmosMongoDBAccountType;

    /** The write locations configured for the Cosmos MongoDB account. */
    @Attribute
    @Singular
    SortedSet<String> cosmosMongoDBAccountWriteLocations;

    /** Number of databases in this Cosmos MongoDB account. */
    @Attribute
    Long cosmosMongoDBDatabaseCount;

    /** Databases that exist within this account. */
    @Attribute
    @Singular
    SortedSet<ICosmosMongoDBDatabase> cosmosMongoDBDatabases;

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

    /**
     * Builds the minimal object necessary to create a relationship to a CosmosMongoDBAccount, from a potentially
     * more-complete CosmosMongoDBAccount object.
     *
     * @return the minimal object necessary to relate to the CosmosMongoDBAccount
     * @throws InvalidRequestException if any of the minimal set of required properties for a CosmosMongoDBAccount relationship are not found in the initial object
     */
    @Override
    public CosmosMongoDBAccount trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all CosmosMongoDBAccount assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) CosmosMongoDBAccount assets will be included.
     *
     * @return a fluent search that includes all CosmosMongoDBAccount assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all CosmosMongoDBAccount assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) CosmosMongoDBAccount assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all CosmosMongoDBAccount assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all CosmosMongoDBAccount assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) CosmosMongoDBAccounts will be included
     * @return a fluent search that includes all CosmosMongoDBAccount assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all CosmosMongoDBAccount assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) CosmosMongoDBAccounts will be included
     * @return a fluent search that includes all CosmosMongoDBAccount assets
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
     * Start an asset filter that will return all CosmosMongoDBAccount assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) CosmosMongoDBAccount assets will be included.
     *
     * @return an asset filter that includes all CosmosMongoDBAccount assets
     * @deprecated replaced by {@link #select()}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all CosmosMongoDBAccount assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) CosmosMongoDBAccount assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all CosmosMongoDBAccount assets
     * @deprecated replaced by {@link #select(AtlanClient)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all CosmosMongoDBAccount assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) CosmosMongoDBAccounts will be included
     * @return an asset filter that includes all CosmosMongoDBAccount assets
     * @deprecated replaced by {@link #select(boolean)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all CosmosMongoDBAccount assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) CosmosMongoDBAccounts will be included
     * @return an asset filter that includes all CosmosMongoDBAccount assets
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
     * Reference to a CosmosMongoDBAccount by GUID. Use this to create a relationship to this CosmosMongoDBAccount,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the CosmosMongoDBAccount to reference
     * @return reference to a CosmosMongoDBAccount that can be used for defining a relationship to a CosmosMongoDBAccount
     */
    public static CosmosMongoDBAccount refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a CosmosMongoDBAccount by GUID. Use this to create a relationship to this CosmosMongoDBAccount,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the CosmosMongoDBAccount to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a CosmosMongoDBAccount that can be used for defining a relationship to a CosmosMongoDBAccount
     */
    public static CosmosMongoDBAccount refByGuid(String guid, Reference.SaveSemantic semantic) {
        return CosmosMongoDBAccount._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a CosmosMongoDBAccount by qualifiedName. Use this to create a relationship to this CosmosMongoDBAccount,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the CosmosMongoDBAccount to reference
     * @return reference to a CosmosMongoDBAccount that can be used for defining a relationship to a CosmosMongoDBAccount
     */
    public static CosmosMongoDBAccount refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a CosmosMongoDBAccount by qualifiedName. Use this to create a relationship to this CosmosMongoDBAccount,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the CosmosMongoDBAccount to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a CosmosMongoDBAccount that can be used for defining a relationship to a CosmosMongoDBAccount
     */
    public static CosmosMongoDBAccount refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return CosmosMongoDBAccount._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a CosmosMongoDBAccount by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the CosmosMongoDBAccount to retrieve, either its GUID or its full qualifiedName
     * @return the requested full CosmosMongoDBAccount, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CosmosMongoDBAccount does not exist or the provided GUID is not a CosmosMongoDBAccount
     */
    @JsonIgnore
    public static CosmosMongoDBAccount get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a CosmosMongoDBAccount by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CosmosMongoDBAccount to retrieve, either its GUID or its full qualifiedName
     * @return the requested full CosmosMongoDBAccount, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CosmosMongoDBAccount does not exist or the provided GUID is not a CosmosMongoDBAccount
     */
    @JsonIgnore
    public static CosmosMongoDBAccount get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a CosmosMongoDBAccount by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CosmosMongoDBAccount to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full CosmosMongoDBAccount, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CosmosMongoDBAccount does not exist or the provided GUID is not a CosmosMongoDBAccount
     */
    @JsonIgnore
    public static CosmosMongoDBAccount get(AtlanClient client, String id, boolean includeRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof CosmosMongoDBAccount) {
                return (CosmosMongoDBAccount) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof CosmosMongoDBAccount) {
                return (CosmosMongoDBAccount) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a CosmosMongoDBAccount by its GUID, complete with all of its relationships.
     *
     * @param guid of the CosmosMongoDBAccount to retrieve
     * @return the requested full CosmosMongoDBAccount, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CosmosMongoDBAccount does not exist or the provided GUID is not a CosmosMongoDBAccount
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static CosmosMongoDBAccount retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a CosmosMongoDBAccount by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the CosmosMongoDBAccount to retrieve
     * @return the requested full CosmosMongoDBAccount, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CosmosMongoDBAccount does not exist or the provided GUID is not a CosmosMongoDBAccount
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static CosmosMongoDBAccount retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a CosmosMongoDBAccount by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the CosmosMongoDBAccount to retrieve
     * @return the requested full CosmosMongoDBAccount, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CosmosMongoDBAccount does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static CosmosMongoDBAccount retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a CosmosMongoDBAccount by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the CosmosMongoDBAccount to retrieve
     * @return the requested full CosmosMongoDBAccount, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CosmosMongoDBAccount does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static CosmosMongoDBAccount retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) CosmosMongoDBAccount to active.
     *
     * @param qualifiedName for the CosmosMongoDBAccount
     * @return true if the CosmosMongoDBAccount is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) CosmosMongoDBAccount to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the CosmosMongoDBAccount
     * @return true if the CosmosMongoDBAccount is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a CosmosMongoDBAccount.
     *
     * @param qualifiedName of the CosmosMongoDBAccount
     * @param name of the CosmosMongoDBAccount
     * @return the minimal request necessary to update the CosmosMongoDBAccount, as a builder
     */
    public static CosmosMongoDBAccountBuilder<?, ?> updater(String qualifiedName, String name) {
        return CosmosMongoDBAccount._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a CosmosMongoDBAccount, from a potentially
     * more-complete CosmosMongoDBAccount object.
     *
     * @return the minimal object necessary to update the CosmosMongoDBAccount, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for CosmosMongoDBAccount are not found in the initial object
     */
    @Override
    public CosmosMongoDBAccountBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(
                TYPE_NAME,
                Map.of(
                        "qualifiedName", this.getQualifiedName(),
                        "name", this.getName()));
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a CosmosMongoDBAccount.
     *
     * @param qualifiedName of the CosmosMongoDBAccount
     * @param name of the CosmosMongoDBAccount
     * @return the updated CosmosMongoDBAccount, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBAccount removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a CosmosMongoDBAccount.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the CosmosMongoDBAccount
     * @param name of the CosmosMongoDBAccount
     * @return the updated CosmosMongoDBAccount, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBAccount removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CosmosMongoDBAccount) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a CosmosMongoDBAccount.
     *
     * @param qualifiedName of the CosmosMongoDBAccount
     * @param name of the CosmosMongoDBAccount
     * @return the updated CosmosMongoDBAccount, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBAccount removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a CosmosMongoDBAccount.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the CosmosMongoDBAccount
     * @param name of the CosmosMongoDBAccount
     * @return the updated CosmosMongoDBAccount, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBAccount removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CosmosMongoDBAccount) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a CosmosMongoDBAccount.
     *
     * @param qualifiedName of the CosmosMongoDBAccount
     * @param name of the CosmosMongoDBAccount
     * @return the updated CosmosMongoDBAccount, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBAccount removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a CosmosMongoDBAccount.
     *
     * @param client connectivity to the Atlan tenant from which to remove the CosmosMongoDBAccount's owners
     * @param qualifiedName of the CosmosMongoDBAccount
     * @param name of the CosmosMongoDBAccount
     * @return the updated CosmosMongoDBAccount, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBAccount removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CosmosMongoDBAccount) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a CosmosMongoDBAccount.
     *
     * @param qualifiedName of the CosmosMongoDBAccount
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated CosmosMongoDBAccount, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBAccount updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a CosmosMongoDBAccount.
     *
     * @param client connectivity to the Atlan tenant on which to update the CosmosMongoDBAccount's certificate
     * @param qualifiedName of the CosmosMongoDBAccount
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated CosmosMongoDBAccount, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBAccount updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (CosmosMongoDBAccount)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a CosmosMongoDBAccount.
     *
     * @param qualifiedName of the CosmosMongoDBAccount
     * @param name of the CosmosMongoDBAccount
     * @return the updated CosmosMongoDBAccount, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBAccount removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a CosmosMongoDBAccount.
     *
     * @param client connectivity to the Atlan tenant from which to remove the CosmosMongoDBAccount's certificate
     * @param qualifiedName of the CosmosMongoDBAccount
     * @param name of the CosmosMongoDBAccount
     * @return the updated CosmosMongoDBAccount, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBAccount removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CosmosMongoDBAccount) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a CosmosMongoDBAccount.
     *
     * @param qualifiedName of the CosmosMongoDBAccount
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBAccount updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a CosmosMongoDBAccount.
     *
     * @param client connectivity to the Atlan tenant on which to update the CosmosMongoDBAccount's announcement
     * @param qualifiedName of the CosmosMongoDBAccount
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBAccount updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (CosmosMongoDBAccount)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a CosmosMongoDBAccount.
     *
     * @param qualifiedName of the CosmosMongoDBAccount
     * @param name of the CosmosMongoDBAccount
     * @return the updated CosmosMongoDBAccount, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBAccount removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a CosmosMongoDBAccount.
     *
     * @param client connectivity to the Atlan client from which to remove the CosmosMongoDBAccount's announcement
     * @param qualifiedName of the CosmosMongoDBAccount
     * @param name of the CosmosMongoDBAccount
     * @return the updated CosmosMongoDBAccount, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBAccount removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CosmosMongoDBAccount) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the CosmosMongoDBAccount.
     *
     * @param qualifiedName for the CosmosMongoDBAccount
     * @param name human-readable name of the CosmosMongoDBAccount
     * @param terms the list of terms to replace on the CosmosMongoDBAccount, or null to remove all terms from the CosmosMongoDBAccount
     * @return the CosmosMongoDBAccount that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBAccount replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the CosmosMongoDBAccount.
     *
     * @param client connectivity to the Atlan tenant on which to replace the CosmosMongoDBAccount's assigned terms
     * @param qualifiedName for the CosmosMongoDBAccount
     * @param name human-readable name of the CosmosMongoDBAccount
     * @param terms the list of terms to replace on the CosmosMongoDBAccount, or null to remove all terms from the CosmosMongoDBAccount
     * @return the CosmosMongoDBAccount that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBAccount replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (CosmosMongoDBAccount) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the CosmosMongoDBAccount, without replacing existing terms linked to the CosmosMongoDBAccount.
     * Note: this operation must make two API calls — one to retrieve the CosmosMongoDBAccount's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the CosmosMongoDBAccount
     * @param terms the list of terms to append to the CosmosMongoDBAccount
     * @return the CosmosMongoDBAccount that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBAccount appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the CosmosMongoDBAccount, without replacing existing terms linked to the CosmosMongoDBAccount.
     * Note: this operation must make two API calls — one to retrieve the CosmosMongoDBAccount's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the CosmosMongoDBAccount
     * @param qualifiedName for the CosmosMongoDBAccount
     * @param terms the list of terms to append to the CosmosMongoDBAccount
     * @return the CosmosMongoDBAccount that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBAccount appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (CosmosMongoDBAccount) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a CosmosMongoDBAccount, without replacing all existing terms linked to the CosmosMongoDBAccount.
     * Note: this operation must make two API calls — one to retrieve the CosmosMongoDBAccount's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the CosmosMongoDBAccount
     * @param terms the list of terms to remove from the CosmosMongoDBAccount, which must be referenced by GUID
     * @return the CosmosMongoDBAccount that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBAccount removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a CosmosMongoDBAccount, without replacing all existing terms linked to the CosmosMongoDBAccount.
     * Note: this operation must make two API calls — one to retrieve the CosmosMongoDBAccount's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the CosmosMongoDBAccount
     * @param qualifiedName for the CosmosMongoDBAccount
     * @param terms the list of terms to remove from the CosmosMongoDBAccount, which must be referenced by GUID
     * @return the CosmosMongoDBAccount that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static CosmosMongoDBAccount removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (CosmosMongoDBAccount) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a CosmosMongoDBAccount, without replacing existing Atlan tags linked to the CosmosMongoDBAccount.
     * Note: this operation must make two API calls — one to retrieve the CosmosMongoDBAccount's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the CosmosMongoDBAccount
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated CosmosMongoDBAccount
     */
    public static CosmosMongoDBAccount appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a CosmosMongoDBAccount, without replacing existing Atlan tags linked to the CosmosMongoDBAccount.
     * Note: this operation must make two API calls — one to retrieve the CosmosMongoDBAccount's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the CosmosMongoDBAccount
     * @param qualifiedName of the CosmosMongoDBAccount
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated CosmosMongoDBAccount
     */
    public static CosmosMongoDBAccount appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (CosmosMongoDBAccount) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a CosmosMongoDBAccount, without replacing existing Atlan tags linked to the CosmosMongoDBAccount.
     * Note: this operation must make two API calls — one to retrieve the CosmosMongoDBAccount's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the CosmosMongoDBAccount
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated CosmosMongoDBAccount
     */
    public static CosmosMongoDBAccount appendAtlanTags(
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
     * Add Atlan tags to a CosmosMongoDBAccount, without replacing existing Atlan tags linked to the CosmosMongoDBAccount.
     * Note: this operation must make two API calls — one to retrieve the CosmosMongoDBAccount's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the CosmosMongoDBAccount
     * @param qualifiedName of the CosmosMongoDBAccount
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated CosmosMongoDBAccount
     */
    public static CosmosMongoDBAccount appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (CosmosMongoDBAccount) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a CosmosMongoDBAccount.
     *
     * @param qualifiedName of the CosmosMongoDBAccount
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the CosmosMongoDBAccount
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a CosmosMongoDBAccount.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the CosmosMongoDBAccount
     * @param qualifiedName of the CosmosMongoDBAccount
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the CosmosMongoDBAccount
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a CosmosMongoDBAccount.
     *
     * @param qualifiedName of the CosmosMongoDBAccount
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the CosmosMongoDBAccount
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
     * Add Atlan tags to a CosmosMongoDBAccount.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the CosmosMongoDBAccount
     * @param qualifiedName of the CosmosMongoDBAccount
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the CosmosMongoDBAccount
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
     * Remove an Atlan tag from a CosmosMongoDBAccount.
     *
     * @param qualifiedName of the CosmosMongoDBAccount
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the CosmosMongoDBAccount
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a CosmosMongoDBAccount.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a CosmosMongoDBAccount
     * @param qualifiedName of the CosmosMongoDBAccount
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the CosmosMongoDBAccount
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
