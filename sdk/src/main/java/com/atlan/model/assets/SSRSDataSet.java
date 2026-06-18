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
 * Instance of a data set within an SSRS report in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class SSRSDataSet extends Asset implements ISSRSDataSet, ISSRS, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SSRSDataSet";

    /** Fixed typeName for SSRSDataSets. */
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

    /** Catalog name associated with this asset in the source system. */
    @Attribute
    String ssrsCatalogName;

    /** Whether the data set is connected. */
    @Attribute
    Boolean ssrsDataSetConnected;

    /** Cube name for the data set. */
    @Attribute
    String ssrsDataSetCubeName;

    /** Data source connection string for the data set. */
    @Attribute
    String ssrsDataSetDataSourceConnectionString;

    /** Data source reference for the data set. */
    @Attribute
    String ssrsDataSetDataSourceReference;

    /** Error code for the data set. */
    @Attribute
    String ssrsDataSetErrorCode;

    /** Extension for the data set. */
    @Attribute
    String ssrsDataSetExtension;

    /** Number of fields in this dataset. */
    @Attribute
    Long ssrsDataSetFieldCount;

    /** Whether the data set is shared. */
    @Attribute
    Boolean ssrsDataSetIsSharedDataSet;

    /** Log messages for the data set. */
    @Attribute
    String ssrsDataSetLogMessages;

    /** Simple name of the DataSet asset that contains this asset. */
    @Attribute
    String ssrsDataSetName;

    /** Processed SQL for the data set. */
    @Attribute
    String ssrsDataSetProcessedSql;

    /** Unique name of the DataSet asset that contains this asset. */
    @Attribute
    String ssrsDataSetQualifiedName;

    /** Query parameters for the data set. */
    @Attribute
    String ssrsDataSetQueryParameters;

    /** Reference table names for the data set. */
    @Attribute
    @Singular
    SortedSet<String> ssrsDataSetReferenceTableNames;

    /** SQL query for the data set. */
    @Attribute
    String ssrsDataSetSqlQuery;

    /** Stored procedure name for the data set. */
    @Attribute
    String ssrsDataSetStoredProcedureName;

    /** Fields contained in the data set. */
    @Attribute
    @Singular
    SortedSet<ISSRSField> ssrsFields;

    /** Ordered array of folder assets with qualified name and name representing the complete folder hierarchy path for this asset, from immediate parent to root folder. */
    @Attribute
    @Singular
    List<Map<String, String>> ssrsFolderHierarchies;

    /** Whether the asset is hidden. */
    @Attribute
    Boolean ssrsHidden;

    /** Unique name of the immediate parent folder containing this asset. */
    @Attribute
    String ssrsParentFolderQualifiedName;

    /** Path of the asset in SSRS. */
    @Attribute
    String ssrsPath;

    /** Reference repository ID for this asset. */
    @Attribute
    String ssrsReferenceRepositoryId;

    /** Report containing the data set. */
    @Attribute
    ISSRSReport ssrsReport;

    /** Simple name of the Report asset that contains this asset. */
    @Attribute
    String ssrsReportName;

    /** Unique name of the Report asset that contains this asset. */
    @Attribute
    String ssrsReportQualifiedName;

    /** Schema name associated with this asset in the source system. */
    @Attribute
    String ssrsSchemaName;

    /** Table name associated with this asset in the source system. */
    @Attribute
    String ssrsTableName;

    /** Whether the asset is used in a report. */
    @Attribute
    Boolean ssrsUsedInReports;

    /**
     * Builds the minimal object necessary to create a relationship to a SSRSDataSet, from a potentially
     * more-complete SSRSDataSet object.
     *
     * @return the minimal object necessary to relate to the SSRSDataSet
     * @throws InvalidRequestException if any of the minimal set of required properties for a SSRSDataSet relationship are not found in the initial object
     */
    @Override
    public SSRSDataSet trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SSRSDataSet assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SSRSDataSet assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SSRSDataSet assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SSRSDataSet assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SSRSDataSets will be included
     * @return a fluent search that includes all SSRSDataSet assets
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
     * Reference to a SSRSDataSet by GUID. Use this to create a relationship to this SSRSDataSet,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SSRSDataSet to reference
     * @return reference to a SSRSDataSet that can be used for defining a relationship to a SSRSDataSet
     */
    public static SSRSDataSet refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SSRSDataSet by GUID. Use this to create a relationship to this SSRSDataSet,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SSRSDataSet to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SSRSDataSet that can be used for defining a relationship to a SSRSDataSet
     */
    public static SSRSDataSet refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SSRSDataSet._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SSRSDataSet by qualifiedName. Use this to create a relationship to this SSRSDataSet,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SSRSDataSet to reference
     * @return reference to a SSRSDataSet that can be used for defining a relationship to a SSRSDataSet
     */
    public static SSRSDataSet refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SSRSDataSet by qualifiedName. Use this to create a relationship to this SSRSDataSet,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SSRSDataSet to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SSRSDataSet that can be used for defining a relationship to a SSRSDataSet
     */
    public static SSRSDataSet refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SSRSDataSet._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SSRSDataSet by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SSRSDataSet to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SSRSDataSet, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SSRSDataSet does not exist or the provided GUID is not a SSRSDataSet
     */
    @JsonIgnore
    public static SSRSDataSet get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SSRSDataSet by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SSRSDataSet to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SSRSDataSet, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SSRSDataSet does not exist or the provided GUID is not a SSRSDataSet
     */
    @JsonIgnore
    public static SSRSDataSet get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SSRSDataSet) {
                return (SSRSDataSet) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof SSRSDataSet) {
                return (SSRSDataSet) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SSRSDataSet by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SSRSDataSet to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SSRSDataSet, including any relationships
     * @return the requested SSRSDataSet, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SSRSDataSet does not exist or the provided GUID is not a SSRSDataSet
     */
    @JsonIgnore
    public static SSRSDataSet get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a SSRSDataSet by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SSRSDataSet to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SSRSDataSet, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the SSRSDataSet
     * @return the requested SSRSDataSet, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SSRSDataSet does not exist or the provided GUID is not a SSRSDataSet
     */
    @JsonIgnore
    public static SSRSDataSet get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = SSRSDataSet.select(client)
                    .where(SSRSDataSet.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof SSRSDataSet) {
                return (SSRSDataSet) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = SSRSDataSet.select(client)
                    .where(SSRSDataSet.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof SSRSDataSet) {
                return (SSRSDataSet) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SSRSDataSet to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SSRSDataSet
     * @return true if the SSRSDataSet is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SSRSDataSet.
     *
     * @param qualifiedName of the SSRSDataSet
     * @param name of the SSRSDataSet
     * @return the minimal request necessary to update the SSRSDataSet, as a builder
     */
    public static SSRSDataSetBuilder<?, ?> updater(String qualifiedName, String name) {
        return SSRSDataSet._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SSRSDataSet,
     * from a potentially more-complete SSRSDataSet object.
     *
     * @return the minimal object necessary to update the SSRSDataSet, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a SSRSDataSet are not present in the initial object
     */
    @Override
    public SSRSDataSetBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class SSRSDataSetBuilder<C extends SSRSDataSet, B extends SSRSDataSetBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a SSRSDataSet.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SSRSDataSet
     * @param name of the SSRSDataSet
     * @return the updated SSRSDataSet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SSRSDataSet removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SSRSDataSet) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SSRSDataSet.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SSRSDataSet
     * @param name of the SSRSDataSet
     * @return the updated SSRSDataSet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SSRSDataSet removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SSRSDataSet) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SSRSDataSet.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SSRSDataSet's owners
     * @param qualifiedName of the SSRSDataSet
     * @param name of the SSRSDataSet
     * @return the updated SSRSDataSet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SSRSDataSet removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SSRSDataSet) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SSRSDataSet.
     *
     * @param client connectivity to the Atlan tenant on which to update the SSRSDataSet's certificate
     * @param qualifiedName of the SSRSDataSet
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SSRSDataSet, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SSRSDataSet updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SSRSDataSet)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SSRSDataSet.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SSRSDataSet's certificate
     * @param qualifiedName of the SSRSDataSet
     * @param name of the SSRSDataSet
     * @return the updated SSRSDataSet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SSRSDataSet removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SSRSDataSet) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SSRSDataSet.
     *
     * @param client connectivity to the Atlan tenant on which to update the SSRSDataSet's announcement
     * @param qualifiedName of the SSRSDataSet
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SSRSDataSet updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SSRSDataSet)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SSRSDataSet.
     *
     * @param client connectivity to the Atlan client from which to remove the SSRSDataSet's announcement
     * @param qualifiedName of the SSRSDataSet
     * @param name of the SSRSDataSet
     * @return the updated SSRSDataSet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SSRSDataSet removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SSRSDataSet) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SSRSDataSet.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SSRSDataSet's assigned terms
     * @param qualifiedName for the SSRSDataSet
     * @param name human-readable name of the SSRSDataSet
     * @param terms the list of terms to replace on the SSRSDataSet, or null to remove all terms from the SSRSDataSet
     * @return the SSRSDataSet that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SSRSDataSet replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SSRSDataSet) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SSRSDataSet, without replacing existing terms linked to the SSRSDataSet.
     * Note: this operation must make two API calls — one to retrieve the SSRSDataSet's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SSRSDataSet
     * @param qualifiedName for the SSRSDataSet
     * @param terms the list of terms to append to the SSRSDataSet
     * @return the SSRSDataSet that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SSRSDataSet appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SSRSDataSet) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SSRSDataSet, without replacing all existing terms linked to the SSRSDataSet.
     * Note: this operation must make two API calls — one to retrieve the SSRSDataSet's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SSRSDataSet
     * @param qualifiedName for the SSRSDataSet
     * @param terms the list of terms to remove from the SSRSDataSet, which must be referenced by GUID
     * @return the SSRSDataSet that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SSRSDataSet removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SSRSDataSet) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SSRSDataSet, without replacing existing Atlan tags linked to the SSRSDataSet.
     * Note: this operation must make two API calls — one to retrieve the SSRSDataSet's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SSRSDataSet
     * @param qualifiedName of the SSRSDataSet
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SSRSDataSet
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static SSRSDataSet appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SSRSDataSet) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SSRSDataSet, without replacing existing Atlan tags linked to the SSRSDataSet.
     * Note: this operation must make two API calls — one to retrieve the SSRSDataSet's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SSRSDataSet
     * @param qualifiedName of the SSRSDataSet
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SSRSDataSet
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static SSRSDataSet appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SSRSDataSet) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SSRSDataSet.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SSRSDataSet
     * @param qualifiedName of the SSRSDataSet
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SSRSDataSet
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
