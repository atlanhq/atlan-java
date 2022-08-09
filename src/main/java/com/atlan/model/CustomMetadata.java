package com.atlan.model;

import com.atlan.net.AtlanObject;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class CustomMetadata extends AtlanObject {

    /**
     * Mapping of custom metadata sets to attributes to values, all by internal IDs.
     */
    private final Map<String, Map<String, Object>> cmMap;

    private CustomMetadata(Map<String, Map<String, Object>> map) {
        cmMap = map;
    }

    /**
     * List all the names of the custom metadata sets on the object.
     * @return list of custom metadata set names
     */
    public List<String> listSets() {
        return List.copyOf(cmMap.keySet());
    }

    /**
     * List all the names of custom metadata attributes within a given set.
     * @param setName of the custom metadata set
     * @return list of attribute names within the set
     */
    public List<String> listAttributesForSet(String setName) {
        Map<String, Object> set = cmMap.get(setName);
        if (set == null) {
            return Collections.emptyList();
        } else {
            return List.copyOf(set.keySet());
        }
    }

    /**
     * Retrieve the value for an attribute within a custom metadata set.
     * @param setName name of the custom metadata set
     * @param attributeName name of the attribute within the set
     * @return value of that attribute within that set
     */
    public Object getValueForAttribute(String setName, String attributeName) {
        Map<String, Object> set = cmMap.get(setName);
        if (set != null) {
            return set.get(attributeName);
        } else {
            return null;
        }
    }

    /**
     * Quickly check if there is any custom metadata on this asset.
     * @return true if there is no custom metadata on this asset, false if there is custom metadata on the asset
     */
    public boolean isEmpty() {
        return cmMap.isEmpty();
    }

    public static class CustomMetadataBuilder {

        private final Map<String, Map<String, Object>> builderMap;

        public CustomMetadataBuilder() {
            builderMap = new LinkedHashMap<>();
        }

        /**
         * Add (or update) the value of an attribute in a particular custom metadata set.
         * @param setName name of the custom metadata set
         * @param attributeName name of the attribute within the set
         * @param value to add for the attribute / update the existing value with
         */
        public CustomMetadataBuilder withAttribute(String setName, String attributeName, Object value) {
            Map<String, Object> attributes = builderMap.get(setName);
            if (attributes == null) {
                attributes = new LinkedHashMap<>();
            }
            attributes.put(attributeName, value);
            builderMap.put(setName, attributes);
            return this;
        }

        public CustomMetadata build() {
            return new CustomMetadata(builderMap);
        }
    }

    public static CustomMetadataBuilder builder() {
        return new CustomMetadataBuilder();
    }
}
