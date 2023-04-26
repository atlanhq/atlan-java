/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of an Azure Data Lake Storage (ADLS) account in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class ADLSAccount extends ADLS {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ADLSAccount";

    /** Fixed typeName for ADLSAccounts. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Entity tag for the asset. An entity tag is a hash of the object and represents changes to the contents of an object only, not its metadata. */
    @Attribute
    String adlsETag;

    /** TBC */
    @Attribute
    ADLSEncryptionTypes adlsEncryptionType;

    /** TBC */
    @Attribute
    String adlsAccountResourceGroup;

    /** TBC */
    @Attribute
    String adlsAccountSubscription;

    /** TBC */
    @Attribute
    ADLSPerformance adlsAccountPerformance;

    /** TBC */
    @Attribute
    ADLSReplicationType adlsAccountReplication;

    /** TBC */
    @Attribute
    ADLSStorageKind adlsAccountKind;

    /** TBC */
    @Attribute
    ADLSAccountStatus adlsPrimaryDiskState;

    /** TBC */
    @Attribute
    ADLSProvisionState adlsAccountProvisionState;

    /** TBC */
    @Attribute
    ADLSAccessTier adlsAccountAccessTier;

    /** Containers that exist within this account. */
    @Attribute
    @Singular
    SortedSet<ADLSContainer> adlsContainers;

    /**
     * Reference to a ADLSAccount by GUID.
     *
     * @param guid the GUID of the ADLSAccount to reference
     * @return reference to a ADLSAccount that can be used for defining a relationship to a ADLSAccount
     */
    public static ADLSAccount refByGuid(String guid) {
        return ADLSAccount.builder().guid(guid).build();
    }

    /**
     * Reference to a ADLSAccount by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the ADLSAccount to reference
     * @return reference to a ADLSAccount that can be used for defining a relationship to a ADLSAccount
     */
    public static ADLSAccount refByQualifiedName(String qualifiedName) {
        return ADLSAccount.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a ADLSAccount by its GUID, complete with all of its relationships.
     *
     * @param guid of the ADLSAccount to retrieve
     * @return the requested full ADLSAccount, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSAccount does not exist or the provided GUID is not a ADLSAccount
     */
    public static ADLSAccount retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof ADLSAccount) {
            return (ADLSAccount) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "ADLSAccount");
        }
    }

    /**
     * Retrieves a ADLSAccount by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the ADLSAccount to retrieve
     * @return the requested full ADLSAccount, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSAccount does not exist
     */
    public static ADLSAccount retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof ADLSAccount) {
            return (ADLSAccount) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "ADLSAccount");
        }
    }

    /**
     * Restore the archived (soft-deleted) ADLSAccount to active.
     *
     * @param qualifiedName for the ADLSAccount
     * @return true if the ADLSAccount is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ADLSAccount.
     *
     * @param name of the ADLSAccount
     * @param connectionQualifiedName unique name of the connection through which the ADLSAccount is accessible
     * @return the minimal object necessary to create the ADLSAccount, as a builder
     */
    public static ADLSAccountBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return ADLSAccount.builder()
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
        return ADLSAccount.builder().qualifiedName(qualifiedName).name(name);
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
        return (ADLSAccount) Asset.removeDescription(updater(qualifiedName, name));
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
        return (ADLSAccount) Asset.removeUserDescription(updater(qualifiedName, name));
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
        return (ADLSAccount) Asset.removeOwners(updater(qualifiedName, name));
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
        return (ADLSAccount) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
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
        return (ADLSAccount) Asset.removeCertificate(updater(qualifiedName, name));
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
        return (ADLSAccount) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
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
        return (ADLSAccount) Asset.removeAnnouncement(updater(qualifiedName, name));
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
    public static ADLSAccount replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (ADLSAccount) Asset.replaceTerms(updater(qualifiedName, name), terms);
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
    public static ADLSAccount appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (ADLSAccount) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
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
    public static ADLSAccount removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (ADLSAccount) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add classifications to a ADLSAccount.
     *
     * @param qualifiedName of the ADLSAccount
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the ADLSAccount
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a ADLSAccount.
     *
     * @param qualifiedName of the ADLSAccount
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the ADLSAccount
     */
    public static void addClassifications(
            String qualifiedName,
            List<String> classificationNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addClassifications(
                TYPE_NAME,
                qualifiedName,
                classificationNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove a classification from a ADLSAccount.
     *
     * @param qualifiedName of the ADLSAccount
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the ADLSAccount
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }
}
