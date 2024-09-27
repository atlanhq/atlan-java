/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.model.contracts;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.serde.ReadableCustomMetadataDeserializer;
import com.atlan.serde.ReadableCustomMetadataSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Capture the detailed specification of a data contract for an asset.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "template_version")
@JsonSubTypes({
    @JsonSubTypes.Type(value = DCS_V_0_0_2.class, name = "0.0.2"),
})
@SuppressWarnings("cast")
public abstract class DataContractSpec extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Controls the specification as one for a data contract. */
    final String kind = "DataContract";

    /** State of the contract. */
    DataContractStatus status;

    /** Version of the template for the data contract. */
    @JsonProperty("template_version")
    String templateVersion;

    /** Name of the asset as it exists inside Atlan. */
    String dataset;

    /** Type of the dataset in Atlan. */
    String type;

    /** Description of this dataset. */
    String description;

    /** Name that must match a data source defined in your config file. */
    String datasource;

    /** Owners of the dataset, which can include users (by username) and / or groups (by internal Atlan alias). */
    Owners owners;

    /** Certification to apply to the dataset. */
    Certification certification;

    /** Announcement to apply to the dataset. */
    Announcement announcement;

    /** Glossary terms to assign to the dataset. */
    List<String> terms;

    /** Atlan tags for the dataset. */
    List<DCTag> tags;

    /** Custom metadata for the dataset. */
    @Singular
    @JsonProperty("customMetadata")
    @JsonSerialize(using = ReadableCustomMetadataSerializer.class)
    @JsonDeserialize(using = ReadableCustomMetadataDeserializer.class)
    Map<String, CustomMetadataAttributes> customMetadataSets;

    /** Details of each column in the dataset to be governed. */
    @Singular
    List<DCColumn> columns;

    /** List of checks to run to verify data quality of the dataset. */
    @Singular
    List<String> checks;

    @Getter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = false)
    public static final class Owners {
        /** Individual users who own the dataset. */
        List<String> users;

        /** Groups that own the dataset. */
        List<String> groups;
    }

    @Getter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = false)
    public static final class Certification {
        /** State of the certification. */
        CertificateStatus status;

        /** Message to accompany the certification. */
        String message;
    }

    @Getter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = false)
    public static final class Announcement {
        /** Type of announcement. */
        AtlanAnnouncementType type;

        /** Title to use for the announcement. */
        String title;

        /** Message to accompany the announcement. */
        String description;
    }

    @Getter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = false)
    public static final class DCTag {
        /** Human-readable name of the Atlan tag. */
        String name;

        /** Whether to propagate the tag at all (true) or not (false). */
        Boolean propagate;

        /** Whether to propagate the tag through lineage (true) or not (false). */
        @JsonProperty("restrict_propagation_through_lineage")
        Boolean propagateThroughLineage;

        /** Whether to propagate the tag through asset's containment hierarchy (true) or not (false). */
        @JsonProperty("restrict_propagation_through_hierarchy")
        Boolean propagateThroughHierarchy;
    }

    @Getter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = false)
    public static final class DCColumn {
        /** Name of the column as it is defined in the source system (often technical). */
        String name;

        /** Alias for the column, to make its name more readable. */
        @JsonProperty("business_name")
        String displayName;

        /** Description of this column, for documentation purposes. */
        String description;

        /** When true, this column is the primary key for the table. */
        @JsonProperty("is_primary")
        Boolean isPrimary;

        /** Physical data type of values in this column (e.g. {@code varchar(20)}). */
        @JsonProperty("data_type")
        String dataType;

        /** Logical data type of values in this column (e.g. {@code string}). */
        @JsonProperty("logical_type")
        String logicalType;

        /** Format of data to consider invalid. */
        @JsonProperty("invalid_format")
        String invalidFormat;

        /** Format of data to consider valid. */
        @JsonProperty("valid_format")
        String validFormat;

        /** Regular expression to match invalid values. */
        @JsonProperty("invalid_regex")
        String invalidRegex;

        /** Regular expression to match valid values. */
        @JsonProperty("valid_regex")
        String validRegex;

        /** Regular expression to match missing values. */
        @JsonProperty("missing_regex")
        String missingRegex;

        /** Enumeration of values that should be considered invalid. */
        @Singular
        @JsonProperty("invalid_values")
        List<String> invalidValues;

        /** Enumeration of values that should be considered valid. */
        @Singular
        @JsonProperty("valid_values")
        List<String> validValues;

        /** Enumeration of values that should be considered missing. */
        @Singular
        @JsonProperty("missing_values")
        List<String> missingValues;

        /** When true, this column cannot be empty (without values). */
        @JsonProperty("not_null")
        Boolean notNull;

        /** Fixed length for a string to be considered valid. */
        @JsonProperty("valid_length")
        Long validLength;

        /** Maximum length for a string to be considered valid. */
        @JsonProperty("valid_max_length")
        Long validMaxLength;

        /** Minimum numeric value considered valid. */
        @JsonProperty("valid_min")
        Double validMin;

        /** Maximum numeric value considered valid. */
        @JsonProperty("valid_max")
        Double validMax;

        /** Minimum length for a string to be considered valid. */
        @JsonProperty("valid_min_length")
        Long validMinLength;

        /** When true, this column must have unique values. */
        Boolean unique;
    }
}
