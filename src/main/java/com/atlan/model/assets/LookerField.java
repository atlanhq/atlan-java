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
 * Instance of a Looker field in Atlan.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class LookerField extends Looker {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "LookerField";

    /** Fixed typeName for LookerFields. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String projectName;

    /** TBC */
    @Attribute
    String lookerExploreQualifiedName;

    /** TBC */
    @Attribute
    String lookerViewQualifiedName;

    /** TBC */
    @Attribute
    String modelName;

    /** TBC */
    @Attribute
    String sourceDefinition;

    /** TBC */
    @Attribute
    String lookerFieldDataType;

    /** TBC */
    @Attribute
    Integer lookerTimesUsed;

    /** TBC */
    @Attribute
    LookerView view;

    /** TBC */
    @Attribute
    LookerExplore explore;

    /** TBC */
    @Attribute
    LookerProject project;

    /** TBC */
    @Attribute
    LookerModel model;

    /**
     * Reference to a LookerField by GUID.
     *
     * @param guid the GUID of the LookerField to reference
     * @return reference to a LookerField that can be used for defining a relationship to a LookerField
     */
    public static LookerField refByGuid(String guid) {
        return LookerField.builder().guid(guid).build();
    }

    /**
     * Reference to a LookerField by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the LookerField to reference
     * @return reference to a LookerField that can be used for defining a relationship to a LookerField
     */
    public static LookerField refByQualifiedName(String qualifiedName) {
        return LookerField.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a LookerField.
     *
     * @param qualifiedName of the LookerField
     * @param name of the LookerField
     * @return the minimal request necessary to update the LookerField, as a builder
     */
    public static LookerFieldBuilder<?, ?> updater(String qualifiedName, String name) {
        return LookerField.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a LookerField, from a potentially
     * more-complete LookerField object.
     *
     * @return the minimal object necessary to update the LookerField, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for LookerField are not found in the initial object
     */
    @Override
    public LookerFieldBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "LookerField", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a LookerField by its GUID, complete with all of its relationships.
     *
     * @param guid of the LookerField to retrieve
     * @return the requested full LookerField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerField does not exist or the provided GUID is not a LookerField
     */
    public static LookerField retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof LookerField) {
            return (LookerField) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "LookerField");
        }
    }

    /**
     * Retrieves a LookerField by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the LookerField to retrieve
     * @return the requested full LookerField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerField does not exist
     */
    public static LookerField retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof LookerField) {
            return (LookerField) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "LookerField");
        }
    }

    /**
     * Restore the archived (soft-deleted) LookerField to active.
     *
     * @param qualifiedName for the LookerField
     * @return true if the LookerField is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a LookerField.
     *
     * @param qualifiedName of the LookerField
     * @param name of the LookerField
     * @return the updated LookerField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerField removeDescription(String qualifiedName, String name) throws AtlanException {
        return (LookerField) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a LookerField.
     *
     * @param qualifiedName of the LookerField
     * @param name of the LookerField
     * @return the updated LookerField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerField removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (LookerField) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a LookerField.
     *
     * @param qualifiedName of the LookerField
     * @param name of the LookerField
     * @return the updated LookerField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerField removeOwners(String qualifiedName, String name) throws AtlanException {
        return (LookerField) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a LookerField.
     *
     * @param qualifiedName of the LookerField
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated LookerField, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerField updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (LookerField) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a LookerField.
     *
     * @param qualifiedName of the LookerField
     * @param name of the LookerField
     * @return the updated LookerField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerField removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (LookerField) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a LookerField.
     *
     * @param qualifiedName of the LookerField
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerField updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (LookerField) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a LookerField.
     *
     * @param qualifiedName of the LookerField
     * @param name of the LookerField
     * @return the updated LookerField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerField removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (LookerField) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Add classifications to a LookerField.
     *
     * @param qualifiedName of the LookerField
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the LookerField
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a LookerField.
     *
     * @param qualifiedName of the LookerField
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the LookerField
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the LookerField.
     *
     * @param qualifiedName for the LookerField
     * @param name human-readable name of the LookerField
     * @param terms the list of terms to replace on the LookerField, or null to remove all terms from the LookerField
     * @return the LookerField that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static LookerField replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (LookerField) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the LookerField, without replacing existing terms linked to the LookerField.
     * Note: this operation must make two API calls ??? one to retrieve the LookerField's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the LookerField
     * @param terms the list of terms to append to the LookerField
     * @return the LookerField that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static LookerField appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (LookerField) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a LookerField, without replacing all existing terms linked to the LookerField.
     * Note: this operation must make two API calls ??? one to retrieve the LookerField's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the LookerField
     * @param terms the list of terms to remove from the LookerField, which must be referenced by GUID
     * @return the LookerField that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static LookerField removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (LookerField) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
