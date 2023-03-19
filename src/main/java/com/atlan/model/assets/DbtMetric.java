/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.*;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a dbt metric in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class DbtMetric extends Metric {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DbtMetric";

    /** Fixed typeName for DbtMetrics. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    List<DbtMetricFilter> dbtMetricFilters;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<Column> dbtMetricFilterColumns;

    /** TBC */
    @Attribute
    DbtModel dbtModel;

    /**
     * Reference to a DbtMetric by GUID.
     *
     * @param guid the GUID of the DbtMetric to reference
     * @return reference to a DbtMetric that can be used for defining a relationship to a DbtMetric
     */
    public static DbtMetric refByGuid(String guid) {
        return DbtMetric.builder().guid(guid).build();
    }

    /**
     * Reference to a DbtMetric by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the DbtMetric to reference
     * @return reference to a DbtMetric that can be used for defining a relationship to a DbtMetric
     */
    public static DbtMetric refByQualifiedName(String qualifiedName) {
        return DbtMetric.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param name of the DbtMetric
     * @return the minimal request necessary to update the DbtMetric, as a builder
     */
    public static DbtMetricBuilder<?, ?> updater(String qualifiedName, String name) {
        return DbtMetric.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DbtMetric, from a potentially
     * more-complete DbtMetric object.
     *
     * @return the minimal object necessary to update the DbtMetric, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DbtMetric are not found in the initial object
     */
    @Override
    public DbtMetricBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "DbtMetric", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a DbtMetric by its GUID, complete with all of its relationships.
     *
     * @param guid of the DbtMetric to retrieve
     * @return the requested full DbtMetric, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtMetric does not exist or the provided GUID is not a DbtMetric
     */
    public static DbtMetric retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof DbtMetric) {
            return (DbtMetric) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "DbtMetric");
        }
    }

    /**
     * Retrieves a DbtMetric by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the DbtMetric to retrieve
     * @return the requested full DbtMetric, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtMetric does not exist
     */
    public static DbtMetric retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof DbtMetric) {
            return (DbtMetric) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "DbtMetric");
        }
    }

    /**
     * Restore the archived (soft-deleted) DbtMetric to active.
     *
     * @param qualifiedName for the DbtMetric
     * @return true if the DbtMetric is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param name of the DbtMetric
     * @return the updated DbtMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtMetric removeDescription(String qualifiedName, String name) throws AtlanException {
        return (DbtMetric) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param name of the DbtMetric
     * @return the updated DbtMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtMetric removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (DbtMetric) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param name of the DbtMetric
     * @return the updated DbtMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtMetric removeOwners(String qualifiedName, String name) throws AtlanException {
        return (DbtMetric) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DbtMetric, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtMetric updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (DbtMetric) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param name of the DbtMetric
     * @return the updated DbtMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtMetric removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (DbtMetric) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtMetric updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (DbtMetric) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param name of the DbtMetric
     * @return the updated DbtMetric, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtMetric removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (DbtMetric) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Add classifications to a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the DbtMetric
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a DbtMetric.
     *
     * @param qualifiedName of the DbtMetric
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the DbtMetric
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the DbtMetric.
     *
     * @param qualifiedName for the DbtMetric
     * @param name human-readable name of the DbtMetric
     * @param terms the list of terms to replace on the DbtMetric, or null to remove all terms from the DbtMetric
     * @return the DbtMetric that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DbtMetric replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (DbtMetric) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DbtMetric, without replacing existing terms linked to the DbtMetric.
     * Note: this operation must make two API calls — one to retrieve the DbtMetric's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the DbtMetric
     * @param terms the list of terms to append to the DbtMetric
     * @return the DbtMetric that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DbtMetric appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (DbtMetric) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DbtMetric, without replacing all existing terms linked to the DbtMetric.
     * Note: this operation must make two API calls — one to retrieve the DbtMetric's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the DbtMetric
     * @param terms the list of terms to remove from the DbtMetric, which must be referenced by GUID
     * @return the DbtMetric that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DbtMetric removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (DbtMetric) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
