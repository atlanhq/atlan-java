package com.atlan.model.typedefs;

import com.atlan.api.TypeDefsEndpointJ;
import com.atlan.cache.CustomMetadataCacheJ;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.responses.TypeDefResponseJ;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Structural definition of custom metadata.
 */
@Getter
@Setter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class CustomMetadataDefJ extends TypeDefJ {
    private static final long serialVersionUID = 2L;

    /** Fixed category for custom metadata typedefs. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    AtlanTypeCategory category = AtlanTypeCategory.BUSINESS_METADATA;

    /** Options for the custom metadata. */
    BusinessMetadataOptionsJ options;

    /**
     * Builds the minimal object necessary to create a custom metadata definition.
     * To continue adding to the object, call {@link #toBuilder()} on the result and continue calling additional
     * methods to add metadata followed by {@link CustomMetadataDefJBuilder#build()}.
     * Note: without any enrichment, this will create a custom metadata set with no attributes. This is valid,
     * but probably not useful for anything!
     *
     * @param displayName the human-readable name for the custom metadata set
     * @return the minimal request necessary to create the custom metadata typedef
     */
    public static CustomMetadataDefJ toCreate(String displayName) {
        return CustomMetadataDefJ.builder()
                .name(displayName)
                .displayName(displayName)
                .build();
    }

    /**
     * Create this custom metadata definition in Atlan.
     * @return the result of the creation, or null if the creation failed
     * @throws AtlanException on any API communication issues
     */
    public CustomMetadataDefJ create() throws AtlanException {
        TypeDefResponseJ response = TypeDefsEndpointJ.createTypeDef(this);
        if (response != null && !response.getBusinessMetadataDefs().isEmpty()) {
            return response.getBusinessMetadataDefs().get(0);
        }
        return null;
    }

    /**
     * Hard-deletes (purges) a custom metadata definition by its human-readable name. This operation is irreversible.
     * If there are any existing uses of the custom metadata on an asset, this operation will fail.
     *
     * @param displayName human-readable name of the custom metadata definition
     * @throws AtlanException on any error during the API invocation
     */
    public static void purge(String displayName) throws AtlanException {
        String internalName = CustomMetadataCacheJ.getIdForName(displayName);
        if (internalName != null) {
            TypeDefsEndpointJ.purgeTypeDef(internalName);
        } else {
            throw new InvalidRequestException(
                    "Unable to find a custom metadata definition with the name: " + displayName,
                    "name",
                    "",
                    "ATLAN-CLIENT-400-012",
                    400,
                    null);
        }
    }
}
