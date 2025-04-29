/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum AssetSidebarTab implements AtlanEnum {
    OVERVIEW("Overview"),
    COLUMNS("Columns"),
    RUNS("Runs"),
    TASKS("Tasks"),
    COMPONENTS("Components"),
    PROJECTS("Projects"),
    COLLECTIONS("Collections"),
    USAGE("Usage"),
    OBJECTS("Objects"),
    LINEAGE("Lineage"),
    INCIDENTS("Incidents"),
    FIELDS("Fields"),
    VISUALS("Visuals"),
    VISUALIZATIONS("Visualizations"),
    SCHEMA_OBJECTS("Schema Objects"),
    RELATIONS("Relations"),
    FACT_DIM_RELATIONS("Fact-Dim Relations"),
    PROFILE("Profile"),
    ASSETS("Assets"),
    ACTIVITY("Activity"),
    SCHEDULES("Schedules"),
    RESOURCES("Resources"),
    QUERIES("Queries"),
    REQUESTS("Requests"),
    PROPERTIES("Properties"),
    MONTE_CARLO("Monte Carlo"),
    DBT_TEST("dbt Test"),
    SODA("Soda"),
    ANOMALO("Anomalo"),
    LAYERS("Layers"),
    PARTITIONS("Partitions"),
    ATTRIBUTES("Attributes"),
    UNMAPPED("(unmapped)");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AssetSidebarTab(String value) {
        this.value = value;
    }

    public static AssetSidebarTab fromValue(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        for (AssetSidebarTab b : AssetSidebarTab.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        log.warn("Found an unmapped sidebar tab, please raise a ticket to get this added: {}", value);
        return UNMAPPED;
    }
}
