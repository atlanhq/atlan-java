/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.generators;

import com.atlan.generators.lombok.Singulars;
import com.atlan.model.typedefs.EntityDef;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class AssetTestGenerator extends AssetGenerator {

    public static final String DIRECTORY = "src" + File.separator
            + "test" + File.separator
            + "java" + File.separator
            + "com" + File.separator
            + "atlan" + File.separator
            + "model" + File.separator
            + "assets";

    private static final String ASSET_GUID = "705d96f4-bdb6-4792-8dfe-8dc4ca3d2c23";
    private static final String ASSET_QN = "default/snowflake/1234567890/test/qualifiedName";

    private final AssetGenerator asset;
    private final List<TestAttribute> testAttributes;

    public AssetTestGenerator(AssetGenerator asset) {
        super(asset.getEntityDef());
        this.asset = asset;
        this.testAttributes = new ArrayList<>();
    }

    @Override
    public void resolveDetails() {
        addTestAttributes(asset);
    }

    @Getter
    public static final class TestAttribute {

        private final String builderMethod;
        private final List<String> values;

        public TestAttribute(String builderMethod, List<String> values) {
            this.builderMethod = builderMethod;
            this.values = values;
        }
    }

    private void addTestAttributes(AssetGenerator assetGenerator) {
        EntityDef typeDetails = assetGenerator.getEntityDef();
        List<String> superTypes = typeDetails.getSuperTypes();
        if (superTypes != null && !superTypes.isEmpty()) {
            String singleSuperType = getSingleTypeToExtend(assetGenerator.getOriginalName(), superTypes);
            if (singleSuperType != null && !singleSuperType.equals("Reference")) {
                // We can short-circuit when the next level up is Reference (the top)
                addTestAttributes(ModelGeneratorV2.getCachedAssetType(singleSuperType));
            }
        }
        List<Attribute> attributes = assetGenerator.getAttributes();
        if (attributes != null) {
            for (Attribute attribute : attributes) {
                MappedType type = attribute.getType();
                boolean multiValued = attribute.getSingular() != null;
                String renamedAttr = attribute.getRenamed();
                String builderMethod = renamedAttr;
                if (multiValued) {
                    // If the attribute can be multivalued, figure out the singular form of the
                    // attribute's name
                    if (attribute.getSingular().length() == 0) {
                        builderMethod = Singulars.autoSingularize(renamedAttr);
                    } else {
                        builderMethod = attribute.getSingular();
                    }
                }
                // TODO: Handle maps - possibly within the template itself (?)
                switch (type.getType()) {
                    case PRIMITIVE:
                        addPrimitive(builderMethod, multiValued, type.getName());
                        break;
                    case ENUM:
                        addEnum(builderMethod, multiValued, type.getName());
                        break;
                    case ASSET:
                        addAssetRef(builderMethod, multiValued, type.getName());
                        break;
                    case STRUCT:
                        addStructRef(builderMethod, multiValued, type.getName());
                        break;
                    default:
                        log.warn("Unhandled testing type {} - skipping.", type.getType());
                        break;
                }
            }
        } else {
            log.warn("No attributes found for {}, skipping any test inclusion.", assetGenerator.getClassName());
        }
    }

    private void addPrimitive(String builderMethod, boolean multiValued, String typeName) {
        if (!multiValued) {
            testAttributes.add(new TestAttribute(builderMethod, List.of(getPrimitiveValue(typeName, 0))));
        } else {
            testAttributes.add(new TestAttribute(
                    builderMethod, List.of(getPrimitiveValue(typeName, 0), getPrimitiveValue(typeName, 1))));
        }
    }

    private String getPrimitiveValue(String typeName, int count) {
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
                if (Math.floorMod(count, 2) == 0) {
                    value = "\"key1\", \"value1\"";
                } else {
                    value = "\"key2\", \"value2\"";
                }
                break;
            case "String, Long":
                if (Math.floorMod(count, 2) == 0) {
                    value = "\"key1\", 123456L";
                } else {
                    value = "\"key2\", 654321L";
                }
                break;
            default:
                log.warn("Unknown primitive type for test attribute {} - skipping.", typeName);
                break;
        }
        return value;
    }

    private void addEnum(String builderMethod, boolean multiValued, String typeName) {
        if (!multiValued) {
            testAttributes.add(new TestAttribute(builderMethod, List.of(getEnumValue(typeName, 0))));
        } else {
            testAttributes.add(
                    new TestAttribute(builderMethod, List.of(getEnumValue(typeName, 0), getEnumValue(typeName, 1))));
        }
    }

    private String getEnumValue(String typeName, int count) {
        try {
            // Introspect the enum to draw out actual values
            Class<?> clazz = Class.forName("com.atlan.model.enums." + typeName);
            Field f = clazz.getDeclaredField("$VALUES");
            f.setAccessible(true);
            Object o = f.get(null);
            Enum<?>[] values = (Enum<?>[]) o;
            if (values.length > count) {
                return typeName + "." + values[count].name();
            } else {
                return typeName + "." + values[0].name();
            }
        } catch (ClassNotFoundException e) {
            log.error("Unable to reflectively introspect enumeration: {}", typeName, e);
        } catch (NoSuchFieldException e) {
            log.error("Unable to find any values in enumeration: {}", typeName, e);
        } catch (IllegalAccessException e) {
            log.error("Unable to access values in enumeration: {}", typeName, e);
        }
        return null;
    }

    private void addAssetRef(String builderMethod, boolean multiValued, String typeName) {
        if (!multiValued) {
            testAttributes.add(new TestAttribute(builderMethod, List.of(getAssetValue(typeName, 0))));
        } else {
            testAttributes.add(
                    new TestAttribute(builderMethod, List.of(getAssetValue(typeName, 0), getAssetValue(typeName, 1))));
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

    private String traverseToConcreteType(String typeName) {
        // TODO: Note that the lookup below is on the renamed class (will only get a hit if
        //  the class is NOT renamed but reuses the out-of-the-box name). Today this works
        //  for all classes, but may not always be the case in the future.
        AssetGenerator assetGen = ModelGeneratorV2.getCachedAssetType(typeName);
        if (assetGen != null) {
            if (!assetGen.isAbstract()) {
                // If we arrive at a concrete class, return it
                return typeName;
            } else {
                List<String> subTypes = assetGen.getSubTypes();
                if (subTypes != null && !subTypes.isEmpty()) {
                    for (String subType : subTypes) {
                        String candidate = traverseToConcreteType(subType);
                        AssetGenerator candidateGen = ModelGeneratorV2.getCachedAssetType(candidate);
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

    private void addStructRef(String builderMethod, boolean multiValued, String typeName) {
        if (!multiValued) {
            testAttributes.add(new TestAttribute(builderMethod, List.of(getStructValue(typeName, 0))));
        } else {
            testAttributes.add(new TestAttribute(
                    builderMethod, List.of(getStructValue(typeName, 0), getStructValue(typeName, 1))));
        }
    }

    private String getStructValue(String typeName, int count) {
        try {
            // Introspect the members of the struct to add all attributes to the builder
            Class<?> clazz = Class.forName("com.atlan.model.structs." + typeName);
            Field[] fields = clazz.getDeclaredFields();
            StringBuilder sb = new StringBuilder();
            sb.append(typeName).append(".builder()");
            for (Field field : fields) {
                Class<?> fieldType = field.getType();
                sb.append(".").append(field.getName()).append("(");
                if (isPrimitive(fieldType)) {
                    sb.append(getPrimitiveValue(fieldType.getSimpleName(), count));
                } else if (fieldType == List.class) {
                    // Handle non-primitive fields
                    Type generic = field.getGenericType();
                    if (generic instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) generic;
                        Type type = pt.getActualTypeArguments()[0];
                        Class<?> embedded = Class.forName(type.getTypeName());
                        String simpleClassName = embedded.getSimpleName();
                        sb.append("List.of(")
                                .append(getPrimitiveValue(simpleClassName, 0))
                                .append(", ")
                                .append(getPrimitiveValue(simpleClassName, 1))
                                .append(")");
                    } else {
                        log.warn("Unable to reflectively identify list-wrapped type: {}", generic.getTypeName());
                    }
                } else if (fieldType.getCanonicalName().startsWith("com.atlan.model.enums.")) {
                    sb.append(getEnumValue(fieldType.getSimpleName(), count));
                } else {
                    log.error("Type not yet handled for structs: {}", fieldType.getCanonicalName());
                }
                sb.append(")");
            }
            sb.append(".build()");
            return sb.toString();
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
