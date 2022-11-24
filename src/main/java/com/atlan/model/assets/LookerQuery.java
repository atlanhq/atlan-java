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
public class LookerQuery extends Looker {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "LookerQuery";

    /** Fixed typeName for LookerQuerys. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String sourceDefinition;

    /** TBC */
    @Attribute
    String sourceDefinitionDatabase;

    /** TBC */
    @Attribute
    String sourceDefinitionSchema;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> fields;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<LookerTile> tiles;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<LookerLook> looks;

    /** TBC */
    @Attribute
    LookerModel model;

    /**
     * Reference to a LookerQuery by GUID.
     *
     * @param guid the GUID of the LookerQuery to reference
     * @return reference to a LookerQuery that can be used for defining a relationship to a LookerQuery
     */
    public static LookerQuery refByGuid(String guid) {
        return LookerQuery.builder().guid(guid).build();
    }

    /**
     * Reference to a LookerQuery by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the LookerQuery to reference
     * @return reference to a LookerQuery that can be used for defining a relationship to a LookerQuery
     */
    public static LookerQuery refByQualifiedName(String qualifiedName) {
        return LookerQuery.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a LookerQuery.
     *
     * @param qualifiedName of the LookerQuery
     * @param name of the LookerQuery
     * @return the minimal request necessary to update the LookerQuery, as a builder
     */
    public static LookerQueryBuilder<?, ?> updater(String qualifiedName, String name) {
        return LookerQuery.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a LookerQuery, from a potentially
     * more-complete LookerQuery object.
     *
     * @return the minimal object necessary to update the LookerQuery, as a builder
     */
    @Override
    protected LookerQueryBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a LookerQuery by its GUID, complete with all of its relationships.
     *
     * @param guid of the LookerQuery to retrieve
     * @return the requested full LookerQuery, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerQuery does not exist or the provided GUID is not a LookerQuery
     */
    public static LookerQuery retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof LookerQuery) {
            return (LookerQuery) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a LookerQuery.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a LookerQuery by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the LookerQuery to retrieve
     * @return the requested full LookerQuery, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerQuery does not exist
     */
    public static LookerQuery retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof LookerQuery) {
            return (LookerQuery) entity;
        } else {
            throw new NotFoundException(
                    "No LookerQuery found with qualifiedName: " + qualifiedName,
                    "ATLAN_JAVA_CLIENT-404-003",
                    404,
                    null);
        }
    }

    /**
     * Restore the archived (soft-deleted) LookerQuery to active.
     *
     * @param qualifiedName for the LookerQuery
     * @return the LookerQuery that was restored
     * @throws AtlanException on any API problems
     */
    public static LookerQuery restore(String qualifiedName) throws AtlanException {
        return (LookerQuery) Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a LookerQuery.
     *
     * @param qualifiedName of the LookerQuery
     * @param name of the LookerQuery
     * @return the updated LookerQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerQuery removeDescription(String qualifiedName, String name) throws AtlanException {
        return (LookerQuery)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a LookerQuery.
     *
     * @param qualifiedName of the LookerQuery
     * @param name of the LookerQuery
     * @return the updated LookerQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerQuery removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (LookerQuery) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the owners from a LookerQuery.
     *
     * @param qualifiedName of the LookerQuery
     * @param name of the LookerQuery
     * @return the updated LookerQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerQuery removeOwners(String qualifiedName, String name) throws AtlanException {
        return (LookerQuery)
                Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the certificate on a LookerQuery.
     *
     * @param qualifiedName of the LookerQuery
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated LookerQuery, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerQuery updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (LookerQuery) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a LookerQuery.
     *
     * @param qualifiedName of the LookerQuery
     * @param name of the LookerQuery
     * @return the updated LookerQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerQuery removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (LookerQuery)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a LookerQuery.
     *
     * @param qualifiedName of the LookerQuery
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerQuery updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (LookerQuery) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a LookerQuery.
     *
     * @param qualifiedName of the LookerQuery
     * @param name of the LookerQuery
     * @return the updated LookerQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerQuery removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (LookerQuery)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a LookerQuery.
     *
     * @param qualifiedName of the LookerQuery
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the LookerQuery
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a LookerQuery.
     *
     * @param qualifiedName of the LookerQuery
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the LookerQuery
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the LookerQuery.
     *
     * @param qualifiedName for the LookerQuery
     * @param name human-readable name of the LookerQuery
     * @param terms the list of terms to replace on the LookerQuery, or null to remove all terms from the LookerQuery
     * @return the LookerQuery that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static LookerQuery replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (LookerQuery) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the LookerQuery, without replacing existing terms linked to the LookerQuery.
     * Note: this operation must make two API calls — one to retrieve the LookerQuery's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the LookerQuery
     * @param terms the list of terms to append to the LookerQuery
     * @return the LookerQuery that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static LookerQuery appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (LookerQuery) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a LookerQuery, without replacing all existing terms linked to the LookerQuery.
     * Note: this operation must make two API calls — one to retrieve the LookerQuery's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the LookerQuery
     * @param terms the list of terms to remove from the LookerQuery, which must be referenced by GUID
     * @return the LookerQuery that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static LookerQuery removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (LookerQuery) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
