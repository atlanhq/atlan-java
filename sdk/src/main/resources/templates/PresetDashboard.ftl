<#macro all>
    /**
     * Builds the minimal object necessary to create a Preset collection.
     *
     * @param name of the collection
     * @param workspace in which the collection should be created, which must have at least
     *                  a qualifiedName
     * @return the minimal request necessary to create the collection, as a builder
     * @throws InvalidRequestException if the workspace provided is without a qualifiedName
     */
    public static PresetDashboardBuilder<?, ?> creator(String name, PresetWorkspace workspace)
            throws InvalidRequestException {
        validateRelationship(PresetWorkspace.TYPE_NAME, Map.of(
            "connectionQualifiedName", workspace.getConnectionQualifiedName(),
            "qualifiedName", workspace.getQualifiedName()
        ));
        return creator(
            name,
            workspace.getConnectionQualifiedName(),
            workspace.getQualifiedName()
        ).presetWorkspace(workspace.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Preset collection.
     *
     * @param name of the collection
     * @param workspaceQualifiedName unique name of the workspace in which the collection exists
     * @return the minimal object necessary to create the collection, as a builder
     */
    public static PresetDashboardBuilder<?, ?> creator(String name, String workspaceQualifiedName) {
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(workspaceQualifiedName);
        return creator(name, connectionQualifiedName, workspaceQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Preset collection.
     *
     * @param name of the collection
     * @param connectionQualifiedName unique name of the connection in which to create the PresetDashboard
     * @param workspaceQualifiedName unique name of the PresetWorkspace in which to create the PresetDashboard
     * @return the minimal object necessary to create the collection, as a builder
     */
    public static PresetDashboardBuilder<?, ?> creator(String name, String connectionQualifiedName, String workspaceQualifiedName) {
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return PresetDashboard._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
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
        return PresetDashboard._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
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
        validateRequired(TYPE_NAME, Map.of(
            "qualifiedName", this.getQualifiedName(),
            "name", this.getName()
        ));
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
