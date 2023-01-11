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
 * Instance of a Power BI tile in Atlan.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class PowerBITile extends PowerBI {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "PowerBITile";

    /** Fixed typeName for PowerBITiles. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String workspaceQualifiedName;

    /** TBC */
    @Attribute
    String dashboardQualifiedName;

    /** TBC */
    @Attribute
    PowerBIReport report;

    /** TBC */
    @Attribute
    PowerBIDataset dataset;

    /** TBC */
    @Attribute
    PowerBIDashboard dashboard;

    /**
     * Reference to a PowerBITile by GUID.
     *
     * @param guid the GUID of the PowerBITile to reference
     * @return reference to a PowerBITile that can be used for defining a relationship to a PowerBITile
     */
    public static PowerBITile refByGuid(String guid) {
        return PowerBITile.builder().guid(guid).build();
    }

    /**
     * Reference to a PowerBITile by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the PowerBITile to reference
     * @return reference to a PowerBITile that can be used for defining a relationship to a PowerBITile
     */
    public static PowerBITile refByQualifiedName(String qualifiedName) {
        return PowerBITile.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a PowerBITile.
     *
     * @param qualifiedName of the PowerBITile
     * @param name of the PowerBITile
     * @return the minimal request necessary to update the PowerBITile, as a builder
     */
    public static PowerBITileBuilder<?, ?> updater(String qualifiedName, String name) {
        return PowerBITile.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PowerBITile, from a potentially
     * more-complete PowerBITile object.
     *
     * @return the minimal object necessary to update the PowerBITile, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for PowerBITile are not found in the initial object
     */
    @Override
    public PowerBITileBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "PowerBITile", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a PowerBITile by its GUID, complete with all of its relationships.
     *
     * @param guid of the PowerBITile to retrieve
     * @return the requested full PowerBITile, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PowerBITile does not exist or the provided GUID is not a PowerBITile
     */
    public static PowerBITile retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof PowerBITile) {
            return (PowerBITile) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "PowerBITile");
        }
    }

    /**
     * Retrieves a PowerBITile by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the PowerBITile to retrieve
     * @return the requested full PowerBITile, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PowerBITile does not exist
     */
    public static PowerBITile retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof PowerBITile) {
            return (PowerBITile) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "PowerBITile");
        }
    }

    /**
     * Restore the archived (soft-deleted) PowerBITile to active.
     *
     * @param qualifiedName for the PowerBITile
     * @return true if the PowerBITile is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a PowerBITile.
     *
     * @param qualifiedName of the PowerBITile
     * @param name of the PowerBITile
     * @return the updated PowerBITile, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBITile removeDescription(String qualifiedName, String name) throws AtlanException {
        return (PowerBITile) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a PowerBITile.
     *
     * @param qualifiedName of the PowerBITile
     * @param name of the PowerBITile
     * @return the updated PowerBITile, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBITile removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (PowerBITile) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a PowerBITile.
     *
     * @param qualifiedName of the PowerBITile
     * @param name of the PowerBITile
     * @return the updated PowerBITile, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBITile removeOwners(String qualifiedName, String name) throws AtlanException {
        return (PowerBITile) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a PowerBITile.
     *
     * @param qualifiedName of the PowerBITile
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated PowerBITile, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBITile updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (PowerBITile) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a PowerBITile.
     *
     * @param qualifiedName of the PowerBITile
     * @param name of the PowerBITile
     * @return the updated PowerBITile, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBITile removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (PowerBITile) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a PowerBITile.
     *
     * @param qualifiedName of the PowerBITile
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBITile updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (PowerBITile) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a PowerBITile.
     *
     * @param qualifiedName of the PowerBITile
     * @param name of the PowerBITile
     * @return the updated PowerBITile, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBITile removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (PowerBITile) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Add classifications to a PowerBITile.
     *
     * @param qualifiedName of the PowerBITile
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the PowerBITile
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a PowerBITile.
     *
     * @param qualifiedName of the PowerBITile
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the PowerBITile
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the PowerBITile.
     *
     * @param qualifiedName for the PowerBITile
     * @param name human-readable name of the PowerBITile
     * @param terms the list of terms to replace on the PowerBITile, or null to remove all terms from the PowerBITile
     * @return the PowerBITile that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBITile replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (PowerBITile) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the PowerBITile, without replacing existing terms linked to the PowerBITile.
     * Note: this operation must make two API calls — one to retrieve the PowerBITile's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the PowerBITile
     * @param terms the list of terms to append to the PowerBITile
     * @return the PowerBITile that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBITile appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PowerBITile) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a PowerBITile, without replacing all existing terms linked to the PowerBITile.
     * Note: this operation must make two API calls — one to retrieve the PowerBITile's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the PowerBITile
     * @param terms the list of terms to remove from the PowerBITile, which must be referenced by GUID
     * @return the PowerBITile that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBITile removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PowerBITile) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
