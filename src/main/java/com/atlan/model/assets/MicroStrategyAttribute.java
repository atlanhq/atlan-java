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
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a MicroStrategy attribute in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class MicroStrategyAttribute extends Asset
        implements IMicroStrategyAttribute, IMicroStrategy, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MicroStrategyAttribute";

    /** Fixed typeName for MicroStrategyAttributes. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** Attribute form name, description, display format and expression as a JSON string. */
    @Attribute
    String microStrategyAttributeForms;

    /** TBC */
    @Attribute
    Long microStrategyCertifiedAt;

    /** TBC */
    @Attribute
    String microStrategyCertifiedBy;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> microStrategyCubeNames;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> microStrategyCubeQualifiedNames;

    /** Cubes where the attribute is used. */
    @Attribute
    @Singular
    SortedSet<IMicroStrategyCube> microStrategyCubes;

    /** TBC */
    @Attribute
    Boolean microStrategyIsCertified;

    /** TBC */
    @Attribute
    @Singular("putMicroStrategyLocation")
    List<Map<String, String>> microStrategyLocation;

    /** Metrics where the attribute is used. */
    @Attribute
    @Singular
    SortedSet<IMicroStrategyMetric> microStrategyMetrics;

    /** Project containing the attribute. */
    @Attribute
    IMicroStrategyProject microStrategyProject;

    /** TBC */
    @Attribute
    String microStrategyProjectName;

    /** TBC */
    @Attribute
    String microStrategyProjectQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> microStrategyReportNames;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> microStrategyReportQualifiedNames;

    /** Reports where the attribute is used. */
    @Attribute
    @Singular
    SortedSet<IMicroStrategyReport> microStrategyReports;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /**
     * Reference to a MicroStrategyAttribute by GUID.
     *
     * @param guid the GUID of the MicroStrategyAttribute to reference
     * @return reference to a MicroStrategyAttribute that can be used for defining a relationship to a MicroStrategyAttribute
     */
    public static MicroStrategyAttribute refByGuid(String guid) {
        return MicroStrategyAttribute.builder().guid(guid).build();
    }

    /**
     * Reference to a MicroStrategyAttribute by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the MicroStrategyAttribute to reference
     * @return reference to a MicroStrategyAttribute that can be used for defining a relationship to a MicroStrategyAttribute
     */
    public static MicroStrategyAttribute refByQualifiedName(String qualifiedName) {
        return MicroStrategyAttribute.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a MicroStrategyAttribute by its GUID, complete with all of its relationships.
     *
     * @param guid of the MicroStrategyAttribute to retrieve
     * @return the requested full MicroStrategyAttribute, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyAttribute does not exist or the provided GUID is not a MicroStrategyAttribute
     */
    public static MicroStrategyAttribute retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a MicroStrategyAttribute by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the MicroStrategyAttribute to retrieve
     * @return the requested full MicroStrategyAttribute, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyAttribute does not exist or the provided GUID is not a MicroStrategyAttribute
     */
    public static MicroStrategyAttribute retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof MicroStrategyAttribute) {
            return (MicroStrategyAttribute) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "MicroStrategyAttribute");
        }
    }

    /**
     * Retrieves a MicroStrategyAttribute by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the MicroStrategyAttribute to retrieve
     * @return the requested full MicroStrategyAttribute, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyAttribute does not exist
     */
    public static MicroStrategyAttribute retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a MicroStrategyAttribute by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the MicroStrategyAttribute to retrieve
     * @return the requested full MicroStrategyAttribute, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyAttribute does not exist
     */
    public static MicroStrategyAttribute retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof MicroStrategyAttribute) {
            return (MicroStrategyAttribute) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "MicroStrategyAttribute");
        }
    }

    /**
     * Restore the archived (soft-deleted) MicroStrategyAttribute to active.
     *
     * @param qualifiedName for the MicroStrategyAttribute
     * @return true if the MicroStrategyAttribute is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) MicroStrategyAttribute to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the MicroStrategyAttribute
     * @return true if the MicroStrategyAttribute is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a MicroStrategyAttribute.
     *
     * @param qualifiedName of the MicroStrategyAttribute
     * @param name of the MicroStrategyAttribute
     * @return the minimal request necessary to update the MicroStrategyAttribute, as a builder
     */
    public static MicroStrategyAttributeBuilder<?, ?> updater(String qualifiedName, String name) {
        return MicroStrategyAttribute.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a MicroStrategyAttribute, from a potentially
     * more-complete MicroStrategyAttribute object.
     *
     * @return the minimal object necessary to update the MicroStrategyAttribute, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for MicroStrategyAttribute are not found in the initial object
     */
    @Override
    public MicroStrategyAttributeBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "MicroStrategyAttribute", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a MicroStrategyAttribute.
     *
     * @param qualifiedName of the MicroStrategyAttribute
     * @param name of the MicroStrategyAttribute
     * @return the updated MicroStrategyAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyAttribute removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a MicroStrategyAttribute.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MicroStrategyAttribute
     * @param name of the MicroStrategyAttribute
     * @return the updated MicroStrategyAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyAttribute removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyAttribute) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a MicroStrategyAttribute.
     *
     * @param qualifiedName of the MicroStrategyAttribute
     * @param name of the MicroStrategyAttribute
     * @return the updated MicroStrategyAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyAttribute removeUserDescription(String qualifiedName, String name)
            throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a MicroStrategyAttribute.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MicroStrategyAttribute
     * @param name of the MicroStrategyAttribute
     * @return the updated MicroStrategyAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyAttribute removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyAttribute) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a MicroStrategyAttribute.
     *
     * @param qualifiedName of the MicroStrategyAttribute
     * @param name of the MicroStrategyAttribute
     * @return the updated MicroStrategyAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyAttribute removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a MicroStrategyAttribute.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MicroStrategyAttribute's owners
     * @param qualifiedName of the MicroStrategyAttribute
     * @param name of the MicroStrategyAttribute
     * @return the updated MicroStrategyAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyAttribute removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyAttribute) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a MicroStrategyAttribute.
     *
     * @param qualifiedName of the MicroStrategyAttribute
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MicroStrategyAttribute, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyAttribute updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a MicroStrategyAttribute.
     *
     * @param client connectivity to the Atlan tenant on which to update the MicroStrategyAttribute's certificate
     * @param qualifiedName of the MicroStrategyAttribute
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MicroStrategyAttribute, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyAttribute updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (MicroStrategyAttribute)
                Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MicroStrategyAttribute.
     *
     * @param qualifiedName of the MicroStrategyAttribute
     * @param name of the MicroStrategyAttribute
     * @return the updated MicroStrategyAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyAttribute removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a MicroStrategyAttribute.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MicroStrategyAttribute's certificate
     * @param qualifiedName of the MicroStrategyAttribute
     * @param name of the MicroStrategyAttribute
     * @return the updated MicroStrategyAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyAttribute removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyAttribute) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a MicroStrategyAttribute.
     *
     * @param qualifiedName of the MicroStrategyAttribute
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyAttribute updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a MicroStrategyAttribute.
     *
     * @param client connectivity to the Atlan tenant on which to update the MicroStrategyAttribute's announcement
     * @param qualifiedName of the MicroStrategyAttribute
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyAttribute updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (MicroStrategyAttribute)
                Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MicroStrategyAttribute.
     *
     * @param qualifiedName of the MicroStrategyAttribute
     * @param name of the MicroStrategyAttribute
     * @return the updated MicroStrategyAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyAttribute removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a MicroStrategyAttribute.
     *
     * @param client connectivity to the Atlan client from which to remove the MicroStrategyAttribute's announcement
     * @param qualifiedName of the MicroStrategyAttribute
     * @param name of the MicroStrategyAttribute
     * @return the updated MicroStrategyAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyAttribute removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyAttribute) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the MicroStrategyAttribute.
     *
     * @param qualifiedName for the MicroStrategyAttribute
     * @param name human-readable name of the MicroStrategyAttribute
     * @param terms the list of terms to replace on the MicroStrategyAttribute, or null to remove all terms from the MicroStrategyAttribute
     * @return the MicroStrategyAttribute that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyAttribute replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the MicroStrategyAttribute.
     *
     * @param client connectivity to the Atlan tenant on which to replace the MicroStrategyAttribute's assigned terms
     * @param qualifiedName for the MicroStrategyAttribute
     * @param name human-readable name of the MicroStrategyAttribute
     * @param terms the list of terms to replace on the MicroStrategyAttribute, or null to remove all terms from the MicroStrategyAttribute
     * @return the MicroStrategyAttribute that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyAttribute replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (MicroStrategyAttribute) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MicroStrategyAttribute, without replacing existing terms linked to the MicroStrategyAttribute.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyAttribute's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the MicroStrategyAttribute
     * @param terms the list of terms to append to the MicroStrategyAttribute
     * @return the MicroStrategyAttribute that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyAttribute appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the MicroStrategyAttribute, without replacing existing terms linked to the MicroStrategyAttribute.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyAttribute's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the MicroStrategyAttribute
     * @param qualifiedName for the MicroStrategyAttribute
     * @param terms the list of terms to append to the MicroStrategyAttribute
     * @return the MicroStrategyAttribute that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyAttribute appendTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (MicroStrategyAttribute) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MicroStrategyAttribute, without replacing all existing terms linked to the MicroStrategyAttribute.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyAttribute's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the MicroStrategyAttribute
     * @param terms the list of terms to remove from the MicroStrategyAttribute, which must be referenced by GUID
     * @return the MicroStrategyAttribute that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyAttribute removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a MicroStrategyAttribute, without replacing all existing terms linked to the MicroStrategyAttribute.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyAttribute's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the MicroStrategyAttribute
     * @param qualifiedName for the MicroStrategyAttribute
     * @param terms the list of terms to remove from the MicroStrategyAttribute, which must be referenced by GUID
     * @return the MicroStrategyAttribute that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyAttribute removeTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (MicroStrategyAttribute) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a MicroStrategyAttribute, without replacing existing Atlan tags linked to the MicroStrategyAttribute.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyAttribute's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MicroStrategyAttribute
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyAttribute
     */
    public static MicroStrategyAttribute appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyAttribute, without replacing existing Atlan tags linked to the MicroStrategyAttribute.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyAttribute's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MicroStrategyAttribute
     * @param qualifiedName of the MicroStrategyAttribute
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyAttribute
     */
    public static MicroStrategyAttribute appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (MicroStrategyAttribute) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyAttribute, without replacing existing Atlan tags linked to the MicroStrategyAttribute.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyAttribute's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MicroStrategyAttribute
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyAttribute
     */
    public static MicroStrategyAttribute appendAtlanTags(
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
     * Add Atlan tags to a MicroStrategyAttribute, without replacing existing Atlan tags linked to the MicroStrategyAttribute.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyAttribute's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MicroStrategyAttribute
     * @param qualifiedName of the MicroStrategyAttribute
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyAttribute
     */
    public static MicroStrategyAttribute appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (MicroStrategyAttribute) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a MicroStrategyAttribute.
     *
     * @param qualifiedName of the MicroStrategyAttribute
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MicroStrategyAttribute
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyAttribute.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the MicroStrategyAttribute
     * @param qualifiedName of the MicroStrategyAttribute
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MicroStrategyAttribute
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyAttribute.
     *
     * @param qualifiedName of the MicroStrategyAttribute
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MicroStrategyAttribute
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
     * Add Atlan tags to a MicroStrategyAttribute.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the MicroStrategyAttribute
     * @param qualifiedName of the MicroStrategyAttribute
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MicroStrategyAttribute
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
     * Remove an Atlan tag from a MicroStrategyAttribute.
     *
     * @param qualifiedName of the MicroStrategyAttribute
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MicroStrategyAttribute
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a MicroStrategyAttribute.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a MicroStrategyAttribute
     * @param qualifiedName of the MicroStrategyAttribute
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MicroStrategyAttribute
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
