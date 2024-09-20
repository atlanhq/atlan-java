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
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Snowflake pipe in Atlan. These are used to ingest data from external sources into Snowflake.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class SnowflakePipe extends Asset implements ISnowflakePipe, ISQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SnowflakePipe";

    /** Fixed typeName for SnowflakePipes. */
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

    /** SQL definition of this pipe. */
    @Attribute
    String definition;

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

    /** Schema in which this Snowflake pipe exists. */
    @Attribute
    @JsonProperty("atlanSchema")
    ISchema schema;

    /** Simple name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaName;

    /** Unique name of the schema in which this SQL asset exists, or empty if it does not exist within a schema. */
    @Attribute
    String schemaQualifiedName;

    /** Whether auto-ingest is enabled for this pipe (true) or not (false). */
    @Attribute
    Boolean snowflakePipeIsAutoIngestEnabled;

    /** Name of the notification channel for this pipe. */
    @Attribute
    String snowflakePipeNotificationChannelName;

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
     * Builds the minimal object necessary to create a relationship to a SnowflakePipe, from a potentially
     * more-complete SnowflakePipe object.
     *
     * @return the minimal object necessary to relate to the SnowflakePipe
     * @throws InvalidRequestException if any of the minimal set of required properties for a SnowflakePipe relationship are not found in the initial object
     */
    @Override
    public SnowflakePipe trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SnowflakePipe assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SnowflakePipe assets will be included.
     *
     * @return a fluent search that includes all SnowflakePipe assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all SnowflakePipe assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SnowflakePipe assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SnowflakePipe assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SnowflakePipe assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) SnowflakePipes will be included
     * @return a fluent search that includes all SnowflakePipe assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all SnowflakePipe assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SnowflakePipes will be included
     * @return a fluent search that includes all SnowflakePipe assets
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
     * Reference to a SnowflakePipe by GUID. Use this to create a relationship to this SnowflakePipe,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SnowflakePipe to reference
     * @return reference to a SnowflakePipe that can be used for defining a relationship to a SnowflakePipe
     */
    public static SnowflakePipe refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SnowflakePipe by GUID. Use this to create a relationship to this SnowflakePipe,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SnowflakePipe to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SnowflakePipe that can be used for defining a relationship to a SnowflakePipe
     */
    public static SnowflakePipe refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SnowflakePipe._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SnowflakePipe by qualifiedName. Use this to create a relationship to this SnowflakePipe,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SnowflakePipe to reference
     * @return reference to a SnowflakePipe that can be used for defining a relationship to a SnowflakePipe
     */
    public static SnowflakePipe refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SnowflakePipe by qualifiedName. Use this to create a relationship to this SnowflakePipe,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SnowflakePipe to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SnowflakePipe that can be used for defining a relationship to a SnowflakePipe
     */
    public static SnowflakePipe refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SnowflakePipe._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SnowflakePipe by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the SnowflakePipe to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SnowflakePipe, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakePipe does not exist or the provided GUID is not a SnowflakePipe
     */
    @JsonIgnore
    public static SnowflakePipe get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a SnowflakePipe by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SnowflakePipe to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SnowflakePipe, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakePipe does not exist or the provided GUID is not a SnowflakePipe
     */
    @JsonIgnore
    public static SnowflakePipe get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a SnowflakePipe by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SnowflakePipe to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SnowflakePipe, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SnowflakePipe does not exist or the provided GUID is not a SnowflakePipe
     */
    @JsonIgnore
    public static SnowflakePipe get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SnowflakePipe) {
                return (SnowflakePipe) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof SnowflakePipe) {
                return (SnowflakePipe) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SnowflakePipe to active.
     *
     * @param qualifiedName for the SnowflakePipe
     * @return true if the SnowflakePipe is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) SnowflakePipe to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SnowflakePipe
     * @return true if the SnowflakePipe is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SnowflakePipe.
     *
     * @param qualifiedName of the SnowflakePipe
     * @param name of the SnowflakePipe
     * @return the minimal request necessary to update the SnowflakePipe, as a builder
     */
    public static SnowflakePipeBuilder<?, ?> updater(String qualifiedName, String name) {
        return SnowflakePipe._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SnowflakePipe, from a potentially
     * more-complete SnowflakePipe object.
     *
     * @return the minimal object necessary to update the SnowflakePipe, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SnowflakePipe are not found in the initial object
     */
    @Override
    public SnowflakePipeBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(
                TYPE_NAME,
                Map.of(
                        "qualifiedName", this.getQualifiedName(),
                        "name", this.getName()));
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a SnowflakePipe.
     *
     * @param qualifiedName of the SnowflakePipe
     * @param name of the SnowflakePipe
     * @return the updated SnowflakePipe, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a SnowflakePipe.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SnowflakePipe
     * @param name of the SnowflakePipe
     * @return the updated SnowflakePipe, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SnowflakePipe) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SnowflakePipe.
     *
     * @param qualifiedName of the SnowflakePipe
     * @param name of the SnowflakePipe
     * @return the updated SnowflakePipe, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a SnowflakePipe.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SnowflakePipe
     * @param name of the SnowflakePipe
     * @return the updated SnowflakePipe, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SnowflakePipe) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SnowflakePipe.
     *
     * @param qualifiedName of the SnowflakePipe
     * @param name of the SnowflakePipe
     * @return the updated SnowflakePipe, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a SnowflakePipe.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SnowflakePipe's owners
     * @param qualifiedName of the SnowflakePipe
     * @param name of the SnowflakePipe
     * @return the updated SnowflakePipe, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SnowflakePipe) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SnowflakePipe.
     *
     * @param qualifiedName of the SnowflakePipe
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SnowflakePipe, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a SnowflakePipe.
     *
     * @param client connectivity to the Atlan tenant on which to update the SnowflakePipe's certificate
     * @param qualifiedName of the SnowflakePipe
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SnowflakePipe, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SnowflakePipe)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SnowflakePipe.
     *
     * @param qualifiedName of the SnowflakePipe
     * @param name of the SnowflakePipe
     * @return the updated SnowflakePipe, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a SnowflakePipe.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SnowflakePipe's certificate
     * @param qualifiedName of the SnowflakePipe
     * @param name of the SnowflakePipe
     * @return the updated SnowflakePipe, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SnowflakePipe) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SnowflakePipe.
     *
     * @param qualifiedName of the SnowflakePipe
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a SnowflakePipe.
     *
     * @param client connectivity to the Atlan tenant on which to update the SnowflakePipe's announcement
     * @param qualifiedName of the SnowflakePipe
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SnowflakePipe)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SnowflakePipe.
     *
     * @param qualifiedName of the SnowflakePipe
     * @param name of the SnowflakePipe
     * @return the updated SnowflakePipe, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a SnowflakePipe.
     *
     * @param client connectivity to the Atlan client from which to remove the SnowflakePipe's announcement
     * @param qualifiedName of the SnowflakePipe
     * @param name of the SnowflakePipe
     * @return the updated SnowflakePipe, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SnowflakePipe) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SnowflakePipe.
     *
     * @param qualifiedName for the SnowflakePipe
     * @param name human-readable name of the SnowflakePipe
     * @param terms the list of terms to replace on the SnowflakePipe, or null to remove all terms from the SnowflakePipe
     * @return the SnowflakePipe that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the SnowflakePipe.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SnowflakePipe's assigned terms
     * @param qualifiedName for the SnowflakePipe
     * @param name human-readable name of the SnowflakePipe
     * @param terms the list of terms to replace on the SnowflakePipe, or null to remove all terms from the SnowflakePipe
     * @return the SnowflakePipe that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SnowflakePipe) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SnowflakePipe, without replacing existing terms linked to the SnowflakePipe.
     * Note: this operation must make two API calls — one to retrieve the SnowflakePipe's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the SnowflakePipe
     * @param terms the list of terms to append to the SnowflakePipe
     * @return the SnowflakePipe that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the SnowflakePipe, without replacing existing terms linked to the SnowflakePipe.
     * Note: this operation must make two API calls — one to retrieve the SnowflakePipe's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SnowflakePipe
     * @param qualifiedName for the SnowflakePipe
     * @param terms the list of terms to append to the SnowflakePipe
     * @return the SnowflakePipe that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SnowflakePipe) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SnowflakePipe, without replacing all existing terms linked to the SnowflakePipe.
     * Note: this operation must make two API calls — one to retrieve the SnowflakePipe's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the SnowflakePipe
     * @param terms the list of terms to remove from the SnowflakePipe, which must be referenced by GUID
     * @return the SnowflakePipe that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a SnowflakePipe, without replacing all existing terms linked to the SnowflakePipe.
     * Note: this operation must make two API calls — one to retrieve the SnowflakePipe's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SnowflakePipe
     * @param qualifiedName for the SnowflakePipe
     * @param terms the list of terms to remove from the SnowflakePipe, which must be referenced by GUID
     * @return the SnowflakePipe that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SnowflakePipe removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SnowflakePipe) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SnowflakePipe, without replacing existing Atlan tags linked to the SnowflakePipe.
     * Note: this operation must make two API calls — one to retrieve the SnowflakePipe's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SnowflakePipe
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SnowflakePipe
     */
    public static SnowflakePipe appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SnowflakePipe, without replacing existing Atlan tags linked to the SnowflakePipe.
     * Note: this operation must make two API calls — one to retrieve the SnowflakePipe's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SnowflakePipe
     * @param qualifiedName of the SnowflakePipe
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SnowflakePipe
     */
    public static SnowflakePipe appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SnowflakePipe) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SnowflakePipe, without replacing existing Atlan tags linked to the SnowflakePipe.
     * Note: this operation must make two API calls — one to retrieve the SnowflakePipe's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SnowflakePipe
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SnowflakePipe
     */
    public static SnowflakePipe appendAtlanTags(
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
     * Add Atlan tags to a SnowflakePipe, without replacing existing Atlan tags linked to the SnowflakePipe.
     * Note: this operation must make two API calls — one to retrieve the SnowflakePipe's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SnowflakePipe
     * @param qualifiedName of the SnowflakePipe
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SnowflakePipe
     */
    public static SnowflakePipe appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SnowflakePipe) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SnowflakePipe.
     *
     * @param qualifiedName of the SnowflakePipe
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SnowflakePipe
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a SnowflakePipe.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SnowflakePipe
     * @param qualifiedName of the SnowflakePipe
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SnowflakePipe
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
