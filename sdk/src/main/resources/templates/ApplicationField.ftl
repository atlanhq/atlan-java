<#macro all>
    /**
     * Builds the minimal object necessary to create a ApplicationField.
     *
     * @param name of the field
     * @param application in which the field should be created, which must have at least
     *                 a qualifiedName
     * @return the minimal request necessary to create the field, as a builder
     * @throws InvalidRequestException if the application provided is without a qualifiedName
     */
    public static ApplicationField.ApplicationFieldBuilder<?, ?> creator(String name, Application application)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("applicationQualifiedName", application.getQualifiedName());
        map.put("connectionQualifiedName", application.getConnectionQualifiedName());
        validateRelationship(Application.TYPE_NAME, map);
        return creator(name, application.getConnectionQualifiedName(), application.getQualifiedName())
                .application(application.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a ApplicationField.
     *
     * @param name of the field
     * @param applicationQualifiedName unique name of the application in which this field exists
     * @return the minimal request necessary to create the field, as a builder
     */
    public static ApplicationField.ApplicationFieldBuilder<?, ?> creator(String name, String applicationQualifiedName) {
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(applicationQualifiedName);
        return creator(name, connectionQualifiedName, applicationQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ApplicationField.
     *
     * @param name of the field
     * @param connectionQualifiedName unique name of the connection in which to create the field
     * @param applicationQualifiedName unique name of the application in which to create the field
     * @return the minimal request necessary to create the field, as a builder
     */
    public static ApplicationField.ApplicationFieldBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String applicationQualifiedName) {
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return ApplicationField._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, applicationQualifiedName))
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(connectorType)
                .applicationParentQualifiedName(applicationQualifiedName)
                .applicationParent(Application.refByQualifiedName(applicationQualifiedName));
    }

    /**
     * Generate a unique field name.
     *
     * @param name of the field
     * @param applicationQualifiedName unique name of the application in which this field exists
     * @return a unique name for the field
     */
    public static String generateQualifiedName(String name, String applicationQualifiedName) {
        return applicationQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a ApplicationField.
     *
     * @param qualifiedName of the ApplicationField
     * @param name of the ApplicationField
     * @return the minimal request necessary to update the ApplicationField, as a builder
     */
    public static ApplicationFieldBuilder<?, ?> updater(String qualifiedName, String name) {
        return ApplicationField._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ApplicationField, from a potentially
     * more-complete ApplicationField object.
     *
     * @return the minimal object necessary to update the ApplicationField, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ApplicationField are not found in the initial object
     */
    @Override
    public ApplicationFieldBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
