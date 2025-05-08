<#macro all>
    /**
     * Builds the minimal object necessary to create a GCSObject.
     *
     * @param name of the GCSObject
     * @param bucket in which the GCSObject should be created, which must have at least
     *               a qualifiedName
     * @return the minimal request necessary to create the GCSObject, as a builder
     * @throws InvalidRequestException if the bucket provided is without a qualifiedName
     */
    public static GCSObjectBuilder<?, ?> creator(String name, GCSBucket bucket) throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", bucket.getConnectionQualifiedName());
        map.put("name", bucket.getName());
        map.put("qualifiedName", bucket.getQualifiedName());
        validateRelationship(GCSBucket.TYPE_NAME, map);
        return creator(
            name,
            bucket.getConnectionQualifiedName(),
            bucket.getName(),
            bucket.getQualifiedName()
        ).gcsBucket(bucket.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a GCSObject.
     *
     * @param name of the GCSObject
     * @param bucketQualifiedName unique name of the bucket in which the GCSObject is contained
     * @return the minimal object necessary to create the GCSObject, as a builder
     */
    public static GCSObjectBuilder<?, ?> creator(String name, String bucketQualifiedName) {
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(bucketQualifiedName);
        String bucketName = StringUtils.getNameFromQualifiedName(bucketQualifiedName);
        return creator(name, connectionQualifiedName, bucketName, bucketQualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a GCSObject.
     *
     * @param name of the GCSObject
     * @param connectionQualifiedName unique name of the connection in which the GCSObject should be created
     * @param bucketName simple name of the GCSBucket in which the GCSObject should be created
     * @param bucketQualifiedName unique name of the GCSBucket in which the GCSObject should be created
     * @return the minimal object necessary to create the GCSObject, as a builder
     */
    public static GCSObjectBuilder<?, ?> creator(String name, String connectionQualifiedName, String bucketName, String bucketQualifiedName) {
        return GCSObject._internal()
            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .qualifiedName(generateQualifiedName(name, bucketQualifiedName))
            .name(name)
            .connectionQualifiedName(connectionQualifiedName)
            .gcsBucketName(bucketName)
            .gcsBucketQualifiedName(bucketQualifiedName)
            .gcsBucket(GCSBucket.refByQualifiedName(bucketQualifiedName));
    }

    /**
     * Generate a unique GCSObject name.
     *
     * @param name of the GCSObject
     * @param bucketQualifiedName unique name of the bucket in which the GCSObject is contained
     * @return a unique name for the GCSObject
     */
    public static String generateQualifiedName(String name, String bucketQualifiedName) {
        return bucketQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a GCSObject.
     *
     * @param qualifiedName of the GCSObject
     * @param name of the GCSObject
     * @return the minimal request necessary to update the GCSObject, as a builder
     */
    public static GCSObjectBuilder<?, ?> updater(String qualifiedName, String name) {
        return GCSObject._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a GCSObject, from a potentially
     * more-complete GCSObject object.
     *
     * @return the minimal object necessary to update the GCSObject, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for GCSObject are not found in the initial object
     */
    @Override
    public GCSObjectBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
