<#macro all>
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
        return GCSObject._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, bucketQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.GCS)
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
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "GCSObject", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
