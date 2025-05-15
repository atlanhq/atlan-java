<#macro all>
    /**
     * Builds the minimal object necessary to create a SalesforceReport asset.
     *
     * @param name of the report
     * @param organizationQualifiedName unique name of the organization through which the asset is accessible
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static SalesforceReportBuilder<?, ?> creator(String name, String organizationQualifiedName) {
        return SalesforceReport.creator(
                name, organizationQualifiedName, UUID.randomUUID().toString());
    }

    /**
     * Builds the minimal object necessary to create a SalesforceReport asset.
     *
     * @param name of the report
     * @param organization Salesforce organization through which the asset is accessible, which must have its qualifiedName populated
     * @return the minimal object necessary to create the asset, as a builder
     * @throws InvalidRequestException if the provided organization does not have a qualifiedName
     */
    public static SalesforceReportBuilder<?, ?> creator(String name, SalesforceOrganization organization)
            throws InvalidRequestException {
        return creator(name, organization, UUID.randomUUID().toString()).organization(organization.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a SalesforceReport asset.
     *
     * @param name of the report
     * @param organization Salesforce organization through which the asset is accessible, which must have its qualifiedName populated
     * @param salesforceId unique identifier of this report in Salesforce
     * @return the minimal object necessary to create the asset, as a builder
     * @throws InvalidRequestException if the provided organization does not have a qualifiedName
     */
    public static SalesforceReportBuilder<?, ?> creator(
            String name, SalesforceOrganization organization, String salesforceId) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", organization.getQualifiedName());
        map.put("connectionQualifiedName", organization.getConnectionQualifiedName());
        validateRelationship(SalesforceOrganization.TYPE_NAME, map);
        return creator(
            name,
            organization.getConnectionQualifiedName(),
            organization.getQualifiedName(),
            salesforceId
        ).organization(organization.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a SalesforceReport asset.
     *
     * @param name of the report
     * @param organizationQualifiedName unique name of the organization through which the asset is accessible
     * @param salesforceId unique identifier of this report in Salesforce
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static SalesforceReportBuilder<?, ?> creator(
            String name, String organizationQualifiedName, String salesforceId) {
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(organizationQualifiedName);
        return creator(name, connectionQualifiedName, organizationQualifiedName, salesforceId);
    }

    /**
     * Builds the minimal object necessary to create a SalesforceReport asset.
     *
     * @param name of the report
     * @param connectionQualifiedName unique name of the connection in which to create the SalesforceReport
     * @param organizationQualifiedName unique name of the organization in which to create the SalesforceReport
     * @param salesforceId unique identifier of this report in Salesforce
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static SalesforceReportBuilder<?, ?> creator(
        String name, String connectionQualifiedName, String organizationQualifiedName, String salesforceId) {
        return SalesforceReport._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .sourceId(salesforceId)
            .qualifiedName(generateQualifiedName(salesforceId, organizationQualifiedName))
            .name(name)
            .connectionQualifiedName(connectionQualifiedName)
            .organization(SalesforceOrganization.refByQualifiedName(organizationQualifiedName))
            .organizationQualifiedName(organizationQualifiedName);
    }

    /**
     * Generate a unique SalesforceReport name.
     *
     * @param salesforceId unique identifier of this report in Salesforce
     * @param organizationQualifiedName unique name of the organization through which the SalesforceReport is accessible
     * @return a unique name for the SalesforceReport
     */
    public static String generateQualifiedName(String salesforceId, String organizationQualifiedName) {
        return organizationQualifiedName + "/" + salesforceId;
    }

    /**
     * Builds the minimal object necessary to update a SalesforceReport.
     *
     * @param qualifiedName of the SalesforceReport
     * @param name of the SalesforceReport
     * @return the minimal request necessary to update the SalesforceReport, as a builder
     */
    public static SalesforceReportBuilder<?, ?> updater(String qualifiedName, String name) {
        return SalesforceReport._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SalesforceReport, from a potentially
     * more-complete SalesforceReport object.
     *
     * @return the minimal object necessary to update the SalesforceReport, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SalesforceReport are not found in the initial object
     */
    @Override
    public SalesforceReportBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
