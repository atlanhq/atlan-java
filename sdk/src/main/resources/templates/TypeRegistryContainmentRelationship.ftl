<#macro all>
    /**
     * Builds the minimal object necessary to create a TypeRegistryContainmentRelationship.
     *
     * @param name of the TypeRegistryContainmentRelationship
     * @param workspace in which the TypeRegistryContainmentRelationship should be registered
     * @param parent end of the relationship that represents the containing asset (parent)
     * @param children end of the relationship that represents the contained assets (children)
     * @return the minimal request necessary to create the TypeRegistryContainmentRelationship, as a builder
     * @throws InvalidRequestException if the table provided is without a qualifiedName
     */
    public static TypeRegistryContainmentRelationshipBuilder<?, ?> creator(String name, TypeRegistryWorkspace workspace, TypeRegistryEndDef parent, TypeRegistryEndDef children) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", workspace.getQualifiedName());
        validateRelationship(Table.TYPE_NAME, map);
        return creator(
            name,
            workspace.getQualifiedName(),
            parent,
            children)
            .typeRegistryWorkspace(workspace.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a TypeRegistryContainmentRelationship.
     *
     * @param name of the TypeRegistryContainmentRelationship
     * @param workspaceQualifiedName unique name of the workspace in which the TypeRegistryContainmentRelationship should be registered
     * @param parent end of the relationship that represents the containing asset (parent)
     * @param children end of the relationship that represents the contained assets (children)
     * @return the minimal request necessary to create the TypeRegistryContainmentRelationship, as a builder
     */
    public static TypeRegistryContainmentRelationshipBuilder<?, ?> creator(String name, String workspaceQualifiedName, TypeRegistryEndDef parent, TypeRegistryEndDef children) {
        return TypeRegistryContainmentRelationship._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .qualifiedName(ITypeRegistryRelationship.generateQualifiedName(workspaceQualifiedName, parent, children))
            .name(ITypeRegistryRelationship.generateName(parent, children))
            .typeRegistryParent(parent)
            .typeRegistryChildren(children)
            .typeRegistryWorkspace(TypeRegistryWorkspace.refByQualifiedName(workspaceQualifiedName));
    }

    /**
     * Builds the minimal object necessary to update a TypeRegistryContainmentRelationship.
     *
     * @param qualifiedName of the TypeRegistryContainmentRelationship
     * @param name of the TypeRegistryContainmentRelationship
     * @return the minimal request necessary to update the TypeRegistryContainmentRelationship, as a builder
     */
    public static TypeRegistryContainmentRelationshipBuilder<?, ?> updater(String qualifiedName, String name) {
        return TypeRegistryContainmentRelationship._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a TypeRegistryContainmentRelationship, from a potentially
     * more-complete TypeRegistryContainmentRelationship object.
     *
     * @return the minimal object necessary to update the TypeRegistryContainmentRelationship, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for TypeRegistryContainmentRelationship are not found in the initial object
     */
    @Override
    public TypeRegistryContainmentRelationshipBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
