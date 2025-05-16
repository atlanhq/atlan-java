<#macro all>
    /**
     * Retrieve the parent of this Column, irrespective of its type.
     * @return the reference to this Column's parent
     */
    public ISQL getParent() {
        if (table != null) {
            return (ISQL) table;
        } else if (view != null) {
            return (ISQL) view;
        } else if (materializedView != null) {
            return (ISQL) materializedView;
        } else if (tablePartition != null) {
            return (ISQL) tablePartition;
        }
        return null;
    }

    /**
     * Builds the minimal object necessary to create a Column.
     *
     * @param name of the Column
     * @param table in which the Column should be created, which must have at least
     *              a qualifiedName
     * @param order the order the Column appears within its table (the Column's position)
     * @return the minimal request necessary to create the Column, as a builder
     * @throws InvalidRequestException if the table provided is without a qualifiedName
     */
    public static ColumnBuilder<?, ?> creator(String name, Table table, int order) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", table.getConnectionQualifiedName());
        map.put("databaseName", table.getDatabaseName());
        map.put("databaseQualifiedName", table.getDatabaseQualifiedName());
        map.put("schemaName", table.getSchemaName());
        map.put("name", table.getName());
        map.put("qualifiedName", table.getQualifiedName());
        validateRelationship(Table.TYPE_NAME, map);
        return creator(
            name,
            table.getConnectionQualifiedName(),
            table.getDatabaseName(),
            table.getDatabaseQualifiedName(),
            table.getSchemaName(),
            table.getSchemaQualifiedName(),
            table.getName(),
            table.getQualifiedName(),
            Table.TYPE_NAME,
            null,
            null,
            order
        ).table(table.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Column.
     *
     * @param name of the Column
     * @param partition in which the Column should be created, which must have at least
     *                  a qualifiedName
     * @param order the order the Column appears within its partition (the Column's position)
     * @return the minimal request necessary to create the Column, as a builder
     * @throws InvalidRequestException if the partition provided is without a qualifiedName
     */
    public static ColumnBuilder<?, ?> creator(String name, TablePartition partition, int order)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", partition.getConnectionQualifiedName());
        map.put("databaseName", partition.getDatabaseName());
        map.put("databaseQualifiedName", partition.getDatabaseQualifiedName());
        map.put("schemaName", partition.getSchemaName());
        map.put("schemaQualifiedName", partition.getSchemaQualifiedName());
        map.put("name", partition.getName());
        map.put("qualifiedName", partition.getQualifiedName());
        validateRelationship(TablePartition.TYPE_NAME, map);
        return creator(
            name,
            partition.getConnectionQualifiedName(),
            partition.getDatabaseName(),
            partition.getDatabaseQualifiedName(),
            partition.getSchemaName(),
            partition.getSchemaQualifiedName(),
            partition.getName(),
            partition.getQualifiedName(),
            TablePartition.TYPE_NAME,
            null,
            null,
            order
        ).tablePartition(partition.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Column.
     *
     * @param name of the Column
     * @param view in which the Column should be created, which must have at least
     *             a qualifiedName
     * @param order the order the Column appears within its view (the Column's position)
     * @return the minimal request necessary to create the Column, as a builder
     * @throws InvalidRequestException if the view provided is without a qualifiedName
     */
    public static ColumnBuilder<?, ?> creator(String name, View view, int order) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", view.getConnectionQualifiedName());
        map.put("databaseName", view.getDatabaseName());
        map.put("databaseQualifiedName", view.getDatabaseQualifiedName());
        map.put("schemaName", view.getSchemaName());
        map.put("schemaQualifiedName", view.getSchemaQualifiedName());
        map.put("name", view.getName());
        map.put("qualifiedName", view.getQualifiedName());
        validateRelationship(View.TYPE_NAME, map);
        return creator(
            name,
            view.getConnectionQualifiedName(),
            view.getDatabaseName(),
            view.getDatabaseQualifiedName(),
            view.getSchemaName(),
            view.getSchemaQualifiedName(),
            view.getName(),
            view.getQualifiedName(),
            View.TYPE_NAME,
            null,
            null,
            order
        ).view(view.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Column.
     *
     * @param name of the Column
     * @param view in which the Column should be created, which must have at least
     *             a qualifiedName
     * @param order the order the Column appears within its materialized view (the Column's position)
     * @return the minimal request necessary to create the Column, as a builder
     * @throws InvalidRequestException if the materialized view provided is without a qualifiedName
     */
    public static ColumnBuilder<?, ?> creator(String name, MaterializedView view, int order)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", view.getConnectionQualifiedName());
        map.put("databaseName", view.getDatabaseName());
        map.put("databaseQualifiedName", view.getDatabaseQualifiedName());
        map.put("schemaName", view.getSchemaName());
        map.put("schemaQualifiedName", view.getSchemaQualifiedName());
        map.put("name", view.getName());
        map.put("qualifiedName", view.getQualifiedName());
        validateRelationship(MaterializedView.TYPE_NAME, map);
        return creator(
            name,
            view.getConnectionQualifiedName(),
            view.getDatabaseName(),
            view.getDatabaseQualifiedName(),
            view.getSchemaName(),
            view.getSchemaQualifiedName(),
            view.getName(),
            view.getQualifiedName(),
            MaterializedView.TYPE_NAME,
            null,
            null,
            order
        ).materializedView(view.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Column.
     *
     * @param name of the Column
     * @param parentType type of parent (table, view, materialized view), should be a TYPE_NAME static string
     * @param parentQualifiedName unique name of the table / view / materialized view in which this Column exists
     * @param order the order the Column appears within its parent (the Column's position)
     * @return the minimal request necessary to create the Column, as a builder
     */
    public static ColumnBuilder<?, ?> creator(String name, String parentType, String parentQualifiedName, int order) {
        String parentName = StringUtils.getNameFromQualifiedName(parentQualifiedName);
        String schemaQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(parentQualifiedName);
        String schemaName = StringUtils.getNameFromQualifiedName(schemaQualifiedName);
        String databaseQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(schemaQualifiedName);
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        return creator(
            name,
            connectionQualifiedName,
            databaseName,
            databaseQualifiedName,
            schemaName,
            schemaQualifiedName,
            parentName,
            parentQualifiedName,
            parentType,
            null,
            null,
            order
        );
    }

    /**
     * Builds the minimal object necessary to create a Column.
     *
     * @param name of the Column
     * @param connectionQualifiedName unique name of the connection in which the Column should be created
     * @param databaseName simple name of the database in which the Column should be created
     * @param databaseQualifiedName unique name of the database in which the Column should be created
     * @param schemaName simple name of the schema in which the Column should be created
     * @param schemaQualifiedName unique name of the schema in which the Column should be created
     * @param parentName simple name of the table / view / materialized view in which the Column should be created
     * @param parentQualifiedName unique name of the table / view / materialized view in which this Column exists
     * @param parentType type of parent (table, view, materialized view), should be a TYPE_NAME static string
     * @param tableName (deprecated - unused)
     * @param tableQualifiedName (deprecated - unused)
     * @param order the order the Column appears within its parent (the Column's position)
     * @return the minimal request necessary to create the Column, as a builder
     */
    public static ColumnBuilder<?, ?> creator(
        String name,
        String connectionQualifiedName,
        String databaseName,
        String databaseQualifiedName,
        String schemaName,
        String schemaQualifiedName,
        String parentName,
        String parentQualifiedName,
        String parentType,
        String tableName,
        String tableQualifiedName,
        int order
    ) {
        ColumnBuilder<?, ?> builder = Column._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .name(name)
            .qualifiedName(generateQualifiedName(name, parentQualifiedName))
            .schemaName(schemaName)
            .schemaQualifiedName(schemaQualifiedName)
            .databaseName(databaseName)
            .databaseQualifiedName(databaseQualifiedName)
            .connectionQualifiedName(connectionQualifiedName)
            .order(order);
        switch (parentType) {
            case Table.TYPE_NAME:
                builder.tableName(parentName)
                    .tableQualifiedName(parentQualifiedName)
                    .table(Table.refByQualifiedName(parentQualifiedName));
                break;
            case View.TYPE_NAME:
                builder.viewName(parentName)
                    .viewQualifiedName(parentQualifiedName)
                    .view(View.refByQualifiedName(parentQualifiedName));
                break;
            case MaterializedView.TYPE_NAME:
                builder.viewName(parentName)
                    .viewQualifiedName(parentQualifiedName)
                    .materializedView(MaterializedView.refByQualifiedName(parentQualifiedName));
                break;
            case TablePartition.TYPE_NAME:
                builder.tableName(parentName)
                    .tableQualifiedName(parentQualifiedName)
                    .tablePartition(TablePartition.refByQualifiedName(parentQualifiedName));
                break;
            case SnowflakeDynamicTable.TYPE_NAME:
                builder.tableName(parentName)
                    .tableQualifiedName(parentQualifiedName)
                    .snowflakeDynamicTable(SnowflakeDynamicTable.refByQualifiedName(parentQualifiedName));
                break;
        }
        return builder;
    }

    /**
     * Generate a unique Column name.
     *
     * @param name of the Column
     * @param parentQualifiedName unique name of the container in which this Column exists
     * @return a unique name for the Column
     */
    public static String generateQualifiedName(String name, String parentQualifiedName) {
        return parentQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a Column.
     *
     * @param qualifiedName of the Column
     * @param name of the Column
     * @return the minimal request necessary to update the Column, as a builder
     */
    public static ColumnBuilder<?, ?> updater(String qualifiedName, String name) {
        return Column._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Column, from a potentially
     * more-complete Column object.
     *
     * @return the minimal object necessary to update the Column, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for Column are not found in the initial object
     */
    @Override
    public ColumnBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
