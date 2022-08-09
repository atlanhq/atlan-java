package com.atlan.model.typedefs;

import java.util.List;

public class ClassificationDef extends TypeDef {
    private static final long serialVersionUID = 2L;

    ClassificationOptions options;
    List<String> superTypes;
    List<String> entityTypes;
    List<String> subTypes;
}
