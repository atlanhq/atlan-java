/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.AssetFilter;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.QueryFactory;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Metabase dashboard in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class MetabaseDashboard extends Asset
        implements IMetabaseDashboard, IMetabase, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MetabaseDashboard";

    /** Fixed typeName for MetabaseDashboards. */
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
    Long metabaseQuestionCount;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IMetabaseQuestion> metabaseQuestions;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /**
     * Start an asset filter that will return all MetabaseDashboard assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) MetabaseDashboard assets will be included.
     *
     * @return an asset filter that includes all MetabaseDashboard assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all MetabaseDashboard assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) MetabaseDashboard assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all MetabaseDashboard assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all MetabaseDashboard assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) MetabaseDashboards will be included
     * @return an asset filter that includes all MetabaseDashboard assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all MetabaseDashboard assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) MetabaseDashboards will be included
     * @return an asset filter that includes all MetabaseDashboard assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client, boolean includeArchived) {
        AssetFilter.AssetFilterBuilder builder =
                AssetFilter.builder().client(client).filter(QueryFactory.type(TYPE_NAME));
        if (!includeArchived) {
            builder.filter(QueryFactory.active());
        }
        return builder;
    }

    /**
     * Reference to a MetabaseDashboard by GUID.
     *
     * @param guid the GUID of the MetabaseDashboard to reference
     * @return reference to a MetabaseDashboard that can be used for defining a relationship to a MetabaseDashboard
     */
    public static MetabaseDashboard refByGuid(String guid) {
        return MetabaseDashboard._internal().guid(guid).build();
    }

    /**
     * Reference to a MetabaseDashboard by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the MetabaseDashboard to reference
     * @return reference to a MetabaseDashboard that can be used for defining a relationship to a MetabaseDashboard
     */
    public static MetabaseDashboard refByQualifiedName(String qualifiedName) {
        return MetabaseDashboard._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a MetabaseDashboard by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the MetabaseDashboard to retrieve, either its GUID or its full qualifiedName
     * @return the requested full MetabaseDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MetabaseDashboard does not exist or the provided GUID is not a MetabaseDashboard
     */
    @JsonIgnore
    public static MetabaseDashboard get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a MetabaseDashboard by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the MetabaseDashboard to retrieve, either its GUID or its full qualifiedName
     * @return the requested full MetabaseDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MetabaseDashboard does not exist or the provided GUID is not a MetabaseDashboard
     */
    @JsonIgnore
    public static MetabaseDashboard get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a MetabaseDashboard by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the MetabaseDashboard to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full MetabaseDashboard, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MetabaseDashboard does not exist or the provided GUID is not a MetabaseDashboard
     */
    @JsonIgnore
    public static MetabaseDashboard get(AtlanClient client, String id, boolean includeRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof MetabaseDashboard) {
                return (MetabaseDashboard) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, "MetabaseDashboard");
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof MetabaseDashboard) {
                return (MetabaseDashboard) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, "MetabaseDashboard");
            }
        }
    }

    /**
     * Retrieves a MetabaseDashboard by its GUID, complete with all of its relationships.
     *
     * @param guid of the MetabaseDashboard to retrieve
     * @return the requested full MetabaseDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MetabaseDashboard does not exist or the provided GUID is not a MetabaseDashboard
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static MetabaseDashboard retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a MetabaseDashboard by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the MetabaseDashboard to retrieve
     * @return the requested full MetabaseDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MetabaseDashboard does not exist or the provided GUID is not a MetabaseDashboard
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static MetabaseDashboard retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a MetabaseDashboard by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the MetabaseDashboard to retrieve
     * @return the requested full MetabaseDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MetabaseDashboard does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static MetabaseDashboard retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a MetabaseDashboard by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the MetabaseDashboard to retrieve
     * @return the requested full MetabaseDashboard, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MetabaseDashboard does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static MetabaseDashboard retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) MetabaseDashboard to active.
     *
     * @param qualifiedName for the MetabaseDashboard
     * @return true if the MetabaseDashboard is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) MetabaseDashboard to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the MetabaseDashboard
     * @return true if the MetabaseDashboard is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param name of the MetabaseDashboard
     * @return the minimal request necessary to update the MetabaseDashboard, as a builder
     */
    public static MetabaseDashboardBuilder<?, ?> updater(String qualifiedName, String name) {
        return MetabaseDashboard._internal().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a MetabaseDashboard, from a potentially
     * more-complete MetabaseDashboard object.
     *
     * @return the minimal object necessary to update the MetabaseDashboard, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for MetabaseDashboard are not found in the initial object
     */
    @Override
    public MetabaseDashboardBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "MetabaseDashboard", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param name of the MetabaseDashboard
     * @return the updated MetabaseDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a MetabaseDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MetabaseDashboard
     * @param name of the MetabaseDashboard
     * @return the updated MetabaseDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MetabaseDashboard) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param name of the MetabaseDashboard
     * @return the updated MetabaseDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a MetabaseDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MetabaseDashboard
     * @param name of the MetabaseDashboard
     * @return the updated MetabaseDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MetabaseDashboard) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param name of the MetabaseDashboard
     * @return the updated MetabaseDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a MetabaseDashboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MetabaseDashboard's owners
     * @param qualifiedName of the MetabaseDashboard
     * @param name of the MetabaseDashboard
     * @return the updated MetabaseDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MetabaseDashboard) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MetabaseDashboard, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a MetabaseDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to update the MetabaseDashboard's certificate
     * @param qualifiedName of the MetabaseDashboard
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MetabaseDashboard, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (MetabaseDashboard)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param name of the MetabaseDashboard
     * @return the updated MetabaseDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a MetabaseDashboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MetabaseDashboard's certificate
     * @param qualifiedName of the MetabaseDashboard
     * @param name of the MetabaseDashboard
     * @return the updated MetabaseDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MetabaseDashboard) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a MetabaseDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to update the MetabaseDashboard's announcement
     * @param qualifiedName of the MetabaseDashboard
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (MetabaseDashboard)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param name of the MetabaseDashboard
     * @return the updated MetabaseDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a MetabaseDashboard.
     *
     * @param client connectivity to the Atlan client from which to remove the MetabaseDashboard's announcement
     * @param qualifiedName of the MetabaseDashboard
     * @param name of the MetabaseDashboard
     * @return the updated MetabaseDashboard, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MetabaseDashboard) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the MetabaseDashboard.
     *
     * @param qualifiedName for the MetabaseDashboard
     * @param name human-readable name of the MetabaseDashboard
     * @param terms the list of terms to replace on the MetabaseDashboard, or null to remove all terms from the MetabaseDashboard
     * @return the MetabaseDashboard that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the MetabaseDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to replace the MetabaseDashboard's assigned terms
     * @param qualifiedName for the MetabaseDashboard
     * @param name human-readable name of the MetabaseDashboard
     * @param terms the list of terms to replace on the MetabaseDashboard, or null to remove all terms from the MetabaseDashboard
     * @return the MetabaseDashboard that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (MetabaseDashboard) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MetabaseDashboard, without replacing existing terms linked to the MetabaseDashboard.
     * Note: this operation must make two API calls — one to retrieve the MetabaseDashboard's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the MetabaseDashboard
     * @param terms the list of terms to append to the MetabaseDashboard
     * @return the MetabaseDashboard that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the MetabaseDashboard, without replacing existing terms linked to the MetabaseDashboard.
     * Note: this operation must make two API calls — one to retrieve the MetabaseDashboard's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the MetabaseDashboard
     * @param qualifiedName for the MetabaseDashboard
     * @param terms the list of terms to append to the MetabaseDashboard
     * @return the MetabaseDashboard that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MetabaseDashboard) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MetabaseDashboard, without replacing all existing terms linked to the MetabaseDashboard.
     * Note: this operation must make two API calls — one to retrieve the MetabaseDashboard's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the MetabaseDashboard
     * @param terms the list of terms to remove from the MetabaseDashboard, which must be referenced by GUID
     * @return the MetabaseDashboard that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a MetabaseDashboard, without replacing all existing terms linked to the MetabaseDashboard.
     * Note: this operation must make two API calls — one to retrieve the MetabaseDashboard's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the MetabaseDashboard
     * @param qualifiedName for the MetabaseDashboard
     * @param terms the list of terms to remove from the MetabaseDashboard, which must be referenced by GUID
     * @return the MetabaseDashboard that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MetabaseDashboard removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MetabaseDashboard) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a MetabaseDashboard, without replacing existing Atlan tags linked to the MetabaseDashboard.
     * Note: this operation must make two API calls — one to retrieve the MetabaseDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MetabaseDashboard
     */
    public static MetabaseDashboard appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MetabaseDashboard, without replacing existing Atlan tags linked to the MetabaseDashboard.
     * Note: this operation must make two API calls — one to retrieve the MetabaseDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MetabaseDashboard
     * @param qualifiedName of the MetabaseDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MetabaseDashboard
     */
    public static MetabaseDashboard appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (MetabaseDashboard) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MetabaseDashboard, without replacing existing Atlan tags linked to the MetabaseDashboard.
     * Note: this operation must make two API calls — one to retrieve the MetabaseDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MetabaseDashboard
     */
    public static MetabaseDashboard appendAtlanTags(
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
     * Add Atlan tags to a MetabaseDashboard, without replacing existing Atlan tags linked to the MetabaseDashboard.
     * Note: this operation must make two API calls — one to retrieve the MetabaseDashboard's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MetabaseDashboard
     * @param qualifiedName of the MetabaseDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MetabaseDashboard
     */
    public static MetabaseDashboard appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (MetabaseDashboard) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MetabaseDashboard
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MetabaseDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the MetabaseDashboard
     * @param qualifiedName of the MetabaseDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MetabaseDashboard
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MetabaseDashboard
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
     * Add Atlan tags to a MetabaseDashboard.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the MetabaseDashboard
     * @param qualifiedName of the MetabaseDashboard
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MetabaseDashboard
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
     * Remove an Atlan tag from a MetabaseDashboard.
     *
     * @param qualifiedName of the MetabaseDashboard
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MetabaseDashboard
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a MetabaseDashboard.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a MetabaseDashboard
     * @param qualifiedName of the MetabaseDashboard
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MetabaseDashboard
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
