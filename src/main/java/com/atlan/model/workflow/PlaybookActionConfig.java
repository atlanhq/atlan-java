/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import com.atlan.model.core.AtlanObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

// TODO: split this class into different subclasses with custom serde,
//  - metadata updates (first 4 properties)
//  - bulk classifications (classificationsConfig property)
//  - others?
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class PlaybookActionConfig extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Whether to replace all classifications (true) or none (false). */
    @JsonProperty("replace_classifications")
    Boolean replaceClassifications;

    /** Whether to replace all custom metadata (true) or none (false). */
    @JsonProperty("replace_bms")
    Boolean replaceCustomMetadata;

    /** Filename for the template being output. */
    @JsonProperty("output_filename")
    String outputFilename;

    /** Base64-encoded jinja template detailing the action. */
    String template;

    /** Details of the classification(s) to apply, for bulk-classification actions. */
    // TODO: further (de)serialize this stringified value
    @JsonProperty("classifications-config")
    String classificationsConfig;
}
