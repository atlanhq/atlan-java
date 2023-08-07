<#macro all>
    /**
     * Builds the minimal object necessary to create a README.
     *
     * @param reference a reference, by GUID, to the asset to which the README should be attached
     * @param assetName name of the asset to which the README should be attached
     * @param content the HTML content to use for the README
     * @return the minimal object necessary to create the README and attach it to the asset, as a builder
     */
    public static ReadmeBuilder<?, ?> creator(Asset reference, String assetName, String content) {
        return Readme._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(reference.getGuid()))
                .name(generateName(assetName))
                .description(content)
                .asset(reference);
    }

    /**
     * Builds the minimal object necessary to update a Readme.
     *
     * @param assetGuid the GUID of the asset to which the README is attached
     * @param assetName name of the asset to which the README is attached
     * @return the minimal request necessary to update the Readme, as a builder
     */
    public static ReadmeBuilder<?, ?> updater(String assetGuid, String assetName) {
        return Readme._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(assetGuid))
                .name(generateName(assetName));
    }

    /**
     * Builds the minimal object necessary to apply an update to a Readme, from a potentially
     * more-complete Readme object.
     *
     * @return the minimal object necessary to update the Readme, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for Readme are not found in the initial object
     */
    @Override
    public ReadmeBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "Readme", String.join(",", missing));
        }
        return Readme._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(this.getQualifiedName())
                .name(this.getName());
    }

    /**
     * Generate a unique README name.
     *
     * @param assetGuid GUID of the asset to which the README should be attached
     * @return a unique name for the README
     */
    private static String generateQualifiedName(String assetGuid) {
        return assetGuid + "/readme";
    }

    /**
     * Generate a readable README name (although this does not appear anywhere in the UI).
     *
     * @param assetName name of the asset to which the README should be attached
     * @return a readable name for the README
     */
    private static String generateName(String assetName) {
        return assetName + " Readme";
    }
</#macro>
