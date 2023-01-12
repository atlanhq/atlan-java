/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.*;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of an API path that could contain one or more endpoints in Atlan.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("cast")
public class APIPath extends API {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "APIPath";

    /** Fixed typeName for APIPaths. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String apiPathSummary;

    /** TBC */
    @Attribute
    String apiPathRawURI;

    /** TBC */
    @Attribute
    Boolean apiPathIsTemplated;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> apiPathAvailableOperations;

    /** TBC */
    @Attribute
    @Singular
    Map<String, String> apiPathAvailableResponseCodes;

    /** TBC */
    @Attribute
    Boolean apiPathIsIngressExposed;

    /** TBC */
    @Attribute
    APISpec apiSpec;

    /**
     * Reference to a APIPath by GUID.
     *
     * @param guid the GUID of the APIPath to reference
     * @return reference to a APIPath that can be used for defining a relationship to a APIPath
     */
    public static APIPath refByGuid(String guid) {
        return APIPath.builder().guid(guid).build();
    }

    /**
     * Reference to a APIPath by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the APIPath to reference
     * @return reference to a APIPath that can be used for defining a relationship to a APIPath
     */
    public static APIPath refByQualifiedName(String qualifiedName) {
        return APIPath.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to create an API path.
     *
     * @param pathURI unique URI of the API path
     * @param apiSpecQualifiedName unique name of the API spec through which the path is accessible
     * @return the minimal object necessary to create the API path, as a builder
     */
    public static APIPathBuilder<?, ?> creator(String pathURI, String apiSpecQualifiedName) {
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(apiSpecQualifiedName);
        String normalizedURI = pathURI.startsWith("/") ? pathURI : "/" + pathURI;
        return APIPath.builder()
                .qualifiedName(apiSpecQualifiedName + normalizedURI)
                .name(normalizedURI)
                .apiPathRawURI(normalizedURI)
                .apiSpec(APISpec.refByQualifiedName(apiSpecQualifiedName))
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.API);
    }

    /**
     * Builds the minimal object necessary to update a APIPath.
     *
     * @param qualifiedName of the APIPath
     * @param name of the APIPath
     * @return the minimal request necessary to update the APIPath, as a builder
     */
    public static APIPathBuilder<?, ?> updater(String qualifiedName, String name) {
        return APIPath.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a APIPath, from a potentially
     * more-complete APIPath object.
     *
     * @return the minimal object necessary to update the APIPath, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for APIPath are not found in the initial object
     */
    @Override
    public APIPathBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "APIPath", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a APIPath by its GUID, complete with all of its relationships.
     *
     * @param guid of the APIPath to retrieve
     * @return the requested full APIPath, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the APIPath does not exist or the provided GUID is not a APIPath
     */
    public static APIPath retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof APIPath) {
            return (APIPath) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "APIPath");
        }
    }

    /**
     * Retrieves a APIPath by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the APIPath to retrieve
     * @return the requested full APIPath, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the APIPath does not exist
     */
    public static APIPath retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof APIPath) {
            return (APIPath) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "APIPath");
        }
    }

    /**
     * Restore the archived (soft-deleted) APIPath to active.
     *
     * @param qualifiedName for the APIPath
     * @return true if the APIPath is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a APIPath.
     *
     * @param qualifiedName of the APIPath
     * @param name of the APIPath
     * @return the updated APIPath, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIPath removeDescription(String qualifiedName, String name) throws AtlanException {
        return (APIPath) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a APIPath.
     *
     * @param qualifiedName of the APIPath
     * @param name of the APIPath
     * @return the updated APIPath, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIPath removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (APIPath) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a APIPath.
     *
     * @param qualifiedName of the APIPath
     * @param name of the APIPath
     * @return the updated APIPath, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIPath removeOwners(String qualifiedName, String name) throws AtlanException {
        return (APIPath) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a APIPath.
     *
     * @param qualifiedName of the APIPath
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated APIPath, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static APIPath updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (APIPath) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a APIPath.
     *
     * @param qualifiedName of the APIPath
     * @param name of the APIPath
     * @return the updated APIPath, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIPath removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (APIPath) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a APIPath.
     *
     * @param qualifiedName of the APIPath
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static APIPath updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (APIPath) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a APIPath.
     *
     * @param qualifiedName of the APIPath
     * @param name of the APIPath
     * @return the updated APIPath, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIPath removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (APIPath) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Add classifications to a APIPath.
     *
     * @param qualifiedName of the APIPath
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the APIPath
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a APIPath.
     *
     * @param qualifiedName of the APIPath
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the APIPath
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the APIPath.
     *
     * @param qualifiedName for the APIPath
     * @param name human-readable name of the APIPath
     * @param terms the list of terms to replace on the APIPath, or null to remove all terms from the APIPath
     * @return the APIPath that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static APIPath replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (APIPath) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the APIPath, without replacing existing terms linked to the APIPath.
     * Note: this operation must make two API calls — one to retrieve the APIPath's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the APIPath
     * @param terms the list of terms to append to the APIPath
     * @return the APIPath that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static APIPath appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (APIPath) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a APIPath, without replacing all existing terms linked to the APIPath.
     * Note: this operation must make two API calls — one to retrieve the APIPath's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the APIPath
     * @param terms the list of terms to remove from the APIPath, which must be referenced by GUID
     * @return the APIPath that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static APIPath removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (APIPath) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
