<#macro all>
    /**
     * Builds the minimal object necessary to create a Persona.
     *
     * @param name of the Persona
     * @return the minimal request necessary to create the Persona, as a builder
     */
    public static PersonaBuilder<?, ?> creator(String name) {
        return Persona._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(name)
                .name(name)
                .displayName(name)
                .isAccessControlEnabled(true)
                .description("");
    }

    /**
     * Builds the minimal object necessary to update a Persona.
     *
     * @param qualifiedName of the Persona
     * @param name of the Persona
     * @param isEnabled whether the Persona should be activated (true) or deactivated (false)
     * @return the minimal request necessary to update the Persona, as a builder
     */
    public static PersonaBuilder<?, ?> updater(String qualifiedName, String name, boolean isEnabled) {
        return Persona._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name)
                .isAccessControlEnabled(isEnabled);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Persona, from a potentially
     * more-complete Persona object.
     *
     * @return the minimal object necessary to update the Persona, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for Persona are not found in the initial object
     */
    @Override
    public PersonaBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        if (this.getIsAccessControlEnabled() == null) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, TYPE_NAME, "isAccessControlEnabled");
        }
        return updater(this.getQualifiedName(), this.getName(), this.getIsAccessControlEnabled());
    }

    /**
     * Find a Persona by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the persona, if found.
     *
     * @param client connectivity to the Atlan tenant in which to search for the Persona
     * @param name of the Persona
     * @return all Personas with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the Persona does not exist
     */
    public static List<Persona> findByName(AtlanClient client, String name) throws AtlanException {
        return findByName(client, name, (List<AtlanField>) null);
    }

    /**
     * Find a Persona by its human-readable name.
     *
     * @param client connectivity to the Atlan tenant in which to search for the Persona
     * @param name of the Persona
     * @param attributes an optional collection of attributes (unchecked) to retrieve for the Persona
     * @return all Personas with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the Persona does not exist
     */
    public static List<Persona> findByName(AtlanClient client, String name, Collection<String> attributes)
            throws AtlanException {
        List<Persona> results = new ArrayList<>();
        Persona.select(client)
                .where(Persona.NAME.eq(name))
                ._includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof Persona)
                .forEach(p -> results.add((Persona) p));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.PERSONA_NOT_FOUND_BY_NAME, name);
        }
        return results;
    }

    /**
     * Find a Persona by its human-readable name.
     *
     * @param client connectivity to the Atlan tenant in which to search for the Persona
     * @param name of the Persona
     * @param attributes an optional list of attributes (checked) to retrieve for the Persona
     * @return all Personas with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the Persona does not exist
     */
    public static List<Persona> findByName(AtlanClient client, String name, List<AtlanField> attributes)
            throws AtlanException {
        List<Persona> results = new ArrayList<>();
        Persona.select(client)
                .where(Persona.NAME.eq(name))
                .includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof Persona)
                .forEach(p -> results.add((Persona) p));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.PERSONA_NOT_FOUND_BY_NAME, name);
        }
        return results;
    }

    /**
     * Builds the minimal object necessary to create a metadata policy for a Persona.
     *
     * @param name of the policy
     * @param personaId unique identifier (GUID) of the persona for which to create this metadata policy
     * @param policyType type of policy (for example allow vs deny)
     * @param actions to include in the policy
     * @param connectionQualifiedName unique name of the connection whose assets this policy will control
     * @param resources against which to apply the policy, given in the form {@code entity:qualifiedNamePrefix}
     * @return the minimal request necessary to create the metadata policy for the Persona, as a builder
     */
    public static AuthPolicy.AuthPolicyBuilder<?, ?> createMetadataPolicy(
        String name,
        String personaId,
        AuthPolicyType policyType,
        Collection<PersonaMetadataAction> actions,
        String connectionQualifiedName,
        Collection<String> resources) {
        return AuthPolicy.creator(name)
                .policyActions(actions)
                .policyCategory(AuthPolicyCategory.PERSONA)
                .policyType(policyType)
                .connectionQualifiedName(connectionQualifiedName)
                .policyResources(resources)
                .policyResourceCategory(AuthPolicyResourceCategory.CUSTOM)
                .policyServiceName("atlas")
                .policySubCategory("metadata")
                .accessControl(Persona.refByGuid(personaId));
    }

    /**
     * Builds the minimal object necessary to create a data policy for a Persona.
     *
     * @param name of the policy
     * @param personaId unique identifier (GUID) of the persona for which to create this data policy
     * @param policyType type of policy (for example allow vs deny)
     * @param connectionQualifiedName unique name of the connection whose assets this policy will control
     * @param resources against which to apply the policy, given in the form {@code entity:qualifiedNamePrefix}
     * @return the minimal request necessary to create the data policy for the Persona, as a builder
     */
    public static AuthPolicy.AuthPolicyBuilder<?, ?> createDataPolicy(
        String name,
        String personaId,
        AuthPolicyType policyType,
        String connectionQualifiedName,
        Collection<String> resources) {
        return AuthPolicy.creator(name)
            .policyAction(DataAction.SELECT)
            .policyCategory(AuthPolicyCategory.PERSONA)
            .policyType(policyType)
            .connectionQualifiedName(connectionQualifiedName)
            .policyResources(resources)
            .policyResource("entity-type:*")
            .policyResourceCategory(AuthPolicyResourceCategory.ENTITY)
            .policyServiceName("heka")
            .policySubCategory("data")
            .accessControl(Persona.refByGuid(personaId));
    }

    /**
     * Builds the minimal object necessary to create a glossary policy for a Persona.
     *
     * @param name of the policy
     * @param personaId unique identifier (GUID) of the persona for which to create this glossary policy
     * @param policyType type of policy (for example allow vs deny)
     * @param actions to include in the policy
     * @param resources against which to apply the policy, given in the form {@code entity:qualifiedName} of the glossary
     * @return the minimal request necessary to create the glossary policy for the Persona, as a builder
     */
    public static AuthPolicy.AuthPolicyBuilder<?, ?> createGlossaryPolicy(
        String name,
        String personaId,
        AuthPolicyType policyType,
        Collection<PersonaGlossaryAction> actions,
        Collection<String> resources) {
        return AuthPolicy.creator(name)
            .policyActions(actions)
            .policyCategory(AuthPolicyCategory.PERSONA)
            .policyType(policyType)
            .policyResources(resources)
            .policyResourceCategory(AuthPolicyResourceCategory.CUSTOM)
            .policyServiceName("atlas")
            .policySubCategory("glossary")
            .accessControl(Persona.refByGuid(personaId));
    }

    /**
     * Builds the minimal object necessary to create a domain policy for a Persona.
     *
     * @param name of the policy
     * @param personaId unique identifier (GUID) of the persona for which to create this domain policy
     * @param actions to include in the policy
     * @param resources against which to apply the policy, given in the form {@code entity:qualifiedName} where the qualifiedName is for a domain or subdomain
     * @return the minimal request necessary to create the domain policy for the Persona, as a builder
     */
    public static AuthPolicy.AuthPolicyBuilder<?, ?> createDomainPolicy(
        String name,
        String personaId,
        Collection<PersonaDomainAction> actions,
        Collection<String> resources) {
        return AuthPolicy.creator(name)
            .policyActions(actions)
            .policyCategory(AuthPolicyCategory.PERSONA)
            .policyType(AuthPolicyType.ALLOW)
            .policyResources(resources)
            .policyResourceCategory(AuthPolicyResourceCategory.CUSTOM)
            .policyServiceName("atlas")
            .policySubCategory("domain")
            .accessControl(Persona.refByGuid(personaId));
    }

    /**
     * Builds the minimal object necessary to create an AI policy for a Persona.
     *
     * @param name of the policy
     * @param personaId unique identifier (GUID) of the persona for which to create this AI policy
     * @param actions to include in the policy
     * @param resourceTypes against which to apply the policy, either AIApplication or AIModel (or both)
     * @return the minimal request necessary to create the AI policy for the Persona, as a builder
     */
    public static AuthPolicy.AuthPolicyBuilder<?, ?> createAIPolicy(
        String name, String personaId, Collection<PersonaAIAction> actions, Collection<String> resourceTypes) {
        AuthPolicy.AuthPolicyBuilder<?, ?> builder = AuthPolicy.creator(name)
                .policyActions(actions)
                .policyCategory(AuthPolicyCategory.PERSONA)
                .policyType(AuthPolicyType.ALLOW)
                .policyResource("entity:*")
                .policyResourceCategory(AuthPolicyResourceCategory.CUSTOM)
                .policyServiceName("atlas")
                .policySubCategory("ai")
                .policyFilterCriteria("")
                .accessControl(Persona.refByGuid(personaId));
        for (String resourceType : resourceTypes) {
            builder.policyResource("entity-type:" + resourceType);
        }
        return builder;
    }

    /**
     * Remove the system description from a ${className}.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ${className}'s description
     * @param qualifiedName of the ${className}
     * @param name of the ${className}
     * @param isEnabled whether the Persona should be activated (true) or deactivated (false)
     * @return the updated ${className}, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ${className} removeDescription(AtlanClient client, String qualifiedName, String name, boolean isEnabled) throws AtlanException {
        return (${className}) Asset.removeDescription(client, updater(qualifiedName, name, isEnabled));
    }

    /**
     * Remove the user's description from a ${className}.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ${className}'s description
     * @param qualifiedName of the ${className}
     * @param name of the ${className}
     * @param isEnabled whether the Persona should be activated (true) or deactivated (false)
     * @return the updated ${className}, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ${className} removeUserDescription(AtlanClient client, String qualifiedName, String name, boolean isEnabled) throws AtlanException {
        return (${className}) Asset.removeUserDescription(client, updater(qualifiedName, name, isEnabled));
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
