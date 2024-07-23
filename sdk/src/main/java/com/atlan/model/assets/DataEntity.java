/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022- Atlan Pte. Ltd. */
package com.atlan.model.assets;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.ApiException;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.LogicException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.admin.ApiToken;
import com.atlan.model.core.AssetDeletionResponse;
import com.atlan.model.core.AssetFilter;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.core.AssetResponse;
import com.atlan.model.core.AtlanTag;
import com.atlan.model.core.ConnectionCreationResponse;
import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectionCategory;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanDeleteType;
import com.atlan.model.enums.AtlanIcon;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.enums.AtlanPolicyAction;
import com.atlan.model.enums.AuthPolicyCategory;
import com.atlan.model.enums.AuthPolicyResourceCategory;
import com.atlan.model.enums.AuthPolicyType;
import com.atlan.model.enums.PersonaMetadataAction;
import com.atlan.model.enums.PersonaGlossaryAction;
import com.atlan.model.enums.PurposeMetadataAction;
import com.atlan.model.enums.PersonaDomainAction;
import com.atlan.model.enums.DataAction;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.KeywordFields;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.mesh.DataProductAssetsDSL;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.lineage.FluentLineage;
import com.atlan.model.relations.Reference;
import com.atlan.model.search.FluentSearch;
import com.atlan.model.search.CompoundQuery;
import com.atlan.model.assets.IDataAttribute;
import com.atlan.model.assets.IDataModelVersion;
import com.atlan.model.assets.IAirflowTask;
import com.atlan.model.assets.ILineageProcess;
import com.atlan.model.assets.ISparkJob;
import com.atlan.model.assets.IGlossaryTerm;
import com.atlan.model.assets.IAirflowTask;
import com.atlan.model.assets.ILineageProcess;
import com.atlan.model.assets.ISparkJob;
import com.atlan.model.assets.IDataEntity;
import com.atlan.model.assets.IDataEntity;
import com.atlan.model.assets.IDataEntity;
import com.atlan.model.assets.IDataEntity;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.util.StringUtils;
import com.atlan.util.QueryFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import com.atlan.model.assets.Attribute;
import com.atlan.model.assets.Date;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.IGlossaryTerm;
import com.atlan.model.assets.IDataModeling;
import com.atlan.model.assets.ICatalog;
import com.atlan.model.assets.IAsset;
import com.atlan.model.assets.IReferenceable;

import javax.annotation.processing.Generated;

/**
 * TBC
 */
@Generated(value="com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j

public  class DataEntity extends Asset implements IDataEntity, IDataModeling, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DataEntity";

    /** Fixed typeName for DataEntitys. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute

    @Singular

    SortedSet<IDataAttribute> dataAttributes;

    /** TBC */
    @Attribute



    String dataEntityFullyQualifiedName;

    /** TBC */
    @Attribute



    String dataEntityId;

    /** TBC */
    @Attribute



    String dataEntitySubjectArea;

    /** TBC */
    @Attribute



    String dataModelDomain;

    /** Simple name of the entity in which this asset exists, or empty if it is itself an entity. */
    @Attribute



    String dataModelEntityName;

    /** Unique name of the entity in which this asset exists, or empty if it is itself an entity. */
    @Attribute



    String dataModelEntityQualifiedName;

    /** TBC */
    @Attribute



    String dataModelEnvironment;

    /** TBC */
    @Attribute



    String dataModelId;

    /** Simple name of the data model in which this asset exists, or empty if it is itself a data model. */
    @Attribute



    String dataModelName;

    /** TBC */
    @Attribute



    String dataModelNamespace;

    /** TBC */
    @Attribute



    String dataModelQualifiedName;

    /** TBC */
    @Attribute

    @Singular

    SortedSet<String> dataModelVersionQualifiedNames;

    /** TBC */
    @Attribute

    @Singular

    SortedSet<IDataModelVersion> dataModelVersions;

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

    /** TBC */
    @Attribute

    @Singular

    SortedSet<IGlossaryTerm> mappedGlossaryTerms;

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

    /** TBC */
    @Attribute

    @Singular

    SortedSet<IDataEntity> sourceDataEntities;

    /** TBC */
    @Attribute

    @Singular

    SortedSet<IDataEntity> sourceRelatedDataEntities;

    /** TBC */
    @Attribute

    @Singular

    SortedSet<IDataEntity> targetDataEntities;

    /** TBC */
    @Attribute

    @Singular

    SortedSet<IDataEntity> targetRelatedDataEntities;

    /**
     * Builds the minimal object necessary to create a relationship to a DataEntity, from a potentially
     * more-complete DataEntity object.
     *
     * @return the minimal object necessary to relate to the DataEntity
     * @throws InvalidRequestException if any of the minimal set of required properties for a DataEntity relationship are not found in the initial object
     */
    @Override
    public DataEntity trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all DataEntity assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DataEntity assets will be included.
     *
     * @return a fluent search that includes all DataEntity assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all DataEntity assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DataEntity assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all DataEntity assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all DataEntity assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) DataEntitys will be included
     * @return a fluent search that includes all DataEntity assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all DataEntity assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DataEntitys will be included
     * @return a fluent search that includes all DataEntity assets
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
     * Start an asset filter that will return all DataEntity assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DataEntity assets will be included.
     *
     * @return an asset filter that includes all DataEntity assets
     * @deprecated replaced by {@link #select()}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all DataEntity assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DataEntity assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all DataEntity assets
     * @deprecated replaced by {@link #select(AtlanClient)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all DataEntity assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) DataEntitys will be included
     * @return an asset filter that includes all DataEntity assets
     * @deprecated replaced by {@link #select(boolean)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all DataEntity assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DataEntitys will be included
     * @return an asset filter that includes all DataEntity assets
     * @deprecated replaced by {@link #select(AtlanClient, boolean)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client, boolean includeArchived) {
        AssetFilter.AssetFilterBuilder builder = AssetFilter.builder()
            .client(client)
            .filter(QueryFactory.type(TYPE_NAME));
        if (!includeArchived) {
            builder.filter(QueryFactory.active());
        }
        return builder;
    }

    /**
     * Reference to a DataEntity by GUID. Use this to create a relationship to this DataEntity,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the DataEntity to reference
     * @return reference to a DataEntity that can be used for defining a relationship to a DataEntity
     */
    public static DataEntity refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DataEntity by GUID. Use this to create a relationship to this DataEntity,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the DataEntity to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DataEntity that can be used for defining a relationship to a DataEntity
     */
    public static DataEntity refByGuid(String guid, Reference.SaveSemantic semantic) {
        return DataEntity._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a DataEntity by qualifiedName. Use this to create a relationship to this DataEntity,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the DataEntity to reference
     * @return reference to a DataEntity that can be used for defining a relationship to a DataEntity
     */
    public static DataEntity refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DataEntity by qualifiedName. Use this to create a relationship to this DataEntity,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the DataEntity to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DataEntity that can be used for defining a relationship to a DataEntity
     */
    public static DataEntity refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return DataEntity._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a DataEntity by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the DataEntity to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DataEntity, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataEntity does not exist or the provided GUID is not a DataEntity
     */
    @JsonIgnore
    public static DataEntity get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a DataEntity by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DataEntity to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DataEntity, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataEntity does not exist or the provided GUID is not a DataEntity
     */
    @JsonIgnore
    public static DataEntity get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a DataEntity by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DataEntity to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full DataEntity, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataEntity does not exist or the provided GUID is not a DataEntity
     */
    @JsonIgnore
    public static DataEntity get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof DataEntity) {
                return (DataEntity) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof DataEntity) {
                return (DataEntity) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a DataEntity by its GUID, complete with all of its relationships.
     *
     * @param guid of the DataEntity to retrieve
     * @return the requested full DataEntity, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataEntity does not exist or the provided GUID is not a DataEntity
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static DataEntity retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a DataEntity by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the DataEntity to retrieve
     * @return the requested full DataEntity, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataEntity does not exist or the provided GUID is not a DataEntity
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static DataEntity retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a DataEntity by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the DataEntity to retrieve
     * @return the requested full DataEntity, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataEntity does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static DataEntity retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a DataEntity by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the DataEntity to retrieve
     * @return the requested full DataEntity, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataEntity does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static DataEntity retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) DataEntity to active.
     *
     * @param qualifiedName for the DataEntity
     * @return true if the DataEntity is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) DataEntity to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DataEntity
     * @return true if the DataEntity is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a DataModeling DataEntity.
     *
     * @param name of the DataEntity
     * @param datamodelversion in which the DatraEntity should be created, which must have at least
     *                   a qualifiedName
     * @return the minimal request necessary to create the DataEntity, as a builder
     * @throws InvalidRequestException if the datamodelversion provided is without a qualifiedName
     */
    public static DataEntityBuilder<?, ?> creator(String name, DataModelVersion datamodelversion)
        throws InvalidRequestException {
        validateRelationship(
            DataModelVersion.TYPE_NAME,
            Map.of(
                "connectionQualifiedName", datamodelversion.getConnectionQualifiedName(),
                "qualifiedName", datamodelversion.getQualifiedName()));
        return creator(name, datamodelversion.getConnectionQualifiedName(), datamodelversion.getQualifiedName())
            .dataModelVersion(datamodelversion.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a DataModeling DataEntity.
     *
     * @param name of the DataEntity
     * @param dataModelVersionQualifiedName unique name of the datamodelversion in which the dataentity exists
     * @return the minimal object necessary to create the dataentity, as a builder
     */
    public static DataEntityBuilder<?, ?> creator(String name, String dataModelVersionQualifiedName) {
        String dataModelQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(dataModelVersionQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(dataModelQualifiedName);
        return creator(name, connectionQualifiedName, dataModelVersionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a DataModeling DataEntity.
     *
     * @param name of the DataEntity
     * @param connectionQualifiedName unique name of the connection in which to create the DataEntity
     * @param dataModelVersionQualifiedName unique name of the DataModelVersion in which to create the DataEntity
     * @return the minimal object necessary to create the dataentity, as a builder
     */
    public static DataEntityBuilder<?, ?> creator(
        String name, String connectionQualifiedName, String dataModelVersionQualifiedName) {
//                AtlanConnectorType connectorType =
//         Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return DataEntity._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .name(name)
            .qualifiedName(dataModelVersionQualifiedName + "/" + name)
            .connectorType(AtlanConnectorType.DATA_MODELING)
            .dataModelVersionQualifiedName(dataModelVersionQualifiedName)
            .dataModelVersion(DataModelVersion.refByQualifiedName(dataModelVersionQualifiedName))
            .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a DataEntity.
     *
     * @param qualifiedName of the DataEntity
     * @param name of the DataEntity
     * @return the minimal request necessary to update the DataEntity, as a builder
     */
    public static DataEntityBuilder<?, ?> updater(String qualifiedName, String name) {
        return DataEntity._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DataEntity, from a potentially
     * more-complete DataEntity object.
     *
     * @return the minimal object necessary to update the DataEntity, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DataEntity are not found in the initial object
     */
    @Override
    public DataEntityBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(TYPE_NAME, Map.of(
            "qualifiedName", this.getQualifiedName(),
            "name", this.getName()
        ));
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a DataEntity.
     *
     * @param qualifiedName of the DataEntity
     * @param name of the DataEntity
     * @return the updated DataEntity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataEntity removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a DataEntity.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DataEntity
     * @param name of the DataEntity
     * @return the updated DataEntity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataEntity removeDescription(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (DataEntity) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DataEntity.
     *
     * @param qualifiedName of the DataEntity
     * @param name of the DataEntity
     * @return the updated DataEntity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataEntity removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a DataEntity.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DataEntity
     * @param name of the DataEntity
     * @return the updated DataEntity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataEntity removeUserDescription(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (DataEntity) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DataEntity.
     *
     * @param qualifiedName of the DataEntity
     * @param name of the DataEntity
     * @return the updated DataEntity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataEntity removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a DataEntity.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DataEntity's owners
     * @param qualifiedName of the DataEntity
     * @param name of the DataEntity
     * @return the updated DataEntity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataEntity removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (DataEntity) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DataEntity.
     *
     * @param qualifiedName of the DataEntity
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DataEntity, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataEntity updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a DataEntity.
     *
     * @param client connectivity to the Atlan tenant on which to update the DataEntity's certificate
     * @param qualifiedName of the DataEntity
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DataEntity, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataEntity updateCertificate(AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DataEntity) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DataEntity.
     *
     * @param qualifiedName of the DataEntity
     * @param name of the DataEntity
     * @return the updated DataEntity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataEntity removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a DataEntity.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DataEntity's certificate
     * @param qualifiedName of the DataEntity
     * @param name of the DataEntity
     * @return the updated DataEntity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataEntity removeCertificate(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (DataEntity) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DataEntity.
     *
     * @param qualifiedName of the DataEntity
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataEntity updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a DataEntity.
     *
     * @param client connectivity to the Atlan tenant on which to update the DataEntity's announcement
     * @param qualifiedName of the DataEntity
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataEntity updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (DataEntity) Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DataEntity.
     *
     * @param qualifiedName of the DataEntity
     * @param name of the DataEntity
     * @return the updated DataEntity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataEntity removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a DataEntity.
     *
     * @param client connectivity to the Atlan client from which to remove the DataEntity's announcement
     * @param qualifiedName of the DataEntity
     * @param name of the DataEntity
     * @return the updated DataEntity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataEntity removeAnnouncement(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (DataEntity) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DataEntity.
     *
     * @param qualifiedName for the DataEntity
     * @param name human-readable name of the DataEntity
     * @param terms the list of terms to replace on the DataEntity, or null to remove all terms from the DataEntity
     * @return the DataEntity that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DataEntity replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the DataEntity.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DataEntity's assigned terms
     * @param qualifiedName for the DataEntity
     * @param name human-readable name of the DataEntity
     * @param terms the list of terms to replace on the DataEntity, or null to remove all terms from the DataEntity
     * @return the DataEntity that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DataEntity replaceTerms(AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DataEntity) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DataEntity, without replacing existing terms linked to the DataEntity.
     * Note: this operation must make two API calls — one to retrieve the DataEntity's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the DataEntity
     * @param terms the list of terms to append to the DataEntity
     * @return the DataEntity that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DataEntity appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the DataEntity, without replacing existing terms linked to the DataEntity.
     * Note: this operation must make two API calls — one to retrieve the DataEntity's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DataEntity
     * @param qualifiedName for the DataEntity
     * @param terms the list of terms to append to the DataEntity
     * @return the DataEntity that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DataEntity appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (DataEntity) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DataEntity, without replacing all existing terms linked to the DataEntity.
     * Note: this operation must make two API calls — one to retrieve the DataEntity's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the DataEntity
     * @param terms the list of terms to remove from the DataEntity, which must be referenced by GUID
     * @return the DataEntity that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DataEntity removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a DataEntity, without replacing all existing terms linked to the DataEntity.
     * Note: this operation must make two API calls — one to retrieve the DataEntity's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DataEntity
     * @param qualifiedName for the DataEntity
     * @param terms the list of terms to remove from the DataEntity, which must be referenced by GUID
     * @return the DataEntity that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DataEntity removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (DataEntity) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DataEntity, without replacing existing Atlan tags linked to the DataEntity.
     * Note: this operation must make two API calls — one to retrieve the DataEntity's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DataEntity
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DataEntity
     */
    public static DataEntity appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DataEntity, without replacing existing Atlan tags linked to the DataEntity.
     * Note: this operation must make two API calls — one to retrieve the DataEntity's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DataEntity
     * @param qualifiedName of the DataEntity
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DataEntity
     */
    public static DataEntity appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (DataEntity) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DataEntity, without replacing existing Atlan tags linked to the DataEntity.
     * Note: this operation must make two API calls — one to retrieve the DataEntity's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DataEntity
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DataEntity
     */
    public static DataEntity appendAtlanTags(
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
     * Add Atlan tags to a DataEntity, without replacing existing Atlan tags linked to the DataEntity.
     * Note: this operation must make two API calls — one to retrieve the DataEntity's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DataEntity
     * @param qualifiedName of the DataEntity
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DataEntity
     */
    public static DataEntity appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DataEntity) Asset.appendAtlanTags(
            client,
            TYPE_NAME,
            qualifiedName,
            atlanTagNames,
            propagate,
            removePropagationsOnDelete,
            restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a DataEntity.
     *
     * @param qualifiedName of the DataEntity
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DataEntity
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DataEntity.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the DataEntity
     * @param qualifiedName of the DataEntity
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DataEntity
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DataEntity.
     *
     * @param qualifiedName of the DataEntity
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DataEntity
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
     * Add Atlan tags to a DataEntity.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the DataEntity
     * @param qualifiedName of the DataEntity
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DataEntity
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
     * Remove an Atlan tag from a DataEntity.
     *
     * @param qualifiedName of the DataEntity
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DataEntity
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a DataEntity.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DataEntity
     * @param qualifiedName of the DataEntity
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DataEntity
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
