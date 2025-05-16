<#macro all>
    /**
     * Builds the minimal object necessary to create an Anaplan workspace.
     *
     * @param name of the Anaplan workspace
     * @param connectionQualifiedName unique name of the connection through which the workspace is accessible
     * @return the minimal object necessary to create the Anaplan workspace, as a builder
     */
    public static AnaplanWorkspace.AnaplanWorkspaceBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return AnaplanWorkspace._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(connectionQualifiedName + "/" + name)
                .name(name)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a AnaplanWorkspace.
     *
     * @param qualifiedName of the AnaplanWorkspace
     * @param name of the AnaplanWorkspace
     * @return the minimal request necessary to update the AnaplanWorkspace, as a builder
     */
    public static AnaplanWorkspaceBuilder<?, ?> updater(String qualifiedName, String name) {
        return AnaplanWorkspace._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AnaplanWorkspace, from a potentially
     * more-complete AnaplanWorkspace object.
     *
     * @return the minimal object necessary to update the AnaplanWorkspace, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AnaplanWorkspace are not found in the initial object
     */
    @Override
    public AnaplanWorkspaceBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
