/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a materialized view in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class MaterializedView extends SQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MaterialisedView";

    /** Fixed typeName for materialized views. */
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
    String alias;

    /** Whether this materialized view is temporary (true) or not (false). */
    @Attribute
    Boolean isTemporary;

    /** TBC */
    @Attribute
    Boolean isQueryPreview;

    /** Unused attributes. */
    @JsonIgnore
    Map<String, String> queryPreviewConfig;

    /** Definition of the materialized view (DDL). */
    @Attribute
    String definition;

    /** Schema in which this materialized view exists. */
    @Attribute
    @JsonProperty("atlanSchema")
    Schema schema;

    /** Columns that exist within this materialized view. */
    @Singular
    @Attribute
    SortedSet<Column> columns;

    /**
     * Reference to a materialized view by GUID.
     *
     * @param guid the GUID of the materialized view to reference
     * @return reference to a materialized view that can be used for defining a relationship to a materialized view
     */
    public static MaterializedView refByGuid(String guid) {
        return MaterializedView.builder().guid(guid).build();
    }

    /**
     * Reference to a materialized view by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the materialized view to reference
     * @return reference to a materialized view that can be used for defining a relationship to a materialized view
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
     * Builds the minimal object necessary to update a materialized view.
     *
     * @param qualifiedName of the materialized view
     * @param name of the materialized view
     * @return the minimal request necessary to update the materialized view, as a builder
     */
    public static MaterializedViewBuilder<?, ?> updater(String qualifiedName, String name) {
        return MaterializedView.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a materialized view, from a potentially
     * more-complete materialized view object.
     *
     * @return the minimal object necessary to update the materialized view, as a builder
     */
    @Override
    protected MaterializedViewBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Update the certificate on a materialized view.
     *
     * @param qualifiedName of the materialized view
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated materialized view, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (MaterializedView) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a materialized view.
     *
     * @param qualifiedName of the materialized view
     * @param name of the materialized view
     * @return the updated materialized view, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (MaterializedView)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a materialized view.
     *
     * @param qualifiedName of the materialized view
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
     * Remove the announcement from a materialized view.
     *
     * @param qualifiedName of the materialized view
     * @param name of the materialized view
     * @return the updated materialized view, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (MaterializedView)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a materialized view.
     *
     * @param qualifiedName of the materialized view
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the materialized view
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a materialized view.
     *
     * @param qualifiedName of the materialized view
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the materialized view
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the materialized view.
     *
     * @param qualifiedName for the materialized view
     * @param name human-readable name of the materialized view
     * @param terms the list of terms to replace on the materialized view, or null to remove all terms from the materialized view
     * @return the materialized view that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MaterializedView replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (MaterializedView) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the materialized view, without replacing existing terms linked to the materialized view.
     * Note: this operation must make two API calls — one to retrieve the materialized view's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the materialized view
     * @param terms the list of terms to append to the materialized view
     * @return the materialized view that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MaterializedView appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (MaterializedView) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a materialized view, without replacing all existing terms linked to the materialized view.
     * Note: this operation must make two API calls — one to retrieve the materialized view's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the materialized view
     * @param terms the list of terms to remove from the materialized view, which must be referenced by GUID
     * @return the materialized view that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (MaterializedView) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
