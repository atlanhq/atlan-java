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
        validateRelationship(Table.TYPE_NAME, Map.of(
            "connectionQualifiedName", table.getConnectionQualifiedName(),
            "databaseName", table.getDatabaseName(),
            "databaseQualifiedName", table.getDatabaseQualifiedName(),
            "schemaName", table.getSchemaName(),
            "schemaQualifiedName", table.getSchemaQualifiedName(),
            "name", table.getName(),
            "qualifiedName", table.getQualifiedName()
        ));
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
        validateRelationship(TablePartition.TYPE_NAME, Map.of(
            "connectionQualifiedName", partition.getConnectionQualifiedName(),
            "databaseName", partition.getDatabaseName(),
            "databaseQualifiedName", partition.getDatabaseQualifiedName(),
            "schemaName", partition.getSchemaName(),
            "schemaQualifiedName", partition.getSchemaQualifiedName(),
            "name", partition.getName(),
            "qualifiedName", partition.getQualifiedName(),
            "tableName", partition.getTableName(),
            "tableQualifiedName", partition.getTableQualifiedName()
        ));
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
            partition.getTableName(),
            partition.getTableQualifiedName(),
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
        validateRelationship(View.TYPE_NAME, Map.of(
            "connectionQualifiedName", view.getConnectionQualifiedName(),
            "databaseName", view.getDatabaseName(),
            "databaseQualifiedName", view.getDatabaseQualifiedName(),
            "schemaName", view.getSchemaName(),
            "schemaQualifiedName", view.getSchemaQualifiedName(),
            "name", view.getName(),
            "qualifiedName", view.getQualifiedName()
        ));
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
        validateRelationship(MaterializedView.TYPE_NAME, Map.of(
            "connectionQualifiedName", view.getConnectionQualifiedName(),
            "databaseName", view.getDatabaseName(),
            "databaseQualifiedName", view.getDatabaseQualifiedName(),
            "schemaName", view.getSchemaName(),
            "schemaQualifiedName", view.getSchemaQualifiedName(),
            "name", view.getName(),
            "qualifiedName", view.getQualifiedName()
        ));
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
        String tableName = null;
        String tableQualifiedName = null;
        String schemaQualifiedName;
        if (TablePartition.TYPE_NAME.equals(parentType)) {
            tableQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(parentQualifiedName);
            tableName = StringUtils.getNameFromQualifiedName(tableQualifiedName);
            schemaQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(tableQualifiedName);
        } else {
            schemaQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(parentQualifiedName);
        }
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
            tableName,
            tableQualifiedName,
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
     * @param tableName simple name of the table if the parentType is TablePartition
     * @param tableQualifiedName unique name of the table if the parentType is TablePartition
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
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(parentQualifiedName);
        ColumnBuilder<?, ?> builder = Column._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .name(name)
            .qualifiedName(generateQualifiedName(name, parentQualifiedName))
            .connectorType(connectorType)
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
                builder.tableName(tableName)
                    .tableQualifiedName(tableQualifiedName)
                    .tablePartition(TablePartition.refByQualifiedName(parentQualifiedName));
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
        validateRequired(TYPE_NAME, Map.of(
            "qualifiedName", this.getQualifiedName(),
            "name", this.getName()
        ));
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
