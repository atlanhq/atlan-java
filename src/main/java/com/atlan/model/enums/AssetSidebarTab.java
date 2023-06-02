/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AssetSidebarTab implements AtlanEnum {
    INCIDENTS("Incidents"),
    VISUALS("Visuals"),
    COLUMNS("Columns"),
    RUNS("Runs"),
    TASKS("Tasks"),
    USAGE("Usage"),
    OBJECTS("Objects"),
    LINEAGE("Lineage"),
    FIELDS("Fields"),
    VISUALIZATIONS("Visualizations"),
    RELATIONS("Relations"),
    PROFILE("Profile"),
    ASSETS("Assets"),
    ACTIVITY("Activity"),
    SCHEDULES("Schedules"),
    RESOURCES("Resources"),
    QUERIES("Queries"),
    REQUESTS("Requests"),
    PROPERTIES("Properties"),
    MONTE_CARLO("Monte Carlo");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AssetSidebarTab(String value) {
        this.value = value;
    }

    public static AssetSidebarTab fromValue(String value) {
        for (AssetSidebarTab b : AssetSidebarTab.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
