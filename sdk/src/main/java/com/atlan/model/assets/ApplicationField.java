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
 * Instances of ApplicationField in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class ApplicationField extends Asset implements IApplicationField, IApp, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ApplicationField";

    /** Fixed typeName for ApplicationFields. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Unique identifier for the application asset from the source system. */
    @Attribute
    String appId;

    /** Assets owned by the ApplicationField. */
    @Attribute
    @Singular
    SortedSet<IAsset> applicationFieldOwnedAssets;

    /** Application owning the ApplicationField. */
    @Attribute
    IApplication applicationParent;

    /** Unique name of the parent Application asset that contains this ApplicationField asset. */
    @Attribute
    String applicationParentQualifiedName;

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
     * Builds the minimal object necessary to create a relationship to a ApplicationField, from a potentially
     * more-complete ApplicationField object.
     *
     * @return the minimal object necessary to relate to the ApplicationField
     * @throws InvalidRequestException if any of the minimal set of required properties for a ApplicationField relationship are not found in the initial object
     */
    @Override
    public ApplicationField trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all ApplicationField assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ApplicationField assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all ApplicationField assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all ApplicationField assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ApplicationFields will be included
     * @return a fluent search that includes all ApplicationField assets
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
     * Reference to a ApplicationField by GUID. Use this to create a relationship to this ApplicationField,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the ApplicationField to reference
     * @return reference to a ApplicationField that can be used for defining a relationship to a ApplicationField
     */
    public static ApplicationField refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ApplicationField by GUID. Use this to create a relationship to this ApplicationField,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the ApplicationField to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ApplicationField that can be used for defining a relationship to a ApplicationField
     */
    public static ApplicationField refByGuid(String guid, Reference.SaveSemantic semantic) {
        return ApplicationField._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a ApplicationField by qualifiedName. Use this to create a relationship to this ApplicationField,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the ApplicationField to reference
     * @return reference to a ApplicationField that can be used for defining a relationship to a ApplicationField
     */
    public static ApplicationField refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ApplicationField by qualifiedName. Use this to create a relationship to this ApplicationField,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the ApplicationField to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ApplicationField that can be used for defining a relationship to a ApplicationField
     */
    public static ApplicationField refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return ApplicationField._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a ApplicationField by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ApplicationField to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ApplicationField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ApplicationField does not exist or the provided GUID is not a ApplicationField
     */
    @JsonIgnore
    public static ApplicationField get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a ApplicationField by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ApplicationField to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full ApplicationField, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ApplicationField does not exist or the provided GUID is not a ApplicationField
     */
    @JsonIgnore
    public static ApplicationField get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof ApplicationField) {
                return (ApplicationField) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof ApplicationField) {
                return (ApplicationField) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a ApplicationField by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ApplicationField to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the ApplicationField, including any relationships
     * @return the requested ApplicationField, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ApplicationField does not exist or the provided GUID is not a ApplicationField
     */
    @JsonIgnore
    public static ApplicationField get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a ApplicationField by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ApplicationField to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the ApplicationField, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the ApplicationField
     * @return the requested ApplicationField, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ApplicationField does not exist or the provided GUID is not a ApplicationField
     */
    @JsonIgnore
    public static ApplicationField get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = ApplicationField.select(client)
                    .where(ApplicationField.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof ApplicationField) {
                return (ApplicationField) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = ApplicationField.select(client)
                    .where(ApplicationField.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof ApplicationField) {
                return (ApplicationField) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) ApplicationField to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the ApplicationField
     * @return true if the ApplicationField is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ApplicationField.
     *
     * @param name of the field
     * @param application in which the field should be created, which must have at least
     *                 a qualifiedName
     * @return the minimal request necessary to create the field, as a builder
     * @throws InvalidRequestException if the application provided is without a qualifiedName
     */
    public static ApplicationField.ApplicationFieldBuilder<?, ?> creator(String name, Application application)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("applicationQualifiedName", application.getQualifiedName());
        map.put("connectionQualifiedName", application.getConnectionQualifiedName());
        validateRelationship(Application.TYPE_NAME, map);
        return creator(name, application.getConnectionQualifiedName(), application.getQualifiedName())
                .application(application.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a ApplicationField.
     *
     * @param name of the field
     * @param applicationQualifiedName unique name of the application in which this field exists
     * @return the minimal request necessary to create the field, as a builder
     */
    public static ApplicationField.ApplicationFieldBuilder<?, ?> creator(String name, String applicationQualifiedName) {
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(applicationQualifiedName);
        return creator(name, connectionQualifiedName, applicationQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ApplicationField.
     *
     * @param name of the field
     * @param connectionQualifiedName unique name of the connection in which to create the field
     * @param applicationQualifiedName unique name of the application in which to create the field
     * @return the minimal request necessary to create the field, as a builder
     */
    public static ApplicationField.ApplicationFieldBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String applicationQualifiedName) {
        return ApplicationField._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, applicationQualifiedName))
                .connectionQualifiedName(connectionQualifiedName)
                .applicationParentQualifiedName(applicationQualifiedName)
                .applicationParent(Application.refByQualifiedName(applicationQualifiedName));
    }

    /**
     * Generate a unique field name.
     *
     * @param name of the field
     * @param applicationQualifiedName unique name of the application in which this field exists
     * @return a unique name for the field
     */
    public static String generateQualifiedName(String name, String applicationQualifiedName) {
        return applicationQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a ApplicationField.
     *
     * @param qualifiedName of the ApplicationField
     * @param name of the ApplicationField
     * @return the minimal request necessary to update the ApplicationField, as a builder
     */
    public static ApplicationFieldBuilder<?, ?> updater(String qualifiedName, String name) {
        return ApplicationField._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ApplicationField, from a potentially
     * more-complete ApplicationField object.
     *
     * @return the minimal object necessary to update the ApplicationField, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ApplicationField are not found in the initial object
     */
    @Override
    public ApplicationFieldBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ApplicationFieldBuilder<
                    C extends ApplicationField, B extends ApplicationFieldBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a ApplicationField.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ApplicationField
     * @param name of the ApplicationField
     * @return the updated ApplicationField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ApplicationField removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ApplicationField) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ApplicationField.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ApplicationField
     * @param name of the ApplicationField
     * @return the updated ApplicationField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ApplicationField removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ApplicationField) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ApplicationField.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ApplicationField's owners
     * @param qualifiedName of the ApplicationField
     * @param name of the ApplicationField
     * @return the updated ApplicationField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ApplicationField removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ApplicationField) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ApplicationField.
     *
     * @param client connectivity to the Atlan tenant on which to update the ApplicationField's certificate
     * @param qualifiedName of the ApplicationField
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ApplicationField, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ApplicationField updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (ApplicationField)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ApplicationField.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ApplicationField's certificate
     * @param qualifiedName of the ApplicationField
     * @param name of the ApplicationField
     * @return the updated ApplicationField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ApplicationField removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ApplicationField) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ApplicationField.
     *
     * @param client connectivity to the Atlan tenant on which to update the ApplicationField's announcement
     * @param qualifiedName of the ApplicationField
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ApplicationField updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (ApplicationField)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ApplicationField.
     *
     * @param client connectivity to the Atlan client from which to remove the ApplicationField's announcement
     * @param qualifiedName of the ApplicationField
     * @param name of the ApplicationField
     * @return the updated ApplicationField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ApplicationField removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ApplicationField) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the ApplicationField.
     *
     * @param client connectivity to the Atlan tenant on which to replace the ApplicationField's assigned terms
     * @param qualifiedName for the ApplicationField
     * @param name human-readable name of the ApplicationField
     * @param terms the list of terms to replace on the ApplicationField, or null to remove all terms from the ApplicationField
     * @return the ApplicationField that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ApplicationField replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (ApplicationField) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ApplicationField, without replacing existing terms linked to the ApplicationField.
     * Note: this operation must make two API calls — one to retrieve the ApplicationField's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the ApplicationField
     * @param qualifiedName for the ApplicationField
     * @param terms the list of terms to append to the ApplicationField
     * @return the ApplicationField that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static ApplicationField appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ApplicationField) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ApplicationField, without replacing all existing terms linked to the ApplicationField.
     * Note: this operation must make two API calls — one to retrieve the ApplicationField's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the ApplicationField
     * @param qualifiedName for the ApplicationField
     * @param terms the list of terms to remove from the ApplicationField, which must be referenced by GUID
     * @return the ApplicationField that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static ApplicationField removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ApplicationField) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a ApplicationField, without replacing existing Atlan tags linked to the ApplicationField.
     * Note: this operation must make two API calls — one to retrieve the ApplicationField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ApplicationField
     * @param qualifiedName of the ApplicationField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ApplicationField
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static ApplicationField appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (ApplicationField) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ApplicationField, without replacing existing Atlan tags linked to the ApplicationField.
     * Note: this operation must make two API calls — one to retrieve the ApplicationField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ApplicationField
     * @param qualifiedName of the ApplicationField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ApplicationField
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static ApplicationField appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (ApplicationField) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a ApplicationField.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a ApplicationField
     * @param qualifiedName of the ApplicationField
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ApplicationField
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
