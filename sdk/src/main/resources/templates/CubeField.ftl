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
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", hierarchy.getConnectionQualifiedName());
        map.put("cubeName", hierarchy.getCubeName());
        map.put("cubeQualifiedName", hierarchy.getCubeQualifiedName());
        map.put("cubeDimensionName", hierarchy.getCubeDimensionName());
        map.put("cubeDimensionQualifiedName", hierarchy.getCubeDimensionQualifiedName());
        map.put("name", hierarchy.getName());
        map.put("qualifiedName", hierarchy.getQualifiedName());
        validateRelationship(CubeHierarchy.TYPE_NAME, map);
        return creator(
            name,
            hierarchy.getConnectionQualifiedName(),
            hierarchy.getCubeName(),
            hierarchy.getCubeQualifiedName(),
            hierarchy.getCubeDimensionName(),
            hierarchy.getCubeDimensionQualifiedName(),
            hierarchy.getName(),
            hierarchy.getQualifiedName(),
            null,
            null
        ).cubeHierarchy(hierarchy.trimToReference());
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
        Map<String, String> map = new HashMap<>();
        map.put("connectionQualifiedName", parentField.getConnectionQualifiedName());
        map.put("cubeName", parentField.getCubeName());
        map.put("cubeQualifiedName", parentField.getCubeQualifiedName());
        map.put("cubeDimensionName", parentField.getCubeDimensionName());
        map.put("cubeDimensionQualifiedName", parentField.getCubeDimensionQualifiedName());
        map.put("cubeHierarchyName", parentField.getCubeHierarchyName());
        map.put("cubeHierarchyQualifiedName", parentField.getCubeHierarchyQualifiedName());
        map.put("name", parentField.getName());
        map.put("qualifiedName", parentField.getQualifiedName());
        validateRelationship(CubeField.TYPE_NAME, map);
        return creator(
            name,
            parentField.getConnectionQualifiedName(),
            parentField.getCubeName(),
            parentField.getCubeQualifiedName(),
            parentField.getCubeDimensionName(),
            parentField.getCubeDimensionQualifiedName(),
            parentField.getCubeHierarchyName(),
            parentField.getCubeHierarchyQualifiedName(),
            parentField.getName(),
            parentField.getQualifiedName()
        ).cubeParentField(parentField.trimToReference());
    }

    /**
     * Builds the minimal object necessary to create a CubeField.
     *
     * @param name of the CubeField
     * @param parentQualifiedName unique name of the parent of the CubeField (either of its hierarchy or parent field)
     * @return the minimal request necessary to create the CubeField, as a builder
     */
    public static CubeFieldBuilder<?, ?> creator(String name, String parentQualifiedName) {
        String parentFieldName = null;
        String parentFieldQualifiedName = null;
        String hierarchyQualifiedName = getHierarchyQualifiedName(parentQualifiedName);
        if (!hierarchyQualifiedName.equals(parentQualifiedName)) {
            parentFieldQualifiedName = parentQualifiedName;
            String parentSlug = StringUtils.getNameFromQualifiedName(parentQualifiedName);
            parentFieldName = IMultiDimensionalDataset.getNameFromSlug(parentSlug);
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
        return creator(
            name,
            connectionQualifiedName,
            cubeName,
            cubeQualifiedName,
            dimensionName,
            dimensionQualifiedName,
            hierarchyName,
            hierarchyQualifiedName,
            parentFieldName,
            parentFieldQualifiedName
        );
    }

    /**
     * Builds the minimal object necessary to create a CubeField.
     *
     * @param name of the CubeField
     * @param connectionQualifiedName unique name of the connection in which the CubeField should be created
     * @param cubeName simple name of the Cube in which the CubeField should be created
     * @param cubeQualifiedName unique name of the Cube in which the CubeField should be created
     * @param dimensionName simple name of the CubeDimension in which the CubeField should be created
     * @param dimensionQualifiedName unique name of the CubeDimension in which the CubeField should be created
     * @param hierarchyName simple name of the CubeHierarchy in which the CubeField should be created
     * @param hierarchyQualifiedName unique name of the CubeHierarchy in which the CubeField should be created
     * @param parentFieldName simple name of the CubeField in which the CubeField should be nested
     * @param parentFieldQualifiedName unique name of the CubeField in which the CubeField should be nested
     * @return the minimal request necessary to create the CubeField, as a builder
     */
    public static CubeFieldBuilder<?, ?> creator(
        String name,
        String connectionQualifiedName,
        String cubeName,
        String cubeQualifiedName,
        String dimensionName,
        String dimensionQualifiedName,
        String hierarchyName,
        String hierarchyQualifiedName,
        String parentFieldName,
        String parentFieldQualifiedName
    ) {
        CubeFieldBuilder<?, ?> builder =
            CubeField._internal().name(name);
        if (parentFieldName != null && parentFieldQualifiedName != null) {
            builder.cubeParentField(CubeField.refByQualifiedName(parentFieldQualifiedName))
                .cubeParentFieldName(parentFieldName)
                .cubeParentFieldQualifiedName(parentFieldQualifiedName)
                .qualifiedName(generateQualifiedName(name, parentFieldQualifiedName));
        } else {
            builder.qualifiedName(generateQualifiedName(name, hierarchyQualifiedName));
        }
        return builder.guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
            .cubeName(cubeName)
            .cubeQualifiedName(cubeQualifiedName)
            .cubeDimensionName(dimensionName)
            .cubeDimensionQualifiedName(dimensionQualifiedName)
            .cubeHierarchyName(hierarchyName)
            .cubeHierarchyQualifiedName(hierarchyQualifiedName)
            .cubeHierarchy(CubeHierarchy.refByQualifiedName(hierarchyQualifiedName))
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
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
            extends ${parentClassName}.${parentClassName}Builder<C, B> {}
</#macro>
