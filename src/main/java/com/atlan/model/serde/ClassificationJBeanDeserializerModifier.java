package com.atlan.model.serde;

import com.atlan.model.core.ClassificationJ;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;

@SuppressWarnings("unchecked")
public class ClassificationJBeanDeserializerModifier extends BeanDeserializerModifier {

    @Override
    public JsonDeserializer<?> modifyDeserializer(
            DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
        if (beanDesc.getBeanClass().equals(ClassificationJ.class)) {
            return new ClassificationJDeserializer((JsonDeserializer<ClassificationJ>) deserializer);
        }
        return deserializer;
    }
}
