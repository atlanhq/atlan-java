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
 * Instance of a Sigma dataset in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class SigmaDataset extends Asset implements ISigmaDataset, ISigma, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SigmaDataset";

    /** Fixed typeName for SigmaDatasets. */
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

    /** Simple name of the data element in which this asset exists. */
    @Attribute
    String sigmaDataElementName;

    /** Unique name of the data element in which this asset exists. */
    @Attribute
    String sigmaDataElementQualifiedName;

    /** Number of columns in this dataset. */
    @Attribute
    Long sigmaDatasetColumnCount;

    /** Dataset columns that exist in this dataset. */
    @Attribute
    @Singular
    SortedSet<ISigmaDatasetColumn> sigmaDatasetColumns;

    /** Simple name of the page on which this asset exists. */
    @Attribute
    String sigmaPageName;

    /** Unique name of the page on which this asset exists. */
    @Attribute
    String sigmaPageQualifiedName;

    /** Simple name of the workbook in which this asset exists. */
    @Attribute
    String sigmaWorkbookName;

    /** Unique name of the workbook in which this asset exists. */
    @Attribute
    String sigmaWorkbookQualifiedName;

    /**
     * Builds the minimal object necessary to create a relationship to a SigmaDataset, from a potentially
     * more-complete SigmaDataset object.
     *
     * @return the minimal object necessary to relate to the SigmaDataset
     * @throws InvalidRequestException if any of the minimal set of required properties for a SigmaDataset relationship are not found in the initial object
     */
    @Override
    public SigmaDataset trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SigmaDataset assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SigmaDataset assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SigmaDataset assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SigmaDataset assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SigmaDatasets will be included
     * @return a fluent search that includes all SigmaDataset assets
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
     * Reference to a SigmaDataset by GUID. Use this to create a relationship to this SigmaDataset,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SigmaDataset to reference
     * @return reference to a SigmaDataset that can be used for defining a relationship to a SigmaDataset
     */
    public static SigmaDataset refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SigmaDataset by GUID. Use this to create a relationship to this SigmaDataset,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SigmaDataset to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SigmaDataset that can be used for defining a relationship to a SigmaDataset
     */
    public static SigmaDataset refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SigmaDataset._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SigmaDataset by qualifiedName. Use this to create a relationship to this SigmaDataset,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SigmaDataset to reference
     * @return reference to a SigmaDataset that can be used for defining a relationship to a SigmaDataset
     */
    public static SigmaDataset refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SigmaDataset by qualifiedName. Use this to create a relationship to this SigmaDataset,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SigmaDataset to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SigmaDataset that can be used for defining a relationship to a SigmaDataset
     */
    public static SigmaDataset refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SigmaDataset._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SigmaDataset by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SigmaDataset to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SigmaDataset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDataset does not exist or the provided GUID is not a SigmaDataset
     */
    @JsonIgnore
    public static SigmaDataset get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SigmaDataset by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SigmaDataset to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SigmaDataset, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDataset does not exist or the provided GUID is not a SigmaDataset
     */
    @JsonIgnore
    public static SigmaDataset get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SigmaDataset) {
                return (SigmaDataset) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof SigmaDataset) {
                return (SigmaDataset) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SigmaDataset by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SigmaDataset to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SigmaDataset, including any relationships
     * @return the requested SigmaDataset, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDataset does not exist or the provided GUID is not a SigmaDataset
     */
    @JsonIgnore
    public static SigmaDataset get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a SigmaDataset by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SigmaDataset to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SigmaDataset, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the SigmaDataset
     * @return the requested SigmaDataset, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDataset does not exist or the provided GUID is not a SigmaDataset
     */
    @JsonIgnore
    public static SigmaDataset get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = SigmaDataset.select(client)
                    .where(SigmaDataset.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof SigmaDataset) {
                return (SigmaDataset) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = SigmaDataset.select(client)
                    .where(SigmaDataset.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof SigmaDataset) {
                return (SigmaDataset) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SigmaDataset to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SigmaDataset
     * @return true if the SigmaDataset is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SigmaDataset.
     *
     * @param qualifiedName of the SigmaDataset
     * @param name of the SigmaDataset
     * @return the minimal request necessary to update the SigmaDataset, as a builder
     */
    public static SigmaDatasetBuilder<?, ?> updater(String qualifiedName, String name) {
        return SigmaDataset._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SigmaDataset, from a potentially
     * more-complete SigmaDataset object.
     *
     * @return the minimal object necessary to update the SigmaDataset, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SigmaDataset are not found in the initial object
     */
    @Override
    public SigmaDatasetBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a SigmaDataset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SigmaDataset
     * @param name of the SigmaDataset
     * @return the updated SigmaDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataset removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SigmaDataset) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SigmaDataset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SigmaDataset
     * @param name of the SigmaDataset
     * @return the updated SigmaDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataset removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SigmaDataset) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SigmaDataset.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SigmaDataset's owners
     * @param qualifiedName of the SigmaDataset
     * @param name of the SigmaDataset
     * @return the updated SigmaDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataset removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SigmaDataset) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SigmaDataset.
     *
     * @param client connectivity to the Atlan tenant on which to update the SigmaDataset's certificate
     * @param qualifiedName of the SigmaDataset
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SigmaDataset, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataset updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SigmaDataset)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SigmaDataset.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SigmaDataset's certificate
     * @param qualifiedName of the SigmaDataset
     * @param name of the SigmaDataset
     * @return the updated SigmaDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataset removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SigmaDataset) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SigmaDataset.
     *
     * @param client connectivity to the Atlan tenant on which to update the SigmaDataset's announcement
     * @param qualifiedName of the SigmaDataset
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataset updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SigmaDataset)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SigmaDataset.
     *
     * @param client connectivity to the Atlan client from which to remove the SigmaDataset's announcement
     * @param qualifiedName of the SigmaDataset
     * @param name of the SigmaDataset
     * @return the updated SigmaDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataset removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SigmaDataset) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SigmaDataset.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SigmaDataset's assigned terms
     * @param qualifiedName for the SigmaDataset
     * @param name human-readable name of the SigmaDataset
     * @param terms the list of terms to replace on the SigmaDataset, or null to remove all terms from the SigmaDataset
     * @return the SigmaDataset that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaDataset replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SigmaDataset) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SigmaDataset, without replacing existing terms linked to the SigmaDataset.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataset's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SigmaDataset
     * @param qualifiedName for the SigmaDataset
     * @param terms the list of terms to append to the SigmaDataset
     * @return the SigmaDataset that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SigmaDataset appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SigmaDataset) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SigmaDataset, without replacing all existing terms linked to the SigmaDataset.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataset's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SigmaDataset
     * @param qualifiedName for the SigmaDataset
     * @param terms the list of terms to remove from the SigmaDataset, which must be referenced by GUID
     * @return the SigmaDataset that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SigmaDataset removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SigmaDataset) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SigmaDataset, without replacing existing Atlan tags linked to the SigmaDataset.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SigmaDataset
     * @param qualifiedName of the SigmaDataset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SigmaDataset
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static SigmaDataset appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SigmaDataset) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SigmaDataset, without replacing existing Atlan tags linked to the SigmaDataset.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SigmaDataset
     * @param qualifiedName of the SigmaDataset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SigmaDataset
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static SigmaDataset appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SigmaDataset) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SigmaDataset.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SigmaDataset
     * @param qualifiedName of the SigmaDataset
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SigmaDataset
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
