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
 * Instance of a Mode query in Atlan.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class ModeQuery extends Mode {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ModeQuery";

    /** Fixed typeName for ModeQuerys. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String modeRawQuery;

    /** TBC */
    @Attribute
    Long modeReportImportCount;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ModeChart> modeCharts;

    /** TBC */
    @Attribute
    ModeReport modeReport;

    /**
     * Reference to a ModeQuery by GUID.
     *
     * @param guid the GUID of the ModeQuery to reference
     * @return reference to a ModeQuery that can be used for defining a relationship to a ModeQuery
     */
    public static ModeQuery refByGuid(String guid) {
        return ModeQuery.builder().guid(guid).build();
    }

    /**
     * Reference to a ModeQuery by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the ModeQuery to reference
     * @return reference to a ModeQuery that can be used for defining a relationship to a ModeQuery
     */
    public static ModeQuery refByQualifiedName(String qualifiedName) {
        return ModeQuery.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a ModeQuery.
     *
     * @param qualifiedName of the ModeQuery
     * @param name of the ModeQuery
     * @return the minimal request necessary to update the ModeQuery, as a builder
     */
    public static ModeQueryBuilder<?, ?> updater(String qualifiedName, String name) {
        return ModeQuery.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ModeQuery, from a potentially
     * more-complete ModeQuery object.
     *
     * @return the minimal object necessary to update the ModeQuery, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ModeQuery are not found in the initial object
     */
    @Override
    public ModeQueryBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "ModeQuery", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a ModeQuery by its GUID, complete with all of its relationships.
     *
     * @param guid of the ModeQuery to retrieve
     * @return the requested full ModeQuery, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeQuery does not exist or the provided GUID is not a ModeQuery
     */
    public static ModeQuery retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof ModeQuery) {
            return (ModeQuery) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "ModeQuery");
        }
    }

    /**
     * Retrieves a ModeQuery by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the ModeQuery to retrieve
     * @return the requested full ModeQuery, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeQuery does not exist
     */
    public static ModeQuery retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof ModeQuery) {
            return (ModeQuery) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "ModeQuery");
        }
    }

    /**
     * Restore the archived (soft-deleted) ModeQuery to active.
     *
     * @param qualifiedName for the ModeQuery
     * @return true if the ModeQuery is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a ModeQuery.
     *
     * @param qualifiedName of the ModeQuery
     * @param name of the ModeQuery
     * @return the updated ModeQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeQuery removeDescription(String qualifiedName, String name) throws AtlanException {
        return (ModeQuery) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ModeQuery.
     *
     * @param qualifiedName of the ModeQuery
     * @param name of the ModeQuery
     * @return the updated ModeQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeQuery removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (ModeQuery) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ModeQuery.
     *
     * @param qualifiedName of the ModeQuery
     * @param name of the ModeQuery
     * @return the updated ModeQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeQuery removeOwners(String qualifiedName, String name) throws AtlanException {
        return (ModeQuery) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ModeQuery.
     *
     * @param qualifiedName of the ModeQuery
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ModeQuery, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModeQuery updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (ModeQuery) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ModeQuery.
     *
     * @param qualifiedName of the ModeQuery
     * @param name of the ModeQuery
     * @return the updated ModeQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeQuery removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (ModeQuery) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ModeQuery.
     *
     * @param qualifiedName of the ModeQuery
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModeQuery updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (ModeQuery) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ModeQuery.
     *
     * @param qualifiedName of the ModeQuery
     * @param name of the ModeQuery
     * @return the updated ModeQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeQuery removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (ModeQuery) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Add classifications to a ModeQuery.
     *
     * @param qualifiedName of the ModeQuery
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the ModeQuery
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a ModeQuery.
     *
     * @param qualifiedName of the ModeQuery
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the ModeQuery
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the ModeQuery.
     *
     * @param qualifiedName for the ModeQuery
     * @param name human-readable name of the ModeQuery
     * @param terms the list of terms to replace on the ModeQuery, or null to remove all terms from the ModeQuery
     * @return the ModeQuery that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ModeQuery replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (ModeQuery) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ModeQuery, without replacing existing terms linked to the ModeQuery.
     * Note: this operation must make two API calls ??? one to retrieve the ModeQuery's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the ModeQuery
     * @param terms the list of terms to append to the ModeQuery
     * @return the ModeQuery that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ModeQuery appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (ModeQuery) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ModeQuery, without replacing all existing terms linked to the ModeQuery.
     * Note: this operation must make two API calls ??? one to retrieve the ModeQuery's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the ModeQuery
     * @param terms the list of terms to remove from the ModeQuery, which must be referenced by GUID
     * @return the ModeQuery that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ModeQuery removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (ModeQuery) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
