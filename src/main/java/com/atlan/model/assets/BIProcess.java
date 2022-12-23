/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class BIProcess extends AbstractProcess {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "BIProcess";

    /** Fixed typeName for BIProcesss. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /**
     * Reference to a BIProcess by GUID.
     *
     * @param guid the GUID of the BIProcess to reference
     * @return reference to a BIProcess that can be used for defining a relationship to a BIProcess
     */
    public static BIProcess refByGuid(String guid) {
        return BIProcess.builder().guid(guid).build();
    }

    /**
     * Reference to a BIProcess by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the BIProcess to reference
     * @return reference to a BIProcess that can be used for defining a relationship to a BIProcess
     */
    public static BIProcess refByQualifiedName(String qualifiedName) {
        return BIProcess.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a BIProcess.
     *
     * @param qualifiedName of the BIProcess
     * @param name of the BIProcess
     * @return the minimal request necessary to update the BIProcess, as a builder
     */
    public static BIProcessBuilder<?, ?> updater(String qualifiedName, String name) {
        return BIProcess.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a BIProcess, from a potentially
     * more-complete BIProcess object.
     *
     * @return the minimal object necessary to update the BIProcess, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for BIProcess are not found in the initial object
     */
    @Override
    public BIProcessBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "BIProcess", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a BIProcess by its GUID, complete with all of its relationships.
     *
     * @param guid of the BIProcess to retrieve
     * @return the requested full BIProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the BIProcess does not exist or the provided GUID is not a BIProcess
     */
    public static BIProcess retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof BIProcess) {
            return (BIProcess) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "BIProcess");
        }
    }

    /**
     * Retrieves a BIProcess by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the BIProcess to retrieve
     * @return the requested full BIProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the BIProcess does not exist
     */
    public static BIProcess retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof BIProcess) {
            return (BIProcess) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "BIProcess");
        }
    }

    /**
     * Restore the archived (soft-deleted) BIProcess to active.
     *
     * @param qualifiedName for the BIProcess
     * @return true if the BIProcess is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a BIProcess.
     *
     * @param qualifiedName of the BIProcess
     * @param name of the BIProcess
     * @return the updated BIProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BIProcess removeDescription(String qualifiedName, String name) throws AtlanException {
        return (BIProcess)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a BIProcess.
     *
     * @param qualifiedName of the BIProcess
     * @param name of the BIProcess
     * @return the updated BIProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BIProcess removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (BIProcess) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the owners from a BIProcess.
     *
     * @param qualifiedName of the BIProcess
     * @param name of the BIProcess
     * @return the updated BIProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BIProcess removeOwners(String qualifiedName, String name) throws AtlanException {
        return (BIProcess)
                Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the certificate on a BIProcess.
     *
     * @param qualifiedName of the BIProcess
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated BIProcess, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static BIProcess updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (BIProcess) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a BIProcess.
     *
     * @param qualifiedName of the BIProcess
     * @param name of the BIProcess
     * @return the updated BIProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BIProcess removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (BIProcess)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a BIProcess.
     *
     * @param qualifiedName of the BIProcess
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static BIProcess updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (BIProcess) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a BIProcess.
     *
     * @param qualifiedName of the BIProcess
     * @param name of the BIProcess
     * @return the updated BIProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BIProcess removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (BIProcess)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a BIProcess.
     *
     * @param qualifiedName of the BIProcess
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the BIProcess
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a BIProcess.
     *
     * @param qualifiedName of the BIProcess
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the BIProcess
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the BIProcess.
     *
     * @param qualifiedName for the BIProcess
     * @param name human-readable name of the BIProcess
     * @param terms the list of terms to replace on the BIProcess, or null to remove all terms from the BIProcess
     * @return the BIProcess that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static BIProcess replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (BIProcess) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the BIProcess, without replacing existing terms linked to the BIProcess.
     * Note: this operation must make two API calls — one to retrieve the BIProcess's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the BIProcess
     * @param terms the list of terms to append to the BIProcess
     * @return the BIProcess that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static BIProcess appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (BIProcess) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a BIProcess, without replacing all existing terms linked to the BIProcess.
     * Note: this operation must make two API calls — one to retrieve the BIProcess's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the BIProcess
     * @param terms the list of terms to remove from the BIProcess, which must be referenced by GUID
     * @return the BIProcess that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static BIProcess removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (BIProcess) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
