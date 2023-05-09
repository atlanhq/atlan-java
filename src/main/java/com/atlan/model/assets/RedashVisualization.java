/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Redash visualization in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class RedashVisualization extends Redash {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "RedashVisualization";

    /** Fixed typeName for RedashVisualizations. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Type of the Redash visualization. */
    @Attribute
    String redashVisualizationType;

    /** Name of the query from which the visualization was created. */
    @Attribute
    String redashQueryName;

    /** Unique name of the query from which the visualization was created. */
    @Attribute
    String redashQueryQualifiedName;

    /** Query from which the visualization was created. */
    @Attribute
    RedashQuery redashQuery;

    /**
     * Reference to a RedashVisualization by GUID.
     *
     * @param guid the GUID of the RedashVisualization to reference
     * @return reference to a RedashVisualization that can be used for defining a relationship to a RedashVisualization
     */
    public static RedashVisualization refByGuid(String guid) {
        return RedashVisualization.builder().guid(guid).build();
    }

    /**
     * Reference to a RedashVisualization by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the RedashVisualization to reference
     * @return reference to a RedashVisualization that can be used for defining a relationship to a RedashVisualization
     */
    public static RedashVisualization refByQualifiedName(String qualifiedName) {
        return RedashVisualization.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a RedashVisualization by its GUID, complete with all of its relationships.
     *
     * @param guid of the RedashVisualization to retrieve
     * @return the requested full RedashVisualization, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the RedashVisualization does not exist or the provided GUID is not a RedashVisualization
     */
    public static RedashVisualization retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof RedashVisualization) {
            return (RedashVisualization) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "RedashVisualization");
        }
    }

    /**
     * Retrieves a RedashVisualization by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the RedashVisualization to retrieve
     * @return the requested full RedashVisualization, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the RedashVisualization does not exist
     */
    public static RedashVisualization retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof RedashVisualization) {
            return (RedashVisualization) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "RedashVisualization");
        }
    }

    /**
     * Restore the archived (soft-deleted) RedashVisualization to active.
     *
     * @param qualifiedName for the RedashVisualization
     * @return true if the RedashVisualization is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a RedashVisualization.
     *
     * @param qualifiedName of the RedashVisualization
     * @param name of the RedashVisualization
     * @return the minimal request necessary to update the RedashVisualization, as a builder
     */
    public static RedashVisualizationBuilder<?, ?> updater(String qualifiedName, String name) {
        return RedashVisualization.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a RedashVisualization, from a potentially
     * more-complete RedashVisualization object.
     *
     * @return the minimal object necessary to update the RedashVisualization, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for RedashVisualization are not found in the initial object
     */
    @Override
    public RedashVisualizationBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "RedashVisualization", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a RedashVisualization.
     *
     * @param qualifiedName of the RedashVisualization
     * @param name of the RedashVisualization
     * @return the updated RedashVisualization, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashVisualization removeDescription(String qualifiedName, String name) throws AtlanException {
        return (RedashVisualization) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a RedashVisualization.
     *
     * @param qualifiedName of the RedashVisualization
     * @param name of the RedashVisualization
     * @return the updated RedashVisualization, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashVisualization removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (RedashVisualization) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a RedashVisualization.
     *
     * @param qualifiedName of the RedashVisualization
     * @param name of the RedashVisualization
     * @return the updated RedashVisualization, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashVisualization removeOwners(String qualifiedName, String name) throws AtlanException {
        return (RedashVisualization) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a RedashVisualization.
     *
     * @param qualifiedName of the RedashVisualization
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated RedashVisualization, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static RedashVisualization updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return (RedashVisualization) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a RedashVisualization.
     *
     * @param qualifiedName of the RedashVisualization
     * @param name of the RedashVisualization
     * @return the updated RedashVisualization, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashVisualization removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (RedashVisualization) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a RedashVisualization.
     *
     * @param qualifiedName of the RedashVisualization
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static RedashVisualization updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (RedashVisualization)
                Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a RedashVisualization.
     *
     * @param qualifiedName of the RedashVisualization
     * @param name of the RedashVisualization
     * @return the updated RedashVisualization, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static RedashVisualization removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (RedashVisualization) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the RedashVisualization.
     *
     * @param qualifiedName for the RedashVisualization
     * @param name human-readable name of the RedashVisualization
     * @param terms the list of terms to replace on the RedashVisualization, or null to remove all terms from the RedashVisualization
     * @return the RedashVisualization that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static RedashVisualization replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (RedashVisualization) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the RedashVisualization, without replacing existing terms linked to the RedashVisualization.
     * Note: this operation must make two API calls — one to retrieve the RedashVisualization's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the RedashVisualization
     * @param terms the list of terms to append to the RedashVisualization
     * @return the RedashVisualization that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static RedashVisualization appendTerms(String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        return (RedashVisualization) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a RedashVisualization, without replacing all existing terms linked to the RedashVisualization.
     * Note: this operation must make two API calls — one to retrieve the RedashVisualization's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the RedashVisualization
     * @param terms the list of terms to remove from the RedashVisualization, which must be referenced by GUID
     * @return the RedashVisualization that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static RedashVisualization removeTerms(String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        return (RedashVisualization) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add classifications to a RedashVisualization, without replacing existing classifications linked to the RedashVisualization.
     * Note: this operation must make two API calls — one to retrieve the RedashVisualization's existing classifications,
     * and a second to append the new classifications.
     *
     * @param qualifiedName of the RedashVisualization
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems
     * @return the updated RedashVisualization
     */
    public static RedashVisualization appendClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        return (RedashVisualization) Asset.appendClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a RedashVisualization, without replacing existing classifications linked to the RedashVisualization.
     * Note: this operation must make two API calls — one to retrieve the RedashVisualization's existing classifications,
     * and a second to append the new classifications.
     *
     * @param qualifiedName of the RedashVisualization
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated RedashVisualization
     */
    public static RedashVisualization appendClassifications(
            String qualifiedName,
            List<String> classificationNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (RedashVisualization) Asset.appendClassifications(
                TYPE_NAME,
                qualifiedName,
                classificationNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add classifications to a RedashVisualization.
     *
     * @param qualifiedName of the RedashVisualization
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the RedashVisualization
     * @deprecated see {@link #appendClassifications(String, List)} instead
     */
    @Deprecated
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a RedashVisualization.
     *
     * @param qualifiedName of the RedashVisualization
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the RedashVisualization
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
     * Remove a classification from a RedashVisualization.
     *
     * @param qualifiedName of the RedashVisualization
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the RedashVisualization
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }
}
