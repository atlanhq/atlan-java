<#macro all>
    /**
     * Builds the minimal object necessary to create a SalesforceOrganization asset.
     *
     * @param name of the organization
     * @param connectionQualifiedName unique name of the connection through which the asset is accessible
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static SalesforceOrganizationBuilder<?, ?> creator(
            String name, String connectionQualifiedName) {
        return SalesforceOrganization.creator(name, connectionQualifiedName, UUID.randomUUID().toString());
    }

    /**
     * Builds the minimal object necessary to create a SalesforceOrganization asset.
     *
     * @param name of the organization
     * @param connectionQualifiedName unique name of the connection through which the asset is accessible
     * @param salesforceId unique identifier of this organization in Salesforce
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static SalesforceOrganizationBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String salesforceId) {
        return SalesforceOrganization._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .sourceId(salesforceId)
                .qualifiedName(generateQualifiedName(salesforceId, connectionQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Generate a unique SalesforceOrganization name.
     *
     * @param salesforceId unique identifier of the organization within Salesforce
     * @param connectionQualifiedName unique name of the connection through which the SalesforceOrganization is accessible
     * @return a unique name for the SalesforceOrganization
     */
    public static String generateQualifiedName(String salesforceId, String connectionQualifiedName) {
        return connectionQualifiedName + "/" + salesforceId;
    }

    /**
     * Builds the minimal object necessary to update a SalesforceOrganization.
     *
     * @param qualifiedName of the SalesforceOrganization
     * @param name of the SalesforceOrganization
     * @return the minimal request necessary to update the SalesforceOrganization, as a builder
     */
    public static SalesforceOrganizationBuilder<?, ?> updater(String qualifiedName, String name) {
        return SalesforceOrganization._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SalesforceOrganization, from a potentially
     * more-complete SalesforceOrganization object.
     *
     * @return the minimal object necessary to update the SalesforceOrganization, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SalesforceOrganization are not found in the initial object
     */
    @Override
    public SalesforceOrganizationBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
