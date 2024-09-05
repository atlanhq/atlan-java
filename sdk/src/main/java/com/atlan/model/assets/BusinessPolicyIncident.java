/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.IncidentSeverity;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Incident of business policy
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class BusinessPolicyIncident extends Asset
        implements IBusinessPolicyIncident, IIncident, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "BusinessPolicyIncident";

    /** Fixed typeName for BusinessPolicyIncidents. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Filter ES DSL to denote the associate asset/s involved. */
    @Attribute
    String businessPolicyIncidentFilterDSL;

    /** count of noncompliant assets in the incident */
    @Attribute
    Long businessPolicyIncidentNoncompliantCount;

    /** policy ids related to this incident */
    @Attribute
    @Singular
    SortedSet<String> businessPolicyIncidentRelatedPolicyGUIDs;

    /** Status of this asset's severity. */
    @Attribute
    IncidentSeverity incidentSeverity;

    /**
     * Builds the minimal object necessary to create a relationship to a BusinessPolicyIncident, from a potentially
     * more-complete BusinessPolicyIncident object.
     *
     * @return the minimal object necessary to relate to the BusinessPolicyIncident
     * @throws InvalidRequestException if any of the minimal set of required properties for a BusinessPolicyIncident relationship are not found in the initial object
     */
    @Override
    public BusinessPolicyIncident trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all BusinessPolicyIncident assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) BusinessPolicyIncident assets will be included.
     *
     * @return a fluent search that includes all BusinessPolicyIncident assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all BusinessPolicyIncident assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) BusinessPolicyIncident assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all BusinessPolicyIncident assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all BusinessPolicyIncident assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) BusinessPolicyIncidents will be included
     * @return a fluent search that includes all BusinessPolicyIncident assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all BusinessPolicyIncident assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) BusinessPolicyIncidents will be included
     * @return a fluent search that includes all BusinessPolicyIncident assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client, boolean includeArchived) {
        FluentSearch.FluentSearchBuilder<?, ?> builder =
                FluentSearch.builder(client).where(Asset.TYPE_NAME.eq(TYPE_NAME));
        if (!includeArchived) {
            builder.active();
        }
        return builder;
    }

    /**
     * Reference to a BusinessPolicyIncident by GUID. Use this to create a relationship to this BusinessPolicyIncident,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the BusinessPolicyIncident to reference
     * @return reference to a BusinessPolicyIncident that can be used for defining a relationship to a BusinessPolicyIncident
     */
    public static BusinessPolicyIncident refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a BusinessPolicyIncident by GUID. Use this to create a relationship to this BusinessPolicyIncident,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the BusinessPolicyIncident to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a BusinessPolicyIncident that can be used for defining a relationship to a BusinessPolicyIncident
     */
    public static BusinessPolicyIncident refByGuid(String guid, Reference.SaveSemantic semantic) {
        return BusinessPolicyIncident._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a BusinessPolicyIncident by qualifiedName. Use this to create a relationship to this BusinessPolicyIncident,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the BusinessPolicyIncident to reference
     * @return reference to a BusinessPolicyIncident that can be used for defining a relationship to a BusinessPolicyIncident
     */
    public static BusinessPolicyIncident refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a BusinessPolicyIncident by qualifiedName. Use this to create a relationship to this BusinessPolicyIncident,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the BusinessPolicyIncident to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a BusinessPolicyIncident that can be used for defining a relationship to a BusinessPolicyIncident
     */
    public static BusinessPolicyIncident refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return BusinessPolicyIncident._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a BusinessPolicyIncident by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the BusinessPolicyIncident to retrieve, either its GUID or its full qualifiedName
     * @return the requested full BusinessPolicyIncident, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the BusinessPolicyIncident does not exist or the provided GUID is not a BusinessPolicyIncident
     */
    @JsonIgnore
    public static BusinessPolicyIncident get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a BusinessPolicyIncident by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the BusinessPolicyIncident to retrieve, either its GUID or its full qualifiedName
     * @return the requested full BusinessPolicyIncident, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the BusinessPolicyIncident does not exist or the provided GUID is not a BusinessPolicyIncident
     */
    @JsonIgnore
    public static BusinessPolicyIncident get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a BusinessPolicyIncident by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the BusinessPolicyIncident to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full BusinessPolicyIncident, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the BusinessPolicyIncident does not exist or the provided GUID is not a BusinessPolicyIncident
     */
    @JsonIgnore
    public static BusinessPolicyIncident get(AtlanClient client, String id, boolean includeRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof BusinessPolicyIncident) {
                return (BusinessPolicyIncident) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof BusinessPolicyIncident) {
                return (BusinessPolicyIncident) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) BusinessPolicyIncident to active.
     *
     * @param qualifiedName for the BusinessPolicyIncident
     * @return true if the BusinessPolicyIncident is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) BusinessPolicyIncident to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the BusinessPolicyIncident
     * @return true if the BusinessPolicyIncident is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a BusinessPolicyIncident.
     *
     * @param qualifiedName of the BusinessPolicyIncident
     * @param name of the BusinessPolicyIncident
     * @return the minimal request necessary to update the BusinessPolicyIncident, as a builder
     */
    public static BusinessPolicyIncidentBuilder<?, ?> updater(String qualifiedName, String name) {
        return BusinessPolicyIncident._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a BusinessPolicyIncident, from a potentially
     * more-complete BusinessPolicyIncident object.
     *
     * @return the minimal object necessary to update the BusinessPolicyIncident, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for BusinessPolicyIncident are not found in the initial object
     */
    @Override
    public BusinessPolicyIncidentBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(
                TYPE_NAME,
                Map.of(
                        "qualifiedName", this.getQualifiedName(),
                        "name", this.getName()));
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a BusinessPolicyIncident.
     *
     * @param qualifiedName of the BusinessPolicyIncident
     * @param name of the BusinessPolicyIncident
     * @return the updated BusinessPolicyIncident, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicyIncident removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a BusinessPolicyIncident.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the BusinessPolicyIncident
     * @param name of the BusinessPolicyIncident
     * @return the updated BusinessPolicyIncident, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicyIncident removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (BusinessPolicyIncident) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a BusinessPolicyIncident.
     *
     * @param qualifiedName of the BusinessPolicyIncident
     * @param name of the BusinessPolicyIncident
     * @return the updated BusinessPolicyIncident, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicyIncident removeUserDescription(String qualifiedName, String name)
            throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a BusinessPolicyIncident.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the BusinessPolicyIncident
     * @param name of the BusinessPolicyIncident
     * @return the updated BusinessPolicyIncident, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicyIncident removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (BusinessPolicyIncident) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a BusinessPolicyIncident.
     *
     * @param qualifiedName of the BusinessPolicyIncident
     * @param name of the BusinessPolicyIncident
     * @return the updated BusinessPolicyIncident, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicyIncident removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a BusinessPolicyIncident.
     *
     * @param client connectivity to the Atlan tenant from which to remove the BusinessPolicyIncident's owners
     * @param qualifiedName of the BusinessPolicyIncident
     * @param name of the BusinessPolicyIncident
     * @return the updated BusinessPolicyIncident, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicyIncident removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (BusinessPolicyIncident) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a BusinessPolicyIncident.
     *
     * @param qualifiedName of the BusinessPolicyIncident
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated BusinessPolicyIncident, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicyIncident updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a BusinessPolicyIncident.
     *
     * @param client connectivity to the Atlan tenant on which to update the BusinessPolicyIncident's certificate
     * @param qualifiedName of the BusinessPolicyIncident
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated BusinessPolicyIncident, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicyIncident updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (BusinessPolicyIncident)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a BusinessPolicyIncident.
     *
     * @param qualifiedName of the BusinessPolicyIncident
     * @param name of the BusinessPolicyIncident
     * @return the updated BusinessPolicyIncident, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicyIncident removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a BusinessPolicyIncident.
     *
     * @param client connectivity to the Atlan tenant from which to remove the BusinessPolicyIncident's certificate
     * @param qualifiedName of the BusinessPolicyIncident
     * @param name of the BusinessPolicyIncident
     * @return the updated BusinessPolicyIncident, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicyIncident removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (BusinessPolicyIncident) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a BusinessPolicyIncident.
     *
     * @param qualifiedName of the BusinessPolicyIncident
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicyIncident updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a BusinessPolicyIncident.
     *
     * @param client connectivity to the Atlan tenant on which to update the BusinessPolicyIncident's announcement
     * @param qualifiedName of the BusinessPolicyIncident
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicyIncident updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (BusinessPolicyIncident)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a BusinessPolicyIncident.
     *
     * @param qualifiedName of the BusinessPolicyIncident
     * @param name of the BusinessPolicyIncident
     * @return the updated BusinessPolicyIncident, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicyIncident removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a BusinessPolicyIncident.
     *
     * @param client connectivity to the Atlan client from which to remove the BusinessPolicyIncident's announcement
     * @param qualifiedName of the BusinessPolicyIncident
     * @param name of the BusinessPolicyIncident
     * @return the updated BusinessPolicyIncident, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicyIncident removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (BusinessPolicyIncident) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the BusinessPolicyIncident.
     *
     * @param qualifiedName for the BusinessPolicyIncident
     * @param name human-readable name of the BusinessPolicyIncident
     * @param terms the list of terms to replace on the BusinessPolicyIncident, or null to remove all terms from the BusinessPolicyIncident
     * @return the BusinessPolicyIncident that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicyIncident replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the BusinessPolicyIncident.
     *
     * @param client connectivity to the Atlan tenant on which to replace the BusinessPolicyIncident's assigned terms
     * @param qualifiedName for the BusinessPolicyIncident
     * @param name human-readable name of the BusinessPolicyIncident
     * @param terms the list of terms to replace on the BusinessPolicyIncident, or null to remove all terms from the BusinessPolicyIncident
     * @return the BusinessPolicyIncident that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicyIncident replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (BusinessPolicyIncident) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the BusinessPolicyIncident, without replacing existing terms linked to the BusinessPolicyIncident.
     * Note: this operation must make two API calls — one to retrieve the BusinessPolicyIncident's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the BusinessPolicyIncident
     * @param terms the list of terms to append to the BusinessPolicyIncident
     * @return the BusinessPolicyIncident that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicyIncident appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the BusinessPolicyIncident, without replacing existing terms linked to the BusinessPolicyIncident.
     * Note: this operation must make two API calls — one to retrieve the BusinessPolicyIncident's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the BusinessPolicyIncident
     * @param qualifiedName for the BusinessPolicyIncident
     * @param terms the list of terms to append to the BusinessPolicyIncident
     * @return the BusinessPolicyIncident that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicyIncident appendTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (BusinessPolicyIncident) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a BusinessPolicyIncident, without replacing all existing terms linked to the BusinessPolicyIncident.
     * Note: this operation must make two API calls — one to retrieve the BusinessPolicyIncident's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the BusinessPolicyIncident
     * @param terms the list of terms to remove from the BusinessPolicyIncident, which must be referenced by GUID
     * @return the BusinessPolicyIncident that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicyIncident removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a BusinessPolicyIncident, without replacing all existing terms linked to the BusinessPolicyIncident.
     * Note: this operation must make two API calls — one to retrieve the BusinessPolicyIncident's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the BusinessPolicyIncident
     * @param qualifiedName for the BusinessPolicyIncident
     * @param terms the list of terms to remove from the BusinessPolicyIncident, which must be referenced by GUID
     * @return the BusinessPolicyIncident that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicyIncident removeTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (BusinessPolicyIncident) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a BusinessPolicyIncident, without replacing existing Atlan tags linked to the BusinessPolicyIncident.
     * Note: this operation must make two API calls — one to retrieve the BusinessPolicyIncident's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the BusinessPolicyIncident
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated BusinessPolicyIncident
     */
    public static BusinessPolicyIncident appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a BusinessPolicyIncident, without replacing existing Atlan tags linked to the BusinessPolicyIncident.
     * Note: this operation must make two API calls — one to retrieve the BusinessPolicyIncident's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the BusinessPolicyIncident
     * @param qualifiedName of the BusinessPolicyIncident
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated BusinessPolicyIncident
     */
    public static BusinessPolicyIncident appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (BusinessPolicyIncident) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a BusinessPolicyIncident, without replacing existing Atlan tags linked to the BusinessPolicyIncident.
     * Note: this operation must make two API calls — one to retrieve the BusinessPolicyIncident's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the BusinessPolicyIncident
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated BusinessPolicyIncident
     */
    public static BusinessPolicyIncident appendAtlanTags(
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
     * Add Atlan tags to a BusinessPolicyIncident, without replacing existing Atlan tags linked to the BusinessPolicyIncident.
     * Note: this operation must make two API calls — one to retrieve the BusinessPolicyIncident's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the BusinessPolicyIncident
     * @param qualifiedName of the BusinessPolicyIncident
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated BusinessPolicyIncident
     */
    public static BusinessPolicyIncident appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (BusinessPolicyIncident) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a BusinessPolicyIncident.
     *
     * @param qualifiedName of the BusinessPolicyIncident
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the BusinessPolicyIncident
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a BusinessPolicyIncident.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a BusinessPolicyIncident
     * @param qualifiedName of the BusinessPolicyIncident
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the BusinessPolicyIncident
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
