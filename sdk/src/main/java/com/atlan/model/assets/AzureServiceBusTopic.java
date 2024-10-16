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
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instances of AzureServiceBusField in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class AzureServiceBusTopic extends Asset
        implements IAzureServiceBusTopic, IAzureServiceBus, IEventStore, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AzureServiceBusTopic";

    /** Fixed typeName for AzureServiceBusTopics. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** AzureServiceBusNamespace asset containing this AzureServiceBusTopic. */
    @Attribute
    IAzureServiceBusNamespace azureServiceBusNamespace;

    /** Simple name of the AzureServiceBus Namespace in which this asset exists. */
    @Attribute
    String azureServiceBusNamespaceName;

    /** Unique name of the AzureServiceBus Namespace in which this asset exists. */
    @Attribute
    String azureServiceBusNamespaceQualifiedName;

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

    /** Entities implemented by this asset. */
    @Attribute
    @Singular
    @JsonProperty("modelEntityImplemented")
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
     * Builds the minimal object necessary to create a relationship to a AzureServiceBusTopic, from a potentially
     * more-complete AzureServiceBusTopic object.
     *
     * @return the minimal object necessary to relate to the AzureServiceBusTopic
     * @throws InvalidRequestException if any of the minimal set of required properties for a AzureServiceBusTopic relationship are not found in the initial object
     */
    @Override
    public AzureServiceBusTopic trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all AzureServiceBusTopic assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) AzureServiceBusTopic assets will be included.
     *
     * @return a fluent search that includes all AzureServiceBusTopic assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all AzureServiceBusTopic assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) AzureServiceBusTopic assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all AzureServiceBusTopic assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all AzureServiceBusTopic assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) AzureServiceBusTopics will be included
     * @return a fluent search that includes all AzureServiceBusTopic assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all AzureServiceBusTopic assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) AzureServiceBusTopics will be included
     * @return a fluent search that includes all AzureServiceBusTopic assets
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
     * Reference to a AzureServiceBusTopic by GUID. Use this to create a relationship to this AzureServiceBusTopic,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the AzureServiceBusTopic to reference
     * @return reference to a AzureServiceBusTopic that can be used for defining a relationship to a AzureServiceBusTopic
     */
    public static AzureServiceBusTopic refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AzureServiceBusTopic by GUID. Use this to create a relationship to this AzureServiceBusTopic,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the AzureServiceBusTopic to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AzureServiceBusTopic that can be used for defining a relationship to a AzureServiceBusTopic
     */
    public static AzureServiceBusTopic refByGuid(String guid, Reference.SaveSemantic semantic) {
        return AzureServiceBusTopic._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a AzureServiceBusTopic by qualifiedName. Use this to create a relationship to this AzureServiceBusTopic,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the AzureServiceBusTopic to reference
     * @return reference to a AzureServiceBusTopic that can be used for defining a relationship to a AzureServiceBusTopic
     */
    public static AzureServiceBusTopic refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AzureServiceBusTopic by qualifiedName. Use this to create a relationship to this AzureServiceBusTopic,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the AzureServiceBusTopic to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AzureServiceBusTopic that can be used for defining a relationship to a AzureServiceBusTopic
     */
    public static AzureServiceBusTopic refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return AzureServiceBusTopic._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a AzureServiceBusTopic by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the AzureServiceBusTopic to retrieve, either its GUID or its full qualifiedName
     * @return the requested full AzureServiceBusTopic, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AzureServiceBusTopic does not exist or the provided GUID is not a AzureServiceBusTopic
     */
    @JsonIgnore
    public static AzureServiceBusTopic get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a AzureServiceBusTopic by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AzureServiceBusTopic to retrieve, either its GUID or its full qualifiedName
     * @return the requested full AzureServiceBusTopic, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AzureServiceBusTopic does not exist or the provided GUID is not a AzureServiceBusTopic
     */
    @JsonIgnore
    public static AzureServiceBusTopic get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a AzureServiceBusTopic by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AzureServiceBusTopic to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full AzureServiceBusTopic, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AzureServiceBusTopic does not exist or the provided GUID is not a AzureServiceBusTopic
     */
    @JsonIgnore
    public static AzureServiceBusTopic get(AtlanClient client, String id, boolean includeRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof AzureServiceBusTopic) {
                return (AzureServiceBusTopic) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof AzureServiceBusTopic) {
                return (AzureServiceBusTopic) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) AzureServiceBusTopic to active.
     *
     * @param qualifiedName for the AzureServiceBusTopic
     * @return true if the AzureServiceBusTopic is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) AzureServiceBusTopic to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the AzureServiceBusTopic
     * @return true if the AzureServiceBusTopic is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a AzureServiceBusTopic.
     *
     * @param qualifiedName of the AzureServiceBusTopic
     * @param name of the AzureServiceBusTopic
     * @return the minimal request necessary to update the AzureServiceBusTopic, as a builder
     */
    public static AzureServiceBusTopicBuilder<?, ?> updater(String qualifiedName, String name) {
        return AzureServiceBusTopic._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AzureServiceBusTopic, from a potentially
     * more-complete AzureServiceBusTopic object.
     *
     * @return the minimal object necessary to update the AzureServiceBusTopic, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AzureServiceBusTopic are not found in the initial object
     */
    @Override
    public AzureServiceBusTopicBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a AzureServiceBusTopic.
     *
     * @param qualifiedName of the AzureServiceBusTopic
     * @param name of the AzureServiceBusTopic
     * @return the updated AzureServiceBusTopic, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusTopic removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a AzureServiceBusTopic.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AzureServiceBusTopic
     * @param name of the AzureServiceBusTopic
     * @return the updated AzureServiceBusTopic, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusTopic removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AzureServiceBusTopic) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a AzureServiceBusTopic.
     *
     * @param qualifiedName of the AzureServiceBusTopic
     * @param name of the AzureServiceBusTopic
     * @return the updated AzureServiceBusTopic, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusTopic removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a AzureServiceBusTopic.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AzureServiceBusTopic
     * @param name of the AzureServiceBusTopic
     * @return the updated AzureServiceBusTopic, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusTopic removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AzureServiceBusTopic) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a AzureServiceBusTopic.
     *
     * @param qualifiedName of the AzureServiceBusTopic
     * @param name of the AzureServiceBusTopic
     * @return the updated AzureServiceBusTopic, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusTopic removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a AzureServiceBusTopic.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AzureServiceBusTopic's owners
     * @param qualifiedName of the AzureServiceBusTopic
     * @param name of the AzureServiceBusTopic
     * @return the updated AzureServiceBusTopic, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusTopic removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AzureServiceBusTopic) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a AzureServiceBusTopic.
     *
     * @param qualifiedName of the AzureServiceBusTopic
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AzureServiceBusTopic, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusTopic updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a AzureServiceBusTopic.
     *
     * @param client connectivity to the Atlan tenant on which to update the AzureServiceBusTopic's certificate
     * @param qualifiedName of the AzureServiceBusTopic
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AzureServiceBusTopic, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusTopic updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (AzureServiceBusTopic)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a AzureServiceBusTopic.
     *
     * @param qualifiedName of the AzureServiceBusTopic
     * @param name of the AzureServiceBusTopic
     * @return the updated AzureServiceBusTopic, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusTopic removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a AzureServiceBusTopic.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AzureServiceBusTopic's certificate
     * @param qualifiedName of the AzureServiceBusTopic
     * @param name of the AzureServiceBusTopic
     * @return the updated AzureServiceBusTopic, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusTopic removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AzureServiceBusTopic) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a AzureServiceBusTopic.
     *
     * @param qualifiedName of the AzureServiceBusTopic
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusTopic updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a AzureServiceBusTopic.
     *
     * @param client connectivity to the Atlan tenant on which to update the AzureServiceBusTopic's announcement
     * @param qualifiedName of the AzureServiceBusTopic
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusTopic updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (AzureServiceBusTopic)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a AzureServiceBusTopic.
     *
     * @param qualifiedName of the AzureServiceBusTopic
     * @param name of the AzureServiceBusTopic
     * @return the updated AzureServiceBusTopic, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusTopic removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a AzureServiceBusTopic.
     *
     * @param client connectivity to the Atlan client from which to remove the AzureServiceBusTopic's announcement
     * @param qualifiedName of the AzureServiceBusTopic
     * @param name of the AzureServiceBusTopic
     * @return the updated AzureServiceBusTopic, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusTopic removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AzureServiceBusTopic) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the AzureServiceBusTopic.
     *
     * @param qualifiedName for the AzureServiceBusTopic
     * @param name human-readable name of the AzureServiceBusTopic
     * @param terms the list of terms to replace on the AzureServiceBusTopic, or null to remove all terms from the AzureServiceBusTopic
     * @return the AzureServiceBusTopic that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusTopic replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the AzureServiceBusTopic.
     *
     * @param client connectivity to the Atlan tenant on which to replace the AzureServiceBusTopic's assigned terms
     * @param qualifiedName for the AzureServiceBusTopic
     * @param name human-readable name of the AzureServiceBusTopic
     * @param terms the list of terms to replace on the AzureServiceBusTopic, or null to remove all terms from the AzureServiceBusTopic
     * @return the AzureServiceBusTopic that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusTopic replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (AzureServiceBusTopic) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the AzureServiceBusTopic, without replacing existing terms linked to the AzureServiceBusTopic.
     * Note: this operation must make two API calls — one to retrieve the AzureServiceBusTopic's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the AzureServiceBusTopic
     * @param terms the list of terms to append to the AzureServiceBusTopic
     * @return the AzureServiceBusTopic that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusTopic appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the AzureServiceBusTopic, without replacing existing terms linked to the AzureServiceBusTopic.
     * Note: this operation must make two API calls — one to retrieve the AzureServiceBusTopic's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the AzureServiceBusTopic
     * @param qualifiedName for the AzureServiceBusTopic
     * @param terms the list of terms to append to the AzureServiceBusTopic
     * @return the AzureServiceBusTopic that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusTopic appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AzureServiceBusTopic) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a AzureServiceBusTopic, without replacing all existing terms linked to the AzureServiceBusTopic.
     * Note: this operation must make two API calls — one to retrieve the AzureServiceBusTopic's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the AzureServiceBusTopic
     * @param terms the list of terms to remove from the AzureServiceBusTopic, which must be referenced by GUID
     * @return the AzureServiceBusTopic that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusTopic removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a AzureServiceBusTopic, without replacing all existing terms linked to the AzureServiceBusTopic.
     * Note: this operation must make two API calls — one to retrieve the AzureServiceBusTopic's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the AzureServiceBusTopic
     * @param qualifiedName for the AzureServiceBusTopic
     * @param terms the list of terms to remove from the AzureServiceBusTopic, which must be referenced by GUID
     * @return the AzureServiceBusTopic that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusTopic removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AzureServiceBusTopic) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a AzureServiceBusTopic, without replacing existing Atlan tags linked to the AzureServiceBusTopic.
     * Note: this operation must make two API calls — one to retrieve the AzureServiceBusTopic's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the AzureServiceBusTopic
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated AzureServiceBusTopic
     */
    public static AzureServiceBusTopic appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AzureServiceBusTopic, without replacing existing Atlan tags linked to the AzureServiceBusTopic.
     * Note: this operation must make two API calls — one to retrieve the AzureServiceBusTopic's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AzureServiceBusTopic
     * @param qualifiedName of the AzureServiceBusTopic
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated AzureServiceBusTopic
     */
    public static AzureServiceBusTopic appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (AzureServiceBusTopic) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AzureServiceBusTopic, without replacing existing Atlan tags linked to the AzureServiceBusTopic.
     * Note: this operation must make two API calls — one to retrieve the AzureServiceBusTopic's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the AzureServiceBusTopic
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated AzureServiceBusTopic
     */
    public static AzureServiceBusTopic appendAtlanTags(
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
     * Add Atlan tags to a AzureServiceBusTopic, without replacing existing Atlan tags linked to the AzureServiceBusTopic.
     * Note: this operation must make two API calls — one to retrieve the AzureServiceBusTopic's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AzureServiceBusTopic
     * @param qualifiedName of the AzureServiceBusTopic
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated AzureServiceBusTopic
     */
    public static AzureServiceBusTopic appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (AzureServiceBusTopic) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a AzureServiceBusTopic.
     *
     * @param qualifiedName of the AzureServiceBusTopic
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the AzureServiceBusTopic
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a AzureServiceBusTopic.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a AzureServiceBusTopic
     * @param qualifiedName of the AzureServiceBusTopic
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the AzureServiceBusTopic
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
