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
 * Execution of a single orchestrate-able unit of work.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class FlowControlOperation extends Asset implements IFlowControlOperation, IFlow, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "FlowControlOperation";

    /** Fixed typeName for FlowControlOperations. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Individual data flows (processes) orchestrated by this control operation. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> flowDataResults;

    /** Optional error message of the flow run. */
    @Attribute
    String flowErrorMessage;

    /** Date and time at which this point in the data processing or orchestration finished. */
    @Attribute
    @Date
    Long flowFinishedAt;

    /** Simple name of the folder in which this asset is contained. */
    @Attribute
    String flowFolderName;

    /** Unique name of the folder in which this asset is contained. */
    @Attribute
    String flowFolderQualifiedName;

    /** Unique ID for this flow asset, which will remain constant throughout the lifecycle of the asset. */
    @Attribute
    String flowId;

    /** Simple name of the project in which this asset is contained. */
    @Attribute
    String flowProjectName;

    /** Unique name of the project in which this asset is contained. */
    @Attribute
    String flowProjectQualifiedName;

    /** Simple name of the reusable grouping of operations in which this ephemeral data is contained. */
    @Attribute
    String flowReusableUnitName;

    /** Unique name of the reusable grouping of operations in which this ephemeral data is contained. */
    @Attribute
    String flowReusableUnitQualifiedName;

    /** Unique ID of the flow run, which could change on subsequent runs of the same flow. */
    @Attribute
    String flowRunId;

    /** Schedule for this point in the data processing or orchestration. */
    @Attribute
    String flowSchedule;

    /** Date and time at which this point in the data processing or orchestration started. */
    @Attribute
    @Date
    Long flowStartedAt;

    /** Overall status of this point in the data processing or orchestration. */
    @Attribute
    String flowStatus;

    /**
     * Builds the minimal object necessary to create a relationship to a FlowControlOperation, from a potentially
     * more-complete FlowControlOperation object.
     *
     * @return the minimal object necessary to relate to the FlowControlOperation
     * @throws InvalidRequestException if any of the minimal set of required properties for a FlowControlOperation relationship are not found in the initial object
     */
    @Override
    public FlowControlOperation trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all FlowControlOperation assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) FlowControlOperation assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all FlowControlOperation assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all FlowControlOperation assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) FlowControlOperations will be included
     * @return a fluent search that includes all FlowControlOperation assets
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
     * Reference to a FlowControlOperation by GUID. Use this to create a relationship to this FlowControlOperation,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the FlowControlOperation to reference
     * @return reference to a FlowControlOperation that can be used for defining a relationship to a FlowControlOperation
     */
    public static FlowControlOperation refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a FlowControlOperation by GUID. Use this to create a relationship to this FlowControlOperation,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the FlowControlOperation to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a FlowControlOperation that can be used for defining a relationship to a FlowControlOperation
     */
    public static FlowControlOperation refByGuid(String guid, Reference.SaveSemantic semantic) {
        return FlowControlOperation._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a FlowControlOperation by qualifiedName. Use this to create a relationship to this FlowControlOperation,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the FlowControlOperation to reference
     * @return reference to a FlowControlOperation that can be used for defining a relationship to a FlowControlOperation
     */
    public static FlowControlOperation refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a FlowControlOperation by qualifiedName. Use this to create a relationship to this FlowControlOperation,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the FlowControlOperation to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a FlowControlOperation that can be used for defining a relationship to a FlowControlOperation
     */
    public static FlowControlOperation refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return FlowControlOperation._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a FlowControlOperation by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the FlowControlOperation to retrieve, either its GUID or its full qualifiedName
     * @return the requested full FlowControlOperation, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the FlowControlOperation does not exist or the provided GUID is not a FlowControlOperation
     */
    @JsonIgnore
    public static FlowControlOperation get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a FlowControlOperation by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the FlowControlOperation to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full FlowControlOperation, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the FlowControlOperation does not exist or the provided GUID is not a FlowControlOperation
     */
    @JsonIgnore
    public static FlowControlOperation get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof FlowControlOperation) {
                return (FlowControlOperation) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof FlowControlOperation) {
                return (FlowControlOperation) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a FlowControlOperation by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the FlowControlOperation to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the FlowControlOperation, including any relationships
     * @return the requested FlowControlOperation, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the FlowControlOperation does not exist or the provided GUID is not a FlowControlOperation
     */
    @JsonIgnore
    public static FlowControlOperation get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a FlowControlOperation by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the FlowControlOperation to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the FlowControlOperation, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the FlowControlOperation
     * @return the requested FlowControlOperation, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the FlowControlOperation does not exist or the provided GUID is not a FlowControlOperation
     */
    @JsonIgnore
    public static FlowControlOperation get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = FlowControlOperation.select(client)
                    .where(FlowControlOperation.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof FlowControlOperation) {
                return (FlowControlOperation) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = FlowControlOperation.select(client)
                    .where(FlowControlOperation.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof FlowControlOperation) {
                return (FlowControlOperation) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) FlowControlOperation to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the FlowControlOperation
     * @return true if the FlowControlOperation is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a FlowControlOperation.
     *
     * @param qualifiedName of the FlowControlOperation
     * @param name of the FlowControlOperation
     * @return the minimal request necessary to update the FlowControlOperation, as a builder
     */
    public static FlowControlOperationBuilder<?, ?> updater(String qualifiedName, String name) {
        return FlowControlOperation._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a FlowControlOperation, from a potentially
     * more-complete FlowControlOperation object.
     *
     * @return the minimal object necessary to update the FlowControlOperation, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for FlowControlOperation are not found in the initial object
     */
    @Override
    public FlowControlOperationBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class FlowControlOperationBuilder<
                    C extends FlowControlOperation, B extends FlowControlOperationBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a FlowControlOperation.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the FlowControlOperation
     * @param name of the FlowControlOperation
     * @return the updated FlowControlOperation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static FlowControlOperation removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (FlowControlOperation) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a FlowControlOperation.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the FlowControlOperation
     * @param name of the FlowControlOperation
     * @return the updated FlowControlOperation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static FlowControlOperation removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (FlowControlOperation) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a FlowControlOperation.
     *
     * @param client connectivity to the Atlan tenant from which to remove the FlowControlOperation's owners
     * @param qualifiedName of the FlowControlOperation
     * @param name of the FlowControlOperation
     * @return the updated FlowControlOperation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static FlowControlOperation removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (FlowControlOperation) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a FlowControlOperation.
     *
     * @param client connectivity to the Atlan tenant on which to update the FlowControlOperation's certificate
     * @param qualifiedName of the FlowControlOperation
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated FlowControlOperation, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static FlowControlOperation updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (FlowControlOperation)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a FlowControlOperation.
     *
     * @param client connectivity to the Atlan tenant from which to remove the FlowControlOperation's certificate
     * @param qualifiedName of the FlowControlOperation
     * @param name of the FlowControlOperation
     * @return the updated FlowControlOperation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static FlowControlOperation removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (FlowControlOperation) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a FlowControlOperation.
     *
     * @param client connectivity to the Atlan tenant on which to update the FlowControlOperation's announcement
     * @param qualifiedName of the FlowControlOperation
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static FlowControlOperation updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (FlowControlOperation)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a FlowControlOperation.
     *
     * @param client connectivity to the Atlan client from which to remove the FlowControlOperation's announcement
     * @param qualifiedName of the FlowControlOperation
     * @param name of the FlowControlOperation
     * @return the updated FlowControlOperation, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static FlowControlOperation removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (FlowControlOperation) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the FlowControlOperation.
     *
     * @param client connectivity to the Atlan tenant on which to replace the FlowControlOperation's assigned terms
     * @param qualifiedName for the FlowControlOperation
     * @param name human-readable name of the FlowControlOperation
     * @param terms the list of terms to replace on the FlowControlOperation, or null to remove all terms from the FlowControlOperation
     * @return the FlowControlOperation that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static FlowControlOperation replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (FlowControlOperation) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the FlowControlOperation, without replacing existing terms linked to the FlowControlOperation.
     * Note: this operation must make two API calls — one to retrieve the FlowControlOperation's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the FlowControlOperation
     * @param qualifiedName for the FlowControlOperation
     * @param terms the list of terms to append to the FlowControlOperation
     * @return the FlowControlOperation that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static FlowControlOperation appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (FlowControlOperation) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a FlowControlOperation, without replacing all existing terms linked to the FlowControlOperation.
     * Note: this operation must make two API calls — one to retrieve the FlowControlOperation's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the FlowControlOperation
     * @param qualifiedName for the FlowControlOperation
     * @param terms the list of terms to remove from the FlowControlOperation, which must be referenced by GUID
     * @return the FlowControlOperation that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static FlowControlOperation removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (FlowControlOperation) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a FlowControlOperation, without replacing existing Atlan tags linked to the FlowControlOperation.
     * Note: this operation must make two API calls — one to retrieve the FlowControlOperation's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the FlowControlOperation
     * @param qualifiedName of the FlowControlOperation
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated FlowControlOperation
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static FlowControlOperation appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (FlowControlOperation) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a FlowControlOperation, without replacing existing Atlan tags linked to the FlowControlOperation.
     * Note: this operation must make two API calls — one to retrieve the FlowControlOperation's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the FlowControlOperation
     * @param qualifiedName of the FlowControlOperation
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated FlowControlOperation
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static FlowControlOperation appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (FlowControlOperation) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a FlowControlOperation.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a FlowControlOperation
     * @param qualifiedName of the FlowControlOperation
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the FlowControlOperation
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
