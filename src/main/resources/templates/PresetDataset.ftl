<#macro all>
    /**
     * Builds the minimal object necessary to create a Preset dataset.
     *
     * @param name of the dataset
     * @param collectionQualifiedName unique name of the collection in which the dataset exists
     * @return the minimal object necessary to create the dataset, as a builder
     */
    public static PresetDatasetBuilder<?, ?> creator(String name, String collectionQualifiedName) {
        String[] tokens = collectionQualifiedName.split("/");
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(tokens);
        String workspaceQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(collectionQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(workspaceQualifiedName);
        return PresetDataset._internal()
                .name(name)
                .qualifiedName(collectionQualifiedName + "/" + name)
                .connectorType(connectorType)
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
        return PresetDataset._internal().qualifiedName(qualifiedName).name(name);
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
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "PresetDataset", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
