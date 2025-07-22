<#macro all>
    /**
     * Builds the minimal object necessary to create a TypeRegistryNamespace.
     *
     * @param name of the TypeRegistryNamespace
     * @param connectionQualifiedName temporary creation to fit existing bootstrap policies
     * @return the minimal request necessary to create the TypeRegistryNamespace, as a builder
     */
    public static TypeRegistryNamespaceBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return TypeRegistryNamespace._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(connectionQualifiedName, name))
                .name(name)
                .typeRegistryPrefix(name);
    }

    /**
     * Generate a unique name for this TypeRegistryNamespace.
     *
     * @param connectionQualifiedName temporary creation to fit existing bootstrap policies
     * @param name human-readable name for the TypeRegistryNamespace
     * @return the unique qualifiedName of the TypeRegistryNamespace
     */
    public static String generateQualifiedName(String connectionQualifiedName, String name) {
        return connectionQualifiedName + "/TypeRegistry/" + name;
    }

    /**
     * Builds the minimal object necessary to update a TypeRegistryNamespace.
     *
     * @param qualifiedName of the TypeRegistryNamespace
     * @param name of the TypeRegistryNamespace
     * @return the minimal request necessary to update the TypeRegistryNamespace, as a builder
     */
    public static TypeRegistryNamespaceBuilder<?, ?> updater(String qualifiedName, String name) {
        return TypeRegistryNamespace._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TypeRegistryNamespace, from a potentially
     * more-complete TypeRegistryNamespace object.
     *
     * @return the minimal object necessary to update the TypeRegistryNamespace, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TypeRegistryNamespace are not found in the initial object
     */
    @Override
    public TypeRegistryNamespaceBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
