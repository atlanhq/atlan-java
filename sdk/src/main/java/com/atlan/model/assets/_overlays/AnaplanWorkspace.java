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