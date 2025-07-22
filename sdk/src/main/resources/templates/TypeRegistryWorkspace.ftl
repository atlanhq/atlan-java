<#macro all>
    /**
     * Builds the minimal object necessary to create a TypeRegistryWorkspace.
     *
     * @param name of the TypeRegistryWorkspace
     * @param namespace in which the TypeRegistryWorkspace should be registered
     * @return the minimal request necessary to create the TypeRegistryWorkspace, as a builder
     * @throws InvalidRequestException if the namespace provided is without a qualifiedName
     */
    public static TypeRegistryWorkspaceBuilder<?, ?> creator(String name, TypeRegistryNamespace namespace) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", namespace.getQualifiedName());
        validateRelationship(TypeRegistryNamespace.TYPE_NAME, map);
        return creator(
            name,
            namespace.getQualifiedName())
            .typeRegistryNamespace(namespace.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a TypeRegistryWorkspace.
     *
     * @param name of the TypeRegistryWorkspace
     * @param namespaceQualifiedName unique name of the namespace in which the TypeRegistryWorkspace should be registered
     * @return the minimal request necessary to create the TypeRegistryWorkspace, as a builder
     */
    public static TypeRegistryWorkspaceBuilder<?, ?> creator(String name, String namespaceQualifiedName) {
        return TypeRegistryWorkspace._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .qualifiedName(generateQualifiedName(namespaceQualifiedName, name))
            .name(name)
            .typeRegistryNamespace(TypeRegistryNamespace.refByQualifiedName(namespaceQualifiedName));
    }

    /**
     * Generate a unique name for this TypeRegistryWorkspace.
     *
     * @param namespaceQualifiedName unique name of the namespace in which the TypeRegistryWorkspace should be registered
     * @param name human-readable name for the TypeRegistryWorkspace
     * @return the unique qualifiedName of the TypeRegistryWorkspace
     */
    public static String generateQualifiedName(String namespaceQualifiedName, String name) {
        return namespaceQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a TypeRegistryWorkspace.
     *
     * @param qualifiedName of the TypeRegistryWorkspace
     * @param name of the TypeRegistryWorkspace
     * @return the minimal request necessary to update the TypeRegistryWorkspace, as a builder
     */
    public static TypeRegistryWorkspaceBuilder<?, ?> updater(String qualifiedName, String name) {
        return TypeRegistryWorkspace._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TypeRegistryWorkspace, from a potentially
     * more-complete TypeRegistryWorkspace object.
     *
     * @return the minimal object necessary to update the TypeRegistryWorkspace, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TypeRegistryWorkspace are not found in the initial object
     */
    @Override
    public TypeRegistryWorkspaceBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
