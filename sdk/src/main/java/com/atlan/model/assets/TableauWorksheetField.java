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
 * Instance of a Tableau worksheet field in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class TableauWorksheetField extends Asset
        implements ITableauWorksheetField, ITableau, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TableauWorksheetField";

    /** Fixed typeName for TableauWorksheetFields. */
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

    /** Calculated field this worksheet field uses. */
    @Attribute
    ITableauCalculatedField tableauCalculatedField;

    /** Worksheet field used by this dashboard field. */
    @Attribute
    ITableauDashboardField tableauDashboardField;

    /** Datasource field this worksheet field uses. */
    @Attribute
    ITableauDatasourceField tableauDatasourceField;

    /** Name used internally in Tableau to uniquely identify this field. */
    @Attribute
    String tableauFullyQualifiedName;

    /** List of top-level projects and their nested child projects. */
    @Attribute
    @Singular("addTableauProjectHierarchy")
    List<Map<String, String>> tableauProjectHierarchy;

    /** Unique name of the project in which this worksheet field exists. */
    @Attribute
    String tableauProjectQualifiedName;

    /** Unique name of the site in which this worksheet field exists. */
    @Attribute
    String tableauSiteQualifiedName;

    /** Unique name of the top-level project in which this worksheet field exists. */
    @Attribute
    String tableauTopLevelProjectQualifiedName;

    /** Unique name of the workbook in which this worksheet field exists. */
    @Attribute
    String tableauWorkbookQualifiedName;

    /** Worksheet in which this field exists. */
    @Attribute
    ITableauWorksheet tableauWorksheet;

    /** Bin size of this field. */
    @Attribute
    String tableauWorksheetFieldBinSize;

    /** Data category of this field. */
    @Attribute
    String tableauWorksheetFieldDataCategory;

    /** Data type of this field. */
    @Attribute
    String tableauWorksheetFieldDataType;

    /** Formula for this field. */
    @Attribute
    String tableauWorksheetFieldFormula;

    /** Role of this field, for example: 'dimension', 'measure', or 'unknown'. */
    @Attribute
    String tableauWorksheetFieldRole;

    /** Type of this worksheet field. */
    @Attribute
    String tableauWorksheetFieldType;

    /** Columns upstream to this field. */
    @Attribute
    @Singular
    List<Map<String, String>> tableauWorksheetFieldUpstreamColumns;

    /** Fields upstream to this field. */
    @Attribute
    @Singular
    List<Map<String, String>> tableauWorksheetFieldUpstreamFields;

    /** Tables upstream to this worksheet field. */
    @Attribute
    @Singular
    List<Map<String, String>> tableauWorksheetFieldUpstreamTables;

    /** Unique name of the datasource in which this worksheet field exists. */
    @Attribute
    String tableauWorksheetQualifiedName;

    /**
     * Builds the minimal object necessary to create a relationship to a TableauWorksheetField, from a potentially
     * more-complete TableauWorksheetField object.
     *
     * @return the minimal object necessary to relate to the TableauWorksheetField
     * @throws InvalidRequestException if any of the minimal set of required properties for a TableauWorksheetField relationship are not found in the initial object
     */
    @Override
    public TableauWorksheetField trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all TableauWorksheetField assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) TableauWorksheetField assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all TableauWorksheetField assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all TableauWorksheetField assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) TableauWorksheetFields will be included
     * @return a fluent search that includes all TableauWorksheetField assets
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
     * Reference to a TableauWorksheetField by GUID. Use this to create a relationship to this TableauWorksheetField,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the TableauWorksheetField to reference
     * @return reference to a TableauWorksheetField that can be used for defining a relationship to a TableauWorksheetField
     */
    public static TableauWorksheetField refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a TableauWorksheetField by GUID. Use this to create a relationship to this TableauWorksheetField,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the TableauWorksheetField to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a TableauWorksheetField that can be used for defining a relationship to a TableauWorksheetField
     */
    public static TableauWorksheetField refByGuid(String guid, Reference.SaveSemantic semantic) {
        return TableauWorksheetField._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a TableauWorksheetField by qualifiedName. Use this to create a relationship to this TableauWorksheetField,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the TableauWorksheetField to reference
     * @return reference to a TableauWorksheetField that can be used for defining a relationship to a TableauWorksheetField
     */
    public static TableauWorksheetField refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a TableauWorksheetField by qualifiedName. Use this to create a relationship to this TableauWorksheetField,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the TableauWorksheetField to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a TableauWorksheetField that can be used for defining a relationship to a TableauWorksheetField
     */
    public static TableauWorksheetField refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return TableauWorksheetField._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a TableauWorksheetField by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the TableauWorksheetField to retrieve, either its GUID or its full qualifiedName
     * @return the requested full TableauWorksheetField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauWorksheetField does not exist or the provided GUID is not a TableauWorksheetField
     */
    @JsonIgnore
    public static TableauWorksheetField get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a TableauWorksheetField by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the TableauWorksheetField to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full TableauWorksheetField, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauWorksheetField does not exist or the provided GUID is not a TableauWorksheetField
     */
    @JsonIgnore
    public static TableauWorksheetField get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof TableauWorksheetField) {
                return (TableauWorksheetField) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof TableauWorksheetField) {
                return (TableauWorksheetField) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a TableauWorksheetField by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the TableauWorksheetField to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the TableauWorksheetField, including any relationships
     * @return the requested TableauWorksheetField, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauWorksheetField does not exist or the provided GUID is not a TableauWorksheetField
     */
    @JsonIgnore
    public static TableauWorksheetField get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a TableauWorksheetField by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the TableauWorksheetField to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the TableauWorksheetField, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the TableauWorksheetField
     * @return the requested TableauWorksheetField, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauWorksheetField does not exist or the provided GUID is not a TableauWorksheetField
     */
    @JsonIgnore
    public static TableauWorksheetField get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = TableauWorksheetField.select(client)
                    .where(TableauWorksheetField.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof TableauWorksheetField) {
                return (TableauWorksheetField) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = TableauWorksheetField.select(client)
                    .where(TableauWorksheetField.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof TableauWorksheetField) {
                return (TableauWorksheetField) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) TableauWorksheetField to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the TableauWorksheetField
     * @return true if the TableauWorksheetField is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a TableauWorksheetField.
     *
     * @param qualifiedName of the TableauWorksheetField
     * @param name of the TableauWorksheetField
     * @return the minimal request necessary to update the TableauWorksheetField, as a builder
     */
    public static TableauWorksheetFieldBuilder<?, ?> updater(String qualifiedName, String name) {
        return TableauWorksheetField._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TableauWorksheetField, from a potentially
     * more-complete TableauWorksheetField object.
     *
     * @return the minimal object necessary to update the TableauWorksheetField, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TableauWorksheetField are not found in the initial object
     */
    @Override
    public TableauWorksheetFieldBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class TableauWorksheetFieldBuilder<
                    C extends TableauWorksheetField, B extends TableauWorksheetFieldBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a TableauWorksheetField.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the TableauWorksheetField
     * @param name of the TableauWorksheetField
     * @return the updated TableauWorksheetField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheetField removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauWorksheetField) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a TableauWorksheetField.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the TableauWorksheetField
     * @param name of the TableauWorksheetField
     * @return the updated TableauWorksheetField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheetField removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauWorksheetField) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a TableauWorksheetField.
     *
     * @param client connectivity to the Atlan tenant from which to remove the TableauWorksheetField's owners
     * @param qualifiedName of the TableauWorksheetField
     * @param name of the TableauWorksheetField
     * @return the updated TableauWorksheetField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheetField removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauWorksheetField) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a TableauWorksheetField.
     *
     * @param client connectivity to the Atlan tenant on which to update the TableauWorksheetField's certificate
     * @param qualifiedName of the TableauWorksheetField
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TableauWorksheetField, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheetField updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (TableauWorksheetField)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a TableauWorksheetField.
     *
     * @param client connectivity to the Atlan tenant from which to remove the TableauWorksheetField's certificate
     * @param qualifiedName of the TableauWorksheetField
     * @param name of the TableauWorksheetField
     * @return the updated TableauWorksheetField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheetField removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauWorksheetField) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a TableauWorksheetField.
     *
     * @param client connectivity to the Atlan tenant on which to update the TableauWorksheetField's announcement
     * @param qualifiedName of the TableauWorksheetField
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheetField updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (TableauWorksheetField)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a TableauWorksheetField.
     *
     * @param client connectivity to the Atlan client from which to remove the TableauWorksheetField's announcement
     * @param qualifiedName of the TableauWorksheetField
     * @param name of the TableauWorksheetField
     * @return the updated TableauWorksheetField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheetField removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauWorksheetField) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the TableauWorksheetField.
     *
     * @param client connectivity to the Atlan tenant on which to replace the TableauWorksheetField's assigned terms
     * @param qualifiedName for the TableauWorksheetField
     * @param name human-readable name of the TableauWorksheetField
     * @param terms the list of terms to replace on the TableauWorksheetField, or null to remove all terms from the TableauWorksheetField
     * @return the TableauWorksheetField that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TableauWorksheetField replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (TableauWorksheetField) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the TableauWorksheetField, without replacing existing terms linked to the TableauWorksheetField.
     * Note: this operation must make two API calls — one to retrieve the TableauWorksheetField's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the TableauWorksheetField
     * @param qualifiedName for the TableauWorksheetField
     * @param terms the list of terms to append to the TableauWorksheetField
     * @return the TableauWorksheetField that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static TableauWorksheetField appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (TableauWorksheetField) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a TableauWorksheetField, without replacing all existing terms linked to the TableauWorksheetField.
     * Note: this operation must make two API calls — one to retrieve the TableauWorksheetField's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the TableauWorksheetField
     * @param qualifiedName for the TableauWorksheetField
     * @param terms the list of terms to remove from the TableauWorksheetField, which must be referenced by GUID
     * @return the TableauWorksheetField that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static TableauWorksheetField removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (TableauWorksheetField) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a TableauWorksheetField, without replacing existing Atlan tags linked to the TableauWorksheetField.
     * Note: this operation must make two API calls — one to retrieve the TableauWorksheetField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the TableauWorksheetField
     * @param qualifiedName of the TableauWorksheetField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated TableauWorksheetField
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static TableauWorksheetField appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (TableauWorksheetField) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TableauWorksheetField, without replacing existing Atlan tags linked to the TableauWorksheetField.
     * Note: this operation must make two API calls — one to retrieve the TableauWorksheetField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the TableauWorksheetField
     * @param qualifiedName of the TableauWorksheetField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated TableauWorksheetField
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static TableauWorksheetField appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (TableauWorksheetField) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a TableauWorksheetField.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a TableauWorksheetField
     * @param qualifiedName of the TableauWorksheetField
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the TableauWorksheetField
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
