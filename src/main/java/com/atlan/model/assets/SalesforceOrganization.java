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
 * Instance of a Salesforce organization in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class SalesforceOrganization extends Asset
        implements ISalesforceOrganization, ISalesforce, ISaaS, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SalesforceOrganization";

    /** Fixed typeName for SalesforceOrganizations. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String apiName;

    /** TBC */
    @Attribute
    String organizationQualifiedName;

    /** ID of the organization in Salesforce. */
    @Attribute
    String sourceId;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISalesforceDashboard> dashboards;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISalesforceObject> objects;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISalesforceReport> reports;

    /**
     * Reference to a SalesforceOrganization by GUID.
     *
     * @param guid the GUID of the SalesforceOrganization to reference
     * @return reference to a SalesforceOrganization that can be used for defining a relationship to a SalesforceOrganization
     */
    public static SalesforceOrganization refByGuid(String guid) {
        return SalesforceOrganization.builder().guid(guid).build();
    }

    /**
     * Reference to a SalesforceOrganization by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the SalesforceOrganization to reference
     * @return reference to a SalesforceOrganization that can be used for defining a relationship to a SalesforceOrganization
     */
    public static SalesforceOrganization refByQualifiedName(String qualifiedName) {
        return SalesforceOrganization.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a SalesforceOrganization by its GUID, complete with all of its relationships.
     *
     * @param guid of the SalesforceOrganization to retrieve
     * @return the requested full SalesforceOrganization, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceOrganization does not exist or the provided GUID is not a SalesforceOrganization
     */
    public static SalesforceOrganization retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof SalesforceOrganization) {
            return (SalesforceOrganization) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "SalesforceOrganization");
        }
    }

    /**
     * Retrieves a SalesforceOrganization by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the SalesforceOrganization to retrieve
     * @return the requested full SalesforceOrganization, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceOrganization does not exist
     */
    public static SalesforceOrganization retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof SalesforceOrganization) {
            return (SalesforceOrganization) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "SalesforceOrganization");
        }
    }

    /**
     * Restore the archived (soft-deleted) SalesforceOrganization to active.
     *
     * @param qualifiedName for the SalesforceOrganization
     * @return true if the SalesforceOrganization is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SalesforceOrganization.
     *
     * @param qualifiedName of the SalesforceOrganization
     * @param name of the SalesforceOrganization
     * @return the minimal request necessary to update the SalesforceOrganization, as a builder
     */
    public static SalesforceOrganizationBuilder<?, ?> updater(String qualifiedName, String name) {
        return SalesforceOrganization.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SalesforceOrganization, from a potentially
     * more-complete SalesforceOrganization object.
     *
     * @return the minimal object necessary to update the SalesforceOrganization, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SalesforceOrganization are not found in the initial object
     */
    @Override
    public SalesforceOrganizationBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "SalesforceOrganization", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a SalesforceOrganization.
     *
     * @param qualifiedName of the SalesforceOrganization
     * @param name of the SalesforceOrganization
     * @return the updated SalesforceOrganization, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceOrganization removeDescription(String qualifiedName, String name) throws AtlanException {
        return (SalesforceOrganization) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SalesforceOrganization.
     *
     * @param qualifiedName of the SalesforceOrganization
     * @param name of the SalesforceOrganization
     * @return the updated SalesforceOrganization, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceOrganization removeUserDescription(String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceOrganization) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SalesforceOrganization.
     *
     * @param qualifiedName of the SalesforceOrganization
     * @param name of the SalesforceOrganization
     * @return the updated SalesforceOrganization, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceOrganization removeOwners(String qualifiedName, String name) throws AtlanException {
        return (SalesforceOrganization) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SalesforceOrganization.
     *
     * @param qualifiedName of the SalesforceOrganization
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SalesforceOrganization, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceOrganization updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return (SalesforceOrganization)
                Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SalesforceOrganization.
     *
     * @param qualifiedName of the SalesforceOrganization
     * @param name of the SalesforceOrganization
     * @return the updated SalesforceOrganization, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceOrganization removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (SalesforceOrganization) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SalesforceOrganization.
     *
     * @param qualifiedName of the SalesforceOrganization
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceOrganization updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (SalesforceOrganization)
                Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SalesforceOrganization.
     *
     * @param qualifiedName of the SalesforceOrganization
     * @param name of the SalesforceOrganization
     * @return the updated SalesforceOrganization, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceOrganization removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (SalesforceOrganization) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SalesforceOrganization.
     *
     * @param qualifiedName for the SalesforceOrganization
     * @param name human-readable name of the SalesforceOrganization
     * @param terms the list of terms to replace on the SalesforceOrganization, or null to remove all terms from the SalesforceOrganization
     * @return the SalesforceOrganization that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceOrganization replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SalesforceOrganization) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SalesforceOrganization, without replacing existing terms linked to the SalesforceOrganization.
     * Note: this operation must make two API calls — one to retrieve the SalesforceOrganization's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the SalesforceOrganization
     * @param terms the list of terms to append to the SalesforceOrganization
     * @return the SalesforceOrganization that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceOrganization appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SalesforceOrganization) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SalesforceOrganization, without replacing all existing terms linked to the SalesforceOrganization.
     * Note: this operation must make two API calls — one to retrieve the SalesforceOrganization's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the SalesforceOrganization
     * @param terms the list of terms to remove from the SalesforceOrganization, which must be referenced by GUID
     * @return the SalesforceOrganization that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceOrganization removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SalesforceOrganization) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SalesforceOrganization, without replacing existing Atlan tags linked to the SalesforceOrganization.
     * Note: this operation must make two API calls — one to retrieve the SalesforceOrganization's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SalesforceOrganization
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SalesforceOrganization
     */
    public static SalesforceOrganization appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SalesforceOrganization) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SalesforceOrganization, without replacing existing Atlan tags linked to the SalesforceOrganization.
     * Note: this operation must make two API calls — one to retrieve the SalesforceOrganization's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SalesforceOrganization
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SalesforceOrganization
     */
    public static SalesforceOrganization appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SalesforceOrganization) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a SalesforceOrganization.
     *
     * @param qualifiedName of the SalesforceOrganization
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SalesforceOrganization
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SalesforceOrganization.
     *
     * @param qualifiedName of the SalesforceOrganization
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SalesforceOrganization
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
     * Remove an Atlan tag from a SalesforceOrganization.
     *
     * @param qualifiedName of the SalesforceOrganization
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SalesforceOrganization
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
