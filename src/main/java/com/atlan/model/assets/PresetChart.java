/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.StringUtils;
import java.util.List;
import java.util.Map;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a Preset chart in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class PresetChart extends Preset {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "PresetChart";

    /** Fixed typeName for Preset charts. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Markdown-based description of the chart. */
    @Attribute
    String presetChartDescriptionMarkdown;

    /** TBC */
    @Attribute
    Map<String, String> presetChartFormData;

    /** Collection in which the chart exists. */
    @Attribute
    PresetDashboard presetDashboard;

    /**
     * Reference to a Preset chart by GUID.
     *
     * @param guid the GUID of the Preset chart to reference
     * @return reference to a Preset chart that can be used for defining a relationship to a Preset chart
     */
    public static PresetChart refByGuid(String guid) {
        return PresetChart.builder().guid(guid).build();
    }

    /**
     * Reference to a Preset chart by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the Preset chart to reference
     * @return reference to a Preset chart that can be used for defining a relationship to a Preset chart
     */
    public static PresetChart refByQualifiedName(String qualifiedName) {
        return PresetChart.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to create a Preset chart.
     *
     * @param name of the chart
     * @param collectionQualifiedName unique name of the collection in which the chart exists
     * @return the minimal object necessary to create the chart, as a builder
     */
    public static PresetChartBuilder<?, ?> creator(String name, String collectionQualifiedName) {
        String[] tokens = collectionQualifiedName.split("/");
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(tokens);
        String workspaceQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(collectionQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(workspaceQualifiedName);
        return PresetChart.builder()
                .name(name)
                .qualifiedName(collectionQualifiedName + "/" + name)
                .connectorType(connectorType)
                .presetDashboardQualifiedName(collectionQualifiedName)
                .presetDashboard(PresetDashboard.refByQualifiedName(collectionQualifiedName))
                .presetWorkspaceQualifiedName(workspaceQualifiedName)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a Preset chart.
     *
     * @param qualifiedName of the chart
     * @param name of the chart
     * @return the minimal object necessary to update the chart, as a builder
     */
    public static PresetChartBuilder<?, ?> updater(String qualifiedName, String name) {
        return PresetChart.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Preset chart, from a potentially
     * more-complete Preset chart object.
     *
     * @return the minimal object necessary to update the Preset chart, as a builder
     */
    @Override
    protected PresetChartBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Update the certificate on a Preset chart.
     *
     * @param qualifiedName of the chart
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated chart, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (PresetChart) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a Preset chart.
     *
     * @param qualifiedName of the chart
     * @param name of the chart
     * @return the updated chart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (PresetChart)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a Preset chart.
     *
     * @param qualifiedName of the chart
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (PresetChart) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a Preset chart.
     *
     * @param qualifiedName of the chart
     * @param name of the chart
     * @return the updated chart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (PresetChart)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a Preset chart.
     *
     * @param qualifiedName of the chart
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the chart
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a Preset chart.
     *
     * @param qualifiedName of the chart
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the chart
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the chart.
     *
     * @param qualifiedName for the chart
     * @param name human-readable name of the chart
     * @param terms the list of terms to replace on the chart, or null to remove all terms from the chart
     * @return the chart that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static PresetChart replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (PresetChart) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the Preset chart, without replacing existing terms linked to the chart.
     * Note: this operation must make two API calls — one to retrieve the chart's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the chart
     * @param terms the list of terms to append to the chart
     * @return the chart that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static PresetChart appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PresetChart) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a Preset chart, without replacing all existing terms linked to the chart.
     * Note: this operation must make two API calls — one to retrieve the chart's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the chart
     * @param terms the list of terms to remove from the chart, which must be referenced by GUID
     * @return the chart that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PresetChart) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
