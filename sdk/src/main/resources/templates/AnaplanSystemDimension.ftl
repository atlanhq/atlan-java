<#macro all>
    /**
     * Builds the minimal object necessary to create an Anaplan System Dimension.
     *
     * @param name of the Anaplan System Dimension
     * @param connectionQualifiedName unique name of the connection through which the system dimension is accessible
     * @return the minimal object necessary to create the Anaplan system dimension, as a builder
     */
    public static AnaplanSystemDimension.AnaplanSystemDimensionBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return AnaplanSystemDimension._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .qualifiedName(connectionQualifiedName + "/" + name)
            .name(name)
            .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a AnaplanSystemDimension.
     *
     * @param qualifiedName of the AnaplanSystemDimension
     * @param name of the AnaplanSystemDimension
     * @return the minimal request necessary to update the AnaplanSystemDimension, as a builder
     */
    public static AnaplanSystemDimensionBuilder<?, ?> updater(String qualifiedName, String name) {
        return AnaplanSystemDimension._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AnaplanSystemDimension, from a potentially
     * more-complete AnaplanSystemDimension object.
     *
     * @return the minimal object necessary to update the AnaplanSystemDimension, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AnaplanSystemDimension are not found in the initial object
     */
    @Override
    public AnaplanSystemDimensionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
