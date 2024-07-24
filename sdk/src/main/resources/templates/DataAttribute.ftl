<#macro all>
    /**
     * Builds the minimal object necessary to create a DataModeling DataAttribute.
     *
     * @param name of the DataAttribute
     * @param dataentity in which the DataAttribute should be created, which must have at least
     *                   a qualifiedName
     * @return the minimal request necessary to create the DataAttribute, as a builder
     * @throws InvalidRequestException if the dataentity provided is without a qualifiedName
     */
    public static DataAttributeBuilder<?, ?> creator(String name, DataEntity dataentity)
            throws InvalidRequestException {
        validateRelationship(
                DataEntity.TYPE_NAME,
                Map.of(
                        "connectionQualifiedName", dataentity.getConnectionQualifiedName(),
                        "qualifiedName", dataentity.getQualifiedName()));
        return creator(name, dataentity.getConnectionQualifiedName(), dataentity.getQualifiedName())
                .dataEntity(dataentity.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a DataModeling DataAttribute.
     *
     * @param name of the DataAttribute
     * @param dataEntityQualifiedName unique name of the dataentity in which the dataattribute exists
     * @return the minimal object necessary to create the dataattribute, as a builder
     */
    public static DataAttributeBuilder<?, ?> creator(String name, String dataEntityQualifiedName) {
        String dataModelVersionQualifiedName =
                StringUtils.getParentQualifiedNameFromQualifiedName(dataEntityQualifiedName);
        String dataModelQualifiedName =
                StringUtils.getParentQualifiedNameFromQualifiedName(dataModelVersionQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(dataModelQualifiedName);
        return creator(name, connectionQualifiedName, dataEntityQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a DataModeling DataAttribute.
     *
     * @param name of the DataAttribute
     * @param connectionQualifiedName unique name of the connection in which to create the DataAttribute
     * @param dataEntityQualifiedName unique name of the DataEntity in which to create the DataAttribute
     * @return the minimal object necessary to create the dataattribute, as a builder
     */
    public static DataAttributeBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String dataEntityQualifiedName) {
        //                AtlanConnectorType connectorType =
        //         Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return DataAttribute._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(dataEntityQualifiedName + "/" + name)
                .connectorType(AtlanConnectorType.DATA_MODELING)
                .dataEntityQualifiedName(dataEntityQualifiedName)
                .dataEntity(DataEntity.refByQualifiedName(dataEntityQualifiedName))
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a DataAttribute.
     *
     * @param qualifiedName of the DataAttribute
     * @param name of the DataAttribute
     * @return the minimal request necessary to update the DataAttribute, as a builder
     */
    public static DataAttributeBuilder<?, ?> updater(String qualifiedName, String name) {
        return DataAttribute._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DataAttribute, from a potentially
     * more-complete DataAttribute object.
     *
     * @return the minimal object necessary to update the DataAttribute, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DataAttribute are not found in the initial object
     */
    @Override
    public DataAttributeBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(
                TYPE_NAME,
                Map.of(
                        "qualifiedName", this.getQualifiedName(),
                        "name", this.getName()));
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
