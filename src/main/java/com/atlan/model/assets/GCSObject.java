/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.StringUtils;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class GCSObject extends GCS {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "GCSObject";

    /** Fixed typeName for GCSObjects. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String gcsBucketName;

    /** TBC */
    @Attribute
    String gcsBucketQualifiedName;

    /** TBC */
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

    /** TBC */
    @Attribute
    String gcsObjectContentType;

    /** TBC */
    @Attribute
    String gcsObjectContentEncoding;

    /** TBC */
    @Attribute
    String gcsObjectContentDisposition;

    /** TBC */
    @Attribute
    String gcsObjectContentLanguage;

    /** TBC */
    @Attribute
    Long gcsObjectRetentionExpirationDate;

    /** TBC */
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
     * Builds the minimal object necessary to create a GCSObject.
     *
     * @param name of the GCSObject
     * @param bucketQualifiedName unique name of the bucket in which the GCSObject is contained
     * @return the minimal object necessary to create the GCSObject, as a builder
     */
    public static GCSObjectBuilder<?, ?> creator(String name, String bucketQualifiedName) {
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(bucketQualifiedName);
        String bucketName = StringUtils.getNameFromQualifiedName(bucketQualifiedName);
        return GCSObject.builder()
                .qualifiedName(bucketQualifiedName + "/" + name)
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.GCS)
                .gcsBucketName(bucketName)
                .gcsBucketQualifiedName(bucketQualifiedName)
                .gcsBucket(GCSBucket.refByQualifiedName(bucketQualifiedName));
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
     */
    @Override
    protected GCSObjectBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a GCSObject by its GUID, complete with all of its relationships.
     *
     * @param guid of the GCSObject to retrieve
     * @return the requested full GCSObject, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GCSObject does not exist or the provided GUID is not a GCSObject
     */
    public static GCSObject retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof GCSObject) {
            return (GCSObject) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a GCSObject.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
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
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof GCSObject) {
            return (GCSObject) entity;
        } else {
            throw new NotFoundException(
                    "No GCSObject found with qualifiedName: " + qualifiedName, "ATLAN_JAVA_CLIENT-404-003", 404, null);
        }
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
    public static GCSObject updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
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
        return (GCSObject)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
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
        return (GCSObject)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a GCSObject.
     *
     * @param qualifiedName of the GCSObject
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the GCSObject
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
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
}
