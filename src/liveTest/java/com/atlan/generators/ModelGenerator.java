/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.model.enums.*;
import com.atlan.model.typedefs.AttributeDef;
import com.atlan.model.typedefs.EntityDef;
import com.atlan.model.typedefs.RelationshipAttributeDef;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class to generate the Atlan bean (model) classes that can be used for (de-)serialising the Atlan API
 * JSON payloads.
 * This class is not part of the SDK itself, and is instead only used as a mechanism to generate other Java classes,
 * and even then only on a fairly rare basis (as releases of Atlan emerge that make any changes to the properties or types that it handles).
 * In light of this, and various code analysis tools indicating potential exposures from allowing a main() method that
 * receives parameters like file-system locations, this has been modified to embed any parameters directly into the
 * code itself.
 */
@Slf4j
public class ModelGenerator extends AbstractGenerator {

    private static final String MODEL_DIRECTORY = ""
            + "src" + File.separator
            + "main" + File.separator
            + "java" + File.separator
            + "com" + File.separator
            + "atlan" + File.separator
            + "model" + File.separator
            + "assets";

    private static final String ENUMS_DIRECTORY = ""
            + "src" + File.separator
            + "main" + File.separator
            + "java" + File.separator
            + "com" + File.separator
            + "atlan" + File.separator
            + "model" + File.separator
            + "enums";

    private static final String TEST_DIRECTORY = ""
            + "src" + File.separator
            + "test" + File.separator
            + "java" + File.separator
            + "com" + File.separator
            + "atlan" + File.separator
            + "model" + File.separator
            + "assets";

    // We'll continue to manage these directly, as they contain a lot of non-generated complexity
    // (or would be completely unused due to polymorphism flattening)
    private static final Set<String> SKIP_GENERATING = Set.of(
            "Referenceable", "Catalog", "DataStudio", "AtlasServer", "DataSet", "Infrastructure", "ProcessExecution");

    // Provide a name that Lombok can use for the singularization of these multivalued attributes
    private static final Map<String, String> SINGULAR_MAPPINGS = Map.ofEntries(
            Map.entry("seeAlso", "seeAlsoOne"),
            Map.entry("replacedByTerm", "replacedByTerm"),
            Map.entry("validValuesFor", "validValueFor"),
            Map.entry("isA", "isATerm"),
            Map.entry("replacedBy", "replacedByTerm"),
            Map.entry("childrenCategories", "childCategory"),
            Map.entry("queryUserMap", "putQueryUserMap"),
            Map.entry("queryPreviewConfig", "putQueryPreviewConfig"),
            Map.entry("reportType", "putReportType"),
            Map.entry("projectHierarchy", "addProjectHierarchy"),
            Map.entry("certifier", "putCertifier"),
            Map.entry("presetChartFormData", "putPresetChartFormData"),
            Map.entry("resourceMetadata", "putResourceMetadata"),
            Map.entry("adlsObjectMetadata", "putAdlsObjectMetadata"));

    private static final SortedSet<String> concreteModels = new TreeSet<>();

    public static void main(String[] args) {
        ModelGenerator generator = new ModelGenerator();
        cacheDescriptions();
        cacheModels();
        generator.generateModels();
        generator.generateTests();
        generator.generateDeserializationStub();
        generator.generateEnums();
    }

    private void generateModels() {
        // First ensure the target directory has been created / exists
        File dir = new File(MODEL_DIRECTORY);
        if (!dir.exists()) {
            log.info("Creating directory: " + MODEL_DIRECTORY);
            if (!dir.mkdirs()) {
                log.error("Unable to create target directory: {}", MODEL_DIRECTORY);
            }
        }
        for (Map.Entry<String, EntityDef> entry : entityDefCache.entrySet()) {
            String name = entry.getKey();
            if (!SKIP_GENERATING.contains(name) && !name.startsWith("__")) {
                log.info("Creating model for: {}", name);
                createModelForType(entry.getValue());
            }
        }
    }

    private void generateTests() {
        // First ensure the target directory has been created / exists
        File dir = new File(TEST_DIRECTORY);
        if (!dir.exists()) {
            log.info("Creating directory: " + TEST_DIRECTORY);
            if (!dir.mkdirs()) {
                log.error("Unable to create target directory: {}", TEST_DIRECTORY);
            }
        }

        for (Map.Entry<String, EntityDef> entry : entityDefCache.entrySet()) {
            String name = entry.getKey();
            EntityDef entityDef = entry.getValue();
            List<String> subTypes = entityDef.getSubTypes();
            if (!SKIP_GENERATING.contains(name) && !name.startsWith("__")) {
                if (subTypes == null || subTypes.isEmpty() || CREATE_NON_ABSTRACT.containsKey(name)) {
                    log.info("Creating test for: {}", name);
                    // Only generate tests for non-abstract classes
                    createTestForType(entityDef);
                }
            }
        }
    }

    private void generateDeserializationStub() {
        for (String className : concreteModels) {
            System.out.println("                case " + className + ".TYPE_NAME:");
            System.out.println("                    builder = " + className + ".builder();");
            System.out.println("                    break;");
        }
        System.out.println("                default:");
        System.out.println("                    builder = IndistinctAsset.builder();");
        System.out.println("                    break;");
    }

    private void generateEnums() {
        File dir = new File(ENUMS_DIRECTORY);
        if (!dir.exists()) {
            log.info("Creating directory: " + ENUMS_DIRECTORY);
            if (!dir.mkdirs()) {
                log.error("Unable to create target directory: {}", ENUMS_DIRECTORY);
            }
        }
        generateEnum("NumericFields", searchableNumerics);
        generateEnum("KeywordFields", searchableKeywords);
        generateEnum("TextFields", searchableText);
        generateEnum("StemmedFields", searchableStemmed);
        generateEnum("BooleanFields", searchableBooleans);
        generateEnum("RankFields", searchableRanks);
    }

    private void generateEnum(String enumName, Map<String, SearchableField> fieldMap) {
        String filename = ENUMS_DIRECTORY + File.separator + enumName + ".java";
        try (BufferedWriter fs =
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {

            fs.append("package com.atlan.model.enums;")
                    .append(System.lineSeparator())
                    .append(System.lineSeparator());
            fs.append("import lombok.Getter;").append(System.lineSeparator()).append(System.lineSeparator());
            fs.append("public enum ")
                    .append(enumName)
                    .append(" implements AtlanSearchableField {")
                    .append(System.lineSeparator());

            for (Map.Entry<String, SearchableField> entry : fieldMap.entrySet()) {
                String attrName = entry.getKey();
                SearchableField field = entry.getValue();
                if (ATTRIBUTE_RENAMING.containsKey(attrName)) {
                    attrName = ATTRIBUTE_RENAMING.get(attrName);
                }
                String enumId = getEnumFromAttrName(attrName);
                fs.append("    /** ")
                        .append(field.getDescription())
                        .append(" */")
                        .append(System.lineSeparator());
                fs.append("    ")
                        .append(enumId)
                        .append("(\"")
                        .append(field.getFieldName())
                        .append("\"),")
                        .append(System.lineSeparator());
            }
            fs.append("    ;").append(System.lineSeparator());

            fs.append("    @Getter(onMethod_ = {@Override})").append(System.lineSeparator());
            fs.append("    private final String indexedFieldName;")
                    .append(System.lineSeparator())
                    .append(System.lineSeparator());
            fs.append("    ")
                    .append(enumName)
                    .append("(String indexedFieldName) {")
                    .append(System.lineSeparator());
            fs.append("        this.indexedFieldName = indexedFieldName;").append(System.lineSeparator());
            fs.append("    }").append(System.lineSeparator());
            fs.append("}").append(System.lineSeparator());

        } catch (IOException e) {
            log.error("Unable to open file output: {}", filename, e);
        }
    }

    private void refByGuid(BufferedWriter fs, String className) throws IOException {
        fs.append("    /**");
        fs.append(System.lineSeparator());
        fs.append("     * Reference to a ").append(className).append(" by GUID.");
        fs.append(System.lineSeparator());
        fs.append("     *");
        fs.append(System.lineSeparator());
        fs.append("     * @param guid the GUID of the ").append(className).append(" to reference");
        fs.append(System.lineSeparator());
        fs.append("     * @return reference to a ")
                .append(className)
                .append(" that can be used for defining a relationship to a ")
                .append(className);
        fs.append(System.lineSeparator());
        fs.append("     */");
        fs.append(System.lineSeparator());
        fs.append("    public static ").append(className).append(" refByGuid(String guid) {");
        fs.append(System.lineSeparator());
        fs.append("        return ").append(className).append(".builder().guid(guid).build();");
        fs.append(System.lineSeparator());
        fs.append("    }");
        fs.append(System.lineSeparator()).append(System.lineSeparator());
    }

    private void refByQualifiedName(BufferedWriter fs, String className) throws IOException {
        fs.append("    /**");
        fs.append(System.lineSeparator());
        fs.append("     * Reference to a ").append(className).append(" by qualifiedName.");
        fs.append(System.lineSeparator());
        fs.append("     *");
        fs.append(System.lineSeparator());
        fs.append("     * @param qualifiedName the qualifiedName of the ")
                .append(className)
                .append(" to reference");
        fs.append(System.lineSeparator());
        fs.append("     * @return reference to a ")
                .append(className)
                .append(" that can be used for defining a relationship to a ")
                .append(className);
        fs.append(System.lineSeparator());
        fs.append("     */");
        fs.append(System.lineSeparator());
        fs.append("    public static ").append(className).append(" refByQualifiedName(String qualifiedName) {");
        fs.append(System.lineSeparator());
        fs.append("        return ").append(className).append(".builder()");
        fs.append(System.lineSeparator());
        fs.append("                .uniqueAttributes(");
        fs.append(System.lineSeparator());
        fs.append("                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())");
        fs.append(System.lineSeparator());
        fs.append("                .build();");
        fs.append(System.lineSeparator());
        fs.append("    }");
        fs.append(System.lineSeparator()).append(System.lineSeparator());
    }

    private void updater(BufferedWriter fs, String className) throws IOException {
        fs.append("    /**");
        fs.append(System.lineSeparator());
        fs.append("     * Builds the minimal object necessary to update a ")
                .append(className)
                .append(".");
        fs.append(System.lineSeparator());
        fs.append("     *");
        fs.append(System.lineSeparator());
        fs.append("     * @param qualifiedName of the ").append(className);
        fs.append(System.lineSeparator());
        fs.append("     * @param name of the ").append(className);
        fs.append(System.lineSeparator());
        fs.append("     * @return the minimal request necessary to update the ")
                .append(className)
                .append(", as a builder");
        fs.append(System.lineSeparator());
        fs.append("     */");
        fs.append(System.lineSeparator());
        fs.append("    public static ")
                .append(className)
                .append("Builder<?, ?> updater(String qualifiedName, String name) {");
        fs.append(System.lineSeparator());
        fs.append("        return ").append(className).append(".builder().qualifiedName(qualifiedName).name(name);");
        fs.append(System.lineSeparator());
        fs.append("    }");
        fs.append(System.lineSeparator()).append(System.lineSeparator());
    }

    private void trimToRequired(BufferedWriter fs, String className) throws IOException {
        fs.append("    /**");
        fs.append(System.lineSeparator());
        fs.append("     * Builds the minimal object necessary to apply an update to a ")
                .append(className)
                .append(", from a potentially");
        fs.append(System.lineSeparator());
        fs.append("     * more-complete ").append(className).append(" object.");
        fs.append(System.lineSeparator());
        fs.append("     *");
        fs.append(System.lineSeparator());
        fs.append("     * @return the minimal object necessary to update the ")
                .append(className)
                .append(", as a builder");
        fs.append(System.lineSeparator());
        fs.append("     * @throws InvalidRequestException if any of the minimal set of required properties for ")
                .append(className)
                .append(" are not found in the initial object");
        fs.append(System.lineSeparator());
        fs.append("     */");
        fs.append(System.lineSeparator());
        fs.append("    @Override");
        fs.append(System.lineSeparator());
        fs.append("    public ")
                .append(className)
                .append("Builder<?, ?> trimToRequired() throws InvalidRequestException {")
                .append(System.lineSeparator());
        fs.append("        List<String> missing = new ArrayList<>();").append(System.lineSeparator());
        fs.append("        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {")
                .append(System.lineSeparator());
        fs.append("            missing.add(\"qualifiedName\");").append(System.lineSeparator());
        fs.append("        }").append(System.lineSeparator());
        fs.append("        if (this.getName() == null || this.getName().length() == 0) {")
                .append(System.lineSeparator());
        fs.append("            missing.add(\"name\");").append(System.lineSeparator());
        fs.append("        }").append(System.lineSeparator());
        fs.append("        if (!missing.isEmpty()) {").append(System.lineSeparator());
        fs.append("            throw new InvalidRequestException(ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, \"")
                .append(className)
                .append("\", String.join(\",\", missing));")
                .append(System.lineSeparator());
        fs.append("        }").append(System.lineSeparator());
        fs.append("        return updater(this.getQualifiedName(), this.getName());")
                .append(System.lineSeparator());
        fs.append("    }");
        fs.append(System.lineSeparator()).append(System.lineSeparator());
    }

    private void retrievals(BufferedWriter fs, String className) throws IOException {
        fs.append("    /**").append(System.lineSeparator());
        fs.append("     * Retrieves a ")
                .append(className)
                .append(" by its GUID, complete with all of its relationships.")
                .append(System.lineSeparator());
        fs.append("     *").append(System.lineSeparator());
        fs.append("     * @param guid of the ")
                .append(className)
                .append(" to retrieve")
                .append(System.lineSeparator());
        fs.append("     * @return the requested full ")
                .append(className)
                .append(", complete with all of its relationships")
                .append(System.lineSeparator());
        fs.append(
                        "     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ")
                .append(className)
                .append(" does not exist or the provided GUID is not a ")
                .append(className)
                .append(System.lineSeparator());
        fs.append("     */").append(System.lineSeparator());
        fs.append("    public static ")
                .append(className)
                .append(" retrieveByGuid(String guid) throws AtlanException {")
                .append(System.lineSeparator());
        fs.append("        Asset asset = Asset.retrieveFull(guid);").append(System.lineSeparator());
        fs.append("        if (asset == null) {").append(System.lineSeparator());
        fs.append("            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);")
                .append(System.lineSeparator());
        fs.append("        } else if (asset instanceof ")
                .append(className)
                .append(") {")
                .append(System.lineSeparator());
        fs.append("            return asset;").append(System.lineSeparator());
        fs.append("        } else {").append(System.lineSeparator());
        fs.append("            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, \"")
                .append(className)
                .append("\");")
                .append(System.lineSeparator());
        fs.append("        }").append(System.lineSeparator());
        fs.append("    }").append(System.lineSeparator());
        fs.append(System.lineSeparator());
        fs.append("    /**").append(System.lineSeparator());
        fs.append("     * Retrieves a ")
                .append(className)
                .append(" by its qualifiedName, complete with all of its relationships.")
                .append(System.lineSeparator());
        fs.append("     *").append(System.lineSeparator());
        fs.append("     * @param qualifiedName of the ")
                .append(className)
                .append(" to retrieve")
                .append(System.lineSeparator());
        fs.append("     * @return the requested full ")
                .append(className)
                .append(", complete with all of its relationships")
                .append(System.lineSeparator());
        fs.append(
                        "     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ")
                .append(className)
                .append(" does not exist")
                .append(System.lineSeparator());
        fs.append("     */").append(System.lineSeparator());
        fs.append("    public static ")
                .append(className)
                .append(" retrieveByQualifiedName(String qualifiedName) throws AtlanException {")
                .append(System.lineSeparator());
        fs.append("        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);")
                .append(System.lineSeparator());
        fs.append("        if (asset instanceof ")
                .append(className)
                .append(") {")
                .append(System.lineSeparator());
        fs.append("            return asset;").append(System.lineSeparator());
        fs.append("        } else {").append(System.lineSeparator());
        fs.append("            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, \"")
                .append(className)
                .append("\");")
                .append(System.lineSeparator());
        fs.append("        }").append(System.lineSeparator());
        fs.append("    }").append(System.lineSeparator());
        fs.append(System.lineSeparator());
    }

    private void updateCertificate(BufferedWriter fs, String className) throws IOException {
        fs.append("    /**");
        fs.append(System.lineSeparator());
        fs.append("     * Update the certificate on a ").append(className).append(".");
        fs.append(System.lineSeparator());
        fs.append("     *");
        fs.append(System.lineSeparator());
        fs.append("     * @param qualifiedName of the ").append(className);
        fs.append(System.lineSeparator());
        fs.append("     * @param certificate to use");
        fs.append(System.lineSeparator());
        fs.append("     * @param message (optional) message, or null if no message");
        fs.append(System.lineSeparator());
        fs.append("     * @return the updated ").append(className).append(", or null if the update failed");
        fs.append(System.lineSeparator());
        fs.append("     * @throws AtlanException on any API problems");
        fs.append(System.lineSeparator());
        fs.append("     */");
        fs.append(System.lineSeparator());
        fs.append("    public static ")
                .append(className)
                .append(" updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)");
        fs.append(System.lineSeparator());
        fs.append("            throws AtlanException {");
        fs.append(System.lineSeparator());
        fs.append("        return (")
                .append(className)
                .append(") Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);");
        fs.append(System.lineSeparator());
        fs.append("    }");
        fs.append(System.lineSeparator()).append(System.lineSeparator());
    }

    private void removeCertificate(BufferedWriter fs, String className) throws IOException {
        fs.append("    /**");
        fs.append(System.lineSeparator());
        fs.append("     * Remove the certificate from a ").append(className).append(".");
        fs.append(System.lineSeparator());
        fs.append("     *");
        fs.append(System.lineSeparator());
        fs.append("     * @param qualifiedName of the ").append(className);
        fs.append(System.lineSeparator());
        fs.append("     * @param name of the ").append(className);
        fs.append(System.lineSeparator());
        fs.append("     * @return the updated ").append(className).append(", or null if the removal failed");
        fs.append(System.lineSeparator());
        fs.append("     * @throws AtlanException on any API problems");
        fs.append(System.lineSeparator());
        fs.append("     */");
        fs.append(System.lineSeparator());
        fs.append("    public static ")
                .append(className)
                .append(" removeCertificate(String qualifiedName, String name) throws AtlanException {");
        fs.append(System.lineSeparator());
        fs.append("        return (").append(className).append(")");
        fs.append(System.lineSeparator());
        fs.append("                Asset.removeCertificate(updater(qualifiedName, name));");
        fs.append(System.lineSeparator());
        fs.append("    }");
        fs.append(System.lineSeparator()).append(System.lineSeparator());
    }

    private void updateAnnouncement(BufferedWriter fs, String className) throws IOException {
        fs.append("    /**");
        fs.append(System.lineSeparator());
        fs.append("     * Update the announcement on a ").append(className).append(".");
        fs.append(System.lineSeparator());
        fs.append("     *");
        fs.append(System.lineSeparator());
        fs.append("     * @param qualifiedName of the ").append(className);
        fs.append(System.lineSeparator());
        fs.append("     * @param type type of announcement to set");
        fs.append(System.lineSeparator());
        fs.append("     * @param title (optional) title of the announcement to set (or null for no title)");
        fs.append(System.lineSeparator());
        fs.append("     * @param message (optional) message of the announcement to set (or null for no message)");
        fs.append(System.lineSeparator());
        fs.append("     * @return the result of the update, or null if the update failed");
        fs.append(System.lineSeparator());
        fs.append("     * @throws AtlanException on any API problems");
        fs.append(System.lineSeparator());
        fs.append("     */");
        fs.append(System.lineSeparator());
        fs.append("    public static ").append(className).append(" updateAnnouncement(");
        fs.append(System.lineSeparator());
        fs.append(
                "            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {");
        fs.append(System.lineSeparator());
        fs.append("        return (")
                .append(className)
                .append(") Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);");
        fs.append(System.lineSeparator());
        fs.append("    }");
        fs.append(System.lineSeparator()).append(System.lineSeparator());
    }

    private void removeDescription(BufferedWriter fs, String className) throws IOException {
        fs.append("    /**");
        fs.append(System.lineSeparator());
        fs.append("     * Remove the system description from a ")
                .append(className)
                .append(".");
        fs.append(System.lineSeparator());
        fs.append("     *");
        fs.append(System.lineSeparator());
        fs.append("     * @param qualifiedName of the ").append(className);
        fs.append(System.lineSeparator());
        fs.append("     * @param name of the ").append(className);
        fs.append(System.lineSeparator());
        fs.append("     * @return the updated ").append(className).append(", or null if the removal failed");
        fs.append(System.lineSeparator());
        fs.append("     * @throws AtlanException on any API problems");
        fs.append(System.lineSeparator());
        fs.append("     */");
        fs.append(System.lineSeparator());
        fs.append("    public static ")
                .append(className)
                .append(" removeDescription(String qualifiedName, String name) throws AtlanException {");
        fs.append(System.lineSeparator());
        fs.append("        return (").append(className).append(")");
        fs.append(System.lineSeparator());
        fs.append("                Asset.removeDescription(updater(qualifiedName, name));");
        fs.append(System.lineSeparator());
        fs.append("    }");
        fs.append(System.lineSeparator()).append(System.lineSeparator());
    }

    private void removeUserDescription(BufferedWriter fs, String className) throws IOException {
        fs.append("    /**");
        fs.append(System.lineSeparator());
        fs.append("     * Remove the user's description from a ")
                .append(className)
                .append(".");
        fs.append(System.lineSeparator());
        fs.append("     *");
        fs.append(System.lineSeparator());
        fs.append("     * @param qualifiedName of the ").append(className);
        fs.append(System.lineSeparator());
        fs.append("     * @param name of the ").append(className);
        fs.append(System.lineSeparator());
        fs.append("     * @return the updated ").append(className).append(", or null if the removal failed");
        fs.append(System.lineSeparator());
        fs.append("     * @throws AtlanException on any API problems");
        fs.append(System.lineSeparator());
        fs.append("     */");
        fs.append(System.lineSeparator());
        fs.append("    public static ")
                .append(className)
                .append(" removeUserDescription(String qualifiedName, String name) throws AtlanException {");
        fs.append(System.lineSeparator());
        fs.append("        return (").append(className).append(")");
        fs.append(System.lineSeparator());
        fs.append("                Asset.removeUserDescription(updater(qualifiedName, name));");
        fs.append(System.lineSeparator());
        fs.append("    }");
        fs.append(System.lineSeparator()).append(System.lineSeparator());
    }

    private void removeOwners(BufferedWriter fs, String className) throws IOException {
        fs.append("    /**");
        fs.append(System.lineSeparator());
        fs.append("     * Remove the owners from a ").append(className).append(".");
        fs.append(System.lineSeparator());
        fs.append("     *");
        fs.append(System.lineSeparator());
        fs.append("     * @param qualifiedName of the ").append(className);
        fs.append(System.lineSeparator());
        fs.append("     * @param name of the ").append(className);
        fs.append(System.lineSeparator());
        fs.append("     * @return the updated ").append(className).append(", or null if the removal failed");
        fs.append(System.lineSeparator());
        fs.append("     * @throws AtlanException on any API problems");
        fs.append(System.lineSeparator());
        fs.append("     */");
        fs.append(System.lineSeparator());
        fs.append("    public static ")
                .append(className)
                .append(" removeOwners(String qualifiedName, String name) throws AtlanException {");
        fs.append(System.lineSeparator());
        fs.append("        return (").append(className).append(")");
        fs.append(System.lineSeparator());
        fs.append("                Asset.removeOwners(updater(qualifiedName, name));");
        fs.append(System.lineSeparator());
        fs.append("    }");
        fs.append(System.lineSeparator()).append(System.lineSeparator());
    }

    private void removeAnnouncement(BufferedWriter fs, String className) throws IOException {
        fs.append("    /**");
        fs.append(System.lineSeparator());
        fs.append("     * Remove the announcement from a ").append(className).append(".");
        fs.append(System.lineSeparator());
        fs.append("     *");
        fs.append(System.lineSeparator());
        fs.append("     * @param qualifiedName of the ").append(className);
        fs.append(System.lineSeparator());
        fs.append("     * @param name of the ").append(className);
        fs.append(System.lineSeparator());
        fs.append("     * @return the updated ").append(className).append(", or null if the removal failed");
        fs.append(System.lineSeparator());
        fs.append("     * @throws AtlanException on any API problems");
        fs.append(System.lineSeparator());
        fs.append("     */");
        fs.append(System.lineSeparator());
        fs.append("    public static ")
                .append(className)
                .append(" removeAnnouncement(String qualifiedName, String name) throws AtlanException {");
        fs.append(System.lineSeparator());
        fs.append("        return (").append(className).append(")");
        fs.append(System.lineSeparator());
        fs.append("                Asset.removeAnnouncement(updater(qualifiedName, name));");
        fs.append(System.lineSeparator());
        fs.append("    }");
        fs.append(System.lineSeparator()).append(System.lineSeparator());
    }

    private void addClassifications(BufferedWriter fs, String className) throws IOException {
        fs.append("    /**");
        fs.append(System.lineSeparator());
        fs.append("     * Add classifications to a ").append(className).append(".");
        fs.append(System.lineSeparator());
        fs.append("     *");
        fs.append(System.lineSeparator());
        fs.append("     * @param qualifiedName of the ").append(className);
        fs.append(System.lineSeparator());
        fs.append("     * @param classificationNames human-readable names of the classifications to add");
        fs.append(System.lineSeparator());
        fs.append(
                        "     * @throws AtlanException on any API problems, or if any of the classifications already exist on the ")
                .append(className);
        fs.append(System.lineSeparator());
        fs.append("     */");
        fs.append(System.lineSeparator());
        fs.append("    public static void addClassifications(String qualifiedName, List<String> classificationNames)");
        fs.append(System.lineSeparator());
        fs.append("            throws AtlanException {");
        fs.append(System.lineSeparator());
        fs.append("        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);");
        fs.append(System.lineSeparator());
        fs.append("    }");
        fs.append(System.lineSeparator()).append(System.lineSeparator());
    }

    private void removeClassification(BufferedWriter fs, String className) throws IOException {
        fs.append("    /**");
        fs.append(System.lineSeparator());
        fs.append("     * Remove a classification from a ").append(className).append(".");
        fs.append(System.lineSeparator());
        fs.append("     *");
        fs.append(System.lineSeparator());
        fs.append("     * @param qualifiedName of the ").append(className);
        fs.append(System.lineSeparator());
        fs.append("     * @param classificationName human-readable name of the classification to remove");
        fs.append(System.lineSeparator());
        fs.append("     * @throws AtlanException on any API problems, or if the classification does not exist on the ")
                .append(className);
        fs.append(System.lineSeparator());
        fs.append("     */");
        fs.append(System.lineSeparator());
        fs.append(
                "    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {");
        fs.append(System.lineSeparator());
        fs.append("        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);");
        fs.append(System.lineSeparator());
        fs.append("    }");
        fs.append(System.lineSeparator()).append(System.lineSeparator());
    }

    private void restore(BufferedWriter fs, String className) throws IOException {
        fs.append("    /**");
        fs.append(System.lineSeparator());
        fs.append("     * Restore the archived (soft-deleted) ")
                .append(className)
                .append(" to active.");
        fs.append(System.lineSeparator());
        fs.append("     *");
        fs.append(System.lineSeparator());
        fs.append("     * @param qualifiedName for the ").append(className);
        fs.append(System.lineSeparator());
        fs.append("     * @return true if the ").append(className).append(" is now active, and false otherwise");
        fs.append(System.lineSeparator());
        fs.append("     * @throws AtlanException on any API problems");
        fs.append(System.lineSeparator());
        fs.append("     */");
        fs.append(System.lineSeparator());
        fs.append("    public static boolean restore(String qualifiedName)");
        fs.append(System.lineSeparator());
        fs.append("            throws AtlanException {");
        fs.append(System.lineSeparator());
        fs.append("        return Asset.restore(TYPE_NAME, qualifiedName);");
        fs.append(System.lineSeparator());
        fs.append("    }");
        fs.append(System.lineSeparator()).append(System.lineSeparator());
    }

    private void replaceTerms(BufferedWriter fs, String className) throws IOException {
        fs.append("    /**");
        fs.append(System.lineSeparator());
        fs.append("     * Replace the terms linked to the ").append(className).append(".");
        fs.append(System.lineSeparator());
        fs.append("     *");
        fs.append(System.lineSeparator());
        fs.append("     * @param qualifiedName for the ").append(className);
        fs.append(System.lineSeparator());
        fs.append("     * @param name human-readable name of the ").append(className);
        fs.append(System.lineSeparator());
        fs.append("     * @param terms the list of terms to replace on the ")
                .append(className)
                .append(", or null to remove all terms from the ")
                .append(className);
        fs.append(System.lineSeparator());
        fs.append("     * @return the ")
                .append(className)
                .append(" that was updated (note that it will NOT contain details of the replaced terms)");
        fs.append(System.lineSeparator());
        fs.append("     * @throws AtlanException on any API problems");
        fs.append(System.lineSeparator());
        fs.append("     */");
        fs.append(System.lineSeparator());
        fs.append("    public static ")
                .append(className)
                .append(" replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)");
        fs.append(System.lineSeparator());
        fs.append("            throws AtlanException {");
        fs.append(System.lineSeparator());
        fs.append("        return (")
                .append(className)
                .append(") Asset.replaceTerms(updater(qualifiedName, name), terms);");
        fs.append(System.lineSeparator());
        fs.append("    }");
        fs.append(System.lineSeparator()).append(System.lineSeparator());
    }

    private void appendTerms(BufferedWriter fs, String className) throws IOException {
        fs.append("    /**");
        fs.append(System.lineSeparator());
        fs.append("     * Link additional terms to the ")
                .append(className)
                .append(", without replacing existing terms linked to the ")
                .append(className)
                .append(".");
        fs.append(System.lineSeparator());
        fs.append("     * Note: this operation must make two API calls — one to retrieve the ")
                .append(className)
                .append("'s existing terms,");
        fs.append(System.lineSeparator());
        fs.append("     * and a second to append the new terms.");
        fs.append(System.lineSeparator());
        fs.append("     *");
        fs.append(System.lineSeparator());
        fs.append("     * @param qualifiedName for the ").append(className);
        fs.append(System.lineSeparator());
        fs.append("     * @param terms the list of terms to append to the ").append(className);
        fs.append(System.lineSeparator());
        fs.append("     * @return the ")
                .append(className)
                .append(" that was updated  (note that it will NOT contain details of the appended terms)");
        fs.append(System.lineSeparator());
        fs.append("     * @throws AtlanException on any API problems");
        fs.append(System.lineSeparator());
        fs.append("     */");
        fs.append(System.lineSeparator());
        fs.append("    public static ")
                .append(className)
                .append(" appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {");
        fs.append(System.lineSeparator());
        fs.append("        return (").append(className).append(") Asset.appendTerms(TYPE_NAME, qualifiedName, terms);");
        fs.append(System.lineSeparator());
        fs.append("    }");
        fs.append(System.lineSeparator()).append(System.lineSeparator());
    }

    private void removeTerms(BufferedWriter fs, String className) throws IOException {
        fs.append("    /**").append(System.lineSeparator());
        fs.append("     * Remove terms from a ")
                .append(className)
                .append(", without replacing all existing terms linked to the ")
                .append(className)
                .append(".")
                .append(System.lineSeparator());
        fs.append("     * Note: this operation must make two API calls — one to retrieve the ")
                .append(className)
                .append("'s existing terms,")
                .append(System.lineSeparator());
        fs.append("     * and a second to remove the provided terms.").append(System.lineSeparator());
        fs.append("     *").append(System.lineSeparator());
        fs.append("     * @param qualifiedName for the ").append(className).append(System.lineSeparator());
        fs.append("     * @param terms the list of terms to remove from the ")
                .append(className)
                .append(", which must be referenced by GUID")
                .append(System.lineSeparator());
        fs.append("     * @return the ")
                .append(className)
                .append(" that was updated (note that it will NOT contain details of the resulting terms)")
                .append(System.lineSeparator());
        fs.append("     * @throws AtlanException on any API problems").append(System.lineSeparator());
        fs.append("     */").append(System.lineSeparator());
        fs.append("    public static ")
                .append(className)
                .append(" removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {")
                .append(System.lineSeparator());
        fs.append("        return (")
                .append(className)
                .append(") Asset.removeTerms(TYPE_NAME, qualifiedName, terms);")
                .append(System.lineSeparator());
        fs.append("    }").append(System.lineSeparator());
        fs.append(System.lineSeparator());
    }

    private void createModelForType(EntityDef typeDetails) {

        String name = typeDetails.getName();
        log.info("Processing type: {}", name);

        String className = NAME_MAPPINGS.getOrDefault(name, name);

        if (CREATE_NON_ABSTRACT.containsKey(className)) {
            // 1. Create an abstract class, using the mapped name
            String abstractName = createAbstractModel(typeDetails, name, className);
            // 2. Create a concrete class (with the original name) that extends the abstract class
            createLeafModel(name, className, abstractName);
        } else {
            // Create a "normal" abstract or concrete class
            createModel(typeDetails, name, className);
        }

        // Write the file for any type that should not be ignored

    }

    private String getClassToExtend(String superType) {
        // Default to the mapped name
        String nameToUse = NAME_MAPPINGS.getOrDefault(superType, superType);
        if (CREATE_NON_ABSTRACT.containsKey(nameToUse)) {
            // ... if that mapped name itself has an abstracted version,
            // use the abstracted version as the parent
            nameToUse = CREATE_NON_ABSTRACT.get(nameToUse);
        }
        return nameToUse;
    }

    private String createAbstractModel(EntityDef typeDetails, String typeName, String className) {
        String abstractName = CREATE_NON_ABSTRACT.get(className);
        String filename = MODEL_DIRECTORY + File.separator + abstractName + ".java";
        String superType = getSingleTypeToExtend(typeName, typeDetails.getSuperTypes());
        // All of these + the original class (we'll later make a leaf node from)
        List<String> subTypes = typeDetails.getSubTypes();
        subTypes.add(className);
        try (BufferedWriter fs =
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {

            String classToExtend = getClassToExtend(superType);

            addHeader(fs, typeName);
            addOpening(fs, typeName, abstractName, classToExtend, subTypes);

            addAttributes(fs, typeName, typeDetails.getAttributeDefs());
            addRelationships(fs, typeName, typeDetails.getRelationshipAttributeDefs());

            addClosing(fs);

        } catch (IOException e) {
            log.error("Unable to open file output: {}", filename, e);
        }
        return abstractName;
    }

    private void createLeafModel(String typeName, String className, String abstractName) {
        String filename = MODEL_DIRECTORY + File.separator + className + ".java";
        try (BufferedWriter fs =
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {

            addHeader(fs, typeName);
            addOpening(fs, typeName, className, abstractName, null);

            refByGuid(fs, className);
            refByQualifiedName(fs, className);
            updater(fs, className);
            trimToRequired(fs, className);
            retrievals(fs, className);
            restore(fs, className);
            removeDescription(fs, className);
            removeUserDescription(fs, className);
            removeOwners(fs, className);
            updateCertificate(fs, className);
            removeCertificate(fs, className);
            updateAnnouncement(fs, className);
            removeAnnouncement(fs, className);
            addClassifications(fs, className);
            removeClassification(fs, className);
            replaceTerms(fs, className);
            appendTerms(fs, className);
            removeTerms(fs, className);

            addClosing(fs);

            concreteModels.add(className);

        } catch (IOException e) {
            log.error("Unable to open file output: {}", filename, e);
        }
    }

    private void createModel(EntityDef typeDetails, String typeName, String className) {
        String filename = MODEL_DIRECTORY + File.separator + className + ".java";
        String superType = getSingleTypeToExtend(typeName, typeDetails.getSuperTypes());
        List<String> subTypes = typeDetails.getSubTypes();
        try (BufferedWriter fs =
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {

            String classToExtend = getClassToExtend(superType);

            addHeader(fs, typeName);
            boolean isAbstract = addOpening(fs, typeName, className, classToExtend, subTypes);

            addAttributes(fs, typeName, typeDetails.getAttributeDefs());
            addRelationships(fs, typeName, typeDetails.getRelationshipAttributeDefs());

            if (!isAbstract) {
                refByGuid(fs, className);
                refByQualifiedName(fs, className);
                updater(fs, className);
                trimToRequired(fs, className);
                retrievals(fs, className);
                restore(fs, className);
                removeDescription(fs, className);
                removeUserDescription(fs, className);
                removeOwners(fs, className);
                updateCertificate(fs, className);
                removeCertificate(fs, className);
                updateAnnouncement(fs, className);
                removeAnnouncement(fs, className);
                addClassifications(fs, className);
                removeClassification(fs, className);
                replaceTerms(fs, className);
                appendTerms(fs, className);
                removeTerms(fs, className);
                concreteModels.add(className);
            }

            addClosing(fs);

        } catch (IOException e) {
            log.error("Unable to open file output: {}", filename, e);
        }
    }

    private void injectSingular(BufferedWriter fs, String name) throws IOException {
        if (SINGULAR_MAPPINGS.containsKey(name)) {
            fs.append("    @Singular(\"").append(SINGULAR_MAPPINGS.get(name)).append("\")");
        } else {
            fs.append("    @Singular");
        }
        fs.append(System.lineSeparator());
    }

    private void addAttributes(BufferedWriter fs, String typeName, List<AttributeDef> attrs) throws IOException {
        for (AttributeDef attribute : attrs) {
            String name = attribute.getName();
            String type = attribute.getTypeName();
            String mappedType = TYPE_MAPPINGS.getOrDefault(type, null);
            if (mappedType == null) {
                // Failing that, attempt a renamed type (structs, enums) or just
                // default to the same name (not renamed, but a struct or enum)
                mappedType = NAME_MAPPINGS.getOrDefault(type, type);
            }
            if (!mappedType.equals("__internal")) {
                fs.append("    /** ")
                        .append(getAttributeDescription(typeName, name))
                        .append(" */");
                fs.append(System.lineSeparator());
                fs.append("    @Attribute");
                fs.append(System.lineSeparator());
                if (ATTRIBUTE_RENAMING.containsKey(name)) {
                    fs.append("    @JsonProperty(\"").append(name).append("\")");
                    fs.append(System.lineSeparator());
                    name = ATTRIBUTE_RENAMING.get(name);
                }
                if (mappedType.contains("<")) {
                    injectSingular(fs, name);
                }
                fs.append("    ").append(mappedType).append(" ").append(name).append(";");
                fs.append(System.lineSeparator()).append(System.lineSeparator());
            }
        }
    }

    private void addRelationships(BufferedWriter fs, String typeName, List<RelationshipAttributeDef> attrs)
            throws IOException {
        Set<String> uniqueRelationships = relationshipsForType.get(typeName);
        for (RelationshipAttributeDef attribute : attrs) {
            String name = attribute.getName();
            if (uniqueRelationships.contains(name)) {
                String type = attribute.getTypeName();
                if (!type.equals("__internal")) {
                    AtlanCustomAttributeCardinality cardinality = attribute.getCardinality();
                    fs.append("    /** ")
                            .append(getAttributeDescription(typeName, name))
                            .append(" */");
                    fs.append(System.lineSeparator());
                    fs.append("    @Attribute");
                    fs.append(System.lineSeparator());
                    if (ATTRIBUTE_RENAMING.containsKey(name)) {
                        fs.append("    @JsonProperty(\"").append(name).append("\")");
                        fs.append(System.lineSeparator());
                        name = ATTRIBUTE_RENAMING.get(name);
                    }
                    String javaType;
                    if (cardinality == AtlanCustomAttributeCardinality.SET) {
                        injectSingular(fs, name);
                        if (!type.startsWith("array<"))
                            log.warn("Unknown set type — defaulting to a SortedSet: {}", type);
                        javaType = "SortedSet<" + getBaseType(type) + ">";
                    } else {
                        javaType = NAME_MAPPINGS.getOrDefault(type, type);
                    }
                    fs.append("    ").append(javaType).append(" ").append(name).append(";");
                    fs.append(System.lineSeparator()).append(System.lineSeparator());
                }
            }
        }
    }

    private String getBaseType(String atlasType) {
        String type = atlasType.substring(atlasType.indexOf("<") + 1, atlasType.indexOf(">"));
        return NAME_MAPPINGS.getOrDefault(type, type);
    }

    private void addHeader(BufferedWriter fs, String typeName) throws IOException {
        fs.append("/* SPDX-License-Identifier: Apache-2.0 */").append(System.lineSeparator());
        fs.append("/* Copyright 2022 Atlan Pte. Ltd. */").append(System.lineSeparator());
        fs.append("package com.atlan.model.assets;").append(System.lineSeparator());
        fs.append(System.lineSeparator());
        fs.append("import com.atlan.exception.ErrorCode;").append(System.lineSeparator());
        fs.append("import com.atlan.exception.AtlanException;").append(System.lineSeparator());
        fs.append("import com.atlan.exception.NotFoundException;").append(System.lineSeparator());
        fs.append("import com.atlan.exception.InvalidRequestException;").append(System.lineSeparator());
        fs.append("import com.atlan.model.enums.*;").append(System.lineSeparator());
        fs.append("import com.atlan.model.relations.UniqueAttributes;").append(System.lineSeparator());
        fs.append("import com.fasterxml.jackson.annotation.JsonSubTypes;").append(System.lineSeparator());
        fs.append("import com.fasterxml.jackson.annotation.JsonProperty;").append(System.lineSeparator());
        fs.append("import java.util.Map;").append(System.lineSeparator());
        fs.append("import java.util.List;").append(System.lineSeparator());
        fs.append("import java.util.ArrayList;").append(System.lineSeparator());
        fs.append("import java.util.SortedSet;").append(System.lineSeparator());
        fs.append("import lombok.*;").append(System.lineSeparator());
        fs.append("import lombok.experimental.SuperBuilder;").append(System.lineSeparator());
        fs.append(System.lineSeparator());
        fs.append("/**").append(System.lineSeparator());
        fs.append(" * ").append(getTypeDescription(typeName)).append(System.lineSeparator());
        fs.append(" */").append(System.lineSeparator());
        fs.append("@Getter").append(System.lineSeparator());
        fs.append("@SuperBuilder(toBuilder = true)").append(System.lineSeparator());
        fs.append("@EqualsAndHashCode(callSuper = true)").append(System.lineSeparator());
    }

    private boolean addOpening(
            BufferedWriter fs, String typeName, String className, String classToExtend, List<String> subTypes)
            throws IOException {
        boolean hasSubTypes = (subTypes != null && !subTypes.isEmpty());
        if (hasSubTypes) {
            fs.append("@JsonSubTypes({");
            fs.append(System.lineSeparator());
            for (String subType : subTypes) {
                String subTypeClass = NAME_MAPPINGS.getOrDefault(subType, subType);
                String flattened = INHERITANCE_OVERRIDES.getOrDefault(subType, null);
                // Only output the subtype if that subtype still considers this type is parent,
                // after polymorphic flattening...
                if (!SKIP_GENERATING.contains(subType) && (flattened == null || flattened.equals(typeName))) {
                    fs.append("    @JsonSubTypes.Type(value = ")
                            .append(subTypeClass)
                            .append(".class, name = ")
                            .append(subTypeClass)
                            .append(".TYPE_NAME),");
                    fs.append(System.lineSeparator());
                }
            }
            fs.append("})").append(System.lineSeparator());
            if (typesWithMaps.contains(typeName)) {
                fs.append("@SuppressWarnings(\"cast\")").append(System.lineSeparator());
            }
            fs.append("public abstract class ");
        } else {
            if (typesWithMaps.contains(typeName)) {
                fs.append("@SuppressWarnings(\"cast\")").append(System.lineSeparator());
            }
            fs.append("public class ");
        }
        fs.append(className).append(" extends ").append(classToExtend).append(" {");
        if (!hasSubTypes) {
            fs.append(System.lineSeparator());
            fs.append("    private static final long serialVersionUID = 2L;");
        }
        fs.append(System.lineSeparator()).append(System.lineSeparator());
        fs.append("    public static final String TYPE_NAME = \"")
                .append(typeName)
                .append("\";");
        fs.append(System.lineSeparator()).append(System.lineSeparator());

        if (!hasSubTypes) {
            fs.append("    /** Fixed typeName for ")
                    .append(className)
                    .append("s. */")
                    .append(System.lineSeparator());
            fs.append("    @Getter(onMethod_ = {@Override})").append(System.lineSeparator());
            fs.append("    @Builder.Default").append(System.lineSeparator());
            fs.append("    String typeName = TYPE_NAME;").append(System.lineSeparator());
            fs.append(System.lineSeparator());
        }
        return hasSubTypes;
    }

    private void addClosing(BufferedWriter fs) throws IOException {
        fs.append("}");
        fs.append(System.lineSeparator());
    }

    private void createTestForType(EntityDef typeDetails) {

        String name = typeDetails.getName();

        log.info("Test for type: {}", name);

        String className = NAME_MAPPINGS.getOrDefault(name, name);
        String testClassName = className + "Test";

        // Write the file for any type that should not be ignored
        String filename = TEST_DIRECTORY + File.separator + testClassName + ".java";
        try (BufferedWriter fs =
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {

            addTestHeader(fs);
            addTestOpening(fs, testClassName);

            addMembers(fs, className, typeDetails);
            builderEquivalency(fs);
            serialization(fs);
            deserialization(fs, className);
            serializedEquivalency(fs);
            deserializedEquivalency(fs);

            addClosing(fs);

        } catch (IOException e) {
            log.error("Unable to open file output: {}", filename, e);
        }
    }

    private void addTestHeader(BufferedWriter fs) throws IOException {
        fs.append("/* SPDX-License-Identifier: Apache-2.0 */").append(System.lineSeparator());
        fs.append("/* Copyright 2022 Atlan Pte. Ltd. */").append(System.lineSeparator());
        fs.append("package com.atlan.model.assets;").append(System.lineSeparator());
        fs.append(System.lineSeparator());
        fs.append("import static org.testng.Assert.*;").append(System.lineSeparator());
        fs.append(System.lineSeparator());
        fs.append("import com.atlan.model.enums.*;").append(System.lineSeparator());
        fs.append("import java.util.*;").append(System.lineSeparator());
        fs.append("import com.atlan.serde.Serde;").append(System.lineSeparator());
        fs.append("import com.fasterxml.jackson.core.JsonProcessingException;").append(System.lineSeparator());
        fs.append("import org.testng.annotations.Test;").append(System.lineSeparator());
        fs.append(System.lineSeparator());
    }

    private void addTestOpening(BufferedWriter fs, String testClassName) throws IOException {
        fs.append("public class ").append(testClassName).append(" {");
        fs.append(System.lineSeparator()).append(System.lineSeparator());
    }

    private void addMembers(BufferedWriter fs, String className, EntityDef typeDetails) throws IOException {

        fs.append("    private static final ")
                .append(className)
                .append(" full = ")
                .append(className)
                .append(".builder()");
        fs.append(System.lineSeparator());
        fs.append("            .guid(\"guid\")").append(System.lineSeparator());
        fs.append("            .displayText(\"displayText\")").append(System.lineSeparator());
        fs.append("            .status(AtlanStatus.ACTIVE)").append(System.lineSeparator());
        fs.append("            .createdBy(\"createdBy\")").append(System.lineSeparator());
        fs.append("            .updatedBy(\"updatedBy\")").append(System.lineSeparator());
        fs.append("            .createTime(123456789L)").append(System.lineSeparator());
        fs.append("            .updateTime(123456789L)").append(System.lineSeparator());
        fs.append("            .isIncomplete(false)").append(System.lineSeparator());
        fs.append("            .qualifiedName(\"qualifiedName\")").append(System.lineSeparator());
        fs.append("            .name(\"name\")").append(System.lineSeparator());
        fs.append("            .displayName(\"displayName\")").append(System.lineSeparator());
        fs.append("            .description(\"description\")").append(System.lineSeparator());
        fs.append("            .userDescription(\"userDescription\")").append(System.lineSeparator());
        fs.append("            .tenantId(\"tenantId\")").append(System.lineSeparator());
        fs.append("            .certificateStatus(AtlanCertificateStatus.VERIFIED)")
                .append(System.lineSeparator());
        fs.append("            .certificateStatusMessage(\"certificateStatusMessage\")")
                .append(System.lineSeparator());
        fs.append("            .certificateUpdatedBy(\"certificateUpdatedBy\")").append(System.lineSeparator());
        fs.append("            .certificateUpdatedAt(123456789L)").append(System.lineSeparator());
        fs.append("            .announcementTitle(\"announcementTitle\")").append(System.lineSeparator());
        fs.append("            .announcementMessage(\"announcementMessage\")").append(System.lineSeparator());
        fs.append("            .announcementUpdatedAt(123456789L)").append(System.lineSeparator());
        fs.append("            .announcementUpdatedBy(\"announcementUpdatedBy\")")
                .append(System.lineSeparator());
        fs.append("            .announcementType(AtlanAnnouncementType.INFORMATION)")
                .append(System.lineSeparator());
        fs.append("            .ownerUser(\"ownerUser\")").append(System.lineSeparator());
        fs.append("            .ownerGroup(\"ownerGroup\")").append(System.lineSeparator());
        fs.append("            .adminUser(\"adminUser\")").append(System.lineSeparator());
        fs.append("            .adminGroup(\"adminGroup\")").append(System.lineSeparator());
        fs.append("            .adminRole(\"adminRole\")").append(System.lineSeparator());
        fs.append("            .viewerUser(\"viewerUser\")").append(System.lineSeparator());
        fs.append("            .viewerGroup(\"viewerGroup\")").append(System.lineSeparator());
        fs.append("            .connectorType(AtlanConnectorType.PRESTO)").append(System.lineSeparator());
        fs.append("            .connectionName(\"connectionName\")").append(System.lineSeparator());
        fs.append("            .connectionQualifiedName(\"connectionQualifiedName\")")
                .append(System.lineSeparator());
        fs.append("            .hasLineage(false)").append(System.lineSeparator());
        fs.append("            .isDiscoverable(true)").append(System.lineSeparator());
        fs.append("            .isEditable(true)").append(System.lineSeparator());
        fs.append("            .viewScore(123456.0)").append(System.lineSeparator());
        fs.append("            .popularityScore(123456.0)").append(System.lineSeparator());
        fs.append("            .sourceOwners(\"sourceOwners\")").append(System.lineSeparator());
        fs.append("            .sourceURL(\"sourceURL\")").append(System.lineSeparator());
        fs.append("            .sourceEmbedURL(\"sourceEmbedURL\")").append(System.lineSeparator());
        fs.append("            .lastSyncWorkflowName(\"lastSyncWorkflowName\")").append(System.lineSeparator());
        fs.append("            .lastSyncRunAt(123456789L)").append(System.lineSeparator());
        fs.append("            .lastSyncRun(\"lastSyncRun\")").append(System.lineSeparator());
        fs.append("            .sourceCreatedBy(\"sourceCreatedBy\")").append(System.lineSeparator());
        fs.append("            .sourceCreatedAt(123456789L)").append(System.lineSeparator());
        fs.append("            .sourceUpdatedAt(123456789L)").append(System.lineSeparator());
        fs.append("            .sourceUpdatedBy(\"sourceUpdatedBy\")").append(System.lineSeparator());
        fs.append("            .dbtQualifiedName(\"dbtQualifiedName\")").append(System.lineSeparator());
        fs.append("            .assetDbtAlias(\"assetDbtAlias\")").append(System.lineSeparator());
        fs.append("            .assetDbtMeta(\"assetDbtMeta\")").append(System.lineSeparator());
        fs.append("            .assetDbtUniqueId(\"assetDbtUniqueId\")").append(System.lineSeparator());
        fs.append("            .assetDbtAccountName(\"assetDbtAccountName\")").append(System.lineSeparator());
        fs.append("            .assetDbtProjectName(\"assetDbtProjectName\")").append(System.lineSeparator());
        fs.append("            .assetDbtPackageName(\"assetDbtPackageName\")").append(System.lineSeparator());
        fs.append("            .assetDbtJobName(\"assetDbtJobName\")").append(System.lineSeparator());
        fs.append("            .assetDbtJobSchedule(\"assetDbtJobSchedule\")").append(System.lineSeparator());
        fs.append("            .assetDbtJobStatus(\"assetDbtJobStatus\")").append(System.lineSeparator());
        fs.append("            .assetDbtJobScheduleCronHumanized(\"assetDbtJobScheduleCronHumanized\")")
                .append(System.lineSeparator());
        fs.append("            .assetDbtJobLastRun(123456789L)").append(System.lineSeparator());
        fs.append("            .assetDbtJobLastRunUrl(\"assetDbtJobLastRunUrl\")")
                .append(System.lineSeparator());
        fs.append("            .assetDbtJobLastRunCreatedAt(123456789L)").append(System.lineSeparator());
        fs.append("            .assetDbtJobLastRunUpdatedAt(123456789L)").append(System.lineSeparator());
        fs.append("            .assetDbtJobLastRunDequedAt(123456789L)").append(System.lineSeparator());
        fs.append("            .assetDbtJobLastRunStartedAt(123456789L)").append(System.lineSeparator());
        fs.append("            .assetDbtJobLastRunTotalDuration(\"assetDbtJobLastRunTotalDuration\")")
                .append(System.lineSeparator());
        fs.append("            .assetDbtJobLastRunTotalDurationHumanized(\"assetDbtJobLastRunTotalDurationHumanized\")")
                .append(System.lineSeparator());
        fs.append("            .assetDbtJobLastRunQueuedDuration(\"assetDbtJobLastRunQueuedDuration\")")
                .append(System.lineSeparator());
        fs.append(
                        "            .assetDbtJobLastRunQueuedDurationHumanized(\"assetDbtJobLastRunQueuedDurationHumanized\")")
                .append(System.lineSeparator());
        fs.append("            .assetDbtJobLastRunRunDuration(\"assetDbtJobLastRunRunDuration\")")
                .append(System.lineSeparator());
        fs.append("            .assetDbtJobLastRunRunDurationHumanized(\"assetDbtJobLastRunRunDurationHumanized\")")
                .append(System.lineSeparator());
        fs.append("            .assetDbtJobLastRunGitBranch(\"assetDbtJobLastRunGitBranch\")")
                .append(System.lineSeparator());
        fs.append("            .assetDbtJobLastRunGitSha(\"assetDbtJobLastRunGitSha\")")
                .append(System.lineSeparator());
        fs.append("            .assetDbtJobLastRunStatusMessage(\"assetDbtJobLastRunStatusMessage\")")
                .append(System.lineSeparator());
        fs.append("            .assetDbtJobLastRunOwnerThreadId(\"assetDbtJobLastRunOwnerThreadId\")")
                .append(System.lineSeparator());
        fs.append("            .assetDbtJobLastRunExecutedByThreadId(\"assetDbtJobLastRunExecutedByThreadId\")")
                .append(System.lineSeparator());
        fs.append("            .assetDbtJobLastRunArtifactsSaved(true)").append(System.lineSeparator());
        fs.append("            .assetDbtJobLastRunArtifactS3Path(\"assetDbtJobLastRunArtifactS3Path\")")
                .append(System.lineSeparator());
        fs.append("            .assetDbtJobLastRunHasDocsGenerated(false)").append(System.lineSeparator());
        fs.append("            .assetDbtJobLastRunHasSourcesGenerated(true)").append(System.lineSeparator());
        fs.append("            .assetDbtJobLastRunNotificationsSent(false)").append(System.lineSeparator());
        fs.append("            .assetDbtJobNextRun(123456789L)").append(System.lineSeparator());
        fs.append("            .assetDbtJobNextRunHumanized(\"assetDbtJobNextRunHumanized\")")
                .append(System.lineSeparator());
        fs.append("            .assetDbtEnvironmentName(\"assetDbtEnvironmentName\")")
                .append(System.lineSeparator());
        fs.append("            .assetDbtEnvironmentDbtVersion(\"assetDbtEnvironmentDbtVersion\")")
                .append(System.lineSeparator());
        fs.append("            .assetDbtTag(\"assetDbtTag1\")").append(System.lineSeparator());
        fs.append("            .assetDbtTag(\"assetDbtTag2\")").append(System.lineSeparator());
        fs.append("            .assetDbtSemanticLayerProxyUrl(\"assetDbtSemanticLayerProxyUrl\")")
                .append(System.lineSeparator());
        fs.append("            .link(Link.refByGuid(\"linkGuid1\"))").append(System.lineSeparator());
        fs.append("            .link(Link.refByGuid(\"linkGuid2\"))").append(System.lineSeparator());
        fs.append("            .readme(Readme.refByGuid(\"readmeGuid\"))").append(System.lineSeparator());
        fs.append("            .assignedTerm(GlossaryTerm.refByGuid(\"termGuid1\"))")
                .append(System.lineSeparator());
        fs.append("            .assignedTerm(GlossaryTerm.refByGuid(\"termGuid2\"))")
                .append(System.lineSeparator());
        addTestAttributes(fs, typeDetails);
        fs.append("            .build();");
        fs.append(System.lineSeparator());

        fs.append("    private static ").append(className).append(" frodo;").append(System.lineSeparator());
        fs.append("    private static String serialized;").append(System.lineSeparator());
        fs.append(System.lineSeparator());
    }

    private String getBuildableType(String typeName) {
        if (!concreteModels.contains(typeName) || SKIP_GENERATING.contains(typeName)) {
            // If it is an abstract type, re-type what we put into the reference
            // to a concrete type
            if (typeName.contains("Process")) {
                typeName = "LineageProcess";
            } else if (typeName.equals("Namespace")) {
                typeName = "Folder";
            } else if (typeName.equals("Metric")) {
                typeName = "DbtMetric";
            } else {
                typeName = "Table";
            }
        }
        return typeName;
    }

    private void addTestAttributes(BufferedWriter fs, EntityDef typeDetails) throws IOException {
        String typeName = typeDetails.getName();
        // We can short-circuit if we've hit Asset level, since those attributes are all already included
        if (!typeName.equals("Asset")) {
            List<String> superTypes = typeDetails.getSuperTypes();
            if (superTypes != null && !superTypes.isEmpty()) {
                String singleSuperType = getSingleTypeToExtend(typeName, superTypes);
                addTestAttributes(fs, entityDefCache.get(singleSuperType));
            }
            List<AttributeDef> attributes = typeDetails.getAttributeDefs();
            for (AttributeDef attribute : attributes) {
                String name = attribute.getName();
                String type = attribute.getTypeName();
                // Prefer a direct type mapping first...
                String mappedType = TYPE_MAPPINGS.getOrDefault(type, null);
                if (mappedType == null) {
                    // Failing that, attempt a renamed type (structs, enums)...
                    mappedType = NAME_MAPPINGS.getOrDefault(type, type);
                }
                if (!mappedType.equals("__internal")) {
                    if (ATTRIBUTE_RENAMING.containsKey(name)) {
                        name = ATTRIBUTE_RENAMING.get(name);
                    }
                    fs.append("            .").append(name).append("(");
                    switch (mappedType) {
                        case "String":
                            fs.append("\"").append(name).append("\"");
                            break;
                        case "Boolean":
                            fs.append(String.valueOf(ThreadLocalRandom.current().nextBoolean()));
                            break;
                        case "Integer":
                            fs.append(String.valueOf(ThreadLocalRandom.current().nextInt()));
                            break;
                        case "Long":
                            fs.append(String.valueOf(ThreadLocalRandom.current().nextLong()))
                                    .append("L");
                            break;
                        case "Double":
                            fs.append(String.valueOf(ThreadLocalRandom.current().nextDouble()));
                            break;
                        case "LinkIconType":
                            fs.append("LinkIconType.EMOJI");
                            break;
                        case "GoogleDataStudioAssetType":
                            fs.append("GoogleDataStudioAssetType.REPORT");
                            break;
                        case "PowerBIEndorsementType":
                            fs.append("PowerBIEndorsementType.PROMOTED");
                            break;
                        case "AWSTag":
                            fs.append("AWSTag.of(").append("\"key\", \"value\"").append(")");
                            break;
                        case "AzureTag":
                            fs.append("AzureTag.of(")
                                    .append("\"key\", \"value\"")
                                    .append(")");
                            break;
                        case "SortedSet<String>":
                            fs.append("Set.of(\"one\", \"two\", \"three\")");
                            break;
                        case "Map<String, String>":
                            fs.append("Map.of(\"key1\", \"value1\", \"key2\", \"value2\")");
                            break;
                        case "Map<String, Long>":
                            fs.append("Map.of(\"key1\", 123456L, \"key2\", 654321L)");
                            break;
                        case "List<Map<String, String>>":
                            fs.append("List.of(Map.of(\"key1\", \"value1\", \"key2\", \"value2\"))");
                            break;
                        default:
                            // Non-primitive type (reference to some other model)
                            mappedType = getBuildableType(mappedType);
                            fs.append(mappedType)
                                    .append(".refByGuid(\"")
                                    .append(UUID.randomUUID().toString())
                                    .append("\")");
                            break;
                    }
                    fs.append(")").append(System.lineSeparator());
                }
            }
            List<RelationshipAttributeDef> relationships = typeDetails.getRelationshipAttributeDefs();
            Set<String> uniqueRelationships = relationshipsForType.get(typeName);
            for (RelationshipAttributeDef attribute : relationships) {
                String name = attribute.getName();
                if (uniqueRelationships.contains(name)) {
                    String type = attribute.getTypeName();
                    if (!type.equals("__internal")) {
                        AtlanCustomAttributeCardinality cardinality = attribute.getCardinality();
                        if (ATTRIBUTE_RENAMING.containsKey(name)) {
                            name = ATTRIBUTE_RENAMING.get(name);
                        }
                        String baseType;
                        fs.append("            .").append(name).append("(");
                        if (cardinality == AtlanCustomAttributeCardinality.SET) {
                            if (!type.startsWith("array<"))
                                log.warn("Unknown set type — defaulting to a SortedSet: {}", type);
                            baseType = getBaseType(type);
                            baseType = getBuildableType(baseType);
                            fs.append("Set.of(");
                            fs.append(baseType)
                                    .append(".refByGuid(\"")
                                    .append(UUID.randomUUID().toString())
                                    .append("\"), ");
                            fs.append(baseType)
                                    .append(".refByGuid(\"")
                                    .append(UUID.randomUUID().toString())
                                    .append("\")");
                            fs.append(")");
                        } else {
                            baseType = NAME_MAPPINGS.getOrDefault(type, type);
                            baseType = getBuildableType(baseType);
                            fs.append(baseType)
                                    .append(".refByGuid(\"")
                                    .append(UUID.randomUUID().toString())
                                    .append("\")");
                        }
                        fs.append(")").append(System.lineSeparator());
                    }
                }
            }
        }
    }

    private void builderEquivalency(BufferedWriter fs) throws IOException {
        fs.append("    @Test(groups = {\"builderEquivalency\"})").append(System.lineSeparator());
        fs.append("    void builderEquivalency() {").append(System.lineSeparator());
        fs.append("        assertEquals(full.toBuilder().build(), full);").append(System.lineSeparator());
        fs.append("    }").append(System.lineSeparator());
        fs.append(System.lineSeparator());
    }

    private void serialization(BufferedWriter fs) throws IOException {
        fs.append("    @Test(").append(System.lineSeparator());
        fs.append("            groups = {\"serialize\"},").append(System.lineSeparator());
        fs.append("            dependsOnGroups = {\"builderEquivalency\"})").append(System.lineSeparator());
        fs.append("    void serialization() {").append(System.lineSeparator());
        fs.append("        assertNotNull(full);").append(System.lineSeparator());
        fs.append("        serialized = full.toJson();").append(System.lineSeparator());
        fs.append("        assertNotNull(serialized);").append(System.lineSeparator());
        fs.append("    }").append(System.lineSeparator());
        fs.append(System.lineSeparator());
    }

    private void deserialization(BufferedWriter fs, String className) throws IOException {
        fs.append("    @Test(").append(System.lineSeparator());
        fs.append("            groups = {\"deserialize\"},").append(System.lineSeparator());
        fs.append("            dependsOnGroups = {\"serialize\"})").append(System.lineSeparator());
        fs.append("    void deserialization() throws JsonProcessingException {").append(System.lineSeparator());
        fs.append("        assertNotNull(serialized);").append(System.lineSeparator());
        fs.append("        frodo = Serde.mapper.readValue(serialized, ")
                .append(className)
                .append(".class);")
                .append(System.lineSeparator());
        fs.append("        assertNotNull(frodo);").append(System.lineSeparator());
        fs.append("    }").append(System.lineSeparator());
        fs.append(System.lineSeparator());
    }

    private void serializedEquivalency(BufferedWriter fs) throws IOException {
        fs.append("    @Test(").append(System.lineSeparator());
        fs.append("            groups = {\"equivalency\"},").append(System.lineSeparator());
        fs.append("            dependsOnGroups = {\"serialize\", \"deserialize\"})")
                .append(System.lineSeparator());
        fs.append("    void serializedEquivalency() {").append(System.lineSeparator());
        fs.append("        assertNotNull(serialized);").append(System.lineSeparator());
        fs.append("        assertNotNull(frodo);").append(System.lineSeparator());
        fs.append("        String backAgain = frodo.toJson();").append(System.lineSeparator());
        fs.append("        assertEquals(backAgain, serialized, \"Serialization is not equivalent after serde loop,\");")
                .append(System.lineSeparator());
        fs.append("    }").append(System.lineSeparator());
        fs.append(System.lineSeparator());
    }

    private void deserializedEquivalency(BufferedWriter fs) throws IOException {
        fs.append("    @Test(").append(System.lineSeparator());
        fs.append("            groups = {\"equivalency\"},").append(System.lineSeparator());
        fs.append("            dependsOnGroups = {\"serialize\", \"deserialize\"})")
                .append(System.lineSeparator());
        fs.append("    void deserializedEquivalency() {").append(System.lineSeparator());
        fs.append("        assertNotNull(full);").append(System.lineSeparator());
        fs.append("        assertNotNull(frodo);").append(System.lineSeparator());
        fs.append("        assertEquals(frodo, full, \"Deserialization is not equivalent after serde loop,\");")
                .append(System.lineSeparator());
        fs.append("    }").append(System.lineSeparator());
        fs.append(System.lineSeparator());
    }
}
