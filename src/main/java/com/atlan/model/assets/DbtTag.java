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
import com.atlan.model.structs.SourceTagAttribute;
import com.atlan.util.QueryFactory;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Atlan Dbt Tag Asset
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class DbtTag extends Asset implements IDbtTag, IDbt, ITag, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DbtTag";

    /** Fixed typeName for DbtTags. */
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
    SortedSet<String> dbtTags;

    /** TBC */
    @Attribute
    String dbtUniqueId;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @JsonProperty("mappedClassificationName")
    String mappedAtlanTagName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> tagAllowedValues;

    /** TBC */
    @Attribute
    @Singular
    List<SourceTagAttribute> tagAttributes;

    /** TBC */
    @Attribute
    String tagId;

    /**
     * Start an asset filter that will return all DbtTag assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DbtTag assets will be included.
     *
     * @return an asset filter that includes all DbtTag assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all DbtTag assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DbtTag assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all DbtTag assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all DbtTag assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) DbtTags will be included
     * @return an asset filter that includes all DbtTag assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all DbtTag assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DbtTags will be included
     * @return an asset filter that includes all DbtTag assets
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
     * Reference to a DbtTag by GUID.
     *
     * @param guid the GUID of the DbtTag to reference
     * @return reference to a DbtTag that can be used for defining a relationship to a DbtTag
     */
    public static DbtTag refByGuid(String guid) {
        return DbtTag._internal().guid(guid).build();
    }

    /**
     * Reference to a DbtTag by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the DbtTag to reference
     * @return reference to a DbtTag that can be used for defining a relationship to a DbtTag
     */
    public static DbtTag refByQualifiedName(String qualifiedName) {
        return DbtTag._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a DbtTag by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the DbtTag to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DbtTag, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtTag does not exist or the provided GUID is not a DbtTag
     */
    @JsonIgnore
    public static DbtTag get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a DbtTag by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DbtTag to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DbtTag, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtTag does not exist or the provided GUID is not a DbtTag
     */
    @JsonIgnore
    public static DbtTag get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a DbtTag by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DbtTag to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full DbtTag, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtTag does not exist or the provided GUID is not a DbtTag
     */
    @JsonIgnore
    public static DbtTag get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof DbtTag) {
                return (DbtTag) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof DbtTag) {
                return (DbtTag) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a DbtTag by its GUID, complete with all of its relationships.
     *
     * @param guid of the DbtTag to retrieve
     * @return the requested full DbtTag, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtTag does not exist or the provided GUID is not a DbtTag
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static DbtTag retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a DbtTag by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the DbtTag to retrieve
     * @return the requested full DbtTag, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtTag does not exist or the provided GUID is not a DbtTag
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static DbtTag retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a DbtTag by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the DbtTag to retrieve
     * @return the requested full DbtTag, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtTag does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static DbtTag retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a DbtTag by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the DbtTag to retrieve
     * @return the requested full DbtTag, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtTag does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static DbtTag retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) DbtTag to active.
     *
     * @param qualifiedName for the DbtTag
     * @return true if the DbtTag is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) DbtTag to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DbtTag
     * @return true if the DbtTag is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a DbtTag.
     *
     * @param qualifiedName of the DbtTag
     * @param name of the DbtTag
     * @return the minimal request necessary to update the DbtTag, as a builder
     */
    public static DbtTagBuilder<?, ?> updater(String qualifiedName, String name) {
        return DbtTag._internal().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DbtTag, from a potentially
     * more-complete DbtTag object.
     *
     * @return the minimal object necessary to update the DbtTag, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DbtTag are not found in the initial object
     */
    @Override
    public DbtTagBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "DbtTag", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a DbtTag.
     *
     * @param qualifiedName of the DbtTag
     * @param name of the DbtTag
     * @return the updated DbtTag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTag removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a DbtTag.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DbtTag
     * @param name of the DbtTag
     * @return the updated DbtTag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTag removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtTag) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DbtTag.
     *
     * @param qualifiedName of the DbtTag
     * @param name of the DbtTag
     * @return the updated DbtTag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTag removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a DbtTag.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DbtTag
     * @param name of the DbtTag
     * @return the updated DbtTag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTag removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtTag) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DbtTag.
     *
     * @param qualifiedName of the DbtTag
     * @param name of the DbtTag
     * @return the updated DbtTag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTag removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a DbtTag.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DbtTag's owners
     * @param qualifiedName of the DbtTag
     * @param name of the DbtTag
     * @return the updated DbtTag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTag removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (DbtTag) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DbtTag.
     *
     * @param qualifiedName of the DbtTag
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DbtTag, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtTag updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a DbtTag.
     *
     * @param client connectivity to the Atlan tenant on which to update the DbtTag's certificate
     * @param qualifiedName of the DbtTag
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DbtTag, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtTag updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DbtTag) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DbtTag.
     *
     * @param qualifiedName of the DbtTag
     * @param name of the DbtTag
     * @return the updated DbtTag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTag removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a DbtTag.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DbtTag's certificate
     * @param qualifiedName of the DbtTag
     * @param name of the DbtTag
     * @return the updated DbtTag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTag removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtTag) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DbtTag.
     *
     * @param qualifiedName of the DbtTag
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtTag updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a DbtTag.
     *
     * @param client connectivity to the Atlan tenant on which to update the DbtTag's announcement
     * @param qualifiedName of the DbtTag
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtTag updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (DbtTag) Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DbtTag.
     *
     * @param qualifiedName of the DbtTag
     * @param name of the DbtTag
     * @return the updated DbtTag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTag removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a DbtTag.
     *
     * @param client connectivity to the Atlan client from which to remove the DbtTag's announcement
     * @param qualifiedName of the DbtTag
     * @param name of the DbtTag
     * @return the updated DbtTag, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTag removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtTag) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DbtTag.
     *
     * @param qualifiedName for the DbtTag
     * @param name human-readable name of the DbtTag
     * @param terms the list of terms to replace on the DbtTag, or null to remove all terms from the DbtTag
     * @return the DbtTag that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DbtTag replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the DbtTag.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DbtTag's assigned terms
     * @param qualifiedName for the DbtTag
     * @param name human-readable name of the DbtTag
     * @param terms the list of terms to replace on the DbtTag, or null to remove all terms from the DbtTag
     * @return the DbtTag that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DbtTag replaceTerms(AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtTag) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DbtTag, without replacing existing terms linked to the DbtTag.
     * Note: this operation must make two API calls — one to retrieve the DbtTag's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the DbtTag
     * @param terms the list of terms to append to the DbtTag
     * @return the DbtTag that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DbtTag appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the DbtTag, without replacing existing terms linked to the DbtTag.
     * Note: this operation must make two API calls — one to retrieve the DbtTag's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DbtTag
     * @param qualifiedName for the DbtTag
     * @param terms the list of terms to append to the DbtTag
     * @return the DbtTag that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DbtTag appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtTag) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DbtTag, without replacing all existing terms linked to the DbtTag.
     * Note: this operation must make two API calls — one to retrieve the DbtTag's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the DbtTag
     * @param terms the list of terms to remove from the DbtTag, which must be referenced by GUID
     * @return the DbtTag that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DbtTag removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a DbtTag, without replacing all existing terms linked to the DbtTag.
     * Note: this operation must make two API calls — one to retrieve the DbtTag's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DbtTag
     * @param qualifiedName for the DbtTag
     * @param terms the list of terms to remove from the DbtTag, which must be referenced by GUID
     * @return the DbtTag that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DbtTag removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtTag) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DbtTag, without replacing existing Atlan tags linked to the DbtTag.
     * Note: this operation must make two API calls — one to retrieve the DbtTag's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DbtTag
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DbtTag
     */
    public static DbtTag appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtTag, without replacing existing Atlan tags linked to the DbtTag.
     * Note: this operation must make two API calls — one to retrieve the DbtTag's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DbtTag
     * @param qualifiedName of the DbtTag
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DbtTag
     */
    public static DbtTag appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (DbtTag) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtTag, without replacing existing Atlan tags linked to the DbtTag.
     * Note: this operation must make two API calls — one to retrieve the DbtTag's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DbtTag
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DbtTag
     */
    public static DbtTag appendAtlanTags(
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
     * Add Atlan tags to a DbtTag, without replacing existing Atlan tags linked to the DbtTag.
     * Note: this operation must make two API calls — one to retrieve the DbtTag's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DbtTag
     * @param qualifiedName of the DbtTag
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DbtTag
     */
    public static DbtTag appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DbtTag) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a DbtTag.
     *
     * @param qualifiedName of the DbtTag
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtTag
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtTag.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the DbtTag
     * @param qualifiedName of the DbtTag
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtTag
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtTag.
     *
     * @param qualifiedName of the DbtTag
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtTag
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
     * Add Atlan tags to a DbtTag.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the DbtTag
     * @param qualifiedName of the DbtTag
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtTag
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
     * Remove an Atlan tag from a DbtTag.
     *
     * @param qualifiedName of the DbtTag
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DbtTag
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a DbtTag.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DbtTag
     * @param qualifiedName of the DbtTag
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DbtTag
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
