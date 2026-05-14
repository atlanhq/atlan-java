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
import com.atlan.model.enums.FileType;
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
 * User-uploaded unstructured file that feeds Atlan enrichment agents and context pipelines.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class KnowledgeFile extends Asset
        implements IKnowledgeFile, IResource, IKnowledge, IAgentic, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "KnowledgeFile";

    /** Fixed typeName for KnowledgeFiles. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Version of this agentic asset as an epoch-millisecond timestamp. One Atlan entity per (slug, version) tuple. */
    @Attribute
    Long agenticVersion;

    /** Unique identifier of the dataset this asset belongs to. */
    @Attribute
    String catalogDatasetGuid;

    /** TBC */
    @Attribute
    IAsset fileAssets;

    /** URL giving the online location where the file can be accessed. */
    @Attribute
    String filePath;

    /** Type (extension) of the file. */
    @Attribute
    FileType fileType;

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

    /** Whether the resource is global (true) or not (false). */
    @Attribute
    Boolean isGlobal;

    /** SHA-256 hex digest of file content, used for deduplication. */
    @Attribute
    String knowledgeContentHash;

    /** Provider-specific version identifier for the active file content (e.g., S3 VersionId, GCS generation number). Use with filePath to retrieve exact bytes at a point in time. */
    @Attribute
    String knowledgeContentVersionId;

    /** Display names of the knowledge folders containing this file. */
    @Attribute
    @Singular
    SortedSet<String> knowledgeFolderNames;

    /** Knowledge folders in which this file exists. */
    @Attribute
    @Singular
    SortedSet<IKnowledgeFolder> knowledgeFolders;

    /** URL to the resource. */
    @Attribute
    String link;

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

    /** Reference to the resource. */
    @Attribute
    String reference;

    /** Size of the file in bytes. */
    @Attribute
    Long resourceFileSize;

    /** Metadata of the resource. */
    @Attribute
    @Singular("putResourceMetadata")
    Map<String, String> resourceMetadata;

    /**
     * Builds the minimal object necessary to create a relationship to a KnowledgeFile, from a potentially
     * more-complete KnowledgeFile object.
     *
     * @return the minimal object necessary to relate to the KnowledgeFile
     * @throws InvalidRequestException if any of the minimal set of required properties for a KnowledgeFile relationship are not found in the initial object
     */
    @Override
    public KnowledgeFile trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all KnowledgeFile assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) KnowledgeFile assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all KnowledgeFile assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all KnowledgeFile assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) KnowledgeFiles will be included
     * @return a fluent search that includes all KnowledgeFile assets
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
     * Reference to a KnowledgeFile by GUID. Use this to create a relationship to this KnowledgeFile,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the KnowledgeFile to reference
     * @return reference to a KnowledgeFile that can be used for defining a relationship to a KnowledgeFile
     */
    public static KnowledgeFile refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a KnowledgeFile by GUID. Use this to create a relationship to this KnowledgeFile,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the KnowledgeFile to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a KnowledgeFile that can be used for defining a relationship to a KnowledgeFile
     */
    public static KnowledgeFile refByGuid(String guid, Reference.SaveSemantic semantic) {
        return KnowledgeFile._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a KnowledgeFile by qualifiedName. Use this to create a relationship to this KnowledgeFile,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the KnowledgeFile to reference
     * @return reference to a KnowledgeFile that can be used for defining a relationship to a KnowledgeFile
     */
    public static KnowledgeFile refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a KnowledgeFile by qualifiedName. Use this to create a relationship to this KnowledgeFile,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the KnowledgeFile to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a KnowledgeFile that can be used for defining a relationship to a KnowledgeFile
     */
    public static KnowledgeFile refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return KnowledgeFile._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a KnowledgeFile by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the KnowledgeFile to retrieve, either its GUID or its full qualifiedName
     * @return the requested full KnowledgeFile, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the KnowledgeFile does not exist or the provided GUID is not a KnowledgeFile
     */
    @JsonIgnore
    public static KnowledgeFile get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a KnowledgeFile by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the KnowledgeFile to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full KnowledgeFile, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the KnowledgeFile does not exist or the provided GUID is not a KnowledgeFile
     */
    @JsonIgnore
    public static KnowledgeFile get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof KnowledgeFile) {
                return (KnowledgeFile) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof KnowledgeFile) {
                return (KnowledgeFile) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a KnowledgeFile by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the KnowledgeFile to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the KnowledgeFile, including any relationships
     * @return the requested KnowledgeFile, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the KnowledgeFile does not exist or the provided GUID is not a KnowledgeFile
     */
    @JsonIgnore
    public static KnowledgeFile get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a KnowledgeFile by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the KnowledgeFile to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the KnowledgeFile, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the KnowledgeFile
     * @return the requested KnowledgeFile, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the KnowledgeFile does not exist or the provided GUID is not a KnowledgeFile
     */
    @JsonIgnore
    public static KnowledgeFile get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = KnowledgeFile.select(client)
                    .where(KnowledgeFile.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof KnowledgeFile) {
                return (KnowledgeFile) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = KnowledgeFile.select(client)
                    .where(KnowledgeFile.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof KnowledgeFile) {
                return (KnowledgeFile) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) KnowledgeFile to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the KnowledgeFile
     * @return true if the KnowledgeFile is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a KnowledgeFile.
     *
     * @param qualifiedName of the KnowledgeFile
     * @param name of the KnowledgeFile
     * @return the minimal request necessary to update the KnowledgeFile, as a builder
     */
    public static KnowledgeFileBuilder<?, ?> updater(String qualifiedName, String name) {
        return KnowledgeFile._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a KnowledgeFile,
     * from a potentially more-complete KnowledgeFile object.
     *
     * @return the minimal object necessary to update the KnowledgeFile, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a KnowledgeFile are not present in the initial object
     */
    @Override
    public KnowledgeFileBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class KnowledgeFileBuilder<C extends KnowledgeFile, B extends KnowledgeFileBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a KnowledgeFile.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the KnowledgeFile
     * @param name of the KnowledgeFile
     * @return the updated KnowledgeFile, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static KnowledgeFile removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (KnowledgeFile) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a KnowledgeFile.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the KnowledgeFile
     * @param name of the KnowledgeFile
     * @return the updated KnowledgeFile, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static KnowledgeFile removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (KnowledgeFile) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a KnowledgeFile.
     *
     * @param client connectivity to the Atlan tenant from which to remove the KnowledgeFile's owners
     * @param qualifiedName of the KnowledgeFile
     * @param name of the KnowledgeFile
     * @return the updated KnowledgeFile, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static KnowledgeFile removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (KnowledgeFile) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a KnowledgeFile.
     *
     * @param client connectivity to the Atlan tenant on which to update the KnowledgeFile's certificate
     * @param qualifiedName of the KnowledgeFile
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated KnowledgeFile, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static KnowledgeFile updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (KnowledgeFile)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a KnowledgeFile.
     *
     * @param client connectivity to the Atlan tenant from which to remove the KnowledgeFile's certificate
     * @param qualifiedName of the KnowledgeFile
     * @param name of the KnowledgeFile
     * @return the updated KnowledgeFile, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static KnowledgeFile removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (KnowledgeFile) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a KnowledgeFile.
     *
     * @param client connectivity to the Atlan tenant on which to update the KnowledgeFile's announcement
     * @param qualifiedName of the KnowledgeFile
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static KnowledgeFile updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (KnowledgeFile)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a KnowledgeFile.
     *
     * @param client connectivity to the Atlan client from which to remove the KnowledgeFile's announcement
     * @param qualifiedName of the KnowledgeFile
     * @param name of the KnowledgeFile
     * @return the updated KnowledgeFile, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static KnowledgeFile removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (KnowledgeFile) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the KnowledgeFile.
     *
     * @param client connectivity to the Atlan tenant on which to replace the KnowledgeFile's assigned terms
     * @param qualifiedName for the KnowledgeFile
     * @param name human-readable name of the KnowledgeFile
     * @param terms the list of terms to replace on the KnowledgeFile, or null to remove all terms from the KnowledgeFile
     * @return the KnowledgeFile that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static KnowledgeFile replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (KnowledgeFile) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the KnowledgeFile, without replacing existing terms linked to the KnowledgeFile.
     * Note: this operation must make two API calls — one to retrieve the KnowledgeFile's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the KnowledgeFile
     * @param qualifiedName for the KnowledgeFile
     * @param terms the list of terms to append to the KnowledgeFile
     * @return the KnowledgeFile that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static KnowledgeFile appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (KnowledgeFile) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a KnowledgeFile, without replacing all existing terms linked to the KnowledgeFile.
     * Note: this operation must make two API calls — one to retrieve the KnowledgeFile's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the KnowledgeFile
     * @param qualifiedName for the KnowledgeFile
     * @param terms the list of terms to remove from the KnowledgeFile, which must be referenced by GUID
     * @return the KnowledgeFile that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static KnowledgeFile removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (KnowledgeFile) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a KnowledgeFile, without replacing existing Atlan tags linked to the KnowledgeFile.
     * Note: this operation must make two API calls — one to retrieve the KnowledgeFile's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the KnowledgeFile
     * @param qualifiedName of the KnowledgeFile
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated KnowledgeFile
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static KnowledgeFile appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (KnowledgeFile) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a KnowledgeFile, without replacing existing Atlan tags linked to the KnowledgeFile.
     * Note: this operation must make two API calls — one to retrieve the KnowledgeFile's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the KnowledgeFile
     * @param qualifiedName of the KnowledgeFile
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated KnowledgeFile
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static KnowledgeFile appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (KnowledgeFile) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a KnowledgeFile.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a KnowledgeFile
     * @param qualifiedName of the KnowledgeFile
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the KnowledgeFile
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
