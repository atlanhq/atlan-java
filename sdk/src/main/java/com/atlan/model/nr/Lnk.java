/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.nr;

import com.atlan.model.assets.Attribute;
import com.atlan.model.enums.IconType;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a link in Atlan.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class Lnk extends Ass {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Link";

    /** Fixed typeName for Links. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Asset to which the link is attached. */
    @JsonIgnore
    public Ass getAsset() {
        return getAttribute("asset", Ass.class);
    }

    /** URL to the resource. */
    @JsonIgnore
    public String getLink() {
        return getAttribute("link", String.class);
    }
}
