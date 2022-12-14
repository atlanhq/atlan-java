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
 * Instance of a Mode collection in Atlan.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class ModeCollection extends Mode {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ModeCollection";

    /** Fixed typeName for ModeCollections. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String modeCollectionType;

    /** TBC */
    @Attribute
    String modeCollectionState;

    /** TBC */
    @Attribute
    ModeWorkspace modeWorkspace;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ModeReport> modeReports;

    /**
     * Reference to a ModeCollection by GUID.
     *
     * @param guid the GUID of the ModeCollection to reference
     * @return reference to a ModeCollection that can be used for defining a relationship to a ModeCollection
     */
    public static ModeCollection refByGuid(String guid) {
        return ModeCollection.builder().guid(guid).build();
    }

    /**
     * Reference to a ModeCollection by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the ModeCollection to reference
     * @return reference to a ModeCollection that can be used for defining a relationship to a ModeCollection
     */
    public static ModeCollection refByQualifiedName(String qualifiedName) {
        return ModeCollection.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a ModeCollection.
     *
     * @param qualifiedName of the ModeCollection
     * @param name of the ModeCollection
     * @return the minimal request necessary to update the ModeCollection, as a builder
     */
    public static ModeCollectionBuilder<?, ?> updater(String qualifiedName, String name) {
        return ModeCollection.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ModeCollection, from a potentially
     * more-complete ModeCollection object.
     *
     * @return the minimal object necessary to update the ModeCollection, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ModeCollection are not found in the initial object
     */
    @Override
    public ModeCollectionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "ModeCollection", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a ModeCollection by its GUID, complete with all of its relationships.
     *
     * @param guid of the ModeCollection to retrieve
     * @return the requested full ModeCollection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeCollection does not exist or the provided GUID is not a ModeCollection
     */
    public static ModeCollection retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof ModeCollection) {
            return (ModeCollection) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "ModeCollection");
        }
    }

    /**
     * Retrieves a ModeCollection by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the ModeCollection to retrieve
     * @return the requested full ModeCollection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeCollection does not exist
     */
    public static ModeCollection retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof ModeCollection) {
            return (ModeCollection) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "ModeCollection");
        }
    }

    /**
     * Restore the archived (soft-deleted) ModeCollection to active.
     *
     * @param qualifiedName for the ModeCollection
     * @return true if the ModeCollection is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a ModeCollection.
     *
     * @param qualifiedName of the ModeCollection
     * @param name of the ModeCollection
     * @return the updated ModeCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection removeDescription(String qualifiedName, String name) throws AtlanException {
        return (ModeCollection) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ModeCollection.
     *
     * @param qualifiedName of the ModeCollection
     * @param name of the ModeCollection
     * @return the updated ModeCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (ModeCollection) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ModeCollection.
     *
     * @param qualifiedName of the ModeCollection
     * @param name of the ModeCollection
     * @return the updated ModeCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection removeOwners(String qualifiedName, String name) throws AtlanException {
        return (ModeCollection) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ModeCollection.
     *
     * @param qualifiedName of the ModeCollection
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ModeCollection, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (ModeCollection) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ModeCollection.
     *
     * @param qualifiedName of the ModeCollection
     * @param name of the ModeCollection
     * @return the updated ModeCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (ModeCollection) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ModeCollection.
     *
     * @param qualifiedName of the ModeCollection
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (ModeCollection) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ModeCollection.
     *
     * @param qualifiedName of the ModeCollection
     * @param name of the ModeCollection
     * @return the updated ModeCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeCollection removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (ModeCollection) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Add classifications to a ModeCollection.
     *
     * @param qualifiedName of the ModeCollection
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the ModeCollection
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a ModeCollection.
     *
     * @param qualifiedName of the ModeCollection
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the ModeCollection
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the ModeCollection.
     *
     * @param qualifiedName for the ModeCollection
     * @param name human-readable name of the ModeCollection
     * @param terms the list of terms to replace on the ModeCollection, or null to remove all terms from the ModeCollection
     * @return the ModeCollection that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ModeCollection replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (ModeCollection) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ModeCollection, without replacing existing terms linked to the ModeCollection.
     * Note: this operation must make two API calls ??? one to retrieve the ModeCollection's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the ModeCollection
     * @param terms the list of terms to append to the ModeCollection
     * @return the ModeCollection that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ModeCollection appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (ModeCollection) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ModeCollection, without replacing all existing terms linked to the ModeCollection.
     * Note: this operation must make two API calls ??? one to retrieve the ModeCollection's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the ModeCollection
     * @param terms the list of terms to remove from the ModeCollection, which must be referenced by GUID
     * @return the ModeCollection that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ModeCollection removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (ModeCollection) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
