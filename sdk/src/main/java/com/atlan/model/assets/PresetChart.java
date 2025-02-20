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
 * Instance of a Preset chart in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class PresetChart extends Asset implements IPresetChart, IPreset, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "PresetChart";

    /** Fixed typeName for PresetCharts. */
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

    /** TBC */
    @Attribute
    String presetChartDescriptionMarkdown;

    /** TBC */
    @Attribute
    @Singular("putPresetChartFormData")
    Map<String, String> presetChartFormData;

    /** Dashboard in which this chart exists. */
    @Attribute
    IPresetDashboard presetDashboard;

    /** Identifier of the dashboard in which this asset exists, in Preset. */
    @Attribute
    Long presetDashboardId;

    /** Unique name of the dashboard in which this asset exists. */
    @Attribute
    String presetDashboardQualifiedName;

    /** Identifier of the workspace in which this asset exists, in Preset. */
    @Attribute
    Long presetWorkspaceId;

    /** Unique name of the workspace in which this asset exists. */
    @Attribute
    String presetWorkspaceQualifiedName;

    /**
     * Builds the minimal object necessary to create a relationship to a PresetChart, from a potentially
     * more-complete PresetChart object.
     *
     * @return the minimal object necessary to relate to the PresetChart
     * @throws InvalidRequestException if any of the minimal set of required properties for a PresetChart relationship are not found in the initial object
     */
    @Override
    public PresetChart trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all PresetChart assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) PresetChart assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all PresetChart assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all PresetChart assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) PresetCharts will be included
     * @return a fluent search that includes all PresetChart assets
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
     * Reference to a PresetChart by GUID. Use this to create a relationship to this PresetChart,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the PresetChart to reference
     * @return reference to a PresetChart that can be used for defining a relationship to a PresetChart
     */
    public static PresetChart refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a PresetChart by GUID. Use this to create a relationship to this PresetChart,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the PresetChart to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a PresetChart that can be used for defining a relationship to a PresetChart
     */
    public static PresetChart refByGuid(String guid, Reference.SaveSemantic semantic) {
        return PresetChart._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a PresetChart by qualifiedName. Use this to create a relationship to this PresetChart,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the PresetChart to reference
     * @return reference to a PresetChart that can be used for defining a relationship to a PresetChart
     */
    public static PresetChart refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a PresetChart by qualifiedName. Use this to create a relationship to this PresetChart,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the PresetChart to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a PresetChart that can be used for defining a relationship to a PresetChart
     */
    public static PresetChart refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return PresetChart._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a PresetChart by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the PresetChart to retrieve, either its GUID or its full qualifiedName
     * @return the requested full PresetChart, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PresetChart does not exist or the provided GUID is not a PresetChart
     */
    @JsonIgnore
    public static PresetChart get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a PresetChart by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the PresetChart to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full PresetChart, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PresetChart does not exist or the provided GUID is not a PresetChart
     */
    @JsonIgnore
    public static PresetChart get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof PresetChart) {
                return (PresetChart) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof PresetChart) {
                return (PresetChart) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a PresetChart by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the PresetChart to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the PresetChart, including any relationships
     * @return the requested PresetChart, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PresetChart does not exist or the provided GUID is not a PresetChart
     */
    @JsonIgnore
    public static PresetChart get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a PresetChart by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the PresetChart to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the PresetChart, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the PresetChart
     * @return the requested PresetChart, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PresetChart does not exist or the provided GUID is not a PresetChart
     */
    @JsonIgnore
    public static PresetChart get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = PresetChart.select(client)
                    .where(PresetChart.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof PresetChart) {
                return (PresetChart) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = PresetChart.select(client)
                    .where(PresetChart.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof PresetChart) {
                return (PresetChart) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) PresetChart to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the PresetChart
     * @return true if the PresetChart is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Preset chart.
     *
     * @param name of the chart
     * @param collection in which the chart should be created, which must have at least
     *                   a qualifiedName
     * @return the minimal request necessary to create the chart, as a builder
     * @throws InvalidRequestException if the collection provided is without a qualifiedName
     */
    public static PresetChartBuilder<?, ?> creator(String name, PresetDashboard collection)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", collection.getConnectionQualifiedName());
        map.put("presetWorkspaceQualifiedName", collection.getPresetWorkspaceQualifiedName());
        map.put("qualifiedName", collection.getQualifiedName());
        validateRelationship(PresetDashboard.TYPE_NAME, map);
        return creator(
                        name,
                        collection.getConnectionQualifiedName(),
                        collection.getPresetWorkspaceQualifiedName(),
                        collection.getQualifiedName())
                .presetDashboard(collection.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Preset chart.
     *
     * @param name of the chart
     * @param collectionQualifiedName unique name of the collection in which the chart exists
     * @return the minimal object necessary to create the chart, as a builder
     */
    public static PresetChartBuilder<?, ?> creator(String name, String collectionQualifiedName) {
        String workspaceQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(collectionQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(workspaceQualifiedName);
        return creator(name, connectionQualifiedName, workspaceQualifiedName, collectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Preset chart.
     *
     * @param name of the chart
     * @param connectionQualifiedName unique name of the connection in which to create the PresetChart
     * @param workspaceQualifiedName unique name of the PresetWorkspace in which to create the PresetChart
     * @param collectionQualifiedName unique name of the PresetDashboard in which to create the PresetChart
     * @return the minimal object necessary to create the chart, as a builder
     */
    public static PresetChartBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String workspaceQualifiedName,
            String collectionQualifiedName) {
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return PresetChart._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(collectionQualifiedName + "/" + name)
                .connectorType(connectorType)
                .presetDashboardQualifiedName(collectionQualifiedName)
                .presetDashboard(PresetDashboard.refByQualifiedName(collectionQualifiedName))
                .presetWorkspaceQualifiedName(workspaceQualifiedName)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a PresetChart.
     *
     * @param qualifiedName of the PresetChart
     * @param name of the PresetChart
     * @return the minimal request necessary to update the PresetChart, as a builder
     */
    public static PresetChartBuilder<?, ?> updater(String qualifiedName, String name) {
        return PresetChart._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PresetChart, from a potentially
     * more-complete PresetChart object.
     *
     * @return the minimal object necessary to update the PresetChart, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for PresetChart are not found in the initial object
     */
    @Override
    public PresetChartBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a PresetChart.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the PresetChart
     * @param name of the PresetChart
     * @return the updated PresetChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (PresetChart) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a PresetChart.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the PresetChart
     * @param name of the PresetChart
     * @return the updated PresetChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (PresetChart) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a PresetChart.
     *
     * @param client connectivity to the Atlan tenant from which to remove the PresetChart's owners
     * @param qualifiedName of the PresetChart
     * @param name of the PresetChart
     * @return the updated PresetChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (PresetChart) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a PresetChart.
     *
     * @param client connectivity to the Atlan tenant on which to update the PresetChart's certificate
     * @param qualifiedName of the PresetChart
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated PresetChart, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (PresetChart)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a PresetChart.
     *
     * @param client connectivity to the Atlan tenant from which to remove the PresetChart's certificate
     * @param qualifiedName of the PresetChart
     * @param name of the PresetChart
     * @return the updated PresetChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (PresetChart) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a PresetChart.
     *
     * @param client connectivity to the Atlan tenant on which to update the PresetChart's announcement
     * @param qualifiedName of the PresetChart
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (PresetChart)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a PresetChart.
     *
     * @param client connectivity to the Atlan client from which to remove the PresetChart's announcement
     * @param qualifiedName of the PresetChart
     * @param name of the PresetChart
     * @return the updated PresetChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (PresetChart) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the PresetChart.
     *
     * @param client connectivity to the Atlan tenant on which to replace the PresetChart's assigned terms
     * @param qualifiedName for the PresetChart
     * @param name human-readable name of the PresetChart
     * @param terms the list of terms to replace on the PresetChart, or null to remove all terms from the PresetChart
     * @return the PresetChart that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static PresetChart replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (PresetChart) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the PresetChart, without replacing existing terms linked to the PresetChart.
     * Note: this operation must make two API calls — one to retrieve the PresetChart's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the PresetChart
     * @param qualifiedName for the PresetChart
     * @param terms the list of terms to append to the PresetChart
     * @return the PresetChart that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static PresetChart appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (PresetChart) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a PresetChart, without replacing all existing terms linked to the PresetChart.
     * Note: this operation must make two API calls — one to retrieve the PresetChart's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the PresetChart
     * @param qualifiedName for the PresetChart
     * @param terms the list of terms to remove from the PresetChart, which must be referenced by GUID
     * @return the PresetChart that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static PresetChart removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (PresetChart) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a PresetChart, without replacing existing Atlan tags linked to the PresetChart.
     * Note: this operation must make two API calls — one to retrieve the PresetChart's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the PresetChart
     * @param qualifiedName of the PresetChart
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated PresetChart
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static PresetChart appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (PresetChart) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a PresetChart, without replacing existing Atlan tags linked to the PresetChart.
     * Note: this operation must make two API calls — one to retrieve the PresetChart's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the PresetChart
     * @param qualifiedName of the PresetChart
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated PresetChart
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static PresetChart appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (PresetChart) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a PresetChart.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a PresetChart
     * @param qualifiedName of the PresetChart
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the PresetChart
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
