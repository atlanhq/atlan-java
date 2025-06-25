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
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
 * Instance of a lineage process in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class LineageProcess extends Asset implements ILineageProcess, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Process";

    /** Fixed typeName for LineageProcesss. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String additionalEtlContext;

    /** TBC */
    @Attribute
    IAdfActivity adfActivity;

    /** TBC */
    @Attribute
    AIDatasetType aiDatasetType;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> airflowTasks;

    /** TBC */
    @Attribute
    String ast;

    /** TBC */
    @Attribute
    String code;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IColumnProcess> columnProcesses;

    /** TBC */
    @Attribute
    IFivetranConnector fivetranConnector;

    /** TBC */
    @Attribute
    IFlowV07ControlOperation flowV07OrchestratedBy;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ICatalog> inputs;

    /** TBC */
    @Attribute
    IMatillionComponent matillionComponent;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ICatalog> outputs;

    /** TBC */
    @Attribute
    @Singular
    @JsonProperty("parentConnectionProcessQualifiedName")
    SortedSet<String> parentConnectionProcessQualifiedNames;

    /** TBC */
    @Attribute
    IPowerBIDataflow powerBIDataflow;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISparkJob> sparkJobs;

    /** TBC */
    @Attribute
    String sql;

    /**
     * Builds the minimal object necessary to create a relationship to a LineageProcess, from a potentially
     * more-complete LineageProcess object.
     *
     * @return the minimal object necessary to relate to the LineageProcess
     * @throws InvalidRequestException if any of the minimal set of required properties for a LineageProcess relationship are not found in the initial object
     */
    @Override
    public LineageProcess trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all LineageProcess assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) LineageProcess assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all LineageProcess assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all LineageProcess assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) LineageProcesss will be included
     * @return a fluent search that includes all LineageProcess assets
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
     * Reference to a LineageProcess by GUID. Use this to create a relationship to this LineageProcess,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the LineageProcess to reference
     * @return reference to a LineageProcess that can be used for defining a relationship to a LineageProcess
     */
    public static LineageProcess refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a LineageProcess by GUID. Use this to create a relationship to this LineageProcess,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the LineageProcess to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a LineageProcess that can be used for defining a relationship to a LineageProcess
     */
    public static LineageProcess refByGuid(String guid, Reference.SaveSemantic semantic) {
        return LineageProcess._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a LineageProcess by qualifiedName. Use this to create a relationship to this LineageProcess,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the LineageProcess to reference
     * @return reference to a LineageProcess that can be used for defining a relationship to a LineageProcess
     */
    public static LineageProcess refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a LineageProcess by qualifiedName. Use this to create a relationship to this LineageProcess,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the LineageProcess to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a LineageProcess that can be used for defining a relationship to a LineageProcess
     */
    public static LineageProcess refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return LineageProcess._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a LineageProcess by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the LineageProcess to retrieve, either its GUID or its full qualifiedName
     * @return the requested full LineageProcess, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LineageProcess does not exist or the provided GUID is not a LineageProcess
     */
    @JsonIgnore
    public static LineageProcess get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a LineageProcess by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the LineageProcess to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full LineageProcess, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LineageProcess does not exist or the provided GUID is not a LineageProcess
     */
    @JsonIgnore
    public static LineageProcess get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof LineageProcess) {
                return (LineageProcess) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof LineageProcess) {
                return (LineageProcess) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a LineageProcess by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the LineageProcess to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the LineageProcess, including any relationships
     * @return the requested LineageProcess, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LineageProcess does not exist or the provided GUID is not a LineageProcess
     */
    @JsonIgnore
    public static LineageProcess get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a LineageProcess by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the LineageProcess to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the LineageProcess, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the LineageProcess
     * @return the requested LineageProcess, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LineageProcess does not exist or the provided GUID is not a LineageProcess
     */
    @JsonIgnore
    public static LineageProcess get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = LineageProcess.select(client)
                    .where(LineageProcess.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof LineageProcess) {
                return (LineageProcess) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = LineageProcess.select(client)
                    .where(LineageProcess.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof LineageProcess) {
                return (LineageProcess) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) LineageProcess to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the LineageProcess
     * @return true if the LineageProcess is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a process.
     *
     * @param name of the process to use for display purposes
     * @param connectionQualifiedName unique name of the specific instance of that software / system that ran the process
     * @param id (optional) unique ID of this process within the software / system that ran it (if not provided, it will be generated)
     * @param inputs sources of data the process reads from
     * @param outputs targets of data the process writes to
     * @param parent (optional) parent process in which this sub-process ran
     * @return the minimal object necessary to create the process, as a builder
     */
    public static LineageProcessBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String id,
            List<ICatalog> inputs,
            List<ICatalog> outputs,
            LineageProcess parent) {
        return LineageProcess._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName, id, inputs, outputs, parent))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .inputs(inputs)
                .outputs(outputs);
    }

    /**
     * Builds the minimal object necessary to update a LineageProcess.
     *
     * @param qualifiedName of the LineageProcess
     * @param name of the LineageProcess
     * @return the minimal request necessary to update the LineageProcess, as a builder
     */
    public static LineageProcessBuilder<?, ?> updater(String qualifiedName, String name) {
        return LineageProcess._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a LineageProcess, from a potentially
     * more-complete LineageProcess object.
     *
     * @return the minimal object necessary to update the LineageProcess, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for LineageProcess are not found in the initial object
     */
    @Override
    public LineageProcessBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Generate a unique qualifiedName for a process.
     *
     * @param name of the process
     * @param connectionQualifiedName unique name of the specific instance of the software / system that ran the process
     * @param id (optional) unique ID of this process within the software / system that ran it (if not provided, it will be generated)
     * @param inputs sources of data the process reads from
     * @param outputs targets of data the process writes to
     * @param parent (optional) parent process in which this sub-process ran
     * @return unique name for the process
     */
    public static String generateQualifiedName(
            String name,
            String connectionQualifiedName,
            String id,
            List<ICatalog> inputs,
            List<ICatalog> outputs,
            LineageProcess parent) {
        // If an ID was provided, use that as the unique name for the process
        if (id != null && id.length() > 0) {
            return connectionQualifiedName + "/" + id;
        } else {
            // Otherwise, hash all the relationships to arrive at a consistent
            // generated qualifiedName
            StringBuilder sb = new StringBuilder();
            sb.append(name).append(connectionQualifiedName);
            if (parent != null) {
                appendRelationship(sb, parent);
            }
            appendRelationships(sb, inputs);
            appendRelationships(sb, outputs);
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(sb.toString().getBytes(StandardCharsets.UTF_8));
                String hashed = String.format("%032x", new BigInteger(1, md.digest()));
                return connectionQualifiedName + "/" + hashed;
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(
                        "Unable to generate the qualifiedName for the process: MD5 algorithm does not exist on your platform!");
            }
        }
    }

    /**
     * Append all the relationships into the provided string builder.
     * @param sb into which to append
     * @param relationships to append
     */
    private static void appendRelationships(StringBuilder sb, List<ICatalog> relationships) {
        for (ICatalog relationship : relationships) {
            appendRelationship(sb, (IAsset) relationship);
        }
    }

    /**
     * Append a single relationship into the provided string builder.
     * @param sb into which to append
     * @param relationship to append
     */
    private static void appendRelationship(StringBuilder sb, IAsset relationship) {
        // TODO: if two calls are made for the same process, but one uses GUIDs for
        //  its references and the other uses qualifiedName, we'll end up with different
        //  hashes (duplicate processes)
        if (relationship.getGuid() != null) {
            sb.append(relationship.getGuid());
        } else if (relationship.getUniqueAttributes() != null
                && relationship.getUniqueAttributes().getQualifiedName() != null) {
            sb.append(relationship.getUniqueAttributes().getQualifiedName());
        }
    }

    public abstract static class LineageProcessBuilder<C extends LineageProcess, B extends LineageProcessBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a LineageProcess.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the LineageProcess
     * @param name of the LineageProcess
     * @return the updated LineageProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LineageProcess removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LineageProcess) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a LineageProcess.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the LineageProcess
     * @param name of the LineageProcess
     * @return the updated LineageProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LineageProcess removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LineageProcess) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a LineageProcess.
     *
     * @param client connectivity to the Atlan tenant from which to remove the LineageProcess's owners
     * @param qualifiedName of the LineageProcess
     * @param name of the LineageProcess
     * @return the updated LineageProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LineageProcess removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LineageProcess) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a LineageProcess.
     *
     * @param client connectivity to the Atlan tenant on which to update the LineageProcess's certificate
     * @param qualifiedName of the LineageProcess
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated LineageProcess, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LineageProcess updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (LineageProcess)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a LineageProcess.
     *
     * @param client connectivity to the Atlan tenant from which to remove the LineageProcess's certificate
     * @param qualifiedName of the LineageProcess
     * @param name of the LineageProcess
     * @return the updated LineageProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LineageProcess removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LineageProcess) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a LineageProcess.
     *
     * @param client connectivity to the Atlan tenant on which to update the LineageProcess's announcement
     * @param qualifiedName of the LineageProcess
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LineageProcess updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (LineageProcess)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a LineageProcess.
     *
     * @param client connectivity to the Atlan client from which to remove the LineageProcess's announcement
     * @param qualifiedName of the LineageProcess
     * @param name of the LineageProcess
     * @return the updated LineageProcess, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LineageProcess removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LineageProcess) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the LineageProcess.
     *
     * @param client connectivity to the Atlan tenant on which to replace the LineageProcess's assigned terms
     * @param qualifiedName for the LineageProcess
     * @param name human-readable name of the LineageProcess
     * @param terms the list of terms to replace on the LineageProcess, or null to remove all terms from the LineageProcess
     * @return the LineageProcess that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static LineageProcess replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (LineageProcess) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the LineageProcess, without replacing existing terms linked to the LineageProcess.
     * Note: this operation must make two API calls — one to retrieve the LineageProcess's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the LineageProcess
     * @param qualifiedName for the LineageProcess
     * @param terms the list of terms to append to the LineageProcess
     * @return the LineageProcess that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static LineageProcess appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (LineageProcess) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a LineageProcess, without replacing all existing terms linked to the LineageProcess.
     * Note: this operation must make two API calls — one to retrieve the LineageProcess's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the LineageProcess
     * @param qualifiedName for the LineageProcess
     * @param terms the list of terms to remove from the LineageProcess, which must be referenced by GUID
     * @return the LineageProcess that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static LineageProcess removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (LineageProcess) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a LineageProcess, without replacing existing Atlan tags linked to the LineageProcess.
     * Note: this operation must make two API calls — one to retrieve the LineageProcess's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the LineageProcess
     * @param qualifiedName of the LineageProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated LineageProcess
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static LineageProcess appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (LineageProcess) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a LineageProcess, without replacing existing Atlan tags linked to the LineageProcess.
     * Note: this operation must make two API calls — one to retrieve the LineageProcess's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the LineageProcess
     * @param qualifiedName of the LineageProcess
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated LineageProcess
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static LineageProcess appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (LineageProcess) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a LineageProcess.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a LineageProcess
     * @param qualifiedName of the LineageProcess
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the LineageProcess
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
