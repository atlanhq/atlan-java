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
import com.atlan.model.enums.EthicalAIAccountabilityConfig;
import com.atlan.model.enums.EthicalAIBiasMitigationConfig;
import com.atlan.model.enums.EthicalAIEnvironmentalConsciousnessConfig;
import com.atlan.model.enums.EthicalAIFairnessConfig;
import com.atlan.model.enums.EthicalAIPrivacyConfig;
import com.atlan.model.enums.EthicalAIReliabilityAndSafetyConfig;
import com.atlan.model.enums.EthicalAITransparencyConfig;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.model.structs.AwsTag;
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
 * Instance of a SageMaker Feature Store Feature Group in Atlan. Represents a collection of related features that can be used for machine learning training and inference.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class SageMakerFeatureGroup extends Asset implements ISageMakerFeatureGroup, ISageMaker, IAI, IAWS, ICatalog, IAsset, IReferenceable, ICloud {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SageMakerFeatureGroup";

    /** Fixed typeName for SageMakerFeatureGroups. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** 12-digit number that uniquely identifies an AWS account. */
    @Attribute
    String awsAccountId;

    /** DEPRECATED: This legacy attribute must be unique across all AWS asset instances. This can create non-obvious edge cases for creating / updating assets, and we therefore recommended NOT using it. See and use cloudResourceName instead. */
    @Attribute
    String awsArn;

    /** Root user's ID. */
    @Attribute
    String awsOwnerId;

    /** Root user's name. */
    @Attribute
    String awsOwnerName;

    /** Group of AWS region and service objects. */
    @Attribute
    String awsPartition;

    /** Physical region where the data center in which the asset exists is clustered. */
    @Attribute
    String awsRegion;

    /** Unique resource ID assigned when a new resource is created. */
    @Attribute
    String awsResourceId;

    /** Type of service in which the asset exists. */
    @Attribute
    String awsService;

    /** List of tags that have been applied to the asset in AWS. */
    @Attribute
    @Singular
    List<AwsTag> awsTags;

    /** Uniform resource name (URN) for the asset: AWS ARN, Google Cloud URI, Azure resource ID, Oracle OCID, and so on. */
    @Attribute
    String cloudUniformResourceName;

    /** Accountability configuration for ensuring the ethical use of an AI asset */
    @Attribute
    EthicalAIAccountabilityConfig ethicalAIAccountabilityConfig;

    /** Bias mitigation configuration for ensuring the ethical use of an AI asset */
    @Attribute
    EthicalAIBiasMitigationConfig ethicalAIBiasMitigationConfig;

    /** Environmental consciousness configuration for ensuring the ethical use of an AI asset */
    @Attribute
    EthicalAIEnvironmentalConsciousnessConfig ethicalAIEnvironmentalConsciousnessConfig;

    /** Fairness configuration for ensuring the ethical use of an AI asset */
    @Attribute
    EthicalAIFairnessConfig ethicalAIFairnessConfig;

    /** Privacy configuration for ensuring the ethical use of an AI asset */
    @Attribute
    EthicalAIPrivacyConfig ethicalAIPrivacyConfig;

    /** Reliability and safety configuration for ensuring the ethical use of an AI asset */
    @Attribute
    EthicalAIReliabilityAndSafetyConfig ethicalAIReliabilityAndSafetyConfig;

    /** Transparency configuration for ensuring the ethical use of an AI asset */
    @Attribute
    EthicalAITransparencyConfig ethicalAITransparencyConfig;

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

    /** Number of features in this Feature Group. */
    @Attribute
    Long sageMakerFeatureGroupFeatureCount;

    /** AWS Glue database name associated with this Feature Group. */
    @Attribute
    String sageMakerFeatureGroupGlueDatabaseName;

    /** AWS Glue table name associated with this Feature Group. */
    @Attribute
    String sageMakerFeatureGroupGlueTableName;

    /** Name of the feature that serves as the record identifier. */
    @Attribute
    String sageMakerFeatureGroupRecordIdName;

    /** Current status of the Feature Group (e.g., Created, Creating, Failed). */
    @Attribute
    String sageMakerFeatureGroupStatus;

    /** Features that are defined within the SageMaker Feature Group. */
    @Attribute
    @Singular
    SortedSet<ISageMakerFeature> sageMakerFeatures;

    /** Primary S3 URI associated with this SageMaker asset. */
    @Attribute
    String sageMakerS3Uri;

    /**
     * Builds the minimal object necessary to create a relationship to a SageMakerFeatureGroup, from a potentially
     * more-complete SageMakerFeatureGroup object.
     *
     * @return the minimal object necessary to relate to the SageMakerFeatureGroup
     * @throws InvalidRequestException if any of the minimal set of required properties for a SageMakerFeatureGroup relationship are not found in the initial object
     */
    @Override
    public SageMakerFeatureGroup trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SageMakerFeatureGroup assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SageMakerFeatureGroup assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SageMakerFeatureGroup assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SageMakerFeatureGroup assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SageMakerFeatureGroups will be included
     * @return a fluent search that includes all SageMakerFeatureGroup assets
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
     * Reference to a SageMakerFeatureGroup by GUID. Use this to create a relationship to this SageMakerFeatureGroup,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SageMakerFeatureGroup to reference
     * @return reference to a SageMakerFeatureGroup that can be used for defining a relationship to a SageMakerFeatureGroup
     */
    public static SageMakerFeatureGroup refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SageMakerFeatureGroup by GUID. Use this to create a relationship to this SageMakerFeatureGroup,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SageMakerFeatureGroup to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SageMakerFeatureGroup that can be used for defining a relationship to a SageMakerFeatureGroup
     */
    public static SageMakerFeatureGroup refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SageMakerFeatureGroup._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SageMakerFeatureGroup by qualifiedName. Use this to create a relationship to this SageMakerFeatureGroup,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SageMakerFeatureGroup to reference
     * @return reference to a SageMakerFeatureGroup that can be used for defining a relationship to a SageMakerFeatureGroup
     */
    public static SageMakerFeatureGroup refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SageMakerFeatureGroup by qualifiedName. Use this to create a relationship to this SageMakerFeatureGroup,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SageMakerFeatureGroup to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SageMakerFeatureGroup that can be used for defining a relationship to a SageMakerFeatureGroup
     */
    public static SageMakerFeatureGroup refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SageMakerFeatureGroup._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SageMakerFeatureGroup by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SageMakerFeatureGroup to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SageMakerFeatureGroup, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SageMakerFeatureGroup does not exist or the provided GUID is not a SageMakerFeatureGroup
     */
    @JsonIgnore
    public static SageMakerFeatureGroup get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SageMakerFeatureGroup by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SageMakerFeatureGroup to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SageMakerFeatureGroup, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SageMakerFeatureGroup does not exist or the provided GUID is not a SageMakerFeatureGroup
     */
    @JsonIgnore
    public static SageMakerFeatureGroup get(AtlanClient client, String id, boolean includeAllRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SageMakerFeatureGroup) {
                return (SageMakerFeatureGroup) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof SageMakerFeatureGroup) {
                return (SageMakerFeatureGroup) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SageMakerFeatureGroup by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SageMakerFeatureGroup to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SageMakerFeatureGroup, including any relationships
     * @return the requested SageMakerFeatureGroup, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SageMakerFeatureGroup does not exist or the provided GUID is not a SageMakerFeatureGroup
     */
    @JsonIgnore
    public static SageMakerFeatureGroup get(AtlanClient client, String id, Collection<AtlanField> attributes) throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a SageMakerFeatureGroup by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SageMakerFeatureGroup to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SageMakerFeatureGroup, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the SageMakerFeatureGroup
     * @return the requested SageMakerFeatureGroup, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SageMakerFeatureGroup does not exist or the provided GUID is not a SageMakerFeatureGroup
     */
    @JsonIgnore
    public static SageMakerFeatureGroup get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = SageMakerFeatureGroup.select(client)
                    .where(SageMakerFeatureGroup.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof SageMakerFeatureGroup) {
                return (SageMakerFeatureGroup) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = SageMakerFeatureGroup.select(client)
                    .where(SageMakerFeatureGroup.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof SageMakerFeatureGroup) {
                return (SageMakerFeatureGroup) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SageMakerFeatureGroup to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SageMakerFeatureGroup
     * @return true if the SageMakerFeatureGroup is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SageMakerFeatureGroup.
     *
     * @param qualifiedName of the SageMakerFeatureGroup
     * @param name of the SageMakerFeatureGroup
     * @return the minimal request necessary to update the SageMakerFeatureGroup, as a builder
     */
    public static SageMakerFeatureGroupBuilder<?, ?> updater(String qualifiedName, String name) {
        return SageMakerFeatureGroup._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SageMakerFeatureGroup,
     * from a potentially more-complete SageMakerFeatureGroup object.
     *
     * @return the minimal object necessary to update the SageMakerFeatureGroup, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a SageMakerFeatureGroup are not present in the initial object
     */
    @Override
    public SageMakerFeatureGroupBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class SageMakerFeatureGroupBuilder<C extends SageMakerFeatureGroup, B extends SageMakerFeatureGroupBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a SageMakerFeatureGroup.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SageMakerFeatureGroup
     * @param name of the SageMakerFeatureGroup
     * @return the updated SageMakerFeatureGroup, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerFeatureGroup removeDescription(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (SageMakerFeatureGroup) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SageMakerFeatureGroup.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SageMakerFeatureGroup
     * @param name of the SageMakerFeatureGroup
     * @return the updated SageMakerFeatureGroup, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerFeatureGroup removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SageMakerFeatureGroup) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SageMakerFeatureGroup.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SageMakerFeatureGroup's owners
     * @param qualifiedName of the SageMakerFeatureGroup
     * @param name of the SageMakerFeatureGroup
     * @return the updated SageMakerFeatureGroup, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerFeatureGroup removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (SageMakerFeatureGroup) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SageMakerFeatureGroup.
     *
     * @param client connectivity to the Atlan tenant on which to update the SageMakerFeatureGroup's certificate
     * @param qualifiedName of the SageMakerFeatureGroup
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SageMakerFeatureGroup, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerFeatureGroup updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SageMakerFeatureGroup) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SageMakerFeatureGroup.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SageMakerFeatureGroup's certificate
     * @param qualifiedName of the SageMakerFeatureGroup
     * @param name of the SageMakerFeatureGroup
     * @return the updated SageMakerFeatureGroup, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerFeatureGroup removeCertificate(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (SageMakerFeatureGroup) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SageMakerFeatureGroup.
     *
     * @param client connectivity to the Atlan tenant on which to update the SageMakerFeatureGroup's announcement
     * @param qualifiedName of the SageMakerFeatureGroup
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerFeatureGroup updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SageMakerFeatureGroup) Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SageMakerFeatureGroup.
     *
     * @param client connectivity to the Atlan client from which to remove the SageMakerFeatureGroup's announcement
     * @param qualifiedName of the SageMakerFeatureGroup
     * @param name of the SageMakerFeatureGroup
     * @return the updated SageMakerFeatureGroup, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerFeatureGroup removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SageMakerFeatureGroup) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SageMakerFeatureGroup.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SageMakerFeatureGroup's assigned terms
     * @param qualifiedName for the SageMakerFeatureGroup
     * @param name human-readable name of the SageMakerFeatureGroup
     * @param terms the list of terms to replace on the SageMakerFeatureGroup, or null to remove all terms from the SageMakerFeatureGroup
     * @return the SageMakerFeatureGroup that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SageMakerFeatureGroup replaceTerms(AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SageMakerFeatureGroup) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SageMakerFeatureGroup, without replacing existing terms linked to the SageMakerFeatureGroup.
     * Note: this operation must make two API calls — one to retrieve the SageMakerFeatureGroup's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SageMakerFeatureGroup
     * @param qualifiedName for the SageMakerFeatureGroup
     * @param terms the list of terms to append to the SageMakerFeatureGroup
     * @return the SageMakerFeatureGroup that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SageMakerFeatureGroup appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SageMakerFeatureGroup) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SageMakerFeatureGroup, without replacing all existing terms linked to the SageMakerFeatureGroup.
     * Note: this operation must make two API calls — one to retrieve the SageMakerFeatureGroup's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SageMakerFeatureGroup
     * @param qualifiedName for the SageMakerFeatureGroup
     * @param terms the list of terms to remove from the SageMakerFeatureGroup, which must be referenced by GUID
     * @return the SageMakerFeatureGroup that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SageMakerFeatureGroup removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SageMakerFeatureGroup) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SageMakerFeatureGroup, without replacing existing Atlan tags linked to the SageMakerFeatureGroup.
     * Note: this operation must make two API calls — one to retrieve the SageMakerFeatureGroup's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SageMakerFeatureGroup
     * @param qualifiedName of the SageMakerFeatureGroup
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SageMakerFeatureGroup
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static SageMakerFeatureGroup appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SageMakerFeatureGroup) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SageMakerFeatureGroup, without replacing existing Atlan tags linked to the SageMakerFeatureGroup.
     * Note: this operation must make two API calls — one to retrieve the SageMakerFeatureGroup's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SageMakerFeatureGroup
     * @param qualifiedName of the SageMakerFeatureGroup
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SageMakerFeatureGroup
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static SageMakerFeatureGroup appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SageMakerFeatureGroup) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SageMakerFeatureGroup.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SageMakerFeatureGroup
     * @param qualifiedName of the SageMakerFeatureGroup
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SageMakerFeatureGroup
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}