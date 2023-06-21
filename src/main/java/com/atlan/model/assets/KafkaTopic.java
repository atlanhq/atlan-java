/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.KafkaTopicCleanupPolicy;
import com.atlan.model.enums.KafkaTopicCompressionType;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Kafka Topic in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class KafkaTopic extends Asset implements IKafkaTopic, IKafka, IEventStore, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "KafkaTopic";

    /** Fixed typeName for KafkaTopics. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** Consumer groups subscribed to this topic. */
    @Attribute
    @Singular
    SortedSet<IKafkaConsumerGroup> kafkaConsumerGroups;

    /** TBC */
    @Attribute
    KafkaTopicCleanupPolicy kafkaTopicCleanupPolicy;

    /** TBC */
    @Attribute
    KafkaTopicCompressionType kafkaTopicCompressionType;

    /** TBC */
    @Attribute
    Boolean kafkaTopicIsInternal;

    /** TBC */
    @Attribute
    Long kafkaTopicPartitionsCount;

    /** Number of (unexpired) messages in this topic. */
    @Attribute
    Long kafkaTopicRecordCount;

    /** TBC */
    @Attribute
    Long kafkaTopicReplicationFactor;

    /** TBC */
    @Attribute
    Long kafkaTopicSegmentBytes;

    /** TBC */
    @Attribute
    Long kafkaTopicSizeInBytes;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /**
     * Reference to a KafkaTopic by GUID.
     *
     * @param guid the GUID of the KafkaTopic to reference
     * @return reference to a KafkaTopic that can be used for defining a relationship to a KafkaTopic
     */
    public static KafkaTopic refByGuid(String guid) {
        return KafkaTopic.builder().guid(guid).build();
    }

    /**
     * Reference to a KafkaTopic by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the KafkaTopic to reference
     * @return reference to a KafkaTopic that can be used for defining a relationship to a KafkaTopic
     */
    public static KafkaTopic refByQualifiedName(String qualifiedName) {
        return KafkaTopic.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a KafkaTopic by its GUID, complete with all of its relationships.
     *
     * @param guid of the KafkaTopic to retrieve
     * @return the requested full KafkaTopic, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the KafkaTopic does not exist or the provided GUID is not a KafkaTopic
     */
    public static KafkaTopic retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof KafkaTopic) {
            return (KafkaTopic) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "KafkaTopic");
        }
    }

    /**
     * Retrieves a KafkaTopic by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the KafkaTopic to retrieve
     * @return the requested full KafkaTopic, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the KafkaTopic does not exist
     */
    public static KafkaTopic retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof KafkaTopic) {
            return (KafkaTopic) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "KafkaTopic");
        }
    }

    /**
     * Restore the archived (soft-deleted) KafkaTopic to active.
     *
     * @param qualifiedName for the KafkaTopic
     * @return true if the KafkaTopic is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a KafkaTopic.
     *
     * @param qualifiedName of the KafkaTopic
     * @param name of the KafkaTopic
     * @return the minimal request necessary to update the KafkaTopic, as a builder
     */
    public static KafkaTopicBuilder<?, ?> updater(String qualifiedName, String name) {
        return KafkaTopic.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a KafkaTopic, from a potentially
     * more-complete KafkaTopic object.
     *
     * @return the minimal object necessary to update the KafkaTopic, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for KafkaTopic are not found in the initial object
     */
    @Override
    public KafkaTopicBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "KafkaTopic", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a KafkaTopic.
     *
     * @param qualifiedName of the KafkaTopic
     * @param name of the KafkaTopic
     * @return the updated KafkaTopic, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static KafkaTopic removeDescription(String qualifiedName, String name) throws AtlanException {
        return (KafkaTopic) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a KafkaTopic.
     *
     * @param qualifiedName of the KafkaTopic
     * @param name of the KafkaTopic
     * @return the updated KafkaTopic, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static KafkaTopic removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (KafkaTopic) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a KafkaTopic.
     *
     * @param qualifiedName of the KafkaTopic
     * @param name of the KafkaTopic
     * @return the updated KafkaTopic, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static KafkaTopic removeOwners(String qualifiedName, String name) throws AtlanException {
        return (KafkaTopic) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a KafkaTopic.
     *
     * @param qualifiedName of the KafkaTopic
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated KafkaTopic, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static KafkaTopic updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (KafkaTopic) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a KafkaTopic.
     *
     * @param qualifiedName of the KafkaTopic
     * @param name of the KafkaTopic
     * @return the updated KafkaTopic, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static KafkaTopic removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (KafkaTopic) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a KafkaTopic.
     *
     * @param qualifiedName of the KafkaTopic
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static KafkaTopic updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (KafkaTopic) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a KafkaTopic.
     *
     * @param qualifiedName of the KafkaTopic
     * @param name of the KafkaTopic
     * @return the updated KafkaTopic, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static KafkaTopic removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (KafkaTopic) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the KafkaTopic.
     *
     * @param qualifiedName for the KafkaTopic
     * @param name human-readable name of the KafkaTopic
     * @param terms the list of terms to replace on the KafkaTopic, or null to remove all terms from the KafkaTopic
     * @return the KafkaTopic that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static KafkaTopic replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (KafkaTopic) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the KafkaTopic, without replacing existing terms linked to the KafkaTopic.
     * Note: this operation must make two API calls — one to retrieve the KafkaTopic's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the KafkaTopic
     * @param terms the list of terms to append to the KafkaTopic
     * @return the KafkaTopic that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static KafkaTopic appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (KafkaTopic) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a KafkaTopic, without replacing all existing terms linked to the KafkaTopic.
     * Note: this operation must make two API calls — one to retrieve the KafkaTopic's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the KafkaTopic
     * @param terms the list of terms to remove from the KafkaTopic, which must be referenced by GUID
     * @return the KafkaTopic that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static KafkaTopic removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (KafkaTopic) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a KafkaTopic, without replacing existing Atlan tags linked to the KafkaTopic.
     * Note: this operation must make two API calls — one to retrieve the KafkaTopic's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the KafkaTopic
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated KafkaTopic
     */
    public static KafkaTopic appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (KafkaTopic) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a KafkaTopic, without replacing existing Atlan tags linked to the KafkaTopic.
     * Note: this operation must make two API calls — one to retrieve the KafkaTopic's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the KafkaTopic
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated KafkaTopic
     */
    public static KafkaTopic appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (KafkaTopic) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a KafkaTopic.
     *
     * @param qualifiedName of the KafkaTopic
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the KafkaTopic
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a KafkaTopic.
     *
     * @param qualifiedName of the KafkaTopic
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the KafkaTopic
     * @deprecated see {@link #appendAtlanTags(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    public static void addAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a KafkaTopic.
     *
     * @param qualifiedName of the KafkaTopic
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the KafkaTopic
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
