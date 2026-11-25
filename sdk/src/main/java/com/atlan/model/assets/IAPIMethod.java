/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.fields.KeywordField;
import com.atlan.model.fields.RelationField;
import com.atlan.model.fields.TextField;
import com.atlan.serde.AssetDeserializer;
import com.atlan.serde.AssetSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.processing.Generated;

/**
 * Instance of an API method (operation) on a path in Atlan.
 * Represents a single HTTP method such as GET, POST, PUT, DELETE on an APIPath.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@JsonSerialize(using = AssetSerializer.class)
@JsonDeserialize(using = AssetDeserializer.class)
public interface IAPIMethod {

    public static final String TYPE_NAME = "APIMethod";

    /** Request body or schema information for this API method. */
    TextField API_METHOD_REQUEST = new TextField("apiMethodRequest", "apiMethodRequest");

    /** Response body or schema information for this API method. */
    TextField API_METHOD_RESPONSE = new TextField("apiMethodResponse", "apiMethodResponse");

    /** Map of HTTP response status codes to the qualified names of the APIObject schemas that describe each response. */
    KeywordField API_METHOD_RESPONSE_CODES = new KeywordField("apiMethodResponseCodes", "apiMethodResponseCodes");

    /** APIObject schema describing this method's request body. */
    RelationField API_METHOD_REQUEST_SCHEMA = new RelationField("apiMethodRequestSchema");

    /** APIObject schemas describing this method's response bodies. */
    RelationField API_METHOD_RESPONSE_SCHEMAS = new RelationField("apiMethodResponseSchemas");

    /** API path on which this method operates. */
    RelationField API_PATH = new RelationField("apiPath");

    /** Request body or schema information for this API method. */
    String getApiMethodRequest();

    /** Response body or schema information for this API method. */
    String getApiMethodResponse();

    /** Map of HTTP response status codes to the qualified names of the APIObject schemas that describe each response. */
    Map<String, String> getApiMethodResponseCodes();

    /** APIObject schema describing this method's request body. */
    default IAPIObject getApiMethodRequestSchema() {
        return null;
    }

    /** APIObject schemas describing this method's response bodies. */
    default SortedSet<IAPIObject> getApiMethodResponseSchemas() {
        return null;
    }

    /** API path on which this method operates. */
    default IAPIPath getApiPath() {
        return null;
    }
}
