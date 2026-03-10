// IMPORT: import java.util.ArrayList;

/**
     * Builds the minimal object necessary for creating a DataDomain.
     *
     * @param name of the DataDomain
     * @return the minimal request necessary to create the DataDomain, as a builder
     * @throws InvalidRequestException will never throw but required given signature of called method
     */
    public static DataDomainBuilder<?, ?> creator(String name) throws InvalidRequestException {
        return creator(name, null);
    }

    /**
     * Builds the minimal object necessary for creating a DataDomain.
     *
     * @param name of the DataDomain
     * @param parentDomainQualifiedName (optional) unique name of the data domain in which to create this subdomain
     * @return the minimal request necessary to create the DataDomain, as a builder
     */
    public static DataDomainBuilder<?, ?> creator(String name, String parentDomainQualifiedName)
            throws InvalidRequestException {
        DataDomainBuilder<?, ?> builder = DataDomain._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(name)
                .name(name);
        if (parentDomainQualifiedName != null) {
            builder.parentDomain(DataDomain.refByQualifiedName(parentDomainQualifiedName))
                    .parentDomainQualifiedName(parentDomainQualifiedName)
                    .superDomainQualifiedName(StringUtils.getSuperDomainQualifiedName(parentDomainQualifiedName));
        }
        return builder;
    }