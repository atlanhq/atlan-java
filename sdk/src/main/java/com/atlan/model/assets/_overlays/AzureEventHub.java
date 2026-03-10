// IMPORT: import com.atlan.model.enums.KafkaTopicCleanupPolicy;
// IMPORT: import com.atlan.model.enums.KafkaTopicCompressionType;

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