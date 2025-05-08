<#macro all>
    /**
     * Builds the minimal object necessary to create a Preset chart.
     *
     * @param name of the chart
     * @param collection in which the chart should be created, which must have at least
     *                   a qualifiedName
     * @return the minimal request necessary to create the chart, as a builder
     * @throws InvalidRequestException if the collection provided is without a qualifiedName
     */
    public static PresetChartBuilder<?, ?> creator(String name, PresetDashboard collection)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", collection.getConnectionQualifiedName());
        map.put("presetWorkspaceQualifiedName", collection.getPresetWorkspaceQualifiedName());
        map.put("qualifiedName", collection.getQualifiedName());
        validateRelationship(PresetDashboard.TYPE_NAME, map);
        return creator(
            name,
            collection.getConnectionQualifiedName(),
            collection.getPresetWorkspaceQualifiedName(),
            collection.getQualifiedName()
        ).presetDashboard(collection.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Preset chart.
     *
     * @param name of the chart
     * @param collectionQualifiedName unique name of the collection in which the chart exists
     * @return the minimal object necessary to create the chart, as a builder
     */
    public static PresetChartBuilder<?, ?> creator(String name, String collectionQualifiedName) {
        String workspaceQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(collectionQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(workspaceQualifiedName);
        return creator(name, connectionQualifiedName, workspaceQualifiedName, collectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Preset chart.
     *
     * @param name of the chart
     * @param connectionQualifiedName unique name of the connection in which to create the PresetChart
     * @param workspaceQualifiedName unique name of the PresetWorkspace in which to create the PresetChart
     * @param collectionQualifiedName unique name of the PresetDashboard in which to create the PresetChart
     * @return the minimal object necessary to create the chart, as a builder
     */
    public static PresetChartBuilder<?, ?> creator(String name, String connectionQualifiedName, String workspaceQualifiedName, String collectionQualifiedName) {
        return PresetChart._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .name(name)
            .qualifiedName(collectionQualifiedName + "/" + name)
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
        return PresetChart._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
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
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
