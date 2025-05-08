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
 * Instance of a Salesforce object in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class SalesforceObject extends Asset
        implements ISalesforceObject, ISalesforce, ISaaS, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SalesforceObject";

    /** Fixed typeName for SalesforceObjects. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Name of this asset in the Salesforce API. */
    @Attribute
    String apiName;

    /** Number of fields in this object. */
    @Attribute
    Long fieldCount;

    /** Fields that exist within this object. */
    @Attribute
    @Singular
    SortedSet<ISalesforceField> fields;

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

    /** Whether this object is a custom object (true) or not (false). */
    @Attribute
    Boolean isCustom;

    /** Whether this object is mergable (true) or not (false). */
    @Attribute
    Boolean isMergable;

    /** Whether this object is queryable (true) or not (false). */
    @Attribute
    Boolean isQueryable;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISalesforceField> lookupFields;

    /** Attributes implemented by this asset. */
    @Attribute
    @Singular
    SortedSet<IModelAttribute> modelImplementedAttributes;

    /** Entities implemented by this asset. */
    @Attribute
    @Singular
    SortedSet<IModelEntity> modelImplementedEntities;

    /** Organization in which this object exists. */
    @Attribute
    ISalesforceOrganization organization;

    /** Fully-qualified name of the organization in Salesforce. */
    @Attribute
    String organizationQualifiedName;

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
     * Builds the minimal object necessary to create a relationship to a SalesforceObject, from a potentially
     * more-complete SalesforceObject object.
     *
     * @return the minimal object necessary to relate to the SalesforceObject
     * @throws InvalidRequestException if any of the minimal set of required properties for a SalesforceObject relationship are not found in the initial object
     */
    @Override
    public SalesforceObject trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SalesforceObject assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SalesforceObject assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SalesforceObject assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SalesforceObject assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SalesforceObjects will be included
     * @return a fluent search that includes all SalesforceObject assets
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
     * Reference to a SalesforceObject by GUID. Use this to create a relationship to this SalesforceObject,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SalesforceObject to reference
     * @return reference to a SalesforceObject that can be used for defining a relationship to a SalesforceObject
     */
    public static SalesforceObject refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SalesforceObject by GUID. Use this to create a relationship to this SalesforceObject,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SalesforceObject to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SalesforceObject that can be used for defining a relationship to a SalesforceObject
     */
    public static SalesforceObject refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SalesforceObject._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SalesforceObject by qualifiedName. Use this to create a relationship to this SalesforceObject,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SalesforceObject to reference
     * @return reference to a SalesforceObject that can be used for defining a relationship to a SalesforceObject
     */
    public static SalesforceObject refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SalesforceObject by qualifiedName. Use this to create a relationship to this SalesforceObject,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SalesforceObject to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SalesforceObject that can be used for defining a relationship to a SalesforceObject
     */
    public static SalesforceObject refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SalesforceObject._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SalesforceObject by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SalesforceObject to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SalesforceObject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceObject does not exist or the provided GUID is not a SalesforceObject
     */
    @JsonIgnore
    public static SalesforceObject get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SalesforceObject by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SalesforceObject to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SalesforceObject, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceObject does not exist or the provided GUID is not a SalesforceObject
     */
    @JsonIgnore
    public static SalesforceObject get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SalesforceObject) {
                return (SalesforceObject) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof SalesforceObject) {
                return (SalesforceObject) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SalesforceObject by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SalesforceObject to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SalesforceObject, including any relationships
     * @return the requested SalesforceObject, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceObject does not exist or the provided GUID is not a SalesforceObject
     */
    @JsonIgnore
    public static SalesforceObject get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a SalesforceObject by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SalesforceObject to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SalesforceObject, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the SalesforceObject
     * @return the requested SalesforceObject, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceObject does not exist or the provided GUID is not a SalesforceObject
     */
    @JsonIgnore
    public static SalesforceObject get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = SalesforceObject.select(client)
                    .where(SalesforceObject.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof SalesforceObject) {
                return (SalesforceObject) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = SalesforceObject.select(client)
                    .where(SalesforceObject.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof SalesforceObject) {
                return (SalesforceObject) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SalesforceObject to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SalesforceObject
     * @return true if the SalesforceObject is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a SalesforceObject asset.
     *
     * @param name of the object
     * @param organization Salesforce organization through which the asset is accessible, which must have its qualifiedName populated
     * @return the minimal object necessary to create the asset, as a builder
     * @throws InvalidRequestException if the provided organization does not have a qualifiedName
     */
    public static SalesforceObjectBuilder<?, ?> creator(String name, SalesforceOrganization organization)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", organization.getQualifiedName());
        map.put("connectionQualifiedName", organization.getConnectionQualifiedName());
        validateRelationship(SalesforceOrganization.TYPE_NAME, map);
        return creator(name, organization.getConnectionQualifiedName(), organization.getQualifiedName())
                .organization(organization.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a SalesforceObject asset.
     *
     * @param name of the object
     * @param organizationQualifiedName unique name of the organization through which the asset is accessible
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static SalesforceObjectBuilder<?, ?> creator(String name, String organizationQualifiedName) {
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(organizationQualifiedName);
        return creator(name, connectionQualifiedName, organizationQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a SalesforceObject asset.
     *
     * @param name of the object
     * @param connectionQualifiedName unique name of the connection in which to create the SalesforceObject
     * @param organizationQualifiedName unique name of the SalesforceOrganization in which to create the SalesforceObject
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static SalesforceObjectBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String organizationQualifiedName) {
        return SalesforceObject._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, organizationQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .organization(SalesforceOrganization.refByQualifiedName(organizationQualifiedName))
                .organizationQualifiedName(organizationQualifiedName);
    }

    /**
     * Generate a unique SalesforceObject name.
     *
     * @param name unique name of the object within Salesforce
     * @param organizationQualifiedName unique name of the organization through which the SalesforceObject is accessible
     * @return a unique name for the SalesforceObject
     */
    public static String generateQualifiedName(String name, String organizationQualifiedName) {
        return organizationQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a SalesforceObject.
     *
     * @param qualifiedName of the SalesforceObject
     * @param name of the SalesforceObject
     * @return the minimal request necessary to update the SalesforceObject, as a builder
     */
    public static SalesforceObjectBuilder<?, ?> updater(String qualifiedName, String name) {
        return SalesforceObject._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SalesforceObject, from a potentially
     * more-complete SalesforceObject object.
     *
     * @return the minimal object necessary to update the SalesforceObject, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SalesforceObject are not found in the initial object
     */
    @Override
    public SalesforceObjectBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a SalesforceObject.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SalesforceObject
     * @param name of the SalesforceObject
     * @return the updated SalesforceObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceObject removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceObject) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SalesforceObject.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SalesforceObject
     * @param name of the SalesforceObject
     * @return the updated SalesforceObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceObject removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceObject) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SalesforceObject.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SalesforceObject's owners
     * @param qualifiedName of the SalesforceObject
     * @param name of the SalesforceObject
     * @return the updated SalesforceObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceObject removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceObject) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SalesforceObject.
     *
     * @param client connectivity to the Atlan tenant on which to update the SalesforceObject's certificate
     * @param qualifiedName of the SalesforceObject
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SalesforceObject, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceObject updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SalesforceObject)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SalesforceObject.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SalesforceObject's certificate
     * @param qualifiedName of the SalesforceObject
     * @param name of the SalesforceObject
     * @return the updated SalesforceObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceObject removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceObject) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SalesforceObject.
     *
     * @param client connectivity to the Atlan tenant on which to update the SalesforceObject's announcement
     * @param qualifiedName of the SalesforceObject
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceObject updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SalesforceObject)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SalesforceObject.
     *
     * @param client connectivity to the Atlan client from which to remove the SalesforceObject's announcement
     * @param qualifiedName of the SalesforceObject
     * @param name of the SalesforceObject
     * @return the updated SalesforceObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceObject removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SalesforceObject) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SalesforceObject.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SalesforceObject's assigned terms
     * @param qualifiedName for the SalesforceObject
     * @param name human-readable name of the SalesforceObject
     * @param terms the list of terms to replace on the SalesforceObject, or null to remove all terms from the SalesforceObject
     * @return the SalesforceObject that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceObject replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SalesforceObject) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SalesforceObject, without replacing existing terms linked to the SalesforceObject.
     * Note: this operation must make two API calls — one to retrieve the SalesforceObject's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SalesforceObject
     * @param qualifiedName for the SalesforceObject
     * @param terms the list of terms to append to the SalesforceObject
     * @return the SalesforceObject that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SalesforceObject appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SalesforceObject) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SalesforceObject, without replacing all existing terms linked to the SalesforceObject.
     * Note: this operation must make two API calls — one to retrieve the SalesforceObject's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SalesforceObject
     * @param qualifiedName for the SalesforceObject
     * @param terms the list of terms to remove from the SalesforceObject, which must be referenced by GUID
     * @return the SalesforceObject that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SalesforceObject removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SalesforceObject) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SalesforceObject, without replacing existing Atlan tags linked to the SalesforceObject.
     * Note: this operation must make two API calls — one to retrieve the SalesforceObject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SalesforceObject
     * @param qualifiedName of the SalesforceObject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SalesforceObject
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static SalesforceObject appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SalesforceObject) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SalesforceObject, without replacing existing Atlan tags linked to the SalesforceObject.
     * Note: this operation must make two API calls — one to retrieve the SalesforceObject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SalesforceObject
     * @param qualifiedName of the SalesforceObject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SalesforceObject
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static SalesforceObject appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SalesforceObject) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SalesforceObject.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SalesforceObject
     * @param qualifiedName of the SalesforceObject
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SalesforceObject
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
