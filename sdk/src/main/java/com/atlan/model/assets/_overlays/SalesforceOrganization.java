// IMPORT: import java.util.UUID;

/**
     * Builds the minimal object necessary to create a SalesforceOrganization asset.
     *
     * @param name of the organization
     * @param connectionQualifiedName unique name of the connection through which the asset is accessible
     * @return the minimal object necessary to create the asset, as a builder
     */
    public static SalesforceOrganizationBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return SalesforceOrganization.creator(
                name, connectionQualifiedName, UUID.randomUUID().toString());
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