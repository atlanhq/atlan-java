<#macro all>
    /**
     * Builds the minimal object necessary to create a CustomField.
     *
     * @param name of the CustomField
     * @param table in which the CustomField should be created, which must have at least
     *               a qualifiedName and name
     * @return the minimal request necessary to create the CustomField, as a builder
     * @throws InvalidRequestException if the table provided is without any required attributes
     */
    public static CustomFieldBuilder<?, ?> creator(String name, CustomTable table)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", table.getQualifiedName());
        map.put("name", table.getName());
        validateRelationship(CustomTable.TYPE_NAME, map);
        return creator(
            name,
            table.getQualifiedName(),
            table.getName()
        ).customTable(table.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a CustomField.
     *
     * @param name of the CustomField (must be unique within the table)
     * @param tableQualifiedName unique name of the CustomTable in which the CustomField exists
     * @param tableName simple human-readable name of the CustomTable in which the CustomField exists
     * @return the minimal object necessary to create the CustomField, as a builder
     */
    public static CustomFieldBuilder<?, ?> creator(
            String name, String tableQualifiedName, String tableName) {
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(tableQualifiedName);
        String datasetQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(tableQualifiedName);
        String datasetName = StringUtils.getNameFromQualifiedName(datasetQualifiedName);
        return CustomField._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, tableQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName))
                .customDatasetQualifiedName(datasetQualifiedName)
                .customDatasetName(datasetName)
                .tableQualifiedName(tableQualifiedName)
                .tableName(tableName)
                .customTable(CustomTable.refByQualifiedName(tableQualifiedName));
    }

    /**
     * Generate a unique CustomField name.
     *
     * @param name of the CustomField
     * @param tableQualifiedName unique name of the CustomTable in which this CustomField exists
     * @return a unique name for the CustomField
     */
    public static String generateQualifiedName(String name, String tableQualifiedName) {
        return tableQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a CustomField.
     *
     * @param qualifiedName of the CustomField
     * @param name of the CustomField
     * @return the minimal request necessary to update the CustomField, as a builder
     */
    public static CustomFieldBuilder<?, ?> updater(String qualifiedName, String name) {
        return CustomField._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a CustomField, from a potentially
     * more-complete CustomField object.
     *
     * @return the minimal object necessary to update the CustomField, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for CustomField are not found in the initial object
     */
    @Override
    public CustomFieldBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
