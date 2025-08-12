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
 * Instance of a calculation view in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class CalculationView extends Asset implements ICalculationView, ISQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "CalculationView";

    /** Fixed typeName for CalculationViews. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Time at which this calculation view was activated at */
    @Attribute
    @Date
    Long calculationViewActivatedAt;

    /** The owner who activated the calculation view */
    @Attribute
    String calculationViewActivatedBy;

    /** Simple name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewName;

    /** The full package id path to which a calculation view belongs/resides in the repository. */
    @Attribute
    String calculationViewPackageId;

    /** Unique name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewQualifiedName;

    /** The version ID of this calculation view. */
    @Attribute
    Long calculationViewVersionId;

    /** Number of columns in this calculation view. */
    @Attribute
    Long columnCount;

    /** Columns that exist within this sap calculate view. */
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

    /** Schema in which this calculation view exists. */
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
     * Builds the minimal object necessary to create a relationship to a CalculationView, from a potentially
     * more-complete CalculationView object.
     *
     * @return the minimal object necessary to relate to the CalculationView
     * @throws InvalidRequestException if any of the minimal set of required properties for a CalculationView relationship are not found in the initial object
     */
    @Override
    public CalculationView trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all CalculationView assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) CalculationView assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all CalculationView assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all CalculationView assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) CalculationViews will be included
     * @return a fluent search that includes all CalculationView assets
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
     * Reference to a CalculationView by GUID. Use this to create a relationship to this CalculationView,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the CalculationView to reference
     * @return reference to a CalculationView that can be used for defining a relationship to a CalculationView
     */
    public static CalculationView refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a CalculationView by GUID. Use this to create a relationship to this CalculationView,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the CalculationView to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a CalculationView that can be used for defining a relationship to a CalculationView
     */
    public static CalculationView refByGuid(String guid, Reference.SaveSemantic semantic) {
        return CalculationView._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a CalculationView by qualifiedName. Use this to create a relationship to this CalculationView,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the CalculationView to reference
     * @return reference to a CalculationView that can be used for defining a relationship to a CalculationView
     */
    public static CalculationView refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a CalculationView by qualifiedName. Use this to create a relationship to this CalculationView,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the CalculationView to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a CalculationView that can be used for defining a relationship to a CalculationView
     */
    public static CalculationView refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return CalculationView._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a CalculationView by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CalculationView to retrieve, either its GUID or its full qualifiedName
     * @return the requested full CalculationView, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CalculationView does not exist or the provided GUID is not a CalculationView
     */
    @JsonIgnore
    public static CalculationView get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a CalculationView by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CalculationView to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full CalculationView, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CalculationView does not exist or the provided GUID is not a CalculationView
     */
    @JsonIgnore
    public static CalculationView get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof CalculationView) {
                return (CalculationView) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof CalculationView) {
                return (CalculationView) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a CalculationView by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CalculationView to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the CalculationView, including any relationships
     * @return the requested CalculationView, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CalculationView does not exist or the provided GUID is not a CalculationView
     */
    @JsonIgnore
    public static CalculationView get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a CalculationView by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CalculationView to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the CalculationView, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the CalculationView
     * @return the requested CalculationView, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CalculationView does not exist or the provided GUID is not a CalculationView
     */
    @JsonIgnore
    public static CalculationView get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = CalculationView.select(client)
                    .where(CalculationView.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof CalculationView) {
                return (CalculationView) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = CalculationView.select(client)
                    .where(CalculationView.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof CalculationView) {
                return (CalculationView) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) CalculationView to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the CalculationView
     * @return true if the CalculationView is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a CalculationView.
     *
     * @param qualifiedName of the CalculationView
     * @param name of the CalculationView
     * @return the minimal request necessary to update the CalculationView, as a builder
     */
    public static CalculationViewBuilder<?, ?> updater(String qualifiedName, String name) {
        return CalculationView._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a CalculationView, from a potentially
     * more-complete CalculationView object.
     *
     * @return the minimal object necessary to update the CalculationView, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for CalculationView are not found in the initial object
     */
    @Override
    public CalculationViewBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class CalculationViewBuilder<
                    C extends CalculationView, B extends CalculationViewBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a CalculationView.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the CalculationView
     * @param name of the CalculationView
     * @return the updated CalculationView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CalculationView removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CalculationView) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a CalculationView.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the CalculationView
     * @param name of the CalculationView
     * @return the updated CalculationView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CalculationView removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CalculationView) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a CalculationView.
     *
     * @param client connectivity to the Atlan tenant from which to remove the CalculationView's owners
     * @param qualifiedName of the CalculationView
     * @param name of the CalculationView
     * @return the updated CalculationView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CalculationView removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CalculationView) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a CalculationView.
     *
     * @param client connectivity to the Atlan tenant on which to update the CalculationView's certificate
     * @param qualifiedName of the CalculationView
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated CalculationView, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CalculationView updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (CalculationView)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a CalculationView.
     *
     * @param client connectivity to the Atlan tenant from which to remove the CalculationView's certificate
     * @param qualifiedName of the CalculationView
     * @param name of the CalculationView
     * @return the updated CalculationView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CalculationView removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CalculationView) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a CalculationView.
     *
     * @param client connectivity to the Atlan tenant on which to update the CalculationView's announcement
     * @param qualifiedName of the CalculationView
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CalculationView updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (CalculationView)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a CalculationView.
     *
     * @param client connectivity to the Atlan client from which to remove the CalculationView's announcement
     * @param qualifiedName of the CalculationView
     * @param name of the CalculationView
     * @return the updated CalculationView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CalculationView removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CalculationView) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the CalculationView.
     *
     * @param client connectivity to the Atlan tenant on which to replace the CalculationView's assigned terms
     * @param qualifiedName for the CalculationView
     * @param name human-readable name of the CalculationView
     * @param terms the list of terms to replace on the CalculationView, or null to remove all terms from the CalculationView
     * @return the CalculationView that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static CalculationView replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (CalculationView) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the CalculationView, without replacing existing terms linked to the CalculationView.
     * Note: this operation must make two API calls — one to retrieve the CalculationView's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the CalculationView
     * @param qualifiedName for the CalculationView
     * @param terms the list of terms to append to the CalculationView
     * @return the CalculationView that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static CalculationView appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (CalculationView) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a CalculationView, without replacing all existing terms linked to the CalculationView.
     * Note: this operation must make two API calls — one to retrieve the CalculationView's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the CalculationView
     * @param qualifiedName for the CalculationView
     * @param terms the list of terms to remove from the CalculationView, which must be referenced by GUID
     * @return the CalculationView that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static CalculationView removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (CalculationView) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a CalculationView, without replacing existing Atlan tags linked to the CalculationView.
     * Note: this operation must make two API calls — one to retrieve the CalculationView's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the CalculationView
     * @param qualifiedName of the CalculationView
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated CalculationView
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static CalculationView appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (CalculationView) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a CalculationView, without replacing existing Atlan tags linked to the CalculationView.
     * Note: this operation must make two API calls — one to retrieve the CalculationView's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the CalculationView
     * @param qualifiedName of the CalculationView
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated CalculationView
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static CalculationView appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (CalculationView) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a CalculationView.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a CalculationView
     * @param qualifiedName of the CalculationView
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the CalculationView
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
