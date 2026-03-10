// IMPORT: import com.atlan.model.enums.OpenLineageRunState;

/**
     * Builds the minimal object necessary to create an AirflowTask.
     *
     * @param name of the AirflowTask
     * @param airflowDag in which the AirflowTask should be created, which must have at least
     *                   a qualifiedName
     * @return the minimal request necessary to create the AirflowTask, as a builder
     * @throws InvalidRequestException if the AirflowDag provided is without a qualifiedName
     */
    public static AirflowTaskBuilder<?, ?> creator(String name, AirflowDag airflowDag) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", airflowDag.getQualifiedName());
        validateRelationship(AirflowDag.TYPE_NAME, map);
        return creator(name, airflowDag.getQualifiedName()).airflowDag(airflowDag.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create an AirflowTask.
     *
     * @param name of the AirflowTask
     * @param airflowDagQualifiedName unique name of the DAG through which the task is accessible
     * @return the minimal object necessary to create the AirflowTask, as a builder
     */
    public static AirflowTaskBuilder<?, ?> creator(String name, String airflowDagQualifiedName) {
        String[] tokens = airflowDagQualifiedName.split("/");
        String airflowDagName = StringUtils.getNameFromQualifiedName(airflowDagQualifiedName);
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(airflowDagQualifiedName);
        return AirflowTask._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(airflowDagQualifiedName + "/" + name)
                .name(name)
                .airflowDagQualifiedName(airflowDagQualifiedName)
                .airflowDagName(airflowDagName)
                .airflowDag(AirflowDag.refByQualifiedName(airflowDagQualifiedName))
                .connectionQualifiedName(connectionQualifiedName);
    }