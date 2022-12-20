/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
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
public class SalesforceDashboard extends Salesforce {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SalesforceDashboard";

    /** Fixed typeName for SalesforceDashboards. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String sourceId;

    /** TBC */
    @Attribute
    String dashboardType;

    /** TBC */
    @Attribute
    Long reportCount;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<SalesforceReport> reports;

    /** TBC */
    @Attribute
    SalesforceOrganization organization;

    /**
     * Reference to a SalesforceDashboard by GUID.
     *
     * @param guid the GUID of the SalesforceDashboard to reference
     * @return reference to a SalesforceDashboard that can be used for defining a relationship to a SalesforceDashboard
     */
    public static SalesforceDashboard refByGuid(String guid) {
        return SalesforceDashboard.builder().guid(guid).build();
    }

    /**
     * Reference to a SalesforceDashboard by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the SalesforceDashboard to reference
     * @return reference to a SalesforceDashboard that can be used for defining a relationship to a SalesforceDashboard
     */
    public static SalesforceDashboard refByQualifiedName(String qualifiedName) {
        return SalesforceDashboard.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a SalesforceDashboard.
     *
     * @param qualifiedName of the SalesforceDashboard
     * @param name of the SalesforceDashboard
     * @return the minimal request necessary to update the SalesforceDashboard, as a builder
     */
    public static SalesforceDashboardBuilder<?, ?> updater(String qualifiedName, String name) {
        return SalesforceDashboard.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SalesforceDashboard, from a potentially
     * more-complete SalesforceDashboard object.
     *
     * @return the minimal object necessary to update the SalesforceDashboard, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SalesforceDashboard are not found in the initial object
     */
    @Override
    public SalesforceDashboardBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    "Required field for updating SalesforceDashboard is missing.",
                    String.join(",", missing),
                    "ATLAN-JAVA-CLIENT-400-404",
                    400,
                    null);
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a SalesforceDashboard by its GUID, complete with all of its relationships.
     *
     * @param guid of the SalesforceDashboard to retrieve
     * @return the requested full SalesforceDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceDashboard does not exist or the provided GUID is not a SalesforceDashboard
     */
    public static SalesforceDashboard retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException("No asset found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (asset instanceof SalesforceDashboard) {
            return (SalesforceDashboard) asset;
        } else {
            throw new NotFoundException(
                    "Asset with GUID " + guid + " is not a SalesforceDashboard.",
                    "ATLAN_JAVA_CLIENT-404-002",
                    404,
                    null);
        }
    }

    /**
     * Retrieves a SalesforceDashboard by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the SalesforceDashboard to retrieve
     * @return the requested full SalesforceDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SalesforceDashboard does not exist
     */
    public static SalesforceDashboard retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof SalesforceDashboard) {
            return (SalesforceDashboard) asset;
        } else {
            throw new NotFoundException(
                    "No SalesforceDashboard found with qualifiedName: " + qualifiedName,
                    "ATLAN_JAVA_CLIENT-404-003",
                    404,
                    null);
        }
    }

    /**
     * Restore the archived (soft-deleted) SalesforceDashboard to active.
     *
     * @param qualifiedName for the SalesforceDashboard
     * @return true if the SalesforceDashboard is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a SalesforceDashboard.
     *
     * @param qualifiedName of the SalesforceDashboard
     * @param name of the SalesforceDashboard
     * @return the updated SalesforceDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard removeDescription(String qualifiedName, String name) throws AtlanException {
        return (SalesforceDashboard)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a SalesforceDashboard.
     *
     * @param qualifiedName of the SalesforceDashboard
     * @param name of the SalesforceDashboard
     * @return the updated SalesforceDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (SalesforceDashboard) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the owners from a SalesforceDashboard.
     *
     * @param qualifiedName of the SalesforceDashboard
     * @param name of the SalesforceDashboard
     * @return the updated SalesforceDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard removeOwners(String qualifiedName, String name) throws AtlanException {
        return (SalesforceDashboard)
                Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the certificate on a SalesforceDashboard.
     *
     * @param qualifiedName of the SalesforceDashboard
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SalesforceDashboard, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (SalesforceDashboard) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SalesforceDashboard.
     *
     * @param qualifiedName of the SalesforceDashboard
     * @param name of the SalesforceDashboard
     * @return the updated SalesforceDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (SalesforceDashboard)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a SalesforceDashboard.
     *
     * @param qualifiedName of the SalesforceDashboard
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (SalesforceDashboard)
                Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SalesforceDashboard.
     *
     * @param qualifiedName of the SalesforceDashboard
     * @param name of the SalesforceDashboard
     * @return the updated SalesforceDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (SalesforceDashboard)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a SalesforceDashboard.
     *
     * @param qualifiedName of the SalesforceDashboard
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the SalesforceDashboard
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a SalesforceDashboard.
     *
     * @param qualifiedName of the SalesforceDashboard
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the SalesforceDashboard
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the SalesforceDashboard.
     *
     * @param qualifiedName for the SalesforceDashboard
     * @param name human-readable name of the SalesforceDashboard
     * @param terms the list of terms to replace on the SalesforceDashboard, or null to remove all terms from the SalesforceDashboard
     * @return the SalesforceDashboard that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (SalesforceDashboard) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SalesforceDashboard, without replacing existing terms linked to the SalesforceDashboard.
     * Note: this operation must make two API calls — one to retrieve the SalesforceDashboard's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the SalesforceDashboard
     * @param terms the list of terms to append to the SalesforceDashboard
     * @return the SalesforceDashboard that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard appendTerms(String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        return (SalesforceDashboard) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SalesforceDashboard, without replacing all existing terms linked to the SalesforceDashboard.
     * Note: this operation must make two API calls — one to retrieve the SalesforceDashboard's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the SalesforceDashboard
     * @param terms the list of terms to remove from the SalesforceDashboard, which must be referenced by GUID
     * @return the SalesforceDashboard that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SalesforceDashboard removeTerms(String qualifiedName, List<GlossaryTerm> terms)
            throws AtlanException {
        return (SalesforceDashboard) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
