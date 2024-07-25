<#macro all>
    /**
     * Builds the minimal object necessary to create a Superset dashboard.
     *
     * @param name of the dashboard
     * @param connectionQualifiedName unique name of the connection through which the dashboard is accessible
     * @return the minimal object necessary to create the dashboard, as a builder
     */
    public static SupersetDashboardBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return SupersetDashboard._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(connectionQualifiedName, name))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.SUPERSET);
    }

    /**
     * Builds the minimal object necessary to update a SupersetDashboard.
     *
     * @param qualifiedName of the SupersetDashboard
     * @param name of the SupersetDashboard
     * @return the minimal request necessary to update the SupersetDashboard, as a builder
     */
    public static SupersetDashboardBuilder<?, ?> updater(String qualifiedName, String name) {
        return SupersetDashboard._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SupersetDashboard, from a potentially
     * more-complete SupersetDashboard object.
     *
     * @return the minimal object necessary to update the SupersetDashboard, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SupersetDashboard are not found in the initial object
     */
    @Override
    public SupersetDashboardBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(
                TYPE_NAME,
                Map.of(
                        "qualifiedName", this.getQualifiedName(),
                        "name", this.getName()));
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Generate a unique Preset workspace name.
     *
     * @param connectionQualifiedName unique name of the connection
     * @param name for the workspace
     * @return a unique name for the workspace
     */
    private static String generateQualifiedName(String connectionQualifiedName, String name) {
        return connectionQualifiedName + "/" + name;
    }
</#macro>
