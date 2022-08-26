package com.atlan.model.serde;

import com.atlan.model.core.ClassificationJ;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

@SuppressWarnings("unchecked")
public class ClassificationJBeanSerializerModifier extends BeanSerializerModifier {

    @Override
    public JsonSerializer<?> modifySerializer(
            SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
        if (beanDesc.getBeanClass().equals(ClassificationJ.class)) {
            return new ClassificationJSerializer((JsonSerializer<ClassificationJ>) serializer);
        }
        return serializer;
    }
}
