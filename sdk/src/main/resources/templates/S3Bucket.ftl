<#macro all>
    /**
     * Builds the minimal object necessary to create an S3 bucket.
     * Note: this will use the name of the bucket to construct the qualifiedName for the bucket.
     *
     * @param name of the S3 bucket
     * @param connectionQualifiedName unique name of the connection through which the bucket is accessible
     * @return the minimal object necessary to create the S3 bucket, as a builder
     */
    public static S3BucketBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return S3Bucket._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(IS3.generateQualifiedName(connectionQualifiedName, name))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.S3);
    }

    /**
     * Builds the minimal object necessary to create an S3 bucket.
     * Note: this will use the provided ARN to construct the qualifiedName for the bucket.
     *
     * @param name of the S3 bucket
     * @param connectionQualifiedName unique name of the connection through which the bucket is accessible
     * @param awsArn unique ARN of the bucket
     * @return the minimal object necessary to create the S3 bucket, as a builder
     */
    public static S3BucketBuilder<?, ?> creator(String name, String connectionQualifiedName, String awsArn) {
        return S3Bucket._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(IS3.generateQualifiedName(connectionQualifiedName, awsArn))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.S3)
                .awsArn(awsArn);
    }

    /**
     * Builds the minimal object necessary to update a S3Bucket.
     *
     * @param qualifiedName of the S3Bucket
     * @param name of the S3Bucket
     * @return the minimal request necessary to update the S3Bucket, as a builder
     */
    public static S3BucketBuilder<?, ?> updater(String qualifiedName, String name) {
        return S3Bucket._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a S3Bucket, from a potentially
     * more-complete S3Bucket object.
     *
     * @return the minimal object necessary to update the S3Bucket, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for S3Bucket are not found in the initial object
     */
    @Override
    public S3BucketBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        validateRequired(TYPE_NAME, Map.of(
            "qualifiedName", this.getQualifiedName(),
            "name", this.getName()
        ));
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
