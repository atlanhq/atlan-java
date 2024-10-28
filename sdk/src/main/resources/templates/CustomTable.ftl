<#macro all>
    /**
     * Builds the minimal object necessary to create a CustomTable.
     *
     * @param name of the CustomTable
     * @param dataset in which the CustomTable should be created, which must have at least
     *               a qualifiedName and name
     * @return the minimal request necessary to create the CustomTable, as a builder
     * @throws InvalidRequestException if the dataset provided is without any required attributes
     */
    public static CustomTableBuilder<?, ?> creator(String name, CustomDataset dataset)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", dataset.getQualifiedName());
        map.put("name", dataset.getName());
        validateRelationship(CustomDataset.TYPE_NAME, map);
        return creator(
            name,
            dataset.getQualifiedName(),
            dataset.getName()
        ).customDataset(dataset.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a CustomTable.
     *
     * @param name of the CustomTable (must be unique within the dataset)
     * @param datasetQualifiedName unique name of the CustomDataset in which the CustomTable exists
     * @param datasetName simple human-readable name of the CustomDataset in which the CustomTable exists
     * @return the minimal object necessary to create the CustomTable, as a builder
     */
    public static CustomTableBuilder<?, ?> creator(
            String name, String datasetQualifiedName, String datasetName) {
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(datasetQualifiedName);
        return CustomTable._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, datasetQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName))
                .customDatasetQualifiedName(datasetQualifiedName)
                .customDatasetName(datasetName)
                .customDataset(CustomDataset.refByQualifiedName(datasetQualifiedName));
    }

    /**
     * Generate a unique CustomTable name.
     *
     * @param name of the CustomTable
     * @param datasetQualifiedName unique name of the CustomDataset in which this CustomTable exists
     * @return a unique name for the CustomTable
     */
    public static String generateQualifiedName(String name, String datasetQualifiedName) {
        return datasetQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a CustomTable.
     *
     * @param qualifiedName of the CustomTable
     * @param name of the CustomTable
     * @return the minimal request necessary to update the CustomTable, as a builder
     */
    public static CustomTableBuilder<?, ?> updater(String qualifiedName, String name) {
        return CustomTable._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a CustomTable, from a potentially
     * more-complete CustomTable object.
     *
     * @return the minimal object necessary to update the CustomTable, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for CustomTable are not found in the initial object
     */
    @Override
    public CustomTableBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
