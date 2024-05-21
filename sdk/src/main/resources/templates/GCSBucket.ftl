<#macro all>
    /**
     * Builds the minimal object necessary to create a GCSBucket.
     *
     * @param name of the GCSBucket
     * @param connectionQualifiedName unique name of the connection through which the GCSBucket is accessible
     * @return the minimal object necessary to create the GCSBucket, as a builder
     */
    public static GCSBucketBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return GCSBucket._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.GCS);
    }

    /**
     * Generate a unique GCSBucket name.
     *
     * @param name of the GCSBucket
     * @param connectionQualifiedName unique name of the connection through which the GCSBucket is accessible
     * @return a unique name for the GCSBucket
     */
    public static String generateQualifiedName(String name, String connectionQualifiedName) {
        return connectionQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a GCSBucket.
     *
     * @param qualifiedName of the GCSBucket
     * @param name of the GCSBucket
     * @return the minimal request necessary to update the GCSBucket, as a builder
     */
    public static GCSBucketBuilder<?, ?> updater(String qualifiedName, String name) {
        return GCSBucket._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a GCSBucket, from a potentially
     * more-complete GCSBucket object.
     *
     * @return the minimal object necessary to update the GCSBucket, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for GCSBucket are not found in the initial object
     */
    @Override
    public GCSBucketBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(TYPE_NAME, Map.of(
            "qualifiedName", this.getQualifiedName(),
            "name", this.getName()
        ));
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
