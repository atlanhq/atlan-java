<#macro all>
    /**
     * Builds the minimal object necessary to create a DataAttributeMappingProcess
     *
     * @param sourceQualifiedName unique name of the source DataAttribute
     * @param targetQualifiedName unique name of the target DataAttribute
     * @param connectionQualifiedName unique name of the connection in which to create the DataAttributeMappingProcess
     * @return the minimal object necessary to create the DataAttributeMappingProcess, as a builder
     */
    public static DataAttributeMappingProcessBuilder<?, ?> creator(
            String sourceQualifiedName, String targetQualifiedName, String connectionQualifiedName) {
        List<ICatalog> inputs = Collections.singletonList(DataAttribute.refByQualifiedName(sourceQualifiedName));
        List<ICatalog> outputs = Collections.singletonList(DataAttribute.refByQualifiedName(targetQualifiedName));
        return DataAttributeMappingProcess._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(targetQualifiedName)
                .name(generateProcessName(sourceQualifiedName, targetQualifiedName))
                .connectorType(AtlanConnectorType.DATA_MODELING)
                .connectionQualifiedName(connectionQualifiedName)
                .inputs(inputs)
                .outputs(outputs);
    }

    /**
     * Builds the minimal string name for DataAttributeMappingProcess name using the source and target QualifiedName.
     *
     * @param sourceQualifiedName
     * @param targetQualifiedName
     * @return the name for DataAttributeMappingProcess
     */
    public static String generateProcessName(String sourceQualifiedName, String targetQualifiedName) {
        return sourceQualifiedName.substring(sourceQualifiedName.lastIndexOf(('/') + 1)) + ">"
                + targetQualifiedName.substring(targetQualifiedName.lastIndexOf(('/') + 1));
    }

    /**
     * Builds the minimal object necessary to update a DataAttributeMappingProcess.
     *
     * @param qualifiedName of the DataAttributeMappingProcess
     * @param name of the DataAttributeMappingProcess
     * @return the minimal request necessary to update the DataAttributeMappingProcess, as a builder
     */
    public static DataAttributeMappingProcessBuilder<?, ?> updater(String qualifiedName, String name) {
        return DataAttributeMappingProcess._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DataAttributeMappingProcess, from a potentially
     * more-complete DataAttributeMappingProcess object.
     *
     * @return the minimal object necessary to update the DataAttributeMappingProcess, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DataAttributeMappingProcess are not found in the initial object
     */
    @Override
    public DataAttributeMappingProcessBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(
                TYPE_NAME,
                Map.of(
                        "qualifiedName", this.getQualifiedName(),
                        "name", this.getName()));
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
