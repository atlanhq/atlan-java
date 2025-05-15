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
                .connectionQualifiedName(connectionQualifiedName);
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
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
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

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
