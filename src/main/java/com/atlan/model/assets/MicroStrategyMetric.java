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
 * Instance of a MicroStrategy metric in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class MicroStrategyMetric extends MicroStrategy {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MicroStrategyMetric";

    /** Fixed typeName for MicroStrategyMetrics. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Expression that defines the metric. */
    @Attribute
    String microStrategyMetricExpression;

    /** Unique names of the related MicroStrategy attributes. */
    @Attribute
    @Singular
    SortedSet<String> microStrategyAttributeQualifiedNames;

    /** Simple names of the related MicroStrategy attributes. */
    @Attribute
    @Singular
    SortedSet<String> microStrategyAttributeNames;

    /** Unique names of the related MicroStrategy facts. */
    @Attribute
    @Singular
    SortedSet<String> microStrategyFactQualifiedNames;

    /** Simple names of the related MicroStrategy facts. */
    @Attribute
    @Singular
    SortedSet<String> microStrategyFactNames;

    /** Unique names of the parent MicroStrategy metrics. */
    @Attribute
    @Singular
    SortedSet<String> microStrategyMetricParentQualifiedNames;

    /** Simple names of the parent MicroStrategy metrics. */
    @Attribute
    @Singular
    SortedSet<String> microStrategyMetricParentNames;

    /** Metrics that are parents of this metric. */
    @Attribute
    @Singular
    SortedSet<MicroStrategyMetric> microStrategyMetricParents;

    /** Facts related to this metric. */
    @Attribute
    @Singular
    SortedSet<MicroStrategyFact> microStrategyFacts;

    /** Reports related to this metric. */
    @Attribute
    @Singular
    SortedSet<MicroStrategyReport> microStrategyReports;

    /** Cubes related to this metric. */
    @Attribute
    @Singular
    SortedSet<MicroStrategyCube> microStrategyCubes;

    /** Metrics that are children of this metric. */
    @Attribute
    @Singular
    SortedSet<MicroStrategyMetric> microStrategyMetricChildren;

    /** Project containing the metric. */
    @Attribute
    MicroStrategyProject microStrategyProject;

    /** Attributes related to this metric. */
    @Attribute
    @Singular
    SortedSet<MicroStrategyAttribute> microStrategyAttributes;

    /**
     * Reference to a MicroStrategyMetric by GUID.
     *
     * @param guid the GUID of the MicroStrategyMetric to reference
     * @return reference to a MicroStrategyMetric that can be used for defining a relationship to a MicroStrategyMetric
     */
    public static MicroStrategyMetric refByGuid(String guid) {
        return MicroStrategyMetric.builder().guid(guid).build();
    }

    /**
     * Reference to a MicroStrategyMetric by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the MicroStrategyMetric to reference
     * @return reference to a MicroStrategyMetric that can be used for defining a relationship to a MicroStrategyMetric
     */
    public static MicroStrategyMetric refByQualifiedName(String qualifiedName) {
        return MicroStrategyMetric.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a MicroStrategyMetric by its GUID, complete with all of its relationships.
     *
     * @param guid of the MicroStrategyMetric to retrieve
     * @return the requested full MicroStrategyMetric, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyMetric does not exist or the provided GUID is not a MicroStrategyMetric
     */
    public static MicroStrategyMetric retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof MicroStrategyMetric) {
            return (MicroStrategyMetric) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "MicroStrategyMetric");
        }
    }

    /**
     * Retrieves a MicroStrategyMetric by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the MicroStrategyMetric to retrieve
     * @return the requested full MicroStrategyMetric, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyMetric does not exist
     */
    public static MicroStrategyMetric retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof MicroStrategyMetric) {
            return (MicroStrategyMetric) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "MicroStrategyMetric");
        }
    }

    /**
     * Restore the archived (soft-deleted) MicroStrategyMetric to active.
     *
     * @param qualifiedName for the MicroStrategyMetric
     * @return true if the MicroStrategyMetric is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a MicroStrategyMetric.
     *
     * @param qualifiedName of the MicroStrategyMetric
     * @param name of the MicroStrategyMetric
     * @return the minimal request necessary to update the MicroStrategyMetric, as a builder
     */
    public static MicroStrategyMetricBuilder<?, ?> updater(String qualifiedName, String name) {
        return MicroStrategyMetric.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a MicroStrategyMetric, from a potentially
     * more-complete MicroStrategyMetric object.
     *
     * @return the minimal object necessary to update the MicroStrategyMetric, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for MicroStrategyMetric are not found in the initial object
     */
    @Override
    public MicroStrategyMetricBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "MicroStrategyMetric", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a MicroStrategyMetric.
     *
     * @param qualifiedName of the MicroStrategyMetric
     * @param name of the MicroStrategyMetric
     * @return the updated MicroStrategyMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyMetric removeDescription(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyMetric) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a MicroStrategyMetric.
     *
     * @param qualifiedName of the MicroStrategyMetric
     * @param name of the MicroStrategyMetric
     * @return the updated MicroStrategyMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyMetric removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyMetric) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a MicroStrategyMetric.
     *
     * @param qualifiedName of the MicroStrategyMetric
     * @param name of the MicroStrategyMetric
     * @return the updated MicroStrategyMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyMetric removeOwners(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyMetric) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a MicroStrategyMetric.
     *
     * @param qualifiedName of the MicroStrategyMetric
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MicroStrategyMetric, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyMetric updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return (MicroStrategyMetric) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MicroStrategyMetric.
     *
     * @param qualifiedName of the MicroStrategyMetric
     * @param name of the MicroStrategyMetric
     * @return the updated MicroStrategyMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyMetric removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyMetric) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a MicroStrategyMetric.
     *
     * @param qualifiedName of the MicroStrategyMetric
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyMetric updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (MicroStrategyMetric)
                Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MicroStrategyMetric.
     *
     * @param qualifiedName of the MicroStrategyMetric
     * @param name of the MicroStrategyMetric
     * @return the updated MicroStrategyMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyMetric removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (MicroStrategyMetric) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the MicroStrategyMetric.
     *
     * @param qualifiedName for the MicroStrategyMetric
     * @param name human-readable name of the MicroStrategyMetric
     * @param terms the list of terms to replace on the MicroStrategyMetric, or null to remove all terms from the MicroStrategyMetric
     * @return the MicroStrategyMetric that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyMetric replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyMetric) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MicroStrategyMetric, without replacing existing terms linked to the MicroStrategyMetric.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyMetric's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the MicroStrategyMetric
     * @param terms the list of terms to append to the MicroStrategyMetric
     * @return the MicroStrategyMetric that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyMetric appendTerms(String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyMetric) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MicroStrategyMetric, without replacing all existing terms linked to the MicroStrategyMetric.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyMetric's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the MicroStrategyMetric
     * @param terms the list of terms to remove from the MicroStrategyMetric, which must be referenced by GUID
     * @return the MicroStrategyMetric that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyMetric removeTerms(String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyMetric) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a MicroStrategyMetric, without replacing existing Atlan tags linked to the MicroStrategyMetric.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyMetric's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MicroStrategyMetric
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyMetric
     */
    public static MicroStrategyMetric appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (MicroStrategyMetric) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyMetric, without replacing existing Atlan tags linked to the MicroStrategyMetric.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyMetric's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MicroStrategyMetric
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyMetric
     */
    public static MicroStrategyMetric appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (MicroStrategyMetric) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a MicroStrategyMetric.
     *
     * @param qualifiedName of the MicroStrategyMetric
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MicroStrategyMetric
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyMetric.
     *
     * @param qualifiedName of the MicroStrategyMetric
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MicroStrategyMetric
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
     * Remove an Atlan tag from a MicroStrategyMetric.
     *
     * @param qualifiedName of the MicroStrategyMetric
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MicroStrategyMetric
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
