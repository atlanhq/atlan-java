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
public class MetabaseDashboard extends Metabase {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MetabaseDashboard";

    /** Fixed typeName for MetabaseDashboards. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    Long metabaseQuestionCount;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<MetabaseQuestion> metabaseQuestions;

    /** TBC */
    @Attribute
    MetabaseCollection metabaseCollection;

    /**
     * Reference to a MetabaseDashboard by GUID.
     *
     * @param guid the GUID of the MetabaseDashboard to reference
     * @return reference to a MetabaseDashboard that can be used for defining a relationship to a MetabaseDashboard
     */
    public static MetabaseDashboard refByGuid(String guid) {
        return MetabaseDashboard.builder().guid(guid).build();
    }

    /**
     * Reference to a MetabaseDashboard by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the MetabaseDashboard to reference
     * @return reference to a MetabaseDashboard that can be used for defining a relationship to a MetabaseDashboard
     */
    public static MetabaseDashboard refByQualifiedName(String qualifiedName) {
        return MetabaseDashboard.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param name of the MetabaseDashboard
     * @return the minimal request necessary to update the MetabaseDashboard, as a builder
     */
    public static MetabaseDashboardBuilder<?, ?> updater(String qualifiedName, String name) {
        return MetabaseDashboard.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a MetabaseDashboard, from a potentially
     * more-complete MetabaseDashboard object.
     *
     * @return the minimal object necessary to update the MetabaseDashboard, as a builder
     */
    @Override
    protected MetabaseDashboardBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Update the certificate on a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MetabaseDashboard, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (MetabaseDashboard) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param name of the MetabaseDashboard
     * @return the updated MetabaseDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (MetabaseDashboard)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (MetabaseDashboard) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param name of the MetabaseDashboard
     * @return the updated MetabaseDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (MetabaseDashboard)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the MetabaseDashboard
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the MetabaseDashboard
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the MetabaseDashboard.
     *
     * @param qualifiedName for the MetabaseDashboard
     * @param name human-readable name of the MetabaseDashboard
     * @param terms the list of terms to replace on the MetabaseDashboard, or null to remove all terms from the MetabaseDashboard
     * @return the MetabaseDashboard that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (MetabaseDashboard) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MetabaseDashboard, without replacing existing terms linked to the MetabaseDashboard.
     * Note: this operation must make two API calls — one to retrieve the MetabaseDashboard's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the MetabaseDashboard
     * @param terms the list of terms to append to the MetabaseDashboard
     * @return the MetabaseDashboard that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (MetabaseDashboard) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MetabaseDashboard, without replacing all existing terms linked to the MetabaseDashboard.
     * Note: this operation must make two API calls — one to retrieve the MetabaseDashboard's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the MetabaseDashboard
     * @param terms the list of terms to remove from the MetabaseDashboard, which must be referenced by GUID
     * @return the MetabaseDashboard that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (MetabaseDashboard) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
