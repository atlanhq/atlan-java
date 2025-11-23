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
 * Instance of AWS SMUS Subscribed Asset in Atlan
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class SageMakerUnifiedStudioSubscribedAsset extends Asset
        implements ISageMakerUnifiedStudioSubscribedAsset,
                ISageMakerUnifiedStudioAsset,
                ISageMakerUnifiedStudio,
                ISaaS,
                ICatalog,
                IAsset,
                IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SageMakerUnifiedStudioSubscribedAsset";

    /** Fixed typeName for SageMakerUnifiedStudioSubscribedAssets. */
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

    /** The latest published version for a Published Asset in AWS SMUS */
    @Attribute
    String smusAssetRevision;

    /** Schemas that exist within this subscribed asset. */
    @Attribute
    @Singular
    SortedSet<ISageMakerUnifiedStudioAssetSchema> smusAssetSchemas;

    /** The asset source Identifier for a Published Asset in AWS SMUS */
    @Attribute
    String smusAssetSourceIdentifier;

    /** The summary text for a Published Asset in AWS SMUS */
    @Attribute
    String smusAssetSummary;

    /** The technical name for a Published Asset in AWS SMUS */
    @Attribute
    String smusAssetTechnicalName;

    /** The Asset Type for a Published Asset in AWS SMUS */
    @Attribute
    String smusAssetType;

    /** AWS SMUS Domain ID */
    @Attribute
    String smusDomainId;

    /** AWS SMUS Domain Name */
    @Attribute
    String smusDomainName;

    /** AWS SMUS Domain Unit ID */
    @Attribute
    String smusDomainUnitId;

    /** AWS SMUS Domain Unit Name */
    @Attribute
    String smusDomainUnitName;

    /** Unique ID of the AWS SMUS Project which owns an Asset */
    @Attribute
    String smusOwningProjectId;

    /** Project containing the subscribed asset. */
    @Attribute
    ISageMakerUnifiedStudioProject smusProject;

    /** Unique ID of the AWS SMUS Project */
    @Attribute
    String smusProjectId;

    /** Published Asset associated with this Subscribed Asset in AWS SMUS */
    @Attribute
    @Singular
    SortedSet<ISageMakerUnifiedStudioPublishedAsset> smusPublishedAssets;

    /** Date when the subscription request was approved */
    @Attribute
    @Date
    Long smusSubscribedAssetApprovalDate;

    /** Reason provided by the approver for approving the subscription */
    @Attribute
    String smusSubscribedAssetApprovedReason;

    /** Name of the user who approved the subscription request */
    @Attribute
    String smusSubscribedAssetApproverName;

    /** Number of Columns provided access grant for this subscribed asset. Example : 3 out of 23 */
    @Attribute
    String smusSubscribedAssetColumnAccessInfo;

    /** Name of the AWS SMUS Project from which this asset is subscribed */
    @Attribute
    String smusSubscribedAssetProjectName;

    /** Date when the subscription request was submitted */
    @Attribute
    @Date
    Long smusSubscribedAssetRequestDate;

    /** Reason provided by the requestor for this subscribed asset */
    @Attribute
    String smusSubscribedAssetRequestReason;

    /** Name of the user who requested access to this subscribed asset */
    @Attribute
    String smusSubscribedAssetRequestorName;

    /**
     * Builds the minimal object necessary to create a relationship to a SageMakerUnifiedStudioSubscribedAsset, from a potentially
     * more-complete SageMakerUnifiedStudioSubscribedAsset object.
     *
     * @return the minimal object necessary to relate to the SageMakerUnifiedStudioSubscribedAsset
     * @throws InvalidRequestException if any of the minimal set of required properties for a SageMakerUnifiedStudioSubscribedAsset relationship are not found in the initial object
     */
    @Override
    public SageMakerUnifiedStudioSubscribedAsset trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SageMakerUnifiedStudioSubscribedAsset assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SageMakerUnifiedStudioSubscribedAsset assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SageMakerUnifiedStudioSubscribedAsset assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SageMakerUnifiedStudioSubscribedAsset assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SageMakerUnifiedStudioSubscribedAssets will be included
     * @return a fluent search that includes all SageMakerUnifiedStudioSubscribedAsset assets
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
     * Reference to a SageMakerUnifiedStudioSubscribedAsset by GUID. Use this to create a relationship to this SageMakerUnifiedStudioSubscribedAsset,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SageMakerUnifiedStudioSubscribedAsset to reference
     * @return reference to a SageMakerUnifiedStudioSubscribedAsset that can be used for defining a relationship to a SageMakerUnifiedStudioSubscribedAsset
     */
    public static SageMakerUnifiedStudioSubscribedAsset refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SageMakerUnifiedStudioSubscribedAsset by GUID. Use this to create a relationship to this SageMakerUnifiedStudioSubscribedAsset,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SageMakerUnifiedStudioSubscribedAsset to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SageMakerUnifiedStudioSubscribedAsset that can be used for defining a relationship to a SageMakerUnifiedStudioSubscribedAsset
     */
    public static SageMakerUnifiedStudioSubscribedAsset refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SageMakerUnifiedStudioSubscribedAsset._internal()
                .guid(guid)
                .semantic(semantic)
                .build();
    }

    /**
     * Reference to a SageMakerUnifiedStudioSubscribedAsset by qualifiedName. Use this to create a relationship to this SageMakerUnifiedStudioSubscribedAsset,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SageMakerUnifiedStudioSubscribedAsset to reference
     * @return reference to a SageMakerUnifiedStudioSubscribedAsset that can be used for defining a relationship to a SageMakerUnifiedStudioSubscribedAsset
     */
    public static SageMakerUnifiedStudioSubscribedAsset refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SageMakerUnifiedStudioSubscribedAsset by qualifiedName. Use this to create a relationship to this SageMakerUnifiedStudioSubscribedAsset,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SageMakerUnifiedStudioSubscribedAsset to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SageMakerUnifiedStudioSubscribedAsset that can be used for defining a relationship to a SageMakerUnifiedStudioSubscribedAsset
     */
    public static SageMakerUnifiedStudioSubscribedAsset refByQualifiedName(
            String qualifiedName, Reference.SaveSemantic semantic) {
        return SageMakerUnifiedStudioSubscribedAsset._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SageMakerUnifiedStudioSubscribedAsset by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SageMakerUnifiedStudioSubscribedAsset to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SageMakerUnifiedStudioSubscribedAsset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SageMakerUnifiedStudioSubscribedAsset does not exist or the provided GUID is not a SageMakerUnifiedStudioSubscribedAsset
     */
    @JsonIgnore
    public static SageMakerUnifiedStudioSubscribedAsset get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SageMakerUnifiedStudioSubscribedAsset by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SageMakerUnifiedStudioSubscribedAsset to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SageMakerUnifiedStudioSubscribedAsset, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SageMakerUnifiedStudioSubscribedAsset does not exist or the provided GUID is not a SageMakerUnifiedStudioSubscribedAsset
     */
    @JsonIgnore
    public static SageMakerUnifiedStudioSubscribedAsset get(
            AtlanClient client, String id, boolean includeAllRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SageMakerUnifiedStudioSubscribedAsset) {
                return (SageMakerUnifiedStudioSubscribedAsset) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof SageMakerUnifiedStudioSubscribedAsset) {
                return (SageMakerUnifiedStudioSubscribedAsset) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SageMakerUnifiedStudioSubscribedAsset by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SageMakerUnifiedStudioSubscribedAsset to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SageMakerUnifiedStudioSubscribedAsset, including any relationships
     * @return the requested SageMakerUnifiedStudioSubscribedAsset, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SageMakerUnifiedStudioSubscribedAsset does not exist or the provided GUID is not a SageMakerUnifiedStudioSubscribedAsset
     */
    @JsonIgnore
    public static SageMakerUnifiedStudioSubscribedAsset get(
            AtlanClient client, String id, Collection<AtlanField> attributes) throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a SageMakerUnifiedStudioSubscribedAsset by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SageMakerUnifiedStudioSubscribedAsset to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SageMakerUnifiedStudioSubscribedAsset, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the SageMakerUnifiedStudioSubscribedAsset
     * @return the requested SageMakerUnifiedStudioSubscribedAsset, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SageMakerUnifiedStudioSubscribedAsset does not exist or the provided GUID is not a SageMakerUnifiedStudioSubscribedAsset
     */
    @JsonIgnore
    public static SageMakerUnifiedStudioSubscribedAsset get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = SageMakerUnifiedStudioSubscribedAsset.select(client)
                    .where(SageMakerUnifiedStudioSubscribedAsset.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof SageMakerUnifiedStudioSubscribedAsset) {
                return (SageMakerUnifiedStudioSubscribedAsset) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = SageMakerUnifiedStudioSubscribedAsset.select(client)
                    .where(SageMakerUnifiedStudioSubscribedAsset.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof SageMakerUnifiedStudioSubscribedAsset) {
                return (SageMakerUnifiedStudioSubscribedAsset) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SageMakerUnifiedStudioSubscribedAsset to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SageMakerUnifiedStudioSubscribedAsset
     * @return true if the SageMakerUnifiedStudioSubscribedAsset is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SageMakerUnifiedStudioSubscribedAsset.
     *
     * @param qualifiedName of the SageMakerUnifiedStudioSubscribedAsset
     * @param name of the SageMakerUnifiedStudioSubscribedAsset
     * @return the minimal request necessary to update the SageMakerUnifiedStudioSubscribedAsset, as a builder
     */
    public static SageMakerUnifiedStudioSubscribedAssetBuilder<?, ?> updater(String qualifiedName, String name) {
        return SageMakerUnifiedStudioSubscribedAsset._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SageMakerUnifiedStudioSubscribedAsset, from a potentially
     * more-complete SageMakerUnifiedStudioSubscribedAsset object.
     *
     * @return the minimal object necessary to update the SageMakerUnifiedStudioSubscribedAsset, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SageMakerUnifiedStudioSubscribedAsset are not found in the initial object
     */
    @Override
    public SageMakerUnifiedStudioSubscribedAssetBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class SageMakerUnifiedStudioSubscribedAssetBuilder<
                    C extends SageMakerUnifiedStudioSubscribedAsset,
                    B extends SageMakerUnifiedStudioSubscribedAssetBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a SageMakerUnifiedStudioSubscribedAsset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SageMakerUnifiedStudioSubscribedAsset
     * @param name of the SageMakerUnifiedStudioSubscribedAsset
     * @return the updated SageMakerUnifiedStudioSubscribedAsset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerUnifiedStudioSubscribedAsset removeDescription(
            AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (SageMakerUnifiedStudioSubscribedAsset) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SageMakerUnifiedStudioSubscribedAsset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SageMakerUnifiedStudioSubscribedAsset
     * @param name of the SageMakerUnifiedStudioSubscribedAsset
     * @return the updated SageMakerUnifiedStudioSubscribedAsset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerUnifiedStudioSubscribedAsset removeUserDescription(
            AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (SageMakerUnifiedStudioSubscribedAsset)
                Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SageMakerUnifiedStudioSubscribedAsset.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SageMakerUnifiedStudioSubscribedAsset's owners
     * @param qualifiedName of the SageMakerUnifiedStudioSubscribedAsset
     * @param name of the SageMakerUnifiedStudioSubscribedAsset
     * @return the updated SageMakerUnifiedStudioSubscribedAsset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerUnifiedStudioSubscribedAsset removeOwners(
            AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (SageMakerUnifiedStudioSubscribedAsset) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SageMakerUnifiedStudioSubscribedAsset.
     *
     * @param client connectivity to the Atlan tenant on which to update the SageMakerUnifiedStudioSubscribedAsset's certificate
     * @param qualifiedName of the SageMakerUnifiedStudioSubscribedAsset
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SageMakerUnifiedStudioSubscribedAsset, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerUnifiedStudioSubscribedAsset updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SageMakerUnifiedStudioSubscribedAsset)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SageMakerUnifiedStudioSubscribedAsset.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SageMakerUnifiedStudioSubscribedAsset's certificate
     * @param qualifiedName of the SageMakerUnifiedStudioSubscribedAsset
     * @param name of the SageMakerUnifiedStudioSubscribedAsset
     * @return the updated SageMakerUnifiedStudioSubscribedAsset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerUnifiedStudioSubscribedAsset removeCertificate(
            AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (SageMakerUnifiedStudioSubscribedAsset) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SageMakerUnifiedStudioSubscribedAsset.
     *
     * @param client connectivity to the Atlan tenant on which to update the SageMakerUnifiedStudioSubscribedAsset's announcement
     * @param qualifiedName of the SageMakerUnifiedStudioSubscribedAsset
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerUnifiedStudioSubscribedAsset updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SageMakerUnifiedStudioSubscribedAsset)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SageMakerUnifiedStudioSubscribedAsset.
     *
     * @param client connectivity to the Atlan client from which to remove the SageMakerUnifiedStudioSubscribedAsset's announcement
     * @param qualifiedName of the SageMakerUnifiedStudioSubscribedAsset
     * @param name of the SageMakerUnifiedStudioSubscribedAsset
     * @return the updated SageMakerUnifiedStudioSubscribedAsset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SageMakerUnifiedStudioSubscribedAsset removeAnnouncement(
            AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (SageMakerUnifiedStudioSubscribedAsset) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SageMakerUnifiedStudioSubscribedAsset.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SageMakerUnifiedStudioSubscribedAsset's assigned terms
     * @param qualifiedName for the SageMakerUnifiedStudioSubscribedAsset
     * @param name human-readable name of the SageMakerUnifiedStudioSubscribedAsset
     * @param terms the list of terms to replace on the SageMakerUnifiedStudioSubscribedAsset, or null to remove all terms from the SageMakerUnifiedStudioSubscribedAsset
     * @return the SageMakerUnifiedStudioSubscribedAsset that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SageMakerUnifiedStudioSubscribedAsset replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SageMakerUnifiedStudioSubscribedAsset) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SageMakerUnifiedStudioSubscribedAsset, without replacing existing terms linked to the SageMakerUnifiedStudioSubscribedAsset.
     * Note: this operation must make two API calls — one to retrieve the SageMakerUnifiedStudioSubscribedAsset's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SageMakerUnifiedStudioSubscribedAsset
     * @param qualifiedName for the SageMakerUnifiedStudioSubscribedAsset
     * @param terms the list of terms to append to the SageMakerUnifiedStudioSubscribedAsset
     * @return the SageMakerUnifiedStudioSubscribedAsset that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SageMakerUnifiedStudioSubscribedAsset appendTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (SageMakerUnifiedStudioSubscribedAsset) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SageMakerUnifiedStudioSubscribedAsset, without replacing all existing terms linked to the SageMakerUnifiedStudioSubscribedAsset.
     * Note: this operation must make two API calls — one to retrieve the SageMakerUnifiedStudioSubscribedAsset's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SageMakerUnifiedStudioSubscribedAsset
     * @param qualifiedName for the SageMakerUnifiedStudioSubscribedAsset
     * @param terms the list of terms to remove from the SageMakerUnifiedStudioSubscribedAsset, which must be referenced by GUID
     * @return the SageMakerUnifiedStudioSubscribedAsset that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SageMakerUnifiedStudioSubscribedAsset removeTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (SageMakerUnifiedStudioSubscribedAsset) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SageMakerUnifiedStudioSubscribedAsset, without replacing existing Atlan tags linked to the SageMakerUnifiedStudioSubscribedAsset.
     * Note: this operation must make two API calls — one to retrieve the SageMakerUnifiedStudioSubscribedAsset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SageMakerUnifiedStudioSubscribedAsset
     * @param qualifiedName of the SageMakerUnifiedStudioSubscribedAsset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SageMakerUnifiedStudioSubscribedAsset
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static SageMakerUnifiedStudioSubscribedAsset appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (SageMakerUnifiedStudioSubscribedAsset)
                Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SageMakerUnifiedStudioSubscribedAsset, without replacing existing Atlan tags linked to the SageMakerUnifiedStudioSubscribedAsset.
     * Note: this operation must make two API calls — one to retrieve the SageMakerUnifiedStudioSubscribedAsset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SageMakerUnifiedStudioSubscribedAsset
     * @param qualifiedName of the SageMakerUnifiedStudioSubscribedAsset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SageMakerUnifiedStudioSubscribedAsset
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static SageMakerUnifiedStudioSubscribedAsset appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SageMakerUnifiedStudioSubscribedAsset) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SageMakerUnifiedStudioSubscribedAsset.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SageMakerUnifiedStudioSubscribedAsset
     * @param qualifiedName of the SageMakerUnifiedStudioSubscribedAsset
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SageMakerUnifiedStudioSubscribedAsset
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
