/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import com.atlan.exception.AtlanException;
import com.atlan.exception.LogicException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.util.QueryFactory;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a term in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class GlossaryTerm extends Asset {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AtlasGlossaryTerm";

    /** Fixed typeName for GlossaryTerms. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /**
     * Unused.
     * @see #description
     */
    @Attribute
    String shortDescription;

    /**
     * Unused.
     * @see #description
     */
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

    /** Unused. */
    @Attribute
    @Singular
    SortedSet<GlossaryTerm> translationTerms;

    /** Unused. */
    @Attribute
    @Singular("validValueFor")
    SortedSet<GlossaryTerm> validValuesFor;

    /** Glossary terms that have the same, or a very similar meaning in the same language. */
    @Attribute
    @Singular
    SortedSet<GlossaryTerm> synonyms;

    /** Unused. */
    @Attribute
    @Singular("replacedByTerm")
    SortedSet<GlossaryTerm> replacedBy;

    /** Unused. */
    @Attribute
    @Singular
    SortedSet<GlossaryTerm> validValues;

    /** Unused. */
    @Attribute
    @Singular
    SortedSet<GlossaryTerm> replacementTerms;

    /** Linked glossary terms that may also be of interest. */
    @Attribute
    @Singular("seeAlsoOne")
    SortedSet<GlossaryTerm> seeAlso;

    /** Unused. */
    @Attribute
    @Singular
    SortedSet<GlossaryTerm> translatedTerms;

    /** Unused. */
    @Attribute
    @Singular("isATerm")
    SortedSet<GlossaryTerm> isA;

    /** Glossary in which the term is located. */
    @Attribute
    Glossary anchor;

    /** Glossary terms that have the opposite (or near opposite) meaning in the same language. */
    @Attribute
    @Singular
    SortedSet<GlossaryTerm> antonyms;

    /** Assets that are attached to this term. */
    @Attribute
    @Singular
    SortedSet<Asset> assignedEntities;

    /** Categories within which this term is organized. */
    @Attribute
    @Singular
    SortedSet<GlossaryCategory> categories;

    /** Unused. */
    @Attribute
    @Singular
    SortedSet<GlossaryTerm> classifies;

    /** Unused. */
    @Attribute
    @Singular
    SortedSet<GlossaryTerm> preferredToTerms;

    /** Unused. */
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

    /** Remove the linked assets from the term, if any are set on the term. */
    public void removeAssignedEntities() {
        addNullField("assignedEntities");
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
     * Builds the minimal object necessary to update a term.
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
     */
    @Override
    protected GlossaryTermBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName(), this.getAnchor().getGuid());
    }

    /**
     * Find a glossary term by its human-readable name.
     *
     * @param name of the glossary term
     * @param glossaryQualifiedName qualifiedName of the glossary in which to look for the term
     * @param attributes an optional collection of attributes to retrieve for the glossary term
     * @return the glossary term, if found
     * @throws AtlanException on any API problems, or if the glossary term does not exist
     */
    public static GlossaryTerm findByName(String name, String glossaryQualifiedName, Collection<String> attributes)
            throws AtlanException {
        Query byType = QueryFactory.withType(TYPE_NAME);
        Query byName = QueryFactory.withExactName(name);
        Query byGlossary = TermsQuery.of(t -> t.field("__glossary")
                        .terms(TermsQueryField.of(f -> f.value(List.of(FieldValue.of(glossaryQualifiedName))))))
                ._toQuery();
        Query active = QueryFactory.active();
        Query filter =
                BoolQuery.of(b -> b.filter(byType, byName, byGlossary, active))._toQuery();
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
                        "Multiple glossary terms found with the name '{}' in glossary '{}', returning only the first.",
                        name,
                        glossaryQualifiedName);
            }
            List<Entity> results = response.getEntities();
            if (results != null && !results.isEmpty()) {
                Entity first = results.get(0);
                if (first instanceof GlossaryTerm) {
                    return (GlossaryTerm) first;
                } else {
                    throw new LogicException(
                            "Found a non-glossary term result when searching for only glossary terms.",
                            "ATLAN-JAVA-CLIENT-500-091",
                            500);
                }
            }
        }
        throw new NotFoundException(
                "Unable to find a glossary term with the name: " + name, "ATLAN-JAVA-CLIENT-404-091", 404, null);
    }

    /**
     * Retrieves a GlossaryTerm by its GUID, complete with all of its relationships.
     *
     * @param guid of the GlossaryTerm to retrieve
     * @return the requested full GlossaryTerm, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GlossaryTerm does not exist or the provided GUID is not a GlossaryTerm
     */
    public static GlossaryTerm retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof GlossaryTerm) {
            return (GlossaryTerm) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a GlossaryTerm.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
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
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof GlossaryTerm) {
            return (GlossaryTerm) entity;
        } else {
            throw new NotFoundException(
                    "No GlossaryTerm found with qualifiedName: " + qualifiedName,
                    "ATLAN_JAVA_CLIENT-404-003",
                    404,
                    null);
        }
    }

    /**
     * Restore the archived (soft-deleted) GlossaryTerm to active.
     *
     * @param qualifiedName for the GlossaryTerm
     * @return the GlossaryTerm that was restored
     * @throws AtlanException on any API problems
     */
    public static GlossaryTerm restore(String qualifiedName) throws AtlanException {
        return (GlossaryTerm) Asset.restore(TYPE_NAME, qualifiedName);
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
            String qualifiedName, String name, String glossaryGuid, AtlanCertificateStatus certificate, String message)
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
