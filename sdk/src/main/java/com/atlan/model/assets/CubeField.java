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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a cube field in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class CubeField extends Asset implements ICubeField, IMultiDimensionalDataset, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "CubeField";

    /** Fixed typeName for CubeFields. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Simple name of the cube dimension in which this asset exists, or empty if it is itself a dimension. */
    @Attribute
    String cubeDimensionName;

    /** Unique name of the cube dimension in which this asset exists, or empty if it is itself a dimension. */
    @Attribute
    String cubeDimensionQualifiedName;

    /** Generation of the field in the cube hierarchy. */
    @Attribute
    Long cubeFieldGeneration;

    /** Level of the field in the cube hierarchy. */
    @Attribute
    Long cubeFieldLevel;

    /** Expression used to calculate this measure. */
    @Attribute
    String cubeFieldMeasureExpression;

    /** Hierarchy in which this field exists. */
    @Attribute
    ICubeHierarchy cubeHierarchy;

    /** Simple name of the dimension hierarchy in which this asset exists, or empty if it is itself a hierarchy. */
    @Attribute
    String cubeHierarchyName;

    /** Unique name of the dimension hierarchy in which this asset exists, or empty if it is itself a hierarchy. */
    @Attribute
    String cubeHierarchyQualifiedName;

    /** Simple name of the cube in which this asset exists, or empty if it is itself a cube. */
    @Attribute
    String cubeName;

    /** Individual fields nested within this cube field. */
    @Attribute
    @Singular
    SortedSet<ICubeField> cubeNestedFields;

    /** Parent field in which this field is nested. */
    @Attribute
    ICubeField cubeParentField;

    /** Name of the parent field in which this field is nested. */
    @Attribute
    String cubeParentFieldName;

    /** Unique name of the parent field in which this field is nested. */
    @Attribute
    String cubeParentFieldQualifiedName;

    /** Unique name of the cube in which this asset exists, or empty if it is itself a cube. */
    @Attribute
    String cubeQualifiedName;

    /** Number of sub-fields that are direct children of this field. */
    @Attribute
    Long cubeSubFieldCount;

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
     * Builds the minimal object necessary to create a relationship to a CubeField, from a potentially
     * more-complete CubeField object.
     *
     * @return the minimal object necessary to relate to the CubeField
     * @throws InvalidRequestException if any of the minimal set of required properties for a CubeField relationship are not found in the initial object
     */
    @Override
    public CubeField trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all CubeField assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) CubeField assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all CubeField assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all CubeField assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) CubeFields will be included
     * @return a fluent search that includes all CubeField assets
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
     * Reference to a CubeField by GUID. Use this to create a relationship to this CubeField,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the CubeField to reference
     * @return reference to a CubeField that can be used for defining a relationship to a CubeField
     */
    public static CubeField refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a CubeField by GUID. Use this to create a relationship to this CubeField,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the CubeField to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a CubeField that can be used for defining a relationship to a CubeField
     */
    public static CubeField refByGuid(String guid, Reference.SaveSemantic semantic) {
        return CubeField._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a CubeField by qualifiedName. Use this to create a relationship to this CubeField,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the CubeField to reference
     * @return reference to a CubeField that can be used for defining a relationship to a CubeField
     */
    public static CubeField refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a CubeField by qualifiedName. Use this to create a relationship to this CubeField,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the CubeField to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a CubeField that can be used for defining a relationship to a CubeField
     */
    public static CubeField refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return CubeField._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a CubeField by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CubeField to retrieve, either its GUID or its full qualifiedName
     * @return the requested full CubeField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CubeField does not exist or the provided GUID is not a CubeField
     */
    @JsonIgnore
    public static CubeField get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a CubeField by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CubeField to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full CubeField, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CubeField does not exist or the provided GUID is not a CubeField
     */
    @JsonIgnore
    public static CubeField get(AtlanClient client, String id, boolean includeAllRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof CubeField) {
                return (CubeField) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof CubeField) {
                return (CubeField) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a CubeField by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CubeField to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the CubeField, including any relationships
     * @return the requested CubeField, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CubeField does not exist or the provided GUID is not a CubeField
     */
    @JsonIgnore
    public static CubeField get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a CubeField by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CubeField to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the CubeField, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the CubeField
     * @return the requested CubeField, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CubeField does not exist or the provided GUID is not a CubeField
     */
    @JsonIgnore
    public static CubeField get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = CubeField.select(client)
                    .where(CubeField.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof CubeField) {
                return (CubeField) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = CubeField.select(client)
                    .where(CubeField.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof CubeField) {
                return (CubeField) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) CubeField to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the CubeField
     * @return true if the CubeField is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a top-level CubeField.
     *
     * @param name of the CubeField
     * @param hierarchy in which the field should be created, which must have at least a qualifiedName
     * @return the minimal request necessary to create the CubeField, as a builder
     * @throws InvalidRequestException if the hierarchy provided is without a qualifiedName
     */
    public static CubeFieldBuilder<?, ?> creator(String name, CubeHierarchy hierarchy) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", hierarchy.getConnectionQualifiedName());
        map.put("cubeName", hierarchy.getCubeName());
        map.put("cubeQualifiedName", hierarchy.getCubeQualifiedName());
        map.put("cubeDimensionName", hierarchy.getCubeDimensionName());
        map.put("cubeDimensionQualifiedName", hierarchy.getCubeDimensionQualifiedName());
        map.put("name", hierarchy.getName());
        map.put("qualifiedName", hierarchy.getQualifiedName());
        validateRelationship(CubeHierarchy.TYPE_NAME, map);
        return creator(
                        name,
                        hierarchy.getConnectionQualifiedName(),
                        hierarchy.getCubeName(),
                        hierarchy.getCubeQualifiedName(),
                        hierarchy.getCubeDimensionName(),
                        hierarchy.getCubeDimensionQualifiedName(),
                        hierarchy.getName(),
                        hierarchy.getQualifiedName(),
                        null,
                        null)
                .cubeHierarchy(hierarchy.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a nested CubeField.
     *
     * @param name of the CubeField
     * @param parentField in which the field should be created, which must have at least a qualifiedName
     * @return the minimal request necessary to create the CubeField, as a builder
     * @throws InvalidRequestException if the parent field provided is without a qualifiedName
     */
    public static CubeFieldBuilder<?, ?> creator(String name, CubeField parentField) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", parentField.getConnectionQualifiedName());
        map.put("cubeName", parentField.getCubeName());
        map.put("cubeQualifiedName", parentField.getCubeQualifiedName());
        map.put("cubeDimensionName", parentField.getCubeDimensionName());
        map.put("cubeDimensionQualifiedName", parentField.getCubeDimensionQualifiedName());
        map.put("cubeHierarchyName", parentField.getCubeHierarchyName());
        map.put("cubeHierarchyQualifiedName", parentField.getCubeHierarchyQualifiedName());
        map.put("name", parentField.getName());
        map.put("qualifiedName", parentField.getQualifiedName());
        validateRelationship(CubeField.TYPE_NAME, map);
        return creator(
                        name,
                        parentField.getConnectionQualifiedName(),
                        parentField.getCubeName(),
                        parentField.getCubeQualifiedName(),
                        parentField.getCubeDimensionName(),
                        parentField.getCubeDimensionQualifiedName(),
                        parentField.getCubeHierarchyName(),
                        parentField.getCubeHierarchyQualifiedName(),
                        parentField.getName(),
                        parentField.getQualifiedName())
                .cubeParentField(parentField.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a CubeField.
     *
     * @param name of the CubeField
     * @param parentQualifiedName unique name of the parent of the CubeField (either of its hierarchy or parent field)
     * @return the minimal request necessary to create the CubeField, as a builder
     */
    public static CubeFieldBuilder<?, ?> creator(String name, String parentQualifiedName) {
        String parentFieldName = null;
        String parentFieldQualifiedName = null;
        String hierarchyQualifiedName = getHierarchyQualifiedName(parentQualifiedName);
        if (!hierarchyQualifiedName.equals(parentQualifiedName)) {
            parentFieldQualifiedName = parentQualifiedName;
            String parentSlug = StringUtils.getNameFromQualifiedName(parentQualifiedName);
            parentFieldName = IMultiDimensionalDataset.getNameFromSlug(parentSlug);
        }
        String hierarchySlug = StringUtils.getNameFromQualifiedName(hierarchyQualifiedName);
        String hierarchyName = IMultiDimensionalDataset.getNameFromSlug(hierarchySlug);
        String dimensionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(hierarchyQualifiedName);
        String dimensionSlug = StringUtils.getNameFromQualifiedName(dimensionQualifiedName);
        String dimensionName = IMultiDimensionalDataset.getNameFromSlug(dimensionSlug);
        String cubeQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(dimensionQualifiedName);
        String cubeSlug = StringUtils.getNameFromQualifiedName(cubeQualifiedName);
        String cubeName = IMultiDimensionalDataset.getNameFromSlug(cubeSlug);
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(cubeQualifiedName);
        return creator(
                name,
                connectionQualifiedName,
                cubeName,
                cubeQualifiedName,
                dimensionName,
                dimensionQualifiedName,
                hierarchyName,
                hierarchyQualifiedName,
                parentFieldName,
                parentFieldQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a CubeField.
     *
     * @param name of the CubeField
     * @param connectionQualifiedName unique name of the connection in which the CubeField should be created
     * @param cubeName simple name of the Cube in which the CubeField should be created
     * @param cubeQualifiedName unique name of the Cube in which the CubeField should be created
     * @param dimensionName simple name of the CubeDimension in which the CubeField should be created
     * @param dimensionQualifiedName unique name of the CubeDimension in which the CubeField should be created
     * @param hierarchyName simple name of the CubeHierarchy in which the CubeField should be created
     * @param hierarchyQualifiedName unique name of the CubeHierarchy in which the CubeField should be created
     * @param parentFieldName simple name of the CubeField in which the CubeField should be nested
     * @param parentFieldQualifiedName unique name of the CubeField in which the CubeField should be nested
     * @return the minimal request necessary to create the CubeField, as a builder
     */
    public static CubeFieldBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String cubeName,
            String cubeQualifiedName,
            String dimensionName,
            String dimensionQualifiedName,
            String hierarchyName,
            String hierarchyQualifiedName,
            String parentFieldName,
            String parentFieldQualifiedName) {
        CubeFieldBuilder<?, ?> builder = CubeField._internal().name(name);
        if (parentFieldName != null && parentFieldQualifiedName != null) {
            builder.cubeParentField(CubeField.refByQualifiedName(parentFieldQualifiedName))
                    .cubeParentFieldName(parentFieldName)
                    .cubeParentFieldQualifiedName(parentFieldQualifiedName)
                    .qualifiedName(generateQualifiedName(name, parentFieldQualifiedName));
        } else {
            builder.qualifiedName(generateQualifiedName(name, hierarchyQualifiedName));
        }
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return builder.guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .cubeName(cubeName)
                .cubeQualifiedName(cubeQualifiedName)
                .cubeDimensionName(dimensionName)
                .cubeDimensionQualifiedName(dimensionQualifiedName)
                .cubeHierarchyName(hierarchyName)
                .cubeHierarchyQualifiedName(hierarchyQualifiedName)
                .cubeHierarchy(CubeHierarchy.refByQualifiedName(hierarchyQualifiedName))
                .connectorType(connectorType)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a CubeField.
     *
     * @param qualifiedName of the CubeField
     * @param name of the CubeField
     * @return the minimal request necessary to update the CubeField, as a builder
     */
    public static CubeFieldBuilder<?, ?> updater(String qualifiedName, String name) {
        return CubeField._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Extracts the unique name of the hierarchy from the qualified name of the CubeField's parent.
     *
     * @param parentQualifiedName unique name of the hierarchy or parent field in which this CubeField exists
     * @return the unique name of the CubeHierarchy in which the field exists
     */
    public static String getHierarchyQualifiedName(String parentQualifiedName) {
        if (parentQualifiedName != null) {
            List<String> tokens = Arrays.stream(parentQualifiedName.split("/")).collect(Collectors.toList());
            return String.join("/", tokens.subList(0, 6));
        }
        return null;
    }

    /**
     * Generate a unique CubeField name.
     *
     * @param name of the CubeField
     * @param parentQualifiedName unique name of the hierarchy or parent field in which this CubeField exists
     * @return a unique name for the CubeField
     */
    public static String generateQualifiedName(String name, String parentQualifiedName) {
        return parentQualifiedName + "/" + IMultiDimensionalDataset.getSlugForName(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a CubeField, from a potentially
     * more-complete CubeField object.
     *
     * @return the minimal object necessary to update the CubeField, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for CubeField are not found in the initial object
     */
    @Override
    public CubeFieldBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a CubeField.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the CubeField
     * @param name of the CubeField
     * @return the updated CubeField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CubeField removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CubeField) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a CubeField.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the CubeField
     * @param name of the CubeField
     * @return the updated CubeField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CubeField removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CubeField) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a CubeField.
     *
     * @param client connectivity to the Atlan tenant from which to remove the CubeField's owners
     * @param qualifiedName of the CubeField
     * @param name of the CubeField
     * @return the updated CubeField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CubeField removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (CubeField) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a CubeField.
     *
     * @param client connectivity to the Atlan tenant on which to update the CubeField's certificate
     * @param qualifiedName of the CubeField
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated CubeField, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CubeField updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (CubeField) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a CubeField.
     *
     * @param client connectivity to the Atlan tenant from which to remove the CubeField's certificate
     * @param qualifiedName of the CubeField
     * @param name of the CubeField
     * @return the updated CubeField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CubeField removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CubeField) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a CubeField.
     *
     * @param client connectivity to the Atlan tenant on which to update the CubeField's announcement
     * @param qualifiedName of the CubeField
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CubeField updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (CubeField)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a CubeField.
     *
     * @param client connectivity to the Atlan client from which to remove the CubeField's announcement
     * @param qualifiedName of the CubeField
     * @param name of the CubeField
     * @return the updated CubeField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CubeField removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CubeField) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the CubeField.
     *
     * @param client connectivity to the Atlan tenant on which to replace the CubeField's assigned terms
     * @param qualifiedName for the CubeField
     * @param name human-readable name of the CubeField
     * @param terms the list of terms to replace on the CubeField, or null to remove all terms from the CubeField
     * @return the CubeField that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static CubeField replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (CubeField) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the CubeField, without replacing existing terms linked to the CubeField.
     * Note: this operation must make two API calls — one to retrieve the CubeField's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the CubeField
     * @param qualifiedName for the CubeField
     * @param terms the list of terms to append to the CubeField
     * @return the CubeField that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static CubeField appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (CubeField) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a CubeField, without replacing all existing terms linked to the CubeField.
     * Note: this operation must make two API calls — one to retrieve the CubeField's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the CubeField
     * @param qualifiedName for the CubeField
     * @param terms the list of terms to remove from the CubeField, which must be referenced by GUID
     * @return the CubeField that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static CubeField removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (CubeField) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a CubeField, without replacing existing Atlan tags linked to the CubeField.
     * Note: this operation must make two API calls — one to retrieve the CubeField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the CubeField
     * @param qualifiedName of the CubeField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated CubeField
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static CubeField appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (CubeField) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a CubeField, without replacing existing Atlan tags linked to the CubeField.
     * Note: this operation must make two API calls — one to retrieve the CubeField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the CubeField
     * @param qualifiedName of the CubeField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated CubeField
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static CubeField appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (CubeField) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a CubeField.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a CubeField
     * @param qualifiedName of the CubeField
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the CubeField
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
