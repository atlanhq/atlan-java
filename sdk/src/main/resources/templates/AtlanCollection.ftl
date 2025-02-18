<#macro all>
    /**
     * Add the API token configured for the default client as an admin for this AtlanCollection.
     * This is necessary to allow the API token to manage the collection itself or any queries within it.
     *
     * @param client connectivity to the Atlan tenant
     * @param impersonationToken a bearer token for an actual user who is already an admin for the AtlanCollection, NOT an API token
     * @throws AtlanException on any error during API invocation
     */
    public AssetMutationResponse addApiTokenAsAdmin(AtlanClient client, final String impersonationToken) throws AtlanException {
        return Asset.addApiTokenAsAdmin(client, getGuid(), impersonationToken);
    }

    /**
     * Add the API token configured for the default client as a viewer for this AtlanCollection.
     * This is necessary to allow the API token to view or run queries within the collection, but not make any
     * changes to them.
     *
     * @param client connectivity to Atlan tenant
     * @param impersonationToken a bearer token for an actual user who is already an admin for the AtlanCollection, NOT an API token
     * @throws AtlanException on any error during API invocation
     */
    public AssetMutationResponse addApiTokenAsViewer(AtlanClient client, final String impersonationToken) throws AtlanException {
        String username = client.users.getCurrentUser().getUsername();
        AssetMutationResponse response = null;
        try (AtlanClient tmp = new AtlanClient(client.getBaseUrl(), impersonationToken)) {
            // Look for the asset as the impersonated user, ensuring we include the viewer users
            // in the results (so we avoid clobbering any existing viewer users)
            Optional<Asset> found = tmp.assets.select().where(GUID.eq(getGuid())).includeOnResults(VIEWER_USERS).stream()
                .findFirst();
            response = null;
            if (found.isPresent()) {
                Asset asset = found.get();
                Set<String> existingViewers = asset.getViewerUsers();
                response = asset.trimToRequired()
                    .viewerUsers(existingViewers)
                    .viewerUser(username)
                    .build()
                    .save(tmp);
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, getGuid());
            }
        } catch (Exception e) {
            log.warn("Unable to remove temporary client using impersonationToken.", e);
        }
        return response;
    }

    /**
     * Builds the minimal object necessary to create an AltanCollection.
     *
     * @param client connectivity to the Atlan tenant
     * @param name of the AtlanCollection as the user who will own the AtlanCollection
     * @return the minimal request necessary to create the AtlanCollection, as a builder
     */
    public static AtlanCollectionBuilder<?, ?> creator(AtlanClient client, String name) {
        return AtlanCollection._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(name)
                .qualifiedName(generateQualifiedName(client));
    }

    /**
     * Generate a unique AltanCollection name.
     *
     * @param client connectivity to the Atlan tenant as the user who will own the AtlanCollection
     * @return a unique name for the AltanCollection
     */
    public static String generateQualifiedName(AtlanClient client) {
        try {
            String username = client.users.getCurrentUser().getUsername();
            return "default/collection/" + username + "/" + UUID.randomUUID();
        } catch (AtlanException e) {
            log.error("Unable to determine the current user.", e);
        }
        return null;
    }

    /**
     * If an asset with the same qualifiedName exists, updates the existing asset. Otherwise, creates the asset.
     * No Atlan tags or custom metadata will be changed if updating an existing asset, irrespective of what
     * is included in the asset itself when the method is called.
     *
     * @param client connectivity to the Atlan tenant where this collection should be saved
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     */
    @Override
    public AsyncCreationResponse save(AtlanClient client) throws AtlanException {
        return client.assets.save(this);
    }

    /**
     * If no asset exists, has the same behavior as the {@link #save(AtlanClient)} method.
     * If an asset does exist, optionally overwrites any Atlan tags. Custom metadata will always
     * be entirely ignored using this method.
     *
     * @param client connectivity to the Atlan tenant where this collection should be saved
     * @param replaceAtlanTags whether to replace Atlan tags during an update (true) or not (false)
     * @return details of the created or updated asset
     * @throws AtlanException on any error during the API invocation
     * @deprecated see {@link #save(AtlanClient)}
     */
    @Deprecated
    @Override
    public AsyncCreationResponse save(AtlanClient client, boolean replaceAtlanTags) throws AtlanException {
        return client.assets.save(this, replaceAtlanTags);
    }

    /**
     * Builds the minimal object necessary to update a AtlanCollection.
     *
     * @param qualifiedName of the AtlanCollection
     * @param name of the AtlanCollection
     * @return the minimal request necessary to update the AtlanCollection, as a builder
     */
    public static AtlanCollectionBuilder<?, ?> updater(String qualifiedName, String name) {
        return AtlanCollection._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AtlanCollection, from a potentially
     * more-complete AtlanCollection object.
     *
     * @return the minimal object necessary to update the AtlanCollection, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AtlanCollection are not found in the initial object
     */
    @Override
    public AtlanCollectionBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Find a collection by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the collection, if found.
     *
     * @param client connectivity to the Atlan tenant in which to search for the collection
     * @param name of the collection
     * @return all collections with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the collection does not exist
     */
    public static List<AtlanCollection> findByName(AtlanClient client, String name)
            throws AtlanException {
        return findByName(client, name, (List<AtlanField>) null);
    }

    /**
     * Find a collection by its human-readable name.
     *
     * @param client connectivity to the Atlan tenant in which to search for the collection
     * @param name of the collection
     * @param attributes an optional collection of attributes to retrieve for the collection
     * @return all collections with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the collection does not exist
     */
    public static List<AtlanCollection> findByName(
            AtlanClient client, String name, Collection<String> attributes)
            throws AtlanException {
        List<AtlanCollection> results = new ArrayList<>();
        AtlanCollection.select(client)
                .where(NAME.eq(name))
                ._includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof AtlanCollection)
                .forEach(c -> results.add((AtlanCollection) c));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.COLLECTION_NOT_FOUND_BY_NAME, name);
        }
        return results;
    }

    /**
     * Find a collection by its human-readable name.
     *
     * @param client connectivity to the Atlan tenant in which to search for the collection
     * @param name of the collection
     * @param attributes an optional collection of attributes (checked) to retrieve for the collection
     * @return all collections with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the collection does not exist
     */
    public static List<AtlanCollection> findByName(
            AtlanClient client, String name, List<AtlanField> attributes)
            throws AtlanException {
        List<AtlanCollection> results = new ArrayList<>();
        AtlanCollection.select(client)
                .where(NAME.eq(name))
                .includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof AtlanCollection)
                .forEach(c -> results.add((AtlanCollection) c));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.COLLECTION_NOT_FOUND_BY_NAME, name);
        }
        return results;
    }
</#macro>
