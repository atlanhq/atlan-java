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
 * Instance of a Qlik stream in Atlan, which is analogus to a Qlik space.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class QlikStream extends Asset implements IQlikStream, IQlikSpace, IQlik, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "QlikStream";

    /** Fixed typeName for QlikStreams. */
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

    /** Identifier of the app in which this asset belongs, from Qlik. */
    @Attribute
    String qlikAppId;

    /** Unique name of the app where this asset belongs. */
    @Attribute
    String qlikAppQualifiedName;

    /** Apps that exist within this space. */
    @Attribute
    @Singular
    SortedSet<IQlikApp> qlikApps;

    /** Datasets that exist within this space. */
    @Attribute
    @Singular
    SortedSet<IQlikDataset> qlikDatasets;

    /** Identifier of this asset, from Qlik. */
    @Attribute
    String qlikId;

    /** Whether this asset is published in Qlik (true) or not (false). */
    @Attribute
    Boolean qlikIsPublished;

    /** Identifier of the owner of this asset, in Qlik. */
    @Attribute
    String qlikOwnerId;

    /** Unique QRI of this asset, from Qlik. */
    @Attribute
    String qlikQRI;

    /** Identifier of the space in which this asset exists, from Qlik. */
    @Attribute
    String qlikSpaceId;

    /** Unique name of the space in which this asset exists. */
    @Attribute
    String qlikSpaceQualifiedName;

    /** Type of this space, for exmaple: Private, Shared, etc. */
    @Attribute
    String qlikSpaceType;

    /**
     * Builds the minimal object necessary to create a relationship to a QlikStream, from a potentially
     * more-complete QlikStream object.
     *
     * @return the minimal object necessary to relate to the QlikStream
     * @throws InvalidRequestException if any of the minimal set of required properties for a QlikStream relationship are not found in the initial object
     */
    @Override
    public QlikStream trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all QlikStream assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) QlikStream assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all QlikStream assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all QlikStream assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) QlikStreams will be included
     * @return a fluent search that includes all QlikStream assets
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
     * Reference to a QlikStream by GUID. Use this to create a relationship to this QlikStream,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the QlikStream to reference
     * @return reference to a QlikStream that can be used for defining a relationship to a QlikStream
     */
    public static QlikStream refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a QlikStream by GUID. Use this to create a relationship to this QlikStream,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the QlikStream to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a QlikStream that can be used for defining a relationship to a QlikStream
     */
    public static QlikStream refByGuid(String guid, Reference.SaveSemantic semantic) {
        return QlikStream._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a QlikStream by qualifiedName. Use this to create a relationship to this QlikStream,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the QlikStream to reference
     * @return reference to a QlikStream that can be used for defining a relationship to a QlikStream
     */
    public static QlikStream refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a QlikStream by qualifiedName. Use this to create a relationship to this QlikStream,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the QlikStream to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a QlikStream that can be used for defining a relationship to a QlikStream
     */
    public static QlikStream refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return QlikStream._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a QlikStream by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the QlikStream to retrieve, either its GUID or its full qualifiedName
     * @return the requested full QlikStream, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikStream does not exist or the provided GUID is not a QlikStream
     */
    @JsonIgnore
    public static QlikStream get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a QlikStream by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the QlikStream to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full QlikStream, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikStream does not exist or the provided GUID is not a QlikStream
     */
    @JsonIgnore
    public static QlikStream get(AtlanClient client, String id, boolean includeAllRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof QlikStream) {
                return (QlikStream) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof QlikStream) {
                return (QlikStream) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a QlikStream by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the QlikStream to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the QlikStream, including any relationships
     * @return the requested QlikStream, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikStream does not exist or the provided GUID is not a QlikStream
     */
    @JsonIgnore
    public static QlikStream get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a QlikStream by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the QlikStream to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the QlikStream, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the QlikStream
     * @return the requested QlikStream, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikStream does not exist or the provided GUID is not a QlikStream
     */
    @JsonIgnore
    public static QlikStream get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = QlikStream.select(client)
                    .where(QlikStream.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof QlikStream) {
                return (QlikStream) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = QlikStream.select(client)
                    .where(QlikStream.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof QlikStream) {
                return (QlikStream) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) QlikStream to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the QlikStream
     * @return true if the QlikStream is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a QlikStream.
     *
     * @param qualifiedName of the QlikStream
     * @param name of the QlikStream
     * @return the minimal request necessary to update the QlikStream, as a builder
     */
    public static QlikStreamBuilder<?, ?> updater(String qualifiedName, String name) {
        return QlikStream._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a QlikStream, from a potentially
     * more-complete QlikStream object.
     *
     * @return the minimal object necessary to update the QlikStream, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for QlikStream are not found in the initial object
     */
    @Override
    public QlikStreamBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a QlikStream.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the QlikStream
     * @param name of the QlikStream
     * @return the updated QlikStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikStream removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QlikStream) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a QlikStream.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the QlikStream
     * @param name of the QlikStream
     * @return the updated QlikStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikStream removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QlikStream) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a QlikStream.
     *
     * @param client connectivity to the Atlan tenant from which to remove the QlikStream's owners
     * @param qualifiedName of the QlikStream
     * @param name of the QlikStream
     * @return the updated QlikStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikStream removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (QlikStream) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a QlikStream.
     *
     * @param client connectivity to the Atlan tenant on which to update the QlikStream's certificate
     * @param qualifiedName of the QlikStream
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QlikStream, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikStream updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (QlikStream)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a QlikStream.
     *
     * @param client connectivity to the Atlan tenant from which to remove the QlikStream's certificate
     * @param qualifiedName of the QlikStream
     * @param name of the QlikStream
     * @return the updated QlikStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikStream removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QlikStream) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a QlikStream.
     *
     * @param client connectivity to the Atlan tenant on which to update the QlikStream's announcement
     * @param qualifiedName of the QlikStream
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikStream updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (QlikStream)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a QlikStream.
     *
     * @param client connectivity to the Atlan client from which to remove the QlikStream's announcement
     * @param qualifiedName of the QlikStream
     * @param name of the QlikStream
     * @return the updated QlikStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikStream removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QlikStream) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the QlikStream.
     *
     * @param client connectivity to the Atlan tenant on which to replace the QlikStream's assigned terms
     * @param qualifiedName for the QlikStream
     * @param name human-readable name of the QlikStream
     * @param terms the list of terms to replace on the QlikStream, or null to remove all terms from the QlikStream
     * @return the QlikStream that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QlikStream replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (QlikStream) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the QlikStream, without replacing existing terms linked to the QlikStream.
     * Note: this operation must make two API calls — one to retrieve the QlikStream's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the QlikStream
     * @param qualifiedName for the QlikStream
     * @param terms the list of terms to append to the QlikStream
     * @return the QlikStream that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static QlikStream appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (QlikStream) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a QlikStream, without replacing all existing terms linked to the QlikStream.
     * Note: this operation must make two API calls — one to retrieve the QlikStream's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the QlikStream
     * @param qualifiedName for the QlikStream
     * @param terms the list of terms to remove from the QlikStream, which must be referenced by GUID
     * @return the QlikStream that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static QlikStream removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (QlikStream) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a QlikStream, without replacing existing Atlan tags linked to the QlikStream.
     * Note: this operation must make two API calls — one to retrieve the QlikStream's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the QlikStream
     * @param qualifiedName of the QlikStream
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated QlikStream
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static QlikStream appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (QlikStream) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QlikStream, without replacing existing Atlan tags linked to the QlikStream.
     * Note: this operation must make two API calls — one to retrieve the QlikStream's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the QlikStream
     * @param qualifiedName of the QlikStream
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated QlikStream
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static QlikStream appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (QlikStream) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a QlikStream.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a QlikStream
     * @param qualifiedName of the QlikStream
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the QlikStream
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
