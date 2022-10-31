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
public class LookerView extends Looker {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "LookerView";

    /** Fixed typeName for LookerViews. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String projectName;

    /** TBC */
    @Attribute
    LookerProject project;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<LookerField> fields;

    /**
     * Reference to a LookerView by GUID.
     *
     * @param guid the GUID of the LookerView to reference
     * @return reference to a LookerView that can be used for defining a relationship to a LookerView
     */
    public static LookerView refByGuid(String guid) {
        return LookerView.builder().guid(guid).build();
    }

    /**
     * Reference to a LookerView by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the LookerView to reference
     * @return reference to a LookerView that can be used for defining a relationship to a LookerView
     */
    public static LookerView refByQualifiedName(String qualifiedName) {
        return LookerView.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a LookerView.
     *
     * @param qualifiedName of the LookerView
     * @param name of the LookerView
     * @return the minimal request necessary to update the LookerView, as a builder
     */
    public static LookerViewBuilder<?, ?> updater(String qualifiedName, String name) {
        return LookerView.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a LookerView, from a potentially
     * more-complete LookerView object.
     *
     * @return the minimal object necessary to update the LookerView, as a builder
     */
    @Override
    protected LookerViewBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a LookerView by its GUID, complete with all of its relationships.
     *
     * @param guid of the LookerView to retrieve
     * @return the requested full LookerView, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerView does not exist or the provided GUID is not a LookerView
     */
    public static LookerView retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof LookerView) {
            return (LookerView) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a LookerView.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a LookerView by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the LookerView to retrieve
     * @return the requested full LookerView, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerView does not exist
     */
    public static LookerView retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof LookerView) {
            return (LookerView) entity;
        } else {
            throw new NotFoundException(
                    "No LookerView found with qualifiedName: " + qualifiedName, "ATLAN_JAVA_CLIENT-404-003", 404, null);
        }
    }

    /**
     * Update the certificate on a LookerView.
     *
     * @param qualifiedName of the LookerView
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated LookerView, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerView updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (LookerView) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a LookerView.
     *
     * @param qualifiedName of the LookerView
     * @param name of the LookerView
     * @return the updated LookerView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerView removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (LookerView)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a LookerView.
     *
     * @param qualifiedName of the LookerView
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerView updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (LookerView) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a LookerView.
     *
     * @param qualifiedName of the LookerView
     * @param name of the LookerView
     * @return the updated LookerView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerView removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (LookerView)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a LookerView.
     *
     * @param qualifiedName of the LookerView
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the LookerView
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a LookerView.
     *
     * @param qualifiedName of the LookerView
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the LookerView
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the LookerView.
     *
     * @param qualifiedName for the LookerView
     * @param name human-readable name of the LookerView
     * @param terms the list of terms to replace on the LookerView, or null to remove all terms from the LookerView
     * @return the LookerView that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static LookerView replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (LookerView) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the LookerView, without replacing existing terms linked to the LookerView.
     * Note: this operation must make two API calls — one to retrieve the LookerView's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the LookerView
     * @param terms the list of terms to append to the LookerView
     * @return the LookerView that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static LookerView appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (LookerView) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a LookerView, without replacing all existing terms linked to the LookerView.
     * Note: this operation must make two API calls — one to retrieve the LookerView's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the LookerView
     * @param terms the list of terms to remove from the LookerView, which must be referenced by GUID
     * @return the LookerView that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static LookerView removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (LookerView) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
