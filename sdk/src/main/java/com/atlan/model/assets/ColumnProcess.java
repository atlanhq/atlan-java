/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AIDatasetType;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
 * Instance of a column-level lineage process in Atlan. Inputs and outputs of these processes should be columns.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class ColumnProcess extends Asset implements IColumnProcess, ILineageProcess, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ColumnProcess";

    /** Fixed typeName for ColumnProcesss. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Additional Context of the ETL pipeline/notebook which creates the process. */
    @Attribute
    String additionalEtlContext;

    /** ADF Activity that is associated with this lineage process. */
    @Attribute
    IAdfActivity adfActivity;

    /** Dataset type for AI Model - dataset process. */
    @Attribute
    AIDatasetType aiDatasetType;

    /** Tasks that exist within this process. */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> airflowTasks;

    /** Parsed AST of the code or SQL statements that describe the logic of this process. */
    @Attribute
    String ast;

    /** Code that ran within the process. */
    @Attribute
    String code;

    /** Processes that detail column-level lineage for this process. */
    @Attribute
    @Singular
    SortedSet<IColumnProcess> columnProcesses;

    /** fivetranConnector in which this process exists. */
    @Attribute
    IFivetranConnector fivetranConnector;

    /** Orchestrated control operation that ran these data flows (process). */
    @Attribute
    IFlowControlOperation flowOrchestratedBy;

    /** Assets that are inputs to this process. */
    @Attribute
    @Singular
    SortedSet<ICatalog> inputs;

    /** Matillion component that contains the logic for this lineage process. */
    @Attribute
    IMatillionComponent matillionComponent;

    /** Assets that are outputs from this process. */
    @Attribute
    @Singular
    SortedSet<ICatalog> outputs;

    /** TBC */
    @Attribute
    @Singular
    @JsonProperty("parentConnectionProcessQualifiedName")
    SortedSet<String> parentConnectionProcessQualifiedNames;

    /** PowerBI Dataflow that is associated with this lineage process. */
    @Attribute
    IPowerBIDataflow powerBIDataflow;

    /** Process in which this task exists. */
    @Attribute
    ILineageProcess process;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISparkJob> sparkJobs;

    /** SQL query that ran to produce the outputs. */
    @Attribute
    String sql;

    /**
     * Builds the minimal object necessary to create a relationship to a ColumnProcess, from a potentially
     * more-complete ColumnProcess object.
     *
     * @return the minimal object necessary to relate to the ColumnProcess
     * @throws InvalidRequestException if any of the minimal set of required properties for a ColumnProcess relationship are not found in the initial object
     */
    @Override
    public ColumnProcess trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all ColumnProcess assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ColumnProcess assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all ColumnProcess assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all ColumnProcess assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ColumnProcesss will be included
     * @return a fluent search that includes all ColumnProcess assets
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
     * Reference to a ColumnProcess by GUID. Use this to create a relationship to this ColumnProcess,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the ColumnProcess to reference
     * @return reference to a ColumnProcess that can be used for defining a relationship to a ColumnProcess
     */
    public static ColumnProcess refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ColumnProcess by GUID. Use this to create a relationship to this ColumnProcess,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the ColumnProcess to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ColumnProcess that can be used for defining a relationship to a ColumnProcess
     */
    public static ColumnProcess refByGuid(String guid, Reference.SaveSemantic semantic) {
        return ColumnProcess._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a ColumnProcess by qualifiedName. Use this to create a relationship to this ColumnProcess,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the ColumnProcess to reference
     * @return reference to a ColumnProcess that can be used for defining a relationship to a ColumnProcess
     */
    public static ColumnProcess refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ColumnProcess by qualifiedName. Use this to create a relationship to this ColumnProcess,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the ColumnProcess to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ColumnProcess that can be used for defining a relationship to a ColumnProcess
     */
    public static ColumnProcess refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return ColumnProcess._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a ColumnProcess by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ColumnProcess to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ColumnProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ColumnProcess does not exist or the provided GUID is not a ColumnProcess
     */
    @JsonIgnore
    public static ColumnProcess get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a ColumnProcess by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ColumnProcess to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full ColumnProcess, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ColumnProcess does not exist or the provided GUID is not a ColumnProcess
     */
    @JsonIgnore
    public static ColumnProcess get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof ColumnProcess) {
                return (ColumnProcess) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof ColumnProcess) {
                return (ColumnProcess) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a ColumnProcess by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ColumnProcess to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the ColumnProcess, including any relationships
     * @return the requested ColumnProcess, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ColumnProcess does not exist or the provided GUID is not a ColumnProcess
     */
    @JsonIgnore
    public static ColumnProcess get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a ColumnProcess by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ColumnProcess to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the ColumnProcess, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the ColumnProcess
     * @return the requested ColumnProcess, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ColumnProcess does not exist or the provided GUID is not a ColumnProcess
     */
    @JsonIgnore
    public static ColumnProcess get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = ColumnProcess.select(client)
                    .where(ColumnProcess.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof ColumnProcess) {
                return (ColumnProcess) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = ColumnProcess.select(client)
                    .where(ColumnProcess.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof ColumnProcess) {
                return (ColumnProcess) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) ColumnProcess to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the ColumnProcess
     * @return true if the ColumnProcess is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a column-level process.
     *
     * @param name of the column-level process
     * @param connectionQualifiedName unique name of the specific instance of that software / system that ran the process
     * @param id (optional) unique ID of this process within the software / system that ran it (if not provided, it will be generated)
     * @param inputs columns of data the process reads from
     * @param outputs columns of data the process writes to
     * @param parent parent process in which this column-level process ran
     * @return the minimal object necessary to create the column-level process, as a builder
     */
    public static ColumnProcessBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String id,
            List<ICatalog> inputs,
            List<ICatalog> outputs,
            LineageProcess parent) {
        String connectionName = StringUtils.getNameFromQualifiedName(connectionQualifiedName);
        ColumnProcessBuilder<?, ?> builder = ColumnProcess._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(LineageProcess.generateQualifiedName(
                        name, connectionQualifiedName, id, inputs, outputs, parent))
                .name(name)
                .connectionName(connectionName)
                .connectionQualifiedName(connectionQualifiedName)
                .inputs(inputs)
                .outputs(outputs);
        if (parent != null) {
            builder.process(parent);
        }
        return builder;
    }

    /**
     * Builds the minimal object necessary to update a ColumnProcess.
     *
     * @param qualifiedName of the ColumnProcess
     * @param name of the ColumnProcess
     * @return the minimal request necessary to update the ColumnProcess, as a builder
     */
    public static ColumnProcessBuilder<?, ?> updater(String qualifiedName, String name) {
        return ColumnProcess._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ColumnProcess, from a potentially
     * more-complete ColumnProcess object.
     *
     * @return the minimal object necessary to update the ColumnProcess, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ColumnProcess are not found in the initial object
     */
    @Override
    public ColumnProcessBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ColumnProcessBuilder<C extends ColumnProcess, B extends ColumnProcessBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a ColumnProcess.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ColumnProcess
     * @param name of the ColumnProcess
     * @return the updated ColumnProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ColumnProcess removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ColumnProcess) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ColumnProcess.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ColumnProcess
     * @param name of the ColumnProcess
     * @return the updated ColumnProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ColumnProcess removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ColumnProcess) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ColumnProcess.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ColumnProcess's owners
     * @param qualifiedName of the ColumnProcess
     * @param name of the ColumnProcess
     * @return the updated ColumnProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ColumnProcess removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ColumnProcess) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ColumnProcess.
     *
     * @param client connectivity to the Atlan tenant on which to update the ColumnProcess's certificate
     * @param qualifiedName of the ColumnProcess
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ColumnProcess, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ColumnProcess updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (ColumnProcess)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ColumnProcess.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ColumnProcess's certificate
     * @param qualifiedName of the ColumnProcess
     * @param name of the ColumnProcess
     * @return the updated ColumnProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ColumnProcess removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ColumnProcess) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ColumnProcess.
     *
     * @param client connectivity to the Atlan tenant on which to update the ColumnProcess's announcement
     * @param qualifiedName of the ColumnProcess
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ColumnProcess updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (ColumnProcess)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ColumnProcess.
     *
     * @param client connectivity to the Atlan client from which to remove the ColumnProcess's announcement
     * @param qualifiedName of the ColumnProcess
     * @param name of the ColumnProcess
     * @return the updated ColumnProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ColumnProcess removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ColumnProcess) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the ColumnProcess.
     *
     * @param client connectivity to the Atlan tenant on which to replace the ColumnProcess's assigned terms
     * @param qualifiedName for the ColumnProcess
     * @param name human-readable name of the ColumnProcess
     * @param terms the list of terms to replace on the ColumnProcess, or null to remove all terms from the ColumnProcess
     * @return the ColumnProcess that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ColumnProcess replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (ColumnProcess) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ColumnProcess, without replacing existing terms linked to the ColumnProcess.
     * Note: this operation must make two API calls — one to retrieve the ColumnProcess's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the ColumnProcess
     * @param qualifiedName for the ColumnProcess
     * @param terms the list of terms to append to the ColumnProcess
     * @return the ColumnProcess that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static ColumnProcess appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ColumnProcess) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ColumnProcess, without replacing all existing terms linked to the ColumnProcess.
     * Note: this operation must make two API calls — one to retrieve the ColumnProcess's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the ColumnProcess
     * @param qualifiedName for the ColumnProcess
     * @param terms the list of terms to remove from the ColumnProcess, which must be referenced by GUID
     * @return the ColumnProcess that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static ColumnProcess removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ColumnProcess) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a ColumnProcess, without replacing existing Atlan tags linked to the ColumnProcess.
     * Note: this operation must make two API calls — one to retrieve the ColumnProcess's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ColumnProcess
     * @param qualifiedName of the ColumnProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ColumnProcess
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static ColumnProcess appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (ColumnProcess) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ColumnProcess, without replacing existing Atlan tags linked to the ColumnProcess.
     * Note: this operation must make two API calls — one to retrieve the ColumnProcess's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ColumnProcess
     * @param qualifiedName of the ColumnProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ColumnProcess
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static ColumnProcess appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (ColumnProcess) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a ColumnProcess.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a ColumnProcess
     * @param qualifiedName of the ColumnProcess
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ColumnProcess
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
