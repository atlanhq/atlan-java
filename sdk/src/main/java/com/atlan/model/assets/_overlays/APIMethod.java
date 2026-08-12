    /**
     * Builds the minimal object necessary to create an API method.
     *
     * @param httpMethod the HTTP method (e.g. GET, POST, PUT, DELETE) for this API method
     * @param apiPath in which the API method should be created, which must have at least
     *                a qualifiedName
     * @return the minimal request necessary to create the API method, as a builder
     * @throws InvalidRequestException if the apiPath provided is without a qualifiedName
     */
    public static APIMethodBuilder<?, ?> creator(String httpMethod, APIPath apiPath) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", apiPath.getQualifiedName());
        validateRelationship(APIPath.TYPE_NAME, map);
        return creator(httpMethod, apiPath.getQualifiedName()).apiPath(apiPath.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create an API method.
     *
     * @param httpMethod the HTTP method (e.g. GET, POST, PUT, DELETE) for this API method
     * @param apiPathQualifiedName unique name of the API path on which this method operates
     * @return the minimal object necessary to create the API method, as a builder
     */
    public static APIMethodBuilder<?, ?> creator(String httpMethod, String apiPathQualifiedName) {
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(
                StringUtils.getParentQualifiedNameFromQualifiedName(apiPathQualifiedName));
        String normalizedMethod = httpMethod.toUpperCase();
        return APIMethod._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(apiPathQualifiedName + "/" + normalizedMethod)
                .name(normalizedMethod)
                .apiPath(APIPath.refByQualifiedName(apiPathQualifiedName))
                .connectionQualifiedName(connectionQualifiedName);
    }
