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
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.model.structs.KafkaTopicConsumption;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.HashMap;
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
 * Instance of an Azure Event Hub Consumer Group asset, equivalent to Kafka ConsumerGroup.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class AzureEventHubConsumerGroup extends Asset
        implements IAzureEventHubConsumerGroup,
                IKafkaConsumerGroup,
                IKafka,
                IEventStore,
                ICatalog,
                IAsset,
                IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AzureEventHubConsumerGroup";

    /** Fixed typeName for AzureEventHubConsumerGroups. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** ApplicationContainer asset containing this Catalog asset. */
    @Attribute
    IApplicationContainer applicationContainer;

    /** Qualified name of the Application Container that contains this asset. */
    @Attribute
    String assetApplicationQualifiedName;

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
     * Builds the minimal object necessary to create a relationship to a AzureEventHubConsumerGroup, from a potentially
     * more-complete AzureEventHubConsumerGroup object.
     *
     * @return the minimal object necessary to relate to the AzureEventHubConsumerGroup
     * @throws InvalidRequestException if any of the minimal set of required properties for a AzureEventHubConsumerGroup relationship are not found in the initial object
     */
    @Override
    public AzureEventHubConsumerGroup trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all AzureEventHubConsumerGroup assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) AzureEventHubConsumerGroup assets will be included.
     *
     * @return a fluent search that includes all AzureEventHubConsumerGroup assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all AzureEventHubConsumerGroup assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) AzureEventHubConsumerGroup assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all AzureEventHubConsumerGroup assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all AzureEventHubConsumerGroup assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) AzureEventHubConsumerGroups will be included
     * @return a fluent search that includes all AzureEventHubConsumerGroup assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all AzureEventHubConsumerGroup assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) AzureEventHubConsumerGroups will be included
     * @return a fluent search that includes all AzureEventHubConsumerGroup assets
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
     * Reference to a AzureEventHubConsumerGroup by GUID. Use this to create a relationship to this AzureEventHubConsumerGroup,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the AzureEventHubConsumerGroup to reference
     * @return reference to a AzureEventHubConsumerGroup that can be used for defining a relationship to a AzureEventHubConsumerGroup
     */
    public static AzureEventHubConsumerGroup refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AzureEventHubConsumerGroup by GUID. Use this to create a relationship to this AzureEventHubConsumerGroup,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the AzureEventHubConsumerGroup to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AzureEventHubConsumerGroup that can be used for defining a relationship to a AzureEventHubConsumerGroup
     */
    public static AzureEventHubConsumerGroup refByGuid(String guid, Reference.SaveSemantic semantic) {
        return AzureEventHubConsumerGroup._internal()
                .guid(guid)
                .semantic(semantic)
                .build();
    }

    /**
     * Reference to a AzureEventHubConsumerGroup by qualifiedName. Use this to create a relationship to this AzureEventHubConsumerGroup,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the AzureEventHubConsumerGroup to reference
     * @return reference to a AzureEventHubConsumerGroup that can be used for defining a relationship to a AzureEventHubConsumerGroup
     */
    public static AzureEventHubConsumerGroup refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AzureEventHubConsumerGroup by qualifiedName. Use this to create a relationship to this AzureEventHubConsumerGroup,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the AzureEventHubConsumerGroup to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AzureEventHubConsumerGroup that can be used for defining a relationship to a AzureEventHubConsumerGroup
     */
    public static AzureEventHubConsumerGroup refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return AzureEventHubConsumerGroup._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a AzureEventHubConsumerGroup by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the AzureEventHubConsumerGroup to retrieve, either its GUID or its full qualifiedName
     * @return the requested full AzureEventHubConsumerGroup, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AzureEventHubConsumerGroup does not exist or the provided GUID is not a AzureEventHubConsumerGroup
     */
    @JsonIgnore
    public static AzureEventHubConsumerGroup get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a AzureEventHubConsumerGroup by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AzureEventHubConsumerGroup to retrieve, either its GUID or its full qualifiedName
     * @return the requested full AzureEventHubConsumerGroup, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AzureEventHubConsumerGroup does not exist or the provided GUID is not a AzureEventHubConsumerGroup
     */
    @JsonIgnore
    public static AzureEventHubConsumerGroup get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a AzureEventHubConsumerGroup by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AzureEventHubConsumerGroup to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full AzureEventHubConsumerGroup, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AzureEventHubConsumerGroup does not exist or the provided GUID is not a AzureEventHubConsumerGroup
     */
    @JsonIgnore
    public static AzureEventHubConsumerGroup get(AtlanClient client, String id, boolean includeRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof AzureEventHubConsumerGroup) {
                return (AzureEventHubConsumerGroup) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof AzureEventHubConsumerGroup) {
                return (AzureEventHubConsumerGroup) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) AzureEventHubConsumerGroup to active.
     *
     * @param qualifiedName for the AzureEventHubConsumerGroup
     * @return true if the AzureEventHubConsumerGroup is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) AzureEventHubConsumerGroup to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the AzureEventHubConsumerGroup
     * @return true if the AzureEventHubConsumerGroup is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create an AzureEventHubConsumerGroup.
     *
     * @param name of the AzureEventHubConsumerGroup
     * @param hubs in which the AzureEventHubConsumerGroup should be created, the first of which must have at least
     *             a qualifiedName
     * @return the minimal request necessary to create the AzureEventHubConsumerGroup, as a builder
     * @throws InvalidRequestException if the first hub provided is without a qualifiedName
     */
    public static AzureEventHubConsumerGroupBuilder<?, ?> creatorObj(String name, List<AzureEventHub> hubs)
            throws InvalidRequestException {
        if (hubs == null || hubs.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, AzureEventHub.TYPE_NAME, "null");
        }
        List<String> hubNames = new ArrayList<>();
        List<String> hubQualifiedNames = new ArrayList<>();
        List<AzureEventHub> minimalHubs = new ArrayList<>();
        for (AzureEventHub hub : hubs) {
            Map<String, String> map = new HashMap<>();
            map.put("connectionQualifiedName", hub.getConnectionQualifiedName());
            map.put("qualifiedName", hub.getQualifiedName());
            map.put("name", hub.getName());
            validateRelationship(AzureEventHub.TYPE_NAME, map);
            hubNames.add(hub.getName());
            hubQualifiedNames.add(hub.getQualifiedName());
            minimalHubs.add(hub.trimToReference());
        }
        return creator(name, hubs.get(0).getConnectionQualifiedName(), hubNames, hubQualifiedNames)
                .kafkaTopics(minimalHubs);
    }

    /**
     * Builds the minimal object necessary to create an AzureEventHubConsumerGroup.
     *
     * @param name of the AzureEventHubConsumerGroup
     * @param hubQualifiedNames unique names of the hubs in which the AzureEventHubConsumerGroup is contained
     * @throws InvalidRequestException if no hub qualifiedNames are provided
     * @return the minimal object necessary to create the AzureEventHubConsumerGroup, as a builder
     */
    public static AzureEventHubConsumerGroupBuilder<?, ?> creator(String name, List<String> hubQualifiedNames)
            throws InvalidRequestException {
        if (hubQualifiedNames == null || hubQualifiedNames.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, AzureEventHub.TYPE_NAME, "null");
        }
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(hubQualifiedNames.get(0));
        List<String> hubNames = hubQualifiedNames.stream()
                .map(h -> (StringUtils.getNameFromQualifiedName(h)))
                .collect(Collectors.toList());
        return creator(name, connectionQualifiedName, hubNames, hubQualifiedNames);
    }

    /**
     * Builds the minimal object necessary to create an AzureEventHubConsumerGroup.
     *
     * @param name of the AzureEventHubConsumerGroup
     * @param connectionQualifiedName unique name of the connection in which the AzureEventHubConsumerGroup should be created
     * @param hubNames simple names of the AzureEventHubs in which the AzureEventHubConsumerGroup should be created
     * @param hubQualifiedNames unique names of the AzureEventHubs in which the AzureEventHubConsumerGroup should be created
     * @return the minimal object necessary to create the AzureEventHubConsumerGroup, as a builder
     */
    public static AzureEventHubConsumerGroupBuilder<?, ?> creator(
            String name, String connectionQualifiedName, List<String> hubNames, List<String> hubQualifiedNames) {
        return AzureEventHubConsumerGroup._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName, hubNames.get(0)))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.AZURE_EVENT_HUB)
                .kafkaTopics(hubQualifiedNames.stream()
                        .map(h -> AzureEventHub.refByQualifiedName(h))
                        .collect(Collectors.toList()))
                .kafkaTopicNames(hubNames)
                .kafkaTopicQualifiedNames(hubQualifiedNames);
    }

    /**
     * Generate a unique AzureEventHubConsumerGroup name.
     *
     * @param name of the AzureEventHubConsumerGroup
     * @param hubName simple name of the first AzureEventHubs in which the AzureEventHubConsumerGroup is contained
     * @return a unique name for the AzureEventHubConsumerGroup
     */
    public static String generateQualifiedName(String name, String connectionQualifiedName, String hubName) {
        return connectionQualifiedName + "/consumer-group/" + hubName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update an AzureEventHubConsumerGroup.
     *
     * @param qualifiedName of the AzureEventHubConsumerGroup
     * @param name of the AzureEventHubConsumerGroup
     * @return the minimal request necessary to update the AzureEventHubConsumerGroup, as a builder
     */
    public static AzureEventHubConsumerGroupBuilder<?, ?> updater(String qualifiedName, String name) {
        return AzureEventHubConsumerGroup._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to an AzureEventHubConsumerGroup, from a potentially
     * more-complete AzureEventHubConsumerGroup object.
     *
     * @return the minimal object necessary to update the AzureEventHubConsumerGroup, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AzureEventHubConsumerGroup are not found in the initial object
     */
    @Override
    public AzureEventHubConsumerGroupBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a AzureEventHubConsumerGroup.
     *
     * @param qualifiedName of the AzureEventHubConsumerGroup
     * @param name of the AzureEventHubConsumerGroup
     * @return the updated AzureEventHubConsumerGroup, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureEventHubConsumerGroup removeDescription(String qualifiedName, String name)
            throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a AzureEventHubConsumerGroup.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AzureEventHubConsumerGroup
     * @param name of the AzureEventHubConsumerGroup
     * @return the updated AzureEventHubConsumerGroup, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureEventHubConsumerGroup removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AzureEventHubConsumerGroup) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a AzureEventHubConsumerGroup.
     *
     * @param qualifiedName of the AzureEventHubConsumerGroup
     * @param name of the AzureEventHubConsumerGroup
     * @return the updated AzureEventHubConsumerGroup, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureEventHubConsumerGroup removeUserDescription(String qualifiedName, String name)
            throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a AzureEventHubConsumerGroup.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AzureEventHubConsumerGroup
     * @param name of the AzureEventHubConsumerGroup
     * @return the updated AzureEventHubConsumerGroup, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureEventHubConsumerGroup removeUserDescription(
            AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (AzureEventHubConsumerGroup) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a AzureEventHubConsumerGroup.
     *
     * @param qualifiedName of the AzureEventHubConsumerGroup
     * @param name of the AzureEventHubConsumerGroup
     * @return the updated AzureEventHubConsumerGroup, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureEventHubConsumerGroup removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a AzureEventHubConsumerGroup.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AzureEventHubConsumerGroup's owners
     * @param qualifiedName of the AzureEventHubConsumerGroup
     * @param name of the AzureEventHubConsumerGroup
     * @return the updated AzureEventHubConsumerGroup, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureEventHubConsumerGroup removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AzureEventHubConsumerGroup) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a AzureEventHubConsumerGroup.
     *
     * @param qualifiedName of the AzureEventHubConsumerGroup
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AzureEventHubConsumerGroup, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AzureEventHubConsumerGroup updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a AzureEventHubConsumerGroup.
     *
     * @param client connectivity to the Atlan tenant on which to update the AzureEventHubConsumerGroup's certificate
     * @param qualifiedName of the AzureEventHubConsumerGroup
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AzureEventHubConsumerGroup, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AzureEventHubConsumerGroup updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (AzureEventHubConsumerGroup)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a AzureEventHubConsumerGroup.
     *
     * @param qualifiedName of the AzureEventHubConsumerGroup
     * @param name of the AzureEventHubConsumerGroup
     * @return the updated AzureEventHubConsumerGroup, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureEventHubConsumerGroup removeCertificate(String qualifiedName, String name)
            throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a AzureEventHubConsumerGroup.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AzureEventHubConsumerGroup's certificate
     * @param qualifiedName of the AzureEventHubConsumerGroup
     * @param name of the AzureEventHubConsumerGroup
     * @return the updated AzureEventHubConsumerGroup, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureEventHubConsumerGroup removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AzureEventHubConsumerGroup) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a AzureEventHubConsumerGroup.
     *
     * @param qualifiedName of the AzureEventHubConsumerGroup
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AzureEventHubConsumerGroup updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a AzureEventHubConsumerGroup.
     *
     * @param client connectivity to the Atlan tenant on which to update the AzureEventHubConsumerGroup's announcement
     * @param qualifiedName of the AzureEventHubConsumerGroup
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AzureEventHubConsumerGroup updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (AzureEventHubConsumerGroup)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a AzureEventHubConsumerGroup.
     *
     * @param qualifiedName of the AzureEventHubConsumerGroup
     * @param name of the AzureEventHubConsumerGroup
     * @return the updated AzureEventHubConsumerGroup, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureEventHubConsumerGroup removeAnnouncement(String qualifiedName, String name)
            throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a AzureEventHubConsumerGroup.
     *
     * @param client connectivity to the Atlan client from which to remove the AzureEventHubConsumerGroup's announcement
     * @param qualifiedName of the AzureEventHubConsumerGroup
     * @param name of the AzureEventHubConsumerGroup
     * @return the updated AzureEventHubConsumerGroup, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureEventHubConsumerGroup removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AzureEventHubConsumerGroup) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the AzureEventHubConsumerGroup.
     *
     * @param qualifiedName for the AzureEventHubConsumerGroup
     * @param name human-readable name of the AzureEventHubConsumerGroup
     * @param terms the list of terms to replace on the AzureEventHubConsumerGroup, or null to remove all terms from the AzureEventHubConsumerGroup
     * @return the AzureEventHubConsumerGroup that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static AzureEventHubConsumerGroup replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the AzureEventHubConsumerGroup.
     *
     * @param client connectivity to the Atlan tenant on which to replace the AzureEventHubConsumerGroup's assigned terms
     * @param qualifiedName for the AzureEventHubConsumerGroup
     * @param name human-readable name of the AzureEventHubConsumerGroup
     * @param terms the list of terms to replace on the AzureEventHubConsumerGroup, or null to remove all terms from the AzureEventHubConsumerGroup
     * @return the AzureEventHubConsumerGroup that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static AzureEventHubConsumerGroup replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (AzureEventHubConsumerGroup) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the AzureEventHubConsumerGroup, without replacing existing terms linked to the AzureEventHubConsumerGroup.
     * Note: this operation must make two API calls — one to retrieve the AzureEventHubConsumerGroup's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the AzureEventHubConsumerGroup
     * @param terms the list of terms to append to the AzureEventHubConsumerGroup
     * @return the AzureEventHubConsumerGroup that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static AzureEventHubConsumerGroup appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the AzureEventHubConsumerGroup, without replacing existing terms linked to the AzureEventHubConsumerGroup.
     * Note: this operation must make two API calls — one to retrieve the AzureEventHubConsumerGroup's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the AzureEventHubConsumerGroup
     * @param qualifiedName for the AzureEventHubConsumerGroup
     * @param terms the list of terms to append to the AzureEventHubConsumerGroup
     * @return the AzureEventHubConsumerGroup that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static AzureEventHubConsumerGroup appendTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (AzureEventHubConsumerGroup) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a AzureEventHubConsumerGroup, without replacing all existing terms linked to the AzureEventHubConsumerGroup.
     * Note: this operation must make two API calls — one to retrieve the AzureEventHubConsumerGroup's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the AzureEventHubConsumerGroup
     * @param terms the list of terms to remove from the AzureEventHubConsumerGroup, which must be referenced by GUID
     * @return the AzureEventHubConsumerGroup that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static AzureEventHubConsumerGroup removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a AzureEventHubConsumerGroup, without replacing all existing terms linked to the AzureEventHubConsumerGroup.
     * Note: this operation must make two API calls — one to retrieve the AzureEventHubConsumerGroup's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the AzureEventHubConsumerGroup
     * @param qualifiedName for the AzureEventHubConsumerGroup
     * @param terms the list of terms to remove from the AzureEventHubConsumerGroup, which must be referenced by GUID
     * @return the AzureEventHubConsumerGroup that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static AzureEventHubConsumerGroup removeTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (AzureEventHubConsumerGroup) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a AzureEventHubConsumerGroup, without replacing existing Atlan tags linked to the AzureEventHubConsumerGroup.
     * Note: this operation must make two API calls — one to retrieve the AzureEventHubConsumerGroup's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the AzureEventHubConsumerGroup
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated AzureEventHubConsumerGroup
     */
    public static AzureEventHubConsumerGroup appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AzureEventHubConsumerGroup, without replacing existing Atlan tags linked to the AzureEventHubConsumerGroup.
     * Note: this operation must make two API calls — one to retrieve the AzureEventHubConsumerGroup's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AzureEventHubConsumerGroup
     * @param qualifiedName of the AzureEventHubConsumerGroup
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated AzureEventHubConsumerGroup
     */
    public static AzureEventHubConsumerGroup appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (AzureEventHubConsumerGroup) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AzureEventHubConsumerGroup, without replacing existing Atlan tags linked to the AzureEventHubConsumerGroup.
     * Note: this operation must make two API calls — one to retrieve the AzureEventHubConsumerGroup's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the AzureEventHubConsumerGroup
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated AzureEventHubConsumerGroup
     */
    public static AzureEventHubConsumerGroup appendAtlanTags(
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
     * Add Atlan tags to a AzureEventHubConsumerGroup, without replacing existing Atlan tags linked to the AzureEventHubConsumerGroup.
     * Note: this operation must make two API calls — one to retrieve the AzureEventHubConsumerGroup's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AzureEventHubConsumerGroup
     * @param qualifiedName of the AzureEventHubConsumerGroup
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated AzureEventHubConsumerGroup
     */
    public static AzureEventHubConsumerGroup appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (AzureEventHubConsumerGroup) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a AzureEventHubConsumerGroup.
     *
     * @param qualifiedName of the AzureEventHubConsumerGroup
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the AzureEventHubConsumerGroup
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a AzureEventHubConsumerGroup.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a AzureEventHubConsumerGroup
     * @param qualifiedName of the AzureEventHubConsumerGroup
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the AzureEventHubConsumerGroup
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
