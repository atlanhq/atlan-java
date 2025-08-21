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
 * Instance of a MicroStrategy metric in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class MicroStrategyMetric extends Asset
        implements IMicroStrategyMetric, IMicroStrategy, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MicroStrategyMetric";

    /** Fixed typeName for MicroStrategyMetrics. */
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

    /** List of simple names of attributes related to this metric. */
    @Attribute
    @Singular
    SortedSet<String> microStrategyAttributeNames;

    /** List of unique names of attributes related to this metric. */
    @Attribute
    @Singular
    SortedSet<String> microStrategyAttributeQualifiedNames;

    /** Attributes this metric uses. */
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

    /** Individual columns contained in the metric. */
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

    /** Cubes this metric uses. */
    @Attribute
    @Singular
    SortedSet<IMicroStrategyCube> microStrategyCubes;

    /** List of simple names of facts related to this metric. */
    @Attribute
    @Singular
    SortedSet<String> microStrategyFactNames;

    /** List of unique names of facts related to this metric. */
    @Attribute
    @Singular
    SortedSet<String> microStrategyFactQualifiedNames;

    /** Facts this metric uses. */
    @Attribute
    @Singular
    SortedSet<IMicroStrategyFact> microStrategyFacts;

    /** Whether the asset is certified in MicroStrategy (true) or not (false). */
    @Attribute
    Boolean microStrategyIsCertified;

    /** Location of this asset in MicroStrategy. */
    @Attribute
    @Singular("putMicroStrategyLocation")
    List<Map<String, String>> microStrategyLocation;

    /** Child metrics of this metric. */
    @Attribute
    @Singular
    SortedSet<IMicroStrategyMetric> microStrategyMetricChildren;

    /** Text specifiying this metric's expression. */
    @Attribute
    String microStrategyMetricExpression;

    /** List of simple names of parent metrics of this metric. */
    @Attribute
    @Singular
    SortedSet<String> microStrategyMetricParentNames;

    /** List of unique names of parent metrics of this metric. */
    @Attribute
    @Singular
    SortedSet<String> microStrategyMetricParentQualifiedNames;

    /** Parent metrics to this metric. */
    @Attribute
    @Singular
    SortedSet<IMicroStrategyMetric> microStrategyMetricParents;

    /** Project in which this metric exists. */
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

    /** Reports in which this metric is used. */
    @Attribute
    @Singular
    SortedSet<IMicroStrategyReport> microStrategyReports;

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
     * Builds the minimal object necessary to create a relationship to a MicroStrategyMetric, from a potentially
     * more-complete MicroStrategyMetric object.
     *
     * @return the minimal object necessary to relate to the MicroStrategyMetric
     * @throws InvalidRequestException if any of the minimal set of required properties for a MicroStrategyMetric relationship are not found in the initial object
     */
    @Override
    public MicroStrategyMetric trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all MicroStrategyMetric assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) MicroStrategyMetric assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all MicroStrategyMetric assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all MicroStrategyMetric assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) MicroStrategyMetrics will be included
     * @return a fluent search that includes all MicroStrategyMetric assets
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
     * Reference to a MicroStrategyMetric by GUID. Use this to create a relationship to this MicroStrategyMetric,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the MicroStrategyMetric to reference
     * @return reference to a MicroStrategyMetric that can be used for defining a relationship to a MicroStrategyMetric
     */
    public static MicroStrategyMetric refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a MicroStrategyMetric by GUID. Use this to create a relationship to this MicroStrategyMetric,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the MicroStrategyMetric to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a MicroStrategyMetric that can be used for defining a relationship to a MicroStrategyMetric
     */
    public static MicroStrategyMetric refByGuid(String guid, Reference.SaveSemantic semantic) {
        return MicroStrategyMetric._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a MicroStrategyMetric by qualifiedName. Use this to create a relationship to this MicroStrategyMetric,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the MicroStrategyMetric to reference
     * @return reference to a MicroStrategyMetric that can be used for defining a relationship to a MicroStrategyMetric
     */
    public static MicroStrategyMetric refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a MicroStrategyMetric by qualifiedName. Use this to create a relationship to this MicroStrategyMetric,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the MicroStrategyMetric to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a MicroStrategyMetric that can be used for defining a relationship to a MicroStrategyMetric
     */
    public static MicroStrategyMetric refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return MicroStrategyMetric._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a MicroStrategyMetric by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the MicroStrategyMetric to retrieve, either its GUID or its full qualifiedName
     * @return the requested full MicroStrategyMetric, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyMetric does not exist or the provided GUID is not a MicroStrategyMetric
     */
    @JsonIgnore
    public static MicroStrategyMetric get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a MicroStrategyMetric by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the MicroStrategyMetric to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full MicroStrategyMetric, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyMetric does not exist or the provided GUID is not a MicroStrategyMetric
     */
    @JsonIgnore
    public static MicroStrategyMetric get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof MicroStrategyMetric) {
                return (MicroStrategyMetric) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof MicroStrategyMetric) {
                return (MicroStrategyMetric) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a MicroStrategyMetric by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the MicroStrategyMetric to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the MicroStrategyMetric, including any relationships
     * @return the requested MicroStrategyMetric, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyMetric does not exist or the provided GUID is not a MicroStrategyMetric
     */
    @JsonIgnore
    public static MicroStrategyMetric get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a MicroStrategyMetric by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the MicroStrategyMetric to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the MicroStrategyMetric, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the MicroStrategyMetric
     * @return the requested MicroStrategyMetric, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyMetric does not exist or the provided GUID is not a MicroStrategyMetric
     */
    @JsonIgnore
    public static MicroStrategyMetric get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = MicroStrategyMetric.select(client)
                    .where(MicroStrategyMetric.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof MicroStrategyMetric) {
                return (MicroStrategyMetric) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = MicroStrategyMetric.select(client)
                    .where(MicroStrategyMetric.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof MicroStrategyMetric) {
                return (MicroStrategyMetric) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) MicroStrategyMetric to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the MicroStrategyMetric
     * @return true if the MicroStrategyMetric is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a MicroStrategyMetric.
     *
     * @param qualifiedName of the MicroStrategyMetric
     * @param name of the MicroStrategyMetric
     * @return the minimal request necessary to update the MicroStrategyMetric, as a builder
     */
    public static MicroStrategyMetricBuilder<?, ?> updater(String qualifiedName, String name) {
        return MicroStrategyMetric._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a MicroStrategyMetric, from a potentially
     * more-complete MicroStrategyMetric object.
     *
     * @return the minimal object necessary to update the MicroStrategyMetric, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for MicroStrategyMetric are not found in the initial object
     */
    @Override
    public MicroStrategyMetricBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class MicroStrategyMetricBuilder<
                    C extends MicroStrategyMetric, B extends MicroStrategyMetricBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a MicroStrategyMetric.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MicroStrategyMetric
     * @param name of the MicroStrategyMetric
     * @return the updated MicroStrategyMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyMetric removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyMetric) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a MicroStrategyMetric.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MicroStrategyMetric
     * @param name of the MicroStrategyMetric
     * @return the updated MicroStrategyMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyMetric removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyMetric) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a MicroStrategyMetric.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MicroStrategyMetric's owners
     * @param qualifiedName of the MicroStrategyMetric
     * @param name of the MicroStrategyMetric
     * @return the updated MicroStrategyMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyMetric removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyMetric) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a MicroStrategyMetric.
     *
     * @param client connectivity to the Atlan tenant on which to update the MicroStrategyMetric's certificate
     * @param qualifiedName of the MicroStrategyMetric
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MicroStrategyMetric, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyMetric updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (MicroStrategyMetric)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MicroStrategyMetric.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MicroStrategyMetric's certificate
     * @param qualifiedName of the MicroStrategyMetric
     * @param name of the MicroStrategyMetric
     * @return the updated MicroStrategyMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyMetric removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyMetric) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a MicroStrategyMetric.
     *
     * @param client connectivity to the Atlan tenant on which to update the MicroStrategyMetric's announcement
     * @param qualifiedName of the MicroStrategyMetric
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyMetric updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (MicroStrategyMetric)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MicroStrategyMetric.
     *
     * @param client connectivity to the Atlan client from which to remove the MicroStrategyMetric's announcement
     * @param qualifiedName of the MicroStrategyMetric
     * @param name of the MicroStrategyMetric
     * @return the updated MicroStrategyMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyMetric removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyMetric) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the MicroStrategyMetric.
     *
     * @param client connectivity to the Atlan tenant on which to replace the MicroStrategyMetric's assigned terms
     * @param qualifiedName for the MicroStrategyMetric
     * @param name human-readable name of the MicroStrategyMetric
     * @param terms the list of terms to replace on the MicroStrategyMetric, or null to remove all terms from the MicroStrategyMetric
     * @return the MicroStrategyMetric that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyMetric replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (MicroStrategyMetric) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MicroStrategyMetric, without replacing existing terms linked to the MicroStrategyMetric.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyMetric's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the MicroStrategyMetric
     * @param qualifiedName for the MicroStrategyMetric
     * @param terms the list of terms to append to the MicroStrategyMetric
     * @return the MicroStrategyMetric that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static MicroStrategyMetric appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyMetric) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MicroStrategyMetric, without replacing all existing terms linked to the MicroStrategyMetric.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyMetric's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the MicroStrategyMetric
     * @param qualifiedName for the MicroStrategyMetric
     * @param terms the list of terms to remove from the MicroStrategyMetric, which must be referenced by GUID
     * @return the MicroStrategyMetric that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static MicroStrategyMetric removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyMetric) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a MicroStrategyMetric, without replacing existing Atlan tags linked to the MicroStrategyMetric.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyMetric's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MicroStrategyMetric
     * @param qualifiedName of the MicroStrategyMetric
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyMetric
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static MicroStrategyMetric appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (MicroStrategyMetric) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyMetric, without replacing existing Atlan tags linked to the MicroStrategyMetric.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyMetric's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MicroStrategyMetric
     * @param qualifiedName of the MicroStrategyMetric
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyMetric
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static MicroStrategyMetric appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (MicroStrategyMetric) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a MicroStrategyMetric.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a MicroStrategyMetric
     * @param qualifiedName of the MicroStrategyMetric
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MicroStrategyMetric
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
