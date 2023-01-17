/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.*;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * TBC
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class SigmaWorkbook extends Sigma {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SigmaWorkbook";

    /** Fixed typeName for SigmaWorkbooks. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    Long sigmaPageCount;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<SigmaPage> sigmaPages;

    /**
     * Reference to a SigmaWorkbook by GUID.
     *
     * @param guid the GUID of the SigmaWorkbook to reference
     * @return reference to a SigmaWorkbook that can be used for defining a relationship to a SigmaWorkbook
     */
    public static SigmaWorkbook refByGuid(String guid) {
        return SigmaWorkbook.builder().guid(guid).build();
    }

    /**
     * Reference to a SigmaWorkbook by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the SigmaWorkbook to reference
     * @return reference to a SigmaWorkbook that can be used for defining a relationship to a SigmaWorkbook
     */
    public static SigmaWorkbook refByQualifiedName(String qualifiedName) {
        return SigmaWorkbook.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a SigmaWorkbook.
     *
     * @param qualifiedName of the SigmaWorkbook
     * @param name of the SigmaWorkbook
     * @return the minimal request necessary to update the SigmaWorkbook, as a builder
     */
    public static SigmaWorkbookBuilder<?, ?> updater(String qualifiedName, String name) {
        return SigmaWorkbook.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SigmaWorkbook, from a potentially
     * more-complete SigmaWorkbook object.
     *
     * @return the minimal object necessary to update the SigmaWorkbook, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SigmaWorkbook are not found in the initial object
     */
    @Override
    public SigmaWorkbookBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "SigmaWorkbook", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a SigmaWorkbook by its GUID, complete with all of its relationships.
     *
     * @param guid of the SigmaWorkbook to retrieve
     * @return the requested full SigmaWorkbook, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaWorkbook does not exist or the provided GUID is not a SigmaWorkbook
     */
    public static SigmaWorkbook retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof SigmaWorkbook) {
            return (SigmaWorkbook) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "SigmaWorkbook");
        }
    }

    /**
     * Retrieves a SigmaWorkbook by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the SigmaWorkbook to retrieve
     * @return the requested full SigmaWorkbook, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaWorkbook does not exist
     */
    public static SigmaWorkbook retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof SigmaWorkbook) {
            return (SigmaWorkbook) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "SigmaWorkbook");
        }
    }

    /**
     * Restore the archived (soft-deleted) SigmaWorkbook to active.
     *
     * @param qualifiedName for the SigmaWorkbook
     * @return true if the SigmaWorkbook is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a SigmaWorkbook.
     *
     * @param qualifiedName of the SigmaWorkbook
     * @param name of the SigmaWorkbook
     * @return the updated SigmaWorkbook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaWorkbook removeDescription(String qualifiedName, String name) throws AtlanException {
        return (SigmaWorkbook) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SigmaWorkbook.
     *
     * @param qualifiedName of the SigmaWorkbook
     * @param name of the SigmaWorkbook
     * @return the updated SigmaWorkbook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaWorkbook removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (SigmaWorkbook) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SigmaWorkbook.
     *
     * @param qualifiedName of the SigmaWorkbook
     * @param name of the SigmaWorkbook
     * @return the updated SigmaWorkbook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaWorkbook removeOwners(String qualifiedName, String name) throws AtlanException {
        return (SigmaWorkbook) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SigmaWorkbook.
     *
     * @param qualifiedName of the SigmaWorkbook
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SigmaWorkbook, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SigmaWorkbook updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (SigmaWorkbook) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SigmaWorkbook.
     *
     * @param qualifiedName of the SigmaWorkbook
     * @param name of the SigmaWorkbook
     * @return the updated SigmaWorkbook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaWorkbook removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (SigmaWorkbook) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SigmaWorkbook.
     *
     * @param qualifiedName of the SigmaWorkbook
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SigmaWorkbook updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (SigmaWorkbook) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SigmaWorkbook.
     *
     * @param qualifiedName of the SigmaWorkbook
     * @param name of the SigmaWorkbook
     * @return the updated SigmaWorkbook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaWorkbook removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (SigmaWorkbook) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Add classifications to a SigmaWorkbook.
     *
     * @param qualifiedName of the SigmaWorkbook
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the SigmaWorkbook
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a SigmaWorkbook.
     *
     * @param qualifiedName of the SigmaWorkbook
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the SigmaWorkbook
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the SigmaWorkbook.
     *
     * @param qualifiedName for the SigmaWorkbook
     * @param name human-readable name of the SigmaWorkbook
     * @param terms the list of terms to replace on the SigmaWorkbook, or null to remove all terms from the SigmaWorkbook
     * @return the SigmaWorkbook that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaWorkbook replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (SigmaWorkbook) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SigmaWorkbook, without replacing existing terms linked to the SigmaWorkbook.
     * Note: this operation must make two API calls — one to retrieve the SigmaWorkbook's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the SigmaWorkbook
     * @param terms the list of terms to append to the SigmaWorkbook
     * @return the SigmaWorkbook that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaWorkbook appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (SigmaWorkbook) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SigmaWorkbook, without replacing all existing terms linked to the SigmaWorkbook.
     * Note: this operation must make two API calls — one to retrieve the SigmaWorkbook's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the SigmaWorkbook
     * @param terms the list of terms to remove from the SigmaWorkbook, which must be referenced by GUID
     * @return the SigmaWorkbook that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaWorkbook removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (SigmaWorkbook) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
