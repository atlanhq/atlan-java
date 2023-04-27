<#macro all>
    /**
     * Generate a unique S3 name.
     *
     * @param connectionQualifiedName unique name of the connection
     * @param awsArn unique ARN for the S3 artifact
     * @return a unique name for the S3 artifact
     */
    public static String generateQualifiedName(String connectionQualifiedName, String awsArn) {
        return connectionQualifiedName + "/" + awsArn;
    }
</#macro>
