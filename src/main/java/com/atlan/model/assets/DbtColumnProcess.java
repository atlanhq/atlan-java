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
 * Instance of a column-level dbt process in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class DbtColumnProcess extends Asset
        implements IDbtColumnProcess, IDbt, IColumnProcess, ICatalog, IAsset, IReferenceable, ILineageProcess {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DbtColumnProcess";

    /** Fixed typeName for DbtColumnProcesss. */
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
    String dbtColumnProcessJobStatus;

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
    ILineageProcess process;

    /** TBC */
    @Attribute
    String sql;

    /**
     * Reference to a DbtColumnProcess by GUID.
     *
     * @param guid the GUID of the DbtColumnProcess to reference
     * @return reference to a DbtColumnProcess that can be used for defining a relationship to a DbtColumnProcess
     */
    public static DbtColumnProcess refByGuid(String guid) {
        return DbtColumnProcess.builder().guid(guid).build();
    }

    /**
     * Reference to a DbtColumnProcess by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the DbtColumnProcess to reference
     * @return reference to a DbtColumnProcess that can be used for defining a relationship to a DbtColumnProcess
     */
    public static DbtColumnProcess refByQualifiedName(String qualifiedName) {
        return DbtColumnProcess.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a DbtColumnProcess by its GUID, complete with all of its relationships.
     *
     * @param guid of the DbtColumnProcess to retrieve
     * @return the requested full DbtColumnProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtColumnProcess does not exist or the provided GUID is not a DbtColumnProcess
     */
    public static DbtColumnProcess retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof DbtColumnProcess) {
            return (DbtColumnProcess) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "DbtColumnProcess");
        }
    }

    /**
     * Retrieves a DbtColumnProcess by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the DbtColumnProcess to retrieve
     * @return the requested full DbtColumnProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtColumnProcess does not exist
     */
    public static DbtColumnProcess retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof DbtColumnProcess) {
            return (DbtColumnProcess) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "DbtColumnProcess");
        }
    }

    /**
     * Restore the archived (soft-deleted) DbtColumnProcess to active.
     *
     * @param qualifiedName for the DbtColumnProcess
     * @return true if the DbtColumnProcess is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a DbtColumnProcess.
     *
     * @param qualifiedName of the DbtColumnProcess
     * @param name of the DbtColumnProcess
     * @return the minimal request necessary to update the DbtColumnProcess, as a builder
     */
    public static DbtColumnProcessBuilder<?, ?> updater(String qualifiedName, String name) {
        return DbtColumnProcess.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DbtColumnProcess, from a potentially
     * more-complete DbtColumnProcess object.
     *
     * @return the minimal object necessary to update the DbtColumnProcess, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DbtColumnProcess are not found in the initial object
     */
    @Override
    public DbtColumnProcessBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "DbtColumnProcess", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a DbtColumnProcess.
     *
     * @param qualifiedName of the DbtColumnProcess
     * @param name of the DbtColumnProcess
     * @return the updated DbtColumnProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtColumnProcess removeDescription(String qualifiedName, String name) throws AtlanException {
        return (DbtColumnProcess) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DbtColumnProcess.
     *
     * @param qualifiedName of the DbtColumnProcess
     * @param name of the DbtColumnProcess
     * @return the updated DbtColumnProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtColumnProcess removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (DbtColumnProcess) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DbtColumnProcess.
     *
     * @param qualifiedName of the DbtColumnProcess
     * @param name of the DbtColumnProcess
     * @return the updated DbtColumnProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtColumnProcess removeOwners(String qualifiedName, String name) throws AtlanException {
        return (DbtColumnProcess) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DbtColumnProcess.
     *
     * @param qualifiedName of the DbtColumnProcess
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DbtColumnProcess, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtColumnProcess updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return (DbtColumnProcess) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DbtColumnProcess.
     *
     * @param qualifiedName of the DbtColumnProcess
     * @param name of the DbtColumnProcess
     * @return the updated DbtColumnProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtColumnProcess removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (DbtColumnProcess) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DbtColumnProcess.
     *
     * @param qualifiedName of the DbtColumnProcess
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtColumnProcess updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (DbtColumnProcess) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DbtColumnProcess.
     *
     * @param qualifiedName of the DbtColumnProcess
     * @param name of the DbtColumnProcess
     * @return the updated DbtColumnProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtColumnProcess removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (DbtColumnProcess) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DbtColumnProcess.
     *
     * @param qualifiedName for the DbtColumnProcess
     * @param name human-readable name of the DbtColumnProcess
     * @param terms the list of terms to replace on the DbtColumnProcess, or null to remove all terms from the DbtColumnProcess
     * @return the DbtColumnProcess that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DbtColumnProcess replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtColumnProcess) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DbtColumnProcess, without replacing existing terms linked to the DbtColumnProcess.
     * Note: this operation must make two API calls — one to retrieve the DbtColumnProcess's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the DbtColumnProcess
     * @param terms the list of terms to append to the DbtColumnProcess
     * @return the DbtColumnProcess that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DbtColumnProcess appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (DbtColumnProcess) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DbtColumnProcess, without replacing all existing terms linked to the DbtColumnProcess.
     * Note: this operation must make two API calls — one to retrieve the DbtColumnProcess's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the DbtColumnProcess
     * @param terms the list of terms to remove from the DbtColumnProcess, which must be referenced by GUID
     * @return the DbtColumnProcess that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DbtColumnProcess removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (DbtColumnProcess) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DbtColumnProcess, without replacing existing Atlan tags linked to the DbtColumnProcess.
     * Note: this operation must make two API calls — one to retrieve the DbtColumnProcess's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DbtColumnProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DbtColumnProcess
     */
    public static DbtColumnProcess appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (DbtColumnProcess) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtColumnProcess, without replacing existing Atlan tags linked to the DbtColumnProcess.
     * Note: this operation must make two API calls — one to retrieve the DbtColumnProcess's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DbtColumnProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DbtColumnProcess
     */
    public static DbtColumnProcess appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DbtColumnProcess) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a DbtColumnProcess.
     *
     * @param qualifiedName of the DbtColumnProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtColumnProcess
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtColumnProcess.
     *
     * @param qualifiedName of the DbtColumnProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtColumnProcess
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
     * Remove an Atlan tag from a DbtColumnProcess.
     *
     * @param qualifiedName of the DbtColumnProcess
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DbtColumnProcess
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
