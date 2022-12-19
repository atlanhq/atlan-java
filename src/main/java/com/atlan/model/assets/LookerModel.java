/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class LookerModel extends Looker {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "LookerModel";

    /** Fixed typeName for LookerModels. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String projectName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<LookerExplore> explores;

    /** TBC */
    @Attribute
    LookerProject project;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<LookerField> fields;

    /** TBC */
    @Attribute
    LookerLook look;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<LookerQuery> queries;

    /**
     * Reference to a LookerModel by GUID.
     *
     * @param guid the GUID of the LookerModel to reference
     * @return reference to a LookerModel that can be used for defining a relationship to a LookerModel
     */
    public static LookerModel refByGuid(String guid) {
        return LookerModel.builder().guid(guid).build();
    }

    /**
     * Reference to a LookerModel by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the LookerModel to reference
     * @return reference to a LookerModel that can be used for defining a relationship to a LookerModel
     */
    public static LookerModel refByQualifiedName(String qualifiedName) {
        return LookerModel.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a LookerModel.
     *
     * @param qualifiedName of the LookerModel
     * @param name of the LookerModel
     * @return the minimal request necessary to update the LookerModel, as a builder
     */
    public static LookerModelBuilder<?, ?> updater(String qualifiedName, String name) {
        return LookerModel.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a LookerModel, from a potentially
     * more-complete LookerModel object.
     *
     * @return the minimal object necessary to update the LookerModel, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for LookerModel are not found in the initial object
     */
    @Override
    public LookerModelBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    "Required field for updating LookerModel is missing.",
                    String.join(",", missing),
                    "ATLAN-JAVA-CLIENT-400-404",
                    400,
                    null);
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a LookerModel by its GUID, complete with all of its relationships.
     *
     * @param guid of the LookerModel to retrieve
     * @return the requested full LookerModel, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerModel does not exist or the provided GUID is not a LookerModel
     */
    public static LookerModel retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof LookerModel) {
            return (LookerModel) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a LookerModel.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a LookerModel by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the LookerModel to retrieve
     * @return the requested full LookerModel, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerModel does not exist
     */
    public static LookerModel retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof LookerModel) {
            return (LookerModel) entity;
        } else {
            throw new NotFoundException(
                    "No LookerModel found with qualifiedName: " + qualifiedName,
                    "ATLAN_JAVA_CLIENT-404-003",
                    404,
                    null);
        }
    }

    /**
     * Restore the archived (soft-deleted) LookerModel to active.
     *
     * @param qualifiedName for the LookerModel
     * @return true if the LookerModel is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a LookerModel.
     *
     * @param qualifiedName of the LookerModel
     * @param name of the LookerModel
     * @return the updated LookerModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerModel removeDescription(String qualifiedName, String name) throws AtlanException {
        return (LookerModel)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a LookerModel.
     *
     * @param qualifiedName of the LookerModel
     * @param name of the LookerModel
     * @return the updated LookerModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerModel removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (LookerModel) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the owners from a LookerModel.
     *
     * @param qualifiedName of the LookerModel
     * @param name of the LookerModel
     * @return the updated LookerModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerModel removeOwners(String qualifiedName, String name) throws AtlanException {
        return (LookerModel)
                Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the certificate on a LookerModel.
     *
     * @param qualifiedName of the LookerModel
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated LookerModel, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerModel updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (LookerModel) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a LookerModel.
     *
     * @param qualifiedName of the LookerModel
     * @param name of the LookerModel
     * @return the updated LookerModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerModel removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (LookerModel)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a LookerModel.
     *
     * @param qualifiedName of the LookerModel
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerModel updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (LookerModel) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a LookerModel.
     *
     * @param qualifiedName of the LookerModel
     * @param name of the LookerModel
     * @return the updated LookerModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerModel removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (LookerModel)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a LookerModel.
     *
     * @param qualifiedName of the LookerModel
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the LookerModel
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a LookerModel.
     *
     * @param qualifiedName of the LookerModel
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the LookerModel
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the LookerModel.
     *
     * @param qualifiedName for the LookerModel
     * @param name human-readable name of the LookerModel
     * @param terms the list of terms to replace on the LookerModel, or null to remove all terms from the LookerModel
     * @return the LookerModel that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static LookerModel replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (LookerModel) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the LookerModel, without replacing existing terms linked to the LookerModel.
     * Note: this operation must make two API calls — one to retrieve the LookerModel's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the LookerModel
     * @param terms the list of terms to append to the LookerModel
     * @return the LookerModel that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static LookerModel appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (LookerModel) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a LookerModel, without replacing all existing terms linked to the LookerModel.
     * Note: this operation must make two API calls — one to retrieve the LookerModel's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the LookerModel
     * @param terms the list of terms to remove from the LookerModel, which must be referenced by GUID
     * @return the LookerModel that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static LookerModel removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (LookerModel) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
