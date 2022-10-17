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
public class PowerBIDataflow extends PowerBI {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "PowerBIDataflow";

    /** Fixed typeName for PowerBIDataflows. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String workspaceQualifiedName;

    /** TBC */
    @Attribute
    String webUrl;

    /** TBC */
    @Attribute
    PowerBIWorkspace workspace;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<PowerBIDataset> datasets;

    /**
     * Reference to a PowerBIDataflow by GUID.
     *
     * @param guid the GUID of the PowerBIDataflow to reference
     * @return reference to a PowerBIDataflow that can be used for defining a relationship to a PowerBIDataflow
     */
    public static PowerBIDataflow refByGuid(String guid) {
        return PowerBIDataflow.builder().guid(guid).build();
    }

    /**
     * Reference to a PowerBIDataflow by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the PowerBIDataflow to reference
     * @return reference to a PowerBIDataflow that can be used for defining a relationship to a PowerBIDataflow
     */
    public static PowerBIDataflow refByQualifiedName(String qualifiedName) {
        return PowerBIDataflow.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a PowerBIDataflow.
     *
     * @param qualifiedName of the PowerBIDataflow
     * @param name of the PowerBIDataflow
     * @return the minimal request necessary to update the PowerBIDataflow, as a builder
     */
    public static PowerBIDataflowBuilder<?, ?> updater(String qualifiedName, String name) {
        return PowerBIDataflow.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PowerBIDataflow, from a potentially
     * more-complete PowerBIDataflow object.
     *
     * @return the minimal object necessary to update the PowerBIDataflow, as a builder
     */
    @Override
    protected PowerBIDataflowBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a PowerBIDataflow by its GUID, complete with all of its relationships.
     *
     * @param guid of the PowerBIDataflow to retrieve
     * @return the requested full PowerBIDataflow, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PowerBIDataflow does not exist or the provided GUID is not a PowerBIDataflow
     */
    public static PowerBIDataflow retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof PowerBIDataflow) {
            return (PowerBIDataflow) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a PowerBIDataflow.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a PowerBIDataflow by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the PowerBIDataflow to retrieve
     * @return the requested full PowerBIDataflow, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PowerBIDataflow does not exist
     */
    public static PowerBIDataflow retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof PowerBIDataflow) {
            return (PowerBIDataflow) entity;
        } else {
            throw new NotFoundException(
                    "No PowerBIDataflow found with qualifiedName: " + qualifiedName,
                    "ATLAN_JAVA_CLIENT-404-003",
                    404,
                    null);
        }
    }
    /**
     * Update the certificate on a PowerBIDataflow.
     *
     * @param qualifiedName of the PowerBIDataflow
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated PowerBIDataflow, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIDataflow updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (PowerBIDataflow) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a PowerBIDataflow.
     *
     * @param qualifiedName of the PowerBIDataflow
     * @param name of the PowerBIDataflow
     * @return the updated PowerBIDataflow, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIDataflow removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (PowerBIDataflow)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a PowerBIDataflow.
     *
     * @param qualifiedName of the PowerBIDataflow
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIDataflow updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (PowerBIDataflow) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a PowerBIDataflow.
     *
     * @param qualifiedName of the PowerBIDataflow
     * @param name of the PowerBIDataflow
     * @return the updated PowerBIDataflow, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIDataflow removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (PowerBIDataflow)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a PowerBIDataflow.
     *
     * @param qualifiedName of the PowerBIDataflow
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the PowerBIDataflow
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a PowerBIDataflow.
     *
     * @param qualifiedName of the PowerBIDataflow
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the PowerBIDataflow
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the PowerBIDataflow.
     *
     * @param qualifiedName for the PowerBIDataflow
     * @param name human-readable name of the PowerBIDataflow
     * @param terms the list of terms to replace on the PowerBIDataflow, or null to remove all terms from the PowerBIDataflow
     * @return the PowerBIDataflow that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIDataflow replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (PowerBIDataflow) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the PowerBIDataflow, without replacing existing terms linked to the PowerBIDataflow.
     * Note: this operation must make two API calls — one to retrieve the PowerBIDataflow's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the PowerBIDataflow
     * @param terms the list of terms to append to the PowerBIDataflow
     * @return the PowerBIDataflow that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIDataflow appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PowerBIDataflow) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a PowerBIDataflow, without replacing all existing terms linked to the PowerBIDataflow.
     * Note: this operation must make two API calls — one to retrieve the PowerBIDataflow's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the PowerBIDataflow
     * @param terms the list of terms to remove from the PowerBIDataflow, which must be referenced by GUID
     * @return the PowerBIDataflow that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIDataflow removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PowerBIDataflow) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
