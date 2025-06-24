/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.api.TypeDefsEndpoint;
import com.atlan.model.typedefs.TypeDef;
import com.atlan.util.StringUtils;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;

/**
 * Class to encapsulate configuration for the generators.
 */
@Builder
@Slf4j
@SuppressWarnings("cast")
public class GeneratorConfig {

    private static final Pattern WORD_FINDER = Pattern.compile("(([A-Z]?[a-z0-9]+)|([A-Z0-9]))");

    private static final Map<String, String> DEFAULT_CLASS_RENAMES = Map.ofEntries(
            Map.entry("google_datastudio_asset_type", "GoogleDataStudioAssetType"),
            Map.entry("powerbi_endorsement", "PowerBIEndorsementType"),
            Map.entry("Referenceable", "Asset"),
            Map.entry("Process", "LineageProcess"),
            Map.entry("Collection", "AtlanCollection"),
            Map.entry("Query", "AtlanQuery"),
            Map.entry("AtlasGlossary", "Glossary"),
            Map.entry("AtlasGlossaryCategory", "GlossaryCategory"),
            Map.entry("AtlasGlossaryTerm", "GlossaryTerm"),
            Map.entry("AtlasGlossaryAntonym", "GlossaryAntonym"),
            Map.entry("AtlasGlossarySynonym", "GlossarySynonym"),
            Map.entry("AtlasGlossaryIsARelationship", "GlossaryIsA"),
            Map.entry("AtlasGlossaryPreferredTerm", "GlossaryPreferredTerm"),
            Map.entry("AtlasGlossaryRelatedTerm", "GlossaryRelatedTerm"),
            Map.entry("AtlasGlossaryReplacementTerm", "GlossaryReplacementTerm"),
            Map.entry("AtlasGlossarySemanticAssignment", "GlossarySemanticAssignment"),
            Map.entry("AtlasGlossaryTermCategorization", "GlossaryTermCategorization"),
            Map.entry("AtlasGlossaryTranslation", "GlossaryTranslation"),
            Map.entry("AtlasGlossaryValidValue", "GlossaryValidValue"),
            Map.entry("MaterialisedView", "MaterializedView"));

    private static final Map<String, String> DEFAULT_SINGULARS = Map.ofEntries(
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
            Map.entry("adlsObjectMetadata", "putAdlsObjectMetadata"),
            Map.entry("foreignKeyTo", "addForeignKeyTo"),
            Map.entry("quickSightFolderHierarchy", "addQuickSightFolderHierarchy"),
            Map.entry("columnMaxs", "addColumnMax"),
            Map.entry("columnMins", "addColumnMin"),
            Map.entry("redashQuerySchedule", "putRedashQuerySchedule"),
            Map.entry("policyValiditySchedule", "addPolicyValiditySchedule"),
            Map.entry("authServiceConfig", "putAuthServiceConfig"),
            Map.entry("microStrategyLocation", "putMicroStrategyLocation"),
            Map.entry("starredBy", "addStarredBy"),
            Map.entry("matillionComponentLinkedJob", "putMatillionComponentLinkedJob"),
            Map.entry("matillionVersions", "addMatillionVersion"),
            Map.entry("cogniteTimeseries", "addCogniteTimeseries"),
            Map.entry("exceptionsForBusinessPolicy", "exceptionForBusinessPolicy"),
            Map.entry("supersetChartFormData", "putSupersetChartFormData"),
            Map.entry("columnHierarchy", "putColumnHierarchy"),
            Map.entry("bigqueryTagHierarchy", "putBigqueryTagHierarchy"),
            Map.entry("applications", "aiApplication"),
            Map.entry("sapErpFunctionExceptionList", "sapErpFunctionException"),
            Map.entry("cassandraViewCaching", "putCassandraViewCaching"),
            Map.entry("cassandraViewCompaction", "putCassandraViewCompaction"),
            Map.entry("cassandraTableCaching", "putCassandraTableCaching"),
            Map.entry("cassandraTableCompaction", "putCassandraTableCompaction"),
            Map.entry("cassandraTableCompression", "putCassandraTableCompression"),
            Map.entry("cassandraKeyspaceReplication", "putCassandraKeyspaceReplication"),
            Map.entry("responseValueArrString", "addResponseValueString"),
            Map.entry("responseValueArrInt", "addResponseValueInt"),
            Map.entry("responseValueArrBoolean", "addResponseValueBoolean"),
            Map.entry("responseValueArrJson", "addResponseValueJson"),
            Map.entry("responseValueArrLong", "addResponseValueLong"),
            Map.entry("responseValueArrDate", "addResponseValueDate"),
            Map.entry("tableauProjectHierarchy", "addTableauProjectHierarchy"));

    private static final Map<String, String> DEFAULT_ATTRIBUTE_RENAMES = Map.ofEntries(
            Map.entry("__hasLineage", "hasLineage"),
            Map.entry("viewsCount", "viewCount"),
            Map.entry("materialisedView", "materializedView"),
            Map.entry("materialisedViews", "materializedViews"),
            Map.entry("atlanSchema", "schema"),
            Map.entry("sourceQueryComputeCostList", "sourceQueryComputeCosts"),
            Map.entry("sourceReadTopUserList", "sourceReadTopUsers"),
            Map.entry("sourceReadRecentUserList", "sourceReadRecentUsers"),
            Map.entry("sourceReadRecentUserRecordList", "sourceReadRecentUserRecords"),
            Map.entry("sourceReadTopUserRecordList", "sourceReadTopUserRecords"),
            Map.entry("sourceReadPopularQueryRecordList", "sourceReadPopularQueryRecords"),
            Map.entry("sourceReadExpensiveQueryRecordList", "sourceReadExpensiveQueryRecords"),
            Map.entry("sourceReadSlowQueryRecordList", "sourceReadSlowQueryRecords"),
            Map.entry("sourceQueryComputeCostRecordList", "sourceQueryComputeCostRecords"),
            Map.entry("meanings", "assignedTerms"),
            Map.entry("sqlAsset", "primarySqlAsset"),
            Map.entry("mappedClassificationName", "mappedAtlanTagName"),
            Map.entry("purposeClassifications", "purposeAtlanTags"),
            Map.entry("starredDetailsList", "starredDetails"),
            Map.entry("sourceTagValue", "sourceTagValues"),
            Map.entry("userDefRelationshipFrom", "userDefRelationshipFroms"),
            Map.entry("userDefRelationshipTo", "userDefRelationshipTos"),
            Map.entry("adfActivityPrecedingDependency", "adfActivityPrecedingDependencies"),
            Map.entry("modelEntityImplemented", "modelImplementedEntities"),
            Map.entry("bprValue", "bprValues"),
            Map.entry("parentConnectionProcessQualifiedName", "parentConnectionProcessQualifiedNames"));

    private static final Map<String, String> DEFAULT_ATTRIBUTE_ENUMS = Map.ofEntries(
            Map.entry("announcementType", "AtlanAnnouncementType"),
            Map.entry("category", "AtlanConnectionCategory"),
            Map.entry("policyCategory", "AuthPolicyCategory"),
            Map.entry("policyResourceCategory", "AuthPolicyResourceCategory"),
            Map.entry("policyActions", "AtlanPolicyAction"),
            Map.entry("denyAssetTabs", "AssetSidebarTab"),
            Map.entry("denyAssetFilters", "AssetFilterGroup"),
            Map.entry("policyMaskType", "DataMaskingType"),
            Map.entry("assetIcon", "AtlanIcon"));

    private static final Map<String, Map<String, String>> DEFAULT_TYPE_OVERRIDES =
            Map.of("TableauDatasource", Map.of("fields", "TableauField"));

    private static final Map<String, String> DEFAULT_SEARCHABLE_RELATIONSHIPS = Map.of(
            "readme", "asset_readme",
            "links", "asset_links");

    // These are built-in structs, with no serviceType defined
    static final Set<String> BUILT_IN_STRUCTS = Set.of("Histogram", "ColumnValueFrequencyMap");

    // These are built-in enums, self-managed (not persisted in Atlas)
    static final Set<String> BUILT_IN_ENUMS = Set.of(
            "AtlanAnnouncementType",
            "AtlanConnectionCategory",
            "AtlanConnectorType",
            "AtlanDeleteType",
            "AtlanIcon",
            "AtlanStatus",
            "AtlanPolicyAction",
            "PersonaMetadataAction",
            "PersonaGlossaryAction",
            "PurposeMetadataAction",
            "DataAction",
            "CertificateStatus");

    @Getter
    private Configuration freemarkerConfig;

    @Getter
    private String packageRoot;

    @Getter
    private String packagePath;

    @Getter
    private String testPath;

    @Getter
    private String generatorName;

    @Getter
    @Builder.Default
    private boolean preferTypeDefDescriptions = true;

    /**
     * Type definition serviceTypes for which to generate model POJOs.
     */
    @Singular
    private Set<String> serviceTypes;

    /**
     * Mapping of classes that should be renamed, either to overcome language
     * keywords (reserved words) or for consistency.
     */
    @Singular
    private Map<String, String> renameClasses;

    /**
     * Enumeration values that should be renamed, usually to avoid excessive
     * underscores when the value is already mostly capital letters.
     */
    @Singular
    private Map<String, String> renameEnumValues;

    /**
     * Assets ƒor which a model POJO should not be generated.
     */
    @Singular
    private Set<String> doNotGenerateAssets;

    /**
     * Assets that have subtypes, but should themselves also be treated as concrete
     * models (POJOs) and not as abstract classes.
     */
    @Singular
    private Set<String> forceNonAbstractAssets;

    /**
     * Mapping of attributes that need an explicit singular form (word), when it
     * cannot automatically be inferred by Lombok.
     */
    @Singular
    private Map<String, String> singularForAttributes;

    /**
     * Mapping of attributes that should be renamed, either to overcome language keywords
     * (reserved words) or for consistency.
     */
    @Singular
    private Map<String, String> renameAttributes;

    /**
     * Mapping of attributes that should have an enumerated type rather than a primitive type
     * (typically String). This is usually used to set a manually-maintained enumeration that
     * overrides such primitive types found in the typedef itself.
     */
    @Singular
    private Map<String, String> attributeToEnums;

    /**
     * Mapping of type to attribute to new type to use for that attribute. This is used to override
     * where the same attribute may be overloaded in the typedef (from being inherited from multiple
     * parents with different types from each). Usually this is used to manually-maintain a super-interface
     * that can cover both inherited types.
     */
    @Singular
    private Map<String, Map<String, String>> retypeAttributes;

    /**
     * Mapping of relationships that can be searched via ElasticSearch.
     * Keyed by the name of the relationship attribute in Atlan, with the value being the name of the
     * type of relationship that attribute captures (as it will be referred to in ElasticSearch).
     */
    @Singular
    private Map<String, String> searchableRelationships;

    /**
     * Configuration for generating using embedded templates in a jar file.
     *
     * @param generatorClass top-level class that is used to generate the code
     * @param packageRoot root Java package location for the generated models
     * @throws IOException if there is any problem configuring the templates through the classloader
     */
    public static GeneratorConfigBuilder creator(Class<?> generatorClass, String packageRoot) throws IOException {
        return creator(generatorClass, null, packageRoot);
    }

    /**
     * Configuration for generating using local directory for templates.
     *
     * @param generatorClass top-level class that is used to generate the code
     * @param directoryForTemplateLoading directory containing the Freemarker templates
     * @param packageRoot root Java package location for the generated models
     * @throws IOException if the directory cannot be accessed
     */
    public static GeneratorConfigBuilder creator(
            Class<?> generatorClass, File directoryForTemplateLoading, String packageRoot) throws IOException {
        return creator(generatorClass, directoryForTemplateLoading, packageRoot, null);
    }

    /**
     * Configuration for generating using local directory for templates.
     *
     * @param generatorClass top-level class that is used to generate the code
     * @param directoryForTemplateLoading directory containing the Freemarker templates
     * @param packageRoot root Java package location for the generated models
     * @param projectName (optional) project name for multi-project builds
     * @throws IOException if the directory cannot be accessed
     */
    public static GeneratorConfigBuilder creator(
            Class<?> generatorClass, File directoryForTemplateLoading, String packageRoot, String projectName)
            throws IOException {
        return GeneratorConfig.builder()
                .generatorName(generatorClass.getCanonicalName())
                .packageRoot(packageRoot)
                .freemarkerConfig(createConfig(directoryForTemplateLoading))
                .packagePath(createPackagePath(packageRoot, projectName))
                .testPath(createTestPath(packageRoot, projectName))
                .renameClasses(DEFAULT_CLASS_RENAMES)
                .singularForAttributes(DEFAULT_SINGULARS)
                .renameAttributes(DEFAULT_ATTRIBUTE_RENAMES)
                .attributeToEnums(DEFAULT_ATTRIBUTE_ENUMS)
                .retypeAttributes(DEFAULT_TYPE_OVERRIDES)
                .searchableRelationships(DEFAULT_SEARCHABLE_RELATIONSHIPS);
    }

    /**
     * Get the default configuration for the out-of-the-box Atlan model and SDK.
     *
     * @param generatorClass top-level class that is used to generate the code
     * @return default configuration
     * @throws IOException if there is any problem configuring the templates through the classloader
     */
    public static GeneratorConfigBuilder getDefault(Class<?> generatorClass) throws IOException {
        return getDefault(generatorClass, null);
    }

    /**
     * Get the default configuration for the out-of-the-box Atlan model and SDK.
     *
     * @param generatorClass top-level class that is used to generate the code
     * @param projectName (optional) project name, for multi-project builds
     * @return default configuration
     * @throws IOException if there is any problem configuring the templates through the classloader
     */
    public static GeneratorConfigBuilder getDefault(Class<?> generatorClass, String projectName) throws IOException {
        return GeneratorConfig.creator(generatorClass, null, "com.atlan.model", projectName)
                .serviceTypes(TypeDefsEndpoint.RESERVED_SERVICE_TYPES)
                .preferTypeDefDescriptions(false)
                .renameClasses(DEFAULT_CLASS_RENAMES)
                .singularForAttributes(DEFAULT_SINGULARS)
                .renameAttributes(DEFAULT_ATTRIBUTE_RENAMES)
                .attributeToEnums(DEFAULT_ATTRIBUTE_ENUMS)
                .retypeAttributes(DEFAULT_TYPE_OVERRIDES)
                .searchableRelationships(DEFAULT_SEARCHABLE_RELATIONSHIPS)
                .renameEnumValue("ResolvingDNS", "RESOLVING_DNS")
                .renameEnumValue("RA-GRS", "RA_GRS")
                .doNotGenerateAsset("Referenceable")
                .doNotGenerateAsset("AtlasServer")
                .doNotGenerateAsset("DataSet")
                .doNotGenerateAsset("Infrastructure")
                .doNotGenerateAsset("ProcessExecution")
                .doNotGenerateAsset("__AtlasAuditEntry")
                .doNotGenerateAsset("__AtlasUserProfile")
                .doNotGenerateAsset("__AtlasUserSavedSearch")
                .doNotGenerateAsset("__ExportImportAuditEntry")
                .doNotGenerateAsset("__internal")
                .forceNonAbstractAsset("Process")
                .forceNonAbstractAsset("ColumnProcess")
                .forceNonAbstractAsset("QlikSpace")
                .forceNonAbstractAsset("KafkaTopic")
                .forceNonAbstractAsset("KafkaConsumerGroup")
                .forceNonAbstractAsset("Table")
                .forceNonAbstractAsset("Column")
                .forceNonAbstractAsset("Database")
                .forceNonAbstractAsset("MongoDBDatabase")
                .forceNonAbstractAsset("MongoDBCollection")
                .forceNonAbstractAsset("Persona");
    }

    /**
     * Whether to generate information for the given type definition.
     *
     * @param typeDef type definition to consider for inclusion
     * @return true if information should be generated for this typedef, otherwise false
     */
    public boolean includeTypedef(TypeDef typeDef) {
        if (typeDef == null || typeDef.getServiceType() == null) {
            return true;
        } else {
            return serviceTypes.contains(typeDef.getServiceType()) && !doNotGenerateAssets.contains(typeDef.getName());
        }
    }

    /**
     * Resolve the provided name to a class name, renaming it if configured and otherwise
     * just UpperCamelCasing the name.
     *
     * @param originalName unmodified name of the type definition
     * @return the resolved name for the POJO class for the type's model
     */
    public String resolveClassName(String originalName) {
        return renameClasses.getOrDefault(originalName, StringUtils.getUpperCamelCase(originalName));
    }

    /**
     * Whether to force the creation of a concrete (non-abstract) POJO class for the
     * given type definition.
     *
     * @param originalName unmodified name of the type definition
     * @return true if a concrete (non-abstract) POJO should be generated for the type definition
     */
    public boolean forceNonAbstract(String originalName) {
        return forceNonAbstractAssets.contains(originalName);
    }

    /**
     * Resolve the provided name to an attribute name, renaming it if configured and otherwise
     * just lowerCamelCasing the name.
     *
     * @param originalName unmodified name of the attribute definition
     * @return the resolved name for the attribute in the POJO
     */
    public String resolveAttributeName(String originalName) {
        return renameAttributes.getOrDefault(originalName, StringUtils.getLowerCamelCase(originalName));
    }

    /**
     * Resolve the type of the attribute to a manually-maintained enumeration, if configured,
     * or return null if there is no enumeration for this attribute.
     *
     * @param originalName unmodified name of the attribute definition
     * @return the resolved enumeration name for the attribute in the POJO, or null if not referring to an enumeration
     */
    public String resolveAttributeToEnumeration(String originalName) {
        return attributeToEnums.getOrDefault(originalName, null);
    }

    /**
     * Resolve the type of the attribute to an overridden type, if configured, or return null
     * if there is no type override for this attribute.
     *
     * @param typeName unmodified name of the type
     * @param attributeName unmodified name of the attribute definition
     * @return the resolved type override for the POJO, or null if no override
     */
    public String resolveAttributeToTypeOverride(String typeName, String attributeName) {
        if (retypeAttributes.containsKey(typeName)) {
            return retypeAttributes.get(typeName).getOrDefault(attributeName, null);
        }
        return null;
    }

    /**
     * Resolve the attribute to a singular form of it for use with builders (Lombok).
     *
     * @param originalName unmodified name of the attribute definition
     * @return the resolved singular form of the word, or an empty string if Lombok can auto-singularize it
     */
    public String resolveSingular(String originalName) {
        return singularForAttributes.getOrDefault(originalName, "");
    }

    /**
     * Resolve the enumeration value to an all-caps underscored form to meet Java conventions.
     *
     * @param value unmodified value of the enumeration
     * @return the unique name for the enumeration matching Java conventions
     */
    public String resolveEnumValue(String value) {
        if (renameEnumValues.containsKey(value)) {
            return renameEnumValues.get(value);
        } else if (value.toUpperCase(Locale.ROOT).equals(value) && !value.contains(" ") && !value.contains("-")) {
            return value;
        }
        String[] words = value.split("[-\\s]");
        if (words.length == 1) {
            List<String> camelCaseWords = new ArrayList<>();
            Matcher matcher = WORD_FINDER.matcher(value);
            while (matcher.find()) {
                camelCaseWords.add(matcher.group(0));
            }
            words = camelCaseWords.toArray(new String[0]);
        }
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            word = word.isEmpty() ? word : word.toUpperCase(Locale.ROOT);
            builder.append(word).append("_");
        }
        String built = builder.toString();
        return built.endsWith("_") ? built.substring(0, built.length() - 1) : built;
    }

    /**
     * Retrieve the relationship type name for any searchable relationship (will be a limited
     * set of the overall relationships).
     *
     * @param field name of the relationship attribute in Atlan
     * @return the relationship type name, if the attribute is searchable, otherwise null
     */
    public String getSearchableRelationship(String field) {
        return searchableRelationships.getOrDefault(field, null);
    }

    private static Configuration createConfig(File directoryForTemplateLoading) throws IOException {
        Configuration cfg = createConfigBase();
        ClassTemplateLoader ctl = new ClassTemplateLoader(GeneratorConfig.class.getClassLoader(), "templates");
        FileTemplateLoader ftl = null;
        if (directoryForTemplateLoading != null && directoryForTemplateLoading.exists()) {
            ftl = new FileTemplateLoader(directoryForTemplateLoading);
        }
        if (ftl != null) {
            // If a directory has been provided, configure it as a fallback location in which to
            // look for templates, but still use the classloader as the first place to go for
            // the templates
            MultiTemplateLoader mtl = new MultiTemplateLoader(new TemplateLoader[] {ctl, ftl});
            cfg.setTemplateLoader(mtl);
        } else {
            cfg.setTemplateLoader(ctl);
        }
        return cfg;
    }

    private static Configuration createConfigBase() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
        cfg.setSQLDateAndTimeTimeZone(TimeZone.getDefault());
        return cfg;
    }

    private static String createPackagePath(String packageRoot, String projectName) {
        return projectName == null
                ? createPath("src" + File.separator + "main" + File.separator + "java" + File.separator, packageRoot)
                : createPath(
                        projectName + File.separator + "src" + File.separator + "main" + File.separator + "java"
                                + File.separator,
                        packageRoot);
    }

    private static String createTestPath(String packageRoot, String projectName) {
        return projectName == null
                ? createPath("src" + File.separator + "test" + File.separator + "java" + File.separator, packageRoot)
                : createPath(
                        projectName + File.separator + "src" + File.separator + "test" + File.separator + "java"
                                + File.separator,
                        packageRoot);
    }

    private static String createPath(String prefix, String packageRoot) {
        return prefix + File.separator + packageRoot.replaceAll("\\.", File.separator);
    }
}
