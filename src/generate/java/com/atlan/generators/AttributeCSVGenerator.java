/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.generators;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.atlan.Atlan;
import com.atlan.api.TypeDefsEndpoint;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.typedefs.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 * Utility class to generate a CSV file containing details of all of the Atlan model's attributes.
 */
@Slf4j
public class AttributeCSVGenerator {

    static {
        Atlan.setBaseUrl(System.getenv("ATLAN_BASE_URL"));
        Atlan.setApiToken(System.getenv("ATLAN_API_KEY"));
    }

    private static final String DOCS_DIRECTORY = "src" + File.separator + "generate" + File.separator + "resources";

    private static final Map<String, EntityDef> entityDefCache = new HashMap<>();
    private static final Map<String, StructDef> structDefCache = new HashMap<>();

    public static void main(String[] args) throws Exception {
        AttributeCSVCache.cacheDescriptions();
        AttributeCSVGenerator generator = new AttributeCSVGenerator();
        List<EntityDef> entityDefs = TypeDefsEndpoint.getTypeDefs(AtlanTypeCategory.ENTITY).getEntityDefs();
        for (EntityDef def : entityDefs) {
            entityDefCache.put(def.getName(), def);
        }
        List<StructDef> structDefs = TypeDefsEndpoint.getTypeDefs(AtlanTypeCategory.STRUCT).getStructDefs();
        for (StructDef def : structDefs) {
            structDefCache.put(def.getName(), def);
        }
        ModelGeneratorV2.cacheRelationshipsForInheritance(entityDefs);
        generator.createDirectoryIdempotent();
        generator.generateAttributeCSV();
    }

    private void createDirectoryIdempotent() {
        // First ensure the target directory has been created / exists
        File dir = new File(DOCS_DIRECTORY);
        if (!dir.exists()) {
            log.info("Creating directory: " + DOCS_DIRECTORY);
            if (!dir.mkdirs()) {
                log.error("Unable to create target directory: {}", DOCS_DIRECTORY);
            }
        }
    }

    private void generateAttributeCSV() {
        try (CSVPrinter printer = new CSVPrinter(
                Files.newBufferedWriter(Paths.get(DOCS_DIRECTORY + File.separator + "attributes.csv"), UTF_8),
                CSVFormat.DEFAULT.builder().setHeader(AttributeCSVCache.CSV_HEADER).setRecordSeparator("\n").build())) {
            List<String> sortedTypeNames = entityDefCache.keySet().stream().sorted().collect(Collectors.toList());
            for (String typeName : sortedTypeNames) {
                addModelToCSV(printer, typeName);
            }
            sortedTypeNames = structDefCache.keySet().stream().sorted().collect(Collectors.toList());
            for (String typeName : sortedTypeNames) {
                addStructToCSV(printer, typeName);
            }
        } catch (IOException e) {
            log.error("Unable to create attributes CSV file as expected.", e);
            System.exit(1);
        }
    }

    private void addModelToCSV(CSVPrinter printer, String typeName) throws IOException {
        if (!typeName.startsWith("__")) {
            EntityDef entityDef = entityDefCache.get(typeName);
            // Add all the plain attributes first
            String description = addAttributesToCSV(printer, entityDef);
            // And then all the relationship attributes (but only if they are unique to the type and not inherited)
            Set<String> uniqueRelationships = ModelGeneratorV2.getUniqueRelationshipsForType(typeName);
            for (RelationshipAttributeDef relationship : entityDef.getRelationshipAttributeDefs()) {
                String name = relationship.getName();
                if (!name.equals("__internal")) {
                    if (uniqueRelationships.contains(name)) {
                        printer.printRecord(typeName, description, name, getMergedDescription(typeName, relationship));
                    }
                }
            }
        }
    }

    private void addStructToCSV(CSVPrinter printer, String typeName) throws IOException {
        if (!typeName.startsWith("__")) {
            StructDef structDef = structDefCache.get(typeName);
            addAttributesToCSV(printer, structDef);
        }
    }

    private String addAttributesToCSV(CSVPrinter printer, TypeDef typeDef) throws IOException {
        String typeName = typeDef.getName();
        String description = AttributeCSVCache.getTypeDescription(typeName);
        if (description.equals(AttributeCSVCache.DEFAULT_CLASS_DESCRIPTION)) {
            description = typeDef.getDescription();
        }
        // Add all the plain attributes first
        for (AttributeDef attribute : typeDef.getAttributeDefs()) {
            String attrName = attribute.getName();
            if (!attrName.equals("__internal")) {
                printer.printRecord(typeName, description, attribute.getName(), getMergedDescription(typeName, attribute));
            }
        }
        return description;
    }

    private String getMergedDescription(String typeName, AttributeDef attribute) {
        String attrDescription = AttributeCSVCache.getAttributeDescription(typeName, attribute.getName());
        return attrDescription.equals(AttributeCSVCache.DEFAULT_ATTR_DESCRIPTION) ? attribute.getDescription() : attrDescription;
    }
}
