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
 * Instances of APIObject in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class APIObject extends Asset implements IAPIObject, IAPI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "APIObject";

    /** Fixed typeName for APIObjects. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** External documentation of the API. */
    @Attribute
    @Singular
    Map<String, String> apiExternalDocs;

    /** Count of the APIField of this object. */
    @Attribute
    Long apiFieldCount;

    /** APIField assets contained within this APIObject. */
    @Attribute
    @Singular
    SortedSet<IAPIField> apiFields;

    /** Whether authentication is optional (true) or required (false). */
    @Attribute
    Boolean apiIsAuthOptional;

    /** If this asset refers to an APIObject */
    @Attribute
    Boolean apiIsObjectReference;

    /** Qualified name of the APIObject that is referred to by this asset. When apiIsObjectReference is true. */
    @Attribute
    String apiObjectQualifiedName;

    /** Simple name of the API spec, if this asset is contained in an API spec. */
    @Attribute
    String apiSpecName;

    /** Unique name of the API spec, if this asset is contained in an API spec. */
    @Attribute
    String apiSpecQualifiedName;

    /** Type of API, for example: OpenAPI, GraphQL, etc. */
    @Attribute
    String apiSpecType;

    /** Version of the API specification. */
    @Attribute
    String apiSpecVersion;

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

    /**
     * Builds the minimal object necessary to create a relationship to a APIObject, from a potentially
     * more-complete APIObject object.
     *
     * @return the minimal object necessary to relate to the APIObject
     * @throws InvalidRequestException if any of the minimal set of required properties for a APIObject relationship are not found in the initial object
     */
    @Override
    public APIObject trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all APIObject assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) APIObject assets will be included.
     *
     * @return a fluent search that includes all APIObject assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all APIObject assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) APIObject assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all APIObject assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all APIObject assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) APIObjects will be included
     * @return a fluent search that includes all APIObject assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all APIObject assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) APIObjects will be included
     * @return a fluent search that includes all APIObject assets
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
     * Reference to a APIObject by GUID. Use this to create a relationship to this APIObject,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the APIObject to reference
     * @return reference to a APIObject that can be used for defining a relationship to a APIObject
     */
    public static APIObject refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a APIObject by GUID. Use this to create a relationship to this APIObject,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the APIObject to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a APIObject that can be used for defining a relationship to a APIObject
     */
    public static APIObject refByGuid(String guid, Reference.SaveSemantic semantic) {
        return APIObject._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a APIObject by qualifiedName. Use this to create a relationship to this APIObject,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the APIObject to reference
     * @return reference to a APIObject that can be used for defining a relationship to a APIObject
     */
    public static APIObject refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a APIObject by qualifiedName. Use this to create a relationship to this APIObject,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the APIObject to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a APIObject that can be used for defining a relationship to a APIObject
     */
    public static APIObject refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return APIObject._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a APIObject by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the APIObject to retrieve, either its GUID or its full qualifiedName
     * @return the requested full APIObject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the APIObject does not exist or the provided GUID is not a APIObject
     */
    @JsonIgnore
    public static APIObject get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a APIObject by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the APIObject to retrieve, either its GUID or its full qualifiedName
     * @return the requested full APIObject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the APIObject does not exist or the provided GUID is not a APIObject
     */
    @JsonIgnore
    public static APIObject get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a APIObject by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the APIObject to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full APIObject, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the APIObject does not exist or the provided GUID is not a APIObject
     */
    @JsonIgnore
    public static APIObject get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof APIObject) {
                return (APIObject) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof APIObject) {
                return (APIObject) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) APIObject to active.
     *
     * @param qualifiedName for the APIObject
     * @return true if the APIObject is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) APIObject to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the APIObject
     * @return true if the APIObject is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a APIObject.
     *
     * @param qualifiedName of the APIObject
     * @param name of the APIObject
     * @return the minimal request necessary to update the APIObject, as a builder
     */
    public static APIObjectBuilder<?, ?> updater(String qualifiedName, String name) {
        return APIObject._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a APIObject, from a potentially
     * more-complete APIObject object.
     *
     * @return the minimal object necessary to update the APIObject, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for APIObject are not found in the initial object
     */
    @Override
    public APIObjectBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a APIObject.
     *
     * @param qualifiedName of the APIObject
     * @param name of the APIObject
     * @return the updated APIObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIObject removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a APIObject.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the APIObject
     * @param name of the APIObject
     * @return the updated APIObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIObject removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (APIObject) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a APIObject.
     *
     * @param qualifiedName of the APIObject
     * @param name of the APIObject
     * @return the updated APIObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIObject removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a APIObject.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the APIObject
     * @param name of the APIObject
     * @return the updated APIObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIObject removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (APIObject) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a APIObject.
     *
     * @param qualifiedName of the APIObject
     * @param name of the APIObject
     * @return the updated APIObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIObject removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a APIObject.
     *
     * @param client connectivity to the Atlan tenant from which to remove the APIObject's owners
     * @param qualifiedName of the APIObject
     * @param name of the APIObject
     * @return the updated APIObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIObject removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (APIObject) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a APIObject.
     *
     * @param qualifiedName of the APIObject
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated APIObject, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static APIObject updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a APIObject.
     *
     * @param client connectivity to the Atlan tenant on which to update the APIObject's certificate
     * @param qualifiedName of the APIObject
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated APIObject, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static APIObject updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (APIObject) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a APIObject.
     *
     * @param qualifiedName of the APIObject
     * @param name of the APIObject
     * @return the updated APIObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIObject removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a APIObject.
     *
     * @param client connectivity to the Atlan tenant from which to remove the APIObject's certificate
     * @param qualifiedName of the APIObject
     * @param name of the APIObject
     * @return the updated APIObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIObject removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (APIObject) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a APIObject.
     *
     * @param qualifiedName of the APIObject
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static APIObject updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a APIObject.
     *
     * @param client connectivity to the Atlan tenant on which to update the APIObject's announcement
     * @param qualifiedName of the APIObject
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static APIObject updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (APIObject)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a APIObject.
     *
     * @param qualifiedName of the APIObject
     * @param name of the APIObject
     * @return the updated APIObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIObject removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a APIObject.
     *
     * @param client connectivity to the Atlan client from which to remove the APIObject's announcement
     * @param qualifiedName of the APIObject
     * @param name of the APIObject
     * @return the updated APIObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIObject removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (APIObject) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the APIObject.
     *
     * @param qualifiedName for the APIObject
     * @param name human-readable name of the APIObject
     * @param terms the list of terms to replace on the APIObject, or null to remove all terms from the APIObject
     * @return the APIObject that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static APIObject replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the APIObject.
     *
     * @param client connectivity to the Atlan tenant on which to replace the APIObject's assigned terms
     * @param qualifiedName for the APIObject
     * @param name human-readable name of the APIObject
     * @param terms the list of terms to replace on the APIObject, or null to remove all terms from the APIObject
     * @return the APIObject that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static APIObject replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (APIObject) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the APIObject, without replacing existing terms linked to the APIObject.
     * Note: this operation must make two API calls — one to retrieve the APIObject's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the APIObject
     * @param terms the list of terms to append to the APIObject
     * @return the APIObject that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static APIObject appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the APIObject, without replacing existing terms linked to the APIObject.
     * Note: this operation must make two API calls — one to retrieve the APIObject's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the APIObject
     * @param qualifiedName for the APIObject
     * @param terms the list of terms to append to the APIObject
     * @return the APIObject that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static APIObject appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (APIObject) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a APIObject, without replacing all existing terms linked to the APIObject.
     * Note: this operation must make two API calls — one to retrieve the APIObject's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the APIObject
     * @param terms the list of terms to remove from the APIObject, which must be referenced by GUID
     * @return the APIObject that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static APIObject removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a APIObject, without replacing all existing terms linked to the APIObject.
     * Note: this operation must make two API calls — one to retrieve the APIObject's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the APIObject
     * @param qualifiedName for the APIObject
     * @param terms the list of terms to remove from the APIObject, which must be referenced by GUID
     * @return the APIObject that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static APIObject removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (APIObject) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a APIObject, without replacing existing Atlan tags linked to the APIObject.
     * Note: this operation must make two API calls — one to retrieve the APIObject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the APIObject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated APIObject
     */
    public static APIObject appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a APIObject, without replacing existing Atlan tags linked to the APIObject.
     * Note: this operation must make two API calls — one to retrieve the APIObject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the APIObject
     * @param qualifiedName of the APIObject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated APIObject
     */
    public static APIObject appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (APIObject) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a APIObject, without replacing existing Atlan tags linked to the APIObject.
     * Note: this operation must make two API calls — one to retrieve the APIObject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the APIObject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated APIObject
     */
    public static APIObject appendAtlanTags(
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
     * Add Atlan tags to a APIObject, without replacing existing Atlan tags linked to the APIObject.
     * Note: this operation must make two API calls — one to retrieve the APIObject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the APIObject
     * @param qualifiedName of the APIObject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated APIObject
     */
    public static APIObject appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (APIObject) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a APIObject.
     *
     * @param qualifiedName of the APIObject
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the APIObject
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a APIObject.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a APIObject
     * @param qualifiedName of the APIObject
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the APIObject
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
