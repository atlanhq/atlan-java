<#macro all>
    /**
     * Builds the minimal object necessary to create an AzureEventHub.
     *
     * @param name of the AzureEventHub
     * @param connectionQualifiedName unique name of the connection through which the AzureEventHub is accessible
     * @return the minimal object necessary to create the AzureEventHub, as a builder
     */
    public static AzureEventHubBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return AzureEventHub._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Generate a unique AzureEventHub name.
     *
     * @param name of the AzureEventHub
     * @param connectionQualifiedName unique name of the connection through which the AzureEventHub is accessible
     * @return a unique name for the AzureEventHub
     */
    public static String generateQualifiedName(String name, String connectionQualifiedName) {
        return connectionQualifiedName + "/topic/" + name;
    }

    /**
     * Builds the minimal object necessary to update a AzureEventHub.
     *
     * @param qualifiedName of the AzureEventHub
     * @param name of the AzureEventHub
     * @return the minimal request necessary to update the AzureEventHub, as a builder
     */
    public static AzureEventHubBuilder<?, ?> updater(String qualifiedName, String name) {
        return AzureEventHub._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AzureEventHub, from a potentially
     * more-complete AzureEventHub object.
     *
     * @return the minimal object necessary to update the AzureEventHub, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AzureEventHub are not found in the initial object
     */
    @Override
    public AzureEventHubBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
