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
 * Instance of a Thoughtspot worksheet in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class ThoughtspotWorksheet extends Asset
        implements IThoughtspotWorksheet, IThoughtspot, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ThoughtspotWorksheet";

    /** Fixed typeName for ThoughtspotWorksheets. */
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
    String thoughtspotChartType;

    /** Number of Columns. */
    @Attribute
    Long thoughtspotColumnCount;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IThoughtspotColumn> thoughtspotColumns;

    /** Total number of data table joins executed for analysis. */
    @Attribute
    Long thoughtspotJoinCount;

    /** TBC */
    @Attribute
    String thoughtspotQuestionText;

    /**
     * Builds the minimal object necessary to create a relationship to a ThoughtspotWorksheet, from a potentially
     * more-complete ThoughtspotWorksheet object.
     *
     * @return the minimal object necessary to relate to the ThoughtspotWorksheet
     * @throws InvalidRequestException if any of the minimal set of required properties for a ThoughtspotWorksheet relationship are not found in the initial object
     */
    @Override
    public ThoughtspotWorksheet trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all ThoughtspotWorksheet assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ThoughtspotWorksheet assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all ThoughtspotWorksheet assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all ThoughtspotWorksheet assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ThoughtspotWorksheets will be included
     * @return a fluent search that includes all ThoughtspotWorksheet assets
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
     * Reference to a ThoughtspotWorksheet by GUID. Use this to create a relationship to this ThoughtspotWorksheet,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the ThoughtspotWorksheet to reference
     * @return reference to a ThoughtspotWorksheet that can be used for defining a relationship to a ThoughtspotWorksheet
     */
    public static ThoughtspotWorksheet refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ThoughtspotWorksheet by GUID. Use this to create a relationship to this ThoughtspotWorksheet,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the ThoughtspotWorksheet to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ThoughtspotWorksheet that can be used for defining a relationship to a ThoughtspotWorksheet
     */
    public static ThoughtspotWorksheet refByGuid(String guid, Reference.SaveSemantic semantic) {
        return ThoughtspotWorksheet._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a ThoughtspotWorksheet by qualifiedName. Use this to create a relationship to this ThoughtspotWorksheet,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the ThoughtspotWorksheet to reference
     * @return reference to a ThoughtspotWorksheet that can be used for defining a relationship to a ThoughtspotWorksheet
     */
    public static ThoughtspotWorksheet refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ThoughtspotWorksheet by qualifiedName. Use this to create a relationship to this ThoughtspotWorksheet,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the ThoughtspotWorksheet to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ThoughtspotWorksheet that can be used for defining a relationship to a ThoughtspotWorksheet
     */
    public static ThoughtspotWorksheet refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return ThoughtspotWorksheet._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a ThoughtspotWorksheet by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ThoughtspotWorksheet to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ThoughtspotWorksheet, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ThoughtspotWorksheet does not exist or the provided GUID is not a ThoughtspotWorksheet
     */
    @JsonIgnore
    public static ThoughtspotWorksheet get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a ThoughtspotWorksheet by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ThoughtspotWorksheet to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full ThoughtspotWorksheet, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ThoughtspotWorksheet does not exist or the provided GUID is not a ThoughtspotWorksheet
     */
    @JsonIgnore
    public static ThoughtspotWorksheet get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof ThoughtspotWorksheet) {
                return (ThoughtspotWorksheet) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof ThoughtspotWorksheet) {
                return (ThoughtspotWorksheet) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a ThoughtspotWorksheet by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ThoughtspotWorksheet to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the ThoughtspotWorksheet, including any relationships
     * @return the requested ThoughtspotWorksheet, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ThoughtspotWorksheet does not exist or the provided GUID is not a ThoughtspotWorksheet
     */
    @JsonIgnore
    public static ThoughtspotWorksheet get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a ThoughtspotWorksheet by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ThoughtspotWorksheet to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the ThoughtspotWorksheet, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the ThoughtspotWorksheet
     * @return the requested ThoughtspotWorksheet, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ThoughtspotWorksheet does not exist or the provided GUID is not a ThoughtspotWorksheet
     */
    @JsonIgnore
    public static ThoughtspotWorksheet get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = ThoughtspotWorksheet.select(client)
                    .where(ThoughtspotWorksheet.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof ThoughtspotWorksheet) {
                return (ThoughtspotWorksheet) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = ThoughtspotWorksheet.select(client)
                    .where(ThoughtspotWorksheet.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof ThoughtspotWorksheet) {
                return (ThoughtspotWorksheet) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) ThoughtspotWorksheet to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the ThoughtspotWorksheet
     * @return true if the ThoughtspotWorksheet is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a ThoughtspotWorksheet.
     *
     * @param qualifiedName of the ThoughtspotWorksheet
     * @param name of the ThoughtspotWorksheet
     * @return the minimal request necessary to update the ThoughtspotWorksheet, as a builder
     */
    public static ThoughtspotWorksheetBuilder<?, ?> updater(String qualifiedName, String name) {
        return ThoughtspotWorksheet._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ThoughtspotWorksheet, from a potentially
     * more-complete ThoughtspotWorksheet object.
     *
     * @return the minimal object necessary to update the ThoughtspotWorksheet, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ThoughtspotWorksheet are not found in the initial object
     */
    @Override
    public ThoughtspotWorksheetBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a ThoughtspotWorksheet.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ThoughtspotWorksheet
     * @param name of the ThoughtspotWorksheet
     * @return the updated ThoughtspotWorksheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotWorksheet removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ThoughtspotWorksheet) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ThoughtspotWorksheet.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ThoughtspotWorksheet
     * @param name of the ThoughtspotWorksheet
     * @return the updated ThoughtspotWorksheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotWorksheet removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ThoughtspotWorksheet) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ThoughtspotWorksheet.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ThoughtspotWorksheet's owners
     * @param qualifiedName of the ThoughtspotWorksheet
     * @param name of the ThoughtspotWorksheet
     * @return the updated ThoughtspotWorksheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotWorksheet removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ThoughtspotWorksheet) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ThoughtspotWorksheet.
     *
     * @param client connectivity to the Atlan tenant on which to update the ThoughtspotWorksheet's certificate
     * @param qualifiedName of the ThoughtspotWorksheet
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ThoughtspotWorksheet, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotWorksheet updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (ThoughtspotWorksheet)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ThoughtspotWorksheet.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ThoughtspotWorksheet's certificate
     * @param qualifiedName of the ThoughtspotWorksheet
     * @param name of the ThoughtspotWorksheet
     * @return the updated ThoughtspotWorksheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotWorksheet removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ThoughtspotWorksheet) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ThoughtspotWorksheet.
     *
     * @param client connectivity to the Atlan tenant on which to update the ThoughtspotWorksheet's announcement
     * @param qualifiedName of the ThoughtspotWorksheet
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotWorksheet updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (ThoughtspotWorksheet)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ThoughtspotWorksheet.
     *
     * @param client connectivity to the Atlan client from which to remove the ThoughtspotWorksheet's announcement
     * @param qualifiedName of the ThoughtspotWorksheet
     * @param name of the ThoughtspotWorksheet
     * @return the updated ThoughtspotWorksheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotWorksheet removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ThoughtspotWorksheet) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the ThoughtspotWorksheet.
     *
     * @param client connectivity to the Atlan tenant on which to replace the ThoughtspotWorksheet's assigned terms
     * @param qualifiedName for the ThoughtspotWorksheet
     * @param name human-readable name of the ThoughtspotWorksheet
     * @param terms the list of terms to replace on the ThoughtspotWorksheet, or null to remove all terms from the ThoughtspotWorksheet
     * @return the ThoughtspotWorksheet that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotWorksheet replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (ThoughtspotWorksheet) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ThoughtspotWorksheet, without replacing existing terms linked to the ThoughtspotWorksheet.
     * Note: this operation must make two API calls — one to retrieve the ThoughtspotWorksheet's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the ThoughtspotWorksheet
     * @param qualifiedName for the ThoughtspotWorksheet
     * @param terms the list of terms to append to the ThoughtspotWorksheet
     * @return the ThoughtspotWorksheet that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static ThoughtspotWorksheet appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ThoughtspotWorksheet) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ThoughtspotWorksheet, without replacing all existing terms linked to the ThoughtspotWorksheet.
     * Note: this operation must make two API calls — one to retrieve the ThoughtspotWorksheet's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the ThoughtspotWorksheet
     * @param qualifiedName for the ThoughtspotWorksheet
     * @param terms the list of terms to remove from the ThoughtspotWorksheet, which must be referenced by GUID
     * @return the ThoughtspotWorksheet that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static ThoughtspotWorksheet removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ThoughtspotWorksheet) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a ThoughtspotWorksheet, without replacing existing Atlan tags linked to the ThoughtspotWorksheet.
     * Note: this operation must make two API calls — one to retrieve the ThoughtspotWorksheet's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ThoughtspotWorksheet
     * @param qualifiedName of the ThoughtspotWorksheet
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ThoughtspotWorksheet
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static ThoughtspotWorksheet appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (ThoughtspotWorksheet) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ThoughtspotWorksheet, without replacing existing Atlan tags linked to the ThoughtspotWorksheet.
     * Note: this operation must make two API calls — one to retrieve the ThoughtspotWorksheet's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ThoughtspotWorksheet
     * @param qualifiedName of the ThoughtspotWorksheet
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ThoughtspotWorksheet
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static ThoughtspotWorksheet appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (ThoughtspotWorksheet) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a ThoughtspotWorksheet.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a ThoughtspotWorksheet
     * @param qualifiedName of the ThoughtspotWorksheet
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ThoughtspotWorksheet
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
