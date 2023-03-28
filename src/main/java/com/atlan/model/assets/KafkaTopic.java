/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.*;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * TBC
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class KafkaTopic extends Kafka {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "KafkaTopic";

    /** Fixed typeName for KafkaTopics. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    Boolean kafkaTopicIsInternal;

    /** TBC */
    @Attribute
    KafkaTopicCompressionType kafkaTopicCompressionType;

    /** TBC */
    @Attribute
    Long kafkaTopicReplicationFactor;

    /** TBC */
    @Attribute
    Long kafkaTopicSegmentBytes;

    /** TBC */
    @Attribute
    Long kafkaTopicPartitionsCount;

    /** TBC */
    @Attribute
    Long kafkaTopicSizeInBytes;

    /** TBC */
    @Attribute
    Long kafkaTopicRecordCount;

    /** TBC */
    @Attribute
    KafkaTopicCleanupPolicy kafkaTopicCleanupPolicy;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<KafkaConsumerGroup> kafkaConsumerGroups;

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
    public static KafkaTopic updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
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
     * Add classifications to a KafkaTopic.
     *
     * @param qualifiedName of the KafkaTopic
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the KafkaTopic
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a KafkaTopic.
     *
     * @param qualifiedName of the KafkaTopic
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the KafkaTopic
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
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
    public static KafkaTopic replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
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
    public static KafkaTopic appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
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
    public static KafkaTopic removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (KafkaTopic) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
