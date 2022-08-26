package com.atlan.model.responses;

import com.atlan.model.admin.AtlanRoleJ;
import com.atlan.net.ApiResourceJ;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RoleResponseJ extends ApiResourceJ {
    private static final long serialVersionUID = 2L;

    Integer totalRecord;
    Integer filterRecord;
    List<AtlanRoleJ> records;
}
