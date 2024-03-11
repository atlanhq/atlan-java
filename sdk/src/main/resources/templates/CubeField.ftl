<#macro imports>
import com.atlan.model.assets.Connection;
import java.util.Arrays;
</#macro>
<#macro all>
    /**
     * Builds the minimal object necessary to create a top-level CubeField.
     *
     * @param name of the CubeField
     * @param hierarchy in which the field should be created, which must have at least a qualifiedName
     * @return the minimal request necessary to create the CubeField, as a builder
     * @throws InvalidRequestException if the hierarchy provided is without a qualifiedName
     */
    public static CubeFieldBuilder<?, ?> creator(String name, CubeHierarchy hierarchy) throws InvalidRequestException {
        if (hierarchy.getQualifiedName() == null || hierarchy.getQualifiedName().isEmpty()) {
            throw new InvalidRequestException(ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, "CubeHierarchy", "qualifiedName");
        }
        return creator(name, hierarchy.getQualifiedName(), null).cubeHierarchy(hierarchy.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a nested CubeField.
     *
     * @param name of the CubeField
     * @param hierarchy in which the field should be created, which must have at least a qualifiedName
     * @param parentField in which the field should be created, which must have at least a qualifiedName
     * @return the minimal request necessary to create the CubeField, as a builder
     * @throws InvalidRequestException if the parent field provided is without a qualifiedName
     */
    public static CubeFieldBuilder<?, ?> creator(String name, CubeHierarchy hierarchy, CubeField parentField) throws InvalidRequestException {
        if (hierarchy.getQualifiedName() == null || hierarchy.getQualifiedName().isEmpty()) {
            throw new InvalidRequestException(ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, "CubeHierarchy", "qualifiedName");
        }
        if (parentField.getQualifiedName() == null || parentField.getQualifiedName().isEmpty()) {
            throw new InvalidRequestException(ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, "CubeField", "qualifiedName");
        }
        return creator(name, hierarchy.getQualifiedName(), parentField.getQualifiedName())
                .cubeHierarchy(hierarchy.trimToReference())
                .cubeParentField(parentField.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a CubeField.
     *
     * @param name of the CubeField
     * @param hierarchyQualifiedName unique name of the hierarchy in which this CubeField exists
     * @param parentFieldQualifiedName (optional) unique name of the parent field in which this CubeField exists
     * @return the minimal request necessary to create the CubeField, as a builder
     */
    public static CubeFieldBuilder<?, ?> creator(String name, String hierarchyQualifiedName, String parentFieldQualifiedName) {
        CubeFieldBuilder<?, ?> builder = CubeField._internal().name(name);
        if (parentFieldQualifiedName != null && !parentFieldQualifiedName.isEmpty()) {
            builder.qualifiedName(generateQualifiedName(name, parentFieldQualifiedName))
                    .cubeParentField(CubeField.refByQualifiedName(parentFieldQualifiedName));
        } else {
            builder.qualifiedName(generateQualifiedName(name, hierarchyQualifiedName));
        }
        String hierarchyName = StringUtils.getNameFromQualifiedName(hierarchyQualifiedName);
        String dimensionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(hierarchyQualifiedName);
        String dimensionName = StringUtils.getNameFromQualifiedName(dimensionQualifiedName);
        String cubeQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(dimensionQualifiedName);
        String cubeName = StringUtils.getNameFromQualifiedName(cubeQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(cubeQualifiedName);
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(connectionQualifiedName);
        return builder
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .cubeName(cubeName)
                .cubeQualifiedName(cubeQualifiedName)
                .cubeDimensionName(dimensionName)
                .cubeDimensionQualifiedName(dimensionQualifiedName)
                .cubeHierarchyName(hierarchyName)
                .cubeHierarchyQualifiedName(hierarchyQualifiedName)
                .cubeHierarchy(CubeHierarchy.refByQualifiedName(hierarchyQualifiedName))
                .connectorType(connectorType)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a CubeField.
     *
     * @param qualifiedName of the CubeField
     * @param name of the CubeField
     * @return the minimal request necessary to update the CubeField, as a builder
     */
    public static CubeFieldBuilder<?, ?> updater(String qualifiedName, String name) {
        return CubeField._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Generate a unique CubeField name.
     *
     * @param name of the CubeField
     * @param hierarchyQualifiedName unique name of the hierarchy in which this CubeField exists
     * @return a unique name for the CubeField
     */
    public static String generateQualifiedName(String name, String hierarchyQualifiedName) {
        return hierarchyQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to apply an update to a CubeField, from a potentially
     * more-complete CubeField object.
     *
     * @return the minimal object necessary to update the CubeField, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for CubeField are not found in the initial object
     */
    @Override
    public CubeFieldBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, TYPE_NAME, String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }
</#macro>
