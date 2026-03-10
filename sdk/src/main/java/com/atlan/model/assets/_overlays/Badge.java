// IMPORT: import com.atlan.model.structs.BadgeCondition;

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
    public static BadgeBuilder<?, ?> creator(AtlanClient client, String name, String cmName, String cmAttribute)
            throws AtlanException {
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
     * @param client connectivity to the Atlan tenant through which to generate the unique name of the badge
     * @param cmName human-readable name of the custom metadata for which to create the badge
     * @param cmAttribute human-readable name of the custom metadata attribute for which to create the badge
     * @return the unique qualifiedName of the badge
     * @throws AtlanException if the specified custom metadata cannot be found
     */
    public static String generateQualifiedName(AtlanClient client, String cmName, String cmAttribute)
            throws AtlanException {
        String cmId = client.getCustomMetadataCache().getIdForName(cmName);
        String cmAttrId = client.getCustomMetadataCache().getAttrIdForName(cmName, cmAttribute);
        return "badges/global/" + cmId + "." + cmAttrId;
    }