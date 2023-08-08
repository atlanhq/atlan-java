<#macro all>
    /**
     * Builds the minimal object necessary to create a ADLSObject.
     *
     * @param name of the ADLSObject
     * @param container in which the ADLSObject should be created, which must have at least
     *                  a qualifiedName
     * @return the minimal request necessary to create the ADLSObject, as a builder
     * @throws InvalidRequestException if the container provided is without a qualifiedName
     */
    public static ADLSObjectBuilder<?, ?> creator(String name, ADLSContainer container) throws InvalidRequestException {
        if (container.getQualifiedName() == null || container.getQualifiedName().isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, "ADLSContainer", "qualifiedName");
        }
        return creator(name, container.getQualifiedName()).adlsContainer(container.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a ADLSObject.
     *
     * @param name of the ADLSObject
     * @param containerQualifiedName unique name of the container through which the ADLSObject is accessible
     * @return the minimal object necessary to create the ADLSObject, as a builder
     */
    public static ADLSObjectBuilder<?, ?> creator(String name, String containerQualifiedName) {
        String accountQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(containerQualifiedName);
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(containerQualifiedName);
        return ADLSObject._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, containerQualifiedName))
                .name(name)
                .adlsContainer(ADLSContainer.refByQualifiedName(containerQualifiedName))
                .adlsAccountQualifiedName(accountQualifiedName)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.ADLS);
    }

    /**
     * Generate a unique ADLSObject name.
     *
     * @param name of the ADLSObject
     * @param containerQualifiedName unique name of the container through which the ADLSObject is accessible
     * @return a unique name for the ADLSObject
     */
    public static String generateQualifiedName(String name, String containerQualifiedName) {
        return containerQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a ADLSObject.
     *
     * @param qualifiedName of the ADLSObject
     * @param name of the ADLSObject
     * @return the minimal request necessary to update the ADLSObject, as a builder
     */
    public static ADLSObjectBuilder<?, ?> updater(String qualifiedName, String name) {
        return ADLSObject._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ADLSObject, from a potentially
     * more-complete ADLSObject object.
     *
     * @return the minimal object necessary to update the ADLSObject, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ADLSObject are not found in the initial object
     */
    @Override
    public ADLSObjectBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "ADLSObject", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
