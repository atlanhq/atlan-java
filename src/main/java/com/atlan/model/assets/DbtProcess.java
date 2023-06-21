/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a lineage process for dbt in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class DbtProcess extends Asset implements IDbtProcess, IDbt, ILineageProcess, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DbtProcess";

    /** Fixed typeName for DbtProcesss. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String ast;

    /** TBC */
    @Attribute
    String code;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IColumnProcess> columnProcesses;

    /** TBC */
    @Attribute
    String dbtAccountName;

    /** TBC */
    @Attribute
    String dbtAlias;

    /** TBC */
    @Attribute
    String dbtConnectionContext;

    /** TBC */
    @Attribute
    String dbtEnvironmentDbtVersion;

    /** TBC */
    @Attribute
    String dbtEnvironmentName;

    /** TBC */
    @Attribute
    Long dbtJobLastRun;

    /** TBC */
    @Attribute
    String dbtJobName;

    /** TBC */
    @Attribute
    Long dbtJobNextRun;

    /** TBC */
    @Attribute
    String dbtJobNextRunHumanized;

    /** TBC */
    @Attribute
    String dbtJobSchedule;

    /** TBC */
    @Attribute
    String dbtJobScheduleCronHumanized;

    /** TBC */
    @Attribute
    String dbtJobStatus;

    /** TBC */
    @Attribute
    String dbtMeta;

    /** TBC */
    @Attribute
    String dbtPackageName;

    /** TBC */
    @Attribute
    String dbtProcessJobStatus;

    /** TBC */
    @Attribute
    String dbtProjectName;

    /** TBC */
    @Attribute
    String dbtSemanticLayerProxyUrl;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> dbtTags;

    /** TBC */
    @Attribute
    String dbtUniqueId;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ICatalog> inputs;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ICatalog> outputs;

    /** TBC */
    @Attribute
    String sql;

    /**
     * Reference to a DbtProcess by GUID.
     *
     * @param guid the GUID of the DbtProcess to reference
     * @return reference to a DbtProcess that can be used for defining a relationship to a DbtProcess
     */
    public static DbtProcess refByGuid(String guid) {
        return DbtProcess.builder().guid(guid).build();
    }

    /**
     * Reference to a DbtProcess by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the DbtProcess to reference
     * @return reference to a DbtProcess that can be used for defining a relationship to a DbtProcess
     */
    public static DbtProcess refByQualifiedName(String qualifiedName) {
        return DbtProcess.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a DbtProcess by its GUID, complete with all of its relationships.
     *
     * @param guid of the DbtProcess to retrieve
     * @return the requested full DbtProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtProcess does not exist or the provided GUID is not a DbtProcess
     */
    public static DbtProcess retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof DbtProcess) {
            return (DbtProcess) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "DbtProcess");
        }
    }

    /**
     * Retrieves a DbtProcess by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the DbtProcess to retrieve
     * @return the requested full DbtProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtProcess does not exist
     */
    public static DbtProcess retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof DbtProcess) {
            return (DbtProcess) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "DbtProcess");
        }
    }

    /**
     * Restore the archived (soft-deleted) DbtProcess to active.
     *
     * @param qualifiedName for the DbtProcess
     * @return true if the DbtProcess is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a DbtProcess.
     *
     * @param qualifiedName of the DbtProcess
     * @param name of the DbtProcess
     * @return the minimal request necessary to update the DbtProcess, as a builder
     */
    public static DbtProcessBuilder<?, ?> updater(String qualifiedName, String name) {
        return DbtProcess.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DbtProcess, from a potentially
     * more-complete DbtProcess object.
     *
     * @return the minimal object necessary to update the DbtProcess, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DbtProcess are not found in the initial object
     */
    @Override
    public DbtProcessBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "DbtProcess", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a DbtProcess.
     *
     * @param qualifiedName of the DbtProcess
     * @param name of the DbtProcess
     * @return the updated DbtProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtProcess removeDescription(String qualifiedName, String name) throws AtlanException {
        return (DbtProcess) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DbtProcess.
     *
     * @param qualifiedName of the DbtProcess
     * @param name of the DbtProcess
     * @return the updated DbtProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtProcess removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (DbtProcess) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DbtProcess.
     *
     * @param qualifiedName of the DbtProcess
     * @param name of the DbtProcess
     * @return the updated DbtProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtProcess removeOwners(String qualifiedName, String name) throws AtlanException {
        return (DbtProcess) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DbtProcess.
     *
     * @param qualifiedName of the DbtProcess
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DbtProcess, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtProcess updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DbtProcess) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DbtProcess.
     *
     * @param qualifiedName of the DbtProcess
     * @param name of the DbtProcess
     * @return the updated DbtProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtProcess removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (DbtProcess) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DbtProcess.
     *
     * @param qualifiedName of the DbtProcess
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtProcess updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (DbtProcess) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DbtProcess.
     *
     * @param qualifiedName of the DbtProcess
     * @param name of the DbtProcess
     * @return the updated DbtProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtProcess removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (DbtProcess) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DbtProcess.
     *
     * @param qualifiedName for the DbtProcess
     * @param name human-readable name of the DbtProcess
     * @param terms the list of terms to replace on the DbtProcess, or null to remove all terms from the DbtProcess
     * @return the DbtProcess that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DbtProcess replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtProcess) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DbtProcess, without replacing existing terms linked to the DbtProcess.
     * Note: this operation must make two API calls — one to retrieve the DbtProcess's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the DbtProcess
     * @param terms the list of terms to append to the DbtProcess
     * @return the DbtProcess that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DbtProcess appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (DbtProcess) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DbtProcess, without replacing all existing terms linked to the DbtProcess.
     * Note: this operation must make two API calls — one to retrieve the DbtProcess's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the DbtProcess
     * @param terms the list of terms to remove from the DbtProcess, which must be referenced by GUID
     * @return the DbtProcess that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DbtProcess removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (DbtProcess) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DbtProcess, without replacing existing Atlan tags linked to the DbtProcess.
     * Note: this operation must make two API calls — one to retrieve the DbtProcess's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DbtProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DbtProcess
     */
    public static DbtProcess appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (DbtProcess) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtProcess, without replacing existing Atlan tags linked to the DbtProcess.
     * Note: this operation must make two API calls — one to retrieve the DbtProcess's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DbtProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DbtProcess
     */
    public static DbtProcess appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DbtProcess) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a DbtProcess.
     *
     * @param qualifiedName of the DbtProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtProcess
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtProcess.
     *
     * @param qualifiedName of the DbtProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtProcess
     * @deprecated see {@link #appendAtlanTags(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    public static void addAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a DbtProcess.
     *
     * @param qualifiedName of the DbtProcess
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DbtProcess
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
