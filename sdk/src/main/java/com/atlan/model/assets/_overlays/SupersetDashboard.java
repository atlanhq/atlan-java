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

    private static String generateQualifiedName(String connectionQualifiedName, String name) {
        return connectionQualifiedName + "/" + name;
    }