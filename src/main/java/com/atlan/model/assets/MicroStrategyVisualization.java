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
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a MicroStrategy visualization in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class MicroStrategyVisualization extends Asset
        implements IMicroStrategyVisualization, IMicroStrategy, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MicroStrategyVisualization";

    /** Fixed typeName for MicroStrategyVisualizations. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

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

    /** Dossier containing the visualization. */
    @Attribute
    IMicroStrategyDossier microStrategyDossier;

    /** Simple name of the dossier containing this visualization. */
    @Attribute
    String microStrategyDossierName;

    /** Unique name of the dossier containing this visualization. */
    @Attribute
    String microStrategyDossierQualifiedName;

    /** TBC */
    @Attribute
    Boolean microStrategyIsCertified;

    /** TBC */
    @Attribute
    @Singular("putMicroStrategyLocation")
    List<Map<String, String>> microStrategyLocation;

    /** Project containing the visualization. */
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

    /** Type of visualization. */
    @Attribute
    String microStrategyVisualizationType;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /**
     * Reference to a MicroStrategyVisualization by GUID.
     *
     * @param guid the GUID of the MicroStrategyVisualization to reference
     * @return reference to a MicroStrategyVisualization that can be used for defining a relationship to a MicroStrategyVisualization
     */
    public static MicroStrategyVisualization refByGuid(String guid) {
        return MicroStrategyVisualization.builder().guid(guid).build();
    }

    /**
     * Reference to a MicroStrategyVisualization by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the MicroStrategyVisualization to reference
     * @return reference to a MicroStrategyVisualization that can be used for defining a relationship to a MicroStrategyVisualization
     */
    public static MicroStrategyVisualization refByQualifiedName(String qualifiedName) {
        return MicroStrategyVisualization.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a MicroStrategyVisualization by its GUID, complete with all of its relationships.
     *
     * @param guid of the MicroStrategyVisualization to retrieve
     * @return the requested full MicroStrategyVisualization, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyVisualization does not exist or the provided GUID is not a MicroStrategyVisualization
     */
    public static MicroStrategyVisualization retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof MicroStrategyVisualization) {
            return (MicroStrategyVisualization) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "MicroStrategyVisualization");
        }
    }

    /**
     * Retrieves a MicroStrategyVisualization by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the MicroStrategyVisualization to retrieve
     * @return the requested full MicroStrategyVisualization, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyVisualization does not exist
     */
    public static MicroStrategyVisualization retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof MicroStrategyVisualization) {
            return (MicroStrategyVisualization) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "MicroStrategyVisualization");
        }
    }

    /**
     * Restore the archived (soft-deleted) MicroStrategyVisualization to active.
     *
     * @param qualifiedName for the MicroStrategyVisualization
     * @return true if the MicroStrategyVisualization is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a MicroStrategyVisualization.
     *
     * @param qualifiedName of the MicroStrategyVisualization
     * @param name of the MicroStrategyVisualization
     * @return the minimal request necessary to update the MicroStrategyVisualization, as a builder
     */
    public static MicroStrategyVisualizationBuilder<?, ?> updater(String qualifiedName, String name) {
        return MicroStrategyVisualization.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a MicroStrategyVisualization, from a potentially
     * more-complete MicroStrategyVisualization object.
     *
     * @return the minimal object necessary to update the MicroStrategyVisualization, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for MicroStrategyVisualization are not found in the initial object
     */
    @Override
    public MicroStrategyVisualizationBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "MicroStrategyVisualization", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a MicroStrategyVisualization.
     *
     * @param qualifiedName of the MicroStrategyVisualization
     * @param name of the MicroStrategyVisualization
     * @return the updated MicroStrategyVisualization, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyVisualization removeDescription(String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyVisualization) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a MicroStrategyVisualization.
     *
     * @param qualifiedName of the MicroStrategyVisualization
     * @param name of the MicroStrategyVisualization
     * @return the updated MicroStrategyVisualization, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyVisualization removeUserDescription(String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyVisualization) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a MicroStrategyVisualization.
     *
     * @param qualifiedName of the MicroStrategyVisualization
     * @param name of the MicroStrategyVisualization
     * @return the updated MicroStrategyVisualization, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyVisualization removeOwners(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyVisualization) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a MicroStrategyVisualization.
     *
     * @param qualifiedName of the MicroStrategyVisualization
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MicroStrategyVisualization, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyVisualization updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return (MicroStrategyVisualization)
                Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MicroStrategyVisualization.
     *
     * @param qualifiedName of the MicroStrategyVisualization
     * @param name of the MicroStrategyVisualization
     * @return the updated MicroStrategyVisualization, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyVisualization removeCertificate(String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyVisualization) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a MicroStrategyVisualization.
     *
     * @param qualifiedName of the MicroStrategyVisualization
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyVisualization updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (MicroStrategyVisualization)
                Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MicroStrategyVisualization.
     *
     * @param qualifiedName of the MicroStrategyVisualization
     * @param name of the MicroStrategyVisualization
     * @return the updated MicroStrategyVisualization, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyVisualization removeAnnouncement(String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyVisualization) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the MicroStrategyVisualization.
     *
     * @param qualifiedName for the MicroStrategyVisualization
     * @param name human-readable name of the MicroStrategyVisualization
     * @param terms the list of terms to replace on the MicroStrategyVisualization, or null to remove all terms from the MicroStrategyVisualization
     * @return the MicroStrategyVisualization that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyVisualization replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyVisualization) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MicroStrategyVisualization, without replacing existing terms linked to the MicroStrategyVisualization.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyVisualization's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the MicroStrategyVisualization
     * @param terms the list of terms to append to the MicroStrategyVisualization
     * @return the MicroStrategyVisualization that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyVisualization appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyVisualization) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MicroStrategyVisualization, without replacing all existing terms linked to the MicroStrategyVisualization.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyVisualization's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the MicroStrategyVisualization
     * @param terms the list of terms to remove from the MicroStrategyVisualization, which must be referenced by GUID
     * @return the MicroStrategyVisualization that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyVisualization removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyVisualization) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a MicroStrategyVisualization, without replacing existing Atlan tags linked to the MicroStrategyVisualization.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyVisualization's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MicroStrategyVisualization
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyVisualization
     */
    public static MicroStrategyVisualization appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (MicroStrategyVisualization) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyVisualization, without replacing existing Atlan tags linked to the MicroStrategyVisualization.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyVisualization's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MicroStrategyVisualization
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyVisualization
     */
    public static MicroStrategyVisualization appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (MicroStrategyVisualization) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a MicroStrategyVisualization.
     *
     * @param qualifiedName of the MicroStrategyVisualization
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MicroStrategyVisualization
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyVisualization.
     *
     * @param qualifiedName of the MicroStrategyVisualization
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MicroStrategyVisualization
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
     * Remove an Atlan tag from a MicroStrategyVisualization.
     *
     * @param qualifiedName of the MicroStrategyVisualization
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MicroStrategyVisualization
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
