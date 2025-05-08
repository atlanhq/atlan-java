<#macro all>
    /**
     * Builds the minimal object necessary to create a SalesforceDashboard asset.
     *
     * @param name of the dashboard
     * @param organizationQualifiedName unique name of the organization through which the asset is accessible
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static SalesforceDashboardBuilder<?, ?> creator(String name, String organizationQualifiedName) {
        return SalesforceDashboard.creator(
                name, organizationQualifiedName, UUID.randomUUID().toString());
    }

    /**
     * Builds the minimal object necessary to create a SalesforceDashboard asset.
     *
     * @param name of the dashboard
     * @param organization Salesforce organization through which the asset is accessible, which must have its qualifiedName populated
     * @return the minimal object necessary to create the asset, as a builder
     * @throws InvalidRequestException if the provided organization does not have a qualifiedName
     */
    public static SalesforceDashboardBuilder<?, ?> creator(String name, SalesforceOrganization organization)
            throws InvalidRequestException {
        return creator(name, organization, UUID.randomUUID().toString()).organization(organization.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a SalesforceDashboard asset.
     *
     * @param name of the dashboard
     * @param organization Salesforce organization through which the asset is accessible, which must have its qualifiedName populated
     * @param salesforceId unique identifier of this report in Salesforce
     * @return the minimal object necessary to create the asset, as a builder
     * @throws InvalidRequestException if the provided organization does not have a qualifiedName
     */
    public static SalesforceDashboardBuilder<?, ?> creator(
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
     * Builds the minimal object necessary to create a SalesforceDashboard asset.
     *
     * @param name of the dashboard
     * @param organizationQualifiedName unique name of the organization through which the asset is accessible
     * @param salesforceId unique identifier of this report in Salesforce
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static SalesforceDashboardBuilder<?, ?> creator(
            String name, String organizationQualifiedName, String salesforceId) {
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(organizationQualifiedName);
        return creator(name, connectionQualifiedName, organizationQualifiedName, salesforceId);
    }

    /**
     * Builds the minimal object necessary to create a SalesforceDashboard asset.
     *
     * @param name of the dashboard
     * @param connectionQualifiedName unique name of the connection in which to create the SalesforceDashboard
     * @param organizationQualifiedName unique name of the organization through which the asset is accessible
     * @param salesforceId unique identifier of this report in Salesforce
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static SalesforceDashboardBuilder<?, ?> creator(
        String name, String connectionQualifiedName, String organizationQualifiedName, String salesforceId) {
        return SalesforceDashboard._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .sourceId(salesforceId)
            .qualifiedName(generateQualifiedName(salesforceId, organizationQualifiedName))
            .name(name)
            .connectionQualifiedName(connectionQualifiedName)
            .organization(SalesforceOrganization.refByQualifiedName(organizationQualifiedName))
            .organizationQualifiedName(organizationQualifiedName);
    }

    /**
     * Generate a unique SalesforceDashboard name.
     *
     * @param salesforceId unique identifier of this dashboard in Salesforce
     * @param organizationQualifiedName unique name of the organization through which the SalesforceDashboard is accessible
     * @return a unique name for the SalesforceDashboard
     */
    public static String generateQualifiedName(String salesforceId, String organizationQualifiedName) {
        return organizationQualifiedName + "/" + salesforceId;
    }

    /**
     * Builds the minimal object necessary to update a SalesforceDashboard.
     *
     * @param qualifiedName of the SalesforceDashboard
     * @param name of the SalesforceDashboard
     * @return the minimal request necessary to update the SalesforceDashboard, as a builder
     */
    public static SalesforceDashboardBuilder<?, ?> updater(String qualifiedName, String name) {
        return SalesforceDashboard._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SalesforceDashboard, from a potentially
     * more-complete SalesforceDashboard object.
     *
     * @return the minimal object necessary to update the SalesforceDashboard, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SalesforceDashboard are not found in the initial object
     */
    @Override
    public SalesforceDashboardBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
