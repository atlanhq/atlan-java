/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.cache.ClassificationCache;
import com.atlan.cache.CustomMetadataCache;
import com.atlan.cache.ReflectionCache;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.*;
import com.atlan.model.core.Classification;
import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.model.core.Entity;
import com.atlan.util.JacksonUtils;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

/**
 * Deserialization of all {@link Entity} objects, down through the entire inheritance hierarchy.
 * This custom deserialization is necessary to flatten some specific aspects of complexity in Atlan's payloads:
 * <ul>
 *     <li>The nested <code>attributes</code> and <code>relationshipAttributes</code> structures.</li>
 *     <li>The possibility that the same (relationship) attribute could appear in either of these nested structures.</li>
 *     <li>Handling the extension of properties as you traverse down the inheritance structures, without needing to also extend these nested structures through inheritance.</li>
 *     <li>Automatically translating the nested <code>businessAttributes</code> structure into custom metadata, including translating from Atlan's internal hashed-string representations into human-readable names.</li>
 * </ul>
 */
public class EntityDeserializer extends StdDeserializer<Entity> {

    private static final long serialVersionUID = 2L;

    public EntityDeserializer() {
        this(null);
    }

    public EntityDeserializer(Class<?> t) {
        super(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object deserializeWithType(
            JsonParser parser, DeserializationContext context, TypeDeserializer typeDeserializer) throws IOException {
        return deserialize(parser, context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entity deserialize(JsonParser parser, DeserializationContext context) throws IOException {

        JsonNode root = parser.getCodec().readTree(parser);
        JsonNode attributes = root.get("attributes");
        JsonNode relationshipGuid = root.get("relationshipGuid");
        JsonNode relationshipAttributes = root.get("relationshipAttributes");
        JsonNode businessAttributes = root.get("businessAttributes");
        JsonNode classificationNames = root.get("classificationNames");

        Entity.EntityBuilder<?, ?> builder;
        String typeName = root.get("typeName").asText();

        // TODO: figure out how to avoid needing to maintain this switch statement
        if (typeName == null) {
            builder = IndistinctAsset.builder();
        } else {
            switch (typeName) {
                case APIPath.TYPE_NAME:
                    builder = APIPath.builder();
                    break;
                case APISpec.TYPE_NAME:
                    builder = APISpec.builder();
                    break;
                case AtlanCollection.TYPE_NAME:
                    builder = AtlanCollection.builder();
                    break;
                case AtlanQuery.TYPE_NAME:
                    builder = AtlanQuery.builder();
                    break;
                case BIProcess.TYPE_NAME:
                    builder = BIProcess.builder();
                    break;
                case Column.TYPE_NAME:
                    builder = Column.builder();
                    break;
                case ColumnProcess.TYPE_NAME:
                    builder = ColumnProcess.builder();
                    break;
                case Connection.TYPE_NAME:
                    builder = Connection.builder();
                    break;
                case DataStudioAsset.TYPE_NAME:
                    builder = DataStudioAsset.builder();
                    break;
                case Database.TYPE_NAME:
                    builder = Database.builder();
                    break;
                case DbtColumnProcess.TYPE_NAME:
                    builder = DbtColumnProcess.builder();
                    break;
                case DbtMetric.TYPE_NAME:
                    builder = DbtMetric.builder();
                    break;
                case DbtModel.TYPE_NAME:
                    builder = DbtModel.builder();
                    break;
                case DbtModelColumn.TYPE_NAME:
                    builder = DbtModelColumn.builder();
                    break;
                case DbtProcess.TYPE_NAME:
                    builder = DbtProcess.builder();
                    break;
                case DbtSource.TYPE_NAME:
                    builder = DbtSource.builder();
                    break;
                case Folder.TYPE_NAME:
                    builder = Folder.builder();
                    break;
                case GCSBucket.TYPE_NAME:
                    builder = GCSBucket.builder();
                    break;
                case GCSObject.TYPE_NAME:
                    builder = GCSObject.builder();
                    break;
                case Glossary.TYPE_NAME:
                    builder = Glossary.builder();
                    break;
                case GlossaryCategory.TYPE_NAME:
                    builder = GlossaryCategory.builder();
                    break;
                case GlossaryTerm.TYPE_NAME:
                    builder = GlossaryTerm.builder();
                    break;
                case Insight.TYPE_NAME:
                    builder = Insight.builder();
                    break;
                case LineageProcess.TYPE_NAME:
                    builder = LineageProcess.builder();
                    break;
                case Link.TYPE_NAME:
                    builder = Link.builder();
                    break;
                case LookerDashboard.TYPE_NAME:
                    builder = LookerDashboard.builder();
                    break;
                case LookerExplore.TYPE_NAME:
                    builder = LookerExplore.builder();
                    break;
                case LookerField.TYPE_NAME:
                    builder = LookerField.builder();
                    break;
                case LookerFolder.TYPE_NAME:
                    builder = LookerFolder.builder();
                    break;
                case LookerLook.TYPE_NAME:
                    builder = LookerLook.builder();
                    break;
                case LookerModel.TYPE_NAME:
                    builder = LookerModel.builder();
                    break;
                case LookerProject.TYPE_NAME:
                    builder = LookerProject.builder();
                    break;
                case LookerQuery.TYPE_NAME:
                    builder = LookerQuery.builder();
                    break;
                case LookerTile.TYPE_NAME:
                    builder = LookerTile.builder();
                    break;
                case LookerView.TYPE_NAME:
                    builder = LookerView.builder();
                    break;
                case MaterializedView.TYPE_NAME:
                    builder = MaterializedView.builder();
                    break;
                case MetabaseCollection.TYPE_NAME:
                    builder = MetabaseCollection.builder();
                    break;
                case MetabaseDashboard.TYPE_NAME:
                    builder = MetabaseDashboard.builder();
                    break;
                case MetabaseQuestion.TYPE_NAME:
                    builder = MetabaseQuestion.builder();
                    break;
                case ModeChart.TYPE_NAME:
                    builder = ModeChart.builder();
                    break;
                case ModeCollection.TYPE_NAME:
                    builder = ModeCollection.builder();
                    break;
                case ModeQuery.TYPE_NAME:
                    builder = ModeQuery.builder();
                    break;
                case ModeReport.TYPE_NAME:
                    builder = ModeReport.builder();
                    break;
                case ModeWorkspace.TYPE_NAME:
                    builder = ModeWorkspace.builder();
                    break;
                case PowerBIColumn.TYPE_NAME:
                    builder = PowerBIColumn.builder();
                    break;
                case PowerBIDashboard.TYPE_NAME:
                    builder = PowerBIDashboard.builder();
                    break;
                case PowerBIDataflow.TYPE_NAME:
                    builder = PowerBIDataflow.builder();
                    break;
                case PowerBIDataset.TYPE_NAME:
                    builder = PowerBIDataset.builder();
                    break;
                case PowerBIDatasource.TYPE_NAME:
                    builder = PowerBIDatasource.builder();
                    break;
                case PowerBIMeasure.TYPE_NAME:
                    builder = PowerBIMeasure.builder();
                    break;
                case PowerBIPage.TYPE_NAME:
                    builder = PowerBIPage.builder();
                    break;
                case PowerBIReport.TYPE_NAME:
                    builder = PowerBIReport.builder();
                    break;
                case PowerBITable.TYPE_NAME:
                    builder = PowerBITable.builder();
                    break;
                case PowerBITile.TYPE_NAME:
                    builder = PowerBITile.builder();
                    break;
                case PowerBIWorkspace.TYPE_NAME:
                    builder = PowerBIWorkspace.builder();
                    break;
                case PresetChart.TYPE_NAME:
                    builder = PresetChart.builder();
                    break;
                case PresetDashboard.TYPE_NAME:
                    builder = PresetDashboard.builder();
                    break;
                case PresetDataset.TYPE_NAME:
                    builder = PresetDataset.builder();
                    break;
                case PresetWorkspace.TYPE_NAME:
                    builder = PresetWorkspace.builder();
                    break;
                case Procedure.TYPE_NAME:
                    builder = Procedure.builder();
                    break;
                case Readme.TYPE_NAME:
                    builder = Readme.builder();
                    break;
                case ReadmeTemplate.TYPE_NAME:
                    builder = ReadmeTemplate.builder();
                    break;
                case S3Bucket.TYPE_NAME:
                    builder = S3Bucket.builder();
                    break;
                case S3Object.TYPE_NAME:
                    builder = S3Object.builder();
                    break;
                case SalesforceDashboard.TYPE_NAME:
                    builder = SalesforceDashboard.builder();
                    break;
                case SalesforceField.TYPE_NAME:
                    builder = SalesforceField.builder();
                    break;
                case SalesforceObject.TYPE_NAME:
                    builder = SalesforceObject.builder();
                    break;
                case SalesforceOrganization.TYPE_NAME:
                    builder = SalesforceOrganization.builder();
                    break;
                case SalesforceReport.TYPE_NAME:
                    builder = SalesforceReport.builder();
                    break;
                case Schema.TYPE_NAME:
                    builder = Schema.builder();
                    break;
                case Table.TYPE_NAME:
                    builder = Table.builder();
                    break;
                case TablePartition.TYPE_NAME:
                    builder = TablePartition.builder();
                    break;
                case TableauCalculatedField.TYPE_NAME:
                    builder = TableauCalculatedField.builder();
                    break;
                case TableauDashboard.TYPE_NAME:
                    builder = TableauDashboard.builder();
                    break;
                case TableauDatasource.TYPE_NAME:
                    builder = TableauDatasource.builder();
                    break;
                case TableauDatasourceField.TYPE_NAME:
                    builder = TableauDatasourceField.builder();
                    break;
                case TableauFlow.TYPE_NAME:
                    builder = TableauFlow.builder();
                    break;
                case TableauMetric.TYPE_NAME:
                    builder = TableauMetric.builder();
                    break;
                case TableauProject.TYPE_NAME:
                    builder = TableauProject.builder();
                    break;
                case TableauSite.TYPE_NAME:
                    builder = TableauSite.builder();
                    break;
                case TableauWorkbook.TYPE_NAME:
                    builder = TableauWorkbook.builder();
                    break;
                case TableauWorksheet.TYPE_NAME:
                    builder = TableauWorksheet.builder();
                    break;
                case View.TYPE_NAME:
                    builder = View.builder();
                    break;
                default:
                    builder = IndistinctAsset.builder();
                    break;
            }
        }

        // Start by deserializing all the non-attribute properties (defined at Entity-level)
        builder = builder.typeName(JacksonUtils.deserializeString(root, "typeName"))
                .guid(JacksonUtils.deserializeString(root, "guid"))
                .displayText(JacksonUtils.deserializeString(root, "displayText"))
                // Include reference attributes, for related entities
                .entityStatus(JacksonUtils.deserializeString(root, "entityStatus"))
                .relationshipType(JacksonUtils.deserializeString(root, "relationshipType"))
                .relationshipGuid(JacksonUtils.deserializeString(root, "relationshipGuid"))
                .relationshipStatus(
                        JacksonUtils.deserializeObject(root, "relationshipStatus", new TypeReference<>() {}))
                .uniqueAttributes(JacksonUtils.deserializeObject(root, "uniqueAttributes", new TypeReference<>() {}))
                .status(JacksonUtils.deserializeObject(root, "status", new TypeReference<>() {}))
                .createdBy(JacksonUtils.deserializeString(root, "createdBy"))
                .updatedBy(JacksonUtils.deserializeString(root, "updatedBy"))
                .createTime(JacksonUtils.deserializeLong(root, "createTime"))
                .updateTime(JacksonUtils.deserializeLong(root, "updateTime"))
                .deleteHandler(JacksonUtils.deserializeString(root, "deleteHandler"))
                .isIncomplete(JacksonUtils.deserializeBoolean(root, "isIncomplete"));
        Set<Classification> classifications =
                JacksonUtils.deserializeObject(root, "classifications", new TypeReference<>() {});
        if (classifications != null) {
            builder = builder.classifications(classifications);
        }
        Set<String> meaningNames = JacksonUtils.deserializeObject(root, "meaningNames", new TypeReference<>() {});
        if (meaningNames != null) {
            builder = builder.meaningNames(meaningNames);
        }
        Set<String> pendingTasks = JacksonUtils.deserializeObject(root, "pendingTasks", new TypeReference<>() {});
        if (pendingTasks != null) {
            builder = builder.pendingTasks(pendingTasks);
        }

        Entity value = builder.build();

        Class<?> clazz = value.getClass();

        Map<String, JsonNode> leftOverAttributes = new HashMap<>();

        if (attributes != null && !attributes.isNull()) {
            Iterator<String> itr = attributes.fieldNames();
            while (itr.hasNext()) {
                String attrKey = itr.next();
                String deserializeName = ReflectionCache.getDeserializedName(clazz, attrKey);
                Method method = ReflectionCache.getSetter(clazz, deserializeName);
                if (method != null) {
                    try {
                        deserialize(value, attributes.get(attrKey), method);
                    } catch (NoSuchMethodException e) {
                        throw new IOException("Missing fromValue method for enum.", e);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new IOException("Failed to deserialize through reflection.", e);
                    }
                } else {
                    // If the setter was not found, still retain it for later processing
                    // (this is where custom attributes will end up for search results)
                    leftOverAttributes.put(attrKey, attributes.get(attrKey));
                }
            }
        }

        // Only process relationshipAttributes if this is a full entity, not a relationship
        // reference. (If it is a relationship reference, the relationshipGuid will be non-null.)
        if (relationshipGuid == null || relationshipGuid.isNull()) {
            if (relationshipAttributes != null && !relationshipAttributes.isNull()) {
                Iterator<String> itr = relationshipAttributes.fieldNames();
                while (itr.hasNext()) {
                    String relnKey = itr.next();
                    String deserializeName = ReflectionCache.getDeserializedName(clazz, relnKey);
                    Method method = ReflectionCache.getSetter(clazz, deserializeName);
                    if (method != null) {
                        try {
                            deserialize(value, relationshipAttributes.get(relnKey), method);
                        } catch (NoSuchMethodException e) {
                            throw new IOException("Missing fromValue method for enum.", e);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new IOException("Failed to deserialize through reflection.", e);
                        }
                    }
                }
            }
        }

        // Custom attributes can come from two places, only one of which should ever have data...
        Map<String, CustomMetadataAttributes> cm = null;

        // 1. For search results, they're embedded in `attributes` in the form <cmId>.<attrId>
        if (!leftOverAttributes.isEmpty()) {
            // Translate these into custom metadata structure
            try {
                cm = CustomMetadataCache.getCustomMetadataFromSearchResult(leftOverAttributes);
            } catch (AtlanException e) {
                e.printStackTrace();
                throw new IOException("Unable to deserialize custom metadata from search result.", e);
            }
        }

        // 2. For entity retrievals, they're all in a `businessAttributes` dict
        if (businessAttributes != null) {
            // Translate these into custom metadata structure
            try {
                cm = CustomMetadataCache.getCustomMetadataFromBusinessAttributes(businessAttributes);
            } catch (AtlanException e) {
                e.printStackTrace();
                throw new IOException("Unable to deserialize custom metadata.", e);
            }
        }

        Set<String> clsNames = null;
        if (classificationNames != null && classificationNames.isArray()) {
            clsNames = new HashSet<>();
            // Translate these IDs in to human-readable names
            try {
                for (JsonNode element : classificationNames) {
                    String name = ClassificationCache.getNameForId(element.asText());
                    clsNames.add(name);
                }
            } catch (AtlanException e) {
                throw new IOException("Unable to deserialize classification name.", e);
            }
        }

        // Special cases to wrap-up:
        // Decode the Readme's description after deserialization
        if (typeName != null && typeName.equals("Readme")) {
            Readme readme = (Readme) value;
            readme.setDescription(StringUtils.decodeContent(readme.getDescription()));
        }

        value.setCustomMetadataSets(cm);
        value.setClassificationNames(clsNames);

        return value;
    }

    private void deserialize(Entity value, JsonNode jsonNode, Method method)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        if (jsonNode.isValueNode()) {
            deserializePrimitive(value, jsonNode, method);
        } else if (jsonNode.isArray()) {
            deserializeList(value, (ArrayNode) jsonNode, method);
        } else if (jsonNode.isObject()) {
            deserializeObject(value, jsonNode, method);
        }
        // Only type left is null, which we don't need to explicitly set (it's the default)
    }

    private void deserializeList(Entity value, ArrayNode array, Method method)
            throws IllegalAccessException, InvocationTargetException, IOException {
        Class<?> paramClass = ReflectionCache.getParameterOfMethod(method);
        List<Object> list = new ArrayList<>();
        for (JsonNode element : array) {
            Object deserialized = deserializeElement(element, method);
            list.add(deserialized);
        }
        if (paramClass == List.class) {
            method.invoke(value, list);
        } else if (paramClass == Set.class || paramClass == SortedSet.class) {
            method.invoke(value, new TreeSet<>(list));
        } else {
            throw new IOException("Unable to deserialize JSON list to Java class: " + paramClass.getCanonicalName());
        }
    }

    private void deserializeObject(Entity value, JsonNode jsonObject, Method method)
            throws IllegalAccessException, InvocationTargetException, IOException {
        Class<?> paramClass = ReflectionCache.getParameterOfMethod(method);
        method.invoke(value, Serde.mapper.readValue(jsonObject.toString(), paramClass));
    }

    /**
     * Deserialize a value direct to an object.
     * @param element to deserialize
     * @param method to which the deserialized value will be built into an entity
     * @return the deserialized object
     * @throws IOException if an array is found nested directly within another array (unsupported)
     */
    private Object deserializeElement(JsonNode element, Method method) throws IOException {
        if (element.isValueNode()) {
            return JacksonUtils.deserializePrimitive(element, method);
        } else if (element.isArray()) {
            throw new IOException("Directly-nested arrays are not supported.");
        } else if (element.isObject()) {
            Type paramType = ReflectionCache.getParameterizedTypeOfMethod(method);
            Class<?> innerClass = ReflectionCache.getClassOfParameterizedType(paramType);
            return Serde.mapper.readValue(element.toString(), innerClass);
        }
        return null;
    }

    private void deserializePrimitive(Entity value, JsonNode primitive, Method method)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        if (primitive.isTextual()) {
            Class<?> paramClass = ReflectionCache.getParameterOfMethod(method);
            if (paramClass.isEnum()) {
                Method fromValue = paramClass.getMethod("fromValue", String.class);
                method.invoke(value, fromValue.invoke(null, primitive.asText()));
            } else {
                method.invoke(value, primitive.asText());
            }
        } else if (primitive.isBoolean()) {
            method.invoke(value, primitive.asBoolean());
        } else if (primitive.isNumber()) {
            deserializeNumber(value, primitive, method);
        }
    }

    private void deserializeNumber(Entity value, JsonNode primitive, Method method)
            throws IllegalAccessException, InvocationTargetException, IOException {
        Object number = JacksonUtils.deserializeNumber(primitive, method);
        method.invoke(value, number);
    }
}
