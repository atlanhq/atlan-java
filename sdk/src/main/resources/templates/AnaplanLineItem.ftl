<#macro all>
    /**
     * Builds the minimal object necessary to create a Anaplan lineitem.
     *
     * @param name of the lineitem
     * @param module in which the lineitem should be created, which must have at least
     *                 a qualifiedName
     * @return the minimal request necessary to create the lineitem, as a builder
     * @throws InvalidRequestException if the module provided is without a qualifiedName
     */
    public static AnaplanLineItem.AnaplanLineItemBuilder<?, ?> creator(String name, AnaplanModule module)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", module.getConnectionQualifiedName());
        map.put("workspaceQualifiedName", module.getAnaplanWorkspaceQualifiedName());
        map.put("workspaceName", module.getAnaplanWorkspaceName());
        map.put("modelQualifiedName", module.getAnaplanModelQualifiedName());
        map.put("modelName", module.getAnaplanModelName());
        map.put("moduleQualifiedName", module.getQualifiedName());
        map.put("moduleName", module.getName());
        validateRelationship(AnaplanWorkspace.TYPE_NAME, map);
        return creator(
                        name,
                        module.getConnectionQualifiedName(),
                        module.getName(),
                        module.getQualifiedName(),
                        module.getAnaplanModelName(),
                        module.getAnaplanModelQualifiedName(),
                        module.getAnaplanWorkspaceName(),
                        module.getAnaplanWorkspaceQualifiedName())
                .anaplanModule(module.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Anaplan lineitem.
     *
     * @param name of the lineitem
     * @param moduleQualifiedName unique name of the module in which this lineitem exists
     * @return the minimal request necessary to create the lineitem, as a builder
     */
    public static AnaplanLineItem.AnaplanLineItemBuilder<?, ?> creator(String name, String moduleQualifiedName) {
        String moduleName = StringUtils.getNameFromQualifiedName(moduleQualifiedName);
        String modelQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(moduleQualifiedName);
        String modelName = StringUtils.getNameFromQualifiedName(modelQualifiedName);
        String workspaceQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(modelQualifiedName);
        String workspaceName = StringUtils.getNameFromQualifiedName(workspaceQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(workspaceQualifiedName);
        return creator(
                name,
                connectionQualifiedName,
                moduleName,
                moduleQualifiedName,
                modelName,
                modelQualifiedName,
                workspaceName,
                workspaceQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Anaplan lineitem.
     *
     * @param name of the lineitem
     * @param connectionQualifiedName unique name of the connection in which to create the lineitem
     * @param moduleName name of the module in which to creat the lineitem
     * @param moduleQualifiedName unique name of the module in which to create the lineitem
     * @param modelName name of the model in which to create the lineitem
     * @param modelQualifiedName unique name of the model in which to create the lineitem
     * @param workspaceName name of the workspace in which to create the lineitem
     * @param workspaceQualifiedName unique name of the workspace in which to create the lineitem
     * @return the minimal request necessary to create the lineitem, as a builder
     */
    public static AnaplanLineItem.AnaplanLineItemBuilder<?, ?> creator(
            String name,
            String connectionQualifiedName,
            String moduleName,
            String moduleQualifiedName,
            String modelName,
            String modelQualifiedName,
            String workspaceName,
            String workspaceQualifiedName) {
        return AnaplanLineItem._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, moduleQualifiedName))
                .connectionQualifiedName(connectionQualifiedName)
                .anaplanWorkspaceName(workspaceName)
                .anaplanWorkspaceQualifiedName(workspaceQualifiedName)
                .anaplanModelName(modelName)
                .anaplanModelQualifiedName(modelQualifiedName)
                .anaplanModuleName(moduleName)
                .anaplanModuleQualifiedName(moduleQualifiedName)
                .anaplanModule(AnaplanModule.refByQualifiedName(moduleQualifiedName));
    }

    /**
     * Generate a unique lineitem name.
     *
     * @param name of the lineitem
     * @param moduleQualifiedName unique name of the module in which this lineitem exists
     * @return a unique name for the lineitem
     */
    public static String generateQualifiedName(String name, String moduleQualifiedName) {
        return moduleQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a AnaplanLineItem.
     *
     * @param qualifiedName of the AnaplanLineItem
     * @param name of the AnaplanLineItem
     * @return the minimal request necessary to update the AnaplanLineItem, as a builder
     */
    public static AnaplanLineItemBuilder<?, ?> updater(String qualifiedName, String name) {
        return AnaplanLineItem._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AnaplanLineItem, from a potentially
     * more-complete AnaplanLineItem object.
     *
     * @return the minimal object necessary to update the AnaplanLineItem, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AnaplanLineItem are not found in the initial object
     */
    @Override
    public AnaplanLineItemBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
