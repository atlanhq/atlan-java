/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * SSO user associated with a SageMaker Unified Studio project.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class SageMakerUnifiedStudioSsoUser extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SageMakerUnifiedStudioSsoUser";

    /** Fixed typeName for SageMakerUnifiedStudioSsoUser. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Email address of the SSO user. */
    String smusSsoUserEmail;

    /** Role assigned to the SSO user within the project. */
    String smusSsoUserRole;

    /**
     * Quickly create a new SageMakerUnifiedStudioSsoUser.
     * @param smusSsoUserEmail Email address of the SSO user.
     * @param smusSsoUserRole Role assigned to the SSO user within the project.
     * @return a SageMakerUnifiedStudioSsoUser with the provided information
     */
    public static SageMakerUnifiedStudioSsoUser of(String smusSsoUserEmail, String smusSsoUserRole) {
        return SageMakerUnifiedStudioSsoUser.builder()
                .smusSsoUserEmail(smusSsoUserEmail)
                .smusSsoUserRole(smusSsoUserRole)
                .build();
    }

    public abstract static class SageMakerUnifiedStudioSsoUserBuilder<
                    C extends SageMakerUnifiedStudioSsoUser, B extends SageMakerUnifiedStudioSsoUserBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
