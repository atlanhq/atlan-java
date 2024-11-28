/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.core.AtlanObject;
import com.atlan.net.ApiResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SSOMapping extends ApiResource {
    private static final long serialVersionUID = 2L;

    /** Unique identifier (GUID) of the mapping. */
    String id;

    /** Name of the mapping. */
    String name;

    /** TBC */
    String identityProviderMapper;

    /** Alias for the identity provider, typically a value from AtlanSSO enumeration. */
    String identityProviderAlias;

    /** Configuration for the mapping. */
    Config config;

    @Getter
    @Jacksonized
    @Builder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class Config extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** Mechanism used for the mapping. */
        String syncMode;

        /** Any additional attributes for the mapping. */
        String attributes;

        /** Internal name of the mapped group in Atlan. */
        @JsonProperty("group")
        String groupName;

        /** Name of the user attribute. */
        @JsonProperty("user.attribute")
        String userAttribute;

        /** Name of the SSO attribute containing the mapping. */
        @JsonProperty("attribute.name")
        String attributeName;

        /** Value of the SSO attribute which should map to the group. */
        @JsonProperty("attribute.value")
        String attributeValue;

        /** Human-readable name of the attribute containing the mapping. */
        @JsonProperty("attribute.friendly.name")
        String attributeFriendlyName;

        /** TBC */
        @JsonProperty("are.attribute.values.regex")
        String attributeValuesRegex;

        /** TBC */
        String role;
    }
}
