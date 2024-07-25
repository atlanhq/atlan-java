<#macro all>
    /**
     * Builds the minimal object necessary to create a DataEntityMappingProcess
     *
     * @param sourceQualifiedName unique name of the source DataEntity
     * @param targetQualifiedName unique name of the target DataEntity
     * @param connectionQualifiedName unique name of the connection in which to create the DataEntityMappingProcess
     * @return the minimal object necessary to create the DataEntityMappingProcess, as a builder
     */
    public static DataEntityMappingProcessBuilder<?, ?> creator(
            String sourceQualifiedName, String targetQualifiedName, String connectionQualifiedName) {
        List<ICatalog> inputs = Collections.singletonList(DataEntity.refByQualifiedName(sourceQualifiedName));
        List<ICatalog> outputs = Collections.singletonList(DataEntity.refByQualifiedName(targetQualifiedName));
        return DataEntityMappingProcess._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(targetQualifiedName)
                .name(generateProcessName(sourceQualifiedName, targetQualifiedName))
                .connectorType(AtlanConnectorType.DATA_MODELING)
                .connectionQualifiedName(connectionQualifiedName)
                .inputs(inputs)
                .outputs(outputs);
    }

    /**
     * Builds the minimal string name for DataEntityMappingProcess name using the source and target QualifiedName.
     *
     * @param sourceQualifiedName
     * @param targetQualifiedName
     * @return the name for DataEntityMappingProcess
     */
    public static String generateProcessName(String sourceQualifiedName, String targetQualifiedName) {
        return sourceQualifiedName.substring(sourceQualifiedName.lastIndexOf(('/') + 1)) + ">"
                + targetQualifiedName.substring(targetQualifiedName.lastIndexOf(('/') + 1));
    }

    /**
     * Builds the minimal object necessary to update a DataEntityMappingProcess.
     *
     * @param qualifiedName of the DataEntityMappingProcess
     * @param name of the DataEntityMappingProcess
     * @return the minimal request necessary to update the DataEntityMappingProcess, as a builder
     */
    public static DataEntityMappingProcessBuilder<?, ?> updater(String qualifiedName, String name) {
        return DataEntityMappingProcess._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DataEntityMappingProcess, from a potentially
     * more-complete DataEntityMappingProcess object.
     *
     * @return the minimal object necessary to update the DataEntityMappingProcess, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DataEntityMappingProcess are not found in the initial object
     */
    @Override
    public DataEntityMappingProcessBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(
                TYPE_NAME,
                Map.of(
                        "qualifiedName", this.getQualifiedName(),
                        "name", this.getName()));
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
