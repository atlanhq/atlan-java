/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.GoogleDataStudioAssetType;
import com.atlan.model.enums.LinkIconType;
import com.atlan.model.enums.PowerBIEndorsementType;
import com.atlan.model.relations.UniqueAttributes;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = APISpec.class, name = APISpec.TYPE_NAME),
    @JsonSubTypes.Type(value = APIPath.class, name = APIPath.TYPE_NAME),
})
@SuppressWarnings("cast")
public abstract class API extends Catalog {

    public static final String TYPE_NAME = "API";

    /** TBC */
    @Attribute
    String apiSpecType;

    /** TBC */
    @Attribute
    String apiSpecVersion;

    /** TBC */
    @Attribute
    String apiSpecName;

    /** TBC */
    @Attribute
    String apiSpecQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    Map<String, String> apiExternalDocs;

    /** TBC */
    @Attribute
    Boolean apiIsAuthOptional;

}
