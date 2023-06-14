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
 * Instance of a MicroStrategy project in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class MicroStrategyProject extends MicroStrategy {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MicroStrategyProject";

    /** Fixed typeName for MicroStrategyProjects. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Reports contained within the project. */
    @Attribute
    @Singular
    SortedSet<MicroStrategyReport> microStrategyReports;

    /** Facts contained within the project. */
    @Attribute
    @Singular
    SortedSet<MicroStrategyFact> microStrategyFacts;

    /** Metrics contained within the project. */
    @Attribute
    @Singular
    SortedSet<MicroStrategyMetric> microStrategyMetrics;

    /** Visualizations contained within the project. */
    @Attribute
    @Singular
    SortedSet<MicroStrategyVisualization> microStrategyVisualizations;

    /** Documents contained within the project. */
    @Attribute
    @Singular
    SortedSet<MicroStrategyDocument> microStrategyDocuments;

    /** Cubes contained within the project. */
    @Attribute
    @Singular
    SortedSet<MicroStrategyCube> microStrategyCubes;

    /** Dossiers contained within the project. */
    @Attribute
    @Singular
    SortedSet<MicroStrategyDossier> microStrategyDossiers;

    /** Attributes contained within the project. */
    @Attribute
    @Singular
    SortedSet<MicroStrategyAttribute> microStrategyAttributes;

    /**
     * Reference to a MicroStrategyProject by GUID.
     *
     * @param guid the GUID of the MicroStrategyProject to reference
     * @return reference to a MicroStrategyProject that can be used for defining a relationship to a MicroStrategyProject
     */
    public static MicroStrategyProject refByGuid(String guid) {
        return MicroStrategyProject.builder().guid(guid).build();
    }

    /**
     * Reference to a MicroStrategyProject by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the MicroStrategyProject to reference
     * @return reference to a MicroStrategyProject that can be used for defining a relationship to a MicroStrategyProject
     */
    public static MicroStrategyProject refByQualifiedName(String qualifiedName) {
        return MicroStrategyProject.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a MicroStrategyProject by its GUID, complete with all of its relationships.
     *
     * @param guid of the MicroStrategyProject to retrieve
     * @return the requested full MicroStrategyProject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyProject does not exist or the provided GUID is not a MicroStrategyProject
     */
    public static MicroStrategyProject retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof MicroStrategyProject) {
            return (MicroStrategyProject) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "MicroStrategyProject");
        }
    }

    /**
     * Retrieves a MicroStrategyProject by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the MicroStrategyProject to retrieve
     * @return the requested full MicroStrategyProject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyProject does not exist
     */
    public static MicroStrategyProject retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof MicroStrategyProject) {
            return (MicroStrategyProject) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "MicroStrategyProject");
        }
    }

    /**
     * Restore the archived (soft-deleted) MicroStrategyProject to active.
     *
     * @param qualifiedName for the MicroStrategyProject
     * @return true if the MicroStrategyProject is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a MicroStrategyProject.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param name of the MicroStrategyProject
     * @return the minimal request necessary to update the MicroStrategyProject, as a builder
     */
    public static MicroStrategyProjectBuilder<?, ?> updater(String qualifiedName, String name) {
        return MicroStrategyProject.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a MicroStrategyProject, from a potentially
     * more-complete MicroStrategyProject object.
     *
     * @return the minimal object necessary to update the MicroStrategyProject, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for MicroStrategyProject are not found in the initial object
     */
    @Override
    public MicroStrategyProjectBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "MicroStrategyProject", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a MicroStrategyProject.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param name of the MicroStrategyProject
     * @return the updated MicroStrategyProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject removeDescription(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyProject) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a MicroStrategyProject.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param name of the MicroStrategyProject
     * @return the updated MicroStrategyProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyProject) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a MicroStrategyProject.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param name of the MicroStrategyProject
     * @return the updated MicroStrategyProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject removeOwners(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyProject) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a MicroStrategyProject.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MicroStrategyProject, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return (MicroStrategyProject)
                Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MicroStrategyProject.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param name of the MicroStrategyProject
     * @return the updated MicroStrategyProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyProject) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a MicroStrategyProject.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (MicroStrategyProject)
                Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MicroStrategyProject.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param name of the MicroStrategyProject
     * @return the updated MicroStrategyProject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyProject) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the MicroStrategyProject.
     *
     * @param qualifiedName for the MicroStrategyProject
     * @param name human-readable name of the MicroStrategyProject
     * @param terms the list of terms to replace on the MicroStrategyProject, or null to remove all terms from the MicroStrategyProject
     * @return the MicroStrategyProject that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyProject) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MicroStrategyProject, without replacing existing terms linked to the MicroStrategyProject.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyProject's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the MicroStrategyProject
     * @param terms the list of terms to append to the MicroStrategyProject
     * @return the MicroStrategyProject that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject appendTerms(String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyProject) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MicroStrategyProject, without replacing all existing terms linked to the MicroStrategyProject.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyProject's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the MicroStrategyProject
     * @param terms the list of terms to remove from the MicroStrategyProject, which must be referenced by GUID
     * @return the MicroStrategyProject that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyProject removeTerms(String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyProject) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a MicroStrategyProject, without replacing existing Atlan tags linked to the MicroStrategyProject.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyProject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyProject
     */
    public static MicroStrategyProject appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (MicroStrategyProject) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyProject, without replacing existing Atlan tags linked to the MicroStrategyProject.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyProject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyProject
     */
    public static MicroStrategyProject appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (MicroStrategyProject) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a MicroStrategyProject.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MicroStrategyProject
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyProject.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MicroStrategyProject
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
     * Remove an Atlan tag from a MicroStrategyProject.
     *
     * @param qualifiedName of the MicroStrategyProject
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MicroStrategyProject
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
