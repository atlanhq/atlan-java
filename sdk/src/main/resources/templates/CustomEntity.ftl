<#macro all>
    /**
     * Builds the minimal object necessary to create a CustomEntity.
     *
     * @param name of the CustomEntity
     * @param connectionQualifiedName unique name of the connection through which the entity is accessible
     * @return the minimal object necessary to create the CustomEntity, as a builder
     */
    public static CustomEntity.CustomEntityBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return CustomEntity._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(connectionQualifiedName + "/" + name)
                .name(name)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a CustomEntity.
     *
     * @param qualifiedName of the CustomEntity
     * @param name of the CustomEntity
     * @return the minimal request necessary to update the CustomEntity, as a builder
     */
    public static CustomEntityBuilder<?, ?> updater(String qualifiedName, String name) {
        return CustomEntity._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a CustomEntity, from a potentially
     * more-complete CustomEntity object.
     *
     * @return the minimal object necessary to update the CustomEntity, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for CustomEntity are not found in the initial object
     */
    @Override
    public CustomEntityBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
