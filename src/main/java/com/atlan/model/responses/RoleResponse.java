package com.atlan.model.responses;

import com.atlan.model.admin.AtlanRole;
import com.atlan.net.ApiResource;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class RoleResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    Integer totalRecord;
    Integer filterRecord;
    List<AtlanRole> records;
}
