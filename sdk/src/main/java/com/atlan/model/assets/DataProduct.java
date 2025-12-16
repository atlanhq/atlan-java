/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.LogicException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.DataProductCriticality;
import com.atlan.model.enums.DataProductLineageStatus;
import com.atlan.model.enums.DataProductSensitivity;
import com.atlan.model.enums.DataProductStatus;
import com.atlan.model.enums.DataProductVisibility;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.mesh.DataProductAssetsDSL;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.IOException;
import java.util.ArrayList;
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
 * Instance of a data product in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class DataProduct extends Asset implements IDataProduct, IDataMesh, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DataProduct";

    /** Fixed typeName for DataProducts. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Criticality of this data product. */
    @Attribute
    DataProductCriticality daapCriticality;

    /** Input ports guids for this data product. */
    @Attribute
    @Singular
    SortedSet<String> daapInputPortGuids;

    /** Status of this data product lineage. */
    @Attribute
    DataProductLineageStatus daapLineageStatus;

    /** Output ports guids for this data product. */
    @Attribute
    @Singular
    SortedSet<String> daapOutputPortGuids;

    /** Information sensitivity of this data product. */
    @Attribute
    DataProductSensitivity daapSensitivity;

    /** Status of this data product. */
    @Attribute
    DataProductStatus daapStatus;

    /** Visibility of a data product. */
    @Attribute
    DataProductVisibility daapVisibility;

    /** list of groups for product visibility control */
    @Attribute
    @Singular
    SortedSet<String> daapVisibilityGroups;

    /** list of users for product visibility control */
    @Attribute
    @Singular
    SortedSet<String> daapVisibilityUsers;

    /** Data domain in which this data product exists. */
    @Attribute
    IDataDomain dataDomain;

    /** Search DSL used to define which assets are part of this data product. */
    @Attribute
    String dataProductAssetsDSL;

    /** Playbook filter to define which assets are part of this data product. */
    @Attribute
    String dataProductAssetsPlaybookFilter;

    /** Criticality of this data product. */
    @Attribute
    DataProductCriticality dataProductCriticality;

    /** Timestamp when the score of this data product was last updated. */
    @Attribute
    @Date
    Long dataProductScoreUpdatedAt;

    /** Score of this data product. */
    @Attribute
    Double dataProductScoreValue;

    /** Information sensitivity of this data product. */
    @Attribute
    DataProductSensitivity dataProductSensitivity;

    /** Status of this data product. */
    @Attribute
    DataProductStatus dataProductStatus;

    /** Visibility of a data product. */
    @Attribute
    DataProductVisibility dataProductVisibility;

    /** Input ports for this data product. */
    @Attribute
    @Singular
    SortedSet<IAsset> inputPorts;

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

    /** Output ports for this data product. */
    @Attribute
    @Singular
    SortedSet<IAsset> outputPorts;

    /** Unique name of the parent domain in which this asset exists. */
    @Attribute
    String parentDomainQualifiedName;

    /** Partial fields contained in the asset. */
    @Attribute
    @Singular
    SortedSet<IPartialField> partialChildFields;

    /** Partial objects contained in the asset. */
    @Attribute
    @Singular
    SortedSet<IPartialObject> partialChildObjects;

    /** Unique name of the top-level domain in which this asset exists. */
    @Attribute
    String superDomainQualifiedName;

    /**
     * Builds the minimal object necessary to create a relationship to a DataProduct, from a potentially
     * more-complete DataProduct object.
     *
     * @return the minimal object necessary to relate to the DataProduct
     * @throws InvalidRequestException if any of the minimal set of required properties for a DataProduct relationship are not found in the initial object
     */
    @Override
    public DataProduct trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all DataProduct assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DataProduct assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all DataProduct assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all DataProduct assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DataProducts will be included
     * @return a fluent search that includes all DataProduct assets
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
     * Reference to a DataProduct by GUID. Use this to create a relationship to this DataProduct,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the DataProduct to reference
     * @return reference to a DataProduct that can be used for defining a relationship to a DataProduct
     */
    public static DataProduct refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DataProduct by GUID. Use this to create a relationship to this DataProduct,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the DataProduct to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DataProduct that can be used for defining a relationship to a DataProduct
     */
    public static DataProduct refByGuid(String guid, Reference.SaveSemantic semantic) {
        return DataProduct._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a DataProduct by qualifiedName. Use this to create a relationship to this DataProduct,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the DataProduct to reference
     * @return reference to a DataProduct that can be used for defining a relationship to a DataProduct
     */
    public static DataProduct refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DataProduct by qualifiedName. Use this to create a relationship to this DataProduct,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the DataProduct to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DataProduct that can be used for defining a relationship to a DataProduct
     */
    public static DataProduct refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return DataProduct._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a DataProduct by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DataProduct to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DataProduct, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataProduct does not exist or the provided GUID is not a DataProduct
     */
    @JsonIgnore
    public static DataProduct get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a DataProduct by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DataProduct to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full DataProduct, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataProduct does not exist or the provided GUID is not a DataProduct
     */
    @JsonIgnore
    public static DataProduct get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof DataProduct) {
                return (DataProduct) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof DataProduct) {
                return (DataProduct) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a DataProduct by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DataProduct to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DataProduct, including any relationships
     * @return the requested DataProduct, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataProduct does not exist or the provided GUID is not a DataProduct
     */
    @JsonIgnore
    public static DataProduct get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a DataProduct by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DataProduct to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DataProduct, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the DataProduct
     * @return the requested DataProduct, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DataProduct does not exist or the provided GUID is not a DataProduct
     */
    @JsonIgnore
    public static DataProduct get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = DataProduct.select(client)
                    .where(DataProduct.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof DataProduct) {
                return (DataProduct) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = DataProduct.select(client)
                    .where(DataProduct.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof DataProduct) {
                return (DataProduct) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) DataProduct to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DataProduct
     * @return true if the DataProduct is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary for creating a DataProduct.
     *
     * @param client connectivity to the Atlan tenant where the DataProduct is intended to be created
     * @param name of the DataProduct
     * @param domainQualifiedName unique name of the DataDomain in which this product exists
     * @param assetSelection a query that defines which assets to include in the data product
     * @return the minimal request necessary to create the DataProduct, as a builder
     * @throws InvalidRequestException if the domain provided is without a qualifiedName
     */
    public static DataProductBuilder<?, ?> creator(
            AtlanClient client, String name, String domainQualifiedName, FluentSearch assetSelection)
            throws InvalidRequestException {
        return creator(name, domainQualifiedName, "").assetSelection(client, assetSelection);
    }

    /**
     * Builds the minimal object necessary for creating a DataProduct.
     *
     * @param client connectivity to the Atlan tenant where the DataProduct is intended to be created
     * @param name of the DataProduct
     * @param domainQualifiedName unique name of the DataDomain in which this product exists
     * @param assetSelection a query that defines which assets to include in the data product
     * @return the minimal request necessary to create the DataProduct, as a builder
     */
    public static DataProductBuilder<?, ?> creator(
            AtlanClient client, String name, String domainQualifiedName, IndexSearchDSL assetSelection) {
        return DataProduct._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(domainQualifiedName + "/product/" + name)
                .name(name)
                .dataProductStatus(DataProductStatus.ACTIVE)
                .parentDomainQualifiedName(domainQualifiedName)
                .superDomainQualifiedName(StringUtils.getSuperDomainQualifiedName(domainQualifiedName))
                .dataDomain(DataDomain.refByQualifiedName(domainQualifiedName))
                .assetSelection(client, assetSelection)
                .dataProductAssetsPlaybookFilter("{\"condition\":\"AND\",\"isGroupLocked\":false,\"rules\":[]}")
                .daapStatus(DataProductStatus.ACTIVE);
    }

    /**
     * Builds the minimal object necessary for creating a DataProduct.
     *
     * @param name of the DataProduct
     * @param domainQualifiedName unique name of the DataDomain in which this product exists
     * @param assetSelection a string containing a query that defines which assets to include in the data product
     * @return the minimal request necessary to create the DataProduct, as a builder
     */
    public static DataProductBuilder<?, ?> creator(String name, String domainQualifiedName, String assetSelection) {
        return DataProduct._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(domainQualifiedName + "/product/" + name)
                .name(name)
                .dataProductStatus(DataProductStatus.ACTIVE)
                .parentDomainQualifiedName(domainQualifiedName)
                .superDomainQualifiedName(StringUtils.getSuperDomainQualifiedName(domainQualifiedName))
                .dataDomain(DataDomain.refByQualifiedName(domainQualifiedName))
                .dataProductAssetsDSL(assetSelection)
                .dataProductAssetsPlaybookFilter("{\"condition\":\"AND\",\"isGroupLocked\":false,\"rules\":[]}")
                .daapStatus(DataProductStatus.ACTIVE);
    }

    /**
     * Builds the minimal object necessary to update a DataProduct.
     *
     * @param qualifiedName of the DataProduct
     * @param name of the DataProduct
     * @return the minimal request necessary to update the DataProduct, as a builder
     */
    public static DataProductBuilder<?, ?> updater(String qualifiedName, String name) {
        return DataProduct._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DataProduct, from a potentially
     * more-complete DataProduct object.
     *
     * @return the minimal object necessary to update the DataProduct, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DataProduct are not found in the initial object
     */
    @Override
    public DataProductBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves the assets associated with this data product.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a stream of the assets related to the data product
     * @throws AtlanException on any API problems
     */
    @JsonIgnore
    public IndexSearchResponse getAssets(AtlanClient client) throws AtlanException {
        try {
            DataProductAssetsDSL dsl = client.readValue(getDataProductAssetsDSL(), DataProductAssetsDSL.class);
            return client.assets.search(dsl.getQuery());
        } catch (IOException e) {
            throw new LogicException(ErrorCode.DATA_PRODUCT_QUERY_ERROR, e);
        }
    }

    /**
     * Find a DataProduct by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the domain, if found.
     *
     * @param client connectivity to the Atlan tenant on which to search for the DataProduct
     * @param name of the DataProduct
     * @return the DataProduct, if found
     * @throws AtlanException on any API problems, or if the DataProduct does not exist
     */
    public static List<DataProduct> findByName(AtlanClient client, String name) throws AtlanException {
        return findByName(client, name, (List<AtlanField>) null);
    }

    /**
     * Find a DataProduct by its human-readable name.
     * Note that domains are not unique by name, so there may be multiple results.
     *
     * @param client connectivity to the Atlan tenant on which to search for the DataProduct
     * @param name of the DataProduct
     * @param attributes an optional collection of attributes (unchecked) to retrieve for the DataProduct
     * @return the DataProduct, if found
     * @throws AtlanException on any API problems, or if the DataProduct does not exist
     */
    public static List<DataProduct> findByName(AtlanClient client, String name, Collection<String> attributes)
            throws AtlanException {
        List<DataProduct> results = new ArrayList<>();
        DataProduct.select(client)
                .where(DataProduct.NAME.eq(name))
                ._includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof DataProduct)
                .forEach(d -> results.add((DataProduct) d));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_NAME, TYPE_NAME, name);
        }
        return results;
    }

    /**
     * Find a DataProduct by its human-readable name.
     * Note that domains are not unique by name, so there may be multiple results.
     *
     * @param client connectivity to the Atlan tenant on which to search for the DataProduct
     * @param name of the DataProduct
     * @param attributes an optional collection of attributes (checked) to retrieve for the DataProduct
     * @return the DataProduct, if found
     * @throws AtlanException on any API problems, or if the DataProduct does not exist
     */
    public static List<DataProduct> findByName(AtlanClient client, String name, List<AtlanField> attributes)
            throws AtlanException {
        List<DataProduct> results = new ArrayList<>();
        DataProduct.select(client)
                .where(DataProduct.NAME.eq(name))
                .includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof DataProduct)
                .forEach(d -> results.add((DataProduct) d));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_NAME, TYPE_NAME, name);
        }
        return results;
    }

    public abstract static class DataProductBuilder<C extends DataProduct, B extends DataProductBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {

        /**
         * Change the selection of assets for the data product, based on the specified Atlan fluent search.
         *
         * @param client connectivity to an Atlan tenant
         * @param assetSelection fluent search query that defines the assets to include in the data product
         * @return the builder for the data product, with an updated set of criteria for its assets
         */
        public B assetSelection(AtlanClient client, FluentSearch assetSelection) {
            return assetSelection(client, IndexSearchDSL.of(assetSelection.toUnfilteredQuery()));
        }

        /**
         * Change the selection of assets for the data product, based on the specified Atlan search query.
         *
         * @param client connectivity to an Atlan tenant
         * @param assetSelection search query that defines the assets to include in the data product
         * @return the builder for the data product, with an updated set of criteria for its assets
         */
        public B assetSelection(AtlanClient client, IndexSearchDSL assetSelection) {
            return this.dataProductAssetsDSL(
                    DataProductAssetsDSL.builder(assetSelection).build().toJson(client));
        }
    }

    /**
     * Remove the system description from a DataProduct.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DataProduct
     * @param name of the DataProduct
     * @return the updated DataProduct, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataProduct removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DataProduct) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DataProduct.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DataProduct
     * @param name of the DataProduct
     * @return the updated DataProduct, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataProduct removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DataProduct) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DataProduct.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DataProduct's owners
     * @param qualifiedName of the DataProduct
     * @param name of the DataProduct
     * @return the updated DataProduct, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataProduct removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DataProduct) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DataProduct.
     *
     * @param client connectivity to the Atlan tenant on which to update the DataProduct's certificate
     * @param qualifiedName of the DataProduct
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DataProduct, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataProduct updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DataProduct)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DataProduct.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DataProduct's certificate
     * @param qualifiedName of the DataProduct
     * @param name of the DataProduct
     * @return the updated DataProduct, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataProduct removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DataProduct) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DataProduct.
     *
     * @param client connectivity to the Atlan tenant on which to update the DataProduct's announcement
     * @param qualifiedName of the DataProduct
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataProduct updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (DataProduct)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DataProduct.
     *
     * @param client connectivity to the Atlan client from which to remove the DataProduct's announcement
     * @param qualifiedName of the DataProduct
     * @param name of the DataProduct
     * @return the updated DataProduct, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataProduct removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DataProduct) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DataProduct.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DataProduct's assigned terms
     * @param qualifiedName for the DataProduct
     * @param name human-readable name of the DataProduct
     * @param terms the list of terms to replace on the DataProduct, or null to remove all terms from the DataProduct
     * @return the DataProduct that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DataProduct replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (DataProduct) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DataProduct, without replacing existing terms linked to the DataProduct.
     * Note: this operation must make two API calls — one to retrieve the DataProduct's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DataProduct
     * @param qualifiedName for the DataProduct
     * @param terms the list of terms to append to the DataProduct
     * @return the DataProduct that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DataProduct appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DataProduct) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DataProduct, without replacing all existing terms linked to the DataProduct.
     * Note: this operation must make two API calls — one to retrieve the DataProduct's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DataProduct
     * @param qualifiedName for the DataProduct
     * @param terms the list of terms to remove from the DataProduct, which must be referenced by GUID
     * @return the DataProduct that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DataProduct removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DataProduct) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DataProduct, without replacing existing Atlan tags linked to the DataProduct.
     * Note: this operation must make two API calls — one to retrieve the DataProduct's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DataProduct
     * @param qualifiedName of the DataProduct
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DataProduct
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static DataProduct appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (DataProduct) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DataProduct, without replacing existing Atlan tags linked to the DataProduct.
     * Note: this operation must make two API calls — one to retrieve the DataProduct's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DataProduct
     * @param qualifiedName of the DataProduct
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DataProduct
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static DataProduct appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DataProduct) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a DataProduct.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DataProduct
     * @param qualifiedName of the DataProduct
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DataProduct
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
