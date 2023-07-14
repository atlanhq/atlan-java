/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Source: keycloak > server-spi-private/src/main/java/org/keycloak/events/admin/ResourceType.java
 */
public enum AdminResourceType implements AtlanEnum {
    REALM("REALM"),
    REALM_ROLE("REALM_ROLE"),
    REALM_ROLE_MAPPING("REALM_ROLE_MAPPING"),
    REALM_SCOPE_MAPPING("REALM_SCOPE_MAPPING"),
    AUTH_FLOW("AUTH_FLOW"),
    AUTH_EXECUTION_FLOW("AUTH_EXECUTION_FLOW"),
    AUTH_EXECUTION("AUTH_EXECUTION"),
    AUTHENTICATOR_CONFIG("AUTHENTICATOR_CONFIG"),
    REQUIRED_ACTION("REQUIRED_ACTION"),
    IDENTITY_PROVIDER("IDENTITY_PROVIDER"),
    IDENTITY_PROVIDER_MAPPER("IDENTITY_PROVIDER_MAPPER"),
    PROTOCOL_MAPPER("PROTOCOL_MAPPER"),
    USER("USER"),
    USER_LOGIN_FAILURE("USER_LOGIN_FAILURE"),
    USER_SESSION("USER_SESSION"),
    USER_FEDERATION_PROVIDER("USER_FEDERATION_PROVIDER"),
    USER_FEDERATION_MAPPER("USER_FEDERATION_MAPPER"),
    GROUP("GROUP"),
    GROUP_MEMBERSHIP("GROUP_MEMBERSHIP"),
    CLIENT("CLIENT"),
    CLIENT_INITIAL_ACCESS_MODEL("CLIENT_INITIAL_ACCESS_MODEL"),
    CLIENT_ROLE("CLIENT_ROLE"),
    CLIENT_ROLE_MAPPING("CLIENT_ROLE_MAPPING"),
    CLIENT_SCOPE("CLIENT_SCOPE"),
    CLIENT_SCOPE_MAPPING("CLIENT_SCOPE_MAPPING"),
    CLIENT_SCOPE_CLIENT_MAPPING("CLIENT_SCOPE_CLIENT_MAPPING"),
    CLUSTER_NODE("CLUSTER_NODE"),
    COMPONENT("COMPONENT"),
    AUTHORIZATION_RESOURCE_SERVER("AUTHORIZATION_RESOURCE_SERVER"),
    AUTHORIZATION_RESOURCE("AUTHORIZATION_RESOURCE"),
    AUTHORIZATION_SCOPE("AUTHORIZATION_SCOPE"),
    AUTHORIZATION_POLICY("AUTHORIZATION_POLICY"),
    CUSTOM("CUSTOM"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AdminResourceType(String value) {
        this.value = value;
    }

    public static AdminResourceType fromValue(String value) {
        for (AdminResourceType b : AdminResourceType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
