package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

public enum AtlanWorkflowPhase implements AtlanEnum {
    @SerializedName("Succeeded")
    SUCCESS("Succeeded"),

    @SerializedName("Running")
    RUNNING("Running"),

    @SerializedName("Failed")
    FAILED("Failed"),

    @SerializedName("Error")
    ERROR("Error"),

    @SerializedName("Pending")
    PENDING("Pending");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanWorkflowPhase(String value) {
        this.value = value;
    }

    public static AtlanWorkflowPhase fromValue(String value) {
        for (AtlanWorkflowPhase b : AtlanWorkflowPhase.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
