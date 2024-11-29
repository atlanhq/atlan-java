/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.AtlanClient;
import freemarker.template.Configuration;
import java.io.File;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractGenerator {

    protected final AtlanClient client;
    protected final ModelCache cache;

    protected final GeneratorConfig cfg;
    protected final Configuration ftl;

    protected AbstractGenerator(AtlanClient client, GeneratorConfig cfg) {
        this.client = client;
        this.cfg = cfg;
        this.ftl = cfg.getFreemarkerConfig();
        this.cache = ModelCache.getInstance(client);
    }

    public abstract void generate() throws Exception;

    protected void createDirectoryIdempotent(String directory) {
        // First ensure the target directory has been created / exists
        File dir = new File(directory);
        if (!dir.exists()) {
            log.info("Creating directory: " + directory);
            if (!dir.mkdirs()) {
                log.error("Unable to create target directory: {}", directory);
            }
        }
    }
}
