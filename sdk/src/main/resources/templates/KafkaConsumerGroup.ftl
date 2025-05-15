<#macro all>
    /**
     * Builds the minimal object necessary to create a KafkaConsumerGroup.
     *
     * @param name of the KafkaConsumerGroup
     * @param topics in which the KafkaConsumerGroup should be created, the first of which must have at least
     *               a qualifiedName
     * @return the minimal request necessary to create the KafkaConsumerGroup, as a builder
     * @throws InvalidRequestException if the first topic provided is without a qualifiedName
     */
    public static KafkaConsumerGroupBuilder<?, ?> creatorObj(String name, List<KafkaTopic> topics) throws InvalidRequestException {
        if (topics == null || topics.isEmpty()) {
            throw new InvalidRequestException(ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, KafkaTopic.TYPE_NAME, "null");
        }
        List<String> topicNames = new ArrayList<>();
        List<String> topicQualifiedNames = new ArrayList<>();
        List<KafkaTopic> minimalTopics = new ArrayList<>();
        for (KafkaTopic topic : topics) {
            Map<String, String> map = new HashMap<>();
            map.put("qualifiedName", topic.getQualifiedName());
            map.put("name", topic.getName());
            map.put("connectionQualifiedName", topic.getConnectionQualifiedName());
            validateRelationship(KafkaTopic.TYPE_NAME, map);
            topicNames.add(topic.getName());
            topicQualifiedNames.add(topic.getQualifiedName());
            minimalTopics.add(topic.trimToReference());
        }
        return creator(
            name,
            topics.get(0).getConnectionQualifiedName(),
            topicNames,
            topicQualifiedNames
        ).kafkaTopics(minimalTopics);
    }

    /**
     * Builds the minimal object necessary to create a KafkaConsumerGroup.
     *
     * @param name of the KafkaConsumerGroup
     * @param topicQualifiedNames unique names of the topics in which the KafkaConsumerGroup is contained
     * @throws InvalidRequestException if no topic qualifiedNames are provided
     * @return the minimal object necessary to create the KafkaConsumerGroup, as a builder
     */
    public static KafkaConsumerGroupBuilder<?, ?> creator(String name, List<String> topicQualifiedNames) throws InvalidRequestException {
        if (topicQualifiedNames == null || topicQualifiedNames.isEmpty()) {
            throw new InvalidRequestException(ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, KafkaTopic.TYPE_NAME, "null");
        }
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(topicQualifiedNames.get(0));
        List<String> topicNames = topicQualifiedNames.stream().map(t -> (StringUtils.getNameFromQualifiedName(t))).collect(Collectors.toList());
        return creator(name, connectionQualifiedName, topicNames, topicQualifiedNames);
    }

    /**
     * Builds the minimal object necessary to create a KafkaConsumerGroup.
     *
     * @param name of the KafkaConsumerGroup
     * @param connectionQualifiedName unique name of the connection in which the KafkaConsumerGroup should be created
     * @param topicNames simple names of the KafkaTopics in which the KafkaConsumerGroup should be created
     * @param topicQualifiedNames unique names of the KafkaTopics in which the KafkaConsumerGroup should be created
     * @return the minimal object necessary to create the KafkaConsumerGroup, as a builder
     */
    public static KafkaConsumerGroupBuilder<?, ?> creator(String name, String connectionQualifiedName, List<String> topicNames, List<String> topicQualifiedNames) {
        return KafkaConsumerGroup._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .qualifiedName(generateQualifiedName(name, connectionQualifiedName))
            .name(name)
            .connectionQualifiedName(connectionQualifiedName)
            .kafkaTopics(topicQualifiedNames.stream().map(t -> KafkaTopic.refByQualifiedName(t)).collect(Collectors.toList()))
            .kafkaTopicNames(topicNames)
            .kafkaTopicQualifiedNames(topicQualifiedNames);
    }

    /**
     * Generate a unique KafkaConsumerGroup name.
     *
     * @param name of the KafkaConsumerGroup
     * @param connectionQualifiedName unique name of the connection in which the KafkaConsumerGroup is contained
     * @return a unique name for the KafkaConsumerGroup
     */
    public static String generateQualifiedName(String name, String connectionQualifiedName) {
        return connectionQualifiedName + "/consumer-group/" + name;
    }

    /**
     * Builds the minimal object necessary to update an KafkaConsumerGroup.
     *
     * @param qualifiedName of the KafkaConsumerGroup
     * @param name of the KafkaConsumerGroup
     * @return the minimal request necessary to update the KafkaConsumerGroup, as a builder
     */
    public static KafkaConsumerGroupBuilder<?, ?> updater(String qualifiedName, String name) {
        return KafkaConsumerGroup._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .qualifiedName(qualifiedName)
            .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to an KafkaConsumerGroup, from a potentially
     * more-complete KafkaConsumerGroup object.
     *
     * @return the minimal object necessary to update the KafkaConsumerGroup, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for KafkaConsumerGroup are not found in the initial object
     */
    @Override
    public KafkaConsumerGroupBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
