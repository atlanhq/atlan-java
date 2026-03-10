// IMPORT: import com.atlan.model.enums.ADLSAccessTier;
// IMPORT: import com.atlan.model.enums.ADLSAccountStatus;
// IMPORT: import com.atlan.model.enums.ADLSEncryptionTypes;
// IMPORT: import com.atlan.model.enums.ADLSPerformance;
// IMPORT: import com.atlan.model.enums.ADLSProvisionState;
// IMPORT: import com.atlan.model.enums.ADLSReplicationType;
// IMPORT: import com.atlan.model.enums.ADLSStorageKind;
// IMPORT: import com.atlan.model.structs.AzureTag;

/**
     * Builds the minimal object necessary to create a ADLSAccount.
     *
     * @param name of the ADLSAccount
     * @param connectionQualifiedName unique name of the connection through which the ADLSAccount is accessible
     * @return the minimal object necessary to create the ADLSAccount, as a builder
     */
    public static ADLSAccountBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return ADLSAccount._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Generate a unique ADLSAccount name.
     *
     * @param name of the ADLSAccount
     * @param connectionQualifiedName unique name of the connection through which the ADLSAccount is accessible
     * @return a unique name for the ADLSAccount
     */
    public static String generateQualifiedName(String name, String connectionQualifiedName) {
        return connectionQualifiedName + "/" + name;
    }