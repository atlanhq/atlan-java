/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
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
 * Instance of a query in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class AtlanQuery extends Asset implements IAtlanQuery, ISQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Query";

    /** Fixed typeName for AtlanQuerys. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String calculationViewName;

    /** TBC */
    @Attribute
    String calculationViewQualifiedName;

    /** TBC */
    @Attribute
    String collectionQualifiedName;

    /** TBC */
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
    String defaultDatabaseQualifiedName;

    /** TBC */
    @Attribute
    String defaultSchemaQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> inputToAirflowTasks;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISparkJob> inputToSparkJobs;

    /** TBC */
    @Attribute
    Boolean isPrivate;

    /** TBC */
    @Attribute
    Boolean isProfiled;

    /** TBC */
    @Attribute
    Boolean isSqlSnippet;

    /** TBC */
    @Attribute
    Boolean isVisualQuery;

    /** TBC */
    @Attribute
    @Date
    Long lastProfiledAt;

    /** TBC */
    @Attribute
    String longRawQuery;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IModelAttribute> modelImplementedAttributes;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IModelEntity> modelImplementedEntities;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> outputFromAirflowTasks;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISparkJob> outputFromSparkJobs;

    /** TBC */
    @Attribute
    INamespace parent;

    /** TBC */
    @Attribute
    String parentQualifiedName;

    /** TBC */
    @Attribute
    Long queryCount;

    /** TBC */
    @Attribute
    @Date
    Long queryCountUpdatedAt;

    /** TBC */
    @Attribute
    Long queryUserCount;

    /** TBC */
    @Attribute
    @Singular("putQueryUserMap")
    Map<String, Long> queryUserMap;

    /** TBC */
    @Attribute
    String rawQuery;

    /** TBC */
    @Attribute
    String rawQueryText;

    /** TBC */
    @Attribute
    String schemaName;

    /** TBC */
    @Attribute
    String schemaQualifiedName;

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
    @Singular
    SortedSet<ITable> tables;

    /** TBC */
    @Attribute
    String variablesSchemaBase64;

    /** TBC */
    @Attribute
    String viewName;

    /** TBC */
    @Attribute
    String viewQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IView> views;

    /** TBC */
    @Attribute
    String visualBuilderSchemaBase64;

    /**
     * Builds the minimal object necessary to create a relationship to a AtlanQuery, from a potentially
     * more-complete AtlanQuery object.
     *
     * @return the minimal object necessary to relate to the AtlanQuery
     * @throws InvalidRequestException if any of the minimal set of required properties for a AtlanQuery relationship are not found in the initial object
     */
    @Override
    public AtlanQuery trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all AtlanQuery assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) AtlanQuery assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all AtlanQuery assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all AtlanQuery assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) AtlanQuerys will be included
     * @return a fluent search that includes all AtlanQuery assets
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
     * Reference to a AtlanQuery by GUID. Use this to create a relationship to this AtlanQuery,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the AtlanQuery to reference
     * @return reference to a AtlanQuery that can be used for defining a relationship to a AtlanQuery
     */
    public static AtlanQuery refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AtlanQuery by GUID. Use this to create a relationship to this AtlanQuery,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the AtlanQuery to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AtlanQuery that can be used for defining a relationship to a AtlanQuery
     */
    public static AtlanQuery refByGuid(String guid, Reference.SaveSemantic semantic) {
        return AtlanQuery._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a AtlanQuery by qualifiedName. Use this to create a relationship to this AtlanQuery,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the AtlanQuery to reference
     * @return reference to a AtlanQuery that can be used for defining a relationship to a AtlanQuery
     */
    public static AtlanQuery refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AtlanQuery by qualifiedName. Use this to create a relationship to this AtlanQuery,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the AtlanQuery to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AtlanQuery that can be used for defining a relationship to a AtlanQuery
     */
    public static AtlanQuery refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return AtlanQuery._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a AtlanQuery by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AtlanQuery to retrieve, either its GUID or its full qualifiedName
     * @return the requested full AtlanQuery, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AtlanQuery does not exist or the provided GUID is not a AtlanQuery
     */
    @JsonIgnore
    public static AtlanQuery get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a AtlanQuery by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AtlanQuery to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full AtlanQuery, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AtlanQuery does not exist or the provided GUID is not a AtlanQuery
     */
    @JsonIgnore
    public static AtlanQuery get(AtlanClient client, String id, boolean includeAllRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof AtlanQuery) {
                return (AtlanQuery) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof AtlanQuery) {
                return (AtlanQuery) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a AtlanQuery by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AtlanQuery to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AtlanQuery, including any relationships
     * @return the requested AtlanQuery, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AtlanQuery does not exist or the provided GUID is not a AtlanQuery
     */
    @JsonIgnore
    public static AtlanQuery get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a AtlanQuery by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AtlanQuery to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AtlanQuery, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the AtlanQuery
     * @return the requested AtlanQuery, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AtlanQuery does not exist or the provided GUID is not a AtlanQuery
     */
    @JsonIgnore
    public static AtlanQuery get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = AtlanQuery.select(client)
                    .where(AtlanQuery.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof AtlanQuery) {
                return (AtlanQuery) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = AtlanQuery.select(client)
                    .where(AtlanQuery.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof AtlanQuery) {
                return (AtlanQuery) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) AtlanQuery to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the AtlanQuery
     * @return true if the AtlanQuery is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Query.
     *
     * @param name of the Query
     * @param parentFolder in which the Query should be created, which must have at least
     *               a qualifiedName
     * @return the minimal request necessary to create the Query, as a builder
     * @throws InvalidRequestException if the parentFolder provided is without a qualifiedName
     */
    public static AtlanQueryBuilder<?, ?> creator(String name, Folder parentFolder) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", parentFolder.getQualifiedName());
        map.put("collectionQualifiedName", parentFolder.getCollectionQualifiedName());
        validateRelationship(AtlanCollection.TYPE_NAME, map);
        return creator(name, parentFolder.getCollectionQualifiedName(), parentFolder.getQualifiedName())
                .parent(parentFolder.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Query.
     *
     * @param name of the Query
     * @param collection in which the Query should be created, which must have at least
     *               a qualifiedName
     * @return the minimal request necessary to create the Query, as a builder
     * @throws InvalidRequestException if the collection provided is without a qualifiedName
     */
    public static AtlanQueryBuilder<?, ?> creator(String name, AtlanCollection collection)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", collection.getQualifiedName());
        validateRelationship(AtlanCollection.TYPE_NAME, map);
        return creator(name, collection.getQualifiedName(), null).parent(collection.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Query.
     *
     * @param name of the Query
     * @param collectionQualifiedName unique name of the AtlanCollection in which the Query should be created
     * @param parentFolderQualifiedName unique name of the Folder in which this Query should be created, or null if it should be created directly in the collection
     * @return the minimal request necessary to create the Query, as a builder
     */
    public static AtlanQueryBuilder<?, ?> creator(
            String name, String collectionQualifiedName, String parentFolderQualifiedName) {
        String qualifiedName = parentFolderQualifiedName == null
                ? generateQualifiedName(name, collectionQualifiedName)
                : generateQualifiedName(name, parentFolderQualifiedName);
        AtlanQueryBuilder<?, ?> builder = AtlanQuery._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(qualifiedName)
                .collectionQualifiedName(collectionQualifiedName)
                .parentQualifiedName(collectionQualifiedName)
                .parent(AtlanCollection.refByQualifiedName(collectionQualifiedName));
        if (parentFolderQualifiedName != null) {
            builder.parentQualifiedName(parentFolderQualifiedName)
                    .parent(Folder.refByQualifiedName(parentFolderQualifiedName));
        }
        return builder;
    }

    /**
     * Generate a unique Query.
     *
     * @param name of the Query
     * @param parentQualifiedName unique name of the collection or folder in which this Query exists
     * @return a unique name for the Query
     */
    public static String generateQualifiedName(String name, String parentQualifiedName) {
        return parentQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a AtlanQuery.
     *
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @param collectionQualifiedName qualifiedName of the parent collection the query is contained within
     * @param parentQualifiedName qualifiedName of the parent collection or folder the query is contained within
     * @return the minimal request necessary to update the AtlanQuery, as a builder
     */
    public static AtlanQueryBuilder<?, ?> updater(
            String qualifiedName, String name, String collectionQualifiedName, String parentQualifiedName) {
        INamespace parent;
        if (collectionQualifiedName.equals(parentQualifiedName)) {
            parent = AtlanCollection.refByQualifiedName(collectionQualifiedName);
        } else {
            parent = Folder.refByQualifiedName(parentQualifiedName);
        }
        return AtlanQuery._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name)
                .parent(parent)
                .parentQualifiedName(parentQualifiedName)
                .collectionQualifiedName(collectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AtlanQuery, from a potentially
     * more-complete AtlanQuery object.
     *
     * @return the minimal object necessary to update the AtlanQuery, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AtlanQuery are not found in the initial object
     */
    @Override
    public AtlanQueryBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        map.put("collectionQualifiedName", this.getCollectionQualifiedName());
        map.put("parentQualifiedName", this.getParentQualifiedName());
        validateRequired(TYPE_NAME, map);
        return updater(
                this.getQualifiedName(),
                this.getName(),
                this.getCollectionQualifiedName(),
                this.getParentQualifiedName());
    }

    public abstract static class AtlanQueryBuilder<C extends AtlanQuery, B extends AtlanQueryBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {

        private static final String DEFAULT_VARIABLE_SCHEMA =
                "{\"customvariablesDateTimeFormat\":{\"defaultDateFormat\":\"YYYY-MM-DD\",\"defaultTimeFormat\":\"HH:mm\"},\"customVariables\":[]}";

        public B withRawQuery(String schemaQualifiedName, String query) {
            String databaseQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(schemaQualifiedName);
            String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
            AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
            return connectionName(connectorType.getValue())
                    .connectionQualifiedName(connectionQualifiedName)
                    .connectorType(connectorType)
                    .defaultDatabaseQualifiedName(databaseQualifiedName)
                    .defaultSchemaQualifiedName(schemaQualifiedName)
                    .isVisualQuery(false)
                    .rawQueryText(query)
                    .variablesSchemaBase64(Base64.getEncoder()
                            .encodeToString(DEFAULT_VARIABLE_SCHEMA.getBytes(StandardCharsets.UTF_8)));
        }
    }

    /**
     * Find a query by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the query, if found.
     *
     * @param client connectivity to the Atlan tenant in which to search for the query
     * @param name of the query
     * @return all queries with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the query does not exist
     */
    public static List<AtlanQuery> findByName(AtlanClient client, String name) throws AtlanException {
        return findByName(client, name, (List<AtlanField>) null);
    }

    /**
     * Find a query by its human-readable name.
     *
     * @param client connectivity to the Atlan tenant in which to search for the query
     * @param name of the query
     * @param attributes an optional collection of attributes to retrieve for the query
     * @return all queries with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the query does not exist
     */
    public static List<AtlanQuery> findByName(AtlanClient client, String name, Collection<String> attributes)
            throws AtlanException {
        List<AtlanQuery> results = new ArrayList<>();
        AtlanQuery.select(client)
                .where(NAME.eq(name))
                ._includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof AtlanQuery)
                .forEach(q -> results.add((AtlanQuery) q));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.QUERY_NOT_FOUND_BY_NAME, name);
        }
        return results;
    }

    /**
     * Find a query by its human-readable name.
     *
     * @param client connectivity to the Atlan tenant in which to search for the query
     * @param name of the query
     * @param attributes an optional collection of attributes (checked) to retrieve for the query
     * @return all queries with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the query does not exist
     */
    public static List<AtlanQuery> findByName(AtlanClient client, String name, List<AtlanField> attributes)
            throws AtlanException {
        List<AtlanQuery> results = new ArrayList<>();
        AtlanQuery.select(client)
                .where(NAME.eq(name))
                .includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof AtlanQuery)
                .forEach(q -> results.add((AtlanQuery) q));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.QUERY_NOT_FOUND_BY_NAME, name);
        }
        return results;
    }

    /**
     * Remove the system description from a AtlanQuery.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AtlanQuery's description
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @param collectionQualifiedName qualifiedName of the AtlanQuery's collection
     * @param parentQualifiedName qualifiedName of the AtlanQuery's parent namespace
     * @return the updated AtlanQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery removeDescription(
            AtlanClient client,
            String qualifiedName,
            String name,
            String collectionQualifiedName,
            String parentQualifiedName)
            throws AtlanException {
        return (AtlanQuery) Asset.removeDescription(
                client, updater(qualifiedName, name, collectionQualifiedName, parentQualifiedName));
    }

    /**
     * Remove the user's description from a AtlanQuery.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AtlanQuery's description
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @param collectionQualifiedName qualifiedName of the AtlanQuery's collection
     * @param parentQualifiedName qualifiedName of the AtlanQuery's parent namespace
     * @return the updated AtlanQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery removeUserDescription(
            AtlanClient client,
            String qualifiedName,
            String name,
            String collectionQualifiedName,
            String parentQualifiedName)
            throws AtlanException {
        return (AtlanQuery) Asset.removeUserDescription(
                client, updater(qualifiedName, name, collectionQualifiedName, parentQualifiedName));
    }

    /**
     * Remove the owners from a AtlanQuery.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AtlanQuery's owners
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @param collectionQualifiedName qualifiedName of the AtlanQuery's collection
     * @param parentQualifiedName qualifiedName of the AtlanQuery's parent namespace
     * @return the updated AtlanQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery removeOwners(
            AtlanClient client,
            String qualifiedName,
            String name,
            String collectionQualifiedName,
            String parentQualifiedName)
            throws AtlanException {
        return (AtlanQuery)
                Asset.removeOwners(client, updater(qualifiedName, name, collectionQualifiedName, parentQualifiedName));
    }

    /**
     * Update the certificate on a AtlanQuery.
     *
     * @param client connectivity to the Atlan tenant on which to update the AtlanQuery's certificate
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @param collectionQualifiedName qualifiedName of the AtlanQuery's collection
     * @param parentQualifiedName qualifiedName of the AtlanQuery's parent namespace
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AtlanQuery, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery updateCertificate(
            AtlanClient client,
            String qualifiedName,
            String name,
            String collectionQualifiedName,
            String parentQualifiedName,
            CertificateStatus certificate,
            String message)
            throws AtlanException {
        return (AtlanQuery) Asset.updateCertificate(
                client,
                updater(qualifiedName, name, collectionQualifiedName, parentQualifiedName),
                certificate,
                message);
    }

    /**
     * Remove the certificate from a AtlanQuery.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AtlanQuery's certificate
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @param collectionQualifiedName qualifiedName of the AtlanQuery's collection
     * @param parentQualifiedName qualifiedName of the AtlanQuery's parent namespace
     * @return the updated AtlanQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery removeCertificate(
            AtlanClient client,
            String qualifiedName,
            String name,
            String collectionQualifiedName,
            String parentQualifiedName)
            throws AtlanException {
        return (AtlanQuery) Asset.removeCertificate(
                client, updater(qualifiedName, name, collectionQualifiedName, parentQualifiedName));
    }

    /**
     * Update the announcement on a AtlanQuery.
     *
     * @param client connectivity to the Atlan tenant on which to update the AtlanQuery's announcement
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @param collectionQualifiedName qualifiedName of the AtlanQuery's collection
     * @param parentQualifiedName qualifiedName of the AtlanQuery's parent namespace
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the updated AtlanQuery, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery updateAnnouncement(
            AtlanClient client,
            String qualifiedName,
            String name,
            String collectionQualifiedName,
            String parentQualifiedName,
            AtlanAnnouncementType type,
            String title,
            String message)
            throws AtlanException {
        return (AtlanQuery) Asset.updateAnnouncement(
                client,
                updater(qualifiedName, name, collectionQualifiedName, parentQualifiedName),
                type,
                title,
                message);
    }

    /**
     * Remove the announcement from a AtlanQuery.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AtlanQuery's announcement
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @param collectionQualifiedName qualifiedName of the AtlanQuery's collection
     * @param parentQualifiedName qualifiedName of the AtlanQuery's parent namespace
     * @return the updated AtlanQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery removeAnnouncement(
            AtlanClient client,
            String qualifiedName,
            String name,
            String collectionQualifiedName,
            String parentQualifiedName)
            throws AtlanException {
        return (AtlanQuery) Asset.removeAnnouncement(
                client, updater(qualifiedName, name, collectionQualifiedName, parentQualifiedName));
    }

    /**
     * Add Atlan tags to a AtlanQuery, without replacing existing Atlan tags linked to the AtlanQuery.
     * Note: this operation must make two API calls — one to retrieve the AtlanQuery's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AtlanQuery
     * @param qualifiedName of the AtlanQuery
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated AtlanQuery
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static AtlanQuery appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (AtlanQuery) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AtlanQuery, without replacing existing Atlan tags linked to the AtlanQuery.
     * Note: this operation must make two API calls — one to retrieve the AtlanQuery's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AtlanQuery
     * @param qualifiedName of the AtlanQuery
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated AtlanQuery
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static AtlanQuery appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (AtlanQuery) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a AtlanQuery.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a AtlanQuery
     * @param qualifiedName of the AtlanQuery
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the AtlanQuery
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
