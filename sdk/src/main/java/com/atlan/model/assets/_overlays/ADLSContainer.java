// IMPORT: import com.atlan.model.enums.ADLSLeaseState;
// IMPORT: import com.atlan.model.enums.ADLSLeaseStatus;
// IMPORT: import com.atlan.model.structs.AzureTag;

/**
     * Builds the minimal object necessary to create a ADLSContainer.
     *
     * @param name of the ADLSContainer
     * @param account in which the ADLSContainer should be created, which must have at least
     *                a qualifiedName
     * @return the minimal request necessary to create the ADLSContainer, as a builder
     * @throws InvalidRequestException if the container provided is without a qualifiedName
     */
    public static ADLSContainerBuilder<?, ?> creator(String name, ADLSAccount account) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", account.getConnectionQualifiedName());
        map.put("qualifiedName", account.getQualifiedName());
        validateRelationship(ADLSAccount.TYPE_NAME, map);
        return creator(name, account.getConnectionQualifiedName(), account.getQualifiedName())
                .adlsAccount(account.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a ADLSContainer.
     *
     * @param name of the ADLSContainer
     * @param accountQualifiedName unique name of the account through which the ADLSContainer is accessible
     * @return the minimal object necessary to create the ADLSContainer, as a builder
     */
    public static ADLSContainerBuilder<?, ?> creator(String name, String accountQualifiedName) {
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(accountQualifiedName);
        return creator(name, connectionQualifiedName, accountQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a ADLSContainer.
     *
     * @param name of the ADLSContainer
     * @param connectionQualifiedName unique name o fthe connection in which the ADLSContainer exists
     * @param accountQualifiedName unique name of the account through which the ADLSContainer is accessible
     * @return the minimal object necessary to create the ADLSContainer, as a builder
     */
    public static ADLSContainerBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String accountQualifiedName) {
        return ADLSContainer._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, accountQualifiedName))
                .name(name)
                .adlsAccount(ADLSAccount.refByQualifiedName(accountQualifiedName))
                .adlsAccountQualifiedName(accountQualifiedName)
                .connectionQualifiedName(connectionQualifiedName);
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