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
 * Instance of a Looker explore in Atlan.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class LookerExplore extends Looker {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "LookerExplore";

    /** Fixed typeName for LookerExplores. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String projectName;

    /** TBC */
    @Attribute
    String modelName;

    /** TBC */
    @Attribute
    String sourceConnectionName;

    /** TBC */
    @Attribute
    String viewName;

    /** TBC */
    @Attribute
    String sqlTableName;

    /** TBC */
    @Attribute
    LookerProject project;

    /** TBC */
    @Attribute
    LookerModel model;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<LookerField> fields;

    /**
     * Reference to a LookerExplore by GUID.
     *
     * @param guid the GUID of the LookerExplore to reference
     * @return reference to a LookerExplore that can be used for defining a relationship to a LookerExplore
     */
    public static LookerExplore refByGuid(String guid) {
        return LookerExplore.builder().guid(guid).build();
    }

    /**
     * Reference to a LookerExplore by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the LookerExplore to reference
     * @return reference to a LookerExplore that can be used for defining a relationship to a LookerExplore
     */
    public static LookerExplore refByQualifiedName(String qualifiedName) {
        return LookerExplore.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param name of the LookerExplore
     * @return the minimal request necessary to update the LookerExplore, as a builder
     */
    public static LookerExploreBuilder<?, ?> updater(String qualifiedName, String name) {
        return LookerExplore.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a LookerExplore, from a potentially
     * more-complete LookerExplore object.
     *
     * @return the minimal object necessary to update the LookerExplore, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for LookerExplore are not found in the initial object
     */
    @Override
    public LookerExploreBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "LookerExplore", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a LookerExplore by its GUID, complete with all of its relationships.
     *
     * @param guid of the LookerExplore to retrieve
     * @return the requested full LookerExplore, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerExplore does not exist or the provided GUID is not a LookerExplore
     */
    public static LookerExplore retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof LookerExplore) {
            return (LookerExplore) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "LookerExplore");
        }
    }

    /**
     * Retrieves a LookerExplore by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the LookerExplore to retrieve
     * @return the requested full LookerExplore, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerExplore does not exist
     */
    public static LookerExplore retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof LookerExplore) {
            return (LookerExplore) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "LookerExplore");
        }
    }

    /**
     * Restore the archived (soft-deleted) LookerExplore to active.
     *
     * @param qualifiedName for the LookerExplore
     * @return true if the LookerExplore is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param name of the LookerExplore
     * @return the updated LookerExplore, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore removeDescription(String qualifiedName, String name) throws AtlanException {
        return (LookerExplore)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param name of the LookerExplore
     * @return the updated LookerExplore, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (LookerExplore) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the owners from a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param name of the LookerExplore
     * @return the updated LookerExplore, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore removeOwners(String qualifiedName, String name) throws AtlanException {
        return (LookerExplore)
                Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the certificate on a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated LookerExplore, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (LookerExplore) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param name of the LookerExplore
     * @return the updated LookerExplore, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (LookerExplore)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (LookerExplore) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param name of the LookerExplore
     * @return the updated LookerExplore, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (LookerExplore)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the LookerExplore
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the LookerExplore
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the LookerExplore.
     *
     * @param qualifiedName for the LookerExplore
     * @param name human-readable name of the LookerExplore
     * @param terms the list of terms to replace on the LookerExplore, or null to remove all terms from the LookerExplore
     * @return the LookerExplore that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static LookerExplore replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (LookerExplore) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the LookerExplore, without replacing existing terms linked to the LookerExplore.
     * Note: this operation must make two API calls — one to retrieve the LookerExplore's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the LookerExplore
     * @param terms the list of terms to append to the LookerExplore
     * @return the LookerExplore that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static LookerExplore appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (LookerExplore) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a LookerExplore, without replacing all existing terms linked to the LookerExplore.
     * Note: this operation must make two API calls — one to retrieve the LookerExplore's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the LookerExplore
     * @param terms the list of terms to remove from the LookerExplore, which must be referenced by GUID
     * @return the LookerExplore that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static LookerExplore removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (LookerExplore) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
