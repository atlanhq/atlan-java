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
     * @return object's unique name
     */
    @Override
    String toString();

    /**
     * Check whether two object names are equivalent (or not).
     * @param o object name to compare against
     * @return true if the two names are equivalent
     */
    @Override
    boolean equals(Object o);

    /**
     * Calculate a unique hash code given the object name's unique characteristics.
     * @return unique hash code for the object name
     */
    @Override
    int hashCode();
}
