<#macro all>
    /**
     * Builds the minimal object necessary to create an AzureEventHubConsumerGroup.
     *
     * @param name of the AzureEventHubConsumerGroup
     * @param hubs in which the AzureEventHubConsumerGroup should be created, the first of which must have at least
     *             a qualifiedName
     * @return the minimal request necessary to create the AzureEventHubConsumerGroup, as a builder
     * @throws InvalidRequestException if the first hub provided is without a qualifiedName
     */
    public static AzureEventHubConsumerGroupBuilder<?, ?> creatorObj(String name, List<AzureEventHub> hubs) throws InvalidRequestException {
        if (hubs == null || hubs.isEmpty()) {
            throw new InvalidRequestException(ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, AzureEventHub.TYPE_NAME, "null");
        }
        List<String> hubNames = new ArrayList<>();
        List<String> hubQualifiedNames = new ArrayList<>();
        List<AzureEventHub> minimalHubs = new ArrayList<>();
        for (AzureEventHub hub : hubs) {
            Map<String, String> map = new HashMap<>();
            map.put("connectionQualifiedName", hub.getConnectionQualifiedName());
            map.put("qualifiedName", hub.getQualifiedName());
            map.put("name", hub.getName());
            validateRelationship(AzureEventHub.TYPE_NAME, map);
            hubNames.add(hub.getName());
            hubQualifiedNames.add(hub.getQualifiedName());
            minimalHubs.add(hub.trimToReference());
        }
        return creator(
            name,
            hubs.get(0).getConnectionQualifiedName(),
            hubNames,
            hubQualifiedNames
        ).kafkaTopics(minimalHubs);
    }

    /**
     * Builds the minimal object necessary to create an AzureEventHubConsumerGroup.
     *
     * @param name of the AzureEventHubConsumerGroup
     * @param hubQualifiedNames unique names of the hubs in which the AzureEventHubConsumerGroup is contained
     * @throws InvalidRequestException if no hub qualifiedNames are provided
     * @return the minimal object necessary to create the AzureEventHubConsumerGroup, as a builder
     */
    public static AzureEventHubConsumerGroupBuilder<?, ?> creator(String name, List<String> hubQualifiedNames) throws InvalidRequestException {
        if (hubQualifiedNames == null || hubQualifiedNames.isEmpty()) {
            throw new InvalidRequestException(ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, AzureEventHub.TYPE_NAME, "null");
        }
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(hubQualifiedNames.get(0));
        List<String> hubNames = hubQualifiedNames.stream().map(h -> (StringUtils.getNameFromQualifiedName(h))).collect(Collectors.toList());
        return creator(name, connectionQualifiedName, hubNames, hubQualifiedNames);
    }

    /**
     * Builds the minimal object necessary to create an AzureEventHubConsumerGroup.
     *
     * @param name of the AzureEventHubConsumerGroup
     * @param connectionQualifiedName unique name of the connection in which the AzureEventHubConsumerGroup should be created
     * @param hubNames simple names of the AzureEventHubs in which the AzureEventHubConsumerGroup should be created
     * @param hubQualifiedNames unique names of the AzureEventHubs in which the AzureEventHubConsumerGroup should be created
     * @return the minimal object necessary to create the AzureEventHubConsumerGroup, as a builder
     */
    public static AzureEventHubConsumerGroupBuilder<?, ?> creator(String name, String connectionQualifiedName, List<String> hubNames, List<String> hubQualifiedNames) {
        return AzureEventHubConsumerGroup._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .qualifiedName(generateQualifiedName(name, connectionQualifiedName, hubNames.get(0)))
            .name(name)
            .connectionQualifiedName(connectionQualifiedName)
            .kafkaTopics(hubQualifiedNames.stream().map(h -> AzureEventHub.refByQualifiedName(h)).collect(Collectors.toList()))
            .kafkaTopicNames(hubNames)
            .kafkaTopicQualifiedNames(hubQualifiedNames);
    }

    /**
     * Generate a unique AzureEventHubConsumerGroup name.
     *
     * @param name of the AzureEventHubConsumerGroup
     * @param hubName simple name of the first AzureEventHubs in which the AzureEventHubConsumerGroup is contained
     * @return a unique name for the AzureEventHubConsumerGroup
     */
    public static String generateQualifiedName(String name, String connectionQualifiedName, String hubName) {
        return connectionQualifiedName + "/consumer-group/" + hubName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update an AzureEventHubConsumerGroup.
     *
     * @param qualifiedName of the AzureEventHubConsumerGroup
     * @param name of the AzureEventHubConsumerGroup
     * @return the minimal request necessary to update the AzureEventHubConsumerGroup, as a builder
     */
    public static AzureEventHubConsumerGroupBuilder<?, ?> updater(String qualifiedName, String name) {
        return AzureEventHubConsumerGroup._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .qualifiedName(qualifiedName)
            .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to an AzureEventHubConsumerGroup, from a potentially
     * more-complete AzureEventHubConsumerGroup object.
     *
     * @return the minimal object necessary to update the AzureEventHubConsumerGroup, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AzureEventHubConsumerGroup are not found in the initial object
     */
    @Override
    public AzureEventHubConsumerGroupBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
