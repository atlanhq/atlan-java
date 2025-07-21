<#macro all>
    /**
     * Builds the minimal object necessary to create a TypeRegistryEntity.
     *
     * @param client connectivity to the Atlan tenant on which the TypeRegistryEntity is intended to be created
     * @param name of the TypeRegistryEntity
     * @param namespace in which the TypeRegistryEntity should be registered
     * @return the minimal request necessary to create the TypeRegistryEntity, as a builder
     */
    public static TypeRegistryEntityBuilder<?, ?> creator(AtlanClient client, String name, String namespace) {
        return TypeRegistryEntity._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, namespace))
                .name(name)
                .typeRegistryNamespace(TypeRegistryNamespace.refByQualifiedName(TypeRegistryNamespace.generateQualifiedName(namespace)))
                // .typeRegistrySuperType("Catalog") // TODO: make this configurable
                .typeRegistryStatus(TypeRegistryStatus.DRAFT);
    }

    /**
     * Generate a unique name for this TypeRegistryEntity.
     *
     * @param name human-readable name for the TypeRegistryEntity
     * @param namespace in which to the TypeRegistryEntity should be registered
     * @return the unique qualifiedName of the TypeRegistryEntity
     */
    public static String generateQualifiedName(String name, String namespace) {
        return "TypeRegistry/" + namespace + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a TypeRegistryEntity.
     *
     * @param qualifiedName of the TypeRegistryEntity
     * @param name of the TypeRegistryEntity
     * @return the minimal request necessary to update the TypeRegistryEntity, as a builder
     */
    public static TypeRegistryEntityBuilder<?, ?> updater(String qualifiedName, String name) {
        return TypeRegistryEntity._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TypeRegistryEntity, from a potentially
     * more-complete TypeRegistryEntity object.
     *
     * @return the minimal object necessary to update the TypeRegistryEntity, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TypeRegistryEntity are not found in the initial object
     */
    @Override
    public TypeRegistryEntityBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
