<#macro imports>
import com.atlan.model.assets.Connection;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        return creator(name, hierarchy.getQualifiedName()).cubeHierarchy(hierarchy.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a nested CubeField.
     *
     * @param name of the CubeField
     * @param parentField in which the field should be created, which must have at least a qualifiedName
     * @return the minimal request necessary to create the CubeField, as a builder
     * @throws InvalidRequestException if the parent field provided is without a qualifiedName
     */
    public static CubeFieldBuilder<?, ?> creator(String name, CubeField parentField) throws InvalidRequestException {
        if (parentField.getQualifiedName() == null || parentField.getQualifiedName().isEmpty()) {
            throw new InvalidRequestException(ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, "CubeField", "qualifiedName");
        }
        return creator(name, parentField.getQualifiedName())
                .cubeParentField(parentField.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a CubeField.
     *
     * @param name of the CubeField
     * @param parentQualifiedName unique name of the parent of the CubeField (either of its hierarchy or parent field)
     * @return the minimal request necessary to create the CubeField, as a builder
     */
    public static CubeFieldBuilder<?, ?> creator(String name, String parentQualifiedName) {
        CubeFieldBuilder<?, ?> builder = CubeField._internal()
                .name(name)
                .qualifiedName(generateQualifiedName(name, parentQualifiedName));
        String hierarchyQualifiedName = getHierarchyQualifiedName(parentQualifiedName);
        if (!hierarchyQualifiedName.equals(parentQualifiedName)) {
            String parentSlug = StringUtils.getNameFromQualifiedName(parentQualifiedName);
            String parentName = IMultiDimensionalDataset.getNameFromSlug(parentSlug);
            builder.cubeParentField(CubeField.refByQualifiedName(parentQualifiedName))
                    .cubeParentFieldName(parentName)
                    .cubeParentFieldQualifiedName(parentQualifiedName);
        }
        String hierarchySlug = StringUtils.getNameFromQualifiedName(hierarchyQualifiedName);
        String hierarchyName = IMultiDimensionalDataset.getNameFromSlug(hierarchySlug);
        String dimensionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(hierarchyQualifiedName);
        String dimensionSlug = StringUtils.getNameFromQualifiedName(dimensionQualifiedName);
        String dimensionName = IMultiDimensionalDataset.getNameFromSlug(dimensionSlug);
        String cubeQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(dimensionQualifiedName);
        String cubeSlug = StringUtils.getNameFromQualifiedName(cubeQualifiedName);
        String cubeName = IMultiDimensionalDataset.getNameFromSlug(cubeSlug);
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(cubeQualifiedName);
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
     * Extracts the unique name of the hierarchy from the qualified name of the CubeField's parent.
     *
     * @param parentQualifiedName unique name of the hierarchy or parent field in which this CubeField exists
     * @return the unique name of the CubeHierarchy in which the field exists
     */
    public static String getHierarchyQualifiedName(String parentQualifiedName) {
        if (parentQualifiedName != null) {
            List<String> tokens = Arrays.stream(parentQualifiedName.split("/")).collect(Collectors.toList());
            return String.join("/", tokens.subList(0, 6));
        }
        return null;
    }

    /**
     * Generate a unique CubeField name.
     *
     * @param name of the CubeField
     * @param parentQualifiedName unique name of the hierarchy or parent field in which this CubeField exists
     * @return a unique name for the CubeField
     */
    public static String generateQualifiedName(String name, String parentQualifiedName) {
        return parentQualifiedName + "/" + IMultiDimensionalDataset.getSlugForName(name);
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
