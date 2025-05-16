/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.ADLSAccessTier;
import com.atlan.model.enums.ADLSAccountStatus;
import com.atlan.model.enums.ADLSEncryptionTypes;
import com.atlan.model.enums.ADLSPerformance;
import com.atlan.model.enums.ADLSProvisionState;
import com.atlan.model.enums.ADLSReplicationType;
import com.atlan.model.enums.ADLSStorageKind;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.model.structs.AzureTag;
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
 * Instance of an Azure Data Lake Storage (ADLS) account in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class ADLSAccount extends Asset
        implements IADLSAccount, IADLS, IAzure, IObjectStore, ICloud, IAsset, IReferenceable, ICatalog {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ADLSAccount";

    /** Fixed typeName for ADLSAccounts. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Access tier of this account. */
    @Attribute
    ADLSAccessTier adlsAccountAccessTier;

    /** Kind of this account. */
    @Attribute
    ADLSStorageKind adlsAccountKind;

    /** Name of the account for this ADLS asset. */
    @Attribute
    String adlsAccountName;

    /** Performance of this account. */
    @Attribute
    ADLSPerformance adlsAccountPerformance;

    /** Provision state of this account. */
    @Attribute
    ADLSProvisionState adlsAccountProvisionState;

    /** Unique name of the account for this ADLS asset. */
    @Attribute
    String adlsAccountQualifiedName;

    /** Replication of this account. */
    @Attribute
    ADLSReplicationType adlsAccountReplication;

    /** Resource group for this account. */
    @Attribute
    String adlsAccountResourceGroup;

    /** Secondary location of the ADLS account. */
    @Attribute
    String adlsAccountSecondaryLocation;

    /** Subscription for this account. */
    @Attribute
    String adlsAccountSubscription;

    /** Containers that exist within this account. */
    @Attribute
    @Singular
    SortedSet<IADLSContainer> adlsContainers;

    /** Entity tag for the asset. An entity tag is a hash of the object and represents changes to the contents of an object only, not its metadata. */
    @Attribute
    String adlsETag;

    /** Type of encryption for this account. */
    @Attribute
    ADLSEncryptionTypes adlsEncryptionType;

    /** Primary disk state of this account. */
    @Attribute
    ADLSAccountStatus adlsPrimaryDiskState;

    /** Location of this asset in Azure. */
    @Attribute
    String azureLocation;

    /** Resource identifier of this asset in Azure. */
    @Attribute
    String azureResourceId;

    /** Tags that have been applied to this asset in Azure. */
    @Attribute
    @Singular
    List<AzureTag> azureTags;

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
     * Builds the minimal object necessary to create a relationship to a ADLSAccount, from a potentially
     * more-complete ADLSAccount object.
     *
     * @return the minimal object necessary to relate to the ADLSAccount
     * @throws InvalidRequestException if any of the minimal set of required properties for a ADLSAccount relationship are not found in the initial object
     */
    @Override
    public ADLSAccount trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all ADLSAccount assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ADLSAccount assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all ADLSAccount assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all ADLSAccount assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ADLSAccounts will be included
     * @return a fluent search that includes all ADLSAccount assets
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
     * Reference to a ADLSAccount by GUID. Use this to create a relationship to this ADLSAccount,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the ADLSAccount to reference
     * @return reference to a ADLSAccount that can be used for defining a relationship to a ADLSAccount
     */
    public static ADLSAccount refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ADLSAccount by GUID. Use this to create a relationship to this ADLSAccount,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the ADLSAccount to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ADLSAccount that can be used for defining a relationship to a ADLSAccount
     */
    public static ADLSAccount refByGuid(String guid, Reference.SaveSemantic semantic) {
        return ADLSAccount._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a ADLSAccount by qualifiedName. Use this to create a relationship to this ADLSAccount,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the ADLSAccount to reference
     * @return reference to a ADLSAccount that can be used for defining a relationship to a ADLSAccount
     */
    public static ADLSAccount refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ADLSAccount by qualifiedName. Use this to create a relationship to this ADLSAccount,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the ADLSAccount to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ADLSAccount that can be used for defining a relationship to a ADLSAccount
     */
    public static ADLSAccount refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return ADLSAccount._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a ADLSAccount by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ADLSAccount to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ADLSAccount, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSAccount does not exist or the provided GUID is not a ADLSAccount
     */
    @JsonIgnore
    public static ADLSAccount get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a ADLSAccount by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ADLSAccount to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full ADLSAccount, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSAccount does not exist or the provided GUID is not a ADLSAccount
     */
    @JsonIgnore
    public static ADLSAccount get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof ADLSAccount) {
                return (ADLSAccount) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof ADLSAccount) {
                return (ADLSAccount) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a ADLSAccount by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ADLSAccount to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the ADLSAccount, including any relationships
     * @return the requested ADLSAccount, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSAccount does not exist or the provided GUID is not a ADLSAccount
     */
    @JsonIgnore
    public static ADLSAccount get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a ADLSAccount by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ADLSAccount to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the ADLSAccount, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the ADLSAccount
     * @return the requested ADLSAccount, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ADLSAccount does not exist or the provided GUID is not a ADLSAccount
     */
    @JsonIgnore
    public static ADLSAccount get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = ADLSAccount.select(client)
                    .where(ADLSAccount.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof ADLSAccount) {
                return (ADLSAccount) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = ADLSAccount.select(client)
                    .where(ADLSAccount.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof ADLSAccount) {
                return (ADLSAccount) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) ADLSAccount to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the ADLSAccount
     * @return true if the ADLSAccount is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ADLSAccount.
     *
     * @param name of the ADLSAccount
     * @param connectionQualifiedName unique name of the connection through which the ADLSAccount is accessible
     * @return the minimal object necessary to create the ADLSAccount, as a builder
     */
    public static ADLSAccountBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return ADLSAccount._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Generate a unique ADLSAccount name.
     *
     * @param name of the ADLSAccount
     * @param connectionQualifiedName unique name of the connection through which the ADLSAccount is accessible
     * @return a unique name for the ADLSAccount
     */
    public static String generateQualifiedName(String name, String connectionQualifiedName) {
        return connectionQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a ADLSAccount.
     *
     * @param qualifiedName of the ADLSAccount
     * @param name of the ADLSAccount
     * @return the minimal request necessary to update the ADLSAccount, as a builder
     */
    public static ADLSAccountBuilder<?, ?> updater(String qualifiedName, String name) {
        return ADLSAccount._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ADLSAccount, from a potentially
     * more-complete ADLSAccount object.
     *
     * @return the minimal object necessary to update the ADLSAccount, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ADLSAccount are not found in the initial object
     */
    @Override
    public ADLSAccountBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ADLSAccountBuilder<C extends ADLSAccount, B extends ADLSAccountBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a ADLSAccount.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ADLSAccount
     * @param name of the ADLSAccount
     * @return the updated ADLSAccount, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ADLSAccount) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ADLSAccount.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ADLSAccount
     * @param name of the ADLSAccount
     * @return the updated ADLSAccount, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ADLSAccount) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ADLSAccount.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ADLSAccount's owners
     * @param qualifiedName of the ADLSAccount
     * @param name of the ADLSAccount
     * @return the updated ADLSAccount, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ADLSAccount) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ADLSAccount.
     *
     * @param client connectivity to the Atlan tenant on which to update the ADLSAccount's certificate
     * @param qualifiedName of the ADLSAccount
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ADLSAccount, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (ADLSAccount)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ADLSAccount.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ADLSAccount's certificate
     * @param qualifiedName of the ADLSAccount
     * @param name of the ADLSAccount
     * @return the updated ADLSAccount, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ADLSAccount) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ADLSAccount.
     *
     * @param client connectivity to the Atlan tenant on which to update the ADLSAccount's announcement
     * @param qualifiedName of the ADLSAccount
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (ADLSAccount)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ADLSAccount.
     *
     * @param client connectivity to the Atlan client from which to remove the ADLSAccount's announcement
     * @param qualifiedName of the ADLSAccount
     * @param name of the ADLSAccount
     * @return the updated ADLSAccount, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ADLSAccount) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the ADLSAccount.
     *
     * @param client connectivity to the Atlan tenant on which to replace the ADLSAccount's assigned terms
     * @param qualifiedName for the ADLSAccount
     * @param name human-readable name of the ADLSAccount
     * @param terms the list of terms to replace on the ADLSAccount, or null to remove all terms from the ADLSAccount
     * @return the ADLSAccount that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ADLSAccount replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (ADLSAccount) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ADLSAccount, without replacing existing terms linked to the ADLSAccount.
     * Note: this operation must make two API calls — one to retrieve the ADLSAccount's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the ADLSAccount
     * @param qualifiedName for the ADLSAccount
     * @param terms the list of terms to append to the ADLSAccount
     * @return the ADLSAccount that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static ADLSAccount appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ADLSAccount) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ADLSAccount, without replacing all existing terms linked to the ADLSAccount.
     * Note: this operation must make two API calls — one to retrieve the ADLSAccount's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the ADLSAccount
     * @param qualifiedName for the ADLSAccount
     * @param terms the list of terms to remove from the ADLSAccount, which must be referenced by GUID
     * @return the ADLSAccount that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static ADLSAccount removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ADLSAccount) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a ADLSAccount, without replacing existing Atlan tags linked to the ADLSAccount.
     * Note: this operation must make two API calls — one to retrieve the ADLSAccount's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ADLSAccount
     * @param qualifiedName of the ADLSAccount
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ADLSAccount
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static ADLSAccount appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (ADLSAccount) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ADLSAccount, without replacing existing Atlan tags linked to the ADLSAccount.
     * Note: this operation must make two API calls — one to retrieve the ADLSAccount's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ADLSAccount
     * @param qualifiedName of the ADLSAccount
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ADLSAccount
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static ADLSAccount appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (ADLSAccount) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a ADLSAccount.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a ADLSAccount
     * @param qualifiedName of the ADLSAccount
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ADLSAccount
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
