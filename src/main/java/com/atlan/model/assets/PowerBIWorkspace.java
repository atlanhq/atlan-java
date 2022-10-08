/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
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
public class PowerBIWorkspace extends PowerBI {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "PowerBIWorkspace";

    /** Fixed typeName for PowerBIWorkspaces. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String webUrl;

    /** TBC */
    @Attribute
    Long reportCount;

    /** TBC */
    @Attribute
    Long dashboardCount;

    /** TBC */
    @Attribute
    Long datasetCount;

    /** TBC */
    @Attribute
    Long dataflowCount;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<PowerBIReport> reports;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<PowerBIDataset> datasets;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<PowerBIDashboard> dashboards;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<PowerBIDataflow> dataflows;

    /**
     * Reference to a PowerBIWorkspace by GUID.
     *
     * @param guid the GUID of the PowerBIWorkspace to reference
     * @return reference to a PowerBIWorkspace that can be used for defining a relationship to a PowerBIWorkspace
     */
    public static PowerBIWorkspace refByGuid(String guid) {
        return PowerBIWorkspace.builder().guid(guid).build();
    }

    /**
     * Reference to a PowerBIWorkspace by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the PowerBIWorkspace to reference
     * @return reference to a PowerBIWorkspace that can be used for defining a relationship to a PowerBIWorkspace
     */
    public static PowerBIWorkspace refByQualifiedName(String qualifiedName) {
        return PowerBIWorkspace.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a PowerBIWorkspace.
     *
     * @param qualifiedName of the PowerBIWorkspace
     * @param name of the PowerBIWorkspace
     * @return the minimal request necessary to update the PowerBIWorkspace, as a builder
     */
    public static PowerBIWorkspaceBuilder<?, ?> updater(String qualifiedName, String name) {
        return PowerBIWorkspace.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PowerBIWorkspace, from a potentially
     * more-complete PowerBIWorkspace object.
     *
     * @return the minimal object necessary to update the PowerBIWorkspace, as a builder
     */
    @Override
    protected PowerBIWorkspaceBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Update the certificate on a PowerBIWorkspace.
     *
     * @param qualifiedName of the PowerBIWorkspace
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated PowerBIWorkspace, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIWorkspace updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (PowerBIWorkspace) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a PowerBIWorkspace.
     *
     * @param qualifiedName of the PowerBIWorkspace
     * @param name of the PowerBIWorkspace
     * @return the updated PowerBIWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIWorkspace removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (PowerBIWorkspace)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a PowerBIWorkspace.
     *
     * @param qualifiedName of the PowerBIWorkspace
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIWorkspace updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (PowerBIWorkspace) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a PowerBIWorkspace.
     *
     * @param qualifiedName of the PowerBIWorkspace
     * @param name of the PowerBIWorkspace
     * @return the updated PowerBIWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIWorkspace removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (PowerBIWorkspace)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a PowerBIWorkspace.
     *
     * @param qualifiedName of the PowerBIWorkspace
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the PowerBIWorkspace
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a PowerBIWorkspace.
     *
     * @param qualifiedName of the PowerBIWorkspace
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the PowerBIWorkspace
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the PowerBIWorkspace.
     *
     * @param qualifiedName for the PowerBIWorkspace
     * @param name human-readable name of the PowerBIWorkspace
     * @param terms the list of terms to replace on the PowerBIWorkspace, or null to remove all terms from the PowerBIWorkspace
     * @return the PowerBIWorkspace that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIWorkspace replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (PowerBIWorkspace) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the PowerBIWorkspace, without replacing existing terms linked to the PowerBIWorkspace.
     * Note: this operation must make two API calls — one to retrieve the PowerBIWorkspace's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the PowerBIWorkspace
     * @param terms the list of terms to append to the PowerBIWorkspace
     * @return the PowerBIWorkspace that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIWorkspace appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PowerBIWorkspace) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a PowerBIWorkspace, without replacing all existing terms linked to the PowerBIWorkspace.
     * Note: this operation must make two API calls — one to retrieve the PowerBIWorkspace's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the PowerBIWorkspace
     * @param terms the list of terms to remove from the PowerBIWorkspace, which must be referenced by GUID
     * @return the PowerBIWorkspace that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIWorkspace removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PowerBIWorkspace) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
