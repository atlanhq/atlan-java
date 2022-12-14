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
 * Instance of a dbt model in Atlan.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class DbtModel extends Dbt {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DbtModel";

    /** Fixed typeName for DbtModels. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String dbtStatus;

    /** TBC */
    @Attribute
    String dbtError;

    /** TBC */
    @Attribute
    String dbtRawSQL;

    /** TBC */
    @Attribute
    String dbtCompiledSQL;

    /** TBC */
    @Attribute
    String dbtStats;

    /** TBC */
    @Attribute
    String dbtMaterializationType;

    /** TBC */
    @Attribute
    Long dbtModelCompileStartedAt;

    /** TBC */
    @Attribute
    Long dbtModelCompileCompletedAt;

    /** TBC */
    @Attribute
    Long dbtModelExecuteStartedAt;

    /** TBC */
    @Attribute
    Long dbtModelExecuteCompletedAt;

    /** TBC */
    @Attribute
    Double dbtModelExecutionTime;

    /** TBC */
    @Attribute
    Long dbtModelRunGeneratedAt;

    /** TBC */
    @Attribute
    Double dbtModelRunElapsedTime;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<DbtMetric> dbtMetrics;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<DbtModelColumn> dbtModelColumns;

    /** TBC */
    @Attribute
    SQL sqlAsset;

    /**
     * Reference to a DbtModel by GUID.
     *
     * @param guid the GUID of the DbtModel to reference
     * @return reference to a DbtModel that can be used for defining a relationship to a DbtModel
     */
    public static DbtModel refByGuid(String guid) {
        return DbtModel.builder().guid(guid).build();
    }

    /**
     * Reference to a DbtModel by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the DbtModel to reference
     * @return reference to a DbtModel that can be used for defining a relationship to a DbtModel
     */
    public static DbtModel refByQualifiedName(String qualifiedName) {
        return DbtModel.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a DbtModel.
     *
     * @param qualifiedName of the DbtModel
     * @param name of the DbtModel
     * @return the minimal request necessary to update the DbtModel, as a builder
     */
    public static DbtModelBuilder<?, ?> updater(String qualifiedName, String name) {
        return DbtModel.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DbtModel, from a potentially
     * more-complete DbtModel object.
     *
     * @return the minimal object necessary to update the DbtModel, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DbtModel are not found in the initial object
     */
    @Override
    public DbtModelBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "DbtModel", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a DbtModel by its GUID, complete with all of its relationships.
     *
     * @param guid of the DbtModel to retrieve
     * @return the requested full DbtModel, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtModel does not exist or the provided GUID is not a DbtModel
     */
    public static DbtModel retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof DbtModel) {
            return (DbtModel) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "DbtModel");
        }
    }

    /**
     * Retrieves a DbtModel by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the DbtModel to retrieve
     * @return the requested full DbtModel, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtModel does not exist
     */
    public static DbtModel retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof DbtModel) {
            return (DbtModel) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "DbtModel");
        }
    }

    /**
     * Restore the archived (soft-deleted) DbtModel to active.
     *
     * @param qualifiedName for the DbtModel
     * @return true if the DbtModel is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a DbtModel.
     *
     * @param qualifiedName of the DbtModel
     * @param name of the DbtModel
     * @return the updated DbtModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModel removeDescription(String qualifiedName, String name) throws AtlanException {
        return (DbtModel) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DbtModel.
     *
     * @param qualifiedName of the DbtModel
     * @param name of the DbtModel
     * @return the updated DbtModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModel removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (DbtModel) Asset.removeUserDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DbtModel.
     *
     * @param qualifiedName of the DbtModel
     * @param name of the DbtModel
     * @return the updated DbtModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModel removeOwners(String qualifiedName, String name) throws AtlanException {
        return (DbtModel) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DbtModel.
     *
     * @param qualifiedName of the DbtModel
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DbtModel, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtModel updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (DbtModel) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DbtModel.
     *
     * @param qualifiedName of the DbtModel
     * @param name of the DbtModel
     * @return the updated DbtModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModel removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (DbtModel) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DbtModel.
     *
     * @param qualifiedName of the DbtModel
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtModel updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (DbtModel) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DbtModel.
     *
     * @param qualifiedName of the DbtModel
     * @param name of the DbtModel
     * @return the updated DbtModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModel removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (DbtModel) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Add classifications to a DbtModel.
     *
     * @param qualifiedName of the DbtModel
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the DbtModel
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a DbtModel.
     *
     * @param qualifiedName of the DbtModel
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the DbtModel
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the DbtModel.
     *
     * @param qualifiedName for the DbtModel
     * @param name human-readable name of the DbtModel
     * @param terms the list of terms to replace on the DbtModel, or null to remove all terms from the DbtModel
     * @return the DbtModel that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DbtModel replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (DbtModel) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DbtModel, without replacing existing terms linked to the DbtModel.
     * Note: this operation must make two API calls ??? one to retrieve the DbtModel's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the DbtModel
     * @param terms the list of terms to append to the DbtModel
     * @return the DbtModel that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DbtModel appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (DbtModel) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DbtModel, without replacing all existing terms linked to the DbtModel.
     * Note: this operation must make two API calls ??? one to retrieve the DbtModel's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the DbtModel
     * @param terms the list of terms to remove from the DbtModel, which must be referenced by GUID
     * @return the DbtModel that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DbtModel removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (DbtModel) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
