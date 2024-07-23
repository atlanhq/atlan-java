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
import com.atlan.model.assets.IAirflowTask;
import com.atlan.model.assets.IColumnProcess;
import com.atlan.model.assets.ICatalog;
import com.atlan.model.assets.IMatillionComponent;
import com.atlan.model.assets.ICatalog;
import com.atlan.model.assets.ISparkJob;
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
import com.atlan.model.assets.ILineageProcess;
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

public  class DataAttributeMappingProcess extends Asset implements IDataAttributeMappingProcess, ILineageProcess, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DataAttributeMappingProcess";

    /** Fixed typeName for DataAttributeMappingProcesss. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Tasks that exist within this process. */
    @Attribute

    @Singular

    SortedSet<IAirflowTask> airflowTasks;

    /** Parsed AST of the code or SQL statements that describe the logic of this process. */
    @Attribute



    String ast;

    /** Code that ran within the process. */
    @Attribute



    String code;

    /** Processes that detail column-level lineage for this process. */
    @Attribute

    @Singular

    SortedSet<IColumnProcess> columnProcesses;

    /** Assets that are inputs to this process. */
    @Attribute

    @Singular

    SortedSet<ICatalog> inputs;

    /** Matillion component that contains the logic for this lineage process. */
    @Attribute



    IMatillionComponent matillionComponent;

    /** Assets that are outputs from this process. */
    @Attribute

    @Singular

    SortedSet<ICatalog> outputs;

    /** TBC */
    @Attribute

    @Singular

    SortedSet<ISparkJob> sparkJobs;

    /** SQL query that ran to produce the outputs. */
    @Attribute



    String sql;

    /**
     * Builds the minimal object necessary to create a relationship to a DataAttributeMappingProcess, from a potentially
     * more-complete DataAttributeMappingProcess object.
     *
     * @return the minimal object necessary to relate to the DataAttributeMappingProcess
     * @throws InvalidRequestException if any of the minimal set of required properties for a DataAttributeMappingProcess relationship are not found in the initial object
     */
    @Override
    public DataAttributeMappingProcess trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all DataAttributeMappingProcess assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DataAttributeMappingProcess assets will be included.
     *
     * @return a fluent search that includes all DataAttributeMappingProcess assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all DataAttributeMappingProcess assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DataAttributeMappingProcess assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all DataAttributeMappingProcess assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all DataAttributeMappingProcess assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) DataAttributeMappingProcesss will be included
     * @return a fluent search that includes all DataAttributeMappingProcess assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all DataAttributeMappingProcess assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DataAttributeMappingProcesss will be included
     * @return a fluent search that includes all DataAttributeMappingProcess assets
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
     * Start an asset filter that will return all DataAttributeMappingProcess assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DataAttributeMappingProcess assets will be included.
     *
     * @return an asset filter that includes all DataAttributeMappingProcess assets
     * @deprecated replaced by {@link #select()}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all DataAttributeMappingProcess assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DataAttributeMappingProcess assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all DataAttributeMappingProcess assets
     * @deprecated replaced by {@link #select(AtlanClient)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all DataAttributeMappingProcess assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) DataAttributeMappingProcesss will be included
     * @return an asset filter that includes all DataAttributeMappingProcess assets
     * @deprecated replaced by {@link #select(boolean)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all DataAttributeMappingProcess assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DataAttributeMappingProcesss will be included
     * @return an asset filter that includes all DataAttributeMappingProcess assets
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
     * Reference to a DataAttributeMappingProcess by GUID. Use this to create a relationship to this DataAttributeMappingProcess,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the DataAttributeMappingProcess to reference
     * @return reference to a DataAttributeMappingProcess that can be used for defining a relationship to a DataAttributeMappingProcess
     */
    public static DataAttributeMappingProcess refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DataAttributeMappingProcess by GUID. Use this to create a relationship to this DataAttributeMappingProcess,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the DataAttributeMappingProcess to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DataAttributeMappingProcess that can be used for defining a relationship to a DataAttributeMappingProcess
     */
    public static DataAttributeMappingProcess refByGuid(String guid, Reference.SaveSemantic semantic) {
        return DataAttributeMappingProcess._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a DataAttributeMappingProcess by qualifiedName. Use this to create a relationship to this DataAttributeMappingProcess,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the DataAttributeMappingProcess to reference
     * @return reference to a DataAttributeMappingProcess that can be used for defining a relationship to a DataAttributeMappingProcess
     */
    public static DataAttributeMappingProcess refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DataAttributeMappingProcess by qualifiedName. Use this to create a relationship to this DataAttributeMappingProcess,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the DataAttributeMappingProcess to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DataAttributeMappingProcess that can be used for defining a relationship to a DataAttributeMappingProcess
     */
    public static DataAttributeMappingProcess refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return DataAttributeMappingProcess._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a DataAttributeMappingProcess by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the DataAttributeMappingProcess to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DataAttributeMappingProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataAttributeMappingProcess does not exist or the provided GUID is not a DataAttributeMappingProcess
     */
    @JsonIgnore
    public static DataAttributeMappingProcess get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a DataAttributeMappingProcess by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DataAttributeMappingProcess to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DataAttributeMappingProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataAttributeMappingProcess does not exist or the provided GUID is not a DataAttributeMappingProcess
     */
    @JsonIgnore
    public static DataAttributeMappingProcess get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a DataAttributeMappingProcess by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DataAttributeMappingProcess to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full DataAttributeMappingProcess, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataAttributeMappingProcess does not exist or the provided GUID is not a DataAttributeMappingProcess
     */
    @JsonIgnore
    public static DataAttributeMappingProcess get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof DataAttributeMappingProcess) {
                return (DataAttributeMappingProcess) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof DataAttributeMappingProcess) {
                return (DataAttributeMappingProcess) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a DataAttributeMappingProcess by its GUID, complete with all of its relationships.
     *
     * @param guid of the DataAttributeMappingProcess to retrieve
     * @return the requested full DataAttributeMappingProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataAttributeMappingProcess does not exist or the provided GUID is not a DataAttributeMappingProcess
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static DataAttributeMappingProcess retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a DataAttributeMappingProcess by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the DataAttributeMappingProcess to retrieve
     * @return the requested full DataAttributeMappingProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataAttributeMappingProcess does not exist or the provided GUID is not a DataAttributeMappingProcess
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static DataAttributeMappingProcess retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a DataAttributeMappingProcess by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the DataAttributeMappingProcess to retrieve
     * @return the requested full DataAttributeMappingProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataAttributeMappingProcess does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static DataAttributeMappingProcess retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a DataAttributeMappingProcess by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the DataAttributeMappingProcess to retrieve
     * @return the requested full DataAttributeMappingProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataAttributeMappingProcess does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static DataAttributeMappingProcess retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) DataAttributeMappingProcess to active.
     *
     * @param qualifiedName for the DataAttributeMappingProcess
     * @return true if the DataAttributeMappingProcess is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) DataAttributeMappingProcess to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DataAttributeMappingProcess
     * @return true if the DataAttributeMappingProcess is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a DataAttributeMappingProcess
     *
     * @param sourceQualifiedName unique name of the source DataAttribute
     * @param targetQualifiedName unique name of the target DataAttribute
     * @param connectionQualifiedName unique name of the connection in which to create the DataAttributeMappingProcess
     * @return the minimal object necessary to create the DataAttributeMappingProcess, as a builder
     */
    public static DataAttributeMappingProcessBuilder<?, ?> creator(String sourceQualifiedName, String targetQualifiedName, String connectionQualifiedName){
        List<ICatalog> inputs = Collections.singletonList(DataAttribute.refByQualifiedName(sourceQualifiedName));
        List<ICatalog> outputs = Collections.singletonList(DataAttribute.refByQualifiedName(targetQualifiedName));
        return DataAttributeMappingProcess._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .qualifiedName(targetQualifiedName)
            .name(generateProcessName(sourceQualifiedName, targetQualifiedName))
            .connectorType(AtlanConnectorType.DATA_MODELING)
            .connectionQualifiedName(connectionQualifiedName)
            .inputs(inputs)
            .outputs(outputs);
    }

    /**
     * Builds the minimal string name for DataAttributeMappingProcess name using the source and target QualifiedName.
     *
     * @param sourceQualifiedName
     * @param targetQualifiedName
     * @return the name for DataAttributeMappingProcess
     */
    public static String generateProcessName(String sourceQualifiedName, String targetQualifiedName){
        return sourceQualifiedName.substring(sourceQualifiedName.lastIndexOf(('/') + 1)) + ">" + targetQualifiedName.substring(targetQualifiedName.lastIndexOf(('/') + 1));

    }

    /**
     * Builds the minimal object necessary to update a DataAttributeMappingProcess.
     *
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param name of the DataAttributeMappingProcess
     * @return the minimal request necessary to update the DataAttributeMappingProcess, as a builder
     */
    public static DataAttributeMappingProcessBuilder<?, ?> updater(String qualifiedName, String name) {
        return DataAttributeMappingProcess._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DataAttributeMappingProcess, from a potentially
     * more-complete DataAttributeMappingProcess object.
     *
     * @return the minimal object necessary to update the DataAttributeMappingProcess, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DataAttributeMappingProcess are not found in the initial object
     */
    @Override
    public DataAttributeMappingProcessBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(TYPE_NAME, Map.of(
            "qualifiedName", this.getQualifiedName(),
            "name", this.getName()
        ));
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a DataAttributeMappingProcess.
     *
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param name of the DataAttributeMappingProcess
     * @return the updated DataAttributeMappingProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataAttributeMappingProcess removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a DataAttributeMappingProcess.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param name of the DataAttributeMappingProcess
     * @return the updated DataAttributeMappingProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataAttributeMappingProcess removeDescription(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (DataAttributeMappingProcess) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DataAttributeMappingProcess.
     *
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param name of the DataAttributeMappingProcess
     * @return the updated DataAttributeMappingProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataAttributeMappingProcess removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a DataAttributeMappingProcess.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param name of the DataAttributeMappingProcess
     * @return the updated DataAttributeMappingProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataAttributeMappingProcess removeUserDescription(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (DataAttributeMappingProcess) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DataAttributeMappingProcess.
     *
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param name of the DataAttributeMappingProcess
     * @return the updated DataAttributeMappingProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataAttributeMappingProcess removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a DataAttributeMappingProcess.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DataAttributeMappingProcess's owners
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param name of the DataAttributeMappingProcess
     * @return the updated DataAttributeMappingProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataAttributeMappingProcess removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (DataAttributeMappingProcess) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DataAttributeMappingProcess.
     *
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DataAttributeMappingProcess, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataAttributeMappingProcess updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a DataAttributeMappingProcess.
     *
     * @param client connectivity to the Atlan tenant on which to update the DataAttributeMappingProcess's certificate
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DataAttributeMappingProcess, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataAttributeMappingProcess updateCertificate(AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DataAttributeMappingProcess) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DataAttributeMappingProcess.
     *
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param name of the DataAttributeMappingProcess
     * @return the updated DataAttributeMappingProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataAttributeMappingProcess removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a DataAttributeMappingProcess.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DataAttributeMappingProcess's certificate
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param name of the DataAttributeMappingProcess
     * @return the updated DataAttributeMappingProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataAttributeMappingProcess removeCertificate(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (DataAttributeMappingProcess) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DataAttributeMappingProcess.
     *
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataAttributeMappingProcess updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a DataAttributeMappingProcess.
     *
     * @param client connectivity to the Atlan tenant on which to update the DataAttributeMappingProcess's announcement
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataAttributeMappingProcess updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (DataAttributeMappingProcess) Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DataAttributeMappingProcess.
     *
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param name of the DataAttributeMappingProcess
     * @return the updated DataAttributeMappingProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataAttributeMappingProcess removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a DataAttributeMappingProcess.
     *
     * @param client connectivity to the Atlan client from which to remove the DataAttributeMappingProcess's announcement
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param name of the DataAttributeMappingProcess
     * @return the updated DataAttributeMappingProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataAttributeMappingProcess removeAnnouncement(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (DataAttributeMappingProcess) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DataAttributeMappingProcess.
     *
     * @param qualifiedName for the DataAttributeMappingProcess
     * @param name human-readable name of the DataAttributeMappingProcess
     * @param terms the list of terms to replace on the DataAttributeMappingProcess, or null to remove all terms from the DataAttributeMappingProcess
     * @return the DataAttributeMappingProcess that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DataAttributeMappingProcess replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the DataAttributeMappingProcess.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DataAttributeMappingProcess's assigned terms
     * @param qualifiedName for the DataAttributeMappingProcess
     * @param name human-readable name of the DataAttributeMappingProcess
     * @param terms the list of terms to replace on the DataAttributeMappingProcess, or null to remove all terms from the DataAttributeMappingProcess
     * @return the DataAttributeMappingProcess that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DataAttributeMappingProcess replaceTerms(AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DataAttributeMappingProcess) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DataAttributeMappingProcess, without replacing existing terms linked to the DataAttributeMappingProcess.
     * Note: this operation must make two API calls — one to retrieve the DataAttributeMappingProcess's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the DataAttributeMappingProcess
     * @param terms the list of terms to append to the DataAttributeMappingProcess
     * @return the DataAttributeMappingProcess that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DataAttributeMappingProcess appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the DataAttributeMappingProcess, without replacing existing terms linked to the DataAttributeMappingProcess.
     * Note: this operation must make two API calls — one to retrieve the DataAttributeMappingProcess's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DataAttributeMappingProcess
     * @param qualifiedName for the DataAttributeMappingProcess
     * @param terms the list of terms to append to the DataAttributeMappingProcess
     * @return the DataAttributeMappingProcess that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DataAttributeMappingProcess appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (DataAttributeMappingProcess) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DataAttributeMappingProcess, without replacing all existing terms linked to the DataAttributeMappingProcess.
     * Note: this operation must make two API calls — one to retrieve the DataAttributeMappingProcess's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the DataAttributeMappingProcess
     * @param terms the list of terms to remove from the DataAttributeMappingProcess, which must be referenced by GUID
     * @return the DataAttributeMappingProcess that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DataAttributeMappingProcess removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a DataAttributeMappingProcess, without replacing all existing terms linked to the DataAttributeMappingProcess.
     * Note: this operation must make two API calls — one to retrieve the DataAttributeMappingProcess's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DataAttributeMappingProcess
     * @param qualifiedName for the DataAttributeMappingProcess
     * @param terms the list of terms to remove from the DataAttributeMappingProcess, which must be referenced by GUID
     * @return the DataAttributeMappingProcess that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DataAttributeMappingProcess removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (DataAttributeMappingProcess) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DataAttributeMappingProcess, without replacing existing Atlan tags linked to the DataAttributeMappingProcess.
     * Note: this operation must make two API calls — one to retrieve the DataAttributeMappingProcess's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DataAttributeMappingProcess
     */
    public static DataAttributeMappingProcess appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DataAttributeMappingProcess, without replacing existing Atlan tags linked to the DataAttributeMappingProcess.
     * Note: this operation must make two API calls — one to retrieve the DataAttributeMappingProcess's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DataAttributeMappingProcess
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DataAttributeMappingProcess
     */
    public static DataAttributeMappingProcess appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (DataAttributeMappingProcess) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DataAttributeMappingProcess, without replacing existing Atlan tags linked to the DataAttributeMappingProcess.
     * Note: this operation must make two API calls — one to retrieve the DataAttributeMappingProcess's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DataAttributeMappingProcess
     */
    public static DataAttributeMappingProcess appendAtlanTags(
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
     * Add Atlan tags to a DataAttributeMappingProcess, without replacing existing Atlan tags linked to the DataAttributeMappingProcess.
     * Note: this operation must make two API calls — one to retrieve the DataAttributeMappingProcess's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DataAttributeMappingProcess
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DataAttributeMappingProcess
     */
    public static DataAttributeMappingProcess appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DataAttributeMappingProcess) Asset.appendAtlanTags(
            client,
            TYPE_NAME,
            qualifiedName,
            atlanTagNames,
            propagate,
            removePropagationsOnDelete,
            restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a DataAttributeMappingProcess.
     *
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DataAttributeMappingProcess
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DataAttributeMappingProcess.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the DataAttributeMappingProcess
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DataAttributeMappingProcess
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DataAttributeMappingProcess.
     *
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DataAttributeMappingProcess
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
     * Add Atlan tags to a DataAttributeMappingProcess.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the DataAttributeMappingProcess
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DataAttributeMappingProcess
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
     * Remove an Atlan tag from a DataAttributeMappingProcess.
     *
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DataAttributeMappingProcess
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a DataAttributeMappingProcess.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DataAttributeMappingProcess
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DataAttributeMappingProcess
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
