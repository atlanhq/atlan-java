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
import com.fasterxml.jackson.annotation.JsonProperty;
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
 * Instance of a Snowflake stream in Atlan. These are used to capture changes in a table.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class SnowflakeStream extends Asset implements ISnowflakeStream, ISQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SnowflakeStream";

    /** Fixed typeName for SnowflakeStreams. */
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

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtModel> dbtModels;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtSource> dbtSources;

    /** TBC */
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

    /** Schema in which this Snowflake stream exists. */
    @Attribute
    @JsonProperty("atlanSchema")
    ISchema schema;

    /** Simple name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaName;

    /** Unique name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaQualifiedName;

    /** Whether this stream is stale (true) or not (false). */
    @Attribute
    Boolean snowflakeStreamIsStale;

    /** Mode of this stream. */
    @Attribute
    String snowflakeStreamMode;

    /** Type of the source of this stream. */
    @Attribute
    String snowflakeStreamSourceType;

    /** Time (epoch) after which this stream will be stale, in milliseconds. */
    @Attribute
    @Date
    Long snowflakeStreamStaleAfter;

    /** Type of this stream, for example: standard, append-only, insert-only, etc. */
    @Attribute
    String snowflakeStreamType;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtSource> sqlDBTSources;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtModel> sqlDbtModels;

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
     * Builds the minimal object necessary to create a relationship to a SnowflakeStream, from a potentially
     * more-complete SnowflakeStream object.
     *
     * @return the minimal object necessary to relate to the SnowflakeStream
     * @throws InvalidRequestException if any of the minimal set of required properties for a SnowflakeStream relationship are not found in the initial object
     */
    @Override
    public SnowflakeStream trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SnowflakeStream assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SnowflakeStream assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SnowflakeStream assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SnowflakeStream assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SnowflakeStreams will be included
     * @return a fluent search that includes all SnowflakeStream assets
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
     * Reference to a SnowflakeStream by GUID. Use this to create a relationship to this SnowflakeStream,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SnowflakeStream to reference
     * @return reference to a SnowflakeStream that can be used for defining a relationship to a SnowflakeStream
     */
    public static SnowflakeStream refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SnowflakeStream by GUID. Use this to create a relationship to this SnowflakeStream,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SnowflakeStream to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SnowflakeStream that can be used for defining a relationship to a SnowflakeStream
     */
    public static SnowflakeStream refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SnowflakeStream._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SnowflakeStream by qualifiedName. Use this to create a relationship to this SnowflakeStream,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SnowflakeStream to reference
     * @return reference to a SnowflakeStream that can be used for defining a relationship to a SnowflakeStream
     */
    public static SnowflakeStream refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SnowflakeStream by qualifiedName. Use this to create a relationship to this SnowflakeStream,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SnowflakeStream to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SnowflakeStream that can be used for defining a relationship to a SnowflakeStream
     */
    public static SnowflakeStream refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SnowflakeStream._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SnowflakeStream by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SnowflakeStream to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SnowflakeStream, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakeStream does not exist or the provided GUID is not a SnowflakeStream
     */
    @JsonIgnore
    public static SnowflakeStream get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SnowflakeStream by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SnowflakeStream to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SnowflakeStream, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakeStream does not exist or the provided GUID is not a SnowflakeStream
     */
    @JsonIgnore
    public static SnowflakeStream get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SnowflakeStream) {
                return (SnowflakeStream) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof SnowflakeStream) {
                return (SnowflakeStream) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SnowflakeStream by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SnowflakeStream to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SnowflakeStream, including any relationships
     * @return the requested SnowflakeStream, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakeStream does not exist or the provided GUID is not a SnowflakeStream
     */
    @JsonIgnore
    public static SnowflakeStream get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a SnowflakeStream by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SnowflakeStream to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SnowflakeStream, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the SnowflakeStream
     * @return the requested SnowflakeStream, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakeStream does not exist or the provided GUID is not a SnowflakeStream
     */
    @JsonIgnore
    public static SnowflakeStream get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = SnowflakeStream.select(client)
                    .where(SnowflakeStream.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof SnowflakeStream) {
                return (SnowflakeStream) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = SnowflakeStream.select(client)
                    .where(SnowflakeStream.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof SnowflakeStream) {
                return (SnowflakeStream) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SnowflakeStream to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SnowflakeStream
     * @return true if the SnowflakeStream is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SnowflakeStream.
     *
     * @param qualifiedName of the SnowflakeStream
     * @param name of the SnowflakeStream
     * @return the minimal request necessary to update the SnowflakeStream, as a builder
     */
    public static SnowflakeStreamBuilder<?, ?> updater(String qualifiedName, String name) {
        return SnowflakeStream._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SnowflakeStream, from a potentially
     * more-complete SnowflakeStream object.
     *
     * @return the minimal object necessary to update the SnowflakeStream, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SnowflakeStream are not found in the initial object
     */
    @Override
    public SnowflakeStreamBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a SnowflakeStream.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SnowflakeStream
     * @param name of the SnowflakeStream
     * @return the updated SnowflakeStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeStream removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SnowflakeStream) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SnowflakeStream.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SnowflakeStream
     * @param name of the SnowflakeStream
     * @return the updated SnowflakeStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeStream removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SnowflakeStream) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SnowflakeStream.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SnowflakeStream's owners
     * @param qualifiedName of the SnowflakeStream
     * @param name of the SnowflakeStream
     * @return the updated SnowflakeStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeStream removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SnowflakeStream) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SnowflakeStream.
     *
     * @param client connectivity to the Atlan tenant on which to update the SnowflakeStream's certificate
     * @param qualifiedName of the SnowflakeStream
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SnowflakeStream, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeStream updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SnowflakeStream)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SnowflakeStream.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SnowflakeStream's certificate
     * @param qualifiedName of the SnowflakeStream
     * @param name of the SnowflakeStream
     * @return the updated SnowflakeStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeStream removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SnowflakeStream) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SnowflakeStream.
     *
     * @param client connectivity to the Atlan tenant on which to update the SnowflakeStream's announcement
     * @param qualifiedName of the SnowflakeStream
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeStream updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SnowflakeStream)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SnowflakeStream.
     *
     * @param client connectivity to the Atlan client from which to remove the SnowflakeStream's announcement
     * @param qualifiedName of the SnowflakeStream
     * @param name of the SnowflakeStream
     * @return the updated SnowflakeStream, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakeStream removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SnowflakeStream) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SnowflakeStream.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SnowflakeStream's assigned terms
     * @param qualifiedName for the SnowflakeStream
     * @param name human-readable name of the SnowflakeStream
     * @param terms the list of terms to replace on the SnowflakeStream, or null to remove all terms from the SnowflakeStream
     * @return the SnowflakeStream that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SnowflakeStream replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SnowflakeStream) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SnowflakeStream, without replacing existing terms linked to the SnowflakeStream.
     * Note: this operation must make two API calls — one to retrieve the SnowflakeStream's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SnowflakeStream
     * @param qualifiedName for the SnowflakeStream
     * @param terms the list of terms to append to the SnowflakeStream
     * @return the SnowflakeStream that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SnowflakeStream appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SnowflakeStream) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SnowflakeStream, without replacing all existing terms linked to the SnowflakeStream.
     * Note: this operation must make two API calls — one to retrieve the SnowflakeStream's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SnowflakeStream
     * @param qualifiedName for the SnowflakeStream
     * @param terms the list of terms to remove from the SnowflakeStream, which must be referenced by GUID
     * @return the SnowflakeStream that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SnowflakeStream removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SnowflakeStream) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SnowflakeStream, without replacing existing Atlan tags linked to the SnowflakeStream.
     * Note: this operation must make two API calls — one to retrieve the SnowflakeStream's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SnowflakeStream
     * @param qualifiedName of the SnowflakeStream
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SnowflakeStream
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static SnowflakeStream appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SnowflakeStream) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SnowflakeStream, without replacing existing Atlan tags linked to the SnowflakeStream.
     * Note: this operation must make two API calls — one to retrieve the SnowflakeStream's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SnowflakeStream
     * @param qualifiedName of the SnowflakeStream
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SnowflakeStream
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static SnowflakeStream appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SnowflakeStream) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SnowflakeStream.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SnowflakeStream
     * @param qualifiedName of the SnowflakeStream
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SnowflakeStream
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
