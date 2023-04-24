/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.model.typedefs.EnumDef;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class EnumGenerator extends TypeGenerator {

    public static final String DIRECTORY = ""
            + "src" + File.separator
            + "main" + File.separator
            + "java" + File.separator
            + "com" + File.separator
            + "atlan" + File.separator
            + "model" + File.separator
            + "enums";

    private static final Map<String, String> CLASS_RENAMING = Map.ofEntries(
            Map.entry("google_datastudio_asset_type", "GoogleDataStudioAssetType"),
            Map.entry("powerbi_endorsement", "PowerBIEndorsementType"));

    private static final Map<String, String> VALUE_RENAMING =
            Map.ofEntries(Map.entry("ResolvingDNS", "RESOLVING_DNS"), Map.entry("RA-GRS", "RA_GRS"));

    private static final Pattern WORD_FINDER = Pattern.compile("(([A-Z]?[a-z0-9]+)|([A-Z0-9]))");

    private EnumDef enumDef;
    private List<ValidValue> values;

    public EnumGenerator(EnumDef enumDef) {
        super(enumDef);
        this.enumDef = enumDef;
        resolveClassName();
        super.description = AttributeCSVCache.getTypeDescription(className);
        resolveValues();
    }

    @Override
    protected void resolveClassName() {
        super.className = CLASS_RENAMING.containsKey(originalName)
                ? CLASS_RENAMING.get(originalName)
                : getUpperCamelCase(originalName);
    }

    private void resolveValues() {
        values = new ArrayList<>();
        for (EnumDef.ElementDef elementDef : enumDef.getElementDefs()) {
            values.add(new ValidValue(elementDef.getValue()));
        }
    }

    @Getter
    public static final class ValidValue {

        private final String actualValue;
        private final String enumeratedValue;

        public ValidValue(String actualValue) {
            this.actualValue = actualValue;
            this.enumeratedValue = getEnumName(actualValue);
        }

        private static String getEnumName(String text) {
            if (VALUE_RENAMING.containsKey(text)) {
                return VALUE_RENAMING.get(text);
            } else if (text.toUpperCase().equals(text)) {
                return text;
            }
            String[] words = text.split("[\\W-]+");
            if (words.length == 1) {
                List<String> camelCaseWords = new ArrayList<>();
                Matcher matcher = WORD_FINDER.matcher(text);
                while (matcher.find()) {
                    camelCaseWords.add(matcher.group(0));
                }
                words = camelCaseWords.toArray(new String[0]);
            }
            StringBuilder builder = new StringBuilder();
            for (String word : words) {
                word = word.isEmpty() ? word : word.toUpperCase();
                builder.append(word).append("_");
            }
            String built = builder.toString();
            return built.endsWith("_") ? built.substring(0, built.length() - 1) : built;
        }
    }
}
