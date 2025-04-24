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
 * Instance of a SAP Component in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class SapErpComponent extends Asset implements ISapErpComponent, ISAP, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SapErpComponent";

    /** Fixed typeName for SapErpComponents. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Child SAP ERP Component associated with this SAP ERP Components. */
    @Attribute
    @Singular
    SortedSet<ISapErpComponent> childComponents;

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

    /** Parent SAP ERP Component in which these child SAP ERP Component exist. */
    @Attribute
    ISapErpComponent parentComponent;

    /** Name of the SAP component, representing a specific functional area in SAP. */
    @Attribute
    String sapComponentName;

    /** SAP-specific data types */
    @Attribute
    String sapDataType;

    /** SAP ERP Component associated with this SAP ERP ABAP Programs. */
    @Attribute
    @Singular
    SortedSet<ISapErpAbapProgram> sapErpAbapPrograms;

    /** SAP ERP Component associated with this SAP ERP Function Modules. */
    @Attribute
    @Singular
    SortedSet<ISapErpFunctionModule> sapErpFunctionModules;

    /** SAP ERP Component associated with these SAP ERP Tables. */
    @Attribute
    @Singular
    SortedSet<ISapErpTable> sapErpTables;

    /** SAP ERP Component associated with these SAP ERP Transaction Codes. */
    @Attribute
    @Singular
    SortedSet<ISapErpTransactionCode> sapErpTransactionCodes;

    /** SAP ERP Component associated with this SAP ERP Views. */
    @Attribute
    @Singular
    SortedSet<ISapErpView> sapErpViews;

    /** Represents the total number of fields, columns, or child assets present in a given SAP asset. */
    @Attribute
    Long sapFieldCount;

    /** Indicates the sequential position of a field, column, or child asset within its parent SAP asset, starting from 1. */
    @Attribute
    Integer sapFieldOrder;

    /** Logical, business-friendly identifier for SAP data objects, aligned with business terminology and concepts. */
    @Attribute
    String sapLogicalName;

    /** Name of the SAP package, representing a logical grouping of related SAP data objects. */
    @Attribute
    String sapPackageName;

    /** Technical identifier for SAP data objects, used for integration and internal reference. */
    @Attribute
    String sapTechnicalName;

    /**
     * Builds the minimal object necessary to create a relationship to a SapErpComponent, from a potentially
     * more-complete SapErpComponent object.
     *
     * @return the minimal object necessary to relate to the SapErpComponent
     * @throws InvalidRequestException if any of the minimal set of required properties for a SapErpComponent relationship are not found in the initial object
     */
    @Override
    public SapErpComponent trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SapErpComponent assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SapErpComponent assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SapErpComponent assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SapErpComponent assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SapErpComponents will be included
     * @return a fluent search that includes all SapErpComponent assets
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
     * Reference to a SapErpComponent by GUID. Use this to create a relationship to this SapErpComponent,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SapErpComponent to reference
     * @return reference to a SapErpComponent that can be used for defining a relationship to a SapErpComponent
     */
    public static SapErpComponent refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SapErpComponent by GUID. Use this to create a relationship to this SapErpComponent,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SapErpComponent to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SapErpComponent that can be used for defining a relationship to a SapErpComponent
     */
    public static SapErpComponent refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SapErpComponent._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SapErpComponent by qualifiedName. Use this to create a relationship to this SapErpComponent,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SapErpComponent to reference
     * @return reference to a SapErpComponent that can be used for defining a relationship to a SapErpComponent
     */
    public static SapErpComponent refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SapErpComponent by qualifiedName. Use this to create a relationship to this SapErpComponent,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SapErpComponent to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SapErpComponent that can be used for defining a relationship to a SapErpComponent
     */
    public static SapErpComponent refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SapErpComponent._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SapErpComponent by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SapErpComponent to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SapErpComponent, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SapErpComponent does not exist or the provided GUID is not a SapErpComponent
     */
    @JsonIgnore
    public static SapErpComponent get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SapErpComponent by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SapErpComponent to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SapErpComponent, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SapErpComponent does not exist or the provided GUID is not a SapErpComponent
     */
    @JsonIgnore
    public static SapErpComponent get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SapErpComponent) {
                return (SapErpComponent) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof SapErpComponent) {
                return (SapErpComponent) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SapErpComponent by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SapErpComponent to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SapErpComponent, including any relationships
     * @return the requested SapErpComponent, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SapErpComponent does not exist or the provided GUID is not a SapErpComponent
     */
    @JsonIgnore
    public static SapErpComponent get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a SapErpComponent by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SapErpComponent to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SapErpComponent, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the SapErpComponent
     * @return the requested SapErpComponent, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SapErpComponent does not exist or the provided GUID is not a SapErpComponent
     */
    @JsonIgnore
    public static SapErpComponent get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = SapErpComponent.select(client)
                    .where(SapErpComponent.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof SapErpComponent) {
                return (SapErpComponent) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = SapErpComponent.select(client)
                    .where(SapErpComponent.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof SapErpComponent) {
                return (SapErpComponent) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SapErpComponent to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SapErpComponent
     * @return true if the SapErpComponent is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SapErpComponent.
     *
     * @param qualifiedName of the SapErpComponent
     * @param name of the SapErpComponent
     * @return the minimal request necessary to update the SapErpComponent, as a builder
     */
    public static SapErpComponentBuilder<?, ?> updater(String qualifiedName, String name) {
        return SapErpComponent._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SapErpComponent, from a potentially
     * more-complete SapErpComponent object.
     *
     * @return the minimal object necessary to update the SapErpComponent, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SapErpComponent are not found in the initial object
     */
    @Override
    public SapErpComponentBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a SapErpComponent.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SapErpComponent
     * @param name of the SapErpComponent
     * @return the updated SapErpComponent, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SapErpComponent removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SapErpComponent) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SapErpComponent.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SapErpComponent
     * @param name of the SapErpComponent
     * @return the updated SapErpComponent, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SapErpComponent removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SapErpComponent) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SapErpComponent.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SapErpComponent's owners
     * @param qualifiedName of the SapErpComponent
     * @param name of the SapErpComponent
     * @return the updated SapErpComponent, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SapErpComponent removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SapErpComponent) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SapErpComponent.
     *
     * @param client connectivity to the Atlan tenant on which to update the SapErpComponent's certificate
     * @param qualifiedName of the SapErpComponent
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SapErpComponent, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SapErpComponent updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SapErpComponent)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SapErpComponent.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SapErpComponent's certificate
     * @param qualifiedName of the SapErpComponent
     * @param name of the SapErpComponent
     * @return the updated SapErpComponent, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SapErpComponent removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SapErpComponent) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SapErpComponent.
     *
     * @param client connectivity to the Atlan tenant on which to update the SapErpComponent's announcement
     * @param qualifiedName of the SapErpComponent
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SapErpComponent updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SapErpComponent)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SapErpComponent.
     *
     * @param client connectivity to the Atlan client from which to remove the SapErpComponent's announcement
     * @param qualifiedName of the SapErpComponent
     * @param name of the SapErpComponent
     * @return the updated SapErpComponent, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SapErpComponent removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SapErpComponent) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SapErpComponent.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SapErpComponent's assigned terms
     * @param qualifiedName for the SapErpComponent
     * @param name human-readable name of the SapErpComponent
     * @param terms the list of terms to replace on the SapErpComponent, or null to remove all terms from the SapErpComponent
     * @return the SapErpComponent that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SapErpComponent replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SapErpComponent) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SapErpComponent, without replacing existing terms linked to the SapErpComponent.
     * Note: this operation must make two API calls — one to retrieve the SapErpComponent's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SapErpComponent
     * @param qualifiedName for the SapErpComponent
     * @param terms the list of terms to append to the SapErpComponent
     * @return the SapErpComponent that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SapErpComponent appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SapErpComponent) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SapErpComponent, without replacing all existing terms linked to the SapErpComponent.
     * Note: this operation must make two API calls — one to retrieve the SapErpComponent's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SapErpComponent
     * @param qualifiedName for the SapErpComponent
     * @param terms the list of terms to remove from the SapErpComponent, which must be referenced by GUID
     * @return the SapErpComponent that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SapErpComponent removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SapErpComponent) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SapErpComponent, without replacing existing Atlan tags linked to the SapErpComponent.
     * Note: this operation must make two API calls — one to retrieve the SapErpComponent's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SapErpComponent
     * @param qualifiedName of the SapErpComponent
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SapErpComponent
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static SapErpComponent appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SapErpComponent) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SapErpComponent, without replacing existing Atlan tags linked to the SapErpComponent.
     * Note: this operation must make two API calls — one to retrieve the SapErpComponent's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SapErpComponent
     * @param qualifiedName of the SapErpComponent
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SapErpComponent
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static SapErpComponent appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SapErpComponent) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SapErpComponent.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SapErpComponent
     * @param qualifiedName of the SapErpComponent
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SapErpComponent
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
