<#macro all>
    /**
     * Builds the minimal object necessary to create a Preset chart.
     *
     * @param name of the chart
     * @param collectionQualifiedName unique name of the collection in which the chart exists
     * @return the minimal object necessary to create the chart, as a builder
     */
    public static PresetChartBuilder<?, ?> creator(String name, String collectionQualifiedName) {
        String[] tokens = collectionQualifiedName.split("/");
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(tokens);
        String workspaceQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(collectionQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(workspaceQualifiedName);
        return PresetChart._internal()
                .name(name)
                .qualifiedName(collectionQualifiedName + "/" + name)
                .connectorType(connectorType)
                .presetDashboardQualifiedName(collectionQualifiedName)
                .presetDashboard(PresetDashboard.refByQualifiedName(collectionQualifiedName))
                .presetWorkspaceQualifiedName(workspaceQualifiedName)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a PresetChart.
     *
     * @param qualifiedName of the PresetChart
     * @param name of the PresetChart
     * @return the minimal request necessary to update the PresetChart, as a builder
     */
    public static PresetChartBuilder<?, ?> updater(String qualifiedName, String name) {
        return PresetChart._internal().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PresetChart, from a potentially
     * more-complete PresetChart object.
     *
     * @return the minimal object necessary to update the PresetChart, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for PresetChart are not found in the initial object
     */
    @Override
    public PresetChartBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "PresetChart", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
