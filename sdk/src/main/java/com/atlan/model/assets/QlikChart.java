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
 * Instance of a Qlik chart in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class QlikChart extends Asset implements IQlikChart, IQlik, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "QlikChart";

    /** Fixed typeName for QlikCharts. */
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

    /** Identifier of the app in which this asset belongs, from Qlik. */
    @Attribute
    String qlikAppId;

    /** Unique name of the app where this asset belongs. */
    @Attribute
    String qlikAppQualifiedName;

    /** Footnote of this chart. */
    @Attribute
    String qlikChartFootnote;

    /** Orientation of this chart. */
    @Attribute
    String qlikChartOrientation;

    /** Subtitle of this chart. */
    @Attribute
    String qlikChartSubtitle;

    /** Subtype of this chart, for example: bar, graph, pie, etc. */
    @Attribute
    String qlikChartType;

    /** Identifier of this asset, from Qlik. */
    @Attribute
    String qlikId;

    /** Whether this asset is published in Qlik (true) or not (false). */
    @Attribute
    Boolean qlikIsPublished;

    /** Identifier of the owner of this asset, in Qlik. */
    @Attribute
    String qlikOwnerId;

    /** Unique QRI of this asset, from Qlik. */
    @Attribute
    String qlikQRI;

    /** Sheet in which this chart exists. */
    @Attribute
    IQlikSheet qlikSheet;

    /** Identifier of the space in which this asset exists, from Qlik. */
    @Attribute
    String qlikSpaceId;

    /** Unique name of the space in which this asset exists. */
    @Attribute
    String qlikSpaceQualifiedName;

    /**
     * Builds the minimal object necessary to create a relationship to a QlikChart, from a potentially
     * more-complete QlikChart object.
     *
     * @return the minimal object necessary to relate to the QlikChart
     * @throws InvalidRequestException if any of the minimal set of required properties for a QlikChart relationship are not found in the initial object
     */
    @Override
    public QlikChart trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all QlikChart assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) QlikChart assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all QlikChart assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all QlikChart assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) QlikCharts will be included
     * @return a fluent search that includes all QlikChart assets
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
     * Reference to a QlikChart by GUID. Use this to create a relationship to this QlikChart,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the QlikChart to reference
     * @return reference to a QlikChart that can be used for defining a relationship to a QlikChart
     */
    public static QlikChart refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a QlikChart by GUID. Use this to create a relationship to this QlikChart,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the QlikChart to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a QlikChart that can be used for defining a relationship to a QlikChart
     */
    public static QlikChart refByGuid(String guid, Reference.SaveSemantic semantic) {
        return QlikChart._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a QlikChart by qualifiedName. Use this to create a relationship to this QlikChart,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the QlikChart to reference
     * @return reference to a QlikChart that can be used for defining a relationship to a QlikChart
     */
    public static QlikChart refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a QlikChart by qualifiedName. Use this to create a relationship to this QlikChart,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the QlikChart to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a QlikChart that can be used for defining a relationship to a QlikChart
     */
    public static QlikChart refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return QlikChart._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a QlikChart by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the QlikChart to retrieve, either its GUID or its full qualifiedName
     * @return the requested full QlikChart, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikChart does not exist or the provided GUID is not a QlikChart
     */
    @JsonIgnore
    public static QlikChart get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a QlikChart by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the QlikChart to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full QlikChart, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikChart does not exist or the provided GUID is not a QlikChart
     */
    @JsonIgnore
    public static QlikChart get(AtlanClient client, String id, boolean includeAllRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof QlikChart) {
                return (QlikChart) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof QlikChart) {
                return (QlikChart) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a QlikChart by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the QlikChart to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the QlikChart, including any relationships
     * @return the requested QlikChart, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikChart does not exist or the provided GUID is not a QlikChart
     */
    @JsonIgnore
    public static QlikChart get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a QlikChart by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the QlikChart to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the QlikChart, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the QlikChart
     * @return the requested QlikChart, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikChart does not exist or the provided GUID is not a QlikChart
     */
    @JsonIgnore
    public static QlikChart get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = QlikChart.select(client)
                    .where(QlikChart.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof QlikChart) {
                return (QlikChart) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = QlikChart.select(client)
                    .where(QlikChart.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof QlikChart) {
                return (QlikChart) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) QlikChart to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the QlikChart
     * @return true if the QlikChart is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a QlikChart.
     *
     * @param qualifiedName of the QlikChart
     * @param name of the QlikChart
     * @return the minimal request necessary to update the QlikChart, as a builder
     */
    public static QlikChartBuilder<?, ?> updater(String qualifiedName, String name) {
        return QlikChart._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a QlikChart, from a potentially
     * more-complete QlikChart object.
     *
     * @return the minimal object necessary to update the QlikChart, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for QlikChart are not found in the initial object
     */
    @Override
    public QlikChartBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class QlikChartBuilder<C extends QlikChart, B extends QlikChartBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a QlikChart.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the QlikChart
     * @param name of the QlikChart
     * @return the updated QlikChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikChart removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QlikChart) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a QlikChart.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the QlikChart
     * @param name of the QlikChart
     * @return the updated QlikChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikChart removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QlikChart) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a QlikChart.
     *
     * @param client connectivity to the Atlan tenant from which to remove the QlikChart's owners
     * @param qualifiedName of the QlikChart
     * @param name of the QlikChart
     * @return the updated QlikChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikChart removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (QlikChart) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a QlikChart.
     *
     * @param client connectivity to the Atlan tenant on which to update the QlikChart's certificate
     * @param qualifiedName of the QlikChart
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QlikChart, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikChart updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (QlikChart) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a QlikChart.
     *
     * @param client connectivity to the Atlan tenant from which to remove the QlikChart's certificate
     * @param qualifiedName of the QlikChart
     * @param name of the QlikChart
     * @return the updated QlikChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikChart removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QlikChart) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a QlikChart.
     *
     * @param client connectivity to the Atlan tenant on which to update the QlikChart's announcement
     * @param qualifiedName of the QlikChart
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikChart updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (QlikChart)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a QlikChart.
     *
     * @param client connectivity to the Atlan client from which to remove the QlikChart's announcement
     * @param qualifiedName of the QlikChart
     * @param name of the QlikChart
     * @return the updated QlikChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikChart removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QlikChart) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the QlikChart.
     *
     * @param client connectivity to the Atlan tenant on which to replace the QlikChart's assigned terms
     * @param qualifiedName for the QlikChart
     * @param name human-readable name of the QlikChart
     * @param terms the list of terms to replace on the QlikChart, or null to remove all terms from the QlikChart
     * @return the QlikChart that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QlikChart replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (QlikChart) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the QlikChart, without replacing existing terms linked to the QlikChart.
     * Note: this operation must make two API calls — one to retrieve the QlikChart's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the QlikChart
     * @param qualifiedName for the QlikChart
     * @param terms the list of terms to append to the QlikChart
     * @return the QlikChart that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static QlikChart appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (QlikChart) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a QlikChart, without replacing all existing terms linked to the QlikChart.
     * Note: this operation must make two API calls — one to retrieve the QlikChart's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the QlikChart
     * @param qualifiedName for the QlikChart
     * @param terms the list of terms to remove from the QlikChart, which must be referenced by GUID
     * @return the QlikChart that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static QlikChart removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (QlikChart) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a QlikChart, without replacing existing Atlan tags linked to the QlikChart.
     * Note: this operation must make two API calls — one to retrieve the QlikChart's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the QlikChart
     * @param qualifiedName of the QlikChart
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated QlikChart
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static QlikChart appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (QlikChart) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QlikChart, without replacing existing Atlan tags linked to the QlikChart.
     * Note: this operation must make two API calls — one to retrieve the QlikChart's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the QlikChart
     * @param qualifiedName of the QlikChart
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated QlikChart
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static QlikChart appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (QlikChart) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a QlikChart.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a QlikChart
     * @param qualifiedName of the QlikChart
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the QlikChart
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
