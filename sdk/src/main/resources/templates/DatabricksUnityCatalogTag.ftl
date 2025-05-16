<#macro all>
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
        List<String> allowedValues
    ) {
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
            .tagAttribute(SourceTagAttribute.builder().tagAttributeKey("allowedValues").tagAttributeValue(allowedValuesString).build())
            .tagAllowedValues(allowedValues);
    }

    /**
     * Builds the minimal object necessary to update a DatabricksUnityCatalogTag.
     *
     * @param qualifiedName of the DatabricksUnityCatalogTag
     * @param name of the DatabricksUnityCatalogTag
     * @return the minimal request necessary to update the DatabricksUnityCatalogTag, as a builder
     */
    public static DatabricksUnityCatalogTagBuilder<?, ?> updater(String qualifiedName, String name) {
        return DatabricksUnityCatalogTag._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
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

    /**
     * Builds the minimal object necessary to apply an update to a DatabricksUnityCatalogTag, from a potentially
     * more-complete DatabricksUnityCatalogTag object.
     *
     * @return the minimal object necessary to update the DatabricksUnityCatalogTag, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DatabricksUnityCatalogTag are not found in the initial object
     */
    @Override
    public DatabricksUnityCatalogTagBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
