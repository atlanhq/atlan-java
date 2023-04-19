/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.generators;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.atlan.model.enums.AtlanCustomAttributeCardinality;
import com.atlan.model.enums.RelationshipCategory;
import com.atlan.model.typedefs.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class to generate a set of Markdown files that document the details of Atlan's metadata model.
 */
@Slf4j
public class DocumentationGenerator extends AbstractGenerator {

    enum RelationshipCardinality {
        MANY_TO_MANY,
        ONE_TO_MANY,
        ONE_TO_ZERO_OR_ONE,
        MANY_TO_ONE,
        ZERO_OR_ONE_TO_ONE,
        ZERO_OR_ONE_TO_ZERO_OR_ONE,
        ZERO_OR_ONE_TO_MANY,
        MANY_TO_ZERO_OR_ONE,
    }

    enum Rename {
        NONE,
        JAVA,
        PYTHON
    }

    private static final String DOCS_DIRECTORY =
            "src" + File.separator + "main" + File.separator + "resources" + File.separator + "markdown";

    private static final Map<String, String> typeToIcon = Map.ofEntries(
            Map.entry("string", ":material-text:{ title=\"string\" }"),
            Map.entry("date", ":material-calendar-clock:{ title=\"date\" }"),
            Map.entry("boolean", ":material-toggle-switch:{ title=\"boolean\" }"),
            Map.entry("float", ":material-decimal:{ title=\"float\" }"),
            Map.entry("double", ":material-decimal:{ title=\"double\" }"),
            Map.entry("int", ":material-numeric:{ title=\"int\" }"),
            Map.entry("long", ":material-numeric:{ title=\"long\" }"));

    private static final Map<RelationshipCardinality, String> cardinalityIcons = Map.ofEntries(
            Map.entry(
                    RelationshipCardinality.MANY_TO_MANY, ":material-relation-many-to-many:{ title=\"many-to-many\" }"),
            Map.entry(RelationshipCardinality.ONE_TO_MANY, ":material-relation-one-to-many:{ title=\"one-to-many\" }"),
            Map.entry(
                    RelationshipCardinality.ONE_TO_ZERO_OR_ONE,
                    ":material-relation-one-to-one:{ title=\"one-to-one\" }"),
            Map.entry(RelationshipCardinality.MANY_TO_ONE, ":material-relation-many-to-one:{ title=\"many-to-one\" }"),
            Map.entry(
                    RelationshipCardinality.ZERO_OR_ONE_TO_ONE,
                    ":material-relation-one-to-one:{ title=\"one-to-one\" }"),
            Map.entry(
                    RelationshipCardinality.ZERO_OR_ONE_TO_ZERO_OR_ONE,
                    ":material-relation-one-to-one:{ title=\"one-to-one\" }"),
            Map.entry(
                    RelationshipCardinality.ZERO_OR_ONE_TO_MANY,
                    ":material-relation-one-to-many:{ title=\"one-to-many\" }"),
            Map.entry(
                    RelationshipCardinality.MANY_TO_ZERO_OR_ONE,
                    ":material-relation-many-to-one:{ title=\"many-to-one\" }"));

    private static final Map<RelationshipCardinality, String> cardinalityQualifiers = Map.ofEntries(
            Map.entry(RelationshipCardinality.MANY_TO_MANY, " \"*\" --> \"*\" "),
            Map.entry(RelationshipCardinality.ONE_TO_MANY, " \"1\" --> \"*\" "),
            Map.entry(RelationshipCardinality.ONE_TO_ZERO_OR_ONE, " \"1\" --> \"0..1\" "),
            Map.entry(RelationshipCardinality.MANY_TO_ONE, " \"*\" --> \"1\" "),
            Map.entry(RelationshipCardinality.ZERO_OR_ONE_TO_ONE, " \"0..1\" --> \"1\" "),
            Map.entry(RelationshipCardinality.ZERO_OR_ONE_TO_ZERO_OR_ONE, " \"0..1\" --> \"0..1\" "),
            Map.entry(RelationshipCardinality.ZERO_OR_ONE_TO_MANY, " \"0..1\" --> \"*\" "),
            Map.entry(RelationshipCardinality.MANY_TO_ZERO_OR_ONE, " \"*\" --> \"0..1\" "));

    private static final Map<IndexType, String> indexIcons = Map.ofEntries(
            Map.entry(IndexType.KEYWORD, "[:material-tag:{ title=\"keyword\" }](../../../search/attributes/#keyword)"),
            Map.entry(IndexType.TEXT, "[:material-tag-text:{ title=\"text\" }](../../../search/attributes/#text)"),
            Map.entry(
                    IndexType.DATE, "[:material-calendar-clock:{ title=\"date\" }](../../../search/attributes/#date)"),
            Map.entry(
                    IndexType.BOOLEAN,
                    "[:material-toggle-switch:{ title=\"boolean\" }](../../../search/attributes/#boolean)"),
            Map.entry(IndexType.FLOAT, "[:material-numeric:{ title=\"float\" }](../../../search/attributes/#float)"),
            Map.entry(
                    IndexType.RANK_FEATURE,
                    "[:material-sort:{ title=\"rank\" }](../../../search/attributes/#rank-feature)"));

    public static void main(String[] args) {
        DocumentationGenerator generator = new DocumentationGenerator();
        cacheDescriptions();
        cacheModels();
        generator.createDirectoryIdempotent();
        generator.generateTypeFiles();
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

    private void generateTypeFiles() {
        for (String typeName : entityDefCache.keySet()) {
            if (!typeName.startsWith("__")) {
                generateTypeFile(typeName);
            }
        }
        for (Map.Entry<String, TypeDef> entry : typeDefCache.entrySet()) {
            String typeName = entry.getKey();
            TypeDef typeDef = entry.getValue();
            if (typeDef instanceof StructDef) {
                generateStructFile(typeName, (StructDef) typeDef);
            } else if (typeDef instanceof EnumDef) {
                generateEnumFile(typeName, (EnumDef) typeDef);
            }
        }
    }

    private void generateTypeFile(String typeName) {
        try (BufferedWriter out = Files.newBufferedWriter(
                Paths.get(DOCS_DIRECTORY + File.separator + typeName.toLowerCase() + ".md"), UTF_8)) {
            writeHeader(out, typeName);
            EntityDef entityDef = entityDefCache.get(typeName);
            writeModelSection(out, typeName, entityDef);
            writeCorePropertiesSection(out);
            writeAttributesSection(out, typeName, entityDef);
            writeRelationshipsSection(out, typeName, entityDef);
        } catch (IOException e) {
            log.error("Unable to generate Markdown for type: {}", typeName, e);
        }
    }

    private void generateStructFile(String typeName, StructDef structDef) {
        try (BufferedWriter out = Files.newBufferedWriter(
                Paths.get(DOCS_DIRECTORY + File.separator + typeName.toLowerCase() + ".md"), UTF_8)) {
            writeHeader(out, typeName);
            writeAttributesSection(out, typeName, structDef);
        } catch (IOException e) {
            log.error("Unable to generate Markdown for type: {}", typeName, e);
        }
    }

    private void generateEnumFile(String typeName, EnumDef enumDef) {
        try (BufferedWriter out = Files.newBufferedWriter(
                Paths.get(DOCS_DIRECTORY + File.separator + typeName.toLowerCase() + ".md"), UTF_8)) {
            writeHeader(out, typeName);
            writeValidValues(out, enumDef);
        } catch (IOException e) {
            log.error("Unable to generate Markdown for type: {}", typeName, e);
        }
    }

    private void writeValidValues(BufferedWriter out, EnumDef enumDef) throws IOException {
        out.write("## Valid values\n\n");
        for (String value : enumDef.getValidValues()) {
            out.write("- `" + value + "`\n");
        }
        out.write("\n");
    }

    private void writeHeader(BufferedWriter out, String typeName) throws IOException {
        out.write("\n# " + typeName + "\n\n");
        out.write(getTypeDescription(typeName) + "\n\n");
        out.write("!!! warning \"Complete reference\"\n");
        out.write("    This is a complete reference for the `" + typeName + "` object in Atlan, showing every possible property and relationship that can exist for these objects. For an introduction, you probably want to start with:\n\n");
        out.write("    - [:material-hexagon-slice-1: snippets](../../sdks) — small, atomic examples of single-step use cases.\n");
        out.write("    - [:material-hexagon-slice-3: implementation patterns](../../patterns) — walkthroughs of common multi-step implementation patterns.\n\n");
        String className = NAME_MAPPINGS.getOrDefault(typeName, typeName);
        if (!className.equals(typeName)) {
            out.write("!!! warning \"Renamed in SDK\"\n");
            out.write("    In the :fontawesome-brands-java: Java SDK, this type is instead named `" + className
                    + "` for simplicity and consistency.\n\n");
        }
    }

    private void writeModelSection(BufferedWriter out, String typeName, EntityDef entityDef) throws IOException {
        out.write("## Model\n\n");
        out.write(
                "Following is the inheritance structure for `" + typeName
                        + "`. The type structure may be simplified in some of the SDKs, but for search purposes you could still use any of the super types shown in the Raw REST API view.\n\n");
        out.write("=== \":fontawesome-brands-java: Java\"\n\n");
        out.write("    ```mermaid\n");
        writeMainModelDiagram(out, typeName, entityDef, "    ", Rename.JAVA);
        out.write("    ```\n\n");
        writeSubtypesDiagram(out, typeName, entityDef, Rename.JAVA);
        out.write("=== \":material-language-python: Python\"\n\n");
        out.write("    ```mermaid\n");
        writeMainModelDiagram(out, typeName, entityDef, "    ", Rename.PYTHON);
        out.write("    ```\n\n");
        writeSubtypesDiagram(out, typeName, entityDef, Rename.PYTHON);
        out.write("=== \":material-code-json: Raw REST API\"\n\n");
        out.write("    ```mermaid\n");
        writeMainModelDiagram(out, typeName, entityDef, "    ", Rename.NONE);
        out.write("    ```\n\n");
        writeSubtypesDiagram(out, typeName, entityDef, Rename.NONE);
    }

    private void writeMainModelDiagram(
            BufferedWriter out, String typeName, EntityDef entityDef, String indent, Rename rename) throws IOException {
        // TODO: Figure out stylesheets for highlighting the type currently being viewed,
        //  then just use the single diagram with both ancestors + direct subtypes.
        //  see: https://mermaid.js.org/syntax/classDiagram.html#styling
        out.write(indent + "classDiagram\n");
        out.write(indent + "    direction LR\n");
        List<String> supers = entityDef.getSuperTypes();
        writeModelClass(out, typeName, indent, rename, !entityDef.getSubTypes().isEmpty());
        writeInheritanceModel(out, typeName, supers, indent, rename, new HashSet<>());
    }

    private void writeInheritanceModel(
            BufferedWriter out,
            String typeName,
            List<String> superTypes,
            String indent,
            Rename rename,
            Set<String> written)
            throws IOException {
        if (superTypes != null && !superTypes.isEmpty()) {
            if (rename == Rename.JAVA) {
                // For Java since there is no polymorphic inheritance, only show the singular
                // inheritance used by the SDK
                String singleExtend = getSingleTypeToExtend(typeName, superTypes);
                _writeInheritanceModel(out, typeName, singleExtend, indent, rename, written);
            } else {
                // For any other type, show the full inheritance structure
                for (String superTypeName : superTypes) {
                    if (!superTypeName.startsWith("__") && !written.contains(superTypeName + "|" + typeName)) {
                        _writeInheritanceModel(out, typeName, superTypeName, indent, rename, written);
                    }
                }
            }
        }
    }

    private void _writeInheritanceModel(
            BufferedWriter out,
            String typeName,
            String superTypeName,
            String indent,
            Rename rename,
            Set<String> written)
            throws IOException {
        EntityDef parent = entityDefCache.get(superTypeName);
        writeInheritanceModel(out, superTypeName, parent.getSuperTypes(), indent, rename, written);
        String renamedSuper = superTypeName;
        String renamedActual = typeName;
        if (rename == Rename.JAVA) {
            renamedSuper = NAME_MAPPINGS.getOrDefault(superTypeName, superTypeName);
            renamedActual = NAME_MAPPINGS.getOrDefault(typeName, typeName);
        }
        if (!renamedSuper.equals(renamedActual)) {
            // Exclude cases where a flattening in the SDK would cause a self-referencing
            // inheritance relationship (in particular Asset <|-- Asset : extends)
            if (!written.contains(superTypeName)) {
                writeModelClass(out, superTypeName, indent, rename, true);
            }
            if (!written.contains(typeName)) {
                EntityDef thisLevel = entityDefCache.get(typeName);
                writeModelClass(
                        out, typeName, indent, rename, !thisLevel.getSubTypes().isEmpty());
            }
            out.write(indent + "    " + renamedSuper + " <|-- " + renamedActual + " : extends\n");
        }
        written.add(superTypeName + "|" + typeName);
    }

    private void writeModelClass(BufferedWriter out, String typeName, String indent, Rename rename, boolean superType)
            throws IOException {
        String renamedActual = typeName;
        if (rename == Rename.JAVA) {
            renamedActual = NAME_MAPPINGS.getOrDefault(typeName, typeName);
        }
        out.write(indent + "    class " + renamedActual);
        if (!superType) {
            out.write("\n");
        } else {
            out.write(" {\n");
            out.write(indent + "        <<abstract>>\n");
            out.write(indent + "    }\n");
        }
        out.write(indent + "    link " + renamedActual + " \"../" + typeName.toLowerCase() + "\"\n");
    }

    private void writeCorePropertiesSection(BufferedWriter out) throws IOException {
        out.write("## Core properties\n\n");
        out.write(
                "These properties are core to all assets in Atlan. As part of the raw API payloads, these are at the top-level of the object.\n\n");
        out.write("=== \":fontawesome-brands-java: Java\"\n\n");
        Map<String, IndexType> typeNameIndexes = Map.of(
                "[`KeywordFields.TYPE_NAME`](../../../search/attributes/common/#__typename)",
                IndexType.KEYWORD,
                "[`TextFields.TYPE_NAME`](../../../search/attributes/common/#__typename)",
                IndexType.TEXT,
                "[`KeywordFields.SUPER_TYPE_NAMES`](../../../search/attributes/common/#__supertypenames)",
                IndexType.KEYWORD,
                "[`TextFields.SUPER_TYPE_NAME`](../../../search/attributes/common/#__supertypenames)",
                IndexType.TEXT);
        Map<String, IndexType> guidIndexes =
                Map.of("[`KeywordFields.GUID`](../../../search/attributes/common/#__guid)", IndexType.KEYWORD);
        Map<String, IndexType> customMetadataIndexes = Map.of(
                "[Finding assets by custom metadata](../../../sdks/common-examples/finding/have-custom-metadata)",
                IndexType.KEYWORD);
        Map<String, IndexType> statusIndexes =
                Map.of("[`KeywordFields.STATE`](../../../search/attributes/common/#__state)", IndexType.KEYWORD);
        Map<String, IndexType> createdByIndexes = Map.of(
                "[`KeywordFields.CREATED_BY`](../../../search/attributes/common/#__createdby)", IndexType.KEYWORD);
        Map<String, IndexType> updatedByIndexes = Map.of(
                "[`KeywordFields.MODIFIED_BY`](../../../search/attributes/common/#__modifiedby)", IndexType.KEYWORD);
        Map<String, IndexType> createTimeIndexes =
                Map.of("[`NumericFields.TIMESTAMP`](../../../search/attributes/common/#__timestamp)", IndexType.DATE);
        Map<String, IndexType> updateTimeIndexes = Map.of(
                "[`NumericFields.MODIFICATION_TIMESTAMP`](../../../search/attributes/common/#__modificationtimestamp)",
                IndexType.DATE);
        Map<String, IndexType> classificationNameIndexes = Map.of(
                "[`KeywordFields.TRAIT_NAMES`](../../../search/attributes/common/#__traitnames)", IndexType.KEYWORD,
                "[`KeywordFields.PROPAGATED_TRAIT_NAMES`](../../../search/attributes/common/#__propagatedtraitnames)",
                        IndexType.KEYWORD,
                "[`TextFields.CLASSIFICATIONS_TEXT`](../../../search/attributes/common/#__classificationstext)",
                        IndexType.TEXT);
        Map<String, IndexType> meaningNamesIndexes = Map.of(
                "[`KeywordFields.MEANINGS`](../../../search/attributes/common/#__meanings)", IndexType.KEYWORD,
                "[`TextFields.MEANINGS_TEXT`](../../../search/attributes/common/#__meaningstext)", IndexType.TEXT);
        writeProperty(out, "typeName", "Type of this asset.", ":material-text:{ title=\"string\" }", typeNameIndexes);
        writeProperty(
                out,
                "guid",
                "Globally-unique identifier for this asset.",
                ":material-text:{ title=\"string\" }",
                guidIndexes);
        writeProperty(
                out,
                "classifications",
                "Classifications assigned to the asset.",
                ":material-code-brackets:{ title=\"array of\" } :material-code-json:{ title=\"struct\" }",
                null);
        writeProperty(
                out,
                "customMetadataSets",
                "Map of custom metadata attributes and values defined on the asset. The map is keyed by the human-readable name of the custom metadata set, and the values are a further mapping from human-readable attribute name to the value for that attribute on this asset.",
                ":material-code-braces:{ title=\"map of\" } :material-text:{ title=\"string\" } :material-code-json:{ title=\"struct\" }",
                customMetadataIndexes);
        writeProperty(
                out,
                "status",
                "Status of the asset.",
                ":material-format-list-group:{ title=\"enumeration\" }",
                statusIndexes);
        writeProperty(
                out,
                "createdBy",
                "User or account that created the asset.",
                ":material-text:{ title=\"string\" }",
                createdByIndexes);
        writeProperty(
                out,
                "updatedBy",
                "User or account that last updated the asset.",
                ":material-text:{ title=\"string\" }",
                updatedByIndexes);
        writeProperty(
                out,
                "createTime",
                "Time (epoch) at which the asset was created, in milliseconds.",
                ":material-calendar-clock:{ title=\"date\" }",
                createTimeIndexes);
        writeProperty(
                out,
                "updateTime",
                "Time (epoch) at which the asset was last updated, in milliseconds.",
                ":material-calendar-clock:{ title=\"date\" }",
                updateTimeIndexes);
        writeProperty(
                out,
                "deleteHandler",
                "Details on the handler used for deletion of the asset.",
                ":material-text:{ title=\"string\" }",
                null);
        writeProperty(
                out,
                "classificationNames",
                "Human-readable names of the classifications that exist on the asset.",
                ":material-code-brackets:{ title=\"array of\" } :material-text:{ title=\"string\" }",
                classificationNameIndexes);
        writeProperty(out, "isIncomplete", "Unused.", ":material-toggle-switch:{ title=\"boolean\" }", null);
        writeProperty(
                out,
                "meaningNames",
                "Human-readable names of terms that have been linked to this asset.",
                ":material-code-brackets:{ title=\"array of\" } :material-text:{ title=\"string\" }",
                meaningNamesIndexes);
        writeProperty(
                out,
                "meanings",
                "Details of terms that have been linked to this asset.",
                ":material-code-brackets:{ title=\"array of\" } :material-code-json:{ title=\"struct\" }",
                null);
        writeProperty(
                out,
                "pendingTasks",
                "Unique identifiers (GUIDs) for any background tasks that are yet to operate on this asset.",
                ":material-code-brackets:{ title=\"array of\" } :material-text:{ title=\"string\" }",
                null);
        out.write("=== \":material-language-python: Python\"\n\n");
        out.write("    !!! construction \"Coming soon\"\n\n");
        out.write("=== \":material-code-json: Raw REST API\"\n\n");
        typeNameIndexes = Map.of(
                "[`__typeName.keyword`](../../../search/attributes/common/#__typename)",
                IndexType.KEYWORD,
                "[`__typeName`](../../../search/attributes/common/#__typename)",
                IndexType.TEXT,
                "[`__superTypeNames.keyword`](../../../search/attributes/common/#__supertypenames)",
                IndexType.KEYWORD,
                "[`__superTypeNames`](../../../search/attributes/common/#__supertypenames)",
                IndexType.TEXT);
        guidIndexes = Map.of("[`__guid`](../../../search/attributes/common/#__guid)", IndexType.KEYWORD);
        customMetadataIndexes = Map.of(
                "[Finding assets by custom metadata](../../../sdks/common-examples/finding/have-custom-metadata)",
                IndexType.KEYWORD);
        statusIndexes = Map.of("[`__state`](../../../search/attributes/common/#__state)", IndexType.KEYWORD);
        createdByIndexes = Map.of("[`__createdBy`](../../../search/attributes/common/#__createdby)", IndexType.KEYWORD);
        updatedByIndexes =
                Map.of("[`__modifiedBy`](../../../search/attributes/common/#__modifiedby)", IndexType.KEYWORD);
        createTimeIndexes = Map.of("[`__timestamp`](../../../search/attributes/common/#__timestamp)", IndexType.DATE);
        updateTimeIndexes = Map.of(
                "[`__modificationTimestamp`](../../../search/attributes/common/#__modificationtimestamp)",
                IndexType.DATE);
        classificationNameIndexes = Map.of(
                "[`__traitNames`](../../../search/attributes/common/#__traitnames)",
                IndexType.KEYWORD,
                "[`__propagatedTraitNames`](../../../search/attributes/common/#__propagatedtraitnames)",
                IndexType.KEYWORD,
                "[`__classificationsText`](../../../search/attributes/common/#__classificationstext)",
                IndexType.TEXT);
        meaningNamesIndexes = Map.of(
                "[`__meanings`](../../../search/attributes/common/#__meanings)", IndexType.KEYWORD,
                "[`__meaningsText`](../../../search/attributes/common/#__meaningstext)", IndexType.TEXT);
        writeProperty(out, "typeName", "Type of this asset.", ":material-text:{ title=\"string\" }", typeNameIndexes);
        writeProperty(
                out,
                "guid",
                "Globally-unique identifier for this asset.",
                ":material-text:{ title=\"string\" }",
                guidIndexes);
        writeProperty(
                out,
                "classifications",
                "Classifications assigned to the asset.",
                ":material-code-brackets:{ title=\"array of\" } :material-code-json:{ title=\"struct\" }",
                null);
        writeProperty(
                out,
                "businessAttributes",
                "Map of custom metadata attributes and values defined on the asset. The map is keyed by the Atlan-internal hashed string name of the custom metadata set, and the values are a further mapping from Atlan-internal hashed string attribute name to the value for that attribute on this asset.",
                ":material-code-braces:{ title=\"map of\" } :material-text:{ title=\"string\" } :material-code-braces:{ title=\"map of\" } :material-text:{ title=\"string\" } :material-text:{ title=\"string\" }",
                customMetadataIndexes);
        writeProperty(
                out,
                "status",
                "Status of the asset.",
                ":material-format-list-group:{ title=\"enumeration\" }",
                statusIndexes);
        writeProperty(
                out,
                "createdBy",
                "User or account that created the asset.",
                ":material-text:{ title=\"string\" }",
                createdByIndexes);
        writeProperty(
                out,
                "updatedBy",
                "User or account that last updated the asset.",
                ":material-text:{ title=\"string\" }",
                updatedByIndexes);
        writeProperty(
                out,
                "createTime",
                "Time (epoch) at which the asset was created, in milliseconds.",
                ":material-calendar-clock:{ title=\"date\" }",
                createTimeIndexes);
        writeProperty(
                out,
                "updateTime",
                "Time (epoch) at which the asset was last updated, in milliseconds.",
                ":material-calendar-clock:{ title=\"date\" }",
                updateTimeIndexes);
        writeProperty(
                out,
                "deleteHandler",
                "Details on the handler used for deletion of the asset.",
                ":material-text:{ title=\"string\" }",
                null);
        writeProperty(
                out,
                "classificationNames",
                "Atlan-internal hashed string names of the classifications that exist on the asset.",
                ":material-code-brackets:{ title=\"array of\" } :material-text:{ title=\"string\" }",
                classificationNameIndexes);
        writeProperty(out, "isIncomplete", "Unused.", ":material-toggle-switch:{ title=\"boolean\" }", null);
        writeProperty(
                out,
                "meaningNames",
                "Human-readable names of terms that have been linked to this asset.",
                ":material-code-brackets:{ title=\"array of\" } :material-text:{ title=\"string\" }",
                meaningNamesIndexes);
        writeProperty(
                out,
                "meanings",
                "Details of terms that have been linked to this asset.",
                ":material-code-brackets:{ title=\"array of\" } :material-code-json:{ title=\"struct\" }",
                null);
        writeProperty(
                out,
                "pendingTasks",
                "Unique identifiers (GUIDs) for any background tasks that are yet to operate on this asset.",
                ":material-code-brackets:{ title=\"array of\" } :material-text:{ title=\"string\" }",
                null);
    }

    private void writeAttributesSection(BufferedWriter out, String typeName, TypeDef typeDef) throws IOException {
        out.write("## Attributes\n\n");
        out.write(
                "These further specify the details of an instance of `" + typeName
                        + "`. As part of the raw API payloads, these appear inside an embedded `attributes` object in the payload.\n\n");
        out.write("### Inherited attributes\n\n");
        out.write("=== \":fontawesome-brands-java: Java\"\n\n");
        writeInheritedAttributes(out, typeDef, Rename.JAVA);
        out.write("=== \":material-language-python: Python\"\n\n");
        writeInheritedAttributes(out, typeDef, Rename.PYTHON);
        out.write("=== \":material-code-json: Raw REST API\"\n\n");
        writeInheritedAttributes(out, typeDef, Rename.NONE);
        out.write("### Type-specific attributes\n\n");
        out.write("=== \":fontawesome-brands-java: Java\"\n\n");
        writeAttributes(out, typeName, typeDef, Rename.JAVA);
        out.write("=== \":material-language-python: Python\"\n\n");
        writeAttributes(out, typeName, typeDef, Rename.PYTHON);
        out.write("=== \":material-code-json: Raw REST API\"\n\n");
        writeAttributes(out, typeName, typeDef, Rename.NONE);
    }

    private void writeRelationshipsSection(BufferedWriter out, String typeName, EntityDef entityDef)
            throws IOException {
        out.write("## Relationships\n\n");
        out.write(
                "These further specify the details of any relationships between instances of `" + typeName
                        + "` and other objects. As part of the raw API payloads, these can appear inside either an embedded `attributes` object or an embedded `relationshipAttributes` object in the payload.\n\n");
        out.write("### Inherited relationships\n\n");
        out.write("=== \":fontawesome-brands-java: Java\"\n\n");
        writeInheritedRelationships(out, entityDef, Rename.JAVA);
        out.write("=== \":material-language-python: Python\"\n\n");
        writeInheritedRelationships(out, entityDef, Rename.PYTHON);
        out.write("=== \":material-code-json: Raw REST API\"\n\n");
        writeInheritedRelationships(out, entityDef, Rename.NONE);
        out.write("### Type-specific relationships\n\n");
        out.write("=== \":fontawesome-brands-java: Java\"\n\n");
        writeRelationships(out, typeName, entityDef, Rename.JAVA);
        out.write("=== \":material-language-python: Python\"\n\n");
        writeRelationships(out, typeName, entityDef, Rename.PYTHON);
        out.write("=== \":material-code-json: Raw REST API\"\n\n");
        writeRelationships(out, typeName, entityDef, Rename.NONE);
    }

    private void writeInheritedAttributes(BufferedWriter out, TypeDef typeDef, Rename rename) throws IOException {
        Map<String, List<AttributeDef>> inherited = getAllInheritedAttributes(typeDef);
        for (String superTypeName : inherited.keySet()) {
            if (!superTypeName.startsWith("__")) {
                List<AttributeDef> inheritedAttrs = inherited.get(superTypeName);
                for (AttributeDef inheritedAttrDef : inheritedAttrs) {
                    writeAttribute(out, superTypeName, inheritedAttrDef, true, rename);
                }
            }
        }
    }

    private void writeAttributes(BufferedWriter out, String typeName, TypeDef typeDef, Rename rename)
            throws IOException {
        List<AttributeDef> attributes = typeDef.getAttributeDefs();
        for (AttributeDef attributeDef : attributes) {
            writeAttribute(out, typeName, attributeDef, false, rename);
        }
    }

    private void writeInheritedRelationships(BufferedWriter out, EntityDef entityDef, Rename rename)
            throws IOException {
        Map<String, RelationshipDetails> relationshipMap = new LinkedHashMap<>();
        // Relationship attributes appear at each level of the hierarchy, so we'll take
        // advantage of the linked map (insertion-ordered) response and block out any
        // that we've already output (from top of inheritance hierarchy downwards)
        // to avoid duplication
        Map<String, List<RelationshipAttributeDef>> inheritedRelationships =
                getAllInheritedRelationshipAttributes(entityDef);
        for (Map.Entry<String, List<RelationshipAttributeDef>> entry : inheritedRelationships.entrySet()) {
            String superTypeName = entry.getKey();
            List<RelationshipAttributeDef> inheritedAttrs = entry.getValue();
            for (RelationshipAttributeDef inheritedAttrDef : inheritedAttrs) {
                String attributeName = inheritedAttrDef.getName();
                if (!attributeName.startsWith("__") && !relationshipMap.containsKey(attributeName)) {
                    relationshipMap.put(attributeName, getRelationshipDetails(superTypeName, inheritedAttrDef, true));
                }
            }
        }
        for (Map.Entry<String, RelationshipDetails> entry : relationshipMap.entrySet()) {
            RelationshipDetails details = entry.getValue();
            writeRelationshipAttribute(out, details, rename);
        }
    }

    private void writeRelationships(BufferedWriter out, String typeName, EntityDef entityDef, Rename rename)
            throws IOException {
        Map<String, RelationshipDetails> relationshipMap = new LinkedHashMap<>();
        // Relationship attributes appear at each level of the hierarchy, so we'll take
        // advantage of the linked map (insertion-ordered) response and block out any
        // that we've already output (from top of inheritance hierarchy downwards)
        // to avoid duplication
        Map<String, List<RelationshipAttributeDef>> inheritedRelationships =
                getAllInheritedRelationshipAttributes(entityDef);
        for (Map.Entry<String, List<RelationshipAttributeDef>> entry : inheritedRelationships.entrySet()) {
            String superTypeName = entry.getKey();
            List<RelationshipAttributeDef> inheritedAttrs = entry.getValue();
            for (RelationshipAttributeDef inheritedAttrDef : inheritedAttrs) {
                String attributeName = inheritedAttrDef.getName();
                if (!attributeName.startsWith("__") && !relationshipMap.containsKey(attributeName)) {
                    relationshipMap.put(attributeName, getRelationshipDetails(superTypeName, inheritedAttrDef, true));
                }
            }
        }
        Map<String, RelationshipDetails> directRelationships = new LinkedHashMap<>();
        List<RelationshipAttributeDef> relationships = entityDef.getRelationshipAttributeDefs();
        for (RelationshipAttributeDef relationshipAttributeDef : relationships) {
            String attributeName = relationshipAttributeDef.getName();
            if (!attributeName.startsWith("__") && !relationshipMap.containsKey(attributeName)) {
                directRelationships.put(
                        attributeName, getRelationshipDetails(typeName, relationshipAttributeDef, false));
            }
        }
        for (Map.Entry<String, RelationshipDetails> entry : directRelationships.entrySet()) {
            RelationshipDetails details = entry.getValue();
            writeRelationshipAttribute(out, details, rename);
        }
    }

    private void writeAttribute(
            BufferedWriter out, String typeName, AttributeDef attributeDef, boolean inherited, Rename rename)
            throws IOException {

        String attrType = attributeDef.getTypeName();
        String attrName = attributeDef.getName();
        String description = getAttributeDescription(typeName, attrName);
        Map<String, IndexType> searchIndex = getSearchFieldsForAttribute(attributeDef);

        String icon = "    ";
        String referencedType = null;
        String embeddedType = attrType;
        if (attrType.startsWith("array<")) {
            if (attrType.startsWith("array<map<")) {
                icon += ":material-code-brackets:{ title=\"array of\" } ";
                icon += getMapIcon(attrType.substring("array<".length(), attrType.length() - 1));
            } else {
                embeddedType = getEmbeddedType(attrType);
                icon += ":material-code-brackets:{ title=\"array of\" } ";
            }
        } else if (attrType.startsWith("map<")) {
            icon += getMapIcon(attrType);
        }
        String embeddedIcon = typeToIcon.getOrDefault(embeddedType, null);
        if (embeddedIcon == null) {
            TypeDef typeDef = typeDefCache.getOrDefault(embeddedType, null);
            if (typeDef instanceof EnumDef) {
                embeddedIcon = ":material-format-list-group:{ title=\"enumeration\" }";
                referencedType = typeDef.getName();
            } else if (typeDef instanceof StructDef) {
                embeddedIcon = ":material-code-json:{ title=\"struct\" }";
                referencedType = typeDef.getName();
            }
        }
        if (embeddedIcon != null) {
            icon += embeddedIcon;
        }
        if (icon.length() == 4) {
            log.warn("Unable to find any icon for type: {}", embeddedType);
        }

        String attrRename = attrName;
        if (rename == Rename.JAVA) {
            attrRename = ATTRIBUTE_RENAMING.getOrDefault(attrName, attrName);
        }
        out.write("    <div class=\"grid\" markdown>\n\n");
        out.write(icon);
        if (inherited) {
            out.write(" ***`" + attrRename + "`***");
        } else {
            out.write(" **`" + attrRename + "`**");
        }
        if (referencedType != null) {
            addRelatedTypeLink(out, referencedType, rename);
        }
        out.write("\n    :   " + description + "\n\n");
        writeSearchTable(out, attrName, searchIndex, rename, true);
        out.write("    </div>\n\n");
    }

    private void writeProperty(
            BufferedWriter out, String name, String description, String icon, Map<String, IndexType> indexes)
            throws IOException {
        out.write("    <div class=\"grid\" markdown>\n\n");
        out.write("    " + icon + " **`" + name + "`**\n");
        out.write("    :    " + description + "\n\n");
        writeSearchTable(out, name, indexes, Rename.NONE, false);
        out.write("    </div>\n\n");
    }

    private void writeSearchTable(
            BufferedWriter out, String attrName, Map<String, IndexType> indexes, Rename rename, boolean monospaceFields)
            throws IOException {
        if (indexes != null && !indexes.isEmpty()) {
            out.write("    | :material-magnify:{ title=\"Search field type\" } | Search field name |\n");
            out.write("    |---:|:---|\n");
            List<String> alphaNames = indexes.keySet().stream().sorted().collect(Collectors.toList());
            for (String fieldName : alphaNames) {
                IndexType type = indexes.get(fieldName);
                String renamed = fieldName;
                if (rename == Rename.JAVA) {
                    String enumId = getEnumFromAttrName(attrName);
                    switch (type) {
                        case BOOLEAN:
                            renamed = "BooleanFields." + enumId;
                            break;
                        case FLOAT:
                        case DATE:
                            renamed = "NumericFields." + enumId;
                            break;
                        case TEXT:
                            if (fieldName.endsWith(".stemmed")) {
                                renamed = "StemmedFields." + enumId;
                            } else {
                                renamed = "TextFields." + enumId;
                            }
                            break;
                        case RANK_FEATURE:
                            renamed = "RankFields." + enumId;
                            break;
                        case KEYWORD:
                        default:
                            renamed = "KeywordFields." + enumId;
                            break;
                    }
                }
                if (monospaceFields) {
                    out.write("    | " + getIndexIcon(type) + " | `" + renamed + "` |\n");
                } else {
                    out.write("    | " + getIndexIcon(type) + " | " + renamed + " |\n");
                }
            }
            out.write("\n");
        }
    }

    private String getMapIcon(String attrType) {
        String embeddedType = getEmbeddedType(attrType);
        String icon = ":material-code-braces:{ title=\"map of\" } ";
        String[] tokens = embeddedType.split(",");
        icon += typeToIcon.getOrDefault(tokens[0], ":material-help-box:{ title=\"unknown\"}") + " ";
        icon += typeToIcon.getOrDefault(tokens[1], ":material-help-box:{ title=\"unknown\"} ");
        return icon;
    }

    private String getIndexIcon(IndexType indexType) {
        String icon = indexIcons.getOrDefault(indexType, null);
        if (icon == null) {
            log.warn("Unable to find an icon for index type: {}", indexType);
        }
        return icon;
    }

    private void writeRelationshipAttribute(BufferedWriter out, RelationshipDetails details, Rename rename)
            throws IOException {
        String attrName = details.getAttributeName();
        String fromTypeName = details.getFromTypeName();
        String description = getAttributeDescription(fromTypeName, attrName);
        boolean inherited = details.getInherited();
        out.write("    <div class=\"grid\" markdown>\n\n");
        out.write("    ");
        out.write(getRelationshipCardinalityIcon(details.getCardinality()));
        String attrRename = attrName;
        if (rename == Rename.JAVA) {
            attrRename = ATTRIBUTE_RENAMING.getOrDefault(attrName, attrName);
        }
        if (inherited) {
            out.write(" ***`" + attrRename + "`***");
        } else {
            out.write(" **`" + attrRename + "`**");
        }
        out.write("\n    :   " + description + "\n\n");
        writeRelationshipDiagram(out, details, rename);
        out.write("    </div>\n\n");
    }

    private String getRelationshipCardinalityIcon(RelationshipCardinality cardinality) {
        String icon = cardinalityIcons.getOrDefault(cardinality, null);
        if (icon == null) {
            log.error("Unable to find any icon for cardinality: {}", cardinality);
        }
        return icon == null ? "" : icon;
    }

    private void writeRelationshipDiagram(BufferedWriter out, RelationshipDetails details, Rename rename)
            throws IOException {
        out.write("    ```mermaid\n");
        out.write("    classDiagram\n");
        out.write("        direction LR\n");
        String attrName = details.getAttributeName();
        String fromType = details.getFromTypeName();
        String relatedType = details.getRelatedToType();
        writeModelClass(
                out,
                fromType,
                "    ",
                rename,
                !entityDefCache.get(fromType).getSubTypes().isEmpty());
        writeModelClass(
                out,
                relatedType,
                "    ",
                rename,
                !entityDefCache.get(relatedType).getSubTypes().isEmpty());
        String cardinalityQualifier = cardinalityQualifiers.getOrDefault(details.getCardinality(), " --> ");
        String renamedAttr = attrName;
        String renamedFrom = fromType;
        String renamedRelated = relatedType;
        if (rename == Rename.JAVA) {
            renamedAttr = ATTRIBUTE_RENAMING.getOrDefault(attrName, attrName);
            renamedFrom = NAME_MAPPINGS.getOrDefault(fromType, fromType);
            renamedRelated = NAME_MAPPINGS.getOrDefault(relatedType, relatedType);
        }
        out.write("        " + renamedFrom + cardinalityQualifier + renamedRelated + " : " + renamedAttr + "\n");
        out.write("    ```\n\n");
    }

    private RelationshipDetails getRelationshipDetails(
            String fromTypeName, RelationshipAttributeDef attributeDef, boolean inherited) {
        String attributeName = attributeDef.getName();
        String relationshipName = attributeDef.getRelationshipTypeName();
        RelationshipDef relationshipDef = relationshipDefCache.get(relationshipName);
        RelationshipCategory category = relationshipDef.getRelationshipCategory();

        RelationshipEndDef one = relationshipDef.getEndDef1();
        RelationshipEndDef two = relationshipDef.getEndDef2();

        AtlanCustomAttributeCardinality from = null;
        AtlanCustomAttributeCardinality to = null;
        boolean end1 = false;
        String relatedType = null;
        if (one.getName().equals(attributeName)) {
            relatedType = two.getType();
            to = one.getCardinality();
            from = two.getCardinality();
            end1 = true;
        } else if (two.getName().equals(attributeName)) {
            relatedType = one.getType();
            to = two.getCardinality();
            from = one.getCardinality();
        } else {
            log.error(
                    "Unable to find a matching attribute ({}) in the relationship for: {}",
                    attributeName,
                    relationshipDef);
        }

        RelationshipCardinality cardinality = null;
        switch (category) {
            case COMPOSITION:
                // A composition relationship is the only one that has strong ownership dictates
                // and would thus give a mandatory singular cardinality to one end of the relationship
                // (and that owning end of the relationship is always the first endpoint)
                if (end1) {
                    if (from != AtlanCustomAttributeCardinality.SINGLE) {
                        log.error("Unmanaged state where composition source to multiple parents (?!)");
                    }
                    switch (to) {
                        case SET:
                            cardinality = RelationshipCardinality.ONE_TO_MANY;
                            break;
                        case SINGLE:
                            cardinality = RelationshipCardinality.ONE_TO_ZERO_OR_ONE;
                            break;
                        default:
                            log.error("Unknown cardinality! {}", to);
                            break;
                    }
                } else {
                    if (to != AtlanCustomAttributeCardinality.SINGLE) {
                        log.error("Unmanaged state where composition target to multiple parents (?!)");
                    }
                    switch (to) {
                        case SET:
                            cardinality = RelationshipCardinality.MANY_TO_ONE;
                            break;
                        case SINGLE:
                            cardinality = RelationshipCardinality.ZERO_OR_ONE_TO_ONE;
                            break;
                        default:
                            log.error("Unknown cardinality! {}", to);
                            break;
                    }
                }
                break;
            case AGGREGATION:
            case ASSOCIATION:
                if (from == AtlanCustomAttributeCardinality.SINGLE) {
                    if (to == AtlanCustomAttributeCardinality.SINGLE) {
                        cardinality = RelationshipCardinality.ZERO_OR_ONE_TO_ZERO_OR_ONE;
                    } else {
                        cardinality = RelationshipCardinality.ZERO_OR_ONE_TO_MANY;
                    }
                } else {
                    if (to == AtlanCustomAttributeCardinality.SINGLE) {
                        cardinality = RelationshipCardinality.MANY_TO_ZERO_OR_ONE;
                    } else {
                        cardinality = RelationshipCardinality.MANY_TO_MANY;
                    }
                }
                break;
            default:
                log.error("Unknown relationship category! {}", category);
                break;
        }

        return RelationshipDetails.builder()
                .fromTypeName(fromTypeName)
                .attributeName(attributeName)
                .relatedToType(relatedType)
                .cardinality(cardinality)
                .inherited(inherited)
                .build();
    }

    private void writeSubtypesDiagram(BufferedWriter out, String typeName, EntityDef entityDef, Rename rename)
            throws IOException {
        List<String> subTypes = entityDef.getSubTypes();
        if (subTypes != null && !subTypes.isEmpty()) {
            out.write("    ??? model \"Including direct subtypes\"\n");
            out.write("        ```mermaid\n");
            writeMainModelDiagram(out, typeName, entityDef, "        ", rename);
            for (String subTypeName : subTypes) {
                writeModelClass(
                        out,
                        subTypeName,
                        "        ",
                        rename,
                        !entityDefCache.get(subTypeName).getSubTypes().isEmpty());
                String renamedActual = typeName;
                String renamedSub = subTypeName;
                if (rename == Rename.JAVA) {
                    renamedActual = NAME_MAPPINGS.getOrDefault(typeName, typeName);
                    renamedSub = NAME_MAPPINGS.getOrDefault(subTypeName, subTypeName);
                }
                out.write("        " + renamedActual + " <|-- " + renamedSub + " : extends\n");
            }
            out.write("        ```\n\n");
        }
    }

    private void addRelatedTypeLink(BufferedWriter out, String relatedType, Rename rename) throws IOException {
        String renamedType = relatedType;
        if (rename == Rename.JAVA) {
            renamedType = NAME_MAPPINGS.getOrDefault(relatedType, relatedType);
        }
        out.write(" ([" + renamedType + "](../" + relatedType.toLowerCase() + "))");
    }

    @Data
    @Builder
    static class RelationshipDetails {
        String fromTypeName;
        String attributeName;
        String relatedToType;
        RelationshipCardinality cardinality;

        @Builder.Default
        boolean inherited = false;
    }
}
