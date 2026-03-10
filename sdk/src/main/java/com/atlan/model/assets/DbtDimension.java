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
 * Instance of a dbt semantic dimension in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class DbtDimension extends Asset
        implements IDbtDimension, ISemanticDimension, IDbt, ISemantic, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DbtDimension";

    /** Fixed typeName for DbtDimensions. */
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

    /** Time granularity for time dimensions only (day/week/month/quarter/year). */
    @Attribute
    String dbtSemanticFieldTimeGranularity;

    /** URL of the semantic layer proxy for this asset in dbt. */
    @Attribute
    String dbtSemanticLayerProxyUrl;

    /** Qualified name of the dbt semantic model this dimension belongs to. */
    @Attribute
    String dbtSemanticModelQualifiedName;

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

    /** Access level for the semantic field (e.g., public_access/private_access). */
    @Attribute
    String semanticAccessModifier;

    /** Data type of the semantic field. */
    @Attribute
    String semanticDataType;

    /** Column name or SQL expression for the semantic field. */
    @Attribute
    String semanticExpression;

    /** Labels associated with the semantic field. */
    @Attribute
    @Singular
    SortedSet<String> semanticLabels;

    /** Sample values for the semantic field. */
    @Attribute
    @Singular
    SortedSet<String> semanticSampleValues;

    /** Alternative names or terms for the semantic field. */
    @Attribute
    @Singular
    SortedSet<String> semanticSynonyms;

    /** Detailed type of the semantic field (e.g., type of measure, type of dimension, or type of entity). */
    @Attribute
    String semanticType;

    /**
     * Builds the minimal object necessary to create a relationship to a DbtDimension, from a potentially
     * more-complete DbtDimension object.
     *
     * @return the minimal object necessary to relate to the DbtDimension
     * @throws InvalidRequestException if any of the minimal set of required properties for a DbtDimension relationship are not found in the initial object
     */
    @Override
    public DbtDimension trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all DbtDimension assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DbtDimension assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all DbtDimension assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all DbtDimension assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DbtDimensions will be included
     * @return a fluent search that includes all DbtDimension assets
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
     * Reference to a DbtDimension by GUID. Use this to create a relationship to this DbtDimension,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the DbtDimension to reference
     * @return reference to a DbtDimension that can be used for defining a relationship to a DbtDimension
     */
    public static DbtDimension refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DbtDimension by GUID. Use this to create a relationship to this DbtDimension,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the DbtDimension to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DbtDimension that can be used for defining a relationship to a DbtDimension
     */
    public static DbtDimension refByGuid(String guid, Reference.SaveSemantic semantic) {
        return DbtDimension._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a DbtDimension by qualifiedName. Use this to create a relationship to this DbtDimension,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the DbtDimension to reference
     * @return reference to a DbtDimension that can be used for defining a relationship to a DbtDimension
     */
    public static DbtDimension refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DbtDimension by qualifiedName. Use this to create a relationship to this DbtDimension,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the DbtDimension to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DbtDimension that can be used for defining a relationship to a DbtDimension
     */
    public static DbtDimension refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return DbtDimension._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a DbtDimension by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DbtDimension to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DbtDimension, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtDimension does not exist or the provided GUID is not a DbtDimension
     */
    @JsonIgnore
    public static DbtDimension get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a DbtDimension by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DbtDimension to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full DbtDimension, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtDimension does not exist or the provided GUID is not a DbtDimension
     */
    @JsonIgnore
    public static DbtDimension get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof DbtDimension) {
                return (DbtDimension) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof DbtDimension) {
                return (DbtDimension) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a DbtDimension by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DbtDimension to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DbtDimension, including any relationships
     * @return the requested DbtDimension, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtDimension does not exist or the provided GUID is not a DbtDimension
     */
    @JsonIgnore
    public static DbtDimension get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a DbtDimension by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DbtDimension to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DbtDimension, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the DbtDimension
     * @return the requested DbtDimension, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtDimension does not exist or the provided GUID is not a DbtDimension
     */
    @JsonIgnore
    public static DbtDimension get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = DbtDimension.select(client)
                    .where(DbtDimension.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof DbtDimension) {
                return (DbtDimension) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = DbtDimension.select(client)
                    .where(DbtDimension.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof DbtDimension) {
                return (DbtDimension) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) DbtDimension to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DbtDimension
     * @return true if the DbtDimension is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a DbtDimension.
     *
     * @param qualifiedName of the DbtDimension
     * @param name of the DbtDimension
     * @return the minimal request necessary to update the DbtDimension, as a builder
     */
    public static DbtDimensionBuilder<?, ?> updater(String qualifiedName, String name) {
        return DbtDimension._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DbtDimension,
     * from a potentially more-complete DbtDimension object.
     *
     * @return the minimal object necessary to update the DbtDimension, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a DbtDimension are not present in the initial object
     */
    @Override
    public DbtDimensionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class DbtDimensionBuilder<C extends DbtDimension, B extends DbtDimensionBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a DbtDimension.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DbtDimension
     * @param name of the DbtDimension
     * @return the updated DbtDimension, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtDimension removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtDimension) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DbtDimension.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DbtDimension
     * @param name of the DbtDimension
     * @return the updated DbtDimension, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtDimension removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtDimension) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DbtDimension.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DbtDimension's owners
     * @param qualifiedName of the DbtDimension
     * @param name of the DbtDimension
     * @return the updated DbtDimension, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtDimension removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtDimension) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DbtDimension.
     *
     * @param client connectivity to the Atlan tenant on which to update the DbtDimension's certificate
     * @param qualifiedName of the DbtDimension
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DbtDimension, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtDimension updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DbtDimension)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DbtDimension.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DbtDimension's certificate
     * @param qualifiedName of the DbtDimension
     * @param name of the DbtDimension
     * @return the updated DbtDimension, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtDimension removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtDimension) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DbtDimension.
     *
     * @param client connectivity to the Atlan tenant on which to update the DbtDimension's announcement
     * @param qualifiedName of the DbtDimension
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtDimension updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (DbtDimension)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DbtDimension.
     *
     * @param client connectivity to the Atlan client from which to remove the DbtDimension's announcement
     * @param qualifiedName of the DbtDimension
     * @param name of the DbtDimension
     * @return the updated DbtDimension, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtDimension removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtDimension) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DbtDimension.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DbtDimension's assigned terms
     * @param qualifiedName for the DbtDimension
     * @param name human-readable name of the DbtDimension
     * @param terms the list of terms to replace on the DbtDimension, or null to remove all terms from the DbtDimension
     * @return the DbtDimension that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DbtDimension replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (DbtDimension) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DbtDimension, without replacing existing terms linked to the DbtDimension.
     * Note: this operation must make two API calls — one to retrieve the DbtDimension's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DbtDimension
     * @param qualifiedName for the DbtDimension
     * @param terms the list of terms to append to the DbtDimension
     * @return the DbtDimension that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DbtDimension appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtDimension) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DbtDimension, without replacing all existing terms linked to the DbtDimension.
     * Note: this operation must make two API calls — one to retrieve the DbtDimension's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DbtDimension
     * @param qualifiedName for the DbtDimension
     * @param terms the list of terms to remove from the DbtDimension, which must be referenced by GUID
     * @return the DbtDimension that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DbtDimension removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtDimension) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DbtDimension, without replacing existing Atlan tags linked to the DbtDimension.
     * Note: this operation must make two API calls — one to retrieve the DbtDimension's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DbtDimension
     * @param qualifiedName of the DbtDimension
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DbtDimension
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static DbtDimension appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (DbtDimension) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtDimension, without replacing existing Atlan tags linked to the DbtDimension.
     * Note: this operation must make two API calls — one to retrieve the DbtDimension's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DbtDimension
     * @param qualifiedName of the DbtDimension
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DbtDimension
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static DbtDimension appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DbtDimension) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a DbtDimension.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DbtDimension
     * @param qualifiedName of the DbtDimension
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DbtDimension
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
