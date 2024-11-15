<#macro all>
    /**
     * Builds the minimal object necessary to create an ApplicationContainer asset.
     *
     * @param name of the application
     * @param connectionQualifiedName unique name of the connection through which the application is accessible
     * @return the minimal object necessary to create the application, as a builder
     */
    public static ApplicationContainerBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return ApplicationContainer._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .qualifiedName(generateQualifiedName(name, connectionQualifiedName))
            .name(name)
            .connectionQualifiedName(connectionQualifiedName)
            .connectorType(AtlanConnectorType.APPLICATION);
    }

    /**
     * Builds the minimal object necessary to update an ApplicationContainer.
     *
     * @param qualifiedName of the application
     * @param name of the application
     * @return the minimal request necessary to update the application, as a builder
     */
    public static ApplicationContainerBuilder<?, ?> updater(String qualifiedName, String name) {
        return ApplicationContainer._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .qualifiedName(qualifiedName)
            .name(name);
    }

    /**
     * Generate a unique application name.
     *
     * @param name of the application
     * @param connectionQualifiedName unique name of the connection in which this application exists
     * @return a unique name for the module
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
    public ApplicationContainerBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
