<#macro all>
    /**
     * Builds the minimal object necessary to create an S3 object.
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
                .connectorType(AtlanConnectorType.S3)
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
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "S3Object", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
