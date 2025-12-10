/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.BigqueryRoutineType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.model.structs.SQLProcedureArgument;
import com.atlan.model.structs.SQLProcedureReturn;
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
 * Instance of a bigquery routine in atlan. Can be a stored procedure, udf, or tvf.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class BigqueryRoutine extends Asset
        implements IBigqueryRoutine, IProcedure, ISQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "BigqueryRoutine";

    /** Fixed typeName for BigqueryRoutines. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Schema in which this routine exists. */
    @Attribute
    ISchema bigqueryAtlanSchema;

    /** Processes that utilize this routine. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> bigqueryProcesses;

    /** Arguments that are passed in to the routine. */
    @Attribute
    @Singular
    SortedSet<String> bigqueryRoutineArguments;

    /** The ddl statement used to create the bigquery routine. */
    @Attribute
    String bigqueryRoutineDdl;

    /** Return data type of the bigquery routine (null for stored procedures). */
    @Attribute
    String bigqueryRoutineReturnType;

    /** Security type of the routine, always null. */
    @Attribute
    String bigqueryRoutineSecurityType;

    /** Type of bigquery routine (sp, udf, or tvf). */
    @Attribute
    BigqueryRoutineType bigqueryRoutineType;

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

    /** SQL definition of the procedure. */
    @Attribute
    String definition;

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

    /** Schema in which this stored procedure exists. */
    @Attribute
    @JsonProperty("atlanSchema")
    ISchema schema;

    /** Simple name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaName;

    /** Unique name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaQualifiedName;

    /** Unique name of the context in which the model versions exist, or empty if it does not exist within an AI model context. */
    @Attribute
    String sqlAIModelContextQualifiedName;

    /** List of procedure arguments with name and type information. */
    @Attribute
    @Singular
    List<SQLProcedureArgument> sqlArguments;

    /** Internal ID for the database containing the procedure. */
    @Attribute
    String sqlCatalogId;

    /** Sources related to this asset. */
    @Attribute
    @Singular
    SortedSet<IDbtSource> sqlDBTSources;

    /** Assets related to the model. */
    @Attribute
    @Singular
    SortedSet<IDbtModel> sqlDbtModels;

    /** Names of external access integrations used by the procedure. */
    @Attribute
    String sqlExternalAccessIntegrations;

    /** Packages actually installed for the procedure. */
    @Attribute
    String sqlInstalledPackages;

    /** Whether this asset is secure (true) or not (false). */
    @Attribute
    Boolean sqlIsSecure;

    /** Programming language used for the procedure (e.g., SQL, JavaScript, Python, Scala). */
    @Attribute
    String sqlLanguage;

    /** Type of role that owns the procedure. */
    @Attribute
    String sqlOwnerRoleType;

    /** Packages requested by the procedure. */
    @Attribute
    String sqlPackages;

    /** Detailed information about the procedure's return type. */
    @Attribute
    SQLProcedureReturn sqlProcedureReturn;

    /** Processes that utilize this procedure. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> sqlProcesses;

    /** Version of the language runtime used by the procedure. */
    @Attribute
    String sqlRuntimeVersion;

    /** Internal ID for the schema containing the procedure. */
    @Attribute
    String sqlSchemaId;

    /** Secret variables used by the procedure. */
    @Attribute
    String sqlSecrets;

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
     * Builds the minimal object necessary to create a relationship to a BigqueryRoutine, from a potentially
     * more-complete BigqueryRoutine object.
     *
     * @return the minimal object necessary to relate to the BigqueryRoutine
     * @throws InvalidRequestException if any of the minimal set of required properties for a BigqueryRoutine relationship are not found in the initial object
     */
    @Override
    public BigqueryRoutine trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all BigqueryRoutine assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) BigqueryRoutine assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all BigqueryRoutine assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all BigqueryRoutine assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) BigqueryRoutines will be included
     * @return a fluent search that includes all BigqueryRoutine assets
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
     * Reference to a BigqueryRoutine by GUID. Use this to create a relationship to this BigqueryRoutine,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the BigqueryRoutine to reference
     * @return reference to a BigqueryRoutine that can be used for defining a relationship to a BigqueryRoutine
     */
    public static BigqueryRoutine refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a BigqueryRoutine by GUID. Use this to create a relationship to this BigqueryRoutine,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the BigqueryRoutine to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a BigqueryRoutine that can be used for defining a relationship to a BigqueryRoutine
     */
    public static BigqueryRoutine refByGuid(String guid, Reference.SaveSemantic semantic) {
        return BigqueryRoutine._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a BigqueryRoutine by qualifiedName. Use this to create a relationship to this BigqueryRoutine,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the BigqueryRoutine to reference
     * @return reference to a BigqueryRoutine that can be used for defining a relationship to a BigqueryRoutine
     */
    public static BigqueryRoutine refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a BigqueryRoutine by qualifiedName. Use this to create a relationship to this BigqueryRoutine,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the BigqueryRoutine to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a BigqueryRoutine that can be used for defining a relationship to a BigqueryRoutine
     */
    public static BigqueryRoutine refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return BigqueryRoutine._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a BigqueryRoutine by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the BigqueryRoutine to retrieve, either its GUID or its full qualifiedName
     * @return the requested full BigqueryRoutine, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the BigqueryRoutine does not exist or the provided GUID is not a BigqueryRoutine
     */
    @JsonIgnore
    public static BigqueryRoutine get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a BigqueryRoutine by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the BigqueryRoutine to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full BigqueryRoutine, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the BigqueryRoutine does not exist or the provided GUID is not a BigqueryRoutine
     */
    @JsonIgnore
    public static BigqueryRoutine get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof BigqueryRoutine) {
                return (BigqueryRoutine) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof BigqueryRoutine) {
                return (BigqueryRoutine) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a BigqueryRoutine by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the BigqueryRoutine to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the BigqueryRoutine, including any relationships
     * @return the requested BigqueryRoutine, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the BigqueryRoutine does not exist or the provided GUID is not a BigqueryRoutine
     */
    @JsonIgnore
    public static BigqueryRoutine get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a BigqueryRoutine by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the BigqueryRoutine to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the BigqueryRoutine, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the BigqueryRoutine
     * @return the requested BigqueryRoutine, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the BigqueryRoutine does not exist or the provided GUID is not a BigqueryRoutine
     */
    @JsonIgnore
    public static BigqueryRoutine get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = BigqueryRoutine.select(client)
                    .where(BigqueryRoutine.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof BigqueryRoutine) {
                return (BigqueryRoutine) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = BigqueryRoutine.select(client)
                    .where(BigqueryRoutine.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof BigqueryRoutine) {
                return (BigqueryRoutine) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) BigqueryRoutine to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the BigqueryRoutine
     * @return true if the BigqueryRoutine is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a BigqueryRoutine.
     *
     * @param qualifiedName of the BigqueryRoutine
     * @param name of the BigqueryRoutine
     * @return the minimal request necessary to update the BigqueryRoutine, as a builder
     */
    public static BigqueryRoutineBuilder<?, ?> updater(String qualifiedName, String name) {
        return BigqueryRoutine._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a BigqueryRoutine, from a potentially
     * more-complete BigqueryRoutine object.
     *
     * @return the minimal object necessary to update the BigqueryRoutine, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for BigqueryRoutine are not found in the initial object
     */
    @Override
    public BigqueryRoutineBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class BigqueryRoutineBuilder<
                    C extends BigqueryRoutine, B extends BigqueryRoutineBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a BigqueryRoutine.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the BigqueryRoutine
     * @param name of the BigqueryRoutine
     * @return the updated BigqueryRoutine, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BigqueryRoutine removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (BigqueryRoutine) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a BigqueryRoutine.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the BigqueryRoutine
     * @param name of the BigqueryRoutine
     * @return the updated BigqueryRoutine, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BigqueryRoutine removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (BigqueryRoutine) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a BigqueryRoutine.
     *
     * @param client connectivity to the Atlan tenant from which to remove the BigqueryRoutine's owners
     * @param qualifiedName of the BigqueryRoutine
     * @param name of the BigqueryRoutine
     * @return the updated BigqueryRoutine, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BigqueryRoutine removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (BigqueryRoutine) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a BigqueryRoutine.
     *
     * @param client connectivity to the Atlan tenant on which to update the BigqueryRoutine's certificate
     * @param qualifiedName of the BigqueryRoutine
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated BigqueryRoutine, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static BigqueryRoutine updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (BigqueryRoutine)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a BigqueryRoutine.
     *
     * @param client connectivity to the Atlan tenant from which to remove the BigqueryRoutine's certificate
     * @param qualifiedName of the BigqueryRoutine
     * @param name of the BigqueryRoutine
     * @return the updated BigqueryRoutine, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BigqueryRoutine removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (BigqueryRoutine) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a BigqueryRoutine.
     *
     * @param client connectivity to the Atlan tenant on which to update the BigqueryRoutine's announcement
     * @param qualifiedName of the BigqueryRoutine
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static BigqueryRoutine updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (BigqueryRoutine)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a BigqueryRoutine.
     *
     * @param client connectivity to the Atlan client from which to remove the BigqueryRoutine's announcement
     * @param qualifiedName of the BigqueryRoutine
     * @param name of the BigqueryRoutine
     * @return the updated BigqueryRoutine, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BigqueryRoutine removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (BigqueryRoutine) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the BigqueryRoutine.
     *
     * @param client connectivity to the Atlan tenant on which to replace the BigqueryRoutine's assigned terms
     * @param qualifiedName for the BigqueryRoutine
     * @param name human-readable name of the BigqueryRoutine
     * @param terms the list of terms to replace on the BigqueryRoutine, or null to remove all terms from the BigqueryRoutine
     * @return the BigqueryRoutine that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static BigqueryRoutine replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (BigqueryRoutine) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the BigqueryRoutine, without replacing existing terms linked to the BigqueryRoutine.
     * Note: this operation must make two API calls — one to retrieve the BigqueryRoutine's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the BigqueryRoutine
     * @param qualifiedName for the BigqueryRoutine
     * @param terms the list of terms to append to the BigqueryRoutine
     * @return the BigqueryRoutine that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static BigqueryRoutine appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (BigqueryRoutine) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a BigqueryRoutine, without replacing all existing terms linked to the BigqueryRoutine.
     * Note: this operation must make two API calls — one to retrieve the BigqueryRoutine's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the BigqueryRoutine
     * @param qualifiedName for the BigqueryRoutine
     * @param terms the list of terms to remove from the BigqueryRoutine, which must be referenced by GUID
     * @return the BigqueryRoutine that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static BigqueryRoutine removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (BigqueryRoutine) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a BigqueryRoutine, without replacing existing Atlan tags linked to the BigqueryRoutine.
     * Note: this operation must make two API calls — one to retrieve the BigqueryRoutine's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the BigqueryRoutine
     * @param qualifiedName of the BigqueryRoutine
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated BigqueryRoutine
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static BigqueryRoutine appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (BigqueryRoutine) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a BigqueryRoutine, without replacing existing Atlan tags linked to the BigqueryRoutine.
     * Note: this operation must make two API calls — one to retrieve the BigqueryRoutine's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the BigqueryRoutine
     * @param qualifiedName of the BigqueryRoutine
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated BigqueryRoutine
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static BigqueryRoutine appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (BigqueryRoutine) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a BigqueryRoutine.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a BigqueryRoutine
     * @param qualifiedName of the BigqueryRoutine
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the BigqueryRoutine
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
