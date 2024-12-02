/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.probable.guacamole.typedefs;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.probable.guacamole.ExtendedModelGenerator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TypeDefDestroyer extends ExtendedModelGenerator {

    TypeDefDestroyer(AtlanClient client) {
        super(client);
    }

    public static void main(String[] args) {
        try (AtlanClient client = new AtlanClient()) {
            TypeDefDestroyer tdd = new TypeDefDestroyer(client);
            tdd.purgeTypeDefs();
        }
    }

    void purgeTypeDefs() {
        purgeTypeDef(TypeDefCreator.RELATIONSHIP_DEF_NAME);
        purgeTypeDef(TypeDefCreator.ENTITY_DEF_PARENT_NAME);
        purgeTypeDef(TypeDefCreator.ENTITY_DEF_CHILD_NAME);
        purgeTypeDef(TypeDefCreator.ENUM_DEF_NAME);
        purgeTypeDef(TypeDefCreator.STRUCT_DEF_NAME);
    }

    private void purgeTypeDef(String name) {
        try {
            client.typeDefs.purge(name);
            log.info("Purged typedef: {}", name);
        } catch (AtlanException e) {
            log.error("Failed to purge typedef: {}", name, e);
        }
    }
}
