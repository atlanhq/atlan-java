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
import com.atlan.model.structs.DbtJobRun;
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
 * Instance of a dbt test in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class DbtTest extends Asset implements IDbtTest, IDbt, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DbtTest";

    /** Fixed typeName for DbtTests. */
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
    @Date
    Long dbtJobLastRun;

    /** TBC */
    @Attribute
    String dbtJobName;

    /** TBC */
    @Attribute
    @Date
    Long dbtJobNextRun;

    /** TBC */
    @Attribute
    String dbtJobNextRunHumanized;

    /** List of latest DBT job runs across all environments */
    @Attribute
    @Singular
    List<DbtJobRun> dbtJobRuns;

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
    @Singular
    SortedSet<IDbtModelColumn> dbtModelColumns;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtModel> dbtModels;

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
    SortedSet<IDbtSource> dbtSources;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> dbtTags;

    /** Compiled code of the test (when the test is defined using Python). */
    @Attribute
    String dbtTestCompiledCode;

    /** Compiled SQL of the test. */
    @Attribute
    String dbtTestCompiledSQL;

    /** Error message in the case of state being "error". */
    @Attribute
    String dbtTestError;

    /** Language in which the test is written, for example: SQL or Python. */
    @Attribute
    String dbtTestLanguage;

    /** Raw code of the test (when the test is defined using Python). */
    @Attribute
    String dbtTestRawCode;

    /** Raw SQL of the test. */
    @Attribute
    String dbtTestRawSQL;

    /** Test results. Can be one of, in order of severity, "error", "fail", "warn", "pass". */
    @Attribute
    String dbtTestState;

    /** Details of the results of the test. For errors, it reads "ERROR". */
    @Attribute
    String dbtTestStatus;

    /** TBC */
    @Attribute
    String dbtUniqueId;

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
    SortedSet<ISQL> sqlAssets;

    /**
     * Builds the minimal object necessary to create a relationship to a DbtTest, from a potentially
     * more-complete DbtTest object.
     *
     * @return the minimal object necessary to relate to the DbtTest
     * @throws InvalidRequestException if any of the minimal set of required properties for a DbtTest relationship are not found in the initial object
     */
    @Override
    public DbtTest trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all DbtTest assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DbtTest assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all DbtTest assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all DbtTest assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DbtTests will be included
     * @return a fluent search that includes all DbtTest assets
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
     * Reference to a DbtTest by GUID. Use this to create a relationship to this DbtTest,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the DbtTest to reference
     * @return reference to a DbtTest that can be used for defining a relationship to a DbtTest
     */
    public static DbtTest refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DbtTest by GUID. Use this to create a relationship to this DbtTest,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the DbtTest to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DbtTest that can be used for defining a relationship to a DbtTest
     */
    public static DbtTest refByGuid(String guid, Reference.SaveSemantic semantic) {
        return DbtTest._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a DbtTest by qualifiedName. Use this to create a relationship to this DbtTest,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the DbtTest to reference
     * @return reference to a DbtTest that can be used for defining a relationship to a DbtTest
     */
    public static DbtTest refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a DbtTest by qualifiedName. Use this to create a relationship to this DbtTest,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the DbtTest to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a DbtTest that can be used for defining a relationship to a DbtTest
     */
    public static DbtTest refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return DbtTest._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a DbtTest by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DbtTest to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DbtTest, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtTest does not exist or the provided GUID is not a DbtTest
     */
    @JsonIgnore
    public static DbtTest get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a DbtTest by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DbtTest to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full DbtTest, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtTest does not exist or the provided GUID is not a DbtTest
     */
    @JsonIgnore
    public static DbtTest get(AtlanClient client, String id, boolean includeAllRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof DbtTest) {
                return (DbtTest) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof DbtTest) {
                return (DbtTest) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a DbtTest by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DbtTest to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DbtTest, including any relationships
     * @return the requested DbtTest, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtTest does not exist or the provided GUID is not a DbtTest
     */
    @JsonIgnore
    public static DbtTest get(AtlanClient client, String id, Collection<AtlanField> attributes) throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a DbtTest by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DbtTest to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the DbtTest, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the DbtTest
     * @return the requested DbtTest, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtTest does not exist or the provided GUID is not a DbtTest
     */
    @JsonIgnore
    public static DbtTest get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = DbtTest.select(client)
                    .where(DbtTest.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof DbtTest) {
                return (DbtTest) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = DbtTest.select(client)
                    .where(DbtTest.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof DbtTest) {
                return (DbtTest) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) DbtTest to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DbtTest
     * @return true if the DbtTest is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a DbtTest.
     *
     * @param qualifiedName of the DbtTest
     * @param name of the DbtTest
     * @return the minimal request necessary to update the DbtTest, as a builder
     */
    public static DbtTestBuilder<?, ?> updater(String qualifiedName, String name) {
        return DbtTest._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DbtTest, from a potentially
     * more-complete DbtTest object.
     *
     * @return the minimal object necessary to update the DbtTest, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DbtTest are not found in the initial object
     */
    @Override
    public DbtTestBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a DbtTest.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DbtTest
     * @param name of the DbtTest
     * @return the updated DbtTest, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTest removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtTest) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DbtTest.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DbtTest
     * @param name of the DbtTest
     * @return the updated DbtTest, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTest removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtTest) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DbtTest.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DbtTest's owners
     * @param qualifiedName of the DbtTest
     * @param name of the DbtTest
     * @return the updated DbtTest, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTest removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (DbtTest) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DbtTest.
     *
     * @param client connectivity to the Atlan tenant on which to update the DbtTest's certificate
     * @param qualifiedName of the DbtTest
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DbtTest, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtTest updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DbtTest) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DbtTest.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DbtTest's certificate
     * @param qualifiedName of the DbtTest
     * @param name of the DbtTest
     * @return the updated DbtTest, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTest removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtTest) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DbtTest.
     *
     * @param client connectivity to the Atlan tenant on which to update the DbtTest's announcement
     * @param qualifiedName of the DbtTest
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtTest updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (DbtTest) Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DbtTest.
     *
     * @param client connectivity to the Atlan client from which to remove the DbtTest's announcement
     * @param qualifiedName of the DbtTest
     * @param name of the DbtTest
     * @return the updated DbtTest, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtTest removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtTest) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DbtTest.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DbtTest's assigned terms
     * @param qualifiedName for the DbtTest
     * @param name human-readable name of the DbtTest
     * @param terms the list of terms to replace on the DbtTest, or null to remove all terms from the DbtTest
     * @return the DbtTest that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DbtTest replaceTerms(AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtTest) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DbtTest, without replacing existing terms linked to the DbtTest.
     * Note: this operation must make two API calls — one to retrieve the DbtTest's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DbtTest
     * @param qualifiedName for the DbtTest
     * @param terms the list of terms to append to the DbtTest
     * @return the DbtTest that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DbtTest appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtTest) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DbtTest, without replacing all existing terms linked to the DbtTest.
     * Note: this operation must make two API calls — one to retrieve the DbtTest's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DbtTest
     * @param qualifiedName for the DbtTest
     * @param terms the list of terms to remove from the DbtTest, which must be referenced by GUID
     * @return the DbtTest that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static DbtTest removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtTest) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DbtTest, without replacing existing Atlan tags linked to the DbtTest.
     * Note: this operation must make two API calls — one to retrieve the DbtTest's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DbtTest
     * @param qualifiedName of the DbtTest
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DbtTest
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static DbtTest appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (DbtTest) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtTest, without replacing existing Atlan tags linked to the DbtTest.
     * Note: this operation must make two API calls — one to retrieve the DbtTest's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DbtTest
     * @param qualifiedName of the DbtTest
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DbtTest
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static DbtTest appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DbtTest) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a DbtTest.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DbtTest
     * @param qualifiedName of the DbtTest
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DbtTest
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
