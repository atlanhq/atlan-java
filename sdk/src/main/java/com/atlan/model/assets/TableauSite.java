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
 * Instance of a Tableau site in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class TableauSite extends Asset implements ITableauSite, ITableau, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TableauSite";

    /** Fixed typeName for TableauSites. */
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

    /** Projects that exist within this site. */
    @Attribute
    @Singular
    SortedSet<ITableauProject> projects;

    /**
     * Builds the minimal object necessary to create a relationship to a TableauSite, from a potentially
     * more-complete TableauSite object.
     *
     * @return the minimal object necessary to relate to the TableauSite
     * @throws InvalidRequestException if any of the minimal set of required properties for a TableauSite relationship are not found in the initial object
     */
    @Override
    public TableauSite trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all TableauSite assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) TableauSite assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all TableauSite assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all TableauSite assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) TableauSites will be included
     * @return a fluent search that includes all TableauSite assets
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
     * Reference to a TableauSite by GUID. Use this to create a relationship to this TableauSite,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the TableauSite to reference
     * @return reference to a TableauSite that can be used for defining a relationship to a TableauSite
     */
    public static TableauSite refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a TableauSite by GUID. Use this to create a relationship to this TableauSite,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the TableauSite to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a TableauSite that can be used for defining a relationship to a TableauSite
     */
    public static TableauSite refByGuid(String guid, Reference.SaveSemantic semantic) {
        return TableauSite._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a TableauSite by qualifiedName. Use this to create a relationship to this TableauSite,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the TableauSite to reference
     * @return reference to a TableauSite that can be used for defining a relationship to a TableauSite
     */
    public static TableauSite refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a TableauSite by qualifiedName. Use this to create a relationship to this TableauSite,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the TableauSite to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a TableauSite that can be used for defining a relationship to a TableauSite
     */
    public static TableauSite refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return TableauSite._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a TableauSite by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the TableauSite to retrieve, either its GUID or its full qualifiedName
     * @return the requested full TableauSite, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauSite does not exist or the provided GUID is not a TableauSite
     */
    @JsonIgnore
    public static TableauSite get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a TableauSite by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the TableauSite to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full TableauSite, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TableauSite does not exist or the provided GUID is not a TableauSite
     */
    @JsonIgnore
    public static TableauSite get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof TableauSite) {
                return (TableauSite) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof TableauSite) {
                return (TableauSite) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) TableauSite to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the TableauSite
     * @return true if the TableauSite is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a TableauSite.
     *
     * @param qualifiedName of the TableauSite
     * @param name of the TableauSite
     * @return the minimal request necessary to update the TableauSite, as a builder
     */
    public static TableauSiteBuilder<?, ?> updater(String qualifiedName, String name) {
        return TableauSite._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TableauSite, from a potentially
     * more-complete TableauSite object.
     *
     * @return the minimal object necessary to update the TableauSite, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TableauSite are not found in the initial object
     */
    @Override
    public TableauSiteBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a TableauSite.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the TableauSite
     * @param name of the TableauSite
     * @return the updated TableauSite, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauSite removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauSite) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a TableauSite.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the TableauSite
     * @param name of the TableauSite
     * @return the updated TableauSite, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauSite removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauSite) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a TableauSite.
     *
     * @param client connectivity to the Atlan tenant from which to remove the TableauSite's owners
     * @param qualifiedName of the TableauSite
     * @param name of the TableauSite
     * @return the updated TableauSite, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauSite removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauSite) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a TableauSite.
     *
     * @param client connectivity to the Atlan tenant on which to update the TableauSite's certificate
     * @param qualifiedName of the TableauSite
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TableauSite, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauSite updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (TableauSite)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a TableauSite.
     *
     * @param client connectivity to the Atlan tenant from which to remove the TableauSite's certificate
     * @param qualifiedName of the TableauSite
     * @param name of the TableauSite
     * @return the updated TableauSite, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauSite removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauSite) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a TableauSite.
     *
     * @param client connectivity to the Atlan tenant on which to update the TableauSite's announcement
     * @param qualifiedName of the TableauSite
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TableauSite updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (TableauSite)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a TableauSite.
     *
     * @param client connectivity to the Atlan client from which to remove the TableauSite's announcement
     * @param qualifiedName of the TableauSite
     * @param name of the TableauSite
     * @return the updated TableauSite, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TableauSite removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TableauSite) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the TableauSite.
     *
     * @param client connectivity to the Atlan tenant on which to replace the TableauSite's assigned terms
     * @param qualifiedName for the TableauSite
     * @param name human-readable name of the TableauSite
     * @param terms the list of terms to replace on the TableauSite, or null to remove all terms from the TableauSite
     * @return the TableauSite that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TableauSite replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (TableauSite) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the TableauSite, without replacing existing terms linked to the TableauSite.
     * Note: this operation must make two API calls — one to retrieve the TableauSite's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the TableauSite
     * @param qualifiedName for the TableauSite
     * @param terms the list of terms to append to the TableauSite
     * @return the TableauSite that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static TableauSite appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (TableauSite) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a TableauSite, without replacing all existing terms linked to the TableauSite.
     * Note: this operation must make two API calls — one to retrieve the TableauSite's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the TableauSite
     * @param qualifiedName for the TableauSite
     * @param terms the list of terms to remove from the TableauSite, which must be referenced by GUID
     * @return the TableauSite that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static TableauSite removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (TableauSite) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a TableauSite, without replacing existing Atlan tags linked to the TableauSite.
     * Note: this operation must make two API calls — one to retrieve the TableauSite's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the TableauSite
     * @param qualifiedName of the TableauSite
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated TableauSite
     */
    public static TableauSite appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (TableauSite) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TableauSite, without replacing existing Atlan tags linked to the TableauSite.
     * Note: this operation must make two API calls — one to retrieve the TableauSite's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the TableauSite
     * @param qualifiedName of the TableauSite
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated TableauSite
     */
    public static TableauSite appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (TableauSite) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a TableauSite.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a TableauSite
     * @param qualifiedName of the TableauSite
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the TableauSite
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
