/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.*;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a lineage process in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class LineageProcess extends AbstractProcess {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Process";

    /** Fixed typeName for LineageProcesss. */
    @Getter(onMethod_ = {@Override})
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
     * @param name of the process to use for display purposes
     * @param connectionQualifiedName unique name of the specific instance of that software / system that ran the process
     * @param id (optional) unique ID of this process within the software / system that ran it (if not provided, it will be generated)
     * @param inputs sources of data the process reads from
     * @param outputs targets of data the process writes to
     * @param parent (optional) parent process in which this sub-process ran
     * @return the minimal object necessary to create the process, as a builder
     */
    public static LineageProcessBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String id,
            List<Catalog> inputs,
            List<Catalog> outputs,
            LineageProcess parent) {
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return LineageProcess.builder()
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName, id, inputs, outputs, parent))
                .name(name)
                .connectorType(connectorType)
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
     * @throws InvalidRequestException if any of the minimal set of required properties for LineageProcess are not found in the initial object
     */
    @Override
    public LineageProcessBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "LineageProcess", String.join(",", missing));
        }
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
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof LineageProcess) {
            return (LineageProcess) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "LineageProcess");
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
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof LineageProcess) {
            return (LineageProcess) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "LineageProcess");
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
        return (LineageProcess) Asset.removeDescription(updater(qualifiedName, name));
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
        return (LineageProcess) Asset.removeUserDescription(updater(qualifiedName, name));
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
        return (LineageProcess) Asset.removeOwners(updater(qualifiedName, name));
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
    public static LineageProcess updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
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
        return (LineageProcess) Asset.removeCertificate(updater(qualifiedName, name));
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
        return (LineageProcess) Asset.removeAnnouncement(updater(qualifiedName, name));
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
     * Add classifications to a LineageProcess.
     *
     * @param qualifiedName of the LineageProcess
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the LineageProcess
     */
    public static void addClassifications(
            String qualifiedName,
            List<String> classificationNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addClassifications(
                TYPE_NAME,
                qualifiedName,
                classificationNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
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
