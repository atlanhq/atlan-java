/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a README in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class Readme extends Asset implements IReadme, IResource, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Readme";

    /** Fixed typeName for Readmes. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Asset that this README describes. */
    @Attribute
    IAsset asset;

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

    /** Whether the resource is global (true) or not (false). */
    @Attribute
    Boolean isGlobal;

    /** URL to the resource. */
    @Attribute
    String link;

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

    /** Reference to the resource. */
    @Attribute
    String reference;

    /** Metadata of the resource. */
    @Attribute
    @Singular("putResourceMetadata")
    Map<String, String> resourceMetadata;

    /** TBC */
    @Attribute
    @Singular("seeAlsoOne")
    SortedSet<IReadme> seeAlso;

    /**
     * Builds the minimal object necessary to create a relationship to a Readme, from a potentially
     * more-complete Readme object.
     *
     * @return the minimal object necessary to relate to the Readme
     * @throws InvalidRequestException if any of the minimal set of required properties for a Readme relationship are not found in the initial object
     */
    @Override
    public Readme trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all Readme assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) Readme assets will be included.
     *
     * @return a fluent search that includes all Readme assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all Readme assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) Readme assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all Readme assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all Readme assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) Readmes will be included
     * @return a fluent search that includes all Readme assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all Readme assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) Readmes will be included
     * @return a fluent search that includes all Readme assets
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
     * Reference to a Readme by GUID. Use this to create a relationship to this Readme,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the Readme to reference
     * @return reference to a Readme that can be used for defining a relationship to a Readme
     */
    public static Readme refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a Readme by GUID. Use this to create a relationship to this Readme,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the Readme to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a Readme that can be used for defining a relationship to a Readme
     */
    public static Readme refByGuid(String guid, Reference.SaveSemantic semantic) {
        return Readme._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a Readme by qualifiedName. Use this to create a relationship to this Readme,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the Readme to reference
     * @return reference to a Readme that can be used for defining a relationship to a Readme
     */
    public static Readme refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a Readme by qualifiedName. Use this to create a relationship to this Readme,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the Readme to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a Readme that can be used for defining a relationship to a Readme
     */
    public static Readme refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return Readme._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a Readme by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the Readme to retrieve, either its GUID or its full qualifiedName
     * @return the requested full Readme, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Readme does not exist or the provided GUID is not a Readme
     */
    @JsonIgnore
    public static Readme get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a Readme by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Readme to retrieve, either its GUID or its full qualifiedName
     * @return the requested full Readme, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Readme does not exist or the provided GUID is not a Readme
     */
    @JsonIgnore
    public static Readme get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a Readme by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Readme to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full Readme, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Readme does not exist or the provided GUID is not a Readme
     */
    @JsonIgnore
    public static Readme get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof Readme) {
                return (Readme) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof Readme) {
                return (Readme) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) Readme to active.
     *
     * @param qualifiedName for the Readme
     * @return true if the Readme is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) Readme to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the Readme
     * @return true if the Readme is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a README.
     * Note that the provided asset must have a real (not a placeholder) GUID.
     *
     * @param asset the asset to which the README should be attached, including its GUID and name
     * @param content the HTML content to use for the README
     * @return the minimal object necessary to create the README and attach it to the asset, as a builder
     * @throws InvalidRequestException if any of the required details are missing from the provided asset
     */
    public static ReadmeBuilder<?, ?> creator(Asset asset, String content) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("guid", asset.getQualifiedName());
        map.put("name", asset.getName());
        validateRelationship(asset.getTypeName(), map);
        return creator(asset.trimToReference(), asset.getName(), content);
    }

    /**
     * Builds the minimal object necessary to create a README.
     *
     * @param reference a reference, by GUID, to the asset to which the README should be attached
     * @param assetName name of the asset to which the README should be attached
     * @param content the HTML content to use for the README
     * @return the minimal object necessary to create the README and attach it to the asset, as a builder
     */
    public static ReadmeBuilder<?, ?> creator(Asset reference, String assetName, String content) {
        return Readme._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(reference.getGuid()))
                .name(generateName(assetName))
                .description(content)
                .asset(reference);
    }

    /**
     * Builds the minimal object necessary to update a Readme.
     *
     * @param assetGuid the GUID of the asset to which the README is attached
     * @param assetName name of the asset to which the README is attached
     * @return the minimal request necessary to update the Readme, as a builder
     */
    public static ReadmeBuilder<?, ?> updater(String assetGuid, String assetName) {
        return Readme._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(assetGuid))
                .name(generateName(assetName));
    }

    /**
     * Builds the minimal object necessary to apply an update to a Readme, from a potentially
     * more-complete Readme object.
     *
     * @return the minimal object necessary to update the Readme, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for Readme are not found in the initial object
     */
    @Override
    public ReadmeBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Generate a unique README name.
     *
     * @param assetGuid GUID of the asset to which the README should be attached
     * @return a unique name for the README
     */
    private static String generateQualifiedName(String assetGuid) {
        return assetGuid + "/readme";
    }

    /**
     * Generate a readable README name (although this does not appear anywhere in the UI).
     *
     * @param assetName name of the asset to which the README should be attached
     * @return a readable name for the README
     */
    private static String generateName(String assetName) {
        return assetName + " Readme";
    }

    /**
     * Remove the system description from a Readme.
     *
     * @param qualifiedName of the Readme
     * @param name of the Readme
     * @return the updated Readme, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Readme removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a Readme.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the Readme
     * @param name of the Readme
     * @return the updated Readme, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Readme removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Readme) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a Readme.
     *
     * @param qualifiedName of the Readme
     * @param name of the Readme
     * @return the updated Readme, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Readme removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a Readme.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the Readme
     * @param name of the Readme
     * @return the updated Readme, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Readme removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Readme) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }
}
