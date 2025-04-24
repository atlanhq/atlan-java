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
import com.atlan.model.structs.GoogleLabel;
import com.atlan.model.structs.GoogleTag;
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
 * Instance of a Google Cloud Storage object in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class GCSObject extends Asset
        implements IGCSObject, IGCS, IGoogle, IObjectStore, ICloud, IAsset, IReferenceable, ICatalog {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "GCSObject";

    /** Fixed typeName for GCSObjects. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Access control list for this asset. */
    @Attribute
    String gcsAccessControl;

    /** GCS bucket in which the object exists. */
    @Attribute
    IGCSBucket gcsBucket;

    /** Simple name of the bucket in which this object exists. */
    @Attribute
    String gcsBucketName;

    /** Unique name of the bucket in which this object exists. */
    @Attribute
    String gcsBucketQualifiedName;

    /** Entity tag for the asset. An entity tag is a hash of the object and represents changes to the contents of an object only, not its metadata. */
    @Attribute
    String gcsETag;

    /** Encryption algorithm used to encrypt this asset. */
    @Attribute
    String gcsEncryptionType;

    /** Version of metadata for this asset at this generation. Used for preconditions and detecting changes in metadata. A metageneration number is only meaningful in the context of a particular generation of a particular asset. */
    @Attribute
    Long gcsMetaGenerationId;

    /** CRC32C hash of this object. */
    @Attribute
    String gcsObjectCRC32CHash;

    /** Information about how this object's content should be presented. */
    @Attribute
    String gcsObjectContentDisposition;

    /** Content encoding of this object. */
    @Attribute
    String gcsObjectContentEncoding;

    /** Language of this object's contents. */
    @Attribute
    String gcsObjectContentLanguage;

    /** Type of content in this object. */
    @Attribute
    String gcsObjectContentType;

    /** Time (epoch) at which this object's data was last modified, in milliseconds. */
    @Attribute
    @Date
    Long gcsObjectDataLastModifiedTime;

    /** Generation ID of this object. */
    @Attribute
    Long gcsObjectGenerationId;

    /** Type of hold on this object. */
    @Attribute
    String gcsObjectHoldType;

    /** Key of this object, in GCS. */
    @Attribute
    String gcsObjectKey;

    /** MD5 hash of this object. */
    @Attribute
    String gcsObjectMD5Hash;

    /** Media link to this object. */
    @Attribute
    String gcsObjectMediaLink;

    /** Retention expiration date of this object. */
    @Attribute
    @Date
    Long gcsObjectRetentionExpirationDate;

    /** Object size in bytes. */
    @Attribute
    Long gcsObjectSize;

    /** Whether the requester pays header was sent when this asset was created (true) or not (false). */
    @Attribute
    Boolean gcsRequesterPays;

    /** Storage class of this asset. */
    @Attribute
    String gcsStorageClass;

    /** List of labels that have been applied to the asset in Google. */
    @Attribute
    @Singular
    List<GoogleLabel> googleLabels;

    /** Location of this asset in Google. */
    @Attribute
    String googleLocation;

    /** Type of location of this asset in Google. */
    @Attribute
    String googleLocationType;

    /** ID of the project in which the asset exists. */
    @Attribute
    String googleProjectId;

    /** Name of the project in which the asset exists. */
    @Attribute
    String googleProjectName;

    /** Number of the project in which the asset exists. */
    @Attribute
    Long googleProjectNumber;

    /** Service in Google in which the asset exists. */
    @Attribute
    String googleService;

    /** List of tags that have been applied to the asset in Google. */
    @Attribute
    @Singular
    List<GoogleTag> googleTags;

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
     * Builds the minimal object necessary to create a relationship to a GCSObject, from a potentially
     * more-complete GCSObject object.
     *
     * @return the minimal object necessary to relate to the GCSObject
     * @throws InvalidRequestException if any of the minimal set of required properties for a GCSObject relationship are not found in the initial object
     */
    @Override
    public GCSObject trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all GCSObject assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) GCSObject assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all GCSObject assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all GCSObject assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) GCSObjects will be included
     * @return a fluent search that includes all GCSObject assets
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
     * Reference to a GCSObject by GUID. Use this to create a relationship to this GCSObject,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the GCSObject to reference
     * @return reference to a GCSObject that can be used for defining a relationship to a GCSObject
     */
    public static GCSObject refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a GCSObject by GUID. Use this to create a relationship to this GCSObject,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the GCSObject to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a GCSObject that can be used for defining a relationship to a GCSObject
     */
    public static GCSObject refByGuid(String guid, Reference.SaveSemantic semantic) {
        return GCSObject._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a GCSObject by qualifiedName. Use this to create a relationship to this GCSObject,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the GCSObject to reference
     * @return reference to a GCSObject that can be used for defining a relationship to a GCSObject
     */
    public static GCSObject refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a GCSObject by qualifiedName. Use this to create a relationship to this GCSObject,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the GCSObject to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a GCSObject that can be used for defining a relationship to a GCSObject
     */
    public static GCSObject refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return GCSObject._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a GCSObject by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the GCSObject to retrieve, either its GUID or its full qualifiedName
     * @return the requested full GCSObject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GCSObject does not exist or the provided GUID is not a GCSObject
     */
    @JsonIgnore
    public static GCSObject get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a GCSObject by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the GCSObject to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full GCSObject, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GCSObject does not exist or the provided GUID is not a GCSObject
     */
    @JsonIgnore
    public static GCSObject get(AtlanClient client, String id, boolean includeAllRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof GCSObject) {
                return (GCSObject) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof GCSObject) {
                return (GCSObject) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a GCSObject by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the GCSObject to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the GCSObject, including any relationships
     * @return the requested GCSObject, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GCSObject does not exist or the provided GUID is not a GCSObject
     */
    @JsonIgnore
    public static GCSObject get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a GCSObject by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the GCSObject to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the GCSObject, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the GCSObject
     * @return the requested GCSObject, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GCSObject does not exist or the provided GUID is not a GCSObject
     */
    @JsonIgnore
    public static GCSObject get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = GCSObject.select(client)
                    .where(GCSObject.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof GCSObject) {
                return (GCSObject) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = GCSObject.select(client)
                    .where(GCSObject.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof GCSObject) {
                return (GCSObject) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) GCSObject to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the GCSObject
     * @return true if the GCSObject is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a GCSObject.
     *
     * @param name of the GCSObject
     * @param bucket in which the GCSObject should be created, which must have at least
     *               a qualifiedName
     * @return the minimal request necessary to create the GCSObject, as a builder
     * @throws InvalidRequestException if the bucket provided is without a qualifiedName
     */
    public static GCSObjectBuilder<?, ?> creator(String name, GCSBucket bucket) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", bucket.getConnectionQualifiedName());
        map.put("name", bucket.getName());
        map.put("qualifiedName", bucket.getQualifiedName());
        validateRelationship(GCSBucket.TYPE_NAME, map);
        return creator(name, bucket.getConnectionQualifiedName(), bucket.getName(), bucket.getQualifiedName())
                .gcsBucket(bucket.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a GCSObject.
     *
     * @param name of the GCSObject
     * @param bucketQualifiedName unique name of the bucket in which the GCSObject is contained
     * @return the minimal object necessary to create the GCSObject, as a builder
     */
    public static GCSObjectBuilder<?, ?> creator(String name, String bucketQualifiedName) {
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(bucketQualifiedName);
        String bucketName = StringUtils.getNameFromQualifiedName(bucketQualifiedName);
        return creator(name, connectionQualifiedName, bucketName, bucketQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a GCSObject.
     *
     * @param name of the GCSObject
     * @param connectionQualifiedName unique name of the connection in which the GCSObject should be created
     * @param bucketName simple name of the GCSBucket in which the GCSObject should be created
     * @param bucketQualifiedName unique name of the GCSBucket in which the GCSObject should be created
     * @return the minimal object necessary to create the GCSObject, as a builder
     */
    public static GCSObjectBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String bucketName, String bucketQualifiedName) {
        return GCSObject._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, bucketQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.GCS)
                .gcsBucketName(bucketName)
                .gcsBucketQualifiedName(bucketQualifiedName)
                .gcsBucket(GCSBucket.refByQualifiedName(bucketQualifiedName));
    }

    /**
     * Generate a unique GCSObject name.
     *
     * @param name of the GCSObject
     * @param bucketQualifiedName unique name of the bucket in which the GCSObject is contained
     * @return a unique name for the GCSObject
     */
    public static String generateQualifiedName(String name, String bucketQualifiedName) {
        return bucketQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a GCSObject.
     *
     * @param qualifiedName of the GCSObject
     * @param name of the GCSObject
     * @return the minimal request necessary to update the GCSObject, as a builder
     */
    public static GCSObjectBuilder<?, ?> updater(String qualifiedName, String name) {
        return GCSObject._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a GCSObject, from a potentially
     * more-complete GCSObject object.
     *
     * @return the minimal object necessary to update the GCSObject, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for GCSObject are not found in the initial object
     */
    @Override
    public GCSObjectBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a GCSObject.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the GCSObject
     * @param name of the GCSObject
     * @return the updated GCSObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GCSObject removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (GCSObject) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a GCSObject.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the GCSObject
     * @param name of the GCSObject
     * @return the updated GCSObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GCSObject removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (GCSObject) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a GCSObject.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GCSObject's owners
     * @param qualifiedName of the GCSObject
     * @param name of the GCSObject
     * @return the updated GCSObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GCSObject removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (GCSObject) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a GCSObject.
     *
     * @param client connectivity to the Atlan tenant on which to update the GCSObject's certificate
     * @param qualifiedName of the GCSObject
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated GCSObject, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GCSObject updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (GCSObject) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a GCSObject.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GCSObject's certificate
     * @param qualifiedName of the GCSObject
     * @param name of the GCSObject
     * @return the updated GCSObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GCSObject removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (GCSObject) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a GCSObject.
     *
     * @param client connectivity to the Atlan tenant on which to update the GCSObject's announcement
     * @param qualifiedName of the GCSObject
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GCSObject updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (GCSObject)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a GCSObject.
     *
     * @param client connectivity to the Atlan client from which to remove the GCSObject's announcement
     * @param qualifiedName of the GCSObject
     * @param name of the GCSObject
     * @return the updated GCSObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GCSObject removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (GCSObject) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the GCSObject.
     *
     * @param client connectivity to the Atlan tenant on which to replace the GCSObject's assigned terms
     * @param qualifiedName for the GCSObject
     * @param name human-readable name of the GCSObject
     * @param terms the list of terms to replace on the GCSObject, or null to remove all terms from the GCSObject
     * @return the GCSObject that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static GCSObject replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (GCSObject) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the GCSObject, without replacing existing terms linked to the GCSObject.
     * Note: this operation must make two API calls — one to retrieve the GCSObject's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the GCSObject
     * @param qualifiedName for the GCSObject
     * @param terms the list of terms to append to the GCSObject
     * @return the GCSObject that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static GCSObject appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (GCSObject) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a GCSObject, without replacing all existing terms linked to the GCSObject.
     * Note: this operation must make two API calls — one to retrieve the GCSObject's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the GCSObject
     * @param qualifiedName for the GCSObject
     * @param terms the list of terms to remove from the GCSObject, which must be referenced by GUID
     * @return the GCSObject that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static GCSObject removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (GCSObject) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a GCSObject, without replacing existing Atlan tags linked to the GCSObject.
     * Note: this operation must make two API calls — one to retrieve the GCSObject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the GCSObject
     * @param qualifiedName of the GCSObject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated GCSObject
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static GCSObject appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (GCSObject) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a GCSObject, without replacing existing Atlan tags linked to the GCSObject.
     * Note: this operation must make two API calls — one to retrieve the GCSObject's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the GCSObject
     * @param qualifiedName of the GCSObject
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated GCSObject
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static GCSObject appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (GCSObject) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a GCSObject.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a GCSObject
     * @param qualifiedName of the GCSObject
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the GCSObject
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
