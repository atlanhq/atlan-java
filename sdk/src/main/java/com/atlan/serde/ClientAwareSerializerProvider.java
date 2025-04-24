/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.AtlanClient;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.CacheProvider;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public class ClientAwareSerializerProvider extends DefaultSerializerProvider {
    private static final long serialVersionUID = 2L;

    private final transient AtlanClient client;

    public ClientAwareSerializerProvider(AtlanClient client) {
        super();
        this.client = client;
    }

    public ClientAwareSerializerProvider(ClientAwareSerializerProvider src) {
        super(src);
        this.client = src.client;
    }

    protected ClientAwareSerializerProvider(
            SerializerProvider src, SerializationConfig config, SerializerFactory f, AtlanClient client) {
        super(src, config, f);
        this.client = client;
    }

    @Override
    public DefaultSerializerProvider withCaches(CacheProvider cacheProvider) {
        return this;
    }

    @Override
    public DefaultSerializerProvider createInstance(SerializationConfig config, SerializerFactory jsf) {
        return new ClientAwareSerializerProvider(this, config, jsf, client);
    }

    @Override
    public DefaultSerializerProvider copy() {
        if (getClass() != ClientAwareSerializerProvider.class) {
            return super.copy();
        }
        return new ClientAwareSerializerProvider(this);
    }

    @Override
    public JsonSerializer<Object> serializerInstance(Annotated annotated, Object serDef) throws JsonMappingException {
        if (serDef == null) {
            return null;
        }
        JsonSerializer<?> ser;

        if (serDef instanceof JsonSerializer) {
            ser = (JsonSerializer<?>) serDef;
        } else {
            // Alas, there's no way to force return type of "either class
            // X or Y" -- need to throw an exception after the fact
            if (!(serDef instanceof Class)) {
                reportBadDefinition(
                        annotated.getType(),
                        "AnnotationIntrospector returned serializer definition of type "
                                + serDef.getClass().getName()
                                + "; expected type JsonSerializer or Class<JsonSerializer> instead");
            }
            Class<?> serClass = (Class<?>) serDef;
            // there are some known "no class" markers to consider too:
            if (serClass == JsonSerializer.None.class || ClassUtil.isBogusClass(serClass)) {
                return null;
            }
            if (!JsonSerializer.class.isAssignableFrom(serClass)) {
                reportBadDefinition(
                        annotated.getType(),
                        "AnnotationIntrospector returned Class " + serClass.getName()
                                + "; expected Class<JsonSerializer>");
            }
            HandlerInstantiator hi = _config.getHandlerInstantiator();
            ser = (hi == null) ? null : hi.serializerInstance(_config, annotated, serClass);
            if (ser == null) {
                // TODO: find client-constructor and create; or fallback if not available
                Constructor<?> ctor = findClientAwareConstructor(serClass, _config.canOverrideAccessModifiers());
                if (ctor == null) {
                    // If no client-aware constructor can be found, fallback to the default no-arg constructor
                    ser = (JsonSerializer<?>) ClassUtil.createInstance(serClass, _config.canOverrideAccessModifiers());
                } else {
                    // Otherwise, use the client-aware constructor to create the serializer
                    try {
                        ser = (JsonSerializer<?>) ctor.newInstance(client);
                    } catch (Exception e) {
                        ClassUtil.unwrapAndThrowAsIAE(
                                e,
                                "Failed to instantiate class " + serClass.getName() + ", problem: " + e.getMessage());
                        return null;
                    }
                }
            }
        }
        return _handleResolvable(ser);
    }

    private static <T> Constructor<T> findClientAwareConstructor(Class<T> cls, boolean forceAccess) {
        try {
            Constructor<T> ctor = cls.getDeclaredConstructor(AtlanClient.class);
            if (forceAccess) {
                ClassUtil.checkAndFixAccess(ctor, forceAccess);
            } else {
                // Has to be public...
                if (!Modifier.isPublic(ctor.getModifiers())) {
                    throw new IllegalArgumentException(
                            "Default constructor for " + cls.getName()
                                    + " is not accessible (non-public?): not allowed to try modify access via Reflection: cannot instantiate type");
                }
            }
            return ctor;
        } catch (NoSuchMethodException e) {
            ;
        } catch (Exception e) {
            ClassUtil.unwrapAndThrowAsIAE(
                    e, "Failed to find default constructor of class " + cls.getName() + ", problem: " + e.getMessage());
        }
        return null;
    }
}
