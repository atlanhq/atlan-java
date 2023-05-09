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
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of an API specification in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class APISpec extends API {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "APISpec";

    /** Fixed typeName for APISpecs. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String apiSpecTermsOfServiceURL;

    /** TBC */
    @Attribute
    String apiSpecContactEmail;

    /** TBC */
    @Attribute
    String apiSpecContactName;

    /** TBC */
    @Attribute
    String apiSpecContactURL;

    /** TBC */
    @Attribute
    String apiSpecLicenseName;

    /** TBC */
    @Attribute
    String apiSpecLicenseURL;

    /** TBC */
    @Attribute
    String apiSpecContractVersion;

    /** TBC */
    @Attribute
    String apiSpecServiceAlias;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<APIPath> apiPaths;

    /**
     * Reference to a APISpec by GUID.
     *
     * @param guid the GUID of the APISpec to reference
     * @return reference to a APISpec that can be used for defining a relationship to a APISpec
     */
    public static APISpec refByGuid(String guid) {
        return APISpec.builder().guid(guid).build();
    }

    /**
     * Reference to a APISpec by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the APISpec to reference
     * @return reference to a APISpec that can be used for defining a relationship to a APISpec
     */
    public static APISpec refByQualifiedName(String qualifiedName) {
        return APISpec.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a APISpec by its GUID, complete with all of its relationships.
     *
     * @param guid of the APISpec to retrieve
     * @return the requested full APISpec, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the APISpec does not exist or the provided GUID is not a APISpec
     */
    public static APISpec retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof APISpec) {
            return (APISpec) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "APISpec");
        }
    }

    /**
     * Retrieves a APISpec by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the APISpec to retrieve
     * @return the requested full APISpec, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the APISpec does not exist
     */
    public static APISpec retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof APISpec) {
            return (APISpec) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "APISpec");
        }
    }

    /**
     * Restore the archived (soft-deleted) APISpec to active.
     *
     * @param qualifiedName for the APISpec
     * @return true if the APISpec is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create an API spec.
     *
     * @param name of the API spec
     * @param connectionQualifiedName unique name of the connection through which the spec is accessible
     * @return the minimal object necessary to create the API spec, as a builder
     */
    public static APISpecBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return APISpec.builder()
                .qualifiedName(connectionQualifiedName + "/" + name)
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.API);
    }

    /**
     * Builds the minimal object necessary to update a APISpec.
     *
     * @param qualifiedName of the APISpec
     * @param name of the APISpec
     * @return the minimal request necessary to update the APISpec, as a builder
     */
    public static APISpecBuilder<?, ?> updater(String qualifiedName, String name) {
        return APISpec.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a APISpec, from a potentially
     * more-complete APISpec object.
     *
     * @return the minimal object necessary to update the APISpec, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for APISpec are not found in the initial object
     */
    @Override
    public APISpecBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "APISpec", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a APISpec.
     *
     * @param qualifiedName of the APISpec
     * @param name of the APISpec
     * @return the updated APISpec, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APISpec removeDescription(String qualifiedName, String name) throws AtlanException {
        return (APISpec) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a APISpec.
     *
     * @param qualifiedName of the APISpec
     * @param name of the APISpec
     * @return the updated APISpec, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APISpec removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (APISpec) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a APISpec.
     *
     * @param qualifiedName of the APISpec
     * @param name of the APISpec
     * @return the updated APISpec, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APISpec removeOwners(String qualifiedName, String name) throws AtlanException {
        return (APISpec) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a APISpec.
     *
     * @param qualifiedName of the APISpec
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated APISpec, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static APISpec updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (APISpec) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a APISpec.
     *
     * @param qualifiedName of the APISpec
     * @param name of the APISpec
     * @return the updated APISpec, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APISpec removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (APISpec) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a APISpec.
     *
     * @param qualifiedName of the APISpec
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static APISpec updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (APISpec) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a APISpec.
     *
     * @param qualifiedName of the APISpec
     * @param name of the APISpec
     * @return the updated APISpec, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APISpec removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (APISpec) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the APISpec.
     *
     * @param qualifiedName for the APISpec
     * @param name human-readable name of the APISpec
     * @param terms the list of terms to replace on the APISpec, or null to remove all terms from the APISpec
     * @return the APISpec that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static APISpec replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (APISpec) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the APISpec, without replacing existing terms linked to the APISpec.
     * Note: this operation must make two API calls — one to retrieve the APISpec's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the APISpec
     * @param terms the list of terms to append to the APISpec
     * @return the APISpec that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static APISpec appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (APISpec) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a APISpec, without replacing all existing terms linked to the APISpec.
     * Note: this operation must make two API calls — one to retrieve the APISpec's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the APISpec
     * @param terms the list of terms to remove from the APISpec, which must be referenced by GUID
     * @return the APISpec that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static APISpec removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (APISpec) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add classifications to a APISpec, without replacing existing classifications linked to the APISpec.
     * Note: this operation must make two API calls — one to retrieve the APISpec's existing classifications,
     * and a second to append the new classifications.
     *
     * @param qualifiedName of the APISpec
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems
     * @return the updated APISpec
     */
    public static APISpec appendClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        return (APISpec) Asset.appendClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a APISpec, without replacing existing classifications linked to the APISpec.
     * Note: this operation must make two API calls — one to retrieve the APISpec's existing classifications,
     * and a second to append the new classifications.
     *
     * @param qualifiedName of the APISpec
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated APISpec
     */
    public static APISpec appendClassifications(
            String qualifiedName,
            List<String> classificationNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (APISpec) Asset.appendClassifications(
                TYPE_NAME,
                qualifiedName,
                classificationNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add classifications to a APISpec.
     *
     * @param qualifiedName of the APISpec
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the APISpec
     * @deprecated see {@link #appendClassifications(String, List)} instead
     */
    @Deprecated
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a APISpec.
     *
     * @param qualifiedName of the APISpec
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the APISpec
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
     * Remove a classification from a APISpec.
     *
     * @param qualifiedName of the APISpec
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the APISpec
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }
}
