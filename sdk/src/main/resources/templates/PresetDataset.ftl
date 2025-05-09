<#macro all>
    /**
     * Builds the minimal object necessary to create a Preset dataset.
     *
     * @param name of the dataset
     * @param collection in which the dataset should be created, which must have at least
     *                   a qualifiedName
     * @return the minimal request necessary to create the dataset, as a builder
     * @throws InvalidRequestException if the collection provided is without a qualifiedName
     */
    public static PresetDatasetBuilder<?, ?> creator(String name, PresetDashboard collection)
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
     * Builds the minimal object necessary to create a Preset dataset.
     *
     * @param name of the dataset
     * @param collectionQualifiedName unique name of the collection in which the dataset exists
     * @return the minimal object necessary to create the dataset, as a builder
     */
    public static PresetDatasetBuilder<?, ?> creator(String name, String collectionQualifiedName) {
        String workspaceQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(collectionQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(workspaceQualifiedName);
        return creator(name, connectionQualifiedName, workspaceQualifiedName, collectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Preset dataset.
     *
     * @param name of the dataset
     * @param connectionQualifiedName unique name of the connection in which to create the PresetDataset
     * @param workspaceQualifiedName unique name of the PresetWorkspace in which to create the PresetDataset
     * @param collectionQualifiedName unique name of the PresetDashboard in which to create the PresetDataset
     * @return the minimal object necessary to create the dataset, as a builder
     */
    public static PresetDatasetBuilder<?, ?> creator(String name, String connectionQualifiedName, String workspaceQualifiedName, String collectionQualifiedName) {
        return PresetDataset._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .name(name)
            .qualifiedName(collectionQualifiedName + "/" + name)
            .presetDashboardQualifiedName(collectionQualifiedName)
            .presetDashboard(PresetDashboard.refByQualifiedName(collectionQualifiedName))
            .presetWorkspaceQualifiedName(workspaceQualifiedName)
            .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a PresetDataset.
     *
     * @param qualifiedName of the PresetDataset
     * @param name of the PresetDataset
     * @return the minimal request necessary to update the PresetDataset, as a builder
     */
    public static PresetDatasetBuilder<?, ?> updater(String qualifiedName, String name) {
        return PresetDataset._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PresetDataset, from a potentially
     * more-complete PresetDataset object.
     *
     * @return the minimal object necessary to update the PresetDataset, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for PresetDataset are not found in the initial object
     */
    @Override
    public PresetDatasetBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
