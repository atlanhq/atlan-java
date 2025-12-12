/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.generators.lombok.Singulars;
import com.atlan.model.enums.AtlanEnum;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class AssetTestGenerator extends AssetGenerator {

    public static final String DIRECTORY = "assets";

    private static final String ASSET_GUID = "705d96f4-bdb6-4792-8dfe-8dc4ca3d2c23";
    private static final String ASSET_QN = "default/snowflake/1234567890/test/qualifiedName";

    protected final AssetGenerator asset;
    protected final List<TestAttribute> testAttributes;

    public AssetTestGenerator(AssetGenerator asset, GeneratorConfig cfg) {
        super(asset.getClient(), asset.getEntityDef(), cfg);
        this.asset = asset;
        this.testAttributes = new ArrayList<>();
    }

    @Override
    public void resolveDetails() {
        super.resolveDetails();
        addTestAttributes(asset);
    }

    @Getter
    @Builder
    public static final class TestAttribute {
        private SearchableAttribute<?> details;
        private String builderMethod;
        private List<String> values;
        private List<String> rawValues;
        private boolean inherited;
        private boolean relationship;
        private String relatedTypeOriginal;
    }

    private void addTestAttributes(AssetGenerator assetGenerator) {
        Set<String> superTypes = assetGenerator.getSuperTypes();
        if (superTypes != null && !superTypes.isEmpty()) {
            for (String superType : superTypes) {
                if (superType != null && !superType.equals("Referenceable")) {
                    // We can short-circuit when the next level up is Referenceable (the top)
                    addTestAttributes(cache.getCachedAssetType(superType), true);
                }
            }
        }
        // Add attributes for this class itself
        addTestAttributes(assetGenerator, false);
    }

    private void addTestAttributes(AssetGenerator assetGenerator, boolean fromSuperType) {
        Set<SearchableAttribute<?>> attributes = assetGenerator.getNonInheritedAttributes();
        if (attributes != null) {
            for (SearchableAttribute<?> attribute : attributes) {
                TestAttribute.TestAttributeBuilder builder =
                        TestAttribute.builder().details(attribute);
                MappedType type = attribute.getType();
                boolean multiValued = attribute.getSingular() != null;
                String renamedAttr = attribute.getRenamed();
                if (!renamedAttr.equals("serialVersionUID")) {
                    String builderMethod = renamedAttr;
                    if (multiValued) {
                        // If the attribute can be multivalued, figure out the singular form of the
                        // attribute's name
                        if (attribute.getSingular().isEmpty()) {
                            builderMethod = Singulars.autoSingularize(renamedAttr);
                        } else {
                            builderMethod = attribute.getSingular();
                        }
                    }
                    builder.builderMethod(builderMethod).inherited(fromSuperType);
                    switch (type.getType()) {
                        case PRIMITIVE:
                            addPrimitive(builder, multiValued, type.getName(), type.getContainer());
                            break;
                        case ENUM:
                            addEnum(builder, multiValued, type.getName());
                            break;
                        case ASSET:
                            builder.relatedTypeOriginal(type.getOriginalBase());
                            if (!attribute.getRetyped()) {
                                addAssetRef(builder, multiValued, type.getName());
                            } else {
                                // If the attribute was retyped, use the original base type for
                                // generating test values, or we'll end up with a non-existent
                                // abstract class for the test
                                addAssetRef(builder, multiValued, type.getOriginalBase());
                            }
                            break;
                        case STRUCT:
                            addStructRef(builder, multiValued, type.getName(), type.getContainer());
                            break;
                        default:
                            log.warn("Unhandled testing type {} - skipping.", type.getType());
                            break;
                    }
                }
            }
        } else if (!assetGenerator.getOriginalName().equals("Referenceable")) {
            log.warn("No attributes found for {}, skipping any test inclusion.", assetGenerator.getOriginalName());
        }
    }

    private void addPrimitive(
            TestAttribute.TestAttributeBuilder builder, boolean multiValued, String typeName, String containerName) {
        builder.relationship(false);
        if (!multiValued) {
            testAttributes.add(builder.values(List.of(getPrimitiveValue(containerName, typeName, 0)))
                    .rawValues(List.of(getRawPrimitiveValue(containerName, typeName, 0)))
                    .build());
        } else {
            testAttributes.add(builder.values(List.of(
                            getPrimitiveValue(containerName, typeName, 0),
                            getPrimitiveValue(containerName, typeName, 1)))
                    .rawValues(List.of(
                            getRawPrimitiveValue(containerName, typeName, 0),
                            getRawPrimitiveValue(containerName, typeName, 1)))
                    .build());
        }
    }

    private String getPrimitiveValue(String containerName, String typeName, int count) {
        String value = null;
        switch (typeName) {
            case "String":
                value = "\"" + typeName + count + "\"";
                break;
            case "Boolean":
                if (Math.floorMod(count, 2) == 0) {
                    value = "true";
                } else {
                    value = "false";
                }
                break;
            case "Integer":
                if (Math.floorMod(count, 2) == 0) {
                    value = "123";
                } else {
                    value = "456";
                }
                break;
            case "Long":
                if (Math.floorMod(count, 2) == 0) {
                    value = "123456789L";
                } else {
                    value = "987654321L";
                }
                break;
            case "Double":
                if (Math.floorMod(count, 2) == 0) {
                    value = "123.456";
                } else {
                    value = "654.321";
                }
                break;
            case "String, String":
                if (containerName.equals("List<Map<")) {
                    if (Math.floorMod(count, 2) == 0) {
                        value = "Map.of(\"key1\", \"value1\")";
                    } else {
                        value = "Map.of(\"key2\", \"value2\")";
                    }
                } else {
                    if (Math.floorMod(count, 2) == 0) {
                        value = "\"key1\", \"value1\"";
                    } else {
                        value = "\"key2\", \"value2\"";
                    }
                }
                break;
            case "String, Long":
                if (containerName.equals("List<Map<")) {
                    if (Math.floorMod(count, 2) == 0) {
                        value = "Map.of(\"key1\", 123456L)";
                    } else {
                        value = "Map.of(\"key2\", 654321L)";
                    }
                } else {
                    if (Math.floorMod(count, 2) == 0) {
                        value = "\"key1\", 123456L";
                    } else {
                        value = "\"key2\", 654321L";
                    }
                }
                break;
            default:
                log.warn("Unknown primitive type for test attribute {} - skipping.", typeName);
                break;
        }
        return value;
    }

    private String getRawPrimitiveValue(String containerName, String typeName, int count) {
        String value = null;
        switch (typeName) {
            case "String":
            case "Boolean":
            case "Integer":
            case "Double":
                value = getPrimitiveValue(containerName, typeName, count);
                break;
            case "Long":
                if (Math.floorMod(count, 2) == 0) {
                    value = "123456789";
                } else {
                    value = "987654321";
                }
                break;
            case "String, String":
                if (Math.floorMod(count, 2) == 0) {
                    value = "{\"key1\", \"value1\"}";
                } else {
                    value = "{\"key2\", \"value2\"}";
                }
                break;
            case "String, Long":
                if (Math.floorMod(count, 2) == 0) {
                    value = "{\"key1\", 123456}";
                } else {
                    value = "{\"key2\", 654321}";
                }
                break;
            default:
                log.warn("Unknown primitive type for (raw) test attribute {} - skipping.", typeName);
                break;
        }
        return value;
    }

    private void addEnum(TestAttribute.TestAttributeBuilder builder, boolean multiValued, String typeName) {
        builder.relationship(false);
        if (typeName.equals("AtlanPolicyAction")) {
            // Set a concrete type if it's a policy action interface
            typeName = "PersonaMetadataAction";
        }
        if (!multiValued) {
            testAttributes.add(builder.values(List.of(getEnumValue(typeName, 0)))
                    .rawValues(List.of(getRawEnumValue(typeName, 0)))
                    .build());
        } else {
            testAttributes.add(builder.values(List.of(getEnumValue(typeName, 0), getEnumValue(typeName, 1)))
                    .rawValues(List.of(getRawEnumValue(typeName, 0), getRawEnumValue(typeName, 1)))
                    .build());
        }
    }

    private String getEnumValue(String typeName, int count) {
        Enum<?>[] values = getEnumValues(typeName);
        if (values != null) {
            if (values.length > count) {
                return typeName + "." + values[count].name();
            } else {
                return typeName + "." + values[0].name();
            }
        }
        return null;
    }

    private String getRawEnumValue(String typeName, int count) {
        AtlanEnum[] values = (AtlanEnum[]) getEnumValues(typeName);
        if (values != null) {
            if (values.length > count) {
                return "\"" + values[count].getValue() + "\"";
            } else {
                return "\"" + values[0].getValue() + "\"";
            }
        }
        return null;
    }

    private Enum<?>[] getEnumValues(String typeName) {
        Enum<?>[] values = null;
        try {
            // Introspect the enum to draw out actual values
            Class<?> clazz = Class.forName(getPackageRoot() + ".enums." + typeName);
            Field f = clazz.getDeclaredField("$VALUES");
            f.setAccessible(true);
            Object o = f.get(null);
            values = (Enum<?>[]) o;
        } catch (ClassNotFoundException e) {
            log.error("Unable to reflectively introspect enumeration: {}", typeName, e);
        } catch (NoSuchFieldException e) {
            log.error("Unable to find any values in enumeration: {}", typeName, e);
        } catch (IllegalAccessException e) {
            log.error("Unable to access values in enumeration: {}", typeName, e);
        }
        return values;
    }

    private void addAssetRef(TestAttribute.TestAttributeBuilder builder, boolean multiValued, String typeName) {
        builder.relationship(true);
        if (!multiValued) {
            testAttributes.add(builder.values(List.of(getAssetValue(typeName, 0)))
                    .rawValues(List.of(getRawAssetValue(typeName, 0)))
                    .build());
        } else {
            testAttributes.add(builder.values(List.of(getAssetValue(typeName, 0), getAssetValue(typeName, 1)))
                    .rawValues(List.of(getRawAssetValue(typeName, 0), getRawAssetValue(typeName, 1)))
                    .build());
        }
    }

    private String getAssetValue(String typeName, int count) {
        // Always start looking for a concrete type at 0, since we need to branch out
        // down paths that may lead to leaves that are still abstract
        String concreteType = traverseToConcreteType(typeName);
        if (Math.floorMod(count, 2) == 0) {
            return concreteType + ".refByGuid(\"" + ASSET_GUID + "\")";
        } else {
            return concreteType + ".refByQualifiedName(\"" + ASSET_QN + "\")";
        }
    }

    private String getRawAssetValue(String typeName, int count) {
        // Always start looking for a concrete type at 0, since we need to branch out
        // down paths that may lead to leaves that are still abstract
        String concreteType = traverseToConcreteType(typeName);
        if (Math.floorMod(count, 2) == 0) {
            return "{ \"typeName\": \"" + concreteType + "\", \"guid\": \"" + ASSET_GUID + "\" }";
        } else {
            return "{ \"typeName\": \"" + concreteType + "\", \"uniqueAttributes\": { \"qualifiedName\": \"" + ASSET_QN
                    + "\" }}";
        }
    }

    private String traverseToConcreteType(String typeName) {
        // TODO: Note that the lookup below is on the renamed class (will only get a hit if
        //  the class is NOT renamed but reuses the out-of-the-box name). Today this works
        //  for all classes, but may not always be the case in the future.
        AssetGenerator assetGen = cache.getCachedAssetType(typeName);
        if (assetGen != null) {
            if (!assetGen.isAbstract()) {
                // If we arrive at a concrete class, return it
                return typeName;
            } else {
                List<String> subTypes = assetGen.getSubTypes();
                if (subTypes != null && !subTypes.isEmpty()) {
                    for (String subType : subTypes) {
                        String candidate = traverseToConcreteType(subType);
                        AssetGenerator candidateGen = cache.getCachedAssetType(candidate);
                        if (candidateGen != null && !candidateGen.isAbstract()) {
                            // If we hit a leaf, short-circuit out, otherwise continue on with
                            // the recursive loop
                            return candidate;
                        }
                    }
                }
            }
        }
        return typeName;
    }

    private void addStructRef(
            TestAttribute.TestAttributeBuilder builder, boolean multiValued, String typeName, String container) {
        if (!multiValued) {
            testAttributes.add(builder.values(List.of(getStructValue(typeName, 0)))
                    .rawValues(List.of(getRawStructValue(typeName, 0)))
                    .build());
        } else if (container.equals("Map<")) {
            testAttributes.add(builder.values(List.of(
                            getPrimitiveValue(null, "String", 0) + ", " + getStructValue(typeName, 0),
                            getPrimitiveValue(null, "String", 1) + ", " + getStructValue(typeName, 1)))
                    .rawValues(List.of(
                            getPrimitiveValue(null, "String", 0) + ", " + getRawStructValue(typeName, 0),
                            getPrimitiveValue(null, "String", 1) + ", " + getRawStructValue(typeName, 1)))
                    .build());
        } else {
            testAttributes.add(builder.values(List.of(getStructValue(typeName, 0), getStructValue(typeName, 1)))
                    .rawValues(List.of(getRawStructValue(typeName, 0), getRawStructValue(typeName, 1)))
                    .build());
        }
    }

    private String getStructValue(String typeName, int count) {
        // Introspect the members of the struct to add all attributes to the builder
        Field[] fields = getFieldsForStruct(typeName);
        if (fields != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(typeName).append(".builder()");
            for (Field field : fields) {
                Class<?> fieldType = field.getType();
                String fieldName = field.getName();
                // Exclude the embedded type details for structs used for deserialization
                if (!fieldName.equals("TYPE_NAME")
                        && !fieldName.equals("typeName")
                        && !fieldName.equals("serialVersionUID")) {
                    sb.append(".").append(fieldName).append("(");
                    if (isPrimitive(fieldType)) {
                        sb.append(getPrimitiveValue(null, fieldType.getSimpleName(), count));
                    } else if (fieldType == List.class) {
                        // Handle non-primitive fields
                        Type generic = field.getGenericType();
                        if (generic instanceof ParameterizedType) {
                            ParameterizedType pt = (ParameterizedType) generic;
                            Type type = pt.getActualTypeArguments()[0];
                            if (type.getTypeName().equals("java.util.Map<java.lang.String, java.lang.String>")) {
                                sb.append("List.of(")
                                        .append("Map.of(")
                                        .append(getPrimitiveValue("Map<", "String, String", 0))
                                        .append("), ")
                                        .append("Map.of(")
                                        .append(getPrimitiveValue("Map<", "String, String", 1))
                                        .append(")")
                                        .append(")");
                            } else {
                                try {
                                    Class<?> embedded = Class.forName(type.getTypeName());
                                    String simpleClassName = embedded.getSimpleName();
                                    if (isPrimitive(embedded)) {
                                        sb.append("List.of(")
                                                .append(getPrimitiveValue("List<", simpleClassName, 0))
                                                .append(", ")
                                                .append(getPrimitiveValue("List<", simpleClassName, 1))
                                                .append(")");
                                    } else {
                                        sb.append("List.of(")
                                                .append(getStructValue(simpleClassName, 0))
                                                .append(", ")
                                                .append(getStructValue(simpleClassName, 1))
                                                .append(")");
                                    }
                                } catch (ClassNotFoundException e) {
                                    log.error("Unable to find embedded struct class: {}", type.getTypeName(), e);
                                }
                            }
                        } else {
                            log.warn("Unable to reflectively identify list-wrapped type: {}", generic.getTypeName());
                        }
                    } else if (fieldType == Map.class) {
                        // Handle non-primitive fields
                        Type generic = field.getGenericType();
                        if (generic instanceof ParameterizedType) {
                            ParameterizedType pt = (ParameterizedType) generic;
                            Type typeKey = pt.getActualTypeArguments()[0];
                            Type typeVal = pt.getActualTypeArguments()[1];
                            try {
                                Class<?> embeddedKey = Class.forName(typeKey.getTypeName());
                                String simpleClassNameKey = embeddedKey.getSimpleName();
                                Class<?> embeddedVal = Class.forName(typeVal.getTypeName());
                                String simpleClassNameVal = embeddedVal.getSimpleName();
                                sb.append("Map.of(")
                                        .append(getPrimitiveValue(
                                                "Map<", simpleClassNameKey + ", " + simpleClassNameVal, 0))
                                        .append(", ")
                                        .append(getPrimitiveValue(
                                                "Map<", simpleClassNameKey + ", " + simpleClassNameVal, 1))
                                        .append(")");
                            } catch (ClassNotFoundException e) {
                                log.error("Unable to find embedded struct class: {}", pt.getActualTypeArguments(), e);
                            }
                        } else {
                            log.warn("Unable to reflectively identify map-wrapped type: {}", generic.getTypeName());
                        }
                    } else if (fieldType.getCanonicalName().startsWith("com.atlan.model.enums.")) {
                        sb.append(getEnumValue(fieldType.getSimpleName(), count));
                    } else {
                        sb.append(getStructValue(fieldType.getSimpleName(), count));
                    }
                    sb.append(")");
                }
            }
            sb.append(".build()");
            return sb.toString();
        }
        return "";
    }

    private String getRawStructValue(String typeName, int count) {
        // Introspect the members of the struct to add all attributes to the builder
        Field[] fields = getFieldsForStruct(typeName);
        if (fields != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            for (Field field : fields) {
                Class<?> fieldType = field.getType();
                String fieldName = field.getName();
                // Exclude the embedded type details for structs used for deserialization
                if (!fieldName.equals("TYPE_NAME")
                        && !fieldName.equals("typeName")
                        && !fieldName.equals("serialVersionUID")) {
                    sb.append("\"").append(fieldName).append("\": ");
                    if (isPrimitive(fieldType)) {
                        sb.append(getRawPrimitiveValue(null, fieldType.getSimpleName(), count));
                    } else if (fieldType == List.class) {
                        // Handle non-primitive fields
                        Type generic = field.getGenericType();
                        if (generic instanceof ParameterizedType) {
                            ParameterizedType pt = (ParameterizedType) generic;
                            Type type = pt.getActualTypeArguments()[0];
                            if (type.getTypeName().equals("java.util.Map<java.lang.String, java.lang.String>")) {
                                sb.append("[")
                                        .append("{")
                                        .append(getRawPrimitiveValue("Map<", "String, String", 0))
                                        .append("}, ")
                                        .append("{")
                                        .append(getRawPrimitiveValue("Map<", "String, String", 1))
                                        .append("}")
                                        .append("]");
                            } else {
                                try {
                                    Class<?> embedded = Class.forName(type.getTypeName());
                                    String simpleClassName = embedded.getSimpleName();
                                    if (isPrimitive(embedded)) {
                                        sb.append("[")
                                                .append(getRawPrimitiveValue("List<", simpleClassName, 0))
                                                .append(", ")
                                                .append(getRawPrimitiveValue("List<", simpleClassName, 1))
                                                .append("]");
                                    } else {
                                        sb.append("[")
                                                .append(getRawStructValue(simpleClassName, 0))
                                                .append(", ")
                                                .append(getRawStructValue(simpleClassName, 1))
                                                .append("]");
                                    }
                                } catch (ClassNotFoundException e) {
                                    log.error("Unable to find embedded struct class: {}", type.getTypeName(), e);
                                }
                            }
                        } else {
                            log.warn("Unable to reflectively identify list-wrapped type: {}", generic.getTypeName());
                        }
                    } else if (fieldType == Map.class) {
                        // Handle non-primitive fields
                        Type generic = field.getGenericType();
                        if (generic instanceof ParameterizedType) {
                            ParameterizedType pt = (ParameterizedType) generic;
                            Type typeKey = pt.getActualTypeArguments()[0];
                            Type typeVal = pt.getActualTypeArguments()[1];
                            try {
                                Class<?> embeddedKey = Class.forName(typeKey.getTypeName());
                                String simpleClassNameKey = embeddedKey.getSimpleName();
                                Class<?> embeddedVal = Class.forName(typeVal.getTypeName());
                                String simpleClassNameVal = embeddedVal.getSimpleName();
                                sb.append("{")
                                        .append(getRawPrimitiveValue(
                                                "Map<", simpleClassNameKey + ", " + simpleClassNameVal, 0))
                                        .append(": ")
                                        .append(getRawPrimitiveValue(
                                                "Map<", simpleClassNameKey + ", " + simpleClassNameVal, 1))
                                        .append("}");
                            } catch (ClassNotFoundException e) {
                                log.error("Unable to find embedded struct class: {}", pt.getActualTypeArguments(), e);
                            }
                        } else {
                            log.warn("Unable to reflectively identify map-wrapped type: {}", generic.getTypeName());
                        }
                    } else if (fieldType.getCanonicalName().startsWith("com.atlan.model.enums.")) {
                        sb.append(getRawEnumValue(fieldType.getSimpleName(), count));
                    } else {
                        sb.append(getRawStructValue(fieldType.getSimpleName(), count));
                    }
                    sb.append(", ");
                }
            }
            if (sb.length() > 1) {
                sb.deleteCharAt(sb.length() - 2); // Remove the final comma-separator
            }
            sb.append("}");
            return sb.toString();
        }
        return "";
    }

    private Field[] getFieldsForStruct(String typeName) {
        try {
            // Introspect the members of the struct to add all attributes to the builder
            Class<?> clazz = Class.forName(getPackageRoot() + ".structs." + typeName);
            return clazz.getDeclaredFields();
        } catch (ClassNotFoundException e) {
            log.error("Unable to reflectively introspect struct: {}", typeName, e);
        }
        return null;
    }

    private boolean isPrimitive(Class<?> clazz) {
        return clazz == String.class
                || clazz == Boolean.class
                || clazz == Integer.class
                || clazz == Long.class
                || clazz == Double.class;
    }
}
