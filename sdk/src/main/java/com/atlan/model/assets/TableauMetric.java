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
 * Instance of a Tableau metric in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class TableauMetric extends Asset implements ITableauMetric, ITableau, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TableauMetric";

    /** Fixed typeName for TableauMetrics. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

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

    /** Project in which this metric exists. */
    @Attribute
    ITableauProject project;

    /** List of top-level projects with their nested child projects. */
    @Attribute
    @Singular("addProjectHierarchy")
    List<Map<String, String>> projectHierarchy;

    /** Unique name of the project in which this metric exists. */
    @Attribute
    String projectQualifiedName;

    /** Unique name of the site in which this metric exists. */
    @Attribute
    String siteQualifiedName;

    /** Unique name of the top-level project in which this metric exists. */
    @Attribute
    String topLevelProjectQualifiedName;

    /**
     * Builds the minimal object necessary to create a relationship to a TableauMetric, from a potentially
     * more-complete TableauMetric object.
     *
     * @return the minimal object necessary to relate to the TableauMetric
     * @throws InvalidRequestException if any of the minimal set of required properties for a TableauMetric relationship are not found in the initial object
     */
    @Override
    public TableauMetric trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all TableauMetric assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) TableauMetric assets will be included.
     *
     * @return a fluent search that includes all TableauMetric assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all TableauMetric assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) TableauMetric assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all TableauMetric assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all TableauMetric assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) TableauMetrics will be included
     * @return a fluent search that includes all TableauMetric assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all TableauMetric assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) TableauMetrics will be included
     * @return a fluent search that includes all TableauMetric assets
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
     * Reference to a TableauMetric by GUID. Use this to create a relationship to this TableauMetric,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the TableauMetric to reference
     * @return reference to a TableauMetric that can be used for defining a relationship to a TableauMetric
     */
    public static TableauMetric refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a TableauMetric by GUID. Use this to create a relationship to this TableauMetric,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the TableauMetric to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a TableauMetric that can be used for defining a relationship to a TableauMetric
     */
    public static TableauMetric refByGuid(String guid, Reference.SaveSemantic semantic) {
        return TableauMetric._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a TableauMetric by qualifiedName. Use this to create a relationship to this TableauMetric,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the TableauMetric to reference
     * @return reference to a TableauMetric that can be used for defining a relationship to a TableauMetric
     */
    public static TableauMetric refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a TableauMetric by qualifiedName. Use this to create a relationship to this TableauMetric,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the TableauMetric to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a TableauMetric that can be used for defining a relationship to a TableauMetric
     */
    public static TableauMetric refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return TableauMetric._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a TableauMetric by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the TableauMetric to retrieve, either its GUID or its full qualifiedName
     * @return the requested full TableauMetric, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauMetric does not exist or the provided GUID is not a TableauMetric
     */
    @JsonIgnore
    public static TableauMetric get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a TableauMetric by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the TableauMetric to retrieve, either its GUID or its full qualifiedName
     * @return the requested full TableauMetric, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauMetric does not exist or the provided GUID is not a TableauMetric
     */
    @JsonIgnore
    public static TableauMetric get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a TableauMetric by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the TableauMetric to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full TableauMetric, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauMetric does not exist or the provided GUID is not a TableauMetric
     */
    @JsonIgnore
    public static TableauMetric get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof TableauMetric) {
                return (TableauMetric) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof TableauMetric) {
                return (TableauMetric) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) TableauMetric to active.
     *
     * @param qualifiedName for the TableauMetric
     * @return true if the TableauMetric is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) TableauMetric to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the TableauMetric
     * @return true if the TableauMetric is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a TableauMetric.
     *
     * @param qualifiedName of the TableauMetric
     * @param name of the TableauMetric
     * @return the minimal request necessary to update the TableauMetric, as a builder
     */
    public static TableauMetricBuilder<?, ?> updater(String qualifiedName, String name) {
        return TableauMetric._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TableauMetric, from a potentially
     * more-complete TableauMetric object.
     *
     * @return the minimal object necessary to update the TableauMetric, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TableauMetric are not found in the initial object
     */
    @Override
    public TableauMetricBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a TableauMetric.
     *
     * @param qualifiedName of the TableauMetric
     * @param name of the TableauMetric
     * @return the updated TableauMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauMetric removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a TableauMetric.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the TableauMetric
     * @param name of the TableauMetric
     * @return the updated TableauMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauMetric removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauMetric) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a TableauMetric.
     *
     * @param qualifiedName of the TableauMetric
     * @param name of the TableauMetric
     * @return the updated TableauMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauMetric removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a TableauMetric.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the TableauMetric
     * @param name of the TableauMetric
     * @return the updated TableauMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauMetric removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauMetric) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a TableauMetric.
     *
     * @param qualifiedName of the TableauMetric
     * @param name of the TableauMetric
     * @return the updated TableauMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauMetric removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a TableauMetric.
     *
     * @param client connectivity to the Atlan tenant from which to remove the TableauMetric's owners
     * @param qualifiedName of the TableauMetric
     * @param name of the TableauMetric
     * @return the updated TableauMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauMetric removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauMetric) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a TableauMetric.
     *
     * @param qualifiedName of the TableauMetric
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TableauMetric, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauMetric updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a TableauMetric.
     *
     * @param client connectivity to the Atlan tenant on which to update the TableauMetric's certificate
     * @param qualifiedName of the TableauMetric
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TableauMetric, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauMetric updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (TableauMetric)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a TableauMetric.
     *
     * @param qualifiedName of the TableauMetric
     * @param name of the TableauMetric
     * @return the updated TableauMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauMetric removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a TableauMetric.
     *
     * @param client connectivity to the Atlan tenant from which to remove the TableauMetric's certificate
     * @param qualifiedName of the TableauMetric
     * @param name of the TableauMetric
     * @return the updated TableauMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauMetric removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauMetric) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a TableauMetric.
     *
     * @param qualifiedName of the TableauMetric
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauMetric updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a TableauMetric.
     *
     * @param client connectivity to the Atlan tenant on which to update the TableauMetric's announcement
     * @param qualifiedName of the TableauMetric
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauMetric updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (TableauMetric)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a TableauMetric.
     *
     * @param qualifiedName of the TableauMetric
     * @param name of the TableauMetric
     * @return the updated TableauMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauMetric removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a TableauMetric.
     *
     * @param client connectivity to the Atlan client from which to remove the TableauMetric's announcement
     * @param qualifiedName of the TableauMetric
     * @param name of the TableauMetric
     * @return the updated TableauMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauMetric removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauMetric) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the TableauMetric.
     *
     * @param qualifiedName for the TableauMetric
     * @param name human-readable name of the TableauMetric
     * @param terms the list of terms to replace on the TableauMetric, or null to remove all terms from the TableauMetric
     * @return the TableauMetric that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TableauMetric replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the TableauMetric.
     *
     * @param client connectivity to the Atlan tenant on which to replace the TableauMetric's assigned terms
     * @param qualifiedName for the TableauMetric
     * @param name human-readable name of the TableauMetric
     * @param terms the list of terms to replace on the TableauMetric, or null to remove all terms from the TableauMetric
     * @return the TableauMetric that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TableauMetric replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (TableauMetric) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the TableauMetric, without replacing existing terms linked to the TableauMetric.
     * Note: this operation must make two API calls — one to retrieve the TableauMetric's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the TableauMetric
     * @param terms the list of terms to append to the TableauMetric
     * @return the TableauMetric that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static TableauMetric appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the TableauMetric, without replacing existing terms linked to the TableauMetric.
     * Note: this operation must make two API calls — one to retrieve the TableauMetric's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the TableauMetric
     * @param qualifiedName for the TableauMetric
     * @param terms the list of terms to append to the TableauMetric
     * @return the TableauMetric that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static TableauMetric appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (TableauMetric) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a TableauMetric, without replacing all existing terms linked to the TableauMetric.
     * Note: this operation must make two API calls — one to retrieve the TableauMetric's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the TableauMetric
     * @param terms the list of terms to remove from the TableauMetric, which must be referenced by GUID
     * @return the TableauMetric that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static TableauMetric removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a TableauMetric, without replacing all existing terms linked to the TableauMetric.
     * Note: this operation must make two API calls — one to retrieve the TableauMetric's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the TableauMetric
     * @param qualifiedName for the TableauMetric
     * @param terms the list of terms to remove from the TableauMetric, which must be referenced by GUID
     * @return the TableauMetric that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static TableauMetric removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (TableauMetric) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a TableauMetric, without replacing existing Atlan tags linked to the TableauMetric.
     * Note: this operation must make two API calls — one to retrieve the TableauMetric's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the TableauMetric
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated TableauMetric
     */
    public static TableauMetric appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TableauMetric, without replacing existing Atlan tags linked to the TableauMetric.
     * Note: this operation must make two API calls — one to retrieve the TableauMetric's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the TableauMetric
     * @param qualifiedName of the TableauMetric
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated TableauMetric
     */
    public static TableauMetric appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (TableauMetric) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TableauMetric, without replacing existing Atlan tags linked to the TableauMetric.
     * Note: this operation must make two API calls — one to retrieve the TableauMetric's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the TableauMetric
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated TableauMetric
     */
    public static TableauMetric appendAtlanTags(
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
     * Add Atlan tags to a TableauMetric, without replacing existing Atlan tags linked to the TableauMetric.
     * Note: this operation must make two API calls — one to retrieve the TableauMetric's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the TableauMetric
     * @param qualifiedName of the TableauMetric
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated TableauMetric
     */
    public static TableauMetric appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (TableauMetric) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a TableauMetric.
     *
     * @param qualifiedName of the TableauMetric
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the TableauMetric
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a TableauMetric.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a TableauMetric
     * @param qualifiedName of the TableauMetric
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the TableauMetric
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
