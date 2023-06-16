/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.PowerBIEndorsementType;
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
 * Instance of a Power BI datasource in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class PowerBIDatasource extends Asset
        implements IPowerBIDatasource, IPowerBI, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "PowerBIDatasource";

    /** Fixed typeName for PowerBIDatasources. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    Map<String, String> connectionDetails;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IPowerBIDataset> datasets;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    PowerBIEndorsementType powerBIEndorsement;

    /** TBC */
    @Attribute
    String powerBIFormatString;

    /** TBC */
    @Attribute
    Boolean powerBIIsHidden;

    /** TBC */
    @Attribute
    String powerBITableQualifiedName;

    /**
     * Reference to a PowerBIDatasource by GUID.
     *
     * @param guid the GUID of the PowerBIDatasource to reference
     * @return reference to a PowerBIDatasource that can be used for defining a relationship to a PowerBIDatasource
     */
    public static PowerBIDatasource refByGuid(String guid) {
        return PowerBIDatasource.builder().guid(guid).build();
    }

    /**
     * Reference to a PowerBIDatasource by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the PowerBIDatasource to reference
     * @return reference to a PowerBIDatasource that can be used for defining a relationship to a PowerBIDatasource
     */
    public static PowerBIDatasource refByQualifiedName(String qualifiedName) {
        return PowerBIDatasource.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a PowerBIDatasource by its GUID, complete with all of its relationships.
     *
     * @param guid of the PowerBIDatasource to retrieve
     * @return the requested full PowerBIDatasource, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PowerBIDatasource does not exist or the provided GUID is not a PowerBIDatasource
     */
    public static PowerBIDatasource retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof PowerBIDatasource) {
            return (PowerBIDatasource) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "PowerBIDatasource");
        }
    }

    /**
     * Retrieves a PowerBIDatasource by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the PowerBIDatasource to retrieve
     * @return the requested full PowerBIDatasource, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PowerBIDatasource does not exist
     */
    public static PowerBIDatasource retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof PowerBIDatasource) {
            return (PowerBIDatasource) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "PowerBIDatasource");
        }
    }

    /**
     * Restore the archived (soft-deleted) PowerBIDatasource to active.
     *
     * @param qualifiedName for the PowerBIDatasource
     * @return true if the PowerBIDatasource is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a PowerBIDatasource.
     *
     * @param qualifiedName of the PowerBIDatasource
     * @param name of the PowerBIDatasource
     * @return the minimal request necessary to update the PowerBIDatasource, as a builder
     */
    public static PowerBIDatasourceBuilder<?, ?> updater(String qualifiedName, String name) {
        return PowerBIDatasource.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PowerBIDatasource, from a potentially
     * more-complete PowerBIDatasource object.
     *
     * @return the minimal object necessary to update the PowerBIDatasource, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for PowerBIDatasource are not found in the initial object
     */
    @Override
    public PowerBIDatasourceBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "PowerBIDatasource", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a PowerBIDatasource.
     *
     * @param qualifiedName of the PowerBIDatasource
     * @param name of the PowerBIDatasource
     * @return the updated PowerBIDatasource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIDatasource removeDescription(String qualifiedName, String name) throws AtlanException {
        return (PowerBIDatasource) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a PowerBIDatasource.
     *
     * @param qualifiedName of the PowerBIDatasource
     * @param name of the PowerBIDatasource
     * @return the updated PowerBIDatasource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIDatasource removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (PowerBIDatasource) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a PowerBIDatasource.
     *
     * @param qualifiedName of the PowerBIDatasource
     * @param name of the PowerBIDatasource
     * @return the updated PowerBIDatasource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIDatasource removeOwners(String qualifiedName, String name) throws AtlanException {
        return (PowerBIDatasource) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a PowerBIDatasource.
     *
     * @param qualifiedName of the PowerBIDatasource
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated PowerBIDatasource, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIDatasource updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return (PowerBIDatasource) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a PowerBIDatasource.
     *
     * @param qualifiedName of the PowerBIDatasource
     * @param name of the PowerBIDatasource
     * @return the updated PowerBIDatasource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIDatasource removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (PowerBIDatasource) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a PowerBIDatasource.
     *
     * @param qualifiedName of the PowerBIDatasource
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIDatasource updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (PowerBIDatasource) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a PowerBIDatasource.
     *
     * @param qualifiedName of the PowerBIDatasource
     * @param name of the PowerBIDatasource
     * @return the updated PowerBIDatasource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIDatasource removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (PowerBIDatasource) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the PowerBIDatasource.
     *
     * @param qualifiedName for the PowerBIDatasource
     * @param name human-readable name of the PowerBIDatasource
     * @param terms the list of terms to replace on the PowerBIDatasource, or null to remove all terms from the PowerBIDatasource
     * @return the PowerBIDatasource that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIDatasource replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (PowerBIDatasource) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the PowerBIDatasource, without replacing existing terms linked to the PowerBIDatasource.
     * Note: this operation must make two API calls — one to retrieve the PowerBIDatasource's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the PowerBIDatasource
     * @param terms the list of terms to append to the PowerBIDatasource
     * @return the PowerBIDatasource that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIDatasource appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (PowerBIDatasource) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a PowerBIDatasource, without replacing all existing terms linked to the PowerBIDatasource.
     * Note: this operation must make two API calls — one to retrieve the PowerBIDatasource's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the PowerBIDatasource
     * @param terms the list of terms to remove from the PowerBIDatasource, which must be referenced by GUID
     * @return the PowerBIDatasource that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIDatasource removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (PowerBIDatasource) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a PowerBIDatasource, without replacing existing Atlan tags linked to the PowerBIDatasource.
     * Note: this operation must make two API calls — one to retrieve the PowerBIDatasource's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the PowerBIDatasource
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated PowerBIDatasource
     */
    public static PowerBIDatasource appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (PowerBIDatasource) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a PowerBIDatasource, without replacing existing Atlan tags linked to the PowerBIDatasource.
     * Note: this operation must make two API calls — one to retrieve the PowerBIDatasource's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the PowerBIDatasource
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated PowerBIDatasource
     */
    public static PowerBIDatasource appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (PowerBIDatasource) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a PowerBIDatasource.
     *
     * @param qualifiedName of the PowerBIDatasource
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the PowerBIDatasource
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a PowerBIDatasource.
     *
     * @param qualifiedName of the PowerBIDatasource
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the PowerBIDatasource
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
     * Remove an Atlan tag from a PowerBIDatasource.
     *
     * @param qualifiedName of the PowerBIDatasource
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the PowerBIDatasource
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
