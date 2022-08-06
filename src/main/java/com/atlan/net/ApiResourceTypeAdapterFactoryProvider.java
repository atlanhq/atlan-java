/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.net;

import com.atlan.model.serde.EmptyListTypeAdapterFactory;
import com.atlan.model.serde.EntityTypeAdapterFactory;
import com.atlan.model.serde.EntityXAdapterFactory;
import com.atlan.model.serde.RemovableTypeAdapterFactory;
import com.google.gson.TypeAdapterFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Provider for all {@link TypeAdapterFactory} required for deserializing subtypes of an interface.
 */
final class ApiResourceTypeAdapterFactoryProvider {
    private static final List<TypeAdapterFactory> factories = new ArrayList<>();

    static {
        factories.add(EmptyListTypeAdapterFactory.INSTANCE);
        factories.add(new RemovableTypeAdapterFactory());
        factories.add(new EntityXAdapterFactory());
        factories.add(new EntityTypeAdapterFactory());
        factories.add(new ReflectionCheckingTypeAdapterFactory());
    }

    public static List<TypeAdapterFactory> getAll() {
        return factories;
    }
}
