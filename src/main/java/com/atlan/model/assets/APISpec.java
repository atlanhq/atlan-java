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
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class APISpec extends API {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "APISpec";

    /** Fixed typeName for APISpecs. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
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
     */
    @Override
    protected APISpecBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a APISpec by its GUID, complete with all of its relationships.
     *
     * @param guid of the APISpec to retrieve
     * @return the requested full APISpec, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the APISpec does not exist or the provided GUID is not a APISpec
     */
    public static APISpec retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof APISpec) {
            return (APISpec) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a APISpec.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
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
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof APISpec) {
            return (APISpec) entity;
        } else {
            throw new NotFoundException(
                    "No APISpec found with qualifiedName: " + qualifiedName, "ATLAN_JAVA_CLIENT-404-003", 404, null);
        }
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
    public static APISpec updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
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
        return (APISpec)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
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
        return (APISpec)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a APISpec.
     *
     * @param qualifiedName of the APISpec
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the APISpec
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
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
}
