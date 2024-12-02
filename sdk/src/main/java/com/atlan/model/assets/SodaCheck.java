/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Soda check in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class SodaCheck extends Asset implements ISodaCheck, ISoda, IDataQuality, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SodaCheck";

    /** Fixed typeName for SodaChecks. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Tasks to which this asset provides input. */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> inputToAirflowTasks;

    /** Processes to which this asset provides input. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISparkJob> inputToSparkJobs;

    /** Attributes implemented by this asset. */
    @Attribute
    @Singular
    SortedSet<IModelAttribute> modelImplementedAttributes;

    /** Entities implemented by this asset. */
    @Attribute
    @Singular
    SortedSet<IModelEntity> modelImplementedEntities;

    /** Tasks from which this asset is output. */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> outputFromAirflowTasks;

    /** Processes from which this asset is produced as output. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISparkJob> outputFromSparkJobs;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IAsset> sodaCheckAssets;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IColumn> sodaCheckColumns;

    /** Definition of the check in Soda. */
    @Attribute
    String sodaCheckDefinition;

    /** Status of the check in Soda. */
    @Attribute
    String sodaCheckEvaluationStatus;

    /** Identifier of the check in Soda. */
    @Attribute
    String sodaCheckId;

    /** TBC */
    @Attribute
    Long sodaCheckIncidentCount;

    /** TBC */
    @Attribute
    @Date
    Long sodaCheckLastScanAt;

    /**
     * Builds the minimal object necessary to create a relationship to a SodaCheck, from a potentially
     * more-complete SodaCheck object.
     *
     * @return the minimal object necessary to relate to the SodaCheck
     * @throws InvalidRequestException if any of the minimal set of required properties for a SodaCheck relationship are not found in the initial object
     */
    @Override
    public SodaCheck trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SodaCheck assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SodaCheck assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SodaCheck assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SodaCheck assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SodaChecks will be included
     * @return a fluent search that includes all SodaCheck assets
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
     * Reference to a SodaCheck by GUID. Use this to create a relationship to this SodaCheck,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SodaCheck to reference
     * @return reference to a SodaCheck that can be used for defining a relationship to a SodaCheck
     */
    public static SodaCheck refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SodaCheck by GUID. Use this to create a relationship to this SodaCheck,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SodaCheck to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SodaCheck that can be used for defining a relationship to a SodaCheck
     */
    public static SodaCheck refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SodaCheck._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SodaCheck by qualifiedName. Use this to create a relationship to this SodaCheck,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SodaCheck to reference
     * @return reference to a SodaCheck that can be used for defining a relationship to a SodaCheck
     */
    public static SodaCheck refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SodaCheck by qualifiedName. Use this to create a relationship to this SodaCheck,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SodaCheck to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SodaCheck that can be used for defining a relationship to a SodaCheck
     */
    public static SodaCheck refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SodaCheck._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SodaCheck by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SodaCheck to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SodaCheck, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SodaCheck does not exist or the provided GUID is not a SodaCheck
     */
    @JsonIgnore
    public static SodaCheck get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SodaCheck by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SodaCheck to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SodaCheck, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SodaCheck does not exist or the provided GUID is not a SodaCheck
     */
    @JsonIgnore
    public static SodaCheck get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SodaCheck) {
                return (SodaCheck) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof SodaCheck) {
                return (SodaCheck) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SodaCheck to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SodaCheck
     * @return true if the SodaCheck is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SodaCheck.
     *
     * @param qualifiedName of the SodaCheck
     * @param name of the SodaCheck
     * @return the minimal request necessary to update the SodaCheck, as a builder
     */
    public static SodaCheckBuilder<?, ?> updater(String qualifiedName, String name) {
        return SodaCheck._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SodaCheck, from a potentially
     * more-complete SodaCheck object.
     *
     * @return the minimal object necessary to update the SodaCheck, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SodaCheck are not found in the initial object
     */
    @Override
    public SodaCheckBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a SodaCheck.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SodaCheck
     * @param name of the SodaCheck
     * @return the updated SodaCheck, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SodaCheck removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SodaCheck) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SodaCheck.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SodaCheck
     * @param name of the SodaCheck
     * @return the updated SodaCheck, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SodaCheck removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SodaCheck) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SodaCheck.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SodaCheck's owners
     * @param qualifiedName of the SodaCheck
     * @param name of the SodaCheck
     * @return the updated SodaCheck, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SodaCheck removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (SodaCheck) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SodaCheck.
     *
     * @param client connectivity to the Atlan tenant on which to update the SodaCheck's certificate
     * @param qualifiedName of the SodaCheck
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SodaCheck, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SodaCheck updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SodaCheck) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SodaCheck.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SodaCheck's certificate
     * @param qualifiedName of the SodaCheck
     * @param name of the SodaCheck
     * @return the updated SodaCheck, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SodaCheck removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SodaCheck) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SodaCheck.
     *
     * @param client connectivity to the Atlan tenant on which to update the SodaCheck's announcement
     * @param qualifiedName of the SodaCheck
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SodaCheck updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SodaCheck)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SodaCheck.
     *
     * @param client connectivity to the Atlan client from which to remove the SodaCheck's announcement
     * @param qualifiedName of the SodaCheck
     * @param name of the SodaCheck
     * @return the updated SodaCheck, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SodaCheck removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SodaCheck) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SodaCheck.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SodaCheck's assigned terms
     * @param qualifiedName for the SodaCheck
     * @param name human-readable name of the SodaCheck
     * @param terms the list of terms to replace on the SodaCheck, or null to remove all terms from the SodaCheck
     * @return the SodaCheck that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SodaCheck replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SodaCheck) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SodaCheck, without replacing existing terms linked to the SodaCheck.
     * Note: this operation must make two API calls — one to retrieve the SodaCheck's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SodaCheck
     * @param qualifiedName for the SodaCheck
     * @param terms the list of terms to append to the SodaCheck
     * @return the SodaCheck that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SodaCheck appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SodaCheck) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SodaCheck, without replacing all existing terms linked to the SodaCheck.
     * Note: this operation must make two API calls — one to retrieve the SodaCheck's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SodaCheck
     * @param qualifiedName for the SodaCheck
     * @param terms the list of terms to remove from the SodaCheck, which must be referenced by GUID
     * @return the SodaCheck that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SodaCheck removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SodaCheck) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SodaCheck, without replacing existing Atlan tags linked to the SodaCheck.
     * Note: this operation must make two API calls — one to retrieve the SodaCheck's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SodaCheck
     * @param qualifiedName of the SodaCheck
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SodaCheck
     */
    public static SodaCheck appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SodaCheck) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SodaCheck, without replacing existing Atlan tags linked to the SodaCheck.
     * Note: this operation must make two API calls — one to retrieve the SodaCheck's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SodaCheck
     * @param qualifiedName of the SodaCheck
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SodaCheck
     */
    public static SodaCheck appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SodaCheck) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SodaCheck.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SodaCheck
     * @param qualifiedName of the SodaCheck
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SodaCheck
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
