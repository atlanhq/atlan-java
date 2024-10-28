<#macro all>
    /**
     * Builds the minimal object necessary to create a CustomDataset.
     *
     * @param name of the CustomDataset
     * @param connectionQualifiedName unique name of the connection through which the CustomDataset is accessible
     * @return the minimal object necessary to create the CustomDataset, as a builder
     */
    public static CustomDatasetBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return CustomDataset._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName));
    }

    /**
     * Builds the minimal object necessary to update a CustomDataset.
     *
     * @param qualifiedName of the CustomDataset
     * @param name of the CustomDataset
     * @return the minimal request necessary to update the CustomDataset, as a builder
     */
    public static CustomDatasetBuilder<?, ?> updater(String qualifiedName, String name) {
        return CustomDataset._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique CustomDataset name.
     *
     * @param name of the CustomDataset
     * @param connectionQualifiedName unique name of the connection in which this CustomDataset exists
     * @return a unique name for the CustomDataset
     */
    public static String generateQualifiedName(String name, String connectionQualifiedName) {
        return connectionQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to apply an update to a CustomDataset, from a potentially
     * more-complete CustomDataset object.
     *
     * @return the minimal object necessary to update the CustomDataset, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for CustomDataset are not found in the initial object
     */
    @Override
    public CustomDatasetBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
