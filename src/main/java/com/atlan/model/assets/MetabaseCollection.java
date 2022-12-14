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
 * Instance of a Metabase collection in Atlan.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class MetabaseCollection extends Metabase {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MetabaseCollection";

    /** Fixed typeName for MetabaseCollections. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String metabaseSlug;

    /** TBC */
    @Attribute
    String metabaseColor;

    /** TBC */
    @Attribute
    String metabaseNamespace;

    /** TBC */
    @Attribute
    Boolean metabaseIsPersonalCollection;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<MetabaseDashboard> metabaseDashboards;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<MetabaseQuestion> metabaseQuestions;

    /**
     * Reference to a MetabaseCollection by GUID.
     *
     * @param guid the GUID of the MetabaseCollection to reference
     * @return reference to a MetabaseCollection that can be used for defining a relationship to a MetabaseCollection
     */
    public static MetabaseCollection refByGuid(String guid) {
        return MetabaseCollection.builder().guid(guid).build();
    }

    /**
     * Reference to a MetabaseCollection by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the MetabaseCollection to reference
     * @return reference to a MetabaseCollection that can be used for defining a relationship to a MetabaseCollection
     */
    public static MetabaseCollection refByQualifiedName(String qualifiedName) {
        return MetabaseCollection.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a MetabaseCollection.
     *
     * @param qualifiedName of the MetabaseCollection
     * @param name of the MetabaseCollection
     * @return the minimal request necessary to update the MetabaseCollection, as a builder
     */
    public static MetabaseCollectionBuilder<?, ?> updater(String qualifiedName, String name) {
        return MetabaseCollection.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a MetabaseCollection, from a potentially
     * more-complete MetabaseCollection object.
     *
     * @return the minimal object necessary to update the MetabaseCollection, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for MetabaseCollection are not found in the initial object
     */
    @Override
    public MetabaseCollectionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "MetabaseCollection", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a MetabaseCollection by its GUID, complete with all of its relationships.
     *
     * @param guid of the MetabaseCollection to retrieve
     * @return the requested full MetabaseCollection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MetabaseCollection does not exist or the provided GUID is not a MetabaseCollection
     */
    public static MetabaseCollection retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof MetabaseCollection) {
            return (MetabaseCollection) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "MetabaseCollection");
        }
    }

    /**
     * Retrieves a MetabaseCollection by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the MetabaseCollection to retrieve
     * @return the requested full MetabaseCollection, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MetabaseCollection does not exist
     */
    public static MetabaseCollection retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof MetabaseCollection) {
            return (MetabaseCollection) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "MetabaseCollection");
        }
    }

    /**
     * Restore the archived (soft-deleted) MetabaseCollection to active.
     *
     * @param qualifiedName for the MetabaseCollection
     * @return true if the MetabaseCollection is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a MetabaseCollection.
     *
     * @param qualifiedName of the MetabaseCollection
     * @param name of the MetabaseCollection
     * @return the updated MetabaseCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection removeDescription(String qualifiedName, String name) throws AtlanException {
        return (MetabaseCollection) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a MetabaseCollection.
     *
     * @param qualifiedName of the MetabaseCollection
     * @param name of the MetabaseCollection
     * @return the updated MetabaseCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (MetabaseCollection) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a MetabaseCollection.
     *
     * @param qualifiedName of the MetabaseCollection
     * @param name of the MetabaseCollection
     * @return the updated MetabaseCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection removeOwners(String qualifiedName, String name) throws AtlanException {
        return (MetabaseCollection) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a MetabaseCollection.
     *
     * @param qualifiedName of the MetabaseCollection
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MetabaseCollection, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (MetabaseCollection) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MetabaseCollection.
     *
     * @param qualifiedName of the MetabaseCollection
     * @param name of the MetabaseCollection
     * @return the updated MetabaseCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (MetabaseCollection) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a MetabaseCollection.
     *
     * @param qualifiedName of the MetabaseCollection
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (MetabaseCollection) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MetabaseCollection.
     *
     * @param qualifiedName of the MetabaseCollection
     * @param name of the MetabaseCollection
     * @return the updated MetabaseCollection, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (MetabaseCollection) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Add classifications to a MetabaseCollection.
     *
     * @param qualifiedName of the MetabaseCollection
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the MetabaseCollection
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a MetabaseCollection.
     *
     * @param qualifiedName of the MetabaseCollection
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the MetabaseCollection
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the MetabaseCollection.
     *
     * @param qualifiedName for the MetabaseCollection
     * @param name human-readable name of the MetabaseCollection
     * @param terms the list of terms to replace on the MetabaseCollection, or null to remove all terms from the MetabaseCollection
     * @return the MetabaseCollection that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (MetabaseCollection) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MetabaseCollection, without replacing existing terms linked to the MetabaseCollection.
     * Note: this operation must make two API calls ??? one to retrieve the MetabaseCollection's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the MetabaseCollection
     * @param terms the list of terms to append to the MetabaseCollection
     * @return the MetabaseCollection that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (MetabaseCollection) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MetabaseCollection, without replacing all existing terms linked to the MetabaseCollection.
     * Note: this operation must make two API calls ??? one to retrieve the MetabaseCollection's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the MetabaseCollection
     * @param terms the list of terms to remove from the MetabaseCollection, which must be referenced by GUID
     * @return the MetabaseCollection that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseCollection removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (MetabaseCollection) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
