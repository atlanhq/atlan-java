/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.model.admin.AbstractPolicy;
import com.atlan.model.admin.WrappedPolicy;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class WrappedPolicySerializer extends StdSerializer<WrappedPolicy> {
    private static final long serialVersionUID = 2L;

    public WrappedPolicySerializer() {
        this(null);
    }

    public WrappedPolicySerializer(Class<WrappedPolicy> t) {
        super(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(WrappedPolicy wrappedPolicy, JsonGenerator gen, SerializerProvider sp)
            throws IOException, JsonProcessingException {
        AbstractPolicy policy = wrappedPolicy.getPolicy();
        Serde.mapper.writeValue(gen, policy);
    }
}
