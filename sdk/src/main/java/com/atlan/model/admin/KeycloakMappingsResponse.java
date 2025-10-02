/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.net.ApiResource;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings({"serial"})
public class KeycloakMappingsResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    List<KeycloakRole> realmMappings;
    List<KeycloakClientMapping> clientMappings;

    @Getter
    @EqualsAndHashCode(callSuper = false)
    @ToString(callSuper = true)
    public static final class KeycloakRole {
        String id;
        String name;
        String description;
        Boolean scopeParamRequired;
        Boolean composite;

        @JsonIgnore
        Object composites;

        Boolean clientRole;
        String containerId;

        @JsonIgnore
        Object attributes;
    }

    @Getter
    @EqualsAndHashCode(callSuper = false)
    @ToString(callSuper = true)
    public static final class KeycloakClientMapping {
        String id;
        String client;
        List<KeycloakRole> mappings;
    }
}
