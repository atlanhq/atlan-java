<#macro all>
    /**
     * Builds the minimal object necessary to create a File.
     *
     * @param name of the File (if multiple files with the same name exist in the connection, also include the path that makes this file unique)
     * @param connectionQualifiedName unique name of the connection in which the file is contained
     * @param type of the File
     * @return the minimal request necessary to update the File, as a builder
     */
    public static FileBuilder<?, ?> creator(String name, String connectionQualifiedName, FileType type) {
        return File._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .connectionQualifiedName(connectionQualifiedName)
                .name(name)
                .qualifiedName(generateQualifiedName(connectionQualifiedName, name))
                .fileType(type);
    }

    /**
     * Generate a unique File name.
     *
     * @param connectionQualifiedName unique name of the connection in which the file is contained
     * @param name of the File (including any path details, if necessary to ensure this file is unique within the connection)
     * @return a unique name for the File
     */
    public static String generateQualifiedName(String connectionQualifiedName, String name) {
        return connectionQualifiedName + "/" + StringUtils.trimPathDelimiters(name);
    }

    /**
     * Builds the minimal object necessary to update a File.
     *
     * @param qualifiedName of the File
     * @param name of the File
     * @return the minimal request necessary to update the File, as a builder
     */
    public static FileBuilder<?, ?> updater(String qualifiedName, String name) {
        return File._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a File, from a potentially
     * more-complete File object.
     *
     * @return the minimal object necessary to update the File, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for File are not found in the initial object
     */
    @Override
    public FileBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
