/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.core;

import com.atlan.cache.CustomMetadataCache;
import com.atlan.exception.AtlanException;
import com.atlan.net.ApiResource;
import com.atlan.net.AtlanObject;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper = false)
public class CustomMetadataUpdateRequest extends AtlanObject {

    /** Whether to include the custom metadata name as an outer wrapper (true) or not (false). */
    private final transient boolean includeName;

    /** Human-readable name of the custom metadata. */
    private final transient String name;

    /** Mapping of custom metadata attributes to values, all by internal IDs. */
    private final transient Map<String, Object> attributes;

    public CustomMetadataUpdateRequest(String name, Map<String, Object> attributes, boolean includeName) {
        this.name = name;
        this.attributes = attributes;
        this.includeName = includeName;
    }

    /**
     * Convert the embedded map directly into JSON, rather than leaving it with a wrapped 'attributes'.
     *
     * @return the JSON for the embedded map
     */
    @Override
    public String toJson() {
        Map<String, Object> businessMetadataAttributes = new LinkedHashMap<>();
        try {
            CustomMetadataCache.getIdMapFromNameMap(name, attributes, businessMetadataAttributes);
            if (includeName) {
                Map<String, Map<String, Object>> wrapped = new LinkedHashMap<>();
                String cmId = CustomMetadataCache.getIdForName(name);
                wrapped.put(cmId, businessMetadataAttributes);
                return ApiResource.GSON.toJson(wrapped);
            } else {
                return ApiResource.GSON.toJson(businessMetadataAttributes);
            }
        } catch (AtlanException e) {
            log.error("Unable to serialize custom metadata for '{}' with: {}", name, attributes, e);
        }
        return null;
    }
}
