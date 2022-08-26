package com.atlan.model.admin;

import com.atlan.net.AtlanObjectJ;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class WorkflowTaskArgumentsJ extends AtlanObjectJ {
    private static final long serialVersionUID = 2L;

    @Singular
    List<NameValuePairJ> parameters;
}
