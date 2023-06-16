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
 * Instance of a ThoughtSpot Dashlet in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class ThoughtspotDashlet extends Asset
        implements IThoughtspotDashlet, IThoughtspot, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ThoughtspotDashlet";

    /** Fixed typeName for ThoughtspotDashlets. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String thoughtspotChartType;

    /** Name of the Liveboard in which the Dashlet exists. */
    @Attribute
    String thoughtspotLiveboardName;

    /** Unique name of the Liveboard in which the Dashlet exists. */
    @Attribute
    String thoughtspotLiveboardQualifiedName;

    /** TBC */
    @Attribute
    String thoughtspotQuestionText;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** Liveboard in which the Dashlet exists. */
    @Attribute
    IThoughtspotLiveboard thoughtspotLiveboard;

    /**
     * Reference to a ThoughtspotDashlet by GUID.
     *
     * @param guid the GUID of the ThoughtspotDashlet to reference
     * @return reference to a ThoughtspotDashlet that can be used for defining a relationship to a ThoughtspotDashlet
     */
    public static ThoughtspotDashlet refByGuid(String guid) {
        return ThoughtspotDashlet.builder().guid(guid).build();
    }

    /**
     * Reference to a ThoughtspotDashlet by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the ThoughtspotDashlet to reference
     * @return reference to a ThoughtspotDashlet that can be used for defining a relationship to a ThoughtspotDashlet
     */
    public static ThoughtspotDashlet refByQualifiedName(String qualifiedName) {
        return ThoughtspotDashlet.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a ThoughtspotDashlet by its GUID, complete with all of its relationships.
     *
     * @param guid of the ThoughtspotDashlet to retrieve
     * @return the requested full ThoughtspotDashlet, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ThoughtspotDashlet does not exist or the provided GUID is not a ThoughtspotDashlet
     */
    public static ThoughtspotDashlet retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof ThoughtspotDashlet) {
            return (ThoughtspotDashlet) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "ThoughtspotDashlet");
        }
    }

    /**
     * Retrieves a ThoughtspotDashlet by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the ThoughtspotDashlet to retrieve
     * @return the requested full ThoughtspotDashlet, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ThoughtspotDashlet does not exist
     */
    public static ThoughtspotDashlet retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof ThoughtspotDashlet) {
            return (ThoughtspotDashlet) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "ThoughtspotDashlet");
        }
    }

    /**
     * Restore the archived (soft-deleted) ThoughtspotDashlet to active.
     *
     * @param qualifiedName for the ThoughtspotDashlet
     * @return true if the ThoughtspotDashlet is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a ThoughtspotDashlet.
     *
     * @param qualifiedName of the ThoughtspotDashlet
     * @param name of the ThoughtspotDashlet
     * @return the minimal request necessary to update the ThoughtspotDashlet, as a builder
     */
    public static ThoughtspotDashletBuilder<?, ?> updater(String qualifiedName, String name) {
        return ThoughtspotDashlet.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ThoughtspotDashlet, from a potentially
     * more-complete ThoughtspotDashlet object.
     *
     * @return the minimal object necessary to update the ThoughtspotDashlet, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ThoughtspotDashlet are not found in the initial object
     */
    @Override
    public ThoughtspotDashletBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "ThoughtspotDashlet", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a ThoughtspotDashlet.
     *
     * @param qualifiedName of the ThoughtspotDashlet
     * @param name of the ThoughtspotDashlet
     * @return the updated ThoughtspotDashlet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotDashlet removeDescription(String qualifiedName, String name) throws AtlanException {
        return (ThoughtspotDashlet) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ThoughtspotDashlet.
     *
     * @param qualifiedName of the ThoughtspotDashlet
     * @param name of the ThoughtspotDashlet
     * @return the updated ThoughtspotDashlet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotDashlet removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (ThoughtspotDashlet) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ThoughtspotDashlet.
     *
     * @param qualifiedName of the ThoughtspotDashlet
     * @param name of the ThoughtspotDashlet
     * @return the updated ThoughtspotDashlet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotDashlet removeOwners(String qualifiedName, String name) throws AtlanException {
        return (ThoughtspotDashlet) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ThoughtspotDashlet.
     *
     * @param qualifiedName of the ThoughtspotDashlet
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ThoughtspotDashlet, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotDashlet updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return (ThoughtspotDashlet) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ThoughtspotDashlet.
     *
     * @param qualifiedName of the ThoughtspotDashlet
     * @param name of the ThoughtspotDashlet
     * @return the updated ThoughtspotDashlet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotDashlet removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (ThoughtspotDashlet) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ThoughtspotDashlet.
     *
     * @param qualifiedName of the ThoughtspotDashlet
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotDashlet updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (ThoughtspotDashlet) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ThoughtspotDashlet.
     *
     * @param qualifiedName of the ThoughtspotDashlet
     * @param name of the ThoughtspotDashlet
     * @return the updated ThoughtspotDashlet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotDashlet removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (ThoughtspotDashlet) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the ThoughtspotDashlet.
     *
     * @param qualifiedName for the ThoughtspotDashlet
     * @param name human-readable name of the ThoughtspotDashlet
     * @param terms the list of terms to replace on the ThoughtspotDashlet, or null to remove all terms from the ThoughtspotDashlet
     * @return the ThoughtspotDashlet that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotDashlet replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ThoughtspotDashlet) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ThoughtspotDashlet, without replacing existing terms linked to the ThoughtspotDashlet.
     * Note: this operation must make two API calls — one to retrieve the ThoughtspotDashlet's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the ThoughtspotDashlet
     * @param terms the list of terms to append to the ThoughtspotDashlet
     * @return the ThoughtspotDashlet that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotDashlet appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ThoughtspotDashlet) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ThoughtspotDashlet, without replacing all existing terms linked to the ThoughtspotDashlet.
     * Note: this operation must make two API calls — one to retrieve the ThoughtspotDashlet's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the ThoughtspotDashlet
     * @param terms the list of terms to remove from the ThoughtspotDashlet, which must be referenced by GUID
     * @return the ThoughtspotDashlet that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotDashlet removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ThoughtspotDashlet) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a ThoughtspotDashlet, without replacing existing Atlan tags linked to the ThoughtspotDashlet.
     * Note: this operation must make two API calls — one to retrieve the ThoughtspotDashlet's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the ThoughtspotDashlet
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ThoughtspotDashlet
     */
    public static ThoughtspotDashlet appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (ThoughtspotDashlet) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ThoughtspotDashlet, without replacing existing Atlan tags linked to the ThoughtspotDashlet.
     * Note: this operation must make two API calls — one to retrieve the ThoughtspotDashlet's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the ThoughtspotDashlet
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ThoughtspotDashlet
     */
    public static ThoughtspotDashlet appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (ThoughtspotDashlet) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a ThoughtspotDashlet.
     *
     * @param qualifiedName of the ThoughtspotDashlet
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ThoughtspotDashlet
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ThoughtspotDashlet.
     *
     * @param qualifiedName of the ThoughtspotDashlet
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ThoughtspotDashlet
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
     * Remove an Atlan tag from a ThoughtspotDashlet.
     *
     * @param qualifiedName of the ThoughtspotDashlet
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ThoughtspotDashlet
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
