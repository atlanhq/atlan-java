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
import java.util.Map;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Tableau flow in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class TableauFlow extends Tableau {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TableauFlow";

    /** Fixed typeName for TableauFlows. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String siteQualifiedName;

    /** TBC */
    @Attribute
    String projectQualifiedName;

    /** TBC */
    @Attribute
    String topLevelProjectQualifiedName;

    /** TBC */
    @Attribute
    @Singular("addProjectHierarchy")
    List<Map<String, String>> projectHierarchy;

    /** TBC */
    @Attribute
    @Singular
    List<Map<String, String>> inputFields;

    /** TBC */
    @Attribute
    @Singular
    List<Map<String, String>> outputFields;

    /** TBC */
    @Attribute
    @Singular
    List<Map<String, String>> outputSteps;

    /** TBC */
    @Attribute
    TableauProject project;

    /**
     * Reference to a TableauFlow by GUID.
     *
     * @param guid the GUID of the TableauFlow to reference
     * @return reference to a TableauFlow that can be used for defining a relationship to a TableauFlow
     */
    public static TableauFlow refByGuid(String guid) {
        return TableauFlow.builder().guid(guid).build();
    }

    /**
     * Reference to a TableauFlow by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the TableauFlow to reference
     * @return reference to a TableauFlow that can be used for defining a relationship to a TableauFlow
     */
    public static TableauFlow refByQualifiedName(String qualifiedName) {
        return TableauFlow.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a TableauFlow by its GUID, complete with all of its relationships.
     *
     * @param guid of the TableauFlow to retrieve
     * @return the requested full TableauFlow, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauFlow does not exist or the provided GUID is not a TableauFlow
     */
    public static TableauFlow retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof TableauFlow) {
            return (TableauFlow) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "TableauFlow");
        }
    }

    /**
     * Retrieves a TableauFlow by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the TableauFlow to retrieve
     * @return the requested full TableauFlow, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauFlow does not exist
     */
    public static TableauFlow retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof TableauFlow) {
            return (TableauFlow) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "TableauFlow");
        }
    }

    /**
     * Restore the archived (soft-deleted) TableauFlow to active.
     *
     * @param qualifiedName for the TableauFlow
     * @return true if the TableauFlow is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a TableauFlow.
     *
     * @param qualifiedName of the TableauFlow
     * @param name of the TableauFlow
     * @return the minimal request necessary to update the TableauFlow, as a builder
     */
    public static TableauFlowBuilder<?, ?> updater(String qualifiedName, String name) {
        return TableauFlow.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TableauFlow, from a potentially
     * more-complete TableauFlow object.
     *
     * @return the minimal object necessary to update the TableauFlow, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TableauFlow are not found in the initial object
     */
    @Override
    public TableauFlowBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "TableauFlow", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a TableauFlow.
     *
     * @param qualifiedName of the TableauFlow
     * @param name of the TableauFlow
     * @return the updated TableauFlow, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauFlow removeDescription(String qualifiedName, String name) throws AtlanException {
        return (TableauFlow) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a TableauFlow.
     *
     * @param qualifiedName of the TableauFlow
     * @param name of the TableauFlow
     * @return the updated TableauFlow, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauFlow removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (TableauFlow) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a TableauFlow.
     *
     * @param qualifiedName of the TableauFlow
     * @param name of the TableauFlow
     * @return the updated TableauFlow, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauFlow removeOwners(String qualifiedName, String name) throws AtlanException {
        return (TableauFlow) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a TableauFlow.
     *
     * @param qualifiedName of the TableauFlow
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TableauFlow, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauFlow updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (TableauFlow) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a TableauFlow.
     *
     * @param qualifiedName of the TableauFlow
     * @param name of the TableauFlow
     * @return the updated TableauFlow, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauFlow removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (TableauFlow) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a TableauFlow.
     *
     * @param qualifiedName of the TableauFlow
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauFlow updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (TableauFlow) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a TableauFlow.
     *
     * @param qualifiedName of the TableauFlow
     * @param name of the TableauFlow
     * @return the updated TableauFlow, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauFlow removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (TableauFlow) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the TableauFlow.
     *
     * @param qualifiedName for the TableauFlow
     * @param name human-readable name of the TableauFlow
     * @param terms the list of terms to replace on the TableauFlow, or null to remove all terms from the TableauFlow
     * @return the TableauFlow that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TableauFlow replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (TableauFlow) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the TableauFlow, without replacing existing terms linked to the TableauFlow.
     * Note: this operation must make two API calls — one to retrieve the TableauFlow's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the TableauFlow
     * @param terms the list of terms to append to the TableauFlow
     * @return the TableauFlow that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static TableauFlow appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (TableauFlow) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a TableauFlow, without replacing all existing terms linked to the TableauFlow.
     * Note: this operation must make two API calls — one to retrieve the TableauFlow's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the TableauFlow
     * @param terms the list of terms to remove from the TableauFlow, which must be referenced by GUID
     * @return the TableauFlow that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static TableauFlow removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (TableauFlow) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add classifications to a TableauFlow, without replacing existing classifications linked to the TableauFlow.
     * Note: this operation must make two API calls — one to retrieve the TableauFlow's existing classifications,
     * and a second to append the new classifications.
     *
     * @param qualifiedName of the TableauFlow
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems
     * @return the updated TableauFlow
     */
    public static TableauFlow appendClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        return (TableauFlow) Asset.appendClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a TableauFlow, without replacing existing classifications linked to the TableauFlow.
     * Note: this operation must make two API calls — one to retrieve the TableauFlow's existing classifications,
     * and a second to append the new classifications.
     *
     * @param qualifiedName of the TableauFlow
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated TableauFlow
     */
    public static TableauFlow appendClassifications(
            String qualifiedName,
            List<String> classificationNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (TableauFlow) Asset.appendClassifications(
                TYPE_NAME,
                qualifiedName,
                classificationNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add classifications to a TableauFlow.
     *
     * @param qualifiedName of the TableauFlow
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the TableauFlow
     * @deprecated see {@link #appendClassifications(String, List)} instead
     */
    @Deprecated
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a TableauFlow.
     *
     * @param qualifiedName of the TableauFlow
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the TableauFlow
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
     * Remove a classification from a TableauFlow.
     *
     * @param qualifiedName of the TableauFlow
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the TableauFlow
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }
}
