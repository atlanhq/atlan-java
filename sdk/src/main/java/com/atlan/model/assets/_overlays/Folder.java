/**
     * Builds the minimal object necessary to create a Folder.
     *
     * @param name of the Folder
     * @param parentFolder in which the Folder should be created, which must have at least
     *               a qualifiedName
     * @return the minimal request necessary to create the Folder, as a builder
     * @throws InvalidRequestException if the parentFolder provided is without a qualifiedName
     */
    public static FolderBuilder<?, ?> creator(String name, Folder parentFolder) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", parentFolder.getQualifiedName());
        map.put("collectionQualifiedName", parentFolder.getCollectionQualifiedName());
        validateRelationship(AtlanCollection.TYPE_NAME, map);
        return creator(name, parentFolder.getCollectionQualifiedName(), parentFolder.getQualifiedName())
                .parent(parentFolder.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Folder.
     *
     * @param name of the Folder
     * @param collection in which the Folder should be created, which must have at least
     *               a qualifiedName
     * @return the minimal request necessary to create the Folder, as a builder
     * @throws InvalidRequestException if the collection provided is without a qualifiedName
     */
    public static FolderBuilder<?, ?> creator(String name, AtlanCollection collection) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", collection.getQualifiedName());
        validateRequired(AtlanCollection.TYPE_NAME, map);
        return creator(name, collection.getQualifiedName(), null).parent(collection.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Folder.
     *
     * @param name of the Folder
     * @param collectionQualifiedName unique name of the AtlanCollection in which the Folder should be created
     * @param parentQualifiedName unique name of the Folder in which this Folder should be created (or null if it should be created directly in the collection)
     * @return the minimal request necessary to create the Folder, as a builder
     */
    public static FolderBuilder<?, ?> creator(String name, String collectionQualifiedName, String parentQualifiedName) {
        String qualifiedName = parentQualifiedName == null
                ? generateQualifiedName(name, collectionQualifiedName)
                : generateQualifiedName(name, parentQualifiedName);
        FolderBuilder<?, ?> builder = Folder._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(qualifiedName)
                .collectionQualifiedName(collectionQualifiedName)
                .parentQualifiedName(collectionQualifiedName)
                .parent(AtlanCollection.refByQualifiedName(collectionQualifiedName));
        if (parentQualifiedName != null) {
            builder.parentQualifiedName(parentQualifiedName).parent(Folder.refByQualifiedName(parentQualifiedName));
        }
        return builder;
    }

    /**
     * Generate a unique Folder.
     *
     * @param name of the Folder
     * @param parentQualifiedName unique name of the collection or parent folder in which this Folder exists
     * @return a unique name for the Folder
     */
    public static String generateQualifiedName(String name, String parentQualifiedName) {
        return parentQualifiedName + "/" + name;
    }