/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.CompoundQuery;
import com.atlan.model.search.FluentSearch;
import com.atlan.model.structs.KafkaTopicConsumption;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Kafka ConsumerGroup in Atlan. These group consumers of topics in Kafka.
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

    /** Application that is implemented by this asset. */
    @Attribute
    IAppApplication appApplicationImplemented;

    /** Application component that is implemented by this asset. */
    @Attribute
    IAppComponent appComponentImplemented;

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

    /** Number of members in this consumer group. */
    @Attribute
    Long kafkaConsumerGroupMemberCount;

    /** List of consumption properties for Kafka topics, for this consumer group. */
    @Attribute
    @Singular
    List<KafkaTopicConsumption> kafkaConsumerGroupTopicConsumptionProperties;

    /** Simple names of the topics consumed by this consumer group. */
    @Attribute
    @Singular
    SortedSet<String> kafkaTopicNames;

    /** Unique names of the topics consumed by this consumer group. */
    @Attribute
    @Singular
    SortedSet<String> kafkaTopicQualifiedNames;

    /** Topics consumed by this consumer group. */
    @Attribute
    @Singular
    SortedSet<IKafkaTopic> kafkaTopics;

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
     * Builds the minimal object necessary to create a relationship to a KafkaConsumerGroup, from a potentially
     * more-complete KafkaConsumerGroup object.
     *
     * @return the minimal object necessary to relate to the KafkaConsumerGroup
     * @throws InvalidRequestException if any of the minimal set of required properties for a KafkaConsumerGroup relationship are not found in the initial object
     */
    @Override
    public KafkaConsumerGroup trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all KafkaConsumerGroup assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) KafkaConsumerGroup assets will be included.
     *
     * @return a fluent search that includes all KafkaConsumerGroup assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all KafkaConsumerGroup assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) KafkaConsumerGroup assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all KafkaConsumerGroup assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all KafkaConsumerGroup assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) KafkaConsumerGroups will be included
     * @return a fluent search that includes all KafkaConsumerGroup assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all KafkaConsumerGroup assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) KafkaConsumerGroups will be included
     * @return a fluent search that includes all KafkaConsumerGroup assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client, boolean includeArchived) {
        FluentSearch.FluentSearchBuilder<?, ?> builder =
                FluentSearch.builder(client).where(CompoundQuery.assetType(TYPE_NAME));
        if (!includeArchived) {
            builder.where(CompoundQuery.ACTIVE);
        }
        return builder;
    }

    /**
     * Reference to a KafkaConsumerGroup by GUID. Use this to create a relationship to this KafkaConsumerGroup,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the KafkaConsumerGroup to reference
     * @return reference to a KafkaConsumerGroup that can be used for defining a relationship to a KafkaConsumerGroup
     */
    public static KafkaConsumerGroup refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a KafkaConsumerGroup by GUID. Use this to create a relationship to this KafkaConsumerGroup,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the KafkaConsumerGroup to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a KafkaConsumerGroup that can be used for defining a relationship to a KafkaConsumerGroup
     */
    public static KafkaConsumerGroup refByGuid(String guid, Reference.SaveSemantic semantic) {
        return KafkaConsumerGroup._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a KafkaConsumerGroup by qualifiedName. Use this to create a relationship to this KafkaConsumerGroup,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the KafkaConsumerGroup to reference
     * @return reference to a KafkaConsumerGroup that can be used for defining a relationship to a KafkaConsumerGroup
     */
    public static KafkaConsumerGroup refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a KafkaConsumerGroup by qualifiedName. Use this to create a relationship to this KafkaConsumerGroup,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the KafkaConsumerGroup to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a KafkaConsumerGroup that can be used for defining a relationship to a KafkaConsumerGroup
     */
    public static KafkaConsumerGroup refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return KafkaConsumerGroup._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
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
        return get(client, id, true);
    }

    /**
     * Retrieves a KafkaConsumerGroup by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the KafkaConsumerGroup to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full KafkaConsumerGroup, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the KafkaConsumerGroup does not exist or the provided GUID is not a KafkaConsumerGroup
     */
    @JsonIgnore
    public static KafkaConsumerGroup get(AtlanClient client, String id, boolean includeRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof KafkaConsumerGroup) {
                return (KafkaConsumerGroup) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof KafkaConsumerGroup) {
                return (KafkaConsumerGroup) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
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
     * Builds the minimal object necessary to create a KafkaConsumerGroup.
     *
     * @param name of the KafkaConsumerGroup
     * @param topics in which the KafkaConsumerGroup should be created, the first of which must have at least
     *               a qualifiedName
     * @return the minimal request necessary to create the KafkaConsumerGroup, as a builder
     * @throws InvalidRequestException if the first topic provided is without a qualifiedName
     */
    public static KafkaConsumerGroupBuilder<?, ?> creatorObj(String name, List<KafkaTopic> topics)
            throws InvalidRequestException {
        if (topics == null || topics.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, KafkaTopic.TYPE_NAME, "null");
        }
        List<String> topicNames = new ArrayList<>();
        List<String> topicQualifiedNames = new ArrayList<>();
        List<KafkaTopic> minimalTopics = new ArrayList<>();
        for (KafkaTopic topic : topics) {
            validateRelationship(
                    KafkaTopic.TYPE_NAME,
                    Map.of(
                            "connectionQualifiedName", topic.getConnectionQualifiedName(),
                            "name", topic.getName(),
                            "qualifiedName", topic.getQualifiedName()));
            topicNames.add(topic.getName());
            topicQualifiedNames.add(topic.getQualifiedName());
            minimalTopics.add(topic.trimToReference());
        }
        return creator(name, topics.get(0).getConnectionQualifiedName(), topicNames, topicQualifiedNames)
                .kafkaTopics(minimalTopics);
    }

    /**
     * Builds the minimal object necessary to create a KafkaConsumerGroup.
     *
     * @param name of the KafkaConsumerGroup
     * @param topicQualifiedNames unique names of the topics in which the KafkaConsumerGroup is contained
     * @throws InvalidRequestException if no topic qualifiedNames are provided
     * @return the minimal object necessary to create the KafkaConsumerGroup, as a builder
     */
    public static KafkaConsumerGroupBuilder<?, ?> creator(String name, List<String> topicQualifiedNames)
            throws InvalidRequestException {
        if (topicQualifiedNames == null || topicQualifiedNames.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, KafkaTopic.TYPE_NAME, "null");
        }
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(topicQualifiedNames.get(0));
        List<String> topicNames = topicQualifiedNames.stream()
                .map(t -> (StringUtils.getNameFromQualifiedName(t)))
                .collect(Collectors.toList());
        return creator(name, connectionQualifiedName, topicNames, topicQualifiedNames);
    }

    /**
     * Builds the minimal object necessary to create a KafkaConsumerGroup.
     *
     * @param name of the KafkaConsumerGroup
     * @param connectionQualifiedName unique name of the connection in which the KafkaConsumerGroup should be created
     * @param topicNames simple names of the KafkaTopics in which the KafkaConsumerGroup should be created
     * @param topicQualifiedNames unique names of the KafkaTopics in which the KafkaConsumerGroup should be created
     * @return the minimal object necessary to create the KafkaConsumerGroup, as a builder
     */
    public static KafkaConsumerGroupBuilder<?, ?> creator(
            String name, String connectionQualifiedName, List<String> topicNames, List<String> topicQualifiedNames) {
        return KafkaConsumerGroup._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName))
                .kafkaTopics(topicQualifiedNames.stream()
                        .map(t -> KafkaTopic.refByQualifiedName(t))
                        .collect(Collectors.toList()))
                .kafkaTopicNames(topicNames)
                .kafkaTopicQualifiedNames(topicQualifiedNames);
    }

    /**
     * Generate a unique KafkaConsumerGroup name.
     *
     * @param name of the KafkaConsumerGroup
     * @param connectionQualifiedName unique name of the connection in which the KafkaConsumerGroup is contained
     * @return a unique name for the KafkaConsumerGroup
     */
    public static String generateQualifiedName(String name, String connectionQualifiedName) {
        return connectionQualifiedName + "/consumer-group/" + name;
    }

    /**
     * Builds the minimal object necessary to update an KafkaConsumerGroup.
     *
     * @param qualifiedName of the KafkaConsumerGroup
     * @param name of the KafkaConsumerGroup
     * @return the minimal request necessary to update the KafkaConsumerGroup, as a builder
     */
    public static KafkaConsumerGroupBuilder<?, ?> updater(String qualifiedName, String name) {
        return KafkaConsumerGroup._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to an KafkaConsumerGroup, from a potentially
     * more-complete KafkaConsumerGroup object.
     *
     * @return the minimal object necessary to update the KafkaConsumerGroup, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for KafkaConsumerGroup are not found in the initial object
     */
    @Override
    public KafkaConsumerGroupBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(
                TYPE_NAME,
                Map.of(
                        "qualifiedName", this.getQualifiedName(),
                        "name", this.getName()));
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
