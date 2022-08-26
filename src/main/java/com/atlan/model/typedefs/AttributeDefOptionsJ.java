package com.atlan.model.typedefs;

import com.atlan.model.enums.AtlanCustomAttributePrimitiveType;
import com.atlan.net.AtlanObjectJ;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Options that can be set on each attribute within a type definition.
 */
@Getter
@Setter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class AttributeDefOptionsJ extends AtlanObjectJ {
    private static final long serialVersionUID = 2L;

    /**
     * Instantiate a new set of attribute options from the provided parameters.
     * @param type primitive type of the attribute
     * @param optionsName name of the options (enumeration) if the primitive type is an enumeration (can be null otherwise)
     * @return the attribute options
     */
    public static AttributeDefOptionsJ of(AtlanCustomAttributePrimitiveType type, String optionsName) {
        AttributeDefOptionsJBuilder<?, ?> builder =
                AttributeDefOptionsJ.builder().primitiveType(type);
        switch (type) {
            case USERS:
            case GROUPS:
            case URL:
            case SQL:
                builder = builder.customType(type.getValue());
                break;
            case OPTIONS:
                builder = builder.isEnum(true).enumType(optionsName);
                break;
            default:
                // do nothing
                break;
        }
        return builder.build();
    }

    /** Optional description of the attribute. */
    String description;

    /** Set of entities on which this attribute can be applied. */
    @Builder.Default
    String applicableEntityTypes = "[\"Asset\"]";

    /** Set of entities on which this attribute should appear. */
    @Builder.Default
    String customApplicableEntityTypes =
            "[\"Database\",\"Schema\",\"View\",\"Table\",\"TablePartition\",\"MaterialisedView\",\"Column\",\"TableauSite\",\"TableauProject\",\"TableauWorkbook\",\"TableauWorksheet\",\"TableauDashboard\",\"TableauDatasource\",\"TableauDatasourceField\",\"TableauCalculatedField\",\"TableauFlow\",\"TableauMetric\",\"PowerBIWorkspace\",\"PowerBIDashboard\",\"PowerBIReport\",\"PowerBIDataset\",\"PowerBIDataflow\",\"PowerBITile\",\"PowerBIPage\",\"PowerBIDatasource\",\"PowerBITable\",\"PowerBIColumn\",\"PowerBIMeasure\",\"LookerDashboard\",\"LookerExplore\",\"LookerField\",\"LookerFolder\",\"LookerLook\",\"LookerModel\",\"LookerProject\",\"LookerQuery\",\"LookerTile\",\"LookerView\",\"MetabaseCollection\",\"MetabaseDashboard\",\"MetabaseQuestion\",\"ModeWorkspace\",\"ModeCollection\",\"ModeReport\",\"ModeQuery\",\"ModeChart\",\"SalesforceOrganization\",\"SalesforceDashboard\",\"SalesforceReport\",\"SalesforceObject\",\"SalesforceField\",\"S3Bucket\",\"S3Object\",\"Query\",\"Folder\",\"Collection\",\"Process\",\"ColumnProcess\",\"BIProcess\",\"AtlasGlossary\",\"AtlasGlossaryTerm\",\"AtlasGlossaryCategory\"]";

    /** Whether the attribute should be searchable (true) or not (false). */
    @Builder.Default
    Boolean allowSearch = false;

    /** Maximum length allowed for a string value. */
    @Builder.Default
    String maxStrLength = "100000000";

    /** Whether this attribute should appear in the filterable facets of discovery (true) or not (false). */
    @Builder.Default
    Boolean allowFiltering = true;

    /** Whether this attribute can have multiple values (true) or only a single value (false). */
    @Builder.Default
    Boolean multiValueSelect = false;

    /** Whether users will see this attribute in the overview tab of the sidebar (true) or not (false). */
    @Builder.Default
    Boolean showInOverview = false;

    /** Whether the attribute is deprecated ("true") or not (null or "false"). */
    String isDeprecated;

    /** Primitive type of the attribute. */
    AtlanCustomAttributePrimitiveType primitiveType;

    /** Whether the attribute is an enumeration (true) or not (false). */
    Boolean isEnum;

    /** Name of the enumeration (options), when the attribute is an enumeration. */
    String enumType;

    /** Used for Atlan-specific types like {@code users}, {@code groups}, {@code url}, and {@code SQL}. */
    String customType;

    /** Whether the attribute has been deleted (true) or is still active (false). */
    final Boolean isArchived;

    /** When the attribute was deleted. */
    final Long archivedAt;

    /** User who deleted the attribute. */
    final String archivedBy;
}
