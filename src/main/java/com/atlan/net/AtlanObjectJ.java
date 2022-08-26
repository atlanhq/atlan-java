/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.net;

import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public abstract class AtlanObjectJ {

    public AtlanObjectJ() {
        // Do nothing - needed for Lombok SuperBuilder generations...
    }

    @Override
    public String toString() {
        return String.format(
                "<%s@%s> JSON: %s", this.getClass().getName(), System.identityHashCode(this), this.toJson());
    }

    public String toJson() {
        try {
            return Serde.mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("Unable to serialize this object: {}", this.getClass().getName(), e);
        }
        return null;
    }
}
