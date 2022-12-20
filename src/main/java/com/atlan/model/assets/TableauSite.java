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
public class TableauSite extends Tableau {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TableauSite";

    /** Fixed typeName for TableauSites. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<TableauProject> projects;

    /**
     * Reference to a TableauSite by GUID.
     *
     * @param guid the GUID of the TableauSite to reference
     * @return reference to a TableauSite that can be used for defining a relationship to a TableauSite
     */
    public static TableauSite refByGuid(String guid) {
        return TableauSite.builder().guid(guid).build();
    }

    /**
     * Reference to a TableauSite by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the TableauSite to reference
     * @return reference to a TableauSite that can be used for defining a relationship to a TableauSite
     */
    public static TableauSite refByQualifiedName(String qualifiedName) {
        return TableauSite.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a TableauSite.
     *
     * @param qualifiedName of the TableauSite
     * @param name of the TableauSite
     * @return the minimal request necessary to update the TableauSite, as a builder
     */
    public static TableauSiteBuilder<?, ?> updater(String qualifiedName, String name) {
        return TableauSite.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TableauSite, from a potentially
     * more-complete TableauSite object.
     *
     * @return the minimal object necessary to update the TableauSite, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TableauSite are not found in the initial object
     */
    @Override
    public TableauSiteBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    "Required field for updating TableauSite is missing.",
                    String.join(",", missing),
                    "ATLAN-JAVA-CLIENT-400-404",
                    400,
                    null);
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a TableauSite by its GUID, complete with all of its relationships.
     *
     * @param guid of the TableauSite to retrieve
     * @return the requested full TableauSite, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauSite does not exist or the provided GUID is not a TableauSite
     */
    public static TableauSite retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException("No asset found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (asset instanceof TableauSite) {
            return (TableauSite) asset;
        } else {
            throw new NotFoundException(
                    "Asset with GUID " + guid + " is not a TableauSite.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a TableauSite by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the TableauSite to retrieve
     * @return the requested full TableauSite, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauSite does not exist
     */
    public static TableauSite retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof TableauSite) {
            return (TableauSite) asset;
        } else {
            throw new NotFoundException(
                    "No TableauSite found with qualifiedName: " + qualifiedName,
                    "ATLAN_JAVA_CLIENT-404-003",
                    404,
                    null);
        }
    }

    /**
     * Restore the archived (soft-deleted) TableauSite to active.
     *
     * @param qualifiedName for the TableauSite
     * @return true if the TableauSite is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a TableauSite.
     *
     * @param qualifiedName of the TableauSite
     * @param name of the TableauSite
     * @return the updated TableauSite, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauSite removeDescription(String qualifiedName, String name) throws AtlanException {
        return (TableauSite)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a TableauSite.
     *
     * @param qualifiedName of the TableauSite
     * @param name of the TableauSite
     * @return the updated TableauSite, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauSite removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (TableauSite) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the owners from a TableauSite.
     *
     * @param qualifiedName of the TableauSite
     * @param name of the TableauSite
     * @return the updated TableauSite, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauSite removeOwners(String qualifiedName, String name) throws AtlanException {
        return (TableauSite)
                Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the certificate on a TableauSite.
     *
     * @param qualifiedName of the TableauSite
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TableauSite, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauSite updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (TableauSite) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a TableauSite.
     *
     * @param qualifiedName of the TableauSite
     * @param name of the TableauSite
     * @return the updated TableauSite, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauSite removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (TableauSite)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a TableauSite.
     *
     * @param qualifiedName of the TableauSite
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauSite updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (TableauSite) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a TableauSite.
     *
     * @param qualifiedName of the TableauSite
     * @param name of the TableauSite
     * @return the updated TableauSite, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauSite removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (TableauSite)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a TableauSite.
     *
     * @param qualifiedName of the TableauSite
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the TableauSite
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a TableauSite.
     *
     * @param qualifiedName of the TableauSite
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the TableauSite
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the TableauSite.
     *
     * @param qualifiedName for the TableauSite
     * @param name human-readable name of the TableauSite
     * @param terms the list of terms to replace on the TableauSite, or null to remove all terms from the TableauSite
     * @return the TableauSite that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TableauSite replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (TableauSite) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the TableauSite, without replacing existing terms linked to the TableauSite.
     * Note: this operation must make two API calls — one to retrieve the TableauSite's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the TableauSite
     * @param terms the list of terms to append to the TableauSite
     * @return the TableauSite that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static TableauSite appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (TableauSite) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a TableauSite, without replacing all existing terms linked to the TableauSite.
     * Note: this operation must make two API calls — one to retrieve the TableauSite's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the TableauSite
     * @param terms the list of terms to remove from the TableauSite, which must be referenced by GUID
     * @return the TableauSite that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static TableauSite removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (TableauSite) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
