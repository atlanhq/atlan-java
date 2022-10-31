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
public class SalesforceField extends Salesforce {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SalesforceField";

    /** Fixed typeName for SalesforceFields. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String dataType;

    /** TBC */
    @Attribute
    String objectQualifiedName;

    /** TBC */
    @Attribute
    Integer order;

    /** TBC */
    @Attribute
    String inlineHelpText;

    /** TBC */
    @Attribute
    Boolean isCalculated;

    /** TBC */
    @Attribute
    String formula;

    /** TBC */
    @Attribute
    Boolean isCaseSensitive;

    /** TBC */
    @Attribute
    Boolean isEncrypted;

    /** TBC */
    @Attribute
    Long maxLength;

    /** TBC */
    @Attribute
    Boolean isNullable;

    /** TBC */
    @Attribute
    Integer precision;

    /** TBC */
    @Attribute
    Double numericScale;

    /** TBC */
    @Attribute
    Boolean isUnique;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> picklistValues;

    /** TBC */
    @Attribute
    Boolean isPolymorphicForeignKey;

    /** TBC */
    @Attribute
    String defaultValueFormula;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<SalesforceObject> lookupObjects;

    /** TBC */
    @Attribute
    SalesforceObject object;

    /**
     * Reference to a SalesforceField by GUID.
     *
     * @param guid the GUID of the SalesforceField to reference
     * @return reference to a SalesforceField that can be used for defining a relationship to a SalesforceField
     */
    public static SalesforceField refByGuid(String guid) {
        return SalesforceField.builder().guid(guid).build();
    }

    /**
     * Reference to a SalesforceField by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the SalesforceField to reference
     * @return reference to a SalesforceField that can be used for defining a relationship to a SalesforceField
     */
    public static SalesforceField refByQualifiedName(String qualifiedName) {
        return SalesforceField.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a SalesforceField.
     *
     * @param qualifiedName of the SalesforceField
     * @param name of the SalesforceField
     * @return the minimal request necessary to update the SalesforceField, as a builder
     */
    public static SalesforceFieldBuilder<?, ?> updater(String qualifiedName, String name) {
        return SalesforceField.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SalesforceField, from a potentially
     * more-complete SalesforceField object.
     *
     * @return the minimal object necessary to update the SalesforceField, as a builder
     */
    @Override
    protected SalesforceFieldBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a SalesforceField by its GUID, complete with all of its relationships.
     *
     * @param guid of the SalesforceField to retrieve
     * @return the requested full SalesforceField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceField does not exist or the provided GUID is not a SalesforceField
     */
    public static SalesforceField retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof SalesforceField) {
            return (SalesforceField) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a SalesforceField.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a SalesforceField by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the SalesforceField to retrieve
     * @return the requested full SalesforceField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceField does not exist
     */
    public static SalesforceField retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof SalesforceField) {
            return (SalesforceField) entity;
        } else {
            throw new NotFoundException(
                    "No SalesforceField found with qualifiedName: " + qualifiedName,
                    "ATLAN_JAVA_CLIENT-404-003",
                    404,
                    null);
        }
    }

    /**
     * Update the certificate on a SalesforceField.
     *
     * @param qualifiedName of the SalesforceField
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SalesforceField, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceField updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (SalesforceField) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SalesforceField.
     *
     * @param qualifiedName of the SalesforceField
     * @param name of the SalesforceField
     * @return the updated SalesforceField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceField removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (SalesforceField)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a SalesforceField.
     *
     * @param qualifiedName of the SalesforceField
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceField updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (SalesforceField) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SalesforceField.
     *
     * @param qualifiedName of the SalesforceField
     * @param name of the SalesforceField
     * @return the updated SalesforceField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceField removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (SalesforceField)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a SalesforceField.
     *
     * @param qualifiedName of the SalesforceField
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the SalesforceField
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a SalesforceField.
     *
     * @param qualifiedName of the SalesforceField
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the SalesforceField
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the SalesforceField.
     *
     * @param qualifiedName for the SalesforceField
     * @param name human-readable name of the SalesforceField
     * @param terms the list of terms to replace on the SalesforceField, or null to remove all terms from the SalesforceField
     * @return the SalesforceField that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceField replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (SalesforceField) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SalesforceField, without replacing existing terms linked to the SalesforceField.
     * Note: this operation must make two API calls — one to retrieve the SalesforceField's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the SalesforceField
     * @param terms the list of terms to append to the SalesforceField
     * @return the SalesforceField that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceField appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (SalesforceField) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SalesforceField, without replacing all existing terms linked to the SalesforceField.
     * Note: this operation must make two API calls — one to retrieve the SalesforceField's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the SalesforceField
     * @param terms the list of terms to remove from the SalesforceField, which must be referenced by GUID
     * @return the SalesforceField that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceField removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (SalesforceField) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
