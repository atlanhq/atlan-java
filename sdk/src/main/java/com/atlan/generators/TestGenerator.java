/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.AtlanClient;
import com.atlan.model.typedefs.EntityDef;
import freemarker.template.Template;
import java.io.*;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestGenerator extends AbstractGenerator {

    public TestGenerator(AtlanClient client, GeneratorConfig cfg) {
        super(client, cfg);
    }

    @Override
    public void generate() throws Exception {
        generateAssetTests();
    }

    private void generateAssetTests() throws Exception {
        Template testTemplate = ftl.getTemplate("asset_test.ftl");
        for (EntityDef entityDef : cache.getEntityDefCache().values()) {
            if (cfg.includeTypedef(entityDef)) {
                AssetGenerator assetGen = cache.getAssetGenerator(entityDef.getName());
                if (!assetGen.isAbstract()) {
                    AssetTestGenerator generator = new AssetTestGenerator(assetGen, cfg);
                    createDirectoryIdempotent(cfg.getTestPath() + File.separator + AssetTestGenerator.DIRECTORY);
                    String filename = cfg.getTestPath() + File.separator + AssetTestGenerator.DIRECTORY + File.separator
                            + generator.getClassName() + "Test.java";
                    try (BufferedWriter fs = new BufferedWriter(
                            new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
                        // Now that all are cached, render the inner details of the generator
                        // before processing the template
                        generator.resolveDetails();
                        testTemplate.process(generator, fs);
                    } catch (IOException e) {
                        log.error("Unable to open file output: {}", filename, e);
                    }
                }
            }
        }
    }
}
