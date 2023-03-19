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
import java.util.Map;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a Salesforce report in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("cast")
public class SalesforceReport extends Salesforce {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SalesforceReport";

    /** Fixed typeName for SalesforceReports. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** ID of the report in Salesforce. */
    @Attribute
    String sourceId;

    /** Type of report in Salesforce. */
    @Attribute
    @Singular("putReportType")
    Map<String, String> reportType;

    /** List of column names on the report. */
    @Attribute
    @Singular
    SortedSet<String> detailColumns;

    /** TBC */
    @Attribute
    SalesforceOrganization organization;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<SalesforceDashboard> dashboards;

    /**
     * Reference to a SalesforceReport by GUID.
     *
     * @param guid the GUID of the SalesforceReport to reference
     * @return reference to a SalesforceReport that can be used for defining a relationship to a SalesforceReport
     */
    public static SalesforceReport refByGuid(String guid) {
        return SalesforceReport.builder().guid(guid).build();
    }

    /**
     * Reference to a SalesforceReport by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the SalesforceReport to reference
     * @return reference to a SalesforceReport that can be used for defining a relationship to a SalesforceReport
     */
    public static SalesforceReport refByQualifiedName(String qualifiedName) {
        return SalesforceReport.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a SalesforceReport.
     *
     * @param qualifiedName of the SalesforceReport
     * @param name of the SalesforceReport
     * @return the minimal request necessary to update the SalesforceReport, as a builder
     */
    public static SalesforceReportBuilder<?, ?> updater(String qualifiedName, String name) {
        return SalesforceReport.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SalesforceReport, from a potentially
     * more-complete SalesforceReport object.
     *
     * @return the minimal object necessary to update the SalesforceReport, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SalesforceReport are not found in the initial object
     */
    @Override
    public SalesforceReportBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "SalesforceReport", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a SalesforceReport by its GUID, complete with all of its relationships.
     *
     * @param guid of the SalesforceReport to retrieve
     * @return the requested full SalesforceReport, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceReport does not exist or the provided GUID is not a SalesforceReport
     */
    public static SalesforceReport retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof SalesforceReport) {
            return (SalesforceReport) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "SalesforceReport");
        }
    }

    /**
     * Retrieves a SalesforceReport by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the SalesforceReport to retrieve
     * @return the requested full SalesforceReport, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceReport does not exist
     */
    public static SalesforceReport retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof SalesforceReport) {
            return (SalesforceReport) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "SalesforceReport");
        }
    }

    /**
     * Restore the archived (soft-deleted) SalesforceReport to active.
     *
     * @param qualifiedName for the SalesforceReport
     * @return true if the SalesforceReport is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a SalesforceReport.
     *
     * @param qualifiedName of the SalesforceReport
     * @param name of the SalesforceReport
     * @return the updated SalesforceReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport removeDescription(String qualifiedName, String name) throws AtlanException {
        return (SalesforceReport) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SalesforceReport.
     *
     * @param qualifiedName of the SalesforceReport
     * @param name of the SalesforceReport
     * @return the updated SalesforceReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (SalesforceReport) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SalesforceReport.
     *
     * @param qualifiedName of the SalesforceReport
     * @param name of the SalesforceReport
     * @return the updated SalesforceReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport removeOwners(String qualifiedName, String name) throws AtlanException {
        return (SalesforceReport) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SalesforceReport.
     *
     * @param qualifiedName of the SalesforceReport
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SalesforceReport, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (SalesforceReport) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SalesforceReport.
     *
     * @param qualifiedName of the SalesforceReport
     * @param name of the SalesforceReport
     * @return the updated SalesforceReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (SalesforceReport) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SalesforceReport.
     *
     * @param qualifiedName of the SalesforceReport
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (SalesforceReport) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SalesforceReport.
     *
     * @param qualifiedName of the SalesforceReport
     * @param name of the SalesforceReport
     * @return the updated SalesforceReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (SalesforceReport) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Add classifications to a SalesforceReport.
     *
     * @param qualifiedName of the SalesforceReport
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the SalesforceReport
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a SalesforceReport.
     *
     * @param qualifiedName of the SalesforceReport
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the SalesforceReport
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the SalesforceReport.
     *
     * @param qualifiedName for the SalesforceReport
     * @param name human-readable name of the SalesforceReport
     * @param terms the list of terms to replace on the SalesforceReport, or null to remove all terms from the SalesforceReport
     * @return the SalesforceReport that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (SalesforceReport) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SalesforceReport, without replacing existing terms linked to the SalesforceReport.
     * Note: this operation must make two API calls — one to retrieve the SalesforceReport's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the SalesforceReport
     * @param terms the list of terms to append to the SalesforceReport
     * @return the SalesforceReport that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (SalesforceReport) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SalesforceReport, without replacing all existing terms linked to the SalesforceReport.
     * Note: this operation must make two API calls — one to retrieve the SalesforceReport's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the SalesforceReport
     * @param terms the list of terms to remove from the SalesforceReport, which must be referenced by GUID
     * @return the SalesforceReport that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceReport removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (SalesforceReport) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
