<#macro all>
    /**
     * Builds the minimal object necessary to create an API path.
     *
     * @param name of the API path
     * @param apiSpec in which the API path should be created, which must have at least
     *                a qualifiedName
     * @return the minimal request necessary to create the API path, as a builder
     * @throws InvalidRequestException if the apiSpec provided is without a qualifiedName
     */
    public static APIPathBuilder<?, ?> creator(String name, APISpec apiSpec) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", apiSpec.getQualifiedName());
        validateRelationship(APISpec.TYPE_NAME, map);
        return creator(name, apiSpec.getQualifiedName()).apiSpec(apiSpec.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create an API path.
     *
     * @param pathURI unique URI of the API path
     * @param apiSpecQualifiedName unique name of the API spec through which the path is accessible
     * @return the minimal object necessary to create the API path, as a builder
     */
    public static APIPathBuilder<?, ?> creator(String pathURI, String apiSpecQualifiedName) {
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(apiSpecQualifiedName);
        String normalizedURI = pathURI.startsWith("/") ? pathURI : "/" + pathURI;
        return APIPath._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(apiSpecQualifiedName + normalizedURI)
                .name(normalizedURI)
                .apiPathRawURI(normalizedURI)
                .apiSpec(APISpec.refByQualifiedName(apiSpecQualifiedName))
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a APIPath.
     *
     * @param qualifiedName of the APIPath
     * @param name of the APIPath
     * @return the minimal request necessary to update the APIPath, as a builder
     */
    public static APIPathBuilder<?, ?> updater(String qualifiedName, String name) {
        return APIPath._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a APIPath, from a potentially
     * more-complete APIPath object.
     *
     * @return the minimal object necessary to update the APIPath, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for APIPath are not found in the initial object
     */
    @Override
    public APIPathBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
