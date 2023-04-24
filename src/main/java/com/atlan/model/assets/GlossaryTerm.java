/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.LogicException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.*;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.util.QueryFactory;
import java.util.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a term in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class GlossaryTerm extends Asset {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AtlasGlossaryTerm";

    /** Fixed typeName for GlossaryTerms. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Unused. */
    @Attribute
    String shortDescription;

    /** Unused. */
    @Attribute
    String longDescription;

    /** Unused. */
    @Attribute
    @Singular
    SortedSet<String> examples;

    /** Unused. */
    @Attribute
    String abbreviation;

    /** Unused. */
    @Attribute
    String usage;

    /** Unused. */
    @Attribute
    @Singular
    Map<String, String> additionalAttributes;

    /** TBC (Other direction is translatedTerms.) */
    @Attribute
    @Singular
    SortedSet<GlossaryTerm> translationTerms;

    /** TBC (Other direction is validValues.) */
    @Attribute
    @Singular("validValueFor")
    SortedSet<GlossaryTerm> validValuesFor;

    /** Terms that have the same (or a very similar) meaning in the same language. */
    @Attribute
    @Singular
    SortedSet<GlossaryTerm> synonyms;

    /** TBC */
    @Attribute
    @Singular("replacedByTerm")
    SortedSet<GlossaryTerm> replacedBy;

    /** TBC (Other direction is validValuesFor.) */
    @Attribute
    @Singular
    SortedSet<GlossaryTerm> validValues;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<GlossaryTerm> replacementTerms;

    /** Linked terms that may also be of interest. */
    @Attribute
    @Singular("seeAlsoOne")
    SortedSet<GlossaryTerm> seeAlso;

    /** TBC (Other direction is translationTerms.) */
    @Attribute
    @Singular
    SortedSet<GlossaryTerm> translatedTerms;

    /** TBC (Other direction is classifies.) */
    @Attribute
    @Singular("isATerm")
    SortedSet<GlossaryTerm> isA;

    /** Glossary in which this term is contained. */
    @Attribute
    Glossary anchor;

    /** Terms that have the opposite (or near opposite) meaning in the same language. */
    @Attribute
    @Singular
    SortedSet<GlossaryTerm> antonyms;

    /** Assets that are attached to this term. Note: this should ONLY be used to view such relationships, never to set such relationships. Only set such relationships in the other direction. */
    @Attribute
    @Singular
    SortedSet<Asset> assignedEntities;

    /** Categories within which this term is organized. */
    @Attribute
    @Singular
    SortedSet<GlossaryCategory> categories;

    /** TBC (Other direction is isA.) */
    @Attribute
    @Singular
    SortedSet<GlossaryTerm> classifies;

    /** Other terms that are less common or less preferred than this term. (Other direction is preferredTerms.) */
    @Attribute
    @Singular
    SortedSet<GlossaryTerm> preferredToTerms;

    /** Preferred terms to use instead of this term. (Other direction is preferredToTerms.) */
    @Attribute
    @Singular
    SortedSet<GlossaryTerm> preferredTerms;

    /**
     * Reference to a GlossaryTerm by GUID.
     *
     * @param guid the GUID of the GlossaryTerm to reference
     * @return reference to a GlossaryTerm that can be used for defining a relationship to a GlossaryTerm
     */
    public static GlossaryTerm refByGuid(String guid) {
        return GlossaryTerm.builder().guid(guid).build();
    }

    /**
     * Reference to a GlossaryTerm by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the GlossaryTerm to reference
     * @return reference to a GlossaryTerm that can be used for defining a relationship to a GlossaryTerm
     */
    public static GlossaryTerm refByQualifiedName(String qualifiedName) {
        return GlossaryTerm.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary for creating a term. At least one of glossaryGuid or
     * glossaryQualifiedName must be provided.
     *
     * @param name of the term
     * @param glossaryGuid unique identifier of the term's glossary
     * @param glossaryQualifiedName unique name of the term's glossary
     * @return the minimal request necessary to create the term, as a builder
     */
    public static GlossaryTermBuilder<?, ?> creator(String name, String glossaryGuid, String glossaryQualifiedName) {
        return GlossaryTerm.builder()
                .qualifiedName(name)
                .name(name)
                .anchor(Glossary.anchorLink(glossaryGuid, glossaryQualifiedName));
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
        return GlossaryTerm.builder()
                .qualifiedName(qualifiedName)
                .name(name)
                .anchor(Glossary.anchorLink(glossaryGuid, null));
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
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (this.getAnchor() == null
                || this.getAnchor().getGuid() == null
                || this.getAnchor().getGuid().length() == 0) {
            missing.add("anchor.guid");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "GlossaryTerm", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName(), this.getAnchor().getGuid());
    }

    /**
     * Find a GlossaryTerm by its human-readable name. Note that this operation must run two
     * separate queries to first resolve the qualifiedName of the glossary, so will be somewhat slower.
     * If you already have the qualifiedName of the glossary, use findByNameFast instead.
     *
     * @param name of the GlossaryTerm
     * @param glossaryName human-readable name of the Glossary in which the category exists
     * @param attributes an optional collection of attributes to retrieve for the GlossaryTerm
     * @return the GlossaryTerm, if found
     * @throws AtlanException on any API problems, or if the GlossaryTerm does not exist
     */
    public static GlossaryTerm findByName(String name, String glossaryName, Collection<String> attributes)
            throws AtlanException {
        Glossary glossary = Glossary.findByName(glossaryName, null);
        return findByNameFast(name, glossary.getQualifiedName(), attributes);
    }

    /**
     * Find a GlossaryTerm by its human-readable name.
     *
     * @param name of the GlossaryTerm
     * @param glossaryQualifiedName qualifiedName of the Glossary in which the category exists
     * @param attributes an optional collection of attributes to retrieve for the GlossaryTerm
     * @return the GlossaryTerm, if found
     * @throws AtlanException on any API problems, or if the GlossaryTerm does not exist
     */
    public static GlossaryTerm findByNameFast(String name, String glossaryQualifiedName, Collection<String> attributes)
            throws AtlanException {
        Query filter = QueryFactory.CompoundQuery.builder()
                .must(QueryFactory.beActive())
                .must(QueryFactory.beOfType(TYPE_NAME))
                .must(QueryFactory.have(KeywordFields.NAME).eq(name))
                .must(QueryFactory.have(KeywordFields.GLOSSARY).eq(glossaryQualifiedName))
                .build()
                ._toQuery();
        IndexSearchRequest.IndexSearchRequestBuilder<?, ?> builder = IndexSearchRequest.builder()
                .dsl(IndexSearchDSL.builder().from(0).size(2).query(filter).build());
        if (attributes != null && !attributes.isEmpty()) {
            builder.attributes(attributes);
        }
        IndexSearchRequest request = builder.build();
        IndexSearchResponse response = request.search();
        if (response != null) {
            long count = response.getApproximateCount();
            if (count > 1) {
                log.warn(
                        "Multiple terms found with the name '{}' in glossary '{}', returning only the first.",
                        name,
                        glossaryQualifiedName);
            }
            List<Asset> results = response.getAssets();
            if (results != null && !results.isEmpty()) {
                Asset first = results.get(0);
                if (first instanceof GlossaryTerm) {
                    return (GlossaryTerm) first;
                } else {
                    throw new LogicException(ErrorCode.FOUND_UNEXPECTED_ASSET_TYPE);
                }
            }
        }
        throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_NAME, TYPE_NAME, name);
    }

    /**
     * Retrieves a GlossaryTerm by its GUID, complete with all of its relationships.
     *
     * @param guid of the GlossaryTerm to retrieve
     * @return the requested full GlossaryTerm, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GlossaryTerm does not exist or the provided GUID is not a GlossaryTerm
     */
    public static GlossaryTerm retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof GlossaryTerm) {
            return (GlossaryTerm) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "GlossaryTerm");
        }
    }

    /**
     * Retrieves a GlossaryTerm by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the GlossaryTerm to retrieve
     * @return the requested full GlossaryTerm, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GlossaryTerm does not exist
     */
    public static GlossaryTerm retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof GlossaryTerm) {
            return (GlossaryTerm) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "GlossaryTerm");
        }
    }

    /**
     * Restore the archived (soft-deleted) GlossaryTerm to active.
     *
     * @param qualifiedName for the GlossaryTerm
     * @return true if the GlossaryTerm is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a GlossaryTerm.
     *
     * @param qualifiedName of the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryGuid unique ID (GUID) of the GlossaryTerm's glossary
     * @return the updated GlossaryTerm, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryTerm removeDescription(String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return (GlossaryTerm) Asset.removeDescription(updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Remove the user's description from a GlossaryTerm.
     *
     * @param qualifiedName of the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryGuid unique ID (GUID) of the GlossaryTerm's glossary
     * @return the updated GlossaryTerm, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryTerm removeUserDescription(String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return (GlossaryTerm) Asset.removeUserDescription(updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Remove the owners from a GlossaryTerm.
     *
     * @param qualifiedName of the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryGuid unique ID (GUID) of the GlossaryTerm's glossary
     * @return the updated GlossaryTerm, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryTerm removeOwners(String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return (GlossaryTerm) Asset.removeOwners(updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Update the certificate on a GlossaryTerm.
     *
     * @param qualifiedName of the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryGuid unique ID (GUID) of the GlossaryTerm's glossary
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated GlossaryTerm, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryTerm updateCertificate(
            String qualifiedName, String name, String glossaryGuid, CertificateStatus certificate, String message)
            throws AtlanException {
        return (GlossaryTerm) Asset.updateCertificate(updater(qualifiedName, name, glossaryGuid), certificate, message);
    }

    /**
     * Remove the certificate from a GlossaryTerm.
     *
     * @param qualifiedName of the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryGuid unique ID (GUID) of the GlossaryTerm's glossary
     * @return the updated GlossaryTerm, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryTerm removeCertificate(String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return (GlossaryTerm) Asset.removeCertificate(updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Update the announcement on a GlossaryTerm.
     *
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
            String qualifiedName,
            String name,
            String glossaryGuid,
            AtlanAnnouncementType type,
            String title,
            String message)
            throws AtlanException {
        return (GlossaryTerm)
                Asset.updateAnnouncement(updater(qualifiedName, name, glossaryGuid), type, title, message);
    }

    /**
     * Remove the announcement from a GlossaryTerm.
     *
     * @param qualifiedName of the GlossaryTerm
     * @param name of the GlossaryTerm
     * @param glossaryGuid unique ID (GUID) of the GlossaryTerm's glossary
     * @return the updated GlossaryTerm, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GlossaryTerm removeAnnouncement(String qualifiedName, String name, String glossaryGuid)
            throws AtlanException {
        return (GlossaryTerm) Asset.removeAnnouncement(updater(qualifiedName, name, glossaryGuid));
    }

    /**
     * Add classifications to a GlossaryTerm.
     *
     * @param qualifiedName of the GlossaryTerm
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the GlossaryTerm
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Add classifications to a GlossaryTerm.
     *
     * @param qualifiedName of the GlossaryTerm
     * @param classificationNames human-readable names of the classifications to add
     * @param propagate whether to propagate the classification (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated classifications when the classification is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the GlossaryTerm
     */
    public static void addClassifications(
            String qualifiedName,
            List<String> classificationNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addClassifications(
                TYPE_NAME,
                qualifiedName,
                classificationNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove a classification from a GlossaryTerm.
     *
     * @param qualifiedName of the GlossaryTerm
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the GlossaryTerm
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }
}
