/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.relations.UniqueAttributes;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a process in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class LineageProcess extends AbstractProcess {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Process";

    /** Fixed typeName for LineageProcesss. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /**
     * Reference to a LineageProcess by GUID.
     *
     * @param guid the GUID of the LineageProcess to reference
     * @return reference to a LineageProcess that can be used for defining a relationship to a LineageProcess
     */
    public static LineageProcess refByGuid(String guid) {
        return LineageProcess.builder().guid(guid).build();
    }

    /**
     * Reference to a LineageProcess by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the LineageProcess to reference
     * @return reference to a LineageProcess that can be used for defining a relationship to a LineageProcess
     */
    public static LineageProcess refByQualifiedName(String qualifiedName) {
        return LineageProcess.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to create a process.
     *
     * @param name of the process
     * @param connectorType type of the connector (software / system) that ran the process
     * @param connectionName name of the specific instance of that software / system that ran the process
     * @param connectionQualifiedName unique name of the specific instance of that software / system that ran the process
     * @param inputs sources of data the process reads from
     * @param outputs targets of data the process writes to
     * @return the minimal object necessary to create the process, as a builder
     */
    public static LineageProcessBuilder<?, ?> creator(
            String name,
            AtlanConnectorType connectorType,
            String connectionName,
            String connectionQualifiedName,
            List<Catalog> inputs,
            List<Catalog> outputs) {
        return LineageProcess.builder()
                .qualifiedName(generateQualifiedName(
                        name, connectorType, connectionName, connectionQualifiedName, inputs, outputs, null))
                .name(name)
                .connectorType(connectorType)
                .connectionName(connectionName)
                .connectionQualifiedName(connectionQualifiedName)
                .inputs(inputs)
                .outputs(outputs);
    }

    /**
     * Builds the minimal object necessary to update a LineageProcess.
     *
     * @param qualifiedName of the LineageProcess
     * @param name of the LineageProcess
     * @return the minimal request necessary to update the LineageProcess, as a builder
     */
    public static LineageProcessBuilder<?, ?> updater(String qualifiedName, String name) {
        return LineageProcess.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a LineageProcess, from a potentially
     * more-complete LineageProcess object.
     *
     * @return the minimal object necessary to update the LineageProcess, as a builder
     */
    @Override
    protected LineageProcessBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a LineageProcess by its GUID, complete with all of its relationships.
     *
     * @param guid of the LineageProcess to retrieve
     * @return the requested full LineageProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LineageProcess does not exist or the provided GUID is not a LineageProcess
     */
    public static LineageProcess retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof LineageProcess) {
            return (LineageProcess) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a LineageProcess.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a LineageProcess by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the LineageProcess to retrieve
     * @return the requested full LineageProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LineageProcess does not exist
     */
    public static LineageProcess retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof LineageProcess) {
            return (LineageProcess) entity;
        } else {
            throw new NotFoundException(
                    "No LineageProcess found with qualifiedName: " + qualifiedName,
                    "ATLAN_JAVA_CLIENT-404-003",
                    404,
                    null);
        }
    }

    /**
     * Restore the archived (soft-deleted) LineageProcess to active.
     *
     * @param qualifiedName for the LineageProcess
     * @return true if the LineageProcess is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a LineageProcess.
     *
     * @param qualifiedName of the LineageProcess
     * @param name of the LineageProcess
     * @return the updated LineageProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LineageProcess removeDescription(String qualifiedName, String name) throws AtlanException {
        return (LineageProcess)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a LineageProcess.
     *
     * @param qualifiedName of the LineageProcess
     * @param name of the LineageProcess
     * @return the updated LineageProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LineageProcess removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (LineageProcess) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the owners from a LineageProcess.
     *
     * @param qualifiedName of the LineageProcess
     * @param name of the LineageProcess
     * @return the updated LineageProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LineageProcess removeOwners(String qualifiedName, String name) throws AtlanException {
        return (LineageProcess)
                Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the certificate on a LineageProcess.
     *
     * @param qualifiedName of the LineageProcess
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated LineageProcess, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LineageProcess updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (LineageProcess) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a LineageProcess.
     *
     * @param qualifiedName of the LineageProcess
     * @param name of the LineageProcess
     * @return the updated LineageProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LineageProcess removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (LineageProcess)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a LineageProcess.
     *
     * @param qualifiedName of the LineageProcess
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LineageProcess updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (LineageProcess) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a LineageProcess.
     *
     * @param qualifiedName of the LineageProcess
     * @param name of the LineageProcess
     * @return the updated LineageProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LineageProcess removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (LineageProcess)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a LineageProcess.
     *
     * @param qualifiedName of the LineageProcess
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the LineageProcess
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a LineageProcess.
     *
     * @param qualifiedName of the LineageProcess
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the LineageProcess
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the LineageProcess.
     *
     * @param qualifiedName for the LineageProcess
     * @param name human-readable name of the LineageProcess
     * @param terms the list of terms to replace on the LineageProcess, or null to remove all terms from the LineageProcess
     * @return the LineageProcess that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static LineageProcess replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (LineageProcess) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the LineageProcess, without replacing existing terms linked to the LineageProcess.
     * Note: this operation must make two API calls — one to retrieve the LineageProcess's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the LineageProcess
     * @param terms the list of terms to append to the LineageProcess
     * @return the LineageProcess that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static LineageProcess appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (LineageProcess) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a LineageProcess, without replacing all existing terms linked to the LineageProcess.
     * Note: this operation must make two API calls — one to retrieve the LineageProcess's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the LineageProcess
     * @param terms the list of terms to remove from the LineageProcess, which must be referenced by GUID
     * @return the LineageProcess that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static LineageProcess removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (LineageProcess) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
