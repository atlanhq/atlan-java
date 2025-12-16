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
 * Instance of a MicroStrategy dossier in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class MicroStrategyDossier extends Asset
        implements IMicroStrategyDossier, IMicroStrategy, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MicroStrategyDossier";

    /** Fixed typeName for MicroStrategyDossiers. */
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

    /** Time (epoch) this asset was certified in MicroStrategy, in milliseconds. */
    @Attribute
    @Date
    Long microStrategyCertifiedAt;

    /** User who certified this asset, in MicroStrategy. */
    @Attribute
    String microStrategyCertifiedBy;

    /** Individual columns contained in the dossier. */
    @Attribute
    @Singular
    SortedSet<IMicroStrategyColumn> microStrategyColumns;

    /** Simple names of the cubes related to this asset. */
    @Attribute
    @Singular
    SortedSet<String> microStrategyCubeNames;

    /** Unique names of the cubes related to this asset. */
    @Attribute
    @Singular
    SortedSet<String> microStrategyCubeQualifiedNames;

    /** List of chapter names in this dossier. */
    @Attribute
    @Singular
    SortedSet<String> microStrategyDossierChapterNames;

    /** Whether the asset is certified in MicroStrategy (true) or not (false). */
    @Attribute
    Boolean microStrategyIsCertified;

    /** Location of this asset in MicroStrategy. */
    @Attribute
    @Singular("putMicroStrategyLocation")
    List<Map<String, String>> microStrategyLocation;

    /** Project in which this dossier exists. */
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

    /** Visualizations that exist within this dossier. */
    @Attribute
    @Singular
    SortedSet<IMicroStrategyVisualization> microStrategyVisualizations;

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

    /** Partial fields contained in the asset. */
    @Attribute
    @Singular
    SortedSet<IPartialField> partialChildFields;

    /** Partial objects contained in the asset. */
    @Attribute
    @Singular
    SortedSet<IPartialObject> partialChildObjects;

    /**
     * Builds the minimal object necessary to create a relationship to a MicroStrategyDossier, from a potentially
     * more-complete MicroStrategyDossier object.
     *
     * @return the minimal object necessary to relate to the MicroStrategyDossier
     * @throws InvalidRequestException if any of the minimal set of required properties for a MicroStrategyDossier relationship are not found in the initial object
     */
    @Override
    public MicroStrategyDossier trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all MicroStrategyDossier assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) MicroStrategyDossier assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all MicroStrategyDossier assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all MicroStrategyDossier assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) MicroStrategyDossiers will be included
     * @return a fluent search that includes all MicroStrategyDossier assets
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
     * Reference to a MicroStrategyDossier by GUID. Use this to create a relationship to this MicroStrategyDossier,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the MicroStrategyDossier to reference
     * @return reference to a MicroStrategyDossier that can be used for defining a relationship to a MicroStrategyDossier
     */
    public static MicroStrategyDossier refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a MicroStrategyDossier by GUID. Use this to create a relationship to this MicroStrategyDossier,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the MicroStrategyDossier to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a MicroStrategyDossier that can be used for defining a relationship to a MicroStrategyDossier
     */
    public static MicroStrategyDossier refByGuid(String guid, Reference.SaveSemantic semantic) {
        return MicroStrategyDossier._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a MicroStrategyDossier by qualifiedName. Use this to create a relationship to this MicroStrategyDossier,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the MicroStrategyDossier to reference
     * @return reference to a MicroStrategyDossier that can be used for defining a relationship to a MicroStrategyDossier
     */
    public static MicroStrategyDossier refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a MicroStrategyDossier by qualifiedName. Use this to create a relationship to this MicroStrategyDossier,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the MicroStrategyDossier to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a MicroStrategyDossier that can be used for defining a relationship to a MicroStrategyDossier
     */
    public static MicroStrategyDossier refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return MicroStrategyDossier._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a MicroStrategyDossier by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the MicroStrategyDossier to retrieve, either its GUID or its full qualifiedName
     * @return the requested full MicroStrategyDossier, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyDossier does not exist or the provided GUID is not a MicroStrategyDossier
     */
    @JsonIgnore
    public static MicroStrategyDossier get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a MicroStrategyDossier by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the MicroStrategyDossier to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full MicroStrategyDossier, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyDossier does not exist or the provided GUID is not a MicroStrategyDossier
     */
    @JsonIgnore
    public static MicroStrategyDossier get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof MicroStrategyDossier) {
                return (MicroStrategyDossier) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof MicroStrategyDossier) {
                return (MicroStrategyDossier) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a MicroStrategyDossier by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the MicroStrategyDossier to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the MicroStrategyDossier, including any relationships
     * @return the requested MicroStrategyDossier, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyDossier does not exist or the provided GUID is not a MicroStrategyDossier
     */
    @JsonIgnore
    public static MicroStrategyDossier get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a MicroStrategyDossier by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the MicroStrategyDossier to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the MicroStrategyDossier, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the MicroStrategyDossier
     * @return the requested MicroStrategyDossier, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyDossier does not exist or the provided GUID is not a MicroStrategyDossier
     */
    @JsonIgnore
    public static MicroStrategyDossier get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = MicroStrategyDossier.select(client)
                    .where(MicroStrategyDossier.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof MicroStrategyDossier) {
                return (MicroStrategyDossier) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = MicroStrategyDossier.select(client)
                    .where(MicroStrategyDossier.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof MicroStrategyDossier) {
                return (MicroStrategyDossier) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) MicroStrategyDossier to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the MicroStrategyDossier
     * @return true if the MicroStrategyDossier is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a MicroStrategyDossier.
     *
     * @param qualifiedName of the MicroStrategyDossier
     * @param name of the MicroStrategyDossier
     * @return the minimal request necessary to update the MicroStrategyDossier, as a builder
     */
    public static MicroStrategyDossierBuilder<?, ?> updater(String qualifiedName, String name) {
        return MicroStrategyDossier._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a MicroStrategyDossier, from a potentially
     * more-complete MicroStrategyDossier object.
     *
     * @return the minimal object necessary to update the MicroStrategyDossier, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for MicroStrategyDossier are not found in the initial object
     */
    @Override
    public MicroStrategyDossierBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class MicroStrategyDossierBuilder<
                    C extends MicroStrategyDossier, B extends MicroStrategyDossierBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a MicroStrategyDossier.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MicroStrategyDossier
     * @param name of the MicroStrategyDossier
     * @return the updated MicroStrategyDossier, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDossier removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyDossier) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a MicroStrategyDossier.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MicroStrategyDossier
     * @param name of the MicroStrategyDossier
     * @return the updated MicroStrategyDossier, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDossier removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyDossier) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a MicroStrategyDossier.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MicroStrategyDossier's owners
     * @param qualifiedName of the MicroStrategyDossier
     * @param name of the MicroStrategyDossier
     * @return the updated MicroStrategyDossier, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDossier removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyDossier) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a MicroStrategyDossier.
     *
     * @param client connectivity to the Atlan tenant on which to update the MicroStrategyDossier's certificate
     * @param qualifiedName of the MicroStrategyDossier
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MicroStrategyDossier, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDossier updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (MicroStrategyDossier)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MicroStrategyDossier.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MicroStrategyDossier's certificate
     * @param qualifiedName of the MicroStrategyDossier
     * @param name of the MicroStrategyDossier
     * @return the updated MicroStrategyDossier, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDossier removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyDossier) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a MicroStrategyDossier.
     *
     * @param client connectivity to the Atlan tenant on which to update the MicroStrategyDossier's announcement
     * @param qualifiedName of the MicroStrategyDossier
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDossier updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (MicroStrategyDossier)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MicroStrategyDossier.
     *
     * @param client connectivity to the Atlan client from which to remove the MicroStrategyDossier's announcement
     * @param qualifiedName of the MicroStrategyDossier
     * @param name of the MicroStrategyDossier
     * @return the updated MicroStrategyDossier, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDossier removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyDossier) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the MicroStrategyDossier.
     *
     * @param client connectivity to the Atlan tenant on which to replace the MicroStrategyDossier's assigned terms
     * @param qualifiedName for the MicroStrategyDossier
     * @param name human-readable name of the MicroStrategyDossier
     * @param terms the list of terms to replace on the MicroStrategyDossier, or null to remove all terms from the MicroStrategyDossier
     * @return the MicroStrategyDossier that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDossier replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (MicroStrategyDossier) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MicroStrategyDossier, without replacing existing terms linked to the MicroStrategyDossier.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyDossier's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the MicroStrategyDossier
     * @param qualifiedName for the MicroStrategyDossier
     * @param terms the list of terms to append to the MicroStrategyDossier
     * @return the MicroStrategyDossier that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static MicroStrategyDossier appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyDossier) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MicroStrategyDossier, without replacing all existing terms linked to the MicroStrategyDossier.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyDossier's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the MicroStrategyDossier
     * @param qualifiedName for the MicroStrategyDossier
     * @param terms the list of terms to remove from the MicroStrategyDossier, which must be referenced by GUID
     * @return the MicroStrategyDossier that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static MicroStrategyDossier removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyDossier) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a MicroStrategyDossier, without replacing existing Atlan tags linked to the MicroStrategyDossier.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyDossier's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MicroStrategyDossier
     * @param qualifiedName of the MicroStrategyDossier
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyDossier
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static MicroStrategyDossier appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (MicroStrategyDossier) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyDossier, without replacing existing Atlan tags linked to the MicroStrategyDossier.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyDossier's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MicroStrategyDossier
     * @param qualifiedName of the MicroStrategyDossier
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyDossier
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static MicroStrategyDossier appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (MicroStrategyDossier) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a MicroStrategyDossier.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a MicroStrategyDossier
     * @param qualifiedName of the MicroStrategyDossier
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MicroStrategyDossier
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
