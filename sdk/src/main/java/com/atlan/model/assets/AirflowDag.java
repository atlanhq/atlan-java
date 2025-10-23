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
import com.atlan.model.enums.OpenLineageRunState;
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
 * Instance of an Airflow DAG in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class AirflowDag extends Asset implements IAirflowDag, IAirflow, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AirflowDag";

    /** Fixed typeName for AirflowDags. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Schedule for the DAG. */
    @Attribute
    String airflowDagSchedule;

    /** Duration between scheduled runs, in seconds. */
    @Attribute
    Long airflowDagScheduleDelta;

    /** End time of the run. */
    @Attribute
    @Date
    Long airflowRunEndTime;

    /** Name of the run. */
    @Attribute
    String airflowRunName;

    /** State of the run in OpenLineage. */
    @Attribute
    OpenLineageRunState airflowRunOpenLineageState;

    /** Version of the run in OpenLineage. */
    @Attribute
    String airflowRunOpenLineageVersion;

    /** Start time of the run. */
    @Attribute
    @Date
    Long airflowRunStartTime;

    /** Type of the run. */
    @Attribute
    String airflowRunType;

    /** Version of the run in Airflow. */
    @Attribute
    String airflowRunVersion;

    /** Tags assigned to the asset in Airflow. */
    @Attribute
    @Singular
    SortedSet<String> airflowTags;

    /** Tasks that exist within this DAG. */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> airflowTasks;

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

    /** Spark assets that are executed by this airflow asset. */
    @Attribute
    @Singular
    SortedSet<ISpark> sparkOrchestratedAssets;

    /**
     * Builds the minimal object necessary to create a relationship to a AirflowDag, from a potentially
     * more-complete AirflowDag object.
     *
     * @return the minimal object necessary to relate to the AirflowDag
     * @throws InvalidRequestException if any of the minimal set of required properties for a AirflowDag relationship are not found in the initial object
     */
    @Override
    public AirflowDag trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all AirflowDag assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) AirflowDag assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all AirflowDag assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all AirflowDag assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) AirflowDags will be included
     * @return a fluent search that includes all AirflowDag assets
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
     * Reference to a AirflowDag by GUID. Use this to create a relationship to this AirflowDag,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the AirflowDag to reference
     * @return reference to a AirflowDag that can be used for defining a relationship to a AirflowDag
     */
    public static AirflowDag refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AirflowDag by GUID. Use this to create a relationship to this AirflowDag,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the AirflowDag to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AirflowDag that can be used for defining a relationship to a AirflowDag
     */
    public static AirflowDag refByGuid(String guid, Reference.SaveSemantic semantic) {
        return AirflowDag._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a AirflowDag by qualifiedName. Use this to create a relationship to this AirflowDag,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the AirflowDag to reference
     * @return reference to a AirflowDag that can be used for defining a relationship to a AirflowDag
     */
    public static AirflowDag refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AirflowDag by qualifiedName. Use this to create a relationship to this AirflowDag,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the AirflowDag to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AirflowDag that can be used for defining a relationship to a AirflowDag
     */
    public static AirflowDag refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return AirflowDag._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a AirflowDag by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AirflowDag to retrieve, either its GUID or its full qualifiedName
     * @return the requested full AirflowDag, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AirflowDag does not exist or the provided GUID is not a AirflowDag
     */
    @JsonIgnore
    public static AirflowDag get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a AirflowDag by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AirflowDag to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full AirflowDag, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AirflowDag does not exist or the provided GUID is not a AirflowDag
     */
    @JsonIgnore
    public static AirflowDag get(AtlanClient client, String id, boolean includeAllRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof AirflowDag) {
                return (AirflowDag) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof AirflowDag) {
                return (AirflowDag) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a AirflowDag by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AirflowDag to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AirflowDag, including any relationships
     * @return the requested AirflowDag, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AirflowDag does not exist or the provided GUID is not a AirflowDag
     */
    @JsonIgnore
    public static AirflowDag get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a AirflowDag by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AirflowDag to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AirflowDag, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the AirflowDag
     * @return the requested AirflowDag, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AirflowDag does not exist or the provided GUID is not a AirflowDag
     */
    @JsonIgnore
    public static AirflowDag get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = AirflowDag.select(client)
                    .where(AirflowDag.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof AirflowDag) {
                return (AirflowDag) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = AirflowDag.select(client)
                    .where(AirflowDag.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof AirflowDag) {
                return (AirflowDag) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) AirflowDag to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the AirflowDag
     * @return true if the AirflowDag is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create an AirflowDag.
     *
     * @param name of the AirflowDag
     * @param connectionQualifiedName unique name of the connection through which the DAG is accessible
     * @return the minimal object necessary to create the AirflowDag, as a builder
     */
    public static AirflowDagBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return AirflowDag._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(connectionQualifiedName + "/" + name)
                .name(name)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a AirflowDag.
     *
     * @param qualifiedName of the AirflowDag
     * @param name of the AirflowDag
     * @return the minimal request necessary to update the AirflowDag, as a builder
     */
    public static AirflowDagBuilder<?, ?> updater(String qualifiedName, String name) {
        return AirflowDag._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AirflowDag, from a potentially
     * more-complete AirflowDag object.
     *
     * @return the minimal object necessary to update the AirflowDag, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AirflowDag are not found in the initial object
     */
    @Override
    public AirflowDagBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class AirflowDagBuilder<C extends AirflowDag, B extends AirflowDagBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a AirflowDag.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AirflowDag
     * @param name of the AirflowDag
     * @return the updated AirflowDag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AirflowDag removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AirflowDag) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a AirflowDag.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AirflowDag
     * @param name of the AirflowDag
     * @return the updated AirflowDag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AirflowDag removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AirflowDag) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a AirflowDag.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AirflowDag's owners
     * @param qualifiedName of the AirflowDag
     * @param name of the AirflowDag
     * @return the updated AirflowDag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AirflowDag removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (AirflowDag) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a AirflowDag.
     *
     * @param client connectivity to the Atlan tenant on which to update the AirflowDag's certificate
     * @param qualifiedName of the AirflowDag
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AirflowDag, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AirflowDag updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (AirflowDag)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a AirflowDag.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AirflowDag's certificate
     * @param qualifiedName of the AirflowDag
     * @param name of the AirflowDag
     * @return the updated AirflowDag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AirflowDag removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AirflowDag) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a AirflowDag.
     *
     * @param client connectivity to the Atlan tenant on which to update the AirflowDag's announcement
     * @param qualifiedName of the AirflowDag
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AirflowDag updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (AirflowDag)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a AirflowDag.
     *
     * @param client connectivity to the Atlan client from which to remove the AirflowDag's announcement
     * @param qualifiedName of the AirflowDag
     * @param name of the AirflowDag
     * @return the updated AirflowDag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AirflowDag removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AirflowDag) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the AirflowDag.
     *
     * @param client connectivity to the Atlan tenant on which to replace the AirflowDag's assigned terms
     * @param qualifiedName for the AirflowDag
     * @param name human-readable name of the AirflowDag
     * @param terms the list of terms to replace on the AirflowDag, or null to remove all terms from the AirflowDag
     * @return the AirflowDag that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static AirflowDag replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (AirflowDag) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the AirflowDag, without replacing existing terms linked to the AirflowDag.
     * Note: this operation must make two API calls — one to retrieve the AirflowDag's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the AirflowDag
     * @param qualifiedName for the AirflowDag
     * @param terms the list of terms to append to the AirflowDag
     * @return the AirflowDag that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static AirflowDag appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AirflowDag) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a AirflowDag, without replacing all existing terms linked to the AirflowDag.
     * Note: this operation must make two API calls — one to retrieve the AirflowDag's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the AirflowDag
     * @param qualifiedName for the AirflowDag
     * @param terms the list of terms to remove from the AirflowDag, which must be referenced by GUID
     * @return the AirflowDag that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static AirflowDag removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AirflowDag) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a AirflowDag, without replacing existing Atlan tags linked to the AirflowDag.
     * Note: this operation must make two API calls — one to retrieve the AirflowDag's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AirflowDag
     * @param qualifiedName of the AirflowDag
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated AirflowDag
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static AirflowDag appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (AirflowDag) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AirflowDag, without replacing existing Atlan tags linked to the AirflowDag.
     * Note: this operation must make two API calls — one to retrieve the AirflowDag's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AirflowDag
     * @param qualifiedName of the AirflowDag
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated AirflowDag
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static AirflowDag appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (AirflowDag) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a AirflowDag.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a AirflowDag
     * @param qualifiedName of the AirflowDag
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the AirflowDag
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
