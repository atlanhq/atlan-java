<#macro all>
    /**
     * Builds the minimal object necessary to create a Persona.
     *
     * @param name of the Persona
     * @return the minimal request necessary to create the Persona, as a builder
     */
    public static PersonaBuilder<?, ?> creator(String name) {
        return Persona.builder()
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
        return Persona.builder().qualifiedName(qualifiedName).name(name).isAccessControlEnabled(isEnabled);
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
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (this.getIsAccessControlEnabled() == null) {
            missing.add("isAccessControlEnabled");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "Persona", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName(), this.getIsAccessControlEnabled());
    }

    /**
     * Find a Persona by its human-readable name.
     *
     * @param name of the Persona
     * @param attributes an optional collection of attributes to retrieve for the Persona
     * @return all Personas with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the Persona does not exist
     */
    public static List<Persona> findByName(String name, Collection<String> attributes)
            throws AtlanException {
        Query filter = QueryFactory.CompoundQuery.builder()
                .must(QueryFactory.beActive())
                .must(QueryFactory.beOfType(TYPE_NAME))
                .must(QueryFactory.have(KeywordFields.NAME).eq(name))
                .build()
                ._toQuery();
        IndexSearchRequest.IndexSearchRequestBuilder<?, ?> builder = IndexSearchRequest.builder()
                .dsl(IndexSearchDSL.builder().query(filter).build());
        if (attributes != null && !attributes.isEmpty()) {
            builder.attributes(attributes);
        }
        IndexSearchRequest request = builder.build();
        IndexSearchResponse response = request.search();
        List<Persona> personas = new ArrayList<>();
        if (response != null) {
            List<Asset> results = response.getAssets();
            while (results != null) {
                for (Asset result : results) {
                    if (result instanceof Persona) {
                        personas.add((Persona) result);
                    }
                }
                response = response.getNextPage();
                results = response.getAssets();
            }
        }
        if (personas.isEmpty()) {
            throw new NotFoundException(ErrorCode.PERSONA_NOT_FOUND_BY_NAME, name);
        } else {
            return personas;
        }
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
     * Remove the system description from a ${className}.
     *
     * @param qualifiedName of the ${className}
     * @param name of the ${className}
     * @param isEnabled whether the Persona should be activated (true) or deactivated (false)
     * @return the updated ${className}, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ${className} removeDescription(String qualifiedName, String name, boolean isEnabled) throws AtlanException {
        return (${className}) Asset.removeDescription(updater(qualifiedName, name, isEnabled));
    }

    /**
     * Remove the user's description from a ${className}.
     *
     * @param qualifiedName of the ${className}
     * @param name of the ${className}
     * @param isEnabled whether the Persona should be activated (true) or deactivated (false)
     * @return the updated ${className}, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ${className} removeUserDescription(String qualifiedName, String name, boolean isEnabled) throws AtlanException {
        return (${className}) Asset.removeUserDescription(updater(qualifiedName, name, isEnabled));
    }
</#macro>
