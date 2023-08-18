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
import com.atlan.model.enums.ADLSLeaseState;
import com.atlan.model.enums.ADLSLeaseStatus;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.structs.AzureTag;
import com.atlan.util.QueryFactory;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of an Azure Data Lake Storage (ADLS) container in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class ADLSContainer extends Asset
        implements IADLSContainer, IADLS, IObjectStore, IAzure, ICatalog, IAsset, IReferenceable, ICloud {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ADLSContainer";

    /** Fixed typeName for ADLSContainers. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Account this container exists within. */
    @Attribute
    IADLSAccount adlsAccount;

    /** TBC */
    @Attribute
    String adlsAccountQualifiedName;

    /** TBC */
    @Attribute
    String adlsAccountSecondaryLocation;

    /** TBC */
    @Attribute
    String adlsContainerEncryptionScope;

    /** TBC */
    @Attribute
    ADLSLeaseState adlsContainerLeaseState;

    /** TBC */
    @Attribute
    ADLSLeaseStatus adlsContainerLeaseStatus;

    /** TBC */
    @Attribute
    String adlsContainerUrl;

    /** TBC */
    @Attribute
    Boolean adlsContainerVersionLevelImmutabilitySupport;

    /** Number of objects that exist within this container. */
    @Attribute
    Integer adlsObjectCount;

    /** Objects that exist within this container. */
    @Attribute
    @Singular
    SortedSet<IADLSObject> adlsObjects;

    /** TBC */
    @Attribute
    String azureLocation;

    /** TBC */
    @Attribute
    String azureResourceId;

    /** TBC */
    @Attribute
    @Singular
    List<AzureTag> azureTags;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> inputToAirflowTasks;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> outputFromAirflowTasks;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /**
     * Builds the minimal object necessary to create a relationship to a ADLSContainer, from a potentially
     * more-complete ADLSContainer object.
     *
     * @return the minimal object necessary to relate to the ADLSContainer
     * @throws InvalidRequestException if any of the minimal set of required properties for a ADLSContainer relationship are not found in the initial object
     */
    @Override
    public ADLSContainer trimToReference() throws InvalidRequestException {
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
     * Start an asset filter that will return all ADLSContainer assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ADLSContainer assets will be included.
     *
     * @return an asset filter that includes all ADLSContainer assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all ADLSContainer assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ADLSContainer assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all ADLSContainer assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all ADLSContainer assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) ADLSContainers will be included
     * @return an asset filter that includes all ADLSContainer assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all ADLSContainer assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ADLSContainers will be included
     * @return an asset filter that includes all ADLSContainer assets
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
     * Reference to a ADLSContainer by GUID.
     *
     * @param guid the GUID of the ADLSContainer to reference
     * @return reference to a ADLSContainer that can be used for defining a relationship to a ADLSContainer
     */
    public static ADLSContainer refByGuid(String guid) {
        return ADLSContainer._internal().guid(guid).build();
    }

    /**
     * Reference to a ADLSContainer by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the ADLSContainer to reference
     * @return reference to a ADLSContainer that can be used for defining a relationship to a ADLSContainer
     */
    public static ADLSContainer refByQualifiedName(String qualifiedName) {
        return ADLSContainer._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a ADLSContainer by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the ADLSContainer to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ADLSContainer, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSContainer does not exist or the provided GUID is not a ADLSContainer
     */
    @JsonIgnore
    public static ADLSContainer get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a ADLSContainer by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ADLSContainer to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ADLSContainer, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSContainer does not exist or the provided GUID is not a ADLSContainer
     */
    @JsonIgnore
    public static ADLSContainer get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a ADLSContainer by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ADLSContainer to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full ADLSContainer, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSContainer does not exist or the provided GUID is not a ADLSContainer
     */
    @JsonIgnore
    public static ADLSContainer get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof ADLSContainer) {
                return (ADLSContainer) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof ADLSContainer) {
                return (ADLSContainer) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a ADLSContainer by its GUID, complete with all of its relationships.
     *
     * @param guid of the ADLSContainer to retrieve
     * @return the requested full ADLSContainer, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSContainer does not exist or the provided GUID is not a ADLSContainer
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static ADLSContainer retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a ADLSContainer by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the ADLSContainer to retrieve
     * @return the requested full ADLSContainer, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSContainer does not exist or the provided GUID is not a ADLSContainer
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static ADLSContainer retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a ADLSContainer by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the ADLSContainer to retrieve
     * @return the requested full ADLSContainer, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSContainer does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static ADLSContainer retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a ADLSContainer by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the ADLSContainer to retrieve
     * @return the requested full ADLSContainer, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSContainer does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static ADLSContainer retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) ADLSContainer to active.
     *
     * @param qualifiedName for the ADLSContainer
     * @return true if the ADLSContainer is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) ADLSContainer to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the ADLSContainer
     * @return true if the ADLSContainer is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ADLSContainer.
     *
     * @param name of the ADLSContainer
     * @param account in which the ADLSContainer should be created, which must have at least
     *                a qualifiedName
     * @return the minimal request necessary to create the ADLSContainer, as a builder
     * @throws InvalidRequestException if the container provided is without a qualifiedName
     */
    public static ADLSContainerBuilder<?, ?> creator(String name, ADLSAccount account) throws InvalidRequestException {
        if (account.getQualifiedName() == null || account.getQualifiedName().isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, "ADLSAccount", "qualifiedName");
        }
        return creator(name, account.getQualifiedName()).adlsAccount(account.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a ADLSContainer.
     *
     * @param name of the ADLSContainer
     * @param accountQualifiedName unique name of the account through which the ADLSContainer is accessible
     * @return the minimal object necessary to create the ADLSContainer, as a builder
     */
    public static ADLSContainerBuilder<?, ?> creator(String name, String accountQualifiedName) {
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(accountQualifiedName);
        return ADLSContainer._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, accountQualifiedName))
                .name(name)
                .adlsAccount(ADLSAccount.refByQualifiedName(accountQualifiedName))
                .adlsAccountQualifiedName(accountQualifiedName)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.ADLS);
    }

    /**
     * Generate a unique ADLSContainer name.
     *
     * @param name of the ADLSContainer
     * @param accountQualifiedName unique name of the account through which the ADLSContainer is accessible
     * @return a unique name for the ADLSContainer
     */
    public static String generateQualifiedName(String name, String accountQualifiedName) {
        return accountQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a ADLSContainer.
     *
     * @param qualifiedName of the ADLSContainer
     * @param name of the ADLSContainer
     * @return the minimal request necessary to update the ADLSContainer, as a builder
     */
    public static ADLSContainerBuilder<?, ?> updater(String qualifiedName, String name) {
        return ADLSContainer._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ADLSContainer, from a potentially
     * more-complete ADLSContainer object.
     *
     * @return the minimal object necessary to update the ADLSContainer, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ADLSContainer are not found in the initial object
     */
    @Override
    public ADLSContainerBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "ADLSContainer", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a ADLSContainer.
     *
     * @param qualifiedName of the ADLSContainer
     * @param name of the ADLSContainer
     * @return the updated ADLSContainer, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSContainer removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a ADLSContainer.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ADLSContainer
     * @param name of the ADLSContainer
     * @return the updated ADLSContainer, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSContainer removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ADLSContainer) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ADLSContainer.
     *
     * @param qualifiedName of the ADLSContainer
     * @param name of the ADLSContainer
     * @return the updated ADLSContainer, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSContainer removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a ADLSContainer.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ADLSContainer
     * @param name of the ADLSContainer
     * @return the updated ADLSContainer, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSContainer removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ADLSContainer) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ADLSContainer.
     *
     * @param qualifiedName of the ADLSContainer
     * @param name of the ADLSContainer
     * @return the updated ADLSContainer, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSContainer removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a ADLSContainer.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ADLSContainer's owners
     * @param qualifiedName of the ADLSContainer
     * @param name of the ADLSContainer
     * @return the updated ADLSContainer, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSContainer removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ADLSContainer) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ADLSContainer.
     *
     * @param qualifiedName of the ADLSContainer
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ADLSContainer, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ADLSContainer updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a ADLSContainer.
     *
     * @param client connectivity to the Atlan tenant on which to update the ADLSContainer's certificate
     * @param qualifiedName of the ADLSContainer
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ADLSContainer, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ADLSContainer updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (ADLSContainer)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ADLSContainer.
     *
     * @param qualifiedName of the ADLSContainer
     * @param name of the ADLSContainer
     * @return the updated ADLSContainer, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSContainer removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a ADLSContainer.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ADLSContainer's certificate
     * @param qualifiedName of the ADLSContainer
     * @param name of the ADLSContainer
     * @return the updated ADLSContainer, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSContainer removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ADLSContainer) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ADLSContainer.
     *
     * @param qualifiedName of the ADLSContainer
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ADLSContainer updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a ADLSContainer.
     *
     * @param client connectivity to the Atlan tenant on which to update the ADLSContainer's announcement
     * @param qualifiedName of the ADLSContainer
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ADLSContainer updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (ADLSContainer)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ADLSContainer.
     *
     * @param qualifiedName of the ADLSContainer
     * @param name of the ADLSContainer
     * @return the updated ADLSContainer, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSContainer removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a ADLSContainer.
     *
     * @param client connectivity to the Atlan client from which to remove the ADLSContainer's announcement
     * @param qualifiedName of the ADLSContainer
     * @param name of the ADLSContainer
     * @return the updated ADLSContainer, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSContainer removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ADLSContainer) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the ADLSContainer.
     *
     * @param qualifiedName for the ADLSContainer
     * @param name human-readable name of the ADLSContainer
     * @param terms the list of terms to replace on the ADLSContainer, or null to remove all terms from the ADLSContainer
     * @return the ADLSContainer that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ADLSContainer replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the ADLSContainer.
     *
     * @param client connectivity to the Atlan tenant on which to replace the ADLSContainer's assigned terms
     * @param qualifiedName for the ADLSContainer
     * @param name human-readable name of the ADLSContainer
     * @param terms the list of terms to replace on the ADLSContainer, or null to remove all terms from the ADLSContainer
     * @return the ADLSContainer that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ADLSContainer replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (ADLSContainer) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ADLSContainer, without replacing existing terms linked to the ADLSContainer.
     * Note: this operation must make two API calls — one to retrieve the ADLSContainer's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the ADLSContainer
     * @param terms the list of terms to append to the ADLSContainer
     * @return the ADLSContainer that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ADLSContainer appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the ADLSContainer, without replacing existing terms linked to the ADLSContainer.
     * Note: this operation must make two API calls — one to retrieve the ADLSContainer's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the ADLSContainer
     * @param qualifiedName for the ADLSContainer
     * @param terms the list of terms to append to the ADLSContainer
     * @return the ADLSContainer that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ADLSContainer appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ADLSContainer) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ADLSContainer, without replacing all existing terms linked to the ADLSContainer.
     * Note: this operation must make two API calls — one to retrieve the ADLSContainer's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the ADLSContainer
     * @param terms the list of terms to remove from the ADLSContainer, which must be referenced by GUID
     * @return the ADLSContainer that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ADLSContainer removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a ADLSContainer, without replacing all existing terms linked to the ADLSContainer.
     * Note: this operation must make two API calls — one to retrieve the ADLSContainer's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the ADLSContainer
     * @param qualifiedName for the ADLSContainer
     * @param terms the list of terms to remove from the ADLSContainer, which must be referenced by GUID
     * @return the ADLSContainer that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ADLSContainer removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ADLSContainer) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a ADLSContainer, without replacing existing Atlan tags linked to the ADLSContainer.
     * Note: this operation must make two API calls — one to retrieve the ADLSContainer's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the ADLSContainer
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ADLSContainer
     */
    public static ADLSContainer appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ADLSContainer, without replacing existing Atlan tags linked to the ADLSContainer.
     * Note: this operation must make two API calls — one to retrieve the ADLSContainer's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ADLSContainer
     * @param qualifiedName of the ADLSContainer
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ADLSContainer
     */
    public static ADLSContainer appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (ADLSContainer) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ADLSContainer, without replacing existing Atlan tags linked to the ADLSContainer.
     * Note: this operation must make two API calls — one to retrieve the ADLSContainer's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the ADLSContainer
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ADLSContainer
     */
    public static ADLSContainer appendAtlanTags(
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
     * Add Atlan tags to a ADLSContainer, without replacing existing Atlan tags linked to the ADLSContainer.
     * Note: this operation must make two API calls — one to retrieve the ADLSContainer's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ADLSContainer
     * @param qualifiedName of the ADLSContainer
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ADLSContainer
     */
    public static ADLSContainer appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (ADLSContainer) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a ADLSContainer.
     *
     * @param qualifiedName of the ADLSContainer
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ADLSContainer
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ADLSContainer.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the ADLSContainer
     * @param qualifiedName of the ADLSContainer
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ADLSContainer
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ADLSContainer.
     *
     * @param qualifiedName of the ADLSContainer
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ADLSContainer
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
     * Add Atlan tags to a ADLSContainer.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the ADLSContainer
     * @param qualifiedName of the ADLSContainer
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ADLSContainer
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
     * Remove an Atlan tag from a ADLSContainer.
     *
     * @param qualifiedName of the ADLSContainer
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ADLSContainer
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a ADLSContainer.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a ADLSContainer
     * @param qualifiedName of the ADLSContainer
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ADLSContainer
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
