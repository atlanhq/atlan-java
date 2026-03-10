// IMPORT: import com.atlan.model.enums.KafkaTopicCleanupPolicy;
// IMPORT: import com.atlan.model.enums.KafkaTopicCompressionType;

/**
     * Builds the minimal object necessary to create a KafkaTopic.
     *
     * @param name of the KafkaTopic
     * @param connectionQualifiedName unique name of the connection through which the KafkaTopic is accessible
     * @return the minimal object necessary to create the KafkaTopic, as a builder
     */
    public static KafkaTopicBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return KafkaTopic._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Generate a unique KafkaTopic name.
     *
     * @param name of the KafkaTopic
     * @param connectionQualifiedName unique name of the connection through which the KafkaTopic is accessible
     * @return a unique name for the KafkaTopic
     */
    public static String generateQualifiedName(String name, String connectionQualifiedName) {
        return connectionQualifiedName + "/topic/" + name;
    }