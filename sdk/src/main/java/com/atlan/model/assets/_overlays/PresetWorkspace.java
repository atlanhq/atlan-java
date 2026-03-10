/**
     * Builds the minimal object necessary to create a Preset workspace.
     *
     * @param name of the workspace
     * @param connectionQualifiedName unique name of the connection through which the workspace is accessible
     * @return the minimal object necessary to create the workspace, as a builder
     */
    public static PresetWorkspaceBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return PresetWorkspace._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(connectionQualifiedName, name))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Generate a unique Preset workspace name.
     *
     * @param connectionQualifiedName unique name of the connection
     * @param name for the workspace
     * @return a unique name for the workspace
     */
    private static String generateQualifiedName(String connectionQualifiedName, String name) {
        return connectionQualifiedName + "/" + name;
    }