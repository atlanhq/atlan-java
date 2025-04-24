/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.net.ApiResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings({"cast", "serial"})
public class CredentialResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    String id;
    String version;
    Boolean isActive;
    Long createdAt;
    Long updatedAt;
    String createdBy;
    String tenantId;
    String name;
    String description;
    String connectorConfigName;
    String connector;
    String connectorType;
    String authType;
    String host;
    Integer port;
    Object metadata;
    Object level;
    Object connection;
    String username;

    @JsonProperty("extra")
    Map<String, String> extras;

    /**
     * Convert this response into a credential builder.
     * Note: the username, password, and extras fields must still all be populated, as they
     * will never be returned by a credential lookup (for security reasons).
     *
     * @return builder for this credential
     */
    public Credential.CredentialBuilder<?, ?> toCredential() {
        return Credential.builder()
                .id(id)
                .name(name)
                .host(host)
                .port(port)
                .authType(authType)
                .connectorType(connectorType)
                .connectorConfigName(connectorConfigName);
    }
}
