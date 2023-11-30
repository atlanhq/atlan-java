<#macro all subTypes>
    /**
     * Generate a slug from the provided name.
     *
     * @param name name of the data mesh asset
     * @return a URL-embeddable slug for the asset
     */
    public static String generateSlugForName(String name) {
        return StringUtils.getLowerCamelCase(name);
    }
</#macro>
