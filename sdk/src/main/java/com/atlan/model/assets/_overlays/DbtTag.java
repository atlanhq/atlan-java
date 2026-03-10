// IMPORT: import com.atlan.model.structs.DbtJobRun;
// IMPORT: import com.atlan.model.structs.SourceTagAttribute;
// IMPORT: import com.atlan.serde.Serde;
// IMPORT: import com.fasterxml.jackson.core.JsonProcessingException;

/**
     * Builds the minimal object necessary to create a DbtTag.
     *
     * @param name of the DbtTag
     * @param connectionQualifiedName unique name of the connection in which to create the DbtTag
     * @param mappedAtlanTagName the human-readable name of the Atlan tag to which this DbtTag should map
     * @param accountId the numeric ID of the dbt account in which the tag exists
     * @param projectId the numeric ID of the dbt project in which the tag exists
     * @param sourceId unique identifier for the tag in the source
     * @param allowedValues the values allowed to be set for this tag in the source
     * @return the minimal request necessary to create the DbtTag, as a builder
     */
    public static DbtTagBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String mappedAtlanTagName,
            String accountId,
            String projectId,
            String sourceId,
            List<String> allowedValues) {
        String allowedValuesString = "";
        try {
            allowedValuesString = Serde.allInclusiveMapper.writeValueAsString(allowedValues);
        } catch (JsonProcessingException e) {
            log.error("Unable to transform list of allowed values into singular string.", e);
        }
        return DbtTag._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName, accountId, projectId))
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
     * Generate a unique DbtTag name.
     *
     * @param name of the DbtTag
     * @param connectionQualifiedName unique name of the schema in which this DbtTag exists
     * @param accountId the numeric ID of the dbt account in which the tag exists
     * @param projectId the numeric ID of the dbt project in which the tag exists
     * @return a unique name for the DbtTag
     */
    public static String generateQualifiedName(
            String name, String connectionQualifiedName, String accountId, String projectId) {
        return connectionQualifiedName + "/account/" + accountId + "/project/" + projectId + "/tag/" + name;
    }