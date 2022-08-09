package com.atlan.model.typedefs;

import com.atlan.net.AtlanObject;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TypeDef extends AtlanObject {
    private static final long serialVersionUID = 2L;

    String category;
    String guid;
    String createdBy;
    String updatedBy;
    Long createTime;
    Long updateTime;
    Long version;
    String name;
    String description;
    String typeVersion;
    List<AttributeDef> attributeDefs;
    String displayName;
}
