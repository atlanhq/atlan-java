<#macro all>
    /**
     * Builds the minimal object necessary to create a Anaplan page.
     *
     * @param name of the page
     * @param app in which the page should be created, which must have at least
     *                 a qualifiedName
     * @return the minimal request necessary to create the page, as a builder
     * @throws InvalidRequestException if the app provided is without a qualifiedName
     */
    public static AnaplanPage.AnaplanPageBuilder<?, ?> creator(String name, AnaplanApp app)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("appQualifiedName", app.getQualifiedName());
        map.put("appName", app.getName());
        map.put("connectionQualifiedName", app.getConnectionQualifiedName());
        validateRelationship(AnaplanApp.TYPE_NAME, map);
        return creator(name, app.getConnectionQualifiedName(), app.getQualifiedName())
                .anaplanApp(app.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Anaplan page.
     *
     * @param name of the page
     * @param appQualifiedName unique name of the app in which this page exists
     * @return the minimal request necessary to create the page, as a builder
     */
    public static AnaplanPage.AnaplanPageBuilder<?, ?> creator(String name, String appQualifiedName) {
        String appName = StringUtils.getNameFromQualifiedName(appQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(appQualifiedName);
        return creator(name, connectionQualifiedName, appQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Anaplan page.
     *
     * @param name of the page
     * @param connectionQualifiedName unique name of the connection in which to create the page
     * @param appQualifiedName unique name of the app in which to create the page
     * @return the minimal request necessary to create the page, as a builder
     */
    public static AnaplanPage.AnaplanPageBuilder<?, ?> creator(
            String name, String connectionQualifiedName, String appQualifiedName) {
        return AnaplanPage._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(name, appQualifiedName))
                .connectionQualifiedName(connectionQualifiedName)
                .anaplanAppQualifiedName(appQualifiedName)
                .anaplanApp(AnaplanApp.refByQualifiedName(appQualifiedName));
    }

    /**
     * Generate a unique page name.
     *
     * @param name of the page
     * @param appQualifiedName unique name of the app in which this page exists
     * @return a unique name for the page
     */
    public static String generateQualifiedName(String name, String appQualifiedName) {
        return appQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a AnaplanPage.
     *
     * @param qualifiedName of the AnaplanPage
     * @param name of the AnaplanPage
     * @return the minimal request necessary to update the AnaplanPage, as a builder
     */
    public static AnaplanPageBuilder<?, ?> updater(String qualifiedName, String name) {
        return AnaplanPage._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AnaplanPage, from a potentially
     * more-complete AnaplanPage object.
     *
     * @return the minimal object necessary to update the AnaplanPage, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AnaplanPage are not found in the initial object
     */
    @Override
    public AnaplanPageBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
