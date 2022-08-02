/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.core;

import com.atlan.net.AtlanObject;
import com.google.gson.JsonObject;

/** Fallback class for when we do not recognize the object that we have received. */
public class AtlanRawJsonObject extends AtlanObject {
    JsonObject json;
}
