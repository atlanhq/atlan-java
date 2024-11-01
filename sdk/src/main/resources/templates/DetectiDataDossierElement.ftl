<#macro all>
    /**
     * Builds the minimal object necessary to create a DetectiDataDossierElement.
     *
     * @param name of the DetectiDataDossierElement
     * @param dossier in which the DetectiDataDossierElement should be created, which must have at least
     *               a qualifiedName and name
     * @return the minimal request necessary to create the DetectiDataDossierElement, as a builder
     * @throws InvalidRequestException if the dossier provided is without any required attributes
     */
    public static DetectiDataDossierElementBuilder<?, ?> creator(String name, DetectiDataDossier dossier)
            throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", dossier.getQualifiedName());
        map.put("name", dossier.getName());
        validateRelationship(DetectiDataDossier.TYPE_NAME, map);
        return creator(
            name,
            dossier.getQualifiedName(),
            dossier.getName()
        ).detectiDataDossier(dossier.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a DetectiDataDossierElement.
     *
     * @param name of the DetectiDataDossierElement (must be unique within the dossier)
     * @param dossierQualifiedName unique name of the DetectiDataDossier in which the DetectiDataDossierElement exists
     * @param dossierName simple human-readable name of the DetectiDataDossier in which the DetectiDataDossierElement exists
     * @return the minimal object necessary to create the DetectiDataDossierElement, as a builder
     */
    public static DetectiDataDossierElementBuilder<?, ?> creator(
            String name, String dossierQualifiedName, String dossierName) {
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(dossierQualifiedName);
        return DetectiDataDossierElement._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(name, dossierQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName))
                .detectiDataDossierQualifiedName(dossierQualifiedName)
                .detectiDataDossierName(dossierName)
                .detectiDataDossier(DetectiDataDossier.refByQualifiedName(dossierQualifiedName));
    }

    /**
     * Generate a unique DetectiDataDossierElement name.
     *
     * @param name of the DetectiDataDossierElement
     * @param dossierQualifiedName unique name of the DetectiDataDossier in which this DetectiDataDossierElement exists
     * @return a unique name for the DetectiDataDossierElement
     */
    public static String generateQualifiedName(String name, String dossierQualifiedName) {
        return dossierQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a DetectiDataDossierElement.
     *
     * @param qualifiedName of the DetectiDataDossierElement
     * @param name of the DetectiDataDossierElement
     * @return the minimal request necessary to update the DetectiDataDossierElement, as a builder
     */
    public static DetectiDataDossierElementBuilder<?, ?> updater(String qualifiedName, String name) {
        return DetectiDataDossierElement._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DetectiDataDossierElement, from a potentially
     * more-complete DetectiDataDossierElement object.
     *
     * @return the minimal object necessary to update the DetectiDataDossierElement, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DetectiDataDossierElement are not found in the initial object
     */
    @Override
    public DetectiDataDossierElementBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
