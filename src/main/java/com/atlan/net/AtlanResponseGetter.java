/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.net;

import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanResponseInterface;

public interface AtlanResponseGetter {
    <T extends AtlanResponseInterface> T request(
            ApiResource.RequestMethod method, String url, String body, Class<T> clazz, RequestOptions options)
            throws AtlanException;
}
