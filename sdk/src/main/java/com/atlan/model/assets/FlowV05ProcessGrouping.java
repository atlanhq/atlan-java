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
 * A grouping of data flows that will be orchestrated together as a single unit.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class FlowV05ProcessGrouping extends Asset implements IFlowV05ProcessGrouping, IFlowV05, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "FlowV05ProcessGrouping";

    /** Fixed typeName for FlowV05ProcessGroupings. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Ephemeral datasets that abstract the sub-processing carried out by the process grouping. */
    @Attribute
    @Singular
    SortedSet<IFlowV05Dataset> flowV05Abstracts;

    /** Individual data flows (processes) contained in this grouping. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> flowV05DataFlows;

    /** Optional error message of the flow run. */
    @Attribute
    String flowV05ErrorMessage;

    /** Control operations that execute this process grouping. */
    @Attribute
    @Singular
    SortedSet<IFlowV05ControlOperation> flowV05ExecutedByControls;

    /** Date and time at which this point in the data processing or orchestration finished. */
    @Attribute
    @Date
    Long flowV05FinishedAt;

    /** Simple name of the folder in which this asset is contained. */
    @Attribute
    String flowV05FolderName;

    /** Unique name of the folder in which this asset is contained. */
    @Attribute
    String flowV05FolderQualifiedName;

    /** Unique ID for this flow asset, which will remain constant throughout the lifecycle of the asset. */
    @Attribute
    String flowV05Id;

    /** Simple name of the project in which this asset is contained. */
    @Attribute
    String flowV05ProjectName;

    /** Unique name of the project in which this asset is contained. */
    @Attribute
    String flowV05ProjectQualifiedName;

    /** Unique ID of the flow run, which could change on subsequent runs of the same flow. */
    @Attribute
    String flowV05RunId;

    /** Schedule for this point in the data processing or orchestration. */
    @Attribute
    String flowV05Schedule;

    /** Date and time at which this point in the data processing or orchestration started. */
    @Attribute
    @Date
    Long flowV05StartedAt;

    /** Overall status of this point in the data processing or orchestration. */
    @Attribute
    String flowV05Status;

    /**
     * Builds the minimal object necessary to create a relationship to a FlowV05ProcessGrouping, from a potentially
     * more-complete FlowV05ProcessGrouping object.
     *
     * @return the minimal object necessary to relate to the FlowV05ProcessGrouping
     * @throws InvalidRequestException if any of the minimal set of required properties for a FlowV05ProcessGrouping relationship are not found in the initial object
     */
    @Override
    public FlowV05ProcessGrouping trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all FlowV05ProcessGrouping assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) FlowV05ProcessGrouping assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all FlowV05ProcessGrouping assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all FlowV05ProcessGrouping assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) FlowV05ProcessGroupings will be included
     * @return a fluent search that includes all FlowV05ProcessGrouping assets
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
     * Reference to a FlowV05ProcessGrouping by GUID. Use this to create a relationship to this FlowV05ProcessGrouping,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the FlowV05ProcessGrouping to reference
     * @return reference to a FlowV05ProcessGrouping that can be used for defining a relationship to a FlowV05ProcessGrouping
     */
    public static FlowV05ProcessGrouping refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a FlowV05ProcessGrouping by GUID. Use this to create a relationship to this FlowV05ProcessGrouping,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the FlowV05ProcessGrouping to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a FlowV05ProcessGrouping that can be used for defining a relationship to a FlowV05ProcessGrouping
     */
    public static FlowV05ProcessGrouping refByGuid(String guid, Reference.SaveSemantic semantic) {
        return FlowV05ProcessGrouping._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a FlowV05ProcessGrouping by qualifiedName. Use this to create a relationship to this FlowV05ProcessGrouping,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the FlowV05ProcessGrouping to reference
     * @return reference to a FlowV05ProcessGrouping that can be used for defining a relationship to a FlowV05ProcessGrouping
     */
    public static FlowV05ProcessGrouping refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a FlowV05ProcessGrouping by qualifiedName. Use this to create a relationship to this FlowV05ProcessGrouping,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the FlowV05ProcessGrouping to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a FlowV05ProcessGrouping that can be used for defining a relationship to a FlowV05ProcessGrouping
     */
    public static FlowV05ProcessGrouping refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return FlowV05ProcessGrouping._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a FlowV05ProcessGrouping by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the FlowV05ProcessGrouping to retrieve, either its GUID or its full qualifiedName
     * @return the requested full FlowV05ProcessGrouping, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the FlowV05ProcessGrouping does not exist or the provided GUID is not a FlowV05ProcessGrouping
     */
    @JsonIgnore
    public static FlowV05ProcessGrouping get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a FlowV05ProcessGrouping by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the FlowV05ProcessGrouping to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full FlowV05ProcessGrouping, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the FlowV05ProcessGrouping does not exist or the provided GUID is not a FlowV05ProcessGrouping
     */
    @JsonIgnore
    public static FlowV05ProcessGrouping get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof FlowV05ProcessGrouping) {
                return (FlowV05ProcessGrouping) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof FlowV05ProcessGrouping) {
                return (FlowV05ProcessGrouping) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a FlowV05ProcessGrouping by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the FlowV05ProcessGrouping to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the FlowV05ProcessGrouping, including any relationships
     * @return the requested FlowV05ProcessGrouping, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the FlowV05ProcessGrouping does not exist or the provided GUID is not a FlowV05ProcessGrouping
     */
    @JsonIgnore
    public static FlowV05ProcessGrouping get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a FlowV05ProcessGrouping by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the FlowV05ProcessGrouping to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the FlowV05ProcessGrouping, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the FlowV05ProcessGrouping
     * @return the requested FlowV05ProcessGrouping, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the FlowV05ProcessGrouping does not exist or the provided GUID is not a FlowV05ProcessGrouping
     */
    @JsonIgnore
    public static FlowV05ProcessGrouping get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = FlowV05ProcessGrouping.select(client)
                    .where(FlowV05ProcessGrouping.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof FlowV05ProcessGrouping) {
                return (FlowV05ProcessGrouping) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = FlowV05ProcessGrouping.select(client)
                    .where(FlowV05ProcessGrouping.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof FlowV05ProcessGrouping) {
                return (FlowV05ProcessGrouping) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) FlowV05ProcessGrouping to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the FlowV05ProcessGrouping
     * @return true if the FlowV05ProcessGrouping is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a FlowV05ProcessGrouping.
     *
     * @param qualifiedName of the FlowV05ProcessGrouping
     * @param name of the FlowV05ProcessGrouping
     * @return the minimal request necessary to update the FlowV05ProcessGrouping, as a builder
     */
    public static FlowV05ProcessGroupingBuilder<?, ?> updater(String qualifiedName, String name) {
        return FlowV05ProcessGrouping._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a FlowV05ProcessGrouping, from a potentially
     * more-complete FlowV05ProcessGrouping object.
     *
     * @return the minimal object necessary to update the FlowV05ProcessGrouping, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for FlowV05ProcessGrouping are not found in the initial object
     */
    @Override
    public FlowV05ProcessGroupingBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class FlowV05ProcessGroupingBuilder<
                    C extends FlowV05ProcessGrouping, B extends FlowV05ProcessGroupingBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a FlowV05ProcessGrouping.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the FlowV05ProcessGrouping
     * @param name of the FlowV05ProcessGrouping
     * @return the updated FlowV05ProcessGrouping, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static FlowV05ProcessGrouping removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (FlowV05ProcessGrouping) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a FlowV05ProcessGrouping.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the FlowV05ProcessGrouping
     * @param name of the FlowV05ProcessGrouping
     * @return the updated FlowV05ProcessGrouping, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static FlowV05ProcessGrouping removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (FlowV05ProcessGrouping) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a FlowV05ProcessGrouping.
     *
     * @param client connectivity to the Atlan tenant from which to remove the FlowV05ProcessGrouping's owners
     * @param qualifiedName of the FlowV05ProcessGrouping
     * @param name of the FlowV05ProcessGrouping
     * @return the updated FlowV05ProcessGrouping, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static FlowV05ProcessGrouping removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (FlowV05ProcessGrouping) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a FlowV05ProcessGrouping.
     *
     * @param client connectivity to the Atlan tenant on which to update the FlowV05ProcessGrouping's certificate
     * @param qualifiedName of the FlowV05ProcessGrouping
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated FlowV05ProcessGrouping, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static FlowV05ProcessGrouping updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (FlowV05ProcessGrouping)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a FlowV05ProcessGrouping.
     *
     * @param client connectivity to the Atlan tenant from which to remove the FlowV05ProcessGrouping's certificate
     * @param qualifiedName of the FlowV05ProcessGrouping
     * @param name of the FlowV05ProcessGrouping
     * @return the updated FlowV05ProcessGrouping, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static FlowV05ProcessGrouping removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (FlowV05ProcessGrouping) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a FlowV05ProcessGrouping.
     *
     * @param client connectivity to the Atlan tenant on which to update the FlowV05ProcessGrouping's announcement
     * @param qualifiedName of the FlowV05ProcessGrouping
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static FlowV05ProcessGrouping updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (FlowV05ProcessGrouping)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a FlowV05ProcessGrouping.
     *
     * @param client connectivity to the Atlan client from which to remove the FlowV05ProcessGrouping's announcement
     * @param qualifiedName of the FlowV05ProcessGrouping
     * @param name of the FlowV05ProcessGrouping
     * @return the updated FlowV05ProcessGrouping, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static FlowV05ProcessGrouping removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (FlowV05ProcessGrouping) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the FlowV05ProcessGrouping.
     *
     * @param client connectivity to the Atlan tenant on which to replace the FlowV05ProcessGrouping's assigned terms
     * @param qualifiedName for the FlowV05ProcessGrouping
     * @param name human-readable name of the FlowV05ProcessGrouping
     * @param terms the list of terms to replace on the FlowV05ProcessGrouping, or null to remove all terms from the FlowV05ProcessGrouping
     * @return the FlowV05ProcessGrouping that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static FlowV05ProcessGrouping replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (FlowV05ProcessGrouping) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the FlowV05ProcessGrouping, without replacing existing terms linked to the FlowV05ProcessGrouping.
     * Note: this operation must make two API calls — one to retrieve the FlowV05ProcessGrouping's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the FlowV05ProcessGrouping
     * @param qualifiedName for the FlowV05ProcessGrouping
     * @param terms the list of terms to append to the FlowV05ProcessGrouping
     * @return the FlowV05ProcessGrouping that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static FlowV05ProcessGrouping appendTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (FlowV05ProcessGrouping) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a FlowV05ProcessGrouping, without replacing all existing terms linked to the FlowV05ProcessGrouping.
     * Note: this operation must make two API calls — one to retrieve the FlowV05ProcessGrouping's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the FlowV05ProcessGrouping
     * @param qualifiedName for the FlowV05ProcessGrouping
     * @param terms the list of terms to remove from the FlowV05ProcessGrouping, which must be referenced by GUID
     * @return the FlowV05ProcessGrouping that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static FlowV05ProcessGrouping removeTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (FlowV05ProcessGrouping) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a FlowV05ProcessGrouping, without replacing existing Atlan tags linked to the FlowV05ProcessGrouping.
     * Note: this operation must make two API calls — one to retrieve the FlowV05ProcessGrouping's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the FlowV05ProcessGrouping
     * @param qualifiedName of the FlowV05ProcessGrouping
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated FlowV05ProcessGrouping
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static FlowV05ProcessGrouping appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (FlowV05ProcessGrouping) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a FlowV05ProcessGrouping, without replacing existing Atlan tags linked to the FlowV05ProcessGrouping.
     * Note: this operation must make two API calls — one to retrieve the FlowV05ProcessGrouping's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the FlowV05ProcessGrouping
     * @param qualifiedName of the FlowV05ProcessGrouping
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated FlowV05ProcessGrouping
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static FlowV05ProcessGrouping appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (FlowV05ProcessGrouping) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a FlowV05ProcessGrouping.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a FlowV05ProcessGrouping
     * @param qualifiedName of the FlowV05ProcessGrouping
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the FlowV05ProcessGrouping
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
