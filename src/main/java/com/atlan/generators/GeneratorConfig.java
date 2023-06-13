/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.api.TypeDefsEndpoint;
import com.atlan.model.typedefs.TypeDef;
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

    @Getter
    private Configuration freemarkerConfig;

    @Getter
    private String packageRoot;

    @Getter
    private String packagePath;

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
     * Overrides the single parent class a given type should extend, since we do
     * not currently support polymorphic inheritance through the SDK.
     */
    @Singular
    private Map<String, String> parentForAssets;

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
     * Mapping of attributes that should have a different type than what appears in
     * the type definition, usually used to set a manually-maintained enumeration instead
     * of a free-form string value.
     */
    @Singular
    private Map<String, String> retypeAttributes;

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
        return GeneratorConfig.builder()
                .generatorName(generatorClass.getCanonicalName())
                .packageRoot(packageRoot)
                .freemarkerConfig(createConfig(directoryForTemplateLoading))
                .packagePath(createPackagePath(packageRoot));
    }

    /**
     * Configuration for generating using embedded templates in a jar file.
     *
     * @param generatorClass top-level class that is used to generate the code
     * @param packageRoot root Java package location for the generated models
     */
    public static GeneratorConfigBuilder creator(Class<?> generatorClass, String packageRoot) {
        return GeneratorConfig.builder()
                .generatorName(generatorClass.getCanonicalName())
                .packageRoot(packageRoot)
                .freemarkerConfig(createConfig())
                .packagePath(createPackagePath(packageRoot));
    }

    /**
     * Get the default configuration for the out-of-the-box Atlan model and SDK.
     *
     * @param generatorClass top-level class that is used to generate the code
     * @return default configuration
     */
    public static GeneratorConfigBuilder getDefault(Class<?> generatorClass) {
        return GeneratorConfig.creator(generatorClass, "com.atlan.model")
                .serviceTypes(TypeDefsEndpoint.RESERVED_SERVICE_TYPES)
                .preferTypeDefDescriptions(false)
                .renameClass("google_datastudio_asset_type", "GoogleDataStudioAssetType")
                .renameClass("powerbi_endorsement", "PowerBIEndorsementType")
                .renameClass("Referenceable", "Asset")
                .renameClass("Process", "LineageProcess")
                .renameClass("Collection", "AtlanCollection")
                .renameClass("Query", "AtlanQuery")
                .renameClass("AtlasGlossary", "Glossary")
                .renameClass("AtlasGlossaryCategory", "GlossaryCategory")
                .renameClass("AtlasGlossaryTerm", "GlossaryTerm")
                .renameClass("MaterialisedView", "MaterializedView")
                .renameEnumValue("ResolvingDNS", "RESOLVING_DNS")
                .renameEnumValue("RA-GRS", "RA_GRS")
                .doNotGenerateAsset("Referenceable")
                .doNotGenerateAsset("DataStudio")
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
                .parentForAsset("Asset", "Reference")
                .parentForAsset("S3", "AWS")
                .parentForAsset("DataStudioAsset", "Google")
                .parentForAsset("DbtColumnProcess", "ColumnProcess")
                .parentForAsset("DbtProcess", "Process")
                .parentForAsset("DbtMetric", "Metric")
                .parentForAsset("AWS", "Catalog")
                .parentForAsset("Google", "Catalog")
                .parentForAsset("Azure", "Catalog")
                .parentForAsset("GCS", "Google")
                .parentForAsset("ADLS", "Azure")
                .singularForAttribute("seeAlso", "seeAlsoOne")
                .singularForAttribute("replacedByTerm", "replacedByTerm")
                .singularForAttribute("validValuesFor", "validValueFor")
                .singularForAttribute("isA", "isATerm")
                .singularForAttribute("replacedBy", "replacedByTerm")
                .singularForAttribute("childrenCategories", "childCategory")
                .singularForAttribute("queryUserMap", "putQueryUserMap")
                .singularForAttribute("queryPreviewConfig", "putQueryPreviewConfig")
                .singularForAttribute("reportType", "putReportType")
                .singularForAttribute("projectHierarchy", "addProjectHierarchy")
                .singularForAttribute("certifier", "putCertifier")
                .singularForAttribute("presetChartFormData", "putPresetChartFormData")
                .singularForAttribute("resourceMetadata", "putResourceMetadata")
                .singularForAttribute("adlsObjectMetadata", "putAdlsObjectMetadata")
                .singularForAttribute("columnHistogram", "addColumnHistogram")
                .singularForAttribute("foreignKeyTo", "addForeignKeyTo")
                .singularForAttribute("quickSightFolderHierarchy", "addQuickSightFolderHierarchy")
                .singularForAttribute("columnMaxs", "addColumnMax")
                .singularForAttribute("columnMins", "addColumnMin")
                .singularForAttribute("redashQuerySchedule", "putRedashQuerySchedule")
                .singularForAttribute("mcMonitorRuleScheduleConfig", "addMcMonitorRuleSchedule")
                .singularForAttribute("policyValiditySchedule", "addPolicyValiditySchedule")
                .singularForAttribute("authServiceConfig", "putAuthServiceConfig")
                .singularForAttribute("microStrategyLocation", "putMicroStrategyLocation")
                .renameAttribute("connectorName", "connectorType")
                .renameAttribute("__hasLineage", "hasLineage")
                .renameAttribute("viewsCount", "viewCount")
                .renameAttribute("materialisedView", "materializedView")
                .renameAttribute("materialisedViews", "materializedViews")
                .renameAttribute("atlanSchema", "schema")
                .renameAttribute("sourceQueryComputeCostList", "sourceQueryComputeCosts")
                .renameAttribute("sourceReadTopUserList", "sourceReadTopUsers")
                .renameAttribute("sourceReadRecentUserList", "sourceReadRecentUsers")
                .renameAttribute("sourceReadRecentUserRecordList", "sourceReadRecentUserRecords")
                .renameAttribute("sourceReadTopUserRecordList", "sourceReadTopUserRecords")
                .renameAttribute("sourceReadPopularQueryRecordList", "sourceReadPopularQueryRecords")
                .renameAttribute("sourceReadExpensiveQueryRecordList", "sourceReadExpensiveQueryRecords")
                .renameAttribute("sourceReadSlowQueryRecordList", "sourceReadSlowQueryRecords")
                .renameAttribute("sourceQueryComputeCostRecordList", "sourceQueryComputeCostRecords")
                .renameAttribute("meanings", "assignedTerms")
                .renameAttribute("sqlAsset", "primarySqlAsset")
                .renameAttribute("mappedClassificationName", "mappedAtlanTagName")
                .renameAttribute("purposeClassifications", "purposeAtlanTags")
                .retypeAttribute("announcementType", "AtlanAnnouncementType")
                .retypeAttribute("connectorName", "AtlanConnectorType")
                .retypeAttribute("category", "AtlanConnectionCategory")
                .retypeAttribute("policyCategory", "AuthPolicyCategory")
                .retypeAttribute("policyResourceCategory", "AuthPolicyResourceCategory")
                .retypeAttribute("policyActions", "AtlanPolicyAction")
                .retypeAttribute("denyAssetTabs", "AssetSidebarTab")
                .retypeAttribute("policyMaskType", "DataMaskingType");
    }

    /**
     * Whether to generate information for the given type definition.
     *
     * @param typeDef type definition to consider for inclusion
     * @return true if information should be generated for this typedef, otherwise false
     */
    public boolean includeTypedef(TypeDef typeDef) {
        if (typeDef == null || typeDef.getServiceType() == null) {
            return false;
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
        return renameClasses.getOrDefault(originalName, getUpperCamelCase(originalName));
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
     * Retrieve the name of the singular type that we should extend for inheritance.
     *
     * @param originalName unmodified name of the type definition to determine inheritance for
     * @param superTypes list of super types that are defined for that type
     * @return the name of a single type to use for inheritance
     */
    public String getSingleTypeToExtend(String originalName, List<String> superTypes) {
        if (parentForAssets.containsKey(originalName)) {
            return parentForAssets.get(originalName);
        } else if (superTypes == null || superTypes.isEmpty()) {
            return "AtlanObject";
        } else if (superTypes.size() == 1) {
            return superTypes.get(0);
        } else {
            log.warn("Multiple superTypes detected — returning only the first: {}", superTypes);
            return superTypes.get(0);
        }
    }

    /**
     * Resolve the provided name to an attribute name, renaming it if configured and otherwise
     * just lowerCamelCasing the name.
     *
     * @param originalName unmodified name of the attribute definition
     * @return the resolved name for the attribute in the POJO
     */
    public String resolveAttributeName(String originalName) {
        return renameAttributes.getOrDefault(originalName, getLowerCamelCase(originalName));
    }

    /**
     * Resolve the type of the attribute to a manually-maintained enumeration, if configured,
     * or return null if there is no enumeration for this attribute.
     *
     * @param originalName unmodified name of the attribute definition
     * @return the resolved enumeration name for the attribute in the POJO, or null if not referring to an enumeration
     */
    public String resolveAttributeToEnumeration(String originalName) {
        return retypeAttributes.getOrDefault(originalName, null);
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
        } else if (value.toUpperCase().equals(value)) {
            return value;
        }
        String[] words = value.split("[\\W-]+");
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
            word = word.isEmpty() ? word : word.toUpperCase();
            builder.append(word).append("_");
        }
        String built = builder.toString();
        return built.endsWith("_") ? built.substring(0, built.length() - 1) : built;
    }

    private static String getUpperCamelCase(String text) {
        String[] words = text.split("[\\W_]+");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            word = word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1);
            builder.append(word);
        }
        return builder.toString();
    }

    protected static String getLowerCamelCase(String text) {
        String[] words = text.split("[\\W_]+");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                word = word.isEmpty() ? word : Character.toLowerCase(word.charAt(0)) + word.substring(1);
            } else {
                word = word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1);
            }
            builder.append(word);
        }
        return builder.toString();
    }

    private static Configuration createConfig(File directoryForTemplateLoading) throws IOException {
        Configuration cfg = createConfigBase();
        cfg.setDirectoryForTemplateLoading(directoryForTemplateLoading);
        return cfg;
    }

    private static Configuration createConfig() {
        Configuration cfg = createConfigBase();
        cfg.setClassLoaderForTemplateLoading(GeneratorConfig.class.getClassLoader(), "templates");
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

    private static String createPackagePath(String packageRoot) {
        return "src" + File.separator
                + "main" + File.separator
                + "java" + File.separator
                + packageRoot.replaceAll("\\.", File.separator);
    }
}
