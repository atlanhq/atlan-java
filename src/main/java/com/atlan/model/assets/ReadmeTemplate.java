/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.AssetFilter;
import com.atlan.model.enums.IconType;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.QueryFactory;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a README template in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class ReadmeTemplate extends Asset implements IReadmeTemplate, IResource, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ReadmeTemplate";

    /** Fixed typeName for ReadmeTemplates. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String icon;

    /** TBC */
    @Attribute
    IconType iconType;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> inputToAirflowTasks;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    Boolean isGlobal;

    /** TBC */
    @Attribute
    String link;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> outputFromAirflowTasks;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    String reference;

    /** TBC */
    @Attribute
    @Singular("putResourceMetadata")
    Map<String, String> resourceMetadata;

    /**
     * Builds the minimal object necessary to create a relationship to a ReadmeTemplate, from a potentially
     * more-complete ReadmeTemplate object.
     *
     * @return the minimal object necessary to relate to the ReadmeTemplate
     * @throws InvalidRequestException if any of the minimal set of required properties for a ReadmeTemplate relationship are not found in the initial object
     */
    @Override
    public ReadmeTemplate trimToReference() throws InvalidRequestException {
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
     * Start an asset filter that will return all ReadmeTemplate assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ReadmeTemplate assets will be included.
     *
     * @return an asset filter that includes all ReadmeTemplate assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all ReadmeTemplate assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ReadmeTemplate assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all ReadmeTemplate assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all ReadmeTemplate assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) ReadmeTemplates will be included
     * @return an asset filter that includes all ReadmeTemplate assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all ReadmeTemplate assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ReadmeTemplates will be included
     * @return an asset filter that includes all ReadmeTemplate assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client, boolean includeArchived) {
        AssetFilter.AssetFilterBuilder builder =
                AssetFilter.builder().client(client).filter(QueryFactory.type(TYPE_NAME));
        if (!includeArchived) {
            builder.filter(QueryFactory.active());
        }
        return builder;
    }

    /**
     * Reference to a ReadmeTemplate by GUID.
     *
     * @param guid the GUID of the ReadmeTemplate to reference
     * @return reference to a ReadmeTemplate that can be used for defining a relationship to a ReadmeTemplate
     */
    public static ReadmeTemplate refByGuid(String guid) {
        return ReadmeTemplate._internal().guid(guid).build();
    }

    /**
     * Reference to a ReadmeTemplate by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the ReadmeTemplate to reference
     * @return reference to a ReadmeTemplate that can be used for defining a relationship to a ReadmeTemplate
     */
    public static ReadmeTemplate refByQualifiedName(String qualifiedName) {
        return ReadmeTemplate._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a ReadmeTemplate by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the ReadmeTemplate to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ReadmeTemplate, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ReadmeTemplate does not exist or the provided GUID is not a ReadmeTemplate
     */
    @JsonIgnore
    public static ReadmeTemplate get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a ReadmeTemplate by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ReadmeTemplate to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ReadmeTemplate, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ReadmeTemplate does not exist or the provided GUID is not a ReadmeTemplate
     */
    @JsonIgnore
    public static ReadmeTemplate get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a ReadmeTemplate by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ReadmeTemplate to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full ReadmeTemplate, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ReadmeTemplate does not exist or the provided GUID is not a ReadmeTemplate
     */
    @JsonIgnore
    public static ReadmeTemplate get(AtlanClient client, String id, boolean includeRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof ReadmeTemplate) {
                return (ReadmeTemplate) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof ReadmeTemplate) {
                return (ReadmeTemplate) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a ReadmeTemplate by its GUID, complete with all of its relationships.
     *
     * @param guid of the ReadmeTemplate to retrieve
     * @return the requested full ReadmeTemplate, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ReadmeTemplate does not exist or the provided GUID is not a ReadmeTemplate
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static ReadmeTemplate retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a ReadmeTemplate by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the ReadmeTemplate to retrieve
     * @return the requested full ReadmeTemplate, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ReadmeTemplate does not exist or the provided GUID is not a ReadmeTemplate
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static ReadmeTemplate retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a ReadmeTemplate by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the ReadmeTemplate to retrieve
     * @return the requested full ReadmeTemplate, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ReadmeTemplate does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static ReadmeTemplate retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a ReadmeTemplate by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the ReadmeTemplate to retrieve
     * @return the requested full ReadmeTemplate, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ReadmeTemplate does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static ReadmeTemplate retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) ReadmeTemplate to active.
     *
     * @param qualifiedName for the ReadmeTemplate
     * @return true if the ReadmeTemplate is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) ReadmeTemplate to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the ReadmeTemplate
     * @return true if the ReadmeTemplate is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a ReadmeTemplate.
     *
     * @param qualifiedName of the ReadmeTemplate
     * @param name of the ReadmeTemplate
     * @return the minimal request necessary to update the ReadmeTemplate, as a builder
     */
    public static ReadmeTemplateBuilder<?, ?> updater(String qualifiedName, String name) {
        return ReadmeTemplate._internal().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ReadmeTemplate, from a potentially
     * more-complete ReadmeTemplate object.
     *
     * @return the minimal object necessary to update the ReadmeTemplate, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ReadmeTemplate are not found in the initial object
     */
    @Override
    public ReadmeTemplateBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "ReadmeTemplate", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a ReadmeTemplate.
     *
     * @param qualifiedName of the ReadmeTemplate
     * @param name of the ReadmeTemplate
     * @return the updated ReadmeTemplate, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ReadmeTemplate removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a ReadmeTemplate.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ReadmeTemplate
     * @param name of the ReadmeTemplate
     * @return the updated ReadmeTemplate, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ReadmeTemplate removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ReadmeTemplate) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ReadmeTemplate.
     *
     * @param qualifiedName of the ReadmeTemplate
     * @param name of the ReadmeTemplate
     * @return the updated ReadmeTemplate, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ReadmeTemplate removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a ReadmeTemplate.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ReadmeTemplate
     * @param name of the ReadmeTemplate
     * @return the updated ReadmeTemplate, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ReadmeTemplate removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ReadmeTemplate) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }
}
