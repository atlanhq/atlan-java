<#macro all>
    /**
     * Builds the minimal object necessary to create a Badge.
     *
     * @param name of the Badge
     * @param cmName human-readable name of the custom metadata for which to create the badge
     * @param cmAttribute human-readable name of the custom metadata attribute for which to create the badge
     * @return the minimal request necessary to create the Badge, as a builder
     * @throws AtlanException if the specified custom metadata for the badge cannot be found
     */
    public static BadgeBuilder<?, ?> creator(String name, String cmName, String cmAttribute) throws AtlanException {
        return creator(Atlan.getDefaultClient(), name, cmName, cmAttribute);
    }

    /**
     * Builds the minimal object necessary to create a Badge.
     *
     * @param client connectivity to the Atlan tenant on which the Badge is intended to be created
     * @param name of the Badge
     * @param cmName human-readable name of the custom metadata for which to create the badge
     * @param cmAttribute human-readable name of the custom metadata attribute for which to create the badge
     * @return the minimal request necessary to create the Badge, as a builder
     * @throws AtlanException if the specified custom metadata for the badge cannot be found
     */
    public static BadgeBuilder<?, ?> creator(AtlanClient client, String name, String cmName, String cmAttribute) throws AtlanException {
        String cmId = client.getCustomMetadataCache().getIdForName(cmName);
        String cmAttrId = client.getCustomMetadataCache().getAttrIdForName(cmName, cmAttribute);
        return Badge._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(client, cmName, cmAttribute))
                .name(name)
                .badgeMetadataAttribute(cmId + "." + cmAttrId);
    }

    /**
     * Generate a unique name for this badge.
     *
     * @param cmName human-readable name of the custom metadata for which to create the badge
     * @param cmAttribute human-readable name of the custom metadata attribute for which to create the badge
     * @return the unique qualifiedName of the badge
     * @throws AtlanException if the specified custom metadata cannot be found
     */
    public static String generateQualifiedName(String cmName, String cmAttribute) throws AtlanException {
        return generateQualifiedName(Atlan.getDefaultClient(), cmName, cmAttribute);
    }

    /**
     * Generate a unique name for this badge.
     *
     * @param client connectivity to the Atlan tenant through which to generate the unique name of the badge
     * @param cmName human-readable name of the custom metadata for which to create the badge
     * @param cmAttribute human-readable name of the custom metadata attribute for which to create the badge
     * @return the unique qualifiedName of the badge
     * @throws AtlanException if the specified custom metadata cannot be found
     */
    public static String generateQualifiedName(AtlanClient client, String cmName, String cmAttribute) throws AtlanException {
        String cmId = client.getCustomMetadataCache().getIdForName(cmName);
        String cmAttrId = client.getCustomMetadataCache().getAttrIdForName(cmName, cmAttribute);
        return "badges/global/" + cmId + "." + cmAttrId;
    }

    /**
     * Builds the minimal object necessary to update a Badge.
     *
     * @param qualifiedName of the Badge
     * @param name of the Badge
     * @return the minimal request necessary to update the Badge, as a builder
     */
    public static BadgeBuilder<?, ?> updater(String qualifiedName, String name) {
        return Badge._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Badge, from a potentially
     * more-complete Badge object.
     *
     * @return the minimal object necessary to update the Badge, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for Badge are not found in the initial object
     */
    @Override
    public BadgeBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "Badge", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
