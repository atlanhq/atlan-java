<#macro all>
    /**
     * Builds the minimal object necessary to create an S3 object.
     * Note: this will use the provided prefix and name of the object to construct the qualifiedName for the object.
     *
     * @param name of the S3 object
     * @param bucket in which the S3 object should be created, which must have at least
     *               a qualifiedName and name
     * @param prefix the "folder(s)" in which the object exists, within the bucket
     * @return the minimal request necessary to create the S3 object, as a builder
     * @throws InvalidRequestException if the bucket provided is without any required attributes
     */
    public static S3ObjectBuilder<?, ?> creatorWithPrefix(String name, S3Bucket bucket, String prefix)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", bucket.getQualifiedName());
        map.put("name", bucket.getName());
        validateRelationship(S3Bucket.TYPE_NAME, map);
        return creatorWithPrefix(
            name,
            bucket.getQualifiedName(),
            bucket.getName(),
            prefix
        ).bucket(bucket.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create an S3 object.
     * Note: this will use the provided ARN to construct the qualifiedName for the object.
     *
     * @param name of the S3 object
     * @param bucket in which the S3 object should be created, which must have at least
     *               a qualifiedName and name
     * @param awsArn unique ARN of the object
     * @return the minimal request necessary to create the S3 object, as a builder
     * @throws InvalidRequestException if the bucket provided is without any required attributes
     */
    public static S3ObjectBuilder<?, ?> creator(String name, S3Bucket bucket, String awsArn)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", bucket.getQualifiedName());
        map.put("name", bucket.getName());
        validateRelationship(S3Bucket.TYPE_NAME, map);
        return creator(
            name,
            bucket.getQualifiedName(),
            bucket.getName(),
            awsArn
        ).bucket(bucket.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create an S3 object.
     * Note: this will use the prefix and name of the object to construct the qualifiedName for the object.
     *
     * @param name of the S3 object (must be unique within the bucket)
     * @param bucketQualifiedName unique name of the S3 bucket in which the object exists
     * @param bucketName simple human-readable name of the S3 bucket in which the object exists
     * @param prefix the "folder(s)" in which the object exists, within the bucket
     * @return the minimal object necessary to create the S3 object, as a builder
     */
    public static S3ObjectBuilder<?, ?> creatorWithPrefix(
            String name, String bucketQualifiedName, String bucketName, String prefix) {
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(bucketQualifiedName);
        String objectKey = prefix + "/" + name;
        return S3Object._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(IS3.generateQualifiedName(connectionQualifiedName, objectKey))
                .name(name)
                .s3ObjectKey(objectKey)
                .connectionQualifiedName(connectionQualifiedName)
                .s3BucketQualifiedName(bucketQualifiedName)
                .s3BucketName(bucketName)
                .bucket(S3Bucket.refByQualifiedName(bucketQualifiedName));
    }

    /**
     * Builds the minimal object necessary to create an S3 object.
     * Note: this will use the provided ARN to construct the qualifiedName for the object.
     *
     * @param name of the S3 object
     * @param bucketQualifiedName unique name of the S3 bucket in which the object exists
     * @param bucketName simple human-readable name of the S3 bucket in which the object exists
     * @param awsArn unique ARN of the object
     * @return the minimal object necessary to create the S3 object, as a builder
     */
    public static S3ObjectBuilder<?, ?> creator(
            String name, String bucketQualifiedName, String bucketName, String awsArn) {
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(bucketQualifiedName);
        return S3Object._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(IS3.generateQualifiedName(connectionQualifiedName, awsArn))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .awsArn(awsArn)
                .s3BucketQualifiedName(bucketQualifiedName)
                .s3BucketName(bucketName)
                .bucket(S3Bucket.refByQualifiedName(bucketQualifiedName));
    }

    /**
     * Builds the minimal object necessary to update a S3Object.
     *
     * @param qualifiedName of the S3Object
     * @param name of the S3Object
     * @return the minimal request necessary to update the S3Object, as a builder
     */
    public static S3ObjectBuilder<?, ?> updater(String qualifiedName, String name) {
        return S3Object._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a S3Object, from a potentially
     * more-complete S3Object object.
     *
     * @return the minimal object necessary to update the S3Object, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for S3Object are not found in the initial object
     */
    @Override
    public S3ObjectBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
