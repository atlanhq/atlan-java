/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Google Cloud Storage object in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class GCSObject extends GCS {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "GCSObject";

    /** Fixed typeName for GCSObjects. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Human-readable name of the bucket in which this object exists. */
    @Attribute
    String gcsBucketName;

    /** qualifiedName of the bucket in which this object exists. */
    @Attribute
    String gcsBucketQualifiedName;

    /** Object size in bytes. */
    @Attribute
    Long gcsObjectSize;

    /** TBC */
    @Attribute
    String gcsObjectKey;

    /** TBC */
    @Attribute
    String gcsObjectMediaLink;

    /** TBC */
    @Attribute
    String gcsObjectHoldType;

    /** TBC */
    @Attribute
    Long gcsObjectGenerationId;

    /** TBC */
    @Attribute
    String gcsObjectCRC32CHash;

    /** TBC */
    @Attribute
    String gcsObjectMD5Hash;

    /** TBC */
    @Attribute
    Long gcsObjectDataLastModifiedTime;

    /** Type of content in the object. */
    @Attribute
    String gcsObjectContentType;

    /** TBC */
    @Attribute
    String gcsObjectContentEncoding;

    /** Information about how the object's content should be presented. */
    @Attribute
    String gcsObjectContentDisposition;

    /** TBC */
    @Attribute
    String gcsObjectContentLanguage;

    /** TBC */
    @Attribute
    Long gcsObjectRetentionExpirationDate;

    /** GCS bucket in which the object exists. */
    @Attribute
    GCSBucket gcsBucket;

    /**
     * Reference to a GCSObject by GUID.
     *
     * @param guid the GUID of the GCSObject to reference
     * @return reference to a GCSObject that can be used for defining a relationship to a GCSObject
     */
    public static GCSObject refByGuid(String guid) {
        return GCSObject.builder().guid(guid).build();
    }

    /**
     * Reference to a GCSObject by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the GCSObject to reference
     * @return reference to a GCSObject that can be used for defining a relationship to a GCSObject
     */
    public static GCSObject refByQualifiedName(String qualifiedName) {
        return GCSObject.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a GCSObject by its GUID, complete with all of its relationships.
     *
     * @param guid of the GCSObject to retrieve
     * @return the requested full GCSObject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GCSObject does not exist or the provided GUID is not a GCSObject
     */
    public static GCSObject retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof GCSObject) {
            return (GCSObject) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "GCSObject");
        }
    }

    /**
     * Retrieves a GCSObject by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the GCSObject to retrieve
     * @return the requested full GCSObject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GCSObject does not exist
     */
    public static GCSObject retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof GCSObject) {
            return (GCSObject) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "GCSObject");
        }
    }

    /**
     * Restore the archived (soft-deleted) GCSObject to active.
     *
     * @param qualifiedName for the GCSObject
     * @return true if the GCSObject is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
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
        return GCSObject.builder()
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
        return GCSObject.builder().qualifiedName(qualifiedName).name(name);
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
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "GCSObject", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a GCSObject.
     *
     * @param qualifiedName of the GCSObject
     * @param name of the GCSObject
     * @return the updated GCSObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GCSObject removeDescription(String qualifiedName, String name) throws AtlanException {
        return (GCSObject) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a GCSObject.
     *
     * @param qualifiedName of the GCSObject
     * @param name of the GCSObject
     * @return the updated GCSObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GCSObject removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (GCSObject) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a GCSObject.
     *
     * @param qualifiedName of the GCSObject
     * @param name of the GCSObject
     * @return the updated GCSObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GCSObject removeOwners(String qualifiedName, String name) throws AtlanException {
        return (GCSObject) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a GCSObject.
     *
     * @param qualifiedName of the GCSObject
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated GCSObject, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GCSObject updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (GCSObject) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a GCSObject.
     *
     * @param qualifiedName of the GCSObject
     * @param name of the GCSObject
     * @return the updated GCSObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GCSObject removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (GCSObject) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a GCSObject.
     *
     * @param qualifiedName of the GCSObject
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GCSObject updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (GCSObject) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a GCSObject.
     *
     * @param qualifiedName of the GCSObject
     * @param name of the GCSObject
     * @return the updated GCSObject, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GCSObject removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (GCSObject) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the GCSObject.
     *
     * @param qualifiedName for the GCSObject
     * @param name human-readable name of the GCSObject
     * @param terms the list of terms to replace on the GCSObject, or null to remove all terms from the GCSObject
     * @return the GCSObject that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static GCSObject replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (GCSObject) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the GCSObject, without replacing existing terms linked to the GCSObject.
     * Note: this operation must make two API calls — one to retrieve the GCSObject's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the GCSObject
     * @param terms the list of terms to append to the GCSObject
     * @return the GCSObject that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static GCSObject appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (GCSObject) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a GCSObject, without replacing all existing terms linked to the GCSObject.
     * Note: this operation must make two API calls — one to retrieve the GCSObject's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the GCSObject
     * @param terms the list of terms to remove from the GCSObject, which must be referenced by GUID
     * @return the GCSObject that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static GCSObject removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (GCSObject) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add classifications to a GCSObject, without replacing existing classifications linked to the GCSObject.
     * Note: this operation must make two API calls — one to retrieve the GCSObject's existing classifications,
     * and a second to append the new classifications.
     *
     * @param qualifiedName of the GCSObject
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems
     * @return the updated GCSObject
     */
    public static GCSObject appendClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        return (GCSObject) Asset.appendClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a GCSObject, without replacing existing classifications linked to the GCSObject.
     * Note: this operation must make two API calls — one to retrieve the GCSObject's existing classifications,
     * and a second to append the new classifications.
     *
     * @param qualifiedName of the GCSObject
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated GCSObject
     */
    public static GCSObject appendClassifications(
            String qualifiedName,
            List<String> classificationNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (GCSObject) Asset.appendClassifications(
                TYPE_NAME,
                qualifiedName,
                classificationNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add classifications to a GCSObject.
     *
     * @param qualifiedName of the GCSObject
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the GCSObject
     * @deprecated see {@link #appendClassifications(String, List)} instead
     */
    @Deprecated
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a GCSObject.
     *
     * @param qualifiedName of the GCSObject
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the GCSObject
     * @deprecated see {@link #appendClassifications(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    public static void addClassifications(
            String qualifiedName,
            List<String> classificationNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addClassifications(
                TYPE_NAME,
                qualifiedName,
                classificationNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove a classification from a GCSObject.
     *
     * @param qualifiedName of the GCSObject
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the GCSObject
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }
}
