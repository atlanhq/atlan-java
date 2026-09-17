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
 * Analytic or planning model in SAP Analytics Cloud. A model is a multidimensional object composed of a fact table, measures, dimensions and hierarchies, whose data is either imported into SAP Analytics Cloud or queried live from a remote system.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class SapAnalyticsCloudModel extends Asset
        implements ISapAnalyticsCloudModel, ISapAnalyticsCloud, ISAP, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SapAnalyticsCloudModel";

    /** Fixed typeName for SapAnalyticsCloudModels. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Unique identifier of the dataset this asset belongs to. */
    @Attribute
    String catalogDatasetGuid;

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

    /** Columns defined within this model. */
    @Attribute
    @Singular
    SortedSet<ISapAnalyticsCloudColumn> sapAnalyticsCloudColumns;

    /** How this model reaches its data, as reported by the source. IMPORT when the data is acquired into SAP Analytics Cloud and stored there, LIVE when it stays in a remote system and is queried live over a connection. */
    @Attribute
    String sapAnalyticsCloudDataAccessMode;

    /** Folder containing this model. */
    @Attribute
    ISapAnalyticsCloudFolder sapAnalyticsCloudFolder;

    /** Host of the remote system a live model reads from. Empty for imported models. */
    @Attribute
    String sapAnalyticsCloudLiveConnectionHost;

    /** Identifier of the remote connection a live model reads from. Empty for imported models. */
    @Attribute
    String sapAnalyticsCloudLiveConnectionId;

    /** Simple name of the remote connection a live model reads from. Empty for imported models. */
    @Attribute
    String sapAnalyticsCloudLiveConnectionName;

    /** Port of the remote system a live model reads from. Empty for imported models. */
    @Attribute
    Integer sapAnalyticsCloudLiveConnectionPort;

    /** Protocol used to reach the remote system a live model reads from, such as HTTPS. Empty for imported models. */
    @Attribute
    String sapAnalyticsCloudLiveConnectionProtocol;

    /** Type of the remote system a live model reads from, such as DWC for SAP Datasphere. Empty for imported models. */
    @Attribute
    String sapAnalyticsCloudLiveConnectionSystemType;

    /** Type of the remote connection a live model reads from, such as DIRECT. Empty for imported models. */
    @Attribute
    String sapAnalyticsCloudLiveConnectionType;

    /** Model identifier reported by the SAP Analytics Cloud tenant APIs, which differs from the file-repository resource identifier for live models. */
    @Attribute
    String sapAnalyticsCloudModelId;

    /** Whether this model is an analytic model or a planning model, as reported by the source. ANALYTIC for a read-only model used for analysis and reporting, PLANNING for a model that supports write-back, versions and planning operations. */
    @Attribute
    String sapAnalyticsCloudModelKind;

    /** Underlying object identifier reported by the SAP Analytics Cloud file repository for this asset. */
    @Attribute
    String sapAnalyticsCloudObjectId;

    /** Simple name of the SAP Analytics Cloud folder that directly contains this asset. Empty for a root-level folder and for a live model, neither of which is contained by a folder. */
    @Attribute
    String sapAnalyticsCloudParentFolderName;

    /** Unique name of the SAP Analytics Cloud folder that directly contains this asset. Empty for a root-level folder and for a live model, neither of which is contained by a folder. */
    @Attribute
    String sapAnalyticsCloudParentFolderQualifiedName;

    /** Identifier of the OData provider that exposes this model's metadata. This is the key that joins a model to its columns. */
    @Attribute
    String sapAnalyticsCloudProviderId;

    /** Partition of the SAP Analytics Cloud file repository this asset lives in: PUBLIC for shared tenant content, SYSTEM for SAP-shipped content and SAP Analytics Cloud's own telemetry, USERS for the container holding per-user private areas, and PRIVATE for an individual user's own content. Reported by the source as folderType, and carried by every resource rather than only by folders. */
    @Attribute
    String sapAnalyticsCloudRepositoryPartition;

    /** Identifier of this asset in the SAP Analytics Cloud file repository. Stable across renames and used by the source APIs to address the resource. */
    @Attribute
    String sapAnalyticsCloudResourceId;

    /** Number of SAP Analytics Cloud stories that consume this model, as reported by the source. */
    @Attribute
    Long sapAnalyticsCloudStoryCount;

    /** Identifier of the SAP Analytics Cloud workspace that owns this asset. */
    @Attribute
    String sapAnalyticsCloudWorkspaceId;

    /** Simple name of the SAP Analytics Cloud workspace that owns this asset. */
    @Attribute
    String sapAnalyticsCloudWorkspaceName;

    /** Name of the SAP component, representing a specific functional area in SAP. */
    @Attribute
    String sapComponentName;

    /** SAP-specific data types. */
    @Attribute
    String sapDataType;

    /** Represents the total number of fields, columns, or child assets present in a given SAP asset. */
    @Attribute
    Long sapFieldCount;

    /** Indicates the sequential position of a field, column, or child asset within its parent SAP asset, starting from 1. */
    @Attribute
    Integer sapFieldOrder;

    /** Logical, business-friendly identifier for SAP data objects, aligned with business terminology and concepts. */
    @Attribute
    String sapLogicalName;

    /** Name of the SAP package, representing a logical grouping of related SAP data objects. */
    @Attribute
    String sapPackageName;

    /** Technical identifier for SAP data objects, used for integration and internal reference. */
    @Attribute
    String sapTechnicalName;

    /**
     * Builds the minimal object necessary to create a relationship to a SapAnalyticsCloudModel, from a potentially
     * more-complete SapAnalyticsCloudModel object.
     *
     * @return the minimal object necessary to relate to the SapAnalyticsCloudModel
     * @throws InvalidRequestException if any of the minimal set of required properties for a SapAnalyticsCloudModel relationship are not found in the initial object
     */
    @Override
    public SapAnalyticsCloudModel trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SapAnalyticsCloudModel assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SapAnalyticsCloudModel assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SapAnalyticsCloudModel assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SapAnalyticsCloudModel assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SapAnalyticsCloudModels will be included
     * @return a fluent search that includes all SapAnalyticsCloudModel assets
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
     * Reference to a SapAnalyticsCloudModel by GUID. Use this to create a relationship to this SapAnalyticsCloudModel,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SapAnalyticsCloudModel to reference
     * @return reference to a SapAnalyticsCloudModel that can be used for defining a relationship to a SapAnalyticsCloudModel
     */
    public static SapAnalyticsCloudModel refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SapAnalyticsCloudModel by GUID. Use this to create a relationship to this SapAnalyticsCloudModel,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SapAnalyticsCloudModel to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SapAnalyticsCloudModel that can be used for defining a relationship to a SapAnalyticsCloudModel
     */
    public static SapAnalyticsCloudModel refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SapAnalyticsCloudModel._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SapAnalyticsCloudModel by qualifiedName. Use this to create a relationship to this SapAnalyticsCloudModel,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SapAnalyticsCloudModel to reference
     * @return reference to a SapAnalyticsCloudModel that can be used for defining a relationship to a SapAnalyticsCloudModel
     */
    public static SapAnalyticsCloudModel refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SapAnalyticsCloudModel by qualifiedName. Use this to create a relationship to this SapAnalyticsCloudModel,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SapAnalyticsCloudModel to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SapAnalyticsCloudModel that can be used for defining a relationship to a SapAnalyticsCloudModel
     */
    public static SapAnalyticsCloudModel refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SapAnalyticsCloudModel._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SapAnalyticsCloudModel by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SapAnalyticsCloudModel to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SapAnalyticsCloudModel, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SapAnalyticsCloudModel does not exist or the provided GUID is not a SapAnalyticsCloudModel
     */
    @JsonIgnore
    public static SapAnalyticsCloudModel get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SapAnalyticsCloudModel by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SapAnalyticsCloudModel to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SapAnalyticsCloudModel, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SapAnalyticsCloudModel does not exist or the provided GUID is not a SapAnalyticsCloudModel
     */
    @JsonIgnore
    public static SapAnalyticsCloudModel get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SapAnalyticsCloudModel) {
                return (SapAnalyticsCloudModel) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof SapAnalyticsCloudModel) {
                return (SapAnalyticsCloudModel) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SapAnalyticsCloudModel by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SapAnalyticsCloudModel to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SapAnalyticsCloudModel, including any relationships
     * @return the requested SapAnalyticsCloudModel, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SapAnalyticsCloudModel does not exist or the provided GUID is not a SapAnalyticsCloudModel
     */
    @JsonIgnore
    public static SapAnalyticsCloudModel get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a SapAnalyticsCloudModel by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SapAnalyticsCloudModel to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SapAnalyticsCloudModel, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the SapAnalyticsCloudModel
     * @return the requested SapAnalyticsCloudModel, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SapAnalyticsCloudModel does not exist or the provided GUID is not a SapAnalyticsCloudModel
     */
    @JsonIgnore
    public static SapAnalyticsCloudModel get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = SapAnalyticsCloudModel.select(client)
                    .where(SapAnalyticsCloudModel.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof SapAnalyticsCloudModel) {
                return (SapAnalyticsCloudModel) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = SapAnalyticsCloudModel.select(client)
                    .where(SapAnalyticsCloudModel.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof SapAnalyticsCloudModel) {
                return (SapAnalyticsCloudModel) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SapAnalyticsCloudModel to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SapAnalyticsCloudModel
     * @return true if the SapAnalyticsCloudModel is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SapAnalyticsCloudModel.
     *
     * @param qualifiedName of the SapAnalyticsCloudModel
     * @param name of the SapAnalyticsCloudModel
     * @return the minimal request necessary to update the SapAnalyticsCloudModel, as a builder
     */
    public static SapAnalyticsCloudModelBuilder<?, ?> updater(String qualifiedName, String name) {
        return SapAnalyticsCloudModel._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SapAnalyticsCloudModel,
     * from a potentially more-complete SapAnalyticsCloudModel object.
     *
     * @return the minimal object necessary to update the SapAnalyticsCloudModel, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a SapAnalyticsCloudModel are not present in the initial object
     */
    @Override
    public SapAnalyticsCloudModelBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class SapAnalyticsCloudModelBuilder<
                    C extends SapAnalyticsCloudModel, B extends SapAnalyticsCloudModelBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a SapAnalyticsCloudModel.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SapAnalyticsCloudModel
     * @param name of the SapAnalyticsCloudModel
     * @return the updated SapAnalyticsCloudModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SapAnalyticsCloudModel removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SapAnalyticsCloudModel) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SapAnalyticsCloudModel.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SapAnalyticsCloudModel
     * @param name of the SapAnalyticsCloudModel
     * @return the updated SapAnalyticsCloudModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SapAnalyticsCloudModel removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SapAnalyticsCloudModel) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SapAnalyticsCloudModel.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SapAnalyticsCloudModel's owners
     * @param qualifiedName of the SapAnalyticsCloudModel
     * @param name of the SapAnalyticsCloudModel
     * @return the updated SapAnalyticsCloudModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SapAnalyticsCloudModel removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SapAnalyticsCloudModel) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SapAnalyticsCloudModel.
     *
     * @param client connectivity to the Atlan tenant on which to update the SapAnalyticsCloudModel's certificate
     * @param qualifiedName of the SapAnalyticsCloudModel
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SapAnalyticsCloudModel, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SapAnalyticsCloudModel updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SapAnalyticsCloudModel)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SapAnalyticsCloudModel.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SapAnalyticsCloudModel's certificate
     * @param qualifiedName of the SapAnalyticsCloudModel
     * @param name of the SapAnalyticsCloudModel
     * @return the updated SapAnalyticsCloudModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SapAnalyticsCloudModel removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SapAnalyticsCloudModel) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SapAnalyticsCloudModel.
     *
     * @param client connectivity to the Atlan tenant on which to update the SapAnalyticsCloudModel's announcement
     * @param qualifiedName of the SapAnalyticsCloudModel
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SapAnalyticsCloudModel updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SapAnalyticsCloudModel)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SapAnalyticsCloudModel.
     *
     * @param client connectivity to the Atlan client from which to remove the SapAnalyticsCloudModel's announcement
     * @param qualifiedName of the SapAnalyticsCloudModel
     * @param name of the SapAnalyticsCloudModel
     * @return the updated SapAnalyticsCloudModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SapAnalyticsCloudModel removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SapAnalyticsCloudModel) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SapAnalyticsCloudModel.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SapAnalyticsCloudModel's assigned terms
     * @param qualifiedName for the SapAnalyticsCloudModel
     * @param name human-readable name of the SapAnalyticsCloudModel
     * @param terms the list of terms to replace on the SapAnalyticsCloudModel, or null to remove all terms from the SapAnalyticsCloudModel
     * @return the SapAnalyticsCloudModel that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SapAnalyticsCloudModel replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SapAnalyticsCloudModel) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SapAnalyticsCloudModel, without replacing existing terms linked to the SapAnalyticsCloudModel.
     * Note: this operation must make two API calls — one to retrieve the SapAnalyticsCloudModel's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SapAnalyticsCloudModel
     * @param qualifiedName for the SapAnalyticsCloudModel
     * @param terms the list of terms to append to the SapAnalyticsCloudModel
     * @return the SapAnalyticsCloudModel that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SapAnalyticsCloudModel appendTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (SapAnalyticsCloudModel) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SapAnalyticsCloudModel, without replacing all existing terms linked to the SapAnalyticsCloudModel.
     * Note: this operation must make two API calls — one to retrieve the SapAnalyticsCloudModel's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SapAnalyticsCloudModel
     * @param qualifiedName for the SapAnalyticsCloudModel
     * @param terms the list of terms to remove from the SapAnalyticsCloudModel, which must be referenced by GUID
     * @return the SapAnalyticsCloudModel that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SapAnalyticsCloudModel removeTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (SapAnalyticsCloudModel) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SapAnalyticsCloudModel, without replacing existing Atlan tags linked to the SapAnalyticsCloudModel.
     * Note: this operation must make two API calls — one to retrieve the SapAnalyticsCloudModel's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SapAnalyticsCloudModel
     * @param qualifiedName of the SapAnalyticsCloudModel
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SapAnalyticsCloudModel
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static SapAnalyticsCloudModel appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (SapAnalyticsCloudModel) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SapAnalyticsCloudModel, without replacing existing Atlan tags linked to the SapAnalyticsCloudModel.
     * Note: this operation must make two API calls — one to retrieve the SapAnalyticsCloudModel's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SapAnalyticsCloudModel
     * @param qualifiedName of the SapAnalyticsCloudModel
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SapAnalyticsCloudModel
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static SapAnalyticsCloudModel appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SapAnalyticsCloudModel) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SapAnalyticsCloudModel.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SapAnalyticsCloudModel
     * @param qualifiedName of the SapAnalyticsCloudModel
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SapAnalyticsCloudModel
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
