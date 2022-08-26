package com.atlan.serde;

import com.atlan.model.core.Classification;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

@SuppressWarnings("unchecked")
public class ClassificationBeanSerializerModifier extends BeanSerializerModifier {

    @Override
    public JsonSerializer<?> modifySerializer(
            SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
        if (beanDesc.getBeanClass().equals(Classification.class)) {
            return new ClassificationSerializer((JsonSerializer<Classification>) serializer);
        }
        return serializer;
    }
}
