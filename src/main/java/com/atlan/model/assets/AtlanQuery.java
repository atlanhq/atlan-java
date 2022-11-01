/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.Entity;
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

    /** Fixed typeName for AtlanQuerys. */
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
     * Reference to a AtlanQuery by GUID.
     *
     * @param guid the GUID of the AtlanQuery to reference
     * @return reference to a AtlanQuery that can be used for defining a relationship to a AtlanQuery
     */
    public static AtlanQuery refByGuid(String guid) {
        return AtlanQuery.builder().guid(guid).build();
    }

    /**
     * Reference to a AtlanQuery by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the AtlanQuery to reference
     * @return reference to a AtlanQuery that can be used for defining a relationship to a AtlanQuery
     */
    public static AtlanQuery refByQualifiedName(String qualifiedName) {
        return AtlanQuery.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a AtlanQuery.
     *
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @return the minimal request necessary to update the AtlanQuery, as a builder
     */
    public static AtlanQueryBuilder<?, ?> updater(String qualifiedName, String name) {
        return AtlanQuery.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AtlanQuery, from a potentially
     * more-complete AtlanQuery object.
     *
     * @return the minimal object necessary to update the AtlanQuery, as a builder
     */
    @Override
    protected AtlanQueryBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a AtlanQuery by its GUID, complete with all of its relationships.
     *
     * @param guid of the AtlanQuery to retrieve
     * @return the requested full AtlanQuery, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AtlanQuery does not exist or the provided GUID is not a AtlanQuery
     */
    public static AtlanQuery retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof AtlanQuery) {
            return (AtlanQuery) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a AtlanQuery.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a AtlanQuery by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the AtlanQuery to retrieve
     * @return the requested full AtlanQuery, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AtlanQuery does not exist
     */
    public static AtlanQuery retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof AtlanQuery) {
            return (AtlanQuery) entity;
        } else {
            throw new NotFoundException(
                    "No AtlanQuery found with qualifiedName: " + qualifiedName, "ATLAN_JAVA_CLIENT-404-003", 404, null);
        }
    }

    /**
     * Update the certificate on a AtlanQuery.
     *
     * @param qualifiedName of the AtlanQuery
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AtlanQuery, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (AtlanQuery) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a AtlanQuery.
     *
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @return the updated AtlanQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (AtlanQuery)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a AtlanQuery.
     *
     * @param qualifiedName of the AtlanQuery
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
     * Remove the announcement from a AtlanQuery.
     *
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @return the updated AtlanQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (AtlanQuery)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a AtlanQuery.
     *
     * @param qualifiedName of the AtlanQuery
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the AtlanQuery
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a AtlanQuery.
     *
     * @param qualifiedName of the AtlanQuery
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the AtlanQuery
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the AtlanQuery.
     *
     * @param qualifiedName for the AtlanQuery
     * @param name human-readable name of the AtlanQuery
     * @param terms the list of terms to replace on the AtlanQuery, or null to remove all terms from the AtlanQuery
     * @return the AtlanQuery that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (AtlanQuery) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the AtlanQuery, without replacing existing terms linked to the AtlanQuery.
     * Note: this operation must make two API calls — one to retrieve the AtlanQuery's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the AtlanQuery
     * @param terms the list of terms to append to the AtlanQuery
     * @return the AtlanQuery that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (AtlanQuery) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a AtlanQuery, without replacing all existing terms linked to the AtlanQuery.
     * Note: this operation must make two API calls — one to retrieve the AtlanQuery's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the AtlanQuery
     * @param terms the list of terms to remove from the AtlanQuery, which must be referenced by GUID
     * @return the AtlanQuery that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (AtlanQuery) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
