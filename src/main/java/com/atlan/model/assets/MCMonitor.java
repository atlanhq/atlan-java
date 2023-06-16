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
import com.atlan.model.structs.MCRuleComparison;
import com.atlan.model.structs.MCRuleSchedule;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Monte Carlo monitor in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class MCMonitor extends Asset
        implements IMCMonitor, IMonteCarlo, IDataQuality, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MCMonitor";

    /** Fixed typeName for MCMonitors. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> mcAssetQualifiedNames;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> mcLabels;

    /** Condition on which the monitor produces an alert. */
    @Attribute
    String mcMonitorAlertCondition;

    /** TBC */
    @Attribute
    Double mcMonitorBreachRate;

    /** Unique identifier for the monitor. */
    @Attribute
    String mcMonitorId;

    /** Number of incidents associated with this monitor. */
    @Attribute
    Long mcMonitorIncidentCount;

    /** Namespace of the monitor. */
    @Attribute
    String mcMonitorNamespace;

    /** Comparison logic used for the rule. */
    @Attribute
    @Singular
    List<MCRuleComparison> mcMonitorRuleComparisons;

    /** SQL code for custom SQL rules. */
    @Attribute
    String mcMonitorRuleCustomSql;

    /** Whether the rule is currently snoozed (true) or not (false). */
    @Attribute
    Boolean mcMonitorRuleIsSnoozed;

    /** Time at which the next execution of the rule should occur. */
    @Attribute
    Long mcMonitorRuleNextExecutionTime;

    /** Time at which the previous execution of the rule occurred. */
    @Attribute
    Long mcMonitorRulePreviousExecutionTime;

    /** Schedule details for the rule. */
    @Attribute
    @Singular("addMcMonitorRuleSchedule")
    List<MCRuleSchedule> mcMonitorRuleScheduleConfig;

    /** Readable description of the schedule for the rule. */
    @Attribute
    String mcMonitorRuleScheduleConfigHumanized;

    /** Type of rule for the monitor. */
    @Attribute
    String mcMonitorRuleType;

    /** Type of schedule for the monitor, for example fixed or dynamic. */
    @Attribute
    String mcMonitorScheduleType;

    /** Status of the monitor. */
    @Attribute
    String mcMonitorStatus;

    /** Type of monitor, for example field health (stats) or dimension tracking (categories). */
    @Attribute
    String mcMonitorType;

    /** Name of the warehouse for the monitor. */
    @Attribute
    String mcMonitorWarehouse;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** Assets impacted by this monitor. */
    @Attribute
    @Singular
    SortedSet<IAsset> mcMonitorAssets;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /**
     * Reference to a MCMonitor by GUID.
     *
     * @param guid the GUID of the MCMonitor to reference
     * @return reference to a MCMonitor that can be used for defining a relationship to a MCMonitor
     */
    public static MCMonitor refByGuid(String guid) {
        return MCMonitor.builder().guid(guid).build();
    }

    /**
     * Reference to a MCMonitor by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the MCMonitor to reference
     * @return reference to a MCMonitor that can be used for defining a relationship to a MCMonitor
     */
    public static MCMonitor refByQualifiedName(String qualifiedName) {
        return MCMonitor.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a MCMonitor by its GUID, complete with all of its relationships.
     *
     * @param guid of the MCMonitor to retrieve
     * @return the requested full MCMonitor, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MCMonitor does not exist or the provided GUID is not a MCMonitor
     */
    public static MCMonitor retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof MCMonitor) {
            return (MCMonitor) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "MCMonitor");
        }
    }

    /**
     * Retrieves a MCMonitor by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the MCMonitor to retrieve
     * @return the requested full MCMonitor, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MCMonitor does not exist
     */
    public static MCMonitor retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof MCMonitor) {
            return (MCMonitor) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "MCMonitor");
        }
    }

    /**
     * Restore the archived (soft-deleted) MCMonitor to active.
     *
     * @param qualifiedName for the MCMonitor
     * @return true if the MCMonitor is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a MCMonitor.
     *
     * @param qualifiedName of the MCMonitor
     * @param name of the MCMonitor
     * @return the minimal request necessary to update the MCMonitor, as a builder
     */
    public static MCMonitorBuilder<?, ?> updater(String qualifiedName, String name) {
        return MCMonitor.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a MCMonitor, from a potentially
     * more-complete MCMonitor object.
     *
     * @return the minimal object necessary to update the MCMonitor, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for MCMonitor are not found in the initial object
     */
    @Override
    public MCMonitorBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "MCMonitor", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a MCMonitor.
     *
     * @param qualifiedName of the MCMonitor
     * @param name of the MCMonitor
     * @return the updated MCMonitor, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MCMonitor removeDescription(String qualifiedName, String name) throws AtlanException {
        return (MCMonitor) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a MCMonitor.
     *
     * @param qualifiedName of the MCMonitor
     * @param name of the MCMonitor
     * @return the updated MCMonitor, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MCMonitor removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (MCMonitor) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a MCMonitor.
     *
     * @param qualifiedName of the MCMonitor
     * @param name of the MCMonitor
     * @return the updated MCMonitor, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MCMonitor removeOwners(String qualifiedName, String name) throws AtlanException {
        return (MCMonitor) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a MCMonitor.
     *
     * @param qualifiedName of the MCMonitor
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MCMonitor, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MCMonitor updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (MCMonitor) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MCMonitor.
     *
     * @param qualifiedName of the MCMonitor
     * @param name of the MCMonitor
     * @return the updated MCMonitor, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MCMonitor removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (MCMonitor) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a MCMonitor.
     *
     * @param qualifiedName of the MCMonitor
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MCMonitor updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (MCMonitor) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MCMonitor.
     *
     * @param qualifiedName of the MCMonitor
     * @param name of the MCMonitor
     * @return the updated MCMonitor, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MCMonitor removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (MCMonitor) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the MCMonitor.
     *
     * @param qualifiedName for the MCMonitor
     * @param name human-readable name of the MCMonitor
     * @param terms the list of terms to replace on the MCMonitor, or null to remove all terms from the MCMonitor
     * @return the MCMonitor that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MCMonitor replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MCMonitor) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MCMonitor, without replacing existing terms linked to the MCMonitor.
     * Note: this operation must make two API calls — one to retrieve the MCMonitor's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the MCMonitor
     * @param terms the list of terms to append to the MCMonitor
     * @return the MCMonitor that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MCMonitor appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (MCMonitor) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MCMonitor, without replacing all existing terms linked to the MCMonitor.
     * Note: this operation must make two API calls — one to retrieve the MCMonitor's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the MCMonitor
     * @param terms the list of terms to remove from the MCMonitor, which must be referenced by GUID
     * @return the MCMonitor that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MCMonitor removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (MCMonitor) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a MCMonitor, without replacing existing Atlan tags linked to the MCMonitor.
     * Note: this operation must make two API calls — one to retrieve the MCMonitor's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MCMonitor
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MCMonitor
     */
    public static MCMonitor appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (MCMonitor) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MCMonitor, without replacing existing Atlan tags linked to the MCMonitor.
     * Note: this operation must make two API calls — one to retrieve the MCMonitor's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MCMonitor
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MCMonitor
     */
    public static MCMonitor appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (MCMonitor) Asset.appendAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a MCMonitor.
     *
     * @param qualifiedName of the MCMonitor
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MCMonitor
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MCMonitor.
     *
     * @param qualifiedName of the MCMonitor
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MCMonitor
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
     * Remove an Atlan tag from a MCMonitor.
     *
     * @param qualifiedName of the MCMonitor
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MCMonitor
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
