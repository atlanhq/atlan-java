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
import com.atlan.model.enums.TypeRegistryStatus;
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
 * Definition of a custom asset type in the metamodel.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class TypeRegistryEntity extends Asset implements ITypeRegistryEntity, ITypeRegistry, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TypeRegistryEntity";

    /** Fixed typeName for TypeRegistryEntitys. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Attributes contained within the entity. */
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

    /** Explanatory definition of this portion of the metamodel. */
    @Attribute
    String typeRegistryDescription;

    /** Whether this portion of the metamodel is deprecated (marked for removal). */
    @Attribute
    Boolean typeRegistryIsDeprecated;

    /** Unique name for this portion of the metamodel. */
    @Attribute
    String typeRegistryName;

    /** Namespace in which the asset type's definition is contained. */
    @Attribute
    ITypeRegistryNamespace typeRegistryNamespace;

    /** Metamodel object that replaces this one. */
    @Attribute
    ITypeRegistry typeRegistryReplacedBy;

    /** Metamodel object(s) that are replaced by this one. */
    @Attribute
    @Singular
    SortedSet<ITypeRegistry> typeRegistryReplaces;

    /** Status of this portion of the metamodel. */
    @Attribute
    TypeRegistryStatus typeRegistryStatus;

    /** Subtype(s) that extend from this asset type (which will inherit all its attributes and relationships). */
    @Attribute
    @Singular
    SortedSet<ITypeRegistryEntity> typeRegistrySubTypes;

    /** Supertype(s) from which this asset type extends (and inherits all attributes and relationships). */
    @Attribute
    @Singular
    SortedSet<ITypeRegistryEntity> typeRegistrySuperTypes;

    /** Version of this asset type's definition. */
    @Attribute
    String typeRegistryVersion;

    /**
     * Builds the minimal object necessary to create a relationship to a TypeRegistryEntity, from a potentially
     * more-complete TypeRegistryEntity object.
     *
     * @return the minimal object necessary to relate to the TypeRegistryEntity
     * @throws InvalidRequestException if any of the minimal set of required properties for a TypeRegistryEntity relationship are not found in the initial object
     */
    @Override
    public TypeRegistryEntity trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all TypeRegistryEntity assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) TypeRegistryEntity assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all TypeRegistryEntity assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all TypeRegistryEntity assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) TypeRegistryEntitys will be included
     * @return a fluent search that includes all TypeRegistryEntity assets
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
     * Reference to a TypeRegistryEntity by GUID. Use this to create a relationship to this TypeRegistryEntity,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the TypeRegistryEntity to reference
     * @return reference to a TypeRegistryEntity that can be used for defining a relationship to a TypeRegistryEntity
     */
    public static TypeRegistryEntity refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a TypeRegistryEntity by GUID. Use this to create a relationship to this TypeRegistryEntity,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the TypeRegistryEntity to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a TypeRegistryEntity that can be used for defining a relationship to a TypeRegistryEntity
     */
    public static TypeRegistryEntity refByGuid(String guid, Reference.SaveSemantic semantic) {
        return TypeRegistryEntity._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a TypeRegistryEntity by qualifiedName. Use this to create a relationship to this TypeRegistryEntity,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the TypeRegistryEntity to reference
     * @return reference to a TypeRegistryEntity that can be used for defining a relationship to a TypeRegistryEntity
     */
    public static TypeRegistryEntity refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a TypeRegistryEntity by qualifiedName. Use this to create a relationship to this TypeRegistryEntity,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the TypeRegistryEntity to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a TypeRegistryEntity that can be used for defining a relationship to a TypeRegistryEntity
     */
    public static TypeRegistryEntity refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return TypeRegistryEntity._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a TypeRegistryEntity by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the TypeRegistryEntity to retrieve, either its GUID or its full qualifiedName
     * @return the requested full TypeRegistryEntity, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TypeRegistryEntity does not exist or the provided GUID is not a TypeRegistryEntity
     */
    @JsonIgnore
    public static TypeRegistryEntity get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a TypeRegistryEntity by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the TypeRegistryEntity to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full TypeRegistryEntity, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TypeRegistryEntity does not exist or the provided GUID is not a TypeRegistryEntity
     */
    @JsonIgnore
    public static TypeRegistryEntity get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof TypeRegistryEntity) {
                return (TypeRegistryEntity) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof TypeRegistryEntity) {
                return (TypeRegistryEntity) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a TypeRegistryEntity by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the TypeRegistryEntity to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the TypeRegistryEntity, including any relationships
     * @return the requested TypeRegistryEntity, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TypeRegistryEntity does not exist or the provided GUID is not a TypeRegistryEntity
     */
    @JsonIgnore
    public static TypeRegistryEntity get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a TypeRegistryEntity by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the TypeRegistryEntity to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the TypeRegistryEntity, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the TypeRegistryEntity
     * @return the requested TypeRegistryEntity, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the TypeRegistryEntity does not exist or the provided GUID is not a TypeRegistryEntity
     */
    @JsonIgnore
    public static TypeRegistryEntity get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = TypeRegistryEntity.select(client)
                    .where(TypeRegistryEntity.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof TypeRegistryEntity) {
                return (TypeRegistryEntity) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = TypeRegistryEntity.select(client)
                    .where(TypeRegistryEntity.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof TypeRegistryEntity) {
                return (TypeRegistryEntity) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) TypeRegistryEntity to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the TypeRegistryEntity
     * @return true if the TypeRegistryEntity is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a TypeRegistryEntity.
     *
     * @param client connectivity to the Atlan tenant on which the TypeRegistryEntity is intended to be created
     * @param name of the TypeRegistryEntity
     * @param namespace in which the TypeRegistryEntity should be registered
     * @return the minimal request necessary to create the TypeRegistryEntity, as a builder
     */
    public static TypeRegistryEntityBuilder<?, ?> creator(AtlanClient client, String name, String namespace) {
        return TypeRegistryEntity._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, namespace))
                .name(name)
                .typeRegistryNamespace(TypeRegistryNamespace.refByQualifiedName(
                        TypeRegistryNamespace.generateQualifiedName(namespace)))
                // .typeRegistrySuperType("Catalog") // TODO: make this configurable
                .typeRegistryStatus(TypeRegistryStatus.DRAFT);
    }

    /**
     * Generate a unique name for this TypeRegistryEntity.
     *
     * @param name human-readable name for the TypeRegistryEntity
     * @param namespace in which to the TypeRegistryEntity should be registered
     * @return the unique qualifiedName of the TypeRegistryEntity
     */
    public static String generateQualifiedName(String name, String namespace) {
        return "TypeRegistry/" + namespace + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a TypeRegistryEntity.
     *
     * @param qualifiedName of the TypeRegistryEntity
     * @param name of the TypeRegistryEntity
     * @return the minimal request necessary to update the TypeRegistryEntity, as a builder
     */
    public static TypeRegistryEntityBuilder<?, ?> updater(String qualifiedName, String name) {
        return TypeRegistryEntity._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TypeRegistryEntity, from a potentially
     * more-complete TypeRegistryEntity object.
     *
     * @return the minimal object necessary to update the TypeRegistryEntity, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TypeRegistryEntity are not found in the initial object
     */
    @Override
    public TypeRegistryEntityBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class TypeRegistryEntityBuilder<
                    C extends TypeRegistryEntity, B extends TypeRegistryEntityBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a TypeRegistryEntity.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the TypeRegistryEntity
     * @param name of the TypeRegistryEntity
     * @return the updated TypeRegistryEntity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TypeRegistryEntity removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TypeRegistryEntity) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a TypeRegistryEntity.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the TypeRegistryEntity
     * @param name of the TypeRegistryEntity
     * @return the updated TypeRegistryEntity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TypeRegistryEntity removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TypeRegistryEntity) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a TypeRegistryEntity.
     *
     * @param client connectivity to the Atlan tenant from which to remove the TypeRegistryEntity's owners
     * @param qualifiedName of the TypeRegistryEntity
     * @param name of the TypeRegistryEntity
     * @return the updated TypeRegistryEntity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TypeRegistryEntity removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TypeRegistryEntity) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a TypeRegistryEntity.
     *
     * @param client connectivity to the Atlan tenant on which to update the TypeRegistryEntity's certificate
     * @param qualifiedName of the TypeRegistryEntity
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated TypeRegistryEntity, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TypeRegistryEntity updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (TypeRegistryEntity)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a TypeRegistryEntity.
     *
     * @param client connectivity to the Atlan tenant from which to remove the TypeRegistryEntity's certificate
     * @param qualifiedName of the TypeRegistryEntity
     * @param name of the TypeRegistryEntity
     * @return the updated TypeRegistryEntity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TypeRegistryEntity removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TypeRegistryEntity) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a TypeRegistryEntity.
     *
     * @param client connectivity to the Atlan tenant on which to update the TypeRegistryEntity's announcement
     * @param qualifiedName of the TypeRegistryEntity
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static TypeRegistryEntity updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (TypeRegistryEntity)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a TypeRegistryEntity.
     *
     * @param client connectivity to the Atlan client from which to remove the TypeRegistryEntity's announcement
     * @param qualifiedName of the TypeRegistryEntity
     * @param name of the TypeRegistryEntity
     * @return the updated TypeRegistryEntity, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static TypeRegistryEntity removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (TypeRegistryEntity) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the TypeRegistryEntity.
     *
     * @param client connectivity to the Atlan tenant on which to replace the TypeRegistryEntity's assigned terms
     * @param qualifiedName for the TypeRegistryEntity
     * @param name human-readable name of the TypeRegistryEntity
     * @param terms the list of terms to replace on the TypeRegistryEntity, or null to remove all terms from the TypeRegistryEntity
     * @return the TypeRegistryEntity that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static TypeRegistryEntity replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (TypeRegistryEntity) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the TypeRegistryEntity, without replacing existing terms linked to the TypeRegistryEntity.
     * Note: this operation must make two API calls — one to retrieve the TypeRegistryEntity's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the TypeRegistryEntity
     * @param qualifiedName for the TypeRegistryEntity
     * @param terms the list of terms to append to the TypeRegistryEntity
     * @return the TypeRegistryEntity that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static TypeRegistryEntity appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (TypeRegistryEntity) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a TypeRegistryEntity, without replacing all existing terms linked to the TypeRegistryEntity.
     * Note: this operation must make two API calls — one to retrieve the TypeRegistryEntity's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the TypeRegistryEntity
     * @param qualifiedName for the TypeRegistryEntity
     * @param terms the list of terms to remove from the TypeRegistryEntity, which must be referenced by GUID
     * @return the TypeRegistryEntity that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static TypeRegistryEntity removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (TypeRegistryEntity) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a TypeRegistryEntity, without replacing existing Atlan tags linked to the TypeRegistryEntity.
     * Note: this operation must make two API calls — one to retrieve the TypeRegistryEntity's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the TypeRegistryEntity
     * @param qualifiedName of the TypeRegistryEntity
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated TypeRegistryEntity
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static TypeRegistryEntity appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (TypeRegistryEntity) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a TypeRegistryEntity, without replacing existing Atlan tags linked to the TypeRegistryEntity.
     * Note: this operation must make two API calls — one to retrieve the TypeRegistryEntity's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the TypeRegistryEntity
     * @param qualifiedName of the TypeRegistryEntity
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated TypeRegistryEntity
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static TypeRegistryEntity appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (TypeRegistryEntity) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a TypeRegistryEntity.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a TypeRegistryEntity
     * @param qualifiedName of the TypeRegistryEntity
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the TypeRegistryEntity
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
