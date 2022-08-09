package com.atlan.model.typedefs;

import com.atlan.net.AtlanObject;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AttributeDef extends AtlanObject {
    private static final long serialVersionUID = 2L;

    String name;
    String typeName;
    Boolean isOptional;
    String cardinality;
    Long valuesMinCount;
    Long valuesMaxCount;
    Boolean isUnique;
    Boolean isIndexable;
    Boolean includeInNotification;
    Boolean skipScrubbing;
    Long searchWeight;
    String indexType;
    AttributeDefOptions options;
    String displayName;
    Map<String, String> indexTypeESConfig;
    Map<String, Map<String, String>> indexTypeESFields;
}
