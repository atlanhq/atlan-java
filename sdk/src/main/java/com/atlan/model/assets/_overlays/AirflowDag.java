// IMPORT: import com.atlan.model.enums.OpenLineageRunState;

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