/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class AtlanQuery extends SQL {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Query";

    /** Fixed typeName for Querys. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String rawQuery;

    /** TBC */
    @Attribute
    String defaultSchemaQualifiedName;

    /** TBC */
    @Attribute
    String defaultDatabaseQualifiedName;

    /** TBC */
    @Attribute
    String variablesSchemaBase64;

    /** TBC */
    @Attribute
    Boolean isPrivate;

    /** TBC */
    @Attribute
    Boolean isSqlSnippet;

    /** TBC */
    @Attribute
    String parentQualifiedName;

    /** TBC */
    @Attribute
    String collectionQualifiedName;

    /** TBC */
    @Attribute
    Boolean isVisualQuery;

    /** TBC */
    @Attribute
    String visualBuilderSchemaBase64;

    /** TBC */
    @Attribute
    Namespace parent;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<Table> tables;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<Column> columns;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<View> views;

    /**
     * Reference to a Query by GUID.
     *
     * @param guid the GUID of the Query to reference
     * @return reference to a Query that can be used for defining a relationship to a Query
     */
    public static AtlanQuery refByGuid(String guid) {
        return AtlanQuery.builder().guid(guid).build();
    }

    /**
     * Reference to a Query by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the Query to reference
     * @return reference to a Query that can be used for defining a relationship to a Query
     */
    public static AtlanQuery refByQualifiedName(String qualifiedName) {
        return AtlanQuery.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a Query.
     *
     * @param qualifiedName of the Query
     * @param name of the Query
     * @return the minimal request necessary to update the Query, as a builder
     */
    public static AtlanQueryBuilder<?, ?> updater(String qualifiedName, String name) {
        return AtlanQuery.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Query, from a potentially
     * more-complete Query object.
     *
     * @return the minimal object necessary to update the Query, as a builder
     */
    @Override
    protected AtlanQueryBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Update the certificate on a Query.
     *
     * @param qualifiedName of the Query
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated Query, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (AtlanQuery) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a Query.
     *
     * @param qualifiedName of the Query
     * @param name of the Query
     * @return the updated Query, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (AtlanQuery)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a Query.
     *
     * @param qualifiedName of the Query
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (AtlanQuery) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a Query.
     *
     * @param qualifiedName of the Query
     * @param name of the Query
     * @return the updated Query, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (AtlanQuery)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a Query.
     *
     * @param qualifiedName of the Query
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the Query
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a Query.
     *
     * @param qualifiedName of the Query
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the Query
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the Query.
     *
     * @param qualifiedName for the Query
     * @param name human-readable name of the Query
     * @param terms the list of terms to replace on the Query, or null to remove all terms from the Query
     * @return the Query that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (AtlanQuery) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the Query, without replacing existing terms linked to the Query.
     * Note: this operation must make two API calls — one to retrieve the Query's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the Query
     * @param terms the list of terms to append to the Query
     * @return the Query that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (AtlanQuery) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a Query, without replacing all existing terms linked to the Query.
     * Note: this operation must make two API calls — one to retrieve the Query's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the Query
     * @param terms the list of terms to remove from the Query, which must be referenced by GUID
     * @return the Query that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (AtlanQuery) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
