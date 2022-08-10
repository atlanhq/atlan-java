package com.atlan.model.admin;

import com.atlan.net.AtlanObject;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class WorkflowTaskArguments extends AtlanObject {
    private static final long serialVersionUID = 2L;

    @Singular
    List<NameValuePair> parameters;
}
