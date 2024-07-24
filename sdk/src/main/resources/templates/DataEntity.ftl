<#macro all>
    /**
     * Builds the minimal object necessary to create a DataModeling DataEntity.
     *
     * @param name of the DataEntity
     * @param datamodelversion in which the DatraEntity should be created, which must have at least
     *                   a qualifiedName
     * @return the minimal request necessary to create the DataEntity, as a builder
     * @throws InvalidRequestException if the datamodelversion provided is without a qualifiedName
     */
    public static DataEntityBuilder<?, ?> creator(String name, DataModelVersion datamodelversion)
            throws InvalidRequestException {
        validateRelationship(
                DataModelVersion.TYPE_NAME,
                Map.of(
                        "connectionQualifiedName", datamodelversion.getConnectionQualifiedName(),
                        "qualifiedName", datamodelversion.getQualifiedName()));
        return creator(name, datamodelversion.getConnectionQualifiedName(), datamodelversion.getQualifiedName())
                .dataModelVersion(datamodelversion.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a DataModeling DataEntity.
     *
     * @param name of the DataEntity
     * @param dataModelVersionQualifiedName unique name of the datamodelversion in which the dataentity exists
     * @return the minimal object necessary to create the dataentity, as a builder
     */
    public static DataEntityBuilder<?, ?> creator(String name, String dataModelVersionQualifiedName) {
        String dataModelQualifiedName =
                StringUtils.getParentQualifiedNameFromQualifiedName(dataModelVersionQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(dataModelQualifiedName);
        return creator(name, connectionQualifiedName, dataModelVersionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a DataModeling DataEntity.
     *
     * @param name of the DataEntity
     * @param connectionQualifiedName unique name of the connection in which to create the DataEntity
     * @param dataModelVersionQualifiedName unique name of the DataModelVersion in which to create the DataEntity
     * @return the minimal object necessary to create the dataentity, as a builder
     */
    public static DataEntityBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String dataModelVersionQualifiedName) {
        //                AtlanConnectorType connectorType =
        //         Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return DataEntity._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(dataModelVersionQualifiedName + "/" + name)
                .connectorType(AtlanConnectorType.DATA_MODELING)
                .dataModelVersionQualifiedName(dataModelVersionQualifiedName)
                .dataModelVersion(DataModelVersion.refByQualifiedName(dataModelVersionQualifiedName))
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a DataEntity.
     *
     * @param qualifiedName of the DataEntity
     * @param name of the DataEntity
     * @return the minimal request necessary to update the DataEntity, as a builder
     */
    public static DataEntityBuilder<?, ?> updater(String qualifiedName, String name) {
        return DataEntity._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DataEntity, from a potentially
     * more-complete DataEntity object.
     *
     * @return the minimal object necessary to update the DataEntity, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DataEntity are not found in the initial object
     */
    @Override
    public DataEntityBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(
                TYPE_NAME,
                Map.of(
                        "qualifiedName", this.getQualifiedName(),
                        "name", this.getName()));
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
