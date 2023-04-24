/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.Atlan;
import com.atlan.api.TypeDefsEndpoint;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.typedefs.EntityDef;
import com.atlan.model.typedefs.EnumDef;
import com.atlan.model.typedefs.StructDef;
import com.atlan.model.typedefs.TypeDefResponse;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModelGeneratorV2 {

    static {
        Atlan.setBaseUrl(System.getenv("ATLAN_BASE_URL"));
        Atlan.setApiToken(System.getenv("ATLAN_API_KEY"));
    }

    private static final String TEMPLATES_DIRECTORY =
            "" + "src" + File.separator + "liveTest" + File.separator + "resources" + File.separator + "templates";

    private static final Map<String, EnumGenerator> enumCache = new HashMap<>();
    private static final Map<String, StructGenerator> structCache = new HashMap<>();
    private static final Map<String, AssetGenerator> assetCache = new HashMap<>();

    public static void main(String[] args) throws Exception {

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setDirectoryForTemplateLoading(new File(TEMPLATES_DIRECTORY));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
        cfg.setSQLDateAndTimeTimeZone(TimeZone.getDefault());

        generateEnums(cfg);
        generateStructs(cfg);
        generateAssets(cfg);
    }

    private static void generateEnums(Configuration cfg) throws Exception {
        TypeDefResponse enums = TypeDefsEndpoint.getTypeDefs(AtlanTypeCategory.ENUM);
        if (enums != null && enums.getEnumDefs() != null) {
            Template enumTemplate = cfg.getTemplate("enum.ftl");
            for (EnumDef enumDef : enums.getEnumDefs()) {
                EnumGenerator generator = new EnumGenerator(enumDef);
                String filename = EnumGenerator.DIRECTORY + File.separator + generator.getClassName() + ".java";
                try (BufferedWriter fs = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
                    enumTemplate.process(generator, fs);
                    enumCache.put(enumDef.getName(), generator);
                } catch (IOException e) {
                    log.error("Unable to open file output: {}", filename, e);
                }
            }
        }
    }

    private static void generateStructs(Configuration cfg) throws Exception {
        TypeDefResponse structs = TypeDefsEndpoint.getTypeDefs(AtlanTypeCategory.STRUCT);
        if (structs != null && structs.getStructDefs() != null) {
            Template structTemplate = cfg.getTemplate("struct.ftl");
            for (StructDef structDef : structs.getStructDefs()) {
                StructGenerator generator = new StructGenerator(structDef);
                String filename = StructGenerator.DIRECTORY + File.separator + generator.getClassName() + ".java";
                try (BufferedWriter fs = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
                    structTemplate.process(generator, fs);
                    structCache.put(structDef.getName(), generator);
                } catch (IOException e) {
                    log.error("Unable to open file output: {}", filename, e);
                }
            }
        }
    }

    private static void generateAssets(Configuration cfg) throws Exception {
        TypeDefResponse entities = TypeDefsEndpoint.getTypeDefs(AtlanTypeCategory.ENTITY);
        if (entities != null && entities.getEntityDefs() != null) {
            // In the first pass, only cache the class names and the un-resolved generators
            // (need all class names resolved first, since they may all reference each other
            // in their resolved details)
            for (EntityDef entityDef : entities.getEntityDefs()) {
                AssetGenerator generator = new AssetGenerator(entityDef);
                assetCache.put(entityDef.getName(), generator);
            }
        }
        Template entityTemplate = cfg.getTemplate("entity.ftl");
        for (AssetGenerator generator : assetCache.values()) {
            String filename = AssetGenerator.DIRECTORY + File.separator + generator.getClassName() + ".java";
            try (BufferedWriter fs = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
                // Now that all are cached, render the inner details of the generator
                // before processing the template
                generator.resolveDetails();
                entityTemplate.process(generator, fs);
            } catch (IOException e) {
                log.error("Unable to open file output: {}", filename, e);
            }
        }
    }

    public static TypeGenerator.MappedType getCachedType(String typeName) {
        if (enumCache.containsKey(typeName)) {
            return TypeGenerator.MappedType.builder()
                    .type(TypeGenerator.MappedType.Type.ENUM)
                    .name(enumCache.get(typeName).getClassName())
                    .build();
        } else if (structCache.containsKey(typeName)) {
            return TypeGenerator.MappedType.builder()
                    .type(TypeGenerator.MappedType.Type.STRUCT)
                    .name(structCache.get(typeName).getClassName())
                    .build();
        } else if (assetCache.containsKey(typeName)) {
            return TypeGenerator.MappedType.builder()
                    .type(TypeGenerator.MappedType.Type.ASSET)
                    .name(assetCache.get(typeName).getClassName())
                    .build();
        }
        return null;
    }

    public static String getTemplateFile(String className) {
        File file = new File(TEMPLATES_DIRECTORY + File.separator + className + ".ftl");
        return file.isFile() ? file.getName() : null;
    }
}
