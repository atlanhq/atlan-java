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
 * Base class for ADF Linkedservices. It is a connection to a data source or compute resource used by Azure Data Factory.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class AdfLinkedservice extends Asset implements IAdfLinkedservice, IADF, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AdfLinkedservice";

    /** Fixed typeName for AdfLinkedservices. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** ADF Linkedservice that is associated with these ADF activities. */
    @Attribute
    @Singular
    SortedSet<IAdfActivity> adfActivities;

    /** Defines the folder path in which this ADF asset exists. */
    @Attribute
    String adfAssetFolderPath;

    /** ADF Linkedservices that are associated with this ADF Dataflows. */
    @Attribute
    @Singular
    SortedSet<IAdfDataflow> adfDataflows;

    /** ADF Linkedservice that is associated with these ADF datasets. */
    @Attribute
    @Singular
    SortedSet<IAdfDataset> adfDatasets;

    /** Defines the name of the factory in which this asset exists. */
    @Attribute
    String adfFactoryName;

    /** Defines the name of the account used in the cosmos linked service. */
    @Attribute
    String adfLinkedserviceAccountName;

    /** The list of annotation assigned to a linked service. */
    @Attribute
    @Singular
    SortedSet<String> adfLinkedserviceAnnotations;

    /** Defines the type of cloud being used in the ADLS linked service. */
    @Attribute
    String adfLinkedserviceAzureCloudType;

    /** Defines the cluster id in the Azure databricks delta lake linked service. */
    @Attribute
    String adfLinkedserviceClusterId;

    /** Defines the type of credential, authentication being used in the ADLS, snowflake, azure sql linked service. */
    @Attribute
    String adfLinkedserviceCredentialType;

    /** Defines the name of the database used in the cosmos, snowflake linked service. */
    @Attribute
    String adfLinkedserviceDatabaseName;

    /** Defines the url, domain, account_identifier, server in the ADLS, Azure databricks delta lake, snowflake, azure sql linked service. */
    @Attribute
    String adfLinkedserviceDomainEndpoint;

    /** Defines the resource id in the Azure databricks delta lake linked service. */
    @Attribute
    String adfLinkedserviceResourceId;

    /** Defines the name of the role in the snowflake linked service. */
    @Attribute
    String adfLinkedserviceRoleName;

    /** Defines the tenant of cloud being used in the ADLS linked service. */
    @Attribute
    String adfLinkedserviceTenant;

    /** Defines the type of the linked service. */
    @Attribute
    String adfLinkedserviceType;

    /** Defines the name of the db user in the snowflake linked service. */
    @Attribute
    String adfLinkedserviceUserName;

    /** Defines the version of the linked service in the cosmos linked service. */
    @Attribute
    String adfLinkedserviceVersion;

    /** Indicates whether the service version is above 3.2 or not in the cosmos linked service. */
    @Attribute
    Boolean adfLinkedserviceVersionAbove;

    /** Defines the name of the warehouse in the snowflake linked service. */
    @Attribute
    String adfLinkedserviceWarehouseName;

    /** ADF Linkedservices that are associated with this ADF pipelines. */
    @Attribute
    @Singular
    SortedSet<IAdfPipeline> adfPipelines;

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
     * Builds the minimal object necessary to create a relationship to a AdfLinkedservice, from a potentially
     * more-complete AdfLinkedservice object.
     *
     * @return the minimal object necessary to relate to the AdfLinkedservice
     * @throws InvalidRequestException if any of the minimal set of required properties for a AdfLinkedservice relationship are not found in the initial object
     */
    @Override
    public AdfLinkedservice trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all AdfLinkedservice assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) AdfLinkedservice assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all AdfLinkedservice assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all AdfLinkedservice assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) AdfLinkedservices will be included
     * @return a fluent search that includes all AdfLinkedservice assets
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
     * Reference to a AdfLinkedservice by GUID. Use this to create a relationship to this AdfLinkedservice,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the AdfLinkedservice to reference
     * @return reference to a AdfLinkedservice that can be used for defining a relationship to a AdfLinkedservice
     */
    public static AdfLinkedservice refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AdfLinkedservice by GUID. Use this to create a relationship to this AdfLinkedservice,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the AdfLinkedservice to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AdfLinkedservice that can be used for defining a relationship to a AdfLinkedservice
     */
    public static AdfLinkedservice refByGuid(String guid, Reference.SaveSemantic semantic) {
        return AdfLinkedservice._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a AdfLinkedservice by qualifiedName. Use this to create a relationship to this AdfLinkedservice,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the AdfLinkedservice to reference
     * @return reference to a AdfLinkedservice that can be used for defining a relationship to a AdfLinkedservice
     */
    public static AdfLinkedservice refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AdfLinkedservice by qualifiedName. Use this to create a relationship to this AdfLinkedservice,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the AdfLinkedservice to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AdfLinkedservice that can be used for defining a relationship to a AdfLinkedservice
     */
    public static AdfLinkedservice refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return AdfLinkedservice._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a AdfLinkedservice by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AdfLinkedservice to retrieve, either its GUID or its full qualifiedName
     * @return the requested full AdfLinkedservice, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AdfLinkedservice does not exist or the provided GUID is not a AdfLinkedservice
     */
    @JsonIgnore
    public static AdfLinkedservice get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a AdfLinkedservice by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AdfLinkedservice to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full AdfLinkedservice, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AdfLinkedservice does not exist or the provided GUID is not a AdfLinkedservice
     */
    @JsonIgnore
    public static AdfLinkedservice get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof AdfLinkedservice) {
                return (AdfLinkedservice) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof AdfLinkedservice) {
                return (AdfLinkedservice) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a AdfLinkedservice by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AdfLinkedservice to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AdfLinkedservice, including any relationships
     * @return the requested AdfLinkedservice, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AdfLinkedservice does not exist or the provided GUID is not a AdfLinkedservice
     */
    @JsonIgnore
    public static AdfLinkedservice get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a AdfLinkedservice by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AdfLinkedservice to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AdfLinkedservice, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the AdfLinkedservice
     * @return the requested AdfLinkedservice, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AdfLinkedservice does not exist or the provided GUID is not a AdfLinkedservice
     */
    @JsonIgnore
    public static AdfLinkedservice get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = AdfLinkedservice.select(client)
                    .where(AdfLinkedservice.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof AdfLinkedservice) {
                return (AdfLinkedservice) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = AdfLinkedservice.select(client)
                    .where(AdfLinkedservice.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof AdfLinkedservice) {
                return (AdfLinkedservice) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) AdfLinkedservice to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the AdfLinkedservice
     * @return true if the AdfLinkedservice is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a AdfLinkedservice.
     *
     * @param qualifiedName of the AdfLinkedservice
     * @param name of the AdfLinkedservice
     * @return the minimal request necessary to update the AdfLinkedservice, as a builder
     */
    public static AdfLinkedserviceBuilder<?, ?> updater(String qualifiedName, String name) {
        return AdfLinkedservice._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AdfLinkedservice, from a potentially
     * more-complete AdfLinkedservice object.
     *
     * @return the minimal object necessary to update the AdfLinkedservice, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AdfLinkedservice are not found in the initial object
     */
    @Override
    public AdfLinkedserviceBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class AdfLinkedserviceBuilder<
                    C extends AdfLinkedservice, B extends AdfLinkedserviceBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a AdfLinkedservice.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AdfLinkedservice
     * @param name of the AdfLinkedservice
     * @return the updated AdfLinkedservice, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AdfLinkedservice removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AdfLinkedservice) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a AdfLinkedservice.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AdfLinkedservice
     * @param name of the AdfLinkedservice
     * @return the updated AdfLinkedservice, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AdfLinkedservice removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AdfLinkedservice) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a AdfLinkedservice.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AdfLinkedservice's owners
     * @param qualifiedName of the AdfLinkedservice
     * @param name of the AdfLinkedservice
     * @return the updated AdfLinkedservice, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AdfLinkedservice removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AdfLinkedservice) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a AdfLinkedservice.
     *
     * @param client connectivity to the Atlan tenant on which to update the AdfLinkedservice's certificate
     * @param qualifiedName of the AdfLinkedservice
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AdfLinkedservice, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AdfLinkedservice updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (AdfLinkedservice)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a AdfLinkedservice.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AdfLinkedservice's certificate
     * @param qualifiedName of the AdfLinkedservice
     * @param name of the AdfLinkedservice
     * @return the updated AdfLinkedservice, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AdfLinkedservice removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AdfLinkedservice) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a AdfLinkedservice.
     *
     * @param client connectivity to the Atlan tenant on which to update the AdfLinkedservice's announcement
     * @param qualifiedName of the AdfLinkedservice
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AdfLinkedservice updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (AdfLinkedservice)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a AdfLinkedservice.
     *
     * @param client connectivity to the Atlan client from which to remove the AdfLinkedservice's announcement
     * @param qualifiedName of the AdfLinkedservice
     * @param name of the AdfLinkedservice
     * @return the updated AdfLinkedservice, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AdfLinkedservice removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AdfLinkedservice) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the AdfLinkedservice.
     *
     * @param client connectivity to the Atlan tenant on which to replace the AdfLinkedservice's assigned terms
     * @param qualifiedName for the AdfLinkedservice
     * @param name human-readable name of the AdfLinkedservice
     * @param terms the list of terms to replace on the AdfLinkedservice, or null to remove all terms from the AdfLinkedservice
     * @return the AdfLinkedservice that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static AdfLinkedservice replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (AdfLinkedservice) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the AdfLinkedservice, without replacing existing terms linked to the AdfLinkedservice.
     * Note: this operation must make two API calls — one to retrieve the AdfLinkedservice's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the AdfLinkedservice
     * @param qualifiedName for the AdfLinkedservice
     * @param terms the list of terms to append to the AdfLinkedservice
     * @return the AdfLinkedservice that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static AdfLinkedservice appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AdfLinkedservice) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a AdfLinkedservice, without replacing all existing terms linked to the AdfLinkedservice.
     * Note: this operation must make two API calls — one to retrieve the AdfLinkedservice's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the AdfLinkedservice
     * @param qualifiedName for the AdfLinkedservice
     * @param terms the list of terms to remove from the AdfLinkedservice, which must be referenced by GUID
     * @return the AdfLinkedservice that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static AdfLinkedservice removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AdfLinkedservice) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a AdfLinkedservice, without replacing existing Atlan tags linked to the AdfLinkedservice.
     * Note: this operation must make two API calls — one to retrieve the AdfLinkedservice's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AdfLinkedservice
     * @param qualifiedName of the AdfLinkedservice
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated AdfLinkedservice
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static AdfLinkedservice appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (AdfLinkedservice) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AdfLinkedservice, without replacing existing Atlan tags linked to the AdfLinkedservice.
     * Note: this operation must make two API calls — one to retrieve the AdfLinkedservice's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AdfLinkedservice
     * @param qualifiedName of the AdfLinkedservice
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated AdfLinkedservice
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static AdfLinkedservice appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (AdfLinkedservice) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a AdfLinkedservice.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a AdfLinkedservice
     * @param qualifiedName of the AdfLinkedservice
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the AdfLinkedservice
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
