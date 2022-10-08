/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
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
public class Folder extends Namespace {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Folder";

    /** Fixed typeName for Folders. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String parentQualifiedName;

    /** TBC */
    @Attribute
    String collectionQualifiedName;

    /** TBC */
    @Attribute
    Namespace parent;

    /**
     * Reference to a Folder by GUID.
     *
     * @param guid the GUID of the Folder to reference
     * @return reference to a Folder that can be used for defining a relationship to a Folder
     */
    public static Folder refByGuid(String guid) {
        return Folder.builder().guid(guid).build();
    }

    /**
     * Reference to a Folder by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the Folder to reference
     * @return reference to a Folder that can be used for defining a relationship to a Folder
     */
    public static Folder refByQualifiedName(String qualifiedName) {
        return Folder.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a Folder.
     *
     * @param qualifiedName of the Folder
     * @param name of the Folder
     * @return the minimal request necessary to update the Folder, as a builder
     */
    public static FolderBuilder<?, ?> updater(String qualifiedName, String name) {
        return Folder.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Folder, from a potentially
     * more-complete Folder object.
     *
     * @return the minimal object necessary to update the Folder, as a builder
     */
    @Override
    protected FolderBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Update the certificate on a Folder.
     *
     * @param qualifiedName of the Folder
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated Folder, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Folder updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (Folder) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a Folder.
     *
     * @param qualifiedName of the Folder
     * @param name of the Folder
     * @return the updated Folder, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Folder removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (Folder)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a Folder.
     *
     * @param qualifiedName of the Folder
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Folder updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (Folder) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a Folder.
     *
     * @param qualifiedName of the Folder
     * @param name of the Folder
     * @return the updated Folder, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Folder removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (Folder)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a Folder.
     *
     * @param qualifiedName of the Folder
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the Folder
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a Folder.
     *
     * @param qualifiedName of the Folder
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the Folder
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the Folder.
     *
     * @param qualifiedName for the Folder
     * @param name human-readable name of the Folder
     * @param terms the list of terms to replace on the Folder, or null to remove all terms from the Folder
     * @return the Folder that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static Folder replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (Folder) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the Folder, without replacing existing terms linked to the Folder.
     * Note: this operation must make two API calls — one to retrieve the Folder's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the Folder
     * @param terms the list of terms to append to the Folder
     * @return the Folder that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static Folder appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (Folder) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a Folder, without replacing all existing terms linked to the Folder.
     * Note: this operation must make two API calls — one to retrieve the Folder's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the Folder
     * @param terms the list of terms to remove from the Folder, which must be referenced by GUID
     * @return the Folder that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static Folder removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (Folder) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
