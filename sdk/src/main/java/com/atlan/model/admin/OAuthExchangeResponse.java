/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.net.ApiResource;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class OAuthExchangeResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    /** Bearer token that can be used to authenticate API calls. */
    String accessToken;

    /** Number of seconds after which the bearer token will expire. */
    Long expiresIn;

    /** TBC */
    String scope;

    /** Type of token exchanged. */
    String tokenType;
}
