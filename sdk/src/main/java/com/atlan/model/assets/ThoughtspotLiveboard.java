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
 * Instance of a Thoughtspot liveboard in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class ThoughtspotLiveboard extends Asset
        implements IThoughtspotLiveboard, IThoughtspot, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ThoughtspotLiveboard";

    /** Fixed typeName for ThoughtspotLiveboards. */
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

    /** Entities implemented by this asset. */
    @Attribute
    @Singular
    @JsonProperty("modelEntityImplemented")
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

    /** TBC */
    @Attribute
    String thoughtspotChartType;

    /** Number of Columns. */
    @Attribute
    Long thoughtspotColumnCount;

    /** Dashlets that exist within this liveboard. */
    @Attribute
    @Singular
    SortedSet<IThoughtspotDashlet> thoughtspotDashlets;

    /** Total number of data table joins executed for analysis. */
    @Attribute
    Long thoughtspotJoinCount;

    /** TBC */
    @Attribute
    String thoughtspotQuestionText;

    /**
     * Builds the minimal object necessary to create a relationship to a ThoughtspotLiveboard, from a potentially
     * more-complete ThoughtspotLiveboard object.
     *
     * @return the minimal object necessary to relate to the ThoughtspotLiveboard
     * @throws InvalidRequestException if any of the minimal set of required properties for a ThoughtspotLiveboard relationship are not found in the initial object
     */
    @Override
    public ThoughtspotLiveboard trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all ThoughtspotLiveboard assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ThoughtspotLiveboard assets will be included.
     *
     * @return a fluent search that includes all ThoughtspotLiveboard assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all ThoughtspotLiveboard assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ThoughtspotLiveboard assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all ThoughtspotLiveboard assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all ThoughtspotLiveboard assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) ThoughtspotLiveboards will be included
     * @return a fluent search that includes all ThoughtspotLiveboard assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all ThoughtspotLiveboard assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ThoughtspotLiveboards will be included
     * @return a fluent search that includes all ThoughtspotLiveboard assets
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
     * Reference to a ThoughtspotLiveboard by GUID. Use this to create a relationship to this ThoughtspotLiveboard,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the ThoughtspotLiveboard to reference
     * @return reference to a ThoughtspotLiveboard that can be used for defining a relationship to a ThoughtspotLiveboard
     */
    public static ThoughtspotLiveboard refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ThoughtspotLiveboard by GUID. Use this to create a relationship to this ThoughtspotLiveboard,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the ThoughtspotLiveboard to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ThoughtspotLiveboard that can be used for defining a relationship to a ThoughtspotLiveboard
     */
    public static ThoughtspotLiveboard refByGuid(String guid, Reference.SaveSemantic semantic) {
        return ThoughtspotLiveboard._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a ThoughtspotLiveboard by qualifiedName. Use this to create a relationship to this ThoughtspotLiveboard,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the ThoughtspotLiveboard to reference
     * @return reference to a ThoughtspotLiveboard that can be used for defining a relationship to a ThoughtspotLiveboard
     */
    public static ThoughtspotLiveboard refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ThoughtspotLiveboard by qualifiedName. Use this to create a relationship to this ThoughtspotLiveboard,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the ThoughtspotLiveboard to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ThoughtspotLiveboard that can be used for defining a relationship to a ThoughtspotLiveboard
     */
    public static ThoughtspotLiveboard refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return ThoughtspotLiveboard._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a ThoughtspotLiveboard by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the ThoughtspotLiveboard to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ThoughtspotLiveboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ThoughtspotLiveboard does not exist or the provided GUID is not a ThoughtspotLiveboard
     */
    @JsonIgnore
    public static ThoughtspotLiveboard get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a ThoughtspotLiveboard by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ThoughtspotLiveboard to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ThoughtspotLiveboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ThoughtspotLiveboard does not exist or the provided GUID is not a ThoughtspotLiveboard
     */
    @JsonIgnore
    public static ThoughtspotLiveboard get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a ThoughtspotLiveboard by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ThoughtspotLiveboard to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full ThoughtspotLiveboard, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ThoughtspotLiveboard does not exist or the provided GUID is not a ThoughtspotLiveboard
     */
    @JsonIgnore
    public static ThoughtspotLiveboard get(AtlanClient client, String id, boolean includeRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof ThoughtspotLiveboard) {
                return (ThoughtspotLiveboard) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof ThoughtspotLiveboard) {
                return (ThoughtspotLiveboard) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) ThoughtspotLiveboard to active.
     *
     * @param qualifiedName for the ThoughtspotLiveboard
     * @return true if the ThoughtspotLiveboard is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) ThoughtspotLiveboard to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the ThoughtspotLiveboard
     * @return true if the ThoughtspotLiveboard is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a ThoughtspotLiveboard.
     *
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param name of the ThoughtspotLiveboard
     * @return the minimal request necessary to update the ThoughtspotLiveboard, as a builder
     */
    public static ThoughtspotLiveboardBuilder<?, ?> updater(String qualifiedName, String name) {
        return ThoughtspotLiveboard._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ThoughtspotLiveboard, from a potentially
     * more-complete ThoughtspotLiveboard object.
     *
     * @return the minimal object necessary to update the ThoughtspotLiveboard, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ThoughtspotLiveboard are not found in the initial object
     */
    @Override
    public ThoughtspotLiveboardBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a ThoughtspotLiveboard.
     *
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param name of the ThoughtspotLiveboard
     * @return the updated ThoughtspotLiveboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a ThoughtspotLiveboard.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param name of the ThoughtspotLiveboard
     * @return the updated ThoughtspotLiveboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ThoughtspotLiveboard) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ThoughtspotLiveboard.
     *
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param name of the ThoughtspotLiveboard
     * @return the updated ThoughtspotLiveboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a ThoughtspotLiveboard.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param name of the ThoughtspotLiveboard
     * @return the updated ThoughtspotLiveboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ThoughtspotLiveboard) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ThoughtspotLiveboard.
     *
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param name of the ThoughtspotLiveboard
     * @return the updated ThoughtspotLiveboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a ThoughtspotLiveboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ThoughtspotLiveboard's owners
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param name of the ThoughtspotLiveboard
     * @return the updated ThoughtspotLiveboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ThoughtspotLiveboard) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ThoughtspotLiveboard.
     *
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ThoughtspotLiveboard, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a ThoughtspotLiveboard.
     *
     * @param client connectivity to the Atlan tenant on which to update the ThoughtspotLiveboard's certificate
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ThoughtspotLiveboard, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (ThoughtspotLiveboard)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ThoughtspotLiveboard.
     *
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param name of the ThoughtspotLiveboard
     * @return the updated ThoughtspotLiveboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a ThoughtspotLiveboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ThoughtspotLiveboard's certificate
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param name of the ThoughtspotLiveboard
     * @return the updated ThoughtspotLiveboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ThoughtspotLiveboard) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ThoughtspotLiveboard.
     *
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a ThoughtspotLiveboard.
     *
     * @param client connectivity to the Atlan tenant on which to update the ThoughtspotLiveboard's announcement
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (ThoughtspotLiveboard)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ThoughtspotLiveboard.
     *
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param name of the ThoughtspotLiveboard
     * @return the updated ThoughtspotLiveboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a ThoughtspotLiveboard.
     *
     * @param client connectivity to the Atlan client from which to remove the ThoughtspotLiveboard's announcement
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param name of the ThoughtspotLiveboard
     * @return the updated ThoughtspotLiveboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ThoughtspotLiveboard) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the ThoughtspotLiveboard.
     *
     * @param qualifiedName for the ThoughtspotLiveboard
     * @param name human-readable name of the ThoughtspotLiveboard
     * @param terms the list of terms to replace on the ThoughtspotLiveboard, or null to remove all terms from the ThoughtspotLiveboard
     * @return the ThoughtspotLiveboard that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the ThoughtspotLiveboard.
     *
     * @param client connectivity to the Atlan tenant on which to replace the ThoughtspotLiveboard's assigned terms
     * @param qualifiedName for the ThoughtspotLiveboard
     * @param name human-readable name of the ThoughtspotLiveboard
     * @param terms the list of terms to replace on the ThoughtspotLiveboard, or null to remove all terms from the ThoughtspotLiveboard
     * @return the ThoughtspotLiveboard that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (ThoughtspotLiveboard) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ThoughtspotLiveboard, without replacing existing terms linked to the ThoughtspotLiveboard.
     * Note: this operation must make two API calls — one to retrieve the ThoughtspotLiveboard's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the ThoughtspotLiveboard
     * @param terms the list of terms to append to the ThoughtspotLiveboard
     * @return the ThoughtspotLiveboard that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the ThoughtspotLiveboard, without replacing existing terms linked to the ThoughtspotLiveboard.
     * Note: this operation must make two API calls — one to retrieve the ThoughtspotLiveboard's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the ThoughtspotLiveboard
     * @param qualifiedName for the ThoughtspotLiveboard
     * @param terms the list of terms to append to the ThoughtspotLiveboard
     * @return the ThoughtspotLiveboard that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ThoughtspotLiveboard) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ThoughtspotLiveboard, without replacing all existing terms linked to the ThoughtspotLiveboard.
     * Note: this operation must make two API calls — one to retrieve the ThoughtspotLiveboard's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the ThoughtspotLiveboard
     * @param terms the list of terms to remove from the ThoughtspotLiveboard, which must be referenced by GUID
     * @return the ThoughtspotLiveboard that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a ThoughtspotLiveboard, without replacing all existing terms linked to the ThoughtspotLiveboard.
     * Note: this operation must make two API calls — one to retrieve the ThoughtspotLiveboard's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the ThoughtspotLiveboard
     * @param qualifiedName for the ThoughtspotLiveboard
     * @param terms the list of terms to remove from the ThoughtspotLiveboard, which must be referenced by GUID
     * @return the ThoughtspotLiveboard that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ThoughtspotLiveboard removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ThoughtspotLiveboard) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a ThoughtspotLiveboard, without replacing existing Atlan tags linked to the ThoughtspotLiveboard.
     * Note: this operation must make two API calls — one to retrieve the ThoughtspotLiveboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ThoughtspotLiveboard
     */
    public static ThoughtspotLiveboard appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ThoughtspotLiveboard, without replacing existing Atlan tags linked to the ThoughtspotLiveboard.
     * Note: this operation must make two API calls — one to retrieve the ThoughtspotLiveboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ThoughtspotLiveboard
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ThoughtspotLiveboard
     */
    public static ThoughtspotLiveboard appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (ThoughtspotLiveboard) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ThoughtspotLiveboard, without replacing existing Atlan tags linked to the ThoughtspotLiveboard.
     * Note: this operation must make two API calls — one to retrieve the ThoughtspotLiveboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ThoughtspotLiveboard
     */
    public static ThoughtspotLiveboard appendAtlanTags(
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
     * Add Atlan tags to a ThoughtspotLiveboard, without replacing existing Atlan tags linked to the ThoughtspotLiveboard.
     * Note: this operation must make two API calls — one to retrieve the ThoughtspotLiveboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ThoughtspotLiveboard
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ThoughtspotLiveboard
     */
    public static ThoughtspotLiveboard appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (ThoughtspotLiveboard) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a ThoughtspotLiveboard.
     *
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ThoughtspotLiveboard
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a ThoughtspotLiveboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a ThoughtspotLiveboard
     * @param qualifiedName of the ThoughtspotLiveboard
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ThoughtspotLiveboard
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
