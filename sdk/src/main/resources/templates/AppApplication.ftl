<#macro all>
    /**
     * Builds the minimal object necessary to create an application.
     *
     * @param name of the application
     * @param connectionQualifiedName unique name of the connection in which this application exists
     * @return the minimal request necessary to create the application, as a builder
     */
    public static AppApplicationBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return AppApplication._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .name(name)
            .qualifiedName(generateQualifiedName(name, connectionQualifiedName))
            .connectorType(connectorType)
            .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update an application.
     *
     * @param qualifiedName of the application
     * @param name of the application
     * @return the minimal request necessary to update the application, as a builder
     */
    public static AppApplicationBuilder<?, ?> updater(String qualifiedName, String name) {
        return AppApplication._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique application name.
     *
     * @param name of the application
     * @param connectionQualifiedName unique name of the connection in which this application exists
     * @return a unique name for the application
     */
    public static String generateQualifiedName(String name, String connectionQualifiedName) {
        return connectionQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to apply an update to an application, from a potentially
     * more-complete application object.
     *
     * @return the minimal object necessary to update the application, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for application are not found in the initial object
     */
    @Override
    public AppApplicationBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(TYPE_NAME, Map.of(
            "qualifiedName", this.getQualifiedName(),
            "name", this.getName()
        ));
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
