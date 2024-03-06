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
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Sisense datamodel in Atlan. These group tables together that you can use to build dashboards.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class SisenseDatamodel extends Asset
        implements ISisenseDatamodel, ISisense, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SisenseDatamodel";

    /** Fixed typeName for SisenseDatamodels. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

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
    SortedSet<ISisenseDashboard> sisenseDashboards;

    /** Time (epoch) when this datamodel was last built, in milliseconds. */
    @Attribute
    @Date
    Long sisenseDatamodelLastBuildTime;

    /** Time (epoch) when this datamodel was last published, in milliseconds. */
    @Attribute
    @Date
    Long sisenseDatamodelLastPublishTime;

    /** Time (epoch) when this datamodel was last built successfully, in milliseconds. */
    @Attribute
    @Date
    Long sisenseDatamodelLastSuccessfulBuildTime;

    /** Default relation type for this datamodel. 'extract' type Datamodels have regular relations by default. 'live' type Datamodels have direct relations by default. */
    @Attribute
    String sisenseDatamodelRelationType;

    /** Revision of this datamodel. */
    @Attribute
    String sisenseDatamodelRevision;

    /** Hostname of the server on which this datamodel was created. */
    @Attribute
    String sisenseDatamodelServer;

    /** Number of tables in this datamodel. */
    @Attribute
    Long sisenseDatamodelTableCount;

    /** Datamodel tables that exist within this datamodel. */
    @Attribute
    @Singular
    SortedSet<ISisenseDatamodelTable> sisenseDatamodelTables;

    /** Type of this datamodel, for example: 'extract' or 'custom'. */
    @Attribute
    String sisenseDatamodelType;

    /**
     * Builds the minimal object necessary to create a relationship to a SisenseDatamodel, from a potentially
     * more-complete SisenseDatamodel object.
     *
     * @return the minimal object necessary to relate to the SisenseDatamodel
     * @throws InvalidRequestException if any of the minimal set of required properties for a SisenseDatamodel relationship are not found in the initial object
     */
    @Override
    public SisenseDatamodel trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SisenseDatamodel assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SisenseDatamodel assets will be included.
     *
     * @return a fluent search that includes all SisenseDatamodel assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all SisenseDatamodel assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SisenseDatamodel assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SisenseDatamodel assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SisenseDatamodel assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) SisenseDatamodels will be included
     * @return a fluent search that includes all SisenseDatamodel assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all SisenseDatamodel assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SisenseDatamodels will be included
     * @return a fluent search that includes all SisenseDatamodel assets
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
     * Start an asset filter that will return all SisenseDatamodel assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SisenseDatamodel assets will be included.
     *
     * @return an asset filter that includes all SisenseDatamodel assets
     * @deprecated replaced by {@link #select()}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all SisenseDatamodel assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SisenseDatamodel assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all SisenseDatamodel assets
     * @deprecated replaced by {@link #select(AtlanClient)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all SisenseDatamodel assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) SisenseDatamodels will be included
     * @return an asset filter that includes all SisenseDatamodel assets
     * @deprecated replaced by {@link #select(boolean)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all SisenseDatamodel assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SisenseDatamodels will be included
     * @return an asset filter that includes all SisenseDatamodel assets
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
     * Reference to a SisenseDatamodel by GUID. Use this to create a relationship to this SisenseDatamodel,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SisenseDatamodel to reference
     * @return reference to a SisenseDatamodel that can be used for defining a relationship to a SisenseDatamodel
     */
    public static SisenseDatamodel refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SisenseDatamodel by GUID. Use this to create a relationship to this SisenseDatamodel,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SisenseDatamodel to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SisenseDatamodel that can be used for defining a relationship to a SisenseDatamodel
     */
    public static SisenseDatamodel refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SisenseDatamodel._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SisenseDatamodel by qualifiedName. Use this to create a relationship to this SisenseDatamodel,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SisenseDatamodel to reference
     * @return reference to a SisenseDatamodel that can be used for defining a relationship to a SisenseDatamodel
     */
    public static SisenseDatamodel refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SisenseDatamodel by qualifiedName. Use this to create a relationship to this SisenseDatamodel,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SisenseDatamodel to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SisenseDatamodel that can be used for defining a relationship to a SisenseDatamodel
     */
    public static SisenseDatamodel refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SisenseDatamodel._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SisenseDatamodel by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the SisenseDatamodel to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SisenseDatamodel, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SisenseDatamodel does not exist or the provided GUID is not a SisenseDatamodel
     */
    @JsonIgnore
    public static SisenseDatamodel get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a SisenseDatamodel by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SisenseDatamodel to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SisenseDatamodel, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SisenseDatamodel does not exist or the provided GUID is not a SisenseDatamodel
     */
    @JsonIgnore
    public static SisenseDatamodel get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a SisenseDatamodel by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SisenseDatamodel to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SisenseDatamodel, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SisenseDatamodel does not exist or the provided GUID is not a SisenseDatamodel
     */
    @JsonIgnore
    public static SisenseDatamodel get(AtlanClient client, String id, boolean includeRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SisenseDatamodel) {
                return (SisenseDatamodel) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof SisenseDatamodel) {
                return (SisenseDatamodel) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SisenseDatamodel by its GUID, complete with all of its relationships.
     *
     * @param guid of the SisenseDatamodel to retrieve
     * @return the requested full SisenseDatamodel, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SisenseDatamodel does not exist or the provided GUID is not a SisenseDatamodel
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static SisenseDatamodel retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a SisenseDatamodel by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the SisenseDatamodel to retrieve
     * @return the requested full SisenseDatamodel, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SisenseDatamodel does not exist or the provided GUID is not a SisenseDatamodel
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static SisenseDatamodel retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a SisenseDatamodel by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the SisenseDatamodel to retrieve
     * @return the requested full SisenseDatamodel, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SisenseDatamodel does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static SisenseDatamodel retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a SisenseDatamodel by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the SisenseDatamodel to retrieve
     * @return the requested full SisenseDatamodel, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SisenseDatamodel does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static SisenseDatamodel retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) SisenseDatamodel to active.
     *
     * @param qualifiedName for the SisenseDatamodel
     * @return true if the SisenseDatamodel is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) SisenseDatamodel to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SisenseDatamodel
     * @return true if the SisenseDatamodel is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SisenseDatamodel.
     *
     * @param qualifiedName of the SisenseDatamodel
     * @param name of the SisenseDatamodel
     * @return the minimal request necessary to update the SisenseDatamodel, as a builder
     */
    public static SisenseDatamodelBuilder<?, ?> updater(String qualifiedName, String name) {
        return SisenseDatamodel._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SisenseDatamodel, from a potentially
     * more-complete SisenseDatamodel object.
     *
     * @return the minimal object necessary to update the SisenseDatamodel, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SisenseDatamodel are not found in the initial object
     */
    @Override
    public SisenseDatamodelBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "SisenseDatamodel", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a SisenseDatamodel.
     *
     * @param qualifiedName of the SisenseDatamodel
     * @param name of the SisenseDatamodel
     * @return the updated SisenseDatamodel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SisenseDatamodel removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a SisenseDatamodel.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SisenseDatamodel
     * @param name of the SisenseDatamodel
     * @return the updated SisenseDatamodel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SisenseDatamodel removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SisenseDatamodel) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SisenseDatamodel.
     *
     * @param qualifiedName of the SisenseDatamodel
     * @param name of the SisenseDatamodel
     * @return the updated SisenseDatamodel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SisenseDatamodel removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a SisenseDatamodel.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SisenseDatamodel
     * @param name of the SisenseDatamodel
     * @return the updated SisenseDatamodel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SisenseDatamodel removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SisenseDatamodel) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SisenseDatamodel.
     *
     * @param qualifiedName of the SisenseDatamodel
     * @param name of the SisenseDatamodel
     * @return the updated SisenseDatamodel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SisenseDatamodel removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a SisenseDatamodel.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SisenseDatamodel's owners
     * @param qualifiedName of the SisenseDatamodel
     * @param name of the SisenseDatamodel
     * @return the updated SisenseDatamodel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SisenseDatamodel removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SisenseDatamodel) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SisenseDatamodel.
     *
     * @param qualifiedName of the SisenseDatamodel
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SisenseDatamodel, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SisenseDatamodel updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a SisenseDatamodel.
     *
     * @param client connectivity to the Atlan tenant on which to update the SisenseDatamodel's certificate
     * @param qualifiedName of the SisenseDatamodel
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SisenseDatamodel, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SisenseDatamodel updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SisenseDatamodel)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SisenseDatamodel.
     *
     * @param qualifiedName of the SisenseDatamodel
     * @param name of the SisenseDatamodel
     * @return the updated SisenseDatamodel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SisenseDatamodel removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a SisenseDatamodel.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SisenseDatamodel's certificate
     * @param qualifiedName of the SisenseDatamodel
     * @param name of the SisenseDatamodel
     * @return the updated SisenseDatamodel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SisenseDatamodel removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SisenseDatamodel) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SisenseDatamodel.
     *
     * @param qualifiedName of the SisenseDatamodel
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SisenseDatamodel updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a SisenseDatamodel.
     *
     * @param client connectivity to the Atlan tenant on which to update the SisenseDatamodel's announcement
     * @param qualifiedName of the SisenseDatamodel
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SisenseDatamodel updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SisenseDatamodel)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SisenseDatamodel.
     *
     * @param qualifiedName of the SisenseDatamodel
     * @param name of the SisenseDatamodel
     * @return the updated SisenseDatamodel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SisenseDatamodel removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a SisenseDatamodel.
     *
     * @param client connectivity to the Atlan client from which to remove the SisenseDatamodel's announcement
     * @param qualifiedName of the SisenseDatamodel
     * @param name of the SisenseDatamodel
     * @return the updated SisenseDatamodel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SisenseDatamodel removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SisenseDatamodel) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SisenseDatamodel.
     *
     * @param qualifiedName for the SisenseDatamodel
     * @param name human-readable name of the SisenseDatamodel
     * @param terms the list of terms to replace on the SisenseDatamodel, or null to remove all terms from the SisenseDatamodel
     * @return the SisenseDatamodel that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SisenseDatamodel replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the SisenseDatamodel.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SisenseDatamodel's assigned terms
     * @param qualifiedName for the SisenseDatamodel
     * @param name human-readable name of the SisenseDatamodel
     * @param terms the list of terms to replace on the SisenseDatamodel, or null to remove all terms from the SisenseDatamodel
     * @return the SisenseDatamodel that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SisenseDatamodel replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SisenseDatamodel) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SisenseDatamodel, without replacing existing terms linked to the SisenseDatamodel.
     * Note: this operation must make two API calls — one to retrieve the SisenseDatamodel's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the SisenseDatamodel
     * @param terms the list of terms to append to the SisenseDatamodel
     * @return the SisenseDatamodel that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SisenseDatamodel appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the SisenseDatamodel, without replacing existing terms linked to the SisenseDatamodel.
     * Note: this operation must make two API calls — one to retrieve the SisenseDatamodel's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SisenseDatamodel
     * @param qualifiedName for the SisenseDatamodel
     * @param terms the list of terms to append to the SisenseDatamodel
     * @return the SisenseDatamodel that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SisenseDatamodel appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SisenseDatamodel) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SisenseDatamodel, without replacing all existing terms linked to the SisenseDatamodel.
     * Note: this operation must make two API calls — one to retrieve the SisenseDatamodel's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the SisenseDatamodel
     * @param terms the list of terms to remove from the SisenseDatamodel, which must be referenced by GUID
     * @return the SisenseDatamodel that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SisenseDatamodel removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a SisenseDatamodel, without replacing all existing terms linked to the SisenseDatamodel.
     * Note: this operation must make two API calls — one to retrieve the SisenseDatamodel's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SisenseDatamodel
     * @param qualifiedName for the SisenseDatamodel
     * @param terms the list of terms to remove from the SisenseDatamodel, which must be referenced by GUID
     * @return the SisenseDatamodel that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SisenseDatamodel removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SisenseDatamodel) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SisenseDatamodel, without replacing existing Atlan tags linked to the SisenseDatamodel.
     * Note: this operation must make two API calls — one to retrieve the SisenseDatamodel's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SisenseDatamodel
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SisenseDatamodel
     */
    public static SisenseDatamodel appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SisenseDatamodel, without replacing existing Atlan tags linked to the SisenseDatamodel.
     * Note: this operation must make two API calls — one to retrieve the SisenseDatamodel's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SisenseDatamodel
     * @param qualifiedName of the SisenseDatamodel
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SisenseDatamodel
     */
    public static SisenseDatamodel appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SisenseDatamodel) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SisenseDatamodel, without replacing existing Atlan tags linked to the SisenseDatamodel.
     * Note: this operation must make two API calls — one to retrieve the SisenseDatamodel's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SisenseDatamodel
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SisenseDatamodel
     */
    public static SisenseDatamodel appendAtlanTags(
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
     * Add Atlan tags to a SisenseDatamodel, without replacing existing Atlan tags linked to the SisenseDatamodel.
     * Note: this operation must make two API calls — one to retrieve the SisenseDatamodel's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SisenseDatamodel
     * @param qualifiedName of the SisenseDatamodel
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SisenseDatamodel
     */
    public static SisenseDatamodel appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SisenseDatamodel) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a SisenseDatamodel.
     *
     * @param qualifiedName of the SisenseDatamodel
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SisenseDatamodel
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SisenseDatamodel.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the SisenseDatamodel
     * @param qualifiedName of the SisenseDatamodel
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SisenseDatamodel
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SisenseDatamodel.
     *
     * @param qualifiedName of the SisenseDatamodel
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SisenseDatamodel
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
     * Add Atlan tags to a SisenseDatamodel.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the SisenseDatamodel
     * @param qualifiedName of the SisenseDatamodel
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SisenseDatamodel
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
     * Remove an Atlan tag from a SisenseDatamodel.
     *
     * @param qualifiedName of the SisenseDatamodel
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SisenseDatamodel
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a SisenseDatamodel.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SisenseDatamodel
     * @param qualifiedName of the SisenseDatamodel
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SisenseDatamodel
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
