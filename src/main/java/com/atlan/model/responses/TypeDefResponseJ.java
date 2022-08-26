package com.atlan.model.responses;

import com.atlan.model.typedefs.*;
import com.atlan.net.ApiResourceJ;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Wrapper used for both requests and responses for type definitions.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TypeDefResponseJ extends ApiResourceJ {
    private static final long serialVersionUID = 2L;

    /** List of enumeration type definitions. */
    List<TypeDefJ> enumDefs;

    /** List of struct type definitions. */
    List<TypeDefJ> structDefs;

    /** List of classification type definitions. */
    List<ClassificationDefJ> classificationDefs;

    /** List of entity type definitions. */
    List<TypeDefJ> entityDefs;

    /** List of relationship type definitions. */
    List<TypeDefJ> relationshipDefs;

    /** List of custom metadata type definitions. */
    List<CustomMetadataDefJ> businessMetadataDefs;
}
