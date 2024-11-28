/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.core.AtlanObject;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SSOProviderRequest extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Alias for the SSO provider. */
    String alias;

    /** Type of SSO provider (for example, {@code saml}). */
    String providerId;

    /** Whether the provider should be active (true) or not (false). */
    @Builder.Default
    Boolean enabled = true;

    /** TBC */
    Boolean trustEmail;

    /** TBC */
    Boolean storeToken;

    /** TBC */
    Boolean addReadTokenRoleOnCreate;

    /** TBC */
    Boolean linkOnly;

    /** TBC */
    String firstBrokerLoginFlowAlias;

    /** Label to show users for logging in with this provider. */
    String displayName;

    /** Configuration for the provider. */
    Config config;

    @Getter
    @Jacksonized
    @Builder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class Config extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** TBC */
        String nameIDPolicyFormat;

        /** TBC */
        String postBindingAuthnRequest;

        /** TBC */
        String postBindingResponse;

        /** TBC */
        String principalType;

        /** TBC */
        String syncMode;

        /** TBC */
        String singleSignOnServiceUrl;

        /** TBC */
        String signingCertificate;
    }
}
