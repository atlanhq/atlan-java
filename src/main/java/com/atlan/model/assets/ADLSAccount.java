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
import com.atlan.model.enums.ADLSAccessTier;
import com.atlan.model.enums.ADLSAccountStatus;
import com.atlan.model.enums.ADLSEncryptionTypes;
import com.atlan.model.enums.ADLSPerformance;
import com.atlan.model.enums.ADLSProvisionState;
import com.atlan.model.enums.ADLSReplicationType;
import com.atlan.model.enums.ADLSStorageKind;
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
 * Instance of an Azure Data Lake Storage (ADLS) account in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class ADLSAccount extends Asset
        implements IADLSAccount, IADLS, IObjectStore, IAzure, ICatalog, IAsset, IReferenceable, ICloud {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ADLSAccount";

    /** Fixed typeName for ADLSAccounts. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    ADLSAccessTier adlsAccountAccessTier;

    /** TBC */
    @Attribute
    ADLSStorageKind adlsAccountKind;

    /** TBC */
    @Attribute
    ADLSPerformance adlsAccountPerformance;

    /** TBC */
    @Attribute
    ADLSProvisionState adlsAccountProvisionState;

    /** TBC */
    @Attribute
    String adlsAccountQualifiedName;

    /** TBC */
    @Attribute
    ADLSReplicationType adlsAccountReplication;

    /** TBC */
    @Attribute
    String adlsAccountResourceGroup;

    /** TBC */
    @Attribute
    String adlsAccountSecondaryLocation;

    /** TBC */
    @Attribute
    String adlsAccountSubscription;

    /** Containers that exist within this account. */
    @Attribute
    @Singular
    SortedSet<IADLSContainer> adlsContainers;

    /** Entity tag for the asset. An entity tag is a hash of the object and represents changes to the contents of an object only, not its metadata. */
    @Attribute
    String adlsETag;

    /** TBC */
    @Attribute
    ADLSEncryptionTypes adlsEncryptionType;

    /** TBC */
    @Attribute
    ADLSAccountStatus adlsPrimaryDiskState;

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
     * Builds the minimal object necessary to create a relationship to a ADLSAccount, from a potentially
     * more-complete ADLSAccount object.
     *
     * @return the minimal object necessary to relate to the ADLSAccount
     * @throws InvalidRequestException if any of the minimal set of required properties for a ADLSAccount relationship are not found in the initial object
     */
    @Override
    public ADLSAccount trimToReference() throws InvalidRequestException {
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
     * Start an asset filter that will return all ADLSAccount assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ADLSAccount assets will be included.
     *
     * @return an asset filter that includes all ADLSAccount assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all ADLSAccount assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ADLSAccount assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all ADLSAccount assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all ADLSAccount assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) ADLSAccounts will be included
     * @return an asset filter that includes all ADLSAccount assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all ADLSAccount assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ADLSAccounts will be included
     * @return an asset filter that includes all ADLSAccount assets
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
     * Reference to a ADLSAccount by GUID.
     *
     * @param guid the GUID of the ADLSAccount to reference
     * @return reference to a ADLSAccount that can be used for defining a relationship to a ADLSAccount
     */
    public static ADLSAccount refByGuid(String guid) {
        return ADLSAccount._internal().guid(guid).build();
    }

    /**
     * Reference to a ADLSAccount by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the ADLSAccount to reference
     * @return reference to a ADLSAccount that can be used for defining a relationship to a ADLSAccount
     */
    public static ADLSAccount refByQualifiedName(String qualifiedName) {
        return ADLSAccount._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a ADLSAccount by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the ADLSAccount to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ADLSAccount, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSAccount does not exist or the provided GUID is not a ADLSAccount
     */
    @JsonIgnore
    public static ADLSAccount get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a ADLSAccount by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ADLSAccount to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ADLSAccount, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSAccount does not exist or the provided GUID is not a ADLSAccount
     */
    @JsonIgnore
    public static ADLSAccount get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a ADLSAccount by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ADLSAccount to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full ADLSAccount, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSAccount does not exist or the provided GUID is not a ADLSAccount
     */
    @JsonIgnore
    public static ADLSAccount get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof ADLSAccount) {
                return (ADLSAccount) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof ADLSAccount) {
                return (ADLSAccount) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a ADLSAccount by its GUID, complete with all of its relationships.
     *
     * @param guid of the ADLSAccount to retrieve
     * @return the requested full ADLSAccount, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSAccount does not exist or the provided GUID is not a ADLSAccount
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static ADLSAccount retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a ADLSAccount by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the ADLSAccount to retrieve
     * @return the requested full ADLSAccount, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSAccount does not exist or the provided GUID is not a ADLSAccount
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static ADLSAccount retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a ADLSAccount by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the ADLSAccount to retrieve
     * @return the requested full ADLSAccount, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSAccount does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static ADLSAccount retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a ADLSAccount by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the ADLSAccount to retrieve
     * @return the requested full ADLSAccount, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSAccount does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static ADLSAccount retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) ADLSAccount to active.
     *
     * @param qualifiedName for the ADLSAccount
     * @return true if the ADLSAccount is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) ADLSAccount to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the ADLSAccount
     * @return true if the ADLSAccount is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ADLSAccount.
     *
     * @param name of the ADLSAccount
     * @param connectionQualifiedName unique name of the connection through which the ADLSAccount is accessible
     * @return the minimal object necessary to create the ADLSAccount, as a builder
     */
    public static ADLSAccountBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return ADLSAccount._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.ADLS);
    }

    /**
     * Generate a unique ADLSAccount name.
     *
     * @param name of the ADLSAccount
     * @param connectionQualifiedName unique name of the connection through which the ADLSAccount is accessible
     * @return a unique name for the ADLSAccount
     */
    public static String generateQualifiedName(String name, String connectionQualifiedName) {
        return connectionQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a ADLSAccount.
     *
     * @param qualifiedName of the ADLSAccount
     * @param name of the ADLSAccount
     * @return the minimal request necessary to update the ADLSAccount, as a builder
     */
    public static ADLSAccountBuilder<?, ?> updater(String qualifiedName, String name) {
        return ADLSAccount._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ADLSAccount, from a potentially
     * more-complete ADLSAccount object.
     *
     * @return the minimal object necessary to update the ADLSAccount, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ADLSAccount are not found in the initial object
     */
    @Override
    public ADLSAccountBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "ADLSAccount", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a ADLSAccount.
     *
     * @param qualifiedName of the ADLSAccount
     * @param name of the ADLSAccount
     * @return the updated ADLSAccount, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a ADLSAccount.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ADLSAccount
     * @param name of the ADLSAccount
     * @return the updated ADLSAccount, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ADLSAccount) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ADLSAccount.
     *
     * @param qualifiedName of the ADLSAccount
     * @param name of the ADLSAccount
     * @return the updated ADLSAccount, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a ADLSAccount.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ADLSAccount
     * @param name of the ADLSAccount
     * @return the updated ADLSAccount, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ADLSAccount) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ADLSAccount.
     *
     * @param qualifiedName of the ADLSAccount
     * @param name of the ADLSAccount
     * @return the updated ADLSAccount, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a ADLSAccount.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ADLSAccount's owners
     * @param qualifiedName of the ADLSAccount
     * @param name of the ADLSAccount
     * @return the updated ADLSAccount, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ADLSAccount) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ADLSAccount.
     *
     * @param qualifiedName of the ADLSAccount
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ADLSAccount, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a ADLSAccount.
     *
     * @param client connectivity to the Atlan tenant on which to update the ADLSAccount's certificate
     * @param qualifiedName of the ADLSAccount
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ADLSAccount, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (ADLSAccount)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ADLSAccount.
     *
     * @param qualifiedName of the ADLSAccount
     * @param name of the ADLSAccount
     * @return the updated ADLSAccount, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a ADLSAccount.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ADLSAccount's certificate
     * @param qualifiedName of the ADLSAccount
     * @param name of the ADLSAccount
     * @return the updated ADLSAccount, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ADLSAccount) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ADLSAccount.
     *
     * @param qualifiedName of the ADLSAccount
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a ADLSAccount.
     *
     * @param client connectivity to the Atlan tenant on which to update the ADLSAccount's announcement
     * @param qualifiedName of the ADLSAccount
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (ADLSAccount)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ADLSAccount.
     *
     * @param qualifiedName of the ADLSAccount
     * @param name of the ADLSAccount
     * @return the updated ADLSAccount, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a ADLSAccount.
     *
     * @param client connectivity to the Atlan client from which to remove the ADLSAccount's announcement
     * @param qualifiedName of the ADLSAccount
     * @param name of the ADLSAccount
     * @return the updated ADLSAccount, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ADLSAccount) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the ADLSAccount.
     *
     * @param qualifiedName for the ADLSAccount
     * @param name human-readable name of the ADLSAccount
     * @param terms the list of terms to replace on the ADLSAccount, or null to remove all terms from the ADLSAccount
     * @return the ADLSAccount that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the ADLSAccount.
     *
     * @param client connectivity to the Atlan tenant on which to replace the ADLSAccount's assigned terms
     * @param qualifiedName for the ADLSAccount
     * @param name human-readable name of the ADLSAccount
     * @param terms the list of terms to replace on the ADLSAccount, or null to remove all terms from the ADLSAccount
     * @return the ADLSAccount that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (ADLSAccount) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ADLSAccount, without replacing existing terms linked to the ADLSAccount.
     * Note: this operation must make two API calls — one to retrieve the ADLSAccount's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the ADLSAccount
     * @param terms the list of terms to append to the ADLSAccount
     * @return the ADLSAccount that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the ADLSAccount, without replacing existing terms linked to the ADLSAccount.
     * Note: this operation must make two API calls — one to retrieve the ADLSAccount's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the ADLSAccount
     * @param qualifiedName for the ADLSAccount
     * @param terms the list of terms to append to the ADLSAccount
     * @return the ADLSAccount that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ADLSAccount) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ADLSAccount, without replacing all existing terms linked to the ADLSAccount.
     * Note: this operation must make two API calls — one to retrieve the ADLSAccount's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the ADLSAccount
     * @param terms the list of terms to remove from the ADLSAccount, which must be referenced by GUID
     * @return the ADLSAccount that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a ADLSAccount, without replacing all existing terms linked to the ADLSAccount.
     * Note: this operation must make two API calls — one to retrieve the ADLSAccount's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the ADLSAccount
     * @param qualifiedName for the ADLSAccount
     * @param terms the list of terms to remove from the ADLSAccount, which must be referenced by GUID
     * @return the ADLSAccount that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ADLSAccount) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a ADLSAccount, without replacing existing Atlan tags linked to the ADLSAccount.
     * Note: this operation must make two API calls — one to retrieve the ADLSAccount's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the ADLSAccount
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ADLSAccount
     */
    public static ADLSAccount appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ADLSAccount, without replacing existing Atlan tags linked to the ADLSAccount.
     * Note: this operation must make two API calls — one to retrieve the ADLSAccount's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ADLSAccount
     * @param qualifiedName of the ADLSAccount
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ADLSAccount
     */
    public static ADLSAccount appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (ADLSAccount) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ADLSAccount, without replacing existing Atlan tags linked to the ADLSAccount.
     * Note: this operation must make two API calls — one to retrieve the ADLSAccount's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the ADLSAccount
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ADLSAccount
     */
    public static ADLSAccount appendAtlanTags(
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
     * Add Atlan tags to a ADLSAccount, without replacing existing Atlan tags linked to the ADLSAccount.
     * Note: this operation must make two API calls — one to retrieve the ADLSAccount's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ADLSAccount
     * @param qualifiedName of the ADLSAccount
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ADLSAccount
     */
    public static ADLSAccount appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (ADLSAccount) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a ADLSAccount.
     *
     * @param qualifiedName of the ADLSAccount
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ADLSAccount
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ADLSAccount.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the ADLSAccount
     * @param qualifiedName of the ADLSAccount
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ADLSAccount
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ADLSAccount.
     *
     * @param qualifiedName of the ADLSAccount
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ADLSAccount
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
     * Add Atlan tags to a ADLSAccount.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the ADLSAccount
     * @param qualifiedName of the ADLSAccount
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ADLSAccount
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
     * Remove an Atlan tag from a ADLSAccount.
     *
     * @param qualifiedName of the ADLSAccount
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ADLSAccount
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a ADLSAccount.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a ADLSAccount
     * @param qualifiedName of the ADLSAccount
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ADLSAccount
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
