/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.atlan.model.enums.FormFieldDimension;
import com.atlan.model.enums.FormFieldType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import java.util.Map;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Structure of a field in a form.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings({"cast", "serial"})
public class FormField extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "FormField";

    /** Fixed typeName for FormField. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Unique identifier of the field in a form. */
    String formFieldId;

    /** Name of the field in a form. */
    String formFieldName;

    /** Type of the field in a form. */
    FormFieldType formFieldType;

    /** Dimension of the field's value in a form. */
    FormFieldDimension formFieldDimension;

    /** Options of the field in a form. */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    Map<String, String> formFieldOptions;

    /**
     * Quickly create a new FormField.
     * @param formFieldId Unique identifier of the field in a form.
     * @param formFieldName Name of the field in a form.
     * @param formFieldType Type of the field in a form.
     * @param formFieldDimension Dimension of the field's value in a form.
     * @param formFieldOptions Options of the field in a form.
     * @return a FormField with the provided information
     */
    public static FormField of(
            String formFieldId,
            String formFieldName,
            FormFieldType formFieldType,
            FormFieldDimension formFieldDimension,
            Map<String, String> formFieldOptions) {
        return FormField.builder()
                .formFieldId(formFieldId)
                .formFieldName(formFieldName)
                .formFieldType(formFieldType)
                .formFieldDimension(formFieldDimension)
                .formFieldOptions(formFieldOptions)
                .build();
    }
}
