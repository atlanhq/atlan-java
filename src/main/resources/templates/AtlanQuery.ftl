<#macro all>
    /**
     * Builds the minimal object necessary to update a AtlanQuery.
     *
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @param collectionQualifiedName qualifiedName of the parent collection the query is contained within
     * @param parentQualifiedName qualifiedName of the parent collection or folder the query is contained within
     * @return the minimal request necessary to update the AtlanQuery, as a builder
     */
    public static AtlanQueryBuilder<?, ?> updater(String qualifiedName, String name, String collectionQualifiedName, String parentQualifiedName) {
        INamespace parent;
        if (collectionQualifiedName.equals(parentQualifiedName)) {
            parent = AtlanCollection.refByQualifiedName(collectionQualifiedName);
        } else {
            parent = Folder.refByQualifiedName(parentQualifiedName);
        }
        return AtlanQuery._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name)
                .parent(parent)
                .parentQualifiedName(parentQualifiedName)
                .collectionQualifiedName(collectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to apply an update to a AtlanQuery, from a potentially
     * more-complete AtlanQuery object.
     *
     * @return the minimal object necessary to update the AtlanQuery, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AtlanQuery are not found in the initial object
     */
    @Override
    public AtlanQueryBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (this.getCollectionQualifiedName() == null || this.getCollectionQualifiedName().length() == 0) {
            missing.add("collectionQualifiedName");
        }
        if (this.getParentQualifiedName() == null || this.getParentQualifiedName().length() == 0) {
            missing.add("parentQualifiedName");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, TYPE_NAME, String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName(), this.getCollectionQualifiedName(), this.getParentQualifiedName());
    }

    /**
     * Find a query by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the query, if found.
     *
     * @param name of the query
     * @return all queries with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the query does not exist
     */
    public static List<AtlanQuery> findByName(String name) throws AtlanException {
        return findByName(name, (List<AtlanField>) null);
    }

    /**
     * Find a query by its human-readable name.
     *
     * @param name of the query
     * @param attributes an optional collection of attributes (unchecked) to retrieve for the query
     * @return all queries with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the query does not exist
     */
    public static List<AtlanQuery> findByName(String name, Collection<String> attributes)
            throws AtlanException {
        return findByName(Atlan.getDefaultClient(), name, attributes);
    }

    /**
     * Find a query by its human-readable name.
     *
     * @param name of the query
     * @param attributes an optional collection of attributes (checked) to retrieve for the query
     * @return all queries with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the query does not exist
     */
    public static List<AtlanQuery> findByName(String name, List<AtlanField> attributes)
            throws AtlanException {
        return findByName(Atlan.getDefaultClient(), name, attributes);
    }

    /**
     * Find a query by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the query, if found.
     *
     * @param client connectivity to the Atlan tenant in which to search for the query
     * @param name of the query
     * @return all queries with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the query does not exist
     */
    public static List<AtlanQuery> findByName(AtlanClient client, String name)
            throws AtlanException {
        return findByName(client, name, (List<AtlanField>) null);
    }

    /**
     * Find a query by its human-readable name.
     *
     * @param client connectivity to the Atlan tenant in which to search for the query
     * @param name of the query
     * @param attributes an optional collection of attributes to retrieve for the query
     * @return all queries with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the query does not exist
     */
    public static List<AtlanQuery> findByName(
            AtlanClient client, String name, Collection<String> attributes)
            throws AtlanException {
        List<AtlanQuery> results = new ArrayList<>();
        AtlanQuery.select(client)
                .where(NAME.eq(name))
                ._includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof AtlanQuery)
                .forEach(q -> results.add((AtlanQuery) q));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.QUERY_NOT_FOUND_BY_NAME, name);
        }
        return results;
    }

    /**
     * Find a query by its human-readable name.
     *
     * @param client connectivity to the Atlan tenant in which to search for the query
     * @param name of the query
     * @param attributes an optional collection of attributes (checked) to retrieve for the query
     * @return all queries with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the query does not exist
     */
    public static List<AtlanQuery> findByName(
            AtlanClient client, String name, List<AtlanField> attributes)
            throws AtlanException {
        List<AtlanQuery> results = new ArrayList<>();
        AtlanQuery.select(client)
                .where(NAME.eq(name))
                .includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof AtlanQuery)
                .forEach(q -> results.add((AtlanQuery) q));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.QUERY_NOT_FOUND_BY_NAME, name);
        }
        return results;
    }

    /**
     * Remove the system description from a AtlanQuery.
     *
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @param collectionQualifiedName qualifiedName of the AtlanQuery's collection
     * @param parentQualifiedName qualifiedName of the AtlanQuery's parent namespace
     * @return the updated AtlanQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery removeDescription(String qualifiedName, String name, String collectionQualifiedName, String parentQualifiedName)
            throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name, collectionQualifiedName, parentQualifiedName);
    }

    /**
     * Remove the system description from a AtlanQuery.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AtlanQuery's description
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @param collectionQualifiedName qualifiedName of the AtlanQuery's collection
     * @param parentQualifiedName qualifiedName of the AtlanQuery's parent namespace
     * @return the updated AtlanQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery removeDescription(AtlanClient client, String qualifiedName, String name, String collectionQualifiedName, String parentQualifiedName)
            throws AtlanException {
        return (AtlanQuery) Asset.removeDescription(client, updater(qualifiedName, name, collectionQualifiedName, parentQualifiedName));
    }

    /**
     * Remove the user's description from a AtlanQuery.
     *
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @param collectionQualifiedName qualifiedName of the AtlanQuery's collection
     * @param parentQualifiedName qualifiedName of the AtlanQuery's parent namespace
     * @return the updated AtlanQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery removeUserDescription(String qualifiedName, String name, String collectionQualifiedName, String parentQualifiedName)
            throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name, collectionQualifiedName, parentQualifiedName);
    }

    /**
     * Remove the user's description from a AtlanQuery.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AtlanQuery's description
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @param collectionQualifiedName qualifiedName of the AtlanQuery's collection
     * @param parentQualifiedName qualifiedName of the AtlanQuery's parent namespace
     * @return the updated AtlanQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery removeUserDescription(AtlanClient client, String qualifiedName, String name, String collectionQualifiedName, String parentQualifiedName)
            throws AtlanException {
        return (AtlanQuery) Asset.removeUserDescription(client, updater(qualifiedName, name, collectionQualifiedName, parentQualifiedName));
    }

    /**
     * Remove the owners from a AtlanQuery.
     *
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @param collectionQualifiedName qualifiedName of the AtlanQuery's collection
     * @param parentQualifiedName qualifiedName of the AtlanQuery's parent namespace
     * @return the updated AtlanQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery removeOwners(String qualifiedName, String name, String collectionQualifiedName, String parentQualifiedName)
            throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name, collectionQualifiedName, parentQualifiedName);
    }

    /**
     * Remove the owners from a AtlanQuery.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AtlanQuery's owners
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @param collectionQualifiedName qualifiedName of the AtlanQuery's collection
     * @param parentQualifiedName qualifiedName of the AtlanQuery's parent namespace
     * @return the updated AtlanQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery removeOwners(AtlanClient client, String qualifiedName, String name, String collectionQualifiedName, String parentQualifiedName)
            throws AtlanException {
        return (AtlanQuery) Asset.removeOwners(client, updater(qualifiedName, name, collectionQualifiedName, parentQualifiedName));
    }

    /**
     * Update the certificate on a AtlanQuery.
     *
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @param collectionQualifiedName qualifiedName of the AtlanQuery's collection
     * @param parentQualifiedName qualifiedName of the AtlanQuery's parent namespace
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AtlanQuery, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery updateCertificate(
            String qualifiedName, String name, String collectionQualifiedName, String parentQualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, name, collectionQualifiedName, parentQualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a AtlanQuery.
     *
     * @param client connectivity to the Atlan tenant on which to update the AtlanQuery's certificate
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @param collectionQualifiedName qualifiedName of the AtlanQuery's collection
     * @param parentQualifiedName qualifiedName of the AtlanQuery's parent namespace
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated AtlanQuery, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery updateCertificate(
            AtlanClient client, String qualifiedName, String name, String collectionQualifiedName, String parentQualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (AtlanQuery) Asset.updateCertificate(client, updater(qualifiedName, name, collectionQualifiedName, parentQualifiedName), certificate, message);
    }

    /**
     * Remove the certificate from a AtlanQuery.
     *
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @param collectionQualifiedName qualifiedName of the AtlanQuery's collection
     * @param parentQualifiedName qualifiedName of the AtlanQuery's parent namespace
     * @return the updated AtlanQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery removeCertificate(String qualifiedName, String name, String collectionQualifiedName, String parentQualifiedName)
            throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name, collectionQualifiedName, parentQualifiedName);
    }

    /**
     * Remove the certificate from a AtlanQuery.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AtlanQuery's certificate
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @param collectionQualifiedName qualifiedName of the AtlanQuery's collection
     * @param parentQualifiedName qualifiedName of the AtlanQuery's parent namespace
     * @return the updated AtlanQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery removeCertificate(AtlanClient client, String qualifiedName, String name, String collectionQualifiedName, String parentQualifiedName)
            throws AtlanException {
        return (AtlanQuery) Asset.removeCertificate(client, updater(qualifiedName, name, collectionQualifiedName, parentQualifiedName));
    }

    /**
     * Update the announcement on a AtlanQuery.
     *
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @param collectionQualifiedName qualifiedName of the AtlanQuery's collection
     * @param parentQualifiedName qualifiedName of the AtlanQuery's parent namespace
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the updated AtlanQuery, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery updateAnnouncement(
            String qualifiedName,
            String name,
            String collectionQualifiedName,
            String parentQualifiedName,
            AtlanAnnouncementType type,
            String title,
            String message)
            throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, name, collectionQualifiedName, parentQualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a AtlanQuery.
     *
     * @param client connectivity to the Atlan tenant on which to update the AtlanQuery's announcement
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @param collectionQualifiedName qualifiedName of the AtlanQuery's collection
     * @param parentQualifiedName qualifiedName of the AtlanQuery's parent namespace
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the updated AtlanQuery, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery updateAnnouncement(
            AtlanClient client,
            String qualifiedName,
            String name,
            String collectionQualifiedName,
            String parentQualifiedName,
            AtlanAnnouncementType type,
            String title,
            String message)
            throws AtlanException {
        return (AtlanQuery)
                Asset.updateAnnouncement(client, updater(qualifiedName, name, collectionQualifiedName, parentQualifiedName), type, title, message);
    }

    /**
     * Remove the announcement from a AtlanQuery.
     *
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @param collectionQualifiedName qualifiedName of the AtlanQuery's collection
     * @param parentQualifiedName qualifiedName of the AtlanQuery's parent namespace
     * @return the updated AtlanQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery removeAnnouncement(String qualifiedName, String name, String collectionQualifiedName, String parentQualifiedName)
            throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name, collectionQualifiedName, parentQualifiedName);
    }

    /**
     * Remove the announcement from a AtlanQuery.
     *
     * @param client connectivity to the Atlan tenant from which to remove the AtlanQuery's announcement
     * @param qualifiedName of the AtlanQuery
     * @param name of the AtlanQuery
     * @param collectionQualifiedName qualifiedName of the AtlanQuery's collection
     * @param parentQualifiedName qualifiedName of the AtlanQuery's parent namespace
     * @return the updated AtlanQuery, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static AtlanQuery removeAnnouncement(AtlanClient client, String qualifiedName, String name, String collectionQualifiedName, String parentQualifiedName)
            throws AtlanException {
        return (AtlanQuery) Asset.removeAnnouncement(client, updater(qualifiedName, name, collectionQualifiedName, parentQualifiedName));
    }
</#macro>
