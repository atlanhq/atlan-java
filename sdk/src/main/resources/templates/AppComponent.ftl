<#macro all>
    /**
     * Builds the minimal object necessary to create an application component.
     *
     * @param name of the application component
     * @param application in which the component should be created, which must have at least
     *               a qualifiedName
     * @return the minimal request necessary to create the component, as a builder
     * @throws InvalidRequestException if the application provided is without a qualifiedName
     */
    public static AppComponentBuilder<?, ?> creator(String name, AppApplication application) throws InvalidRequestException {
        validateRelationship(
            AppApplication.TYPE_NAME,
            Map.of(
                "connectionQualifiedName", application.getConnectionQualifiedName(),
                "name", application.getName(),
                "qualifiedName", application.getQualifiedName()));
        return creator(
            name,
            application.getConnectionQualifiedName(),
            application.getName(),
            application.getQualifiedName())
            .appApplication(application.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create an application component.
     *
     * @param name of the application component
     * @param applicationQualifiedName unique name of the application in which this component exists
     * @return the minimal request necessary to create the component, as a builder
     */
    public static AppComponentBuilder<?, ?> creator(String name, String applicationQualifiedName) {
        String applicationName = StringUtils.getNameFromQualifiedName(applicationQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(applicationQualifiedName);
        return creator(
            name, connectionQualifiedName, applicationName, applicationQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create an application component.
     *
     * @param name of the application component
     * @param connectionQualifiedName unique name of the connection in which to create the AppComponent
     * @param applicationName simple name of the AppApplication in which to create the AppComponent
     * @param applicationQualifiedName unique name of the AppApplication in which to create the AppComponent
     * @return the minimal request necessary to create the component, as a builder
     */
    public static AppComponentBuilder<?, ?> creator(
        String name,
        String connectionQualifiedName,
        String applicationName,
        String applicationQualifiedName) {
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return AppComponent._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .name(name)
            .qualifiedName(generateQualifiedName(name, applicationQualifiedName))
            .connectorType(connectorType)
            .appApplicationName(applicationName)
            .appApplicationQualifiedName(applicationQualifiedName)
            .appApplication(AppApplication.refByQualifiedName(applicationQualifiedName))
            .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update an application component.
     *
     * @param qualifiedName of the application component
     * @param name of the application component
     * @return the minimal request necessary to update the application component, as a builder
     */
    public static AppComponentBuilder<?, ?> updater(String qualifiedName, String name) {
        return AppApplication._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique application component name.
     *
     * @param name of the application component
     * @param applicationQualifiedName unique name of the application in which this component exists
     * @return a unique name for the component
     */
    public static String generateQualifiedName(String name, String applicationQualifiedName) {
        return applicationQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to apply an update to an application component, from a potentially
     * more-complete application component object.
     *
     * @return the minimal object necessary to update the application component, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for application component are not found in the initial object
     */
    @Override
    public AppComponentBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(TYPE_NAME, Map.of(
            "qualifiedName", this.getQualifiedName(),
            "name", this.getName()
        ));
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
