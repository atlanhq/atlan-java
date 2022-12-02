/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.util;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import com.atlan.cache.ClassificationCache;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanStatus;
import java.util.ArrayList;
import java.util.List;

public class QueryFactory {

    private static final Query ACTIVE = TermQuery.of(t -> t.field("__state").value(AtlanStatus.ACTIVE.getValue()))
            ._toQuery();
    private static final Query ARCHIVED = TermQuery.of(t -> t.field("__state").value(AtlanStatus.DELETED.getValue()))
            ._toQuery();
    private static final Query WITH_LINEAGE =
            TermQuery.of(t -> t.field("__hasLineage").value(true))._toQuery();

    /**
     * Returns a query that will only match active assets.
     *
     * @return a query that will only match active assets
     */
    public static Query active() {
        return ACTIVE;
    }

    /**
     * Returns a query that will only match soft-deleted (archived) assets.
     *
     * @return a query that will only match soft-deleted (archived) assets
     */
    public static Query archived() {
        return ARCHIVED;
    }

    /**
     * Returns a query that will only match assets with lineage.
     *
     * @return a query that will only match assets with lineage
     */
    public static Query withLineage() {
        return WITH_LINEAGE;
    }

    /**
     * Returns a query that will only match assets of the type provided.
     *
     * @param typeName for assets to match
     * @return a query that will only match assets of the type provided
     */
    public static Query withType(String typeName) {
        return TermQuery.of(t -> t.field("__typeName.keyword").value(typeName))._toQuery();
    }

    /**
     * Returns a query that will match all assets that are a subtype of the type provided.
     *
     * @param typeName of the supertype for assets to match
     * @return a query that will only match assets of a subtype of the type provided
     */
    public static Query withSuperType(String typeName) {
        return TermQuery.of(t -> t.field("__superTypeNames.keyword").value(typeName))
                ._toQuery();
    }

    /**
     * Returns a query that will only match assets with the certificate status provided.
     *
     * @param certificate for assets to match
     * @return a query that will only match assets with the certificate status provided
     */
    public static Query withCertificate(AtlanCertificateStatus certificate) {
        return TermQuery.of(t -> t.field("certificateStatus").value(certificate.getValue()))
                ._toQuery();
    }

    /**
     * Returns a query that will only match assets with a name that exactly matches the provided string.
     *
     * @param name for assets to match (exactly)
     * @return a query that will only match assets with a name that exactly matches the provided string
     */
    public static Query withExactName(String name) {
        return TermQuery.of(t -> t.field("name.keyword").value(name))._toQuery();
    }

    /**
     * Returns a query that will only match assets with a qualifiedName that begins with the provided string.
     * This is useful for finding all assets within a particular parent asset, such as all tables, views,
     * and columns within a schema (when the provided qualifiedName is for a schema).
     *
     * @param qualifiedName for the parent asset in which to find all child assets
     * @return a query that will only match assets with a qualifiedName that begins with the provided string
     */
    public static Query whereQualifiedNameStartsWith(String qualifiedName) {
        return PrefixQuery.of(t -> t.field("qualifiedName").value(qualifiedName))
                ._toQuery();
    }

    /**
     * Returns a query that will only match assets that have some non-null, non-empty value
     * (no matter what actual value) for the provided attribute.
     *
     * @param attribute for which a value must exist on an asset for the asset to match
     * @return a query that will only match assets that have some non-null, non-empty value
     *         (no matter what actual value) for the provided attribute.
     */
    public static Query withAnyValueFor(String attribute) {
        return ExistsQuery.of(t -> t.field(attribute))._toQuery();
    }

    /**
     * Returns a query that will only match assets that have at least one of the classifications
     * provided. This will match irrespective of the classification being directly applied to the
     * asset, or if it was propagated to the asset.
     *
     * @param classificationNames human-readable names of the classifications
     * @return a query that will only match assets that have at least one of the classifications provided
     * @throws AtlanException on any error communicating with the API to refresh the classification cache
     */
    public static Query withAtLeastOneClassification(List<String> classificationNames) throws AtlanException {
        List<FieldValue> values = new ArrayList<>();
        for (String name : classificationNames) {
            String classificationId = ClassificationCache.getIdForName(name);
            if (classificationId == null) {
                throw new InvalidRequestException(
                        "Unable to find classification with name: " + name,
                        "classificationName",
                        "ATLAN-JAVA-CLIENT-400-200",
                        400,
                        null);
            }
            values.add(FieldValue.of(classificationId));
        }
        Query byDirectClassification = TermsQuery.of(
                        t -> t.field("__traitNames").terms(TermsQueryField.of(f -> f.value(values))))
                ._toQuery();
        Query byPropagatedClassification = TermsQuery.of(
                        t -> t.field("__propagatedTraitNames").terms(TermsQueryField.of(f -> f.value(values))))
                ._toQuery();
        return BoolQuery.of(b -> b.should(byDirectClassification, byPropagatedClassification)
                        .minimumShouldMatch("1"))
                ._toQuery();
    }

    /**
     * Returns a query that will only match assets that have at least one of the terms assigned.
     *
     * @param termQualifiedNames the qualifiedNames of the terms
     * @return a query that will only match assets that have at least one of the terms assigned
     */
    public static Query withAtLeastOneTerm(List<String> termQualifiedNames) {
        List<FieldValue> values = new ArrayList<>();
        for (String qualifiedName : termQualifiedNames) {
            values.add(FieldValue.of(qualifiedName));
        }
        return TermsQuery.of(t -> t.field("__meanings").terms(TermsQueryField.of(f -> f.value(values))))
                ._toQuery();
    }
}
