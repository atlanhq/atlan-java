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
 * Instance of a materialized view in Atlan.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("cast")
public class MaterializedView extends SQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MaterialisedView";

    /** Fixed typeName for MaterializedViews. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String refreshMode;

    /** TBC */
    @Attribute
    String refreshMethod;

    /** TBC */
    @Attribute
    String staleness;

    /** TBC */
    @Attribute
    Long staleSinceDate;

    /** Number of columns in this materialized view. */
    @Attribute
    Long columnCount;

    /** Number of rows in this materialized view. */
    @Attribute
    Long rowCount;

    /** Size of the materialized view in bytes. */
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

    /** Whether this materialized view is temporary (true) or not (false). */
    @Attribute
    Boolean isTemporary;

    /** Definition of the materialized view (DDL). */
    @Attribute
    String definition;

    /** Schema in which this materialized view exists. */
    @Attribute
    @JsonProperty("atlanSchema")
    Schema schema;

    /** Columns that exist within this materialized view. */
    @Attribute
    @Singular
    SortedSet<Column> columns;

    /**
     * Reference to a MaterializedView by GUID.
     *
     * @param guid the GUID of the MaterializedView to reference
     * @return reference to a MaterializedView that can be used for defining a relationship to a MaterializedView
     */
    public static MaterializedView refByGuid(String guid) {
        return MaterializedView.builder().guid(guid).build();
    }

    /**
     * Reference to a MaterializedView by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the MaterializedView to reference
     * @return reference to a MaterializedView that can be used for defining a relationship to a MaterializedView
     */
    public static MaterializedView refByQualifiedName(String qualifiedName) {
        return MaterializedView.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to create a materialized view.
     *
     * @param name of the materialized view
     * @param schemaQualifiedName unique name of the schema in which this materialized view exists
     * @return the minimal request necessary to create the materialized view, as a builder
     */
    public static MaterializedViewBuilder<?, ?> creator(String name, String schemaQualifiedName) {
        String[] tokens = schemaQualifiedName.split("/");
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(tokens);
        String schemaName = StringUtils.getNameFromQualifiedName(schemaQualifiedName);
        String databaseQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(schemaQualifiedName);
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        return MaterializedView.builder()
                .name(name)
                .qualifiedName(schemaQualifiedName + "/" + name)
                .connectorType(connectorType)
                .schemaName(schemaName)
                .schemaQualifiedName(schemaQualifiedName)
                .schema(Schema.refByQualifiedName(schemaQualifiedName))
                .databaseName(databaseName)
                .databaseQualifiedName(databaseQualifiedName)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a MaterializedView.
     *
     * @param qualifiedName of the MaterializedView
     * @param name of the MaterializedView
     * @return the minimal request necessary to update the MaterializedView, as a builder
     */
    public static MaterializedViewBuilder<?, ?> updater(String qualifiedName, String name) {
        return MaterializedView.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a MaterializedView, from a potentially
     * more-complete MaterializedView object.
     *
     * @return the minimal object necessary to update the MaterializedView, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for MaterializedView are not found in the initial object
     */
    @Override
    public MaterializedViewBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "MaterializedView", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a MaterializedView by its GUID, complete with all of its relationships.
     *
     * @param guid of the MaterializedView to retrieve
     * @return the requested full MaterializedView, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MaterializedView does not exist or the provided GUID is not a MaterializedView
     */
    public static MaterializedView retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof MaterializedView) {
            return (MaterializedView) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "MaterializedView");
        }
    }

    /**
     * Retrieves a MaterializedView by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the MaterializedView to retrieve
     * @return the requested full MaterializedView, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MaterializedView does not exist
     */
    public static MaterializedView retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof MaterializedView) {
            return (MaterializedView) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "MaterializedView");
        }
    }

    /**
     * Restore the archived (soft-deleted) MaterializedView to active.
     *
     * @param qualifiedName for the MaterializedView
     * @return true if the MaterializedView is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a MaterializedView.
     *
     * @param qualifiedName of the MaterializedView
     * @param name of the MaterializedView
     * @return the updated MaterializedView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeDescription(String qualifiedName, String name) throws AtlanException {
        return (MaterializedView)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a MaterializedView.
     *
     * @param qualifiedName of the MaterializedView
     * @param name of the MaterializedView
     * @return the updated MaterializedView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (MaterializedView) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the owners from a MaterializedView.
     *
     * @param qualifiedName of the MaterializedView
     * @param name of the MaterializedView
     * @return the updated MaterializedView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeOwners(String qualifiedName, String name) throws AtlanException {
        return (MaterializedView)
                Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the certificate on a MaterializedView.
     *
     * @param qualifiedName of the MaterializedView
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MaterializedView, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (MaterializedView) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MaterializedView.
     *
     * @param qualifiedName of the MaterializedView
     * @param name of the MaterializedView
     * @return the updated MaterializedView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (MaterializedView)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a MaterializedView.
     *
     * @param qualifiedName of the MaterializedView
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (MaterializedView) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MaterializedView.
     *
     * @param qualifiedName of the MaterializedView
     * @param name of the MaterializedView
     * @return the updated MaterializedView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (MaterializedView)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a MaterializedView.
     *
     * @param qualifiedName of the MaterializedView
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the MaterializedView
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a MaterializedView.
     *
     * @param qualifiedName of the MaterializedView
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the MaterializedView
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the MaterializedView.
     *
     * @param qualifiedName for the MaterializedView
     * @param name human-readable name of the MaterializedView
     * @param terms the list of terms to replace on the MaterializedView, or null to remove all terms from the MaterializedView
     * @return the MaterializedView that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MaterializedView replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (MaterializedView) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MaterializedView, without replacing existing terms linked to the MaterializedView.
     * Note: this operation must make two API calls — one to retrieve the MaterializedView's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the MaterializedView
     * @param terms the list of terms to append to the MaterializedView
     * @return the MaterializedView that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MaterializedView appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (MaterializedView) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MaterializedView, without replacing all existing terms linked to the MaterializedView.
     * Note: this operation must make two API calls — one to retrieve the MaterializedView's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the MaterializedView
     * @param terms the list of terms to remove from the MaterializedView, which must be referenced by GUID
     * @return the MaterializedView that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (MaterializedView) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
