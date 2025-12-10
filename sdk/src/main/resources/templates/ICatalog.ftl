<#macro all subTypes>
    /**
     * Reference to an asset by its qualifiedName.
     *
     * @param typeName the type of the asset being referenced
     * @param qualifiedName the qualifiedName of the asset to reference
     * @return reference to an asset that can be used for defining a lineage relationship to the asset
     */
    public static ICatalog getLineageReference(String typeName, String qualifiedName) {
        ICatalog ref = null;
        switch (typeName) {
        <#list subTypes as subType>
            case ${subType}.TYPE_NAME:
                ref = ${subType}.refByQualifiedName(qualifiedName);
                break;
        </#list>
            default:
                // Do nothing — not a supported Catalog subtype
                break;
        }
        return ref;
    }

    /**
     * Reduce the asset to the minimum set of properties required to relate to it.
     *
     * @return an asset containing the minimal set of properties required to relate to this asset
     * @throws InvalidRequestException if any of the minimal set of required properties are not found in the initial object
     */
    ICatalog trimToReference() throws InvalidRequestException;
</#macro>
