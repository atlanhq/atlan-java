/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class DbtMetric extends Metric {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DbtMetric";

    /** Fixed typeName for DbtMetrics. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    DbtModel dbtModel;

    /**
     * Reference to a DbtMetric by GUID.
     *
     * @param guid the GUID of the DbtMetric to reference
     * @return reference to a DbtMetric that can be used for defining a relationship to a DbtMetric
     */
    public static DbtMetric refByGuid(String guid) {
        return DbtMetric.builder().guid(guid).build();
    }

    /**
     * Reference to a DbtMetric by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the DbtMetric to reference
     * @return reference to a DbtMetric that can be used for defining a relationship to a DbtMetric
     */
    public static DbtMetric refByQualifiedName(String qualifiedName) {
        return DbtMetric.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param name of the DbtMetric
     * @return the minimal request necessary to update the DbtMetric, as a builder
     */
    public static DbtMetricBuilder<?, ?> updater(String qualifiedName, String name) {
        return DbtMetric.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DbtMetric, from a potentially
     * more-complete DbtMetric object.
     *
     * @return the minimal object necessary to update the DbtMetric, as a builder
     */
    @Override
    protected DbtMetricBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Update the certificate on a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DbtMetric, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtMetric updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (DbtMetric) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param name of the DbtMetric
     * @return the updated DbtMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtMetric removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (DbtMetric)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtMetric updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (DbtMetric) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param name of the DbtMetric
     * @return the updated DbtMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtMetric removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (DbtMetric)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the DbtMetric
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the DbtMetric
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the DbtMetric.
     *
     * @param qualifiedName for the DbtMetric
     * @param name human-readable name of the DbtMetric
     * @param terms the list of terms to replace on the DbtMetric, or null to remove all terms from the DbtMetric
     * @return the DbtMetric that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DbtMetric replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (DbtMetric) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DbtMetric, without replacing existing terms linked to the DbtMetric.
     * Note: this operation must make two API calls — one to retrieve the DbtMetric's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the DbtMetric
     * @param terms the list of terms to append to the DbtMetric
     * @return the DbtMetric that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DbtMetric appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (DbtMetric) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DbtMetric, without replacing all existing terms linked to the DbtMetric.
     * Note: this operation must make two API calls — one to retrieve the DbtMetric's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the DbtMetric
     * @param terms the list of terms to remove from the DbtMetric, which must be referenced by GUID
     * @return the DbtMetric that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DbtMetric removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (DbtMetric) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
