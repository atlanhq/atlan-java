<#macro all>
    /**
     * Builds the minimal object necessary to create a Superset chart.
     *
     * @param name of the chart
     * @param dashboard in which the chart should be created, which must have at least
     *                   a qualifiedName
     * @return the minimal request necessary to create the chart, as a builder
     * @throws InvalidRequestException if the dashboard provided is without a qualifiedName
     */
    public static SupersetChartBuilder<?, ?> creator(String name, SupersetDashboard dashboard)
            throws InvalidRequestException {
        validateRelationship(
                SupersetDashboard.TYPE_NAME,
                Map.of(
                        "connectionQualifiedName", dashboard.getConnectionQualifiedName(),
                        "qualifiedName", dashboard.getQualifiedName()));
        return creator(
                        name,
                        dashboard.getConnectionQualifiedName(),
                        dashboard.getQualifiedName())
                .supersetDashboard(dashboard.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Superset chart.
     *
     * @param name of the chart
     * @param dashboardQualifiedName unique name of the dashboard in which the chart exists
     * @return the minimal object necessary to create the chart, as a builder
     */
    public static SupersetChartBuilder<?, ?> creator(String name, String dashboardQualifiedName) {
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(dashboardQualifiedName);
        return creator(name, connectionQualifiedName, dashboardQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Superset chart.
     *
     * @param name of the chart
     * @param connectionQualifiedName unique name of the connection in which to create the SupersetChart
     * @param dashboardQualifiedName unique name of the SupersetDashboard in which to create the SupersetChart
     * @return the minimal object necessary to create the chart, as a builder
     */
    public static SupersetChartBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String dashboardQualifiedName) {
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return SupersetChart._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(dashboardQualifiedName + "/" + name)
                .connectorType(connectorType)
                .supersetDashboardQualifiedName(dashboardQualifiedName)
                .supersetDashboard(SupersetDashboard.refByQualifiedName(dashboardQualifiedName))
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SupersetChart.
     *
     * @param qualifiedName of the SupersetChart
     * @param name of the SupersetChart
     * @return the minimal request necessary to update the SupersetChart, as a builder
     */
    public static SupersetChartBuilder<?, ?> updater(String qualifiedName, String name) {
        return SupersetChart._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SupersetChart, from a potentially
     * more-complete SupersetChart object.
     *
     * @return the minimal object necessary to update the SupersetChart, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SupersetChart are not found in the initial object
     */
    @Override
    public SupersetChartBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(
                TYPE_NAME,
                Map.of(
                        "qualifiedName", this.getQualifiedName(),
                        "name", this.getName()));
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
