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
 * Instances of SupersetDataset in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class SupersetDataset extends Asset
        implements ISupersetDataset, ISuperset, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SupersetDataset";

    /** Fixed typeName for SupersetDatasets. */
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

    /** SupersetDashboard asset containing this SupersetDataset. */
    @Attribute
    ISupersetDashboard supersetDashboard;

    /** Identifier of the dashboard in which this asset exists, in Superset. */
    @Attribute
    Long supersetDashboardId;

    /** Unique name of the dashboard in which this asset exists. */
    @Attribute
    String supersetDashboardQualifiedName;

    /** Name of the datasource for the dataset. */
    @Attribute
    String supersetDatasetDatasourceName;

    /** Id of the dataset in superset. */
    @Attribute
    Long supersetDatasetId;

    /** Type of the dataset in superset. */
    @Attribute
    String supersetDatasetType;

    /**
     * Builds the minimal object necessary to create a relationship to a SupersetDataset, from a potentially
     * more-complete SupersetDataset object.
     *
     * @return the minimal object necessary to relate to the SupersetDataset
     * @throws InvalidRequestException if any of the minimal set of required properties for a SupersetDataset relationship are not found in the initial object
     */
    @Override
    public SupersetDataset trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SupersetDataset assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SupersetDataset assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SupersetDataset assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SupersetDataset assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SupersetDatasets will be included
     * @return a fluent search that includes all SupersetDataset assets
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
     * Reference to a SupersetDataset by GUID. Use this to create a relationship to this SupersetDataset,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SupersetDataset to reference
     * @return reference to a SupersetDataset that can be used for defining a relationship to a SupersetDataset
     */
    public static SupersetDataset refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SupersetDataset by GUID. Use this to create a relationship to this SupersetDataset,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SupersetDataset to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SupersetDataset that can be used for defining a relationship to a SupersetDataset
     */
    public static SupersetDataset refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SupersetDataset._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SupersetDataset by qualifiedName. Use this to create a relationship to this SupersetDataset,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SupersetDataset to reference
     * @return reference to a SupersetDataset that can be used for defining a relationship to a SupersetDataset
     */
    public static SupersetDataset refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SupersetDataset by qualifiedName. Use this to create a relationship to this SupersetDataset,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SupersetDataset to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SupersetDataset that can be used for defining a relationship to a SupersetDataset
     */
    public static SupersetDataset refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SupersetDataset._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SupersetDataset by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SupersetDataset to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SupersetDataset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SupersetDataset does not exist or the provided GUID is not a SupersetDataset
     */
    @JsonIgnore
    public static SupersetDataset get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SupersetDataset by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SupersetDataset to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SupersetDataset, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SupersetDataset does not exist or the provided GUID is not a SupersetDataset
     */
    @JsonIgnore
    public static SupersetDataset get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SupersetDataset) {
                return (SupersetDataset) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof SupersetDataset) {
                return (SupersetDataset) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SupersetDataset by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SupersetDataset to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SupersetDataset, including any relationships
     * @return the requested SupersetDataset, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SupersetDataset does not exist or the provided GUID is not a SupersetDataset
     */
    @JsonIgnore
    public static SupersetDataset get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a SupersetDataset by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SupersetDataset to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SupersetDataset, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the SupersetDataset
     * @return the requested SupersetDataset, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SupersetDataset does not exist or the provided GUID is not a SupersetDataset
     */
    @JsonIgnore
    public static SupersetDataset get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = SupersetDataset.select(client)
                    .where(SupersetDataset.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof SupersetDataset) {
                return (SupersetDataset) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = SupersetDataset.select(client)
                    .where(SupersetDataset.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof SupersetDataset) {
                return (SupersetDataset) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SupersetDataset to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SupersetDataset
     * @return true if the SupersetDataset is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Superset dataset.
     *
     * @param name of the dataset
     * @param dashboard in which the dataset should be created, which must have at least
     *                   a qualifiedName
     * @return the minimal request necessary to create the dataset, as a builder
     * @throws InvalidRequestException if the dashboard provided is without a qualifiedName
     */
    public static SupersetDatasetBuilder<?, ?> creator(String name, SupersetDashboard dashboard)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", dashboard.getQualifiedName());
        map.put("connectionQualifiedName", dashboard.getConnectionQualifiedName());
        validateRelationship(SupersetDashboard.TYPE_NAME, map);
        return creator(name, dashboard.getConnectionQualifiedName(), dashboard.getQualifiedName())
                .supersetDashboard(dashboard.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Superset dataset.
     *
     * @param name of the dataset
     * @param dashboardQualifiedName unique name of the dashboard in which the dataset exists
     * @return the minimal object necessary to create the dataset, as a builder
     */
    public static SupersetDatasetBuilder<?, ?> creator(String name, String dashboardQualifiedName) {
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(dashboardQualifiedName);
        return creator(name, connectionQualifiedName, dashboardQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Superset dataset.
     *
     * @param name of the dataset
     * @param connectionQualifiedName unique name of the connection in which to create the SupersetDataset
     * @param dashboardQualifiedName unique name of the SupersetDashboard in which to create the SupersetDataset
     * @return the minimal object necessary to create the dataset, as a builder
     */
    public static SupersetDatasetBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String dashboardQualifiedName) {
        return SupersetDataset._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(dashboardQualifiedName + "/" + name)
                .supersetDashboardQualifiedName(dashboardQualifiedName)
                .supersetDashboard(SupersetDashboard.refByQualifiedName(dashboardQualifiedName))
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SupersetDataset.
     *
     * @param qualifiedName of the SupersetDataset
     * @param name of the SupersetDataset
     * @return the minimal request necessary to update the SupersetDataset, as a builder
     */
    public static SupersetDatasetBuilder<?, ?> updater(String qualifiedName, String name) {
        return SupersetDataset._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SupersetDataset, from a potentially
     * more-complete SupersetDataset object.
     *
     * @return the minimal object necessary to update the SupersetDataset, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SupersetDataset are not found in the initial object
     */
    @Override
    public SupersetDatasetBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class SupersetDatasetBuilder<
                    C extends SupersetDataset, B extends SupersetDatasetBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a SupersetDataset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SupersetDataset
     * @param name of the SupersetDataset
     * @return the updated SupersetDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SupersetDataset removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SupersetDataset) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SupersetDataset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SupersetDataset
     * @param name of the SupersetDataset
     * @return the updated SupersetDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SupersetDataset removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SupersetDataset) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SupersetDataset.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SupersetDataset's owners
     * @param qualifiedName of the SupersetDataset
     * @param name of the SupersetDataset
     * @return the updated SupersetDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SupersetDataset removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SupersetDataset) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SupersetDataset.
     *
     * @param client connectivity to the Atlan tenant on which to update the SupersetDataset's certificate
     * @param qualifiedName of the SupersetDataset
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SupersetDataset, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SupersetDataset updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SupersetDataset)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SupersetDataset.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SupersetDataset's certificate
     * @param qualifiedName of the SupersetDataset
     * @param name of the SupersetDataset
     * @return the updated SupersetDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SupersetDataset removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SupersetDataset) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SupersetDataset.
     *
     * @param client connectivity to the Atlan tenant on which to update the SupersetDataset's announcement
     * @param qualifiedName of the SupersetDataset
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SupersetDataset updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SupersetDataset)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SupersetDataset.
     *
     * @param client connectivity to the Atlan client from which to remove the SupersetDataset's announcement
     * @param qualifiedName of the SupersetDataset
     * @param name of the SupersetDataset
     * @return the updated SupersetDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SupersetDataset removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SupersetDataset) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SupersetDataset.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SupersetDataset's assigned terms
     * @param qualifiedName for the SupersetDataset
     * @param name human-readable name of the SupersetDataset
     * @param terms the list of terms to replace on the SupersetDataset, or null to remove all terms from the SupersetDataset
     * @return the SupersetDataset that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SupersetDataset replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SupersetDataset) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SupersetDataset, without replacing existing terms linked to the SupersetDataset.
     * Note: this operation must make two API calls — one to retrieve the SupersetDataset's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SupersetDataset
     * @param qualifiedName for the SupersetDataset
     * @param terms the list of terms to append to the SupersetDataset
     * @return the SupersetDataset that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SupersetDataset appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SupersetDataset) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SupersetDataset, without replacing all existing terms linked to the SupersetDataset.
     * Note: this operation must make two API calls — one to retrieve the SupersetDataset's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SupersetDataset
     * @param qualifiedName for the SupersetDataset
     * @param terms the list of terms to remove from the SupersetDataset, which must be referenced by GUID
     * @return the SupersetDataset that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SupersetDataset removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SupersetDataset) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SupersetDataset, without replacing existing Atlan tags linked to the SupersetDataset.
     * Note: this operation must make two API calls — one to retrieve the SupersetDataset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SupersetDataset
     * @param qualifiedName of the SupersetDataset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SupersetDataset
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static SupersetDataset appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SupersetDataset) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SupersetDataset, without replacing existing Atlan tags linked to the SupersetDataset.
     * Note: this operation must make two API calls — one to retrieve the SupersetDataset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SupersetDataset
     * @param qualifiedName of the SupersetDataset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SupersetDataset
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static SupersetDataset appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SupersetDataset) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SupersetDataset.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SupersetDataset
     * @param qualifiedName of the SupersetDataset
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SupersetDataset
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
