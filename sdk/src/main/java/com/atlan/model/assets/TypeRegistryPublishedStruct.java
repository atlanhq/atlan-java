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
 * Published complex nested attribute structure.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class TypeRegistryPublishedStruct extends Asset
        implements ITypeRegistryPublishedStruct,
                ITypeRegistryStruct,
                ITypeRegistryPublished,
                ITypeRegistryExtended,
                ITypeRegistry,
                IAsset,
                IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TypeRegistryPublishedStruct";

    /** Fixed typeName for TypeRegistryPublishedStructs. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Attributes constrained by this extended definition. */
    @Attribute
    @Singular
    SortedSet<ITypeRegistryAttribute> typeRegistryAttributes;

    /** Date and time at which this portion of the metamodel was deprecated. */
    @Attribute
    @Date
    Long typeRegistryDeprecatedAt;

    /** User who deprecated this portion of the metamodel. */
    @Attribute
    String typeRegistryDeprecatedBy;

    /** Whether this portion of the metamodel should no longer be used. */
    @Attribute
    Boolean typeRegistryIsDeprecated;

    /** Namespace in which the struct's definition is contained. */
    @Attribute
    ITypeRegistryNamespace typeRegistryNamespace;

    /** Metamodel object that replaces this one. */
    @Attribute
    ITypeRegistry typeRegistryReplacedBy;

    /** Metamodel object(s) that are replaced by this one. */
    @Attribute
    @Singular
    SortedSet<ITypeRegistry> typeRegistryReplaces;

    /** Version of this published portion of the metamodel. */
    @Attribute
    String typeRegistryVersion;

    /**
     * Builds the minimal object necessary to create a relationship to a TypeRegistryPublishedStruct, from a potentially
     * more-complete TypeRegistryPublishedStruct object.
     *
     * @return the minimal object necessary to relate to the TypeRegistryPublishedStruct
     * @throws InvalidRequestException if any of the minimal set of required properties for a TypeRegistryPublishedStruct relationship are not found in the initial object
     */
    @Override
    public TypeRegistryPublishedStruct trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all TypeRegistryPublishedStruct assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) TypeRegistryPublishedStruct assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all TypeRegistryPublishedStruct assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all TypeRegistryPublishedStruct assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) TypeRegistryPublishedStructs will be included
     * @return a fluent search that includes all TypeRegistryPublishedStruct assets
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
     * Reference to a TypeRegistryPublishedStruct by GUID. Use this to create a relationship to this TypeRegistryPublishedStruct,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the TypeRegistryPublishedStruct to reference
     * @return reference to a TypeRegistryPublishedStruct that can be used for defining a relationship to a TypeRegistryPublishedStruct
     */
    public static TypeRegistryPublishedStruct refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a TypeRegistryPublishedStruct by GUID. Use this to create a relationship to this TypeRegistryPublishedStruct,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the TypeRegistryPublishedStruct to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a TypeRegistryPublishedStruct that can be used for defining a relationship to a TypeRegistryPublishedStruct
     */
    public static TypeRegistryPublishedStruct refByGuid(String guid, Reference.SaveSemantic semantic) {
        return TypeRegistryPublishedStruct._internal()
                .guid(guid)
                .semantic(semantic)
                .build();
    }

    /**
     * Reference to a TypeRegistryPublishedStruct by qualifiedName. Use this to create a relationship to this TypeRegistryPublishedStruct,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the TypeRegistryPublishedStruct to reference
     * @return reference to a TypeRegistryPublishedStruct that can be used for defining a relationship to a TypeRegistryPublishedStruct
     */
    public static TypeRegistryPublishedStruct refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a TypeRegistryPublishedStruct by qualifiedName. Use this to create a relationship to this TypeRegistryPublishedStruct,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the TypeRegistryPublishedStruct to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a TypeRegistryPublishedStruct that can be used for defining a relationship to a TypeRegistryPublishedStruct
     */
    public static TypeRegistryPublishedStruct refByQualifiedName(
            String qualifiedName, Reference.SaveSemantic semantic) {
        return TypeRegistryPublishedStruct._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a TypeRegistryPublishedStruct by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the TypeRegistryPublishedStruct to retrieve, either its GUID or its full qualifiedName
     * @return the requested full TypeRegistryPublishedStruct, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TypeRegistryPublishedStruct does not exist or the provided GUID is not a TypeRegistryPublishedStruct
     */
    @JsonIgnore
    public static TypeRegistryPublishedStruct get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a TypeRegistryPublishedStruct by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the TypeRegistryPublishedStruct to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full TypeRegistryPublishedStruct, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TypeRegistryPublishedStruct does not exist or the provided GUID is not a TypeRegistryPublishedStruct
     */
    @JsonIgnore
    public static TypeRegistryPublishedStruct get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof TypeRegistryPublishedStruct) {
                return (TypeRegistryPublishedStruct) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof TypeRegistryPublishedStruct) {
                return (TypeRegistryPublishedStruct) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a TypeRegistryPublishedStruct by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the TypeRegistryPublishedStruct to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the TypeRegistryPublishedStruct, including any relationships
     * @return the requested TypeRegistryPublishedStruct, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TypeRegistryPublishedStruct does not exist or the provided GUID is not a TypeRegistryPublishedStruct
     */
    @JsonIgnore
    public static TypeRegistryPublishedStruct get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a TypeRegistryPublishedStruct by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the TypeRegistryPublishedStruct to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the TypeRegistryPublishedStruct, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the TypeRegistryPublishedStruct
     * @return the requested TypeRegistryPublishedStruct, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TypeRegistryPublishedStruct does not exist or the provided GUID is not a TypeRegistryPublishedStruct
     */
    @JsonIgnore
    public static TypeRegistryPublishedStruct get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = TypeRegistryPublishedStruct.select(client)
                    .where(TypeRegistryPublishedStruct.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof TypeRegistryPublishedStruct) {
                return (TypeRegistryPublishedStruct) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = TypeRegistryPublishedStruct.select(client)
                    .where(TypeRegistryPublishedStruct.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof TypeRegistryPublishedStruct) {
                return (TypeRegistryPublishedStruct) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) TypeRegistryPublishedStruct to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the TypeRegistryPublishedStruct
     * @return true if the TypeRegistryPublishedStruct is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a TypeRegistryPublishedStruct.
     *
     * @param qualifiedName of the TypeRegistryPublishedStruct
     * @param name of the TypeRegistryPublishedStruct
     * @return the minimal request necessary to update the TypeRegistryPublishedStruct, as a builder
     */
    public static TypeRegistryPublishedStructBuilder<?, ?> updater(String qualifiedName, String name) {
        return TypeRegistryPublishedStruct._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TypeRegistryPublishedStruct, from a potentially
     * more-complete TypeRegistryPublishedStruct object.
     *
     * @return the minimal object necessary to update the TypeRegistryPublishedStruct, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TypeRegistryPublishedStruct are not found in the initial object
     */
    @Override
    public TypeRegistryPublishedStructBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class TypeRegistryPublishedStructBuilder<
                    C extends TypeRegistryPublishedStruct, B extends TypeRegistryPublishedStructBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a TypeRegistryPublishedStruct.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the TypeRegistryPublishedStruct
     * @param name of the TypeRegistryPublishedStruct
     * @return the updated TypeRegistryPublishedStruct, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TypeRegistryPublishedStruct removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TypeRegistryPublishedStruct) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a TypeRegistryPublishedStruct.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the TypeRegistryPublishedStruct
     * @param name of the TypeRegistryPublishedStruct
     * @return the updated TypeRegistryPublishedStruct, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TypeRegistryPublishedStruct removeUserDescription(
            AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (TypeRegistryPublishedStruct) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a TypeRegistryPublishedStruct.
     *
     * @param client connectivity to the Atlan tenant from which to remove the TypeRegistryPublishedStruct's owners
     * @param qualifiedName of the TypeRegistryPublishedStruct
     * @param name of the TypeRegistryPublishedStruct
     * @return the updated TypeRegistryPublishedStruct, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TypeRegistryPublishedStruct removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TypeRegistryPublishedStruct) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a TypeRegistryPublishedStruct.
     *
     * @param client connectivity to the Atlan tenant on which to update the TypeRegistryPublishedStruct's certificate
     * @param qualifiedName of the TypeRegistryPublishedStruct
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TypeRegistryPublishedStruct, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TypeRegistryPublishedStruct updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (TypeRegistryPublishedStruct)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a TypeRegistryPublishedStruct.
     *
     * @param client connectivity to the Atlan tenant from which to remove the TypeRegistryPublishedStruct's certificate
     * @param qualifiedName of the TypeRegistryPublishedStruct
     * @param name of the TypeRegistryPublishedStruct
     * @return the updated TypeRegistryPublishedStruct, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TypeRegistryPublishedStruct removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TypeRegistryPublishedStruct) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a TypeRegistryPublishedStruct.
     *
     * @param client connectivity to the Atlan tenant on which to update the TypeRegistryPublishedStruct's announcement
     * @param qualifiedName of the TypeRegistryPublishedStruct
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TypeRegistryPublishedStruct updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (TypeRegistryPublishedStruct)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a TypeRegistryPublishedStruct.
     *
     * @param client connectivity to the Atlan client from which to remove the TypeRegistryPublishedStruct's announcement
     * @param qualifiedName of the TypeRegistryPublishedStruct
     * @param name of the TypeRegistryPublishedStruct
     * @return the updated TypeRegistryPublishedStruct, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TypeRegistryPublishedStruct removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TypeRegistryPublishedStruct) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the TypeRegistryPublishedStruct.
     *
     * @param client connectivity to the Atlan tenant on which to replace the TypeRegistryPublishedStruct's assigned terms
     * @param qualifiedName for the TypeRegistryPublishedStruct
     * @param name human-readable name of the TypeRegistryPublishedStruct
     * @param terms the list of terms to replace on the TypeRegistryPublishedStruct, or null to remove all terms from the TypeRegistryPublishedStruct
     * @return the TypeRegistryPublishedStruct that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TypeRegistryPublishedStruct replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (TypeRegistryPublishedStruct) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the TypeRegistryPublishedStruct, without replacing existing terms linked to the TypeRegistryPublishedStruct.
     * Note: this operation must make two API calls — one to retrieve the TypeRegistryPublishedStruct's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the TypeRegistryPublishedStruct
     * @param qualifiedName for the TypeRegistryPublishedStruct
     * @param terms the list of terms to append to the TypeRegistryPublishedStruct
     * @return the TypeRegistryPublishedStruct that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static TypeRegistryPublishedStruct appendTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (TypeRegistryPublishedStruct) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a TypeRegistryPublishedStruct, without replacing all existing terms linked to the TypeRegistryPublishedStruct.
     * Note: this operation must make two API calls — one to retrieve the TypeRegistryPublishedStruct's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the TypeRegistryPublishedStruct
     * @param qualifiedName for the TypeRegistryPublishedStruct
     * @param terms the list of terms to remove from the TypeRegistryPublishedStruct, which must be referenced by GUID
     * @return the TypeRegistryPublishedStruct that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static TypeRegistryPublishedStruct removeTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (TypeRegistryPublishedStruct) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a TypeRegistryPublishedStruct, without replacing existing Atlan tags linked to the TypeRegistryPublishedStruct.
     * Note: this operation must make two API calls — one to retrieve the TypeRegistryPublishedStruct's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the TypeRegistryPublishedStruct
     * @param qualifiedName of the TypeRegistryPublishedStruct
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated TypeRegistryPublishedStruct
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static TypeRegistryPublishedStruct appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (TypeRegistryPublishedStruct) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TypeRegistryPublishedStruct, without replacing existing Atlan tags linked to the TypeRegistryPublishedStruct.
     * Note: this operation must make two API calls — one to retrieve the TypeRegistryPublishedStruct's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the TypeRegistryPublishedStruct
     * @param qualifiedName of the TypeRegistryPublishedStruct
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated TypeRegistryPublishedStruct
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static TypeRegistryPublishedStruct appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (TypeRegistryPublishedStruct) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a TypeRegistryPublishedStruct.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a TypeRegistryPublishedStruct
     * @param qualifiedName of the TypeRegistryPublishedStruct
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the TypeRegistryPublishedStruct
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
