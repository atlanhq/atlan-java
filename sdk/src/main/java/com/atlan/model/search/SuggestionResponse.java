/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.atlan.model.assets.GlossaryTerm;
import com.atlan.net.ApiResource;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Captures the response from a search for suggestions on an asset's metadata against Atlan.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Builder
@SuppressWarnings("serial")
public class SuggestionResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    /** Suggested system-level descriptions. */
    List<SuggestedItem> systemDescriptions;

    /** Suggested user-provided descriptions. */
    List<SuggestedItem> userDescriptions;

    /** Suggested individual user owners. */
    List<SuggestedItem> ownerUsers;

    /** Suggested group owners. */
    List<SuggestedItem> ownerGroups;

    /** Suggested tags. */
    List<SuggestedItem> atlanTags;

    /** Suggested terms. */
    List<SuggestedTerm> assignedTerms;

    @Getter
    public static final class SuggestedItem {
        /** Number of other assets on which the suggestion appears. */
        long count;

        /** Value of the suggestion. */
        String value;

        public SuggestedItem(long count, String value) {
            this.count = count;
            this.value = value;
        }
    }

    @Getter
    public static final class SuggestedTerm {
        /** Number of other assets on which the suggestion appears. */
        long count;

        /** Reference to the suggested term. */
        GlossaryTerm value;

        public SuggestedTerm(long count, String qualifiedName) {
            this.count = count;
            this.value = GlossaryTerm.refByQualifiedName(qualifiedName);
        }
    }
}
