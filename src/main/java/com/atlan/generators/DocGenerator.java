/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.atlan.model.typedefs.*;
import freemarker.template.Template;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

@Slf4j
public class DocGenerator extends AbstractGenerator {

    public DocGenerator(GeneratorConfig cfg) {
        super(cfg);
    }

    @Override
    public void generate() throws Exception {
        generateAssetDocs();
        generateEnumDocs();
        generateStructDocs();
        generateFullModelDiagram();
        generateAttributeCSV();
    }

    private void generateAssetDocs() throws Exception {
        Template docTemplate = ftl.getTemplate("asset_doc.ftl");
        Template javaPropertySnippetTemplate = ftl.getTemplate("snippet_java_properties.ftl");
        Template javaRelationshipSnippetTemplate = ftl.getTemplate("snippet_java_relationships.ftl");
        Template rawPropertySnippetTemplate = ftl.getTemplate("snippet_raw_properties.ftl");
        Template rawRelationshipSnippetTemplate = ftl.getTemplate("snippet_raw_relationships.ftl");
        for (EntityDef entityDef : cache.getEntityDefCache().values()) {
            if (cfg.includeTypedef(entityDef)) {
                AssetGenerator assetGen = cache.getAssetGenerator(entityDef.getName());
                AssetDocGenerator generator = new AssetDocGenerator(assetGen, cfg);
                // Now that all are cached, render the inner details of the generator
                // before processing the template
                generator.resolveDetails();
                String originalName = generator.getOriginalName().toLowerCase(Locale.ROOT);
                // First the overall asset file
                createDirectoryIdempotent(AssetDocGenerator.DIRECTORY);
                String filename = AssetDocGenerator.DIRECTORY + File.separator + originalName + ".md";
                try (BufferedWriter fs = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
                    docTemplate.process(generator, fs);
                } catch (IOException e) {
                    log.error("Unable to open file output: {}", filename, e);
                }
                // Then the snippets
                String javaSnippets = AssetDocGenerator.DIRECTORY + File.separator + "snippets" + File.separator
                        + "model" + File.separator + "java";
                createDirectoryIdempotent(javaSnippets);
                filename = javaSnippets + File.separator + originalName + "-properties.md";
                try (BufferedWriter fs = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
                    javaPropertySnippetTemplate.process(generator, fs);
                } catch (IOException e) {
                    log.error("Unable to open file output: {}", filename, e);
                }
                filename = javaSnippets + File.separator + originalName + "-relationships.md";
                try (BufferedWriter fs = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
                    javaRelationshipSnippetTemplate.process(generator, fs);
                } catch (IOException e) {
                    log.error("Unable to open file output: {}", filename, e);
                }
                String rawSnippets = AssetDocGenerator.DIRECTORY + File.separator + "snippets" + File.separator
                        + "model" + File.separator + "raw";
                createDirectoryIdempotent(rawSnippets);
                filename = rawSnippets + File.separator + originalName + "-properties.md";
                try (BufferedWriter fs = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
                    rawPropertySnippetTemplate.process(generator, fs);
                } catch (IOException e) {
                    log.error("Unable to open file output: {}", filename, e);
                }
                filename = rawSnippets + File.separator + originalName + "-relationships.md";
                try (BufferedWriter fs = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
                    rawRelationshipSnippetTemplate.process(generator, fs);
                } catch (IOException e) {
                    log.error("Unable to open file output: {}", filename, e);
                }
            }
        }
    }

    private void generateEnumDocs() throws Exception {
        Template docTemplate = ftl.getTemplate("enum_doc.ftl");
        for (EnumDef enumDef : cache.getEnumDefCache().values()) {
            EnumGenerator enumGen = new EnumGenerator(enumDef, cfg);
            String originalName = enumGen.getOriginalName().toLowerCase(Locale.ROOT);
            // For enums there is only one doc file to generate
            createDirectoryIdempotent(AssetDocGenerator.DIRECTORY);
            String filename = AssetDocGenerator.DIRECTORY + File.separator + originalName + ".md";
            try (BufferedWriter fs = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
                docTemplate.process(enumGen, fs);
            } catch (IOException e) {
                log.error("Unable to open file output: {}", filename, e);
            }
        }
    }

    private void generateStructDocs() throws Exception {
        Template docTemplate = ftl.getTemplate("struct_doc.ftl");
        Template javaPropertySnippetTemplate = ftl.getTemplate("snippet_java_properties_struct.ftl");
        Template rawPropertySnippetTemplate = ftl.getTemplate("snippet_raw_properties_struct.ftl");
        for (StructDef structDef : cache.getStructDefCache().values()) {
            StructGenerator structGen = new StructGenerator(structDef, cfg);
            String originalName = structGen.getOriginalName().toLowerCase(Locale.ROOT);
            // First the overall struct file
            createDirectoryIdempotent(AssetDocGenerator.DIRECTORY);
            String filename = AssetDocGenerator.DIRECTORY + File.separator + originalName + ".md";
            try (BufferedWriter fs = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
                docTemplate.process(structGen, fs);
            } catch (IOException e) {
                log.error("Unable to open file output: {}", filename, e);
            }
            // Then the snippets
            String javaSnippets = AssetDocGenerator.DIRECTORY + File.separator + "snippets" + File.separator + "model"
                    + File.separator + "java";
            createDirectoryIdempotent(javaSnippets);
            filename = javaSnippets + File.separator + originalName + "-properties.md";
            try (BufferedWriter fs = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
                javaPropertySnippetTemplate.process(structGen, fs);
            } catch (IOException e) {
                log.error("Unable to open file output: {}", filename, e);
            }
            String rawSnippets = AssetDocGenerator.DIRECTORY + File.separator + "snippets" + File.separator + "model"
                    + File.separator + "raw";
            createDirectoryIdempotent(rawSnippets);
            filename = rawSnippets + File.separator + originalName + "-properties.md";
            try (BufferedWriter fs = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
                rawPropertySnippetTemplate.process(structGen, fs);
            } catch (IOException e) {
                log.error("Unable to open file output: {}", filename, e);
            }
        }
    }

    private void generateFullModelDiagram() throws Exception {
        Template modelTemplate = ftl.getTemplate("full_model.ftl");
        AssetGenerator referenceable = cache.getAssetGenerator("Referenceable");
        AssetDocGenerator generator = new AssetDocGenerator(referenceable, cfg);
        // Now that all are cached, render the inner details of the generator
        // before processing the template
        generator.resolveDetails();
        createDirectoryIdempotent(AssetDocGenerator.DIRECTORY);
        String filename = AssetDocGenerator.DIRECTORY + File.separator + "index.md";
        try (BufferedWriter fs =
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
            modelTemplate.process(generator, fs);
        } catch (IOException e) {
            log.error("Unable to open file output: {}", filename, e);
        }
    }

    private void generateAttributeCSV() {
        try (CSVPrinter printer = new CSVPrinter(
                Files.newBufferedWriter(Paths.get(AttributeCSVCache.DESCRIPTIONS_FILE), UTF_8),
                CSVFormat.DEFAULT
                        .builder()
                        .setHeader(AttributeCSVCache.CSV_HEADER)
                        .setRecordSeparator("\n")
                        .build())) {
            List<String> sortedTypeNames =
                    cache.getEntityDefCache().keySet().stream().sorted().collect(Collectors.toList());
            for (String typeName : sortedTypeNames) {
                addModelToCSV(printer, typeName);
            }
            sortedTypeNames = cache.getStructNames();
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
            EntityDef entityDef = cache.getEntityDefCache().get(typeName);
            // Add all the plain attributes first
            String description = addAttributesToCSV(printer, entityDef);
            // And then all the relationship attributes (but only if they are unique to the type and not inherited)
            Set<String> uniqueRelationships = cache.getUniqueRelationshipsForType(typeName);
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
            StructDef structDef = cache.getStructDefCache().get(typeName);
            addAttributesToCSV(printer, structDef);
        }
    }

    private String addAttributesToCSV(CSVPrinter printer, TypeDef typeDef) throws IOException {
        String typeName = typeDef.getName();
        String description = cache.getTypeDescription(typeName);
        // Add all the plain attributes first
        for (AttributeDef attribute : typeDef.getAttributeDefs()) {
            String attrName = attribute.getName();
            if (!attrName.equals("__internal")) {
                printer.printRecord(
                        typeName, description, attribute.getName(), getMergedDescription(typeName, attribute));
            }
        }
        return description;
    }

    private String getMergedDescription(String typeName, AttributeDef attribute) {
        return cache.getAttributeDescription(typeName, attribute.getName());
    }
}
