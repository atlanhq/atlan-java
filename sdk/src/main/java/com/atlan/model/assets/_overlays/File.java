// IMPORT: import com.atlan.model.enums.FileType;

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