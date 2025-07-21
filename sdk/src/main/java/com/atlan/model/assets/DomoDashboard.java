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
 * Instance of a Domo Dashboard in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class DomoDashboard extends Asset implements IDomoDashboard, IDomo, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DomoDashboard";

    /** Fixed typeName for DomoDashboards. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Domo Cards that associate with this Domo Dashboard. */
    @Attribute
    @Singular
    SortedSet<IDomoCard> domoCards;

    /** Number of cards linked to this dashboard. */
    @Attribute
    Long domoDashboardCardCount;

    /** Child Domo Dashboards that are contained by this parent Domo Dashboard. */
    @Attribute
    @Singular
    SortedSet<IDomoDashboard> domoDashboardChildren;

    /** Parent Domo Dashboard that contains this child Domo Dashboard. */
    @Attribute
    IDomoDashboard domoDashboardParent;

    /** Id of the Domo dataset. */
    @Attribute
    String domoId;

    /** Id of the owner of the Domo dataset. */
    @Attribute
    String domoOwnerId;

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

    /**
     * Builds the minimal object necessary to create a relationship to a DomoDashboard, from a potentially
     * more-complete DomoDashboard object.
     *
     * @return the minimal object necessary to relate to the DomoDashboard
     * @throws InvalidRequestException if any of the minimal set of required properties for a DomoDashboard relationship are not found in the initial object
     */
    @Override
    public DomoDashboard trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all DomoDashboard assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DomoDashboard assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all DomoDashboard assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all DomoDashboard assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DomoDashboards will be included
     * @return a fluent search that includes all DomoDashboard assets
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
     * Reference to a DomoDashboard by GUID. Use this to create a relationship to this DomoDashboard,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the DomoDashboard to reference
     * @return reference to a DomoDashboard that can be used for defining a relationship to a DomoDashboard
     */
    public static DomoDashboard refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DomoDashboard by GUID. Use this to create a relationship to this DomoDashboard,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the DomoDashboard to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DomoDashboard that can be used for defining a relationship to a DomoDashboard
     */
    public static DomoDashboard refByGuid(String guid, Reference.SaveSemantic semantic) {
        return DomoDashboard._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a DomoDashboard by qualifiedName. Use this to create a relationship to this DomoDashboard,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the DomoDashboard to reference
     * @return reference to a DomoDashboard that can be used for defining a relationship to a DomoDashboard
     */
    public static DomoDashboard refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DomoDashboard by qualifiedName. Use this to create a relationship to this DomoDashboard,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the DomoDashboard to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DomoDashboard that can be used for defining a relationship to a DomoDashboard
     */
    public static DomoDashboard refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return DomoDashboard._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a DomoDashboard by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DomoDashboard to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DomoDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DomoDashboard does not exist or the provided GUID is not a DomoDashboard
     */
    @JsonIgnore
    public static DomoDashboard get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a DomoDashboard by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DomoDashboard to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full DomoDashboard, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DomoDashboard does not exist or the provided GUID is not a DomoDashboard
     */
    @JsonIgnore
    public static DomoDashboard get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof DomoDashboard) {
                return (DomoDashboard) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof DomoDashboard) {
                return (DomoDashboard) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a DomoDashboard by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DomoDashboard to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DomoDashboard, including any relationships
     * @return the requested DomoDashboard, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DomoDashboard does not exist or the provided GUID is not a DomoDashboard
     */
    @JsonIgnore
    public static DomoDashboard get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a DomoDashboard by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DomoDashboard to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DomoDashboard, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the DomoDashboard
     * @return the requested DomoDashboard, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DomoDashboard does not exist or the provided GUID is not a DomoDashboard
     */
    @JsonIgnore
    public static DomoDashboard get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = DomoDashboard.select(client)
                    .where(DomoDashboard.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof DomoDashboard) {
                return (DomoDashboard) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = DomoDashboard.select(client)
                    .where(DomoDashboard.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof DomoDashboard) {
                return (DomoDashboard) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) DomoDashboard to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DomoDashboard
     * @return true if the DomoDashboard is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a DomoDashboard.
     *
     * @param qualifiedName of the DomoDashboard
     * @param name of the DomoDashboard
     * @return the minimal request necessary to update the DomoDashboard, as a builder
     */
    public static DomoDashboardBuilder<?, ?> updater(String qualifiedName, String name) {
        return DomoDashboard._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DomoDashboard, from a potentially
     * more-complete DomoDashboard object.
     *
     * @return the minimal object necessary to update the DomoDashboard, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DomoDashboard are not found in the initial object
     */
    @Override
    public DomoDashboardBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class DomoDashboardBuilder<C extends DomoDashboard, B extends DomoDashboardBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a DomoDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DomoDashboard
     * @param name of the DomoDashboard
     * @return the updated DomoDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DomoDashboard removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DomoDashboard) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DomoDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DomoDashboard
     * @param name of the DomoDashboard
     * @return the updated DomoDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DomoDashboard removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DomoDashboard) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DomoDashboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DomoDashboard's owners
     * @param qualifiedName of the DomoDashboard
     * @param name of the DomoDashboard
     * @return the updated DomoDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DomoDashboard removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DomoDashboard) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DomoDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to update the DomoDashboard's certificate
     * @param qualifiedName of the DomoDashboard
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DomoDashboard, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DomoDashboard updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DomoDashboard)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DomoDashboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DomoDashboard's certificate
     * @param qualifiedName of the DomoDashboard
     * @param name of the DomoDashboard
     * @return the updated DomoDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DomoDashboard removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DomoDashboard) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DomoDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to update the DomoDashboard's announcement
     * @param qualifiedName of the DomoDashboard
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DomoDashboard updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (DomoDashboard)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DomoDashboard.
     *
     * @param client connectivity to the Atlan client from which to remove the DomoDashboard's announcement
     * @param qualifiedName of the DomoDashboard
     * @param name of the DomoDashboard
     * @return the updated DomoDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DomoDashboard removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DomoDashboard) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DomoDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DomoDashboard's assigned terms
     * @param qualifiedName for the DomoDashboard
     * @param name human-readable name of the DomoDashboard
     * @param terms the list of terms to replace on the DomoDashboard, or null to remove all terms from the DomoDashboard
     * @return the DomoDashboard that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DomoDashboard replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (DomoDashboard) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DomoDashboard, without replacing existing terms linked to the DomoDashboard.
     * Note: this operation must make two API calls — one to retrieve the DomoDashboard's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DomoDashboard
     * @param qualifiedName for the DomoDashboard
     * @param terms the list of terms to append to the DomoDashboard
     * @return the DomoDashboard that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DomoDashboard appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DomoDashboard) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DomoDashboard, without replacing all existing terms linked to the DomoDashboard.
     * Note: this operation must make two API calls — one to retrieve the DomoDashboard's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DomoDashboard
     * @param qualifiedName for the DomoDashboard
     * @param terms the list of terms to remove from the DomoDashboard, which must be referenced by GUID
     * @return the DomoDashboard that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DomoDashboard removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DomoDashboard) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DomoDashboard, without replacing existing Atlan tags linked to the DomoDashboard.
     * Note: this operation must make two API calls — one to retrieve the DomoDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DomoDashboard
     * @param qualifiedName of the DomoDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DomoDashboard
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static DomoDashboard appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (DomoDashboard) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DomoDashboard, without replacing existing Atlan tags linked to the DomoDashboard.
     * Note: this operation must make two API calls — one to retrieve the DomoDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DomoDashboard
     * @param qualifiedName of the DomoDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DomoDashboard
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static DomoDashboard appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DomoDashboard) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a DomoDashboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DomoDashboard
     * @param qualifiedName of the DomoDashboard
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DomoDashboard
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
