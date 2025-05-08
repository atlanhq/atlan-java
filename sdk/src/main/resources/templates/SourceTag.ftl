<#macro all>
    /**
     * Builds the minimal object necessary to create a SourceTag.
     *
     * @param name of the SourceTag
     * @param connectionQualifiedName unique name of the connection in which to create the SourceTag
     * @param mappedAtlanTagName the human-readable name of the Atlan tag to which this SourceTag should map
     * @param sourceTagId unique identifier for the tag in the source
     * @param allowedValues the values allowed to be set for this tag in the source
     * @return the minimal request necessary to create the SourceTag, as a builder
     */
    public static SourceTagBuilder<?, ?> creator(
        String name,
        String connectionQualifiedName,
        String mappedAtlanTagName,
        String sourceTagId,
        List<String> allowedValues
    ) {
        String allowedValuesString = "";
        try {
            allowedValuesString = Serde.allInclusiveMapper.writeValueAsString(allowedValues);
        } catch (JsonProcessingException e) {
            log.error("Unable to transform list of allowed values into singular string.", e);
        }
        return SourceTag._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .name(name)
            .qualifiedName(generateQualifiedName(name, connectionQualifiedName))
            .connectionQualifiedName(connectionQualifiedName)
            .mappedAtlanTagName(mappedAtlanTagName)
            .tagId(sourceTagId)
            .tagAttribute(SourceTagAttribute.builder().tagAttributeKey("allowedValues").tagAttributeValue(allowedValuesString).build())
            .tagAllowedValues(allowedValues);
    }

    /**
     * Builds the minimal object necessary to update a SourceTag.
     *
     * @param qualifiedName of the SourceTag
     * @param name of the SourceTag
     * @return the minimal request necessary to update the SourceTag, as a builder
     */
    public static SourceTagBuilder<?, ?> updater(String qualifiedName, String name) {
        return SourceTag._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique SourceTag name.
     *
     * @param name of the SourceTag
     * @param connectionQualifiedName unique name of the schema in which this SourceTag exists
     * @return a unique name for the SourceTag
     */
    public static String generateQualifiedName(String name, String connectionQualifiedName) {
        return connectionQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to apply an update to a SourceTag, from a potentially
     * more-complete SourceTag object.
     *
     * @return the minimal object necessary to update the SourceTag, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SourceTag are not found in the initial object
     */
    @Override
    public SourceTagBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
