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
 * Instance of a Sisense widget in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class SisenseWidget extends Asset implements ISisenseWidget, ISisense, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SisenseWidget";

    /** Fixed typeName for SisenseWidgets. */
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

    /** Dashboard in which this widget exists. */
    @Attribute
    ISisenseDashboard sisenseDashboard;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISisenseDatamodelTable> sisenseDatamodelTables;

    /** Folder in which this widget exists. */
    @Attribute
    ISisenseFolder sisenseFolder;

    /** Number of columns used in this widget. */
    @Attribute
    Long sisenseWidgetColumnCount;

    /** Unique name of the dashboard in which this widget exists. */
    @Attribute
    String sisenseWidgetDashboardQualifiedName;

    /** Unique name of the folder in which this widget exists. */
    @Attribute
    String sisenseWidgetFolderQualifiedName;

    /** Size of this widget. */
    @Attribute
    String sisenseWidgetSize;

    /** Subtype of this widget. */
    @Attribute
    String sisenseWidgetSubType;

    /**
     * Builds the minimal object necessary to create a relationship to a SisenseWidget, from a potentially
     * more-complete SisenseWidget object.
     *
     * @return the minimal object necessary to relate to the SisenseWidget
     * @throws InvalidRequestException if any of the minimal set of required properties for a SisenseWidget relationship are not found in the initial object
     */
    @Override
    public SisenseWidget trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SisenseWidget assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SisenseWidget assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SisenseWidget assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SisenseWidget assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SisenseWidgets will be included
     * @return a fluent search that includes all SisenseWidget assets
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
     * Reference to a SisenseWidget by GUID. Use this to create a relationship to this SisenseWidget,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SisenseWidget to reference
     * @return reference to a SisenseWidget that can be used for defining a relationship to a SisenseWidget
     */
    public static SisenseWidget refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SisenseWidget by GUID. Use this to create a relationship to this SisenseWidget,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SisenseWidget to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SisenseWidget that can be used for defining a relationship to a SisenseWidget
     */
    public static SisenseWidget refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SisenseWidget._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SisenseWidget by qualifiedName. Use this to create a relationship to this SisenseWidget,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SisenseWidget to reference
     * @return reference to a SisenseWidget that can be used for defining a relationship to a SisenseWidget
     */
    public static SisenseWidget refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SisenseWidget by qualifiedName. Use this to create a relationship to this SisenseWidget,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SisenseWidget to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SisenseWidget that can be used for defining a relationship to a SisenseWidget
     */
    public static SisenseWidget refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SisenseWidget._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SisenseWidget by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SisenseWidget to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SisenseWidget, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SisenseWidget does not exist or the provided GUID is not a SisenseWidget
     */
    @JsonIgnore
    public static SisenseWidget get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SisenseWidget by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SisenseWidget to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SisenseWidget, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SisenseWidget does not exist or the provided GUID is not a SisenseWidget
     */
    @JsonIgnore
    public static SisenseWidget get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SisenseWidget) {
                return (SisenseWidget) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof SisenseWidget) {
                return (SisenseWidget) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SisenseWidget by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SisenseWidget to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SisenseWidget, including any relationships
     * @return the requested SisenseWidget, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SisenseWidget does not exist or the provided GUID is not a SisenseWidget
     */
    @JsonIgnore
    public static SisenseWidget get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a SisenseWidget by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SisenseWidget to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SisenseWidget, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the SisenseWidget
     * @return the requested SisenseWidget, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SisenseWidget does not exist or the provided GUID is not a SisenseWidget
     */
    @JsonIgnore
    public static SisenseWidget get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = SisenseWidget.select(client)
                    .where(SisenseWidget.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof SisenseWidget) {
                return (SisenseWidget) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = SisenseWidget.select(client)
                    .where(SisenseWidget.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof SisenseWidget) {
                return (SisenseWidget) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SisenseWidget to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SisenseWidget
     * @return true if the SisenseWidget is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SisenseWidget.
     *
     * @param qualifiedName of the SisenseWidget
     * @param name of the SisenseWidget
     * @return the minimal request necessary to update the SisenseWidget, as a builder
     */
    public static SisenseWidgetBuilder<?, ?> updater(String qualifiedName, String name) {
        return SisenseWidget._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SisenseWidget, from a potentially
     * more-complete SisenseWidget object.
     *
     * @return the minimal object necessary to update the SisenseWidget, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SisenseWidget are not found in the initial object
     */
    @Override
    public SisenseWidgetBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class SisenseWidgetBuilder<C extends SisenseWidget, B extends SisenseWidgetBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a SisenseWidget.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SisenseWidget
     * @param name of the SisenseWidget
     * @return the updated SisenseWidget, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SisenseWidget removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SisenseWidget) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SisenseWidget.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SisenseWidget
     * @param name of the SisenseWidget
     * @return the updated SisenseWidget, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SisenseWidget removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SisenseWidget) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SisenseWidget.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SisenseWidget's owners
     * @param qualifiedName of the SisenseWidget
     * @param name of the SisenseWidget
     * @return the updated SisenseWidget, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SisenseWidget removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SisenseWidget) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SisenseWidget.
     *
     * @param client connectivity to the Atlan tenant on which to update the SisenseWidget's certificate
     * @param qualifiedName of the SisenseWidget
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SisenseWidget, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SisenseWidget updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SisenseWidget)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SisenseWidget.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SisenseWidget's certificate
     * @param qualifiedName of the SisenseWidget
     * @param name of the SisenseWidget
     * @return the updated SisenseWidget, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SisenseWidget removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SisenseWidget) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SisenseWidget.
     *
     * @param client connectivity to the Atlan tenant on which to update the SisenseWidget's announcement
     * @param qualifiedName of the SisenseWidget
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SisenseWidget updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SisenseWidget)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SisenseWidget.
     *
     * @param client connectivity to the Atlan client from which to remove the SisenseWidget's announcement
     * @param qualifiedName of the SisenseWidget
     * @param name of the SisenseWidget
     * @return the updated SisenseWidget, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SisenseWidget removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SisenseWidget) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SisenseWidget.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SisenseWidget's assigned terms
     * @param qualifiedName for the SisenseWidget
     * @param name human-readable name of the SisenseWidget
     * @param terms the list of terms to replace on the SisenseWidget, or null to remove all terms from the SisenseWidget
     * @return the SisenseWidget that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SisenseWidget replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SisenseWidget) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SisenseWidget, without replacing existing terms linked to the SisenseWidget.
     * Note: this operation must make two API calls — one to retrieve the SisenseWidget's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SisenseWidget
     * @param qualifiedName for the SisenseWidget
     * @param terms the list of terms to append to the SisenseWidget
     * @return the SisenseWidget that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SisenseWidget appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SisenseWidget) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SisenseWidget, without replacing all existing terms linked to the SisenseWidget.
     * Note: this operation must make two API calls — one to retrieve the SisenseWidget's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SisenseWidget
     * @param qualifiedName for the SisenseWidget
     * @param terms the list of terms to remove from the SisenseWidget, which must be referenced by GUID
     * @return the SisenseWidget that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SisenseWidget removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SisenseWidget) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SisenseWidget, without replacing existing Atlan tags linked to the SisenseWidget.
     * Note: this operation must make two API calls — one to retrieve the SisenseWidget's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SisenseWidget
     * @param qualifiedName of the SisenseWidget
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SisenseWidget
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static SisenseWidget appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SisenseWidget) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SisenseWidget, without replacing existing Atlan tags linked to the SisenseWidget.
     * Note: this operation must make two API calls — one to retrieve the SisenseWidget's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SisenseWidget
     * @param qualifiedName of the SisenseWidget
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SisenseWidget
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static SisenseWidget appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SisenseWidget) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SisenseWidget.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SisenseWidget
     * @param qualifiedName of the SisenseWidget
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SisenseWidget
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
