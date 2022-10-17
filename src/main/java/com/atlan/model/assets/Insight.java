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
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Insight extends Catalog {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Insight";

    /** Fixed typeName for Insights. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /**
     * Reference to a Insight by GUID.
     *
     * @param guid the GUID of the Insight to reference
     * @return reference to a Insight that can be used for defining a relationship to a Insight
     */
    public static Insight refByGuid(String guid) {
        return Insight.builder().guid(guid).build();
    }

    /**
     * Reference to a Insight by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the Insight to reference
     * @return reference to a Insight that can be used for defining a relationship to a Insight
     */
    public static Insight refByQualifiedName(String qualifiedName) {
        return Insight.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a Insight.
     *
     * @param qualifiedName of the Insight
     * @param name of the Insight
     * @return the minimal request necessary to update the Insight, as a builder
     */
    public static InsightBuilder<?, ?> updater(String qualifiedName, String name) {
        return Insight.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Insight, from a potentially
     * more-complete Insight object.
     *
     * @return the minimal object necessary to update the Insight, as a builder
     */
    @Override
    protected InsightBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a Insight by its GUID, complete with all of its relationships.
     *
     * @param guid of the Insight to retrieve
     * @return the requested full Insight, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Insight does not exist or the provided GUID is not a Insight
     */
    public static Insight retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof Insight) {
            return (Insight) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a Insight.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a Insight by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the Insight to retrieve
     * @return the requested full Insight, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Insight does not exist
     */
    public static Insight retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof Insight) {
            return (Insight) entity;
        } else {
            throw new NotFoundException(
                    "No Insight found with qualifiedName: " + qualifiedName, "ATLAN_JAVA_CLIENT-404-003", 404, null);
        }
    }
    /**
     * Update the certificate on a Insight.
     *
     * @param qualifiedName of the Insight
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated Insight, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Insight updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (Insight) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a Insight.
     *
     * @param qualifiedName of the Insight
     * @param name of the Insight
     * @return the updated Insight, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Insight removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (Insight)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a Insight.
     *
     * @param qualifiedName of the Insight
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Insight updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (Insight) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a Insight.
     *
     * @param qualifiedName of the Insight
     * @param name of the Insight
     * @return the updated Insight, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Insight removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (Insight)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a Insight.
     *
     * @param qualifiedName of the Insight
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the Insight
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a Insight.
     *
     * @param qualifiedName of the Insight
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the Insight
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the Insight.
     *
     * @param qualifiedName for the Insight
     * @param name human-readable name of the Insight
     * @param terms the list of terms to replace on the Insight, or null to remove all terms from the Insight
     * @return the Insight that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static Insight replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (Insight) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the Insight, without replacing existing terms linked to the Insight.
     * Note: this operation must make two API calls — one to retrieve the Insight's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the Insight
     * @param terms the list of terms to append to the Insight
     * @return the Insight that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static Insight appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (Insight) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a Insight, without replacing all existing terms linked to the Insight.
     * Note: this operation must make two API calls — one to retrieve the Insight's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the Insight
     * @param terms the list of terms to remove from the Insight, which must be referenced by GUID
     * @return the Insight that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static Insight removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (Insight) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
