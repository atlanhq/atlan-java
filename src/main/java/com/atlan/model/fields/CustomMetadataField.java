/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.enums.AtlanEnum;
import com.atlan.model.typedefs.AttributeDef;
import java.util.Collection;

/**
 * Utility class to simplify searching for values on custom metadata attributes.
 */
public class CustomMetadataField extends SearchableField {

    private final String setName;
    private final String attributeName;
    private final AttributeDef attributeDef;

    public CustomMetadataField(AtlanClient client, String setName, String attributeName) throws AtlanException {
        super(
                client.getCustomMetadataCache().getAttributeForSearchResults(setName, attributeName),
                client.getCustomMetadataCache().getAttrIdForName(setName, attributeName));
        this.setName = setName;
        this.attributeName = attributeName;
        this.attributeDef = client.getCustomMetadataCache().getAttributeDef(getElasticFieldName());
    }

    public static CustomMetadataField of(AtlanClient client, String set, String attr) throws AtlanException {
        return new CustomMetadataField(client, set, attr);
    }

    // TODO: copy across the JavaDocs since these won't inherit, as they will uniquely
    //  raise runtime exceptions (since we cannot determine valid combos for custom metadata
    //  at compile-time)

    public Query startsWith(String value, boolean caseInsensitive) throws InvalidRequestException {
        // TODO: validate the custom metadata attribute is searchable this way
        return IKeywordSearchable.startsWith(getElasticFieldName(), value, caseInsensitive);
    }

    public Query eq(AtlanEnum value) throws InvalidRequestException {
        if (attributeDef.getEnumValues() != null
                && !attributeDef.getEnumValues().isEmpty()) {
            throw new InvalidRequestException(ErrorCode.INVALID_QUERY, "eq", setName + "." + attributeName);
        }
        return IKeywordSearchable.eq(getElasticFieldName(), value);
    }

    public Query eq(boolean value) throws InvalidRequestException {
        // TODO: validate the custom metadata attribute is searchable this way
        return IBooleanSearchable.eq(getElasticFieldName(), value);
    }

    public Query eq(String value, boolean caseInsensitive) throws InvalidRequestException {
        // TODO: validate the custom metadata attribute is searchable this way
        return IKeywordSearchable.eq(getElasticFieldName(), value, caseInsensitive);
    }

    public Query in(Collection<String> values) throws InvalidRequestException {
        // TODO: validate the custom metadata attribute is searchable this way
        return IKeywordSearchable.in(getElasticFieldName(), values);
    }

    public <T extends Number> Query eq(T value) throws InvalidRequestException {
        // TODO: validate the custom metadata attribute is searchable this way
        return INumericallySearchable.eq(getElasticFieldName(), value);
    }

    public <T extends Number> Query gt(T value) throws InvalidRequestException {
        // TODO: validate the custom metadata attribute is searchable this way
        return INumericallySearchable.gt(getElasticFieldName(), value);
    }

    public <T extends Number> Query gte(T value) throws InvalidRequestException {
        // TODO: validate the custom metadata attribute is searchable this way
        return INumericallySearchable.gte(getElasticFieldName(), value);
    }

    public <T extends Number> Query lt(T value) throws InvalidRequestException {
        // TODO: validate the custom metadata attribute is searchable this way
        return INumericallySearchable.lt(getElasticFieldName(), value);
    }

    public <T extends Number> Query lte(T value) throws InvalidRequestException {
        // TODO: validate the custom metadata attribute is searchable this way
        return INumericallySearchable.lte(getElasticFieldName(), value);
    }

    public <T extends Number> Query between(T min, T max) throws InvalidRequestException {
        // TODO: validate the custom metadata attribute is searchable this way
        return INumericallySearchable.between(getElasticFieldName(), min, max);
    }

    public Query match(String value) throws InvalidRequestException {
        // TODO: validate the custom metadata attribute is searchable this way
        return ITextSearchable.match(getElasticFieldName(), value);
    }
}
