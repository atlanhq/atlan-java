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
 * Instance of a Looker Explore in Atlan. Explores are views that users can query in Looker.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class LookerExplore extends Asset implements ILookerExplore, ILooker, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "LookerExplore";

    /** Fixed typeName for LookerExplores. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Fields that exist within this Explore. */
    @Attribute
    @Singular
    SortedSet<ILookerField> fields;

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

    /** Model in which this explore exists. */
    @Attribute
    ILookerModel model;

    /** Entities implemented by this asset. */
    @Attribute
    @Singular
    @JsonProperty("modelEntityImplemented")
    SortedSet<IModelEntity> modelImplementedEntities;

    /** Name of the parent model of this Explore. */
    @Attribute
    String modelName;

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

    /** Project in which this explore exists. */
    @Attribute
    ILookerProject project;

    /** Name of the parent project of this Explore. */
    @Attribute
    String projectName;

    /** Connection name for the Explore, from Looker. */
    @Attribute
    String sourceConnectionName;

    /** Name of the SQL table used to declare the Explore. */
    @Attribute
    String sqlTableName;

    /** Name of the view for the Explore. */
    @Attribute
    String viewName;

    /**
     * Builds the minimal object necessary to create a relationship to a LookerExplore, from a potentially
     * more-complete LookerExplore object.
     *
     * @return the minimal object necessary to relate to the LookerExplore
     * @throws InvalidRequestException if any of the minimal set of required properties for a LookerExplore relationship are not found in the initial object
     */
    @Override
    public LookerExplore trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all LookerExplore assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) LookerExplore assets will be included.
     *
     * @return a fluent search that includes all LookerExplore assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all LookerExplore assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) LookerExplore assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all LookerExplore assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all LookerExplore assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) LookerExplores will be included
     * @return a fluent search that includes all LookerExplore assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all LookerExplore assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) LookerExplores will be included
     * @return a fluent search that includes all LookerExplore assets
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
     * Reference to a LookerExplore by GUID. Use this to create a relationship to this LookerExplore,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the LookerExplore to reference
     * @return reference to a LookerExplore that can be used for defining a relationship to a LookerExplore
     */
    public static LookerExplore refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a LookerExplore by GUID. Use this to create a relationship to this LookerExplore,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the LookerExplore to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a LookerExplore that can be used for defining a relationship to a LookerExplore
     */
    public static LookerExplore refByGuid(String guid, Reference.SaveSemantic semantic) {
        return LookerExplore._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a LookerExplore by qualifiedName. Use this to create a relationship to this LookerExplore,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the LookerExplore to reference
     * @return reference to a LookerExplore that can be used for defining a relationship to a LookerExplore
     */
    public static LookerExplore refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a LookerExplore by qualifiedName. Use this to create a relationship to this LookerExplore,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the LookerExplore to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a LookerExplore that can be used for defining a relationship to a LookerExplore
     */
    public static LookerExplore refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return LookerExplore._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a LookerExplore by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the LookerExplore to retrieve, either its GUID or its full qualifiedName
     * @return the requested full LookerExplore, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerExplore does not exist or the provided GUID is not a LookerExplore
     */
    @JsonIgnore
    public static LookerExplore get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a LookerExplore by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the LookerExplore to retrieve, either its GUID or its full qualifiedName
     * @return the requested full LookerExplore, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerExplore does not exist or the provided GUID is not a LookerExplore
     */
    @JsonIgnore
    public static LookerExplore get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a LookerExplore by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the LookerExplore to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full LookerExplore, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerExplore does not exist or the provided GUID is not a LookerExplore
     */
    @JsonIgnore
    public static LookerExplore get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof LookerExplore) {
                return (LookerExplore) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof LookerExplore) {
                return (LookerExplore) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) LookerExplore to active.
     *
     * @param qualifiedName for the LookerExplore
     * @return true if the LookerExplore is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) LookerExplore to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the LookerExplore
     * @return true if the LookerExplore is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param name of the LookerExplore
     * @return the minimal request necessary to update the LookerExplore, as a builder
     */
    public static LookerExploreBuilder<?, ?> updater(String qualifiedName, String name) {
        return LookerExplore._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a LookerExplore, from a potentially
     * more-complete LookerExplore object.
     *
     * @return the minimal object necessary to update the LookerExplore, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for LookerExplore are not found in the initial object
     */
    @Override
    public LookerExploreBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param name of the LookerExplore
     * @return the updated LookerExplore, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a LookerExplore.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the LookerExplore
     * @param name of the LookerExplore
     * @return the updated LookerExplore, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LookerExplore) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param name of the LookerExplore
     * @return the updated LookerExplore, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a LookerExplore.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the LookerExplore
     * @param name of the LookerExplore
     * @return the updated LookerExplore, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LookerExplore) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param name of the LookerExplore
     * @return the updated LookerExplore, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a LookerExplore.
     *
     * @param client connectivity to the Atlan tenant from which to remove the LookerExplore's owners
     * @param qualifiedName of the LookerExplore
     * @param name of the LookerExplore
     * @return the updated LookerExplore, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LookerExplore) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated LookerExplore, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a LookerExplore.
     *
     * @param client connectivity to the Atlan tenant on which to update the LookerExplore's certificate
     * @param qualifiedName of the LookerExplore
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated LookerExplore, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (LookerExplore)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param name of the LookerExplore
     * @return the updated LookerExplore, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a LookerExplore.
     *
     * @param client connectivity to the Atlan tenant from which to remove the LookerExplore's certificate
     * @param qualifiedName of the LookerExplore
     * @param name of the LookerExplore
     * @return the updated LookerExplore, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LookerExplore) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a LookerExplore.
     *
     * @param client connectivity to the Atlan tenant on which to update the LookerExplore's announcement
     * @param qualifiedName of the LookerExplore
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (LookerExplore)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param name of the LookerExplore
     * @return the updated LookerExplore, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a LookerExplore.
     *
     * @param client connectivity to the Atlan client from which to remove the LookerExplore's announcement
     * @param qualifiedName of the LookerExplore
     * @param name of the LookerExplore
     * @return the updated LookerExplore, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerExplore removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LookerExplore) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the LookerExplore.
     *
     * @param qualifiedName for the LookerExplore
     * @param name human-readable name of the LookerExplore
     * @param terms the list of terms to replace on the LookerExplore, or null to remove all terms from the LookerExplore
     * @return the LookerExplore that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static LookerExplore replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the LookerExplore.
     *
     * @param client connectivity to the Atlan tenant on which to replace the LookerExplore's assigned terms
     * @param qualifiedName for the LookerExplore
     * @param name human-readable name of the LookerExplore
     * @param terms the list of terms to replace on the LookerExplore, or null to remove all terms from the LookerExplore
     * @return the LookerExplore that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static LookerExplore replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (LookerExplore) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the LookerExplore, without replacing existing terms linked to the LookerExplore.
     * Note: this operation must make two API calls — one to retrieve the LookerExplore's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the LookerExplore
     * @param terms the list of terms to append to the LookerExplore
     * @return the LookerExplore that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static LookerExplore appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the LookerExplore, without replacing existing terms linked to the LookerExplore.
     * Note: this operation must make two API calls — one to retrieve the LookerExplore's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the LookerExplore
     * @param qualifiedName for the LookerExplore
     * @param terms the list of terms to append to the LookerExplore
     * @return the LookerExplore that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static LookerExplore appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (LookerExplore) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a LookerExplore, without replacing all existing terms linked to the LookerExplore.
     * Note: this operation must make two API calls — one to retrieve the LookerExplore's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the LookerExplore
     * @param terms the list of terms to remove from the LookerExplore, which must be referenced by GUID
     * @return the LookerExplore that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static LookerExplore removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a LookerExplore, without replacing all existing terms linked to the LookerExplore.
     * Note: this operation must make two API calls — one to retrieve the LookerExplore's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the LookerExplore
     * @param qualifiedName for the LookerExplore
     * @param terms the list of terms to remove from the LookerExplore, which must be referenced by GUID
     * @return the LookerExplore that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static LookerExplore removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (LookerExplore) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a LookerExplore, without replacing existing Atlan tags linked to the LookerExplore.
     * Note: this operation must make two API calls — one to retrieve the LookerExplore's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the LookerExplore
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated LookerExplore
     */
    public static LookerExplore appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a LookerExplore, without replacing existing Atlan tags linked to the LookerExplore.
     * Note: this operation must make two API calls — one to retrieve the LookerExplore's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the LookerExplore
     * @param qualifiedName of the LookerExplore
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated LookerExplore
     */
    public static LookerExplore appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (LookerExplore) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a LookerExplore, without replacing existing Atlan tags linked to the LookerExplore.
     * Note: this operation must make two API calls — one to retrieve the LookerExplore's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the LookerExplore
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated LookerExplore
     */
    public static LookerExplore appendAtlanTags(
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
     * Add Atlan tags to a LookerExplore, without replacing existing Atlan tags linked to the LookerExplore.
     * Note: this operation must make two API calls — one to retrieve the LookerExplore's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the LookerExplore
     * @param qualifiedName of the LookerExplore
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated LookerExplore
     */
    public static LookerExplore appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (LookerExplore) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a LookerExplore.
     *
     * @param qualifiedName of the LookerExplore
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the LookerExplore
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a LookerExplore.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a LookerExplore
     * @param qualifiedName of the LookerExplore
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the LookerExplore
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
