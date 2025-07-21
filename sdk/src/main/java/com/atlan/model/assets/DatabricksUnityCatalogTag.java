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
import com.atlan.model.structs.SourceTagAttribute;
import com.atlan.serde.Serde;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
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
 * Instance of a Databricks' Unity catalog tag in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class DatabricksUnityCatalogTag extends Asset
        implements IDatabricksUnityCatalogTag, ITag, ISQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DatabricksUnityCatalogTag";

    /** Fixed typeName for DatabricksUnityCatalogTags. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Simple name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewName;

    /** Unique name of the calculation view in which this SQL asset exists, or empty if it does not exist within a calculation view. */
    @Attribute
    String calculationViewQualifiedName;

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

    /** Name of the classification in Atlan that is mapped to this tag. */
    @Attribute
    @JsonProperty("mappedClassificationName")
    String mappedAtlanTagName;

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

    /** Simple name of the table in which this SQL asset exists, or empty if it does not exist within a table. */
    @Attribute
    String tableName;

    /** Unique name of the table in which this SQL asset exists, or empty if it does not exist within a table. */
    @Attribute
    String tableQualifiedName;

    /** Allowed values for the tag in the source system. These are denormalized from tagAttributes for ease of querying. */
    @Attribute
    @Singular
    SortedSet<String> tagAllowedValues;

    /** Attributes associated with the tag in the source system. */
    @Attribute
    @Singular
    List<SourceTagAttribute> tagAttributes;

    /** Unique identifier of the tag in the source system. */
    @Attribute
    String tagId;

    /** Simple name of the view in which this SQL asset exists, or empty if it does not exist within a view. */
    @Attribute
    String viewName;

    /** Unique name of the view in which this SQL asset exists, or empty if it does not exist within a view. */
    @Attribute
    String viewQualifiedName;

    /**
     * Builds the minimal object necessary to create a relationship to a DatabricksUnityCatalogTag, from a potentially
     * more-complete DatabricksUnityCatalogTag object.
     *
     * @return the minimal object necessary to relate to the DatabricksUnityCatalogTag
     * @throws InvalidRequestException if any of the minimal set of required properties for a DatabricksUnityCatalogTag relationship are not found in the initial object
     */
    @Override
    public DatabricksUnityCatalogTag trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all DatabricksUnityCatalogTag assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DatabricksUnityCatalogTag assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all DatabricksUnityCatalogTag assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all DatabricksUnityCatalogTag assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DatabricksUnityCatalogTags will be included
     * @return a fluent search that includes all DatabricksUnityCatalogTag assets
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
     * Reference to a DatabricksUnityCatalogTag by GUID. Use this to create a relationship to this DatabricksUnityCatalogTag,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the DatabricksUnityCatalogTag to reference
     * @return reference to a DatabricksUnityCatalogTag that can be used for defining a relationship to a DatabricksUnityCatalogTag
     */
    public static DatabricksUnityCatalogTag refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DatabricksUnityCatalogTag by GUID. Use this to create a relationship to this DatabricksUnityCatalogTag,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the DatabricksUnityCatalogTag to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DatabricksUnityCatalogTag that can be used for defining a relationship to a DatabricksUnityCatalogTag
     */
    public static DatabricksUnityCatalogTag refByGuid(String guid, Reference.SaveSemantic semantic) {
        return DatabricksUnityCatalogTag._internal()
                .guid(guid)
                .semantic(semantic)
                .build();
    }

    /**
     * Reference to a DatabricksUnityCatalogTag by qualifiedName. Use this to create a relationship to this DatabricksUnityCatalogTag,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the DatabricksUnityCatalogTag to reference
     * @return reference to a DatabricksUnityCatalogTag that can be used for defining a relationship to a DatabricksUnityCatalogTag
     */
    public static DatabricksUnityCatalogTag refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DatabricksUnityCatalogTag by qualifiedName. Use this to create a relationship to this DatabricksUnityCatalogTag,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the DatabricksUnityCatalogTag to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DatabricksUnityCatalogTag that can be used for defining a relationship to a DatabricksUnityCatalogTag
     */
    public static DatabricksUnityCatalogTag refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return DatabricksUnityCatalogTag._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a DatabricksUnityCatalogTag by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DatabricksUnityCatalogTag to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DatabricksUnityCatalogTag, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DatabricksUnityCatalogTag does not exist or the provided GUID is not a DatabricksUnityCatalogTag
     */
    @JsonIgnore
    public static DatabricksUnityCatalogTag get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a DatabricksUnityCatalogTag by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DatabricksUnityCatalogTag to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full DatabricksUnityCatalogTag, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DatabricksUnityCatalogTag does not exist or the provided GUID is not a DatabricksUnityCatalogTag
     */
    @JsonIgnore
    public static DatabricksUnityCatalogTag get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof DatabricksUnityCatalogTag) {
                return (DatabricksUnityCatalogTag) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof DatabricksUnityCatalogTag) {
                return (DatabricksUnityCatalogTag) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a DatabricksUnityCatalogTag by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DatabricksUnityCatalogTag to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DatabricksUnityCatalogTag, including any relationships
     * @return the requested DatabricksUnityCatalogTag, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DatabricksUnityCatalogTag does not exist or the provided GUID is not a DatabricksUnityCatalogTag
     */
    @JsonIgnore
    public static DatabricksUnityCatalogTag get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a DatabricksUnityCatalogTag by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DatabricksUnityCatalogTag to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DatabricksUnityCatalogTag, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the DatabricksUnityCatalogTag
     * @return the requested DatabricksUnityCatalogTag, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DatabricksUnityCatalogTag does not exist or the provided GUID is not a DatabricksUnityCatalogTag
     */
    @JsonIgnore
    public static DatabricksUnityCatalogTag get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = DatabricksUnityCatalogTag.select(client)
                    .where(DatabricksUnityCatalogTag.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof DatabricksUnityCatalogTag) {
                return (DatabricksUnityCatalogTag) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = DatabricksUnityCatalogTag.select(client)
                    .where(DatabricksUnityCatalogTag.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof DatabricksUnityCatalogTag) {
                return (DatabricksUnityCatalogTag) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) DatabricksUnityCatalogTag to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DatabricksUnityCatalogTag
     * @return true if the DatabricksUnityCatalogTag is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a DatabricksUnityCatalogTag.
     *
     * @param name of the DatabricksUnityCatalogTag
     * @param connectionQualifiedName unique name of the connection in which to create the DatabricksUnityCatalogTag
     * @param mappedAtlanTagName the human-readable name of the Atlan tag to which this DatabricksUnityCatalogTag should map
     * @param sourceId unique identifier for the tag in the source
     * @param allowedValues the values allowed to be set for this tag in the source
     * @return the minimal request necessary to create the DatabricksUnityCatalogTag, as a builder
     */
    public static DatabricksUnityCatalogTagBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String mappedAtlanTagName,
            String sourceId,
            List<String> allowedValues) {
        String allowedValuesString = "";
        try {
            allowedValuesString = Serde.allInclusiveMapper.writeValueAsString(allowedValues);
        } catch (JsonProcessingException e) {
            log.error("Unable to transform list of allowed values into singular string.", e);
        }
        return DatabricksUnityCatalogTag._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName))
                .connectionQualifiedName(connectionQualifiedName)
                .mappedAtlanTagName(mappedAtlanTagName)
                .tagId(sourceId)
                .tagAttribute(SourceTagAttribute.builder()
                        .tagAttributeKey("allowedValues")
                        .tagAttributeValue(allowedValuesString)
                        .build())
                .tagAllowedValues(allowedValues);
    }

    /**
     * Builds the minimal object necessary to update a DatabricksUnityCatalogTag.
     *
     * @param qualifiedName of the DatabricksUnityCatalogTag
     * @param name of the DatabricksUnityCatalogTag
     * @return the minimal request necessary to update the DatabricksUnityCatalogTag, as a builder
     */
    public static DatabricksUnityCatalogTagBuilder<?, ?> updater(String qualifiedName, String name) {
        return DatabricksUnityCatalogTag._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique DatabricksUnityCatalogTag name.
     *
     * @param name of the DatabricksUnityCatalogTag
     * @param connectionQualifiedName unique name of the schema in which this DatabricksUnityCatalogTag exists
     * @return a unique name for the DatabricksUnityCatalogTag
     */
    public static String generateQualifiedName(String name, String connectionQualifiedName) {
        return connectionQualifiedName + "/tag/" + name;
    }

    /**
     * Builds the minimal object necessary to apply an update to a DatabricksUnityCatalogTag, from a potentially
     * more-complete DatabricksUnityCatalogTag object.
     *
     * @return the minimal object necessary to update the DatabricksUnityCatalogTag, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DatabricksUnityCatalogTag are not found in the initial object
     */
    @Override
    public DatabricksUnityCatalogTagBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class DatabricksUnityCatalogTagBuilder<
                    C extends DatabricksUnityCatalogTag, B extends DatabricksUnityCatalogTagBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a DatabricksUnityCatalogTag.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DatabricksUnityCatalogTag
     * @param name of the DatabricksUnityCatalogTag
     * @return the updated DatabricksUnityCatalogTag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksUnityCatalogTag removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DatabricksUnityCatalogTag) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DatabricksUnityCatalogTag.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DatabricksUnityCatalogTag
     * @param name of the DatabricksUnityCatalogTag
     * @return the updated DatabricksUnityCatalogTag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksUnityCatalogTag removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DatabricksUnityCatalogTag) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DatabricksUnityCatalogTag.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DatabricksUnityCatalogTag's owners
     * @param qualifiedName of the DatabricksUnityCatalogTag
     * @param name of the DatabricksUnityCatalogTag
     * @return the updated DatabricksUnityCatalogTag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksUnityCatalogTag removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DatabricksUnityCatalogTag) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DatabricksUnityCatalogTag.
     *
     * @param client connectivity to the Atlan tenant on which to update the DatabricksUnityCatalogTag's certificate
     * @param qualifiedName of the DatabricksUnityCatalogTag
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DatabricksUnityCatalogTag, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksUnityCatalogTag updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DatabricksUnityCatalogTag)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DatabricksUnityCatalogTag.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DatabricksUnityCatalogTag's certificate
     * @param qualifiedName of the DatabricksUnityCatalogTag
     * @param name of the DatabricksUnityCatalogTag
     * @return the updated DatabricksUnityCatalogTag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksUnityCatalogTag removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DatabricksUnityCatalogTag) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DatabricksUnityCatalogTag.
     *
     * @param client connectivity to the Atlan tenant on which to update the DatabricksUnityCatalogTag's announcement
     * @param qualifiedName of the DatabricksUnityCatalogTag
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksUnityCatalogTag updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (DatabricksUnityCatalogTag)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DatabricksUnityCatalogTag.
     *
     * @param client connectivity to the Atlan client from which to remove the DatabricksUnityCatalogTag's announcement
     * @param qualifiedName of the DatabricksUnityCatalogTag
     * @param name of the DatabricksUnityCatalogTag
     * @return the updated DatabricksUnityCatalogTag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DatabricksUnityCatalogTag removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DatabricksUnityCatalogTag) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DatabricksUnityCatalogTag.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DatabricksUnityCatalogTag's assigned terms
     * @param qualifiedName for the DatabricksUnityCatalogTag
     * @param name human-readable name of the DatabricksUnityCatalogTag
     * @param terms the list of terms to replace on the DatabricksUnityCatalogTag, or null to remove all terms from the DatabricksUnityCatalogTag
     * @return the DatabricksUnityCatalogTag that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DatabricksUnityCatalogTag replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (DatabricksUnityCatalogTag) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DatabricksUnityCatalogTag, without replacing existing terms linked to the DatabricksUnityCatalogTag.
     * Note: this operation must make two API calls — one to retrieve the DatabricksUnityCatalogTag's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DatabricksUnityCatalogTag
     * @param qualifiedName for the DatabricksUnityCatalogTag
     * @param terms the list of terms to append to the DatabricksUnityCatalogTag
     * @return the DatabricksUnityCatalogTag that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DatabricksUnityCatalogTag appendTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (DatabricksUnityCatalogTag) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DatabricksUnityCatalogTag, without replacing all existing terms linked to the DatabricksUnityCatalogTag.
     * Note: this operation must make two API calls — one to retrieve the DatabricksUnityCatalogTag's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DatabricksUnityCatalogTag
     * @param qualifiedName for the DatabricksUnityCatalogTag
     * @param terms the list of terms to remove from the DatabricksUnityCatalogTag, which must be referenced by GUID
     * @return the DatabricksUnityCatalogTag that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DatabricksUnityCatalogTag removeTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (DatabricksUnityCatalogTag) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DatabricksUnityCatalogTag, without replacing existing Atlan tags linked to the DatabricksUnityCatalogTag.
     * Note: this operation must make two API calls — one to retrieve the DatabricksUnityCatalogTag's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DatabricksUnityCatalogTag
     * @param qualifiedName of the DatabricksUnityCatalogTag
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DatabricksUnityCatalogTag
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static DatabricksUnityCatalogTag appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (DatabricksUnityCatalogTag) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DatabricksUnityCatalogTag, without replacing existing Atlan tags linked to the DatabricksUnityCatalogTag.
     * Note: this operation must make two API calls — one to retrieve the DatabricksUnityCatalogTag's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DatabricksUnityCatalogTag
     * @param qualifiedName of the DatabricksUnityCatalogTag
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DatabricksUnityCatalogTag
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static DatabricksUnityCatalogTag appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DatabricksUnityCatalogTag) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a DatabricksUnityCatalogTag.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DatabricksUnityCatalogTag
     * @param qualifiedName of the DatabricksUnityCatalogTag
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DatabricksUnityCatalogTag
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
