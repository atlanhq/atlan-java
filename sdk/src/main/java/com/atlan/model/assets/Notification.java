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
 * Base class for a durable, queryable notification record. Never instantiated: only a subtype is ever created, the way nothing creates a bare Catalog.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class Notification extends Asset implements INotification, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Notification";

    /** Fixed typeName for Notifications. */
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

    /**
     * Builds the minimal object necessary to create a relationship to a Notification, from a potentially
     * more-complete Notification object.
     *
     * @return the minimal object necessary to relate to the Notification
     * @throws InvalidRequestException if any of the minimal set of required properties for a Notification relationship are not found in the initial object
     */
    @Override
    public Notification trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all Notification assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) Notification assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all Notification assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all Notification assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) Notifications will be included
     * @return a fluent search that includes all Notification assets
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
     * Reference to a Notification by GUID. Use this to create a relationship to this Notification,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the Notification to reference
     * @return reference to a Notification that can be used for defining a relationship to a Notification
     */
    public static Notification refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a Notification by GUID. Use this to create a relationship to this Notification,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the Notification to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a Notification that can be used for defining a relationship to a Notification
     */
    public static Notification refByGuid(String guid, Reference.SaveSemantic semantic) {
        return Notification._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a Notification by qualifiedName. Use this to create a relationship to this Notification,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the Notification to reference
     * @return reference to a Notification that can be used for defining a relationship to a Notification
     */
    public static Notification refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a Notification by qualifiedName. Use this to create a relationship to this Notification,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the Notification to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a Notification that can be used for defining a relationship to a Notification
     */
    public static Notification refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return Notification._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a Notification by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Notification to retrieve, either its GUID or its full qualifiedName
     * @return the requested full Notification, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Notification does not exist or the provided GUID is not a Notification
     */
    @JsonIgnore
    public static Notification get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a Notification by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Notification to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full Notification, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Notification does not exist or the provided GUID is not a Notification
     */
    @JsonIgnore
    public static Notification get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof Notification) {
                return (Notification) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof Notification) {
                return (Notification) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a Notification by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Notification to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the Notification, including any relationships
     * @return the requested Notification, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Notification does not exist or the provided GUID is not a Notification
     */
    @JsonIgnore
    public static Notification get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a Notification by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Notification to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the Notification, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the Notification
     * @return the requested Notification, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Notification does not exist or the provided GUID is not a Notification
     */
    @JsonIgnore
    public static Notification get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = Notification.select(client)
                    .where(Notification.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof Notification) {
                return (Notification) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = Notification.select(client)
                    .where(Notification.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof Notification) {
                return (Notification) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) Notification to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the Notification
     * @return true if the Notification is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a Notification.
     *
     * @param qualifiedName of the Notification
     * @param name of the Notification
     * @return the minimal request necessary to update the Notification, as a builder
     */
    public static NotificationBuilder<?, ?> updater(String qualifiedName, String name) {
        return Notification._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Notification,
     * from a potentially more-complete Notification object.
     *
     * @return the minimal object necessary to update the Notification, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a Notification are not present in the initial object
     */
    @Override
    public NotificationBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class NotificationBuilder<C extends Notification, B extends NotificationBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a Notification.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the Notification
     * @param name of the Notification
     * @return the updated Notification, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Notification removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Notification) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a Notification.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the Notification
     * @param name of the Notification
     * @return the updated Notification, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Notification removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Notification) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a Notification.
     *
     * @param client connectivity to the Atlan tenant from which to remove the Notification's owners
     * @param qualifiedName of the Notification
     * @param name of the Notification
     * @return the updated Notification, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Notification removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Notification) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a Notification.
     *
     * @param client connectivity to the Atlan tenant on which to update the Notification's certificate
     * @param qualifiedName of the Notification
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated Notification, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Notification updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (Notification)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a Notification.
     *
     * @param client connectivity to the Atlan tenant from which to remove the Notification's certificate
     * @param qualifiedName of the Notification
     * @param name of the Notification
     * @return the updated Notification, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Notification removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Notification) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a Notification.
     *
     * @param client connectivity to the Atlan tenant on which to update the Notification's announcement
     * @param qualifiedName of the Notification
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Notification updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (Notification)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a Notification.
     *
     * @param client connectivity to the Atlan client from which to remove the Notification's announcement
     * @param qualifiedName of the Notification
     * @param name of the Notification
     * @return the updated Notification, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Notification removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Notification) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the Notification.
     *
     * @param client connectivity to the Atlan tenant on which to replace the Notification's assigned terms
     * @param qualifiedName for the Notification
     * @param name human-readable name of the Notification
     * @param terms the list of terms to replace on the Notification, or null to remove all terms from the Notification
     * @return the Notification that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static Notification replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (Notification) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the Notification, without replacing existing terms linked to the Notification.
     * Note: this operation must make two API calls — one to retrieve the Notification's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the Notification
     * @param qualifiedName for the Notification
     * @param terms the list of terms to append to the Notification
     * @return the Notification that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static Notification appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (Notification) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a Notification, without replacing all existing terms linked to the Notification.
     * Note: this operation must make two API calls — one to retrieve the Notification's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the Notification
     * @param qualifiedName for the Notification
     * @param terms the list of terms to remove from the Notification, which must be referenced by GUID
     * @return the Notification that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static Notification removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (Notification) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a Notification, without replacing existing Atlan tags linked to the Notification.
     * Note: this operation must make two API calls — one to retrieve the Notification's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the Notification
     * @param qualifiedName of the Notification
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated Notification
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static Notification appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (Notification) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Notification, without replacing existing Atlan tags linked to the Notification.
     * Note: this operation must make two API calls — one to retrieve the Notification's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the Notification
     * @param qualifiedName of the Notification
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated Notification
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static Notification appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (Notification) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a Notification.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a Notification
     * @param qualifiedName of the Notification
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the Notification
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
