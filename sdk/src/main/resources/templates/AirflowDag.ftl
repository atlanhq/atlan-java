<#macro all>
    /**
     * Builds the minimal object necessary to create an AirflowDag.
     *
     * @param name of the AirflowDag
     * @param connectionQualifiedName unique name of the connection through which the DAG is accessible
     * @return the minimal object necessary to create the AirflowDag, as a builder
     */
    public static AirflowDagBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return AirflowDag._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(connectionQualifiedName + "/" + name)
                .name(name)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a AirflowDag.
     *
     * @param qualifiedName of the AirflowDag
     * @param name of the AirflowDag
     * @return the minimal request necessary to update the AirflowDag, as a builder
     */
    public static AirflowDagBuilder<?, ?> updater(String qualifiedName, String name) {
        return AirflowDag._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AirflowDag, from a potentially
     * more-complete AirflowDag object.
     *
     * @return the minimal object necessary to update the AirflowDag, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AirflowDag are not found in the initial object
     */
    @Override
    public AirflowDagBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
