/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a cube hierarchy in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class CubeHierarchy extends Asset
        implements ICubeHierarchy, IMultiDimensionalDataset, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "CubeHierarchy";

    /** Fixed typeName for CubeHierarchys. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Dimension in which this hierarchy exists. */
    @Attribute
    ICubeDimension cubeDimension;

    /** Simple name of the cube dimension in which this asset exists, or empty if it is itself a dimension. */
    @Attribute
    String cubeDimensionName;

    /** Unique name of the cube dimension in which this asset exists, or empty if it is itself a dimension. */
    @Attribute
    String cubeDimensionQualifiedName;

    /** Number of total fields in the cube hierarchy. */
    @Attribute
    Long cubeFieldCount;

    /** Individual fields that make up the hierarchy. */
    @Attribute
    @Singular
    SortedSet<ICubeField> cubeFields;

    /** Simple name of the dimension hierarchy in which this asset exists, or empty if it is itself a hierarchy. */
    @Attribute
    String cubeHierarchyName;

    /** Unique name of the dimension hierarchy in which this asset exists, or empty if it is itself a hierarchy. */
    @Attribute
    String cubeHierarchyQualifiedName;

    /** Simple name of the cube in which this asset exists, or empty if it is itself a cube. */
    @Attribute
    String cubeName;

    /** Unique name of the cube in which this asset exists, or empty if it is itself a cube. */
    @Attribute
    String cubeQualifiedName;

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
     * Builds the minimal object necessary to create a relationship to a CubeHierarchy, from a potentially
     * more-complete CubeHierarchy object.
     *
     * @return the minimal object necessary to relate to the CubeHierarchy
     * @throws InvalidRequestException if any of the minimal set of required properties for a CubeHierarchy relationship are not found in the initial object
     */
    @Override
    public CubeHierarchy trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all CubeHierarchy assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) CubeHierarchy assets will be included.
     *
     * @return a fluent search that includes all CubeHierarchy assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all CubeHierarchy assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) CubeHierarchy assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all CubeHierarchy assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all CubeHierarchy assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) CubeHierarchys will be included
     * @return a fluent search that includes all CubeHierarchy assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all CubeHierarchy assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) CubeHierarchys will be included
     * @return a fluent search that includes all CubeHierarchy assets
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
     * Reference to a CubeHierarchy by GUID. Use this to create a relationship to this CubeHierarchy,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the CubeHierarchy to reference
     * @return reference to a CubeHierarchy that can be used for defining a relationship to a CubeHierarchy
     */
    public static CubeHierarchy refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a CubeHierarchy by GUID. Use this to create a relationship to this CubeHierarchy,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the CubeHierarchy to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a CubeHierarchy that can be used for defining a relationship to a CubeHierarchy
     */
    public static CubeHierarchy refByGuid(String guid, Reference.SaveSemantic semantic) {
        return CubeHierarchy._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a CubeHierarchy by qualifiedName. Use this to create a relationship to this CubeHierarchy,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the CubeHierarchy to reference
     * @return reference to a CubeHierarchy that can be used for defining a relationship to a CubeHierarchy
     */
    public static CubeHierarchy refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a CubeHierarchy by qualifiedName. Use this to create a relationship to this CubeHierarchy,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the CubeHierarchy to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a CubeHierarchy that can be used for defining a relationship to a CubeHierarchy
     */
    public static CubeHierarchy refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return CubeHierarchy._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a CubeHierarchy by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the CubeHierarchy to retrieve, either its GUID or its full qualifiedName
     * @return the requested full CubeHierarchy, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CubeHierarchy does not exist or the provided GUID is not a CubeHierarchy
     */
    @JsonIgnore
    public static CubeHierarchy get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a CubeHierarchy by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CubeHierarchy to retrieve, either its GUID or its full qualifiedName
     * @return the requested full CubeHierarchy, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CubeHierarchy does not exist or the provided GUID is not a CubeHierarchy
     */
    @JsonIgnore
    public static CubeHierarchy get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a CubeHierarchy by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CubeHierarchy to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full CubeHierarchy, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CubeHierarchy does not exist or the provided GUID is not a CubeHierarchy
     */
    @JsonIgnore
    public static CubeHierarchy get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof CubeHierarchy) {
                return (CubeHierarchy) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof CubeHierarchy) {
                return (CubeHierarchy) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) CubeHierarchy to active.
     *
     * @param qualifiedName for the CubeHierarchy
     * @return true if the CubeHierarchy is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) CubeHierarchy to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the CubeHierarchy
     * @return true if the CubeHierarchy is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a CubeHierarchy.
     *
     * @param name of the CubeHierarchy
     * @param dimension in which the hierarchy should be created, which must have at least a qualifiedName
     * @return the minimal request necessary to create the CubeHierarchy, as a builder
     * @throws InvalidRequestException if the dimension provided is without a qualifiedName
     */
    public static CubeHierarchyBuilder<?, ?> creator(String name, CubeDimension dimension)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", dimension.getConnectionQualifiedName());
        map.put("cubeName", dimension.getCubeName());
        map.put("cubeQualifiedName", dimension.getCubeQualifiedName());
        map.put("name", dimension.getName());
        map.put("qualifiedName", dimension.getQualifiedName());
        validateRelationship(CubeDimension.TYPE_NAME, map);
        return creator(
                        name,
                        dimension.getConnectionQualifiedName(),
                        dimension.getCubeName(),
                        dimension.getCubeQualifiedName(),
                        dimension.getName(),
                        dimension.getQualifiedName())
                .cubeDimension(dimension.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a CubeHierarchy.
     *
     * @param name of the CubeHierarchy
     * @param dimensionQualifiedName unique name of the cube dimension in which this CubeHierarchy exists
     * @return the minimal request necessary to create the CubeHierarchy, as a builder
     */
    public static CubeHierarchyBuilder<?, ?> creator(String name, String dimensionQualifiedName) {
        String dimensionSlug = StringUtils.getNameFromQualifiedName(dimensionQualifiedName);
        String dimensionName = IMultiDimensionalDataset.getNameFromSlug(dimensionSlug);
        String cubeQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(dimensionQualifiedName);
        String cubeSlug = StringUtils.getNameFromQualifiedName(cubeQualifiedName);
        String cubeName = IMultiDimensionalDataset.getNameFromSlug(cubeSlug);
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(cubeQualifiedName);
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return creator(
                name, connectionQualifiedName, cubeName, cubeQualifiedName, dimensionName, dimensionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a CubeHierarchy.
     *
     * @param name of the CubeHierarchy
     * @param connectionQualifiedName unique name of the connection in which the CubeHierarchy should be created
     * @param cubeName simple name of the Cube in which the CubeHierarchy should be created
     * @param cubeQualifiedName unique name of the Cube in which the CubeHierarchy should be created
     * @param dimensionName simple name of the CubeDimension in which the CubeHierarchy should be created
     * @param dimensionQualifiedName unique name of the CubeDimension in which the CubeHierarchy should be created
     * @return the minimal request necessary to create the CubeHierarchy, as a builder
     */
    public static CubeHierarchyBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String cubeName,
            String cubeQualifiedName,
            String dimensionName,
            String dimensionQualifiedName) {
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return CubeHierarchy._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, dimensionQualifiedName))
                .cubeName(cubeName)
                .cubeQualifiedName(cubeQualifiedName)
                .cubeDimensionName(dimensionName)
                .cubeDimensionQualifiedName(dimensionQualifiedName)
                .cubeDimension(CubeDimension.refByQualifiedName(dimensionQualifiedName))
                .connectorType(connectorType)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a CubeHierarchy.
     *
     * @param qualifiedName of the CubeHierarchy
     * @param name of the CubeHierarchy
     * @return the minimal request necessary to update the CubeHierarchy, as a builder
     */
    public static CubeHierarchyBuilder<?, ?> updater(String qualifiedName, String name) {
        return CubeHierarchy._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique CubeHierarchy name.
     *
     * @param name of the CubeHierarchy
     * @param dimensionQualifiedName unique name of the cube dimension in which this CubeHierarchy exists
     * @return a unique name for the CubeHierarchy
     */
    public static String generateQualifiedName(String name, String dimensionQualifiedName) {
        return dimensionQualifiedName + "/" + IMultiDimensionalDataset.getSlugForName(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a CubeHierarchy, from a potentially
     * more-complete CubeHierarchy object.
     *
     * @return the minimal object necessary to update the CubeHierarchy, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for CubeHierarchy are not found in the initial object
     */
    @Override
    public CubeHierarchyBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a CubeHierarchy.
     *
     * @param qualifiedName of the CubeHierarchy
     * @param name of the CubeHierarchy
     * @return the updated CubeHierarchy, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CubeHierarchy removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a CubeHierarchy.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the CubeHierarchy
     * @param name of the CubeHierarchy
     * @return the updated CubeHierarchy, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CubeHierarchy removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CubeHierarchy) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a CubeHierarchy.
     *
     * @param qualifiedName of the CubeHierarchy
     * @param name of the CubeHierarchy
     * @return the updated CubeHierarchy, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CubeHierarchy removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a CubeHierarchy.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the CubeHierarchy
     * @param name of the CubeHierarchy
     * @return the updated CubeHierarchy, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CubeHierarchy removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CubeHierarchy) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a CubeHierarchy.
     *
     * @param qualifiedName of the CubeHierarchy
     * @param name of the CubeHierarchy
     * @return the updated CubeHierarchy, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CubeHierarchy removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a CubeHierarchy.
     *
     * @param client connectivity to the Atlan tenant from which to remove the CubeHierarchy's owners
     * @param qualifiedName of the CubeHierarchy
     * @param name of the CubeHierarchy
     * @return the updated CubeHierarchy, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CubeHierarchy removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CubeHierarchy) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a CubeHierarchy.
     *
     * @param qualifiedName of the CubeHierarchy
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated CubeHierarchy, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CubeHierarchy updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a CubeHierarchy.
     *
     * @param client connectivity to the Atlan tenant on which to update the CubeHierarchy's certificate
     * @param qualifiedName of the CubeHierarchy
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated CubeHierarchy, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CubeHierarchy updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (CubeHierarchy)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a CubeHierarchy.
     *
     * @param qualifiedName of the CubeHierarchy
     * @param name of the CubeHierarchy
     * @return the updated CubeHierarchy, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CubeHierarchy removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a CubeHierarchy.
     *
     * @param client connectivity to the Atlan tenant from which to remove the CubeHierarchy's certificate
     * @param qualifiedName of the CubeHierarchy
     * @param name of the CubeHierarchy
     * @return the updated CubeHierarchy, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CubeHierarchy removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CubeHierarchy) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a CubeHierarchy.
     *
     * @param qualifiedName of the CubeHierarchy
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CubeHierarchy updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a CubeHierarchy.
     *
     * @param client connectivity to the Atlan tenant on which to update the CubeHierarchy's announcement
     * @param qualifiedName of the CubeHierarchy
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CubeHierarchy updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (CubeHierarchy)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a CubeHierarchy.
     *
     * @param qualifiedName of the CubeHierarchy
     * @param name of the CubeHierarchy
     * @return the updated CubeHierarchy, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CubeHierarchy removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a CubeHierarchy.
     *
     * @param client connectivity to the Atlan client from which to remove the CubeHierarchy's announcement
     * @param qualifiedName of the CubeHierarchy
     * @param name of the CubeHierarchy
     * @return the updated CubeHierarchy, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CubeHierarchy removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CubeHierarchy) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the CubeHierarchy.
     *
     * @param qualifiedName for the CubeHierarchy
     * @param name human-readable name of the CubeHierarchy
     * @param terms the list of terms to replace on the CubeHierarchy, or null to remove all terms from the CubeHierarchy
     * @return the CubeHierarchy that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static CubeHierarchy replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the CubeHierarchy.
     *
     * @param client connectivity to the Atlan tenant on which to replace the CubeHierarchy's assigned terms
     * @param qualifiedName for the CubeHierarchy
     * @param name human-readable name of the CubeHierarchy
     * @param terms the list of terms to replace on the CubeHierarchy, or null to remove all terms from the CubeHierarchy
     * @return the CubeHierarchy that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static CubeHierarchy replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (CubeHierarchy) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the CubeHierarchy, without replacing existing terms linked to the CubeHierarchy.
     * Note: this operation must make two API calls — one to retrieve the CubeHierarchy's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the CubeHierarchy
     * @param terms the list of terms to append to the CubeHierarchy
     * @return the CubeHierarchy that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static CubeHierarchy appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the CubeHierarchy, without replacing existing terms linked to the CubeHierarchy.
     * Note: this operation must make two API calls — one to retrieve the CubeHierarchy's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the CubeHierarchy
     * @param qualifiedName for the CubeHierarchy
     * @param terms the list of terms to append to the CubeHierarchy
     * @return the CubeHierarchy that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static CubeHierarchy appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (CubeHierarchy) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a CubeHierarchy, without replacing all existing terms linked to the CubeHierarchy.
     * Note: this operation must make two API calls — one to retrieve the CubeHierarchy's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the CubeHierarchy
     * @param terms the list of terms to remove from the CubeHierarchy, which must be referenced by GUID
     * @return the CubeHierarchy that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static CubeHierarchy removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a CubeHierarchy, without replacing all existing terms linked to the CubeHierarchy.
     * Note: this operation must make two API calls — one to retrieve the CubeHierarchy's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the CubeHierarchy
     * @param qualifiedName for the CubeHierarchy
     * @param terms the list of terms to remove from the CubeHierarchy, which must be referenced by GUID
     * @return the CubeHierarchy that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static CubeHierarchy removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (CubeHierarchy) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a CubeHierarchy, without replacing existing Atlan tags linked to the CubeHierarchy.
     * Note: this operation must make two API calls — one to retrieve the CubeHierarchy's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the CubeHierarchy
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated CubeHierarchy
     */
    public static CubeHierarchy appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a CubeHierarchy, without replacing existing Atlan tags linked to the CubeHierarchy.
     * Note: this operation must make two API calls — one to retrieve the CubeHierarchy's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the CubeHierarchy
     * @param qualifiedName of the CubeHierarchy
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated CubeHierarchy
     */
    public static CubeHierarchy appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (CubeHierarchy) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a CubeHierarchy, without replacing existing Atlan tags linked to the CubeHierarchy.
     * Note: this operation must make two API calls — one to retrieve the CubeHierarchy's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the CubeHierarchy
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated CubeHierarchy
     */
    public static CubeHierarchy appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return appendAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a CubeHierarchy, without replacing existing Atlan tags linked to the CubeHierarchy.
     * Note: this operation must make two API calls — one to retrieve the CubeHierarchy's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the CubeHierarchy
     * @param qualifiedName of the CubeHierarchy
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated CubeHierarchy
     */
    public static CubeHierarchy appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (CubeHierarchy) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a CubeHierarchy.
     *
     * @param qualifiedName of the CubeHierarchy
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the CubeHierarchy
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a CubeHierarchy.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a CubeHierarchy
     * @param qualifiedName of the CubeHierarchy
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the CubeHierarchy
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
