<#macro all>
    /**
     * Builds the minimal object necessary to create a SnowflakeTag.
     *
     * @param client connectivity to the Atlan tenant
     * @param name of the SnowflakeTag
     * @param schema in which the SnowflakeTag should be created, which must have at least
     *               a qualifiedName
     * @param allowedValues list of values that are allowed for the tag in Snowflake
     * @return the minimal request necessary to create the SnowflakeTag, as a builder
     * @throws InvalidRequestException if the schema provided is without a qualifiedName
     * @throws ConflictException if an Atlan tag already exists with the same name
     * @throws AtlanException on any API communication error
     */
    public static SnowflakeTagBuilder<?, ?> creator(
            AtlanClient client, String name, Schema schema, List<String> allowedValues) throws AtlanException {
        validateRelationship(
                Schema.TYPE_NAME,
                Map.of(
                        "connectionQualifiedName", schema.getConnectionQualifiedName(),
                        "databaseName", schema.getDatabaseName(),
                        "databaseQualifiedName", schema.getDatabaseQualifiedName(),
                        "name", schema.getName(),
                        "qualifiedName", schema.getQualifiedName()));
        return creator(
                        client,
                        name,
                        schema.getConnectionQualifiedName(),
                        schema.getDatabaseName(),
                        schema.getDatabaseQualifiedName(),
                        schema.getName(),
                        schema.getQualifiedName(),
                        allowedValues)
                .schema(schema.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a SnowflakeTag.
     *
     * @param client connectivity to the Atlan tenant
     * @param name of the SnowflakeTag
     * @param schemaQualifiedName unique name of the schema in which this SnowflakeTag exists
     * @param allowedValues list of values that are allowed for the tag in Snowflake
     * @return the minimal request necessary to create the SnowflakeTag, as a builder
     * @throws ConflictException if an Atlan tag already exists with the same name
     * @throws AtlanException on any API communication error
     */
    public static SnowflakeTagBuilder<?, ?> creator(
            AtlanClient client, String name, String schemaQualifiedName, List<String> allowedValues)
            throws AtlanException {
        String schemaName = StringUtils.getNameFromQualifiedName(schemaQualifiedName);
        String databaseQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(schemaQualifiedName);
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        return creator(
                client,
                name,
                connectionQualifiedName,
                databaseName,
                databaseQualifiedName,
                schemaName,
                schemaQualifiedName,
                allowedValues);
    }

    /**
     * Builds the minimal object necessary to create a SnowflakeTag.
     *
     * @param client connectivity to the Atlan tenant
     * @param name of the SnowflakeTag
     * @param connectionQualifiedName unique name of the connection in which to create the SnowflakeTag
     * @param databaseName simple name of the Database in which to create the SnowflakeTag
     * @param databaseQualifiedName unique name of the Database in which to create the SnowflakeTag
     * @param schemaName simple name of the Schema in which to create the SnowflakeTag
     * @param schemaQualifiedName unique name of the Schema in which to create the SnowflakeTag
     * @param allowedValues list of values that are allowed for the tag in Snowflake
     * @return the minimal request necessary to create the SnowflakeTag, as a builder
     * @throws ConflictException if an Atlan tag already exists with the same name
     * @throws AtlanException on any API communication error
     */
    public static SnowflakeTagBuilder<?, ?> creator(
            AtlanClient client,
            String name,
            String connectionQualifiedName,
            String databaseName,
            String databaseQualifiedName,
            String schemaName,
            String schemaQualifiedName,
            List<String> allowedValues)
            throws AtlanException {
        try {
            client.getAtlanTagCache().getIdForName(name);
            throw new ConflictException(ErrorCode.EXISTING_TAG, name);
        } catch (NotFoundException e) {
            AtlanTagDef tag =
                    AtlanTagDef.creator(name, AtlanTagOptions.builder().build())
                        .serviceType("atlan")
                        .attributeDef(AttributeDef.builder()
                            .name("sourceTagAttachment")
                            .displayName("sourceTagAttachment")
                            .typeName("array<SourceTagAttachment>")
                            .cardinality(AtlanCustomAttributeCardinality.SET)
                            .valuesMaxCount(2147483647L)
                            .includeInNotification(false)
                            .build())
                        .entityTypes(List.of("Asset"))
                        .build();
            tag.create(client);
        }
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        SourceTagAttribute sta = null;
        if (allowedValues != null && !allowedValues.isEmpty()) {
            try {
                sta = SourceTagAttribute.builder()
                        .tagAttributeKey("allowedValues")
                        .tagAttributeValue(Serde.allInclusiveMapper.writeValueAsString(allowedValues))
                        .build();
            } catch (JsonProcessingException e) {
                log.warn("Unable to set up allowedValues for SnowflakeTag.", e);
            }
        }
        return SnowflakeTag._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .displayText(name)
                .qualifiedName(generateQualifiedName(name, schemaQualifiedName))
                .connectorType(connectorType)
                .schemaName(schemaName)
                .schemaQualifiedName(schemaQualifiedName)
                .schema(Schema.refByQualifiedName(schemaQualifiedName))
                .databaseName(databaseName)
                .databaseQualifiedName(databaseQualifiedName)
                .connectionQualifiedName(connectionQualifiedName)
                .tagAllowedValues(allowedValues)
                .tagAttribute(sta)
                .mappedAtlanTagName(name);
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
        validateRequired(TYPE_NAME, Map.of(
            "qualifiedName", this.getQualifiedName(),
            "name", this.getName()
        ));
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
