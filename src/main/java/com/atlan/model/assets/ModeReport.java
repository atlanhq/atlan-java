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
 * Instance of a Mode report in Atlan.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class ModeReport extends Mode {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ModeReport";

    /** Fixed typeName for ModeReports. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String modeCollectionToken;

    /** TBC */
    @Attribute
    Long modeReportPublishedAt;

    /** TBC */
    @Attribute
    Long modeQueryCount;

    /** TBC */
    @Attribute
    Long modeChartCount;

    /** TBC */
    @Attribute
    String modeQueryPreview;

    /** TBC */
    @Attribute
    Boolean modeIsPublic;

    /** TBC */
    @Attribute
    Boolean modeIsShared;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ModeCollection> modeCollections;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ModeQuery> modeQueries;

    /**
     * Reference to a ModeReport by GUID.
     *
     * @param guid the GUID of the ModeReport to reference
     * @return reference to a ModeReport that can be used for defining a relationship to a ModeReport
     */
    public static ModeReport refByGuid(String guid) {
        return ModeReport.builder().guid(guid).build();
    }

    /**
     * Reference to a ModeReport by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the ModeReport to reference
     * @return reference to a ModeReport that can be used for defining a relationship to a ModeReport
     */
    public static ModeReport refByQualifiedName(String qualifiedName) {
        return ModeReport.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a ModeReport.
     *
     * @param qualifiedName of the ModeReport
     * @param name of the ModeReport
     * @return the minimal request necessary to update the ModeReport, as a builder
     */
    public static ModeReportBuilder<?, ?> updater(String qualifiedName, String name) {
        return ModeReport.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ModeReport, from a potentially
     * more-complete ModeReport object.
     *
     * @return the minimal object necessary to update the ModeReport, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ModeReport are not found in the initial object
     */
    @Override
    public ModeReportBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "ModeReport", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a ModeReport by its GUID, complete with all of its relationships.
     *
     * @param guid of the ModeReport to retrieve
     * @return the requested full ModeReport, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeReport does not exist or the provided GUID is not a ModeReport
     */
    public static ModeReport retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof ModeReport) {
            return (ModeReport) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "ModeReport");
        }
    }

    /**
     * Retrieves a ModeReport by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the ModeReport to retrieve
     * @return the requested full ModeReport, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeReport does not exist
     */
    public static ModeReport retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof ModeReport) {
            return (ModeReport) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "ModeReport");
        }
    }

    /**
     * Restore the archived (soft-deleted) ModeReport to active.
     *
     * @param qualifiedName for the ModeReport
     * @return true if the ModeReport is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a ModeReport.
     *
     * @param qualifiedName of the ModeReport
     * @param name of the ModeReport
     * @return the updated ModeReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeReport removeDescription(String qualifiedName, String name) throws AtlanException {
        return (ModeReport)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a ModeReport.
     *
     * @param qualifiedName of the ModeReport
     * @param name of the ModeReport
     * @return the updated ModeReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeReport removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (ModeReport) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the owners from a ModeReport.
     *
     * @param qualifiedName of the ModeReport
     * @param name of the ModeReport
     * @return the updated ModeReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeReport removeOwners(String qualifiedName, String name) throws AtlanException {
        return (ModeReport)
                Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the certificate on a ModeReport.
     *
     * @param qualifiedName of the ModeReport
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ModeReport, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModeReport updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (ModeReport) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ModeReport.
     *
     * @param qualifiedName of the ModeReport
     * @param name of the ModeReport
     * @return the updated ModeReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeReport removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (ModeReport)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a ModeReport.
     *
     * @param qualifiedName of the ModeReport
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModeReport updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (ModeReport) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ModeReport.
     *
     * @param qualifiedName of the ModeReport
     * @param name of the ModeReport
     * @return the updated ModeReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeReport removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (ModeReport)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a ModeReport.
     *
     * @param qualifiedName of the ModeReport
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the ModeReport
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a ModeReport.
     *
     * @param qualifiedName of the ModeReport
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the ModeReport
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the ModeReport.
     *
     * @param qualifiedName for the ModeReport
     * @param name human-readable name of the ModeReport
     * @param terms the list of terms to replace on the ModeReport, or null to remove all terms from the ModeReport
     * @return the ModeReport that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ModeReport replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (ModeReport) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ModeReport, without replacing existing terms linked to the ModeReport.
     * Note: this operation must make two API calls — one to retrieve the ModeReport's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the ModeReport
     * @param terms the list of terms to append to the ModeReport
     * @return the ModeReport that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ModeReport appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (ModeReport) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ModeReport, without replacing all existing terms linked to the ModeReport.
     * Note: this operation must make two API calls — one to retrieve the ModeReport's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the ModeReport
     * @param terms the list of terms to remove from the ModeReport, which must be referenced by GUID
     * @return the ModeReport that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ModeReport removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (ModeReport) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
