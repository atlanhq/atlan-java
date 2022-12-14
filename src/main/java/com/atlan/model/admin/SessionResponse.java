/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.net.ApiResource;
import com.atlan.serde.SessionResponseDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@JsonDeserialize(using = SessionResponseDeserializer.class)
@EqualsAndHashCode(callSuper = false)
public class SessionResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    List<Session> sessions;

    public SessionResponse(List<Session> sessions) {
        this.sessions = sessions;
    }
}
