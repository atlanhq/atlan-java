/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.AssetFilter;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.structs.KafkaTopicConsumption;
import com.atlan.util.QueryFactory;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Kafka ConsumerGroup in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class KafkaConsumerGroup extends Asset
        implements IKafkaConsumerGroup, IKafka, IEventStore, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "KafkaConsumerGroup";

    /** Fixed typeName for KafkaConsumerGroups. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    Long kafkaConsumerGroupMemberCount;

    /** TBC */
    @Attribute
    @Singular
    List<KafkaTopicConsumption> kafkaConsumerGroupTopicConsumptionProperties;

    /** Names of the topics for this consumer group. */
    @Attribute
    @Singular
    SortedSet<String> kafkaTopicNames;

    /** Unique names of the topics for this consumer group. */
    @Attribute
    @Singular
    SortedSet<String> kafkaTopicQualifiedNames;

    /** Topics for this consumer group. */
    @Attribute
    @Singular
    SortedSet<IKafkaTopic> kafkaTopics;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /**
     * Start an asset filter that will return all KafkaConsumerGroup assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) KafkaConsumerGroup assets will be included.
     *
     * @return an asset filter that includes all KafkaConsumerGroup assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all KafkaConsumerGroup assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) KafkaConsumerGroup assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all KafkaConsumerGroup assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all KafkaConsumerGroup assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) KafkaConsumerGroups will be included
     * @return an asset filter that includes all KafkaConsumerGroup assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all KafkaConsumerGroup assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) KafkaConsumerGroups will be included
     * @return an asset filter that includes all KafkaConsumerGroup assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client, boolean includeArchived) {
        AssetFilter.AssetFilterBuilder builder =
                AssetFilter.builder().client(client).filter(QueryFactory.type(TYPE_NAME));
        if (!includeArchived) {
            builder.filter(QueryFactory.active());
        }
        return builder;
    }

    /**
     * Reference to a KafkaConsumerGroup by GUID.
     *
     * @param guid the GUID of the KafkaConsumerGroup to reference
     * @return reference to a KafkaConsumerGroup that can be used for defining a relationship to a KafkaConsumerGroup
     */
    public static KafkaConsumerGroup refByGuid(String guid) {
        return KafkaConsumerGroup._internal().guid(guid).build();
    }

    /**
     * Reference to a KafkaConsumerGroup by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the KafkaConsumerGroup to reference
     * @return reference to a KafkaConsumerGroup that can be used for defining a relationship to a KafkaConsumerGroup
     */
    public static KafkaConsumerGroup refByQualifiedName(String qualifiedName) {
        return KafkaConsumerGroup._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a KafkaConsumerGroup by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the KafkaConsumerGroup to retrieve, either its GUID or its full qualifiedName
     * @return the requested full KafkaConsumerGroup, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the KafkaConsumerGroup does not exist or the provided GUID is not a KafkaConsumerGroup
     */
    @JsonIgnore
    public static KafkaConsumerGroup get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a KafkaConsumerGroup by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the KafkaConsumerGroup to retrieve, either its GUID or its full qualifiedName
     * @return the requested full KafkaConsumerGroup, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the KafkaConsumerGroup does not exist or the provided GUID is not a KafkaConsumerGroup
     */
    @JsonIgnore
    public static KafkaConsumerGroup get(AtlanClient client, String id) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.retrieveFull(client, id);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof KafkaConsumerGroup) {
                return (KafkaConsumerGroup) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, "KafkaConsumerGroup");
            }
        } else {
            Asset asset = Asset.retrieveFull(client, TYPE_NAME, id);
            if (asset instanceof KafkaConsumerGroup) {
                return (KafkaConsumerGroup) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, "KafkaConsumerGroup");
            }
        }
    }

    /**
     * Retrieves a KafkaConsumerGroup by its GUID, complete with all of its relationships.
     *
     * @param guid of the KafkaConsumerGroup to retrieve
     * @return the requested full KafkaConsumerGroup, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the KafkaConsumerGroup does not exist or the provided GUID is not a KafkaConsumerGroup
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static KafkaConsumerGroup retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a KafkaConsumerGroup by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the KafkaConsumerGroup to retrieve
     * @return the requested full KafkaConsumerGroup, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the KafkaConsumerGroup does not exist or the provided GUID is not a KafkaConsumerGroup
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static KafkaConsumerGroup retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof KafkaConsumerGroup) {
            return (KafkaConsumerGroup) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "KafkaConsumerGroup");
        }
    }

    /**
     * Retrieves a KafkaConsumerGroup by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the KafkaConsumerGroup to retrieve
     * @return the requested full KafkaConsumerGroup, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the KafkaConsumerGroup does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static KafkaConsumerGroup retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a KafkaConsumerGroup by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the KafkaConsumerGroup to retrieve
     * @return the requested full KafkaConsumerGroup, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the KafkaConsumerGroup does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static KafkaConsumerGroup retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof KafkaConsumerGroup) {
            return (KafkaConsumerGroup) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "KafkaConsumerGroup");
        }
    }

    /**
     * Restore the archived (soft-deleted) KafkaConsumerGroup to active.
     *
     * @param qualifiedName for the KafkaConsumerGroup
     * @return true if the KafkaConsumerGroup is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) KafkaConsumerGroup to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the KafkaConsumerGroup
     * @return true if the KafkaConsumerGroup is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a KafkaConsumerGroup.
     *
     * @param qualifiedName of the KafkaConsumerGroup
     * @param name of the KafkaConsumerGroup
     * @return the minimal request necessary to update the KafkaConsumerGroup, as a builder
     */
    public static KafkaConsumerGroupBuilder<?, ?> updater(String qualifiedName, String name) {
        return KafkaConsumerGroup._internal().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a KafkaConsumerGroup, from a potentially
     * more-complete KafkaConsumerGroup object.
     *
     * @return the minimal object necessary to update the KafkaConsumerGroup, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for KafkaConsumerGroup are not found in the initial object
     */
    @Override
    public KafkaConsumerGroupBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "KafkaConsumerGroup", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a KafkaConsumerGroup.
     *
     * @param qualifiedName of the KafkaConsumerGroup
     * @param name of the KafkaConsumerGroup
     * @return the updated KafkaConsumerGroup, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static KafkaConsumerGroup removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a KafkaConsumerGroup.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the KafkaConsumerGroup
     * @param name of the KafkaConsumerGroup
     * @return the updated KafkaConsumerGroup, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static KafkaConsumerGroup removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (KafkaConsumerGroup) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a KafkaConsumerGroup.
     *
     * @param qualifiedName of the KafkaConsumerGroup
     * @param name of the KafkaConsumerGroup
     * @return the updated KafkaConsumerGroup, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static KafkaConsumerGroup removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a KafkaConsumerGroup.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the KafkaConsumerGroup
     * @param name of the KafkaConsumerGroup
     * @return the updated KafkaConsumerGroup, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static KafkaConsumerGroup removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (KafkaConsumerGroup) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a KafkaConsumerGroup.
     *
     * @param qualifiedName of the KafkaConsumerGroup
     * @param name of the KafkaConsumerGroup
     * @return the updated KafkaConsumerGroup, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static KafkaConsumerGroup removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a KafkaConsumerGroup.
     *
     * @param client connectivity to the Atlan tenant from which to remove the KafkaConsumerGroup's owners
     * @param qualifiedName of the KafkaConsumerGroup
     * @param name of the KafkaConsumerGroup
     * @return the updated KafkaConsumerGroup, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static KafkaConsumerGroup removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (KafkaConsumerGroup) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a KafkaConsumerGroup.
     *
     * @param qualifiedName of the KafkaConsumerGroup
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated KafkaConsumerGroup, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static KafkaConsumerGroup updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a KafkaConsumerGroup.
     *
     * @param client connectivity to the Atlan tenant on which to update the KafkaConsumerGroup's certificate
     * @param qualifiedName of the KafkaConsumerGroup
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated KafkaConsumerGroup, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static KafkaConsumerGroup updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (KafkaConsumerGroup)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a KafkaConsumerGroup.
     *
     * @param qualifiedName of the KafkaConsumerGroup
     * @param name of the KafkaConsumerGroup
     * @return the updated KafkaConsumerGroup, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static KafkaConsumerGroup removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a KafkaConsumerGroup.
     *
     * @param client connectivity to the Atlan tenant from which to remove the KafkaConsumerGroup's certificate
     * @param qualifiedName of the KafkaConsumerGroup
     * @param name of the KafkaConsumerGroup
     * @return the updated KafkaConsumerGroup, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static KafkaConsumerGroup removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (KafkaConsumerGroup) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a KafkaConsumerGroup.
     *
     * @param qualifiedName of the KafkaConsumerGroup
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static KafkaConsumerGroup updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a KafkaConsumerGroup.
     *
     * @param client connectivity to the Atlan tenant on which to update the KafkaConsumerGroup's announcement
     * @param qualifiedName of the KafkaConsumerGroup
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static KafkaConsumerGroup updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (KafkaConsumerGroup)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a KafkaConsumerGroup.
     *
     * @param qualifiedName of the KafkaConsumerGroup
     * @param name of the KafkaConsumerGroup
     * @return the updated KafkaConsumerGroup, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static KafkaConsumerGroup removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a KafkaConsumerGroup.
     *
     * @param client connectivity to the Atlan client from which to remove the KafkaConsumerGroup's announcement
     * @param qualifiedName of the KafkaConsumerGroup
     * @param name of the KafkaConsumerGroup
     * @return the updated KafkaConsumerGroup, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static KafkaConsumerGroup removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (KafkaConsumerGroup) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the KafkaConsumerGroup.
     *
     * @param qualifiedName for the KafkaConsumerGroup
     * @param name human-readable name of the KafkaConsumerGroup
     * @param terms the list of terms to replace on the KafkaConsumerGroup, or null to remove all terms from the KafkaConsumerGroup
     * @return the KafkaConsumerGroup that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static KafkaConsumerGroup replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the KafkaConsumerGroup.
     *
     * @param client connectivity to the Atlan tenant on which to replace the KafkaConsumerGroup's assigned terms
     * @param qualifiedName for the KafkaConsumerGroup
     * @param name human-readable name of the KafkaConsumerGroup
     * @param terms the list of terms to replace on the KafkaConsumerGroup, or null to remove all terms from the KafkaConsumerGroup
     * @return the KafkaConsumerGroup that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static KafkaConsumerGroup replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (KafkaConsumerGroup) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the KafkaConsumerGroup, without replacing existing terms linked to the KafkaConsumerGroup.
     * Note: this operation must make two API calls — one to retrieve the KafkaConsumerGroup's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the KafkaConsumerGroup
     * @param terms the list of terms to append to the KafkaConsumerGroup
     * @return the KafkaConsumerGroup that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static KafkaConsumerGroup appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the KafkaConsumerGroup, without replacing existing terms linked to the KafkaConsumerGroup.
     * Note: this operation must make two API calls — one to retrieve the KafkaConsumerGroup's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the KafkaConsumerGroup
     * @param qualifiedName for the KafkaConsumerGroup
     * @param terms the list of terms to append to the KafkaConsumerGroup
     * @return the KafkaConsumerGroup that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static KafkaConsumerGroup appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (KafkaConsumerGroup) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a KafkaConsumerGroup, without replacing all existing terms linked to the KafkaConsumerGroup.
     * Note: this operation must make two API calls — one to retrieve the KafkaConsumerGroup's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the KafkaConsumerGroup
     * @param terms the list of terms to remove from the KafkaConsumerGroup, which must be referenced by GUID
     * @return the KafkaConsumerGroup that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static KafkaConsumerGroup removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a KafkaConsumerGroup, without replacing all existing terms linked to the KafkaConsumerGroup.
     * Note: this operation must make two API calls — one to retrieve the KafkaConsumerGroup's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the KafkaConsumerGroup
     * @param qualifiedName for the KafkaConsumerGroup
     * @param terms the list of terms to remove from the KafkaConsumerGroup, which must be referenced by GUID
     * @return the KafkaConsumerGroup that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static KafkaConsumerGroup removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (KafkaConsumerGroup) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a KafkaConsumerGroup, without replacing existing Atlan tags linked to the KafkaConsumerGroup.
     * Note: this operation must make two API calls — one to retrieve the KafkaConsumerGroup's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the KafkaConsumerGroup
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated KafkaConsumerGroup
     */
    public static KafkaConsumerGroup appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a KafkaConsumerGroup, without replacing existing Atlan tags linked to the KafkaConsumerGroup.
     * Note: this operation must make two API calls — one to retrieve the KafkaConsumerGroup's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the KafkaConsumerGroup
     * @param qualifiedName of the KafkaConsumerGroup
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated KafkaConsumerGroup
     */
    public static KafkaConsumerGroup appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (KafkaConsumerGroup) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a KafkaConsumerGroup, without replacing existing Atlan tags linked to the KafkaConsumerGroup.
     * Note: this operation must make two API calls — one to retrieve the KafkaConsumerGroup's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the KafkaConsumerGroup
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated KafkaConsumerGroup
     */
    public static KafkaConsumerGroup appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return appendAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a KafkaConsumerGroup, without replacing existing Atlan tags linked to the KafkaConsumerGroup.
     * Note: this operation must make two API calls — one to retrieve the KafkaConsumerGroup's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the KafkaConsumerGroup
     * @param qualifiedName of the KafkaConsumerGroup
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated KafkaConsumerGroup
     */
    public static KafkaConsumerGroup appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (KafkaConsumerGroup) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a KafkaConsumerGroup.
     *
     * @param qualifiedName of the KafkaConsumerGroup
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the KafkaConsumerGroup
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a KafkaConsumerGroup.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the KafkaConsumerGroup
     * @param qualifiedName of the KafkaConsumerGroup
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the KafkaConsumerGroup
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a KafkaConsumerGroup.
     *
     * @param qualifiedName of the KafkaConsumerGroup
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the KafkaConsumerGroup
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
        addAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a KafkaConsumerGroup.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the KafkaConsumerGroup
     * @param qualifiedName of the KafkaConsumerGroup
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the KafkaConsumerGroup
     * @deprecated see {@link #appendAtlanTags(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    public static void addAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a KafkaConsumerGroup.
     *
     * @param qualifiedName of the KafkaConsumerGroup
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the KafkaConsumerGroup
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a KafkaConsumerGroup.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a KafkaConsumerGroup
     * @param qualifiedName of the KafkaConsumerGroup
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the KafkaConsumerGroup
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
