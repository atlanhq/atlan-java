/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.model.typedefs.EntityDef;
import com.atlan.model.typedefs.EnumDef;
import com.atlan.model.typedefs.RelationshipDef;
import com.atlan.model.typedefs.StructDef;
import freemarker.template.Template;
import java.io.*;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModelGenerator extends AbstractGenerator {

    public ModelGenerator(GeneratorConfig cfg) {
        super(cfg);
    }

    @Override
    public void generate() throws Exception {
        generateEnums();
        generateStructs();
        generateAssets();
        generateRelationships();
    }

    private void generateEnums() throws Exception {
        Template enumTemplate = ftl.getTemplate("enum.ftl");
        for (EnumDef enumDef : cache.getEnumDefCache().values()) {
            EnumGenerator generator = new EnumGenerator(enumDef, cfg);
            if (cfg.includeTypedef(enumDef)) {
                createDirectoryIdempotent(cfg.getPackagePath() + File.separator + EnumGenerator.DIRECTORY);
                String filename = cfg.getPackagePath() + File.separator + EnumGenerator.DIRECTORY + File.separator
                        + generator.getClassName() + ".java";
                try (BufferedWriter fs = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
                    enumTemplate.process(generator, fs);
                    cache.addEnumGenerator(enumDef.getName(), generator);
                } catch (IOException e) {
                    log.error("Unable to open file output: {}", filename, e);
                }
            } else {
                cache.addEnumGenerator(enumDef.getName(), generator);
            }
        }
    }

    private void generateStructs() throws Exception {
        Template structTemplate = ftl.getTemplate("struct.ftl");
        for (StructDef structDef : cache.getStructDefCache().values()) {
            StructGenerator generator = new StructGenerator(structDef, cfg);
            createDirectoryIdempotent(cfg.getPackagePath() + File.separator + StructGenerator.DIRECTORY);
            if (cfg.includeTypedef(structDef)) {
                String filename = cfg.getPackagePath() + File.separator + StructGenerator.DIRECTORY + File.separator
                        + generator.getClassName() + ".java";
                try (BufferedWriter fs = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
                    structTemplate.process(generator, fs);
                    cache.addStructGenerator(structDef.getName(), generator);
                } catch (IOException e) {
                    log.error("Unable to open file output: {}", filename, e);
                }
            } else {
                cache.addStructGenerator(structDef.getName(), generator);
            }
        }
        if (cfg.getPackageRoot().equals("com.atlan.model")) {
            Template abstractStructTemplate = ftl.getTemplate("AtlanStruct.ftl");
            createDirectoryIdempotent(cfg.getPackagePath() + File.separator + StructGenerator.DIRECTORY);
            String filename = cfg.getPackagePath() + File.separator + StructGenerator.DIRECTORY + File.separator
                    + "AtlanStruct.java";
            try (BufferedWriter fs = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
                ListGenerator generator = new ListGenerator(cache.getStructNames(), cfg.getGeneratorName());
                abstractStructTemplate.process(generator, fs);
            } catch (IOException e) {
                log.error("Unable to open file output: {}", filename, e);
            }
        }
    }

    private void generateAssets() throws Exception {
        // In the first pass, only cache the class names and the un-resolved generators
        // (need all class names resolved first, since they may all reference each other
        // in their resolved details)
        for (EntityDef entityDef : cache.getEntityDefCache().values()) {
            AssetGenerator generator = new AssetGenerator(entityDef, cfg);
            cache.addAssetGenerator(entityDef.getName(), generator);
        }
        // Then create an interface and class for every asset type
        Template interfaceTemplate = ftl.getTemplate("entity_interface.ftl");
        Template entityTemplate = ftl.getTemplate("entity.ftl");
        for (EntityDef entityDef : cache.getEntityDefCache().values()) {
            if (cfg.includeTypedef(entityDef)) {
                AssetGenerator generator = cache.getAssetGenerator(entityDef.getName());
                // Now that all are cached, render the inner details of the generator
                // before processing the template
                generator.resolveDetails();
                createDirectoryIdempotent(cfg.getPackagePath() + File.separator + AssetGenerator.DIRECTORY);
                String fInterface = cfg.getPackagePath() + File.separator + AssetGenerator.DIRECTORY + File.separator
                        + "I" + generator.getClassName() + ".java";
                try (BufferedWriter fs = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(fInterface), StandardCharsets.UTF_8))) {
                    interfaceTemplate.process(generator, fs);
                } catch (IOException e) {
                    log.error("Unable to open file output: {}", fInterface, e);
                }
                if (generator.getOriginalName().equals("Asset") || !generator.isAbstract()) {
                    // Only generate classes for non-abstract assets (leave the rest as interfaces)
                    String fClass = cfg.getPackagePath() + File.separator + AssetGenerator.DIRECTORY + File.separator
                            + generator.getClassName() + ".java";
                    try (BufferedWriter fs = new BufferedWriter(
                            new OutputStreamWriter(new FileOutputStream(fClass), StandardCharsets.UTF_8))) {
                        entityTemplate.process(generator, fs);
                    } catch (IOException e) {
                        log.error("Unable to open file output: {}", fClass, e);
                    }
                }
            }
        }
        if (cfg.getPackageRoot().equals("com.atlan.model")) {
            // Inject all these generated assets into the AttributeDefOptions class (regenerate it)
            Template attributeDefOptionsTemplate = ftl.getTemplate("AttributeDefOptions.ftl");
            String directory = "sdk" + File.separator
                    + "src" + File.separator
                    + "main" + File.separator
                    + "java" + File.separator
                    + "com" + File.separator
                    + "atlan" + File.separator
                    + "model" + File.separator
                    + "typedefs";
            createDirectoryIdempotent(directory);
            String filename = directory + File.separator + "AttributeDefOptions.java";
            try (BufferedWriter fs = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
                // Now that all are generated, output the generated switch-based deserialization
                SerdeGenerator generator = new SerdeGenerator(cache.getAssetGenerators(), cfg);
                attributeDefOptionsTemplate.process(generator, fs);
            } catch (IOException e) {
                log.error("Unable to open file output: {}", filename, e);
            }
        }
    }

    private void generateRelationships() throws Exception {
        Template relationshipTemplate = ftl.getTemplate("relationship.ftl");
        for (RelationshipDef relationshipDef : cache.getRelationshipDefCache().values()) {
            RelationshipGenerator generator = new RelationshipGenerator(relationshipDef, cfg);
            createDirectoryIdempotent(cfg.getPackagePath() + File.separator + RelationshipGenerator.DIRECTORY);
            // Only generate relationships model if there are any attributes on the relationship
            if (cfg.includeTypedef(relationshipDef)
                    && relationshipDef.getAttributeDefs() != null
                    && !relationshipDef.getAttributeDefs().isEmpty()) {
                String filename = cfg.getPackagePath() + File.separator + RelationshipGenerator.DIRECTORY
                        + File.separator + generator.getClassName() + ".java";
                try (BufferedWriter fs = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
                    relationshipTemplate.process(generator, fs);
                    cache.addRelationshipGenerator(relationshipDef.getName(), generator);
                } catch (IOException e) {
                    log.error("Unable to open file output: {}", filename, e);
                }
            } else {
                cache.addRelationshipGenerator(relationshipDef.getName(), generator);
            }
        }
    }
}
