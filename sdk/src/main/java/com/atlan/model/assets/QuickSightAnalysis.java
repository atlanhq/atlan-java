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
import com.atlan.model.enums.QuickSightAnalysisStatus;
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
 * Instance of a QuickSight analysis in Atlan. In QuickSight, you analyze and visualize your data in analyses, which can be published as a dashboard to share with others.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class QuickSightAnalysis extends Asset
        implements IQuickSightAnalysis, IQuickSight, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "QuickSightAnalysis";

    /** Fixed typeName for QuickSightAnalysiss. */
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

    /** Partial fields contained in the asset. */
    @Attribute
    @Singular
    SortedSet<IPartialField> partialChildFields;

    /** Partial objects contained in the asset. */
    @Attribute
    @Singular
    SortedSet<IPartialObject> partialChildObjects;

    /** List of field names calculated by this analysis. */
    @Attribute
    @Singular
    SortedSet<String> quickSightAnalysisCalculatedFields;

    /** List of filter groups used for this analysis. */
    @Attribute
    @Singular
    SortedSet<String> quickSightAnalysisFilterGroups;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IQuickSightFolder> quickSightAnalysisFolders;

    /** List of parameters used for this analysis. */
    @Attribute
    @Singular
    SortedSet<String> quickSightAnalysisParameterDeclarations;

    /** Status of this analysis, for example: CREATION_IN_PROGRESS, UPDATE_SUCCESSFUL, etc. */
    @Attribute
    QuickSightAnalysisStatus quickSightAnalysisStatus;

    /** Visuals that exist within this analysis. */
    @Attribute
    @Singular
    SortedSet<IQuickSightAnalysisVisual> quickSightAnalysisVisuals;

    /** Unique identifier for the QuickSight asset. */
    @Attribute
    String quickSightId;

    /** Unique identifier for the QuickSight sheet. */
    @Attribute
    String quickSightSheetId;

    /** Name of the QuickSight sheet. */
    @Attribute
    String quickSightSheetName;

    /**
     * Builds the minimal object necessary to create a relationship to a QuickSightAnalysis, from a potentially
     * more-complete QuickSightAnalysis object.
     *
     * @return the minimal object necessary to relate to the QuickSightAnalysis
     * @throws InvalidRequestException if any of the minimal set of required properties for a QuickSightAnalysis relationship are not found in the initial object
     */
    @Override
    public QuickSightAnalysis trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all QuickSightAnalysis assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) QuickSightAnalysis assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all QuickSightAnalysis assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all QuickSightAnalysis assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) QuickSightAnalysiss will be included
     * @return a fluent search that includes all QuickSightAnalysis assets
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
     * Reference to a QuickSightAnalysis by GUID. Use this to create a relationship to this QuickSightAnalysis,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the QuickSightAnalysis to reference
     * @return reference to a QuickSightAnalysis that can be used for defining a relationship to a QuickSightAnalysis
     */
    public static QuickSightAnalysis refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a QuickSightAnalysis by GUID. Use this to create a relationship to this QuickSightAnalysis,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the QuickSightAnalysis to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a QuickSightAnalysis that can be used for defining a relationship to a QuickSightAnalysis
     */
    public static QuickSightAnalysis refByGuid(String guid, Reference.SaveSemantic semantic) {
        return QuickSightAnalysis._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a QuickSightAnalysis by qualifiedName. Use this to create a relationship to this QuickSightAnalysis,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the QuickSightAnalysis to reference
     * @return reference to a QuickSightAnalysis that can be used for defining a relationship to a QuickSightAnalysis
     */
    public static QuickSightAnalysis refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a QuickSightAnalysis by qualifiedName. Use this to create a relationship to this QuickSightAnalysis,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the QuickSightAnalysis to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a QuickSightAnalysis that can be used for defining a relationship to a QuickSightAnalysis
     */
    public static QuickSightAnalysis refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return QuickSightAnalysis._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a QuickSightAnalysis by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the QuickSightAnalysis to retrieve, either its GUID or its full qualifiedName
     * @return the requested full QuickSightAnalysis, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightAnalysis does not exist or the provided GUID is not a QuickSightAnalysis
     */
    @JsonIgnore
    public static QuickSightAnalysis get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a QuickSightAnalysis by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the QuickSightAnalysis to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full QuickSightAnalysis, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightAnalysis does not exist or the provided GUID is not a QuickSightAnalysis
     */
    @JsonIgnore
    public static QuickSightAnalysis get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof QuickSightAnalysis) {
                return (QuickSightAnalysis) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof QuickSightAnalysis) {
                return (QuickSightAnalysis) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a QuickSightAnalysis by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the QuickSightAnalysis to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the QuickSightAnalysis, including any relationships
     * @return the requested QuickSightAnalysis, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightAnalysis does not exist or the provided GUID is not a QuickSightAnalysis
     */
    @JsonIgnore
    public static QuickSightAnalysis get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a QuickSightAnalysis by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the QuickSightAnalysis to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the QuickSightAnalysis, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the QuickSightAnalysis
     * @return the requested QuickSightAnalysis, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightAnalysis does not exist or the provided GUID is not a QuickSightAnalysis
     */
    @JsonIgnore
    public static QuickSightAnalysis get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = QuickSightAnalysis.select(client)
                    .where(QuickSightAnalysis.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof QuickSightAnalysis) {
                return (QuickSightAnalysis) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = QuickSightAnalysis.select(client)
                    .where(QuickSightAnalysis.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof QuickSightAnalysis) {
                return (QuickSightAnalysis) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) QuickSightAnalysis to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the QuickSightAnalysis
     * @return true if the QuickSightAnalysis is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a QuickSightAnalysis.
     *
     * @param qualifiedName of the QuickSightAnalysis
     * @param name of the QuickSightAnalysis
     * @return the minimal request necessary to update the QuickSightAnalysis, as a builder
     */
    public static QuickSightAnalysisBuilder<?, ?> updater(String qualifiedName, String name) {
        return QuickSightAnalysis._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a QuickSightAnalysis, from a potentially
     * more-complete QuickSightAnalysis object.
     *
     * @return the minimal object necessary to update the QuickSightAnalysis, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for QuickSightAnalysis are not found in the initial object
     */
    @Override
    public QuickSightAnalysisBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class QuickSightAnalysisBuilder<
                    C extends QuickSightAnalysis, B extends QuickSightAnalysisBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a QuickSightAnalysis.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the QuickSightAnalysis
     * @param name of the QuickSightAnalysis
     * @return the updated QuickSightAnalysis, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysis removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightAnalysis) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a QuickSightAnalysis.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the QuickSightAnalysis
     * @param name of the QuickSightAnalysis
     * @return the updated QuickSightAnalysis, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysis removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightAnalysis) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a QuickSightAnalysis.
     *
     * @param client connectivity to the Atlan tenant from which to remove the QuickSightAnalysis's owners
     * @param qualifiedName of the QuickSightAnalysis
     * @param name of the QuickSightAnalysis
     * @return the updated QuickSightAnalysis, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysis removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightAnalysis) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a QuickSightAnalysis.
     *
     * @param client connectivity to the Atlan tenant on which to update the QuickSightAnalysis's certificate
     * @param qualifiedName of the QuickSightAnalysis
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QuickSightAnalysis, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysis updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (QuickSightAnalysis)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a QuickSightAnalysis.
     *
     * @param client connectivity to the Atlan tenant from which to remove the QuickSightAnalysis's certificate
     * @param qualifiedName of the QuickSightAnalysis
     * @param name of the QuickSightAnalysis
     * @return the updated QuickSightAnalysis, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysis removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightAnalysis) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a QuickSightAnalysis.
     *
     * @param client connectivity to the Atlan tenant on which to update the QuickSightAnalysis's announcement
     * @param qualifiedName of the QuickSightAnalysis
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysis updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (QuickSightAnalysis)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a QuickSightAnalysis.
     *
     * @param client connectivity to the Atlan client from which to remove the QuickSightAnalysis's announcement
     * @param qualifiedName of the QuickSightAnalysis
     * @param name of the QuickSightAnalysis
     * @return the updated QuickSightAnalysis, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysis removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightAnalysis) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the QuickSightAnalysis.
     *
     * @param client connectivity to the Atlan tenant on which to replace the QuickSightAnalysis's assigned terms
     * @param qualifiedName for the QuickSightAnalysis
     * @param name human-readable name of the QuickSightAnalysis
     * @param terms the list of terms to replace on the QuickSightAnalysis, or null to remove all terms from the QuickSightAnalysis
     * @return the QuickSightAnalysis that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysis replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (QuickSightAnalysis) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the QuickSightAnalysis, without replacing existing terms linked to the QuickSightAnalysis.
     * Note: this operation must make two API calls — one to retrieve the QuickSightAnalysis's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the QuickSightAnalysis
     * @param qualifiedName for the QuickSightAnalysis
     * @param terms the list of terms to append to the QuickSightAnalysis
     * @return the QuickSightAnalysis that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static QuickSightAnalysis appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (QuickSightAnalysis) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a QuickSightAnalysis, without replacing all existing terms linked to the QuickSightAnalysis.
     * Note: this operation must make two API calls — one to retrieve the QuickSightAnalysis's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the QuickSightAnalysis
     * @param qualifiedName for the QuickSightAnalysis
     * @param terms the list of terms to remove from the QuickSightAnalysis, which must be referenced by GUID
     * @return the QuickSightAnalysis that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static QuickSightAnalysis removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (QuickSightAnalysis) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a QuickSightAnalysis, without replacing existing Atlan tags linked to the QuickSightAnalysis.
     * Note: this operation must make two API calls — one to retrieve the QuickSightAnalysis's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the QuickSightAnalysis
     * @param qualifiedName of the QuickSightAnalysis
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated QuickSightAnalysis
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static QuickSightAnalysis appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (QuickSightAnalysis) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QuickSightAnalysis, without replacing existing Atlan tags linked to the QuickSightAnalysis.
     * Note: this operation must make two API calls — one to retrieve the QuickSightAnalysis's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the QuickSightAnalysis
     * @param qualifiedName of the QuickSightAnalysis
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated QuickSightAnalysis
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static QuickSightAnalysis appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (QuickSightAnalysis) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a QuickSightAnalysis.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a QuickSightAnalysis
     * @param qualifiedName of the QuickSightAnalysis
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the QuickSightAnalysis
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
