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
import java.util.Map;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("cast")
public class TableauMetric extends Tableau {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TableauMetric";

    /** Fixed typeName for TableauMetrics. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String siteQualifiedName;

    /** TBC */
    @Attribute
    String projectQualifiedName;

    /** TBC */
    @Attribute
    String topLevelProjectQualifiedName;

    /** TBC */
    @Attribute
    @Singular("addProjectHierarchy")
    List<Map<String, String>> projectHierarchy;

    /** TBC */
    @Attribute
    TableauProject project;

    /**
     * Reference to a TableauMetric by GUID.
     *
     * @param guid the GUID of the TableauMetric to reference
     * @return reference to a TableauMetric that can be used for defining a relationship to a TableauMetric
     */
    public static TableauMetric refByGuid(String guid) {
        return TableauMetric.builder().guid(guid).build();
    }

    /**
     * Reference to a TableauMetric by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the TableauMetric to reference
     * @return reference to a TableauMetric that can be used for defining a relationship to a TableauMetric
     */
    public static TableauMetric refByQualifiedName(String qualifiedName) {
        return TableauMetric.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a TableauMetric.
     *
     * @param qualifiedName of the TableauMetric
     * @param name of the TableauMetric
     * @return the minimal request necessary to update the TableauMetric, as a builder
     */
    public static TableauMetricBuilder<?, ?> updater(String qualifiedName, String name) {
        return TableauMetric.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TableauMetric, from a potentially
     * more-complete TableauMetric object.
     *
     * @return the minimal object necessary to update the TableauMetric, as a builder
     */
    @Override
    protected TableauMetricBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a TableauMetric by its GUID, complete with all of its relationships.
     *
     * @param guid of the TableauMetric to retrieve
     * @return the requested full TableauMetric, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauMetric does not exist or the provided GUID is not a TableauMetric
     */
    public static TableauMetric retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof TableauMetric) {
            return (TableauMetric) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a TableauMetric.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a TableauMetric by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the TableauMetric to retrieve
     * @return the requested full TableauMetric, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauMetric does not exist
     */
    public static TableauMetric retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof TableauMetric) {
            return (TableauMetric) entity;
        } else {
            throw new NotFoundException(
                    "No TableauMetric found with qualifiedName: " + qualifiedName,
                    "ATLAN_JAVA_CLIENT-404-003",
                    404,
                    null);
        }
    }

    /**
     * Update the certificate on a TableauMetric.
     *
     * @param qualifiedName of the TableauMetric
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TableauMetric, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauMetric updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (TableauMetric) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a TableauMetric.
     *
     * @param qualifiedName of the TableauMetric
     * @param name of the TableauMetric
     * @return the updated TableauMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauMetric removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (TableauMetric)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a TableauMetric.
     *
     * @param qualifiedName of the TableauMetric
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauMetric updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (TableauMetric) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a TableauMetric.
     *
     * @param qualifiedName of the TableauMetric
     * @param name of the TableauMetric
     * @return the updated TableauMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauMetric removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (TableauMetric)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a TableauMetric.
     *
     * @param qualifiedName of the TableauMetric
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the TableauMetric
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a TableauMetric.
     *
     * @param qualifiedName of the TableauMetric
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the TableauMetric
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the TableauMetric.
     *
     * @param qualifiedName for the TableauMetric
     * @param name human-readable name of the TableauMetric
     * @param terms the list of terms to replace on the TableauMetric, or null to remove all terms from the TableauMetric
     * @return the TableauMetric that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TableauMetric replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (TableauMetric) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the TableauMetric, without replacing existing terms linked to the TableauMetric.
     * Note: this operation must make two API calls — one to retrieve the TableauMetric's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the TableauMetric
     * @param terms the list of terms to append to the TableauMetric
     * @return the TableauMetric that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static TableauMetric appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (TableauMetric) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a TableauMetric, without replacing all existing terms linked to the TableauMetric.
     * Note: this operation must make two API calls — one to retrieve the TableauMetric's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the TableauMetric
     * @param terms the list of terms to remove from the TableauMetric, which must be referenced by GUID
     * @return the TableauMetric that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static TableauMetric removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (TableauMetric) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
