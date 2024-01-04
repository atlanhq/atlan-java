<#macro all subTypes>
    /**
     * Generate a unique S3 name.
     *
     * @param connectionQualifiedName unique name of the connection
     * @param name unique name for the S3 artifact (i.e. the ARN for the asset)
     * @return a unique name for the S3 artifact
     */
    public static String generateQualifiedName(String connectionQualifiedName, String name) {
        return connectionQualifiedName + "/" + name;
    }
</#macro>
