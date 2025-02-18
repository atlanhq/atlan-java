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
 * Instance of a materialized view in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class MaterializedView extends Asset implements IMaterializedView, ISQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MaterialisedView";

    /** Fixed typeName for MaterializedViews. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String alias;

    /** TBC */
    @Attribute
    String calculationViewName;

    /** TBC */
    @Attribute
    String calculationViewQualifiedName;

    /** TBC */
    @Attribute
    Long columnCount;

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
    String definition;

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
    Boolean isProfiled;

    /** TBC */
    @Attribute
    Boolean isQueryPreview;

    /** TBC */
    @Attribute
    Boolean isTemporary;

    /** TBC */
    @Attribute
    @Date
    Long lastProfiledAt;

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
    Long queryCount;

    /** TBC */
    @Attribute
    @Date
    Long queryCountUpdatedAt;

    /** TBC */
    @Attribute
    @Singular("putQueryPreviewConfig")
    Map<String, String> queryPreviewConfig;

    /** TBC */
    @Attribute
    Long queryUserCount;

    /** TBC */
    @Attribute
    @Singular("putQueryUserMap")
    Map<String, Long> queryUserMap;

    /** TBC */
    @Attribute
    String refreshMethod;

    /** TBC */
    @Attribute
    String refreshMode;

    /** TBC */
    @Attribute
    Long rowCount;

    /** TBC */
    @Attribute
    @JsonProperty("atlanSchema")
    ISchema schema;

    /** TBC */
    @Attribute
    String schemaName;

    /** TBC */
    @Attribute
    String schemaQualifiedName;

    /** TBC */
    @Attribute
    Long sizeBytes;

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
    @Date
    Long staleSinceDate;

    /** TBC */
    @Attribute
    String staleness;

    /** TBC */
    @Attribute
    String tableName;

    /** TBC */
    @Attribute
    String tableQualifiedName;

    /** TBC */
    @Attribute
    String viewName;

    /** TBC */
    @Attribute
    String viewQualifiedName;

    /**
     * Builds the minimal object necessary to create a relationship to a MaterializedView, from a potentially
     * more-complete MaterializedView object.
     *
     * @return the minimal object necessary to relate to the MaterializedView
     * @throws InvalidRequestException if any of the minimal set of required properties for a MaterializedView relationship are not found in the initial object
     */
    @Override
    public MaterializedView trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all MaterializedView assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) MaterializedView assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all MaterializedView assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all MaterializedView assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) MaterializedViews will be included
     * @return a fluent search that includes all MaterializedView assets
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
     * Reference to a MaterializedView by GUID. Use this to create a relationship to this MaterializedView,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the MaterializedView to reference
     * @return reference to a MaterializedView that can be used for defining a relationship to a MaterializedView
     */
    public static MaterializedView refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a MaterializedView by GUID. Use this to create a relationship to this MaterializedView,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the MaterializedView to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a MaterializedView that can be used for defining a relationship to a MaterializedView
     */
    public static MaterializedView refByGuid(String guid, Reference.SaveSemantic semantic) {
        return MaterializedView._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a MaterializedView by qualifiedName. Use this to create a relationship to this MaterializedView,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the MaterializedView to reference
     * @return reference to a MaterializedView that can be used for defining a relationship to a MaterializedView
     */
    public static MaterializedView refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a MaterializedView by qualifiedName. Use this to create a relationship to this MaterializedView,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the MaterializedView to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a MaterializedView that can be used for defining a relationship to a MaterializedView
     */
    public static MaterializedView refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return MaterializedView._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a MaterializedView by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the MaterializedView to retrieve, either its GUID or its full qualifiedName
     * @return the requested full MaterializedView, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MaterializedView does not exist or the provided GUID is not a MaterializedView
     */
    @JsonIgnore
    public static MaterializedView get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a MaterializedView by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the MaterializedView to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full MaterializedView, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MaterializedView does not exist or the provided GUID is not a MaterializedView
     */
    @JsonIgnore
    public static MaterializedView get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof MaterializedView) {
                return (MaterializedView) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof MaterializedView) {
                return (MaterializedView) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a MaterializedView by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the MaterializedView to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the MaterializedView, including any relationships
     * @return the requested MaterializedView, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MaterializedView does not exist or the provided GUID is not a MaterializedView
     */
    @JsonIgnore
    public static MaterializedView get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a MaterializedView by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the MaterializedView to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the MaterializedView, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the MaterializedView
     * @return the requested MaterializedView, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MaterializedView does not exist or the provided GUID is not a MaterializedView
     */
    @JsonIgnore
    public static MaterializedView get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = MaterializedView.select(client)
                    .where(MaterializedView.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof MaterializedView) {
                return (MaterializedView) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = MaterializedView.select(client)
                    .where(MaterializedView.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof MaterializedView) {
                return (MaterializedView) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) MaterializedView to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the MaterializedView
     * @return true if the MaterializedView is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a materialized view.
     *
     * @param name of the materialized view
     * @param schema in which the materialized view should be created, which must have at least
     *               a qualifiedName
     * @return the minimal request necessary to create the materialized view, as a builder
     * @throws InvalidRequestException if the schema provided is without a qualifiedName
     */
    public static MaterializedViewBuilder<?, ?> creator(String name, Schema schema) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", schema.getConnectionQualifiedName());
        map.put("databaseName", schema.getDatabaseName());
        map.put("databaseQualifiedName", schema.getDatabaseQualifiedName());
        map.put("name", schema.getName());
        map.put("qualifiedName", schema.getQualifiedName());
        validateRelationship(Schema.TYPE_NAME, map);
        return creator(
                        name,
                        schema.getConnectionQualifiedName(),
                        schema.getDatabaseName(),
                        schema.getDatabaseQualifiedName(),
                        schema.getName(),
                        schema.getQualifiedName())
                .schema(schema.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a materialized view.
     *
     * @param name of the materialized view
     * @param schemaQualifiedName unique name of the schema in which this materialized view exists
     * @return the minimal request necessary to create the materialized view, as a builder
     */
    public static MaterializedViewBuilder<?, ?> creator(String name, String schemaQualifiedName) {
        String schemaName = StringUtils.getNameFromQualifiedName(schemaQualifiedName);
        String databaseQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(schemaQualifiedName);
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        return creator(
                name, connectionQualifiedName, databaseName, databaseQualifiedName, schemaName, schemaQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a materialized view.
     *
     * @param name of the materialized view
     * @param connectionQualifiedName unique name of the connection in which to create the MaterializedView
     * @param databaseName simple name of the database in which to create the MaterializedView
     * @param databaseQualifiedName unique name of the database in which to create the MaterializedView
     * @param schemaName simple name of the database in which to create the MaterializedView
     * @param schemaQualifiedName unique name of the schema in which to create the MaterializedView
     * @return the minimal request necessary to create the materialized view, as a builder
     */
    public static MaterializedViewBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String databaseName,
            String databaseQualifiedName,
            String schemaName,
            String schemaQualifiedName) {
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return MaterializedView._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, schemaQualifiedName))
                .connectorType(connectorType)
                .schemaName(schemaName)
                .schemaQualifiedName(schemaQualifiedName)
                .schema(Schema.refByQualifiedName(schemaQualifiedName))
                .databaseName(databaseName)
                .databaseQualifiedName(databaseQualifiedName)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Generate a unique materialized view name.
     *
     * @param name of the materialized view
     * @param schemaQualifiedName unique name of the schema in which this materialized view exists
     * @return a unique name for the materialized view
     */
    public static String generateQualifiedName(String name, String schemaQualifiedName) {
        return schemaQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a MaterializedView.
     *
     * @param qualifiedName of the MaterializedView
     * @param name of the MaterializedView
     * @return the minimal request necessary to update the MaterializedView, as a builder
     */
    public static MaterializedViewBuilder<?, ?> updater(String qualifiedName, String name) {
        return MaterializedView._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a MaterializedView, from a potentially
     * more-complete MaterializedView object.
     *
     * @return the minimal object necessary to update the MaterializedView, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for MaterializedView are not found in the initial object
     */
    @Override
    public MaterializedViewBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a MaterializedView.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MaterializedView
     * @param name of the MaterializedView
     * @return the updated MaterializedView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MaterializedView) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a MaterializedView.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MaterializedView
     * @param name of the MaterializedView
     * @return the updated MaterializedView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MaterializedView) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a MaterializedView.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MaterializedView's owners
     * @param qualifiedName of the MaterializedView
     * @param name of the MaterializedView
     * @return the updated MaterializedView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MaterializedView) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a MaterializedView.
     *
     * @param client connectivity to the Atlan tenant on which to update the MaterializedView's certificate
     * @param qualifiedName of the MaterializedView
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MaterializedView, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (MaterializedView)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MaterializedView.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MaterializedView's certificate
     * @param qualifiedName of the MaterializedView
     * @param name of the MaterializedView
     * @return the updated MaterializedView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MaterializedView) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a MaterializedView.
     *
     * @param client connectivity to the Atlan tenant on which to update the MaterializedView's announcement
     * @param qualifiedName of the MaterializedView
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (MaterializedView)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MaterializedView.
     *
     * @param client connectivity to the Atlan client from which to remove the MaterializedView's announcement
     * @param qualifiedName of the MaterializedView
     * @param name of the MaterializedView
     * @return the updated MaterializedView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MaterializedView) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the MaterializedView.
     *
     * @param client connectivity to the Atlan tenant on which to replace the MaterializedView's assigned terms
     * @param qualifiedName for the MaterializedView
     * @param name human-readable name of the MaterializedView
     * @param terms the list of terms to replace on the MaterializedView, or null to remove all terms from the MaterializedView
     * @return the MaterializedView that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MaterializedView replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (MaterializedView) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MaterializedView, without replacing existing terms linked to the MaterializedView.
     * Note: this operation must make two API calls — one to retrieve the MaterializedView's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the MaterializedView
     * @param qualifiedName for the MaterializedView
     * @param terms the list of terms to append to the MaterializedView
     * @return the MaterializedView that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static MaterializedView appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MaterializedView) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MaterializedView, without replacing all existing terms linked to the MaterializedView.
     * Note: this operation must make two API calls — one to retrieve the MaterializedView's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the MaterializedView
     * @param qualifiedName for the MaterializedView
     * @param terms the list of terms to remove from the MaterializedView, which must be referenced by GUID
     * @return the MaterializedView that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static MaterializedView removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MaterializedView) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a MaterializedView, without replacing existing Atlan tags linked to the MaterializedView.
     * Note: this operation must make two API calls — one to retrieve the MaterializedView's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MaterializedView
     * @param qualifiedName of the MaterializedView
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MaterializedView
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static MaterializedView appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (MaterializedView) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MaterializedView, without replacing existing Atlan tags linked to the MaterializedView.
     * Note: this operation must make two API calls — one to retrieve the MaterializedView's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MaterializedView
     * @param qualifiedName of the MaterializedView
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MaterializedView
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static MaterializedView appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (MaterializedView) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a MaterializedView.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a MaterializedView
     * @param qualifiedName of the MaterializedView
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MaterializedView
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
