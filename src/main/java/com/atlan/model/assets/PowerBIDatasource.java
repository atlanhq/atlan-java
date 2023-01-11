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
import java.util.Map;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a Power BI datasource in Atlan.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("cast")
public class PowerBIDatasource extends PowerBI {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "PowerBIDatasource";

    /** Fixed typeName for PowerBIDatasources. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    Map<String, String> connectionDetails;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<PowerBIDataset> datasets;

    /**
     * Reference to a PowerBIDatasource by GUID.
     *
     * @param guid the GUID of the PowerBIDatasource to reference
     * @return reference to a PowerBIDatasource that can be used for defining a relationship to a PowerBIDatasource
     */
    public static PowerBIDatasource refByGuid(String guid) {
        return PowerBIDatasource.builder().guid(guid).build();
    }

    /**
     * Reference to a PowerBIDatasource by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the PowerBIDatasource to reference
     * @return reference to a PowerBIDatasource that can be used for defining a relationship to a PowerBIDatasource
     */
    public static PowerBIDatasource refByQualifiedName(String qualifiedName) {
        return PowerBIDatasource.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a PowerBIDatasource.
     *
     * @param qualifiedName of the PowerBIDatasource
     * @param name of the PowerBIDatasource
     * @return the minimal request necessary to update the PowerBIDatasource, as a builder
     */
    public static PowerBIDatasourceBuilder<?, ?> updater(String qualifiedName, String name) {
        return PowerBIDatasource.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PowerBIDatasource, from a potentially
     * more-complete PowerBIDatasource object.
     *
     * @return the minimal object necessary to update the PowerBIDatasource, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for PowerBIDatasource are not found in the initial object
     */
    @Override
    public PowerBIDatasourceBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "PowerBIDatasource", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a PowerBIDatasource by its GUID, complete with all of its relationships.
     *
     * @param guid of the PowerBIDatasource to retrieve
     * @return the requested full PowerBIDatasource, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PowerBIDatasource does not exist or the provided GUID is not a PowerBIDatasource
     */
    public static PowerBIDatasource retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof PowerBIDatasource) {
            return (PowerBIDatasource) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "PowerBIDatasource");
        }
    }

    /**
     * Retrieves a PowerBIDatasource by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the PowerBIDatasource to retrieve
     * @return the requested full PowerBIDatasource, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PowerBIDatasource does not exist
     */
    public static PowerBIDatasource retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof PowerBIDatasource) {
            return (PowerBIDatasource) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "PowerBIDatasource");
        }
    }

    /**
     * Restore the archived (soft-deleted) PowerBIDatasource to active.
     *
     * @param qualifiedName for the PowerBIDatasource
     * @return true if the PowerBIDatasource is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a PowerBIDatasource.
     *
     * @param qualifiedName of the PowerBIDatasource
     * @param name of the PowerBIDatasource
     * @return the updated PowerBIDatasource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIDatasource removeDescription(String qualifiedName, String name) throws AtlanException {
        return (PowerBIDatasource)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a PowerBIDatasource.
     *
     * @param qualifiedName of the PowerBIDatasource
     * @param name of the PowerBIDatasource
     * @return the updated PowerBIDatasource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIDatasource removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (PowerBIDatasource) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the owners from a PowerBIDatasource.
     *
     * @param qualifiedName of the PowerBIDatasource
     * @param name of the PowerBIDatasource
     * @return the updated PowerBIDatasource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIDatasource removeOwners(String qualifiedName, String name) throws AtlanException {
        return (PowerBIDatasource)
                Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the certificate on a PowerBIDatasource.
     *
     * @param qualifiedName of the PowerBIDatasource
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated PowerBIDatasource, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIDatasource updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (PowerBIDatasource) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a PowerBIDatasource.
     *
     * @param qualifiedName of the PowerBIDatasource
     * @param name of the PowerBIDatasource
     * @return the updated PowerBIDatasource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIDatasource removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (PowerBIDatasource)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a PowerBIDatasource.
     *
     * @param qualifiedName of the PowerBIDatasource
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIDatasource updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (PowerBIDatasource) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a PowerBIDatasource.
     *
     * @param qualifiedName of the PowerBIDatasource
     * @param name of the PowerBIDatasource
     * @return the updated PowerBIDatasource, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PowerBIDatasource removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (PowerBIDatasource)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a PowerBIDatasource.
     *
     * @param qualifiedName of the PowerBIDatasource
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the PowerBIDatasource
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a PowerBIDatasource.
     *
     * @param qualifiedName of the PowerBIDatasource
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the PowerBIDatasource
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the PowerBIDatasource.
     *
     * @param qualifiedName for the PowerBIDatasource
     * @param name human-readable name of the PowerBIDatasource
     * @param terms the list of terms to replace on the PowerBIDatasource, or null to remove all terms from the PowerBIDatasource
     * @return the PowerBIDatasource that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIDatasource replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (PowerBIDatasource) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the PowerBIDatasource, without replacing existing terms linked to the PowerBIDatasource.
     * Note: this operation must make two API calls — one to retrieve the PowerBIDatasource's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the PowerBIDatasource
     * @param terms the list of terms to append to the PowerBIDatasource
     * @return the PowerBIDatasource that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIDatasource appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PowerBIDatasource) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a PowerBIDatasource, without replacing all existing terms linked to the PowerBIDatasource.
     * Note: this operation must make two API calls — one to retrieve the PowerBIDatasource's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the PowerBIDatasource
     * @param terms the list of terms to remove from the PowerBIDatasource, which must be referenced by GUID
     * @return the PowerBIDatasource that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static PowerBIDatasource removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (PowerBIDatasource) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
