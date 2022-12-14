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
 * Instance of a Mode chart in Atlan.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class ModeChart extends Mode {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ModeChart";

    /** Fixed typeName for ModeCharts. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String modeChartType;

    /** TBC */
    @Attribute
    ModeQuery modeQuery;

    /**
     * Reference to a ModeChart by GUID.
     *
     * @param guid the GUID of the ModeChart to reference
     * @return reference to a ModeChart that can be used for defining a relationship to a ModeChart
     */
    public static ModeChart refByGuid(String guid) {
        return ModeChart.builder().guid(guid).build();
    }

    /**
     * Reference to a ModeChart by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the ModeChart to reference
     * @return reference to a ModeChart that can be used for defining a relationship to a ModeChart
     */
    public static ModeChart refByQualifiedName(String qualifiedName) {
        return ModeChart.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a ModeChart.
     *
     * @param qualifiedName of the ModeChart
     * @param name of the ModeChart
     * @return the minimal request necessary to update the ModeChart, as a builder
     */
    public static ModeChartBuilder<?, ?> updater(String qualifiedName, String name) {
        return ModeChart.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ModeChart, from a potentially
     * more-complete ModeChart object.
     *
     * @return the minimal object necessary to update the ModeChart, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ModeChart are not found in the initial object
     */
    @Override
    public ModeChartBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "ModeChart", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a ModeChart by its GUID, complete with all of its relationships.
     *
     * @param guid of the ModeChart to retrieve
     * @return the requested full ModeChart, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeChart does not exist or the provided GUID is not a ModeChart
     */
    public static ModeChart retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof ModeChart) {
            return (ModeChart) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "ModeChart");
        }
    }

    /**
     * Retrieves a ModeChart by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the ModeChart to retrieve
     * @return the requested full ModeChart, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeChart does not exist
     */
    public static ModeChart retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof ModeChart) {
            return (ModeChart) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "ModeChart");
        }
    }

    /**
     * Restore the archived (soft-deleted) ModeChart to active.
     *
     * @param qualifiedName for the ModeChart
     * @return true if the ModeChart is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a ModeChart.
     *
     * @param qualifiedName of the ModeChart
     * @param name of the ModeChart
     * @return the updated ModeChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeChart removeDescription(String qualifiedName, String name) throws AtlanException {
        return (ModeChart) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ModeChart.
     *
     * @param qualifiedName of the ModeChart
     * @param name of the ModeChart
     * @return the updated ModeChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeChart removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (ModeChart) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ModeChart.
     *
     * @param qualifiedName of the ModeChart
     * @param name of the ModeChart
     * @return the updated ModeChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeChart removeOwners(String qualifiedName, String name) throws AtlanException {
        return (ModeChart) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ModeChart.
     *
     * @param qualifiedName of the ModeChart
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ModeChart, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModeChart updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (ModeChart) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ModeChart.
     *
     * @param qualifiedName of the ModeChart
     * @param name of the ModeChart
     * @return the updated ModeChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeChart removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (ModeChart) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ModeChart.
     *
     * @param qualifiedName of the ModeChart
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModeChart updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (ModeChart) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ModeChart.
     *
     * @param qualifiedName of the ModeChart
     * @param name of the ModeChart
     * @return the updated ModeChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeChart removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (ModeChart) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Add classifications to a ModeChart.
     *
     * @param qualifiedName of the ModeChart
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the ModeChart
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a ModeChart.
     *
     * @param qualifiedName of the ModeChart
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the ModeChart
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the ModeChart.
     *
     * @param qualifiedName for the ModeChart
     * @param name human-readable name of the ModeChart
     * @param terms the list of terms to replace on the ModeChart, or null to remove all terms from the ModeChart
     * @return the ModeChart that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ModeChart replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (ModeChart) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ModeChart, without replacing existing terms linked to the ModeChart.
     * Note: this operation must make two API calls ??? one to retrieve the ModeChart's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the ModeChart
     * @param terms the list of terms to append to the ModeChart
     * @return the ModeChart that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ModeChart appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (ModeChart) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ModeChart, without replacing all existing terms linked to the ModeChart.
     * Note: this operation must make two API calls ??? one to retrieve the ModeChart's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the ModeChart
     * @param terms the list of terms to remove from the ModeChart, which must be referenced by GUID
     * @return the ModeChart that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ModeChart removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (ModeChart) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
