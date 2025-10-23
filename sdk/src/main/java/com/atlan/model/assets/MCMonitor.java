/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.model.structs.MCRuleComparison;
import com.atlan.model.structs.MCRuleSchedule;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Monte Carlo monitor in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class MCMonitor extends Asset
        implements IMCMonitor, IMonteCarlo, IDataQuality, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MCMonitor";

    /** Fixed typeName for MCMonitors. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Whether this data quality is part of contract (true) or not (false). */
    @Attribute
    Boolean dqIsPartOfContract;

    /** Tasks to which this asset provides input. */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> inputToAirflowTasks;

    /** Processes to which this asset provides input. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISparkJob> inputToSparkJobs;

    /** List of unique names of assets that are part of this Monte Carlo asset. */
    @Attribute
    @Singular
    SortedSet<String> mcAssetQualifiedNames;

    /** List of labels for this Monte Carlo asset. */
    @Attribute
    @Singular
    SortedSet<String> mcLabels;

    /** Condition on which the monitor produces an alert. */
    @Attribute
    String mcMonitorAlertCondition;

    /** Number of alerts associated with this monitor. */
    @Attribute
    Long mcMonitorAlertCount;

    /** Assets impacted by this monitor. */
    @Attribute
    @Singular
    SortedSet<IAsset> mcMonitorAssets;

    /** Rate at which this monitor is breached. */
    @Attribute
    Double mcMonitorBreachRate;

    /** Unique identifier for this monitor, from Monte Carlo. */
    @Attribute
    String mcMonitorId;

    /** Number of incidents associated with this monitor. */
    @Attribute
    Long mcMonitorIncidentCount;

    /** Whether the monitor is OOTB or not */
    @Attribute
    Boolean mcMonitorIsOotb;

    /** Namespace of this monitor. */
    @Attribute
    String mcMonitorNamespace;

    /** Channels through which notifications are sent for this monitor (e.g., email, slack, webhook). */
    @Attribute
    @Singular
    SortedSet<String> mcMonitorNotificationChannels;

    /** Priority of this monitor. */
    @Attribute
    String mcMonitorPriority;

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
    @Date
    Long mcMonitorRuleNextExecutionTime;

    /** Time at which the previous execution of the rule occurred. */
    @Attribute
    @Date
    Long mcMonitorRulePreviousExecutionTime;

    /** Schedule details for the rule. */
    @Attribute
    MCRuleSchedule mcMonitorRuleScheduleConfig;

    /** Readable description of the schedule for the rule. */
    @Attribute
    String mcMonitorRuleScheduleConfigHumanized;

    /** Type of rule for this monitor. */
    @Attribute
    String mcMonitorRuleType;

    /** Type of schedule for this monitor, for example: fixed or dynamic. */
    @Attribute
    String mcMonitorScheduleType;

    /** Status of this monitor. */
    @Attribute
    String mcMonitorStatus;

    /** Type of this monitor, for example: field health (stats) or dimension tracking (categories). */
    @Attribute
    String mcMonitorType;

    /** Name of the warehouse for this monitor. */
    @Attribute
    String mcMonitorWarehouse;

    /** Attributes implemented by this asset. */
    @Attribute
    @Singular
    SortedSet<IModelAttribute> modelImplementedAttributes;

    /** Entities implemented by this asset. */
    @Attribute
    @Singular
    SortedSet<IModelEntity> modelImplementedEntities;

    /** Tasks from which this asset is output. */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> outputFromAirflowTasks;

    /** Processes from which this asset is produced as output. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISparkJob> outputFromSparkJobs;

    /**
     * Builds the minimal object necessary to create a relationship to a MCMonitor, from a potentially
     * more-complete MCMonitor object.
     *
     * @return the minimal object necessary to relate to the MCMonitor
     * @throws InvalidRequestException if any of the minimal set of required properties for a MCMonitor relationship are not found in the initial object
     */
    @Override
    public MCMonitor trimToReference() throws InvalidRequestException {
        if (this.getGuid() != null && !this.getGuid().isEmpty()) {
            return refByGuid(this.getGuid());
        }
        if (this.getQualifiedName() != null && !this.getQualifiedName().isEmpty()) {
            return refByQualifiedName(this.getQualifiedName());
        }
        if (this.getUniqueAttributes() != null
                && this.getUniqueAttributes().getQualifiedName() != null
                && !this.getUniqueAttributes().getQualifiedName().isEmpty()) {
            return refByQualifiedName(this.getUniqueAttributes().getQualifiedName());
        }
        throw new InvalidRequestException(
                ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, TYPE_NAME, "guid, qualifiedName");
    }

    /**
     * Start a fluent search that will return all MCMonitor assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) MCMonitor assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all MCMonitor assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all MCMonitor assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) MCMonitors will be included
     * @return a fluent search that includes all MCMonitor assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client, boolean includeArchived) {
        FluentSearch.FluentSearchBuilder<?, ?> builder =
                FluentSearch.builder(client).where(Asset.TYPE_NAME.eq(TYPE_NAME));
        if (!includeArchived) {
            builder.active();
        }
        return builder;
    }

    /**
     * Reference to a MCMonitor by GUID. Use this to create a relationship to this MCMonitor,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the MCMonitor to reference
     * @return reference to a MCMonitor that can be used for defining a relationship to a MCMonitor
     */
    public static MCMonitor refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a MCMonitor by GUID. Use this to create a relationship to this MCMonitor,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the MCMonitor to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a MCMonitor that can be used for defining a relationship to a MCMonitor
     */
    public static MCMonitor refByGuid(String guid, Reference.SaveSemantic semantic) {
        return MCMonitor._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a MCMonitor by qualifiedName. Use this to create a relationship to this MCMonitor,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the MCMonitor to reference
     * @return reference to a MCMonitor that can be used for defining a relationship to a MCMonitor
     */
    public static MCMonitor refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a MCMonitor by qualifiedName. Use this to create a relationship to this MCMonitor,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the MCMonitor to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a MCMonitor that can be used for defining a relationship to a MCMonitor
     */
    public static MCMonitor refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return MCMonitor._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a MCMonitor by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the MCMonitor to retrieve, either its GUID or its full qualifiedName
     * @return the requested full MCMonitor, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MCMonitor does not exist or the provided GUID is not a MCMonitor
     */
    @JsonIgnore
    public static MCMonitor get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a MCMonitor by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the MCMonitor to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full MCMonitor, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MCMonitor does not exist or the provided GUID is not a MCMonitor
     */
    @JsonIgnore
    public static MCMonitor get(AtlanClient client, String id, boolean includeAllRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof MCMonitor) {
                return (MCMonitor) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof MCMonitor) {
                return (MCMonitor) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a MCMonitor by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the MCMonitor to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the MCMonitor, including any relationships
     * @return the requested MCMonitor, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MCMonitor does not exist or the provided GUID is not a MCMonitor
     */
    @JsonIgnore
    public static MCMonitor get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a MCMonitor by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the MCMonitor to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the MCMonitor, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the MCMonitor
     * @return the requested MCMonitor, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MCMonitor does not exist or the provided GUID is not a MCMonitor
     */
    @JsonIgnore
    public static MCMonitor get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = MCMonitor.select(client)
                    .where(MCMonitor.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof MCMonitor) {
                return (MCMonitor) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = MCMonitor.select(client)
                    .where(MCMonitor.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof MCMonitor) {
                return (MCMonitor) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) MCMonitor to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the MCMonitor
     * @return true if the MCMonitor is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a MCMonitor.
     *
     * @param qualifiedName of the MCMonitor
     * @param name of the MCMonitor
     * @return the minimal request necessary to update the MCMonitor, as a builder
     */
    public static MCMonitorBuilder<?, ?> updater(String qualifiedName, String name) {
        return MCMonitor._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
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
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class MCMonitorBuilder<C extends MCMonitor, B extends MCMonitorBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a MCMonitor.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MCMonitor
     * @param name of the MCMonitor
     * @return the updated MCMonitor, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MCMonitor removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MCMonitor) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a MCMonitor.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MCMonitor
     * @param name of the MCMonitor
     * @return the updated MCMonitor, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MCMonitor removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MCMonitor) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a MCMonitor.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MCMonitor's owners
     * @param qualifiedName of the MCMonitor
     * @param name of the MCMonitor
     * @return the updated MCMonitor, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MCMonitor removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (MCMonitor) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a MCMonitor.
     *
     * @param client connectivity to the Atlan tenant on which to update the MCMonitor's certificate
     * @param qualifiedName of the MCMonitor
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MCMonitor, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MCMonitor updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (MCMonitor) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MCMonitor.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MCMonitor's certificate
     * @param qualifiedName of the MCMonitor
     * @param name of the MCMonitor
     * @return the updated MCMonitor, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MCMonitor removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MCMonitor) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a MCMonitor.
     *
     * @param client connectivity to the Atlan tenant on which to update the MCMonitor's announcement
     * @param qualifiedName of the MCMonitor
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MCMonitor updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (MCMonitor)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MCMonitor.
     *
     * @param client connectivity to the Atlan client from which to remove the MCMonitor's announcement
     * @param qualifiedName of the MCMonitor
     * @param name of the MCMonitor
     * @return the updated MCMonitor, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MCMonitor removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MCMonitor) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the MCMonitor.
     *
     * @param client connectivity to the Atlan tenant on which to replace the MCMonitor's assigned terms
     * @param qualifiedName for the MCMonitor
     * @param name human-readable name of the MCMonitor
     * @param terms the list of terms to replace on the MCMonitor, or null to remove all terms from the MCMonitor
     * @return the MCMonitor that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MCMonitor replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (MCMonitor) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MCMonitor, without replacing existing terms linked to the MCMonitor.
     * Note: this operation must make two API calls — one to retrieve the MCMonitor's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the MCMonitor
     * @param qualifiedName for the MCMonitor
     * @param terms the list of terms to append to the MCMonitor
     * @return the MCMonitor that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static MCMonitor appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MCMonitor) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MCMonitor, without replacing all existing terms linked to the MCMonitor.
     * Note: this operation must make two API calls — one to retrieve the MCMonitor's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the MCMonitor
     * @param qualifiedName for the MCMonitor
     * @param terms the list of terms to remove from the MCMonitor, which must be referenced by GUID
     * @return the MCMonitor that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static MCMonitor removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MCMonitor) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a MCMonitor, without replacing existing Atlan tags linked to the MCMonitor.
     * Note: this operation must make two API calls — one to retrieve the MCMonitor's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MCMonitor
     * @param qualifiedName of the MCMonitor
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MCMonitor
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static MCMonitor appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (MCMonitor) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MCMonitor, without replacing existing Atlan tags linked to the MCMonitor.
     * Note: this operation must make two API calls — one to retrieve the MCMonitor's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MCMonitor
     * @param qualifiedName of the MCMonitor
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MCMonitor
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static MCMonitor appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (MCMonitor) Asset.appendAtlanTags(
                client,
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
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a MCMonitor
     * @param qualifiedName of the MCMonitor
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MCMonitor
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
