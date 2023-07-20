/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a dbt model in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class DbtModel extends Asset implements IDbtModel, IDbt, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DbtModel";

    /** Fixed typeName for DbtModels. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String dbtAccountName;

    /** TBC */
    @Attribute
    String dbtAlias;

    /** TBC */
    @Attribute
    String dbtCompiledSQL;

    /** TBC */
    @Attribute
    String dbtConnectionContext;

    /** TBC */
    @Attribute
    String dbtEnvironmentDbtVersion;

    /** TBC */
    @Attribute
    String dbtEnvironmentName;

    /** TBC */
    @Attribute
    String dbtError;

    /** TBC */
    @Attribute
    Long dbtJobLastRun;

    /** TBC */
    @Attribute
    String dbtJobName;

    /** TBC */
    @Attribute
    Long dbtJobNextRun;

    /** TBC */
    @Attribute
    String dbtJobNextRunHumanized;

    /** TBC */
    @Attribute
    String dbtJobSchedule;

    /** TBC */
    @Attribute
    String dbtJobScheduleCronHumanized;

    /** TBC */
    @Attribute
    String dbtJobStatus;

    /** TBC */
    @Attribute
    String dbtMaterializationType;

    /** TBC */
    @Attribute
    String dbtMeta;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtMetric> dbtMetrics;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtModelColumn> dbtModelColumns;

    /** TBC */
    @Attribute
    Long dbtModelCompileCompletedAt;

    /** TBC */
    @Attribute
    Long dbtModelCompileStartedAt;

    /** TBC */
    @Attribute
    Long dbtModelExecuteCompletedAt;

    /** TBC */
    @Attribute
    Long dbtModelExecuteStartedAt;

    /** TBC */
    @Attribute
    Double dbtModelExecutionTime;

    /** TBC */
    @Attribute
    Double dbtModelRunElapsedTime;

    /** TBC */
    @Attribute
    Long dbtModelRunGeneratedAt;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISQL> dbtModelSqlAssets;

    /** TBC */
    @Attribute
    String dbtPackageName;

    /** TBC */
    @Attribute
    String dbtProjectName;

    /** TBC */
    @Attribute
    String dbtRawSQL;

    /** TBC */
    @Attribute
    String dbtSemanticLayerProxyUrl;

    /** TBC */
    @Attribute
    String dbtStats;

    /** TBC */
    @Attribute
    String dbtStatus;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> dbtTags;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtTest> dbtTests;

    /** TBC */
    @Attribute
    String dbtUniqueId;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    @JsonProperty("sqlAsset")
    ISQL primarySqlAsset;

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
     * Retrieves a DbtModel by its GUID, complete with all of its relationships.
     *
     * @param guid of the DbtModel to retrieve
     * @return the requested full DbtModel, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtModel does not exist or the provided GUID is not a DbtModel
     */
    public static DbtModel retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a DbtModel by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the DbtModel to retrieve
     * @return the requested full DbtModel, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtModel does not exist or the provided GUID is not a DbtModel
     */
    public static DbtModel retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
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
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a DbtModel by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the DbtModel to retrieve
     * @return the requested full DbtModel, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtModel does not exist
     */
    public static DbtModel retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
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
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) DbtModel to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DbtModel
     * @return true if the DbtModel is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
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
     * Remove the system description from a DbtModel.
     *
     * @param qualifiedName of the DbtModel
     * @param name of the DbtModel
     * @return the updated DbtModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModel removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a DbtModel.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DbtModel
     * @param name of the DbtModel
     * @return the updated DbtModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModel removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtModel) Asset.removeDescription(client, updater(qualifiedName, name));
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
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a DbtModel.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DbtModel
     * @param name of the DbtModel
     * @return the updated DbtModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModel removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtModel) Asset.removeUserDescription(client, updater(qualifiedName, name));
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
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a DbtModel.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DbtModel's owners
     * @param qualifiedName of the DbtModel
     * @param name of the DbtModel
     * @return the updated DbtModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModel removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (DbtModel) Asset.removeOwners(client, updater(qualifiedName, name));
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
    public static DbtModel updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a DbtModel.
     *
     * @param client connectivity to the Atlan tenant on which to update the DbtModel's certificate
     * @param qualifiedName of the DbtModel
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DbtModel, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtModel updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DbtModel) Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
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
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a DbtModel.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DbtModel's certificate
     * @param qualifiedName of the DbtModel
     * @param name of the DbtModel
     * @return the updated DbtModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModel removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtModel) Asset.removeCertificate(client, updater(qualifiedName, name));
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
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a DbtModel.
     *
     * @param client connectivity to the Atlan tenant on which to update the DbtModel's announcement
     * @param qualifiedName of the DbtModel
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtModel updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (DbtModel) Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
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
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a DbtModel.
     *
     * @param client connectivity to the Atlan client from which to remove the DbtModel's announcement
     * @param qualifiedName of the DbtModel
     * @param name of the DbtModel
     * @return the updated DbtModel, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModel removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtModel) Asset.removeAnnouncement(client, updater(qualifiedName, name));
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
    public static DbtModel replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the DbtModel.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DbtModel's assigned terms
     * @param qualifiedName for the DbtModel
     * @param name human-readable name of the DbtModel
     * @param terms the list of terms to replace on the DbtModel, or null to remove all terms from the DbtModel
     * @return the DbtModel that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DbtModel replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (DbtModel) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DbtModel, without replacing existing terms linked to the DbtModel.
     * Note: this operation must make two API calls — one to retrieve the DbtModel's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the DbtModel
     * @param terms the list of terms to append to the DbtModel
     * @return the DbtModel that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DbtModel appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the DbtModel, without replacing existing terms linked to the DbtModel.
     * Note: this operation must make two API calls — one to retrieve the DbtModel's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DbtModel
     * @param qualifiedName for the DbtModel
     * @param terms the list of terms to append to the DbtModel
     * @return the DbtModel that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DbtModel appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtModel) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DbtModel, without replacing all existing terms linked to the DbtModel.
     * Note: this operation must make two API calls — one to retrieve the DbtModel's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the DbtModel
     * @param terms the list of terms to remove from the DbtModel, which must be referenced by GUID
     * @return the DbtModel that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DbtModel removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a DbtModel, without replacing all existing terms linked to the DbtModel.
     * Note: this operation must make two API calls — one to retrieve the DbtModel's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DbtModel
     * @param qualifiedName for the DbtModel
     * @param terms the list of terms to remove from the DbtModel, which must be referenced by GUID
     * @return the DbtModel that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DbtModel removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtModel) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DbtModel, without replacing existing Atlan tags linked to the DbtModel.
     * Note: this operation must make two API calls — one to retrieve the DbtModel's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DbtModel
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DbtModel
     */
    public static DbtModel appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtModel, without replacing existing Atlan tags linked to the DbtModel.
     * Note: this operation must make two API calls — one to retrieve the DbtModel's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DbtModel
     * @param qualifiedName of the DbtModel
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DbtModel
     */
    public static DbtModel appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (DbtModel) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtModel, without replacing existing Atlan tags linked to the DbtModel.
     * Note: this operation must make two API calls — one to retrieve the DbtModel's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DbtModel
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DbtModel
     */
    public static DbtModel appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return appendAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a DbtModel, without replacing existing Atlan tags linked to the DbtModel.
     * Note: this operation must make two API calls — one to retrieve the DbtModel's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DbtModel
     * @param qualifiedName of the DbtModel
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DbtModel
     */
    public static DbtModel appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DbtModel) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a DbtModel.
     *
     * @param qualifiedName of the DbtModel
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtModel
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtModel.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the DbtModel
     * @param qualifiedName of the DbtModel
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtModel
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtModel.
     *
     * @param qualifiedName of the DbtModel
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtModel
     * @deprecated see {@link #appendAtlanTags(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    public static void addAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        addAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a DbtModel.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the DbtModel
     * @param qualifiedName of the DbtModel
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtModel
     * @deprecated see {@link #appendAtlanTags(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    public static void addAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a DbtModel.
     *
     * @param qualifiedName of the DbtModel
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DbtModel
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a DbtModel.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DbtModel
     * @param qualifiedName of the DbtModel
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DbtModel
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
