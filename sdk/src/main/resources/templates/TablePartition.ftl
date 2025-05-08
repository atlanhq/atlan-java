<#macro all>
    /**
     * Builds the minimal object necessary to create a table partition.
     *
     * @param name of the table partition
     * @param table in which the partition should be created, which must have at least
     *              a qualifiedName
     * @return the minimal request necessary to create the table partition, as a builder
     * @throws InvalidRequestException if the table provided is without a qualifiedName
     */
    public static TablePartitionBuilder<?, ?> creator(String name, Table table) throws InvalidRequestException {
        if (table.getQualifiedName() == null || table.getQualifiedName().isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, "Table", "qualifiedName");
        }
        return creator(name, table.getQualifiedName()).parentTable(table.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a table partition.
     *
     * @param name of the table partition
     * @param tableQualifiedName unique name of the table in which this table partition exists
     * @return the minimal request necessary to create the table partition, as a builder
     */
    public static TablePartitionBuilder<?, ?> creator(String name, String tableQualifiedName) {
        String tableName = StringUtils.getNameFromQualifiedName(tableQualifiedName);
        String schemaQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(tableQualifiedName);
        String schemaName = StringUtils.getNameFromQualifiedName(schemaQualifiedName);
        String databaseQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(schemaQualifiedName);
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        return creator(name, connectionQualifiedName, databaseName, databaseQualifiedName, schemaName, schemaQualifiedName, tableName, tableQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a TablePartition.
     *
     * @param name of the TablePartition
     * @param connectionQualifiedName unique name of the connection in which to create the TablePartition
     * @param databaseName simple name of the Database in which to create the TablePartition
     * @param databaseQualifiedName unique name of the Database in which to create the TablePartition
     * @param schemaName simple name of the Schema in which to create the TablePartition
     * @param schemaQualifiedName unique name of the Schema in which to create the TablePartition
     * @param tableName simple name of the Table in which to create the TablePartition
     * @param tableQualifiedName unique name of the Table in which to create the TablePartition
     * @return the minimal request necessary to create the TablePartition, as a builder
     */
    public static TablePartitionBuilder<?, ?> creator(
        String name,
        String connectionQualifiedName,
        String databaseName,
        String databaseQualifiedName,
        String schemaName,
        String schemaQualifiedName,
        String tableName,
        String tableQualifiedName
    ) {
        return TablePartition._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, schemaQualifiedName))
                .parentTable(Table.refByQualifiedName(tableQualifiedName))
                .schemaName(schemaName)
                .schemaQualifiedName(schemaQualifiedName)
                .databaseName(databaseName)
                .databaseQualifiedName(databaseQualifiedName)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Generate a unique table partition name.
     *
     * @param name of the table partition
     * @param schemaQualifiedName unique name of the schema in which this partition exists
     * @return a unique name for the table partition
     */
    public static String generateQualifiedName(String name, String schemaQualifiedName) {
        return schemaQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a TablePartition.
     *
     * @param qualifiedName of the TablePartition
     * @param name of the TablePartition
     * @return the minimal request necessary to update the TablePartition, as a builder
     */
    public static TablePartitionBuilder<?, ?> updater(String qualifiedName, String name) {
        return TablePartition._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TablePartition, from a potentially
     * more-complete TablePartition object.
     *
     * @return the minimal object necessary to update the TablePartition, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TablePartition are not found in the initial object
     */
    @Override
    public TablePartitionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
