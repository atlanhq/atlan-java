/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.generators;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.atlan.Atlan;
import com.atlan.api.TypeDefsEndpoint;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.typedefs.AttributeDef;
import com.atlan.model.typedefs.EntityDef;
import com.atlan.model.typedefs.RelationshipAttributeDef;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.atlan.model.typedefs.TypeDef;
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

    private static Map<String, EntityDef> entityDefCache = new HashMap<>();

    public static void main(String[] args) throws Exception {
        AttributeCSVGenerator generator = new AttributeCSVGenerator();
        List<EntityDef> entityDefs = TypeDefsEndpoint.getTypeDefs(AtlanTypeCategory.ENTITY).getEntityDefs();
        for (EntityDef def : entityDefs) {
            entityDefCache.put(def.getName(), def);
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
                CSVFormat.DEFAULT.builder().setHeader(AttributeCSVCache.CSV_HEADER).build())) {
            List<String> sortedTypeNames = entityDefCache.keySet().stream().sorted().collect(Collectors.toList());
            for (String typeName : sortedTypeNames) {
                addModelToCSV(printer, typeName);
            }
        } catch (IOException e) {
            log.error("Unable to create attributes CSV file as expected.", e);
            System.exit(1);
        }
    }

    private void addModelToCSV(CSVPrinter printer, String typeName) throws IOException {
        EntityDef entityDef = entityDefCache.get(typeName);
        String description = entityDef.getDescription();
        // Add all the plain attributes first
        for (AttributeDef attribute : entityDef.getAttributeDefs()) {
            printer.printRecord(typeName, description, attribute.getName(), attribute.getDescription());
        }
        // And then all the relationship attributes (but only if they are unique to the type and not inherited)
        Set<String> uniqueRelationships = ModelGeneratorV2.getUniqueRelationshipsForType(typeName);
        for (RelationshipAttributeDef relationship : entityDef.getRelationshipAttributeDefs()) {
            String name = relationship.getName();
            if (uniqueRelationships.contains(name)) {
                printer.printRecord(typeName, description, name, relationship.getDescription());
            }
        }
    }
}
