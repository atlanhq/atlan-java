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
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a database view in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("cast")
public class View extends SQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "View";

    /** Fixed typeName for Views. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Number of columns in this view. */
    @Attribute
    Long columnCount;

    /** Number of rows in this view. */
    @Attribute
    Long rowCount;

    /** Size of the view in bytes. */
    @Attribute
    Long sizeBytes;

    /** TBC */
    @Attribute
    Boolean isQueryPreview;

    /** TBC */
    @Attribute
    @Singular("putQueryPreviewConfig")
    Map<String, String> queryPreviewConfig;

    /** TBC */
    @Attribute
    String alias;

    /** Whether this view is temporary (true) or not (false). */
    @Attribute
    Boolean isTemporary;

    /** Definition of the view (DDL). */
    @Attribute
    String definition;

    /** Columns that exist within this view. */
    @Attribute
    @Singular
    SortedSet<Column> columns;

    /** Queries that involve this view. */
    @Attribute
    @Singular
    SortedSet<AtlanQuery> queries;

    /** Schema in which this view exists. */
    @Attribute
    @JsonProperty("atlanSchema")
    Schema schema;

    /**
     * Reference to a View by GUID.
     *
     * @param guid the GUID of the View to reference
     * @return reference to a View that can be used for defining a relationship to a View
     */
    public static View refByGuid(String guid) {
        return View.builder().guid(guid).build();
    }

    /**
     * Reference to a View by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the View to reference
     * @return reference to a View that can be used for defining a relationship to a View
     */
    public static View refByQualifiedName(String qualifiedName) {
        return View.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to create a view.
     *
     * @param name of the view
     * @param schemaQualifiedName unique name of the schema in which this view exists
     * @return the minimal request necessary to create the view, as a builder
     */
    public static ViewBuilder<?, ?> creator(String name, String schemaQualifiedName) {
        String[] tokens = schemaQualifiedName.split("/");
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(tokens);
        String schemaName = StringUtils.getNameFromQualifiedName(schemaQualifiedName);
        String databaseQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(schemaQualifiedName);
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        return View.builder()
                .name(name)
                .qualifiedName(generateQualifiedName(name, schemaQualifiedName))
                .connectorType(connectorType)
                .schemaName(schemaName)
                .schemaQualifiedName(schemaQualifiedName)
                .schema(Schema.refByQualifiedName(schemaQualifiedName))
                .databaseName(databaseName)
                .databaseQualifiedName(databaseQualifiedName)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Generate a unique view name.
     *
     * @param name of the view
     * @param schemaQualifiedName unique name of the schema in which this view exists
     * @return a unique name for the view
     */
    public static String generateQualifiedName(String name, String schemaQualifiedName) {
        return schemaQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a View.
     *
     * @param qualifiedName of the View
     * @param name of the View
     * @return the minimal request necessary to update the View, as a builder
     */
    public static ViewBuilder<?, ?> updater(String qualifiedName, String name) {
        return View.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a View, from a potentially
     * more-complete View object.
     *
     * @return the minimal object necessary to update the View, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for View are not found in the initial object
     */
    @Override
    public ViewBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "View", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a View by its GUID, complete with all of its relationships.
     *
     * @param guid of the View to retrieve
     * @return the requested full View, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the View does not exist or the provided GUID is not a View
     */
    public static View retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof View) {
            return (View) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "View");
        }
    }

    /**
     * Retrieves a View by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the View to retrieve
     * @return the requested full View, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the View does not exist
     */
    public static View retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof View) {
            return (View) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "View");
        }
    }

    /**
     * Restore the archived (soft-deleted) View to active.
     *
     * @param qualifiedName for the View
     * @return true if the View is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a View.
     *
     * @param qualifiedName of the View
     * @param name of the View
     * @return the updated View, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static View removeDescription(String qualifiedName, String name) throws AtlanException {
        return (View) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a View.
     *
     * @param qualifiedName of the View
     * @param name of the View
     * @return the updated View, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static View removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (View) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a View.
     *
     * @param qualifiedName of the View
     * @param name of the View
     * @return the updated View, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static View removeOwners(String qualifiedName, String name) throws AtlanException {
        return (View) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a View.
     *
     * @param qualifiedName of the View
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated View, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static View updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (View) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a View.
     *
     * @param qualifiedName of the View
     * @param name of the View
     * @return the updated View, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static View removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (View) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a View.
     *
     * @param qualifiedName of the View
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static View updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (View) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a View.
     *
     * @param qualifiedName of the View
     * @param name of the View
     * @return the updated View, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static View removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (View) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Add classifications to a View.
     *
     * @param qualifiedName of the View
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the View
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a View.
     *
     * @param qualifiedName of the View
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the View
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the View.
     *
     * @param qualifiedName for the View
     * @param name human-readable name of the View
     * @param terms the list of terms to replace on the View, or null to remove all terms from the View
     * @return the View that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static View replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms) throws AtlanException {
        return (View) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the View, without replacing existing terms linked to the View.
     * Note: this operation must make two API calls — one to retrieve the View's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the View
     * @param terms the list of terms to append to the View
     * @return the View that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static View appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (View) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a View, without replacing all existing terms linked to the View.
     * Note: this operation must make two API calls — one to retrieve the View's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the View
     * @param terms the list of terms to remove from the View, which must be referenced by GUID
     * @return the View that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static View removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (View) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
