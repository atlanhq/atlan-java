/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class PowerBIPage extends PowerBI {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "PowerBIPage";

    /** Fixed typeName for PowerBIPages. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String workspaceQualifiedName;

    /** TBC */
    @Attribute
    String reportQualifiedName;

    /** TBC */
    @Attribute
    PowerBIReport report;

    /**
     * Reference to a PowerBIPage by GUID.
     *
     * @param guid the GUID of the PowerBIPage to reference
     * @return reference to a PowerBIPage that can be used for defining a relationship to a PowerBIPage
     */
    public static PowerBIPage refByGuid(String guid) {
        return PowerBIPage.builder().guid(guid).build();
    }

    /**
     * Reference to a PowerBIPage by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the PowerBIPage to reference
     * @return reference to a PowerBIPage that can be used for defining a relationship to a PowerBIPage
     */
    public static PowerBIPage refByQualifiedName(String qualifiedName) {
        return PowerBIPage.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a PowerBIPage.
     *
     * @param qualifiedName of the PowerBIPage
     * @param name of the PowerBIPage
     * @return the minimal request necessary to update the PowerBIPage, as a builder
     */
    public static PowerBIPageBuilder<?, ?> updater(String qualifiedName, String name) {
        return PowerBIPage.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PowerBIPage, from a potentially
     * more-complete PowerBIPage object.
     *
     * @return the minimal object necessary to update the PowerBIPage, as a builder
     */
    @Override
    protected PowerBIPageBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a PowerBIPage by its GUID, complete with all of its relationships.
     *
     * @param guid of the PowerBIPage to retrieve
     * @return the requested full PowerBIPage, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PowerBIPage does not exist or the provided GUID is not a PowerBIPage
     */
    public static PowerBIPage retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof PowerBIPage) {
            return (PowerBIPage) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a PowerBIPage.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a PowerBIPage by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the PowerBIPage to retrieve
     * @return the requested full PowerBIPage, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PowerBIPage does not exist
     */
    public static PowerBIPage retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof PowerBIPage) {
            return (PowerBIPage) entity;
        } else {
            throw new NotFoundException(
                    "No PowerBIPage found with qualifiedName: " + qualifiedName,
                    "ATLAN_JAVA_CLIENT-404-003",
                    404,
                    null);
        }
    }

    /**
     * Restore the archived (soft-deleted) PowerBIPage to active.
     *
     * @param qualifiedName for the PowerBIPage
     * @return the PowerBIPage that was restored
     * @throws AtlanException on any API problems
     */
    public static PowerBIPage restore(String qualifiedName) throws AtlanException {
        return (PowerBIPage) Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a PowerBIPage.
     *
     * @param qualifiedName of the PowerBIPage
     * @param name of the PowerBIPage
     * @return the updated PowerBIPage, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIPage removeDescription(String qualifiedName, String name) throws AtlanException {
        return (PowerBIPage)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a PowerBIPage.
     *
     * @param qualifiedName of the PowerBIPage
     * @param name of the PowerBIPage
     * @return the updated PowerBIPage, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIPage removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (PowerBIPage) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the owners from a PowerBIPage.
     *
     * @param qualifiedName of the PowerBIPage
     * @param name of the PowerBIPage
     * @return the updated PowerBIPage, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIPage removeOwners(String qualifiedName, String name) throws AtlanException {
        return (PowerBIPage)
                Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the certificate on a PowerBIPage.
     *
     * @param qualifiedName of the PowerBIPage
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated PowerBIPage, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIPage updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (PowerBIPage) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a PowerBIPage.
     *
     * @param qualifiedName of the PowerBIPage
     * @param name of the PowerBIPage
     * @return the updated PowerBIPage, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIPage removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (PowerBIPage)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a PowerBIPage.
     *
     * @param qualifiedName of the PowerBIPage
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIPage updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (PowerBIPage) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a PowerBIPage.
     *
     * @param qualifiedName of the PowerBIPage
     * @param name of the PowerBIPage
     * @return the updated PowerBIPage, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIPage removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (PowerBIPage)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a PowerBIPage.
     *
     * @param qualifiedName of the PowerBIPage
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the PowerBIPage
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a PowerBIPage.
     *
     * @param qualifiedName of the PowerBIPage
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the PowerBIPage
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the PowerBIPage.
     *
     * @param qualifiedName for the PowerBIPage
     * @param name human-readable name of the PowerBIPage
     * @param terms the list of terms to replace on the PowerBIPage, or null to remove all terms from the PowerBIPage
     * @return the PowerBIPage that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIPage replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (PowerBIPage) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the PowerBIPage, without replacing existing terms linked to the PowerBIPage.
     * Note: this operation must make two API calls — one to retrieve the PowerBIPage's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the PowerBIPage
     * @param terms the list of terms to append to the PowerBIPage
     * @return the PowerBIPage that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIPage appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PowerBIPage) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a PowerBIPage, without replacing all existing terms linked to the PowerBIPage.
     * Note: this operation must make two API calls — one to retrieve the PowerBIPage's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the PowerBIPage
     * @param terms the list of terms to remove from the PowerBIPage, which must be referenced by GUID
     * @return the PowerBIPage that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIPage removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PowerBIPage) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
