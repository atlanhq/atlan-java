/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.discovery;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.ITag;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Class to compose a single filter criterion, by tag, for use in a linkable query.
 */
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode
@Slf4j
public class TagFilter {

    /** Atlan-internal tag name for the tag. */
    String name;

    /** Human-readable name for the tag. */
    String displayName;

    /** Value to compare the operand against. */
    @Singular
    List<TagValue> tagValues;

    @Getter
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode
    public static final class TagValue {
        /** Value of the tag to match. */
        String consolidatedValue;

        /** Unique name(s) of the source tags to match the value against. */
        @Singular
        List<String> tagQFNames;
    }

    /**
     * Create a new TagFilter to limit assets to those with the human-readable Atlan tag specified.
     *
     * @param client connectivity to the Atlan tenant
     * @param tagName human-readable name of the Atlan tag
     * @return a filter to limit assets to those with the tag
     */
    public static TagFilter of(AtlanClient client, String tagName) {
        try {
            String clsId = client.getAtlanTagCache().getIdForName(tagName);
            return TagFilter._internal().displayName(tagName).name(clsId).build();
        } catch (AtlanException e) {
            log.error("Unable to translate tag -- skipping: {}", tagName, e);
        }
        return null;
    }

    /**
     * Create a new TagFilter to limit assets to those with the human-readable Atlan tag specified,
     * which in turn is mapped to a source tag, and thus can be further narrowed by the value of the
     * source tag.
     *
     * @param client connectivity to the Atlan tenant
     * @param tagName human-readable name of the Atlan tag
     * @param value of the tag to further narrow by
     * @return a filter to limit assets to those with the tag
     */
    public static TagFilter of(AtlanClient client, String tagName, String value) {
        TagFilter starter = of(client, tagName);
        if (starter != null && value != null && !value.isEmpty()) {
            try {
                List<ITag> sourceTags = client.getSourceTagCache().getByMappedAtlanTag(starter.getName());
                List<String> qualifiedNames =
                        sourceTags.stream().map(ITag::getQualifiedName).toList();
                return starter.toBuilder()
                        .tagValue(TagValue.builder()
                                .tagQFNames(qualifiedNames)
                                .consolidatedValue(value)
                                .build())
                        .build();
            } catch (AtlanException e) {
                log.error("Unable to find any source tags mapped to tag -- skipping: {}", tagName, e);
            }
        }
        return starter;
    }
}
