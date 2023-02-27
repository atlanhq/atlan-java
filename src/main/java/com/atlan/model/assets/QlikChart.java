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
 * TBC
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class QlikChart extends Qlik {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "QlikChart";

    /** Fixed typeName for QlikCharts. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String qlikChartSubtitle;

    /** TBC */
    @Attribute
    String qlikChartFootnote;

    /** TBC */
    @Attribute
    String qlikChartOrientation;

    /** TBC */
    @Attribute
    String qlikChartType;

    /** TBC */
    @Attribute
    QlikSheet qlikSheet;

    /**
     * Reference to a QlikChart by GUID.
     *
     * @param guid the GUID of the QlikChart to reference
     * @return reference to a QlikChart that can be used for defining a relationship to a QlikChart
     */
    public static QlikChart refByGuid(String guid) {
        return QlikChart.builder().guid(guid).build();
    }

    /**
     * Reference to a QlikChart by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the QlikChart to reference
     * @return reference to a QlikChart that can be used for defining a relationship to a QlikChart
     */
    public static QlikChart refByQualifiedName(String qualifiedName) {
        return QlikChart.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a QlikChart.
     *
     * @param qualifiedName of the QlikChart
     * @param name of the QlikChart
     * @return the minimal request necessary to update the QlikChart, as a builder
     */
    public static QlikChartBuilder<?, ?> updater(String qualifiedName, String name) {
        return QlikChart.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a QlikChart, from a potentially
     * more-complete QlikChart object.
     *
     * @return the minimal object necessary to update the QlikChart, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for QlikChart are not found in the initial object
     */
    @Override
    public QlikChartBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "QlikChart", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a QlikChart by its GUID, complete with all of its relationships.
     *
     * @param guid of the QlikChart to retrieve
     * @return the requested full QlikChart, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikChart does not exist or the provided GUID is not a QlikChart
     */
    public static QlikChart retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof QlikChart) {
            return (QlikChart) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "QlikChart");
        }
    }

    /**
     * Retrieves a QlikChart by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the QlikChart to retrieve
     * @return the requested full QlikChart, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikChart does not exist
     */
    public static QlikChart retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof QlikChart) {
            return (QlikChart) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "QlikChart");
        }
    }

    /**
     * Restore the archived (soft-deleted) QlikChart to active.
     *
     * @param qualifiedName for the QlikChart
     * @return true if the QlikChart is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a QlikChart.
     *
     * @param qualifiedName of the QlikChart
     * @param name of the QlikChart
     * @return the updated QlikChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikChart removeDescription(String qualifiedName, String name) throws AtlanException {
        return (QlikChart) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a QlikChart.
     *
     * @param qualifiedName of the QlikChart
     * @param name of the QlikChart
     * @return the updated QlikChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikChart removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (QlikChart) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a QlikChart.
     *
     * @param qualifiedName of the QlikChart
     * @param name of the QlikChart
     * @return the updated QlikChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikChart removeOwners(String qualifiedName, String name) throws AtlanException {
        return (QlikChart) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a QlikChart.
     *
     * @param qualifiedName of the QlikChart
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QlikChart, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikChart updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (QlikChart) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a QlikChart.
     *
     * @param qualifiedName of the QlikChart
     * @param name of the QlikChart
     * @return the updated QlikChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikChart removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (QlikChart) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a QlikChart.
     *
     * @param qualifiedName of the QlikChart
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikChart updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (QlikChart) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a QlikChart.
     *
     * @param qualifiedName of the QlikChart
     * @param name of the QlikChart
     * @return the updated QlikChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikChart removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (QlikChart) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Add classifications to a QlikChart.
     *
     * @param qualifiedName of the QlikChart
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the QlikChart
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a QlikChart.
     *
     * @param qualifiedName of the QlikChart
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the QlikChart
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the QlikChart.
     *
     * @param qualifiedName for the QlikChart
     * @param name human-readable name of the QlikChart
     * @param terms the list of terms to replace on the QlikChart, or null to remove all terms from the QlikChart
     * @return the QlikChart that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QlikChart replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (QlikChart) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the QlikChart, without replacing existing terms linked to the QlikChart.
     * Note: this operation must make two API calls — one to retrieve the QlikChart's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the QlikChart
     * @param terms the list of terms to append to the QlikChart
     * @return the QlikChart that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static QlikChart appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (QlikChart) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a QlikChart, without replacing all existing terms linked to the QlikChart.
     * Note: this operation must make two API calls — one to retrieve the QlikChart's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the QlikChart
     * @param terms the list of terms to remove from the QlikChart, which must be referenced by GUID
     * @return the QlikChart that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static QlikChart removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (QlikChart) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
