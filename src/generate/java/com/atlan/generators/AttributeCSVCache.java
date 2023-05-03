/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

@Slf4j
public class AttributeCSVCache {

    private static final String CSV_TYPE_NAME = "Type Name";
    private static final String CSV_TYPE_DESC = "Type Description";
    private static final String CSV_ATTR_NAME = "Attribute Name";
    private static final String CSV_ATTR_DESC = "Attribute Description";

    public static final String[] CSV_HEADER = {CSV_TYPE_NAME, CSV_TYPE_DESC, CSV_ATTR_NAME, CSV_ATTR_DESC};

    static final String DEFAULT_ATTR_DESCRIPTION = "TBC";
    static final String DEFAULT_CLASS_DESCRIPTION = "TBC";

    private static final String DESCRIPTIONS_FILE =
            "" + "src" + File.separator + "generate" + File.separator + "resources" + File.separator + "attributes.csv";

    private static final Map<String, String> qualifiedAttrToDescription = new ConcurrentHashMap<>();
    private static final Map<String, String> typeNameToDescription = new ConcurrentHashMap<>();

    /** Cache all attribute descriptions that are defined in an external CSV file. */
    public static void cacheDescriptions() {
        try (BufferedReader in = Files.newBufferedReader(Paths.get(DESCRIPTIONS_FILE), UTF_8)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .builder()
                    .setHeader(CSV_HEADER)
                    .setSkipHeaderRecord(true)
                    .build()
                    .parse(in);
            for (CSVRecord record : records) {
                String typeName = record.get(CSV_TYPE_NAME);
                String typeDesc = record.get(CSV_TYPE_DESC);
                typeNameToDescription.put(typeName, typeDesc == null || typeDesc.length() == 0 ? "" : typeDesc);
                String attrName = record.get(CSV_ATTR_NAME);
                String attrDesc = record.get(CSV_ATTR_DESC);
                if (attrDesc != null && attrDesc.length() > 0) {
                    // Don't even bother using up memory if there is no description...
                    String attrQN = getAttrQualifiedName(typeName, attrName);
                    qualifiedAttrToDescription.put(attrQN, attrDesc);
                }
            }
        } catch (IOException e) {
            log.error("Unable to access or read descriptions CSV file.", e);
            System.exit(1);
        }
    }

    static String getAttrQualifiedName(String typeName, String attrName) {
        return typeName + "|" + attrName;
    }

    /**
     * Retrieve the description of the type as defined in the external CSV file.
     *
     * @param typeName name of the type for which to obtain a description
     * @return the description for that type
     */
    public static String getTypeDescription(String typeName) {
        if (typeNameToDescription.isEmpty()) {
            cacheDescriptions();
        }
        return typeNameToDescription.getOrDefault(typeName, DEFAULT_CLASS_DESCRIPTION);
    }

    /**
     * Retrieve the description of the attribute as defined in the external CSV file.
     *
     * @param objectName name of the type in which the attribute exists
     * @param attrName name of the attribute within that type
     * @return the description for that attribute, if any, or 'TBC' if none could be found
     */
    public static String getAttributeDescription(String objectName, String attrName) {
        String attrQN = getAttrQualifiedName(objectName, attrName);
        if (qualifiedAttrToDescription.isEmpty()) {
            cacheDescriptions();
        }
        return qualifiedAttrToDescription.getOrDefault(attrQN, DEFAULT_ATTR_DESCRIPTION);
    }
}
