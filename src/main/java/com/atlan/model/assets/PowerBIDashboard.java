/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Power BI dashboard in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class PowerBIDashboard extends PowerBI {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "PowerBIDashboard";

    /** Fixed typeName for PowerBIDashboards. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String workspaceQualifiedName;

    /** TBC */
    @Attribute
    String webUrl;

    /** TBC */
    @Attribute
    Long tileCount;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<PowerBITile> tiles;

    /** TBC */
    @Attribute
    PowerBIWorkspace workspace;

    /**
     * Reference to a PowerBIDashboard by GUID.
     *
     * @param guid the GUID of the PowerBIDashboard to reference
     * @return reference to a PowerBIDashboard that can be used for defining a relationship to a PowerBIDashboard
     */
    public static PowerBIDashboard refByGuid(String guid) {
        return PowerBIDashboard.builder().guid(guid).build();
    }

    /**
     * Reference to a PowerBIDashboard by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the PowerBIDashboard to reference
     * @return reference to a PowerBIDashboard that can be used for defining a relationship to a PowerBIDashboard
     */
    public static PowerBIDashboard refByQualifiedName(String qualifiedName) {
        return PowerBIDashboard.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a PowerBIDashboard by its GUID, complete with all of its relationships.
     *
     * @param guid of the PowerBIDashboard to retrieve
     * @return the requested full PowerBIDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PowerBIDashboard does not exist or the provided GUID is not a PowerBIDashboard
     */
    public static PowerBIDashboard retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof PowerBIDashboard) {
            return (PowerBIDashboard) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "PowerBIDashboard");
        }
    }

    /**
     * Retrieves a PowerBIDashboard by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the PowerBIDashboard to retrieve
     * @return the requested full PowerBIDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PowerBIDashboard does not exist
     */
    public static PowerBIDashboard retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof PowerBIDashboard) {
            return (PowerBIDashboard) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "PowerBIDashboard");
        }
    }

    /**
     * Restore the archived (soft-deleted) PowerBIDashboard to active.
     *
     * @param qualifiedName for the PowerBIDashboard
     * @return true if the PowerBIDashboard is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a PowerBIDashboard.
     *
     * @param qualifiedName of the PowerBIDashboard
     * @param name of the PowerBIDashboard
     * @return the minimal request necessary to update the PowerBIDashboard, as a builder
     */
    public static PowerBIDashboardBuilder<?, ?> updater(String qualifiedName, String name) {
        return PowerBIDashboard.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PowerBIDashboard, from a potentially
     * more-complete PowerBIDashboard object.
     *
     * @return the minimal object necessary to update the PowerBIDashboard, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for PowerBIDashboard are not found in the initial object
     */
    @Override
    public PowerBIDashboardBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "PowerBIDashboard", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a PowerBIDashboard.
     *
     * @param qualifiedName of the PowerBIDashboard
     * @param name of the PowerBIDashboard
     * @return the updated PowerBIDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIDashboard removeDescription(String qualifiedName, String name) throws AtlanException {
        return (PowerBIDashboard) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a PowerBIDashboard.
     *
     * @param qualifiedName of the PowerBIDashboard
     * @param name of the PowerBIDashboard
     * @return the updated PowerBIDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIDashboard removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (PowerBIDashboard) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a PowerBIDashboard.
     *
     * @param qualifiedName of the PowerBIDashboard
     * @param name of the PowerBIDashboard
     * @return the updated PowerBIDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIDashboard removeOwners(String qualifiedName, String name) throws AtlanException {
        return (PowerBIDashboard) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a PowerBIDashboard.
     *
     * @param qualifiedName of the PowerBIDashboard
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated PowerBIDashboard, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIDashboard updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return (PowerBIDashboard) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a PowerBIDashboard.
     *
     * @param qualifiedName of the PowerBIDashboard
     * @param name of the PowerBIDashboard
     * @return the updated PowerBIDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIDashboard removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (PowerBIDashboard) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a PowerBIDashboard.
     *
     * @param qualifiedName of the PowerBIDashboard
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIDashboard updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (PowerBIDashboard) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a PowerBIDashboard.
     *
     * @param qualifiedName of the PowerBIDashboard
     * @param name of the PowerBIDashboard
     * @return the updated PowerBIDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIDashboard removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (PowerBIDashboard) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the PowerBIDashboard.
     *
     * @param qualifiedName for the PowerBIDashboard
     * @param name human-readable name of the PowerBIDashboard
     * @param terms the list of terms to replace on the PowerBIDashboard, or null to remove all terms from the PowerBIDashboard
     * @return the PowerBIDashboard that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIDashboard replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (PowerBIDashboard) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the PowerBIDashboard, without replacing existing terms linked to the PowerBIDashboard.
     * Note: this operation must make two API calls — one to retrieve the PowerBIDashboard's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the PowerBIDashboard
     * @param terms the list of terms to append to the PowerBIDashboard
     * @return the PowerBIDashboard that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIDashboard appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PowerBIDashboard) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a PowerBIDashboard, without replacing all existing terms linked to the PowerBIDashboard.
     * Note: this operation must make two API calls — one to retrieve the PowerBIDashboard's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the PowerBIDashboard
     * @param terms the list of terms to remove from the PowerBIDashboard, which must be referenced by GUID
     * @return the PowerBIDashboard that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIDashboard removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PowerBIDashboard) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add classifications to a PowerBIDashboard.
     *
     * @param qualifiedName of the PowerBIDashboard
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the PowerBIDashboard
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a PowerBIDashboard.
     *
     * @param qualifiedName of the PowerBIDashboard
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the PowerBIDashboard
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
     * Remove a classification from a PowerBIDashboard.
     *
     * @param qualifiedName of the PowerBIDashboard
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the PowerBIDashboard
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }
}
