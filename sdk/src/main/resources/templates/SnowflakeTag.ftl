<#macro all>
    /**
     * Builds the minimal object necessary to create a SnowflakeTag.
     *
     * @param name of the SnowflakeTag
     * @param schema in which the SnowflakeTag should be created, which must have at least
     *               a qualifiedName
     * @param mappedAtlanTagName the human-readable name of the Atlan tag to which this SnowflakeTag should map
     * @param snowflakeTagId unique identifier for the tag in Snowflake (usually numeric)
     * @param allowedValues the values allowed to be set for this tag in Snowflake
     * @return the minimal request necessary to create the SnowflakeTag, as a builder
     * @throws InvalidRequestException if the schema provided is without a qualifiedName
     */
    public static SnowflakeTagBuilder<?, ?> creator(String name, Schema schema, String mappedAtlanTagName, String snowflakeTagId, List<String> allowedValues) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", schema.getConnectionQualifiedName());
        map.put("databaseName", schema.getDatabaseName());
        map.put("databaseQualifiedName", schema.getDatabaseQualifiedName());
        map.put("name", schema.getName());
        map.put("qualifiedName", schema.getQualifiedName());
        validateRelationship(Schema.TYPE_NAME, map);
        return creator(
            name,
            schema.getConnectionQualifiedName(),
            schema.getDatabaseName(),
            schema.getDatabaseQualifiedName(),
            schema.getName(),
            schema.getQualifiedName(),
            mappedAtlanTagName,
            snowflakeTagId,
            allowedValues
        ).schema(schema.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a SnowflakeTag.
     *
     * @param name of the SnowflakeTag
     * @param schemaQualifiedName unique name of the schema in which this SnowflakeTag exists
     * @param mappedAtlanTagName the human-readable name of the Atlan tag to which this SnowflakeTag should map
     * @param snowflakeTagId unique identifier for the tag in Snowflake (usually numeric)
     * @param allowedValues the values allowed to be set for this tag in Snowflake
     * @return the minimal request necessary to create the SnowflakeTag, as a builder
     */
    public static SnowflakeTagBuilder<?, ?> creator(String name, String schemaQualifiedName, String mappedAtlanTagName, String snowflakeTagId, List<String> allowedValues) {
        String schemaName = StringUtils.getNameFromQualifiedName(schemaQualifiedName);
        String databaseQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(schemaQualifiedName);
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        return creator(name, connectionQualifiedName, databaseName, databaseQualifiedName, schemaName, schemaQualifiedName, mappedAtlanTagName, snowflakeTagId, allowedValues);
    }

    /**
     * Builds the minimal object necessary to create a SnowflakeTag.
     *
     * @param name of the SnowflakeTag
     * @param connectionQualifiedName unique name of the connection in which to create the SnowflakeTag
     * @param databaseName simple name of the Database in which to create the SnowflakeTag
     * @param databaseQualifiedName unique name of the Database in which to create the SnowflakeTag
     * @param schemaName simple name of the Schema in which to create the SnowflakeTag
     * @param schemaQualifiedName unique name of the Schema in which to create the SnowflakeTag
     * @param mappedAtlanTagName the human-readable name of the Atlan tag to which this SnowflakeTag should map
     * @param snowflakeTagId unique identifier for the tag in Snowflake (usually numeric)
     * @param allowedValues the values allowed to be set for this tag in Snowflake
     * @return the minimal request necessary to create the SnowflakeTag, as a builder
     */
    public static SnowflakeTagBuilder<?, ?> creator(
        String name,
        String connectionQualifiedName,
        String databaseName,
        String databaseQualifiedName,
        String schemaName,
        String schemaQualifiedName,
        String mappedAtlanTagName,
        String snowflakeTagId,
        List<String> allowedValues
    ) {
        String allowedValuesString = "";
        try {
            allowedValuesString = Serde.allInclusiveMapper.writeValueAsString(allowedValues);
        } catch (JsonProcessingException e) {
            log.error("Unable to transform list of allowed values into singular string.", e);
        }
        return SnowflakeTag._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .name(name)
            .qualifiedName("abc")
            .schemaName(schemaName)
            .schemaQualifiedName(schemaQualifiedName)
            .schema(Schema.refByQualifiedName(schemaQualifiedName))
            .databaseName(databaseName)
            .databaseQualifiedName(databaseQualifiedName)
            .connectionQualifiedName(connectionQualifiedName)
            .mappedAtlanTagName(mappedAtlanTagName)
            .tagId(snowflakeTagId)
            .tagAttribute(SourceTagAttribute.builder().tagAttributeKey("allowedValues").tagAttributeValue(allowedValuesString).build())
            .tagAllowedValues(allowedValues);
    }

    /**
     * Builds the minimal object necessary to update a SnowflakeTag.
     *
     * @param qualifiedName of the SnowflakeTag
     * @param name of the SnowflakeTag
     * @return the minimal request necessary to update the SnowflakeTag, as a builder
     */
    public static SnowflakeTagBuilder<?, ?> updater(String qualifiedName, String name) {
        return SnowflakeTag._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique SnowflakeTag name.
     *
     * @param name of the SnowflakeTag
     * @param schemaQualifiedName unique name of the schema in which this SnowflakeTag exists
     * @return a unique name for the SnowflakeTag
     */
    public static String generateQualifiedName(String name, String schemaQualifiedName) {
        return schemaQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to apply an update to a SnowflakeTag, from a potentially
     * more-complete SnowflakeTag object.
     *
     * @return the minimal object necessary to update the SnowflakeTag, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SnowflakeTag are not found in the initial object
     */
    @Override
    public SnowflakeTagBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
