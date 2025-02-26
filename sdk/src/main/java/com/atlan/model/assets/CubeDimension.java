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
 * Instance of a cube dimension in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class CubeDimension extends Asset
        implements ICubeDimension, IMultiDimensionalDataset, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "CubeDimension";

    /** Fixed typeName for CubeDimensions. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Cube in which this dimension exists. */
    @Attribute
    ICube cube;

    /** Simple name of the cube dimension in which this asset exists, or empty if it is itself a dimension. */
    @Attribute
    String cubeDimensionName;

    /** Unique name of the cube dimension in which this asset exists, or empty if it is itself a dimension. */
    @Attribute
    String cubeDimensionQualifiedName;

    /** Individual hierarchies that make up the dimension. */
    @Attribute
    @Singular
    SortedSet<ICubeHierarchy> cubeHierarchies;

    /** Number of hierarchies in the cube dimension. */
    @Attribute
    Long cubeHierarchyCount;

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
     * Builds the minimal object necessary to create a relationship to a CubeDimension, from a potentially
     * more-complete CubeDimension object.
     *
     * @return the minimal object necessary to relate to the CubeDimension
     * @throws InvalidRequestException if any of the minimal set of required properties for a CubeDimension relationship are not found in the initial object
     */
    @Override
    public CubeDimension trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all CubeDimension assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) CubeDimension assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all CubeDimension assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all CubeDimension assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) CubeDimensions will be included
     * @return a fluent search that includes all CubeDimension assets
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
     * Reference to a CubeDimension by GUID. Use this to create a relationship to this CubeDimension,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the CubeDimension to reference
     * @return reference to a CubeDimension that can be used for defining a relationship to a CubeDimension
     */
    public static CubeDimension refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a CubeDimension by GUID. Use this to create a relationship to this CubeDimension,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the CubeDimension to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a CubeDimension that can be used for defining a relationship to a CubeDimension
     */
    public static CubeDimension refByGuid(String guid, Reference.SaveSemantic semantic) {
        return CubeDimension._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a CubeDimension by qualifiedName. Use this to create a relationship to this CubeDimension,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the CubeDimension to reference
     * @return reference to a CubeDimension that can be used for defining a relationship to a CubeDimension
     */
    public static CubeDimension refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a CubeDimension by qualifiedName. Use this to create a relationship to this CubeDimension,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the CubeDimension to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a CubeDimension that can be used for defining a relationship to a CubeDimension
     */
    public static CubeDimension refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return CubeDimension._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a CubeDimension by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CubeDimension to retrieve, either its GUID or its full qualifiedName
     * @return the requested full CubeDimension, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CubeDimension does not exist or the provided GUID is not a CubeDimension
     */
    @JsonIgnore
    public static CubeDimension get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a CubeDimension by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CubeDimension to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full CubeDimension, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CubeDimension does not exist or the provided GUID is not a CubeDimension
     */
    @JsonIgnore
    public static CubeDimension get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof CubeDimension) {
                return (CubeDimension) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof CubeDimension) {
                return (CubeDimension) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a CubeDimension by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CubeDimension to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the CubeDimension, including any relationships
     * @return the requested CubeDimension, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CubeDimension does not exist or the provided GUID is not a CubeDimension
     */
    @JsonIgnore
    public static CubeDimension get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a CubeDimension by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CubeDimension to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the CubeDimension, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the CubeDimension
     * @return the requested CubeDimension, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CubeDimension does not exist or the provided GUID is not a CubeDimension
     */
    @JsonIgnore
    public static CubeDimension get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = CubeDimension.select(client)
                    .where(CubeDimension.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof CubeDimension) {
                return (CubeDimension) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = CubeDimension.select(client)
                    .where(CubeDimension.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof CubeDimension) {
                return (CubeDimension) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) CubeDimension to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the CubeDimension
     * @return true if the CubeDimension is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a CubeDimension.
     *
     * @param name of the CubeDimension
     * @param cube in which the dimension should be created, which must have at least a qualifiedName
     * @return the minimal request necessary to create the CubeDimension, as a builder
     * @throws InvalidRequestException if the cube provided is without a qualifiedName
     */
    public static CubeDimensionBuilder<?, ?> creator(String name, Cube cube) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", cube.getQualifiedName());
        map.put("name", cube.getName());
        map.put("connectionQualifiedName", cube.getConnectionQualifiedName());
        validateRelationship(Cube.TYPE_NAME, map);
        return creator(name, cube.getConnectionQualifiedName(), cube.getName(), cube.getQualifiedName())
                .cube(cube.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a CubeDimension.
     *
     * @param name of the CubeDimension
     * @param cubeQualifiedName unique name of the cube in which this CubeDimension exists
     * @return the minimal request necessary to create the CubeDimension, as a builder
     */
    public static CubeDimensionBuilder<?, ?> creator(String name, String cubeQualifiedName) {
        String cubeSlug = StringUtils.getNameFromQualifiedName(cubeQualifiedName);
        String cubeName = IMultiDimensionalDataset.getNameFromSlug(cubeSlug);
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(cubeQualifiedName);
        return creator(name, connectionQualifiedName, cubeName, cubeQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a CubeDimension.
     *
     * @param name of the CubeDimension
     * @param connectionQualifiedName unique name of the connection in which to create this CubeDimension
     * @param cubeName simple name of the cube in which to create this CubeDimension
     * @param cubeQualifiedName unique name of the cube in which to create this CubeDimension
     * @return the minimal request necessary to create the CubeDimension, as a builder
     */
    public static CubeDimensionBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String cubeName, String cubeQualifiedName) {
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return CubeDimension._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, cubeQualifiedName))
                .cubeName(cubeName)
                .cubeQualifiedName(cubeQualifiedName)
                .cube(Cube.refByQualifiedName(cubeQualifiedName))
                .connectorType(connectorType)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a CubeDimension.
     *
     * @param qualifiedName of the CubeDimension
     * @param name of the CubeDimension
     * @return the minimal request necessary to update the CubeDimension, as a builder
     */
    public static CubeDimensionBuilder<?, ?> updater(String qualifiedName, String name) {
        return CubeDimension._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique CubeDimension name.
     *
     * @param name of the CubeDimension
     * @param cubeQualifiedName unique name of the cube in which this CubeDimension exists
     * @return a unique name for the CubeDimension
     */
    public static String generateQualifiedName(String name, String cubeQualifiedName) {
        return cubeQualifiedName + "/" + IMultiDimensionalDataset.getSlugForName(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a CubeDimension, from a potentially
     * more-complete CubeDimension object.
     *
     * @return the minimal object necessary to update the CubeDimension, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for CubeDimension are not found in the initial object
     */
    @Override
    public CubeDimensionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a CubeDimension.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the CubeDimension
     * @param name of the CubeDimension
     * @return the updated CubeDimension, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CubeDimension removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CubeDimension) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a CubeDimension.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the CubeDimension
     * @param name of the CubeDimension
     * @return the updated CubeDimension, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CubeDimension removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CubeDimension) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a CubeDimension.
     *
     * @param client connectivity to the Atlan tenant from which to remove the CubeDimension's owners
     * @param qualifiedName of the CubeDimension
     * @param name of the CubeDimension
     * @return the updated CubeDimension, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CubeDimension removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CubeDimension) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a CubeDimension.
     *
     * @param client connectivity to the Atlan tenant on which to update the CubeDimension's certificate
     * @param qualifiedName of the CubeDimension
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated CubeDimension, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CubeDimension updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (CubeDimension)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a CubeDimension.
     *
     * @param client connectivity to the Atlan tenant from which to remove the CubeDimension's certificate
     * @param qualifiedName of the CubeDimension
     * @param name of the CubeDimension
     * @return the updated CubeDimension, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CubeDimension removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CubeDimension) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a CubeDimension.
     *
     * @param client connectivity to the Atlan tenant on which to update the CubeDimension's announcement
     * @param qualifiedName of the CubeDimension
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CubeDimension updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (CubeDimension)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a CubeDimension.
     *
     * @param client connectivity to the Atlan client from which to remove the CubeDimension's announcement
     * @param qualifiedName of the CubeDimension
     * @param name of the CubeDimension
     * @return the updated CubeDimension, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CubeDimension removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CubeDimension) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the CubeDimension.
     *
     * @param client connectivity to the Atlan tenant on which to replace the CubeDimension's assigned terms
     * @param qualifiedName for the CubeDimension
     * @param name human-readable name of the CubeDimension
     * @param terms the list of terms to replace on the CubeDimension, or null to remove all terms from the CubeDimension
     * @return the CubeDimension that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static CubeDimension replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (CubeDimension) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the CubeDimension, without replacing existing terms linked to the CubeDimension.
     * Note: this operation must make two API calls — one to retrieve the CubeDimension's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the CubeDimension
     * @param qualifiedName for the CubeDimension
     * @param terms the list of terms to append to the CubeDimension
     * @return the CubeDimension that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static CubeDimension appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (CubeDimension) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a CubeDimension, without replacing all existing terms linked to the CubeDimension.
     * Note: this operation must make two API calls — one to retrieve the CubeDimension's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the CubeDimension
     * @param qualifiedName for the CubeDimension
     * @param terms the list of terms to remove from the CubeDimension, which must be referenced by GUID
     * @return the CubeDimension that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static CubeDimension removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (CubeDimension) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a CubeDimension, without replacing existing Atlan tags linked to the CubeDimension.
     * Note: this operation must make two API calls — one to retrieve the CubeDimension's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the CubeDimension
     * @param qualifiedName of the CubeDimension
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated CubeDimension
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static CubeDimension appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (CubeDimension) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a CubeDimension, without replacing existing Atlan tags linked to the CubeDimension.
     * Note: this operation must make two API calls — one to retrieve the CubeDimension's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the CubeDimension
     * @param qualifiedName of the CubeDimension
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated CubeDimension
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static CubeDimension appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (CubeDimension) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a CubeDimension.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a CubeDimension
     * @param qualifiedName of the CubeDimension
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the CubeDimension
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
