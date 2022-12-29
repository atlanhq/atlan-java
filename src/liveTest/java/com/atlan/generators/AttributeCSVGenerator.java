/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.generators;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.atlan.model.typedefs.AttributeDef;
import com.atlan.model.typedefs.EntityDef;
import com.atlan.model.typedefs.RelationshipAttributeDef;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 * Utility class to generate a CSV file containing details of all of the Atlan model's attributes.
 */
@Slf4j
public class AttributeCSVGenerator extends AbstractGenerator {

    private static final String DOCS_DIRECTORY = "src" + File.separator + "main" + File.separator + "resources";

    public static void main(String[] args) {
        AttributeCSVGenerator generator = new AttributeCSVGenerator();
        cacheModels();
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
                CSVFormat.DEFAULT.builder().setHeader(CSV_HEADER).build())) {
            Set<String> typeNames = typeDefCache.keySet();
            List<String> sortedTypeNames = typeNames.stream().sorted().collect(Collectors.toList());
            for (String typeName : sortedTypeNames) {
                addModelToCSV(printer, typeName);
            }
        } catch (IOException e) {
            log.error("Unable to create attributes CSV file as expected.", e);
            System.exit(1);
        }
    }

    private void addModelToCSV(CSVPrinter printer, String typeName) throws IOException {
        EntityDef entityDef = typeDefCache.get(typeName);
        String description = entityDef.getDescription();
        // Add all the plain attributes first
        for (AttributeDef attribute : entityDef.getAttributeDefs()) {
            printer.printRecord(typeName, description, attribute.getName(), attribute.getDescription());
        }
        // And then all the relationship attributes (but only if they are unique to the type and not inherited)
        Set<String> uniqueRelationships = relationshipsForType.get(typeName);
        for (RelationshipAttributeDef relationship : entityDef.getRelationshipAttributeDefs()) {
            String name = relationship.getName();
            if (uniqueRelationships.contains(name)) {
                printer.printRecord(typeName, description, name, relationship.getDescription());
            }
        }
    }
}
