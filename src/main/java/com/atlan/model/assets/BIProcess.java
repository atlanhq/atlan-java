/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
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
 * Atlan BI Process
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class BIProcess extends Asset implements IBIProcess, ILineageProcess, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "BIProcess";

    /** Fixed typeName for BIProcesss. */
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
    @Singular
    SortedSet<ICatalog> inputs;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ICatalog> outputs;

    /** TBC */
    @Attribute
    String sql;

    /**
     * Reference to a BIProcess by GUID.
     *
     * @param guid the GUID of the BIProcess to reference
     * @return reference to a BIProcess that can be used for defining a relationship to a BIProcess
     */
    public static BIProcess refByGuid(String guid) {
        return BIProcess.builder().guid(guid).build();
    }

    /**
     * Reference to a BIProcess by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the BIProcess to reference
     * @return reference to a BIProcess that can be used for defining a relationship to a BIProcess
     */
    public static BIProcess refByQualifiedName(String qualifiedName) {
        return BIProcess.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a BIProcess by its GUID, complete with all of its relationships.
     *
     * @param guid of the BIProcess to retrieve
     * @return the requested full BIProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the BIProcess does not exist or the provided GUID is not a BIProcess
     */
    public static BIProcess retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a BIProcess by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the BIProcess to retrieve
     * @return the requested full BIProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the BIProcess does not exist or the provided GUID is not a BIProcess
     */
    public static BIProcess retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof BIProcess) {
            return (BIProcess) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "BIProcess");
        }
    }

    /**
     * Retrieves a BIProcess by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the BIProcess to retrieve
     * @return the requested full BIProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the BIProcess does not exist
     */
    public static BIProcess retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a BIProcess by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the BIProcess to retrieve
     * @return the requested full BIProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the BIProcess does not exist
     */
    public static BIProcess retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof BIProcess) {
            return (BIProcess) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "BIProcess");
        }
    }

    /**
     * Restore the archived (soft-deleted) BIProcess to active.
     *
     * @param qualifiedName for the BIProcess
     * @return true if the BIProcess is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) BIProcess to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the BIProcess
     * @return true if the BIProcess is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a BIProcess.
     *
     * @param qualifiedName of the BIProcess
     * @param name of the BIProcess
     * @return the minimal request necessary to update the BIProcess, as a builder
     */
    public static BIProcessBuilder<?, ?> updater(String qualifiedName, String name) {
        return BIProcess.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a BIProcess, from a potentially
     * more-complete BIProcess object.
     *
     * @return the minimal object necessary to update the BIProcess, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for BIProcess are not found in the initial object
     */
    @Override
    public BIProcessBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "BIProcess", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a BIProcess.
     *
     * @param qualifiedName of the BIProcess
     * @param name of the BIProcess
     * @return the updated BIProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BIProcess removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a BIProcess.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the BIProcess
     * @param name of the BIProcess
     * @return the updated BIProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BIProcess removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (BIProcess) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a BIProcess.
     *
     * @param qualifiedName of the BIProcess
     * @param name of the BIProcess
     * @return the updated BIProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BIProcess removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a BIProcess.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the BIProcess
     * @param name of the BIProcess
     * @return the updated BIProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BIProcess removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (BIProcess) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a BIProcess.
     *
     * @param qualifiedName of the BIProcess
     * @param name of the BIProcess
     * @return the updated BIProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BIProcess removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a BIProcess.
     *
     * @param client connectivity to the Atlan tenant from which to remove the BIProcess's owners
     * @param qualifiedName of the BIProcess
     * @param name of the BIProcess
     * @return the updated BIProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BIProcess removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (BIProcess) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a BIProcess.
     *
     * @param qualifiedName of the BIProcess
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated BIProcess, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static BIProcess updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a BIProcess.
     *
     * @param client connectivity to the Atlan tenant on which to update the BIProcess's certificate
     * @param qualifiedName of the BIProcess
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated BIProcess, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static BIProcess updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (BIProcess) Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a BIProcess.
     *
     * @param qualifiedName of the BIProcess
     * @param name of the BIProcess
     * @return the updated BIProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BIProcess removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a BIProcess.
     *
     * @param client connectivity to the Atlan tenant from which to remove the BIProcess's certificate
     * @param qualifiedName of the BIProcess
     * @param name of the BIProcess
     * @return the updated BIProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BIProcess removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (BIProcess) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a BIProcess.
     *
     * @param qualifiedName of the BIProcess
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static BIProcess updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a BIProcess.
     *
     * @param client connectivity to the Atlan tenant on which to update the BIProcess's announcement
     * @param qualifiedName of the BIProcess
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static BIProcess updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (BIProcess) Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a BIProcess.
     *
     * @param qualifiedName of the BIProcess
     * @param name of the BIProcess
     * @return the updated BIProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BIProcess removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a BIProcess.
     *
     * @param client connectivity to the Atlan client from which to remove the BIProcess's announcement
     * @param qualifiedName of the BIProcess
     * @param name of the BIProcess
     * @return the updated BIProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BIProcess removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (BIProcess) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the BIProcess.
     *
     * @param qualifiedName for the BIProcess
     * @param name human-readable name of the BIProcess
     * @param terms the list of terms to replace on the BIProcess, or null to remove all terms from the BIProcess
     * @return the BIProcess that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static BIProcess replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the BIProcess.
     *
     * @param client connectivity to the Atlan tenant on which to replace the BIProcess's assigned terms
     * @param qualifiedName for the BIProcess
     * @param name human-readable name of the BIProcess
     * @param terms the list of terms to replace on the BIProcess, or null to remove all terms from the BIProcess
     * @return the BIProcess that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static BIProcess replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (BIProcess) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the BIProcess, without replacing existing terms linked to the BIProcess.
     * Note: this operation must make two API calls — one to retrieve the BIProcess's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the BIProcess
     * @param terms the list of terms to append to the BIProcess
     * @return the BIProcess that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static BIProcess appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the BIProcess, without replacing existing terms linked to the BIProcess.
     * Note: this operation must make two API calls — one to retrieve the BIProcess's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the BIProcess
     * @param qualifiedName for the BIProcess
     * @param terms the list of terms to append to the BIProcess
     * @return the BIProcess that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static BIProcess appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (BIProcess) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a BIProcess, without replacing all existing terms linked to the BIProcess.
     * Note: this operation must make two API calls — one to retrieve the BIProcess's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the BIProcess
     * @param terms the list of terms to remove from the BIProcess, which must be referenced by GUID
     * @return the BIProcess that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static BIProcess removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a BIProcess, without replacing all existing terms linked to the BIProcess.
     * Note: this operation must make two API calls — one to retrieve the BIProcess's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the BIProcess
     * @param qualifiedName for the BIProcess
     * @param terms the list of terms to remove from the BIProcess, which must be referenced by GUID
     * @return the BIProcess that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static BIProcess removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (BIProcess) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a BIProcess, without replacing existing Atlan tags linked to the BIProcess.
     * Note: this operation must make two API calls — one to retrieve the BIProcess's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the BIProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated BIProcess
     */
    public static BIProcess appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a BIProcess, without replacing existing Atlan tags linked to the BIProcess.
     * Note: this operation must make two API calls — one to retrieve the BIProcess's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the BIProcess
     * @param qualifiedName of the BIProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated BIProcess
     */
    public static BIProcess appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (BIProcess) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a BIProcess, without replacing existing Atlan tags linked to the BIProcess.
     * Note: this operation must make two API calls — one to retrieve the BIProcess's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the BIProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated BIProcess
     */
    public static BIProcess appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return appendAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a BIProcess, without replacing existing Atlan tags linked to the BIProcess.
     * Note: this operation must make two API calls — one to retrieve the BIProcess's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the BIProcess
     * @param qualifiedName of the BIProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated BIProcess
     */
    public static BIProcess appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (BIProcess) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a BIProcess.
     *
     * @param qualifiedName of the BIProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the BIProcess
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a BIProcess.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the BIProcess
     * @param qualifiedName of the BIProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the BIProcess
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a BIProcess.
     *
     * @param qualifiedName of the BIProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the BIProcess
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
        addAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a BIProcess.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the BIProcess
     * @param qualifiedName of the BIProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the BIProcess
     * @deprecated see {@link #appendAtlanTags(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    public static void addAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a BIProcess.
     *
     * @param qualifiedName of the BIProcess
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the BIProcess
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a BIProcess.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a BIProcess
     * @param qualifiedName of the BIProcess
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the BIProcess
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
