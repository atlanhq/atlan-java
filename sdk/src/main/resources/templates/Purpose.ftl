<#macro all>
    /**
     * Builds the minimal object necessary to create a Purpose.
     *
     * @param name of the Purpose
     * @param atlanTags Atlan tags on which this purpose should be applied
     * @return the minimal request necessary to create the Purpose, as a builder
     * @throws InvalidRequestException if at least one Atlan tag is not specified
     */
    public static PurposeBuilder<?, ?> creator(String name, Collection<String> atlanTags) throws InvalidRequestException {
        if (atlanTags == null || atlanTags.isEmpty()) {
            throw new InvalidRequestException(ErrorCode.NO_ATLAN_TAG_FOR_PURPOSE);
        }
        return Purpose._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(name)
                .name(name)
                .displayName(name)
                .isAccessControlEnabled(true)
                .description("")
                .purposeAtlanTags(atlanTags);
    }

    /**
     * Builds the minimal object necessary to update a Purpose.
     *
     * @param qualifiedName of the Purpose
     * @param name of the Purpose
     * @param isEnabled whether the Purpose should be activated (true) or deactivated (false)
     * @return the minimal request necessary to update the Purpose, as a builder
     */
    public static PurposeBuilder<?, ?> updater(String qualifiedName, String name, boolean isEnabled) {
        return Purpose._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name)
                .isAccessControlEnabled(isEnabled);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Purpose, from a potentially
     * more-complete Purpose object.
     *
     * @return the minimal object necessary to update the Purpose, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for Purpose are not found in the initial object
     */
    @Override
    public PurposeBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        if (this.getIsAccessControlEnabled() == null) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, TYPE_NAME, "isAccessControlEnabled");
        }
        return updater(this.getQualifiedName(), this.getName(), this.getIsAccessControlEnabled());
    }

    /**
     * Find a Purpose by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the purpose, if found.
     *
     * @param client connectivity to the Atlan tenant in which to search for the purpose
     * @param name of the Purpose
     * @return all Purposes with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the Purpose does not exist
     */
    public static List<Purpose> findByName(AtlanClient client, String name) throws AtlanException {
        return findByName(client, name, (List<AtlanField>) null);
    }

    /**
     * Find a Purpose by its human-readable name.
     *
     * @param client connectivity to the Atlan tenant in which to search for the purpose
     * @param name of the Purpose
     * @param attributes an optional collection of attributes (unchecked) to retrieve for the Purpose
     * @return all Purposes with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the Purpose does not exist
     */
    public static List<Purpose> findByName(AtlanClient client, String name, Collection<String> attributes)
            throws AtlanException {
        List<Purpose> results = new ArrayList<>();
        Purpose.select(client)
                .where(Purpose.NAME.eq(name))
                ._includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof Purpose)
                .forEach(p -> results.add((Purpose) p));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.PURPOSE_NOT_FOUND_BY_NAME, name);
        }
        return results;
    }

    /**
     * Find a Purpose by its human-readable name.
     *
     * @param client connectivity to the Atlan tenant in which to search for the Purpose
     * @param name of the Purpose
     * @param attributes an optional list of attributes (checked) to retrieve for the Purpose
     * @return all Purposes with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the Purpose does not exist
     */
    public static List<Purpose> findByName(AtlanClient client, String name, List<AtlanField> attributes)
            throws AtlanException {
        List<Purpose> results = new ArrayList<>();
        Purpose.select(client)
                .where(Purpose.NAME.eq(name))
                .includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof Purpose)
                .forEach(p -> results.add((Purpose) p));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.PURPOSE_NOT_FOUND_BY_NAME, name);
        }
        return results;
    }

    /**
     * Builds the minimal object necessary to create a metadata policy for a Purpose.
     *
     * @param client connectivity to the Atlan tenant on which the policy is intended to be created
     * @param name of the policy
     * @param purposeId unique identifier (GUID) of the purpose for which to create this metadata policy
     * @param policyType type of policy (for example allow vs deny)
     * @param actions to include in the policy
     * @param policyGroups groups to whom this policy applies, given as internal group names (at least one of these or policyUsers must be specified)
     * @param policyUsers users to whom this policy applies, given as usernames (at least one of these or policyGroups must be specified)
     * @param allUsers whether to apply this policy to all users (true) or not (false). If true this will override the other users and groups parameters.
     * @return the minimal request necessary to create the metadata policy for the Purpose, as a builder
     * @throws AtlanException on any other error related to the request, such as an inability to find the specified users or groups
     */
    public static AuthPolicy.AuthPolicyBuilder<?, ?> createMetadataPolicy(
        AtlanClient client,
        String name,
        String purposeId,
        AuthPolicyType policyType,
        Collection<PurposeMetadataAction> actions,
        Collection<String> policyGroups,
        Collection<String> policyUsers,
        boolean allUsers)
        throws AtlanException {
        boolean targetFound = false;
        AuthPolicy.AuthPolicyBuilder<?, ?> builder = AuthPolicy.creator(name)
                .policyActions(actions)
                .policyCategory(AuthPolicyCategory.PURPOSE)
                .policyType(policyType)
                .policyResourceCategory(AuthPolicyResourceCategory.TAG)
                .policyServiceName("atlas_tag")
                .policySubCategory("metadata")
                .accessControl(Purpose.refByGuid(purposeId));
        if (allUsers) {
            targetFound = true;
            builder.policyGroup("public");
        } else {
            if (policyGroups != null && !policyGroups.isEmpty()) {
                for (String groupName : policyGroups) {
                    client.getGroupCache().getIdForName(groupName);
                }
                targetFound = true;
                builder.policyGroups(policyGroups);
            } else {
                builder.nullField("policyGroups");
            }
            if (policyUsers != null && !policyUsers.isEmpty()) {
                for (String userName : policyUsers) {
                    client.getUserCache().getIdForName(userName);
                }
                targetFound = true;
                builder.policyUsers(policyUsers);
            } else {
                builder.nullField("policyUsers");
            }
        }
        if (targetFound) {
            return builder;
        } else {
            throw new InvalidRequestException(ErrorCode.NO_USERS_FOR_POLICY);
        }
    }

    /**
     * Builds the minimal object necessary to create a data policy for a Purpose.
     *
     * @param client connectivity to the Atlan tenant on which the policy is intended to be created
     * @param name of the policy
     * @param purposeId unique identifier (GUID) of the purpose for which to create this data policy
     * @param policyType type of policy (for example allow vs deny)
     * @param policyGroups groups to whom this policy applies, given as internal group names (at least one of these or policyUsers must be specified)
     * @param policyUsers users to whom this policy applies, given as usernames (at least one of these or policyGroups must be specified)
     * @param allUsers whether to apply this policy to all users (true) or not (false). If true this will override the other users and groups parameters.
     * @return the minimal request necessary to create the data policy for the Purpose, as a builder
     * @throws AtlanException on any other error related to the request, such as an inability to find the specified users or groups
     */
    public static AuthPolicy.AuthPolicyBuilder<?, ?> createDataPolicy(
        AtlanClient client,
        String name,
        String purposeId,
        AuthPolicyType policyType,
        Collection<String> policyGroups,
        Collection<String> policyUsers,
        boolean allUsers)
        throws AtlanException {
        boolean targetFound = false;
        AuthPolicy.AuthPolicyBuilder<?, ?> builder = AuthPolicy.creator(name)
                .policyAction(DataAction.SELECT)
                .policyCategory(AuthPolicyCategory.PURPOSE)
                .policyType(policyType)
                .policyResourceCategory(AuthPolicyResourceCategory.TAG)
                .policyServiceName("atlas_tag")
                .policySubCategory("data")
                .accessControl(Purpose.refByGuid(purposeId));
        if (allUsers) {
            targetFound = true;
            builder.policyGroup("public");
        } else {
            if (policyGroups != null && !policyGroups.isEmpty()) {
                for (String groupName : policyGroups) {
                    client.getGroupCache().getIdForName(groupName);
                }
                targetFound = true;
                builder.policyGroups(policyGroups);
            } else {
                builder.nullField("policyGroups");
            }
            if (policyUsers != null && !policyUsers.isEmpty()) {
                for (String userName : policyUsers) {
                    client.getUserCache().getIdForName(userName);
                }
                targetFound = true;
                builder.policyUsers(policyUsers);
            } else {
                builder.nullField("policyUsers");
            }
        }
        if (targetFound) {
            return builder;
        } else {
            throw new InvalidRequestException(ErrorCode.NO_USERS_FOR_POLICY);
        }
    }

    /**
     * Remove the system description from a ${className}.
     *
     * @param client connectivity to the Atlan tenant from which to remove this ${className}'s description
     * @param qualifiedName of the ${className}
     * @param name of the ${className}
     * @param isEnabled whether the Purpose should be activated (true) or deactivated (false)
     * @return the updated ${className}, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ${className} removeDescription(AtlanClient client, String qualifiedName, String name, boolean isEnabled) throws AtlanException {
        return (${className}) Asset.removeDescription(client, updater(qualifiedName, name, isEnabled));
    }

    /**
     * Remove the user's description from a ${className}.
     *
     * @param client connectivity to the Atlan tenant from which to remove this ${className}'s description
     * @param qualifiedName of the ${className}
     * @param name of the ${className}
     * @param isEnabled whether the Purpose should be activated (true) or deactivated (false)
     * @return the updated ${className}, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ${className} removeUserDescription(AtlanClient client, String qualifiedName, String name, boolean isEnabled) throws AtlanException {
        return (${className}) Asset.removeUserDescription(client, updater(qualifiedName, name, isEnabled));
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
