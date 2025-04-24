/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.AtlanClient;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.CacheProvider;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializerCache;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public class ClientAwareDeserializationContext extends DefaultDeserializationContext {
    private static final long serialVersionUID = 2L;

    private final transient AtlanClient client;

    public ClientAwareDeserializationContext(DeserializerFactory df, DeserializerCache cache, AtlanClient client) {
        super(df, cache);
        this.client = client;
    }

    private ClientAwareDeserializationContext(ClientAwareDeserializationContext src, DeserializerFactory factory) {
        super(src, factory);
        this.client = src.client;
    }

    private ClientAwareDeserializationContext(ClientAwareDeserializationContext src) {
        super(src);
        this.client = src.client;
    }

    private ClientAwareDeserializationContext(
            ClientAwareDeserializationContext src,
            DeserializationConfig config,
            JsonParser p,
            InjectableValues values) {
        super(src, config, p, values);
        this.client = src.client;
    }

    private ClientAwareDeserializationContext(ClientAwareDeserializationContext src, DeserializationConfig config) {
        super(src, config);
        this.client = src.client;
    }

    @Override
    public DefaultDeserializationContext withCaches(CacheProvider cacheProvider) {
        return this;
    }

    @Override
    public DefaultDeserializationContext with(DeserializerFactory factory) {
        return new ClientAwareDeserializationContext(this, factory);
    }

    @Override
    public DefaultDeserializationContext copy() {
        ClassUtil.verifyMustOverride(ClientAwareDeserializationContext.class, this, "copy");
        return new ClientAwareDeserializationContext(this);
    }

    @Override
    public DefaultDeserializationContext createInstance(
            DeserializationConfig config, JsonParser p, InjectableValues values) {
        return new ClientAwareDeserializationContext(this, config, p, values);
    }

    @Override
    public DefaultDeserializationContext createDummyInstance(DeserializationConfig config) {
        // need to be careful to create "real", not blue-print, instance
        return new ClientAwareDeserializationContext(this, config);
    }

    @SuppressWarnings("unchecked")
    @Override
    public JsonDeserializer<Object> deserializerInstance(Annotated ann, Object deserDef) throws JsonMappingException {
        if (deserDef == null) {
            return null;
        }
        JsonDeserializer<?> deser;

        if (deserDef instanceof JsonDeserializer) {
            deser = (JsonDeserializer<?>) deserDef;
        } else {
            // Alas, there's no way to force return type of "either class
            // X or Y" -- need to throw an exception after the fact
            if (!(deserDef instanceof Class)) {
                throw new IllegalStateException("AnnotationIntrospector returned deserializer definition of type "
                        + deserDef.getClass().getName()
                        + "; expected type JsonDeserializer or Class<JsonDeserializer> instead");
            }
            Class<?> deserClass = (Class<?>) deserDef;
            // there are some known "no class" markers to consider too:
            if (deserClass == JsonDeserializer.None.class || ClassUtil.isBogusClass(deserClass)) {
                return null;
            }
            if (!JsonDeserializer.class.isAssignableFrom(deserClass)) {
                throw new IllegalStateException("AnnotationIntrospector returned Class " + deserClass.getName()
                        + "; expected Class<JsonDeserializer>");
            }
            HandlerInstantiator hi = _config.getHandlerInstantiator();
            deser = (hi == null) ? null : hi.deserializerInstance(_config, ann, deserClass);
            if (deser == null) {
                Constructor<?> ctor = findClientAwareConstructor(deserClass, _config.canOverrideAccessModifiers());
                if (ctor == null) {
                    // If no client-aware constructor can be found, fallback to the default no-arg constructor
                    deser = (JsonDeserializer<?>)
                            ClassUtil.createInstance(deserClass, _config.canOverrideAccessModifiers());
                } else {
                    // Otherwise, use the client-aware constructor to create the serializer
                    try {
                        deser = (JsonDeserializer<?>) ctor.newInstance(client);
                    } catch (Exception e) {
                        ClassUtil.unwrapAndThrowAsIAE(
                                e,
                                "Failed to instantiate class " + deserClass.getName() + ", problem: " + e.getMessage());
                        return null;
                    }
                }
            }
        }
        // First: need to resolve
        if (deser instanceof ResolvableDeserializer) {
            ((ResolvableDeserializer) deser).resolve(this);
        }
        return (JsonDeserializer<Object>) deser;
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
