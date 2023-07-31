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
import com.atlan.model.enums.ADLSLeaseState;
import com.atlan.model.enums.ADLSLeaseStatus;
import com.atlan.model.enums.ADLSObjectArchiveStatus;
import com.atlan.model.enums.ADLSObjectType;
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
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of an Azure Data Lake Storage (ADLS) blob / object in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class ADLSObject extends Asset
        implements IADLSObject, IADLS, IObjectStore, IAzure, ICatalog, IAsset, IReferenceable, ICloud {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ADLSObject";

    /** Fixed typeName for ADLSObjects. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String adlsAccountQualifiedName;

    /** TBC */
    @Attribute
    String adlsAccountSecondaryLocation;

    /** Container this object exists within. */
    @Attribute
    IADLSContainer adlsContainer;

    /** Unique name of the container this object exists within. */
    @Attribute
    String adlsContainerQualifiedName;

    /** TBC */
    @Attribute
    ADLSAccessTier adlsObjectAccessTier;

    /** TBC */
    @Attribute
    Long adlsObjectAccessTierLastModifiedTime;

    /** TBC */
    @Attribute
    ADLSObjectArchiveStatus adlsObjectArchiveStatus;

    /** TBC */
    @Attribute
    String adlsObjectCacheControl;

    /** TBC */
    @Attribute
    String adlsObjectContentLanguage;

    /** TBC */
    @Attribute
    String adlsObjectContentMD5Hash;

    /** TBC */
    @Attribute
    String adlsObjectContentType;

    /** TBC */
    @Attribute
    ADLSLeaseState adlsObjectLeaseState;

    /** TBC */
    @Attribute
    ADLSLeaseStatus adlsObjectLeaseStatus;

    /** TBC */
    @Attribute
    @Singular("putAdlsObjectMetadata")
    Map<String, String> adlsObjectMetadata;

    /** TBC */
    @Attribute
    Boolean adlsObjectServerEncrypted;

    /** TBC */
    @Attribute
    Long adlsObjectSize;

    /** TBC */
    @Attribute
    ADLSObjectType adlsObjectType;

    /** TBC */
    @Attribute
    String adlsObjectUrl;

    /** TBC */
    @Attribute
    String adlsObjectVersionId;

    /** TBC */
    @Attribute
    Boolean adlsObjectVersionLevelImmutabilitySupport;

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
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /**
     * Start an asset filter that will return all ADLSObject assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ADLSObject assets will be included.
     *
     * @return an asset filter that includes all ADLSObject assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all ADLSObject assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ADLSObject assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all ADLSObject assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all ADLSObject assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) ADLSObjects will be included
     * @return an asset filter that includes all ADLSObject assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all ADLSObject assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ADLSObjects will be included
     * @return an asset filter that includes all ADLSObject assets
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
     * Reference to a ADLSObject by GUID.
     *
     * @param guid the GUID of the ADLSObject to reference
     * @return reference to a ADLSObject that can be used for defining a relationship to a ADLSObject
     */
    public static ADLSObject refByGuid(String guid) {
        return ADLSObject.builder().guid(guid).build();
    }

    /**
     * Reference to a ADLSObject by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the ADLSObject to reference
     * @return reference to a ADLSObject that can be used for defining a relationship to a ADLSObject
     */
    public static ADLSObject refByQualifiedName(String qualifiedName) {
        return ADLSObject.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a ADLSObject by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the ADLSObject to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ADLSObject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSObject does not exist or the provided GUID is not a ADLSObject
     */
    @JsonIgnore
    public static ADLSObject get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a ADLSObject by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ADLSObject to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ADLSObject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSObject does not exist or the provided GUID is not a ADLSObject
     */
    @JsonIgnore
    public static ADLSObject get(AtlanClient client, String id) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (id.startsWith("default")) {
            Asset asset = Asset.retrieveFull(client, TYPE_NAME, id);
            if (asset instanceof ADLSObject) {
                return (ADLSObject) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, "ADLSObject");
            }
        } else {
            Asset asset = Asset.retrieveFull(client, id);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof ADLSObject) {
                return (ADLSObject) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, "ADLSObject");
            }
        }
    }

    /**
     * Retrieves a ADLSObject by its GUID, complete with all of its relationships.
     *
     * @param guid of the ADLSObject to retrieve
     * @return the requested full ADLSObject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSObject does not exist or the provided GUID is not a ADLSObject
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static ADLSObject retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a ADLSObject by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the ADLSObject to retrieve
     * @return the requested full ADLSObject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSObject does not exist or the provided GUID is not a ADLSObject
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static ADLSObject retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof ADLSObject) {
            return (ADLSObject) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "ADLSObject");
        }
    }

    /**
     * Retrieves a ADLSObject by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the ADLSObject to retrieve
     * @return the requested full ADLSObject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSObject does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static ADLSObject retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a ADLSObject by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the ADLSObject to retrieve
     * @return the requested full ADLSObject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSObject does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static ADLSObject retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof ADLSObject) {
            return (ADLSObject) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "ADLSObject");
        }
    }

    /**
     * Restore the archived (soft-deleted) ADLSObject to active.
     *
     * @param qualifiedName for the ADLSObject
     * @return true if the ADLSObject is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) ADLSObject to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the ADLSObject
     * @return true if the ADLSObject is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ADLSObject.
     *
     * @param name of the ADLSObject
     * @param containerQualifiedName unique name of the container through which the ADLSObject is accessible
     * @return the minimal object necessary to create the ADLSObject, as a builder
     */
    public static ADLSObjectBuilder<?, ?> creator(String name, String containerQualifiedName) {
        String accountQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(containerQualifiedName);
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(containerQualifiedName);
        return ADLSObject.builder()
                .qualifiedName(generateQualifiedName(name, containerQualifiedName))
                .name(name)
                .adlsContainer(ADLSContainer.refByQualifiedName(containerQualifiedName))
                .adlsAccountQualifiedName(accountQualifiedName)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.ADLS);
    }

    /**
     * Generate a unique ADLSObject name.
     *
     * @param name of the ADLSObject
     * @param containerQualifiedName unique name of the container through which the ADLSObject is accessible
     * @return a unique name for the ADLSObject
     */
    public static String generateQualifiedName(String name, String containerQualifiedName) {
        return containerQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a ADLSObject.
     *
     * @param qualifiedName of the ADLSObject
     * @param name of the ADLSObject
     * @return the minimal request necessary to update the ADLSObject, as a builder
     */
    public static ADLSObjectBuilder<?, ?> updater(String qualifiedName, String name) {
        return ADLSObject.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ADLSObject, from a potentially
     * more-complete ADLSObject object.
     *
     * @return the minimal object necessary to update the ADLSObject, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ADLSObject are not found in the initial object
     */
    @Override
    public ADLSObjectBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "ADLSObject", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a ADLSObject.
     *
     * @param qualifiedName of the ADLSObject
     * @param name of the ADLSObject
     * @return the updated ADLSObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSObject removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a ADLSObject.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ADLSObject
     * @param name of the ADLSObject
     * @return the updated ADLSObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSObject removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ADLSObject) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ADLSObject.
     *
     * @param qualifiedName of the ADLSObject
     * @param name of the ADLSObject
     * @return the updated ADLSObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSObject removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a ADLSObject.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ADLSObject
     * @param name of the ADLSObject
     * @return the updated ADLSObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSObject removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ADLSObject) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ADLSObject.
     *
     * @param qualifiedName of the ADLSObject
     * @param name of the ADLSObject
     * @return the updated ADLSObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSObject removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a ADLSObject.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ADLSObject's owners
     * @param qualifiedName of the ADLSObject
     * @param name of the ADLSObject
     * @return the updated ADLSObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSObject removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (ADLSObject) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ADLSObject.
     *
     * @param qualifiedName of the ADLSObject
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ADLSObject, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ADLSObject updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a ADLSObject.
     *
     * @param client connectivity to the Atlan tenant on which to update the ADLSObject's certificate
     * @param qualifiedName of the ADLSObject
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ADLSObject, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ADLSObject updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (ADLSObject) Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ADLSObject.
     *
     * @param qualifiedName of the ADLSObject
     * @param name of the ADLSObject
     * @return the updated ADLSObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSObject removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a ADLSObject.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ADLSObject's certificate
     * @param qualifiedName of the ADLSObject
     * @param name of the ADLSObject
     * @return the updated ADLSObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSObject removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ADLSObject) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ADLSObject.
     *
     * @param qualifiedName of the ADLSObject
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ADLSObject updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a ADLSObject.
     *
     * @param client connectivity to the Atlan tenant on which to update the ADLSObject's announcement
     * @param qualifiedName of the ADLSObject
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ADLSObject updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (ADLSObject) Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ADLSObject.
     *
     * @param qualifiedName of the ADLSObject
     * @param name of the ADLSObject
     * @return the updated ADLSObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSObject removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a ADLSObject.
     *
     * @param client connectivity to the Atlan client from which to remove the ADLSObject's announcement
     * @param qualifiedName of the ADLSObject
     * @param name of the ADLSObject
     * @return the updated ADLSObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSObject removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ADLSObject) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the ADLSObject.
     *
     * @param qualifiedName for the ADLSObject
     * @param name human-readable name of the ADLSObject
     * @param terms the list of terms to replace on the ADLSObject, or null to remove all terms from the ADLSObject
     * @return the ADLSObject that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ADLSObject replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the ADLSObject.
     *
     * @param client connectivity to the Atlan tenant on which to replace the ADLSObject's assigned terms
     * @param qualifiedName for the ADLSObject
     * @param name human-readable name of the ADLSObject
     * @param terms the list of terms to replace on the ADLSObject, or null to remove all terms from the ADLSObject
     * @return the ADLSObject that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ADLSObject replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (ADLSObject) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ADLSObject, without replacing existing terms linked to the ADLSObject.
     * Note: this operation must make two API calls — one to retrieve the ADLSObject's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the ADLSObject
     * @param terms the list of terms to append to the ADLSObject
     * @return the ADLSObject that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ADLSObject appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the ADLSObject, without replacing existing terms linked to the ADLSObject.
     * Note: this operation must make two API calls — one to retrieve the ADLSObject's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the ADLSObject
     * @param qualifiedName for the ADLSObject
     * @param terms the list of terms to append to the ADLSObject
     * @return the ADLSObject that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ADLSObject appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ADLSObject) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ADLSObject, without replacing all existing terms linked to the ADLSObject.
     * Note: this operation must make two API calls — one to retrieve the ADLSObject's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the ADLSObject
     * @param terms the list of terms to remove from the ADLSObject, which must be referenced by GUID
     * @return the ADLSObject that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ADLSObject removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a ADLSObject, without replacing all existing terms linked to the ADLSObject.
     * Note: this operation must make two API calls — one to retrieve the ADLSObject's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the ADLSObject
     * @param qualifiedName for the ADLSObject
     * @param terms the list of terms to remove from the ADLSObject, which must be referenced by GUID
     * @return the ADLSObject that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ADLSObject removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ADLSObject) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a ADLSObject, without replacing existing Atlan tags linked to the ADLSObject.
     * Note: this operation must make two API calls — one to retrieve the ADLSObject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the ADLSObject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ADLSObject
     */
    public static ADLSObject appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ADLSObject, without replacing existing Atlan tags linked to the ADLSObject.
     * Note: this operation must make two API calls — one to retrieve the ADLSObject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ADLSObject
     * @param qualifiedName of the ADLSObject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ADLSObject
     */
    public static ADLSObject appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (ADLSObject) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ADLSObject, without replacing existing Atlan tags linked to the ADLSObject.
     * Note: this operation must make two API calls — one to retrieve the ADLSObject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the ADLSObject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ADLSObject
     */
    public static ADLSObject appendAtlanTags(
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
     * Add Atlan tags to a ADLSObject, without replacing existing Atlan tags linked to the ADLSObject.
     * Note: this operation must make two API calls — one to retrieve the ADLSObject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ADLSObject
     * @param qualifiedName of the ADLSObject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ADLSObject
     */
    public static ADLSObject appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (ADLSObject) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a ADLSObject.
     *
     * @param qualifiedName of the ADLSObject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ADLSObject
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ADLSObject.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the ADLSObject
     * @param qualifiedName of the ADLSObject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ADLSObject
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ADLSObject.
     *
     * @param qualifiedName of the ADLSObject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ADLSObject
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
     * Add Atlan tags to a ADLSObject.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the ADLSObject
     * @param qualifiedName of the ADLSObject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ADLSObject
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
     * Remove an Atlan tag from a ADLSObject.
     *
     * @param qualifiedName of the ADLSObject
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ADLSObject
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a ADLSObject.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a ADLSObject
     * @param qualifiedName of the ADLSObject
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ADLSObject
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
