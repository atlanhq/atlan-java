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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

/**
 * Utility class to generate a set of Markdown files that document the details of Atlan's metadata model.
 */
@Slf4j
public class DocumentationGenerator extends AbstractGenerator {

    private static final String DOCS_DIRECTORY = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "markdown";

    private static final Map<String, String> typeToIcon = Map.ofEntries(
        Map.entry("string", ":material-text:{ title=\"string\" }"),
        Map.entry("date", ":material-calendar-clock:{ title=\"date\" }"),
        Map.entry("boolean", ":material-toggle-switch:{ title=\"boolean\" }"),
        Map.entry("float", ":material-decimal:{ title=\"float\" }"),
        Map.entry("double", ":material-decimal:{ title=\"double\" }"),
        Map.entry("int", ":material-numeric:{ title=\"int\" }"),
        Map.entry("long", ":material-numeric:{ title=\"long\" }")
    );

    private static final Map<String, String> relationshipCardinality = Map.ofEntries(
        Map.entry("many-to-many", ":material-relation-many-to-many:{ title=\"many-to-many\" }"),
        Map.entry("one-to-many", ":material-relation-one-to-many:{ title=\"one-to-many\" }"),
        Map.entry("one-to-zero-or-one", ":material-relation-one-to-zero-or-one:{ title=\"one-to-zero-or-one\" }"),
        Map.entry("many-to-one", ":material-relation-many-to-one:{ title=\"many-to-one\" }"),
        Map.entry("zero-or-one-to-one", ":material-relation-zero-or-one-to-one:{ title=\"zero-or-one-to-one\" }"),
        Map.entry("zero-or-one-to-zero-or-one", ":material-relation-zero-or-one-to-zero-or-one:{ title=\"zero-or-one-to-zero-or-one\" }"),
        Map.entry("zero-or-one-to-many", ":material-relation-zero-or-one-to-many:{ title=\"zero-or-one-to-many\" }"),
        Map.entry("many-to-zero-or-one", ":material-relation-many-to-zero-or-one:{ title=\"many-to-zero-or-one\" }")
    );

    public static void main(String[] args) {
        DocumentationGenerator generator = new DocumentationGenerator();
        cacheModels();
        cacheDescriptions();
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
            generateTypeFile(typeName);
        }
    }

    private void generateTypeFile(String typeName) {
        try (BufferedWriter out = Files.newBufferedWriter(Paths.get(DOCS_DIRECTORY + File.separator + typeName.toLowerCase() + ".md"), UTF_8)) {
            writeHeader(out, typeName);
            writeModelDiagram(out, typeName);
            // TODO: Figure out stylesheets for highlighting the type currently being viewed,
            //  then just use the single diagram with both ancestors + direct subtypes.
            //  see: https://mermaid.js.org/syntax/classDiagram.html#styling
            writeSubtypes(out, typeName);
            writeAttributes(out, typeName);
        } catch (IOException e) {
            log.error("Unable to generate Markdown for type: {}", typeName, e);
        }
    }

    private void writeHeader(BufferedWriter out, String typeName) throws IOException {
        out.write("\n# " + typeName + "\n\n");
        out.write(getTypeDescription(typeName) + "\n\n");
    }

    private void writeModelDiagram(BufferedWriter out, String typeName) throws IOException {
        out.write("## Model\n\n");
        out.write("```mermaid\n");
        writeMainModelDiagram(out, typeName, "");
        out.write("```\n\n");
    }

    private void writeMainModelDiagram(BufferedWriter out, String typeName, String indent) throws IOException {
        out.write(indent + "classDiagram\n");
        out.write(indent + "    direction LR\n");
        EntityDef entityDef = entityDefCache.get(typeName);
        List<String> supers = entityDef.getSuperTypes();
        writeSuperTypes(out, supers, indent, new HashSet<>());
        writeModelClass(out, typeName, indent, !entityDef.getSubTypes().isEmpty());
        writeInheritanceModel(out, typeName, supers, indent, new HashSet<>());
    }

    private void writeSuperTypes(BufferedWriter out, List<String> superTypes, String indent, Set<String> written) throws IOException {
        if (superTypes != null && !superTypes.isEmpty()) {
            for (String superTypeName : superTypes) {
                if (!written.contains(superTypeName)) {
                    EntityDef parent = entityDefCache.get(superTypeName);
                    writeSuperTypes(out, parent.getSuperTypes(), indent, written);
                    writeModelClass(out, superTypeName, indent, true);
                    written.add(superTypeName);
                }
            }
        }
    }

    private void writeInheritanceModel(BufferedWriter out, String typeName, List<String> superTypes, String indent, Set<String> written) throws IOException {
        if (superTypes != null && !superTypes.isEmpty()) {
            for (String superTypeName : superTypes) {
                if (!written.contains(superTypeName + "|" + typeName)) {
                    EntityDef parent = entityDefCache.get(superTypeName);
                    writeInheritanceModel(out, superTypeName, parent.getSuperTypes(), indent, written);
                    out.write(indent + "    " + superTypeName + " <|-- " + typeName + " : extends\n");
                    written.add(superTypeName + "|" + typeName);
                }
            }
        }
    }

    private void writeModelClass(BufferedWriter out, String typeName, String indent, boolean superType) throws IOException {
        out.write(indent + "    class " + typeName);
        if (!superType) {
            out.write("\n");
        } else {
            out.write(" {\n");
            out.write(indent + "        <<abstract>>\n");
            out.write(indent + "    }\n");
        }
        out.write(indent + "    link " + typeName + " \"../" + typeName.toLowerCase() + "\"\n");
    }

    private void writeAttributes(BufferedWriter out, String typeName) throws IOException {
        out.write("## Attributes\n\n");
        Map<String, List<AttributeDef>> inherited = getAllInheritedAttributes(typeName);
        for (String superTypeName : inherited.keySet()) {
            List<AttributeDef> inheritedAttrs = inherited.get(superTypeName);
            for (AttributeDef inheritedAttrDef : inheritedAttrs) {
                writeAttribute(out, superTypeName, inheritedAttrDef, true);
            }
        }
        EntityDef entityDef = entityDefCache.get(typeName);
        List<AttributeDef> attributes = entityDef.getAttributeDefs();
        for (AttributeDef attributeDef : attributes) {
            writeAttribute(out, typeName, attributeDef, false);
        }
        out.write("## Relationships\n\n");
        out.write("??? model \"Visualization\"\n\n");
        // TODO: generate an expandable visualization that shows the classes, attribute names
        //  used for each relationship, and cardinalities (+ links on the classes)
        /*
        ??? model "Visualization"
            ```mermaid
            classDiagram
                direction LR
                GCSBucket "*" --> "*" AtlasGlossaryTerm : meanings
                GCSBucket "*" --> "*" Link : links
                GCSBucket "*" --> "*" Metric : metrics
                GCSBucket "0..1" --> "0..1" Readme : readme
                GCSBucket "*" --> "*" Process : inputToProcesses
                GCSBucket "*" --> "*" Process : outputFromProcesses
                GCSBucket "1" --> "*" GCSObject : gcsObjects
                link GCSBucket "../gcsbucket"
                link AtlasGlossaryTerm "../atlasglossaryterm"
            ```
         */
        // Relationship attributes appear at each level of the hierarchy, so we'll take
        // advantage of the linked map (insertion-ordered) response and block out any
        // that we've already output (from top of inheritance hierarchy downwards)
        // to avoid duplication
        Set<String> relationshipsWritten = new HashSet<>();
        Map<String, List<RelationshipAttributeDef>> inheritedRelationships = getAllInheritedRelationshipAttributes(typeName);
        for (String superTypeName : inheritedRelationships.keySet()) {
            List<RelationshipAttributeDef> inheritedAttrs = inheritedRelationships.get(superTypeName);
            for (RelationshipAttributeDef inheritedAttrDef : inheritedAttrs) {
                if (!relationshipsWritten.contains(inheritedAttrDef.getName())) {
                    writeRelationshipAttribute(out, superTypeName, inheritedAttrDef, true);
                    relationshipsWritten.add(inheritedAttrDef.getName());
                }
            }
        }
        List<RelationshipAttributeDef> relationships = entityDef.getRelationshipAttributeDefs();
        for (RelationshipAttributeDef relationshipAttributeDef : relationships) {
            if (!relationshipsWritten.contains(relationshipAttributeDef.getName())) {
                writeRelationshipAttribute(out, typeName, relationshipAttributeDef, false);
                relationshipsWritten.add(relationshipAttributeDef.getName());
            }
        }
    }

    private void writeAttribute(BufferedWriter out, String typeName, AttributeDef attributeDef, boolean inherited) throws IOException {

        String attrType = attributeDef.getTypeName();
        String attrName = attributeDef.getName();
        String description = getAttributeDescription(typeName, attrName);

        String icon = "";
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
            } else if (typeDef instanceof StructDef) {
                embeddedIcon = ":material-code-json:{ title=\"struct\" }";
                referencedType = typeDef.getName();
            }
        }
        if (embeddedIcon != null) {
            icon += embeddedIcon;
        }
        if (icon.length() == 0) {
            log.warn("Unable to find any icon for type: {}", embeddedType);
        }

        out.write(icon);
        if (inherited) {
            out.write(" ***`" + attrName + "`***");
        } else {
            out.write(" **`" + attrName + "`**");
        }
        if (referencedType != null) {
            out.write(" (" + referencedType + ")\n");
        } else {
            out.write("\n");
        }
        out.write(":   " + description + "\n\n");
    }

    private String getMapIcon(String attrType) {
        String embeddedType = getEmbeddedType(attrType);
        String icon = ":material-code-braces:{ title=\"map of\" } ";
        String[] tokens = embeddedType.split(",");
        icon += typeToIcon.getOrDefault(tokens[0], ":material-help-box:{ title=\"unknown\"}") + " ";
        icon += typeToIcon.getOrDefault(tokens[1], ":material-help-box:{ title=\"unknown\"} ");
        return icon;
    }

    private void writeRelationshipAttribute(BufferedWriter out, String typeName, RelationshipAttributeDef attributeDef, boolean inherited) throws IOException {
        String attrName = attributeDef.getName();
        String description = getAttributeDescription(typeName, attrName);
        out.write(getRelationshipCardinality(attributeDef));
        if (inherited) {
            out.write(" ***`" + attrName + "`***");
        } else {
            out.write(" **`" + attrName + "`**");
        }
        String relatedType = getRelatedType(attributeDef);
        if (relatedType != null) {
            out.write(" ([" + relatedType + "](../" + relatedType.toLowerCase() + "))\n");
        } else {
            out.write("\n");
        }
        out.write(":   " + description + "\n\n");
    }

    private String getRelationshipCardinality(RelationshipAttributeDef attributeDef) {
        String attributeName = attributeDef.getName();
        String relationshipName = attributeDef.getRelationshipTypeName();
        RelationshipDef relationshipDef = relationshipDefCache.get(relationshipName);
        RelationshipCategory category = relationshipDef.getRelationshipCategory();
        RelationshipEndDef one = relationshipDef.getEndDef1();
        RelationshipEndDef two = relationshipDef.getEndDef2();
        AtlanCustomAttributeCardinality from = null;
        AtlanCustomAttributeCardinality to = null;
        boolean end1 = false;
        if (one.getName().equals(attributeName)) {
            to = one.getCardinality();
            from = two.getCardinality();
            end1 = true;
        } else if (two.getName().equals(attributeName)) {
            to = two.getCardinality();
            from = one.getCardinality();
        } else {
            log.error("Unable to find a matching attribute ({}) in the relationship for: {}", attributeName, relationshipDef);
        }
        String cardinality = "";
        switch(category) {
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
                            cardinality = relationshipCardinality.get("one-to-many");
                            break;
                        case SINGLE:
                            cardinality = relationshipCardinality.get("one-to-zero-or-one");
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
                            cardinality = relationshipCardinality.get("many-to-one");
                            break;
                        case SINGLE:
                            cardinality = relationshipCardinality.get("zero-or-one-to-one");
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
                        cardinality = relationshipCardinality.get("zero-or-one-to-zero-or-one");
                    } else {
                        cardinality = relationshipCardinality.get("zero-or-one-to-many");
                    }
                } else {
                    if (to == AtlanCustomAttributeCardinality.SINGLE) {
                        cardinality = relationshipCardinality.get("many-to-zero-or-one");
                    } else {
                        cardinality = relationshipCardinality.get("many-to-many");
                    }
                }
                break;
            default:
                log.error("Unknown relationship category! {}", category);
                break;
        }
        return cardinality;
    }

    private String getRelatedType(RelationshipAttributeDef attributeDef) {
        String attributeName = attributeDef.getName();
        String relationshipName = attributeDef.getRelationshipTypeName();
        RelationshipDef relationshipDef = relationshipDefCache.get(relationshipName);
        RelationshipEndDef one = relationshipDef.getEndDef1();
        RelationshipEndDef two = relationshipDef.getEndDef2();
        String relatedType = null;
        if (one.getName().equals(attributeName)) {
            relatedType = two.getType();
        } else if (two.getName().equals(attributeName)) {
            relatedType = one.getType();
        } else {
            log.error("Unable to find a matching attribute ({}) in the relationship for: {}", attributeName, relationshipDef);
        }
        return relatedType;
    }

    private void writeSubtypes(BufferedWriter out, String typeName) throws IOException {
        EntityDef entityDef = entityDefCache.get(typeName);
        List<String> subTypes = entityDef.getSubTypes();
        if (subTypes != null && !subTypes.isEmpty()) {
            out.write("??? model \"Including direct subtypes\"\n");
            out.write("    ```mermaid\n");
            writeMainModelDiagram(out, typeName, "    ");
            for (String subTypeName : subTypes) {
                writeModelClass(out, subTypeName, "    ", !entityDefCache.get(subTypeName).getSubTypes().isEmpty());
                out.write("    " + typeName + " <|-- " + subTypeName + " : extends\n");
            }
            out.write("    ```\n\n");
        }
    }

    private String getEmbeddedType(String attrType) {
        return attrType.substring(attrType.indexOf("<") + 1, attrType.indexOf(">"));
    }
}
