// IMPORT: import com.atlan.AtlanClient;
// IMPORT: import com.atlan.cache.SourceTagCache;
// IMPORT: import com.atlan.exception.AtlanException;
// IMPORT: import com.atlan.exception.NotFoundException;
// IMPORT: import com.atlan.model.assets.Connection;
// IMPORT: import com.atlan.model.assets.ITag;

    /**
     * Create a source-synced tag attachment with a particular value, when the attachment is
     * synced to the source.
     *
     * @param client connectivity to an Atlan tenant
     * @param name Unique name of the source tag, in Atlan.
     * @param sourceTagValues Value of the tag attachment, from the source.
     * @param sourceTagSyncTimestamp Time (epoch) when the tag attachment was synced at the source, in milliseconds.
     * @param sourceTagSyncError Error message if the tag attachment sync at the source failed.
     * @return a SourceTagAttachment with the provided information
     * @throws AtlanException on any error communicating via the underlying APIs
     * @throws NotFoundException if the source-synced tag cannot be resolved
     */
    public static SourceTagAttachment byName(
            AtlanClient client,
            SourceTagCache.SourceTagName name,
            List<SourceTagAttachmentValue> sourceTagValues,
            Long sourceTagSyncTimestamp,
            String sourceTagSyncError)
            throws AtlanException {
        return byName(client, name, sourceTagValues, true, sourceTagSyncTimestamp, sourceTagSyncError);
    }

    /**
     * Create a source-synced tag attachment with a particular value, when the attachment is
     * not synced to the source.
     *
     * @param client connectivity to an Atlan tenant
     * @param name Unique name of the source tag, in Atlan.
     * @param sourceTagValues Value of the tag attachment, from the source.
     * @return a SourceTagAttachment with the provided information
     * @throws AtlanException on any error communicating via the underlying APIs
     * @throws NotFoundException if the source-synced tag cannot be resolved
     */
    public static SourceTagAttachment byName(
            AtlanClient client, SourceTagCache.SourceTagName name, List<SourceTagAttachmentValue> sourceTagValues)
            throws AtlanException {
        return byName(client, name, sourceTagValues, false, null, null);
    }

    private static SourceTagAttachment byName(
            AtlanClient client,
            SourceTagCache.SourceTagName name,
            List<SourceTagAttachmentValue> sourceTagValues,
            Boolean isSourceTagSynced,
            Long sourceTagSyncTimestamp,
            String sourceTagSyncError)
            throws AtlanException {
        ITag tag = (ITag) client.getSourceTagCache().getByName(name);
        String qualifiedName = tag.getQualifiedName();
        return of(
                tag.getName(),
                qualifiedName,
                tag.getGuid(),
                Connection.getConnectorTypeFromQualifiedName(qualifiedName).getValue(),
                sourceTagValues,
                isSourceTagSynced,
                sourceTagSyncTimestamp,
                sourceTagSyncError,
                null);
    }

    /**
     * Create a source-synced tag attachment with a particular value, when the attachment is
     * synced to the source.
     *
     * @param client connectivity to an Atlan tenant
     * @param sourceTagQualifiedName Unique name of the source tag, in Atlan.
     * @param sourceTagValues Value of the tag attachment, from the source.
     * @param sourceTagSyncTimestamp Time (epoch) when the tag attachment was synced at the source, in milliseconds.
     * @param sourceTagSyncError Error message if the tag attachment sync at the source failed.
     * @return a SourceTagAttachment with the provided information
     * @throws AtlanException on any error communicating via the underlying APIs
     * @throws NotFoundException if the source-synced tag cannot be resolved
     */
    public static SourceTagAttachment byQualifiedName(
            AtlanClient client,
            String sourceTagQualifiedName,
            List<SourceTagAttachmentValue> sourceTagValues,
            Long sourceTagSyncTimestamp,
            String sourceTagSyncError)
            throws AtlanException {
        return byQualifiedName(
                client, sourceTagQualifiedName, sourceTagValues, true, sourceTagSyncTimestamp, sourceTagSyncError);
    }

    /**
     * Create a source-synced tag attachment with a particular value, when the attachment is
     * not synced to the source.
     *
     * @param client connectivity to an Atlan tenant
     * @param sourceTagQualifiedName Unique name of the source tag, in Atlan.
     * @param sourceTagValues Value of the tag attachment, from the source.
     * @return a SourceTagAttachment with the provided information
     * @throws AtlanException on any error communicating via the underlying APIs
     * @throws NotFoundException if the source-synced tag cannot be resolved
     */
    public static SourceTagAttachment byQualifiedName(
            AtlanClient client, String sourceTagQualifiedName, List<SourceTagAttachmentValue> sourceTagValues)
            throws AtlanException {
        return byQualifiedName(client, sourceTagQualifiedName, sourceTagValues, false, null, null);
    }

    private static SourceTagAttachment byQualifiedName(
            AtlanClient client,
            String sourceTagQualifiedName,
            List<SourceTagAttachmentValue> sourceTagValues,
            Boolean isSourceTagSynced,
            Long sourceTagSyncTimestamp,
            String sourceTagSyncError)
            throws AtlanException {
        ITag tag = (ITag) client.getSourceTagCache().getByQualifiedName(sourceTagQualifiedName);
        return of(
                tag.getName(),
                sourceTagQualifiedName,
                tag.getGuid(),
                Connection.getConnectorTypeFromQualifiedName(sourceTagQualifiedName).getValue(),
                sourceTagValues,
                isSourceTagSynced,
                sourceTagSyncTimestamp,
                sourceTagSyncError,
                null);
    }
