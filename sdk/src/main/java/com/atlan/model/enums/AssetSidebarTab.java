/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

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
    PARTITIONS("Partitions");

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
