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
 * Instances of AzureServiceBusNamespace in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class AzureServiceBusNamespace extends Asset
        implements IAzureServiceBusNamespace, IAzureServiceBus, IEventStore, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AzureServiceBusNamespace";

    /** Fixed typeName for AzureServiceBusNamespaces. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** ApplicationContainer asset containing this Catalog asset. */
    @Attribute
    IApplicationContainer applicationContainer;

    /** Qualified name of the Application Container that contains this asset. */
    @Attribute
    String assetApplicationQualifiedName;

    /** Simple name of the AzureServiceBus Namespace in which this asset exists. */
    @Attribute
    String azureServiceBusNamespaceName;

    /** Unique name of the AzureServiceBus Namespace in which this asset exists. */
    @Attribute
    String azureServiceBusNamespaceQualifiedName;

    /** AzureServiceBusTopic assets contained within this AzureServiceBusNamespace. */
    @Attribute
    @Singular
    SortedSet<IAzureServiceBusTopic> azureServiceBusTopics;

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
     * Builds the minimal object necessary to create a relationship to a AzureServiceBusNamespace, from a potentially
     * more-complete AzureServiceBusNamespace object.
     *
     * @return the minimal object necessary to relate to the AzureServiceBusNamespace
     * @throws InvalidRequestException if any of the minimal set of required properties for a AzureServiceBusNamespace relationship are not found in the initial object
     */
    @Override
    public AzureServiceBusNamespace trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all AzureServiceBusNamespace assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) AzureServiceBusNamespace assets will be included.
     *
     * @return a fluent search that includes all AzureServiceBusNamespace assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all AzureServiceBusNamespace assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) AzureServiceBusNamespace assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all AzureServiceBusNamespace assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all AzureServiceBusNamespace assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) AzureServiceBusNamespaces will be included
     * @return a fluent search that includes all AzureServiceBusNamespace assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all AzureServiceBusNamespace assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) AzureServiceBusNamespaces will be included
     * @return a fluent search that includes all AzureServiceBusNamespace assets
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
     * Reference to a AzureServiceBusNamespace by GUID. Use this to create a relationship to this AzureServiceBusNamespace,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the AzureServiceBusNamespace to reference
     * @return reference to a AzureServiceBusNamespace that can be used for defining a relationship to a AzureServiceBusNamespace
     */
    public static AzureServiceBusNamespace refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AzureServiceBusNamespace by GUID. Use this to create a relationship to this AzureServiceBusNamespace,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the AzureServiceBusNamespace to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AzureServiceBusNamespace that can be used for defining a relationship to a AzureServiceBusNamespace
     */
    public static AzureServiceBusNamespace refByGuid(String guid, Reference.SaveSemantic semantic) {
        return AzureServiceBusNamespace._internal()
                .guid(guid)
                .semantic(semantic)
                .build();
    }

    /**
     * Reference to a AzureServiceBusNamespace by qualifiedName. Use this to create a relationship to this AzureServiceBusNamespace,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the AzureServiceBusNamespace to reference
     * @return reference to a AzureServiceBusNamespace that can be used for defining a relationship to a AzureServiceBusNamespace
     */
    public static AzureServiceBusNamespace refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AzureServiceBusNamespace by qualifiedName. Use this to create a relationship to this AzureServiceBusNamespace,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the AzureServiceBusNamespace to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AzureServiceBusNamespace that can be used for defining a relationship to a AzureServiceBusNamespace
     */
    public static AzureServiceBusNamespace refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return AzureServiceBusNamespace._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a AzureServiceBusNamespace by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the AzureServiceBusNamespace to retrieve, either its GUID or its full qualifiedName
     * @return the requested full AzureServiceBusNamespace, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AzureServiceBusNamespace does not exist or the provided GUID is not a AzureServiceBusNamespace
     */
    @JsonIgnore
    public static AzureServiceBusNamespace get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a AzureServiceBusNamespace by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AzureServiceBusNamespace to retrieve, either its GUID or its full qualifiedName
     * @return the requested full AzureServiceBusNamespace, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AzureServiceBusNamespace does not exist or the provided GUID is not a AzureServiceBusNamespace
     */
    @JsonIgnore
    public static AzureServiceBusNamespace get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a AzureServiceBusNamespace by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AzureServiceBusNamespace to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full AzureServiceBusNamespace, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AzureServiceBusNamespace does not exist or the provided GUID is not a AzureServiceBusNamespace
     */
    @JsonIgnore
    public static AzureServiceBusNamespace get(AtlanClient client, String id, boolean includeRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof AzureServiceBusNamespace) {
                return (AzureServiceBusNamespace) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof AzureServiceBusNamespace) {
                return (AzureServiceBusNamespace) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) AzureServiceBusNamespace to active.
     *
     * @param qualifiedName for the AzureServiceBusNamespace
     * @return true if the AzureServiceBusNamespace is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) AzureServiceBusNamespace to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the AzureServiceBusNamespace
     * @return true if the AzureServiceBusNamespace is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a AzureServiceBusNamespace.
     *
     * @param qualifiedName of the AzureServiceBusNamespace
     * @param name of the AzureServiceBusNamespace
     * @return the minimal request necessary to update the AzureServiceBusNamespace, as a builder
     */
    public static AzureServiceBusNamespaceBuilder<?, ?> updater(String qualifiedName, String name) {
        return AzureServiceBusNamespace._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AzureServiceBusNamespace, from a potentially
     * more-complete AzureServiceBusNamespace object.
     *
     * @return the minimal object necessary to update the AzureServiceBusNamespace, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AzureServiceBusNamespace are not found in the initial object
     */
    @Override
    public AzureServiceBusNamespaceBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a AzureServiceBusNamespace.
     *
     * @param qualifiedName of the AzureServiceBusNamespace
     * @param name of the AzureServiceBusNamespace
     * @return the updated AzureServiceBusNamespace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusNamespace removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a AzureServiceBusNamespace.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AzureServiceBusNamespace
     * @param name of the AzureServiceBusNamespace
     * @return the updated AzureServiceBusNamespace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusNamespace removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AzureServiceBusNamespace) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a AzureServiceBusNamespace.
     *
     * @param qualifiedName of the AzureServiceBusNamespace
     * @param name of the AzureServiceBusNamespace
     * @return the updated AzureServiceBusNamespace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusNamespace removeUserDescription(String qualifiedName, String name)
            throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a AzureServiceBusNamespace.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AzureServiceBusNamespace
     * @param name of the AzureServiceBusNamespace
     * @return the updated AzureServiceBusNamespace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusNamespace removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AzureServiceBusNamespace) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a AzureServiceBusNamespace.
     *
     * @param qualifiedName of the AzureServiceBusNamespace
     * @param name of the AzureServiceBusNamespace
     * @return the updated AzureServiceBusNamespace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusNamespace removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a AzureServiceBusNamespace.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AzureServiceBusNamespace's owners
     * @param qualifiedName of the AzureServiceBusNamespace
     * @param name of the AzureServiceBusNamespace
     * @return the updated AzureServiceBusNamespace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusNamespace removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AzureServiceBusNamespace) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a AzureServiceBusNamespace.
     *
     * @param qualifiedName of the AzureServiceBusNamespace
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AzureServiceBusNamespace, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusNamespace updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a AzureServiceBusNamespace.
     *
     * @param client connectivity to the Atlan tenant on which to update the AzureServiceBusNamespace's certificate
     * @param qualifiedName of the AzureServiceBusNamespace
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AzureServiceBusNamespace, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusNamespace updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (AzureServiceBusNamespace)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a AzureServiceBusNamespace.
     *
     * @param qualifiedName of the AzureServiceBusNamespace
     * @param name of the AzureServiceBusNamespace
     * @return the updated AzureServiceBusNamespace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusNamespace removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a AzureServiceBusNamespace.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AzureServiceBusNamespace's certificate
     * @param qualifiedName of the AzureServiceBusNamespace
     * @param name of the AzureServiceBusNamespace
     * @return the updated AzureServiceBusNamespace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusNamespace removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AzureServiceBusNamespace) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a AzureServiceBusNamespace.
     *
     * @param qualifiedName of the AzureServiceBusNamespace
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusNamespace updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a AzureServiceBusNamespace.
     *
     * @param client connectivity to the Atlan tenant on which to update the AzureServiceBusNamespace's announcement
     * @param qualifiedName of the AzureServiceBusNamespace
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusNamespace updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (AzureServiceBusNamespace)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a AzureServiceBusNamespace.
     *
     * @param qualifiedName of the AzureServiceBusNamespace
     * @param name of the AzureServiceBusNamespace
     * @return the updated AzureServiceBusNamespace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusNamespace removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a AzureServiceBusNamespace.
     *
     * @param client connectivity to the Atlan client from which to remove the AzureServiceBusNamespace's announcement
     * @param qualifiedName of the AzureServiceBusNamespace
     * @param name of the AzureServiceBusNamespace
     * @return the updated AzureServiceBusNamespace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusNamespace removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AzureServiceBusNamespace) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the AzureServiceBusNamespace.
     *
     * @param qualifiedName for the AzureServiceBusNamespace
     * @param name human-readable name of the AzureServiceBusNamespace
     * @param terms the list of terms to replace on the AzureServiceBusNamespace, or null to remove all terms from the AzureServiceBusNamespace
     * @return the AzureServiceBusNamespace that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusNamespace replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the AzureServiceBusNamespace.
     *
     * @param client connectivity to the Atlan tenant on which to replace the AzureServiceBusNamespace's assigned terms
     * @param qualifiedName for the AzureServiceBusNamespace
     * @param name human-readable name of the AzureServiceBusNamespace
     * @param terms the list of terms to replace on the AzureServiceBusNamespace, or null to remove all terms from the AzureServiceBusNamespace
     * @return the AzureServiceBusNamespace that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusNamespace replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (AzureServiceBusNamespace) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the AzureServiceBusNamespace, without replacing existing terms linked to the AzureServiceBusNamespace.
     * Note: this operation must make two API calls — one to retrieve the AzureServiceBusNamespace's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the AzureServiceBusNamespace
     * @param terms the list of terms to append to the AzureServiceBusNamespace
     * @return the AzureServiceBusNamespace that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusNamespace appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the AzureServiceBusNamespace, without replacing existing terms linked to the AzureServiceBusNamespace.
     * Note: this operation must make two API calls — one to retrieve the AzureServiceBusNamespace's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the AzureServiceBusNamespace
     * @param qualifiedName for the AzureServiceBusNamespace
     * @param terms the list of terms to append to the AzureServiceBusNamespace
     * @return the AzureServiceBusNamespace that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusNamespace appendTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (AzureServiceBusNamespace) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a AzureServiceBusNamespace, without replacing all existing terms linked to the AzureServiceBusNamespace.
     * Note: this operation must make two API calls — one to retrieve the AzureServiceBusNamespace's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the AzureServiceBusNamespace
     * @param terms the list of terms to remove from the AzureServiceBusNamespace, which must be referenced by GUID
     * @return the AzureServiceBusNamespace that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusNamespace removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a AzureServiceBusNamespace, without replacing all existing terms linked to the AzureServiceBusNamespace.
     * Note: this operation must make two API calls — one to retrieve the AzureServiceBusNamespace's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the AzureServiceBusNamespace
     * @param qualifiedName for the AzureServiceBusNamespace
     * @param terms the list of terms to remove from the AzureServiceBusNamespace, which must be referenced by GUID
     * @return the AzureServiceBusNamespace that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static AzureServiceBusNamespace removeTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (AzureServiceBusNamespace) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a AzureServiceBusNamespace, without replacing existing Atlan tags linked to the AzureServiceBusNamespace.
     * Note: this operation must make two API calls — one to retrieve the AzureServiceBusNamespace's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the AzureServiceBusNamespace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated AzureServiceBusNamespace
     */
    public static AzureServiceBusNamespace appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AzureServiceBusNamespace, without replacing existing Atlan tags linked to the AzureServiceBusNamespace.
     * Note: this operation must make two API calls — one to retrieve the AzureServiceBusNamespace's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AzureServiceBusNamespace
     * @param qualifiedName of the AzureServiceBusNamespace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated AzureServiceBusNamespace
     */
    public static AzureServiceBusNamespace appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (AzureServiceBusNamespace) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AzureServiceBusNamespace, without replacing existing Atlan tags linked to the AzureServiceBusNamespace.
     * Note: this operation must make two API calls — one to retrieve the AzureServiceBusNamespace's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the AzureServiceBusNamespace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated AzureServiceBusNamespace
     */
    public static AzureServiceBusNamespace appendAtlanTags(
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
     * Add Atlan tags to a AzureServiceBusNamespace, without replacing existing Atlan tags linked to the AzureServiceBusNamespace.
     * Note: this operation must make two API calls — one to retrieve the AzureServiceBusNamespace's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AzureServiceBusNamespace
     * @param qualifiedName of the AzureServiceBusNamespace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated AzureServiceBusNamespace
     */
    public static AzureServiceBusNamespace appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (AzureServiceBusNamespace) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a AzureServiceBusNamespace.
     *
     * @param qualifiedName of the AzureServiceBusNamespace
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the AzureServiceBusNamespace
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a AzureServiceBusNamespace.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a AzureServiceBusNamespace
     * @param qualifiedName of the AzureServiceBusNamespace
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the AzureServiceBusNamespace
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
