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
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Cognos Package in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class CognosPackage extends Asset implements ICognosPackage, ICognos, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "CognosPackage";

    /** Fixed typeName for CognosPackages. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Tooltip text present for the Cognos asset */
    @Attribute
    String cognosDefaultScreenTip;

    /** TBC */
    @Attribute
    ICognosFolder cognosFolder;

    /** ID of the asset in Cognos */
    @Attribute
    String cognosId;

    /** Whether the Cognos asset is diabled */
    @Attribute
    Boolean cognosIsDisabled;

    /** Whether the Cognos asset is hidden from the ui */
    @Attribute
    Boolean cognosIsHidden;

    /** Name of the parent asset in Cognos */
    @Attribute
    String cognosParentName;

    /** Qualified name of the parent asset in Cognos */
    @Attribute
    String cognosParentQualifiedName;

    /** Path of the asset in Cognos. E.g. /content/folder[@name='Folder Name'] */
    @Attribute
    String cognosPath;

    /** Cognos type of the Cognos asset. E.g. report, dashboard, package, etc. */
    @Attribute
    String cognosType;

    /** Version of the Cognos asset */
    @Attribute
    String cognosVersion;

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

    /**
     * Builds the minimal object necessary to create a relationship to a CognosPackage, from a potentially
     * more-complete CognosPackage object.
     *
     * @return the minimal object necessary to relate to the CognosPackage
     * @throws InvalidRequestException if any of the minimal set of required properties for a CognosPackage relationship are not found in the initial object
     */
    @Override
    public CognosPackage trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all CognosPackage assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) CognosPackage assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all CognosPackage assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all CognosPackage assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) CognosPackages will be included
     * @return a fluent search that includes all CognosPackage assets
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
     * Reference to a CognosPackage by GUID. Use this to create a relationship to this CognosPackage,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the CognosPackage to reference
     * @return reference to a CognosPackage that can be used for defining a relationship to a CognosPackage
     */
    public static CognosPackage refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a CognosPackage by GUID. Use this to create a relationship to this CognosPackage,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the CognosPackage to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a CognosPackage that can be used for defining a relationship to a CognosPackage
     */
    public static CognosPackage refByGuid(String guid, Reference.SaveSemantic semantic) {
        return CognosPackage._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a CognosPackage by qualifiedName. Use this to create a relationship to this CognosPackage,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the CognosPackage to reference
     * @return reference to a CognosPackage that can be used for defining a relationship to a CognosPackage
     */
    public static CognosPackage refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a CognosPackage by qualifiedName. Use this to create a relationship to this CognosPackage,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the CognosPackage to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a CognosPackage that can be used for defining a relationship to a CognosPackage
     */
    public static CognosPackage refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return CognosPackage._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a CognosPackage by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CognosPackage to retrieve, either its GUID or its full qualifiedName
     * @return the requested full CognosPackage, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CognosPackage does not exist or the provided GUID is not a CognosPackage
     */
    @JsonIgnore
    public static CognosPackage get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a CognosPackage by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CognosPackage to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full CognosPackage, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CognosPackage does not exist or the provided GUID is not a CognosPackage
     */
    @JsonIgnore
    public static CognosPackage get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof CognosPackage) {
                return (CognosPackage) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof CognosPackage) {
                return (CognosPackage) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a CognosPackage by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CognosPackage to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the CognosPackage, including any relationships
     * @return the requested CognosPackage, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CognosPackage does not exist or the provided GUID is not a CognosPackage
     */
    @JsonIgnore
    public static CognosPackage get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a CognosPackage by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CognosPackage to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the CognosPackage, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the CognosPackage
     * @return the requested CognosPackage, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CognosPackage does not exist or the provided GUID is not a CognosPackage
     */
    @JsonIgnore
    public static CognosPackage get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = CognosPackage.select(client)
                    .where(CognosPackage.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof CognosPackage) {
                return (CognosPackage) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = CognosPackage.select(client)
                    .where(CognosPackage.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof CognosPackage) {
                return (CognosPackage) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) CognosPackage to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the CognosPackage
     * @return true if the CognosPackage is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a CognosPackage.
     *
     * @param qualifiedName of the CognosPackage
     * @param name of the CognosPackage
     * @return the minimal request necessary to update the CognosPackage, as a builder
     */
    public static CognosPackageBuilder<?, ?> updater(String qualifiedName, String name) {
        return CognosPackage._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a CognosPackage, from a potentially
     * more-complete CognosPackage object.
     *
     * @return the minimal object necessary to update the CognosPackage, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for CognosPackage are not found in the initial object
     */
    @Override
    public CognosPackageBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a CognosPackage.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the CognosPackage
     * @param name of the CognosPackage
     * @return the updated CognosPackage, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CognosPackage removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CognosPackage) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a CognosPackage.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the CognosPackage
     * @param name of the CognosPackage
     * @return the updated CognosPackage, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CognosPackage removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CognosPackage) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a CognosPackage.
     *
     * @param client connectivity to the Atlan tenant from which to remove the CognosPackage's owners
     * @param qualifiedName of the CognosPackage
     * @param name of the CognosPackage
     * @return the updated CognosPackage, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CognosPackage removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CognosPackage) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a CognosPackage.
     *
     * @param client connectivity to the Atlan tenant on which to update the CognosPackage's certificate
     * @param qualifiedName of the CognosPackage
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated CognosPackage, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CognosPackage updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (CognosPackage)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a CognosPackage.
     *
     * @param client connectivity to the Atlan tenant from which to remove the CognosPackage's certificate
     * @param qualifiedName of the CognosPackage
     * @param name of the CognosPackage
     * @return the updated CognosPackage, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CognosPackage removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CognosPackage) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a CognosPackage.
     *
     * @param client connectivity to the Atlan tenant on which to update the CognosPackage's announcement
     * @param qualifiedName of the CognosPackage
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CognosPackage updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (CognosPackage)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a CognosPackage.
     *
     * @param client connectivity to the Atlan client from which to remove the CognosPackage's announcement
     * @param qualifiedName of the CognosPackage
     * @param name of the CognosPackage
     * @return the updated CognosPackage, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CognosPackage removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CognosPackage) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the CognosPackage.
     *
     * @param client connectivity to the Atlan tenant on which to replace the CognosPackage's assigned terms
     * @param qualifiedName for the CognosPackage
     * @param name human-readable name of the CognosPackage
     * @param terms the list of terms to replace on the CognosPackage, or null to remove all terms from the CognosPackage
     * @return the CognosPackage that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static CognosPackage replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (CognosPackage) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the CognosPackage, without replacing existing terms linked to the CognosPackage.
     * Note: this operation must make two API calls — one to retrieve the CognosPackage's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the CognosPackage
     * @param qualifiedName for the CognosPackage
     * @param terms the list of terms to append to the CognosPackage
     * @return the CognosPackage that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static CognosPackage appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (CognosPackage) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a CognosPackage, without replacing all existing terms linked to the CognosPackage.
     * Note: this operation must make two API calls — one to retrieve the CognosPackage's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the CognosPackage
     * @param qualifiedName for the CognosPackage
     * @param terms the list of terms to remove from the CognosPackage, which must be referenced by GUID
     * @return the CognosPackage that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static CognosPackage removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (CognosPackage) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a CognosPackage, without replacing existing Atlan tags linked to the CognosPackage.
     * Note: this operation must make two API calls — one to retrieve the CognosPackage's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the CognosPackage
     * @param qualifiedName of the CognosPackage
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated CognosPackage
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static CognosPackage appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (CognosPackage) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a CognosPackage, without replacing existing Atlan tags linked to the CognosPackage.
     * Note: this operation must make two API calls — one to retrieve the CognosPackage's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the CognosPackage
     * @param qualifiedName of the CognosPackage
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated CognosPackage
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static CognosPackage appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (CognosPackage) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a CognosPackage.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a CognosPackage
     * @param qualifiedName of the CognosPackage
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the CognosPackage
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
