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
 * Instance of an SAP ERP DataSource (extractor) in Atlan. A DataSource defines how an SAP ECC or S/4HANA system extracts data for delivery to a receiving system through ODP or classic BW staging.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class SapErpDataSource extends Asset implements ISapErpDataSource, ISAP, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SapErpDataSource";

    /** Fixed typeName for SapErpDataSources. */
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

    /** Name of the SAP component, representing a specific functional area in SAP. */
    @Attribute
    String sapComponentName;

    /** SAP-specific data types. */
    @Attribute
    String sapDataType;

    /** Path by which the DataSource delivers data: ODP, CLASSIC or BOTH. */
    @Attribute
    String sapErpDataSourceDeliveryPath;

    /** Name of the extract structure that defines the fields delivered by the DataSource. */
    @Attribute
    String sapErpDataSourceExtractStructure;

    /** Raw extraction method code of the DataSource, such as V, F1, F2, D or CO. */
    @Attribute
    String sapErpDataSourceExtractionMethod;

    /** Readable label of the extraction method of the DataSource. */
    @Attribute
    String sapErpDataSourceExtractionMethodText;

    /** Name of the extractor object (function module, view, table, class or InfoSet) that reads data for the DataSource. */
    @Attribute
    String sapErpDataSourceExtractor;

    /** SAP ERP ABAP Programs (class pools) that act as the extractor for this DataSource. */
    @Attribute
    @Singular
    SortedSet<ISapErpAbapProgram> sapErpDataSourceExtractorAbapPrograms;

    /** Kind of the extractor object: FM, VIEW, TABLE, CLASS or INFOSET. */
    @Attribute
    String sapErpDataSourceExtractorType;

    /** Fields of this SAP ERP DataSource. */
    @Attribute
    @Singular
    SortedSet<ISapErpDataSourceField> sapErpDataSourceFields;

    /** When true, the DataSource is customer-built rather than delivered by SAP. */
    @Attribute
    Boolean sapErpDataSourceIsCustom;

    /** When true, the DataSource is released for the Operational Data Provisioning (ODP) framework. */
    @Attribute
    Boolean sapErpDataSourceIsOdpEnabled;

    /** Day on which the source system last confirmed delivery activity for the DataSource. */
    @Attribute
    @Date
    Long sapErpDataSourceLastConfirmedAt;

    /** Logical system name of the source client that owns the DataSource. */
    @Attribute
    String sapErpDataSourceLogicalSystem;

    /** Other logical system names recorded for the DataSource in the source system. */
    @Attribute
    @Singular
    SortedSet<String> sapErpDataSourceLogicalSystemAliases;

    /** Provider context through which the DataSource is exposed, such as SAPI. */
    @Attribute
    String sapErpDataSourceProviderContext;

    /** Reasons for which the DataSource was included in the crawl, such as an active delta initialisation or ODP subscription. */
    @Attribute
    @Singular
    SortedSet<String> sapErpDataSourceScopeReasons;

    /** SAP ERP Tables that act as setup tables for this DataSource. */
    @Attribute
    @Singular
    SortedSet<ISapErpTable> sapErpDataSourceSetupTables;

    /** Type of the DataSource: transaction data, master data attributes, texts or hierarchies. */
    @Attribute
    String sapErpDataSourceType;

    /** Confidence tier of the upstream lineage of the DataSource: A, B, C or UNRESOLVED. */
    @Attribute
    String sapErpDataSourceUpstreamTier;

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
     * Builds the minimal object necessary to create a relationship to a SapErpDataSource, from a potentially
     * more-complete SapErpDataSource object.
     *
     * @return the minimal object necessary to relate to the SapErpDataSource
     * @throws InvalidRequestException if any of the minimal set of required properties for a SapErpDataSource relationship are not found in the initial object
     */
    @Override
    public SapErpDataSource trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SapErpDataSource assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SapErpDataSource assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SapErpDataSource assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SapErpDataSource assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SapErpDataSources will be included
     * @return a fluent search that includes all SapErpDataSource assets
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
     * Reference to a SapErpDataSource by GUID. Use this to create a relationship to this SapErpDataSource,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SapErpDataSource to reference
     * @return reference to a SapErpDataSource that can be used for defining a relationship to a SapErpDataSource
     */
    public static SapErpDataSource refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SapErpDataSource by GUID. Use this to create a relationship to this SapErpDataSource,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SapErpDataSource to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SapErpDataSource that can be used for defining a relationship to a SapErpDataSource
     */
    public static SapErpDataSource refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SapErpDataSource._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SapErpDataSource by qualifiedName. Use this to create a relationship to this SapErpDataSource,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SapErpDataSource to reference
     * @return reference to a SapErpDataSource that can be used for defining a relationship to a SapErpDataSource
     */
    public static SapErpDataSource refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SapErpDataSource by qualifiedName. Use this to create a relationship to this SapErpDataSource,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SapErpDataSource to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SapErpDataSource that can be used for defining a relationship to a SapErpDataSource
     */
    public static SapErpDataSource refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SapErpDataSource._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SapErpDataSource by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SapErpDataSource to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SapErpDataSource, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SapErpDataSource does not exist or the provided GUID is not a SapErpDataSource
     */
    @JsonIgnore
    public static SapErpDataSource get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SapErpDataSource by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SapErpDataSource to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SapErpDataSource, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SapErpDataSource does not exist or the provided GUID is not a SapErpDataSource
     */
    @JsonIgnore
    public static SapErpDataSource get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SapErpDataSource) {
                return (SapErpDataSource) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof SapErpDataSource) {
                return (SapErpDataSource) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SapErpDataSource by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SapErpDataSource to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SapErpDataSource, including any relationships
     * @return the requested SapErpDataSource, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SapErpDataSource does not exist or the provided GUID is not a SapErpDataSource
     */
    @JsonIgnore
    public static SapErpDataSource get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a SapErpDataSource by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SapErpDataSource to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SapErpDataSource, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the SapErpDataSource
     * @return the requested SapErpDataSource, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SapErpDataSource does not exist or the provided GUID is not a SapErpDataSource
     */
    @JsonIgnore
    public static SapErpDataSource get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = SapErpDataSource.select(client)
                    .where(SapErpDataSource.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof SapErpDataSource) {
                return (SapErpDataSource) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = SapErpDataSource.select(client)
                    .where(SapErpDataSource.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof SapErpDataSource) {
                return (SapErpDataSource) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SapErpDataSource to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SapErpDataSource
     * @return true if the SapErpDataSource is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SapErpDataSource.
     *
     * @param qualifiedName of the SapErpDataSource
     * @param name of the SapErpDataSource
     * @return the minimal request necessary to update the SapErpDataSource, as a builder
     */
    public static SapErpDataSourceBuilder<?, ?> updater(String qualifiedName, String name) {
        return SapErpDataSource._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SapErpDataSource,
     * from a potentially more-complete SapErpDataSource object.
     *
     * @return the minimal object necessary to update the SapErpDataSource, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a SapErpDataSource are not present in the initial object
     */
    @Override
    public SapErpDataSourceBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class SapErpDataSourceBuilder<
                    C extends SapErpDataSource, B extends SapErpDataSourceBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a SapErpDataSource.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SapErpDataSource
     * @param name of the SapErpDataSource
     * @return the updated SapErpDataSource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SapErpDataSource removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SapErpDataSource) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SapErpDataSource.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SapErpDataSource
     * @param name of the SapErpDataSource
     * @return the updated SapErpDataSource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SapErpDataSource removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SapErpDataSource) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SapErpDataSource.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SapErpDataSource's owners
     * @param qualifiedName of the SapErpDataSource
     * @param name of the SapErpDataSource
     * @return the updated SapErpDataSource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SapErpDataSource removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SapErpDataSource) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SapErpDataSource.
     *
     * @param client connectivity to the Atlan tenant on which to update the SapErpDataSource's certificate
     * @param qualifiedName of the SapErpDataSource
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SapErpDataSource, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SapErpDataSource updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SapErpDataSource)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SapErpDataSource.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SapErpDataSource's certificate
     * @param qualifiedName of the SapErpDataSource
     * @param name of the SapErpDataSource
     * @return the updated SapErpDataSource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SapErpDataSource removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SapErpDataSource) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SapErpDataSource.
     *
     * @param client connectivity to the Atlan tenant on which to update the SapErpDataSource's announcement
     * @param qualifiedName of the SapErpDataSource
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SapErpDataSource updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SapErpDataSource)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SapErpDataSource.
     *
     * @param client connectivity to the Atlan client from which to remove the SapErpDataSource's announcement
     * @param qualifiedName of the SapErpDataSource
     * @param name of the SapErpDataSource
     * @return the updated SapErpDataSource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SapErpDataSource removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SapErpDataSource) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SapErpDataSource.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SapErpDataSource's assigned terms
     * @param qualifiedName for the SapErpDataSource
     * @param name human-readable name of the SapErpDataSource
     * @param terms the list of terms to replace on the SapErpDataSource, or null to remove all terms from the SapErpDataSource
     * @return the SapErpDataSource that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SapErpDataSource replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SapErpDataSource) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SapErpDataSource, without replacing existing terms linked to the SapErpDataSource.
     * Note: this operation must make two API calls — one to retrieve the SapErpDataSource's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SapErpDataSource
     * @param qualifiedName for the SapErpDataSource
     * @param terms the list of terms to append to the SapErpDataSource
     * @return the SapErpDataSource that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SapErpDataSource appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SapErpDataSource) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SapErpDataSource, without replacing all existing terms linked to the SapErpDataSource.
     * Note: this operation must make two API calls — one to retrieve the SapErpDataSource's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SapErpDataSource
     * @param qualifiedName for the SapErpDataSource
     * @param terms the list of terms to remove from the SapErpDataSource, which must be referenced by GUID
     * @return the SapErpDataSource that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SapErpDataSource removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SapErpDataSource) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SapErpDataSource, without replacing existing Atlan tags linked to the SapErpDataSource.
     * Note: this operation must make two API calls — one to retrieve the SapErpDataSource's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SapErpDataSource
     * @param qualifiedName of the SapErpDataSource
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SapErpDataSource
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static SapErpDataSource appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SapErpDataSource) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SapErpDataSource, without replacing existing Atlan tags linked to the SapErpDataSource.
     * Note: this operation must make two API calls — one to retrieve the SapErpDataSource's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SapErpDataSource
     * @param qualifiedName of the SapErpDataSource
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SapErpDataSource
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static SapErpDataSource appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SapErpDataSource) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SapErpDataSource.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SapErpDataSource
     * @param qualifiedName of the SapErpDataSource
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SapErpDataSource
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
