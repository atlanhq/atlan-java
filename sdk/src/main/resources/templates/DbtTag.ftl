<#macro all>
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
        List<String> allowedValues
    ) {
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
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
            .connectorType(connectorType)
            .connectionQualifiedName(connectionQualifiedName)
            .mappedAtlanTagName(mappedAtlanTagName)
            .tagId(sourceId)
            .tagAttribute(SourceTagAttribute.builder().tagAttributeKey("allowedValues").tagAttributeValue(allowedValuesString).build())
            .tagAllowedValues(allowedValues);
    }

    /**
     * Builds the minimal object necessary to update a DbtTag.
     *
     * @param qualifiedName of the DbtTag
     * @param name of the DbtTag
     * @return the minimal request necessary to update the DbtTag, as a builder
     */
    public static DbtTagBuilder<?, ?> updater(String qualifiedName, String name) {
        return DbtTag._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
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
    public static String generateQualifiedName(String name, String connectionQualifiedName, String accountId, String projectId) {
        return connectionQualifiedName + "/account/" + accountId + "/project/" + projectId + "/tag/" + name;
    }

    /**
     * Builds the minimal object necessary to apply an update to a DbtTag, from a potentially
     * more-complete DbtTag object.
     *
     * @return the minimal object necessary to update the DbtTag, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DbtTag are not found in the initial object
     */
    @Override
    public DbtTagBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
