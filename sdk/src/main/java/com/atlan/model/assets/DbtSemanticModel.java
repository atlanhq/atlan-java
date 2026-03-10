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
import com.atlan.model.structs.DbtJobRun;
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
 * Instance of a dbt semantic model in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class DbtSemanticModel extends Asset
        implements IDbtSemanticModel, ISemanticModel, IDbt, ISemantic, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DbtSemanticModel";

    /** Fixed typeName for DbtSemanticModels. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Name of the account in which this asset exists in dbt. */
    @Attribute
    String dbtAccountName;

    /** Alias of this asset in dbt. */
    @Attribute
    String dbtAlias;

    /** Connection context for this asset in dbt. */
    @Attribute
    String dbtConnectionContext;

    /** Version of dbt used in the environment. */
    @Attribute
    String dbtEnvironmentDbtVersion;

    /** Name of the environment in which this asset exists in dbt. */
    @Attribute
    String dbtEnvironmentName;

    /** Time (epoch) at which the job that materialized this asset in dbt last ran, in milliseconds. */
    @Attribute
    @Date
    Long dbtJobLastRun;

    /** Name of the job that materialized this asset in dbt. */
    @Attribute
    String dbtJobName;

    /** Time (epoch) at which the job that materialized this asset in dbt will next run, in milliseconds. */
    @Attribute
    @Date
    Long dbtJobNextRun;

    /** Human-readable time at which the job that materialized this asset in dbt will next run. */
    @Attribute
    String dbtJobNextRunHumanized;

    /** List of latest dbt job runs across all environments. */
    @Attribute
    @Singular
    List<DbtJobRun> dbtJobRuns;

    /** Schedule of the job that materialized this asset in dbt. */
    @Attribute
    String dbtJobSchedule;

    /** Human-readable cron schedule of the job that materialized this asset in dbt. */
    @Attribute
    String dbtJobScheduleCronHumanized;

    /** Status of the job that materialized this asset in dbt. */
    @Attribute
    String dbtJobStatus;

    /** Metadata for this asset in dbt, specifically everything under the 'meta' key in the dbt object. */
    @Attribute
    String dbtMeta;

    /** Name of the package in which this asset exists in dbt. */
    @Attribute
    String dbtPackageName;

    /** Name of the project in which this asset exists in dbt. */
    @Attribute
    String dbtProjectName;

    /** URL of the semantic layer proxy for this asset in dbt. */
    @Attribute
    String dbtSemanticLayerProxyUrl;

    /** List of tags attached to this asset in dbt. */
    @Attribute
    @Singular
    SortedSet<String> dbtTags;

    /** Unique identifier of this asset in dbt. */
    @Attribute
    String dbtUniqueId;

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

    /** Partial fields contained in the asset. */
    @Attribute
    @Singular
    SortedSet<IPartialField> partialChildFields;

    /** Partial objects contained in the asset. */
    @Attribute
    @Singular
    SortedSet<IPartialObject> partialChildObjects;

    /**
     * Builds the minimal object necessary to create a relationship to a DbtSemanticModel, from a potentially
     * more-complete DbtSemanticModel object.
     *
     * @return the minimal object necessary to relate to the DbtSemanticModel
     * @throws InvalidRequestException if any of the minimal set of required properties for a DbtSemanticModel relationship are not found in the initial object
     */
    @Override
    public DbtSemanticModel trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all DbtSemanticModel assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DbtSemanticModel assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all DbtSemanticModel assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all DbtSemanticModel assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DbtSemanticModels will be included
     * @return a fluent search that includes all DbtSemanticModel assets
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
     * Reference to a DbtSemanticModel by GUID. Use this to create a relationship to this DbtSemanticModel,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the DbtSemanticModel to reference
     * @return reference to a DbtSemanticModel that can be used for defining a relationship to a DbtSemanticModel
     */
    public static DbtSemanticModel refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DbtSemanticModel by GUID. Use this to create a relationship to this DbtSemanticModel,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the DbtSemanticModel to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DbtSemanticModel that can be used for defining a relationship to a DbtSemanticModel
     */
    public static DbtSemanticModel refByGuid(String guid, Reference.SaveSemantic semantic) {
        return DbtSemanticModel._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a DbtSemanticModel by qualifiedName. Use this to create a relationship to this DbtSemanticModel,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the DbtSemanticModel to reference
     * @return reference to a DbtSemanticModel that can be used for defining a relationship to a DbtSemanticModel
     */
    public static DbtSemanticModel refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DbtSemanticModel by qualifiedName. Use this to create a relationship to this DbtSemanticModel,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the DbtSemanticModel to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DbtSemanticModel that can be used for defining a relationship to a DbtSemanticModel
     */
    public static DbtSemanticModel refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return DbtSemanticModel._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a DbtSemanticModel by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DbtSemanticModel to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DbtSemanticModel, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtSemanticModel does not exist or the provided GUID is not a DbtSemanticModel
     */
    @JsonIgnore
    public static DbtSemanticModel get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a DbtSemanticModel by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DbtSemanticModel to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full DbtSemanticModel, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtSemanticModel does not exist or the provided GUID is not a DbtSemanticModel
     */
    @JsonIgnore
    public static DbtSemanticModel get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof DbtSemanticModel) {
                return (DbtSemanticModel) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof DbtSemanticModel) {
                return (DbtSemanticModel) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a DbtSemanticModel by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DbtSemanticModel to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DbtSemanticModel, including any relationships
     * @return the requested DbtSemanticModel, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtSemanticModel does not exist or the provided GUID is not a DbtSemanticModel
     */
    @JsonIgnore
    public static DbtSemanticModel get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a DbtSemanticModel by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DbtSemanticModel to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DbtSemanticModel, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the DbtSemanticModel
     * @return the requested DbtSemanticModel, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtSemanticModel does not exist or the provided GUID is not a DbtSemanticModel
     */
    @JsonIgnore
    public static DbtSemanticModel get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = DbtSemanticModel.select(client)
                    .where(DbtSemanticModel.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof DbtSemanticModel) {
                return (DbtSemanticModel) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = DbtSemanticModel.select(client)
                    .where(DbtSemanticModel.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof DbtSemanticModel) {
                return (DbtSemanticModel) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) DbtSemanticModel to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DbtSemanticModel
     * @return true if the DbtSemanticModel is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a DbtSemanticModel.
     *
     * @param qualifiedName of the DbtSemanticModel
     * @param name of the DbtSemanticModel
     * @return the minimal request necessary to update the DbtSemanticModel, as a builder
     */
    public static DbtSemanticModelBuilder<?, ?> updater(String qualifiedName, String name) {
        return DbtSemanticModel._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DbtSemanticModel,
     * from a potentially more-complete DbtSemanticModel object.
     *
     * @return the minimal object necessary to update the DbtSemanticModel, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a DbtSemanticModel are not present in the initial object
     */
    @Override
    public DbtSemanticModelBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class DbtSemanticModelBuilder<
                    C extends DbtSemanticModel, B extends DbtSemanticModelBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a DbtSemanticModel.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DbtSemanticModel
     * @param name of the DbtSemanticModel
     * @return the updated DbtSemanticModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtSemanticModel removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtSemanticModel) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DbtSemanticModel.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DbtSemanticModel
     * @param name of the DbtSemanticModel
     * @return the updated DbtSemanticModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtSemanticModel removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtSemanticModel) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DbtSemanticModel.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DbtSemanticModel's owners
     * @param qualifiedName of the DbtSemanticModel
     * @param name of the DbtSemanticModel
     * @return the updated DbtSemanticModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtSemanticModel removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtSemanticModel) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DbtSemanticModel.
     *
     * @param client connectivity to the Atlan tenant on which to update the DbtSemanticModel's certificate
     * @param qualifiedName of the DbtSemanticModel
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DbtSemanticModel, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtSemanticModel updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DbtSemanticModel)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DbtSemanticModel.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DbtSemanticModel's certificate
     * @param qualifiedName of the DbtSemanticModel
     * @param name of the DbtSemanticModel
     * @return the updated DbtSemanticModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtSemanticModel removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtSemanticModel) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DbtSemanticModel.
     *
     * @param client connectivity to the Atlan tenant on which to update the DbtSemanticModel's announcement
     * @param qualifiedName of the DbtSemanticModel
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtSemanticModel updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (DbtSemanticModel)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DbtSemanticModel.
     *
     * @param client connectivity to the Atlan client from which to remove the DbtSemanticModel's announcement
     * @param qualifiedName of the DbtSemanticModel
     * @param name of the DbtSemanticModel
     * @return the updated DbtSemanticModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtSemanticModel removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtSemanticModel) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DbtSemanticModel.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DbtSemanticModel's assigned terms
     * @param qualifiedName for the DbtSemanticModel
     * @param name human-readable name of the DbtSemanticModel
     * @param terms the list of terms to replace on the DbtSemanticModel, or null to remove all terms from the DbtSemanticModel
     * @return the DbtSemanticModel that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DbtSemanticModel replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (DbtSemanticModel) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DbtSemanticModel, without replacing existing terms linked to the DbtSemanticModel.
     * Note: this operation must make two API calls — one to retrieve the DbtSemanticModel's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DbtSemanticModel
     * @param qualifiedName for the DbtSemanticModel
     * @param terms the list of terms to append to the DbtSemanticModel
     * @return the DbtSemanticModel that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DbtSemanticModel appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtSemanticModel) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DbtSemanticModel, without replacing all existing terms linked to the DbtSemanticModel.
     * Note: this operation must make two API calls — one to retrieve the DbtSemanticModel's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DbtSemanticModel
     * @param qualifiedName for the DbtSemanticModel
     * @param terms the list of terms to remove from the DbtSemanticModel, which must be referenced by GUID
     * @return the DbtSemanticModel that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DbtSemanticModel removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtSemanticModel) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DbtSemanticModel, without replacing existing Atlan tags linked to the DbtSemanticModel.
     * Note: this operation must make two API calls — one to retrieve the DbtSemanticModel's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DbtSemanticModel
     * @param qualifiedName of the DbtSemanticModel
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DbtSemanticModel
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static DbtSemanticModel appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (DbtSemanticModel) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtSemanticModel, without replacing existing Atlan tags linked to the DbtSemanticModel.
     * Note: this operation must make two API calls — one to retrieve the DbtSemanticModel's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DbtSemanticModel
     * @param qualifiedName of the DbtSemanticModel
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DbtSemanticModel
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static DbtSemanticModel appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DbtSemanticModel) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a DbtSemanticModel.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DbtSemanticModel
     * @param qualifiedName of the DbtSemanticModel
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DbtSemanticModel
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
