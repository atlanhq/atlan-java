<#macro all subTypes>
    /**
     * Generate a unique name that does not include any path delimiters.
     *
     * @param name of the object for which to generate a unique name
     * @return a unique name for the object
     */
    @JsonIgnore
    public static String getSlugForName(String name) {
        return name.replaceAll("/", "±");
    }

    /**
     * Reverse a unique name without path delimiters back to the original name.
     *
     * @param slug unique name of the object for which to reverse back to its name
     * @return original name of the object
     */
    @JsonIgnore
    public static String getNameFromSlug(String slug) {
        return slug.replaceAll("±", "/");
    }

    /**
     * Find all model assets active at a particular business date.
     *
     * @param client connectivity to the Atlan tenant
     * @param businessDate time at which the model assets were active
     * @param prefix (optional) qualifiedName prefix to limit the model assets to fetch
     * @return all model assets active at the requested time
     * @throws AtlanException on any issues with underlying API interactions
     */
    public static List<Asset> findByTime(AtlanClient client, Date businessDate, String prefix) throws AtlanException {
        return findByTime(client, businessDate.toInstant(), prefix);
    }

    /**
     * Find all model assets active at a particular business date.
     *
     * @param client connectivity to the Atlan tenant
     * @param businessDate time at which the model assets were active
     * @param prefix (optional) qualifiedName prefix to limit the model assets to fetch
     * @return all model assets active at the requested time
     * @throws AtlanException on any issues with underlying API interactions
     */
    public static List<Asset> findByTime(AtlanClient client, Instant businessDate, String prefix) throws AtlanException {
        return findByTime(client, businessDate.toEpochMilli(), prefix);
    }

    /**
     * Find all model assets active at a particular business date.
     *
     * @param client connectivity to the Atlan tenant
     * @param businessDate time at which the model assets were active
     * @param prefix (optional) qualifiedName prefix to limit the model assets to fetch
     * @return all model assets active at the requested time
     * @throws AtlanException on any issues with underlying API interactions
     */
    public static List<Asset> findByTime(AtlanClient client, long businessDate, String prefix) throws AtlanException {
        Query subQuery = FluentSearch._internal()
            .whereSome(ModelDataModel.MODEL_EXPIRED_AT_BUSINESS_DATE.gt(businessDate))
            .whereSome(ModelDataModel.MODEL_EXPIRED_AT_BUSINESS_DATE.eq(0))
            .minSomes(1)
            .build()
            .toQuery();
        return client.assets
            .select()
            .includeOnResults(ModelAttribute.MODEL_BUSINESS_DATE)
            .includeOnResults(ModelDataModel.MODEL_EXPIRED_AT_BUSINESS_DATE)
            .includeOnResults(ModelAttribute.DESCRIPTION)
            .includeOnResults(ModelAttribute.MODEL_NAMESPACE)
            .includeOnResults(ModelAttribute.MODEL_ENTITY_QUALIFIED_NAME)
            .where(Asset.QUALIFIED_NAME.startsWith(prefix))
            .where(ModelDataModel.MODEL_BUSINESS_DATE.lte(businessDate))
            .where(subQuery)
            .stream()
            .toList();
    }
</#macro>
