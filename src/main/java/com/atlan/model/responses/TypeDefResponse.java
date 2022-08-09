package com.atlan.model.responses;

import com.atlan.model.typedefs.BusinessMetadataDef;
import com.atlan.model.typedefs.ClassificationDef;
import com.atlan.model.typedefs.TypeDef;
import com.atlan.net.ApiResource;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TypeDefResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    List<TypeDef> enumDefs;
    List<TypeDef> structDefs;
    List<ClassificationDef> classificationDefs;
    List<TypeDef> entityDefs;
    List<TypeDef> relationshipDefs;
    List<BusinessMetadataDef> businessMetadataDefs;
}
