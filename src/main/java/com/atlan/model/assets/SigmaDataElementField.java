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
 * Instance of a Sigma data element field in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class SigmaDataElementField extends Sigma {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SigmaDataElementField";

    /** Fixed typeName for SigmaDataElementFields. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    Boolean sigmaDataElementFieldIsHidden;

    /** TBC */
    @Attribute
    String sigmaDataElementFieldFormula;

    /** Data element that contains this data element field. */
    @Attribute
    SigmaDataElement sigmaDataElement;

    /**
     * Reference to a SigmaDataElementField by GUID.
     *
     * @param guid the GUID of the SigmaDataElementField to reference
     * @return reference to a SigmaDataElementField that can be used for defining a relationship to a SigmaDataElementField
     */
    public static SigmaDataElementField refByGuid(String guid) {
        return SigmaDataElementField.builder().guid(guid).build();
    }

    /**
     * Reference to a SigmaDataElementField by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the SigmaDataElementField to reference
     * @return reference to a SigmaDataElementField that can be used for defining a relationship to a SigmaDataElementField
     */
    public static SigmaDataElementField refByQualifiedName(String qualifiedName) {
        return SigmaDataElementField.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a SigmaDataElementField.
     *
     * @param qualifiedName of the SigmaDataElementField
     * @param name of the SigmaDataElementField
     * @return the minimal request necessary to update the SigmaDataElementField, as a builder
     */
    public static SigmaDataElementFieldBuilder<?, ?> updater(String qualifiedName, String name) {
        return SigmaDataElementField.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SigmaDataElementField, from a potentially
     * more-complete SigmaDataElementField object.
     *
     * @return the minimal object necessary to update the SigmaDataElementField, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SigmaDataElementField are not found in the initial object
     */
    @Override
    public SigmaDataElementFieldBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "SigmaDataElementField", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a SigmaDataElementField by its GUID, complete with all of its relationships.
     *
     * @param guid of the SigmaDataElementField to retrieve
     * @return the requested full SigmaDataElementField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDataElementField does not exist or the provided GUID is not a SigmaDataElementField
     */
    public static SigmaDataElementField retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof SigmaDataElementField) {
            return (SigmaDataElementField) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "SigmaDataElementField");
        }
    }

    /**
     * Retrieves a SigmaDataElementField by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the SigmaDataElementField to retrieve
     * @return the requested full SigmaDataElementField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDataElementField does not exist
     */
    public static SigmaDataElementField retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof SigmaDataElementField) {
            return (SigmaDataElementField) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "SigmaDataElementField");
        }
    }

    /**
     * Restore the archived (soft-deleted) SigmaDataElementField to active.
     *
     * @param qualifiedName for the SigmaDataElementField
     * @return true if the SigmaDataElementField is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a SigmaDataElementField.
     *
     * @param qualifiedName of the SigmaDataElementField
     * @param name of the SigmaDataElementField
     * @return the updated SigmaDataElementField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField removeDescription(String qualifiedName, String name) throws AtlanException {
        return (SigmaDataElementField) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SigmaDataElementField.
     *
     * @param qualifiedName of the SigmaDataElementField
     * @param name of the SigmaDataElementField
     * @return the updated SigmaDataElementField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (SigmaDataElementField) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SigmaDataElementField.
     *
     * @param qualifiedName of the SigmaDataElementField
     * @param name of the SigmaDataElementField
     * @return the updated SigmaDataElementField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField removeOwners(String qualifiedName, String name) throws AtlanException {
        return (SigmaDataElementField) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SigmaDataElementField.
     *
     * @param qualifiedName of the SigmaDataElementField
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SigmaDataElementField, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (SigmaDataElementField)
                Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SigmaDataElementField.
     *
     * @param qualifiedName of the SigmaDataElementField
     * @param name of the SigmaDataElementField
     * @return the updated SigmaDataElementField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (SigmaDataElementField) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SigmaDataElementField.
     *
     * @param qualifiedName of the SigmaDataElementField
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (SigmaDataElementField)
                Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SigmaDataElementField.
     *
     * @param qualifiedName of the SigmaDataElementField
     * @param name of the SigmaDataElementField
     * @return the updated SigmaDataElementField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (SigmaDataElementField) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Add classifications to a SigmaDataElementField.
     *
     * @param qualifiedName of the SigmaDataElementField
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the SigmaDataElementField
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a SigmaDataElementField.
     *
     * @param qualifiedName of the SigmaDataElementField
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the SigmaDataElementField
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
     * Remove a classification from a SigmaDataElementField.
     *
     * @param qualifiedName of the SigmaDataElementField
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the SigmaDataElementField
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the SigmaDataElementField.
     *
     * @param qualifiedName for the SigmaDataElementField
     * @param name human-readable name of the SigmaDataElementField
     * @param terms the list of terms to replace on the SigmaDataElementField, or null to remove all terms from the SigmaDataElementField
     * @return the SigmaDataElementField that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (SigmaDataElementField) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SigmaDataElementField, without replacing existing terms linked to the SigmaDataElementField.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataElementField's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the SigmaDataElementField
     * @param terms the list of terms to append to the SigmaDataElementField
     * @return the SigmaDataElementField that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField appendTerms(String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        return (SigmaDataElementField) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SigmaDataElementField, without replacing all existing terms linked to the SigmaDataElementField.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataElementField's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the SigmaDataElementField
     * @param terms the list of terms to remove from the SigmaDataElementField, which must be referenced by GUID
     * @return the SigmaDataElementField that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField removeTerms(String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        return (SigmaDataElementField) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
