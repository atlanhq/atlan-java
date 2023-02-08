/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.*;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of an Azure Data Lake Storage (ADLS) blob / object in Atlan.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("cast")
public class ADLSObject extends ADLS {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ADLSObject";

    /** Fixed typeName for ADLSObjects. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String adlsObjectUrl;

    /** TBC */
    @Attribute
    Long adlsObjectCreationTime;

    /** TBC */
    @Attribute
    Long adlsObjectLastModifiedTime;

    /** TBC */
    @Attribute
    String adlsObjectVersionId;

    /** TBC */
    @Attribute
    ADLSObjectType adlsObjectType;

    /** TBC */
    @Attribute
    Long adlsObjectSize;

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
    Boolean adlsObjectServerEncrypted;

    /** TBC */
    @Attribute
    Boolean adlsObjectVersionLevelImmutabilitySupport;

    /** TBC */
    @Attribute
    String adlsObjectCacheControl;

    /** TBC */
    @Attribute
    String adlsObjectContentType;

    /** TBC */
    @Attribute
    String adlsObjectContentMD5Hash;

    /** TBC */
    @Attribute
    String adlsObjectContentLanguage;

    /** TBC */
    @Attribute
    ADLSLeaseStatus adlsObjectLeaseStatus;

    /** TBC */
    @Attribute
    ADLSLeaseState adlsObjectLeaseState;

    /** TBC */
    @Attribute
    @Singular("putAdlsObjectMetadata")
    Map<String, String> adlsObjectMetadata;

    /** Container this object exists within. */
    @Attribute
    ADLSContainer adlsContainer;

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
     * Builds the minimal object necessary to create a ADLSObject.
     *
     * @param name of the ADLSObject
     * @param containerQualifiedName unique name of the container through which the ADLSObject is accessible
     * @return the minimal object necessary to create the ADLSObject, as a builder
     */
    public static ADLSObjectBuilder<?, ?> creator(String name, String containerQualifiedName) {
        String accountQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(containerQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(accountQualifiedName);
        return ADLSObject.builder()
                .qualifiedName(containerQualifiedName + "/" + name)
                .name(name)
                .adlsContainer(ADLSContainer.refByQualifiedName(containerQualifiedName))
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.ADLS);
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
     * Retrieves a ADLSObject by its GUID, complete with all of its relationships.
     *
     * @param guid of the ADLSObject to retrieve
     * @return the requested full ADLSObject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSObject does not exist or the provided GUID is not a ADLSObject
     */
    public static ADLSObject retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
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
     */
    public static ADLSObject retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
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
        return Asset.restore(TYPE_NAME, qualifiedName);
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
        return (ADLSObject) Asset.removeDescription(updater(qualifiedName, name));
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
        return (ADLSObject) Asset.removeUserDescription(updater(qualifiedName, name));
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
        return (ADLSObject) Asset.removeOwners(updater(qualifiedName, name));
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
    public static ADLSObject updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (ADLSObject) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
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
        return (ADLSObject) Asset.removeCertificate(updater(qualifiedName, name));
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
        return (ADLSObject) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
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
        return (ADLSObject) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Add classifications to a ADLSObject.
     *
     * @param qualifiedName of the ADLSObject
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the ADLSObject
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a ADLSObject.
     *
     * @param qualifiedName of the ADLSObject
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the ADLSObject
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
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
    public static ADLSObject replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (ADLSObject) Asset.replaceTerms(updater(qualifiedName, name), terms);
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
    public static ADLSObject appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (ADLSObject) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
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
    public static ADLSObject removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (ADLSObject) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
