<#macro all>
    /** Internal tracking of fields that should be serialized with null values. */
    @JsonIgnore
    @Singular
    transient Set<String> nullFields;

    /** Retrieve the list of fields to be serialized with null values. */
    @JsonIgnore
    public Set<String> getNullFields() {
        if (nullFields == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(nullFields);
    }

    /** Atlan tags assigned to the asset. */
    @Singular
    @JsonProperty("classifications")
    SortedSet<AtlanTag> atlanTags;

    /**
     * Map of custom metadata attributes and values defined on the asset. The map is keyed by the human-readable
     * name of the custom metadata set, and the values are a further mapping from human-readable attribute name
     * to the value for that attribute on this asset.
     */
    @Singular("customMetadata")
    Map<String, CustomMetadataAttributes> customMetadataSets;

    /** Status of the asset. */
    AtlanStatus status;

    /** User or account that created the asset. */
    final String createdBy;

    /** User or account that last updated the asset. */
    final String updatedBy;

    /** Time (epoch) at which the asset was created, in milliseconds. */
    @Date
    final Long createTime;

    /** Time (epoch) at which the asset was last updated, in milliseconds. */
    @Date
    final Long updateTime;

    /** Details on the handler used for deletion of the asset. */
    final String deleteHandler;

    /**
     * Depth of this asset within lineage.
     * Note: this will only available in assets retrieved via lineage, and will vary even for
     * the same asset depending on the starting point of the lineage requested.
     */
    final Integer depth;

    /**
     * The names of the Atlan tags that exist on the asset. This is not always returned, even by
     * full retrieval operations. It is better to depend on the detailed values in the Atlan tags
     * property.
     * @see #atlanTags
     */
    @Deprecated
    @Singular
    @JsonProperty("classificationNames")
    SortedSet<String> atlanTagNames;

    /** Unused. */
    Boolean isIncomplete;

    /** Names of terms that have been linked to this asset. */
    @Singular
    SortedSet<String> meaningNames;

    /**
     * Details of terms that have been linked to this asset. This is not set by all API endpoints, so cannot
     * be relied upon in general, even when there are terms assigned to an asset.
     * @deprecated see {@link #assignedTerms} instead
     */
    @Singular
    @Deprecated
    SortedSet<Meaning> meanings;

    /** Unique identifiers (GUIDs) for any background tasks that are yet to operate on this asset. */
    @Singular
    final SortedSet<String> pendingTasks;

    /** {@inheritDoc} */
    @Override
    public String getQualifiedName() {
        return qualifiedName != null ? qualifiedName : (getUniqueAttributes() != null ? getUniqueAttributes().getQualifiedName() : null);
    }

    /**
     * Retrieve the value of the custom metadata attribute from this asset.
     * Note: returns null in all cases where the custom metadata does not exist, is not available on this asset,
     * or simply is not assigned any value on this asset.
     *
     * @param setName the name of the custom metadata set from which to retrieve the attribute's value
     * @param attrName the name of the custom metadata attribute for which to retrieve the value
     * @return the value of that custom metadata attribute on this asset, or null if there is no value
     */
    @JsonIgnore
    public Object getCustomMetadata(String setName, String attrName) {
        if (customMetadataSets == null) {
            return null;
        } else if (!customMetadataSets.containsKey(setName)) {
            return null;
        } else {
            Map<String, Object> attrs = customMetadataSets.get(setName).getAttributes();
            if (!attrs.containsKey(attrName)) {
                return null;
            } else {
                return attrs.get(attrName);
            }
        }
    }

    /**
     * Reduce the asset to the minimum set of properties required to update it.
     *
     * @return a builder containing the minimal set of properties required to update this asset
     * @throws InvalidRequestException if any of the minimal set of required properties are not found in the initial object
     */
    public abstract AssetBuilder<?, ?> trimToRequired() throws InvalidRequestException;

    /**
     * Reduce the asset to the minimum set of properties required to relate to it.
     *
     * @return an asset containing the minimal set of properties required to relate to this asset
     * @throws InvalidRequestException if any of the minimal set of required properties are not found in the initial object
     */
    public abstract Asset trimToReference() throws InvalidRequestException;

    /**
     * If an asset with the same qualifiedName exists, updates the existing asset. Otherwise, creates the asset.
     * No Atlan tags or custom metadata will be changed if updating an existing asset, irrespective of what
     * is included in the asset itself when the method is called.
     *
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     * @deprecated see {@link #save()} instead
     */
    @Deprecated
    public AssetMutationResponse upsert() throws AtlanException {
        return save(Atlan.getDefaultClient());
    }

    /**
     * If an asset with the same qualifiedName exists, updates the existing asset. Otherwise, creates the asset.
     * No Atlan tags or custom metadata will be changed if updating an existing asset, irrespective of what
     * is included in the asset itself when the method is called.
     *
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     */
    public AssetMutationResponse save() throws AtlanException {
        return save(Atlan.getDefaultClient());
    }

    /**
     * If an asset with the same qualifiedName exists, updates the existing asset. Otherwise, creates the asset.
     * No Atlan tags or custom metadata will be changed if updating an existing asset, irrespective of what
     * is included in the asset itself when the method is called.
     *
     * @param client connectivity to the Atlan tenant on which to save the asset
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     */
    public AssetMutationResponse save(AtlanClient client) throws AtlanException {
        return client.assets.save(this, false);
    }

    /**
     * If no asset exists, has the same behavior as the {@link #save()} method.
     * If an asset does exist, optionally overwrites any Atlan tags. Custom metadata will always
     * be entirely ignored using this method.
     *
     * @param replaceAtlanTags whether to replace Atlan tags during an update (true) or not (false)
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     * @deprecated see {@link #save(boolean)} instead
     */
    @Deprecated
    public AssetMutationResponse upsert(boolean replaceAtlanTags)
            throws AtlanException {
        return save(Atlan.getDefaultClient(), replaceAtlanTags);
    }

    /**
     * If no asset exists, has the same behavior as the {@link #save()} method.
     * If an asset does exist, optionally overwrites any Atlan tags. Custom metadata will always
     * be entirely ignored using this method.
     *
     * @param replaceAtlanTags whether to replace Atlan tags during an update (true) or not (false)
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     */
    public AssetMutationResponse save(boolean replaceAtlanTags)
            throws AtlanException {
        return save(Atlan.getDefaultClient(), replaceAtlanTags);
    }

    /**
     * If no asset exists, has the same behavior as the {@link #save()} method.
     * If an asset does exist, optionally overwrites any Atlan tags. Custom metadata will always
     * be entirely ignored using this method.
     *
     * @param client connectivity to the Atlan tenant on which to save this asset
     * @param replaceAtlanTags whether to replace Atlan tags during an update (true) or not (false)
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     */
    public AssetMutationResponse save(AtlanClient client, boolean replaceAtlanTags)
            throws AtlanException {
        return client.assets.save(this, replaceAtlanTags);
    }

    /**
     * If no asset exists, has the same behavior as the {@link #save()} method, while also setting
     * any custom metadata provided.
     * If an asset does exist, optionally overwrites any Atlan tags.
     * Will merge any provided custom metadata with any custom metadata that already exists on the asset.
     *
     * @param replaceAtlanTags whether to replace AtlanTags during an update (true) or not (false)
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     * @deprecated see {@link #saveMergingCM(boolean)} instead
     */
    @Deprecated
    public AssetMutationResponse upsertMergingCM(boolean replaceAtlanTags)
            throws AtlanException {
        return saveMergingCM(Atlan.getDefaultClient(), replaceAtlanTags);
    }

    /**
     * If no asset exists, has the same behavior as the {@link #save()} method, while also setting
     * any custom metadata provided.
     * If an asset does exist, optionally overwrites any Atlan tags.
     * Will merge any provided custom metadata with any custom metadata that already exists on the asset.
     *
     * @param replaceAtlanTags whether to replace AtlanTags during an update (true) or not (false)
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     */
    public AssetMutationResponse saveMergingCM(boolean replaceAtlanTags)
            throws AtlanException {
        return saveMergingCM(Atlan.getDefaultClient(), replaceAtlanTags);
    }

    /**
     * If no asset exists, has the same behavior as the {@link #save()} method, while also setting
     * any custom metadata provided.
     * If an asset does exist, optionally overwrites any Atlan tags.
     * Will merge any provided custom metadata with any custom metadata that already exists on the asset.
     *
     * @param client connectivity to the Atlan tenant where this asset should be saved
     * @param replaceAtlanTags whether to replace AtlanTags during an update (true) or not (false)
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     */
    public AssetMutationResponse saveMergingCM(AtlanClient client, boolean replaceAtlanTags)
            throws AtlanException {
        return client.assets.saveMergingCM(List.of(this), replaceAtlanTags);
    }

    /**
     * If no asset exists, has the same behavior as the {@link #save()} method, while also setting
     * any custom metadata provided.
     * If an asset does exist, optionally overwrites any Atlan tags.
     * Will overwrite all custom metadata on any existing asset with only the custom metadata provided
     * (wiping out any other custom metadata on an existing asset that is not provided in the request).
     *
     * @param replaceAtlanTags whether to replace Atlan tags during an update (true) or not (false)
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     * @deprecated see {@link #saveReplacingCM(boolean)} instead
     */
    @Deprecated
    public AssetMutationResponse upsertReplacingCM(boolean replaceAtlanTags)
            throws AtlanException {
        return saveReplacingCM(Atlan.getDefaultClient(), replaceAtlanTags);
    }

    /**
     * If no asset exists, has the same behavior as the {@link #save()} method, while also setting
     * any custom metadata provided.
     * If an asset does exist, optionally overwrites any Atlan tags.
     * Will overwrite all custom metadata on any existing asset with only the custom metadata provided
     * (wiping out any other custom metadata on an existing asset that is not provided in the request).
     *
     * @param replaceAtlanTags whether to replace Atlan tags during an update (true) or not (false)
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     */
    public AssetMutationResponse saveReplacingCM(boolean replaceAtlanTags)
            throws AtlanException {
        return saveReplacingCM(Atlan.getDefaultClient(), replaceAtlanTags);
    }

    /**
     * If no asset exists, has the same behavior as the {@link #save()} method, while also setting
     * any custom metadata provided.
     * If an asset does exist, optionally overwrites any Atlan tags.
     * Will overwrite all custom metadata on any existing asset with only the custom metadata provided
     * (wiping out any other custom metadata on an existing asset that is not provided in the request).
     *
     * @param client connectivity to the Atlan tenant where this asset should be saved
     * @param replaceAtlanTags whether to replace Atlan tags during an update (true) or not (false)
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     */
    public AssetMutationResponse saveReplacingCM(AtlanClient client, boolean replaceAtlanTags)
            throws AtlanException {
        return client.assets.saveReplacingCM(List.of(this), replaceAtlanTags);
    }

    /**
     * If no asset exists, fails with a NotFoundException.
     * Will merge any provided custom metadata with any custom metadata that already exists on the asset.
     * If an asset does exist, optionally overwrites any Atlan tags.
     *
     * @param replaceAtlanTags whether to replace AtlanTags during an update (true) or not (false)
     * @return details of the updated asset
     * @throws AtlanException on any error during the API invocation
     * @throws com.atlan.exception.NotFoundException if the asset does not exist (will not create it)
     */
    public AssetMutationResponse updateMergingCM(boolean replaceAtlanTags) throws AtlanException {
        return updateMergingCM(Atlan.getDefaultClient(), replaceAtlanTags);
    }

    /**
     * If no asset exists, fails with a NotFoundException.
     * Will merge any provided custom metadata with any custom metadata that already exists on the asset.
     * If an asset does exist, optionally overwrites any Atlan tags.
     *
     * @param client connectivity to the Atlan tenant where this asset should be saved
     * @param replaceAtlanTags whether to replace AtlanTags during an update (true) or not (false)
     * @return details of the updated asset
     * @throws AtlanException on any error during the API invocation
     * @throws com.atlan.exception.NotFoundException if the asset does not exist (will not create it)
     */
    public AssetMutationResponse updateMergingCM(AtlanClient client, boolean replaceAtlanTags) throws AtlanException {
        // Attempt to retrieve the asset first, and allow this to throw a NotFoundException if it does not exist
        get(client, this.getTypeName(), this.getQualifiedName(), false);
        // Otherwise, attempt the update
        return saveReplacingCM(client, replaceAtlanTags);
    }

    /**
     * If no asset exists, fails with a NotFoundException.
     * Will overwrite all custom metadata on any existing asset with only the custom metadata provided
     * (wiping out any other custom metadata on an existing asset that is not provided in the request).
     * If an asset does exist, optionally overwrites any Atlan tags.
     *
     * @param replaceAtlanTags whether to replace Atlan tags during an update (true) or not (false)
     * @return details of the updated asset
     * @throws AtlanException on any error during the API invocation
     * @throws com.atlan.exception.NotFoundException if the asset does not exist (will not create it)
     */
    public AssetMutationResponse updateReplacingCM(boolean replaceAtlanTags) throws AtlanException {
        return updateReplacingCM(Atlan.getDefaultClient(), replaceAtlanTags);
    }

    /**
     * If no asset exists, fails with a NotFoundException.
     * Will overwrite all custom metadata on any existing asset with only the custom metadata provided
     * (wiping out any other custom metadata on an existing asset that is not provided in the request).
     * If an asset does exist, optionally overwrites any Atlan tags.
     *
     * @param client connectivity to the Atlan tenant where this asset should be saved
     * @param replaceAtlanTags whether to replace Atlan tags during an update (true) or not (false)
     * @return details of the updated asset
     * @throws AtlanException on any error during the API invocation
     * @throws com.atlan.exception.NotFoundException if the asset does not exist (will not create it)
     */
    public AssetMutationResponse updateReplacingCM(AtlanClient client, boolean replaceAtlanTags) throws AtlanException {
        // Attempt to retrieve the asset first, and allow this to throw a NotFoundException if it does not exist
        get(client, this.getTypeName(), this.getQualifiedName(), false);
        // Otherwise, attempt the update
        return saveReplacingCM(client, replaceAtlanTags);
    }

    /**
     * Start a fluent lineage request that will return all active downstream assets.
     * Additional conditions can be chained onto the returned builder before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) assets will be included.
     * (To change the default direction of downstream, chain a .direction() call.)
     *
     * @return a fluent lineage request that includes all active downstream assets
     */
    public FluentLineage.FluentLineageBuilder requestLineage() {
        return Asset.lineage(getGuid());
    }

    /**
     * Start a fluent lineage request that will return all active downstream assets.
     * Additional conditions can be chained onto the returned builder before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) assets will be included.
     * (To change the default direction of downstream, chain a .direction() call.)
     *
     * @param client connectivity to Atlan tenant
     * @return a fluent lineage request that includes all active downstream assets
     */
    public FluentLineage.FluentLineageBuilder requestLineage(AtlanClient client) {
        return Asset.lineage(client, getGuid());
    }

    /**
     * Add the API token configured for the default client as an admin to this object.
     *
     * @param assetGuid unique identifier (GUID) of the asset to which we should add this API token as an admin
     * @param impersonationToken a bearer token for an actual user who is already an admin for the object, NOT an API token
     * @throws AtlanException on any error during API invocation
     */
    protected static AssetMutationResponse addApiTokenAsAdmin(final String assetGuid, final String impersonationToken)
            throws AtlanException {

        AtlanClient client = Atlan.getDefaultClient();
        String token = client.users.getCurrentUser().getUsername();

        String clientGuid = UUID.randomUUID().toString();
        AtlanClient tmp = Atlan.getClient(client.getBaseUrl(), clientGuid);
        tmp.setApiToken(impersonationToken);

        // Look for the asset as the impersonated user, ensuring we include the admin users
        // in the results (so we avoid clobbering any existing admin users)
        Optional<Asset> found = tmp.assets.select().where(GUID.eq(assetGuid)).includeOnResults(ADMIN_USERS).pageSize(1).stream()
                .findFirst();
        AssetMutationResponse response = null;
        if (found.isPresent()) {
            Asset asset = found.get();
            Set<String> existingAdmins = asset.getAdminUsers();
            response = asset.trimToRequired()
                    .adminUsers(existingAdmins)
                    .adminUser(token)
                    .build()
                    .save(tmp);
        } else {
            Atlan.removeClient(client.getBaseUrl(), clientGuid);
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, assetGuid);
        }

        Atlan.removeClient(client.getBaseUrl(), clientGuid);

        return response;

    }

    /**
     * Retrieves an asset by its GUID, optionally complete with all of its relationships.
     * The type of the asset will only be determined at runtime.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the asset to retrieve
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full asset, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link com.atlan.exception.NotFoundException} if the asset does not exist
     */
    @JsonIgnore
    public static Asset get(AtlanClient client, String guid, boolean includeRelationships) throws AtlanException {
        AssetResponse response = client.assets.get(guid, !includeRelationships, !includeRelationships);
        Asset asset = response.getAsset();
        if (asset != null && includeRelationships) {
            asset.setCompleteObject();
        }
        return asset;
    }

    /**
     * Retrieves an asset by its GUID, complete with all of its relationships.
     * The type of the asset will only be determined at runtime.
     *
     * @param guid of the asset to retrieve
     * @return the requested full asset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link com.atlan.exception.NotFoundException} if the asset does not exist
     * @deprecated see {@link #get(AtlanClient, String, boolean)} instead
     */
    @Deprecated
    public static Asset retrieveFull(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid, true);
    }

    /**
     * Retrieves an asset by its GUID, complete with all of its relationships.
     * The type of the asset will only be determined at runtime.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the asset to retrieve
     * @return the requested full asset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link com.atlan.exception.NotFoundException} if the asset does not exist
     * @deprecated see {@link #get(AtlanClient, String, boolean)} instead
     */
    @Deprecated
    public static Asset retrieveFull(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid, true);
    }

    /**
     * Retrieves a minimal asset by its GUID, without its relationships.
     * The type of the asset will only be determined at runtime.
     *
     * @param guid of the asset to retrieve
     * @return the requested minimal asset, without its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link com.atlan.exception.NotFoundException} if the asset does not exist
     * @deprecated see {@link #get(AtlanClient, String, boolean)} instead
     */
    @Deprecated
    public static Asset retrieveMinimal(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid, false);
    }

    /**
     * Retrieves a minimal asset by its GUID, without its relationships.
     * The type of the asset will only be determined at runtime.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the asset to retrieve
     * @return the requested minimal asset, without its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link com.atlan.exception.NotFoundException} if the asset does not exist
     * @deprecated see {@link #get(AtlanClient, String, boolean)} instead
     */
    @Deprecated
    public static Asset retrieveMinimal(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid, false);
    }

    /**
     * Retrieves an asset by its qualifiedName, optionally complete with all of its relationships.
     * The type of the asset will only be determined at runtime.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param typeName the type of the asset to retrieve
     * @param qualifiedName the unique name of the asset to retrieve
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full asset, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link com.atlan.exception.NotFoundException} if the asset does not exist
     */
    @JsonIgnore
    public static Asset get(AtlanClient client, String typeName, String qualifiedName, boolean includeRelationships) throws AtlanException {
        AssetResponse response = client.assets.get(typeName, qualifiedName, !includeRelationships, !includeRelationships);
        Asset asset = response.getAsset();
        if (asset != null && includeRelationships) {
            asset.setCompleteObject();
        }
        return asset;
    }

    /**
     * Retrieves an asset by its qualifiedName, optionally complete with all of its relationships.
     * The type of the asset will only be determined at runtime.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param typeName the type of the asset to retrieve
     * @param qualifiedName the unique name of the asset to retrieve
     * @return the requested full asset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link com.atlan.exception.NotFoundException} if the asset does not exist
     * @deprecated see {@link #get(AtlanClient, String, boolean)} instead
     */
    @Deprecated
    protected static Asset retrieveFull(AtlanClient client, String typeName, String qualifiedName) throws AtlanException {
        return get(client, typeName, qualifiedName, true);
    }

    /**
     * Retrieves an asset by its qualifiedName, without its relationships.
     * The type of the asset will only be determined at runtime.
     *
     * @param typeName the type of the asset to retrieve
     * @param qualifiedName the unique name of the asset to retrieve
     * @return the requested minimal asset, without its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link com.atlan.exception.NotFoundException} if the asset does not exist
     * @deprecated see {@link #get(AtlanClient, String, String, boolean)} instead
     */
    @Deprecated
    public static Asset retrieveMinimal(String typeName, String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), typeName, qualifiedName, false);
    }

    /**
     * Retrieves an asset by its qualifiedName, without its relationships.
     * The type of the asset will only be determined at runtime.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param typeName the type of the asset to retrieve
     * @param qualifiedName the unique name of the asset to retrieve
     * @return the requested minimal asset, without its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link com.atlan.exception.NotFoundException} if the asset does not exist
     * @deprecated see {@link #get(AtlanClient, String, String, boolean)} instead
     */
    @Deprecated
    public static Asset retrieveMinimal(AtlanClient client, String typeName, String qualifiedName) throws AtlanException {
        return get(client, typeName, qualifiedName, false);
    }

    /**
     * Soft-deletes an asset by its GUID. This operation can be reversed by updating the asset and changing
     * its {@link #status} to {@code ACTIVE}.
     *
     * @param guid of the asset to soft-delete
     * @return details of the soft-deleted asset
     * @throws AtlanException on any error during the API invocation
     */
    public static AssetDeletionResponse delete(String guid) throws AtlanException {
        return delete(Atlan.getDefaultClient(), guid);
    }

    /**
     * Soft-deletes an asset by its GUID. This operation can be reversed by updating the asset and changing
     * its {@link #status} to {@code ACTIVE}.
     *
     * @param client connectivity to the Atlan tenant from which to delete the asset
     * @param guid of the asset to soft-delete
     * @return details of the soft-deleted asset
     * @throws AtlanException on any error during the API invocation
     */
    public static AssetDeletionResponse delete(AtlanClient client, String guid) throws AtlanException {
        return client.assets.delete(guid, AtlanDeleteType.SOFT);
    }

    /**
     * Hard-deletes (purges) an asset by its GUID. This operation is irreversible.
     *
     * @param guid of the asset to hard-delete
     * @return details of the hard-deleted asset
     * @throws AtlanException on any error during the API invocation
     */
    public static AssetDeletionResponse purge(String guid) throws AtlanException {
        return purge(Atlan.getDefaultClient(), guid);
    }

    /**
     * Hard-deletes (purges) an asset by its GUID. This operation is irreversible.
     *
     * @param client connectivity to the Atlan tenant from which to delete the asset
     * @param guid of the asset to hard-delete
     * @return details of the hard-deleted asset
     * @throws AtlanException on any error during the API invocation
     */
    public static AssetDeletionResponse purge(AtlanClient client, String guid) throws AtlanException {
        return client.assets.delete(guid, AtlanDeleteType.PURGE);
    }

    /**
     * Start a fluent lineage request that will return all active downstream assets.
     * Additional conditions can be chained onto the returned builder before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) assets will be included.
     * (To change the default direction of downstream, chain a .direction() call.)
     *
     * @param guid unique identifier (GUID) for the starting point of lineage
     * @return a fluent lineage request that includes all active downstream assets
     */
    public static FluentLineage.FluentLineageBuilder lineage(String guid) {
        return lineage(Atlan.getDefaultClient(), guid);
    }

    /**
     * Start a fluent lineage request that will return all active downstream assets.
     * Additional conditions can be chained onto the returned builder before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) assets will be included.
     * (To change the default direction of downstream, chain a .direction() call.)
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param guid unique identifier (GUID) for the starting point of lineage
     * @return a fluent lineage request that includes all active downstream assets
     */
    public static FluentLineage.FluentLineageBuilder lineage(AtlanClient client, String guid) {
        return lineage(client, guid, false);
    }

    /**
     * Start a fluent lineage request that will return all downstream assets.
     * Additional conditions can be chained onto the returned builder before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. (To change the default direction of downstream, chain a
     * .direction() call.)
     *
     * @param guid unique identifier (GUID) for the starting point of lineage
     * @param includeArchived when true, archived (soft-deleted) assets in lineage will be included
     * @return a fluent lineage request that includes all downstream assets
     */
    public static FluentLineage.FluentLineageBuilder lineage(String guid, boolean includeArchived) {
        return lineage(Atlan.getDefaultClient(), guid, includeArchived);
    }

    /**
     * Start a fluent lineage request that will return all downstream assets.
     * Additional conditions can be chained onto the returned builder before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. (To change the default direction of downstream, chain a
     * .direction() call.)
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the lineage
     * @param guid unique identifier (GUID) for the starting point of lineage
     * @param includeArchived when true, archived (soft-deleted) assets in lineage will be included
     * @return a fluent search that includes all downstream assets
     */
    public static FluentLineage.FluentLineageBuilder lineage(AtlanClient client, String guid, boolean includeArchived) {
        FluentLineage.FluentLineageBuilder builder =
            FluentLineage.builder(client, guid);
        if (!includeArchived) {
            builder.whereAsset(FluentLineage.ACTIVE)
                .whereRelationship(FluentLineage.ACTIVE)
                .includeInResults(FluentLineage.ACTIVE);
        }
        return builder;
    }

    /**
     * Update only the provided custom metadata attributes on the asset. This will leave all other custom metadata
     * attributes, even within the same named custom metadata, unchanged.
     *
     * @param guid unique identifier of the asset
     * @param cmName human-readable name of the custom metadata to update
     * @param attributes the values of the custom metadata attributes to change
     * @throws AtlanException on any API problems, or if the custom metadata is not defined in Atlan
     */
    public static void updateCustomMetadataAttributes(String guid, String cmName, CustomMetadataAttributes attributes)
            throws AtlanException {
        updateCustomMetadataAttributes(Atlan.getDefaultClient(), guid, cmName, attributes);
    }

    /**
     * Update only the provided custom metadata attributes on the asset. This will leave all other custom metadata
     * attributes, even within the same named custom metadata, unchanged.
     *
     * @param client connectivity to the Atlan tenant on which to update the asset's custom metadata attributes
     * @param guid unique identifier of the asset
     * @param cmName human-readable name of the custom metadata to update
     * @param attributes the values of the custom metadata attributes to change
     * @throws AtlanException on any API problems, or if the custom metadata is not defined in Atlan
     */
    public static void updateCustomMetadataAttributes(AtlanClient client, String guid, String cmName, CustomMetadataAttributes attributes)
            throws AtlanException {
        client.assets.updateCustomMetadataAttributes(guid, cmName, attributes);
    }

    /**
     * Replace specific custom metadata on the asset. This will replace everything within the named custom metadata,
     * but will not change any of the other named custom metadata on the asset.
     *
     * @param guid unique identifier of the asset
     * @param cmName human-readable name of the custom metadata to replace
     * @param attributes the values of the attributes to replace for the custom metadata
     * @throws AtlanException on any API problems, or if the custom metadata is not defined in Atlan
     */
    public static void replaceCustomMetadata(String guid, String cmName, CustomMetadataAttributes attributes)
            throws AtlanException {
        replaceCustomMetadata(Atlan.getDefaultClient(), guid, cmName, attributes);
    }

    /**
     * Replace specific custom metadata on the asset. This will replace everything within the named custom metadata,
     * but will not change any of the other named custom metadata on the asset.
     *
     * @param client connectivity to the Atlan tenant on which to replace the asset's custom metadata
     * @param guid unique identifier of the asset
     * @param cmName human-readable name of the custom metadata to replace
     * @param attributes the values of the attributes to replace for the custom metadata
     * @throws AtlanException on any API problems, or if the custom metadata is not defined in Atlan
     */
    public static void replaceCustomMetadata(AtlanClient client, String guid, String cmName, CustomMetadataAttributes attributes)
            throws AtlanException {
        client.assets.replaceCustomMetadata(guid, cmName, attributes);
    }

    /**
     * Remove specific custom metadata from an asset.
     *
     * @param guid unique identifier of the asset
     * @param cmName human-readable name of the custom metadata to remove
     * @throws AtlanException on any API problems, or if the custom metadata is not defined in Atlan
     */
    public static void removeCustomMetadata(String guid, String cmName) throws AtlanException {
        removeCustomMetadata(Atlan.getDefaultClient(), guid, cmName);
    }

    /**
     * Remove specific custom metadata from an asset.
     *
     * @param client connectivity to the Atlan tenant from which to remove the asset's custom metadata
     * @param guid unique identifier of the asset
     * @param cmName human-readable name of the custom metadata to remove
     * @throws AtlanException on any API problems, or if the custom metadata is not defined in Atlan
     */
    public static void removeCustomMetadata(AtlanClient client, String guid, String cmName) throws AtlanException {
        client.assets.removeCustomMetadata(guid, cmName);
    }

    /**
     * Add Atlan tags to an asset, without replacing existing Atlan tags linked to the asset.
     * Note: this operation must make two API calls — one to retrieve the asset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the asset
     * @param typeName type of the asset
     * @param qualifiedName of the asset
     * @param atlanTagNames human-readable names of the Atlan tags to append
     * @return the asset that was updated
     * @throws AtlanException on any API problems
     */
    protected static Asset appendAtlanTags(
            AtlanClient client, String typeName, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(client, typeName, qualifiedName, atlanTagNames, true, true, false);
    }

    /**
     * Add Atlan tags to an asset, without replacing existing Atlan tags linked to the asset.
     * Note: this operation must make two API calls — one to retrieve the asset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to add the Atlan tags to the asset
     * @param typeName type of the asset
     * @param qualifiedName of the asset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @return the asset that was updated
     * @throws AtlanException on any API problems
     */
    protected static Asset appendAtlanTags(
            AtlanClient client,
            String typeName,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {

        Asset existing = retrieveFull(client, typeName, qualifiedName);
        if (atlanTagNames == null) {
            return existing;
        } else if (existing != null) {
            Set<AtlanTag> replacementAtlanTags = new TreeSet<>();
            Set<AtlanTag> existingAtlanTags = existing.getAtlanTags();
            if (existingAtlanTags != null) {
                for (AtlanTag atlanTag : existingAtlanTags) {
                    if (existing.getGuid().equals(atlanTag.getEntityGuid())) {
                        // Only re-include Atlan tags that are directly assigned, and whose
                        // propagation settings are not being overridden by this update
                        // (Propagation overrides will be handled by the loop further below)
                        if (!atlanTagNames.contains(atlanTag.getTypeName())) {
                            replacementAtlanTags.add(atlanTag);
                        }
                    }
                }
            }
            // Append all the extra Atlan tags (including any propagation overrides)
            for (String atlanTagName : atlanTagNames) {
                replacementAtlanTags.add(AtlanTag.builder()
                        .typeName(atlanTagName)
                        .propagate(propagate)
                        .removePropagationsOnEntityDelete(removePropagationsOnDelete)
                        .restrictPropagationThroughLineage(restrictLineagePropagation)
                        .build());
            }
            AssetBuilder<?, ?> minimal = existing.trimToRequired();
            return replaceAtlanTags(client,
                    minimal.atlanTags(replacementAtlanTags).build());
        }
        return null;
    }

    /**
     * Add Atlan tags to an asset.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the asset
     * @param typeName type of the asset
     * @param qualifiedName of the asset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the asset
     * @deprecated see {@link #appendAtlanTags(AtlanClient, String, String, List)} instead
     */
    @Deprecated
    protected static void addAtlanTags(AtlanClient client, String typeName, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        client.assets.addAtlanTags(typeName, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to an asset.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the asset
     * @param typeName type of the asset
     * @param qualifiedName of the asset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the asset
     * @deprecated see {@link #appendAtlanTags(AtlanClient, String, String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    protected static void addAtlanTags(
            AtlanClient client,
            String typeName,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        client.assets.addAtlanTags(
                typeName,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from an asset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the Atlan tag from the asset
     * @param typeName type of the asset
     * @param qualifiedName of the asset
     * @param atlanTagName human-readable name of the Atlan tags to remove
     * @throws AtlanException on any API problems, or if any of the Atlan tag does not exist on the asset
     */
    protected static void removeAtlanTag(AtlanClient client, String typeName, String qualifiedName, String atlanTagName)
            throws AtlanException {
        client.assets.removeAtlanTag(typeName, qualifiedName, atlanTagName, true);
    }

    /**
     * Update the certificate on an asset.
     *
     * @param client connectivity to the Atlan tenant on which to update the asset's certificate
     * @param builder the builder to use for updating the certificate
     * @param certificate certificate to set
     * @param message (optional) message to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    protected static Asset updateCertificate(AtlanClient client, AssetBuilder<?, ?> builder, CertificateStatus certificate, String message)
            throws AtlanException {
        builder.certificateStatus(certificate);
        if (message != null && message.length() > 1) {
            builder.certificateStatusMessage(message);
        }
        return updateAttributes(client, builder.build());
    }

    /**
     * Remove the certificate on an asset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's certificate
     * @param builder the builder to use for removing the certificate
     * @return the result of the removal, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    protected static Asset removeCertificate(AtlanClient client, AssetBuilder<?, ?> builder) throws AtlanException {
        Asset asset = builder.removeCertificate().build();
        AssetMutationResponse response = asset.save(client);
        if (response != null && !response.getUpdatedAssets().isEmpty()) {
            return response.getUpdatedAssets().get(0);
        } else {
            return null;
        }
    }

    /**
     * Update the announcement on an asset.
     *
     * @param client connectivity to the Atlan tenant on which to update the asset's announcement
     * @param builder the builder to use for updating the announcement
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    protected static Asset updateAnnouncement(
            AtlanClient client, AssetBuilder<?, ?> builder, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        builder.announcementType(type);
        if (title != null && title.length() > 1) {
            builder.announcementTitle(title);
        }
        if (message != null && message.length() > 1) {
            builder.announcementMessage(message);
        }
        return updateAttributes(client, builder.build());
    }

    /**
     * Remove the announcement on an asset.
     *
     * @param client connectivity to the Atlan tenant from which to remove the asset's announcement
     * @param builder the builder to use for removing the announcement
     * @return the result of the removal, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    protected static Asset removeAnnouncement(AtlanClient client, AssetBuilder<?, ?> builder) throws AtlanException {
        Asset asset = builder.removeAnnouncement().build();
        AssetMutationResponse response = asset.save(client);
        if (response != null && !response.getUpdatedAssets().isEmpty()) {
            return response.getUpdatedAssets().get(0);
        } else {
            return null;
        }
    }

    /**
     * Remove the system description from an asset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param builder the builder to use for removing the description
     * @return the result of the removal, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    protected static Asset removeDescription(AtlanClient client, AssetBuilder<?, ?> builder) throws AtlanException {
        Asset asset = builder.removeDescription().build();
        AssetMutationResponse response = asset.save(client);
        if (response != null && !response.getUpdatedAssets().isEmpty()) {
            return response.getUpdatedAssets().get(0);
        } else {
            return null;
        }
    }

    /**
     * Remove the user-provided description from an asset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param builder the builder to use for removing the description
     * @return the result of the removal, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    protected static Asset removeUserDescription(AtlanClient client, AssetBuilder<?, ?> builder) throws AtlanException {
        Asset asset = builder.removeUserDescription().build();
        AssetMutationResponse response = asset.save(client);
        if (response != null && !response.getUpdatedAssets().isEmpty()) {
            return response.getUpdatedAssets().get(0);
        } else {
            return null;
        }
    }

    /**
     * Remove the owners from an asset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's owners
     * @param builder the builder to use for removing the owners
     * @return the result of the removal, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    protected static Asset removeOwners(AtlanClient client, AssetBuilder<?, ?> builder) throws AtlanException {
        Asset asset = builder.removeOwners().build();
        AssetMutationResponse response = asset.save(client);
        if (response != null && !response.getUpdatedAssets().isEmpty()) {
            return response.getUpdatedAssets().get(0);
        } else {
            return null;
        }
    }

    private static Asset updateAttributes(AtlanClient client, Asset asset) throws AtlanException {
        AssetMutationResponse response = client.assets.save(asset, false);
        if (response != null && !response.getUpdatedAssets().isEmpty()) {
            return response.getUpdatedAssets().get(0);
        }
        return null;
    }

    private static Asset replaceAtlanTags(AtlanClient client, Asset asset) throws AtlanException {
        AssetMutationResponse response = client.assets.save(asset, true);
        if (response != null && !response.getUpdatedAssets().isEmpty()) {
            return response.getUpdatedAssets().get(0);
        }
        return null;
    }

    /**
     * Update the certificate on an asset.
     *
     * @param client connectivity to the Atlan tenant on which to update the asset's certificate
     * @param builder the builder to use for updating the certificate
     * @param typeName type of the asset
     * @param qualifiedName for the asset
     * @param certificate certificate to set
     * @param message (optional) message to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    protected static Asset updateCertificate(
            AtlanClient client,
            AssetBuilder<?, ?> builder,
            String typeName,
            String qualifiedName,
            CertificateStatus certificate,
            String message)
            throws AtlanException {
        builder.qualifiedName(qualifiedName).certificateStatus(certificate);
        if (message != null && message.length() > 1) {
            builder.certificateStatusMessage(message);
        }
        return updateAttributes(client, typeName, qualifiedName, builder.build());
    }

    /**
     * Update the announcement on an asset.
     *
     * @param client connectivity to the Atlan tenant on which to update the asset's announcement
     * @param builder the builder to use for updating the announcement
     * @param typeName type of the asset
     * @param qualifiedName for the asset
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    protected static Asset updateAnnouncement(
            AtlanClient client,
            AssetBuilder<?, ?> builder,
            String typeName,
            String qualifiedName,
            AtlanAnnouncementType type,
            String title,
            String message)
            throws AtlanException {
        builder.qualifiedName(qualifiedName).announcementType(type);
        if (title != null && title.length() > 1) {
            builder.announcementTitle(title);
        }
        if (message != null && message.length() > 1) {
            builder.announcementMessage(message);
        }
        return updateAttributes(client, typeName, qualifiedName, builder.build());
    }

    private static Asset updateAttributes(AtlanClient client, String typeName, String qualifiedName, Asset asset) throws AtlanException {
        AssetMutationResponse response =
                client.assets.updateAttributes(typeName, qualifiedName, asset);
        if (response != null && !response.getPartiallyUpdatedAssets().isEmpty()) {
            return response.getPartiallyUpdatedAssets().get(0);
        }
        if (response != null && !response.getUpdatedAssets().isEmpty()) {
            return response.getUpdatedAssets().get(0);
        }
        return null;
    }

    /**
     * Restore an archived (soft-deleted) asset to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param typeName type of the asset to restore
     * @param qualifiedName of the asset to restore
     * @return true if the asset is now restored, or false if not
     * @throws AtlanException on any API problems
     */
    protected static boolean restore(AtlanClient client, String typeName, String qualifiedName) throws AtlanException {
        try {
            return restore(client, typeName, qualifiedName, 0);
        } catch (InterruptedException e) {
            throw new ApiException(ErrorCode.RETRIES_INTERRUPTED, e);
        }
    }

    /**
     * Restore an archived (soft-deleted) asset to active, retrying in case it is found to
     * already be active (since the delete handlers run asynchronously).
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param typeName type of the asset to restore
     * @param qualifiedName of the asset to restore
     * @param retryCount number of retries we have already attempted
     * @return true if the asset is now restored, or false if not
     * @throws AtlanException on any API problems
     * @throws InterruptedException if the retry cycle sleeps are interrupted
     */
    private static boolean restore(AtlanClient client, String typeName, String qualifiedName, int retryCount)
            throws AtlanException, InterruptedException {
        Asset existing = retrieveFull(client, typeName, qualifiedName);
        if (existing == null) {
            // Nothing to restore, so cannot be restored
            return false;
        } else if (existing.getStatus() == AtlanStatus.ACTIVE) {
            // Already active, but this could be due to the async nature of the delete handlers
            if (retryCount < Atlan.getMaxNetworkRetries()) {
                // So continue to retry up to the maximum number of allowed retries
                log.debug(
                        "Attempted to restore an active asset, retrying status check for async delete handling (attempt: {}).",
                        retryCount + 1);
                Thread.sleep(HttpClient.waitTime(retryCount).toMillis());
                return restore(client, typeName, qualifiedName, retryCount + 1);
            } else {
                // If we have exhausted the retries, though, then we should just short-circuit
                return true;
            }
        } else {
            Optional<String> guidRestored = restore(client, existing);
            return guidRestored.isPresent() && guidRestored.get().equals(existing.getGuid());
        }
    }

    /**
     * Replace the terms linked to an asset.
     *
     * @param client connectivity to the Atlan tenant on which to replace the asset's terms
     * @param builder the builder to use for updating the terms
     * @param terms the list of terms to replace on the asset, or null to remove all terms from an asset
     * @return the asset that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    protected static Asset replaceTerms(AtlanClient client, AssetBuilder<?, ?> builder, List<IGlossaryTerm> terms) throws AtlanException {
        if (terms == null || terms.isEmpty()) {
            Asset asset = builder.removeAssignedTerms().build();
            return updateRelationships(client, asset);
        } else {
            return updateRelationships(client, builder.assignedTerms(getTermRefs(terms)).build());
        }
    }

    /**
     * Link additional terms to an asset, without replacing existing terms linked to the asset.
     * Note: this operation must make two API calls — one to retrieve the asset's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the asset
     * @param typeName type of the asset
     * @param qualifiedName for the asset
     * @param terms the list of terms to append to the asset
     * @return the asset that was updated (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    protected static Asset appendTerms(AtlanClient client, String typeName, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        Asset existing = retrieveFull(client, typeName, qualifiedName);
        if (terms == null) {
            return existing;
        } else if (existing != null) {
            Set<IGlossaryTerm> replacementTerms = new TreeSet<>();
            Set<IGlossaryTerm> existingTerms = existing.getAssignedTerms();
            if (existingTerms != null) {
                for (IGlossaryTerm term : existingTerms) {
                    if (term.getRelationshipStatus() != AtlanStatus.DELETED) {
                        // Only re-include the terms that are not already deleted
                        replacementTerms.add(term);
                    }
                }
            }
            replacementTerms.addAll(terms);
            AssetBuilder<?, ?> minimal = existing.trimToRequired();
            return updateRelationships(client,
                    minimal.assignedTerms(getTermRefs(replacementTerms)).build());
        }
        return null;
    }

    /**
     * Remove terms from an asset, without replacing all existing terms linked to the asset.
     * Note: this operation must make two API calls — one to retrieve the asset's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant on which to remove terms from the asset
     * @param typeName type of the asset
     * @param qualifiedName for the asset
     * @param terms the list of terms to remove from the asset (note: these must be references by GUID
     *              in order to efficiently remove any existing terms)
     * @return the asset that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @throws InvalidRequestException if any of the passed terms are not valid references by GUID to a term
     */
    protected static Asset removeTerms(AtlanClient client, String typeName, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        Asset existing = retrieveFull(client, typeName, qualifiedName);
        if (existing != null) {
            Set<IGlossaryTerm> replacementTerms = new TreeSet<>();
            Set<IGlossaryTerm> existingTerms = existing.getAssignedTerms();
            Set<String> removeGuids = new HashSet<>();
            for (IGlossaryTerm term : terms) {
                if (term.isValidReferenceByGuid()) {
                    removeGuids.add(term.getGuid());
                } else {
                    throw new InvalidRequestException(ErrorCode.MISSING_TERM_GUID);
                }
            }
            for (IGlossaryTerm term : existingTerms) {
                String existingTermGuid = term.getGuid();
                if (!removeGuids.contains(existingTermGuid) && term.getRelationshipStatus() != AtlanStatus.DELETED) {
                    // Only re-include the terms that we are not removing and that are not already deleted
                    replacementTerms.add(term);
                }
            }
            AssetBuilder<?, ?> minimal = existing.trimToRequired();
            Asset update;
            if (replacementTerms.isEmpty()) {
                // If there are no terms left after the removal, we need to do the same as removing all terms
                update = minimal.removeAssignedTerms().build();
            } else {
                // Otherwise we should do the update with the difference
                update = minimal.assignedTerms(getTermRefs(replacementTerms)).build();
            }
            return updateRelationships(client, update);
        }
        return null;
    }

    private static Collection<IGlossaryTerm> getTermRefs(Collection<IGlossaryTerm> terms) {
        if (terms != null && !terms.isEmpty()) {
            Set<IGlossaryTerm> termRefs = new TreeSet<>();
            for (IGlossaryTerm term : terms) {
                if (term.getGuid() != null) {
                    termRefs.add(GlossaryTerm.refByGuid(term.getGuid()));
                } else if (term.getQualifiedName() != null) {
                    termRefs.add(GlossaryTerm.refByQualifiedName(term.getQualifiedName()));
                }
            }
            return termRefs;
        } else {
            return Collections.emptySet();
        }
    }

    private static Asset updateRelationships(AtlanClient client, Asset asset) throws AtlanException {
        String typeNameToUpdate = asset.getTypeName();
        AssetMutationResponse response = client.assets.save(asset, false);
        if (response != null && !response.getUpdatedAssets().isEmpty()) {
            for (Asset result : response.getUpdatedAssets()) {
                if (result.getTypeName().equals(typeNameToUpdate)) {
                    String foundQN = result.getQualifiedName();
                    if (foundQN != null && foundQN.equals(asset.getQualifiedName())) {
                        // Return the first result that matches both the type that we attempted to update
                        // and the qualifiedName of the asset we attempted to update. Irrespective of
                        // the kind of relationship, this should uniquely identify the asset that we
                        // attempted to update
                        return result;
                    }
                }
            }
        }
        return null;
    }

    private static Optional<String> restore(AtlanClient client, Asset asset) throws AtlanException {
        AssetMutationResponse response = client.assets.restore(asset);
        if (response != null && !response.getGuidAssignments().isEmpty()) {
            return response.getGuidAssignments().values().stream().findFirst();
        }
        return Optional.empty();
    }

    public abstract static class AssetBuilder<C extends Asset, B extends Asset.AssetBuilder<C, B>>
            extends Reference.ReferenceBuilder<C, B> {
        /** Remove the announcement from the asset, if any is set on the asset. */
        public B removeAnnouncement() {
            nullField("announcementType");
            nullField("announcementTitle");
            nullField("announcementMessage");
            return self();
        }

        /** Remove all custom metadata from the asset, if any is set on the asset. */
        public B removeCustomMetadata() {
            // It is sufficient to simply exclude businessAttributes from a request in order
            // for them to be removed, as long as the "replaceBusinessAttributes" flag is set
            // to true (which it must be for any update to work to businessAttributes anyway)
            clearCustomMetadataSets();
            return self();
        }

        /** Remove the Atlan tags from the asset, if the asset is classified with any. */
        public B removeAtlanTags() {
            // It is sufficient to simply exclude Atlan tags from a request in order
            // for them to be removed, as long as the "replaceAtlanTags" flag is set to
            // true (which it must be for any update to work to Atlan tags anyway)
            clearAtlanTags();
            clearAtlanTagNames();
            return self();
        }

        /** Remove the system description from the asset, if any is set on the asset. */
        public B removeDescription() {
            nullField("description");
            return self();
        }

        /** Remove the user's description from the asset, if any is set on the asset. */
        public B removeUserDescription() {
            nullField("userDescription");
            return self();
        }

        /** Remove the owners from the asset, if any are set on the asset. */
        public B removeOwners() {
            nullField("ownerUsers");
            nullField("ownerGroups");
            return self();
        }

        /** Remove the certificate from the asset, if any is set on the asset. */
        public B removeCertificate() {
            nullField("certificateStatus");
            nullField("certificateStatusMessage");
            return self();
        }

        /** Remove the linked terms from the asset, if any are set on the asset. */
        public B removeAssignedTerms() {
            nullField("assignedTerms");
            return self();
        }
    }
</#macro>
