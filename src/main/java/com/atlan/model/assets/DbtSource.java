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
 * Instance of a dbt source asset in Atlan.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class DbtSource extends Dbt {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DbtSource";

    /** Fixed typeName for DbtSources. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String dbtState;

    /** TBC */
    @Attribute
    String dbtFreshnessCriteria;

    /** TBC */
    @Attribute
    SQL sqlAsset;

    /**
     * Reference to a DbtSource by GUID.
     *
     * @param guid the GUID of the DbtSource to reference
     * @return reference to a DbtSource that can be used for defining a relationship to a DbtSource
     */
    public static DbtSource refByGuid(String guid) {
        return DbtSource.builder().guid(guid).build();
    }

    /**
     * Reference to a DbtSource by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the DbtSource to reference
     * @return reference to a DbtSource that can be used for defining a relationship to a DbtSource
     */
    public static DbtSource refByQualifiedName(String qualifiedName) {
        return DbtSource.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a DbtSource.
     *
     * @param qualifiedName of the DbtSource
     * @param name of the DbtSource
     * @return the minimal request necessary to update the DbtSource, as a builder
     */
    public static DbtSourceBuilder<?, ?> updater(String qualifiedName, String name) {
        return DbtSource.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DbtSource, from a potentially
     * more-complete DbtSource object.
     *
     * @return the minimal object necessary to update the DbtSource, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DbtSource are not found in the initial object
     */
    @Override
    public DbtSourceBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "DbtSource", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a DbtSource by its GUID, complete with all of its relationships.
     *
     * @param guid of the DbtSource to retrieve
     * @return the requested full DbtSource, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtSource does not exist or the provided GUID is not a DbtSource
     */
    public static DbtSource retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof DbtSource) {
            return (DbtSource) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "DbtSource");
        }
    }

    /**
     * Retrieves a DbtSource by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the DbtSource to retrieve
     * @return the requested full DbtSource, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtSource does not exist
     */
    public static DbtSource retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof DbtSource) {
            return (DbtSource) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "DbtSource");
        }
    }

    /**
     * Restore the archived (soft-deleted) DbtSource to active.
     *
     * @param qualifiedName for the DbtSource
     * @return true if the DbtSource is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a DbtSource.
     *
     * @param qualifiedName of the DbtSource
     * @param name of the DbtSource
     * @return the updated DbtSource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtSource removeDescription(String qualifiedName, String name) throws AtlanException {
        return (DbtSource) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DbtSource.
     *
     * @param qualifiedName of the DbtSource
     * @param name of the DbtSource
     * @return the updated DbtSource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtSource removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (DbtSource) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DbtSource.
     *
     * @param qualifiedName of the DbtSource
     * @param name of the DbtSource
     * @return the updated DbtSource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtSource removeOwners(String qualifiedName, String name) throws AtlanException {
        return (DbtSource) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DbtSource.
     *
     * @param qualifiedName of the DbtSource
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DbtSource, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtSource updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (DbtSource) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DbtSource.
     *
     * @param qualifiedName of the DbtSource
     * @param name of the DbtSource
     * @return the updated DbtSource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtSource removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (DbtSource) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DbtSource.
     *
     * @param qualifiedName of the DbtSource
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtSource updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (DbtSource) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DbtSource.
     *
     * @param qualifiedName of the DbtSource
     * @param name of the DbtSource
     * @return the updated DbtSource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtSource removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (DbtSource) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Add classifications to a DbtSource.
     *
     * @param qualifiedName of the DbtSource
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the DbtSource
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a DbtSource.
     *
     * @param qualifiedName of the DbtSource
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the DbtSource
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the DbtSource.
     *
     * @param qualifiedName for the DbtSource
     * @param name human-readable name of the DbtSource
     * @param terms the list of terms to replace on the DbtSource, or null to remove all terms from the DbtSource
     * @return the DbtSource that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DbtSource replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (DbtSource) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DbtSource, without replacing existing terms linked to the DbtSource.
     * Note: this operation must make two API calls — one to retrieve the DbtSource's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the DbtSource
     * @param terms the list of terms to append to the DbtSource
     * @return the DbtSource that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DbtSource appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (DbtSource) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DbtSource, without replacing all existing terms linked to the DbtSource.
     * Note: this operation must make two API calls — one to retrieve the DbtSource's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the DbtSource
     * @param terms the list of terms to remove from the DbtSource, which must be referenced by GUID
     * @return the DbtSource that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DbtSource removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (DbtSource) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
