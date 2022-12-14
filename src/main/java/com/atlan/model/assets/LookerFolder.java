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
 * Instance of a Looker folder in Atlan.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class LookerFolder extends Looker {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "LookerFolder";

    /** Fixed typeName for LookerFolders. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    Integer sourceContentMetadataId;

    /** TBC */
    @Attribute
    Integer sourceCreatorId;

    /** TBC */
    @Attribute
    Integer sourceChildCount;

    /** TBC */
    @Attribute
    Integer sourceParentID;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<LookerLook> looks;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<LookerDashboard> dashboards;

    /**
     * Reference to a LookerFolder by GUID.
     *
     * @param guid the GUID of the LookerFolder to reference
     * @return reference to a LookerFolder that can be used for defining a relationship to a LookerFolder
     */
    public static LookerFolder refByGuid(String guid) {
        return LookerFolder.builder().guid(guid).build();
    }

    /**
     * Reference to a LookerFolder by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the LookerFolder to reference
     * @return reference to a LookerFolder that can be used for defining a relationship to a LookerFolder
     */
    public static LookerFolder refByQualifiedName(String qualifiedName) {
        return LookerFolder.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a LookerFolder.
     *
     * @param qualifiedName of the LookerFolder
     * @param name of the LookerFolder
     * @return the minimal request necessary to update the LookerFolder, as a builder
     */
    public static LookerFolderBuilder<?, ?> updater(String qualifiedName, String name) {
        return LookerFolder.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a LookerFolder, from a potentially
     * more-complete LookerFolder object.
     *
     * @return the minimal object necessary to update the LookerFolder, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for LookerFolder are not found in the initial object
     */
    @Override
    public LookerFolderBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "LookerFolder", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a LookerFolder by its GUID, complete with all of its relationships.
     *
     * @param guid of the LookerFolder to retrieve
     * @return the requested full LookerFolder, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerFolder does not exist or the provided GUID is not a LookerFolder
     */
    public static LookerFolder retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof LookerFolder) {
            return (LookerFolder) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "LookerFolder");
        }
    }

    /**
     * Retrieves a LookerFolder by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the LookerFolder to retrieve
     * @return the requested full LookerFolder, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerFolder does not exist
     */
    public static LookerFolder retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof LookerFolder) {
            return (LookerFolder) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "LookerFolder");
        }
    }

    /**
     * Restore the archived (soft-deleted) LookerFolder to active.
     *
     * @param qualifiedName for the LookerFolder
     * @return true if the LookerFolder is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a LookerFolder.
     *
     * @param qualifiedName of the LookerFolder
     * @param name of the LookerFolder
     * @return the updated LookerFolder, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerFolder removeDescription(String qualifiedName, String name) throws AtlanException {
        return (LookerFolder) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a LookerFolder.
     *
     * @param qualifiedName of the LookerFolder
     * @param name of the LookerFolder
     * @return the updated LookerFolder, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerFolder removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (LookerFolder) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a LookerFolder.
     *
     * @param qualifiedName of the LookerFolder
     * @param name of the LookerFolder
     * @return the updated LookerFolder, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerFolder removeOwners(String qualifiedName, String name) throws AtlanException {
        return (LookerFolder) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a LookerFolder.
     *
     * @param qualifiedName of the LookerFolder
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated LookerFolder, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerFolder updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (LookerFolder) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a LookerFolder.
     *
     * @param qualifiedName of the LookerFolder
     * @param name of the LookerFolder
     * @return the updated LookerFolder, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerFolder removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (LookerFolder) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a LookerFolder.
     *
     * @param qualifiedName of the LookerFolder
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerFolder updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (LookerFolder) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a LookerFolder.
     *
     * @param qualifiedName of the LookerFolder
     * @param name of the LookerFolder
     * @return the updated LookerFolder, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerFolder removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (LookerFolder) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Add classifications to a LookerFolder.
     *
     * @param qualifiedName of the LookerFolder
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the LookerFolder
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a LookerFolder.
     *
     * @param qualifiedName of the LookerFolder
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the LookerFolder
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the LookerFolder.
     *
     * @param qualifiedName for the LookerFolder
     * @param name human-readable name of the LookerFolder
     * @param terms the list of terms to replace on the LookerFolder, or null to remove all terms from the LookerFolder
     * @return the LookerFolder that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static LookerFolder replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (LookerFolder) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the LookerFolder, without replacing existing terms linked to the LookerFolder.
     * Note: this operation must make two API calls ??? one to retrieve the LookerFolder's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the LookerFolder
     * @param terms the list of terms to append to the LookerFolder
     * @return the LookerFolder that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static LookerFolder appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (LookerFolder) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a LookerFolder, without replacing all existing terms linked to the LookerFolder.
     * Note: this operation must make two API calls ??? one to retrieve the LookerFolder's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the LookerFolder
     * @param terms the list of terms to remove from the LookerFolder, which must be referenced by GUID
     * @return the LookerFolder that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static LookerFolder removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (LookerFolder) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
