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
import com.atlan.model.enums.FivetranConnectorStatus;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
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
 * Instance of a Fivetran connector asset in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class FivetranConnector extends Asset
        implements IFivetranConnector, IFivetran, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "FivetranConnector";

    /** Fixed typeName for FivetranConnectors. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Total credits used by this destination */
    @Attribute
    Double fivetranConnectorCreditsUsed;

    /** Destination name added by the user on Fivetran */
    @Attribute
    String fivetranConnectorDestinationName;

    /** Type of destination on Fivetran. Eg: redshift, bigquery etc. */
    @Attribute
    String fivetranConnectorDestinationType;

    /** URL to open the destination details on Fivetran */
    @Attribute
    String fivetranConnectorDestinationURL;

    /** Extract time in seconds in the latest sync on fivetran */
    @Attribute
    Double fivetranConnectorLastSyncExtractTimeSeconds;

    /** Extracted data volume in metabytes in the latest sync on Fivetran */
    @Attribute
    Double fivetranConnectorLastSyncExtractVolumeMegabytes;

    /** Timestamp (epoch) when the latest sync finished on Fivetran, in milliseconds */
    @Attribute
    @Date
    Long fivetranConnectorLastSyncFinishedAt;

    /** ID of the latest sync */
    @Attribute
    String fivetranConnectorLastSyncId;

    /** Load time in seconds in the latest sync on Fivetran */
    @Attribute
    Double fivetranConnectorLastSyncLoadTimeSeconds;

    /** Loaded data volume in metabytes in the latest sync on Fivetran */
    @Attribute
    Double fivetranConnectorLastSyncLoadVolumeMegabytes;

    /** Process time in seconds in the latest sync on Fivetran */
    @Attribute
    Double fivetranConnectorLastSyncProcessTimeSeconds;

    /** Process volume in metabytes in the latest sync on Fivetran */
    @Attribute
    Double fivetranConnectorLastSyncProcessVolumeMegabytes;

    /** Failure reason for the latest sync on Fivetran. If status is FAILURE, this is the description of the reason why the sync failed. If status is FAILURE_WITH_TASK, this is the description of the Error. If status is RESCHEDULED, this is the description of the reason why the sync is rescheduled. */
    @Attribute
    String fivetranConnectorLastSyncReason;

    /** Timestamp (epoch) at which the latest sync is rescheduled at on Fivetran */
    @Attribute
    @Date
    Long fivetranConnectorLastSyncRescheduledAt;

    /** Timestamp (epoch) when the latest sync started on Fivetran, in milliseconds */
    @Attribute
    @Date
    Long fivetranConnectorLastSyncStartedAt;

    /** Number of tables synced in the latest sync on Fivetran */
    @Attribute
    Long fivetranConnectorLastSyncTablesSynced;

    /** Failure task type for the latest sync on Fivetran. If status is FAILURE_WITH_TASK or RESCHEDULED, this field displays the type of the Error that caused the failure or rescheduling, respectively, e.g., reconnect, update_service_account, etc. */
    @Attribute
    String fivetranConnectorLastSyncTaskType;

    /** Total sync time in seconds in the latest sync on Fivetran */
    @Attribute
    Double fivetranConnectorLastSyncTotalTimeSeconds;

    /** Increase in the percentage of free MAR compared to the previous month */
    @Attribute
    Double fivetranConnectorMonthlyActiveRowsChangePercentageFree;

    /** Increase in the percentage of paid MAR compared to the previous month */
    @Attribute
    Double fivetranConnectorMonthlyActiveRowsChangePercentagePaid;

    /** Increase in the percentage of total MAR compared to the previous month */
    @Attribute
    Double fivetranConnectorMonthlyActiveRowsChangePercentageTotal;

    /** Free Monthly Active Rows used by the connector in the past month */
    @Attribute
    Long fivetranConnectorMonthlyActiveRowsFree;

    /** Percentage of the account's total free MAR used by this connector */
    @Attribute
    Double fivetranConnectorMonthlyActiveRowsFreePercentageOfAccount;

    /** Paid Monthly Active Rows used by the connector in the past month */
    @Attribute
    Long fivetranConnectorMonthlyActiveRowsPaid;

    /** Percentage of the account's total paid MAR used by this connector */
    @Attribute
    Double fivetranConnectorMonthlyActiveRowsPaidPercentageOfAccount;

    /** Total Monthly Active Rows used by the connector in the past month */
    @Attribute
    Long fivetranConnectorMonthlyActiveRowsTotal;

    /** Percentage of the account's total MAR used by this connector */
    @Attribute
    Double fivetranConnectorMonthlyActiveRowsTotalPercentageOfAccount;

    /** Connector name added by the user on Fivetran */
    @Attribute
    String fivetranConnectorName;

    /** Sync frequency for the connector in number of hours. Eg: Every 6 hours */
    @Attribute
    String fivetranConnectorSyncFrequency;

    /** Boolean to indicate whether the sync for this connector is paused or not */
    @Attribute
    Boolean fivetranConnectorSyncPaused;

    /** Timestamp (epoch) on which the connector was setup on Fivetran, in milliseconds */
    @Attribute
    @Date
    Long fivetranConnectorSyncSetupOn;

    /** Email ID of the user who setpu the connector on Fivetran */
    @Attribute
    String fivetranConnectorSyncSetupUserEmail;

    /** Full name of the user who setup the connector on Fivetran */
    @Attribute
    String fivetranConnectorSyncSetupUserFullName;

    /** Total five tables sorted by MAR synced by this connector */
    @Attribute
    String fivetranConnectorTopTablesByMAR;

    /** Total number of tables synced by this connector */
    @Attribute
    Long fivetranConnectorTotalTablesSynced;

    /** Type of connector on Fivetran. Eg: snowflake, google_analytics, notion etc. */
    @Attribute
    String fivetranConnectorType;

    /** URL to open the connector details on Fivetran */
    @Attribute
    String fivetranConnectorURL;

    /** Total usage cost by this destination */
    @Attribute
    Double fivetranConnectorUsageCost;

    /** Number of records updated in the latest sync on Fivetran */
    @Attribute
    Long fivetranLastSyncRecordsUpdated;

    /** Status of the latest sync on Fivetran. */
    @Attribute
    FivetranConnectorStatus fivetranLastSyncStatus;

    /** Name of the atlan fivetran workflow that updated this asset */
    @Attribute
    String fivetranWorkflowName;

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

    /** Processes related to this Fivetran connector */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> processes;

    /**
     * Builds the minimal object necessary to create a relationship to a FivetranConnector, from a potentially
     * more-complete FivetranConnector object.
     *
     * @return the minimal object necessary to relate to the FivetranConnector
     * @throws InvalidRequestException if any of the minimal set of required properties for a FivetranConnector relationship are not found in the initial object
     */
    @Override
    public FivetranConnector trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all FivetranConnector assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) FivetranConnector assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all FivetranConnector assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all FivetranConnector assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) FivetranConnectors will be included
     * @return a fluent search that includes all FivetranConnector assets
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
     * Reference to a FivetranConnector by GUID. Use this to create a relationship to this FivetranConnector,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the FivetranConnector to reference
     * @return reference to a FivetranConnector that can be used for defining a relationship to a FivetranConnector
     */
    public static FivetranConnector refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a FivetranConnector by GUID. Use this to create a relationship to this FivetranConnector,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the FivetranConnector to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a FivetranConnector that can be used for defining a relationship to a FivetranConnector
     */
    public static FivetranConnector refByGuid(String guid, Reference.SaveSemantic semantic) {
        return FivetranConnector._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a FivetranConnector by qualifiedName. Use this to create a relationship to this FivetranConnector,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the FivetranConnector to reference
     * @return reference to a FivetranConnector that can be used for defining a relationship to a FivetranConnector
     */
    public static FivetranConnector refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a FivetranConnector by qualifiedName. Use this to create a relationship to this FivetranConnector,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the FivetranConnector to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a FivetranConnector that can be used for defining a relationship to a FivetranConnector
     */
    public static FivetranConnector refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return FivetranConnector._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a FivetranConnector by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the FivetranConnector to retrieve, either its GUID or its full qualifiedName
     * @return the requested full FivetranConnector, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the FivetranConnector does not exist or the provided GUID is not a FivetranConnector
     */
    @JsonIgnore
    public static FivetranConnector get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a FivetranConnector by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the FivetranConnector to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full FivetranConnector, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the FivetranConnector does not exist or the provided GUID is not a FivetranConnector
     */
    @JsonIgnore
    public static FivetranConnector get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof FivetranConnector) {
                return (FivetranConnector) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof FivetranConnector) {
                return (FivetranConnector) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a FivetranConnector by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the FivetranConnector to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the FivetranConnector, including any relationships
     * @return the requested FivetranConnector, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the FivetranConnector does not exist or the provided GUID is not a FivetranConnector
     */
    @JsonIgnore
    public static FivetranConnector get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a FivetranConnector by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the FivetranConnector to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the FivetranConnector, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the FivetranConnector
     * @return the requested FivetranConnector, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the FivetranConnector does not exist or the provided GUID is not a FivetranConnector
     */
    @JsonIgnore
    public static FivetranConnector get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = FivetranConnector.select(client)
                    .where(FivetranConnector.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof FivetranConnector) {
                return (FivetranConnector) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = FivetranConnector.select(client)
                    .where(FivetranConnector.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof FivetranConnector) {
                return (FivetranConnector) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) FivetranConnector to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the FivetranConnector
     * @return true if the FivetranConnector is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a FivetranConnector.
     *
     * @param qualifiedName of the FivetranConnector
     * @param name of the FivetranConnector
     * @return the minimal request necessary to update the FivetranConnector, as a builder
     */
    public static FivetranConnectorBuilder<?, ?> updater(String qualifiedName, String name) {
        return FivetranConnector._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a FivetranConnector, from a potentially
     * more-complete FivetranConnector object.
     *
     * @return the minimal object necessary to update the FivetranConnector, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for FivetranConnector are not found in the initial object
     */
    @Override
    public FivetranConnectorBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class FivetranConnectorBuilder<
                    C extends FivetranConnector, B extends FivetranConnectorBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a FivetranConnector.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the FivetranConnector
     * @param name of the FivetranConnector
     * @return the updated FivetranConnector, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static FivetranConnector removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (FivetranConnector) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a FivetranConnector.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the FivetranConnector
     * @param name of the FivetranConnector
     * @return the updated FivetranConnector, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static FivetranConnector removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (FivetranConnector) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a FivetranConnector.
     *
     * @param client connectivity to the Atlan tenant from which to remove the FivetranConnector's owners
     * @param qualifiedName of the FivetranConnector
     * @param name of the FivetranConnector
     * @return the updated FivetranConnector, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static FivetranConnector removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (FivetranConnector) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a FivetranConnector.
     *
     * @param client connectivity to the Atlan tenant on which to update the FivetranConnector's certificate
     * @param qualifiedName of the FivetranConnector
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated FivetranConnector, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static FivetranConnector updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (FivetranConnector)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a FivetranConnector.
     *
     * @param client connectivity to the Atlan tenant from which to remove the FivetranConnector's certificate
     * @param qualifiedName of the FivetranConnector
     * @param name of the FivetranConnector
     * @return the updated FivetranConnector, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static FivetranConnector removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (FivetranConnector) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a FivetranConnector.
     *
     * @param client connectivity to the Atlan tenant on which to update the FivetranConnector's announcement
     * @param qualifiedName of the FivetranConnector
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static FivetranConnector updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (FivetranConnector)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a FivetranConnector.
     *
     * @param client connectivity to the Atlan client from which to remove the FivetranConnector's announcement
     * @param qualifiedName of the FivetranConnector
     * @param name of the FivetranConnector
     * @return the updated FivetranConnector, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static FivetranConnector removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (FivetranConnector) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the FivetranConnector.
     *
     * @param client connectivity to the Atlan tenant on which to replace the FivetranConnector's assigned terms
     * @param qualifiedName for the FivetranConnector
     * @param name human-readable name of the FivetranConnector
     * @param terms the list of terms to replace on the FivetranConnector, or null to remove all terms from the FivetranConnector
     * @return the FivetranConnector that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static FivetranConnector replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (FivetranConnector) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the FivetranConnector, without replacing existing terms linked to the FivetranConnector.
     * Note: this operation must make two API calls — one to retrieve the FivetranConnector's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the FivetranConnector
     * @param qualifiedName for the FivetranConnector
     * @param terms the list of terms to append to the FivetranConnector
     * @return the FivetranConnector that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static FivetranConnector appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (FivetranConnector) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a FivetranConnector, without replacing all existing terms linked to the FivetranConnector.
     * Note: this operation must make two API calls — one to retrieve the FivetranConnector's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the FivetranConnector
     * @param qualifiedName for the FivetranConnector
     * @param terms the list of terms to remove from the FivetranConnector, which must be referenced by GUID
     * @return the FivetranConnector that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static FivetranConnector removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (FivetranConnector) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a FivetranConnector, without replacing existing Atlan tags linked to the FivetranConnector.
     * Note: this operation must make two API calls — one to retrieve the FivetranConnector's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the FivetranConnector
     * @param qualifiedName of the FivetranConnector
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated FivetranConnector
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static FivetranConnector appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (FivetranConnector) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a FivetranConnector, without replacing existing Atlan tags linked to the FivetranConnector.
     * Note: this operation must make two API calls — one to retrieve the FivetranConnector's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the FivetranConnector
     * @param qualifiedName of the FivetranConnector
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated FivetranConnector
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static FivetranConnector appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (FivetranConnector) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a FivetranConnector.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a FivetranConnector
     * @param qualifiedName of the FivetranConnector
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the FivetranConnector
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
