/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

/**
 * Unique name of an object in Atlan in a fully constructable way,
 * without relying on any specifics of an Atlan tenant.
 */
public interface ObjectName {
    /**
     * String representation of the object's unique name.
     * @return String
     */
    @Override
    String toString();
}
