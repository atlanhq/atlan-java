/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Structure of a response to a field in a form.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings({"cast", "serial"})
public class ResponseValue extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ResponseValue";

    /** Fixed typeName for ResponseValue. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Unique identifier of the field in a form. */
    String responseFieldId;

    /** Response type 'string'. */
    String responseValueString;

    /** Response type 'int'. */
    Integer responseValueInt;

    /** Response type 'boolean'. */
    Boolean responseValueBoolean;

    /** Response type 'json'. */
    String responseValueJson;

    /** Response type 'long'. */
    Long responseValueLong;

    /** Response type 'date'. */
    Long responseValueDate;

    /** Response type array of 'string'. */
    @Singular("addResponseValueString")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<String> responseValueArrString;

    /** Response type array of 'int'. */
    @Singular("addResponseValueInt")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<Integer> responseValueArrInt;

    /** Response type array of 'boolean'. */
    @Singular("addResponseValueBoolean")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<Boolean> responseValueArrBoolean;

    /** Response type array of 'json'. */
    @Singular("addResponseValueJson")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<String> responseValueArrJson;

    /** Response type array of 'long'. */
    @Singular("addResponseValueLong")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<Long> responseValueArrLong;

    /** Response type array of 'date'. */
    @Singular("addResponseValueDate")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<Long> responseValueArrDate;

    /** Options of the response to a field in a form. */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    Map<String, String> responseValueOptions;

    /**
     * Quickly create a new ResponseValue.
     * @param responseFieldId Unique identifier of the field in a form.
     * @param responseValueString Response type 'string'.
     * @param responseValueInt Response type 'int'.
     * @param responseValueBoolean Response type 'boolean'.
     * @param responseValueJson Response type 'json'.
     * @param responseValueLong Response type 'long'.
     * @param responseValueDate Response type 'date'.
     * @param responseValueArrString Response type array of 'string'.
     * @param responseValueArrInt Response type array of 'int'.
     * @param responseValueArrBoolean Response type array of 'boolean'.
     * @param responseValueArrJson Response type array of 'json'.
     * @param responseValueArrLong Response type array of 'long'.
     * @param responseValueArrDate Response type array of 'date'.
     * @param responseValueOptions Options of the response to a field in a form.
     * @return a ResponseValue with the provided information
     */
    public static ResponseValue of(
            String responseFieldId,
            String responseValueString,
            Integer responseValueInt,
            Boolean responseValueBoolean,
            String responseValueJson,
            Long responseValueLong,
            Long responseValueDate,
            List<String> responseValueArrString,
            List<Integer> responseValueArrInt,
            List<Boolean> responseValueArrBoolean,
            List<String> responseValueArrJson,
            List<Long> responseValueArrLong,
            List<Long> responseValueArrDate,
            Map<String, String> responseValueOptions) {
        return ResponseValue.builder()
                .responseFieldId(responseFieldId)
                .responseValueString(responseValueString)
                .responseValueInt(responseValueInt)
                .responseValueBoolean(responseValueBoolean)
                .responseValueJson(responseValueJson)
                .responseValueLong(responseValueLong)
                .responseValueDate(responseValueDate)
                .responseValueArrString(responseValueArrString)
                .responseValueArrInt(responseValueArrInt)
                .responseValueArrBoolean(responseValueArrBoolean)
                .responseValueArrJson(responseValueArrJson)
                .responseValueArrLong(responseValueArrLong)
                .responseValueArrDate(responseValueArrDate)
                .responseValueOptions(responseValueOptions)
                .build();
    }

    public abstract static class ResponseValueBuilder<C extends ResponseValue, B extends ResponseValueBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
