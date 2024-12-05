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
 * Instances of APIQuery in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class APIQuery extends Asset implements IAPIQuery, IAPI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "APIQuery";

    /** Fixed typeName for APIQuerys. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** External documentation of the API. */
    @Attribute
    @Singular
    Map<String, String> apiExternalDocs;

    /** APIField assets contained within this APIQuery. */
    @Attribute
    @Singular
    SortedSet<IAPIField> apiFields;

    /** Count of the APIField of this query that are input to it. */
    @Attribute
    Long apiInputFieldCount;

    /** Whether authentication is optional (true) or required (false). */
    @Attribute
    Boolean apiIsAuthOptional;

    /** If this asset refers to an APIObject */
    @Attribute
    Boolean apiIsObjectReference;

    /** Qualified name of the APIObject that is referred to by this asset. When apiIsObjectReference is true. */
    @Attribute
    String apiObjectQualifiedName;

    /** Type of APIQueryOutput. E.g. STRING, NUMBER etc. It is free text. */
    @Attribute
    String apiQueryOutputType;

    /** Secondary Type of APIQueryOutput. E.g. LIST/STRING then LIST would be the secondary type. */
    @Attribute
    String apiQueryOutputTypeSecondary;

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

    /**
     * Builds the minimal object necessary to create a relationship to a APIQuery, from a potentially
     * more-complete APIQuery object.
     *
     * @return the minimal object necessary to relate to the APIQuery
     * @throws InvalidRequestException if any of the minimal set of required properties for a APIQuery relationship are not found in the initial object
     */
    @Override
    public APIQuery trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all APIQuery assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) APIQuery assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all APIQuery assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all APIQuery assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) APIQuerys will be included
     * @return a fluent search that includes all APIQuery assets
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
     * Reference to a APIQuery by GUID. Use this to create a relationship to this APIQuery,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the APIQuery to reference
     * @return reference to a APIQuery that can be used for defining a relationship to a APIQuery
     */
    public static APIQuery refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a APIQuery by GUID. Use this to create a relationship to this APIQuery,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the APIQuery to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a APIQuery that can be used for defining a relationship to a APIQuery
     */
    public static APIQuery refByGuid(String guid, Reference.SaveSemantic semantic) {
        return APIQuery._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a APIQuery by qualifiedName. Use this to create a relationship to this APIQuery,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the APIQuery to reference
     * @return reference to a APIQuery that can be used for defining a relationship to a APIQuery
     */
    public static APIQuery refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a APIQuery by qualifiedName. Use this to create a relationship to this APIQuery,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the APIQuery to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a APIQuery that can be used for defining a relationship to a APIQuery
     */
    public static APIQuery refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return APIQuery._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a APIQuery by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the APIQuery to retrieve, either its GUID or its full qualifiedName
     * @return the requested full APIQuery, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the APIQuery does not exist or the provided GUID is not a APIQuery
     */
    @JsonIgnore
    public static APIQuery get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a APIQuery by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the APIQuery to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full APIQuery, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the APIQuery does not exist or the provided GUID is not a APIQuery
     */
    @JsonIgnore
    public static APIQuery get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof APIQuery) {
                return (APIQuery) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof APIQuery) {
                return (APIQuery) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) APIQuery to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the APIQuery
     * @return true if the APIQuery is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a APIQuery.
     *
     * @param qualifiedName of the APIQuery
     * @param name of the APIQuery
     * @return the minimal request necessary to update the APIQuery, as a builder
     */
    public static APIQueryBuilder<?, ?> updater(String qualifiedName, String name) {
        return APIQuery._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a APIQuery, from a potentially
     * more-complete APIQuery object.
     *
     * @return the minimal object necessary to update the APIQuery, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for APIQuery are not found in the initial object
     */
    @Override
    public APIQueryBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a APIQuery.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the APIQuery
     * @param name of the APIQuery
     * @return the updated APIQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIQuery removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (APIQuery) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a APIQuery.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the APIQuery
     * @param name of the APIQuery
     * @return the updated APIQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIQuery removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (APIQuery) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a APIQuery.
     *
     * @param client connectivity to the Atlan tenant from which to remove the APIQuery's owners
     * @param qualifiedName of the APIQuery
     * @param name of the APIQuery
     * @return the updated APIQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIQuery removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (APIQuery) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a APIQuery.
     *
     * @param client connectivity to the Atlan tenant on which to update the APIQuery's certificate
     * @param qualifiedName of the APIQuery
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated APIQuery, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static APIQuery updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (APIQuery) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a APIQuery.
     *
     * @param client connectivity to the Atlan tenant from which to remove the APIQuery's certificate
     * @param qualifiedName of the APIQuery
     * @param name of the APIQuery
     * @return the updated APIQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIQuery removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (APIQuery) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a APIQuery.
     *
     * @param client connectivity to the Atlan tenant on which to update the APIQuery's announcement
     * @param qualifiedName of the APIQuery
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static APIQuery updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (APIQuery) Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a APIQuery.
     *
     * @param client connectivity to the Atlan client from which to remove the APIQuery's announcement
     * @param qualifiedName of the APIQuery
     * @param name of the APIQuery
     * @return the updated APIQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIQuery removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (APIQuery) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the APIQuery.
     *
     * @param client connectivity to the Atlan tenant on which to replace the APIQuery's assigned terms
     * @param qualifiedName for the APIQuery
     * @param name human-readable name of the APIQuery
     * @param terms the list of terms to replace on the APIQuery, or null to remove all terms from the APIQuery
     * @return the APIQuery that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static APIQuery replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (APIQuery) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the APIQuery, without replacing existing terms linked to the APIQuery.
     * Note: this operation must make two API calls — one to retrieve the APIQuery's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the APIQuery
     * @param qualifiedName for the APIQuery
     * @param terms the list of terms to append to the APIQuery
     * @return the APIQuery that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static APIQuery appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (APIQuery) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a APIQuery, without replacing all existing terms linked to the APIQuery.
     * Note: this operation must make two API calls — one to retrieve the APIQuery's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the APIQuery
     * @param qualifiedName for the APIQuery
     * @param terms the list of terms to remove from the APIQuery, which must be referenced by GUID
     * @return the APIQuery that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static APIQuery removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (APIQuery) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a APIQuery, without replacing existing Atlan tags linked to the APIQuery.
     * Note: this operation must make two API calls — one to retrieve the APIQuery's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the APIQuery
     * @param qualifiedName of the APIQuery
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated APIQuery
     */
    public static APIQuery appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (APIQuery) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a APIQuery, without replacing existing Atlan tags linked to the APIQuery.
     * Note: this operation must make two API calls — one to retrieve the APIQuery's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the APIQuery
     * @param qualifiedName of the APIQuery
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated APIQuery
     */
    public static APIQuery appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (APIQuery) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a APIQuery.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a APIQuery
     * @param qualifiedName of the APIQuery
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the APIQuery
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}