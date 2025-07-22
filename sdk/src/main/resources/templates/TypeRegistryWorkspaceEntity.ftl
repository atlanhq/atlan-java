<#macro all>
    /**
     * Builds the minimal object necessary to create a TypeRegistryWorkspaceEntity.
     *
     * @param name of the TypeRegistryWorkspaceEntity
     * @param workspace in which the TypeRegistryWorkspaceEntity should be registered
     * @return the minimal request necessary to create the TypeRegistryWorkspaceEntity, as a builder
     * @throws InvalidRequestException if the workspace provided is without a qualifiedName
     */
    public static TypeRegistryWorkspaceEntityBuilder<?, ?> creator(String name, TypeRegistryWorkspace workspace) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", workspace.getQualifiedName());
        validateRelationship(TypeRegistryWorkspace.TYPE_NAME, map);
        return creator(
            name,
            workspace.getQualifiedName())
            .typeRegistryWorkspace(workspace.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a TypeRegistryWorkspaceEntity.
     *
     * @param name of the TypeRegistryWorkspaceEntity
     * @param workspaceQualifiedName unique name of the workspace in which the TypeRegistryWorkspaceEntity should be registered
     * @return the minimal request necessary to create the TypeRegistryWorkspaceEntity, as a builder
     */
    public static TypeRegistryWorkspaceEntityBuilder<?, ?> creator(String name, String workspaceQualifiedName) {
        return TypeRegistryWorkspaceEntity._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .qualifiedName(generateQualifiedName(workspaceQualifiedName, name))
            .name(name)
            .typeRegistryWorkspace(TypeRegistryWorkspace.refByQualifiedName(workspaceQualifiedName))
            .typeRegistrySuperType("Catalog"); // TODO: make this configurable
    }

    /**
     * Generate a unique name for this TypeRegistryWorkspaceEntity.
     *
     * @param workspaceQualifiedName unique name of the workspace in which to the TypeRegistryWorkspaceEntity should be registered
     * @param name human-readable name for the TypeRegistryWorkspaceEntity
     * @return the unique qualifiedName of the TypeRegistryWorkspaceEntity
     */
    public static String generateQualifiedName(String workspaceQualifiedName, String name) {
        return workspaceQualifiedName + "/entity/" + name;
    }

    /**
     * Builds the minimal object necessary to update a TypeRegistryWorkspaceEntity.
     *
     * @param qualifiedName of the TypeRegistryWorkspaceEntity
     * @param name of the TypeRegistryWorkspaceEntity
     * @return the minimal request necessary to update the TypeRegistryWorkspaceEntity, as a builder
     */
    public static TypeRegistryWorkspaceEntityBuilder<?, ?> updater(String qualifiedName, String name) {
        return TypeRegistryWorkspaceEntity._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TypeRegistryWorkspaceEntity, from a potentially
     * more-complete TypeRegistryWorkspaceEntity object.
     *
     * @return the minimal object necessary to update the TypeRegistryWorkspaceEntity, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TypeRegistryWorkspaceEntity are not found in the initial object
     */
    @Override
    public TypeRegistryWorkspaceEntityBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
