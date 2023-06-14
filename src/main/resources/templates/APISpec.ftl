<#macro all>
    /**
     * Builds the minimal object necessary to create an API spec.
     *
     * @param name of the API spec
     * @param connectionQualifiedName unique name of the connection through which the spec is accessible
     * @return the minimal object necessary to create the API spec, as a builder
     */
    public static APISpecBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return APISpec.builder()
                .qualifiedName(connectionQualifiedName + "/" + name)
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.API);
    }

    /**
     * Builds the minimal object necessary to update a APISpec.
     *
     * @param qualifiedName of the APISpec
     * @param name of the APISpec
     * @return the minimal request necessary to update the APISpec, as a builder
     */
    public static APISpecBuilder<?, ?> updater(String qualifiedName, String name) {
        return APISpec.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a APISpec, from a potentially
     * more-complete APISpec object.
     *
     * @return the minimal object necessary to update the APISpec, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for APISpec are not found in the initial object
     */
    @Override
    public APISpecBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "APISpec", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
