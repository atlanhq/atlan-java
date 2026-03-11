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
 * Instance of a tool defined in an Atlan application.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class AtlanAppTool extends Asset implements IAtlanAppTool, IAtlanApp, IApp, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AtlanAppTool";

    /** Fixed typeName for AtlanAppTools. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Unique identifier for the application asset from the source system. */
    @Attribute
    String appId;

    /** Atlan application containing the tool. */
    @Attribute
    IAtlanApp atlanApp;

    /** Metadata for the Atlan application (escaped JSON string). */
    @Attribute
    String atlanAppMetadata;

    /** Name of the Atlan application this asset belongs to. */
    @Attribute
    String atlanAppName;

    /** Qualified name of the Atlan application this asset belongs to. */
    @Attribute
    String atlanAppQualifiedName;

    /** Category of the tool. */
    @Attribute
    String atlanAppToolCategory;

    /** Input schema for the Atlan application tool (escaped JSON string of JSONSchema). */
    @Attribute
    String atlanAppToolInputSchema;

    /** Output schema for the Atlan application tool (escaped JSON string of JSONSchema). */
    @Attribute
    String atlanAppToolOutputSchema;

    /** Name of the Temporal task queue for the Atlan application tool. */
    @Attribute
    String atlanAppToolTaskQueue;

    /** Tools that exist within this Atlan application. */
    @Attribute
    @Singular
    SortedSet<IAtlanAppTool> atlanAppTools;

    /** Workflows that exist within this Atlan application. */
    @Attribute
    @Singular
    SortedSet<IAtlanAppWorkflow> atlanAppWorkflows;

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

    /**
     * Builds the minimal object necessary to create a relationship to a AtlanAppTool, from a potentially
     * more-complete AtlanAppTool object.
     *
     * @return the minimal object necessary to relate to the AtlanAppTool
     * @throws InvalidRequestException if any of the minimal set of required properties for a AtlanAppTool relationship are not found in the initial object
     */
    @Override
    public AtlanAppTool trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all AtlanAppTool assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) AtlanAppTool assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all AtlanAppTool assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all AtlanAppTool assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) AtlanAppTools will be included
     * @return a fluent search that includes all AtlanAppTool assets
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
     * Reference to a AtlanAppTool by GUID. Use this to create a relationship to this AtlanAppTool,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the AtlanAppTool to reference
     * @return reference to a AtlanAppTool that can be used for defining a relationship to a AtlanAppTool
     */
    public static AtlanAppTool refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AtlanAppTool by GUID. Use this to create a relationship to this AtlanAppTool,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the AtlanAppTool to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AtlanAppTool that can be used for defining a relationship to a AtlanAppTool
     */
    public static AtlanAppTool refByGuid(String guid, Reference.SaveSemantic semantic) {
        return AtlanAppTool._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a AtlanAppTool by qualifiedName. Use this to create a relationship to this AtlanAppTool,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the AtlanAppTool to reference
     * @return reference to a AtlanAppTool that can be used for defining a relationship to a AtlanAppTool
     */
    public static AtlanAppTool refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AtlanAppTool by qualifiedName. Use this to create a relationship to this AtlanAppTool,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the AtlanAppTool to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AtlanAppTool that can be used for defining a relationship to a AtlanAppTool
     */
    public static AtlanAppTool refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return AtlanAppTool._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a AtlanAppTool by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AtlanAppTool to retrieve, either its GUID or its full qualifiedName
     * @return the requested full AtlanAppTool, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AtlanAppTool does not exist or the provided GUID is not a AtlanAppTool
     */
    @JsonIgnore
    public static AtlanAppTool get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a AtlanAppTool by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AtlanAppTool to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full AtlanAppTool, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AtlanAppTool does not exist or the provided GUID is not a AtlanAppTool
     */
    @JsonIgnore
    public static AtlanAppTool get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof AtlanAppTool) {
                return (AtlanAppTool) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof AtlanAppTool) {
                return (AtlanAppTool) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a AtlanAppTool by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AtlanAppTool to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AtlanAppTool, including any relationships
     * @return the requested AtlanAppTool, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AtlanAppTool does not exist or the provided GUID is not a AtlanAppTool
     */
    @JsonIgnore
    public static AtlanAppTool get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a AtlanAppTool by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AtlanAppTool to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AtlanAppTool, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the AtlanAppTool
     * @return the requested AtlanAppTool, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AtlanAppTool does not exist or the provided GUID is not a AtlanAppTool
     */
    @JsonIgnore
    public static AtlanAppTool get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = AtlanAppTool.select(client)
                    .where(AtlanAppTool.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof AtlanAppTool) {
                return (AtlanAppTool) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = AtlanAppTool.select(client)
                    .where(AtlanAppTool.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof AtlanAppTool) {
                return (AtlanAppTool) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) AtlanAppTool to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the AtlanAppTool
     * @return true if the AtlanAppTool is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a AtlanAppTool.
     *
     * @param qualifiedName of the AtlanAppTool
     * @param name of the AtlanAppTool
     * @return the minimal request necessary to update the AtlanAppTool, as a builder
     */
    public static AtlanAppToolBuilder<?, ?> updater(String qualifiedName, String name) {
        return AtlanAppTool._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AtlanAppTool,
     * from a potentially more-complete AtlanAppTool object.
     *
     * @return the minimal object necessary to update the AtlanAppTool, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a AtlanAppTool are not present in the initial object
     */
    @Override
    public AtlanAppToolBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class AtlanAppToolBuilder<C extends AtlanAppTool, B extends AtlanAppToolBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a AtlanAppTool.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AtlanAppTool
     * @param name of the AtlanAppTool
     * @return the updated AtlanAppTool, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanAppTool removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AtlanAppTool) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a AtlanAppTool.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AtlanAppTool
     * @param name of the AtlanAppTool
     * @return the updated AtlanAppTool, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanAppTool removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AtlanAppTool) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a AtlanAppTool.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AtlanAppTool's owners
     * @param qualifiedName of the AtlanAppTool
     * @param name of the AtlanAppTool
     * @return the updated AtlanAppTool, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanAppTool removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AtlanAppTool) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a AtlanAppTool.
     *
     * @param client connectivity to the Atlan tenant on which to update the AtlanAppTool's certificate
     * @param qualifiedName of the AtlanAppTool
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AtlanAppTool, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AtlanAppTool updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (AtlanAppTool)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a AtlanAppTool.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AtlanAppTool's certificate
     * @param qualifiedName of the AtlanAppTool
     * @param name of the AtlanAppTool
     * @return the updated AtlanAppTool, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanAppTool removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AtlanAppTool) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a AtlanAppTool.
     *
     * @param client connectivity to the Atlan tenant on which to update the AtlanAppTool's announcement
     * @param qualifiedName of the AtlanAppTool
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AtlanAppTool updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (AtlanAppTool)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a AtlanAppTool.
     *
     * @param client connectivity to the Atlan client from which to remove the AtlanAppTool's announcement
     * @param qualifiedName of the AtlanAppTool
     * @param name of the AtlanAppTool
     * @return the updated AtlanAppTool, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanAppTool removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AtlanAppTool) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the AtlanAppTool.
     *
     * @param client connectivity to the Atlan tenant on which to replace the AtlanAppTool's assigned terms
     * @param qualifiedName for the AtlanAppTool
     * @param name human-readable name of the AtlanAppTool
     * @param terms the list of terms to replace on the AtlanAppTool, or null to remove all terms from the AtlanAppTool
     * @return the AtlanAppTool that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static AtlanAppTool replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (AtlanAppTool) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the AtlanAppTool, without replacing existing terms linked to the AtlanAppTool.
     * Note: this operation must make two API calls — one to retrieve the AtlanAppTool's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the AtlanAppTool
     * @param qualifiedName for the AtlanAppTool
     * @param terms the list of terms to append to the AtlanAppTool
     * @return the AtlanAppTool that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static AtlanAppTool appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AtlanAppTool) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a AtlanAppTool, without replacing all existing terms linked to the AtlanAppTool.
     * Note: this operation must make two API calls — one to retrieve the AtlanAppTool's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the AtlanAppTool
     * @param qualifiedName for the AtlanAppTool
     * @param terms the list of terms to remove from the AtlanAppTool, which must be referenced by GUID
     * @return the AtlanAppTool that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static AtlanAppTool removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AtlanAppTool) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a AtlanAppTool, without replacing existing Atlan tags linked to the AtlanAppTool.
     * Note: this operation must make two API calls — one to retrieve the AtlanAppTool's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AtlanAppTool
     * @param qualifiedName of the AtlanAppTool
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated AtlanAppTool
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static AtlanAppTool appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (AtlanAppTool) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AtlanAppTool, without replacing existing Atlan tags linked to the AtlanAppTool.
     * Note: this operation must make two API calls — one to retrieve the AtlanAppTool's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AtlanAppTool
     * @param qualifiedName of the AtlanAppTool
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated AtlanAppTool
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static AtlanAppTool appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (AtlanAppTool) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a AtlanAppTool.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a AtlanAppTool
     * @param qualifiedName of the AtlanAppTool
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the AtlanAppTool
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
