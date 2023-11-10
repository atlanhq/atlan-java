<#macro imports>
import com.atlan.model.assets.Connection;
import com.atlan.model.assets.Schema;
</#macro>
<#macro all>
    /**
     * Builds the minimal object necessary to create a Guacamole table.
     *
     * @param name of the Guacamole table
     * @param schemaQualifiedName unique name of the schema in which this Guacamole table exists
     * @return the minimal request necessary to create the Guacamole table, as a builder
     */
    public static GuacamoleTableBuilder<?, ?> creator(String name, String schemaQualifiedName) {
        String[] tokens = schemaQualifiedName.split("/");
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(tokens);
        String schemaName = StringUtils.getNameFromQualifiedName(schemaQualifiedName);
        String databaseQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(schemaQualifiedName);
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        return GuacamoleTable._internal()
                .name(name)
                .qualifiedName(generateQualifiedName(name, schemaQualifiedName))
                .connectorType(connectorType)
                .schemaName(schemaName)
                .schemaQualifiedName(schemaQualifiedName)
                .schema(Schema.refByQualifiedName(schemaQualifiedName))
                .databaseName(databaseName)
                .databaseQualifiedName(databaseQualifiedName)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a GuacamoleTable.
     *
     * @param qualifiedName of the GuacamoleTable
     * @param name of the GuacamoleTable
     * @return the minimal request necessary to update the GuacamoleTable, as a builder
     */
    public static GuacamoleTableBuilder<?, ?> updater(String qualifiedName, String name) {
        return GuacamoleTable._internal().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Generate a unique Guacamole table name.
     *
     * @param name of the Guacamole table
     * @param schemaQualifiedName unique name of the schema in which this Guacamole table exists
     * @return a unique name for the Guacamole table
     */
    public static String generateQualifiedName(String name, String schemaQualifiedName) {
        return schemaQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to apply an update to a GuacamoleTable, from a potentially
     * more-complete GuacamoleTable object.
     *
     * @return the minimal object necessary to update the GuacamoleTable, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for GuacamoleTable are not found in the initial object
     */
    @Override
    public GuacamoleTableBuilder<?, ?> trimToRequired() throws InvalidRequestException {
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
