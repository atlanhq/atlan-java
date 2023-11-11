<#macro imports>
import com.atlan.model.assets.Connection;
import com.atlan.model.assets.Table;
import com.atlan.model.assets.TablePartition;
import com.atlan.model.assets.View;
import com.atlan.model.assets.MaterializedView;
</#macro>
<#macro all>
    /**
     * Builds the minimal object necessary to create a GuacamoleColumn.
     *
     * @param name of the GuacamoleColumn
     * @param parentQualifiedName unique name of the GuacamoleTable in which this GuacamoleColumn exists
     * @param order the order the GuacamoleColumn appears within its parent (the GuacamoleColumn's position)
     * @return the minimal request necessary to create the GuacamoleColumn, as a builder
     */
    public static GuacamoleColumnBuilder<?, ?> creator(String name, String parentQualifiedName, int order) {
        String[] tokens = parentQualifiedName.split("/");
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(tokens);
        String parentName = StringUtils.getNameFromQualifiedName(parentQualifiedName);
        String schemaQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(parentQualifiedName);
        String schemaName = StringUtils.getNameFromQualifiedName(schemaQualifiedName);
        String databaseQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(schemaQualifiedName);
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        return GuacamoleColumn._internal()
                .name(name)
                .qualifiedName(generateQualifiedName(name, parentQualifiedName))
                .connectorType(connectorType)
                .tableName(parentName)
                .tableQualifiedName(parentQualifiedName)
                .table(Table.refByQualifiedName(parentQualifiedName))
                .guacamoleTable(GuacamoleTable.refByQualifiedName(parentQualifiedName))
                .schemaName(schemaName)
                .schemaQualifiedName(schemaQualifiedName)
                .databaseName(databaseName)
                .databaseQualifiedName(databaseQualifiedName)
                .connectionQualifiedName(connectionQualifiedName)
                .order(order);
    }

    /**
     * Generate a unique GuacamoleColumn name.
     *
     * @param name of the GuacamoleColumn
     * @param parentQualifiedName unique name of the container in which this GuacamoleColumn exists
     * @return a unique name for the GuacamoleColumn
     */
    public static String generateQualifiedName(String name, String parentQualifiedName) {
        return parentQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a GuacamoleColumn.
     *
     * @param qualifiedName of the GuacamoleColumn
     * @param name of the GuacamoleColumn
     * @return the minimal request necessary to update the GuacamoleColumn, as a builder
     */
    public static GuacamoleColumnBuilder<?, ?> updater(String qualifiedName, String name) {
        return GuacamoleColumn._internal().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a GuacamoleColumn, from a potentially
     * more-complete GuacamoleColumn object.
     *
     * @return the minimal object necessary to update the GuacamoleColumn, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for GuacamoleColumn are not found in the initial object
     */
    @Override
    public GuacamoleColumnBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, TYPE_NAME, String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
