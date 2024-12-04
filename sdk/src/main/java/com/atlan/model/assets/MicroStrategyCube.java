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
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a MicroStrategy cube in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class MicroStrategyCube extends Asset
        implements IMicroStrategyCube, IMicroStrategy, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MicroStrategyCube";

    /** Fixed typeName for MicroStrategyCubes. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

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

    /** Attributes used by this cube. */
    @Attribute
    @Singular
    SortedSet<IMicroStrategyAttribute> microStrategyAttributes;

    /** Time (epoch) this asset was certified in MicroStrategy, in milliseconds. */
    @Attribute
    @Date
    Long microStrategyCertifiedAt;

    /** User who certified this asset, in MicroStrategy. */
    @Attribute
    String microStrategyCertifiedBy;

    /** Simple names of the cubes related to this asset. */
    @Attribute
    @Singular
    SortedSet<String> microStrategyCubeNames;

    /** Unique names of the cubes related to this asset. */
    @Attribute
    @Singular
    SortedSet<String> microStrategyCubeQualifiedNames;

    /** Query used to create the cube. */
    @Attribute
    String microStrategyCubeQuery;

    /** Type of cube, for example: OLAP or MTDI. */
    @Attribute
    String microStrategyCubeType;

    /** Whether the asset is certified in MicroStrategy (true) or not (false). */
    @Attribute
    Boolean microStrategyIsCertified;

    /** Location of this asset in MicroStrategy. */
    @Attribute
    @Singular("putMicroStrategyLocation")
    List<Map<String, String>> microStrategyLocation;

    /** Metrics that use this cube. */
    @Attribute
    @Singular
    SortedSet<IMicroStrategyMetric> microStrategyMetrics;

    /** Project in which this cube exists. */
    @Attribute
    IMicroStrategyProject microStrategyProject;

    /** Simple name of the project in which this asset exists. */
    @Attribute
    String microStrategyProjectName;

    /** Unique name of the project in which this asset exists. */
    @Attribute
    String microStrategyProjectQualifiedName;

    /** Simple names of the reports related to this asset. */
    @Attribute
    @Singular
    SortedSet<String> microStrategyReportNames;

    /** Unique names of the reports related to this asset. */
    @Attribute
    @Singular
    SortedSet<String> microStrategyReportQualifiedNames;

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
     * Builds the minimal object necessary to create a relationship to a MicroStrategyCube, from a potentially
     * more-complete MicroStrategyCube object.
     *
     * @return the minimal object necessary to relate to the MicroStrategyCube
     * @throws InvalidRequestException if any of the minimal set of required properties for a MicroStrategyCube relationship are not found in the initial object
     */
    @Override
    public MicroStrategyCube trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all MicroStrategyCube assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) MicroStrategyCube assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all MicroStrategyCube assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all MicroStrategyCube assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) MicroStrategyCubes will be included
     * @return a fluent search that includes all MicroStrategyCube assets
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
     * Reference to a MicroStrategyCube by GUID. Use this to create a relationship to this MicroStrategyCube,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the MicroStrategyCube to reference
     * @return reference to a MicroStrategyCube that can be used for defining a relationship to a MicroStrategyCube
     */
    public static MicroStrategyCube refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a MicroStrategyCube by GUID. Use this to create a relationship to this MicroStrategyCube,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the MicroStrategyCube to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a MicroStrategyCube that can be used for defining a relationship to a MicroStrategyCube
     */
    public static MicroStrategyCube refByGuid(String guid, Reference.SaveSemantic semantic) {
        return MicroStrategyCube._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a MicroStrategyCube by qualifiedName. Use this to create a relationship to this MicroStrategyCube,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the MicroStrategyCube to reference
     * @return reference to a MicroStrategyCube that can be used for defining a relationship to a MicroStrategyCube
     */
    public static MicroStrategyCube refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a MicroStrategyCube by qualifiedName. Use this to create a relationship to this MicroStrategyCube,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the MicroStrategyCube to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a MicroStrategyCube that can be used for defining a relationship to a MicroStrategyCube
     */
    public static MicroStrategyCube refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return MicroStrategyCube._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a MicroStrategyCube by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the MicroStrategyCube to retrieve, either its GUID or its full qualifiedName
     * @return the requested full MicroStrategyCube, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyCube does not exist or the provided GUID is not a MicroStrategyCube
     */
    @JsonIgnore
    public static MicroStrategyCube get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a MicroStrategyCube by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the MicroStrategyCube to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full MicroStrategyCube, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyCube does not exist or the provided GUID is not a MicroStrategyCube
     */
    @JsonIgnore
    public static MicroStrategyCube get(AtlanClient client, String id, boolean includeRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof MicroStrategyCube) {
                return (MicroStrategyCube) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof MicroStrategyCube) {
                return (MicroStrategyCube) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) MicroStrategyCube to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the MicroStrategyCube
     * @return true if the MicroStrategyCube is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a MicroStrategyCube.
     *
     * @param qualifiedName of the MicroStrategyCube
     * @param name of the MicroStrategyCube
     * @return the minimal request necessary to update the MicroStrategyCube, as a builder
     */
    public static MicroStrategyCubeBuilder<?, ?> updater(String qualifiedName, String name) {
        return MicroStrategyCube._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a MicroStrategyCube, from a potentially
     * more-complete MicroStrategyCube object.
     *
     * @return the minimal object necessary to update the MicroStrategyCube, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for MicroStrategyCube are not found in the initial object
     */
    @Override
    public MicroStrategyCubeBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a MicroStrategyCube.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MicroStrategyCube
     * @param name of the MicroStrategyCube
     * @return the updated MicroStrategyCube, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyCube removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyCube) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a MicroStrategyCube.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MicroStrategyCube
     * @param name of the MicroStrategyCube
     * @return the updated MicroStrategyCube, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyCube removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyCube) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a MicroStrategyCube.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MicroStrategyCube's owners
     * @param qualifiedName of the MicroStrategyCube
     * @param name of the MicroStrategyCube
     * @return the updated MicroStrategyCube, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyCube removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyCube) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a MicroStrategyCube.
     *
     * @param client connectivity to the Atlan tenant on which to update the MicroStrategyCube's certificate
     * @param qualifiedName of the MicroStrategyCube
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MicroStrategyCube, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyCube updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (MicroStrategyCube)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MicroStrategyCube.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MicroStrategyCube's certificate
     * @param qualifiedName of the MicroStrategyCube
     * @param name of the MicroStrategyCube
     * @return the updated MicroStrategyCube, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyCube removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyCube) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a MicroStrategyCube.
     *
     * @param client connectivity to the Atlan tenant on which to update the MicroStrategyCube's announcement
     * @param qualifiedName of the MicroStrategyCube
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyCube updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (MicroStrategyCube)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MicroStrategyCube.
     *
     * @param client connectivity to the Atlan client from which to remove the MicroStrategyCube's announcement
     * @param qualifiedName of the MicroStrategyCube
     * @param name of the MicroStrategyCube
     * @return the updated MicroStrategyCube, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyCube removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyCube) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the MicroStrategyCube.
     *
     * @param client connectivity to the Atlan tenant on which to replace the MicroStrategyCube's assigned terms
     * @param qualifiedName for the MicroStrategyCube
     * @param name human-readable name of the MicroStrategyCube
     * @param terms the list of terms to replace on the MicroStrategyCube, or null to remove all terms from the MicroStrategyCube
     * @return the MicroStrategyCube that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyCube replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (MicroStrategyCube) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MicroStrategyCube, without replacing existing terms linked to the MicroStrategyCube.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyCube's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the MicroStrategyCube
     * @param qualifiedName for the MicroStrategyCube
     * @param terms the list of terms to append to the MicroStrategyCube
     * @return the MicroStrategyCube that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyCube appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyCube) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MicroStrategyCube, without replacing all existing terms linked to the MicroStrategyCube.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyCube's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the MicroStrategyCube
     * @param qualifiedName for the MicroStrategyCube
     * @param terms the list of terms to remove from the MicroStrategyCube, which must be referenced by GUID
     * @return the MicroStrategyCube that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyCube removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyCube) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a MicroStrategyCube, without replacing existing Atlan tags linked to the MicroStrategyCube.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyCube's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MicroStrategyCube
     * @param qualifiedName of the MicroStrategyCube
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyCube
     */
    public static MicroStrategyCube appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (MicroStrategyCube) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyCube, without replacing existing Atlan tags linked to the MicroStrategyCube.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyCube's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MicroStrategyCube
     * @param qualifiedName of the MicroStrategyCube
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyCube
     */
    public static MicroStrategyCube appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (MicroStrategyCube) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a MicroStrategyCube.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a MicroStrategyCube
     * @param qualifiedName of the MicroStrategyCube
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MicroStrategyCube
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
