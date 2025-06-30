/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.ADLSAccessTier;
import com.atlan.model.enums.ADLSLeaseState;
import com.atlan.model.enums.ADLSLeaseStatus;
import com.atlan.model.enums.ADLSObjectArchiveStatus;
import com.atlan.model.enums.ADLSObjectType;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.model.structs.AzureTag;
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
 * Instance of an Azure Data Lake Storage (ADLS) blob / object in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class ADLSObject extends Asset
        implements IADLSObject, IADLS, IObjectStore, IAzure, ICatalog, IAsset, IReferenceable, ICloud {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ADLSObject";

    /** Fixed typeName for ADLSObjects. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Name of the account for this ADLS asset. */
    @Attribute
    String adlsAccountName;

    /** Unique name of the account for this ADLS asset. */
    @Attribute
    String adlsAccountQualifiedName;

    /** Secondary location of the ADLS account. */
    @Attribute
    String adlsAccountSecondaryLocation;

    /** Container this object exists within. */
    @Attribute
    IADLSContainer adlsContainer;

    /** Name of the container this object exists within. */
    @Attribute
    String adlsContainerName;

    /** Unique name of the container this object exists within. */
    @Attribute
    String adlsContainerQualifiedName;

    /** Access tier of this object. */
    @Attribute
    ADLSAccessTier adlsObjectAccessTier;

    /** Time (epoch) when the acccess tier for this object was last modified, in milliseconds. */
    @Attribute
    @Date
    Long adlsObjectAccessTierLastModifiedTime;

    /** Archive status of this object. */
    @Attribute
    ADLSObjectArchiveStatus adlsObjectArchiveStatus;

    /** Cache control of this object. */
    @Attribute
    String adlsObjectCacheControl;

    /** Language of this object's contents. */
    @Attribute
    String adlsObjectContentLanguage;

    /** MD5 hash of this object's contents. */
    @Attribute
    String adlsObjectContentMD5Hash;

    /** Content type of this object. */
    @Attribute
    String adlsObjectContentType;

    /** Key of this object, in ADLS. */
    @Attribute
    String adlsObjectKey;

    /** State of this object's lease. */
    @Attribute
    ADLSLeaseState adlsObjectLeaseState;

    /** Status of this object's lease. */
    @Attribute
    ADLSLeaseStatus adlsObjectLeaseStatus;

    /** Metadata associated with this object, from ADLS. */
    @Attribute
    @Singular("putAdlsObjectMetadata")
    Map<String, String> adlsObjectMetadata;

    /** Whether this object is server encrypted (true) or not (false). */
    @Attribute
    Boolean adlsObjectServerEncrypted;

    /** Size of this object. */
    @Attribute
    Long adlsObjectSize;

    /** Type of this object. */
    @Attribute
    ADLSObjectType adlsObjectType;

    /** URL of this object. */
    @Attribute
    String adlsObjectUrl;

    /** Identifier of the version of this object, from ADLS. */
    @Attribute
    String adlsObjectVersionId;

    /** Whether this object supports version-level immutability (true) or not (false). */
    @Attribute
    Boolean adlsObjectVersionLevelImmutabilitySupport;

    /** Location of this asset in Azure. */
    @Attribute
    String azureLocation;

    /** Resource identifier of this asset in Azure. */
    @Attribute
    String azureResourceId;

    /** Tags that have been applied to this asset in Azure. */
    @Attribute
    @Singular
    List<AzureTag> azureTags;

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
     * Builds the minimal object necessary to create a relationship to a ADLSObject, from a potentially
     * more-complete ADLSObject object.
     *
     * @return the minimal object necessary to relate to the ADLSObject
     * @throws InvalidRequestException if any of the minimal set of required properties for a ADLSObject relationship are not found in the initial object
     */
    @Override
    public ADLSObject trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all ADLSObject assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ADLSObject assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all ADLSObject assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all ADLSObject assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ADLSObjects will be included
     * @return a fluent search that includes all ADLSObject assets
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
     * Reference to a ADLSObject by GUID. Use this to create a relationship to this ADLSObject,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the ADLSObject to reference
     * @return reference to a ADLSObject that can be used for defining a relationship to a ADLSObject
     */
    public static ADLSObject refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ADLSObject by GUID. Use this to create a relationship to this ADLSObject,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the ADLSObject to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ADLSObject that can be used for defining a relationship to a ADLSObject
     */
    public static ADLSObject refByGuid(String guid, Reference.SaveSemantic semantic) {
        return ADLSObject._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a ADLSObject by qualifiedName. Use this to create a relationship to this ADLSObject,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the ADLSObject to reference
     * @return reference to a ADLSObject that can be used for defining a relationship to a ADLSObject
     */
    public static ADLSObject refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ADLSObject by qualifiedName. Use this to create a relationship to this ADLSObject,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the ADLSObject to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ADLSObject that can be used for defining a relationship to a ADLSObject
     */
    public static ADLSObject refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return ADLSObject._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a ADLSObject by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ADLSObject to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ADLSObject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSObject does not exist or the provided GUID is not a ADLSObject
     */
    @JsonIgnore
    public static ADLSObject get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a ADLSObject by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ADLSObject to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full ADLSObject, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSObject does not exist or the provided GUID is not a ADLSObject
     */
    @JsonIgnore
    public static ADLSObject get(AtlanClient client, String id, boolean includeAllRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof ADLSObject) {
                return (ADLSObject) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof ADLSObject) {
                return (ADLSObject) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a ADLSObject by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ADLSObject to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the ADLSObject, including any relationships
     * @return the requested ADLSObject, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSObject does not exist or the provided GUID is not a ADLSObject
     */
    @JsonIgnore
    public static ADLSObject get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a ADLSObject by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ADLSObject to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the ADLSObject, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the ADLSObject
     * @return the requested ADLSObject, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSObject does not exist or the provided GUID is not a ADLSObject
     */
    @JsonIgnore
    public static ADLSObject get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = ADLSObject.select(client)
                    .where(ADLSObject.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof ADLSObject) {
                return (ADLSObject) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = ADLSObject.select(client)
                    .where(ADLSObject.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof ADLSObject) {
                return (ADLSObject) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) ADLSObject to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the ADLSObject
     * @return true if the ADLSObject is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ADLSObject.
     *
     * @param name of the ADLSObject
     * @param container in which the ADLSObject should be created, which must have at least
     *                  a qualifiedName
     * @return the minimal request necessary to create the ADLSObject, as a builder
     * @throws InvalidRequestException if the container provided is without a qualifiedName
     */
    public static ADLSObjectBuilder<?, ?> creator(String name, ADLSContainer container) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", container.getConnectionQualifiedName());
        map.put("accountQualifiedName", container.getAdlsAccountQualifiedName());
        map.put("qualifiedName", container.getQualifiedName());
        validateRelationship(ADLSContainer.TYPE_NAME, map);
        return creator(
                        name,
                        container.getConnectionQualifiedName(),
                        container.getAdlsAccountQualifiedName(),
                        container.getQualifiedName())
                .adlsContainer(container.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a ADLSObject.
     *
     * @param name of the ADLSObject
     * @param containerQualifiedName unique name of the container through which the ADLSObject is accessible
     * @return the minimal object necessary to create the ADLSObject, as a builder
     */
    public static ADLSObjectBuilder<?, ?> creator(String name, String containerQualifiedName) {
        String accountQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(containerQualifiedName);
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(containerQualifiedName);
        return creator(name, connectionQualifiedName, accountQualifiedName, containerQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ADLSObject.
     *
     * @param name of the ADLSObject
     * @param connectionQualifiedName unique name of the connection in which the ADLSObject should be created
     * @param accountQualifiedName unique name of the account in which the ADLSObject should be created
     * @param containerQualifiedName unique name of the container through which the ADLSObject is accessible
     * @return the minimal object necessary to create the ADLSObject, as a builder
     */
    public static ADLSObjectBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String accountQualifiedName, String containerQualifiedName) {
        return ADLSObject._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, containerQualifiedName))
                .name(name)
                .adlsContainer(ADLSContainer.refByQualifiedName(containerQualifiedName))
                .adlsAccountQualifiedName(accountQualifiedName)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Generate a unique ADLSObject name.
     *
     * @param name of the ADLSObject
     * @param containerQualifiedName unique name of the container through which the ADLSObject is accessible
     * @return a unique name for the ADLSObject
     */
    public static String generateQualifiedName(String name, String containerQualifiedName) {
        return containerQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a ADLSObject.
     *
     * @param qualifiedName of the ADLSObject
     * @param name of the ADLSObject
     * @return the minimal request necessary to update the ADLSObject, as a builder
     */
    public static ADLSObjectBuilder<?, ?> updater(String qualifiedName, String name) {
        return ADLSObject._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ADLSObject, from a potentially
     * more-complete ADLSObject object.
     *
     * @return the minimal object necessary to update the ADLSObject, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ADLSObject are not found in the initial object
     */
    @Override
    public ADLSObjectBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ADLSObjectBuilder<C extends ADLSObject, B extends ADLSObjectBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a ADLSObject.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ADLSObject
     * @param name of the ADLSObject
     * @return the updated ADLSObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSObject removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ADLSObject) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ADLSObject.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ADLSObject
     * @param name of the ADLSObject
     * @return the updated ADLSObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSObject removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ADLSObject) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ADLSObject.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ADLSObject's owners
     * @param qualifiedName of the ADLSObject
     * @param name of the ADLSObject
     * @return the updated ADLSObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSObject removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (ADLSObject) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ADLSObject.
     *
     * @param client connectivity to the Atlan tenant on which to update the ADLSObject's certificate
     * @param qualifiedName of the ADLSObject
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ADLSObject, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ADLSObject updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (ADLSObject)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ADLSObject.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ADLSObject's certificate
     * @param qualifiedName of the ADLSObject
     * @param name of the ADLSObject
     * @return the updated ADLSObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSObject removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ADLSObject) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ADLSObject.
     *
     * @param client connectivity to the Atlan tenant on which to update the ADLSObject's announcement
     * @param qualifiedName of the ADLSObject
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ADLSObject updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (ADLSObject)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ADLSObject.
     *
     * @param client connectivity to the Atlan client from which to remove the ADLSObject's announcement
     * @param qualifiedName of the ADLSObject
     * @param name of the ADLSObject
     * @return the updated ADLSObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSObject removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ADLSObject) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the ADLSObject.
     *
     * @param client connectivity to the Atlan tenant on which to replace the ADLSObject's assigned terms
     * @param qualifiedName for the ADLSObject
     * @param name human-readable name of the ADLSObject
     * @param terms the list of terms to replace on the ADLSObject, or null to remove all terms from the ADLSObject
     * @return the ADLSObject that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ADLSObject replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (ADLSObject) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ADLSObject, without replacing existing terms linked to the ADLSObject.
     * Note: this operation must make two API calls — one to retrieve the ADLSObject's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the ADLSObject
     * @param qualifiedName for the ADLSObject
     * @param terms the list of terms to append to the ADLSObject
     * @return the ADLSObject that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static ADLSObject appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ADLSObject) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ADLSObject, without replacing all existing terms linked to the ADLSObject.
     * Note: this operation must make two API calls — one to retrieve the ADLSObject's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the ADLSObject
     * @param qualifiedName for the ADLSObject
     * @param terms the list of terms to remove from the ADLSObject, which must be referenced by GUID
     * @return the ADLSObject that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static ADLSObject removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ADLSObject) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a ADLSObject, without replacing existing Atlan tags linked to the ADLSObject.
     * Note: this operation must make two API calls — one to retrieve the ADLSObject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ADLSObject
     * @param qualifiedName of the ADLSObject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ADLSObject
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static ADLSObject appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (ADLSObject) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ADLSObject, without replacing existing Atlan tags linked to the ADLSObject.
     * Note: this operation must make two API calls — one to retrieve the ADLSObject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ADLSObject
     * @param qualifiedName of the ADLSObject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ADLSObject
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static ADLSObject appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (ADLSObject) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a ADLSObject.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a ADLSObject
     * @param qualifiedName of the ADLSObject
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ADLSObject
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
