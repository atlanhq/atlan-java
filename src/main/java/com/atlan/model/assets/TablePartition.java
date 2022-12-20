/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("cast")
public class TablePartition extends SQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TablePartition";

    /** Fixed typeName for TablePartitions. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String constraint;

    /** TBC */
    @Attribute
    Long columnCount;

    /** TBC */
    @Attribute
    Long rowCount;

    /** TBC */
    @Attribute
    Long sizeBytes;

    /** TBC */
    @Attribute
    String alias;

    /** TBC */
    @Attribute
    Boolean isTemporary;

    /** TBC */
    @Attribute
    Boolean isQueryPreview;

    /** TBC */
    @Attribute
    @Singular("putQueryPreviewConfig")
    Map<String, String> queryPreviewConfig;

    /** TBC */
    @Attribute
    String externalLocation;

    /** TBC */
    @Attribute
    String externalLocationRegion;

    /** TBC */
    @Attribute
    String externalLocationFormat;

    /** TBC */
    @Attribute
    Boolean isPartitioned;

    /** TBC */
    @Attribute
    String partitionStrategy;

    /** TBC */
    @Attribute
    Long partitionCount;

    /** TBC */
    @Attribute
    String partitionList;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<Column> columns;

    /** TBC */
    @Attribute
    Table parentTable;

    /**
     * Reference to a TablePartition by GUID.
     *
     * @param guid the GUID of the TablePartition to reference
     * @return reference to a TablePartition that can be used for defining a relationship to a TablePartition
     */
    public static TablePartition refByGuid(String guid) {
        return TablePartition.builder().guid(guid).build();
    }

    /**
     * Reference to a TablePartition by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the TablePartition to reference
     * @return reference to a TablePartition that can be used for defining a relationship to a TablePartition
     */
    public static TablePartition refByQualifiedName(String qualifiedName) {
        return TablePartition.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a TablePartition.
     *
     * @param qualifiedName of the TablePartition
     * @param name of the TablePartition
     * @return the minimal request necessary to update the TablePartition, as a builder
     */
    public static TablePartitionBuilder<?, ?> updater(String qualifiedName, String name) {
        return TablePartition.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TablePartition, from a potentially
     * more-complete TablePartition object.
     *
     * @return the minimal object necessary to update the TablePartition, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TablePartition are not found in the initial object
     */
    @Override
    public TablePartitionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    "Required field for updating TablePartition is missing.",
                    String.join(",", missing),
                    "ATLAN-JAVA-CLIENT-400-404",
                    400,
                    null);
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a TablePartition by its GUID, complete with all of its relationships.
     *
     * @param guid of the TablePartition to retrieve
     * @return the requested full TablePartition, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TablePartition does not exist or the provided GUID is not a TablePartition
     */
    public static TablePartition retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException("No asset found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (asset instanceof TablePartition) {
            return (TablePartition) asset;
        } else {
            throw new NotFoundException(
                    "Asset with GUID " + guid + " is not a TablePartition.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a TablePartition by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the TablePartition to retrieve
     * @return the requested full TablePartition, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TablePartition does not exist
     */
    public static TablePartition retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof TablePartition) {
            return (TablePartition) asset;
        } else {
            throw new NotFoundException(
                    "No TablePartition found with qualifiedName: " + qualifiedName,
                    "ATLAN_JAVA_CLIENT-404-003",
                    404,
                    null);
        }
    }

    /**
     * Restore the archived (soft-deleted) TablePartition to active.
     *
     * @param qualifiedName for the TablePartition
     * @return true if the TablePartition is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a TablePartition.
     *
     * @param qualifiedName of the TablePartition
     * @param name of the TablePartition
     * @return the updated TablePartition, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition removeDescription(String qualifiedName, String name) throws AtlanException {
        return (TablePartition)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a TablePartition.
     *
     * @param qualifiedName of the TablePartition
     * @param name of the TablePartition
     * @return the updated TablePartition, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (TablePartition) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the owners from a TablePartition.
     *
     * @param qualifiedName of the TablePartition
     * @param name of the TablePartition
     * @return the updated TablePartition, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition removeOwners(String qualifiedName, String name) throws AtlanException {
        return (TablePartition)
                Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the certificate on a TablePartition.
     *
     * @param qualifiedName of the TablePartition
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TablePartition, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (TablePartition) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a TablePartition.
     *
     * @param qualifiedName of the TablePartition
     * @param name of the TablePartition
     * @return the updated TablePartition, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (TablePartition)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a TablePartition.
     *
     * @param qualifiedName of the TablePartition
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (TablePartition) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a TablePartition.
     *
     * @param qualifiedName of the TablePartition
     * @param name of the TablePartition
     * @return the updated TablePartition, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TablePartition removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (TablePartition)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a TablePartition.
     *
     * @param qualifiedName of the TablePartition
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the TablePartition
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a TablePartition.
     *
     * @param qualifiedName of the TablePartition
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the TablePartition
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the TablePartition.
     *
     * @param qualifiedName for the TablePartition
     * @param name human-readable name of the TablePartition
     * @param terms the list of terms to replace on the TablePartition, or null to remove all terms from the TablePartition
     * @return the TablePartition that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TablePartition replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (TablePartition) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the TablePartition, without replacing existing terms linked to the TablePartition.
     * Note: this operation must make two API calls — one to retrieve the TablePartition's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the TablePartition
     * @param terms the list of terms to append to the TablePartition
     * @return the TablePartition that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static TablePartition appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (TablePartition) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a TablePartition, without replacing all existing terms linked to the TablePartition.
     * Note: this operation must make two API calls — one to retrieve the TablePartition's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the TablePartition
     * @param terms the list of terms to remove from the TablePartition, which must be referenced by GUID
     * @return the TablePartition that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static TablePartition removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (TablePartition) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
