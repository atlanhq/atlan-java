/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlasGlossaryTermType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
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
 * Instance of a term in Atlan. Terms define concepts in natural language that can be associated with other assets to provide meaning.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class GlossaryTerm extends Asset implements IGlossaryTerm, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AtlasGlossaryTerm";

    /** Fixed typeName for GlossaryTerms. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String abbreviation;

    /** TBC */
    @Attribute
    @Singular
    Map<String, String> additionalAttributes;

    /** TBC */
    @Attribute
    IGlossary anchor;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IGlossaryTerm> antonyms;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IAsset> assignedEntities;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IGlossaryCategory> categories;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IGlossaryTerm> classifies;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> examples;

    /** TBC */
    @Attribute
    @Singular("isATerm")
    SortedSet<IGlossaryTerm> isA;

    /** TBC */
    @Attribute
    String longDescription;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IGlossaryTerm> preferredTerms;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IGlossaryTerm> preferredToTerms;

    /** TBC */
    @Attribute
    @Singular("replacedByTerm")
    SortedSet<IGlossaryTerm> replacedBy;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IGlossaryTerm> replacementTerms;

    /** TBC */
    @Attribute
    @Singular("seeAlsoOne")
    SortedSet<IGlossaryTerm> seeAlso;

    /** TBC */
    @Attribute
    String shortDescription;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IGlossaryTerm> synonyms;

    /** TBC */
    @Attribute
    AtlasGlossaryTermType termType;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IGlossaryTerm> translatedTerms;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IGlossaryTerm> translationTerms;

    /** TBC */
    @Attribute
    String usage;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IGlossaryTerm> validValues;

    /** TBC */
    @Attribute
    @Singular("validValueFor")
    SortedSet<IGlossaryTerm> validValuesFor;

    /**
     * Builds the minimal object necessary to create a relationship to a GlossaryTerm, from a potentially
     * more-complete GlossaryTerm object.
     *
     * @return the minimal object necessary to relate to the GlossaryTerm
     * @throws InvalidRequestException if any of the minimal set of required properties for a GlossaryTerm relationship are not found in the initial object
     */
    @Override
    public GlossaryTerm trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all GlossaryTerm assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) GlossaryTerm assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all GlossaryTerm assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all GlossaryTerm assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) GlossaryTerms will be included
     * @return a fluent search that includes all GlossaryTerm assets
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
     * Reference to a GlossaryTerm by GUID. Use this to create a relationship to this GlossaryTerm,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the GlossaryTerm to reference
     * @return reference to a GlossaryTerm that can be used for defining a relationship to a GlossaryTerm
     */
    public static GlossaryTerm refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a GlossaryTerm by GUID. Use this to create a relationship to this GlossaryTerm,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the GlossaryTerm to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a GlossaryTerm that can be used for defining a relationship to a GlossaryTerm
     */
    public static GlossaryTerm refByGuid(String guid, Reference.SaveSemantic semantic) {
        return GlossaryTerm._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a GlossaryTerm by qualifiedName. Use this to create a relationship to this GlossaryTerm,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the GlossaryTerm to reference
     * @return reference to a GlossaryTerm that can be used for defining a relationship to a GlossaryTerm
     */
    public static GlossaryTerm refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a GlossaryTerm by qualifiedName. Use this to create a relationship to this GlossaryTerm,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the GlossaryTerm to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a GlossaryTerm that can be used for defining a relationship to a GlossaryTerm
     */
    public static GlossaryTerm refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return GlossaryTerm._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a GlossaryTerm by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the GlossaryTerm to retrieve, either its GUID or its full qualifiedName
     * @return the requested full GlossaryTerm, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GlossaryTerm does not exist or the provided GUID is not a GlossaryTerm
     */
    @JsonIgnore
    public static GlossaryTerm get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a GlossaryTerm by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the GlossaryTerm to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full GlossaryTerm, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GlossaryTerm does not exist or the provided GUID is not a GlossaryTerm
     */
    @JsonIgnore
    public static GlossaryTerm get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof GlossaryTerm) {
                return (GlossaryTerm) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof GlossaryTerm) {
                return (GlossaryTerm) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a GlossaryTerm by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the GlossaryTerm to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the GlossaryTerm, including any relationships
     * @return the requested GlossaryTerm, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GlossaryTerm does not exist or the provided GUID is not a GlossaryTerm
     */
    @JsonIgnore
    public static GlossaryTerm get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a GlossaryTerm by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the GlossaryTerm to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the GlossaryTerm, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the GlossaryTerm
     * @return the requested GlossaryTerm, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GlossaryTerm does not exist or the provided GUID is not a GlossaryTerm
     */
    @JsonIgnore
    public static GlossaryTerm get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = GlossaryTerm.select(client)
                    .where(GlossaryTerm.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof GlossaryTerm) {
                return (GlossaryTerm) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = GlossaryTerm.select(client)
                    .where(GlossaryTerm.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof GlossaryTerm) {
                return (GlossaryTerm) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) GlossaryTerm to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the GlossaryTerm
     * @return true if the GlossaryTerm is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary for creating a term.
     *
     * @param name of the term
     * @param glossary in which the term should be created
     * @return the minimal request necessary to create the term, as a builder
     * @throws InvalidRequestException if the glossary provided is without a GUID or qualifiedName
     */
    public static GlossaryTermBuilder<?, ?> creator(String name, Glossary glossary) throws InvalidRequestException {
        return creator(name, (String) null).anchor(glossary.trimToReference());
    }

    /**
     * Builds the minimal object necessary for creating a term.
     *
     * @param name of the term
     * @param glossaryId unique identifier of the term's glossary, either is real GUID or qualifiedName
     * @return the minimal request necessary to create the term, as a builder
     */
    public static GlossaryTermBuilder<?, ?> creator(String name, String glossaryId) {
        Glossary anchor = StringUtils.isUUID(glossaryId)
                ? Glossary.refByGuid(glossaryId)
                : Glossary.refByQualifiedName(glossaryId);
        return GlossaryTerm._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(name)
                .name(name)
                .anchor(anchor);
    }

    /**
     * Builds the minimal object necessary to update a GlossaryTerm.
     *
     * @param qualifiedName of the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryGuid unique identifier of the GlossaryTerm's glossary
     * @return the minimal request necessary to update the GlossaryTerm, as a builder
     */
    public static GlossaryTermBuilder<?, ?> updater(String qualifiedName, String name, String glossaryGuid) {
        // Turns out that updating a term requires the glossary GUID, and will not work
        // with the qualifiedName of the glossary
        return GlossaryTerm._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name)
                .anchor(Glossary.refByGuid(glossaryGuid));
    }

    /**
     * Builds the minimal object necessary to apply an update to a GlossaryTerm, from a potentially
     * more-complete GlossaryTerm object.
     *
     * @return the minimal object necessary to update the GlossaryTerm, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for GlossaryTerm are not found in the initial object
     */
    @Override
    public GlossaryTermBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        if (this.getAnchor() == null || !this.getAnchor().isValidReferenceByGuid()) {
            throw new InvalidRequestException(ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, TYPE_NAME, "anchor.guid");
        }
        return updater(this.getQualifiedName(), this.getName(), this.getAnchor().getGuid());
    }

    /**
     * Find a GlossaryTerm by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the term, if found. Note that this operation must run two
     * separate queries to first resolve the qualifiedName of the glossary, so will be somewhat slower.
     * If you already have the qualifiedName of the glossary, use findByNameFast instead.
     *
     * @param client connectivity to the Atlan tenant in which to search for the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryName human-readable name of the Glossary in which the category exists
     * @return the GlossaryTerm, if found
     * @throws AtlanException on any API problems, or if the GlossaryTerm does not exist
     */
    public static GlossaryTerm findByName(AtlanClient client, String name, String glossaryName) throws AtlanException {
        return findByName(client, name, glossaryName, (List<AtlanField>) null);
    }

    /**
     * Find a GlossaryTerm by its human-readable name. Note that this operation must run two
     * separate queries to first resolve the qualifiedName of the glossary, so will be somewhat slower.
     * If you already have the qualifiedName of the glossary, use findByNameFast instead.
     *
     * @param client connectivity to the Atlan tenant in which to search for the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryName human-readable name of the Glossary in which the category exists
     * @param attributes an optional collection of attributes (unchecked) to retrieve for the GlossaryTerm
     * @return the GlossaryTerm, if found
     * @throws AtlanException on any API problems, or if the GlossaryTerm does not exist
     */
    public static GlossaryTerm findByName(
            AtlanClient client, String name, String glossaryName, Collection<String> attributes) throws AtlanException {
        Glossary glossary = Glossary.findByName(client, glossaryName);
        return findByNameFast(client, name, glossary.getQualifiedName(), attributes);
    }

    /**
     * Find a GlossaryTerm by its human-readable name. Note that this operation must run two
     * separate queries to first resolve the qualifiedName of the glossary, so will be somewhat slower.
     * If you already have the qualifiedName of the glossary, use findByNameFast instead.
     *
     * @param client connectivity to the Atlan tenant in which to search for the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryName human-readable name of the Glossary in which the category exists
     * @param attributes an optional collection of attributes (checked) to retrieve for the GlossaryTerm
     * @return the GlossaryTerm, if found
     * @throws AtlanException on any API problems, or if the GlossaryTerm does not exist
     */
    public static GlossaryTerm findByName(
            AtlanClient client, String name, String glossaryName, List<AtlanField> attributes) throws AtlanException {
        Glossary glossary = Glossary.findByName(client, glossaryName);
        return findByNameFast(client, name, glossary.getQualifiedName(), attributes);
    }

    /**
     * Find a GlossaryTerm by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the term, if found.
     *
     * @param client connectivity to the Atlan tenant in which to search for the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryQualifiedName qualifiedName of the Glossary in which the category exists
     * @return the GlossaryTerm, if found
     * @throws AtlanException on any API problems, or if the GlossaryTerm does not exist
     */
    public static GlossaryTerm findByNameFast(AtlanClient client, String name, String glossaryQualifiedName)
            throws AtlanException {
        return findByNameFast(client, name, glossaryQualifiedName, (List<AtlanField>) null);
    }

    /**
     * Find a GlossaryTerm by its human-readable name.
     *
     * @param client connectivity to the Atlan tenant in which to search for the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryQualifiedName qualifiedName of the Glossary in which the category exists
     * @param attributes an optional collection of attributes (unchecked) to retrieve for the GlossaryTerm
     * @return the GlossaryTerm, if found
     * @throws AtlanException on any API problems, or if the GlossaryTerm does not exist
     */
    public static GlossaryTerm findByNameFast(
            AtlanClient client, String name, String glossaryQualifiedName, Collection<String> attributes)
            throws AtlanException {
        List<GlossaryTerm> results = new ArrayList<>();
        GlossaryTerm.select(client)
                .where(GlossaryTerm.NAME.eq(name))
                .where(GlossaryTerm.ANCHOR.eq(glossaryQualifiedName))
                .includeOnResults(GlossaryTerm.ANCHOR)
                ._includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .includeOnRelations(Asset.NAME)
                .pageSize(2)
                .stream()
                .limit(2)
                .filter(a -> a instanceof GlossaryTerm)
                .forEach(t -> results.add((GlossaryTerm) t));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_NAME, TYPE_NAME, name);
        } else if (results.size() > 1) {
            log.warn(
                    "Multiple terms found with the name '{}' in glossary '{}', returning only the first.",
                    name,
                    glossaryQualifiedName);
        }
        return results.get(0);
    }

    /**
     * Find a GlossaryTerm by its human-readable name.
     *
     * @param client connectivity to the Atlan tenant in which to search for the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryQualifiedName qualifiedName of the Glossary in which the category exists
     * @param attributes an optional collection of attributes (checked) to retrieve for the GlossaryTerm
     * @return the GlossaryTerm, if found
     * @throws AtlanException on any API problems, or if the GlossaryTerm does not exist
     */
    public static GlossaryTerm findByNameFast(
            AtlanClient client, String name, String glossaryQualifiedName, List<AtlanField> attributes)
            throws AtlanException {
        List<GlossaryTerm> results = new ArrayList<>();
        GlossaryTerm.select(client)
                .where(GlossaryTerm.NAME.eq(name))
                .where(GlossaryTerm.ANCHOR.eq(glossaryQualifiedName))
                .includeOnResults(GlossaryTerm.ANCHOR)
                .includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .includeOnRelations(Asset.NAME)
                .pageSize(2)
                .stream()
                .limit(2)
                .filter(a -> a instanceof GlossaryTerm)
                .forEach(t -> results.add((GlossaryTerm) t));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_NAME, TYPE_NAME, name);
        } else if (results.size() > 1) {
            log.warn(
                    "Multiple terms found with the name '{}' in glossary '{}', returning only the first.",
                    name,
                    glossaryQualifiedName);
        }
        return results.get(0);
    }

    /**
     * Remove the system description from a GlossaryTerm.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GlossaryTerm's description
     * @param qualifiedName of the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryGuid unique ID (GUID) of the GlossaryTerm's glossary
     * @return the updated GlossaryTerm, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryTerm removeDescription(
            AtlanClient client, String qualifiedName, String name, String glossaryGuid) throws AtlanException {
        return (GlossaryTerm) Asset.removeDescription(client, updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Remove the user's description from a GlossaryTerm.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GlossaryTerm's description
     * @param qualifiedName of the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryGuid unique ID (GUID) of the GlossaryTerm's glossary
     * @return the updated GlossaryTerm, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryTerm removeUserDescription(
            AtlanClient client, String qualifiedName, String name, String glossaryGuid) throws AtlanException {
        return (GlossaryTerm) Asset.removeUserDescription(client, updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Remove the owners from a GlossaryTerm.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GlossaryTerm's owners
     * @param qualifiedName of the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryGuid unique ID (GUID) of the GlossaryTerm's glossary
     * @return the updated GlossaryTerm, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryTerm removeOwners(AtlanClient client, String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return (GlossaryTerm) Asset.removeOwners(client, updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Update the certificate on a GlossaryTerm.
     *
     * @param client connectivity to the Atlan tenant on which to update the GlossaryTerm's certificate
     * @param qualifiedName of the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryGuid unique ID (GUID) of the GlossaryTerm's glossary
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated GlossaryTerm, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryTerm updateCertificate(
            AtlanClient client,
            String qualifiedName,
            String name,
            String glossaryGuid,
            CertificateStatus certificate,
            String message)
            throws AtlanException {
        return (GlossaryTerm)
                Asset.updateCertificate(client, updater(qualifiedName, name, glossaryGuid), certificate, message);
    }

    /**
     * Remove the certificate from a GlossaryTerm.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GlossaryTerm's certificate
     * @param qualifiedName of the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryGuid unique ID (GUID) of the GlossaryTerm's glossary
     * @return the updated GlossaryTerm, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryTerm removeCertificate(
            AtlanClient client, String qualifiedName, String name, String glossaryGuid) throws AtlanException {
        return (GlossaryTerm) Asset.removeCertificate(client, updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Update the announcement on a GlossaryTerm.
     *
     * @param client connectivity to the Atlan tenant on which to update the GlossaryTerm's announcement
     * @param qualifiedName of the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryGuid unique ID (GUID) of the GlossaryTerm's glossary
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the updated GlossaryTerm, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryTerm updateAnnouncement(
            AtlanClient client,
            String qualifiedName,
            String name,
            String glossaryGuid,
            AtlanAnnouncementType type,
            String title,
            String message)
            throws AtlanException {
        return (GlossaryTerm)
                Asset.updateAnnouncement(client, updater(qualifiedName, name, glossaryGuid), type, title, message);
    }

    /**
     * Remove the announcement from a GlossaryTerm.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GlossaryTerm's announcement
     * @param qualifiedName of the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryGuid unique ID (GUID) of the GlossaryTerm's glossary
     * @return the updated GlossaryTerm, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryTerm removeAnnouncement(
            AtlanClient client, String qualifiedName, String name, String glossaryGuid) throws AtlanException {
        return (GlossaryTerm) Asset.removeAnnouncement(client, updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Add Atlan tags to a GlossaryTerm, without replacing existing Atlan tags linked to the GlossaryTerm.
     * Note: this operation must make two API calls — one to retrieve the GlossaryTerm's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the GlossaryTerm
     * @param qualifiedName of the GlossaryTerm
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated GlossaryTerm
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static GlossaryTerm appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (GlossaryTerm) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a GlossaryTerm, without replacing existing Atlan tags linked to the GlossaryTerm.
     * Note: this operation must make two API calls — one to retrieve the GlossaryTerm's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the GlossaryTerm
     * @param qualifiedName of the GlossaryTerm
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated GlossaryTerm
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static GlossaryTerm appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (GlossaryTerm) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a GlossaryTerm.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a GlossaryTerm
     * @param qualifiedName of the GlossaryTerm
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the GlossaryTerm
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
