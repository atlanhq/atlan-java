/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class LookerDashboard extends Looker {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "LookerDashboard";

    /** Fixed typeName for LookerDashboards. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String folderName;

    /** TBC */
    @Attribute
    Integer sourceUserId;

    /** TBC */
    @Attribute
    Integer sourceViewCount;

    /** TBC */
    @Attribute
    Integer sourceMetadataId;

    /** TBC */
    @Attribute
    Integer sourcelastUpdaterId;

    /** TBC */
    @Attribute
    Long sourceLastAccessedAt;

    /** TBC */
    @Attribute
    Long sourceLastViewedAt;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<LookerTile> tiles;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<LookerLook> looks;

    /** TBC */
    @Attribute
    LookerFolder folder;

    /**
     * Reference to a LookerDashboard by GUID.
     *
     * @param guid the GUID of the LookerDashboard to reference
     * @return reference to a LookerDashboard that can be used for defining a relationship to a LookerDashboard
     */
    public static LookerDashboard refByGuid(String guid) {
        return LookerDashboard.builder().guid(guid).build();
    }

    /**
     * Reference to a LookerDashboard by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the LookerDashboard to reference
     * @return reference to a LookerDashboard that can be used for defining a relationship to a LookerDashboard
     */
    public static LookerDashboard refByQualifiedName(String qualifiedName) {
        return LookerDashboard.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a LookerDashboard.
     *
     * @param qualifiedName of the LookerDashboard
     * @param name of the LookerDashboard
     * @return the minimal request necessary to update the LookerDashboard, as a builder
     */
    public static LookerDashboardBuilder<?, ?> updater(String qualifiedName, String name) {
        return LookerDashboard.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a LookerDashboard, from a potentially
     * more-complete LookerDashboard object.
     *
     * @return the minimal object necessary to update the LookerDashboard, as a builder
     */
    @Override
    protected LookerDashboardBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Update the certificate on a LookerDashboard.
     *
     * @param qualifiedName of the LookerDashboard
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated LookerDashboard, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (LookerDashboard) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a LookerDashboard.
     *
     * @param qualifiedName of the LookerDashboard
     * @param name of the LookerDashboard
     * @return the updated LookerDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (LookerDashboard)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a LookerDashboard.
     *
     * @param qualifiedName of the LookerDashboard
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (LookerDashboard) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a LookerDashboard.
     *
     * @param qualifiedName of the LookerDashboard
     * @param name of the LookerDashboard
     * @return the updated LookerDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (LookerDashboard)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a LookerDashboard.
     *
     * @param qualifiedName of the LookerDashboard
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the LookerDashboard
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a LookerDashboard.
     *
     * @param qualifiedName of the LookerDashboard
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the LookerDashboard
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the LookerDashboard.
     *
     * @param qualifiedName for the LookerDashboard
     * @param name human-readable name of the LookerDashboard
     * @param terms the list of terms to replace on the LookerDashboard, or null to remove all terms from the LookerDashboard
     * @return the LookerDashboard that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (LookerDashboard) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the LookerDashboard, without replacing existing terms linked to the LookerDashboard.
     * Note: this operation must make two API calls — one to retrieve the LookerDashboard's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the LookerDashboard
     * @param terms the list of terms to append to the LookerDashboard
     * @return the LookerDashboard that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (LookerDashboard) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a LookerDashboard, without replacing all existing terms linked to the LookerDashboard.
     * Note: this operation must make two API calls — one to retrieve the LookerDashboard's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the LookerDashboard
     * @param terms the list of terms to remove from the LookerDashboard, which must be referenced by GUID
     * @return the LookerDashboard that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static LookerDashboard removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (LookerDashboard) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
