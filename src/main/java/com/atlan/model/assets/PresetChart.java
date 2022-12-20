/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.StringUtils;
import java.util.ArrayList;
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
@SuppressWarnings("cast")
public class PresetChart extends Preset {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "PresetChart";

    /** Fixed typeName for PresetCharts. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Markdown-based description of the chart. */
    @Attribute
    String presetChartDescriptionMarkdown;

    /** TBC */
    @Attribute
    @Singular("putPresetChartFormData")
    Map<String, String> presetChartFormData;

    /** Collection in which the chart exists. */
    @Attribute
    PresetDashboard presetDashboard;

    /**
     * Reference to a PresetChart by GUID.
     *
     * @param guid the GUID of the PresetChart to reference
     * @return reference to a PresetChart that can be used for defining a relationship to a PresetChart
     */
    public static PresetChart refByGuid(String guid) {
        return PresetChart.builder().guid(guid).build();
    }

    /**
     * Reference to a PresetChart by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the PresetChart to reference
     * @return reference to a PresetChart that can be used for defining a relationship to a PresetChart
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
     * Builds the minimal object necessary to update a PresetChart.
     *
     * @param qualifiedName of the PresetChart
     * @param name of the PresetChart
     * @return the minimal request necessary to update the PresetChart, as a builder
     */
    public static PresetChartBuilder<?, ?> updater(String qualifiedName, String name) {
        return PresetChart.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PresetChart, from a potentially
     * more-complete PresetChart object.
     *
     * @return the minimal object necessary to update the PresetChart, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for PresetChart are not found in the initial object
     */
    @Override
    public PresetChartBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    "Required field for updating PresetChart is missing.",
                    String.join(",", missing),
                    "ATLAN-JAVA-CLIENT-400-404",
                    400,
                    null);
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a PresetChart by its GUID, complete with all of its relationships.
     *
     * @param guid of the PresetChart to retrieve
     * @return the requested full PresetChart, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PresetChart does not exist or the provided GUID is not a PresetChart
     */
    public static PresetChart retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException("No asset found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (asset instanceof PresetChart) {
            return (PresetChart) asset;
        } else {
            throw new NotFoundException(
                    "Asset with GUID " + guid + " is not a PresetChart.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a PresetChart by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the PresetChart to retrieve
     * @return the requested full PresetChart, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PresetChart does not exist
     */
    public static PresetChart retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof PresetChart) {
            return (PresetChart) asset;
        } else {
            throw new NotFoundException(
                    "No PresetChart found with qualifiedName: " + qualifiedName,
                    "ATLAN_JAVA_CLIENT-404-003",
                    404,
                    null);
        }
    }

    /**
     * Restore the archived (soft-deleted) PresetChart to active.
     *
     * @param qualifiedName for the PresetChart
     * @return true if the PresetChart is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a PresetChart.
     *
     * @param qualifiedName of the PresetChart
     * @param name of the PresetChart
     * @return the updated PresetChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeDescription(String qualifiedName, String name) throws AtlanException {
        return (PresetChart)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a PresetChart.
     *
     * @param qualifiedName of the PresetChart
     * @param name of the PresetChart
     * @return the updated PresetChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (PresetChart) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the owners from a PresetChart.
     *
     * @param qualifiedName of the PresetChart
     * @param name of the PresetChart
     * @return the updated PresetChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeOwners(String qualifiedName, String name) throws AtlanException {
        return (PresetChart)
                Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the certificate on a PresetChart.
     *
     * @param qualifiedName of the PresetChart
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated PresetChart, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (PresetChart) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a PresetChart.
     *
     * @param qualifiedName of the PresetChart
     * @param name of the PresetChart
     * @return the updated PresetChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (PresetChart)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a PresetChart.
     *
     * @param qualifiedName of the PresetChart
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
     * Remove the announcement from a PresetChart.
     *
     * @param qualifiedName of the PresetChart
     * @param name of the PresetChart
     * @return the updated PresetChart, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (PresetChart)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a PresetChart.
     *
     * @param qualifiedName of the PresetChart
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the PresetChart
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a PresetChart.
     *
     * @param qualifiedName of the PresetChart
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the PresetChart
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the PresetChart.
     *
     * @param qualifiedName for the PresetChart
     * @param name human-readable name of the PresetChart
     * @param terms the list of terms to replace on the PresetChart, or null to remove all terms from the PresetChart
     * @return the PresetChart that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static PresetChart replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (PresetChart) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the PresetChart, without replacing existing terms linked to the PresetChart.
     * Note: this operation must make two API calls — one to retrieve the PresetChart's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the PresetChart
     * @param terms the list of terms to append to the PresetChart
     * @return the PresetChart that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static PresetChart appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PresetChart) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a PresetChart, without replacing all existing terms linked to the PresetChart.
     * Note: this operation must make two API calls — one to retrieve the PresetChart's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the PresetChart
     * @param terms the list of terms to remove from the PresetChart, which must be referenced by GUID
     * @return the PresetChart that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static PresetChart removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PresetChart) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
