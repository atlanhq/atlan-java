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
 * Instance of a Snowflake semantic dimension in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class SnowflakeSemanticDimension extends Asset
        implements ISnowflakeSemanticDimension,
                ISemanticDimension,
                ISnowflake,
                ISemantic,
                ICatalog,
                IAsset,
                IReferenceable,
                ISQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SnowflakeSemanticDimension";

    /** Fixed typeName for SnowflakeSemanticDimensions. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Simple name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewName;

    /** Unique name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewQualifiedName;

    /** Unique identifier of the dataset this asset belongs to. */
    @Attribute
    String catalogDatasetGuid;

    /** Simple name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    @Attribute
    String databaseName;

    /** Unique name of the database in which this SQL asset exists, or empty if it does not exist within a database. */
    @Attribute
    String databaseQualifiedName;

    /** (Deprecated) Model containing the assets. */
    @Attribute
    @Singular
    SortedSet<IDbtModel> dbtModels;

    /** DBT seeds that materialize the SQL asset. */
    @Attribute
    @Singular
    SortedSet<IDbtSeed> dbtSeedAssets;

    /** Source containing the assets. */
    @Attribute
    @Singular
    SortedSet<IDbtSource> dbtSources;

    /** Tests related to this asset. */
    @Attribute
    @Singular
    SortedSet<IDbtTest> dbtTests;

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

    /** Whether this asset has been profiled (true) or not (false). */
    @Attribute
    Boolean isProfiled;

    /** Time (epoch) at which this asset was last profiled, in milliseconds. */
    @Attribute
    @Date
    Long lastProfiledAt;

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

    /** Number of times this asset has been queried. */
    @Attribute
    Long queryCount;

    /** Time (epoch) at which the query count was last updated, in milliseconds. */
    @Attribute
    @Date
    Long queryCountUpdatedAt;

    /** Number of unique users who have queried this asset. */
    @Attribute
    Long queryUserCount;

    /** Map of unique users who have queried this asset to the number of times they have queried it. */
    @Attribute
    @Singular("putQueryUserMap")
    Map<String, Long> queryUserMap;

    /** Simple name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaName;

    /** Unique name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaQualifiedName;

    /** Access level for the semantic field (e.g., public_access/private_access). */
    @Attribute
    String semanticAccessModifier;

    /** Data type of the semantic field. */
    @Attribute
    String semanticDataType;

    /** Column name or SQL expression for the semantic field. */
    @Attribute
    String semanticExpression;

    /** Labels associated with the semantic field. */
    @Attribute
    @Singular
    SortedSet<String> semanticLabels;

    /** Semantic model in which this dimension exists. */
    @Attribute
    ISemanticModel semanticModel;

    /** Sample values for the semantic field. */
    @Attribute
    @Singular
    SortedSet<String> semanticSampleValues;

    /** Alternative names or terms for the semantic field. */
    @Attribute
    @Singular
    SortedSet<String> semanticSynonyms;

    /** Detailed type of the semantic field (e.g., type of measure, type of dimension, or type of entity). */
    @Attribute
    String semanticType;

    /** Logical table containing the dimension. */
    @Attribute
    ISnowflakeSemanticLogicalTable snowflakeSemanticLogicalTable;

    /** Simple name of the logical table in which this dimension exists. */
    @Attribute
    String snowflakeSemanticTableName;

    /** Unique name of the logical table in which this dimension exists. */
    @Attribute
    String snowflakeSemanticTableQualifiedName;

    /** Simple name of the semantic view in which this dimension exists. */
    @Attribute
    String snowflakeSemanticViewName;

    /** Unique name of the semantic view in which this dimension exists. */
    @Attribute
    String snowflakeSemanticViewQualifiedName;

    /** Unique name of the context in which the model versions exist, or empty if it does not exist within an AI model context. */
    @Attribute
    String sqlAIModelContextQualifiedName;

    /** Sources related to this asset. */
    @Attribute
    @Singular
    SortedSet<IDbtSource> sqlDBTSources;

    /** Assets related to the model. */
    @Attribute
    @Singular
    SortedSet<IDbtModel> sqlDbtModels;

    /** Whether this asset is secure (true) or not (false). */
    @Attribute
    Boolean sqlIsSecure;

    /** Simple name of the table in which this SQL asset exists, or empty if it does not exist within a table. */
    @Attribute
    String tableName;

    /** Unique name of the table in which this SQL asset exists, or empty if it does not exist within a table. */
    @Attribute
    String tableQualifiedName;

    /** Simple name of the view in which this SQL asset exists, or empty if it does not exist within a view. */
    @Attribute
    String viewName;

    /** Unique name of the view in which this SQL asset exists, or empty if it does not exist within a view. */
    @Attribute
    String viewQualifiedName;

    /**
     * Builds the minimal object necessary to create a relationship to a SnowflakeSemanticDimension, from a potentially
     * more-complete SnowflakeSemanticDimension object.
     *
     * @return the minimal object necessary to relate to the SnowflakeSemanticDimension
     * @throws InvalidRequestException if any of the minimal set of required properties for a SnowflakeSemanticDimension relationship are not found in the initial object
     */
    @Override
    public SnowflakeSemanticDimension trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SnowflakeSemanticDimension assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SnowflakeSemanticDimension assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SnowflakeSemanticDimension assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SnowflakeSemanticDimension assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SnowflakeSemanticDimensions will be included
     * @return a fluent search that includes all SnowflakeSemanticDimension assets
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
     * Reference to a SnowflakeSemanticDimension by GUID. Use this to create a relationship to this SnowflakeSemanticDimension,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SnowflakeSemanticDimension to reference
     * @return reference to a SnowflakeSemanticDimension that can be used for defining a relationship to a SnowflakeSemanticDimension
     */
    public static SnowflakeSemanticDimension refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SnowflakeSemanticDimension by GUID. Use this to create a relationship to this SnowflakeSemanticDimension,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SnowflakeSemanticDimension to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SnowflakeSemanticDimension that can be used for defining a relationship to a SnowflakeSemanticDimension
     */
    public static SnowflakeSemanticDimension refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SnowflakeSemanticDimension._internal()
                .guid(guid)
                .semantic(semantic)
                .build();
    }

    /**
     * Reference to a SnowflakeSemanticDimension by qualifiedName. Use this to create a relationship to this SnowflakeSemanticDimension,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SnowflakeSemanticDimension to reference
     * @return reference to a SnowflakeSemanticDimension that can be used for defining a relationship to a SnowflakeSemanticDimension
     */
    public static SnowflakeSemanticDimension refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SnowflakeSemanticDimension by qualifiedName. Use this to create a relationship to this SnowflakeSemanticDimension,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SnowflakeSemanticDimension to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SnowflakeSemanticDimension that can be used for defining a relationship to a SnowflakeSemanticDimension
     */
    public static SnowflakeSemanticDimension refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SnowflakeSemanticDimension._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SnowflakeSemanticDimension by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SnowflakeSemanticDimension to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SnowflakeSemanticDimension, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakeSemanticDimension does not exist or the provided GUID is not a SnowflakeSemanticDimension
     */
    @JsonIgnore
    public static SnowflakeSemanticDimension get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SnowflakeSemanticDimension by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SnowflakeSemanticDimension to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SnowflakeSemanticDimension, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakeSemanticDimension does not exist or the provided GUID is not a SnowflakeSemanticDimension
     */
    @JsonIgnore
    public static SnowflakeSemanticDimension get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SnowflakeSemanticDimension) {
                return (SnowflakeSemanticDimension) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof SnowflakeSemanticDimension) {
                return (SnowflakeSemanticDimension) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SnowflakeSemanticDimension by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SnowflakeSemanticDimension to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SnowflakeSemanticDimension, including any relationships
     * @return the requested SnowflakeSemanticDimension, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakeSemanticDimension does not exist or the provided GUID is not a SnowflakeSemanticDimension
     */
    @JsonIgnore
    public static SnowflakeSemanticDimension get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a SnowflakeSemanticDimension by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SnowflakeSemanticDimension to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SnowflakeSemanticDimension, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the SnowflakeSemanticDimension
     * @return the requested SnowflakeSemanticDimension, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakeSemanticDimension does not exist or the provided GUID is not a SnowflakeSemanticDimension
     */
    @JsonIgnore
    public static SnowflakeSemanticDimension get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = SnowflakeSemanticDimension.select(client)
                    .where(SnowflakeSemanticDimension.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof SnowflakeSemanticDimension) {
                return (SnowflakeSemanticDimension) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = SnowflakeSemanticDimension.select(client)
                    .where(SnowflakeSemanticDimension.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof SnowflakeSemanticDimension) {
                return (SnowflakeSemanticDimension) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SnowflakeSemanticDimension to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SnowflakeSemanticDimension
     * @return true if the SnowflakeSemanticDimension is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SnowflakeSemanticDimension.
     *
     * @param qualifiedName of the SnowflakeSemanticDimension
     * @param name of the SnowflakeSemanticDimension
     * @return the minimal request necessary to update the SnowflakeSemanticDimension, as a builder
     */
    public static SnowflakeSemanticDimensionBuilder<?, ?> updater(String qualifiedName, String name) {
        return SnowflakeSemanticDimension._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SnowflakeSemanticDimension,
     * from a potentially more-complete SnowflakeSemanticDimension object.
     *
     * @return the minimal object necessary to update the SnowflakeSemanticDimension, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a SnowflakeSemanticDimension are not present in the initial object
     */
    @Override
    public SnowflakeSemanticDimensionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class SnowflakeSemanticDimensionBuilder<
                    C extends SnowflakeSemanticDimension, B extends SnowflakeSemanticDimensionBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a SnowflakeSemanticDimension.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SnowflakeSemanticDimension
     * @param name of the SnowflakeSemanticDimension
     * @return the updated SnowflakeSemanticDimension, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeSemanticDimension removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SnowflakeSemanticDimension) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SnowflakeSemanticDimension.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SnowflakeSemanticDimension
     * @param name of the SnowflakeSemanticDimension
     * @return the updated SnowflakeSemanticDimension, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeSemanticDimension removeUserDescription(
            AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (SnowflakeSemanticDimension) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SnowflakeSemanticDimension.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SnowflakeSemanticDimension's owners
     * @param qualifiedName of the SnowflakeSemanticDimension
     * @param name of the SnowflakeSemanticDimension
     * @return the updated SnowflakeSemanticDimension, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeSemanticDimension removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SnowflakeSemanticDimension) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SnowflakeSemanticDimension.
     *
     * @param client connectivity to the Atlan tenant on which to update the SnowflakeSemanticDimension's certificate
     * @param qualifiedName of the SnowflakeSemanticDimension
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SnowflakeSemanticDimension, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeSemanticDimension updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SnowflakeSemanticDimension)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SnowflakeSemanticDimension.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SnowflakeSemanticDimension's certificate
     * @param qualifiedName of the SnowflakeSemanticDimension
     * @param name of the SnowflakeSemanticDimension
     * @return the updated SnowflakeSemanticDimension, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeSemanticDimension removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SnowflakeSemanticDimension) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SnowflakeSemanticDimension.
     *
     * @param client connectivity to the Atlan tenant on which to update the SnowflakeSemanticDimension's announcement
     * @param qualifiedName of the SnowflakeSemanticDimension
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeSemanticDimension updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SnowflakeSemanticDimension)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SnowflakeSemanticDimension.
     *
     * @param client connectivity to the Atlan client from which to remove the SnowflakeSemanticDimension's announcement
     * @param qualifiedName of the SnowflakeSemanticDimension
     * @param name of the SnowflakeSemanticDimension
     * @return the updated SnowflakeSemanticDimension, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeSemanticDimension removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SnowflakeSemanticDimension) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SnowflakeSemanticDimension.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SnowflakeSemanticDimension's assigned terms
     * @param qualifiedName for the SnowflakeSemanticDimension
     * @param name human-readable name of the SnowflakeSemanticDimension
     * @param terms the list of terms to replace on the SnowflakeSemanticDimension, or null to remove all terms from the SnowflakeSemanticDimension
     * @return the SnowflakeSemanticDimension that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SnowflakeSemanticDimension replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SnowflakeSemanticDimension) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SnowflakeSemanticDimension, without replacing existing terms linked to the SnowflakeSemanticDimension.
     * Note: this operation must make two API calls — one to retrieve the SnowflakeSemanticDimension's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SnowflakeSemanticDimension
     * @param qualifiedName for the SnowflakeSemanticDimension
     * @param terms the list of terms to append to the SnowflakeSemanticDimension
     * @return the SnowflakeSemanticDimension that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SnowflakeSemanticDimension appendTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (SnowflakeSemanticDimension) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SnowflakeSemanticDimension, without replacing all existing terms linked to the SnowflakeSemanticDimension.
     * Note: this operation must make two API calls — one to retrieve the SnowflakeSemanticDimension's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SnowflakeSemanticDimension
     * @param qualifiedName for the SnowflakeSemanticDimension
     * @param terms the list of terms to remove from the SnowflakeSemanticDimension, which must be referenced by GUID
     * @return the SnowflakeSemanticDimension that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SnowflakeSemanticDimension removeTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (SnowflakeSemanticDimension) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SnowflakeSemanticDimension, without replacing existing Atlan tags linked to the SnowflakeSemanticDimension.
     * Note: this operation must make two API calls — one to retrieve the SnowflakeSemanticDimension's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SnowflakeSemanticDimension
     * @param qualifiedName of the SnowflakeSemanticDimension
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SnowflakeSemanticDimension
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static SnowflakeSemanticDimension appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (SnowflakeSemanticDimension) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SnowflakeSemanticDimension, without replacing existing Atlan tags linked to the SnowflakeSemanticDimension.
     * Note: this operation must make two API calls — one to retrieve the SnowflakeSemanticDimension's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SnowflakeSemanticDimension
     * @param qualifiedName of the SnowflakeSemanticDimension
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SnowflakeSemanticDimension
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static SnowflakeSemanticDimension appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SnowflakeSemanticDimension) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SnowflakeSemanticDimension.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SnowflakeSemanticDimension
     * @param qualifiedName of the SnowflakeSemanticDimension
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SnowflakeSemanticDimension
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
