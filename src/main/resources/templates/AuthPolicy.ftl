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
     * @see Purpose#createMetadataPolicy(String, String, AuthPolicyType, Collection, Collection, Collection, boolean)
     * @see Purpose#createDataPolicy(String, String, AuthPolicyType, Collection, Collection, boolean)
     */
    public static AuthPolicyBuilder<?, ?> creator(String name) {
        return AuthPolicy._internal()
                .qualifiedName(name)
                .name(name)
                .displayName("");
    }

    /**
     * Builds the minimal object necessary to update an AuthPolicy.
     *
     * @param qualifiedName of the AuthPolicy
     * @param name of the AuthPolicy
     * @return the minimal request necessary to update the AuthPolicy, as a builder
     */
    public static AuthPolicyBuilder<?, ?> updater(String qualifiedName, String name) {
        return AuthPolicy._internal().qualifiedName(qualifiedName).name(name);
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
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "AuthPolicy", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
