/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
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
 * Instance of an API path that could contain one or more endpoints in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class APIPath extends Asset implements IAPIPath, IAPI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "APIPath";

    /** Fixed typeName for APIPaths. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** External documentation of the API. */
    @Attribute
    @Singular
    Map<String, String> apiExternalDocs;

    /** Whether authentication is optional (true) or required (false). */
    @Attribute
    Boolean apiIsAuthOptional;

    /** If this asset refers to an APIObject */
    @Attribute
    Boolean apiIsObjectReference;

    /** Qualified name of the APIObject that is referred to by this asset. When apiIsObjectReference is true. */
    @Attribute
    String apiObjectQualifiedName;

    /** List of the operations available on the endpoint. */
    @Attribute
    @Singular
    SortedSet<String> apiPathAvailableOperations;

    /** Response codes available on the path across all operations. */
    @Attribute
    @Singular
    Map<String, String> apiPathAvailableResponseCodes;

    /** Whether the path is exposed as an ingress (true) or not (false). */
    @Attribute
    Boolean apiPathIsIngressExposed;

    /** Whether the endpoint's path contains replaceable parameters (true) or not (false). */
    @Attribute
    Boolean apiPathIsTemplated;

    /** Absolute path to an individual endpoint. */
    @Attribute
    String apiPathRawURI;

    /** Descriptive summary intended to apply to all operations in this path. */
    @Attribute
    String apiPathSummary;

    /** API specification in which this path exists. */
    @Attribute
    IAPISpec apiSpec;

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
     * Builds the minimal object necessary to create a relationship to a APIPath, from a potentially
     * more-complete APIPath object.
     *
     * @return the minimal object necessary to relate to the APIPath
     * @throws InvalidRequestException if any of the minimal set of required properties for a APIPath relationship are not found in the initial object
     */
    @Override
    public APIPath trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all APIPath assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) APIPath assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all APIPath assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all APIPath assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) APIPaths will be included
     * @return a fluent search that includes all APIPath assets
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
     * Reference to a APIPath by GUID. Use this to create a relationship to this APIPath,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the APIPath to reference
     * @return reference to a APIPath that can be used for defining a relationship to a APIPath
     */
    public static APIPath refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a APIPath by GUID. Use this to create a relationship to this APIPath,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the APIPath to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a APIPath that can be used for defining a relationship to a APIPath
     */
    public static APIPath refByGuid(String guid, Reference.SaveSemantic semantic) {
        return APIPath._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a APIPath by qualifiedName. Use this to create a relationship to this APIPath,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the APIPath to reference
     * @return reference to a APIPath that can be used for defining a relationship to a APIPath
     */
    public static APIPath refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a APIPath by qualifiedName. Use this to create a relationship to this APIPath,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the APIPath to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a APIPath that can be used for defining a relationship to a APIPath
     */
    public static APIPath refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return APIPath._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a APIPath by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the APIPath to retrieve, either its GUID or its full qualifiedName
     * @return the requested full APIPath, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the APIPath does not exist or the provided GUID is not a APIPath
     */
    @JsonIgnore
    public static APIPath get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a APIPath by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the APIPath to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full APIPath, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the APIPath does not exist or the provided GUID is not a APIPath
     */
    @JsonIgnore
    public static APIPath get(AtlanClient client, String id, boolean includeAllRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof APIPath) {
                return (APIPath) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof APIPath) {
                return (APIPath) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a APIPath by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the APIPath to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the APIPath, including any relationships
     * @return the requested APIPath, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the APIPath does not exist or the provided GUID is not a APIPath
     */
    @JsonIgnore
    public static APIPath get(AtlanClient client, String id, Collection<AtlanField> attributes) throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a APIPath by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the APIPath to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the APIPath, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the APIPath
     * @return the requested APIPath, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the APIPath does not exist or the provided GUID is not a APIPath
     */
    @JsonIgnore
    public static APIPath get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = APIPath.select(client)
                    .where(APIPath.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof APIPath) {
                return (APIPath) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = APIPath.select(client)
                    .where(APIPath.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof APIPath) {
                return (APIPath) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) APIPath to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the APIPath
     * @return true if the APIPath is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create an API path.
     *
     * @param name of the API path
     * @param apiSpec in which the API path should be created, which must have at least
     *                a qualifiedName
     * @return the minimal request necessary to create the API path, as a builder
     * @throws InvalidRequestException if the apiSpec provided is without a qualifiedName
     */
    public static APIPathBuilder<?, ?> creator(String name, APISpec apiSpec) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", apiSpec.getQualifiedName());
        validateRelationship(APISpec.TYPE_NAME, map);
        return creator(name, apiSpec.getQualifiedName()).apiSpec(apiSpec.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create an API path.
     *
     * @param pathURI unique URI of the API path
     * @param apiSpecQualifiedName unique name of the API spec through which the path is accessible
     * @return the minimal object necessary to create the API path, as a builder
     */
    public static APIPathBuilder<?, ?> creator(String pathURI, String apiSpecQualifiedName) {
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(apiSpecQualifiedName);
        String normalizedURI = pathURI.startsWith("/") ? pathURI : "/" + pathURI;
        return APIPath._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(apiSpecQualifiedName + normalizedURI)
                .name(normalizedURI)
                .apiPathRawURI(normalizedURI)
                .apiSpec(APISpec.refByQualifiedName(apiSpecQualifiedName))
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.API);
    }

    /**
     * Builds the minimal object necessary to update a APIPath.
     *
     * @param qualifiedName of the APIPath
     * @param name of the APIPath
     * @return the minimal request necessary to update the APIPath, as a builder
     */
    public static APIPathBuilder<?, ?> updater(String qualifiedName, String name) {
        return APIPath._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a APIPath, from a potentially
     * more-complete APIPath object.
     *
     * @return the minimal object necessary to update the APIPath, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for APIPath are not found in the initial object
     */
    @Override
    public APIPathBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a APIPath.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the APIPath
     * @param name of the APIPath
     * @return the updated APIPath, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIPath removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (APIPath) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a APIPath.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the APIPath
     * @param name of the APIPath
     * @return the updated APIPath, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIPath removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (APIPath) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a APIPath.
     *
     * @param client connectivity to the Atlan tenant from which to remove the APIPath's owners
     * @param qualifiedName of the APIPath
     * @param name of the APIPath
     * @return the updated APIPath, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIPath removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (APIPath) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a APIPath.
     *
     * @param client connectivity to the Atlan tenant on which to update the APIPath's certificate
     * @param qualifiedName of the APIPath
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated APIPath, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static APIPath updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (APIPath) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a APIPath.
     *
     * @param client connectivity to the Atlan tenant from which to remove the APIPath's certificate
     * @param qualifiedName of the APIPath
     * @param name of the APIPath
     * @return the updated APIPath, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIPath removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (APIPath) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a APIPath.
     *
     * @param client connectivity to the Atlan tenant on which to update the APIPath's announcement
     * @param qualifiedName of the APIPath
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static APIPath updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (APIPath) Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a APIPath.
     *
     * @param client connectivity to the Atlan client from which to remove the APIPath's announcement
     * @param qualifiedName of the APIPath
     * @param name of the APIPath
     * @return the updated APIPath, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIPath removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (APIPath) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the APIPath.
     *
     * @param client connectivity to the Atlan tenant on which to replace the APIPath's assigned terms
     * @param qualifiedName for the APIPath
     * @param name human-readable name of the APIPath
     * @param terms the list of terms to replace on the APIPath, or null to remove all terms from the APIPath
     * @return the APIPath that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static APIPath replaceTerms(AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (APIPath) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the APIPath, without replacing existing terms linked to the APIPath.
     * Note: this operation must make two API calls — one to retrieve the APIPath's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the APIPath
     * @param qualifiedName for the APIPath
     * @param terms the list of terms to append to the APIPath
     * @return the APIPath that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static APIPath appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (APIPath) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a APIPath, without replacing all existing terms linked to the APIPath.
     * Note: this operation must make two API calls — one to retrieve the APIPath's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the APIPath
     * @param qualifiedName for the APIPath
     * @param terms the list of terms to remove from the APIPath, which must be referenced by GUID
     * @return the APIPath that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static APIPath removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (APIPath) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a APIPath, without replacing existing Atlan tags linked to the APIPath.
     * Note: this operation must make two API calls — one to retrieve the APIPath's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the APIPath
     * @param qualifiedName of the APIPath
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated APIPath
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static APIPath appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (APIPath) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a APIPath, without replacing existing Atlan tags linked to the APIPath.
     * Note: this operation must make two API calls — one to retrieve the APIPath's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the APIPath
     * @param qualifiedName of the APIPath
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated APIPath
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static APIPath appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (APIPath) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a APIPath.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a APIPath
     * @param qualifiedName of the APIPath
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the APIPath
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
