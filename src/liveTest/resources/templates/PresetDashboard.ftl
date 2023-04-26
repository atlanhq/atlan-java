<#macro all>
    /**
     * Builds the minimal object necessary to create a Preset collection.
     *
     * @param name of the collection
     * @param workspaceQualifiedName unique name of the workspace in which the collection exists
     * @return the minimal object necessary to create the collection, as a builder
     */
    public static PresetDashboardBuilder<?, ?> creator(String name, String workspaceQualifiedName) {
        String[] tokens = workspaceQualifiedName.split("/");
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(tokens);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(workspaceQualifiedName);
        return PresetDashboard.builder()
                .name(name)
                .qualifiedName(workspaceQualifiedName + "/" + name)
                .connectorType(connectorType)
                .presetWorkspaceQualifiedName(workspaceQualifiedName)
                .presetWorkspace(PresetWorkspace.refByQualifiedName(workspaceQualifiedName))
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a PresetDashboard.
     *
     * @param qualifiedName of the PresetDashboard
     * @param name of the PresetDashboard
     * @return the minimal request necessary to update the PresetDashboard, as a builder
     */
    public static PresetDashboardBuilder<?, ?> updater(String qualifiedName, String name) {
        return PresetDashboard.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PresetDashboard, from a potentially
     * more-complete PresetDashboard object.
     *
     * @return the minimal object necessary to update the PresetDashboard, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for PresetDashboard are not found in the initial object
     */
    @Override
    public PresetDashboardBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "PresetDashboard", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
