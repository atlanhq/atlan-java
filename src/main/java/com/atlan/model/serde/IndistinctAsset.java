package com.atlan.model.serde;

import com.atlan.model.Asset;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Instance of an asset where we cannot determine (have not yet modeled) its detailed information.
 * In the meanwhile, this provides a catch-all case where at least the basic asset information is
 * available.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class IndistinctAsset extends Asset {
    private static final long serialVersionUID = 2L;

    /** Create a non-transient typeName to ensure it is included in serde. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;
}
