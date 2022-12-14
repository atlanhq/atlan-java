/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.*;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a Metabase question in Atlan.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class MetabaseQuestion extends Metabase {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MetabaseQuestion";

    /** Fixed typeName for MetabaseQuestions. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    Long metabaseDashboardCount;

    /** TBC */
    @Attribute
    String metabaseQueryType;

    /** TBC */
    @Attribute
    String metabaseQuery;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<MetabaseDashboard> metabaseDashboards;

    /** TBC */
    @Attribute
    MetabaseCollection metabaseCollection;

    /**
     * Reference to a MetabaseQuestion by GUID.
     *
     * @param guid the GUID of the MetabaseQuestion to reference
     * @return reference to a MetabaseQuestion that can be used for defining a relationship to a MetabaseQuestion
     */
    public static MetabaseQuestion refByGuid(String guid) {
        return MetabaseQuestion.builder().guid(guid).build();
    }

    /**
     * Reference to a MetabaseQuestion by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the MetabaseQuestion to reference
     * @return reference to a MetabaseQuestion that can be used for defining a relationship to a MetabaseQuestion
     */
    public static MetabaseQuestion refByQualifiedName(String qualifiedName) {
        return MetabaseQuestion.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a MetabaseQuestion.
     *
     * @param qualifiedName of the MetabaseQuestion
     * @param name of the MetabaseQuestion
     * @return the minimal request necessary to update the MetabaseQuestion, as a builder
     */
    public static MetabaseQuestionBuilder<?, ?> updater(String qualifiedName, String name) {
        return MetabaseQuestion.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a MetabaseQuestion, from a potentially
     * more-complete MetabaseQuestion object.
     *
     * @return the minimal object necessary to update the MetabaseQuestion, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for MetabaseQuestion are not found in the initial object
     */
    @Override
    public MetabaseQuestionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "MetabaseQuestion", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a MetabaseQuestion by its GUID, complete with all of its relationships.
     *
     * @param guid of the MetabaseQuestion to retrieve
     * @return the requested full MetabaseQuestion, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MetabaseQuestion does not exist or the provided GUID is not a MetabaseQuestion
     */
    public static MetabaseQuestion retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof MetabaseQuestion) {
            return (MetabaseQuestion) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "MetabaseQuestion");
        }
    }

    /**
     * Retrieves a MetabaseQuestion by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the MetabaseQuestion to retrieve
     * @return the requested full MetabaseQuestion, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MetabaseQuestion does not exist
     */
    public static MetabaseQuestion retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof MetabaseQuestion) {
            return (MetabaseQuestion) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "MetabaseQuestion");
        }
    }

    /**
     * Restore the archived (soft-deleted) MetabaseQuestion to active.
     *
     * @param qualifiedName for the MetabaseQuestion
     * @return true if the MetabaseQuestion is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a MetabaseQuestion.
     *
     * @param qualifiedName of the MetabaseQuestion
     * @param name of the MetabaseQuestion
     * @return the updated MetabaseQuestion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseQuestion removeDescription(String qualifiedName, String name) throws AtlanException {
        return (MetabaseQuestion) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a MetabaseQuestion.
     *
     * @param qualifiedName of the MetabaseQuestion
     * @param name of the MetabaseQuestion
     * @return the updated MetabaseQuestion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseQuestion removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (MetabaseQuestion) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a MetabaseQuestion.
     *
     * @param qualifiedName of the MetabaseQuestion
     * @param name of the MetabaseQuestion
     * @return the updated MetabaseQuestion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseQuestion removeOwners(String qualifiedName, String name) throws AtlanException {
        return (MetabaseQuestion) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a MetabaseQuestion.
     *
     * @param qualifiedName of the MetabaseQuestion
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MetabaseQuestion, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseQuestion updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (MetabaseQuestion) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MetabaseQuestion.
     *
     * @param qualifiedName of the MetabaseQuestion
     * @param name of the MetabaseQuestion
     * @return the updated MetabaseQuestion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseQuestion removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (MetabaseQuestion) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a MetabaseQuestion.
     *
     * @param qualifiedName of the MetabaseQuestion
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseQuestion updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (MetabaseQuestion) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MetabaseQuestion.
     *
     * @param qualifiedName of the MetabaseQuestion
     * @param name of the MetabaseQuestion
     * @return the updated MetabaseQuestion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseQuestion removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (MetabaseQuestion) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Add classifications to a MetabaseQuestion.
     *
     * @param qualifiedName of the MetabaseQuestion
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the MetabaseQuestion
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a MetabaseQuestion.
     *
     * @param qualifiedName of the MetabaseQuestion
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the MetabaseQuestion
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the MetabaseQuestion.
     *
     * @param qualifiedName for the MetabaseQuestion
     * @param name human-readable name of the MetabaseQuestion
     * @param terms the list of terms to replace on the MetabaseQuestion, or null to remove all terms from the MetabaseQuestion
     * @return the MetabaseQuestion that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseQuestion replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (MetabaseQuestion) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MetabaseQuestion, without replacing existing terms linked to the MetabaseQuestion.
     * Note: this operation must make two API calls ??? one to retrieve the MetabaseQuestion's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the MetabaseQuestion
     * @param terms the list of terms to append to the MetabaseQuestion
     * @return the MetabaseQuestion that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseQuestion appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (MetabaseQuestion) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MetabaseQuestion, without replacing all existing terms linked to the MetabaseQuestion.
     * Note: this operation must make two API calls ??? one to retrieve the MetabaseQuestion's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the MetabaseQuestion
     * @param terms the list of terms to remove from the MetabaseQuestion, which must be referenced by GUID
     * @return the MetabaseQuestion that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseQuestion removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (MetabaseQuestion) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
