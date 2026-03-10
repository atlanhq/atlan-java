// IMPORT: import com.atlan.model.structs.SourceTagAttribute;
// IMPORT: import com.atlan.serde.Serde;
// IMPORT: import com.fasterxml.jackson.core.JsonProcessingException;

/**
     * Builds the minimal object necessary to create a DatabricksUnityCatalogTag.
     *
     * @param name of the DatabricksUnityCatalogTag
     * @param connectionQualifiedName unique name of the connection in which to create the DatabricksUnityCatalogTag
     * @param mappedAtlanTagName the human-readable name of the Atlan tag to which this DatabricksUnityCatalogTag should map
     * @param sourceId unique identifier for the tag in the source
     * @param allowedValues the values allowed to be set for this tag in the source
     * @return the minimal request necessary to create the DatabricksUnityCatalogTag, as a builder
     */
    public static DatabricksUnityCatalogTagBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String mappedAtlanTagName,
            String sourceId,
            List<String> allowedValues) {
        String allowedValuesString = "";
        try {
            allowedValuesString = Serde.allInclusiveMapper.writeValueAsString(allowedValues);
        } catch (JsonProcessingException e) {
            log.error("Unable to transform list of allowed values into singular string.", e);
        }
        return DatabricksUnityCatalogTag._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName))
                .connectionQualifiedName(connectionQualifiedName)
                .mappedAtlanTagName(mappedAtlanTagName)
                .tagId(sourceId)
                .tagAttribute(SourceTagAttribute.builder()
                        .tagAttributeKey("allowedValues")
                        .tagAttributeValue(allowedValuesString)
                        .build())
                .tagAllowedValues(allowedValues);
    }

/**
     * Generate a unique DatabricksUnityCatalogTag name.
     *
     * @param name of the DatabricksUnityCatalogTag
     * @param connectionQualifiedName unique name of the schema in which this DatabricksUnityCatalogTag exists
     * @return a unique name for the DatabricksUnityCatalogTag
     */
    public static String generateQualifiedName(String name, String connectionQualifiedName) {
        return connectionQualifiedName + "/tag/" + name;
    }