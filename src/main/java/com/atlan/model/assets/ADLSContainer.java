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
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of an Azure Data Lake Storage (ADLS) container in Atlan.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class ADLSContainer extends ADLS {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ADLSContainer";

    /** Fixed typeName for ADLSContainers. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String adlsContainerUrl;

    /** TBC */
    @Attribute
    ADLSLeaseState adlsContainerLeaseState;

    /** TBC */
    @Attribute
    ADLSLeaseStatus adlsContainerLeaseStatus;

    /** TBC */
    @Attribute
    String adlsContainerEncryptionScope;

    /** TBC */
    @Attribute
    Boolean adlsContainerVersionLevelImmutabilitySupport;

    /** TBC */
    @Attribute
    Integer adlsObjectCount;

    /** Objects that exist within this container. */
    @Attribute
    @Singular
    SortedSet<ADLSObject> adlsObjects;

    /** Account this container exists within. */
    @Attribute
    ADLSAccount adlsAccount;

    /**
     * Reference to a ADLSContainer by GUID.
     *
     * @param guid the GUID of the ADLSContainer to reference
     * @return reference to a ADLSContainer that can be used for defining a relationship to a ADLSContainer
     */
    public static ADLSContainer refByGuid(String guid) {
        return ADLSContainer.builder().guid(guid).build();
    }

    /**
     * Reference to a ADLSContainer by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the ADLSContainer to reference
     * @return reference to a ADLSContainer that can be used for defining a relationship to a ADLSContainer
     */
    public static ADLSContainer refByQualifiedName(String qualifiedName) {
        return ADLSContainer.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
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
        return ADLSContainer.builder()
                .qualifiedName(accountQualifiedName + "/" + name)
                .name(name)
                .adlsAccount(ADLSAccount.refByQualifiedName(accountQualifiedName))
                .adlsAccountQualifiedName(accountQualifiedName)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.ADLS);
    }

    /**
     * Builds the minimal object necessary to update a ADLSContainer.
     *
     * @param qualifiedName of the ADLSContainer
     * @param name of the ADLSContainer
     * @return the minimal request necessary to update the ADLSContainer, as a builder
     */
    public static ADLSContainerBuilder<?, ?> updater(String qualifiedName, String name) {
        return ADLSContainer.builder().qualifiedName(qualifiedName).name(name);
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
     * Retrieves a ADLSContainer by its GUID, complete with all of its relationships.
     *
     * @param guid of the ADLSContainer to retrieve
     * @return the requested full ADLSContainer, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSContainer does not exist or the provided GUID is not a ADLSContainer
     */
    public static ADLSContainer retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof ADLSContainer) {
            return (ADLSContainer) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "ADLSContainer");
        }
    }

    /**
     * Retrieves a ADLSContainer by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the ADLSContainer to retrieve
     * @return the requested full ADLSContainer, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSContainer does not exist
     */
    public static ADLSContainer retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof ADLSContainer) {
            return (ADLSContainer) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "ADLSContainer");
        }
    }

    /**
     * Restore the archived (soft-deleted) ADLSContainer to active.
     *
     * @param qualifiedName for the ADLSContainer
     * @return true if the ADLSContainer is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
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
        return (ADLSContainer) Asset.removeDescription(updater(qualifiedName, name));
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
        return (ADLSContainer) Asset.removeUserDescription(updater(qualifiedName, name));
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
        return (ADLSContainer) Asset.removeOwners(updater(qualifiedName, name));
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
    public static ADLSContainer updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (ADLSContainer) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
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
        return (ADLSContainer) Asset.removeCertificate(updater(qualifiedName, name));
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
        return (ADLSContainer) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
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
        return (ADLSContainer) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Add classifications to a ADLSContainer.
     *
     * @param qualifiedName of the ADLSContainer
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the ADLSContainer
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a ADLSContainer.
     *
     * @param qualifiedName of the ADLSContainer
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the ADLSContainer
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
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
    public static ADLSContainer replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (ADLSContainer) Asset.replaceTerms(updater(qualifiedName, name), terms);
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
    public static ADLSContainer appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (ADLSContainer) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
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
    public static ADLSContainer removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (ADLSContainer) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
