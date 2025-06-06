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
 * Instance of a stored procedure (routine) in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class Procedure extends Asset implements IProcedure, ISQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Procedure";

    /** Fixed typeName for Procedures. */
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

    /** (Deprecated) Model containing the assets. */
    @Attribute
    @Singular
    SortedSet<IDbtModel> dbtModels;

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
     * Builds the minimal object necessary to create a relationship to a Procedure, from a potentially
     * more-complete Procedure object.
     *
     * @return the minimal object necessary to relate to the Procedure
     * @throws InvalidRequestException if any of the minimal set of required properties for a Procedure relationship are not found in the initial object
     */
    @Override
    public Procedure trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all Procedure assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) Procedure assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all Procedure assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all Procedure assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) Procedures will be included
     * @return a fluent search that includes all Procedure assets
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
     * Reference to a Procedure by GUID. Use this to create a relationship to this Procedure,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the Procedure to reference
     * @return reference to a Procedure that can be used for defining a relationship to a Procedure
     */
    public static Procedure refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a Procedure by GUID. Use this to create a relationship to this Procedure,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the Procedure to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a Procedure that can be used for defining a relationship to a Procedure
     */
    public static Procedure refByGuid(String guid, Reference.SaveSemantic semantic) {
        return Procedure._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a Procedure by qualifiedName. Use this to create a relationship to this Procedure,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the Procedure to reference
     * @return reference to a Procedure that can be used for defining a relationship to a Procedure
     */
    public static Procedure refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a Procedure by qualifiedName. Use this to create a relationship to this Procedure,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the Procedure to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a Procedure that can be used for defining a relationship to a Procedure
     */
    public static Procedure refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return Procedure._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a Procedure by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Procedure to retrieve, either its GUID or its full qualifiedName
     * @return the requested full Procedure, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Procedure does not exist or the provided GUID is not a Procedure
     */
    @JsonIgnore
    public static Procedure get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a Procedure by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Procedure to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full Procedure, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Procedure does not exist or the provided GUID is not a Procedure
     */
    @JsonIgnore
    public static Procedure get(AtlanClient client, String id, boolean includeAllRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof Procedure) {
                return (Procedure) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof Procedure) {
                return (Procedure) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a Procedure by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Procedure to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the Procedure, including any relationships
     * @return the requested Procedure, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Procedure does not exist or the provided GUID is not a Procedure
     */
    @JsonIgnore
    public static Procedure get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a Procedure by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Procedure to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the Procedure, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the Procedure
     * @return the requested Procedure, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Procedure does not exist or the provided GUID is not a Procedure
     */
    @JsonIgnore
    public static Procedure get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = Procedure.select(client)
                    .where(Procedure.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof Procedure) {
                return (Procedure) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = Procedure.select(client)
                    .where(Procedure.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof Procedure) {
                return (Procedure) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) Procedure to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the Procedure
     * @return true if the Procedure is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a procedure.
     *
     * @param name of the procedure
     * @param definition of the procedure
     * @param schema in which the table should be created, which must have at least
     *               a qualifiedName
     * @return the minimal request necessary to create the table, as a builder
     * @throws InvalidRequestException if the schema provided is without a qualifiedName
     */
    public static Procedure.ProcedureBuilder<?, ?> creator(String name, String definition, Schema schema)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", schema.getConnectionQualifiedName());
        map.put("databaseName", schema.getDatabaseName());
        map.put("databaseQualifiedName", schema.getDatabaseQualifiedName());
        map.put("name", schema.getName());
        map.put("qualifiedName", schema.getQualifiedName());
        validateRelationship(Schema.TYPE_NAME, map);
        return creator(
                        name,
                        definition,
                        schema.getConnectionQualifiedName(),
                        schema.getDatabaseName(),
                        schema.getDatabaseQualifiedName(),
                        schema.getName(),
                        schema.getQualifiedName())
                .schema(schema.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a procedure.
     *
     * @param name of the procedure
     * @param definition of the procedure
     * @param schemaQualifiedName unique name of the schema in which this table exists
     * @return the minimal request necessary to create the table, as a builder
     */
    public static Procedure.ProcedureBuilder<?, ?> creator(String name, String definition, String schemaQualifiedName) {
        String schemaName = StringUtils.getNameFromQualifiedName(schemaQualifiedName);
        String databaseQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(schemaQualifiedName);
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        return creator(
                name,
                definition,
                connectionQualifiedName,
                databaseName,
                databaseQualifiedName,
                schemaName,
                schemaQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a procedure.
     *
     * @param name of the procedure
     * @param definition of the procedure
     * @param connectionQualifiedName unique name of the connection in which to create the Table
     * @param databaseName simple name of the Database in which to create the Table
     * @param databaseQualifiedName unique name of the Database in which to create the Table
     * @param schemaName simple name of the Schema in which to create the Table
     * @param schemaQualifiedName unique name of the Schema in which to create the Table
     * @return the minimal request necessary to create the table, as a builder
     */
    public static Procedure.ProcedureBuilder<?, ?> creator(
            String name,
            String definition,
            String connectionQualifiedName,
            String databaseName,
            String databaseQualifiedName,
            String schemaName,
            String schemaQualifiedName) {
        return Procedure._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .definition(definition)
                .qualifiedName(generateQualifiedName(name, schemaQualifiedName))
                .schemaName(schemaName)
                .schemaQualifiedName(schemaQualifiedName)
                .schema(Schema.refByQualifiedName(schemaQualifiedName))
                .databaseName(databaseName)
                .databaseQualifiedName(databaseQualifiedName)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param name of the Procedure
     * @return the minimal request necessary to update the Procedure, as a builder
     */
    public static ProcedureBuilder<?, ?> updater(String qualifiedName, String name) {
        return Procedure._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique table name.
     *
     * @param name of the table
     * @param schemaQualifiedName unique name of the schema in which this table exists
     * @return a unique name for the table
     */
    public static String generateQualifiedName(String name, String schemaQualifiedName) {
        return schemaQualifiedName + "/_procedures_/" + name;
    }

    /**
     * Builds the minimal object necessary to apply an update to a Procedure, from a potentially
     * more-complete Procedure object.
     *
     * @return the minimal object necessary to update the Procedure, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for Procedure are not found in the initial object
     */
    @Override
    public ProcedureBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ProcedureBuilder<C extends Procedure, B extends ProcedureBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a Procedure.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the Procedure
     * @param name of the Procedure
     * @return the updated Procedure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Procedure removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Procedure) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a Procedure.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the Procedure
     * @param name of the Procedure
     * @return the updated Procedure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Procedure removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Procedure) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a Procedure.
     *
     * @param client connectivity to the Atlan tenant from which to remove the Procedure's owners
     * @param qualifiedName of the Procedure
     * @param name of the Procedure
     * @return the updated Procedure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Procedure removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (Procedure) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a Procedure.
     *
     * @param client connectivity to the Atlan tenant on which to update the Procedure's certificate
     * @param qualifiedName of the Procedure
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated Procedure, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Procedure updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (Procedure) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a Procedure.
     *
     * @param client connectivity to the Atlan tenant from which to remove the Procedure's certificate
     * @param qualifiedName of the Procedure
     * @param name of the Procedure
     * @return the updated Procedure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Procedure removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Procedure) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a Procedure.
     *
     * @param client connectivity to the Atlan tenant on which to update the Procedure's announcement
     * @param qualifiedName of the Procedure
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Procedure updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (Procedure)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a Procedure.
     *
     * @param client connectivity to the Atlan client from which to remove the Procedure's announcement
     * @param qualifiedName of the Procedure
     * @param name of the Procedure
     * @return the updated Procedure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Procedure removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Procedure) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the Procedure.
     *
     * @param client connectivity to the Atlan tenant on which to replace the Procedure's assigned terms
     * @param qualifiedName for the Procedure
     * @param name human-readable name of the Procedure
     * @param terms the list of terms to replace on the Procedure, or null to remove all terms from the Procedure
     * @return the Procedure that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static Procedure replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (Procedure) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the Procedure, without replacing existing terms linked to the Procedure.
     * Note: this operation must make two API calls — one to retrieve the Procedure's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the Procedure
     * @param qualifiedName for the Procedure
     * @param terms the list of terms to append to the Procedure
     * @return the Procedure that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static Procedure appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (Procedure) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a Procedure, without replacing all existing terms linked to the Procedure.
     * Note: this operation must make two API calls — one to retrieve the Procedure's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the Procedure
     * @param qualifiedName for the Procedure
     * @param terms the list of terms to remove from the Procedure, which must be referenced by GUID
     * @return the Procedure that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static Procedure removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (Procedure) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a Procedure, without replacing existing Atlan tags linked to the Procedure.
     * Note: this operation must make two API calls — one to retrieve the Procedure's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the Procedure
     * @param qualifiedName of the Procedure
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated Procedure
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static Procedure appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (Procedure) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Procedure, without replacing existing Atlan tags linked to the Procedure.
     * Note: this operation must make two API calls — one to retrieve the Procedure's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the Procedure
     * @param qualifiedName of the Procedure
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated Procedure
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static Procedure appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (Procedure) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a Procedure.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a Procedure
     * @param qualifiedName of the Procedure
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the Procedure
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
