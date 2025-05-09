<#macro all>
    /**
     * Builds the minimal object necessary to create an Anaplan app.
     *
     * @param name of the Anaplan app
     * @param connectionQualifiedName unique name of the connection through which the app is accessible
     * @return the minimal object necessary to create the Anaplan app, as a builder
     */
    public static AnaplanApp.AnaplanAppBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return AnaplanApp._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(connectionQualifiedName + "/" + name)
                .name(name)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a AnaplanApp.
     *
     * @param qualifiedName of the AnaplanApp
     * @param name of the AnaplanApp
     * @return the minimal request necessary to update the AnaplanApp, as a builder
     */
    public static AnaplanAppBuilder<?, ?> updater(String qualifiedName, String name) {
        return AnaplanApp._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AnaplanApp, from a potentially
     * more-complete AnaplanApp object.
     *
     * @return the minimal object necessary to update the AnaplanApp, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AnaplanApp are not found in the initial object
     */
    @Override
    public AnaplanAppBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
