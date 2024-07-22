<#macro all>
    /**
     * Builds the minimal object necessary to create a Superset dataset.
     *
     * @param name of the dataset
     * @param dashboard in which the dataset should be created, which must have at least
     *                   a qualifiedName
     * @return the minimal request necessary to create the dataset, as a builder
     * @throws InvalidRequestException if the dashboard provided is without a qualifiedName
     */
    public static SupersetDatasetBuilder<?, ?> creator(String name, SupersetDashboard dashboard)
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
     * Builds the minimal object necessary to create a Superset dataset.
     *
     * @param name of the dataset
     * @param dashboardQualifiedName unique name of the dashboard in which the dataset exists
     * @return the minimal object necessary to create the dataset, as a builder
     */
    public static SupersetDatasetBuilder<?, ?> creator(String name, String dashboardQualifiedName) {
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(dashboardQualifiedName);
        return creator(name, connectionQualifiedName, dashboardQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Superset dataset.
     *
     * @param name of the dataset
     * @param connectionQualifiedName unique name of the connection in which to create the SupersetDataset
     * @param dashboardQualifiedName unique name of the SupersetDashboard in which to create the SupersetDataset
     * @return the minimal object necessary to create the dataset, as a builder
     */
    public static SupersetDatasetBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String dashboardQualifiedName) {
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return SupersetDataset._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(dashboardQualifiedName + "/" + name)
                .connectorType(connectorType)
                .supersetDashboardQualifiedName(dashboardQualifiedName)
                .supersetDashboard(SupersetDashboard.refByQualifiedName(dashboardQualifiedName))
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SupersetDataset.
     *
     * @param qualifiedName of the SupersetDataset
     * @param name of the SupersetDataset
     * @return the minimal request necessary to update the SupersetDataset, as a builder
     */
    public static SupersetDatasetBuilder<?, ?> updater(String qualifiedName, String name) {
        return SupersetDataset._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SupersetDataset, from a potentially
     * more-complete SupersetDataset object.
     *
     * @return the minimal object necessary to update the SupersetDataset, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SupersetDataset are not found in the initial object
     */
    @Override
    public SupersetDatasetBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(
                TYPE_NAME,
                Map.of(
                        "qualifiedName", this.getQualifiedName(),
                        "name", this.getName()));
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
