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
import com.atlan.model.enums.NotificationState;
import com.atlan.model.enums.WorkflowNotificationAction;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.model.structs.NotificationExternalReference;
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
 * A notification about an Atlan app workflow. The only notification type created today, and the one every future workflow-related notification should use.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class WorkflowNotification extends Asset
        implements IWorkflowNotification, INotification, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "WorkflowNotification";

    /** Fixed typeName for WorkflowNotifications. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Where this notification also lives outside Atlan, one entry per surface. This is what lets an action taken in the inbox go back and update the message it came from. */
    @Attribute
    @Singular
    List<NotificationExternalReference> notificationExternalReferences;

    /** When a person last moved the state. Read against the creation timestamp this gives time to resolution. */
    @Attribute
    @Date
    Long notificationLastActedAt;

    /** Username of the person who last moved the state. Declared separately because the write is service-mediated, so the entity's own modifier records the service and not the person. */
    @Attribute
    String notificationLastActedBy;

    /** Reason or note the person gave for the last action. Mandatory for some actions and optional for others, so it is free text rather than a code. */
    @Attribute
    String notificationLastActedReason;

    /** Identifiers of the Atlan groups the notification is meant for. A group outlives its members, so a rule addressing a group stays generic as people join and leave. */
    @Attribute
    @Singular
    SortedSet<String> notificationRecipientGroups;

    /** Identifiers of the Atlan roles that need to see the notification. Resolved to individuals by the interface at read time rather than stored. */
    @Attribute
    @Singular
    SortedSet<String> notificationRecipientRoles;

    /** Usernames the notification was addressed to. Drives the assigned-to-me view together with the group and role recipients. */
    @Attribute
    @Singular
    SortedSet<String> notificationRecipientUsers;

    /** Where the notification is in its lifecycle. Also how a check decides whether a live notification already exists before raising another. */
    @Attribute
    NotificationState notificationState;

    /** Which option the person took. Declared here rather than on the notification supertype because these values only mean anything for a workflow. */
    @Attribute
    WorkflowNotificationAction workflowNotificationAction;

    /** When the check last failed. Distinct from when it was last checked, which every write already records. */
    @Attribute
    @Date
    Long workflowNotificationLastPreflightFailedAt;

    /** The whole check response as one escaped JSON string, rewritten on every check while the notification is live. Rendered by the widget the interface already has, so nothing is denormalised into attributes. */
    @Attribute
    String workflowNotificationLastPreflightResult;

    /** How many times the check has failed for this notification, counted from the failures that raised it and incremented on every failure since. */
    @Attribute
    Integer workflowNotificationPreflightFailureCount;

    /** The workflow this notification is about. */
    @Attribute
    IAtlanAppWorkflow workflowNotificationWorkflow;

    /** Slug of the workflow this notification is about. What grouping by workflow groups on, and how a check finds the live notification before deciding whether to raise another. */
    @Attribute
    String workflowNotificationWorkflowSlug;

    /**
     * Builds the minimal object necessary to create a relationship to a WorkflowNotification, from a potentially
     * more-complete WorkflowNotification object.
     *
     * @return the minimal object necessary to relate to the WorkflowNotification
     * @throws InvalidRequestException if any of the minimal set of required properties for a WorkflowNotification relationship are not found in the initial object
     */
    @Override
    public WorkflowNotification trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all WorkflowNotification assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) WorkflowNotification assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all WorkflowNotification assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all WorkflowNotification assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) WorkflowNotifications will be included
     * @return a fluent search that includes all WorkflowNotification assets
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
     * Reference to a WorkflowNotification by GUID. Use this to create a relationship to this WorkflowNotification,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the WorkflowNotification to reference
     * @return reference to a WorkflowNotification that can be used for defining a relationship to a WorkflowNotification
     */
    public static WorkflowNotification refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a WorkflowNotification by GUID. Use this to create a relationship to this WorkflowNotification,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the WorkflowNotification to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a WorkflowNotification that can be used for defining a relationship to a WorkflowNotification
     */
    public static WorkflowNotification refByGuid(String guid, Reference.SaveSemantic semantic) {
        return WorkflowNotification._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a WorkflowNotification by qualifiedName. Use this to create a relationship to this WorkflowNotification,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the WorkflowNotification to reference
     * @return reference to a WorkflowNotification that can be used for defining a relationship to a WorkflowNotification
     */
    public static WorkflowNotification refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a WorkflowNotification by qualifiedName. Use this to create a relationship to this WorkflowNotification,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the WorkflowNotification to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a WorkflowNotification that can be used for defining a relationship to a WorkflowNotification
     */
    public static WorkflowNotification refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return WorkflowNotification._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a WorkflowNotification by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the WorkflowNotification to retrieve, either its GUID or its full qualifiedName
     * @return the requested full WorkflowNotification, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the WorkflowNotification does not exist or the provided GUID is not a WorkflowNotification
     */
    @JsonIgnore
    public static WorkflowNotification get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a WorkflowNotification by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the WorkflowNotification to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full WorkflowNotification, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the WorkflowNotification does not exist or the provided GUID is not a WorkflowNotification
     */
    @JsonIgnore
    public static WorkflowNotification get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof WorkflowNotification) {
                return (WorkflowNotification) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof WorkflowNotification) {
                return (WorkflowNotification) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a WorkflowNotification by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the WorkflowNotification to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the WorkflowNotification, including any relationships
     * @return the requested WorkflowNotification, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the WorkflowNotification does not exist or the provided GUID is not a WorkflowNotification
     */
    @JsonIgnore
    public static WorkflowNotification get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a WorkflowNotification by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the WorkflowNotification to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the WorkflowNotification, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the WorkflowNotification
     * @return the requested WorkflowNotification, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the WorkflowNotification does not exist or the provided GUID is not a WorkflowNotification
     */
    @JsonIgnore
    public static WorkflowNotification get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = WorkflowNotification.select(client)
                    .where(WorkflowNotification.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof WorkflowNotification) {
                return (WorkflowNotification) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = WorkflowNotification.select(client)
                    .where(WorkflowNotification.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof WorkflowNotification) {
                return (WorkflowNotification) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) WorkflowNotification to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the WorkflowNotification
     * @return true if the WorkflowNotification is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a WorkflowNotification.
     *
     * @param qualifiedName of the WorkflowNotification
     * @param name of the WorkflowNotification
     * @return the minimal request necessary to update the WorkflowNotification, as a builder
     */
    public static WorkflowNotificationBuilder<?, ?> updater(String qualifiedName, String name) {
        return WorkflowNotification._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a WorkflowNotification,
     * from a potentially more-complete WorkflowNotification object.
     *
     * @return the minimal object necessary to update the WorkflowNotification, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a WorkflowNotification are not present in the initial object
     */
    @Override
    public WorkflowNotificationBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class WorkflowNotificationBuilder<
                    C extends WorkflowNotification, B extends WorkflowNotificationBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a WorkflowNotification.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the WorkflowNotification
     * @param name of the WorkflowNotification
     * @return the updated WorkflowNotification, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static WorkflowNotification removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (WorkflowNotification) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a WorkflowNotification.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the WorkflowNotification
     * @param name of the WorkflowNotification
     * @return the updated WorkflowNotification, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static WorkflowNotification removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (WorkflowNotification) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a WorkflowNotification.
     *
     * @param client connectivity to the Atlan tenant from which to remove the WorkflowNotification's owners
     * @param qualifiedName of the WorkflowNotification
     * @param name of the WorkflowNotification
     * @return the updated WorkflowNotification, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static WorkflowNotification removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (WorkflowNotification) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a WorkflowNotification.
     *
     * @param client connectivity to the Atlan tenant on which to update the WorkflowNotification's certificate
     * @param qualifiedName of the WorkflowNotification
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated WorkflowNotification, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static WorkflowNotification updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (WorkflowNotification)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a WorkflowNotification.
     *
     * @param client connectivity to the Atlan tenant from which to remove the WorkflowNotification's certificate
     * @param qualifiedName of the WorkflowNotification
     * @param name of the WorkflowNotification
     * @return the updated WorkflowNotification, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static WorkflowNotification removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (WorkflowNotification) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a WorkflowNotification.
     *
     * @param client connectivity to the Atlan tenant on which to update the WorkflowNotification's announcement
     * @param qualifiedName of the WorkflowNotification
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static WorkflowNotification updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (WorkflowNotification)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a WorkflowNotification.
     *
     * @param client connectivity to the Atlan client from which to remove the WorkflowNotification's announcement
     * @param qualifiedName of the WorkflowNotification
     * @param name of the WorkflowNotification
     * @return the updated WorkflowNotification, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static WorkflowNotification removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (WorkflowNotification) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the WorkflowNotification.
     *
     * @param client connectivity to the Atlan tenant on which to replace the WorkflowNotification's assigned terms
     * @param qualifiedName for the WorkflowNotification
     * @param name human-readable name of the WorkflowNotification
     * @param terms the list of terms to replace on the WorkflowNotification, or null to remove all terms from the WorkflowNotification
     * @return the WorkflowNotification that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static WorkflowNotification replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (WorkflowNotification) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the WorkflowNotification, without replacing existing terms linked to the WorkflowNotification.
     * Note: this operation must make two API calls — one to retrieve the WorkflowNotification's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the WorkflowNotification
     * @param qualifiedName for the WorkflowNotification
     * @param terms the list of terms to append to the WorkflowNotification
     * @return the WorkflowNotification that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static WorkflowNotification appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (WorkflowNotification) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a WorkflowNotification, without replacing all existing terms linked to the WorkflowNotification.
     * Note: this operation must make two API calls — one to retrieve the WorkflowNotification's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the WorkflowNotification
     * @param qualifiedName for the WorkflowNotification
     * @param terms the list of terms to remove from the WorkflowNotification, which must be referenced by GUID
     * @return the WorkflowNotification that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static WorkflowNotification removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (WorkflowNotification) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a WorkflowNotification, without replacing existing Atlan tags linked to the WorkflowNotification.
     * Note: this operation must make two API calls — one to retrieve the WorkflowNotification's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the WorkflowNotification
     * @param qualifiedName of the WorkflowNotification
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated WorkflowNotification
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static WorkflowNotification appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (WorkflowNotification) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a WorkflowNotification, without replacing existing Atlan tags linked to the WorkflowNotification.
     * Note: this operation must make two API calls — one to retrieve the WorkflowNotification's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the WorkflowNotification
     * @param qualifiedName of the WorkflowNotification
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated WorkflowNotification
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static WorkflowNotification appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (WorkflowNotification) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a WorkflowNotification.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a WorkflowNotification
     * @param qualifiedName of the WorkflowNotification
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the WorkflowNotification
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
