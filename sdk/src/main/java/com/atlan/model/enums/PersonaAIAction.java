/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum PersonaAIAction implements AtlanEnum, AtlanPolicyAction {
    APP_READ("persona-ai-application-read"),
    APP_CREATE("persona-ai-application-create"),
    APP_UPDATE("persona-ai-application-update"),
    APP_DELETE("persona-ai-application-delete"),
    APP_UPDATE_CM("persona-ai-application-business-update-metadata"),
    APP_ADD_TERMS("persona-ai-application-add-terms"),
    APP_REMOVE_TERMS("persona-ai-application-remove-terms"),
    APP_ADD_ATLAN_TAGS("persona-ai-application-add-classification"),
    APP_REMOVE_ATLAN_TAGS("persona-ai-application-remove-classification"),
    MODEL_READ("persona-ai-model-read"),
    MODEL_CREATE("persona-ai-model-create"),
    MODEL_UPDATE("persona-ai-model-update"),
    MODEL_DELETE("persona-ai-model-delete"),
    MODEL_UPDATE_CM("persona-ai-model-business-update-metadata"),
    MODEL_ADD_TERMS("persona-ai-model-add-terms"),
    MODEL_REMOVE_TERMS("persona-ai-model-remove-terms"),
    MODEL_ADD_ATLAN_TAGS("persona-ai-model-add-classification"),
    MODEL_REMOVE_ATLAN_TAGS("persona-ai-model-remove-classification");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    PersonaAIAction(String value) {
        this.value = value;
    }

    public static PersonaAIAction fromValue(String value) {
        for (PersonaAIAction b : PersonaAIAction.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
