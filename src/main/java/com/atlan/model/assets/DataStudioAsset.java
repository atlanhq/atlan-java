/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.GoogleDataStudioAssetType;
import com.atlan.model.relations.GuidReference;
import com.atlan.model.relations.Reference;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a Google Data Studio asset in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class DataStudioAsset extends Google {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DataStudioAsset";

    /** Fixed typeName for Google Data Studio assets. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Type of Google Data Studio asset. */
    @Attribute
    GoogleDataStudioAssetType dataStudioAssetType;

    /** Title for the asset. */
    @Attribute
    String dataStudioAssetTitle;

    /** Owner of the asset within Google Data Studio. */
    @Attribute
    String dataStudioAssetOwner;

    /** Whether the asset is soft-deleted in Google Data Studio (true) or not (false). */
    @Attribute
    Boolean isTrashedDataStudioAsset;

    /**
     * Builds the minimal object necessary to create a Google Data Studio asset.
     *
     * @param name of the asset
     * @param connectionQualifiedName unique name of the connection through which the asset is accessible
     * @param assetType type of the asset
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static DataStudioAssetBuilder<?, ?> creator(
            String name, String connectionQualifiedName, GoogleDataStudioAssetType assetType) {
        return DataStudioAsset.builder()
                .qualifiedName(connectionQualifiedName + "/" + name)
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.DATASTUDIO)
                .dataStudioAssetType(assetType);
    }

    /**
     * Builds the minimal object necessary to update a Google Data Studio asset.
     *
     * @param qualifiedName of the asset
     * @param name of the asset
     * @return the minimal object necessary to update the asset, as a builder
     */
    public static DataStudioAssetBuilder<?, ?> updater(String qualifiedName, String name) {
        return DataStudioAsset.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Google Data Studio asset, from a potentially
     * more-complete Google Data Studio asset object.
     *
     * @return the minimal object necessary to update the asset, as a builder
     */
    @Override
    protected DataStudioAssetBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Update the certificate on a Google Data Studio asset.
     *
     * @param qualifiedName of the asset
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated asset, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataStudioAsset updateCertificate(
            String qualifiedName, AtlanCertificateStatus certificate, String message) throws AtlanException {
        return (DataStudioAsset) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a Google Data Studio asset.
     *
     * @param qualifiedName of the asset
     * @param name of the asset
     * @return the updated asset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataStudioAsset removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (DataStudioAsset)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a Google Data Studio asset.
     *
     * @param qualifiedName of the asset
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DataStudioAsset updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (DataStudioAsset) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a Google Data Studio asset.
     *
     * @param qualifiedName of the asset
     * @param name of the asset
     * @return the updated asset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DataStudioAsset removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (DataStudioAsset)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a Google Data Studio asset.
     *
     * @param qualifiedName of the asset
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the asset
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a Google Data Studio asset.
     *
     * @param qualifiedName of the asset
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the asset
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the asset.
     *
     * @param qualifiedName for the asset
     * @param name human-readable name of the asset
     * @param terms the list of terms to replace on the asset, or null to remove all terms from the asset
     * @return the asset that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DataStudioAsset replaceTerms(String qualifiedName, String name, List<Reference> terms)
            throws AtlanException {
        return (DataStudioAsset) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the asset, without replacing existing terms linked to the asset.
     * Note: this operation must make two API calls — one to retrieve the asset's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the asset
     * @param terms the list of terms to append to the asset
     * @return the asset that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DataStudioAsset appendTerms(String qualifiedName, List<Reference> terms) throws AtlanException {
        return (DataStudioAsset) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a Google Data Studio asset, without replacing all existing terms linked to the asset.
     * Note: this operation must make two API calls — one to retrieve the asset's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the asset
     * @param terms the list of terms to remove from the asset, which must be referenced by GUID
     * @return the asset that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DataStudioAsset removeTerms(String qualifiedName, List<GuidReference> terms) throws AtlanException {
        return (DataStudioAsset) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
