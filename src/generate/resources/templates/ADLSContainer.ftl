<#macro all>
    /**
     * Builds the minimal object necessary to create a ADLSContainer.
     *
     * @param name of the ADLSContainer
     * @param accountQualifiedName unique name of the account through which the ADLSContainer is accessible
     * @return the minimal object necessary to create the ADLSContainer, as a builder
     */
    public static ADLSContainerBuilder<?, ?> creator(String name, String accountQualifiedName) {
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(accountQualifiedName);
        return ADLSContainer.builder()
                .qualifiedName(generateQualifiedName(name, accountQualifiedName))
                .name(name)
                .adlsAccount(ADLSAccount.refByQualifiedName(accountQualifiedName))
                .adlsAccountQualifiedName(accountQualifiedName)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.ADLS);
    }

    /**
     * Generate a unique ADLSContainer name.
     *
     * @param name of the ADLSContainer
     * @param accountQualifiedName unique name of the account through which the ADLSContainer is accessible
     * @return a unique name for the ADLSContainer
     */
    public static String generateQualifiedName(String name, String accountQualifiedName) {
        return accountQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a ADLSContainer.
     *
     * @param qualifiedName of the ADLSContainer
     * @param name of the ADLSContainer
     * @return the minimal request necessary to update the ADLSContainer, as a builder
     */
    public static ADLSContainerBuilder<?, ?> updater(String qualifiedName, String name) {
        return ADLSContainer.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ADLSContainer, from a potentially
     * more-complete ADLSContainer object.
     *
     * @return the minimal object necessary to update the ADLSContainer, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ADLSContainer are not found in the initial object
     */
    @Override
    public ADLSContainerBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "ADLSContainer", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
