<#macro all subTypes>
    /**
     * Generate a unique name that does not include any path delimiters.
     *
     * @param name of the object for which to generate a unique name
     * @return a unique name for the object
     */
    @JsonIgnore
    public static String getSlugForName(String name) {
        return name.replaceAll("/", "±");
    }

    /**
     * Reverse a unique name without path delimiters back to the original name.
     *
     * @param slug unique name of the object for which to reverse back to its name
     * @return original name of the object
     */
    @JsonIgnore
    public static String getNameFromSlug(String slug) {
        return slug.replaceAll("±", "/");
    }
</#macro>
