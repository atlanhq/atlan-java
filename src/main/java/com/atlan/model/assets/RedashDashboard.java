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
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Redash dashboard in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class RedashDashboard extends Redash {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "RedashDashboard";

    /** Fixed typeName for RedashDashboards. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Number of widgets in the Redash dashboard. */
    @Attribute
    Long redashDashboardWidgetCount;

    /**
     * Reference to a RedashDashboard by GUID.
     *
     * @param guid the GUID of the RedashDashboard to reference
     * @return reference to a RedashDashboard that can be used for defining a relationship to a RedashDashboard
     */
    public static RedashDashboard refByGuid(String guid) {
        return RedashDashboard.builder().guid(guid).build();
    }

    /**
     * Reference to a RedashDashboard by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the RedashDashboard to reference
     * @return reference to a RedashDashboard that can be used for defining a relationship to a RedashDashboard
     */
    public static RedashDashboard refByQualifiedName(String qualifiedName) {
        return RedashDashboard.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a RedashDashboard by its GUID, complete with all of its relationships.
     *
     * @param guid of the RedashDashboard to retrieve
     * @return the requested full RedashDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the RedashDashboard does not exist or the provided GUID is not a RedashDashboard
     */
    public static RedashDashboard retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof RedashDashboard) {
            return (RedashDashboard) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "RedashDashboard");
        }
    }

    /**
     * Retrieves a RedashDashboard by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the RedashDashboard to retrieve
     * @return the requested full RedashDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the RedashDashboard does not exist
     */
    public static RedashDashboard retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof RedashDashboard) {
            return (RedashDashboard) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "RedashDashboard");
        }
    }

    /**
     * Restore the archived (soft-deleted) RedashDashboard to active.
     *
     * @param qualifiedName for the RedashDashboard
     * @return true if the RedashDashboard is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a RedashDashboard.
     *
     * @param qualifiedName of the RedashDashboard
     * @param name of the RedashDashboard
     * @return the minimal request necessary to update the RedashDashboard, as a builder
     */
    public static RedashDashboardBuilder<?, ?> updater(String qualifiedName, String name) {
        return RedashDashboard.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a RedashDashboard, from a potentially
     * more-complete RedashDashboard object.
     *
     * @return the minimal object necessary to update the RedashDashboard, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for RedashDashboard are not found in the initial object
     */
    @Override
    public RedashDashboardBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "RedashDashboard", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a RedashDashboard.
     *
     * @param qualifiedName of the RedashDashboard
     * @param name of the RedashDashboard
     * @return the updated RedashDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard removeDescription(String qualifiedName, String name) throws AtlanException {
        return (RedashDashboard) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a RedashDashboard.
     *
     * @param qualifiedName of the RedashDashboard
     * @param name of the RedashDashboard
     * @return the updated RedashDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (RedashDashboard) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a RedashDashboard.
     *
     * @param qualifiedName of the RedashDashboard
     * @param name of the RedashDashboard
     * @return the updated RedashDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard removeOwners(String qualifiedName, String name) throws AtlanException {
        return (RedashDashboard) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a RedashDashboard.
     *
     * @param qualifiedName of the RedashDashboard
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated RedashDashboard, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (RedashDashboard) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a RedashDashboard.
     *
     * @param qualifiedName of the RedashDashboard
     * @param name of the RedashDashboard
     * @return the updated RedashDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (RedashDashboard) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a RedashDashboard.
     *
     * @param qualifiedName of the RedashDashboard
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (RedashDashboard) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a RedashDashboard.
     *
     * @param qualifiedName of the RedashDashboard
     * @param name of the RedashDashboard
     * @return the updated RedashDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (RedashDashboard) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the RedashDashboard.
     *
     * @param qualifiedName for the RedashDashboard
     * @param name human-readable name of the RedashDashboard
     * @param terms the list of terms to replace on the RedashDashboard, or null to remove all terms from the RedashDashboard
     * @return the RedashDashboard that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (RedashDashboard) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the RedashDashboard, without replacing existing terms linked to the RedashDashboard.
     * Note: this operation must make two API calls — one to retrieve the RedashDashboard's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the RedashDashboard
     * @param terms the list of terms to append to the RedashDashboard
     * @return the RedashDashboard that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (RedashDashboard) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a RedashDashboard, without replacing all existing terms linked to the RedashDashboard.
     * Note: this operation must make two API calls — one to retrieve the RedashDashboard's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the RedashDashboard
     * @param terms the list of terms to remove from the RedashDashboard, which must be referenced by GUID
     * @return the RedashDashboard that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static RedashDashboard removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (RedashDashboard) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add classifications to a RedashDashboard.
     *
     * @param qualifiedName of the RedashDashboard
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the RedashDashboard
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a RedashDashboard.
     *
     * @param qualifiedName of the RedashDashboard
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the RedashDashboard
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
     * Remove a classification from a RedashDashboard.
     *
     * @param qualifiedName of the RedashDashboard
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the RedashDashboard
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }
}
