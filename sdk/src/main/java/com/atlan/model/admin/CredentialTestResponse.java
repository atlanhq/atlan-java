/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.net.ApiResource;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class CredentialTestResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    Integer code;
    String error;
    Object info;
    String message;
    String requestId;

    /**
     * Whether the test was successful (true) or failed (false).
     *
     * @return boolean indicating whether the test was successful
     */
    @JsonIgnore
    public boolean isSuccessful() {
        return message != null && message.equals("successful");
    }
}
