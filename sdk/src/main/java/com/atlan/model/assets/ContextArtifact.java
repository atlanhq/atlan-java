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
 * A context-specific artifact produced by a context repository. Inherits from both Context and Artifact for file type, versioning, and storage path.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class ContextArtifact extends Asset
        implements IContextArtifact, IArtifact, IContext, IAgentic, IResource, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ContextArtifact";

    /** Fixed typeName for ContextArtifacts. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Version of this agentic asset as an epoch-millisecond timestamp. One Atlan entity per (slug, version) tuple. */
    @Attribute
    Long agenticVersion;

    /** String version identifier for this artifact. Will be superseded by agenticVersion (long, epoch-ms) on the Agentic supertype in a future release; continue using this for now. */
    @Attribute
    String artifactVersion;

    /** Unique identifier of the dataset this asset belongs to. */
    @Attribute
    String catalogDatasetGuid;

    /** Context repository that produced this artifact. */
    @Attribute
    IContextRepository contextRepository;

    /** Qualified name of the context repository to which this asset belongs. */
    @Attribute
    String contextRepositoryQualifiedName;

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
     * Builds the minimal object necessary to create a relationship to a ContextArtifact, from a potentially
     * more-complete ContextArtifact object.
     *
     * @return the minimal object necessary to relate to the ContextArtifact
     * @throws InvalidRequestException if any of the minimal set of required properties for a ContextArtifact relationship are not found in the initial object
     */
    @Override
    public ContextArtifact trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all ContextArtifact assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ContextArtifact assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all ContextArtifact assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all ContextArtifact assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ContextArtifacts will be included
     * @return a fluent search that includes all ContextArtifact assets
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
     * Reference to a ContextArtifact by GUID. Use this to create a relationship to this ContextArtifact,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the ContextArtifact to reference
     * @return reference to a ContextArtifact that can be used for defining a relationship to a ContextArtifact
     */
    public static ContextArtifact refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ContextArtifact by GUID. Use this to create a relationship to this ContextArtifact,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the ContextArtifact to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ContextArtifact that can be used for defining a relationship to a ContextArtifact
     */
    public static ContextArtifact refByGuid(String guid, Reference.SaveSemantic semantic) {
        return ContextArtifact._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a ContextArtifact by qualifiedName. Use this to create a relationship to this ContextArtifact,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the ContextArtifact to reference
     * @return reference to a ContextArtifact that can be used for defining a relationship to a ContextArtifact
     */
    public static ContextArtifact refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ContextArtifact by qualifiedName. Use this to create a relationship to this ContextArtifact,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the ContextArtifact to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ContextArtifact that can be used for defining a relationship to a ContextArtifact
     */
    public static ContextArtifact refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return ContextArtifact._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a ContextArtifact by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ContextArtifact to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ContextArtifact, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ContextArtifact does not exist or the provided GUID is not a ContextArtifact
     */
    @JsonIgnore
    public static ContextArtifact get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a ContextArtifact by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ContextArtifact to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full ContextArtifact, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ContextArtifact does not exist or the provided GUID is not a ContextArtifact
     */
    @JsonIgnore
    public static ContextArtifact get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof ContextArtifact) {
                return (ContextArtifact) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof ContextArtifact) {
                return (ContextArtifact) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a ContextArtifact by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ContextArtifact to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the ContextArtifact, including any relationships
     * @return the requested ContextArtifact, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ContextArtifact does not exist or the provided GUID is not a ContextArtifact
     */
    @JsonIgnore
    public static ContextArtifact get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a ContextArtifact by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ContextArtifact to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the ContextArtifact, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the ContextArtifact
     * @return the requested ContextArtifact, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ContextArtifact does not exist or the provided GUID is not a ContextArtifact
     */
    @JsonIgnore
    public static ContextArtifact get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = ContextArtifact.select(client)
                    .where(ContextArtifact.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof ContextArtifact) {
                return (ContextArtifact) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = ContextArtifact.select(client)
                    .where(ContextArtifact.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof ContextArtifact) {
                return (ContextArtifact) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) ContextArtifact to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the ContextArtifact
     * @return true if the ContextArtifact is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a ContextArtifact.
     *
     * @param qualifiedName of the ContextArtifact
     * @param name of the ContextArtifact
     * @return the minimal request necessary to update the ContextArtifact, as a builder
     */
    public static ContextArtifactBuilder<?, ?> updater(String qualifiedName, String name) {
        return ContextArtifact._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ContextArtifact,
     * from a potentially more-complete ContextArtifact object.
     *
     * @return the minimal object necessary to update the ContextArtifact, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a ContextArtifact are not present in the initial object
     */
    @Override
    public ContextArtifactBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ContextArtifactBuilder<
                    C extends ContextArtifact, B extends ContextArtifactBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a ContextArtifact.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ContextArtifact
     * @param name of the ContextArtifact
     * @return the updated ContextArtifact, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ContextArtifact removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ContextArtifact) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ContextArtifact.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ContextArtifact
     * @param name of the ContextArtifact
     * @return the updated ContextArtifact, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ContextArtifact removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ContextArtifact) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ContextArtifact.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ContextArtifact's owners
     * @param qualifiedName of the ContextArtifact
     * @param name of the ContextArtifact
     * @return the updated ContextArtifact, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ContextArtifact removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ContextArtifact) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ContextArtifact.
     *
     * @param client connectivity to the Atlan tenant on which to update the ContextArtifact's certificate
     * @param qualifiedName of the ContextArtifact
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ContextArtifact, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ContextArtifact updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (ContextArtifact)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ContextArtifact.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ContextArtifact's certificate
     * @param qualifiedName of the ContextArtifact
     * @param name of the ContextArtifact
     * @return the updated ContextArtifact, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ContextArtifact removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ContextArtifact) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ContextArtifact.
     *
     * @param client connectivity to the Atlan tenant on which to update the ContextArtifact's announcement
     * @param qualifiedName of the ContextArtifact
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ContextArtifact updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (ContextArtifact)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ContextArtifact.
     *
     * @param client connectivity to the Atlan client from which to remove the ContextArtifact's announcement
     * @param qualifiedName of the ContextArtifact
     * @param name of the ContextArtifact
     * @return the updated ContextArtifact, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ContextArtifact removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ContextArtifact) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the ContextArtifact.
     *
     * @param client connectivity to the Atlan tenant on which to replace the ContextArtifact's assigned terms
     * @param qualifiedName for the ContextArtifact
     * @param name human-readable name of the ContextArtifact
     * @param terms the list of terms to replace on the ContextArtifact, or null to remove all terms from the ContextArtifact
     * @return the ContextArtifact that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ContextArtifact replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (ContextArtifact) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ContextArtifact, without replacing existing terms linked to the ContextArtifact.
     * Note: this operation must make two API calls — one to retrieve the ContextArtifact's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the ContextArtifact
     * @param qualifiedName for the ContextArtifact
     * @param terms the list of terms to append to the ContextArtifact
     * @return the ContextArtifact that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static ContextArtifact appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ContextArtifact) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ContextArtifact, without replacing all existing terms linked to the ContextArtifact.
     * Note: this operation must make two API calls — one to retrieve the ContextArtifact's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the ContextArtifact
     * @param qualifiedName for the ContextArtifact
     * @param terms the list of terms to remove from the ContextArtifact, which must be referenced by GUID
     * @return the ContextArtifact that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static ContextArtifact removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ContextArtifact) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a ContextArtifact, without replacing existing Atlan tags linked to the ContextArtifact.
     * Note: this operation must make two API calls — one to retrieve the ContextArtifact's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ContextArtifact
     * @param qualifiedName of the ContextArtifact
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ContextArtifact
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static ContextArtifact appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (ContextArtifact) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ContextArtifact, without replacing existing Atlan tags linked to the ContextArtifact.
     * Note: this operation must make two API calls — one to retrieve the ContextArtifact's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ContextArtifact
     * @param qualifiedName of the ContextArtifact
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ContextArtifact
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static ContextArtifact appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (ContextArtifact) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a ContextArtifact.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a ContextArtifact
     * @param qualifiedName of the ContextArtifact
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ContextArtifact
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
