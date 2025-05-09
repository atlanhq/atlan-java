<#macro all>
    /**
     * Builds the minimal object necessary to create a SalesforceObject asset.
     *
     * @param name of the object
     * @param organization Salesforce organization through which the asset is accessible, which must have its qualifiedName populated
     * @return the minimal object necessary to create the asset, as a builder
     * @throws InvalidRequestException if the provided organization does not have a qualifiedName
     */
    public static SalesforceObjectBuilder<?, ?> creator(String name, SalesforceOrganization organization)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", organization.getQualifiedName());
        map.put("connectionQualifiedName", organization.getConnectionQualifiedName());
        validateRelationship(SalesforceOrganization.TYPE_NAME, map);
        return creator(
            name,
            organization.getConnectionQualifiedName(),
            organization.getQualifiedName()
        ).organization(organization.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a SalesforceObject asset.
     *
     * @param name of the object
     * @param organizationQualifiedName unique name of the organization through which the asset is accessible
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static SalesforceObjectBuilder<?, ?> creator(String name, String organizationQualifiedName) {
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(organizationQualifiedName);
        return creator(name, connectionQualifiedName, organizationQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a SalesforceObject asset.
     *
     * @param name of the object
     * @param connectionQualifiedName unique name of the connection in which to create the SalesforceObject
     * @param organizationQualifiedName unique name of the SalesforceOrganization in which to create the SalesforceObject
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static SalesforceObjectBuilder<?, ?> creator(String name, String connectionQualifiedName, String organizationQualifiedName) {
        return SalesforceObject._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .qualifiedName(generateQualifiedName(name, organizationQualifiedName))
            .name(name)
            .connectionQualifiedName(connectionQualifiedName)
            .organization(SalesforceOrganization.refByQualifiedName(organizationQualifiedName))
            .organizationQualifiedName(organizationQualifiedName);
    }

    /**
     * Generate a unique SalesforceObject name.
     *
     * @param name unique name of the object within Salesforce
     * @param organizationQualifiedName unique name of the organization through which the SalesforceObject is accessible
     * @return a unique name for the SalesforceObject
     */
    public static String generateQualifiedName(String name, String organizationQualifiedName) {
        return organizationQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a SalesforceObject.
     *
     * @param qualifiedName of the SalesforceObject
     * @param name of the SalesforceObject
     * @return the minimal request necessary to update the SalesforceObject, as a builder
     */
    public static SalesforceObjectBuilder<?, ?> updater(String qualifiedName, String name) {
        return SalesforceObject._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SalesforceObject, from a potentially
     * more-complete SalesforceObject object.
     *
     * @return the minimal object necessary to update the SalesforceObject, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SalesforceObject are not found in the initial object
     */
    @Override
    public SalesforceObjectBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
