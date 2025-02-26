/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
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
 * Instances of DataverseAttribute in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class DataverseAttribute extends Asset
        implements IDataverseAttribute, IDataverse, ISaaS, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DataverseAttribute";

    /** Fixed typeName for DataverseAttributes. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Indicator if DataverseAttribute is the primary key. */
    @Attribute
    Boolean dataverseAttributeIsPrimaryId;

    /** Indicator if DataverseAttribute is searchable. */
    @Attribute
    Boolean dataverseAttributeIsSearchable;

    /** Schema Name of the DataverseAttribute. */
    @Attribute
    String dataverseAttributeSchemaName;

    /** Type of the DataverseAttribute. */
    @Attribute
    String dataverseAttributeType;

    /** DataverseEntity asset containing this DataverseAttribute. */
    @Attribute
    IDataverseEntity dataverseEntity;

    /** Entity Qualified Name of the DataverseAttribute. */
    @Attribute
    String dataverseEntityQualifiedName;

    /** Indicator if DataverseEntity has auditing enabled. */
    @Attribute
    Boolean dataverseIsAuditEnabled;

    /** Indicator if DataverseEntity is custom built. */
    @Attribute
    Boolean dataverseIsCustom;

    /** Indicator if DataverseEntity is customizable. */
    @Attribute
    Boolean dataverseIsCustomizable;

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

    /**
     * Builds the minimal object necessary to create a relationship to a DataverseAttribute, from a potentially
     * more-complete DataverseAttribute object.
     *
     * @return the minimal object necessary to relate to the DataverseAttribute
     * @throws InvalidRequestException if any of the minimal set of required properties for a DataverseAttribute relationship are not found in the initial object
     */
    @Override
    public DataverseAttribute trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all DataverseAttribute assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DataverseAttribute assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all DataverseAttribute assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all DataverseAttribute assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DataverseAttributes will be included
     * @return a fluent search that includes all DataverseAttribute assets
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
     * Reference to a DataverseAttribute by GUID. Use this to create a relationship to this DataverseAttribute,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the DataverseAttribute to reference
     * @return reference to a DataverseAttribute that can be used for defining a relationship to a DataverseAttribute
     */
    public static DataverseAttribute refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DataverseAttribute by GUID. Use this to create a relationship to this DataverseAttribute,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the DataverseAttribute to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DataverseAttribute that can be used for defining a relationship to a DataverseAttribute
     */
    public static DataverseAttribute refByGuid(String guid, Reference.SaveSemantic semantic) {
        return DataverseAttribute._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a DataverseAttribute by qualifiedName. Use this to create a relationship to this DataverseAttribute,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the DataverseAttribute to reference
     * @return reference to a DataverseAttribute that can be used for defining a relationship to a DataverseAttribute
     */
    public static DataverseAttribute refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DataverseAttribute by qualifiedName. Use this to create a relationship to this DataverseAttribute,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the DataverseAttribute to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DataverseAttribute that can be used for defining a relationship to a DataverseAttribute
     */
    public static DataverseAttribute refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return DataverseAttribute._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a DataverseAttribute by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DataverseAttribute to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DataverseAttribute, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataverseAttribute does not exist or the provided GUID is not a DataverseAttribute
     */
    @JsonIgnore
    public static DataverseAttribute get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a DataverseAttribute by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DataverseAttribute to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full DataverseAttribute, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataverseAttribute does not exist or the provided GUID is not a DataverseAttribute
     */
    @JsonIgnore
    public static DataverseAttribute get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof DataverseAttribute) {
                return (DataverseAttribute) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof DataverseAttribute) {
                return (DataverseAttribute) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a DataverseAttribute by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DataverseAttribute to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DataverseAttribute, including any relationships
     * @return the requested DataverseAttribute, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataverseAttribute does not exist or the provided GUID is not a DataverseAttribute
     */
    @JsonIgnore
    public static DataverseAttribute get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a DataverseAttribute by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DataverseAttribute to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DataverseAttribute, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the DataverseAttribute
     * @return the requested DataverseAttribute, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataverseAttribute does not exist or the provided GUID is not a DataverseAttribute
     */
    @JsonIgnore
    public static DataverseAttribute get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = DataverseAttribute.select(client)
                    .where(DataverseAttribute.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof DataverseAttribute) {
                return (DataverseAttribute) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = DataverseAttribute.select(client)
                    .where(DataverseAttribute.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof DataverseAttribute) {
                return (DataverseAttribute) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) DataverseAttribute to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DataverseAttribute
     * @return true if the DataverseAttribute is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a DataverseAttribute.
     *
     * @param name of the attribute
     * @param entity in which the attribute should be created, which must have at least
     *                 a qualifiedName
     * @return the minimal request necessary to create the attribute, as a builder
     * @throws InvalidRequestException if the entity provided is without a qualifiedName
     */
    public static DataverseAttribute.DataverseAttributeBuilder<?, ?> creator(String name, DataverseEntity entity)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("entityQualifiedName", entity.getQualifiedName());
        map.put("entityName", entity.getName());
        map.put("connectionQualifiedName", entity.getConnectionQualifiedName());
        validateRelationship(DataverseEntity.TYPE_NAME, map);
        return creator(name, entity.getConnectionQualifiedName(), entity.getQualifiedName())
                .dataverseEntity(entity.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a DataverseAttribute.
     *
     * @param name of the attribute
     * @param entityQualifiedName unique name of the entity in which this attribute exists
     * @return the minimal request necessary to create the attribute, as a builder
     */
    public static DataverseAttribute.DataverseAttributeBuilder<?, ?> creator(String name, String entityQualifiedName) {
        String entityName = StringUtils.getNameFromQualifiedName(entityQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(entityQualifiedName);
        return creator(name, connectionQualifiedName, entityQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a DataverseAttribute.
     *
     * @param name of the attribute
     * @param connectionQualifiedName unique name of the connection in which to create the attribute
     * @param entityQualifiedName unique name of the entity in which to create the attribute
     * @return the minimal request necessary to create the attribute, as a builder
     */
    public static DataverseAttribute.DataverseAttributeBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String entityQualifiedName) {
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return DataverseAttribute._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, entityQualifiedName))
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(connectorType)
                .dataverseEntityQualifiedName(entityQualifiedName)
                .dataverseEntity(DataverseEntity.refByQualifiedName(entityQualifiedName));
    }

    /**
     * Generate a unique attribute name.
     *
     * @param name of the attribute
     * @param entityQualifiedName unique name of the entity in which this attribute exists
     * @return a unique name for the attribute
     */
    public static String generateQualifiedName(String name, String entityQualifiedName) {
        return entityQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a DataverseAttribute.
     *
     * @param qualifiedName of the DataverseAttribute
     * @param name of the DataverseAttribute
     * @return the minimal request necessary to update the DataverseAttribute, as a builder
     */
    public static DataverseAttributeBuilder<?, ?> updater(String qualifiedName, String name) {
        return DataverseAttribute._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DataverseAttribute, from a potentially
     * more-complete DataverseAttribute object.
     *
     * @return the minimal object necessary to update the DataverseAttribute, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DataverseAttribute are not found in the initial object
     */
    @Override
    public DataverseAttributeBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a DataverseAttribute.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DataverseAttribute
     * @param name of the DataverseAttribute
     * @return the updated DataverseAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataverseAttribute removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DataverseAttribute) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DataverseAttribute.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DataverseAttribute
     * @param name of the DataverseAttribute
     * @return the updated DataverseAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataverseAttribute removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DataverseAttribute) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DataverseAttribute.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DataverseAttribute's owners
     * @param qualifiedName of the DataverseAttribute
     * @param name of the DataverseAttribute
     * @return the updated DataverseAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataverseAttribute removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DataverseAttribute) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DataverseAttribute.
     *
     * @param client connectivity to the Atlan tenant on which to update the DataverseAttribute's certificate
     * @param qualifiedName of the DataverseAttribute
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DataverseAttribute, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataverseAttribute updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DataverseAttribute)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DataverseAttribute.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DataverseAttribute's certificate
     * @param qualifiedName of the DataverseAttribute
     * @param name of the DataverseAttribute
     * @return the updated DataverseAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataverseAttribute removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DataverseAttribute) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DataverseAttribute.
     *
     * @param client connectivity to the Atlan tenant on which to update the DataverseAttribute's announcement
     * @param qualifiedName of the DataverseAttribute
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataverseAttribute updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (DataverseAttribute)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DataverseAttribute.
     *
     * @param client connectivity to the Atlan client from which to remove the DataverseAttribute's announcement
     * @param qualifiedName of the DataverseAttribute
     * @param name of the DataverseAttribute
     * @return the updated DataverseAttribute, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataverseAttribute removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DataverseAttribute) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DataverseAttribute.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DataverseAttribute's assigned terms
     * @param qualifiedName for the DataverseAttribute
     * @param name human-readable name of the DataverseAttribute
     * @param terms the list of terms to replace on the DataverseAttribute, or null to remove all terms from the DataverseAttribute
     * @return the DataverseAttribute that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DataverseAttribute replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (DataverseAttribute) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DataverseAttribute, without replacing existing terms linked to the DataverseAttribute.
     * Note: this operation must make two API calls — one to retrieve the DataverseAttribute's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DataverseAttribute
     * @param qualifiedName for the DataverseAttribute
     * @param terms the list of terms to append to the DataverseAttribute
     * @return the DataverseAttribute that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DataverseAttribute appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DataverseAttribute) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DataverseAttribute, without replacing all existing terms linked to the DataverseAttribute.
     * Note: this operation must make two API calls — one to retrieve the DataverseAttribute's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DataverseAttribute
     * @param qualifiedName for the DataverseAttribute
     * @param terms the list of terms to remove from the DataverseAttribute, which must be referenced by GUID
     * @return the DataverseAttribute that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DataverseAttribute removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DataverseAttribute) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DataverseAttribute, without replacing existing Atlan tags linked to the DataverseAttribute.
     * Note: this operation must make two API calls — one to retrieve the DataverseAttribute's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DataverseAttribute
     * @param qualifiedName of the DataverseAttribute
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DataverseAttribute
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static DataverseAttribute appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (DataverseAttribute) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DataverseAttribute, without replacing existing Atlan tags linked to the DataverseAttribute.
     * Note: this operation must make two API calls — one to retrieve the DataverseAttribute's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DataverseAttribute
     * @param qualifiedName of the DataverseAttribute
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DataverseAttribute
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static DataverseAttribute appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DataverseAttribute) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a DataverseAttribute.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DataverseAttribute
     * @param qualifiedName of the DataverseAttribute
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DataverseAttribute
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
