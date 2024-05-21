<#macro all>
    /**
     * Add the API token configured for the default client as an admin for this AtlanCollection.
     * This is necessary to allow the API token to manage the collection itself or any queries within it.
     *
     * @param impersonationToken a bearer token for an actual user who is already an admin for the AtlanCollection, NOT an API token
     * @throws AtlanException on any error during API invocation
     */
    public AssetMutationResponse addApiTokenAsAdmin(final String impersonationToken) throws AtlanException {
        return Asset.addApiTokenAsAdmin(getGuid(), impersonationToken);
    }

    /**
     * Add the API token configured for the default client as a viewer for this AtlanCollection.
     * This is necessary to allow the API token to view or run queries within the collection, but not make any
     * changes to them.
     *
     * @param impersonationToken a bearer token for an actual user who is already an admin for the AtlanCollection, NOT an API token
     * @throws AtlanException on any error during API invocation
     */
    public AssetMutationResponse addApiTokenAsViewer(final String impersonationToken) throws AtlanException {

        AtlanClient client = Atlan.getDefaultClient();
        String token = client.users.getCurrentUser().getUsername();

        String clientGuid = UUID.randomUUID().toString();
        AtlanClient tmp = Atlan.getClient(client.getBaseUrl(), clientGuid);
        tmp.setApiToken(impersonationToken);

        // Look for the asset as the impersonated user, ensuring we include the viewer users
        // in the results (so we avoid clobbering any existing viewer users)
        Optional<Asset> found = tmp.assets.select().where(GUID.eq(getGuid())).includeOnResults(VIEWER_USERS).stream()
                .findFirst();
        AssetMutationResponse response = null;
        if (found.isPresent()) {
            Asset asset = found.get();
            Set<String> existingViewers = asset.getViewerUsers();
            response = asset.trimToRequired()
                    .viewerUsers(existingViewers)
                    .viewerUser(token)
                    .build()
                    .save(tmp);
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, getGuid());
        }

        Atlan.removeClient(client.getBaseUrl(), clientGuid);

        return response;
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
        validateRequired(TYPE_NAME, Map.of(
            "qualifiedName", this.getQualifiedName(),
            "name", this.getName()
        ));
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Find a collection by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the collection, if found.
     *
     * @param name of the collection
     * @return all collections with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the collection does not exist
     */
    public static List<AtlanCollection> findByName(String name) throws AtlanException {
        return findByName(name, (List<AtlanField>) null);
    }

    /**
     * Find a collection by its human-readable name.
     *
     * @param name of the collection
     * @param attributes an optional collection of attributes (unchecked) to retrieve for the collection
     * @return all collections with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the collection does not exist
     */
    public static List<AtlanCollection> findByName(String name, Collection<String> attributes)
            throws AtlanException {
        return findByName(Atlan.getDefaultClient(), name, attributes);
    }

    /**
     * Find a collection by its human-readable name.
     *
     * @param name of the collection
     * @param attributes an optional collection of attributes (checked) to retrieve for the collection
     * @return all collections with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the collection does not exist
     */
    public static List<AtlanCollection> findByName(String name, List<AtlanField> attributes)
            throws AtlanException {
        return findByName(Atlan.getDefaultClient(), name, attributes);
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
