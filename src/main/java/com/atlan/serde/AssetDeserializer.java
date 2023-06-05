/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.cache.AtlanTagCache;
import com.atlan.cache.CustomMetadataCache;
import com.atlan.cache.ReflectionCache;
import com.atlan.exception.AtlanException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.assets.*;
import com.atlan.model.core.AtlanTag;
import com.atlan.model.core.CustomMetadataAttributes;
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
 * Deserialization of all {@link Asset} objects, down through the entire inheritance hierarchy.
 * This custom deserialization is necessary to flatten some specific aspects of complexity in Atlan's payloads:
 * <ul>
 *     <li>The nested <code>attributes</code> and <code>relationshipAttributes</code> structures.</li>
 *     <li>The possibility that the same (relationship) attribute could appear in either of these nested structures.</li>
 *     <li>Handling the extension of properties as you traverse down the inheritance structures, without needing to also extend these nested structures through inheritance.</li>
 *     <li>Automatically translating the nested <code>businessAttributes</code> structure into custom metadata, including translating from Atlan's internal hashed-string representations into human-readable names.</li>
 * </ul>
 */
public class AssetDeserializer extends StdDeserializer<Asset> {

    private static final long serialVersionUID = 2L;

    public AssetDeserializer() {
        this(null);
    }

    public AssetDeserializer(Class<?> t) {
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
    public Asset deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return deserialize(parser.getCodec().readTree(parser));
    }

    /**
     * Actually do the work of deserializing an asset.
     *
     * @param root of the parsed JSON tree
     * @return the deserialized asset
     * @throws IOException on any issues parsing the JSON
     */
    @SuppressWarnings("deprecation") // Suppress deprecation notice on use of atlanTagNames builder
    Asset deserialize(JsonNode root) throws IOException {

        JsonNode attributes = root.get("attributes");
        JsonNode relationshipGuid = root.get("relationshipGuid");
        JsonNode relationshipAttributes = root.get("relationshipAttributes");
        JsonNode businessAttributes = root.get("businessAttributes");
        JsonNode classificationNames = root.get("classificationNames");

        Asset.AssetBuilder<?, ?> builder;
        JsonNode typeNameJson = root.get("typeName");
        String typeName = null;

        // TODO: figure out how to avoid needing to maintain this switch statement
        if (typeNameJson == null || typeNameJson.isNull()) {
            builder = IndistinctAsset.builder();
        } else {
            typeName = root.get("typeName").asText();
            switch (typeName) {
                case ADLSAccount.TYPE_NAME:
                    builder = ADLSAccount.builder();
                    break;
                case ADLSContainer.TYPE_NAME:
                    builder = ADLSContainer.builder();
                    break;
                case ADLSObject.TYPE_NAME:
                    builder = ADLSObject.builder();
                    break;
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
                case AuthPolicy.TYPE_NAME:
                    builder = AuthPolicy.builder();
                    break;
                case AuthService.TYPE_NAME:
                    builder = AuthService.builder();
                    break;
                case BIProcess.TYPE_NAME:
                    builder = BIProcess.builder();
                    break;
                case Badge.TYPE_NAME:
                    builder = Badge.builder();
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
                case File.TYPE_NAME:
                    builder = File.builder();
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
                case KafkaConsumerGroup.TYPE_NAME:
                    builder = KafkaConsumerGroup.builder();
                    break;
                case KafkaTopic.TYPE_NAME:
                    builder = KafkaTopic.builder();
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
                case MCIncident.TYPE_NAME:
                    builder = MCIncident.builder();
                    break;
                case MCMonitor.TYPE_NAME:
                    builder = MCMonitor.builder();
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
                case Persona.TYPE_NAME:
                    builder = Persona.builder();
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
                case Purpose.TYPE_NAME:
                    builder = Purpose.builder();
                    break;
                case QlikApp.TYPE_NAME:
                    builder = QlikApp.builder();
                    break;
                case QlikChart.TYPE_NAME:
                    builder = QlikChart.builder();
                    break;
                case QlikDataset.TYPE_NAME:
                    builder = QlikDataset.builder();
                    break;
                case QlikSheet.TYPE_NAME:
                    builder = QlikSheet.builder();
                    break;
                case QlikSpace.TYPE_NAME:
                    builder = QlikSpace.builder();
                    break;
                case QlikStream.TYPE_NAME:
                    builder = QlikStream.builder();
                    break;
                case QuickSightAnalysis.TYPE_NAME:
                    builder = QuickSightAnalysis.builder();
                    break;
                case QuickSightAnalysisVisual.TYPE_NAME:
                    builder = QuickSightAnalysisVisual.builder();
                    break;
                case QuickSightDashboard.TYPE_NAME:
                    builder = QuickSightDashboard.builder();
                    break;
                case QuickSightDashboardVisual.TYPE_NAME:
                    builder = QuickSightDashboardVisual.builder();
                    break;
                case QuickSightDataset.TYPE_NAME:
                    builder = QuickSightDataset.builder();
                    break;
                case QuickSightDatasetField.TYPE_NAME:
                    builder = QuickSightDatasetField.builder();
                    break;
                case QuickSightFolder.TYPE_NAME:
                    builder = QuickSightFolder.builder();
                    break;
                case Readme.TYPE_NAME:
                    builder = Readme.builder();
                    break;
                case ReadmeTemplate.TYPE_NAME:
                    builder = ReadmeTemplate.builder();
                    break;
                case RedashDashboard.TYPE_NAME:
                    builder = RedashDashboard.builder();
                    break;
                case RedashQuery.TYPE_NAME:
                    builder = RedashQuery.builder();
                    break;
                case RedashVisualization.TYPE_NAME:
                    builder = RedashVisualization.builder();
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
                case SigmaDataElement.TYPE_NAME:
                    builder = SigmaDataElement.builder();
                    break;
                case SigmaDataElementField.TYPE_NAME:
                    builder = SigmaDataElementField.builder();
                    break;
                case SigmaDataset.TYPE_NAME:
                    builder = SigmaDataset.builder();
                    break;
                case SigmaDatasetColumn.TYPE_NAME:
                    builder = SigmaDatasetColumn.builder();
                    break;
                case SigmaPage.TYPE_NAME:
                    builder = SigmaPage.builder();
                    break;
                case SigmaWorkbook.TYPE_NAME:
                    builder = SigmaWorkbook.builder();
                    break;
                case SnowflakePipe.TYPE_NAME:
                    builder = SnowflakePipe.builder();
                    break;
                case SnowflakeStream.TYPE_NAME:
                    builder = SnowflakeStream.builder();
                    break;
                case SnowflakeTag.TYPE_NAME:
                    builder = SnowflakeTag.builder();
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
                case ThoughtspotAnswer.TYPE_NAME:
                    builder = ThoughtspotAnswer.builder();
                    break;
                case ThoughtspotDashlet.TYPE_NAME:
                    builder = ThoughtspotDashlet.builder();
                    break;
                case ThoughtspotLiveboard.TYPE_NAME:
                    builder = ThoughtspotLiveboard.builder();
                    break;
                case View.TYPE_NAME:
                    builder = View.builder();
                    break;
                default:
                    builder = IndistinctAsset.builder();
                    break;
            }
        }

        // Start by deserializing all the non-attribute properties (defined at Asset-level)
        builder.typeName(JacksonUtils.deserializeString(root, "typeName"))
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
        Set<AtlanTag> atlanTags = JacksonUtils.deserializeObject(root, "classifications", new TypeReference<>() {});
        if (atlanTags != null) {
            builder.atlanTags(atlanTags);
        }
        TreeSet<String> meaningNames = JacksonUtils.deserializeObject(root, "meaningNames", new TypeReference<>() {});
        if (meaningNames != null) {
            builder.meaningNames(meaningNames);
        }
        TreeSet<Meaning> meanings = JacksonUtils.deserializeObject(root, "meanings", new TypeReference<>() {});
        if (meanings != null) {
            builder.meanings(meanings);
        }
        TreeSet<String> pendingTasks = JacksonUtils.deserializeObject(root, "pendingTasks", new TypeReference<>() {});
        if (pendingTasks != null) {
            builder.pendingTasks(pendingTasks);
        }

        Class<?> rootClazz = builder.build().getClass();
        Class<?> clazz = builder.getClass();

        Map<String, JsonNode> leftOverAttributes = new HashMap<>();
        // If the same attribute appears in both 'attributes' and 'relationshipAttributes'
        // then retain the 'relationshipAttributes' (more information) and skip the 'attributes'
        // copy of the same
        Set<String> processedAttributes = new HashSet<>();

        // Only process relationshipAttributes if this is a full asset, not a relationship
        // reference. (If it is a relationship reference, the relationshipGuid will be non-null.)
        if (relationshipGuid == null || relationshipGuid.isNull()) {
            if (relationshipAttributes != null && !relationshipAttributes.isNull()) {
                Iterator<String> itr = relationshipAttributes.fieldNames();
                while (itr.hasNext()) {
                    String relnKey = itr.next();
                    String deserializeName = ReflectionCache.getDeserializedName(rootClazz, relnKey);
                    Method method = ReflectionCache.getSetter(clazz, deserializeName);
                    if (method != null) {
                        try {
                            deserialize(builder, relationshipAttributes.get(relnKey), method, deserializeName);
                            processedAttributes.add(deserializeName);
                        } catch (NoSuchMethodException e) {
                            throw new IOException("Missing fromValue method for enum.", e);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new IOException("Failed to deserialize through reflection.", e);
                        }
                    }
                }
            }
        }

        if (attributes != null && !attributes.isNull()) {
            Iterator<String> itr = attributes.fieldNames();
            while (itr.hasNext()) {
                String attrKey = itr.next();
                String deserializeName = ReflectionCache.getDeserializedName(rootClazz, attrKey);
                // Only proceed with deserializing the 'attributes' copy of an attribute if
                // it was not already deserialized as a more complete relationship (above)
                if (!processedAttributes.contains(deserializeName)) {
                    Method method = ReflectionCache.getSetter(clazz, deserializeName);
                    if (method != null) {
                        try {
                            deserialize(builder, attributes.get(attrKey), method, deserializeName);
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

        // 2. For asset retrievals, they're all in a `businessAttributes` dict
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
                    String name = AtlanTagCache.getNameForId(element.asText());
                    clsNames.add(name);
                }
            } catch (AtlanException e) {
                throw new IOException("Unable to deserialize Atlan tag name.", e);
            }
        }

        // Special cases to wrap-up:
        // Decode the Readme's description after deserialization
        if (typeName != null && typeName.equals("Readme")) {
            builder.description(StringUtils.decodeContent(builder.build().getDescription()));
        }

        if (cm != null) {
            builder.customMetadataSets(cm);
        }
        if (clsNames != null) {
            builder.atlanTagNames(clsNames);
        }

        return builder.build();
    }

    private void deserialize(Asset.AssetBuilder<?, ?> builder, JsonNode jsonNode, Method method, String fieldName)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        if (jsonNode.isValueNode()) {
            deserializePrimitive(builder, jsonNode, method, fieldName);
        } else if (jsonNode.isArray()) {
            deserializeList(builder, (ArrayNode) jsonNode, method, fieldName);
        } else if (jsonNode.isObject()) {
            deserializeObject(builder, jsonNode, method);
        }
    }

    private void deserializeList(Asset.AssetBuilder<?, ?> builder, ArrayNode array, Method method, String fieldName)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        Class<?> paramClass = ReflectionCache.getParameterOfMethod(method);
        List<Object> list = new ArrayList<>();
        for (JsonNode element : array) {
            Object deserialized = deserializeElement(element, method, fieldName);
            list.add(deserialized);
        }
        if (paramClass == Collection.class || paramClass == List.class) {
            method.invoke(builder, list);
        } else if (paramClass == Set.class || paramClass == SortedSet.class) {
            method.invoke(builder, new TreeSet<>(list));
        } else {
            throw new IOException("Unable to deserialize JSON list to Java class: " + paramClass.getCanonicalName());
        }
    }

    private void deserializeObject(Asset.AssetBuilder<?, ?> builder, JsonNode jsonObject, Method method)
            throws IllegalAccessException, InvocationTargetException, IOException {
        Class<?> paramClass = ReflectionCache.getParameterOfMethod(method);
        if (paramClass == Map.class
                && ReflectionCache.getParameterizedTypeOfMethod(method)
                        .getTypeName()
                        .equals("java.util.Map<? extends java.lang.String, ? extends java.lang.Long>")) {
            // TODO: Unclear why this cannot be handled more generically, but nothing else seems to work
            method.invoke(builder, Serde.mapper.convertValue(jsonObject, new TypeReference<Map<String, Long>>() {}));
        } else {
            method.invoke(builder, Serde.mapper.convertValue(jsonObject, paramClass));
        }
    }

    /**
     * Deserialize a value direct to an object.
     * @param element to deserialize
     * @param method to which the deserialized value will be built into an asset
     * @param fieldName name of the field into which the value is being deserialized
     * @return the deserialized object
     * @throws IOException if an array is found nested directly within another array (unsupported)
     */
    private Object deserializeElement(JsonNode element, Method method, String fieldName)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        Type paramType = ReflectionCache.getParameterizedTypeOfMethod(method);
        Class<?> innerClass = ReflectionCache.getClassOfParameterizedType(paramType);
        if (element.isValueNode()) {
            if (fieldName.equals("purposeAtlanTags")) {
                String value;
                try {
                    value = AtlanTagCache.getNameForId(element.asText());
                } catch (NotFoundException e) {
                    value = Serde.DELETED_AUDIT_OBJECT;
                } catch (AtlanException e) {
                    throw new IOException("Unable to deserialize purposeAtlanTags.", e);
                }
                return value;
            }
            return JacksonUtils.deserializePrimitive(element, method, innerClass);
        } else if (element.isArray()) {
            throw new IOException("Directly-nested arrays are not supported.");
        } else if (element.isObject()) {
            return Serde.mapper.convertValue(element, innerClass);
        }
        return null;
    }

    private void deserializePrimitive(
            Asset.AssetBuilder<?, ?> builder, JsonNode primitive, Method method, String fieldName)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        if (primitive.isNull()) {
            // Explicitly deserialize null values to a representation
            // that we can identify on the object — necessary for audit entries
            builder.nullField(fieldName);
        } else {
            Object value = JacksonUtils.deserializePrimitive(primitive, method);
            if (fieldName.equals("mappedAtlanTagName")) {
                try {
                    value = AtlanTagCache.getNameForId(primitive.asText());
                } catch (NotFoundException e) {
                    value = Serde.DELETED_AUDIT_OBJECT;
                } catch (AtlanException e) {
                    throw new IOException("Unable to deserialize mappedAtlanTagName.", e);
                }
            }
            method.invoke(builder, value);
        }
    }
}
