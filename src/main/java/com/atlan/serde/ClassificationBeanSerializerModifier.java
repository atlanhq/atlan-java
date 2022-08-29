/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.model.core.Classification;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

/**
 * This special Jackson extension allows us to reuse the default serialization of the {@link Classification} object,
 * since all we really change in serialization is the string value.
 */
@SuppressWarnings("unchecked")
public class ClassificationBeanSerializerModifier extends BeanSerializerModifier {

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonSerializer<?> modifySerializer(
            SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
        if (beanDesc.getBeanClass().equals(Classification.class)) {
            return new ClassificationSerializer((JsonSerializer<Classification>) serializer);
        }
        return serializer;
    }
}
