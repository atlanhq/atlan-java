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
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class PowerBIMeasure extends PowerBI {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "PowerBIMeasure";

    /** Fixed typeName for PowerBIMeasures. */
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
    String powerBIMeasureExpression;

    /** TBC */
    @Attribute
    Boolean powerBIIsExternalMeasure;

    /** TBC */
    @Attribute
    PowerBITable table;

    /**
     * Reference to a PowerBIMeasure by GUID.
     *
     * @param guid the GUID of the PowerBIMeasure to reference
     * @return reference to a PowerBIMeasure that can be used for defining a relationship to a PowerBIMeasure
     */
    public static PowerBIMeasure refByGuid(String guid) {
        return PowerBIMeasure.builder().guid(guid).build();
    }

    /**
     * Reference to a PowerBIMeasure by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the PowerBIMeasure to reference
     * @return reference to a PowerBIMeasure that can be used for defining a relationship to a PowerBIMeasure
     */
    public static PowerBIMeasure refByQualifiedName(String qualifiedName) {
        return PowerBIMeasure.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a PowerBIMeasure.
     *
     * @param qualifiedName of the PowerBIMeasure
     * @param name of the PowerBIMeasure
     * @return the minimal request necessary to update the PowerBIMeasure, as a builder
     */
    public static PowerBIMeasureBuilder<?, ?> updater(String qualifiedName, String name) {
        return PowerBIMeasure.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PowerBIMeasure, from a potentially
     * more-complete PowerBIMeasure object.
     *
     * @return the minimal object necessary to update the PowerBIMeasure, as a builder
     */
    @Override
    protected PowerBIMeasureBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a PowerBIMeasure by its GUID, complete with all of its relationships.
     *
     * @param guid of the PowerBIMeasure to retrieve
     * @return the requested full PowerBIMeasure, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PowerBIMeasure does not exist or the provided GUID is not a PowerBIMeasure
     */
    public static PowerBIMeasure retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof PowerBIMeasure) {
            return (PowerBIMeasure) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a PowerBIMeasure.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a PowerBIMeasure by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the PowerBIMeasure to retrieve
     * @return the requested full PowerBIMeasure, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PowerBIMeasure does not exist
     */
    public static PowerBIMeasure retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof PowerBIMeasure) {
            return (PowerBIMeasure) entity;
        } else {
            throw new NotFoundException(
                    "No PowerBIMeasure found with qualifiedName: " + qualifiedName,
                    "ATLAN_JAVA_CLIENT-404-003",
                    404,
                    null);
        }
    }

    /**
     * Update the certificate on a PowerBIMeasure.
     *
     * @param qualifiedName of the PowerBIMeasure
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated PowerBIMeasure, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIMeasure updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (PowerBIMeasure) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a PowerBIMeasure.
     *
     * @param qualifiedName of the PowerBIMeasure
     * @param name of the PowerBIMeasure
     * @return the updated PowerBIMeasure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIMeasure removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (PowerBIMeasure)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a PowerBIMeasure.
     *
     * @param qualifiedName of the PowerBIMeasure
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIMeasure updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (PowerBIMeasure) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a PowerBIMeasure.
     *
     * @param qualifiedName of the PowerBIMeasure
     * @param name of the PowerBIMeasure
     * @return the updated PowerBIMeasure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIMeasure removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (PowerBIMeasure)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a PowerBIMeasure.
     *
     * @param qualifiedName of the PowerBIMeasure
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the PowerBIMeasure
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a PowerBIMeasure.
     *
     * @param qualifiedName of the PowerBIMeasure
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the PowerBIMeasure
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the PowerBIMeasure.
     *
     * @param qualifiedName for the PowerBIMeasure
     * @param name human-readable name of the PowerBIMeasure
     * @param terms the list of terms to replace on the PowerBIMeasure, or null to remove all terms from the PowerBIMeasure
     * @return the PowerBIMeasure that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIMeasure replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (PowerBIMeasure) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the PowerBIMeasure, without replacing existing terms linked to the PowerBIMeasure.
     * Note: this operation must make two API calls — one to retrieve the PowerBIMeasure's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the PowerBIMeasure
     * @param terms the list of terms to append to the PowerBIMeasure
     * @return the PowerBIMeasure that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIMeasure appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PowerBIMeasure) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a PowerBIMeasure, without replacing all existing terms linked to the PowerBIMeasure.
     * Note: this operation must make two API calls — one to retrieve the PowerBIMeasure's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the PowerBIMeasure
     * @param terms the list of terms to remove from the PowerBIMeasure, which must be referenced by GUID
     * @return the PowerBIMeasure that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIMeasure removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PowerBIMeasure) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
