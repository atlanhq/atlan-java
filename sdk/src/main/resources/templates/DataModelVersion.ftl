<#macro all>
    /**
     * Builds the minimal object necessary to create a DataModeling DataModelVersion.
     *
     * @param name of the DataModelVersion
     * @param datamodel in which the DataModelVersion should be created, which must have at least
     *                   a qualifiedName
     * @return the minimal request necessary to create the DataModelVersion, as a builder
     * @throws InvalidRequestException if the datamodel provided is without a qualifiedName
     */
    public static DataModelVersionBuilder<?, ?> creator(String name, DataModel datamodel)
            throws InvalidRequestException {
        validateRelationship(
                DataModel.TYPE_NAME,
                Map.of(
                        "connectionQualifiedName", datamodel.getConnectionQualifiedName(),
                        "qualifiedName", datamodel.getQualifiedName()));
        return creator(name, datamodel.getConnectionQualifiedName(), datamodel.getQualifiedName())
                .dataModel(datamodel.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a DataModeling DataModelVersion.
     *
     * @param name of the DataModelVersion
     * @param dataModelQualifiedName unique name of the datamodel in which the datamodelversion exists
     * @return the minimal object necessary to create the datamodelversion, as a builder
     */
    public static DataModelVersionBuilder<?, ?> creator(String name, String dataModelQualifiedName) {
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(dataModelQualifiedName);
        return creator(name, connectionQualifiedName, dataModelQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a DataModeling DataModelVersion.
     *
     * @param name of the DataModelVersion
     * @param connectionQualifiedName unique name of the connection in which to create the DataModelVersion
     * @param dataModelQualifiedName unique name of the DataModel in which to create the DataModelVersion
     * @return the minimal object necessary to create the datamodelversion, as a builder
     */
    public static DataModelVersionBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String dataModelQualifiedName) {
        //                AtlanConnectorType connectorType =
        //         Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return DataModelVersion._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(dataModelQualifiedName + "/" + name)
                .connectorType(AtlanConnectorType.DATA_MODELING)
                .dataModelQualifiedName(dataModelQualifiedName)
                .dataModel(DataModel.refByQualifiedName(dataModelQualifiedName))
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a DataModelVersion.
     *
     * @param qualifiedName of the DataModelVersion
     * @param name of the DataModelVersion
     * @return the minimal request necessary to update the DataModelVersion, as a builder
     */
    public static DataModelVersionBuilder<?, ?> updater(String qualifiedName, String name) {
        return DataModelVersion._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DataModelVersion, from a potentially
     * more-complete DataModelVersion object.
     *
     * @return the minimal object necessary to update the DataModelVersion, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DataModelVersion are not found in the initial object
     */
    @Override
    public DataModelVersionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(
                TYPE_NAME,
                Map.of(
                        "qualifiedName", this.getQualifiedName(),
                        "name", this.getName()));
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
