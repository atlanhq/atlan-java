/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.probable.guacamole.typedefs;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanAttributeType;
import com.atlan.model.enums.AtlanCustomAttributeCardinality;
import com.atlan.model.enums.RelationshipCategory;
import com.atlan.model.typedefs.*;
import com.probable.guacamole.ExtendedModelGenerator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TypeDefCreator extends ExtendedModelGenerator {

    static final String STRUCT_DEF_NAME = TYPE_PREFIX + "Struct";
    static final String ENUM_DEF_NAME = TYPE_PREFIX + "Temperature";
    static final String ENTITY_DEF_PARENT_NAME = TYPE_PREFIX + "Table";
    static final String ENTITY_DEF_CHILD_NAME = TYPE_PREFIX + "Column";
    static final String RELATIONSHIP_DEF_NAME = TYPE_PREFIX + "TableColumns";

    TypeDefCreator(AtlanClient client) {
        super(client);
    }

    public static void main(String[] args) {
        try (AtlanClient client = new AtlanClient()) {
            TypeDefCreator tdc = new TypeDefCreator(client);
            tdc.createEnumDef();
            tdc.createStructDef();
            tdc.createEntityDef();
            tdc.createRelationshipDef();
            tdc.updateEntityDef();
        }
    }

    void createStructDef() {
        List<AttributeDef> attrs = List.of(
                AttributeDef.creator(SERVICE_TYPE + "ComplexOne", AtlanAttributeType.STRING)
                        .build(),
                AttributeDef.creator(SERVICE_TYPE + "ComplexTwo", AtlanAttributeType.LONG)
                        .build());
        StructDef sd = StructDef.creator(STRUCT_DEF_NAME, attrs)
                .serviceType(SERVICE_TYPE)
                .description("Complex embedded attributes for " + TYPE_PREFIX + " objects.")
                .build();
        try {
            TypeDefResponse response = client.typeDefs._create(sd);
            log.info("Created structDef: {}", response);
        } catch (AtlanException e) {
            log.error("Failed to create StructDef.", e);
        }
    }

    void createEnumDef() {
        EnumDef enumDef = EnumDef.creator(ENUM_DEF_NAME, List.of("Hot", "Mild", "Cold"))
                .serviceType(SERVICE_TYPE)
                .description("Valid values for the temperature of a " + TYPE_PREFIX + " table.")
                .build();
        try {
            EnumDef response = enumDef.create(client);
            log.info("Created enumDef: {}", response);
        } catch (AtlanException e) {
            log.error("Failed to create EnumDef.", e);
        }
    }

    void createEntityDef() {
        List<AttributeDef> attrs = List.of(
                AttributeDef.creator(SERVICE_TYPE + "Size", AtlanAttributeType.LONG)
                        .description(
                                "Consolidated quantification metric spanning number of columns, rows, and sparsity of population.")
                        .build(),
                AttributeDef.creator(
                                SERVICE_TYPE + "Temperature",
                                AtlanAttributeType.ENUM,
                                ENUM_DEF_NAME,
                                AtlanCustomAttributeCardinality.SINGLE)
                        .description("Rough measure of the IOPS allocated to the table's processing.")
                        .build());
        EntityDef ed = EntityDef.creator(ENTITY_DEF_PARENT_NAME, attrs)
                .serviceType(SERVICE_TYPE)
                .description("Specialized form of a table specific to " + TYPE_PREFIX + ".")
                .superTypes(List.of("Table"))
                .build();
        try {
            TypeDefResponse response = client.typeDefs._create(ed);
            log.info("Created entityDef: {}", response);
        } catch (AtlanException e) {
            log.error("Failed to create EntityDef.", e);
        }
        attrs = List.of(
                AttributeDef.creator(SERVICE_TYPE + "Width", AtlanAttributeType.LONG)
                        .description("Maximum size of a " + TYPE_PREFIX + " column.")
                        .build(),
                AttributeDef.creator(SERVICE_TYPE + "Conceptualized", AtlanAttributeType.DATE)
                        .description("Time (epoch) when this column was imagined, in milliseconds.")
                        .build());
        ed = EntityDef.creator(ENTITY_DEF_CHILD_NAME, attrs)
                .serviceType(SERVICE_TYPE)
                .description("Specialized form of a column specific to " + TYPE_PREFIX + ".")
                .superTypes(List.of("Column"))
                .build();
        try {
            TypeDefResponse response = client.typeDefs._create(ed);
            log.info("Created entityDef: {}", response);
        } catch (AtlanException e) {
            log.error("Failed to create EntityDef.", e);
        }
    }

    void createRelationshipDef() {
        List<AttributeDef> attrs = List.of(AttributeDef.creator(SERVICE_TYPE + "Partition", AtlanAttributeType.STRING)
                .description("Table partition within which the column exists.")
                .build());
        RelationshipEndDef one = RelationshipEndDef.creator(
                        SERVICE_TYPE + "Columns", ENTITY_DEF_PARENT_NAME, true, AtlanCustomAttributeCardinality.SET)
                .description("Specialized columns contained within this specialized table.")
                .build();
        RelationshipEndDef two = RelationshipEndDef.creator(
                        SERVICE_TYPE + "Table", ENTITY_DEF_CHILD_NAME, false, AtlanCustomAttributeCardinality.SINGLE)
                .description("Specialized table that contains this specialized column.")
                .build();
        RelationshipDef rd = RelationshipDef.creator(RELATIONSHIP_DEF_NAME, one, two, attrs)
                .serviceType(SERVICE_TYPE)
                .relationshipCategory(RelationshipCategory.AGGREGATION)
                .description("Parent-child relationship between specialized table and its columns.")
                .build();
        try {
            TypeDefResponse response = client.typeDefs._create(rd);
            log.info("Created relationshipDef: {}", response);
        } catch (AtlanException e) {
            log.error("Failed to create RelationshipDef.", e);
        }
    }

    void updateEntityDef() {
        try {
            EntityDef parent = (EntityDef) client.typeDefs.get(ENTITY_DEF_PARENT_NAME);
            parent = parent.toBuilder()
                    .attributeDef(AttributeDef.creator(SERVICE_TYPE + "Archived", AtlanAttributeType.BOOLEAN)
                            .description("Whether this table is currently archived (true) or not (false).")
                            .build())
                    .typeVersion("1.1")
                    .build();
            TypeDefResponse response = client.typeDefs._update(parent);
            log.info("Updated entityDef: {}", response);
        } catch (AtlanException e) {
            log.error("Failed to update entityDef.", e);
        }
    }
}
