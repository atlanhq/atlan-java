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
import com.atlan.model.structs.SourceTagAttribute;
import com.atlan.serde.Serde;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
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
 * Instance of a dbt tag in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class DbtTag extends Asset implements IDbtTag, IDbt, ITag, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DbtTag";

    /** Fixed typeName for DbtTags. */
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

    /** Name of the classification in Atlan that is mapped to this tag. */
    @Attribute
    @JsonProperty("mappedClassificationName")
    String mappedAtlanTagName;

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

    /** Allowed values for the tag in the source system. These are denormalized from tagAttributes for ease of querying. */
    @Attribute
    @Singular
    SortedSet<String> tagAllowedValues;

    /** Attributes associated with the tag in the source system. */
    @Attribute
    @Singular
    List<SourceTagAttribute> tagAttributes;

    /** Unique identifier of the tag in the source system. */
    @Attribute
    String tagId;

    /**
     * Builds the minimal object necessary to create a relationship to a DbtTag, from a potentially
     * more-complete DbtTag object.
     *
     * @return the minimal object necessary to relate to the DbtTag
     * @throws InvalidRequestException if any of the minimal set of required properties for a DbtTag relationship are not found in the initial object
     */
    @Override
    public DbtTag trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all DbtTag assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DbtTag assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all DbtTag assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all DbtTag assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DbtTags will be included
     * @return a fluent search that includes all DbtTag assets
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
     * Reference to a DbtTag by GUID. Use this to create a relationship to this DbtTag,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the DbtTag to reference
     * @return reference to a DbtTag that can be used for defining a relationship to a DbtTag
     */
    public static DbtTag refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DbtTag by GUID. Use this to create a relationship to this DbtTag,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the DbtTag to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DbtTag that can be used for defining a relationship to a DbtTag
     */
    public static DbtTag refByGuid(String guid, Reference.SaveSemantic semantic) {
        return DbtTag._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a DbtTag by qualifiedName. Use this to create a relationship to this DbtTag,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the DbtTag to reference
     * @return reference to a DbtTag that can be used for defining a relationship to a DbtTag
     */
    public static DbtTag refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DbtTag by qualifiedName. Use this to create a relationship to this DbtTag,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the DbtTag to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DbtTag that can be used for defining a relationship to a DbtTag
     */
    public static DbtTag refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return DbtTag._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a DbtTag by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DbtTag to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DbtTag, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtTag does not exist or the provided GUID is not a DbtTag
     */
    @JsonIgnore
    public static DbtTag get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a DbtTag by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DbtTag to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full DbtTag, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtTag does not exist or the provided GUID is not a DbtTag
     */
    @JsonIgnore
    public static DbtTag get(AtlanClient client, String id, boolean includeAllRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof DbtTag) {
                return (DbtTag) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof DbtTag) {
                return (DbtTag) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a DbtTag by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DbtTag to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DbtTag, including any relationships
     * @return the requested DbtTag, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtTag does not exist or the provided GUID is not a DbtTag
     */
    @JsonIgnore
    public static DbtTag get(AtlanClient client, String id, Collection<AtlanField> attributes) throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a DbtTag by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DbtTag to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DbtTag, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the DbtTag
     * @return the requested DbtTag, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtTag does not exist or the provided GUID is not a DbtTag
     */
    @JsonIgnore
    public static DbtTag get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = DbtTag.select(client)
                    .where(DbtTag.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof DbtTag) {
                return (DbtTag) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = DbtTag.select(client)
                    .where(DbtTag.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof DbtTag) {
                return (DbtTag) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) DbtTag to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DbtTag
     * @return true if the DbtTag is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a DbtTag.
     *
     * @param name of the DbtTag
     * @param connectionQualifiedName unique name of the connection in which to create the DbtTag
     * @param mappedAtlanTagName the human-readable name of the Atlan tag to which this DbtTag should map
     * @param accountId the numeric ID of the dbt account in which the tag exists
     * @param projectId the numeric ID of the dbt project in which the tag exists
     * @param sourceId unique identifier for the tag in the source
     * @param allowedValues the values allowed to be set for this tag in the source
     * @return the minimal request necessary to create the DbtTag, as a builder
     */
    public static DbtTagBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String mappedAtlanTagName,
            String accountId,
            String projectId,
            String sourceId,
            List<String> allowedValues) {
        String allowedValuesString = "";
        try {
            allowedValuesString = Serde.allInclusiveMapper.writeValueAsString(allowedValues);
        } catch (JsonProcessingException e) {
            log.error("Unable to transform list of allowed values into singular string.", e);
        }
        return DbtTag._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName, accountId, projectId))
                .connectionQualifiedName(connectionQualifiedName)
                .mappedAtlanTagName(mappedAtlanTagName)
                .tagId(sourceId)
                .tagAttribute(SourceTagAttribute.builder()
                        .tagAttributeKey("allowedValues")
                        .tagAttributeValue(allowedValuesString)
                        .build())
                .tagAllowedValues(allowedValues);
    }

    /**
     * Builds the minimal object necessary to update a DbtTag.
     *
     * @param qualifiedName of the DbtTag
     * @param name of the DbtTag
     * @return the minimal request necessary to update the DbtTag, as a builder
     */
    public static DbtTagBuilder<?, ?> updater(String qualifiedName, String name) {
        return DbtTag._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique DbtTag name.
     *
     * @param name of the DbtTag
     * @param connectionQualifiedName unique name of the schema in which this DbtTag exists
     * @param accountId the numeric ID of the dbt account in which the tag exists
     * @param projectId the numeric ID of the dbt project in which the tag exists
     * @return a unique name for the DbtTag
     */
    public static String generateQualifiedName(
            String name, String connectionQualifiedName, String accountId, String projectId) {
        return connectionQualifiedName + "/account/" + accountId + "/project/" + projectId + "/tag/" + name;
    }

    /**
     * Builds the minimal object necessary to apply an update to a DbtTag, from a potentially
     * more-complete DbtTag object.
     *
     * @return the minimal object necessary to update the DbtTag, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DbtTag are not found in the initial object
     */
    @Override
    public DbtTagBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class DbtTagBuilder<C extends DbtTag, B extends DbtTagBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a DbtTag.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DbtTag
     * @param name of the DbtTag
     * @return the updated DbtTag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTag removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtTag) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DbtTag.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DbtTag
     * @param name of the DbtTag
     * @return the updated DbtTag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTag removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtTag) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DbtTag.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DbtTag's owners
     * @param qualifiedName of the DbtTag
     * @param name of the DbtTag
     * @return the updated DbtTag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTag removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (DbtTag) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DbtTag.
     *
     * @param client connectivity to the Atlan tenant on which to update the DbtTag's certificate
     * @param qualifiedName of the DbtTag
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DbtTag, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtTag updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DbtTag) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DbtTag.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DbtTag's certificate
     * @param qualifiedName of the DbtTag
     * @param name of the DbtTag
     * @return the updated DbtTag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTag removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtTag) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DbtTag.
     *
     * @param client connectivity to the Atlan tenant on which to update the DbtTag's announcement
     * @param qualifiedName of the DbtTag
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtTag updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (DbtTag) Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DbtTag.
     *
     * @param client connectivity to the Atlan client from which to remove the DbtTag's announcement
     * @param qualifiedName of the DbtTag
     * @param name of the DbtTag
     * @return the updated DbtTag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTag removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtTag) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DbtTag.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DbtTag's assigned terms
     * @param qualifiedName for the DbtTag
     * @param name human-readable name of the DbtTag
     * @param terms the list of terms to replace on the DbtTag, or null to remove all terms from the DbtTag
     * @return the DbtTag that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DbtTag replaceTerms(AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtTag) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DbtTag, without replacing existing terms linked to the DbtTag.
     * Note: this operation must make two API calls — one to retrieve the DbtTag's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DbtTag
     * @param qualifiedName for the DbtTag
     * @param terms the list of terms to append to the DbtTag
     * @return the DbtTag that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DbtTag appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtTag) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DbtTag, without replacing all existing terms linked to the DbtTag.
     * Note: this operation must make two API calls — one to retrieve the DbtTag's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DbtTag
     * @param qualifiedName for the DbtTag
     * @param terms the list of terms to remove from the DbtTag, which must be referenced by GUID
     * @return the DbtTag that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DbtTag removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtTag) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DbtTag, without replacing existing Atlan tags linked to the DbtTag.
     * Note: this operation must make two API calls — one to retrieve the DbtTag's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DbtTag
     * @param qualifiedName of the DbtTag
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DbtTag
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static DbtTag appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (DbtTag) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtTag, without replacing existing Atlan tags linked to the DbtTag.
     * Note: this operation must make two API calls — one to retrieve the DbtTag's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DbtTag
     * @param qualifiedName of the DbtTag
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DbtTag
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static DbtTag appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DbtTag) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a DbtTag.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DbtTag
     * @param qualifiedName of the DbtTag
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DbtTag
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
