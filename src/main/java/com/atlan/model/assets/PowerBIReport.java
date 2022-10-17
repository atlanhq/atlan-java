/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class PowerBIReport extends PowerBI {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "PowerBIReport";

    /** Fixed typeName for PowerBIReports. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String workspaceQualifiedName;

    /** TBC */
    @Attribute
    String datasetQualifiedName;

    /** TBC */
    @Attribute
    String webUrl;

    /** TBC */
    @Attribute
    Long pageCount;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<PowerBITile> tiles;

    /** TBC */
    @Attribute
    PowerBIWorkspace workspace;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<PowerBIPage> pages;

    /** TBC */
    @Attribute
    PowerBIDataset dataset;

    /**
     * Reference to a PowerBIReport by GUID.
     *
     * @param guid the GUID of the PowerBIReport to reference
     * @return reference to a PowerBIReport that can be used for defining a relationship to a PowerBIReport
     */
    public static PowerBIReport refByGuid(String guid) {
        return PowerBIReport.builder().guid(guid).build();
    }

    /**
     * Reference to a PowerBIReport by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the PowerBIReport to reference
     * @return reference to a PowerBIReport that can be used for defining a relationship to a PowerBIReport
     */
    public static PowerBIReport refByQualifiedName(String qualifiedName) {
        return PowerBIReport.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a PowerBIReport.
     *
     * @param qualifiedName of the PowerBIReport
     * @param name of the PowerBIReport
     * @return the minimal request necessary to update the PowerBIReport, as a builder
     */
    public static PowerBIReportBuilder<?, ?> updater(String qualifiedName, String name) {
        return PowerBIReport.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PowerBIReport, from a potentially
     * more-complete PowerBIReport object.
     *
     * @return the minimal object necessary to update the PowerBIReport, as a builder
     */
    @Override
    protected PowerBIReportBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a PowerBIReport by its GUID, complete with all of its relationships.
     *
     * @param guid of the PowerBIReport to retrieve
     * @return the requested full PowerBIReport, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PowerBIReport does not exist or the provided GUID is not a PowerBIReport
     */
    public static PowerBIReport retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof PowerBIReport) {
            return (PowerBIReport) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a PowerBIReport.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a PowerBIReport by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the PowerBIReport to retrieve
     * @return the requested full PowerBIReport, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PowerBIReport does not exist
     */
    public static PowerBIReport retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof PowerBIReport) {
            return (PowerBIReport) entity;
        } else {
            throw new NotFoundException(
                    "No PowerBIReport found with qualifiedName: " + qualifiedName,
                    "ATLAN_JAVA_CLIENT-404-003",
                    404,
                    null);
        }
    }
    /**
     * Update the certificate on a PowerBIReport.
     *
     * @param qualifiedName of the PowerBIReport
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated PowerBIReport, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIReport updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (PowerBIReport) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a PowerBIReport.
     *
     * @param qualifiedName of the PowerBIReport
     * @param name of the PowerBIReport
     * @return the updated PowerBIReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIReport removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (PowerBIReport)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a PowerBIReport.
     *
     * @param qualifiedName of the PowerBIReport
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIReport updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (PowerBIReport) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a PowerBIReport.
     *
     * @param qualifiedName of the PowerBIReport
     * @param name of the PowerBIReport
     * @return the updated PowerBIReport, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIReport removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (PowerBIReport)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a PowerBIReport.
     *
     * @param qualifiedName of the PowerBIReport
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the PowerBIReport
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a PowerBIReport.
     *
     * @param qualifiedName of the PowerBIReport
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the PowerBIReport
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the PowerBIReport.
     *
     * @param qualifiedName for the PowerBIReport
     * @param name human-readable name of the PowerBIReport
     * @param terms the list of terms to replace on the PowerBIReport, or null to remove all terms from the PowerBIReport
     * @return the PowerBIReport that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIReport replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (PowerBIReport) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the PowerBIReport, without replacing existing terms linked to the PowerBIReport.
     * Note: this operation must make two API calls — one to retrieve the PowerBIReport's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the PowerBIReport
     * @param terms the list of terms to append to the PowerBIReport
     * @return the PowerBIReport that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIReport appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PowerBIReport) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a PowerBIReport, without replacing all existing terms linked to the PowerBIReport.
     * Note: this operation must make two API calls — one to retrieve the PowerBIReport's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the PowerBIReport
     * @param terms the list of terms to remove from the PowerBIReport, which must be referenced by GUID
     * @return the PowerBIReport that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIReport removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PowerBIReport) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
