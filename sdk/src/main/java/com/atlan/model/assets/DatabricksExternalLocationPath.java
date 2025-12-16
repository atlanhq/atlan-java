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
 * Represents a path within a Databricks External Location, providing access to specific data files or directories in external storage.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class DatabricksExternalLocationPath extends Asset
        implements IDatabricksExternalLocationPath, IDatabricks, ISQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DatabricksExternalLocationPath";

    /** Fixed typeName for DatabricksExternalLocationPaths. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Simple name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewName;

    /** Unique name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewQualifiedName;

    /** Simple name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    @Attribute
    String databaseName;

    /** Unique name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    @Attribute
    String databaseQualifiedName;

    /** External location that contains the paths. */
    @Attribute
    IDatabricksExternalLocation databricksExternalLocation;

    /** Name of the parent external location. */
    @Attribute
    String databricksParentName;

    /** Qualified name of the parent external location. */
    @Attribute
    String databricksParentQualifiedName;

    /** Path of data at the external location. */
    @Attribute
    String databricksPath;

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

    /** Partial fields contained in the asset. */
    @Attribute
    @Singular
    SortedSet<IPartialField> partialChildFields;

    /** Partial objects contained in the asset. */
    @Attribute
    @Singular
    SortedSet<IPartialObject> partialChildObjects;

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

    /** Simple name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaName;

    /** Unique name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaQualifiedName;

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
     * Builds the minimal object necessary to create a relationship to a DatabricksExternalLocationPath, from a potentially
     * more-complete DatabricksExternalLocationPath object.
     *
     * @return the minimal object necessary to relate to the DatabricksExternalLocationPath
     * @throws InvalidRequestException if any of the minimal set of required properties for a DatabricksExternalLocationPath relationship are not found in the initial object
     */
    @Override
    public DatabricksExternalLocationPath trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all DatabricksExternalLocationPath assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DatabricksExternalLocationPath assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all DatabricksExternalLocationPath assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all DatabricksExternalLocationPath assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DatabricksExternalLocationPaths will be included
     * @return a fluent search that includes all DatabricksExternalLocationPath assets
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
     * Reference to a DatabricksExternalLocationPath by GUID. Use this to create a relationship to this DatabricksExternalLocationPath,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the DatabricksExternalLocationPath to reference
     * @return reference to a DatabricksExternalLocationPath that can be used for defining a relationship to a DatabricksExternalLocationPath
     */
    public static DatabricksExternalLocationPath refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DatabricksExternalLocationPath by GUID. Use this to create a relationship to this DatabricksExternalLocationPath,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the DatabricksExternalLocationPath to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DatabricksExternalLocationPath that can be used for defining a relationship to a DatabricksExternalLocationPath
     */
    public static DatabricksExternalLocationPath refByGuid(String guid, Reference.SaveSemantic semantic) {
        return DatabricksExternalLocationPath._internal()
                .guid(guid)
                .semantic(semantic)
                .build();
    }

    /**
     * Reference to a DatabricksExternalLocationPath by qualifiedName. Use this to create a relationship to this DatabricksExternalLocationPath,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the DatabricksExternalLocationPath to reference
     * @return reference to a DatabricksExternalLocationPath that can be used for defining a relationship to a DatabricksExternalLocationPath
     */
    public static DatabricksExternalLocationPath refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DatabricksExternalLocationPath by qualifiedName. Use this to create a relationship to this DatabricksExternalLocationPath,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the DatabricksExternalLocationPath to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DatabricksExternalLocationPath that can be used for defining a relationship to a DatabricksExternalLocationPath
     */
    public static DatabricksExternalLocationPath refByQualifiedName(
            String qualifiedName, Reference.SaveSemantic semantic) {
        return DatabricksExternalLocationPath._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a DatabricksExternalLocationPath by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DatabricksExternalLocationPath to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DatabricksExternalLocationPath, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DatabricksExternalLocationPath does not exist or the provided GUID is not a DatabricksExternalLocationPath
     */
    @JsonIgnore
    public static DatabricksExternalLocationPath get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a DatabricksExternalLocationPath by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DatabricksExternalLocationPath to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full DatabricksExternalLocationPath, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DatabricksExternalLocationPath does not exist or the provided GUID is not a DatabricksExternalLocationPath
     */
    @JsonIgnore
    public static DatabricksExternalLocationPath get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof DatabricksExternalLocationPath) {
                return (DatabricksExternalLocationPath) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof DatabricksExternalLocationPath) {
                return (DatabricksExternalLocationPath) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a DatabricksExternalLocationPath by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DatabricksExternalLocationPath to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DatabricksExternalLocationPath, including any relationships
     * @return the requested DatabricksExternalLocationPath, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DatabricksExternalLocationPath does not exist or the provided GUID is not a DatabricksExternalLocationPath
     */
    @JsonIgnore
    public static DatabricksExternalLocationPath get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a DatabricksExternalLocationPath by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DatabricksExternalLocationPath to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DatabricksExternalLocationPath, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the DatabricksExternalLocationPath
     * @return the requested DatabricksExternalLocationPath, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DatabricksExternalLocationPath does not exist or the provided GUID is not a DatabricksExternalLocationPath
     */
    @JsonIgnore
    public static DatabricksExternalLocationPath get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = DatabricksExternalLocationPath.select(client)
                    .where(DatabricksExternalLocationPath.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof DatabricksExternalLocationPath) {
                return (DatabricksExternalLocationPath) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = DatabricksExternalLocationPath.select(client)
                    .where(DatabricksExternalLocationPath.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof DatabricksExternalLocationPath) {
                return (DatabricksExternalLocationPath) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) DatabricksExternalLocationPath to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DatabricksExternalLocationPath
     * @return true if the DatabricksExternalLocationPath is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a DatabricksExternalLocationPath.
     *
     * @param qualifiedName of the DatabricksExternalLocationPath
     * @param name of the DatabricksExternalLocationPath
     * @return the minimal request necessary to update the DatabricksExternalLocationPath, as a builder
     */
    public static DatabricksExternalLocationPathBuilder<?, ?> updater(String qualifiedName, String name) {
        return DatabricksExternalLocationPath._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DatabricksExternalLocationPath, from a potentially
     * more-complete DatabricksExternalLocationPath object.
     *
     * @return the minimal object necessary to update the DatabricksExternalLocationPath, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DatabricksExternalLocationPath are not found in the initial object
     */
    @Override
    public DatabricksExternalLocationPathBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class DatabricksExternalLocationPathBuilder<
                    C extends DatabricksExternalLocationPath, B extends DatabricksExternalLocationPathBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a DatabricksExternalLocationPath.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DatabricksExternalLocationPath
     * @param name of the DatabricksExternalLocationPath
     * @return the updated DatabricksExternalLocationPath, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksExternalLocationPath removeDescription(
            AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (DatabricksExternalLocationPath) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DatabricksExternalLocationPath.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DatabricksExternalLocationPath
     * @param name of the DatabricksExternalLocationPath
     * @return the updated DatabricksExternalLocationPath, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksExternalLocationPath removeUserDescription(
            AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (DatabricksExternalLocationPath) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DatabricksExternalLocationPath.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DatabricksExternalLocationPath's owners
     * @param qualifiedName of the DatabricksExternalLocationPath
     * @param name of the DatabricksExternalLocationPath
     * @return the updated DatabricksExternalLocationPath, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksExternalLocationPath removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DatabricksExternalLocationPath) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DatabricksExternalLocationPath.
     *
     * @param client connectivity to the Atlan tenant on which to update the DatabricksExternalLocationPath's certificate
     * @param qualifiedName of the DatabricksExternalLocationPath
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DatabricksExternalLocationPath, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksExternalLocationPath updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DatabricksExternalLocationPath)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DatabricksExternalLocationPath.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DatabricksExternalLocationPath's certificate
     * @param qualifiedName of the DatabricksExternalLocationPath
     * @param name of the DatabricksExternalLocationPath
     * @return the updated DatabricksExternalLocationPath, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksExternalLocationPath removeCertificate(
            AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (DatabricksExternalLocationPath) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DatabricksExternalLocationPath.
     *
     * @param client connectivity to the Atlan tenant on which to update the DatabricksExternalLocationPath's announcement
     * @param qualifiedName of the DatabricksExternalLocationPath
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksExternalLocationPath updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (DatabricksExternalLocationPath)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DatabricksExternalLocationPath.
     *
     * @param client connectivity to the Atlan client from which to remove the DatabricksExternalLocationPath's announcement
     * @param qualifiedName of the DatabricksExternalLocationPath
     * @param name of the DatabricksExternalLocationPath
     * @return the updated DatabricksExternalLocationPath, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksExternalLocationPath removeAnnouncement(
            AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (DatabricksExternalLocationPath) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DatabricksExternalLocationPath.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DatabricksExternalLocationPath's assigned terms
     * @param qualifiedName for the DatabricksExternalLocationPath
     * @param name human-readable name of the DatabricksExternalLocationPath
     * @param terms the list of terms to replace on the DatabricksExternalLocationPath, or null to remove all terms from the DatabricksExternalLocationPath
     * @return the DatabricksExternalLocationPath that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DatabricksExternalLocationPath replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (DatabricksExternalLocationPath) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DatabricksExternalLocationPath, without replacing existing terms linked to the DatabricksExternalLocationPath.
     * Note: this operation must make two API calls — one to retrieve the DatabricksExternalLocationPath's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DatabricksExternalLocationPath
     * @param qualifiedName for the DatabricksExternalLocationPath
     * @param terms the list of terms to append to the DatabricksExternalLocationPath
     * @return the DatabricksExternalLocationPath that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DatabricksExternalLocationPath appendTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (DatabricksExternalLocationPath) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DatabricksExternalLocationPath, without replacing all existing terms linked to the DatabricksExternalLocationPath.
     * Note: this operation must make two API calls — one to retrieve the DatabricksExternalLocationPath's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DatabricksExternalLocationPath
     * @param qualifiedName for the DatabricksExternalLocationPath
     * @param terms the list of terms to remove from the DatabricksExternalLocationPath, which must be referenced by GUID
     * @return the DatabricksExternalLocationPath that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DatabricksExternalLocationPath removeTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (DatabricksExternalLocationPath) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DatabricksExternalLocationPath, without replacing existing Atlan tags linked to the DatabricksExternalLocationPath.
     * Note: this operation must make two API calls — one to retrieve the DatabricksExternalLocationPath's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DatabricksExternalLocationPath
     * @param qualifiedName of the DatabricksExternalLocationPath
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DatabricksExternalLocationPath
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static DatabricksExternalLocationPath appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (DatabricksExternalLocationPath) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DatabricksExternalLocationPath, without replacing existing Atlan tags linked to the DatabricksExternalLocationPath.
     * Note: this operation must make two API calls — one to retrieve the DatabricksExternalLocationPath's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DatabricksExternalLocationPath
     * @param qualifiedName of the DatabricksExternalLocationPath
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DatabricksExternalLocationPath
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static DatabricksExternalLocationPath appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DatabricksExternalLocationPath) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a DatabricksExternalLocationPath.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DatabricksExternalLocationPath
     * @param qualifiedName of the DatabricksExternalLocationPath
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DatabricksExternalLocationPath
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
