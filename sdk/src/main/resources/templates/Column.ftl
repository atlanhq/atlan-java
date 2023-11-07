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
        if (table.getQualifiedName() == null || table.getQualifiedName().isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, "Table", "qualifiedName");
        }
        return creator(name, table.getTypeName(), table.getQualifiedName(), order)
                .table(table.trimToReference());
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
    public static ColumnBuilder<?, ?> creator(String name, TablePartition partition, int order) throws InvalidRequestException {
        if (partition.getQualifiedName() == null || partition.getQualifiedName().isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, "TablePartition", "qualifiedName");
        }
        return creator(name, partition.getTypeName(), partition.getQualifiedName(), order)
                .tablePartition(partition.trimToReference());
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
        if (view.getQualifiedName() == null || view.getQualifiedName().isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, "View", "qualifiedName");
        }
        return creator(name, view.getTypeName(), view.getQualifiedName(), order)
                .view(view.trimToReference());
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
    public static ColumnBuilder<?, ?> creator(String name, MaterializedView view, int order) throws InvalidRequestException {
        if (view.getQualifiedName() == null || view.getQualifiedName().isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, "MaterializedView", "qualifiedName");
        }
        return creator(name, view.getTypeName(), view.getQualifiedName(), order)
                .materializedView(view.trimToReference());
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
        String[] tokens = parentQualifiedName.split("/");
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(tokens);
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
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "Column", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
