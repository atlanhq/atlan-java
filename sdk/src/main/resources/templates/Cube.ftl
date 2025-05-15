<#macro all>
    /**
     * Builds the minimal object necessary to create a Cube.
     *
     * @param name of the Cube
     * @param connectionQualifiedName unique name of the connection in which this Cube exists
     * @return the minimal request necessary to create the Cube, as a builder
     */
    public static CubeBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return Cube._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName))
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a Cube.
     *
     * @param qualifiedName of the Cube
     * @param name of the Cube
     * @return the minimal request necessary to update the Cube, as a builder
     */
    public static CubeBuilder<?, ?> updater(String qualifiedName, String name) {
        return Cube._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique Cube name.
     *
     * @param name of the Cube
     * @param connectionQualifiedName unique name of the connection in which this Cube exists
     * @return a unique name for the Cube
     */
    public static String generateQualifiedName(String name, String connectionQualifiedName) {
        return connectionQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to apply an update to a Cube, from a potentially
     * more-complete Cube object.
     *
     * @return the minimal object necessary to update the Cube, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for Cube are not found in the initial object
     */
    @Override
    public CubeBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
