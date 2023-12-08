<#macro all>
    /**
     * Builds the minimal object necessary to create a SalesforceField asset.
     *
     * @param name of the field
     * @param object Salesforce object through which the asset is accessible, which must have its qualifiedName populated
     * @return the minimal object necessary to create the asset, as a builder
     * @throws InvalidRequestException if the provided object does not have a qualifiedName
     */
    public static SalesforceFieldBuilder<?, ?> creator(
            String name, SalesforceObject object) throws InvalidRequestException {
        if (object.getQualifiedName() == null || object.getQualifiedName().isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, "SalesforceObject", "qualifiedName");
        }
        return creator(name, object.getQualifiedName()).object(object.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a SalesforceField asset.
     *
     * @param name of the field
     * @param objectQualifiedName unique name of the object through which the asset is accessible
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static SalesforceFieldBuilder<?, ?> creator(
            String name, String objectQualifiedName) {
        String organizationQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(objectQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(organizationQualifiedName);
        return SalesforceField._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, objectQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.SALESFORCE)
                .organizationQualifiedName(organizationQualifiedName)
                .object(SalesforceObject.refByQualifiedName(objectQualifiedName))
                .objectQualifiedName(objectQualifiedName);
    }

    /**
     * Generate a unique SalesforceField name.
     *
     * @param name unique name of the object within Salesforce
     * @param objectQualifiedName unique name of the object through which the asset is accessible
     * @return a unique name for the SalesforceField
     */
    public static String generateQualifiedName(String name, String objectQualifiedName) {
        return objectQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a SalesforceField.
     *
     * @param qualifiedName of the SalesforceField
     * @param name of the SalesforceField
     * @return the minimal request necessary to update the SalesforceField, as a builder
     */
    public static SalesforceFieldBuilder<?, ?> updater(String qualifiedName, String name) {
        return SalesforceField._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SalesforceField, from a potentially
     * more-complete SalesforceField object.
     *
     * @return the minimal object necessary to update the SalesforceField, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SalesforceField are not found in the initial object
     */
    @Override
    public SalesforceFieldBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, TYPE_NAME, String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
