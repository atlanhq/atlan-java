/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.KeycloakEventType;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class KeycloakEventRequest extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Application or OAuth client name. */
    String client;

    /** IP address from which the event was triggered. */
    String ipAddress;

    /** Earliest date from which to include events (format: yyyy-MM-dd). */
    String dateFrom;

    /** Latest date up to which to include events (format: yyyy-MM-dd). */
    String dateTo;

    /** Starting point for the events (for paging). */
    @Builder.Default
    int offset = 0;

    /** Maximum number of events to retrieve (per page). */
    @Builder.Default
    int size = 100;

    /** Include events only of the supplied types. */
    @Singular
    List<KeycloakEventType> types;

    /** Unique identifier (GUID) of the user who triggered the event. */
    String userId;

    /**
     * Search for events using the filters specified in this request object.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the events
     * @return the filtered events
     * @throws AtlanException on any issues interacting with the APIs
     */
    public KeycloakEventResponse search(AtlanClient client) throws AtlanException {
        return client.logs.getEvents(this);
    }

    public abstract static class KeycloakEventRequestBuilder<
                    C extends KeycloakEventRequest, B extends KeycloakEventRequestBuilder<C, B>>
            extends AtlanObject.AtlanObjectBuilder<C, B> {}
}
