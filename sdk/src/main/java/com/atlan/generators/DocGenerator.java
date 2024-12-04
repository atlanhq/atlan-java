/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.AtlanClient;
import com.atlan.model.typedefs.*;
import freemarker.template.Template;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocGenerator extends AbstractGenerator {

    public DocGenerator(AtlanClient client, GeneratorConfig cfg) {
        super(client, cfg);
    }

    @Override
    public void generate() throws Exception {
        generateAssetDocs();
        generateEnumDocs();
        generateStructDocs();
        generateFullModelDiagram();
    }

    private void generateAssetDocs() throws Exception {
        Template docTemplate = ftl.getTemplate("asset_doc.ftl");
        Template propertySnippetTemplate = ftl.getTemplate("snippet_properties.ftl");
        Template relationshipSnippetTemplate = ftl.getTemplate("snippet_relationships.ftl");
        // Template javaPropertySnippetTemplate = ftl.getTemplate("snippet_java_properties.ftl");
        // Template javaRelationshipSnippetTemplate = ftl.getTemplate("snippet_java_relationships.ftl");
        // Template rawPropertySnippetTemplate = ftl.getTemplate("snippet_raw_properties.ftl");
        // Template rawRelationshipSnippetTemplate = ftl.getTemplate("snippet_raw_relationships.ftl");
        for (EntityDef entityDef : cache.getEntityDefCache().values()) {
            if (cfg.includeTypedef(entityDef)) {
                AssetGenerator assetGen = cache.getAssetGenerator(entityDef.getName());
                AssetDocGenerator generator = new AssetDocGenerator(assetGen, cfg);
                // Now that all are cached, render the inner details of the generator
                // before processing the template
                generator.resolveDetails();
                String originalName = generator.getOriginalName().toLowerCase(Locale.ROOT);
                // First the overall asset file
                String directory = AssetDocGenerator.DIRECTORY + File.separator + "entities";
                createDirectoryIdempotent(directory);
                String filename = directory + File.separator + originalName + ".md";
                try (BufferedWriter fs = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
                    docTemplate.process(generator, fs);
                } catch (IOException e) {
                    log.error("Unable to open file output: {}", filename, e);
                }
                // Then the snippets
                String snippets = AssetDocGenerator.DIRECTORY + File.separator + "snippets" + File.separator + "model";
                createDirectoryIdempotent(snippets);
                filename = snippets + File.separator + originalName + "-properties.md";
                try (BufferedWriter fs = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
                    propertySnippetTemplate.process(generator, fs);
                } catch (IOException e) {
                    log.error("Unable to open file output: {}", filename, e);
                }
                filename = snippets + File.separator + originalName + "-relationships.md";
                try (BufferedWriter fs = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
                    relationshipSnippetTemplate.process(generator, fs);
                } catch (IOException e) {
                    log.error("Unable to open file output: {}", filename, e);
                }
                /*String javaSnippets = AssetDocGenerator.DIRECTORY + File.separator + "snippets" + File.separator
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
                }*/
            }
        }
    }

    private void generateEnumDocs() throws Exception {
        Template docTemplate = ftl.getTemplate("enum_doc.ftl");
        for (EnumDef enumDef : cache.getEnumDefCache().values()) {
            EnumGenerator enumGen = new EnumGenerator(client, enumDef, cfg);
            String originalName = enumGen.getOriginalName().toLowerCase(Locale.ROOT);
            // For enums there is only one doc file to generate
            String directory = AssetDocGenerator.DIRECTORY + File.separator + "enums";
            createDirectoryIdempotent(directory);
            String filename = directory + File.separator + originalName + ".md";
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
        Template propertySnippetTemplate = ftl.getTemplate("snippet_properties_struct.ftl");
        // Template javaPropertySnippetTemplate = ftl.getTemplate("snippet_java_properties_struct.ftl");
        // Template rawPropertySnippetTemplate = ftl.getTemplate("snippet_raw_properties_struct.ftl");
        for (StructDef structDef : cache.getStructDefCache().values()) {
            StructGenerator structGen = new StructGenerator(client, structDef, cfg);
            String originalName = structGen.getOriginalName().toLowerCase(Locale.ROOT);
            // First the overall struct file
            String directory = AssetDocGenerator.DIRECTORY + File.separator + "structs";
            createDirectoryIdempotent(directory);
            String filename = directory + File.separator + originalName + ".md";
            try (BufferedWriter fs = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
                docTemplate.process(structGen, fs);
            } catch (IOException e) {
                log.error("Unable to open file output: {}", filename, e);
            }
            // Then the snippets
            String snippets = AssetDocGenerator.DIRECTORY + File.separator + "snippets" + File.separator + "model";
            createDirectoryIdempotent(snippets);
            filename = snippets + File.separator + originalName + "-properties.md";
            try (BufferedWriter fs = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
                propertySnippetTemplate.process(structGen, fs);
            } catch (IOException e) {
                log.error("Unable to open file output: {}", filename, e);
            }
            /*String javaSnippets = AssetDocGenerator.DIRECTORY + File.separator + "snippets" + File.separator + "model"
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
            }*/
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
}
