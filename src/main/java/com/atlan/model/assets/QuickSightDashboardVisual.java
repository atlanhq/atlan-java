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
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * TBC
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class QuickSightDashboardVisual extends QuickSight {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "QuickSightDashboardVisual";

    /** Fixed typeName for QuickSightDashboardVisuals. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String quickSightDashboardQualifiedName;

    /** TBC */
    @Attribute
    QuickSightDashboard quickSightDashboard;

    /**
     * Reference to a QuickSightDashboardVisual by GUID.
     *
     * @param guid the GUID of the QuickSightDashboardVisual to reference
     * @return reference to a QuickSightDashboardVisual that can be used for defining a relationship to a QuickSightDashboardVisual
     */
    public static QuickSightDashboardVisual refByGuid(String guid) {
        return QuickSightDashboardVisual.builder().guid(guid).build();
    }

    /**
     * Reference to a QuickSightDashboardVisual by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the QuickSightDashboardVisual to reference
     * @return reference to a QuickSightDashboardVisual that can be used for defining a relationship to a QuickSightDashboardVisual
     */
    public static QuickSightDashboardVisual refByQualifiedName(String qualifiedName) {
        return QuickSightDashboardVisual.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a QuickSightDashboardVisual.
     *
     * @param qualifiedName of the QuickSightDashboardVisual
     * @param name of the QuickSightDashboardVisual
     * @return the minimal request necessary to update the QuickSightDashboardVisual, as a builder
     */
    public static QuickSightDashboardVisualBuilder<?, ?> updater(String qualifiedName, String name) {
        return QuickSightDashboardVisual.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a QuickSightDashboardVisual, from a potentially
     * more-complete QuickSightDashboardVisual object.
     *
     * @return the minimal object necessary to update the QuickSightDashboardVisual, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for QuickSightDashboardVisual are not found in the initial object
     */
    @Override
    public QuickSightDashboardVisualBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "QuickSightDashboardVisual", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a QuickSightDashboardVisual by its GUID, complete with all of its relationships.
     *
     * @param guid of the QuickSightDashboardVisual to retrieve
     * @return the requested full QuickSightDashboardVisual, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightDashboardVisual does not exist or the provided GUID is not a QuickSightDashboardVisual
     */
    public static QuickSightDashboardVisual retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof QuickSightDashboardVisual) {
            return (QuickSightDashboardVisual) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "QuickSightDashboardVisual");
        }
    }

    /**
     * Retrieves a QuickSightDashboardVisual by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the QuickSightDashboardVisual to retrieve
     * @return the requested full QuickSightDashboardVisual, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightDashboardVisual does not exist
     */
    public static QuickSightDashboardVisual retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof QuickSightDashboardVisual) {
            return (QuickSightDashboardVisual) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "QuickSightDashboardVisual");
        }
    }

    /**
     * Restore the archived (soft-deleted) QuickSightDashboardVisual to active.
     *
     * @param qualifiedName for the QuickSightDashboardVisual
     * @return true if the QuickSightDashboardVisual is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a QuickSightDashboardVisual.
     *
     * @param qualifiedName of the QuickSightDashboardVisual
     * @param name of the QuickSightDashboardVisual
     * @return the updated QuickSightDashboardVisual, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboardVisual removeDescription(String qualifiedName, String name) throws AtlanException {
        return (QuickSightDashboardVisual) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a QuickSightDashboardVisual.
     *
     * @param qualifiedName of the QuickSightDashboardVisual
     * @param name of the QuickSightDashboardVisual
     * @return the updated QuickSightDashboardVisual, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboardVisual removeUserDescription(String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightDashboardVisual) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a QuickSightDashboardVisual.
     *
     * @param qualifiedName of the QuickSightDashboardVisual
     * @param name of the QuickSightDashboardVisual
     * @return the updated QuickSightDashboardVisual, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboardVisual removeOwners(String qualifiedName, String name) throws AtlanException {
        return (QuickSightDashboardVisual) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a QuickSightDashboardVisual.
     *
     * @param qualifiedName of the QuickSightDashboardVisual
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QuickSightDashboardVisual, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboardVisual updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (QuickSightDashboardVisual)
                Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a QuickSightDashboardVisual.
     *
     * @param qualifiedName of the QuickSightDashboardVisual
     * @param name of the QuickSightDashboardVisual
     * @return the updated QuickSightDashboardVisual, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboardVisual removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (QuickSightDashboardVisual) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a QuickSightDashboardVisual.
     *
     * @param qualifiedName of the QuickSightDashboardVisual
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboardVisual updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (QuickSightDashboardVisual)
                Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a QuickSightDashboardVisual.
     *
     * @param qualifiedName of the QuickSightDashboardVisual
     * @param name of the QuickSightDashboardVisual
     * @return the updated QuickSightDashboardVisual, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboardVisual removeAnnouncement(String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightDashboardVisual) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Add classifications to a QuickSightDashboardVisual.
     *
     * @param qualifiedName of the QuickSightDashboardVisual
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the QuickSightDashboardVisual
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a QuickSightDashboardVisual.
     *
     * @param qualifiedName of the QuickSightDashboardVisual
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the QuickSightDashboardVisual
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the QuickSightDashboardVisual.
     *
     * @param qualifiedName for the QuickSightDashboardVisual
     * @param name human-readable name of the QuickSightDashboardVisual
     * @param terms the list of terms to replace on the QuickSightDashboardVisual, or null to remove all terms from the QuickSightDashboardVisual
     * @return the QuickSightDashboardVisual that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboardVisual replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (QuickSightDashboardVisual) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the QuickSightDashboardVisual, without replacing existing terms linked to the QuickSightDashboardVisual.
     * Note: this operation must make two API calls — one to retrieve the QuickSightDashboardVisual's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the QuickSightDashboardVisual
     * @param terms the list of terms to append to the QuickSightDashboardVisual
     * @return the QuickSightDashboardVisual that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboardVisual appendTerms(String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        return (QuickSightDashboardVisual) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a QuickSightDashboardVisual, without replacing all existing terms linked to the QuickSightDashboardVisual.
     * Note: this operation must make two API calls — one to retrieve the QuickSightDashboardVisual's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the QuickSightDashboardVisual
     * @param terms the list of terms to remove from the QuickSightDashboardVisual, which must be referenced by GUID
     * @return the QuickSightDashboardVisual that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightDashboardVisual removeTerms(String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        return (QuickSightDashboardVisual) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
