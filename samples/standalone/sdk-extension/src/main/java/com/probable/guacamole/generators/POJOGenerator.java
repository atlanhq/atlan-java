/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.probable.guacamole.generators;

import com.atlan.generators.DocGenerator;
import com.atlan.generators.GeneratorConfig;
import com.atlan.generators.ModelGenerator;
import com.atlan.generators.TestGenerator;
import com.probable.guacamole.ExtendedModelGenerator;
import com.probable.guacamole.typedefs.TypeDefCreator;
import java.io.File;
import lombok.extern.slf4j.Slf4j;

// TODO: Won't work without multiple runs due to generated POJOs being something we introspect
//  as part of subsequent steps (classes need to re-load in-between)
@Slf4j
public class POJOGenerator extends ExtendedModelGenerator {
    public static void main(String[] args) throws Exception {
        String projectName = "samples" + File.separator + "standalone" + File.separator + "sdk-extension";
        GeneratorConfig cfg = GeneratorConfig.creator(
                        POJOGenerator.class, new File("resources/templates"), PACKAGE_ROOT, projectName)
                .serviceType(TypeDefCreator.SERVICE_TYPE)
                .build();
        new ModelGenerator(cfg).generate();
        new TestGenerator(cfg).generate();
        new DocGenerator(cfg).generate();
    }
}
