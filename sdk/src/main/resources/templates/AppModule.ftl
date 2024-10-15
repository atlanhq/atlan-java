<#macro all>
    /**
     * Builds the minimal object necessary to create an application module.
     *
     * @param name of the application module
     * @param connectionQualifiedName unique name of the connection in which to create the AppModule
     * @return the minimal request necessary to create the module, as a builder
     */
    public static AppModuleBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return creator(name, connectionQualifiedName, null);
    }

    /**
     * Builds the minimal object necessary to create an application sub-module.
     *
     * @param name of the application sub-module
     * @param parent application module in which the sub-module should be created, which must have at least
     *               a qualifiedName and / or connectionQualifiedName
     * @return the minimal request necessary to create the module, as a builder
     * @throws InvalidRequestException if the module provided is without a qualifiedName
     */
    public static AppModuleBuilder<?, ?> creator(String name, AppModule parent) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", parent.getConnectionQualifiedName());
        map.put("qualifiedName", parent.getQualifiedName());
        validateRelationship(AppModule.TYPE_NAME, map);
        return creator(
            name,
            parent.getConnectionQualifiedName(),
            parent.getQualifiedName())
            .appParentModule(parent.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create an application module.
     *
     * @param name of the application module
     * @param connectionQualifiedName unique name of the connection in which to create the AppModule
     * @param parentQualifiedName unique name of the parent application module in which to create the AppModule (as a sub-module)
     * @return the minimal request necessary to create the module, as a builder
     */
    public static AppModuleBuilder<?, ?> creator(String name, String connectionQualifiedName, String parentQualifiedName) {
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        AppModuleBuilder<?, ?> builder = AppModule._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .name(name)
            .qualifiedName(generateQualifiedName(name, connectionQualifiedName))
            .connectorType(connectorType)
            .connectionQualifiedName(connectionQualifiedName);
        if (parentQualifiedName != null) {
            builder.appParentModule(AppModule.refByQualifiedName(parentQualifiedName));
        }
        return builder;
    }

    /**
     * Builds the minimal object necessary to update an application module.
     *
     * @param qualifiedName of the application module
     * @param name of the application module
     * @return the minimal request necessary to update the application module, as a builder
     */
    public static AppModuleBuilder<?, ?> updater(String qualifiedName, String name) {
        return AppModule._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique application module name.
     *
     * @param name of the application module
     * @param connectionQualifiedName unique name of the connection in which this module exists
     * @return a unique name for the module
     */
    public static String generateQualifiedName(String name, String connectionQualifiedName) {
        return connectionQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to apply an update to an application module, from a potentially
     * more-complete application module object.
     *
     * @return the minimal object necessary to update the application module, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for application module are not found in the initial object
     */
    @Override
    public AppModuleBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
