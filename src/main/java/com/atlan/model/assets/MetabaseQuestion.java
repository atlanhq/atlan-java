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
 * Instance of a Metabase question in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class MetabaseQuestion extends Asset
        implements IMetabaseQuestion, IMetabase, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MetabaseQuestion";

    /** Fixed typeName for MetabaseQuestions. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    IMetabaseCollection metabaseCollection;

    /** TBC */
    @Attribute
    String metabaseCollectionName;

    /** TBC */
    @Attribute
    String metabaseCollectionQualifiedName;

    /** TBC */
    @Attribute
    Long metabaseDashboardCount;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IMetabaseDashboard> metabaseDashboards;

    /** TBC */
    @Attribute
    String metabaseQuery;

    /** TBC */
    @Attribute
    String metabaseQueryType;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

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
     * Retrieves a MetabaseQuestion by its GUID, complete with all of its relationships.
     *
     * @param guid of the MetabaseQuestion to retrieve
     * @return the requested full MetabaseQuestion, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MetabaseQuestion does not exist or the provided GUID is not a MetabaseQuestion
     */
    public static MetabaseQuestion retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a MetabaseQuestion by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the MetabaseQuestion to retrieve
     * @return the requested full MetabaseQuestion, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MetabaseQuestion does not exist or the provided GUID is not a MetabaseQuestion
     */
    public static MetabaseQuestion retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
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
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a MetabaseQuestion by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the MetabaseQuestion to retrieve
     * @return the requested full MetabaseQuestion, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MetabaseQuestion does not exist
     */
    public static MetabaseQuestion retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
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
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) MetabaseQuestion to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the MetabaseQuestion
     * @return true if the MetabaseQuestion is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
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
     * Remove the system description from a MetabaseQuestion.
     *
     * @param qualifiedName of the MetabaseQuestion
     * @param name of the MetabaseQuestion
     * @return the updated MetabaseQuestion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseQuestion removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a MetabaseQuestion.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MetabaseQuestion
     * @param name of the MetabaseQuestion
     * @return the updated MetabaseQuestion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseQuestion removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MetabaseQuestion) Asset.removeDescription(client, updater(qualifiedName, name));
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
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a MetabaseQuestion.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MetabaseQuestion
     * @param name of the MetabaseQuestion
     * @return the updated MetabaseQuestion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseQuestion removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MetabaseQuestion) Asset.removeUserDescription(client, updater(qualifiedName, name));
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
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a MetabaseQuestion.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MetabaseQuestion's owners
     * @param qualifiedName of the MetabaseQuestion
     * @param name of the MetabaseQuestion
     * @return the updated MetabaseQuestion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseQuestion removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MetabaseQuestion) Asset.removeOwners(client, updater(qualifiedName, name));
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
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a MetabaseQuestion.
     *
     * @param client connectivity to the Atlan tenant on which to update the MetabaseQuestion's certificate
     * @param qualifiedName of the MetabaseQuestion
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MetabaseQuestion, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseQuestion updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (MetabaseQuestion)
                Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
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
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a MetabaseQuestion.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MetabaseQuestion's certificate
     * @param qualifiedName of the MetabaseQuestion
     * @param name of the MetabaseQuestion
     * @return the updated MetabaseQuestion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseQuestion removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MetabaseQuestion) Asset.removeCertificate(client, updater(qualifiedName, name));
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
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a MetabaseQuestion.
     *
     * @param client connectivity to the Atlan tenant on which to update the MetabaseQuestion's announcement
     * @param qualifiedName of the MetabaseQuestion
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseQuestion updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (MetabaseQuestion)
                Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
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
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a MetabaseQuestion.
     *
     * @param client connectivity to the Atlan client from which to remove the MetabaseQuestion's announcement
     * @param qualifiedName of the MetabaseQuestion
     * @param name of the MetabaseQuestion
     * @return the updated MetabaseQuestion, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseQuestion removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MetabaseQuestion) Asset.removeAnnouncement(client, updater(qualifiedName, name));
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
    public static MetabaseQuestion replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the MetabaseQuestion.
     *
     * @param client connectivity to the Atlan tenant on which to replace the MetabaseQuestion's assigned terms
     * @param qualifiedName for the MetabaseQuestion
     * @param name human-readable name of the MetabaseQuestion
     * @param terms the list of terms to replace on the MetabaseQuestion, or null to remove all terms from the MetabaseQuestion
     * @return the MetabaseQuestion that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseQuestion replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (MetabaseQuestion) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MetabaseQuestion, without replacing existing terms linked to the MetabaseQuestion.
     * Note: this operation must make two API calls — one to retrieve the MetabaseQuestion's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the MetabaseQuestion
     * @param terms the list of terms to append to the MetabaseQuestion
     * @return the MetabaseQuestion that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseQuestion appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the MetabaseQuestion, without replacing existing terms linked to the MetabaseQuestion.
     * Note: this operation must make two API calls — one to retrieve the MetabaseQuestion's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the MetabaseQuestion
     * @param qualifiedName for the MetabaseQuestion
     * @param terms the list of terms to append to the MetabaseQuestion
     * @return the MetabaseQuestion that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseQuestion appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MetabaseQuestion) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MetabaseQuestion, without replacing all existing terms linked to the MetabaseQuestion.
     * Note: this operation must make two API calls — one to retrieve the MetabaseQuestion's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the MetabaseQuestion
     * @param terms the list of terms to remove from the MetabaseQuestion, which must be referenced by GUID
     * @return the MetabaseQuestion that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseQuestion removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a MetabaseQuestion, without replacing all existing terms linked to the MetabaseQuestion.
     * Note: this operation must make two API calls — one to retrieve the MetabaseQuestion's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the MetabaseQuestion
     * @param qualifiedName for the MetabaseQuestion
     * @param terms the list of terms to remove from the MetabaseQuestion, which must be referenced by GUID
     * @return the MetabaseQuestion that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseQuestion removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MetabaseQuestion) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a MetabaseQuestion, without replacing existing Atlan tags linked to the MetabaseQuestion.
     * Note: this operation must make two API calls — one to retrieve the MetabaseQuestion's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MetabaseQuestion
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MetabaseQuestion
     */
    public static MetabaseQuestion appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MetabaseQuestion, without replacing existing Atlan tags linked to the MetabaseQuestion.
     * Note: this operation must make two API calls — one to retrieve the MetabaseQuestion's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MetabaseQuestion
     * @param qualifiedName of the MetabaseQuestion
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MetabaseQuestion
     */
    public static MetabaseQuestion appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (MetabaseQuestion) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MetabaseQuestion, without replacing existing Atlan tags linked to the MetabaseQuestion.
     * Note: this operation must make two API calls — one to retrieve the MetabaseQuestion's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MetabaseQuestion
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MetabaseQuestion
     */
    public static MetabaseQuestion appendAtlanTags(
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
     * Add Atlan tags to a MetabaseQuestion, without replacing existing Atlan tags linked to the MetabaseQuestion.
     * Note: this operation must make two API calls — one to retrieve the MetabaseQuestion's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MetabaseQuestion
     * @param qualifiedName of the MetabaseQuestion
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MetabaseQuestion
     */
    public static MetabaseQuestion appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (MetabaseQuestion) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a MetabaseQuestion.
     *
     * @param qualifiedName of the MetabaseQuestion
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MetabaseQuestion
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MetabaseQuestion.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the MetabaseQuestion
     * @param qualifiedName of the MetabaseQuestion
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MetabaseQuestion
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MetabaseQuestion.
     *
     * @param qualifiedName of the MetabaseQuestion
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MetabaseQuestion
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
     * Add Atlan tags to a MetabaseQuestion.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the MetabaseQuestion
     * @param qualifiedName of the MetabaseQuestion
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MetabaseQuestion
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
     * Remove an Atlan tag from a MetabaseQuestion.
     *
     * @param qualifiedName of the MetabaseQuestion
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MetabaseQuestion
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a MetabaseQuestion.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a MetabaseQuestion
     * @param qualifiedName of the MetabaseQuestion
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MetabaseQuestion
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
