/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AdminOperationType;
import com.atlan.model.enums.AdminResourceType;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AdminEventRequest extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Unique identifier (GUID) of the client that carried out the operation. */
    String clientId;

    /** IP address from which the operation was carried out. */
    String ipAddress;

    /** Unique name of the realm from which the operation was carried out. */
    String realmId;

    /** Unique identifier (GUID) of the user who carried out the operation. */
    String userId;

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

    /** Include events only with the supplied types of operations. */
    @Singular
    List<AdminOperationType> operationTypes;

    /** Include events only against the supplied resource. */
    String resourcePath;

    /** Include events only against the supplied types of resources. */
    @Singular
    List<AdminResourceType> resourceTypes;

    /**
     * Search for admin events using the filters specified in this request object.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the admin events
     * @return the filtered admin events
     * @throws AtlanException on any issues interacting with the APIs
     */
    public AdminEventResponse search(AtlanClient client) throws AtlanException {
        return client.logs.getAdminEvents(this);
    }
}
