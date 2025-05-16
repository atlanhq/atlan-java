<#macro all>
    /**
     * Builds the minimal object necessary to update a Link.
     *
     * @param reference a reference to the asset to which the Link should be attached
     * @param title for the Link
     * @param url of the Link
     * @return the minimal object necessary to create the Link and attach it to the asset, as a builder
     * @throws InvalidRequestException if the provided asset reference is missing any required information
     */
    public static LinkBuilder<?, ?> creator(Asset reference, String title, String url) throws InvalidRequestException {
        return creator(reference, title, url, false);
    }

    /**
     * Builds the minimal object necessary to update a Link.
     *
     * @param reference a reference to the asset to which the Link should be attached
     * @param title for the Link
     * @param url of the Link
     * @param idempotent if true, creates the link such that its title is unique against the provided asset (making it easier to update the link), otherwise will create a link whose title need not be unique on the asset
     * @return the minimal object necessary to create the Link and attach it to the asset, as a builder
     * @throws InvalidRequestException if the provided asset reference is missing any required information
     */
    public static LinkBuilder<?, ?> creator(Asset reference, String title, String url, boolean idempotent) throws InvalidRequestException {
        LinkBuilder<?, ?> builder = Link._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(title)
                .link(url)
                .asset(reference.trimToReference());
        return idempotent
            ? builder.qualifiedName(generateQualifiedName(reference.getQualifiedName(), title))
            : builder.qualifiedName(generateQualifiedName());
    }

    /**
     * Builds the minimal object necessary to update a Link.
     *
     * @param qualifiedName of the Link
     * @param name of the Link
     * @return the minimal request necessary to update the Link, as a builder
     */
    public static LinkBuilder<?, ?> updater(String qualifiedName, String name) {
        return Link._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Link, from a potentially
     * more-complete Link object.
     *
     * @return the minimal object necessary to update the Link, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for Link are not found in the initial object
     */
    @Override
    public LinkBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Generate a unique link name.
     * Note: while the UI allows you to create any number of links with the same title on
     * a single asset, using this will ensure there is only a single link with a given title
     * on an asset (and will replace any such existing link if it already exists).
     *
     * @param assetQualifiedName unique name of the asset the link is being related to
     * @param title unique title to give to the link
     * @return a unique name for the link
     */
    public static String generateQualifiedName(String assetQualifiedName, String title) {
        return assetQualifiedName + "/" + title;
    }

    /**
     * Generate a unique link name.
     * Note: this ensures an entirely unique name is generated each time it is called.
     * While this guarantees no conflicts, it also prevents idempotency.
     *
     * @return a unique name for the link
     */
    private static String generateQualifiedName() {
        return UUID.randomUUID().toString();
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
