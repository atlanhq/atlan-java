<#macro all>
    /**
     * Builds the minimal object necessary to create a DetectiDataDossier.
     *
     * @param name of the DetectiDataDossier
     * @param connectionQualifiedName unique name of the connection through which the DetectiDataDossier is accessible
     * @return the minimal object necessary to create the DetectiDataDossier, as a builder
     */
    public static DetectiDataDossierBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return DetectiDataDossier._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName));
    }

    /**
     * Builds the minimal object necessary to update a DetectiDataDossier.
     *
     * @param qualifiedName of the DetectiDataDossier
     * @param name of the DetectiDataDossier
     * @return the minimal request necessary to update the DetectiDataDossier, as a builder
     */
    public static DetectiDataDossierBuilder<?, ?> updater(String qualifiedName, String name) {
        return DetectiDataDossier._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique DetectiDataDossier name.
     *
     * @param name of the DetectiDataDossier
     * @param connectionQualifiedName unique name of the connection in which this DetectiDataDossier exists
     * @return a unique name for the DetectiDataDossier
     */
    public static String generateQualifiedName(String name, String connectionQualifiedName) {
        return connectionQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to apply an update to a DetectiDataDossier, from a potentially
     * more-complete DetectiDataDossier object.
     *
     * @return the minimal object necessary to update the DetectiDataDossier, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DetectiDataDossier are not found in the initial object
     */
    @Override
    public DetectiDataDossierBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
