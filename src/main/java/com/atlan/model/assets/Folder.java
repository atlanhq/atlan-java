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
 * Instance of a folder within a query collection in Atlan.
 */
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

    /** qualifiedName of the collection in which this folder exists. */
    @Attribute
    String collectionQualifiedName;

    /** Namespace in which this folder exists. */
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
     * @throws InvalidRequestException if any of the minimal set of required properties for Folder are not found in the initial object
     */
    @Override
    public FolderBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "Folder", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a Folder by its GUID, complete with all of its relationships.
     *
     * @param guid of the Folder to retrieve
     * @return the requested full Folder, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Folder does not exist or the provided GUID is not a Folder
     */
    public static Folder retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof Folder) {
            return (Folder) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "Folder");
        }
    }

    /**
     * Retrieves a Folder by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the Folder to retrieve
     * @return the requested full Folder, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Folder does not exist
     */
    public static Folder retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof Folder) {
            return (Folder) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "Folder");
        }
    }

    /**
     * Restore the archived (soft-deleted) Folder to active.
     *
     * @param qualifiedName for the Folder
     * @return true if the Folder is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a Folder.
     *
     * @param qualifiedName of the Folder
     * @param name of the Folder
     * @return the updated Folder, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Folder removeDescription(String qualifiedName, String name) throws AtlanException {
        return (Folder) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a Folder.
     *
     * @param qualifiedName of the Folder
     * @param name of the Folder
     * @return the updated Folder, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Folder removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (Folder) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a Folder.
     *
     * @param qualifiedName of the Folder
     * @param name of the Folder
     * @return the updated Folder, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Folder removeOwners(String qualifiedName, String name) throws AtlanException {
        return (Folder) Asset.removeOwners(updater(qualifiedName, name));
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
        return (Folder) Asset.removeCertificate(updater(qualifiedName, name));
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
        return (Folder) Asset.removeAnnouncement(updater(qualifiedName, name));
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
     * Note: this operation must make two API calls ??? one to retrieve the Folder's existing terms,
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
     * Note: this operation must make two API calls ??? one to retrieve the Folder's existing terms,
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
