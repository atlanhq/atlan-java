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
 * Base class for ADF Datasets. It is a named view of data that references or points to the data you want to use in activities.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class AdfDataset extends Asset implements IAdfDataset, IADF, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AdfDataset";

    /** Fixed typeName for AdfDatasets. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** ADF Dataset that is associated with these ADF activities. */
    @Attribute
    @Singular
    SortedSet<IAdfActivity> adfActivities;

    /** Defines the folder path in which this ADF asset exists. */
    @Attribute
    String adfAssetFolderPath;

    /** ADF Datasets that are associated with this ADF Dataflows. */
    @Attribute
    @Singular
    SortedSet<IAdfDataflow> adfDataflows;

    /** The list of annotation assigned to a dataset. */
    @Attribute
    @Singular
    SortedSet<String> adfDatasetAnnotations;

    /** Defines the name collection in the cosmos dataset. */
    @Attribute
    String adfDatasetCollectionName;

    /** Defines the container or bucket name in the storage file system dataset. */
    @Attribute
    String adfDatasetContainerName;

    /** Defines the name of the database used in the azure delta lake type of dataset. */
    @Attribute
    String adfDatasetDatabaseName;

    /** Defines the folder path of the file in the storage file system dataset. */
    @Attribute
    String adfDatasetFileFolderPath;

    /** Defines the name of the file in the storage file system dataset. */
    @Attribute
    String adfDatasetFileName;

    /** Defines the name of the linked service used to create this dataset. */
    @Attribute
    String adfDatasetLinkedService;

    /** Defines the name of the schema used in the snowflake, mssql, azure sql database type of dataset. */
    @Attribute
    String adfDatasetSchemaName;

    /** Defines the storage type of storage file system dataset. */
    @Attribute
    String adfDatasetStorageType;

    /** Defines the name of the table used in the snowflake, mssql, azure sql database type of dataset. */
    @Attribute
    String adfDatasetTableName;

    /** Defines the type of the dataset. */
    @Attribute
    String adfDatasetType;

    /** Defines the name of the factory in which this asset exists. */
    @Attribute
    String adfFactoryName;

    /** ADF datasets that are associated with this ADF Linkedservice. */
    @Attribute
    IAdfLinkedservice adfLinkedservice;

    /** ADF Datasets that are associated with this ADF pipelines. */
    @Attribute
    @Singular
    SortedSet<IAdfPipeline> adfPipelines;

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
     * Builds the minimal object necessary to create a relationship to a AdfDataset, from a potentially
     * more-complete AdfDataset object.
     *
     * @return the minimal object necessary to relate to the AdfDataset
     * @throws InvalidRequestException if any of the minimal set of required properties for a AdfDataset relationship are not found in the initial object
     */
    @Override
    public AdfDataset trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all AdfDataset assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) AdfDataset assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all AdfDataset assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all AdfDataset assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) AdfDatasets will be included
     * @return a fluent search that includes all AdfDataset assets
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
     * Reference to a AdfDataset by GUID. Use this to create a relationship to this AdfDataset,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the AdfDataset to reference
     * @return reference to a AdfDataset that can be used for defining a relationship to a AdfDataset
     */
    public static AdfDataset refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AdfDataset by GUID. Use this to create a relationship to this AdfDataset,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the AdfDataset to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AdfDataset that can be used for defining a relationship to a AdfDataset
     */
    public static AdfDataset refByGuid(String guid, Reference.SaveSemantic semantic) {
        return AdfDataset._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a AdfDataset by qualifiedName. Use this to create a relationship to this AdfDataset,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the AdfDataset to reference
     * @return reference to a AdfDataset that can be used for defining a relationship to a AdfDataset
     */
    public static AdfDataset refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AdfDataset by qualifiedName. Use this to create a relationship to this AdfDataset,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the AdfDataset to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AdfDataset that can be used for defining a relationship to a AdfDataset
     */
    public static AdfDataset refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return AdfDataset._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a AdfDataset by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AdfDataset to retrieve, either its GUID or its full qualifiedName
     * @return the requested full AdfDataset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AdfDataset does not exist or the provided GUID is not a AdfDataset
     */
    @JsonIgnore
    public static AdfDataset get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a AdfDataset by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AdfDataset to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full AdfDataset, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AdfDataset does not exist or the provided GUID is not a AdfDataset
     */
    @JsonIgnore
    public static AdfDataset get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof AdfDataset) {
                return (AdfDataset) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof AdfDataset) {
                return (AdfDataset) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) AdfDataset to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the AdfDataset
     * @return true if the AdfDataset is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a AdfDataset.
     *
     * @param qualifiedName of the AdfDataset
     * @param name of the AdfDataset
     * @return the minimal request necessary to update the AdfDataset, as a builder
     */
    public static AdfDatasetBuilder<?, ?> updater(String qualifiedName, String name) {
        return AdfDataset._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AdfDataset, from a potentially
     * more-complete AdfDataset object.
     *
     * @return the minimal object necessary to update the AdfDataset, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AdfDataset are not found in the initial object
     */
    @Override
    public AdfDatasetBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a AdfDataset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AdfDataset
     * @param name of the AdfDataset
     * @return the updated AdfDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AdfDataset removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AdfDataset) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a AdfDataset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the AdfDataset
     * @param name of the AdfDataset
     * @return the updated AdfDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AdfDataset removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AdfDataset) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a AdfDataset.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AdfDataset's owners
     * @param qualifiedName of the AdfDataset
     * @param name of the AdfDataset
     * @return the updated AdfDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AdfDataset removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (AdfDataset) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a AdfDataset.
     *
     * @param client connectivity to the Atlan tenant on which to update the AdfDataset's certificate
     * @param qualifiedName of the AdfDataset
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AdfDataset, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AdfDataset updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (AdfDataset)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a AdfDataset.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AdfDataset's certificate
     * @param qualifiedName of the AdfDataset
     * @param name of the AdfDataset
     * @return the updated AdfDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AdfDataset removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AdfDataset) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a AdfDataset.
     *
     * @param client connectivity to the Atlan tenant on which to update the AdfDataset's announcement
     * @param qualifiedName of the AdfDataset
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AdfDataset updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (AdfDataset)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a AdfDataset.
     *
     * @param client connectivity to the Atlan client from which to remove the AdfDataset's announcement
     * @param qualifiedName of the AdfDataset
     * @param name of the AdfDataset
     * @return the updated AdfDataset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AdfDataset removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (AdfDataset) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the AdfDataset.
     *
     * @param client connectivity to the Atlan tenant on which to replace the AdfDataset's assigned terms
     * @param qualifiedName for the AdfDataset
     * @param name human-readable name of the AdfDataset
     * @param terms the list of terms to replace on the AdfDataset, or null to remove all terms from the AdfDataset
     * @return the AdfDataset that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static AdfDataset replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (AdfDataset) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the AdfDataset, without replacing existing terms linked to the AdfDataset.
     * Note: this operation must make two API calls — one to retrieve the AdfDataset's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the AdfDataset
     * @param qualifiedName for the AdfDataset
     * @param terms the list of terms to append to the AdfDataset
     * @return the AdfDataset that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static AdfDataset appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AdfDataset) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a AdfDataset, without replacing all existing terms linked to the AdfDataset.
     * Note: this operation must make two API calls — one to retrieve the AdfDataset's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the AdfDataset
     * @param qualifiedName for the AdfDataset
     * @param terms the list of terms to remove from the AdfDataset, which must be referenced by GUID
     * @return the AdfDataset that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static AdfDataset removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (AdfDataset) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a AdfDataset, without replacing existing Atlan tags linked to the AdfDataset.
     * Note: this operation must make two API calls — one to retrieve the AdfDataset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AdfDataset
     * @param qualifiedName of the AdfDataset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated AdfDataset
     */
    public static AdfDataset appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (AdfDataset) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AdfDataset, without replacing existing Atlan tags linked to the AdfDataset.
     * Note: this operation must make two API calls — one to retrieve the AdfDataset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AdfDataset
     * @param qualifiedName of the AdfDataset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated AdfDataset
     */
    public static AdfDataset appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (AdfDataset) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a AdfDataset.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a AdfDataset
     * @param qualifiedName of the AdfDataset
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the AdfDataset
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
