<#macro all>
    /**
     * Builds the minimal object necessary to create a Dataverse entity.
     *
     * @param name of the Dataverse entity
     * @param connectionQualifiedName unique name of the connection through which the entity is accessible
     * @return the minimal object necessary to create the Dataverse entity, as a builder
     */
    public static DataverseEntity.DataverseEntityBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return DataverseEntity._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(connectionQualifiedName + "/" + name)
                .name(name)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a DataverseEntity.
     *
     * @param qualifiedName of the DataverseEntity
     * @param name of the DataverseEntity
     * @return the minimal request necessary to update the DataverseEntity, as a builder
     */
    public static DataverseEntityBuilder<?, ?> updater(String qualifiedName, String name) {
        return DataverseEntity._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DataverseEntity, from a potentially
     * more-complete DataverseEntity object.
     *
     * @return the minimal object necessary to update the DataverseEntity, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DataverseEntity are not found in the initial object
     */

    @Override
    public DataverseEntityBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
