/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.AtlanClient;
import com.atlan.model.typedefs.EnumDef;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class EnumGenerator extends TypeGenerator {

    public static final String DIRECTORY = "enums";

    private final EnumDef enumDef;
    private List<ValidValue> values;

    @SuppressWarnings("this-escape")
    public EnumGenerator(AtlanClient client, EnumDef enumDef, GeneratorConfig cfg) {
        super(client, enumDef, cfg);
        this.enumDef = enumDef;
        resolveClassName();
        super.description = cache.getTypeDescription(className);
        resolveValues();
    }

    @Override
    protected void resolveClassName() {
        super.className = cfg.resolveClassName(getOriginalName());
    }

    private void resolveValues() {
        values = new ArrayList<>();
        for (EnumDef.ElementDef elementDef : enumDef.getElementDefs()) {
            values.add(new ValidValue(elementDef.getValue()));
        }
    }

    @Getter
    public final class ValidValue {

        private final String actualValue;
        private final String enumeratedValue;

        public ValidValue(String actualValue) {
            this.actualValue = actualValue;
            this.enumeratedValue = getEnumName(actualValue);
        }

        private String getEnumName(String text) {
            return cfg.resolveEnumValue(text);
        }
    }
}
