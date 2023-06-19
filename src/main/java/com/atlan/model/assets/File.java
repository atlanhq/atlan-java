/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.FileType;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a file in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class File extends Asset implements IFile, IResource, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "File";

    /** Fixed typeName for Files. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    IAsset fileAssets;

    /** TBC */
    @Attribute
    String filePath;

    /** TBC */
    @Attribute
    FileType fileType;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    Boolean isGlobal;

    /** TBC */
    @Attribute
    String link;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    String reference;

    /** TBC */
    @Attribute
    @Singular("putResourceMetadata")
    Map<String, String> resourceMetadata;

    /**
     * Reference to a File by GUID.
     *
     * @param guid the GUID of the File to reference
     * @return reference to a File that can be used for defining a relationship to a File
     */
    public static File refByGuid(String guid) {
        return File.builder().guid(guid).build();
    }

    /**
     * Reference to a File by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the File to reference
     * @return reference to a File that can be used for defining a relationship to a File
     */
    public static File refByQualifiedName(String qualifiedName) {
        return File.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a File by its GUID, complete with all of its relationships.
     *
     * @param guid of the File to retrieve
     * @return the requested full File, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the File does not exist or the provided GUID is not a File
     */
    public static File retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof File) {
            return (File) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "File");
        }
    }

    /**
     * Retrieves a File by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the File to retrieve
     * @return the requested full File, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the File does not exist
     */
    public static File retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof File) {
            return (File) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "File");
        }
    }

    /**
     * Restore the archived (soft-deleted) File to active.
     *
     * @param qualifiedName for the File
     * @return true if the File is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a File.
     *
     * @param qualifiedName of the File
     * @param name of the File
     * @return the minimal request necessary to update the File, as a builder
     */
    public static FileBuilder<?, ?> updater(String qualifiedName, String name) {
        return File.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a File, from a potentially
     * more-complete File object.
     *
     * @return the minimal object necessary to update the File, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for File are not found in the initial object
     */
    @Override
    public FileBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "File", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a File.
     *
     * @param qualifiedName of the File
     * @param name of the File
     * @return the updated File, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static File removeDescription(String qualifiedName, String name) throws AtlanException {
        return (File) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a File.
     *
     * @param qualifiedName of the File
     * @param name of the File
     * @return the updated File, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static File removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (File) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a File.
     *
     * @param qualifiedName of the File
     * @param name of the File
     * @return the updated File, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static File removeOwners(String qualifiedName, String name) throws AtlanException {
        return (File) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a File.
     *
     * @param qualifiedName of the File
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated File, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static File updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (File) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a File.
     *
     * @param qualifiedName of the File
     * @param name of the File
     * @return the updated File, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static File removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (File) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a File.
     *
     * @param qualifiedName of the File
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static File updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (File) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a File.
     *
     * @param qualifiedName of the File
     * @param name of the File
     * @return the updated File, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static File removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (File) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the File.
     *
     * @param qualifiedName for the File
     * @param name human-readable name of the File
     * @param terms the list of terms to replace on the File, or null to remove all terms from the File
     * @return the File that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static File replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (File) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the File, without replacing existing terms linked to the File.
     * Note: this operation must make two API calls — one to retrieve the File's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the File
     * @param terms the list of terms to append to the File
     * @return the File that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static File appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (File) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a File, without replacing all existing terms linked to the File.
     * Note: this operation must make two API calls — one to retrieve the File's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the File
     * @param terms the list of terms to remove from the File, which must be referenced by GUID
     * @return the File that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static File removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (File) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a File, without replacing existing Atlan tags linked to the File.
     * Note: this operation must make two API calls — one to retrieve the File's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the File
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated File
     */
    public static File appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (File) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a File, without replacing existing Atlan tags linked to the File.
     * Note: this operation must make two API calls — one to retrieve the File's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the File
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated File
     */
    public static File appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (File) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a File.
     *
     * @param qualifiedName of the File
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the File
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a File.
     *
     * @param qualifiedName of the File
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the File
     * @deprecated see {@link #appendAtlanTags(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    public static void addAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a File.
     *
     * @param qualifiedName of the File
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the File
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
