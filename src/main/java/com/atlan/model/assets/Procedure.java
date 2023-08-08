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
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a stored procedure (routine) in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class Procedure extends Asset implements IProcedure, ISQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Procedure";

    /** Fixed typeName for Procedures. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String databaseName;

    /** TBC */
    @Attribute
    String databaseQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtModel> dbtModels;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtSource> dbtSources;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtTest> dbtTests;

    /** Logic of the procedure. */
    @Attribute
    String definition;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    Boolean isProfiled;

    /** TBC */
    @Attribute
    Long lastProfiledAt;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    Long queryCount;

    /** TBC */
    @Attribute
    Long queryCountUpdatedAt;

    /** TBC */
    @Attribute
    Long queryUserCount;

    /** TBC */
    @Attribute
    @Singular("putQueryUserMap")
    Map<String, Long> queryUserMap;

    /** Schema in which the procedure is contained. */
    @Attribute
    @JsonProperty("atlanSchema")
    ISchema schema;

    /** TBC */
    @Attribute
    String schemaName;

    /** TBC */
    @Attribute
    String schemaQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtSource> sqlDBTSources;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtModel> sqlDbtModels;

    /** TBC */
    @Attribute
    String tableName;

    /** TBC */
    @Attribute
    String tableQualifiedName;

    /** TBC */
    @Attribute
    String viewName;

    /** TBC */
    @Attribute
    String viewQualifiedName;

    /**
     * Builds the minimal object necessary to create a relationship to a Procedure, from a potentially
     * more-complete Procedure object.
     *
     * @return the minimal object necessary to relate to the Procedure
     * @throws InvalidRequestException if any of the minimal set of required properties for a Procedure relationship are not found in the initial object
     */
    @Override
    public Procedure trimToReference() throws InvalidRequestException {
        if (this.getGuid() != null && !this.getGuid().isEmpty()) {
            return refByGuid(this.getGuid());
        }
        if (this.getQualifiedName() != null && !this.getQualifiedName().isEmpty()) {
            return refByQualifiedName(this.getQualifiedName());
        }
        if (this.getUniqueAttributes() != null
                && this.getUniqueAttributes().getQualifiedName() != null
                && !this.getUniqueAttributes().getQualifiedName().isEmpty()) {
            return refByQualifiedName(this.getUniqueAttributes().getQualifiedName());
        }
        throw new InvalidRequestException(
                ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, TYPE_NAME, "guid, qualifiedName");
    }

    /**
     * Start an asset filter that will return all Procedure assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) Procedure assets will be included.
     *
     * @return an asset filter that includes all Procedure assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all Procedure assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) Procedure assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all Procedure assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all Procedure assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) Procedures will be included
     * @return an asset filter that includes all Procedure assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all Procedure assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) Procedures will be included
     * @return an asset filter that includes all Procedure assets
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
     * Reference to a Procedure by GUID.
     *
     * @param guid the GUID of the Procedure to reference
     * @return reference to a Procedure that can be used for defining a relationship to a Procedure
     */
    public static Procedure refByGuid(String guid) {
        return Procedure._internal().guid(guid).build();
    }

    /**
     * Reference to a Procedure by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the Procedure to reference
     * @return reference to a Procedure that can be used for defining a relationship to a Procedure
     */
    public static Procedure refByQualifiedName(String qualifiedName) {
        return Procedure._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a Procedure by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the Procedure to retrieve, either its GUID or its full qualifiedName
     * @return the requested full Procedure, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Procedure does not exist or the provided GUID is not a Procedure
     */
    @JsonIgnore
    public static Procedure get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a Procedure by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Procedure to retrieve, either its GUID or its full qualifiedName
     * @return the requested full Procedure, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Procedure does not exist or the provided GUID is not a Procedure
     */
    @JsonIgnore
    public static Procedure get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a Procedure by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Procedure to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full Procedure, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Procedure does not exist or the provided GUID is not a Procedure
     */
    @JsonIgnore
    public static Procedure get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof Procedure) {
                return (Procedure) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof Procedure) {
                return (Procedure) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a Procedure by its GUID, complete with all of its relationships.
     *
     * @param guid of the Procedure to retrieve
     * @return the requested full Procedure, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Procedure does not exist or the provided GUID is not a Procedure
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static Procedure retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a Procedure by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the Procedure to retrieve
     * @return the requested full Procedure, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Procedure does not exist or the provided GUID is not a Procedure
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static Procedure retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a Procedure by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the Procedure to retrieve
     * @return the requested full Procedure, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Procedure does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static Procedure retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a Procedure by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the Procedure to retrieve
     * @return the requested full Procedure, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Procedure does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static Procedure retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) Procedure to active.
     *
     * @param qualifiedName for the Procedure
     * @return true if the Procedure is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) Procedure to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the Procedure
     * @return true if the Procedure is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param name of the Procedure
     * @return the minimal request necessary to update the Procedure, as a builder
     */
    public static ProcedureBuilder<?, ?> updater(String qualifiedName, String name) {
        return Procedure._internal().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Procedure, from a potentially
     * more-complete Procedure object.
     *
     * @return the minimal object necessary to update the Procedure, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for Procedure are not found in the initial object
     */
    @Override
    public ProcedureBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "Procedure", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param name of the Procedure
     * @return the updated Procedure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Procedure removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a Procedure.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the Procedure
     * @param name of the Procedure
     * @return the updated Procedure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Procedure removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Procedure) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param name of the Procedure
     * @return the updated Procedure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Procedure removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a Procedure.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the Procedure
     * @param name of the Procedure
     * @return the updated Procedure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Procedure removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Procedure) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param name of the Procedure
     * @return the updated Procedure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Procedure removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a Procedure.
     *
     * @param client connectivity to the Atlan tenant from which to remove the Procedure's owners
     * @param qualifiedName of the Procedure
     * @param name of the Procedure
     * @return the updated Procedure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Procedure removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (Procedure) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated Procedure, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Procedure updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a Procedure.
     *
     * @param client connectivity to the Atlan tenant on which to update the Procedure's certificate
     * @param qualifiedName of the Procedure
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated Procedure, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Procedure updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (Procedure) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param name of the Procedure
     * @return the updated Procedure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Procedure removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a Procedure.
     *
     * @param client connectivity to the Atlan tenant from which to remove the Procedure's certificate
     * @param qualifiedName of the Procedure
     * @param name of the Procedure
     * @return the updated Procedure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Procedure removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Procedure) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Procedure updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a Procedure.
     *
     * @param client connectivity to the Atlan tenant on which to update the Procedure's announcement
     * @param qualifiedName of the Procedure
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Procedure updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (Procedure)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param name of the Procedure
     * @return the updated Procedure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Procedure removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a Procedure.
     *
     * @param client connectivity to the Atlan client from which to remove the Procedure's announcement
     * @param qualifiedName of the Procedure
     * @param name of the Procedure
     * @return the updated Procedure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Procedure removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Procedure) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the Procedure.
     *
     * @param qualifiedName for the Procedure
     * @param name human-readable name of the Procedure
     * @param terms the list of terms to replace on the Procedure, or null to remove all terms from the Procedure
     * @return the Procedure that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static Procedure replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the Procedure.
     *
     * @param client connectivity to the Atlan tenant on which to replace the Procedure's assigned terms
     * @param qualifiedName for the Procedure
     * @param name human-readable name of the Procedure
     * @param terms the list of terms to replace on the Procedure, or null to remove all terms from the Procedure
     * @return the Procedure that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static Procedure replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (Procedure) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the Procedure, without replacing existing terms linked to the Procedure.
     * Note: this operation must make two API calls — one to retrieve the Procedure's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the Procedure
     * @param terms the list of terms to append to the Procedure
     * @return the Procedure that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static Procedure appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the Procedure, without replacing existing terms linked to the Procedure.
     * Note: this operation must make two API calls — one to retrieve the Procedure's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the Procedure
     * @param qualifiedName for the Procedure
     * @param terms the list of terms to append to the Procedure
     * @return the Procedure that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static Procedure appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (Procedure) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a Procedure, without replacing all existing terms linked to the Procedure.
     * Note: this operation must make two API calls — one to retrieve the Procedure's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the Procedure
     * @param terms the list of terms to remove from the Procedure, which must be referenced by GUID
     * @return the Procedure that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static Procedure removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a Procedure, without replacing all existing terms linked to the Procedure.
     * Note: this operation must make two API calls — one to retrieve the Procedure's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the Procedure
     * @param qualifiedName for the Procedure
     * @param terms the list of terms to remove from the Procedure, which must be referenced by GUID
     * @return the Procedure that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static Procedure removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (Procedure) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a Procedure, without replacing existing Atlan tags linked to the Procedure.
     * Note: this operation must make two API calls — one to retrieve the Procedure's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the Procedure
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated Procedure
     */
    public static Procedure appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Procedure, without replacing existing Atlan tags linked to the Procedure.
     * Note: this operation must make two API calls — one to retrieve the Procedure's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the Procedure
     * @param qualifiedName of the Procedure
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated Procedure
     */
    public static Procedure appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (Procedure) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Procedure, without replacing existing Atlan tags linked to the Procedure.
     * Note: this operation must make two API calls — one to retrieve the Procedure's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the Procedure
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated Procedure
     */
    public static Procedure appendAtlanTags(
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
     * Add Atlan tags to a Procedure, without replacing existing Atlan tags linked to the Procedure.
     * Note: this operation must make two API calls — one to retrieve the Procedure's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the Procedure
     * @param qualifiedName of the Procedure
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated Procedure
     */
    public static Procedure appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (Procedure) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Procedure
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Procedure.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the Procedure
     * @param qualifiedName of the Procedure
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Procedure
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Procedure
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
     * Add Atlan tags to a Procedure.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the Procedure
     * @param qualifiedName of the Procedure
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Procedure
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
     * Remove an Atlan tag from a Procedure.
     *
     * @param qualifiedName of the Procedure
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the Procedure
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a Procedure.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a Procedure
     * @param qualifiedName of the Procedure
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the Procedure
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
