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
import com.fasterxml.jackson.annotation.JsonProperty;
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
 * Instance of a Dremio Virtual Dataset (View) in Atlan. Represents SQL-defined views that transform and combine data from physical datasets or other virtual datasets.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class DremioVirtualDataset extends Asset
        implements IDremioVirtualDataset, IDremio, IView, ISQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DremioVirtualDataset";

    /** Fixed typeName for DremioVirtualDatasets. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Alias for this view. */
    @Attribute
    String alias;

    /** Simple name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewName;

    /** Unique name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewQualifiedName;

    /** Number of columns in this view. */
    @Attribute
    Long columnCount;

    /** Columns that exist within this view. */
    @Attribute
    @Singular
    SortedSet<IColumn> columns;

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

    /** SQL definition of this view. */
    @Attribute
    String definition;

    /** Dremio Folder that contains the virtual datasets (views). */
    @Attribute
    IDremioFolder dremioFolder;

    /** Ordered array of folder assets with qualified name and name representing the complete folder hierarchy path for this asset, from immediate parent to root folder. */
    @Attribute
    @Singular("addDremioFolderHierarchy")
    List<Map<String, String>> dremioFolderHierarchy;

    /** Source ID of this asset in Dremio. */
    @Attribute
    String dremioId;

    /** Dremio Labels associated with this asset. */
    @Attribute
    @Singular
    SortedSet<String> dremioLabels;

    /** Unique qualified name of the immediate parent folder containing this asset. */
    @Attribute
    String dremioParentFolderQualifiedName;

    /** Simple name of the Dremio Source containing this asset. */
    @Attribute
    String dremioSourceName;

    /** Unique qualified name of the Dremio Source containing this asset. */
    @Attribute
    String dremioSourceQualifiedName;

    /** Dremio Space that contains the virtual datasets (views). */
    @Attribute
    IDremioSpace dremioSpace;

    /** Simple name of the Dremio Space containing this asset. */
    @Attribute
    String dremioSpaceName;

    /** Unique qualified name of the Dremio Space containing this asset. */
    @Attribute
    String dremioSpaceQualifiedName;

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

    /** Whether preview queries are allowed on this view (true) or not (false). */
    @Attribute
    Boolean isQueryPreview;

    /** Whether this view is temporary (true) or not (false). */
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

    /** Queries that access this view. */
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

    /** Configuration for preview queries on this view. */
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

    /** Number of rows in this view. */
    @Attribute
    Long rowCount;

    /** Schema in which this view exists. */
    @Attribute
    @JsonProperty("atlanSchema")
    ISchema schema;

    /** Simple name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaName;

    /** Unique name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaQualifiedName;

    /** Size of this view, in bytes. */
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

    /** Whether this asset is secure (true) or not (false). */
    @Attribute
    Boolean sqlIsSecure;

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
     * Builds the minimal object necessary to create a relationship to a DremioVirtualDataset, from a potentially
     * more-complete DremioVirtualDataset object.
     *
     * @return the minimal object necessary to relate to the DremioVirtualDataset
     * @throws InvalidRequestException if any of the minimal set of required properties for a DremioVirtualDataset relationship are not found in the initial object
     */
    @Override
    public DremioVirtualDataset trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all DremioVirtualDataset assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DremioVirtualDataset assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all DremioVirtualDataset assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all DremioVirtualDataset assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DremioVirtualDatasets will be included
     * @return a fluent search that includes all DremioVirtualDataset assets
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
     * Reference to a DremioVirtualDataset by GUID. Use this to create a relationship to this DremioVirtualDataset,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the DremioVirtualDataset to reference
     * @return reference to a DremioVirtualDataset that can be used for defining a relationship to a DremioVirtualDataset
     */
    public static DremioVirtualDataset refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DremioVirtualDataset by GUID. Use this to create a relationship to this DremioVirtualDataset,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the DremioVirtualDataset to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DremioVirtualDataset that can be used for defining a relationship to a DremioVirtualDataset
     */
    public static DremioVirtualDataset refByGuid(String guid, Reference.SaveSemantic semantic) {
        return DremioVirtualDataset._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a DremioVirtualDataset by qualifiedName. Use this to create a relationship to this DremioVirtualDataset,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the DremioVirtualDataset to reference
     * @return reference to a DremioVirtualDataset that can be used for defining a relationship to a DremioVirtualDataset
     */
    public static DremioVirtualDataset refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DremioVirtualDataset by qualifiedName. Use this to create a relationship to this DremioVirtualDataset,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the DremioVirtualDataset to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DremioVirtualDataset that can be used for defining a relationship to a DremioVirtualDataset
     */
    public static DremioVirtualDataset refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return DremioVirtualDataset._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a DremioVirtualDataset by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DremioVirtualDataset to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DremioVirtualDataset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DremioVirtualDataset does not exist or the provided GUID is not a DremioVirtualDataset
     */
    @JsonIgnore
    public static DremioVirtualDataset get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a DremioVirtualDataset by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DremioVirtualDataset to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full DremioVirtualDataset, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DremioVirtualDataset does not exist or the provided GUID is not a DremioVirtualDataset
     */
    @JsonIgnore
    public static DremioVirtualDataset get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof DremioVirtualDataset) {
                return (DremioVirtualDataset) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof DremioVirtualDataset) {
                return (DremioVirtualDataset) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a DremioVirtualDataset by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DremioVirtualDataset to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DremioVirtualDataset, including any relationships
     * @return the requested DremioVirtualDataset, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DremioVirtualDataset does not exist or the provided GUID is not a DremioVirtualDataset
     */
    @JsonIgnore
    public static DremioVirtualDataset get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a DremioVirtualDataset by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DremioVirtualDataset to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DremioVirtualDataset, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the DremioVirtualDataset
     * @return the requested DremioVirtualDataset, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DremioVirtualDataset does not exist or the provided GUID is not a DremioVirtualDataset
     */
    @JsonIgnore
    public static DremioVirtualDataset get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = DremioVirtualDataset.select(client)
                    .where(DremioVirtualDataset.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof DremioVirtualDataset) {
                return (DremioVirtualDataset) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = DremioVirtualDataset.select(client)
                    .where(DremioVirtualDataset.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof DremioVirtualDataset) {
                return (DremioVirtualDataset) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) DremioVirtualDataset to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DremioVirtualDataset
     * @return true if the DremioVirtualDataset is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a DremioVirtualDataset.
     *
     * @param qualifiedName of the DremioVirtualDataset
     * @param name of the DremioVirtualDataset
     * @return the minimal request necessary to update the DremioVirtualDataset, as a builder
     */
    public static DremioVirtualDatasetBuilder<?, ?> updater(String qualifiedName, String name) {
        return DremioVirtualDataset._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DremioVirtualDataset, from a potentially
     * more-complete DremioVirtualDataset object.
     *
     * @return the minimal object necessary to update the DremioVirtualDataset, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DremioVirtualDataset are not found in the initial object
     */
    @Override
    public DremioVirtualDatasetBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class DremioVirtualDatasetBuilder<
                    C extends DremioVirtualDataset, B extends DremioVirtualDatasetBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a DremioVirtualDataset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DremioVirtualDataset
     * @param name of the DremioVirtualDataset
     * @return the updated DremioVirtualDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DremioVirtualDataset removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DremioVirtualDataset) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DremioVirtualDataset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DremioVirtualDataset
     * @param name of the DremioVirtualDataset
     * @return the updated DremioVirtualDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DremioVirtualDataset removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DremioVirtualDataset) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DremioVirtualDataset.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DremioVirtualDataset's owners
     * @param qualifiedName of the DremioVirtualDataset
     * @param name of the DremioVirtualDataset
     * @return the updated DremioVirtualDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DremioVirtualDataset removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DremioVirtualDataset) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DremioVirtualDataset.
     *
     * @param client connectivity to the Atlan tenant on which to update the DremioVirtualDataset's certificate
     * @param qualifiedName of the DremioVirtualDataset
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DremioVirtualDataset, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DremioVirtualDataset updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DremioVirtualDataset)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DremioVirtualDataset.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DremioVirtualDataset's certificate
     * @param qualifiedName of the DremioVirtualDataset
     * @param name of the DremioVirtualDataset
     * @return the updated DremioVirtualDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DremioVirtualDataset removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DremioVirtualDataset) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DremioVirtualDataset.
     *
     * @param client connectivity to the Atlan tenant on which to update the DremioVirtualDataset's announcement
     * @param qualifiedName of the DremioVirtualDataset
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DremioVirtualDataset updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (DremioVirtualDataset)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DremioVirtualDataset.
     *
     * @param client connectivity to the Atlan client from which to remove the DremioVirtualDataset's announcement
     * @param qualifiedName of the DremioVirtualDataset
     * @param name of the DremioVirtualDataset
     * @return the updated DremioVirtualDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DremioVirtualDataset removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DremioVirtualDataset) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DremioVirtualDataset.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DremioVirtualDataset's assigned terms
     * @param qualifiedName for the DremioVirtualDataset
     * @param name human-readable name of the DremioVirtualDataset
     * @param terms the list of terms to replace on the DremioVirtualDataset, or null to remove all terms from the DremioVirtualDataset
     * @return the DremioVirtualDataset that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DremioVirtualDataset replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (DremioVirtualDataset) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DremioVirtualDataset, without replacing existing terms linked to the DremioVirtualDataset.
     * Note: this operation must make two API calls — one to retrieve the DremioVirtualDataset's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DremioVirtualDataset
     * @param qualifiedName for the DremioVirtualDataset
     * @param terms the list of terms to append to the DremioVirtualDataset
     * @return the DremioVirtualDataset that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DremioVirtualDataset appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DremioVirtualDataset) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DremioVirtualDataset, without replacing all existing terms linked to the DremioVirtualDataset.
     * Note: this operation must make two API calls — one to retrieve the DremioVirtualDataset's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DremioVirtualDataset
     * @param qualifiedName for the DremioVirtualDataset
     * @param terms the list of terms to remove from the DremioVirtualDataset, which must be referenced by GUID
     * @return the DremioVirtualDataset that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DremioVirtualDataset removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DremioVirtualDataset) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DremioVirtualDataset, without replacing existing Atlan tags linked to the DremioVirtualDataset.
     * Note: this operation must make two API calls — one to retrieve the DremioVirtualDataset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DremioVirtualDataset
     * @param qualifiedName of the DremioVirtualDataset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DremioVirtualDataset
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static DremioVirtualDataset appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (DremioVirtualDataset) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DremioVirtualDataset, without replacing existing Atlan tags linked to the DremioVirtualDataset.
     * Note: this operation must make two API calls — one to retrieve the DremioVirtualDataset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DremioVirtualDataset
     * @param qualifiedName of the DremioVirtualDataset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DremioVirtualDataset
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static DremioVirtualDataset appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DremioVirtualDataset) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a DremioVirtualDataset.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DremioVirtualDataset
     * @param qualifiedName of the DremioVirtualDataset
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DremioVirtualDataset
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
