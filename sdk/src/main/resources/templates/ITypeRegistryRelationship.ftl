<#macro all subTypes>
    /**
     * Generate a simple name for this TypeRegistryContainmentRelationship.
     *
     * @param end1 first end of the relationship
     * @param end2 second end of the relationship
     * @return the simple name of the TypeRegistryContainmentRelationship
     */
    public static String generateName(TypeRegistryEndDef end1, TypeRegistryEndDef end2) {
        return StringUtils.getLowerSnakeCase(end2.getTypeRegistryType()) + "_"
                + StringUtils.getLowerSnakeCase(end1.getTypeRegistryType());
    }

    /**
     * Generate a unique name for this TypeRegistryRelationship.
     *
     * @param parentQualifiedName unique name of the container in which to the TypeRegistryRelationship should be registered
     * @param end1 first end of the relationship
     * @param end2 second end of the relationship
     * @return the unique qualifiedName of the TypeRegistryRelationship
     */
    public static String generateQualifiedName(String parentQualifiedName, TypeRegistryEndDef end1, TypeRegistryEndDef end2) {
        String name = generateName(end1, end2);
        return generateQualifiedName(parentQualifiedName, name);
    }

    /**
     * Generate a unique name for this TypeRegistryRelationship.
     *
     * @param parentQualifiedName unique name of the container in which to the TypeRegistryContainmentRelationship should be registered
     * @param name human-readable name for the TypeRegistryRelationship
     * @return the unique qualifiedName of the TypeRegistryRelationship
     */
    static String generateQualifiedName(String parentQualifiedName, String name) {
        return parentQualifiedName + "/relationship/" + name;
    }
</#macro>
