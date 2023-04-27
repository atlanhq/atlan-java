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
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Metabase dashboard in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class MetabaseDashboard extends Metabase {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MetabaseDashboard";

    /** Fixed typeName for MetabaseDashboards. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    Long metabaseQuestionCount;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<MetabaseQuestion> metabaseQuestions;

    /** TBC */
    @Attribute
    MetabaseCollection metabaseCollection;

    /**
     * Reference to a MetabaseDashboard by GUID.
     *
     * @param guid the GUID of the MetabaseDashboard to reference
     * @return reference to a MetabaseDashboard that can be used for defining a relationship to a MetabaseDashboard
     */
    public static MetabaseDashboard refByGuid(String guid) {
        return MetabaseDashboard.builder().guid(guid).build();
    }

    /**
     * Reference to a MetabaseDashboard by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the MetabaseDashboard to reference
     * @return reference to a MetabaseDashboard that can be used for defining a relationship to a MetabaseDashboard
     */
    public static MetabaseDashboard refByQualifiedName(String qualifiedName) {
        return MetabaseDashboard.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a MetabaseDashboard by its GUID, complete with all of its relationships.
     *
     * @param guid of the MetabaseDashboard to retrieve
     * @return the requested full MetabaseDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MetabaseDashboard does not exist or the provided GUID is not a MetabaseDashboard
     */
    public static MetabaseDashboard retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof MetabaseDashboard) {
            return (MetabaseDashboard) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "MetabaseDashboard");
        }
    }

    /**
     * Retrieves a MetabaseDashboard by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the MetabaseDashboard to retrieve
     * @return the requested full MetabaseDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MetabaseDashboard does not exist
     */
    public static MetabaseDashboard retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof MetabaseDashboard) {
            return (MetabaseDashboard) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "MetabaseDashboard");
        }
    }

    /**
     * Restore the archived (soft-deleted) MetabaseDashboard to active.
     *
     * @param qualifiedName for the MetabaseDashboard
     * @return true if the MetabaseDashboard is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param name of the MetabaseDashboard
     * @return the minimal request necessary to update the MetabaseDashboard, as a builder
     */
    public static MetabaseDashboardBuilder<?, ?> updater(String qualifiedName, String name) {
        return MetabaseDashboard.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a MetabaseDashboard, from a potentially
     * more-complete MetabaseDashboard object.
     *
     * @return the minimal object necessary to update the MetabaseDashboard, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for MetabaseDashboard are not found in the initial object
     */
    @Override
    public MetabaseDashboardBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "MetabaseDashboard", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param name of the MetabaseDashboard
     * @return the updated MetabaseDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard removeDescription(String qualifiedName, String name) throws AtlanException {
        return (MetabaseDashboard) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param name of the MetabaseDashboard
     * @return the updated MetabaseDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (MetabaseDashboard) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param name of the MetabaseDashboard
     * @return the updated MetabaseDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard removeOwners(String qualifiedName, String name) throws AtlanException {
        return (MetabaseDashboard) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MetabaseDashboard, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return (MetabaseDashboard) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param name of the MetabaseDashboard
     * @return the updated MetabaseDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (MetabaseDashboard) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (MetabaseDashboard) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param name of the MetabaseDashboard
     * @return the updated MetabaseDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (MetabaseDashboard) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the MetabaseDashboard.
     *
     * @param qualifiedName for the MetabaseDashboard
     * @param name human-readable name of the MetabaseDashboard
     * @param terms the list of terms to replace on the MetabaseDashboard, or null to remove all terms from the MetabaseDashboard
     * @return the MetabaseDashboard that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (MetabaseDashboard) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MetabaseDashboard, without replacing existing terms linked to the MetabaseDashboard.
     * Note: this operation must make two API calls — one to retrieve the MetabaseDashboard's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the MetabaseDashboard
     * @param terms the list of terms to append to the MetabaseDashboard
     * @return the MetabaseDashboard that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (MetabaseDashboard) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MetabaseDashboard, without replacing all existing terms linked to the MetabaseDashboard.
     * Note: this operation must make two API calls — one to retrieve the MetabaseDashboard's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the MetabaseDashboard
     * @param terms the list of terms to remove from the MetabaseDashboard, which must be referenced by GUID
     * @return the MetabaseDashboard that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (MetabaseDashboard) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add classifications to a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the MetabaseDashboard
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the MetabaseDashboard
     */
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
     * Remove a classification from a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the MetabaseDashboard
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }
}
