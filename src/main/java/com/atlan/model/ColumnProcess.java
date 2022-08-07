/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import com.atlan.model.relations.Reference;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a process in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class ColumnProcess extends AbstractProcess {

    public static final String TYPE_NAME = "ColumnProcess";

    /** Fixed typeName for column processes. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Parent process through which this column-level process runs. */
    @Attribute
    Reference process;
}
