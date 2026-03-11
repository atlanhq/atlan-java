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
 * Instance of a SageMaker Endpoint in Atlan. Represents deployed models that can serve real-time inference requests.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class SageMakerModelDeployment extends Asset
        implements ISageMakerModelDeployment, ISageMaker, IAI, IAWS, ICatalog, IAsset, IReferenceable, ICloud {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SageMakerModelDeployment";

    /** Fixed typeName for SageMakerModelDeployments. */
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

    /** SageMaker Model that is deployed. */
    @Attribute
    ISageMakerModel sageMakerModel;

    /** Name of the endpoint configuration used by this deployment. */
    @Attribute
    String sageMakerModelDeploymentEndpointConfigName;

    /** Name of the parent Model. */
    @Attribute
    String sageMakerModelDeploymentModelName;

    /** Qualified name of the parent Model. */
    @Attribute
    String sageMakerModelDeploymentModelQualifiedName;

    /** Current status of the endpoint (e.g., InService, OutOfService, Creating, Failed). */
    @Attribute
    String sageMakerModelDeploymentStatus;

    /** Primary S3 URI associated with this SageMaker asset. */
    @Attribute
    String sageMakerS3Uri;

    /**
     * Builds the minimal object necessary to create a relationship to a SageMakerModelDeployment, from a potentially
     * more-complete SageMakerModelDeployment object.
     *
     * @return the minimal object necessary to relate to the SageMakerModelDeployment
     * @throws InvalidRequestException if any of the minimal set of required properties for a SageMakerModelDeployment relationship are not found in the initial object
     */
    @Override
    public SageMakerModelDeployment trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SageMakerModelDeployment assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SageMakerModelDeployment assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SageMakerModelDeployment assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SageMakerModelDeployment assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SageMakerModelDeployments will be included
     * @return a fluent search that includes all SageMakerModelDeployment assets
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
     * Reference to a SageMakerModelDeployment by GUID. Use this to create a relationship to this SageMakerModelDeployment,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SageMakerModelDeployment to reference
     * @return reference to a SageMakerModelDeployment that can be used for defining a relationship to a SageMakerModelDeployment
     */
    public static SageMakerModelDeployment refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SageMakerModelDeployment by GUID. Use this to create a relationship to this SageMakerModelDeployment,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SageMakerModelDeployment to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SageMakerModelDeployment that can be used for defining a relationship to a SageMakerModelDeployment
     */
    public static SageMakerModelDeployment refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SageMakerModelDeployment._internal()
                .guid(guid)
                .semantic(semantic)
                .build();
    }

    /**
     * Reference to a SageMakerModelDeployment by qualifiedName. Use this to create a relationship to this SageMakerModelDeployment,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SageMakerModelDeployment to reference
     * @return reference to a SageMakerModelDeployment that can be used for defining a relationship to a SageMakerModelDeployment
     */
    public static SageMakerModelDeployment refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SageMakerModelDeployment by qualifiedName. Use this to create a relationship to this SageMakerModelDeployment,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SageMakerModelDeployment to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SageMakerModelDeployment that can be used for defining a relationship to a SageMakerModelDeployment
     */
    public static SageMakerModelDeployment refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SageMakerModelDeployment._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SageMakerModelDeployment by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SageMakerModelDeployment to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SageMakerModelDeployment, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SageMakerModelDeployment does not exist or the provided GUID is not a SageMakerModelDeployment
     */
    @JsonIgnore
    public static SageMakerModelDeployment get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SageMakerModelDeployment by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SageMakerModelDeployment to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SageMakerModelDeployment, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SageMakerModelDeployment does not exist or the provided GUID is not a SageMakerModelDeployment
     */
    @JsonIgnore
    public static SageMakerModelDeployment get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SageMakerModelDeployment) {
                return (SageMakerModelDeployment) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof SageMakerModelDeployment) {
                return (SageMakerModelDeployment) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SageMakerModelDeployment by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SageMakerModelDeployment to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SageMakerModelDeployment, including any relationships
     * @return the requested SageMakerModelDeployment, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SageMakerModelDeployment does not exist or the provided GUID is not a SageMakerModelDeployment
     */
    @JsonIgnore
    public static SageMakerModelDeployment get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a SageMakerModelDeployment by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SageMakerModelDeployment to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SageMakerModelDeployment, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the SageMakerModelDeployment
     * @return the requested SageMakerModelDeployment, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SageMakerModelDeployment does not exist or the provided GUID is not a SageMakerModelDeployment
     */
    @JsonIgnore
    public static SageMakerModelDeployment get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = SageMakerModelDeployment.select(client)
                    .where(SageMakerModelDeployment.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof SageMakerModelDeployment) {
                return (SageMakerModelDeployment) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = SageMakerModelDeployment.select(client)
                    .where(SageMakerModelDeployment.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof SageMakerModelDeployment) {
                return (SageMakerModelDeployment) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SageMakerModelDeployment to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SageMakerModelDeployment
     * @return true if the SageMakerModelDeployment is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SageMakerModelDeployment.
     *
     * @param qualifiedName of the SageMakerModelDeployment
     * @param name of the SageMakerModelDeployment
     * @return the minimal request necessary to update the SageMakerModelDeployment, as a builder
     */
    public static SageMakerModelDeploymentBuilder<?, ?> updater(String qualifiedName, String name) {
        return SageMakerModelDeployment._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SageMakerModelDeployment,
     * from a potentially more-complete SageMakerModelDeployment object.
     *
     * @return the minimal object necessary to update the SageMakerModelDeployment, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a SageMakerModelDeployment are not present in the initial object
     */
    @Override
    public SageMakerModelDeploymentBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class SageMakerModelDeploymentBuilder<
                    C extends SageMakerModelDeployment, B extends SageMakerModelDeploymentBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a SageMakerModelDeployment.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SageMakerModelDeployment
     * @param name of the SageMakerModelDeployment
     * @return the updated SageMakerModelDeployment, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerModelDeployment removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SageMakerModelDeployment) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SageMakerModelDeployment.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SageMakerModelDeployment
     * @param name of the SageMakerModelDeployment
     * @return the updated SageMakerModelDeployment, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerModelDeployment removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SageMakerModelDeployment) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SageMakerModelDeployment.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SageMakerModelDeployment's owners
     * @param qualifiedName of the SageMakerModelDeployment
     * @param name of the SageMakerModelDeployment
     * @return the updated SageMakerModelDeployment, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerModelDeployment removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SageMakerModelDeployment) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SageMakerModelDeployment.
     *
     * @param client connectivity to the Atlan tenant on which to update the SageMakerModelDeployment's certificate
     * @param qualifiedName of the SageMakerModelDeployment
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SageMakerModelDeployment, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerModelDeployment updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SageMakerModelDeployment)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SageMakerModelDeployment.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SageMakerModelDeployment's certificate
     * @param qualifiedName of the SageMakerModelDeployment
     * @param name of the SageMakerModelDeployment
     * @return the updated SageMakerModelDeployment, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerModelDeployment removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SageMakerModelDeployment) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SageMakerModelDeployment.
     *
     * @param client connectivity to the Atlan tenant on which to update the SageMakerModelDeployment's announcement
     * @param qualifiedName of the SageMakerModelDeployment
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerModelDeployment updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SageMakerModelDeployment)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SageMakerModelDeployment.
     *
     * @param client connectivity to the Atlan client from which to remove the SageMakerModelDeployment's announcement
     * @param qualifiedName of the SageMakerModelDeployment
     * @param name of the SageMakerModelDeployment
     * @return the updated SageMakerModelDeployment, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerModelDeployment removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SageMakerModelDeployment) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SageMakerModelDeployment.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SageMakerModelDeployment's assigned terms
     * @param qualifiedName for the SageMakerModelDeployment
     * @param name human-readable name of the SageMakerModelDeployment
     * @param terms the list of terms to replace on the SageMakerModelDeployment, or null to remove all terms from the SageMakerModelDeployment
     * @return the SageMakerModelDeployment that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SageMakerModelDeployment replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SageMakerModelDeployment) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SageMakerModelDeployment, without replacing existing terms linked to the SageMakerModelDeployment.
     * Note: this operation must make two API calls — one to retrieve the SageMakerModelDeployment's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SageMakerModelDeployment
     * @param qualifiedName for the SageMakerModelDeployment
     * @param terms the list of terms to append to the SageMakerModelDeployment
     * @return the SageMakerModelDeployment that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SageMakerModelDeployment appendTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (SageMakerModelDeployment) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SageMakerModelDeployment, without replacing all existing terms linked to the SageMakerModelDeployment.
     * Note: this operation must make two API calls — one to retrieve the SageMakerModelDeployment's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SageMakerModelDeployment
     * @param qualifiedName for the SageMakerModelDeployment
     * @param terms the list of terms to remove from the SageMakerModelDeployment, which must be referenced by GUID
     * @return the SageMakerModelDeployment that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SageMakerModelDeployment removeTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (SageMakerModelDeployment) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SageMakerModelDeployment, without replacing existing Atlan tags linked to the SageMakerModelDeployment.
     * Note: this operation must make two API calls — one to retrieve the SageMakerModelDeployment's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SageMakerModelDeployment
     * @param qualifiedName of the SageMakerModelDeployment
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SageMakerModelDeployment
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static SageMakerModelDeployment appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (SageMakerModelDeployment) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SageMakerModelDeployment, without replacing existing Atlan tags linked to the SageMakerModelDeployment.
     * Note: this operation must make two API calls — one to retrieve the SageMakerModelDeployment's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SageMakerModelDeployment
     * @param qualifiedName of the SageMakerModelDeployment
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SageMakerModelDeployment
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static SageMakerModelDeployment appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SageMakerModelDeployment) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SageMakerModelDeployment.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SageMakerModelDeployment
     * @param qualifiedName of the SageMakerModelDeployment
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SageMakerModelDeployment
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
