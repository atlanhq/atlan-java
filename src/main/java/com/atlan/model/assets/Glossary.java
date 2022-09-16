/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.exception.AtlanException;
import com.atlan.exception.LogicException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.relations.Reference;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.util.QueryFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a glossary in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class Glossary extends Asset {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AtlasGlossary";

    /** Fixed typeName for glossaries. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Unused attributes. */
    @JsonIgnore
    String shortDescription;

    @JsonIgnore
    String longDescription;

    @JsonIgnore
    String language;

    @JsonIgnore
    String usage;

    @JsonIgnore
    Map<String, String> additionalAttributes;

    /** Terms within this glossary. */
    @Singular
    @Attribute
    Set<Reference> terms;

    /** Categories within this glossary. */
    @Singular
    @Attribute
    Set<Reference> categories;

    /**
     * Builds the minimal object necessary for creating a glossary.
     *
     * @param name of the glossary
     * @return the minimal object necessary to create the glossary, as a builder
     */
    public static GlossaryBuilder<?, ?> creator(String name) {
        return Glossary.builder().qualifiedName(name).name(name);
    }

    /**
     * Builds the minimal object necessary to update a glossary.
     *
     * @param guid unique identifier of the glossary
     * @param name of the glossary
     * @return the minimal object necessary to update the glossary, as a builder
     */
    public static GlossaryBuilder<?, ?> updater(String guid, String name) {
        return Glossary.builder().guid(guid).qualifiedName(name).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a glossary, from a potentially
     * more-complete glossary object.
     *
     * @return the minimal object necessary to update the glossary, as a builder
     */
    @Override
    protected GlossaryBuilder<?, ?> trimToRequired() {
        return updater(this.getGuid(), this.getName());
    }

    /**
     * Set up the minimal object required to reference a glossary. Only one of the following is required.
     *
     * @param glossaryGuid unique identifier of the glossary for the term
     * @param glossaryQualifiedName unique name of the glossary
     * @return a builder that can be further extended with other metadata
     */
    static Reference anchorLink(String glossaryGuid, String glossaryQualifiedName) {
        Reference anchor = null;
        if (glossaryGuid == null && glossaryQualifiedName == null) {
            return null;
        } else if (glossaryGuid != null) {
            anchor = Reference.to(TYPE_NAME, glossaryGuid);
        } else {
            anchor = Reference.by(TYPE_NAME, glossaryQualifiedName);
        }
        return anchor;
    }

    /**
     * Update the certificate on a glossary.
     *
     * @param qualifiedName of the glossary
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated glossary, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Glossary updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (Glossary) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a glossary.
     *
     * @param qualifiedName of the glossary
     * @param name of the glossary
     * @return the updated glossary, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Glossary removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (Glossary)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a glossary.
     *
     * @param qualifiedName of the glossary
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Glossary updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (Glossary) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a glossary.
     *
     * @param qualifiedName of the glossary
     * @param name of the glossary
     * @return the updated glossary, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Glossary removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (Glossary)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a glossary.
     *
     * @param qualifiedName of the glossary
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the glossary
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a glossary.
     *
     * @param qualifiedName of the glossary
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the glossary
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Find a glossary by its human-readable name.
     *
     * @param name of the glossary
     * @param attributes an optional collection of attributes to retrieve for the glossary
     * @return the glossary, if found
     * @throws AtlanException on any API problems, or if the glossary does not exist
     */
    public static Glossary findByName(String name, Collection<String> attributes) throws AtlanException {
        Query byType = QueryFactory.withType(TYPE_NAME);
        Query byName = QueryFactory.withExactName(name);
        Query active = QueryFactory.active();
        Query filter = BoolQuery.of(b -> b.filter(byType, byName, active))._toQuery();
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
                log.warn("Multiple glossaries found with the name '{}', returning only the first.", name);
            }
            List<Entity> results = response.getEntities();
            if (results != null && !results.isEmpty()) {
                Entity first = results.get(0);
                if (first instanceof Glossary) {
                    return (Glossary) first;
                } else {
                    throw new LogicException(
                            "Found a non-glossary result when searching for only glossaries.",
                            "ATLAN-JAVA-CLIENT-500-090",
                            500);
                }
            }
        }
        throw new NotFoundException(
                "Unable to find a glossary with the name: " + name, "ATLAN-JAVA-CLIENT-404-090", 404, null);
    }
}
