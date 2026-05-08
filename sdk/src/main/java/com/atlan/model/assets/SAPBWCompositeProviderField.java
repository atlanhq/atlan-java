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
 * Column or dimension exposed by a SAP BW CompositeProvider (RSOHCPRT).
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class SAPBWCompositeProviderField extends Asset
        implements ISAPBWCompositeProviderField, ISAPBW, ISAP, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SAPBWCompositeProviderField";

    /** Fixed typeName for SAPBWCompositeProviderFields. */
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

    /** CompositeProvider exposing this field. */
    @Attribute
    ISAPBWCompositeProvider sapBwCompositeProvider;

    /** Simple name of the SAP BW InfoObject asset related to this asset. */
    @Attribute
    String sapBwInfoObjectName;

    /** Unique name of the SAP BW InfoObject asset related to this asset. */
    @Attribute
    String sapBwInfoObjectQualifiedName;

    /** InfoObject (data element) referenced by this CompositeProvider field. */
    @Attribute
    @Singular
    SortedSet<ISAPBWInfoObject> sapBwInfoObjects;

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
     * Builds the minimal object necessary to create a relationship to a SAPBWCompositeProviderField, from a potentially
     * more-complete SAPBWCompositeProviderField object.
     *
     * @return the minimal object necessary to relate to the SAPBWCompositeProviderField
     * @throws InvalidRequestException if any of the minimal set of required properties for a SAPBWCompositeProviderField relationship are not found in the initial object
     */
    @Override
    public SAPBWCompositeProviderField trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SAPBWCompositeProviderField assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SAPBWCompositeProviderField assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SAPBWCompositeProviderField assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SAPBWCompositeProviderField assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SAPBWCompositeProviderFields will be included
     * @return a fluent search that includes all SAPBWCompositeProviderField assets
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
     * Reference to a SAPBWCompositeProviderField by GUID. Use this to create a relationship to this SAPBWCompositeProviderField,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SAPBWCompositeProviderField to reference
     * @return reference to a SAPBWCompositeProviderField that can be used for defining a relationship to a SAPBWCompositeProviderField
     */
    public static SAPBWCompositeProviderField refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SAPBWCompositeProviderField by GUID. Use this to create a relationship to this SAPBWCompositeProviderField,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SAPBWCompositeProviderField to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SAPBWCompositeProviderField that can be used for defining a relationship to a SAPBWCompositeProviderField
     */
    public static SAPBWCompositeProviderField refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SAPBWCompositeProviderField._internal()
                .guid(guid)
                .semantic(semantic)
                .build();
    }

    /**
     * Reference to a SAPBWCompositeProviderField by qualifiedName. Use this to create a relationship to this SAPBWCompositeProviderField,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SAPBWCompositeProviderField to reference
     * @return reference to a SAPBWCompositeProviderField that can be used for defining a relationship to a SAPBWCompositeProviderField
     */
    public static SAPBWCompositeProviderField refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SAPBWCompositeProviderField by qualifiedName. Use this to create a relationship to this SAPBWCompositeProviderField,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SAPBWCompositeProviderField to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SAPBWCompositeProviderField that can be used for defining a relationship to a SAPBWCompositeProviderField
     */
    public static SAPBWCompositeProviderField refByQualifiedName(
            String qualifiedName, Reference.SaveSemantic semantic) {
        return SAPBWCompositeProviderField._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SAPBWCompositeProviderField by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SAPBWCompositeProviderField to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SAPBWCompositeProviderField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SAPBWCompositeProviderField does not exist or the provided GUID is not a SAPBWCompositeProviderField
     */
    @JsonIgnore
    public static SAPBWCompositeProviderField get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SAPBWCompositeProviderField by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SAPBWCompositeProviderField to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SAPBWCompositeProviderField, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SAPBWCompositeProviderField does not exist or the provided GUID is not a SAPBWCompositeProviderField
     */
    @JsonIgnore
    public static SAPBWCompositeProviderField get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SAPBWCompositeProviderField) {
                return (SAPBWCompositeProviderField) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof SAPBWCompositeProviderField) {
                return (SAPBWCompositeProviderField) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SAPBWCompositeProviderField by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SAPBWCompositeProviderField to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SAPBWCompositeProviderField, including any relationships
     * @return the requested SAPBWCompositeProviderField, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SAPBWCompositeProviderField does not exist or the provided GUID is not a SAPBWCompositeProviderField
     */
    @JsonIgnore
    public static SAPBWCompositeProviderField get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a SAPBWCompositeProviderField by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SAPBWCompositeProviderField to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SAPBWCompositeProviderField, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the SAPBWCompositeProviderField
     * @return the requested SAPBWCompositeProviderField, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SAPBWCompositeProviderField does not exist or the provided GUID is not a SAPBWCompositeProviderField
     */
    @JsonIgnore
    public static SAPBWCompositeProviderField get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = SAPBWCompositeProviderField.select(client)
                    .where(SAPBWCompositeProviderField.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof SAPBWCompositeProviderField) {
                return (SAPBWCompositeProviderField) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = SAPBWCompositeProviderField.select(client)
                    .where(SAPBWCompositeProviderField.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof SAPBWCompositeProviderField) {
                return (SAPBWCompositeProviderField) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SAPBWCompositeProviderField to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SAPBWCompositeProviderField
     * @return true if the SAPBWCompositeProviderField is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SAPBWCompositeProviderField.
     *
     * @param qualifiedName of the SAPBWCompositeProviderField
     * @param name of the SAPBWCompositeProviderField
     * @return the minimal request necessary to update the SAPBWCompositeProviderField, as a builder
     */
    public static SAPBWCompositeProviderFieldBuilder<?, ?> updater(String qualifiedName, String name) {
        return SAPBWCompositeProviderField._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SAPBWCompositeProviderField,
     * from a potentially more-complete SAPBWCompositeProviderField object.
     *
     * @return the minimal object necessary to update the SAPBWCompositeProviderField, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a SAPBWCompositeProviderField are not present in the initial object
     */
    @Override
    public SAPBWCompositeProviderFieldBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class SAPBWCompositeProviderFieldBuilder<
                    C extends SAPBWCompositeProviderField, B extends SAPBWCompositeProviderFieldBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a SAPBWCompositeProviderField.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SAPBWCompositeProviderField
     * @param name of the SAPBWCompositeProviderField
     * @return the updated SAPBWCompositeProviderField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SAPBWCompositeProviderField removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SAPBWCompositeProviderField) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SAPBWCompositeProviderField.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SAPBWCompositeProviderField
     * @param name of the SAPBWCompositeProviderField
     * @return the updated SAPBWCompositeProviderField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SAPBWCompositeProviderField removeUserDescription(
            AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (SAPBWCompositeProviderField) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SAPBWCompositeProviderField.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SAPBWCompositeProviderField's owners
     * @param qualifiedName of the SAPBWCompositeProviderField
     * @param name of the SAPBWCompositeProviderField
     * @return the updated SAPBWCompositeProviderField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SAPBWCompositeProviderField removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SAPBWCompositeProviderField) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SAPBWCompositeProviderField.
     *
     * @param client connectivity to the Atlan tenant on which to update the SAPBWCompositeProviderField's certificate
     * @param qualifiedName of the SAPBWCompositeProviderField
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SAPBWCompositeProviderField, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SAPBWCompositeProviderField updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SAPBWCompositeProviderField)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SAPBWCompositeProviderField.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SAPBWCompositeProviderField's certificate
     * @param qualifiedName of the SAPBWCompositeProviderField
     * @param name of the SAPBWCompositeProviderField
     * @return the updated SAPBWCompositeProviderField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SAPBWCompositeProviderField removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SAPBWCompositeProviderField) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SAPBWCompositeProviderField.
     *
     * @param client connectivity to the Atlan tenant on which to update the SAPBWCompositeProviderField's announcement
     * @param qualifiedName of the SAPBWCompositeProviderField
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SAPBWCompositeProviderField updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SAPBWCompositeProviderField)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SAPBWCompositeProviderField.
     *
     * @param client connectivity to the Atlan client from which to remove the SAPBWCompositeProviderField's announcement
     * @param qualifiedName of the SAPBWCompositeProviderField
     * @param name of the SAPBWCompositeProviderField
     * @return the updated SAPBWCompositeProviderField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SAPBWCompositeProviderField removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SAPBWCompositeProviderField) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SAPBWCompositeProviderField.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SAPBWCompositeProviderField's assigned terms
     * @param qualifiedName for the SAPBWCompositeProviderField
     * @param name human-readable name of the SAPBWCompositeProviderField
     * @param terms the list of terms to replace on the SAPBWCompositeProviderField, or null to remove all terms from the SAPBWCompositeProviderField
     * @return the SAPBWCompositeProviderField that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SAPBWCompositeProviderField replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SAPBWCompositeProviderField) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SAPBWCompositeProviderField, without replacing existing terms linked to the SAPBWCompositeProviderField.
     * Note: this operation must make two API calls — one to retrieve the SAPBWCompositeProviderField's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SAPBWCompositeProviderField
     * @param qualifiedName for the SAPBWCompositeProviderField
     * @param terms the list of terms to append to the SAPBWCompositeProviderField
     * @return the SAPBWCompositeProviderField that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SAPBWCompositeProviderField appendTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (SAPBWCompositeProviderField) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SAPBWCompositeProviderField, without replacing all existing terms linked to the SAPBWCompositeProviderField.
     * Note: this operation must make two API calls — one to retrieve the SAPBWCompositeProviderField's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SAPBWCompositeProviderField
     * @param qualifiedName for the SAPBWCompositeProviderField
     * @param terms the list of terms to remove from the SAPBWCompositeProviderField, which must be referenced by GUID
     * @return the SAPBWCompositeProviderField that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SAPBWCompositeProviderField removeTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (SAPBWCompositeProviderField) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SAPBWCompositeProviderField, without replacing existing Atlan tags linked to the SAPBWCompositeProviderField.
     * Note: this operation must make two API calls — one to retrieve the SAPBWCompositeProviderField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SAPBWCompositeProviderField
     * @param qualifiedName of the SAPBWCompositeProviderField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SAPBWCompositeProviderField
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static SAPBWCompositeProviderField appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (SAPBWCompositeProviderField) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SAPBWCompositeProviderField, without replacing existing Atlan tags linked to the SAPBWCompositeProviderField.
     * Note: this operation must make two API calls — one to retrieve the SAPBWCompositeProviderField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SAPBWCompositeProviderField
     * @param qualifiedName of the SAPBWCompositeProviderField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SAPBWCompositeProviderField
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static SAPBWCompositeProviderField appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SAPBWCompositeProviderField) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SAPBWCompositeProviderField.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SAPBWCompositeProviderField
     * @param qualifiedName of the SAPBWCompositeProviderField
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SAPBWCompositeProviderField
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
