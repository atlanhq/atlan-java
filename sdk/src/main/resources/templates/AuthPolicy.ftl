<#macro all>
    /**
     * Builds the minimal object necessary to create an AuthPolicy.
     * Note: this method is only for internal use; for creating policies specific to a persona
     * or purpose, use the helper methods from those classes.
     *
     * @param name of the AuthPolicy
     * @return the minimal request necessary to create the AuthPolicy, as a builder
     * @see Persona#createMetadataPolicy(String, String, AuthPolicyType, Collection, String, Collection)
     * @see Persona#createDataPolicy(String, String, AuthPolicyType, String, Collection)
     * @see Persona#createGlossaryPolicy(String, String, AuthPolicyType, Collection, Collection)
     * @see Purpose#createMetadataPolicy(AtlanClient, String, String, AuthPolicyType, Collection, Collection, Collection, boolean)
     * @see Purpose#createDataPolicy(AtlanClient, String, String, AuthPolicyType, Collection, Collection, boolean)
     */
    public static AuthPolicyBuilder<?, ?> creator(String name) {
        return AuthPolicy._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(name)
                .name(name)
                .displayName("");
    }

    /**
     * Builds the minimal object necessary to apply an update to an AuthPolicy, from a potentially
     * more-complete AuthPolicy object.
     *
     * @return the minimal object necessary to update the AuthPolicy, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AuthPolicy are not found in the initial object
     */
    @Override
    public AuthPolicyBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        throw new InvalidRequestException(ErrorCode.FULL_UPDATE_ONLY, "AuthPolicy");
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
