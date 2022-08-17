package com.atlan.model.responses;

import com.atlan.model.typedefs.ClassificationDef;
import com.atlan.model.typedefs.CustomMetadataDef;
import com.atlan.model.typedefs.TypeDef;
import com.atlan.net.ApiResource;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Wrapper used for both requests and responses for type definitions.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TypeDefResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    /** List of enumeration type definitions. */
    List<TypeDef> enumDefs;

    /** List of struct type definitions. */
    List<TypeDef> structDefs;

    /** List of classification type definitions. */
    List<ClassificationDef> classificationDefs;

    /** List of entity type definitions. */
    List<TypeDef> entityDefs;

    /** List of relationship type definitions. */
    List<TypeDef> relationshipDefs;

    /** List of custom metadata type definitions. */
    List<CustomMetadataDef> businessMetadataDefs;
}
