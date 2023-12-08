/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum UTMTags implements AtlanEnum {
    /* PAGE_ entries indicate where the action was taken. */
    /** Search was made from the home page. */
    PAGE_HOME("page_home"),
    /** Search was made from the assets (discovery) page. */
    PAGE_ASSETS("page_assets"),
    /** Asset was viewed from within a glossary. */
    PAGE_GLOSSARY("page_glossary"),
    /** Asset was viewed from within insights. */
    PAGE_INSIGHTS("page_insights"),

    /* PROJECT_ entries indicate how (via what application) the action was taken. */
    /** Search was made via the webapp (UI). */
    PROJECT_WEBAPP("project_webapp"),
    /** Search was made via the Java SDK. */
    PROJECT_SDK_JAVA("project_sdk_java"),
    /** Search was made via the Python SDK. */
    PROJECT_SDK_PYTHON("project_sdk_python"),

    /* ACTION_ entries dictate the specific action that was taken. */
    /** Assets were searched. */
    ACTION_SEARCHED("action_searched"),
    /** Search was run through the Cmd-K popup. */
    ACTION_CMD_K("action_cmd_k"),
    /** Search was through changing a filter in the UI (discovery). */
    ACTION_FILTER_CHANGED("action_filter_changed"),
    /** Search was through changing a type filter (pill) in the UI (discovery). */
    ACTION_ASSET_TYPE_CHANGED("action_asset_type_changed"),
    /** Asset was viewed, rather than an explicit search. */
    ACTION_ASSET_VIEWED("action_asset_viewed"),

    /* Others indicate any special mechanisms used for the action. */
    /** Search was run using the UI popup searchbar. */
    UI_POPUP_SEARCHBAR("ui_popup_searchbar"),
    /** Search was through a UI filter (discovery). */
    UI_FILTERS("ui_filters"),
    /** View was done via the UI's sidebar. */
    UI_SIDEBAR("ui_sidebar"),
    /** View was done of the full asset profile, not only sidebar. */
    UI_PROFILE("ui_profile"),
    /** Listing of assets, usually by a particular type, in discovery page. */
    UI_MAIN_LIST("ui_main_list"),

/* ASSET_TYPE_... entries indicate the specific kind of asset that was viewed. */
// asset_type_atlasglossaryterm
// asset_type_view
// asset_type___all <-- special case for all types
;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    UTMTags(String value) {
        this.value = value;
    }

    public static UTMTags fromValue(String value) {
        for (UTMTags b : UTMTags.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
