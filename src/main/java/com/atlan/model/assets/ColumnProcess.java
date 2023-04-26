/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a column-level lineage process in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({})
@Slf4j
public class ColumnProcess extends LineageProcess {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ColumnProcess";

    /** Fixed typeName for ColumnProcesss. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Parent process that contains this column-level process. */
    @Attribute
    LineageProcess process;

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
     * Retrieves a ColumnProcess by its GUID, complete with all of its relationships.
     *
     * @param guid of the ColumnProcess to retrieve
     * @return the requested full ColumnProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ColumnProcess does not exist or the provided GUID is not a ColumnProcess
     */
    public static ColumnProcess retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof ColumnProcess) {
            return (ColumnProcess) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "ColumnProcess");
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
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof ColumnProcess) {
            return (ColumnProcess) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "ColumnProcess");
        }
    }

    /**
     * Restore the archived (soft-deleted) ColumnProcess to active.
     *
     * @param qualifiedName for the ColumnProcess
     * @return true if the ColumnProcess is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a column-level process.
     *
     * @param name of the column-level process
     * @param connectionQualifiedName unique name of the specific instance of that software / system that ran the process
     * @param id (optional) unique ID of this process within the software / system that ran it (if not provided, it will be generated)
     * @param inputs columns of data the process reads from
     * @param outputs columns of data the process writes to
     * @param parent (optional) parent process in which this column-level process ran
     * @return the minimal object necessary to create the column-level process, as a builder
     */
    public static ColumnProcessBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String id,
            List<Catalog> inputs,
            List<Catalog> outputs,
            LineageProcess parent) {
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        String connectionName = StringUtils.getNameFromQualifiedName(connectionQualifiedName);
        return ColumnProcess.builder()
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName, id, inputs, outputs, parent))
                .name(name)
                .connectorType(connectorType)
                .connectionName(connectionName)
                .connectionQualifiedName(connectionQualifiedName)
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
     * @throws InvalidRequestException if any of the minimal set of required properties for ColumnProcess are not found in the initial object
     */
    @Override
    public ColumnProcessBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "ColumnProcess", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
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
        return (ColumnProcess) Asset.removeDescription(updater(qualifiedName, name));
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
        return (ColumnProcess) Asset.removeUserDescription(updater(qualifiedName, name));
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
        return (ColumnProcess) Asset.removeOwners(updater(qualifiedName, name));
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
    public static ColumnProcess updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
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
        return (ColumnProcess) Asset.removeCertificate(updater(qualifiedName, name));
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
        return (ColumnProcess) Asset.removeAnnouncement(updater(qualifiedName, name));
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
     * Add classifications to a ColumnProcess.
     *
     * @param qualifiedName of the ColumnProcess
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the ColumnProcess
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
     * Remove a classification from a ColumnProcess.
     *
     * @param qualifiedName of the ColumnProcess
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the ColumnProcess
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }
}
