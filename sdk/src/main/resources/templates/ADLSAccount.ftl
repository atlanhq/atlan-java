<#macro all>
    /**
     * Builds the minimal object necessary to create a ADLSAccount.
     *
     * @param name of the ADLSAccount
     * @param connectionQualifiedName unique name of the connection through which the ADLSAccount is accessible
     * @return the minimal object necessary to create the ADLSAccount, as a builder
     */
    public static ADLSAccountBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return ADLSAccount._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Generate a unique ADLSAccount name.
     *
     * @param name of the ADLSAccount
     * @param connectionQualifiedName unique name of the connection through which the ADLSAccount is accessible
     * @return a unique name for the ADLSAccount
     */
    public static String generateQualifiedName(String name, String connectionQualifiedName) {
        return connectionQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a ADLSAccount.
     *
     * @param qualifiedName of the ADLSAccount
     * @param name of the ADLSAccount
     * @return the minimal request necessary to update the ADLSAccount, as a builder
     */
    public static ADLSAccountBuilder<?, ?> updater(String qualifiedName, String name) {
        return ADLSAccount._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ADLSAccount, from a potentially
     * more-complete ADLSAccount object.
     *
     * @return the minimal object necessary to update the ADLSAccount, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ADLSAccount are not found in the initial object
     */
    @Override
    public ADLSAccountBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
