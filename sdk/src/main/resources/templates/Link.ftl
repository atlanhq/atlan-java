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
        return Link._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName())
                .name(title)
                .link(url)
                .asset(reference.trimToReference());
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
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "Link", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Generate a unique link name.
     *
     * @return a unique name for the link
     */
    private static String generateQualifiedName() {
        return UUID.randomUUID().toString();
    }
</#macro>
