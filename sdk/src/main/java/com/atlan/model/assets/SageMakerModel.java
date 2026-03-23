/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AIModelVersionStage;
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
 * Instance of a SageMaker ML Model in Atlan. Represents trained machine learning models that can be deployed for inference.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class SageMakerModel extends Asset
        implements ISageMakerModel, IAIModelVersion, ISageMaker, IAI, IAWS, ICatalog, IAsset, IReferenceable, ICloud {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SageMakerModel";

    /** Fixed typeName for SageMakerModels. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Model containing the versions. */
    @Attribute
    IAIModel aiModel;

    /** Unique name of the AI model to which this version belongs, used to navigate from a version back to its parent model. */
    @Attribute
    String aiModelQualifiedName;

    /** Evaluation and performance metrics recorded for this AI model version, stored as key-value pairs (e.g. accuracy, F1 score, precision, recall). */
    @Attribute
    @Singular
    Map<String, String> aiModelVersionMetrics;

    /** Lifecycle deployment stage of this AI model version, indicating its readiness for production use. */
    @Attribute
    AIModelVersionStage aiModelVersionStage;

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

    /** Docker container image used for the model. */
    @Attribute
    String sageMakerModelContainerImage;

    /** Deployments (endpoints) of this SageMaker Model. */
    @Attribute
    @Singular
    SortedSet<ISageMakerModelDeployment> sageMakerModelDeployments;

    /** ARN of the IAM role used by the model for accessing AWS resources. */
    @Attribute
    String sageMakerModelExecutionRoleArn;

    /** SageMaker Model Group that contains the models. */
    @Attribute
    ISageMakerModelGroup sageMakerModelGroup;

    /** Name of the parent Model Group. */
    @Attribute
    String sageMakerModelModelGroupName;

    /** Qualified name of the parent Model Group. */
    @Attribute
    String sageMakerModelModelGroupQualifiedName;

    /** Status of the SageMaker Model Package (ACTIVE or INACTIVE). */
    @Attribute
    String sageMakerModelStatus;

    /** Version of the SageMaker Model Package. */
    @Attribute
    String sageMakerModelVersion;

    /** Primary S3 URI associated with this SageMaker asset. */
    @Attribute
    String sageMakerS3Uri;

    /**
     * Builds the minimal object necessary to create a relationship to a SageMakerModel, from a potentially
     * more-complete SageMakerModel object.
     *
     * @return the minimal object necessary to relate to the SageMakerModel
     * @throws InvalidRequestException if any of the minimal set of required properties for a SageMakerModel relationship are not found in the initial object
     */
    @Override
    public SageMakerModel trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SageMakerModel assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SageMakerModel assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SageMakerModel assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SageMakerModel assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SageMakerModels will be included
     * @return a fluent search that includes all SageMakerModel assets
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
     * Reference to a SageMakerModel by GUID. Use this to create a relationship to this SageMakerModel,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SageMakerModel to reference
     * @return reference to a SageMakerModel that can be used for defining a relationship to a SageMakerModel
     */
    public static SageMakerModel refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SageMakerModel by GUID. Use this to create a relationship to this SageMakerModel,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SageMakerModel to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SageMakerModel that can be used for defining a relationship to a SageMakerModel
     */
    public static SageMakerModel refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SageMakerModel._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SageMakerModel by qualifiedName. Use this to create a relationship to this SageMakerModel,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SageMakerModel to reference
     * @return reference to a SageMakerModel that can be used for defining a relationship to a SageMakerModel
     */
    public static SageMakerModel refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SageMakerModel by qualifiedName. Use this to create a relationship to this SageMakerModel,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SageMakerModel to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SageMakerModel that can be used for defining a relationship to a SageMakerModel
     */
    public static SageMakerModel refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SageMakerModel._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SageMakerModel by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SageMakerModel to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SageMakerModel, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SageMakerModel does not exist or the provided GUID is not a SageMakerModel
     */
    @JsonIgnore
    public static SageMakerModel get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SageMakerModel by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SageMakerModel to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SageMakerModel, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SageMakerModel does not exist or the provided GUID is not a SageMakerModel
     */
    @JsonIgnore
    public static SageMakerModel get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SageMakerModel) {
                return (SageMakerModel) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof SageMakerModel) {
                return (SageMakerModel) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SageMakerModel by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SageMakerModel to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SageMakerModel, including any relationships
     * @return the requested SageMakerModel, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SageMakerModel does not exist or the provided GUID is not a SageMakerModel
     */
    @JsonIgnore
    public static SageMakerModel get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a SageMakerModel by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SageMakerModel to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SageMakerModel, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the SageMakerModel
     * @return the requested SageMakerModel, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SageMakerModel does not exist or the provided GUID is not a SageMakerModel
     */
    @JsonIgnore
    public static SageMakerModel get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = SageMakerModel.select(client)
                    .where(SageMakerModel.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof SageMakerModel) {
                return (SageMakerModel) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = SageMakerModel.select(client)
                    .where(SageMakerModel.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof SageMakerModel) {
                return (SageMakerModel) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SageMakerModel to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SageMakerModel
     * @return true if the SageMakerModel is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SageMakerModel.
     *
     * @param qualifiedName of the SageMakerModel
     * @param name of the SageMakerModel
     * @return the minimal request necessary to update the SageMakerModel, as a builder
     */
    public static SageMakerModelBuilder<?, ?> updater(String qualifiedName, String name) {
        return SageMakerModel._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SageMakerModel,
     * from a potentially more-complete SageMakerModel object.
     *
     * @return the minimal object necessary to update the SageMakerModel, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a SageMakerModel are not present in the initial object
     */
    @Override
    public SageMakerModelBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class SageMakerModelBuilder<C extends SageMakerModel, B extends SageMakerModelBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a SageMakerModel.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SageMakerModel
     * @param name of the SageMakerModel
     * @return the updated SageMakerModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerModel removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SageMakerModel) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SageMakerModel.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SageMakerModel
     * @param name of the SageMakerModel
     * @return the updated SageMakerModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerModel removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SageMakerModel) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SageMakerModel.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SageMakerModel's owners
     * @param qualifiedName of the SageMakerModel
     * @param name of the SageMakerModel
     * @return the updated SageMakerModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerModel removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SageMakerModel) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SageMakerModel.
     *
     * @param client connectivity to the Atlan tenant on which to update the SageMakerModel's certificate
     * @param qualifiedName of the SageMakerModel
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SageMakerModel, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerModel updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SageMakerModel)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SageMakerModel.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SageMakerModel's certificate
     * @param qualifiedName of the SageMakerModel
     * @param name of the SageMakerModel
     * @return the updated SageMakerModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerModel removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SageMakerModel) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SageMakerModel.
     *
     * @param client connectivity to the Atlan tenant on which to update the SageMakerModel's announcement
     * @param qualifiedName of the SageMakerModel
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerModel updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SageMakerModel)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SageMakerModel.
     *
     * @param client connectivity to the Atlan client from which to remove the SageMakerModel's announcement
     * @param qualifiedName of the SageMakerModel
     * @param name of the SageMakerModel
     * @return the updated SageMakerModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerModel removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SageMakerModel) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SageMakerModel.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SageMakerModel's assigned terms
     * @param qualifiedName for the SageMakerModel
     * @param name human-readable name of the SageMakerModel
     * @param terms the list of terms to replace on the SageMakerModel, or null to remove all terms from the SageMakerModel
     * @return the SageMakerModel that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SageMakerModel replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SageMakerModel) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SageMakerModel, without replacing existing terms linked to the SageMakerModel.
     * Note: this operation must make two API calls — one to retrieve the SageMakerModel's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SageMakerModel
     * @param qualifiedName for the SageMakerModel
     * @param terms the list of terms to append to the SageMakerModel
     * @return the SageMakerModel that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SageMakerModel appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SageMakerModel) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SageMakerModel, without replacing all existing terms linked to the SageMakerModel.
     * Note: this operation must make two API calls — one to retrieve the SageMakerModel's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SageMakerModel
     * @param qualifiedName for the SageMakerModel
     * @param terms the list of terms to remove from the SageMakerModel, which must be referenced by GUID
     * @return the SageMakerModel that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SageMakerModel removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SageMakerModel) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SageMakerModel, without replacing existing Atlan tags linked to the SageMakerModel.
     * Note: this operation must make two API calls — one to retrieve the SageMakerModel's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SageMakerModel
     * @param qualifiedName of the SageMakerModel
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SageMakerModel
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static SageMakerModel appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SageMakerModel) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SageMakerModel, without replacing existing Atlan tags linked to the SageMakerModel.
     * Note: this operation must make two API calls — one to retrieve the SageMakerModel's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SageMakerModel
     * @param qualifiedName of the SageMakerModel
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SageMakerModel
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static SageMakerModel appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SageMakerModel) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SageMakerModel.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SageMakerModel
     * @param qualifiedName of the SageMakerModel
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SageMakerModel
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
