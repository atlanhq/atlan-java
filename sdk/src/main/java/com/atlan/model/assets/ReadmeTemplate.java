/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.IconType;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
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

    /** Application module that is implemented by this asset. */
    @Attribute
    IAppModule appModuleImplemented;

    /** Icon to use for the README template. */
    @Attribute
    String icon;

    /** Type of icon, for example: image or emoji. */
    @Attribute
    IconType iconType;

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
     * Start a fluent search that will return all ReadmeTemplate assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ReadmeTemplate assets will be included.
     *
     * @return a fluent search that includes all ReadmeTemplate assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all ReadmeTemplate assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ReadmeTemplate assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all ReadmeTemplate assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all ReadmeTemplate assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) ReadmeTemplates will be included
     * @return a fluent search that includes all ReadmeTemplate assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all ReadmeTemplate assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ReadmeTemplates will be included
     * @return a fluent search that includes all ReadmeTemplate assets
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
     * Reference to a ReadmeTemplate by GUID. Use this to create a relationship to this ReadmeTemplate,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the ReadmeTemplate to reference
     * @return reference to a ReadmeTemplate that can be used for defining a relationship to a ReadmeTemplate
     */
    public static ReadmeTemplate refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ReadmeTemplate by GUID. Use this to create a relationship to this ReadmeTemplate,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the ReadmeTemplate to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ReadmeTemplate that can be used for defining a relationship to a ReadmeTemplate
     */
    public static ReadmeTemplate refByGuid(String guid, Reference.SaveSemantic semantic) {
        return ReadmeTemplate._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a ReadmeTemplate by qualifiedName. Use this to create a relationship to this ReadmeTemplate,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the ReadmeTemplate to reference
     * @return reference to a ReadmeTemplate that can be used for defining a relationship to a ReadmeTemplate
     */
    public static ReadmeTemplate refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ReadmeTemplate by qualifiedName. Use this to create a relationship to this ReadmeTemplate,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the ReadmeTemplate to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ReadmeTemplate that can be used for defining a relationship to a ReadmeTemplate
     */
    public static ReadmeTemplate refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return ReadmeTemplate._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
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
        return ReadmeTemplate._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
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
        validateRequired(
                TYPE_NAME,
                Map.of(
                        "qualifiedName", this.getQualifiedName(),
                        "name", this.getName()));
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
