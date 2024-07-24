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
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.CompoundQuery;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.QueryFactory;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * TBC
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class DataEntityMappingProcess extends Asset
        implements IDataEntityMappingProcess, ILineageProcess, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DataEntityMappingProcess";

    /** Fixed typeName for DataEntityMappingProcesss. */
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
     * Builds the minimal object necessary to create a relationship to a DataEntityMappingProcess, from a potentially
     * more-complete DataEntityMappingProcess object.
     *
     * @return the minimal object necessary to relate to the DataEntityMappingProcess
     * @throws InvalidRequestException if any of the minimal set of required properties for a DataEntityMappingProcess relationship are not found in the initial object
     */
    @Override
    public DataEntityMappingProcess trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all DataEntityMappingProcess assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DataEntityMappingProcess assets will be included.
     *
     * @return a fluent search that includes all DataEntityMappingProcess assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all DataEntityMappingProcess assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DataEntityMappingProcess assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all DataEntityMappingProcess assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all DataEntityMappingProcess assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) DataEntityMappingProcesss will be included
     * @return a fluent search that includes all DataEntityMappingProcess assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all DataEntityMappingProcess assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DataEntityMappingProcesss will be included
     * @return a fluent search that includes all DataEntityMappingProcess assets
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
     * Start an asset filter that will return all DataEntityMappingProcess assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DataEntityMappingProcess assets will be included.
     *
     * @return an asset filter that includes all DataEntityMappingProcess assets
     * @deprecated replaced by {@link #select()}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all DataEntityMappingProcess assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DataEntityMappingProcess assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all DataEntityMappingProcess assets
     * @deprecated replaced by {@link #select(AtlanClient)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all DataEntityMappingProcess assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) DataEntityMappingProcesss will be included
     * @return an asset filter that includes all DataEntityMappingProcess assets
     * @deprecated replaced by {@link #select(boolean)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all DataEntityMappingProcess assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DataEntityMappingProcesss will be included
     * @return an asset filter that includes all DataEntityMappingProcess assets
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
     * Reference to a DataEntityMappingProcess by GUID. Use this to create a relationship to this DataEntityMappingProcess,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the DataEntityMappingProcess to reference
     * @return reference to a DataEntityMappingProcess that can be used for defining a relationship to a DataEntityMappingProcess
     */
    public static DataEntityMappingProcess refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DataEntityMappingProcess by GUID. Use this to create a relationship to this DataEntityMappingProcess,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the DataEntityMappingProcess to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DataEntityMappingProcess that can be used for defining a relationship to a DataEntityMappingProcess
     */
    public static DataEntityMappingProcess refByGuid(String guid, Reference.SaveSemantic semantic) {
        return DataEntityMappingProcess._internal()
                .guid(guid)
                .semantic(semantic)
                .build();
    }

    /**
     * Reference to a DataEntityMappingProcess by qualifiedName. Use this to create a relationship to this DataEntityMappingProcess,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the DataEntityMappingProcess to reference
     * @return reference to a DataEntityMappingProcess that can be used for defining a relationship to a DataEntityMappingProcess
     */
    public static DataEntityMappingProcess refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DataEntityMappingProcess by qualifiedName. Use this to create a relationship to this DataEntityMappingProcess,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the DataEntityMappingProcess to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DataEntityMappingProcess that can be used for defining a relationship to a DataEntityMappingProcess
     */
    public static DataEntityMappingProcess refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return DataEntityMappingProcess._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a DataEntityMappingProcess by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the DataEntityMappingProcess to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DataEntityMappingProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataEntityMappingProcess does not exist or the provided GUID is not a DataEntityMappingProcess
     */
    @JsonIgnore
    public static DataEntityMappingProcess get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a DataEntityMappingProcess by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DataEntityMappingProcess to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DataEntityMappingProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataEntityMappingProcess does not exist or the provided GUID is not a DataEntityMappingProcess
     */
    @JsonIgnore
    public static DataEntityMappingProcess get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a DataEntityMappingProcess by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DataEntityMappingProcess to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full DataEntityMappingProcess, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataEntityMappingProcess does not exist or the provided GUID is not a DataEntityMappingProcess
     */
    @JsonIgnore
    public static DataEntityMappingProcess get(AtlanClient client, String id, boolean includeRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof DataEntityMappingProcess) {
                return (DataEntityMappingProcess) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof DataEntityMappingProcess) {
                return (DataEntityMappingProcess) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a DataEntityMappingProcess by its GUID, complete with all of its relationships.
     *
     * @param guid of the DataEntityMappingProcess to retrieve
     * @return the requested full DataEntityMappingProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataEntityMappingProcess does not exist or the provided GUID is not a DataEntityMappingProcess
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static DataEntityMappingProcess retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a DataEntityMappingProcess by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the DataEntityMappingProcess to retrieve
     * @return the requested full DataEntityMappingProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataEntityMappingProcess does not exist or the provided GUID is not a DataEntityMappingProcess
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static DataEntityMappingProcess retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a DataEntityMappingProcess by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the DataEntityMappingProcess to retrieve
     * @return the requested full DataEntityMappingProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataEntityMappingProcess does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static DataEntityMappingProcess retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a DataEntityMappingProcess by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the DataEntityMappingProcess to retrieve
     * @return the requested full DataEntityMappingProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataEntityMappingProcess does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static DataEntityMappingProcess retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) DataEntityMappingProcess to active.
     *
     * @param qualifiedName for the DataEntityMappingProcess
     * @return true if the DataEntityMappingProcess is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) DataEntityMappingProcess to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DataEntityMappingProcess
     * @return true if the DataEntityMappingProcess is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a DataEntityMappingProcess
     *
     * @param sourceQualifiedName unique name of the source DataEntity
     * @param targetQualifiedName unique name of the target DataEntity
     * @param connectionQualifiedName unique name of the connection in which to create the DataEntityMappingProcess
     * @return the minimal object necessary to create the DataEntityMappingProcess, as a builder
     */
    public static DataEntityMappingProcessBuilder<?, ?> creator(
            String sourceQualifiedName, String targetQualifiedName, String connectionQualifiedName) {
        List<ICatalog> inputs = Collections.singletonList(DataEntity.refByQualifiedName(sourceQualifiedName));
        List<ICatalog> outputs = Collections.singletonList(DataEntity.refByQualifiedName(targetQualifiedName));
        return DataEntityMappingProcess._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(targetQualifiedName)
                .name(generateProcessName(sourceQualifiedName, targetQualifiedName))
                .connectorType(AtlanConnectorType.DATA_MODELING)
                .connectionQualifiedName(connectionQualifiedName)
                .inputs(inputs)
                .outputs(outputs);
    }

    /**
     * Builds the minimal string name for DataEntityMappingProcess name using the source and target QualifiedName.
     *
     * @param sourceQualifiedName
     * @param targetQualifiedName
     * @return the name for DataEntityMappingProcess
     */
    public static String generateProcessName(String sourceQualifiedName, String targetQualifiedName) {
        return sourceQualifiedName.substring(sourceQualifiedName.lastIndexOf(('/') + 1)) + ">"
                + targetQualifiedName.substring(targetQualifiedName.lastIndexOf(('/') + 1));
    }

    /**
     * Builds the minimal object necessary to update a DataEntityMappingProcess.
     *
     * @param qualifiedName of the DataEntityMappingProcess
     * @param name of the DataEntityMappingProcess
     * @return the minimal request necessary to update the DataEntityMappingProcess, as a builder
     */
    public static DataEntityMappingProcessBuilder<?, ?> updater(String qualifiedName, String name) {
        return DataEntityMappingProcess._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DataEntityMappingProcess, from a potentially
     * more-complete DataEntityMappingProcess object.
     *
     * @return the minimal object necessary to update the DataEntityMappingProcess, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DataEntityMappingProcess are not found in the initial object
     */
    @Override
    public DataEntityMappingProcessBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(
                TYPE_NAME,
                Map.of(
                        "qualifiedName", this.getQualifiedName(),
                        "name", this.getName()));
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a DataEntityMappingProcess.
     *
     * @param qualifiedName of the DataEntityMappingProcess
     * @param name of the DataEntityMappingProcess
     * @return the updated DataEntityMappingProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataEntityMappingProcess removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a DataEntityMappingProcess.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DataEntityMappingProcess
     * @param name of the DataEntityMappingProcess
     * @return the updated DataEntityMappingProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataEntityMappingProcess removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DataEntityMappingProcess) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DataEntityMappingProcess.
     *
     * @param qualifiedName of the DataEntityMappingProcess
     * @param name of the DataEntityMappingProcess
     * @return the updated DataEntityMappingProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataEntityMappingProcess removeUserDescription(String qualifiedName, String name)
            throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a DataEntityMappingProcess.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DataEntityMappingProcess
     * @param name of the DataEntityMappingProcess
     * @return the updated DataEntityMappingProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataEntityMappingProcess removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DataEntityMappingProcess) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DataEntityMappingProcess.
     *
     * @param qualifiedName of the DataEntityMappingProcess
     * @param name of the DataEntityMappingProcess
     * @return the updated DataEntityMappingProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataEntityMappingProcess removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a DataEntityMappingProcess.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DataEntityMappingProcess's owners
     * @param qualifiedName of the DataEntityMappingProcess
     * @param name of the DataEntityMappingProcess
     * @return the updated DataEntityMappingProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataEntityMappingProcess removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DataEntityMappingProcess) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DataEntityMappingProcess.
     *
     * @param qualifiedName of the DataEntityMappingProcess
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DataEntityMappingProcess, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataEntityMappingProcess updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a DataEntityMappingProcess.
     *
     * @param client connectivity to the Atlan tenant on which to update the DataEntityMappingProcess's certificate
     * @param qualifiedName of the DataEntityMappingProcess
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DataEntityMappingProcess, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataEntityMappingProcess updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DataEntityMappingProcess)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DataEntityMappingProcess.
     *
     * @param qualifiedName of the DataEntityMappingProcess
     * @param name of the DataEntityMappingProcess
     * @return the updated DataEntityMappingProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataEntityMappingProcess removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a DataEntityMappingProcess.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DataEntityMappingProcess's certificate
     * @param qualifiedName of the DataEntityMappingProcess
     * @param name of the DataEntityMappingProcess
     * @return the updated DataEntityMappingProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataEntityMappingProcess removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DataEntityMappingProcess) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DataEntityMappingProcess.
     *
     * @param qualifiedName of the DataEntityMappingProcess
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataEntityMappingProcess updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a DataEntityMappingProcess.
     *
     * @param client connectivity to the Atlan tenant on which to update the DataEntityMappingProcess's announcement
     * @param qualifiedName of the DataEntityMappingProcess
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataEntityMappingProcess updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (DataEntityMappingProcess)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DataEntityMappingProcess.
     *
     * @param qualifiedName of the DataEntityMappingProcess
     * @param name of the DataEntityMappingProcess
     * @return the updated DataEntityMappingProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataEntityMappingProcess removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a DataEntityMappingProcess.
     *
     * @param client connectivity to the Atlan client from which to remove the DataEntityMappingProcess's announcement
     * @param qualifiedName of the DataEntityMappingProcess
     * @param name of the DataEntityMappingProcess
     * @return the updated DataEntityMappingProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataEntityMappingProcess removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DataEntityMappingProcess) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DataEntityMappingProcess.
     *
     * @param qualifiedName for the DataEntityMappingProcess
     * @param name human-readable name of the DataEntityMappingProcess
     * @param terms the list of terms to replace on the DataEntityMappingProcess, or null to remove all terms from the DataEntityMappingProcess
     * @return the DataEntityMappingProcess that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DataEntityMappingProcess replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the DataEntityMappingProcess.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DataEntityMappingProcess's assigned terms
     * @param qualifiedName for the DataEntityMappingProcess
     * @param name human-readable name of the DataEntityMappingProcess
     * @param terms the list of terms to replace on the DataEntityMappingProcess, or null to remove all terms from the DataEntityMappingProcess
     * @return the DataEntityMappingProcess that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DataEntityMappingProcess replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (DataEntityMappingProcess) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DataEntityMappingProcess, without replacing existing terms linked to the DataEntityMappingProcess.
     * Note: this operation must make two API calls — one to retrieve the DataEntityMappingProcess's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the DataEntityMappingProcess
     * @param terms the list of terms to append to the DataEntityMappingProcess
     * @return the DataEntityMappingProcess that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DataEntityMappingProcess appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the DataEntityMappingProcess, without replacing existing terms linked to the DataEntityMappingProcess.
     * Note: this operation must make two API calls — one to retrieve the DataEntityMappingProcess's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DataEntityMappingProcess
     * @param qualifiedName for the DataEntityMappingProcess
     * @param terms the list of terms to append to the DataEntityMappingProcess
     * @return the DataEntityMappingProcess that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DataEntityMappingProcess appendTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (DataEntityMappingProcess) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DataEntityMappingProcess, without replacing all existing terms linked to the DataEntityMappingProcess.
     * Note: this operation must make two API calls — one to retrieve the DataEntityMappingProcess's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the DataEntityMappingProcess
     * @param terms the list of terms to remove from the DataEntityMappingProcess, which must be referenced by GUID
     * @return the DataEntityMappingProcess that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DataEntityMappingProcess removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a DataEntityMappingProcess, without replacing all existing terms linked to the DataEntityMappingProcess.
     * Note: this operation must make two API calls — one to retrieve the DataEntityMappingProcess's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DataEntityMappingProcess
     * @param qualifiedName for the DataEntityMappingProcess
     * @param terms the list of terms to remove from the DataEntityMappingProcess, which must be referenced by GUID
     * @return the DataEntityMappingProcess that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DataEntityMappingProcess removeTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (DataEntityMappingProcess) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DataEntityMappingProcess, without replacing existing Atlan tags linked to the DataEntityMappingProcess.
     * Note: this operation must make two API calls — one to retrieve the DataEntityMappingProcess's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DataEntityMappingProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DataEntityMappingProcess
     */
    public static DataEntityMappingProcess appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DataEntityMappingProcess, without replacing existing Atlan tags linked to the DataEntityMappingProcess.
     * Note: this operation must make two API calls — one to retrieve the DataEntityMappingProcess's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DataEntityMappingProcess
     * @param qualifiedName of the DataEntityMappingProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DataEntityMappingProcess
     */
    public static DataEntityMappingProcess appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (DataEntityMappingProcess) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DataEntityMappingProcess, without replacing existing Atlan tags linked to the DataEntityMappingProcess.
     * Note: this operation must make two API calls — one to retrieve the DataEntityMappingProcess's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DataEntityMappingProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DataEntityMappingProcess
     */
    public static DataEntityMappingProcess appendAtlanTags(
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
     * Add Atlan tags to a DataEntityMappingProcess, without replacing existing Atlan tags linked to the DataEntityMappingProcess.
     * Note: this operation must make two API calls — one to retrieve the DataEntityMappingProcess's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DataEntityMappingProcess
     * @param qualifiedName of the DataEntityMappingProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DataEntityMappingProcess
     */
    public static DataEntityMappingProcess appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DataEntityMappingProcess) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a DataEntityMappingProcess.
     *
     * @param qualifiedName of the DataEntityMappingProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DataEntityMappingProcess
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DataEntityMappingProcess.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the DataEntityMappingProcess
     * @param qualifiedName of the DataEntityMappingProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DataEntityMappingProcess
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DataEntityMappingProcess.
     *
     * @param qualifiedName of the DataEntityMappingProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DataEntityMappingProcess
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
     * Add Atlan tags to a DataEntityMappingProcess.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the DataEntityMappingProcess
     * @param qualifiedName of the DataEntityMappingProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DataEntityMappingProcess
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
     * Remove an Atlan tag from a DataEntityMappingProcess.
     *
     * @param qualifiedName of the DataEntityMappingProcess
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DataEntityMappingProcess
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a DataEntityMappingProcess.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DataEntityMappingProcess
     * @param qualifiedName of the DataEntityMappingProcess
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DataEntityMappingProcess
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
