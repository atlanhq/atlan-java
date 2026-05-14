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
 * Intermediate harmonisation layer between a DataSource and a storage object in SAP BW (RSKSNEW).
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class SAPBWInfoSource extends Asset implements ISAPBWInfoSource, ISAPBW, ISAP, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SAPBWInfoSource";

    /** Fixed typeName for SAPBWInfoSources. */
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

    /** InfoArea containing this InfoSource. */
    @Attribute
    ISAPBWInfoArea sapBwInfoArea;

    /** Simple name of the SAP BW InfoObject asset related to this asset. */
    @Attribute
    String sapBwInfoObjectName;

    /** Unique name of the SAP BW InfoObject asset related to this asset. */
    @Attribute
    String sapBwInfoObjectQualifiedName;

    /** Fields in this InfoSource. */
    @Attribute
    @Singular
    SortedSet<ISAPBWInfoSourceField> sapBwInfoSourceFields;

    /** Length of the field in characters or bytes (e.g. RSDSSEGFD.LENG, RSKSFIELDNEW.LENG). */
    @Attribute
    Long sapBwLength;

    /** Lifecycle status of the object in SAP BW such as active, inactive, or modified (e.g. RSDAREA.OBJSTAT, RSKSNEW.OBJSTAT). */
    @Attribute
    String sapBwObjectStatus;

    /** Simple name of the SAP BW parent asset in which this asset exists. */
    @Attribute
    String sapBwParentName;

    /** Unique name of the SAP BW parent asset in which this asset exists. */
    @Attribute
    String sapBwParentQualifiedName;

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
     * Builds the minimal object necessary to create a relationship to a SAPBWInfoSource, from a potentially
     * more-complete SAPBWInfoSource object.
     *
     * @return the minimal object necessary to relate to the SAPBWInfoSource
     * @throws InvalidRequestException if any of the minimal set of required properties for a SAPBWInfoSource relationship are not found in the initial object
     */
    @Override
    public SAPBWInfoSource trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SAPBWInfoSource assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SAPBWInfoSource assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SAPBWInfoSource assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SAPBWInfoSource assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SAPBWInfoSources will be included
     * @return a fluent search that includes all SAPBWInfoSource assets
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
     * Reference to a SAPBWInfoSource by GUID. Use this to create a relationship to this SAPBWInfoSource,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SAPBWInfoSource to reference
     * @return reference to a SAPBWInfoSource that can be used for defining a relationship to a SAPBWInfoSource
     */
    public static SAPBWInfoSource refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SAPBWInfoSource by GUID. Use this to create a relationship to this SAPBWInfoSource,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SAPBWInfoSource to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SAPBWInfoSource that can be used for defining a relationship to a SAPBWInfoSource
     */
    public static SAPBWInfoSource refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SAPBWInfoSource._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SAPBWInfoSource by qualifiedName. Use this to create a relationship to this SAPBWInfoSource,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SAPBWInfoSource to reference
     * @return reference to a SAPBWInfoSource that can be used for defining a relationship to a SAPBWInfoSource
     */
    public static SAPBWInfoSource refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SAPBWInfoSource by qualifiedName. Use this to create a relationship to this SAPBWInfoSource,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SAPBWInfoSource to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SAPBWInfoSource that can be used for defining a relationship to a SAPBWInfoSource
     */
    public static SAPBWInfoSource refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SAPBWInfoSource._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SAPBWInfoSource by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SAPBWInfoSource to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SAPBWInfoSource, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SAPBWInfoSource does not exist or the provided GUID is not a SAPBWInfoSource
     */
    @JsonIgnore
    public static SAPBWInfoSource get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SAPBWInfoSource by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SAPBWInfoSource to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SAPBWInfoSource, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SAPBWInfoSource does not exist or the provided GUID is not a SAPBWInfoSource
     */
    @JsonIgnore
    public static SAPBWInfoSource get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SAPBWInfoSource) {
                return (SAPBWInfoSource) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof SAPBWInfoSource) {
                return (SAPBWInfoSource) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SAPBWInfoSource by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SAPBWInfoSource to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SAPBWInfoSource, including any relationships
     * @return the requested SAPBWInfoSource, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SAPBWInfoSource does not exist or the provided GUID is not a SAPBWInfoSource
     */
    @JsonIgnore
    public static SAPBWInfoSource get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a SAPBWInfoSource by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SAPBWInfoSource to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SAPBWInfoSource, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the SAPBWInfoSource
     * @return the requested SAPBWInfoSource, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SAPBWInfoSource does not exist or the provided GUID is not a SAPBWInfoSource
     */
    @JsonIgnore
    public static SAPBWInfoSource get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = SAPBWInfoSource.select(client)
                    .where(SAPBWInfoSource.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof SAPBWInfoSource) {
                return (SAPBWInfoSource) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = SAPBWInfoSource.select(client)
                    .where(SAPBWInfoSource.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof SAPBWInfoSource) {
                return (SAPBWInfoSource) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SAPBWInfoSource to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SAPBWInfoSource
     * @return true if the SAPBWInfoSource is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SAPBWInfoSource.
     *
     * @param qualifiedName of the SAPBWInfoSource
     * @param name of the SAPBWInfoSource
     * @return the minimal request necessary to update the SAPBWInfoSource, as a builder
     */
    public static SAPBWInfoSourceBuilder<?, ?> updater(String qualifiedName, String name) {
        return SAPBWInfoSource._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SAPBWInfoSource,
     * from a potentially more-complete SAPBWInfoSource object.
     *
     * @return the minimal object necessary to update the SAPBWInfoSource, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a SAPBWInfoSource are not present in the initial object
     */
    @Override
    public SAPBWInfoSourceBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class SAPBWInfoSourceBuilder<
                    C extends SAPBWInfoSource, B extends SAPBWInfoSourceBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a SAPBWInfoSource.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SAPBWInfoSource
     * @param name of the SAPBWInfoSource
     * @return the updated SAPBWInfoSource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SAPBWInfoSource removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SAPBWInfoSource) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SAPBWInfoSource.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SAPBWInfoSource
     * @param name of the SAPBWInfoSource
     * @return the updated SAPBWInfoSource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SAPBWInfoSource removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SAPBWInfoSource) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SAPBWInfoSource.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SAPBWInfoSource's owners
     * @param qualifiedName of the SAPBWInfoSource
     * @param name of the SAPBWInfoSource
     * @return the updated SAPBWInfoSource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SAPBWInfoSource removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SAPBWInfoSource) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SAPBWInfoSource.
     *
     * @param client connectivity to the Atlan tenant on which to update the SAPBWInfoSource's certificate
     * @param qualifiedName of the SAPBWInfoSource
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SAPBWInfoSource, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SAPBWInfoSource updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SAPBWInfoSource)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SAPBWInfoSource.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SAPBWInfoSource's certificate
     * @param qualifiedName of the SAPBWInfoSource
     * @param name of the SAPBWInfoSource
     * @return the updated SAPBWInfoSource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SAPBWInfoSource removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SAPBWInfoSource) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SAPBWInfoSource.
     *
     * @param client connectivity to the Atlan tenant on which to update the SAPBWInfoSource's announcement
     * @param qualifiedName of the SAPBWInfoSource
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SAPBWInfoSource updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SAPBWInfoSource)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SAPBWInfoSource.
     *
     * @param client connectivity to the Atlan client from which to remove the SAPBWInfoSource's announcement
     * @param qualifiedName of the SAPBWInfoSource
     * @param name of the SAPBWInfoSource
     * @return the updated SAPBWInfoSource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SAPBWInfoSource removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SAPBWInfoSource) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SAPBWInfoSource.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SAPBWInfoSource's assigned terms
     * @param qualifiedName for the SAPBWInfoSource
     * @param name human-readable name of the SAPBWInfoSource
     * @param terms the list of terms to replace on the SAPBWInfoSource, or null to remove all terms from the SAPBWInfoSource
     * @return the SAPBWInfoSource that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SAPBWInfoSource replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SAPBWInfoSource) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SAPBWInfoSource, without replacing existing terms linked to the SAPBWInfoSource.
     * Note: this operation must make two API calls — one to retrieve the SAPBWInfoSource's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SAPBWInfoSource
     * @param qualifiedName for the SAPBWInfoSource
     * @param terms the list of terms to append to the SAPBWInfoSource
     * @return the SAPBWInfoSource that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SAPBWInfoSource appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SAPBWInfoSource) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SAPBWInfoSource, without replacing all existing terms linked to the SAPBWInfoSource.
     * Note: this operation must make two API calls — one to retrieve the SAPBWInfoSource's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SAPBWInfoSource
     * @param qualifiedName for the SAPBWInfoSource
     * @param terms the list of terms to remove from the SAPBWInfoSource, which must be referenced by GUID
     * @return the SAPBWInfoSource that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SAPBWInfoSource removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SAPBWInfoSource) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SAPBWInfoSource, without replacing existing Atlan tags linked to the SAPBWInfoSource.
     * Note: this operation must make two API calls — one to retrieve the SAPBWInfoSource's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SAPBWInfoSource
     * @param qualifiedName of the SAPBWInfoSource
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SAPBWInfoSource
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static SAPBWInfoSource appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SAPBWInfoSource) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SAPBWInfoSource, without replacing existing Atlan tags linked to the SAPBWInfoSource.
     * Note: this operation must make two API calls — one to retrieve the SAPBWInfoSource's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SAPBWInfoSource
     * @param qualifiedName of the SAPBWInfoSource
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SAPBWInfoSource
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static SAPBWInfoSource appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SAPBWInfoSource) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SAPBWInfoSource.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SAPBWInfoSource
     * @param qualifiedName of the SAPBWInfoSource
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SAPBWInfoSource
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
