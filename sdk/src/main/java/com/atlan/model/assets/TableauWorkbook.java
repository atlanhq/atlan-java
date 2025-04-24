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
 * Instance of a Tableau workbook in Atlan. These contain one or more worksheets, datasources or dashboards.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class TableauWorkbook extends Asset
        implements ITableauWorkbook, ITableau, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TableauWorkbook";

    /** Fixed typeName for TableauWorkbooks. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Dashboards that exist within this workbook. */
    @Attribute
    @Singular
    SortedSet<ITableauDashboard> dashboards;

    /** Datasources that exist within this workbook. */
    @Attribute
    @Singular
    SortedSet<ITableauDatasource> datasources;

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

    /** Project in which this workbook exists. */
    @Attribute
    ITableauProject project;

    /** List of top-level projects with their nested child projects. */
    @Attribute
    @Singular("addProjectHierarchy")
    List<Map<String, String>> projectHierarchy;

    /** Unique name of the project in which this workbook exists. */
    @Attribute
    String projectQualifiedName;

    /** Unique name of the site in which this workbook exists. */
    @Attribute
    String siteQualifiedName;

    /** Simple name of the top-level project in which this workbook exists. */
    @Attribute
    String topLevelProjectName;

    /** Unique name of the top-level project in which this workbook exists. */
    @Attribute
    String topLevelProjectQualifiedName;

    /** Worksheets that exist within this workbook. */
    @Attribute
    @Singular
    SortedSet<ITableauWorksheet> worksheets;

    /**
     * Builds the minimal object necessary to create a relationship to a TableauWorkbook, from a potentially
     * more-complete TableauWorkbook object.
     *
     * @return the minimal object necessary to relate to the TableauWorkbook
     * @throws InvalidRequestException if any of the minimal set of required properties for a TableauWorkbook relationship are not found in the initial object
     */
    @Override
    public TableauWorkbook trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all TableauWorkbook assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) TableauWorkbook assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all TableauWorkbook assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all TableauWorkbook assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) TableauWorkbooks will be included
     * @return a fluent search that includes all TableauWorkbook assets
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
     * Reference to a TableauWorkbook by GUID. Use this to create a relationship to this TableauWorkbook,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the TableauWorkbook to reference
     * @return reference to a TableauWorkbook that can be used for defining a relationship to a TableauWorkbook
     */
    public static TableauWorkbook refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a TableauWorkbook by GUID. Use this to create a relationship to this TableauWorkbook,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the TableauWorkbook to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a TableauWorkbook that can be used for defining a relationship to a TableauWorkbook
     */
    public static TableauWorkbook refByGuid(String guid, Reference.SaveSemantic semantic) {
        return TableauWorkbook._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a TableauWorkbook by qualifiedName. Use this to create a relationship to this TableauWorkbook,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the TableauWorkbook to reference
     * @return reference to a TableauWorkbook that can be used for defining a relationship to a TableauWorkbook
     */
    public static TableauWorkbook refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a TableauWorkbook by qualifiedName. Use this to create a relationship to this TableauWorkbook,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the TableauWorkbook to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a TableauWorkbook that can be used for defining a relationship to a TableauWorkbook
     */
    public static TableauWorkbook refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return TableauWorkbook._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a TableauWorkbook by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the TableauWorkbook to retrieve, either its GUID or its full qualifiedName
     * @return the requested full TableauWorkbook, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauWorkbook does not exist or the provided GUID is not a TableauWorkbook
     */
    @JsonIgnore
    public static TableauWorkbook get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a TableauWorkbook by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the TableauWorkbook to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full TableauWorkbook, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauWorkbook does not exist or the provided GUID is not a TableauWorkbook
     */
    @JsonIgnore
    public static TableauWorkbook get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof TableauWorkbook) {
                return (TableauWorkbook) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof TableauWorkbook) {
                return (TableauWorkbook) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a TableauWorkbook by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the TableauWorkbook to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the TableauWorkbook, including any relationships
     * @return the requested TableauWorkbook, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauWorkbook does not exist or the provided GUID is not a TableauWorkbook
     */
    @JsonIgnore
    public static TableauWorkbook get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a TableauWorkbook by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the TableauWorkbook to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the TableauWorkbook, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the TableauWorkbook
     * @return the requested TableauWorkbook, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauWorkbook does not exist or the provided GUID is not a TableauWorkbook
     */
    @JsonIgnore
    public static TableauWorkbook get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = TableauWorkbook.select(client)
                    .where(TableauWorkbook.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof TableauWorkbook) {
                return (TableauWorkbook) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = TableauWorkbook.select(client)
                    .where(TableauWorkbook.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof TableauWorkbook) {
                return (TableauWorkbook) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) TableauWorkbook to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the TableauWorkbook
     * @return true if the TableauWorkbook is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a TableauWorkbook.
     *
     * @param qualifiedName of the TableauWorkbook
     * @param name of the TableauWorkbook
     * @return the minimal request necessary to update the TableauWorkbook, as a builder
     */
    public static TableauWorkbookBuilder<?, ?> updater(String qualifiedName, String name) {
        return TableauWorkbook._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TableauWorkbook, from a potentially
     * more-complete TableauWorkbook object.
     *
     * @return the minimal object necessary to update the TableauWorkbook, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TableauWorkbook are not found in the initial object
     */
    @Override
    public TableauWorkbookBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a TableauWorkbook.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the TableauWorkbook
     * @param name of the TableauWorkbook
     * @return the updated TableauWorkbook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorkbook removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauWorkbook) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a TableauWorkbook.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the TableauWorkbook
     * @param name of the TableauWorkbook
     * @return the updated TableauWorkbook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorkbook removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauWorkbook) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a TableauWorkbook.
     *
     * @param client connectivity to the Atlan tenant from which to remove the TableauWorkbook's owners
     * @param qualifiedName of the TableauWorkbook
     * @param name of the TableauWorkbook
     * @return the updated TableauWorkbook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorkbook removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauWorkbook) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a TableauWorkbook.
     *
     * @param client connectivity to the Atlan tenant on which to update the TableauWorkbook's certificate
     * @param qualifiedName of the TableauWorkbook
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TableauWorkbook, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorkbook updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (TableauWorkbook)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a TableauWorkbook.
     *
     * @param client connectivity to the Atlan tenant from which to remove the TableauWorkbook's certificate
     * @param qualifiedName of the TableauWorkbook
     * @param name of the TableauWorkbook
     * @return the updated TableauWorkbook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorkbook removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauWorkbook) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a TableauWorkbook.
     *
     * @param client connectivity to the Atlan tenant on which to update the TableauWorkbook's announcement
     * @param qualifiedName of the TableauWorkbook
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorkbook updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (TableauWorkbook)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a TableauWorkbook.
     *
     * @param client connectivity to the Atlan client from which to remove the TableauWorkbook's announcement
     * @param qualifiedName of the TableauWorkbook
     * @param name of the TableauWorkbook
     * @return the updated TableauWorkbook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauWorkbook removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauWorkbook) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the TableauWorkbook.
     *
     * @param client connectivity to the Atlan tenant on which to replace the TableauWorkbook's assigned terms
     * @param qualifiedName for the TableauWorkbook
     * @param name human-readable name of the TableauWorkbook
     * @param terms the list of terms to replace on the TableauWorkbook, or null to remove all terms from the TableauWorkbook
     * @return the TableauWorkbook that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TableauWorkbook replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (TableauWorkbook) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the TableauWorkbook, without replacing existing terms linked to the TableauWorkbook.
     * Note: this operation must make two API calls — one to retrieve the TableauWorkbook's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the TableauWorkbook
     * @param qualifiedName for the TableauWorkbook
     * @param terms the list of terms to append to the TableauWorkbook
     * @return the TableauWorkbook that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static TableauWorkbook appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (TableauWorkbook) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a TableauWorkbook, without replacing all existing terms linked to the TableauWorkbook.
     * Note: this operation must make two API calls — one to retrieve the TableauWorkbook's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the TableauWorkbook
     * @param qualifiedName for the TableauWorkbook
     * @param terms the list of terms to remove from the TableauWorkbook, which must be referenced by GUID
     * @return the TableauWorkbook that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static TableauWorkbook removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (TableauWorkbook) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a TableauWorkbook, without replacing existing Atlan tags linked to the TableauWorkbook.
     * Note: this operation must make two API calls — one to retrieve the TableauWorkbook's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the TableauWorkbook
     * @param qualifiedName of the TableauWorkbook
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated TableauWorkbook
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static TableauWorkbook appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (TableauWorkbook) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TableauWorkbook, without replacing existing Atlan tags linked to the TableauWorkbook.
     * Note: this operation must make two API calls — one to retrieve the TableauWorkbook's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the TableauWorkbook
     * @param qualifiedName of the TableauWorkbook
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated TableauWorkbook
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static TableauWorkbook appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (TableauWorkbook) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a TableauWorkbook.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a TableauWorkbook
     * @param qualifiedName of the TableauWorkbook
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the TableauWorkbook
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
