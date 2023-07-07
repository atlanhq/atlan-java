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
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * dbt Test Assets
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class DbtTest extends Asset implements IDbtTest, IDbt, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DbtTest";

    /** Fixed typeName for DbtTests. */
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
    String dbtConnectionContext;

    /** TBC */
    @Attribute
    String dbtEnvironmentDbtVersion;

    /** TBC */
    @Attribute
    String dbtEnvironmentName;

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
    String dbtMeta;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtModelColumn> dbtModelColumns;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtModel> dbtModels;

    /** TBC */
    @Attribute
    String dbtPackageName;

    /** TBC */
    @Attribute
    String dbtProjectName;

    /** TBC */
    @Attribute
    String dbtSemanticLayerProxyUrl;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtSource> dbtSources;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> dbtTags;

    /** The compiled code of a test ( tests in dbt can be defined using python ) */
    @Attribute
    String dbtTestCompiledCode;

    /** The compiled sql of a test */
    @Attribute
    String dbtTestCompiledSQL;

    /** The error message in the case of state being "error" */
    @Attribute
    String dbtTestError;

    /** The language in which a dbt test is written. Example: sql,python */
    @Attribute
    String dbtTestLanguage;

    /** The raw code of a test ( tests in dbt can be defined using python ) */
    @Attribute
    String dbtTestRawCode;

    /** The raw sql of a test */
    @Attribute
    String dbtTestRawSQL;

    /** The test results. Can be one of, in order of severity, "error", "fail", "warn", "pass" */
    @Attribute
    String dbtTestState;

    /** Status provides the details of the results of a test. For errors, it reads "ERROR". */
    @Attribute
    String dbtTestStatus;

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
    @Singular
    SortedSet<ISQL> sqlAssets;

    /**
     * Reference to a DbtTest by GUID.
     *
     * @param guid the GUID of the DbtTest to reference
     * @return reference to a DbtTest that can be used for defining a relationship to a DbtTest
     */
    public static DbtTest refByGuid(String guid) {
        return DbtTest.builder().guid(guid).build();
    }

    /**
     * Reference to a DbtTest by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the DbtTest to reference
     * @return reference to a DbtTest that can be used for defining a relationship to a DbtTest
     */
    public static DbtTest refByQualifiedName(String qualifiedName) {
        return DbtTest.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a DbtTest by its GUID, complete with all of its relationships.
     *
     * @param guid of the DbtTest to retrieve
     * @return the requested full DbtTest, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtTest does not exist or the provided GUID is not a DbtTest
     */
    public static DbtTest retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a DbtTest by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the DbtTest to retrieve
     * @return the requested full DbtTest, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtTest does not exist or the provided GUID is not a DbtTest
     */
    public static DbtTest retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof DbtTest) {
            return (DbtTest) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "DbtTest");
        }
    }

    /**
     * Retrieves a DbtTest by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the DbtTest to retrieve
     * @return the requested full DbtTest, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtTest does not exist
     */
    public static DbtTest retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a DbtTest by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the DbtTest to retrieve
     * @return the requested full DbtTest, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtTest does not exist
     */
    public static DbtTest retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof DbtTest) {
            return (DbtTest) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "DbtTest");
        }
    }

    /**
     * Restore the archived (soft-deleted) DbtTest to active.
     *
     * @param qualifiedName for the DbtTest
     * @return true if the DbtTest is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) DbtTest to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DbtTest
     * @return true if the DbtTest is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a DbtTest.
     *
     * @param qualifiedName of the DbtTest
     * @param name of the DbtTest
     * @return the minimal request necessary to update the DbtTest, as a builder
     */
    public static DbtTestBuilder<?, ?> updater(String qualifiedName, String name) {
        return DbtTest.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DbtTest, from a potentially
     * more-complete DbtTest object.
     *
     * @return the minimal object necessary to update the DbtTest, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DbtTest are not found in the initial object
     */
    @Override
    public DbtTestBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "DbtTest", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a DbtTest.
     *
     * @param qualifiedName of the DbtTest
     * @param name of the DbtTest
     * @return the updated DbtTest, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTest removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a DbtTest.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DbtTest
     * @param name of the DbtTest
     * @return the updated DbtTest, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTest removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtTest) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DbtTest.
     *
     * @param qualifiedName of the DbtTest
     * @param name of the DbtTest
     * @return the updated DbtTest, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTest removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a DbtTest.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DbtTest
     * @param name of the DbtTest
     * @return the updated DbtTest, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTest removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtTest) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DbtTest.
     *
     * @param qualifiedName of the DbtTest
     * @param name of the DbtTest
     * @return the updated DbtTest, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTest removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a DbtTest.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DbtTest's owners
     * @param qualifiedName of the DbtTest
     * @param name of the DbtTest
     * @return the updated DbtTest, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTest removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (DbtTest) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DbtTest.
     *
     * @param qualifiedName of the DbtTest
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DbtTest, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtTest updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a DbtTest.
     *
     * @param client connectivity to the Atlan tenant on which to update the DbtTest's certificate
     * @param qualifiedName of the DbtTest
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DbtTest, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtTest updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DbtTest) Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DbtTest.
     *
     * @param qualifiedName of the DbtTest
     * @param name of the DbtTest
     * @return the updated DbtTest, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTest removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a DbtTest.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DbtTest's certificate
     * @param qualifiedName of the DbtTest
     * @param name of the DbtTest
     * @return the updated DbtTest, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTest removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtTest) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DbtTest.
     *
     * @param qualifiedName of the DbtTest
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtTest updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a DbtTest.
     *
     * @param client connectivity to the Atlan tenant on which to update the DbtTest's announcement
     * @param qualifiedName of the DbtTest
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtTest updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (DbtTest) Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DbtTest.
     *
     * @param qualifiedName of the DbtTest
     * @param name of the DbtTest
     * @return the updated DbtTest, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTest removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a DbtTest.
     *
     * @param client connectivity to the Atlan client from which to remove the DbtTest's announcement
     * @param qualifiedName of the DbtTest
     * @param name of the DbtTest
     * @return the updated DbtTest, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTest removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtTest) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DbtTest.
     *
     * @param qualifiedName for the DbtTest
     * @param name human-readable name of the DbtTest
     * @param terms the list of terms to replace on the DbtTest, or null to remove all terms from the DbtTest
     * @return the DbtTest that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DbtTest replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the DbtTest.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DbtTest's assigned terms
     * @param qualifiedName for the DbtTest
     * @param name human-readable name of the DbtTest
     * @param terms the list of terms to replace on the DbtTest, or null to remove all terms from the DbtTest
     * @return the DbtTest that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DbtTest replaceTerms(AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtTest) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DbtTest, without replacing existing terms linked to the DbtTest.
     * Note: this operation must make two API calls — one to retrieve the DbtTest's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the DbtTest
     * @param terms the list of terms to append to the DbtTest
     * @return the DbtTest that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DbtTest appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the DbtTest, without replacing existing terms linked to the DbtTest.
     * Note: this operation must make two API calls — one to retrieve the DbtTest's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DbtTest
     * @param qualifiedName for the DbtTest
     * @param terms the list of terms to append to the DbtTest
     * @return the DbtTest that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DbtTest appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtTest) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DbtTest, without replacing all existing terms linked to the DbtTest.
     * Note: this operation must make two API calls — one to retrieve the DbtTest's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the DbtTest
     * @param terms the list of terms to remove from the DbtTest, which must be referenced by GUID
     * @return the DbtTest that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DbtTest removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a DbtTest, without replacing all existing terms linked to the DbtTest.
     * Note: this operation must make two API calls — one to retrieve the DbtTest's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DbtTest
     * @param qualifiedName for the DbtTest
     * @param terms the list of terms to remove from the DbtTest, which must be referenced by GUID
     * @return the DbtTest that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DbtTest removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtTest) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DbtTest, without replacing existing Atlan tags linked to the DbtTest.
     * Note: this operation must make two API calls — one to retrieve the DbtTest's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DbtTest
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DbtTest
     */
    public static DbtTest appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtTest, without replacing existing Atlan tags linked to the DbtTest.
     * Note: this operation must make two API calls — one to retrieve the DbtTest's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DbtTest
     * @param qualifiedName of the DbtTest
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DbtTest
     */
    public static DbtTest appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (DbtTest) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtTest, without replacing existing Atlan tags linked to the DbtTest.
     * Note: this operation must make two API calls — one to retrieve the DbtTest's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DbtTest
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DbtTest
     */
    public static DbtTest appendAtlanTags(
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
     * Add Atlan tags to a DbtTest, without replacing existing Atlan tags linked to the DbtTest.
     * Note: this operation must make two API calls — one to retrieve the DbtTest's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DbtTest
     * @param qualifiedName of the DbtTest
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DbtTest
     */
    public static DbtTest appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DbtTest) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a DbtTest.
     *
     * @param qualifiedName of the DbtTest
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtTest
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtTest.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the DbtTest
     * @param qualifiedName of the DbtTest
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtTest
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtTest.
     *
     * @param qualifiedName of the DbtTest
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtTest
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
     * Add Atlan tags to a DbtTest.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the DbtTest
     * @param qualifiedName of the DbtTest
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtTest
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
     * Remove an Atlan tag from a DbtTest.
     *
     * @param qualifiedName of the DbtTest
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DbtTest
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a DbtTest.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DbtTest
     * @param qualifiedName of the DbtTest
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DbtTest
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
