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
public class PowerBITable extends PowerBI {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "PowerBITable";

    /** Fixed typeName for PowerBITables. */
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
    @Singular
    SortedSet<String> powerBITableSourceExpressions;

    /** TBC */
    @Attribute
    Long powerBITableColumnCount;

    /** TBC */
    @Attribute
    Long powerBITableMeasureCount;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<PowerBIMeasure> measures;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<PowerBIColumn> columns;

    /** TBC */
    @Attribute
    PowerBIDataset dataset;

    /**
     * Reference to a PowerBITable by GUID.
     *
     * @param guid the GUID of the PowerBITable to reference
     * @return reference to a PowerBITable that can be used for defining a relationship to a PowerBITable
     */
    public static PowerBITable refByGuid(String guid) {
        return PowerBITable.builder().guid(guid).build();
    }

    /**
     * Reference to a PowerBITable by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the PowerBITable to reference
     * @return reference to a PowerBITable that can be used for defining a relationship to a PowerBITable
     */
    public static PowerBITable refByQualifiedName(String qualifiedName) {
        return PowerBITable.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a PowerBITable.
     *
     * @param qualifiedName of the PowerBITable
     * @param name of the PowerBITable
     * @return the minimal request necessary to update the PowerBITable, as a builder
     */
    public static PowerBITableBuilder<?, ?> updater(String qualifiedName, String name) {
        return PowerBITable.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PowerBITable, from a potentially
     * more-complete PowerBITable object.
     *
     * @return the minimal object necessary to update the PowerBITable, as a builder
     */
    @Override
    protected PowerBITableBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a PowerBITable by its GUID, complete with all of its relationships.
     *
     * @param guid of the PowerBITable to retrieve
     * @return the requested full PowerBITable, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PowerBITable does not exist or the provided GUID is not a PowerBITable
     */
    public static PowerBITable retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof PowerBITable) {
            return (PowerBITable) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a PowerBITable.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a PowerBITable by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the PowerBITable to retrieve
     * @return the requested full PowerBITable, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PowerBITable does not exist
     */
    public static PowerBITable retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof PowerBITable) {
            return (PowerBITable) entity;
        } else {
            throw new NotFoundException(
                    "No PowerBITable found with qualifiedName: " + qualifiedName,
                    "ATLAN_JAVA_CLIENT-404-003",
                    404,
                    null);
        }
    }
    /**
     * Update the certificate on a PowerBITable.
     *
     * @param qualifiedName of the PowerBITable
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated PowerBITable, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBITable updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (PowerBITable) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a PowerBITable.
     *
     * @param qualifiedName of the PowerBITable
     * @param name of the PowerBITable
     * @return the updated PowerBITable, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBITable removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (PowerBITable)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a PowerBITable.
     *
     * @param qualifiedName of the PowerBITable
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBITable updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (PowerBITable) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a PowerBITable.
     *
     * @param qualifiedName of the PowerBITable
     * @param name of the PowerBITable
     * @return the updated PowerBITable, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBITable removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (PowerBITable)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a PowerBITable.
     *
     * @param qualifiedName of the PowerBITable
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the PowerBITable
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a PowerBITable.
     *
     * @param qualifiedName of the PowerBITable
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the PowerBITable
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the PowerBITable.
     *
     * @param qualifiedName for the PowerBITable
     * @param name human-readable name of the PowerBITable
     * @param terms the list of terms to replace on the PowerBITable, or null to remove all terms from the PowerBITable
     * @return the PowerBITable that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBITable replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (PowerBITable) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the PowerBITable, without replacing existing terms linked to the PowerBITable.
     * Note: this operation must make two API calls — one to retrieve the PowerBITable's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the PowerBITable
     * @param terms the list of terms to append to the PowerBITable
     * @return the PowerBITable that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBITable appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PowerBITable) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a PowerBITable, without replacing all existing terms linked to the PowerBITable.
     * Note: this operation must make two API calls — one to retrieve the PowerBITable's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the PowerBITable
     * @param terms the list of terms to remove from the PowerBITable, which must be referenced by GUID
     * @return the PowerBITable that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBITable removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PowerBITable) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
