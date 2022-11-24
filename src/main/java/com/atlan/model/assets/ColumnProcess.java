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

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class ColumnProcess extends AbstractColumnProcess {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ColumnProcess";

    /** Fixed typeName for ColumnProcesss. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /**
     * Reference to a ColumnProcess by GUID.
     *
     * @param guid the GUID of the ColumnProcess to reference
     * @return reference to a ColumnProcess that can be used for defining a relationship to a ColumnProcess
     */
    public static ColumnProcess refByGuid(String guid) {
        return ColumnProcess.builder().guid(guid).build();
    }

    /**
     * Reference to a ColumnProcess by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the ColumnProcess to reference
     * @return reference to a ColumnProcess that can be used for defining a relationship to a ColumnProcess
     */
    public static ColumnProcess refByQualifiedName(String qualifiedName) {
        return ColumnProcess.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to create a column-level process.
     *
     * @param name of the column-level process
     * @param connectorType type of the connector (software / system) that ran the process
     * @param connectionName name of the specific instance of that software / system that ran the process
     * @param connectionQualifiedName unique name of the specific instance of that software / system that ran the process
     * @param inputs columns of data the process reads from
     * @param outputs columns of data the process writes to
     * @return the minimal object necessary to create the column-level process, as a builder
     */
    public static ColumnProcessBuilder<?, ?> creator(
            String name,
            AtlanConnectorType connectorType,
            String connectionName,
            String connectionQualifiedName,
            List<Catalog> inputs,
            List<Catalog> outputs) {
        return ColumnProcess.builder()
                .qualifiedName(generateQualifiedName(
                        name, connectorType, connectionName, connectionQualifiedName, inputs, outputs, null))
                .name(name)
                .inputs(inputs)
                .outputs(outputs);
    }

    /**
     * Builds the minimal object necessary to update a ColumnProcess.
     *
     * @param qualifiedName of the ColumnProcess
     * @param name of the ColumnProcess
     * @return the minimal request necessary to update the ColumnProcess, as a builder
     */
    public static ColumnProcessBuilder<?, ?> updater(String qualifiedName, String name) {
        return ColumnProcess.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ColumnProcess, from a potentially
     * more-complete ColumnProcess object.
     *
     * @return the minimal object necessary to update the ColumnProcess, as a builder
     */
    @Override
    protected ColumnProcessBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a ColumnProcess by its GUID, complete with all of its relationships.
     *
     * @param guid of the ColumnProcess to retrieve
     * @return the requested full ColumnProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ColumnProcess does not exist or the provided GUID is not a ColumnProcess
     */
    public static ColumnProcess retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof ColumnProcess) {
            return (ColumnProcess) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a ColumnProcess.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a ColumnProcess by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the ColumnProcess to retrieve
     * @return the requested full ColumnProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ColumnProcess does not exist
     */
    public static ColumnProcess retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof ColumnProcess) {
            return (ColumnProcess) entity;
        } else {
            throw new NotFoundException(
                    "No ColumnProcess found with qualifiedName: " + qualifiedName,
                    "ATLAN_JAVA_CLIENT-404-003",
                    404,
                    null);
        }
    }

    /**
     * Restore the archived (soft-deleted) ColumnProcess to active.
     *
     * @param qualifiedName for the ColumnProcess
     * @return the ColumnProcess that was restored
     * @throws AtlanException on any API problems
     */
    public static ColumnProcess restore(String qualifiedName) throws AtlanException {
        return (ColumnProcess) Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a ColumnProcess.
     *
     * @param qualifiedName of the ColumnProcess
     * @param name of the ColumnProcess
     * @return the updated ColumnProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ColumnProcess removeDescription(String qualifiedName, String name) throws AtlanException {
        return (ColumnProcess)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a ColumnProcess.
     *
     * @param qualifiedName of the ColumnProcess
     * @param name of the ColumnProcess
     * @return the updated ColumnProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ColumnProcess removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (ColumnProcess) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the owners from a ColumnProcess.
     *
     * @param qualifiedName of the ColumnProcess
     * @param name of the ColumnProcess
     * @return the updated ColumnProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ColumnProcess removeOwners(String qualifiedName, String name) throws AtlanException {
        return (ColumnProcess)
                Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the certificate on a ColumnProcess.
     *
     * @param qualifiedName of the ColumnProcess
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ColumnProcess, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ColumnProcess updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (ColumnProcess) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ColumnProcess.
     *
     * @param qualifiedName of the ColumnProcess
     * @param name of the ColumnProcess
     * @return the updated ColumnProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ColumnProcess removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (ColumnProcess)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a ColumnProcess.
     *
     * @param qualifiedName of the ColumnProcess
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ColumnProcess updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (ColumnProcess) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ColumnProcess.
     *
     * @param qualifiedName of the ColumnProcess
     * @param name of the ColumnProcess
     * @return the updated ColumnProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ColumnProcess removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (ColumnProcess)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a ColumnProcess.
     *
     * @param qualifiedName of the ColumnProcess
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the ColumnProcess
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a ColumnProcess.
     *
     * @param qualifiedName of the ColumnProcess
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the ColumnProcess
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the ColumnProcess.
     *
     * @param qualifiedName for the ColumnProcess
     * @param name human-readable name of the ColumnProcess
     * @param terms the list of terms to replace on the ColumnProcess, or null to remove all terms from the ColumnProcess
     * @return the ColumnProcess that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ColumnProcess replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (ColumnProcess) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ColumnProcess, without replacing existing terms linked to the ColumnProcess.
     * Note: this operation must make two API calls — one to retrieve the ColumnProcess's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the ColumnProcess
     * @param terms the list of terms to append to the ColumnProcess
     * @return the ColumnProcess that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ColumnProcess appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (ColumnProcess) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ColumnProcess, without replacing all existing terms linked to the ColumnProcess.
     * Note: this operation must make two API calls — one to retrieve the ColumnProcess's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the ColumnProcess
     * @param terms the list of terms to remove from the ColumnProcess, which must be referenced by GUID
     * @return the ColumnProcess that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ColumnProcess removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (ColumnProcess) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
