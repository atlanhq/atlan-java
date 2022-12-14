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
 * Instance of a README template in Atlan.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class ReadmeTemplate extends Resource {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ReadmeTemplate";

    /** Fixed typeName for ReadmeTemplates. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String icon;

    /** TBC */
    @Attribute
    LinkIconType iconType;

    /**
     * Reference to a ReadmeTemplate by GUID.
     *
     * @param guid the GUID of the ReadmeTemplate to reference
     * @return reference to a ReadmeTemplate that can be used for defining a relationship to a ReadmeTemplate
     */
    public static ReadmeTemplate refByGuid(String guid) {
        return ReadmeTemplate.builder().guid(guid).build();
    }

    /**
     * Reference to a ReadmeTemplate by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the ReadmeTemplate to reference
     * @return reference to a ReadmeTemplate that can be used for defining a relationship to a ReadmeTemplate
     */
    public static ReadmeTemplate refByQualifiedName(String qualifiedName) {
        return ReadmeTemplate.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a ReadmeTemplate.
     *
     * @param qualifiedName of the ReadmeTemplate
     * @param name of the ReadmeTemplate
     * @return the minimal request necessary to update the ReadmeTemplate, as a builder
     */
    public static ReadmeTemplateBuilder<?, ?> updater(String qualifiedName, String name) {
        return ReadmeTemplate.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ReadmeTemplate, from a potentially
     * more-complete ReadmeTemplate object.
     *
     * @return the minimal object necessary to update the ReadmeTemplate, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ReadmeTemplate are not found in the initial object
     */
    @Override
    public ReadmeTemplateBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "ReadmeTemplate", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a ReadmeTemplate by its GUID, complete with all of its relationships.
     *
     * @param guid of the ReadmeTemplate to retrieve
     * @return the requested full ReadmeTemplate, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ReadmeTemplate does not exist or the provided GUID is not a ReadmeTemplate
     */
    public static ReadmeTemplate retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof ReadmeTemplate) {
            return (ReadmeTemplate) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "ReadmeTemplate");
        }
    }

    /**
     * Retrieves a ReadmeTemplate by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the ReadmeTemplate to retrieve
     * @return the requested full ReadmeTemplate, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ReadmeTemplate does not exist
     */
    public static ReadmeTemplate retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof ReadmeTemplate) {
            return (ReadmeTemplate) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "ReadmeTemplate");
        }
    }

    /**
     * Restore the archived (soft-deleted) ReadmeTemplate to active.
     *
     * @param qualifiedName for the ReadmeTemplate
     * @return true if the ReadmeTemplate is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a ReadmeTemplate.
     *
     * @param qualifiedName of the ReadmeTemplate
     * @param name of the ReadmeTemplate
     * @return the updated ReadmeTemplate, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ReadmeTemplate removeDescription(String qualifiedName, String name) throws AtlanException {
        return (ReadmeTemplate) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ReadmeTemplate.
     *
     * @param qualifiedName of the ReadmeTemplate
     * @param name of the ReadmeTemplate
     * @return the updated ReadmeTemplate, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ReadmeTemplate removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (ReadmeTemplate) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ReadmeTemplate.
     *
     * @param qualifiedName of the ReadmeTemplate
     * @param name of the ReadmeTemplate
     * @return the updated ReadmeTemplate, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ReadmeTemplate removeOwners(String qualifiedName, String name) throws AtlanException {
        return (ReadmeTemplate) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ReadmeTemplate.
     *
     * @param qualifiedName of the ReadmeTemplate
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ReadmeTemplate, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ReadmeTemplate updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (ReadmeTemplate) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ReadmeTemplate.
     *
     * @param qualifiedName of the ReadmeTemplate
     * @param name of the ReadmeTemplate
     * @return the updated ReadmeTemplate, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ReadmeTemplate removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (ReadmeTemplate) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ReadmeTemplate.
     *
     * @param qualifiedName of the ReadmeTemplate
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ReadmeTemplate updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (ReadmeTemplate) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ReadmeTemplate.
     *
     * @param qualifiedName of the ReadmeTemplate
     * @param name of the ReadmeTemplate
     * @return the updated ReadmeTemplate, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ReadmeTemplate removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (ReadmeTemplate) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Add classifications to a ReadmeTemplate.
     *
     * @param qualifiedName of the ReadmeTemplate
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the ReadmeTemplate
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a ReadmeTemplate.
     *
     * @param qualifiedName of the ReadmeTemplate
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the ReadmeTemplate
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the ReadmeTemplate.
     *
     * @param qualifiedName for the ReadmeTemplate
     * @param name human-readable name of the ReadmeTemplate
     * @param terms the list of terms to replace on the ReadmeTemplate, or null to remove all terms from the ReadmeTemplate
     * @return the ReadmeTemplate that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ReadmeTemplate replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (ReadmeTemplate) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ReadmeTemplate, without replacing existing terms linked to the ReadmeTemplate.
     * Note: this operation must make two API calls ??? one to retrieve the ReadmeTemplate's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the ReadmeTemplate
     * @param terms the list of terms to append to the ReadmeTemplate
     * @return the ReadmeTemplate that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ReadmeTemplate appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (ReadmeTemplate) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ReadmeTemplate, without replacing all existing terms linked to the ReadmeTemplate.
     * Note: this operation must make two API calls ??? one to retrieve the ReadmeTemplate's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the ReadmeTemplate
     * @param terms the list of terms to remove from the ReadmeTemplate, which must be referenced by GUID
     * @return the ReadmeTemplate that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ReadmeTemplate removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (ReadmeTemplate) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
