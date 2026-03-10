// IMPORT: import java.nio.charset.StandardCharsets;
// IMPORT: import java.util.Base64;
// IMPORT: import com.atlan.model.enums.AtlanAnnouncementType;
// IMPORT: import com.atlan.model.enums.CertificateStatus;

    /**
     * Builds the minimal object necessary to create a Query.
     *
     * @param name of the Query
     * @param parentFolder in which the Query should be created, which must have at least
     *               a qualifiedName
     * @return the minimal request necessary to create the Query, as a builder
     * @throws InvalidRequestException if the parentFolder provided is without a qualifiedName
     */
    public static AtlanQueryBuilder<?, ?> creator(String name, Folder parentFolder) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", parentFolder.getQualifiedName());
        map.put("collectionQualifiedName", parentFolder.getCollectionQualifiedName());
        validateRelationship(AtlanCollection.TYPE_NAME, map);
        return creator(name, parentFolder.getCollectionQualifiedName(), parentFolder.getQualifiedName())
                .parent(parentFolder.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Query.
     *
     * @param name of the Query
     * @param collection in which the Query should be created, which must have at least
     *               a qualifiedName
     * @return the minimal request necessary to create the Query, as a builder
     * @throws InvalidRequestException if the collection provided is without a qualifiedName
     */
    public static AtlanQueryBuilder<?, ?> creator(String name, AtlanCollection collection)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", collection.getQualifiedName());
        validateRelationship(AtlanCollection.TYPE_NAME, map);
        return creator(name, collection.getQualifiedName(), null).parent(collection.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a Query.
     *
     * @param name of the Query
     * @param collectionQualifiedName unique name of the AtlanCollection in which the Query should be created
     * @param parentFolderQualifiedName unique name of the Folder in which this Query should be created, or null if it should be created directly in the collection
     * @return the minimal request necessary to create the Query, as a builder
     */
    public static AtlanQueryBuilder<?, ?> creator(
            String name, String collectionQualifiedName, String parentFolderQualifiedName) {
        String qualifiedName = parentFolderQualifiedName == null
                ? generateQualifiedName(name, collectionQualifiedName)
                : generateQualifiedName(name, parentFolderQualifiedName);
        AtlanQueryBuilder<?, ?> builder = AtlanQuery._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(qualifiedName)
                .collectionQualifiedName(collectionQualifiedName)
                .parentQualifiedName(collectionQualifiedName)
                .parent(AtlanCollection.refByQualifiedName(collectionQualifiedName));
        if (parentFolderQualifiedName != null) {
            builder.parentQualifiedName(parentFolderQualifiedName)
                    .parent(Folder.refByQualifiedName(parentFolderQualifiedName));
        }
        return builder;
    }

    /**
     * Generate a unique Query.
     *
     * @param name of the Query
     * @param parentQualifiedName unique name of the collection or folder in which this Query exists
     * @return a unique name for the Query
     */
    public static String generateQualifiedName(String name, String parentQualifiedName) {
        return parentQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a AtlanQuery.
     *
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @param collectionQualifiedName qualifiedName of the parent collection the query is contained within
     * @param parentQualifiedName qualifiedName of the parent collection or folder the query is contained within
     * @return the minimal request necessary to update the AtlanQuery, as a builder
     */
    public static AtlanQueryBuilder<?, ?> updater(
            String qualifiedName, String name, String collectionQualifiedName, String parentQualifiedName) {
        INamespace parent;
        if (collectionQualifiedName.equals(parentQualifiedName)) {
            parent = AtlanCollection.refByQualifiedName(collectionQualifiedName);
        } else {
            parent = Folder.refByQualifiedName(parentQualifiedName);
        }
        return AtlanQuery._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name)
                .parent(parent)
                .parentQualifiedName(parentQualifiedName)
                .collectionQualifiedName(collectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AtlanQuery, from a potentially
     * more-complete AtlanQuery object.
     *
     * @return the minimal object necessary to update the AtlanQuery, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AtlanQuery are not found in the initial object
     */
    @Override
    public AtlanQueryBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        map.put("collectionQualifiedName", this.getCollectionQualifiedName());
        map.put("parentQualifiedName", this.getParentQualifiedName());
        validateRequired(TYPE_NAME, map);
        return updater(
                this.getQualifiedName(),
                this.getName(),
                this.getCollectionQualifiedName(),
                this.getParentQualifiedName());
    }

    public abstract static class AtlanQueryBuilder<C extends AtlanQuery, B extends AtlanQueryBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {

        private static final String DEFAULT_VARIABLE_SCHEMA =
                "{\"customvariablesDateTimeFormat\":{\"defaultDateFormat\":\"YYYY-MM-DD\",\"defaultTimeFormat\":\"HH:mm\"},\"customVariables\":[]}";

        public B withRawQuery(String schemaQualifiedName, String query) {
            String databaseQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(schemaQualifiedName);
            String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
            return connectionName(Connection.getConnectorFromQualifiedName(connectionQualifiedName))
                    .connectionQualifiedName(connectionQualifiedName)
                    .defaultDatabaseQualifiedName(databaseQualifiedName)
                    .defaultSchemaQualifiedName(schemaQualifiedName)
                    .isVisualQuery(false)
                    .rawQueryText(query)
                    .variablesSchemaBase64(Base64.getEncoder()
                            .encodeToString(DEFAULT_VARIABLE_SCHEMA.getBytes(StandardCharsets.UTF_8)));
        }
    }