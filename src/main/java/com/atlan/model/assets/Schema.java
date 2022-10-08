/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a schema in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Schema extends SQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Schema";

    /** Fixed typeName for Schemas. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Number of tables in this schema. */
    @Attribute
    Integer tableCount;

    /** Number of views in this schema. */
    @Attribute
    @JsonProperty("viewsCount")
    Integer viewCount;

    /** Materialized views that exist within this schema. */
    @Attribute
    @JsonProperty("materialisedViews")
    @Singular
    SortedSet<MaterializedView> materializedViews;

    /** Tables that exist within this schema. */
    @Attribute
    @Singular
    SortedSet<Table> tables;

    /** Database in which this schema exists. */
    @Attribute
    Database database;

    /** Procedures that are defined within this schema. */
    @Attribute
    @Singular
    SortedSet<Procedure> procedures;

    /** Views that exist within this schema. */
    @Attribute
    @Singular
    SortedSet<View> views;

    /**
     * Reference to a Schema by GUID.
     *
     * @param guid the GUID of the Schema to reference
     * @return reference to a Schema that can be used for defining a relationship to a Schema
     */
    public static Schema refByGuid(String guid) {
        return Schema.builder().guid(guid).build();
    }

    /**
     * Reference to a Schema by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the Schema to reference
     * @return reference to a Schema that can be used for defining a relationship to a Schema
     */
    public static Schema refByQualifiedName(String qualifiedName) {
        return Schema.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to create a schema.
     *
     * @param name of the schema
     * @param databaseQualifiedName unique name of the database in which this schema exists
     * @return the minimal request necessary to create the schema, as a builder
     */
    public static SchemaBuilder<?, ?> creator(String name, String databaseQualifiedName) {
        String[] tokens = databaseQualifiedName.split("/");
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(tokens);
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        return Schema.builder()
                .name(name)
                .qualifiedName(databaseQualifiedName + "/" + name)
                .connectorType(connectorType)
                .databaseName(databaseName)
                .databaseQualifiedName(databaseQualifiedName)
                .database(Database.refByQualifiedName(databaseQualifiedName))
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a Schema.
     *
     * @param qualifiedName of the Schema
     * @param name of the Schema
     * @return the minimal request necessary to update the Schema, as a builder
     */
    public static SchemaBuilder<?, ?> updater(String qualifiedName, String name) {
        return Schema.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Schema, from a potentially
     * more-complete Schema object.
     *
     * @return the minimal object necessary to update the Schema, as a builder
     */
    @Override
    protected SchemaBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Update the certificate on a Schema.
     *
     * @param qualifiedName of the Schema
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated Schema, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Schema updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (Schema) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a Schema.
     *
     * @param qualifiedName of the Schema
     * @param name of the Schema
     * @return the updated Schema, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Schema removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (Schema)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a Schema.
     *
     * @param qualifiedName of the Schema
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Schema updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (Schema) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a Schema.
     *
     * @param qualifiedName of the Schema
     * @param name of the Schema
     * @return the updated Schema, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Schema removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (Schema)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a Schema.
     *
     * @param qualifiedName of the Schema
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the Schema
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a Schema.
     *
     * @param qualifiedName of the Schema
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the Schema
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the Schema.
     *
     * @param qualifiedName for the Schema
     * @param name human-readable name of the Schema
     * @param terms the list of terms to replace on the Schema, or null to remove all terms from the Schema
     * @return the Schema that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static Schema replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (Schema) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the Schema, without replacing existing terms linked to the Schema.
     * Note: this operation must make two API calls — one to retrieve the Schema's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the Schema
     * @param terms the list of terms to append to the Schema
     * @return the Schema that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static Schema appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (Schema) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a Schema, without replacing all existing terms linked to the Schema.
     * Note: this operation must make two API calls — one to retrieve the Schema's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the Schema
     * @param terms the list of terms to remove from the Schema, which must be referenced by GUID
     * @return the Schema that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static Schema removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (Schema) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
