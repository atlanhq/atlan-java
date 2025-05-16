/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

import com.atlan.AtlanClient;
import com.atlan.model.assets.*;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanCustomAttributePrimitiveType;
import com.atlan.serde.SetToStringSerializer;
import com.atlan.serde.StringToSetDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Set;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import javax.annotation.processing.Generated;

/**
 * Options that can be set on each attribute within a type definition.
 */
@Generated(value="${generatorName}")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AttributeDefOptions extends AtlanObject {
    private static final long serialVersionUID = 2L;

    public static final Set<String> ALL_ASSET_TYPES = Set.of(
<#list assetTypes as assetType>
<#if assetType != "File" && !(assetType?starts_with("Glossary")) && assetType != "DataDomain" && assetType != "DataProduct">
            ${assetType}.TYPE_NAME<#sep>,</#sep>
</#if>
</#list>
        );
    public static final Set<String> ALL_GLOSSARY_TYPES = Set.of(
        Glossary.TYPE_NAME,
        GlossaryTerm.TYPE_NAME,
        GlossaryCategory.TYPE_NAME
    );
    public static final Set<String> ALL_DOMAIN_TYPES = Set.of(DataDomain.TYPE_NAME, DataProduct.TYPE_NAME);
    public static final Set<String> ALL_OTHER_TYPES = Set.of(
        File.TYPE_NAME
    );
    public static final Set<String> ALL_DOMAINS = Set.of("*/super");

    /**
     * Instantiate a new set of attribute options from the provided parameters.
     * @param client connectivity to the Atlan tenant
     * @param type primitive type of the attribute
     * @param optionsName name of the options (enumeration) if the primitive type is an enumeration (can be null otherwise)
     * @return the attribute options
     * @throws AtlanException on any API issues looking up existing connections and glossaries
     */
    public static AttributeDefOptions of(AtlanClient client, AtlanCustomAttributePrimitiveType type, String optionsName)
            throws AtlanException {
        return of(client, type, optionsName, null);
    }

    /**
     * Instantiate a new set of attribute options from the provided parameters.
     * @param client connectivity to the Atlan tenant
     * @param type primitive type of the attribute
     * @param optionsName name of the options (enumeration) if the primitive type is an enumeration (can be null otherwise)
     * @param options starting point of options on which to extend
     * @return the attribute options
     * @throws AtlanException on any API issues looking up existing connections and glossaries
     */
    public static AttributeDefOptions of(AtlanClient client, AtlanCustomAttributePrimitiveType type, String optionsName, AttributeDefOptions options) throws AtlanException {
        AttributeDefOptionsBuilder<?, ?> builder;
        if (options != null) {
            // If we are provided options, use those as the starting point
            builder = options.toBuilder();
        } else {
            // Otherwise set defaults to allow the attribute to be available on all assets
            builder = AttributeDefOptions.builder()
                    .applicableConnections(Connection.getAllQualifiedNames(client))
                    .applicableAssetTypes(ALL_ASSET_TYPES)
                    .applicableGlossaries(Glossary.getAllQualifiedNames(client))
                    .applicableGlossaryTypes(ALL_GLOSSARY_TYPES)
                    .applicableDomains(ALL_DOMAINS)
                    .applicableDomainTypes(ALL_DOMAIN_TYPES)
                    .applicableOtherAssetTypes(ALL_OTHER_TYPES);
        }
        builder.primitiveType(type);
        switch (type) {
            case USERS:
            case GROUPS:
            case URL:
            case SQL:
                builder.customType(type.getValue());
                break;
            case OPTIONS:
                builder.isEnum(true).enumType(optionsName);
                break;
            default:
                // do nothing
                break;
        }
        return builder.build();
    }

    /** Indicates the version of the custom metadata structure. This determines which other options are available and used. */
    @Builder.Default
    String customMetadataVersion = "v2";

    /** Optional description of the attribute. */
    String description;

    /**
     * Set of entities on which this attribute can be applied.
     * Note: generally this should be left as-is. Any overrides should instead be applied through
     * one or more of {@link #applicableAssetTypes}, {@link #applicableGlossaryTypes}, or {@link #applicableOtherAssetTypes}.
     */
    @Builder.Default
    @JsonSerialize(using = SetToStringSerializer.class)
    @JsonDeserialize(using = StringToSetDeserializer.class)
    Set<String> applicableEntityTypes = Set.of("Asset");

    /**
     * Set of entities on which this attribute should appear.
     * Note: this is only used when customMetadataVersion is less than v2.
     * @deprecated see {@link #applicableAssetTypes}, {@link #applicableGlossaryTypes}, {@link #applicableOtherAssetTypes} instead
     */
    @JsonSerialize(using = SetToStringSerializer.class)
    @JsonDeserialize(using = StringToSetDeserializer.class)
    @Deprecated
    Set<String> customApplicableEntityTypes;

    /**
     * Qualified names of connections to which to restrict the attribute.
     * Only assets within one of these connections will have this attribute available.
     * To further restrict the types of assets within the connections, see {@link #applicableAssetTypes}.
     */
    @Singular
    @JsonSerialize(using = SetToStringSerializer.class)
    @JsonDeserialize(using = StringToSetDeserializer.class)
    Set<String> applicableConnections;

    /**
     * Qualified names of glossaries to which to restrict the attribute.
     * Only glossary assets within one of these glossaries will have this attribute available.
     * To further restrict the types of assets within the glossaries, see {@link #applicableGlossaryTypes}.
     */
    @Singular
    @JsonSerialize(using = SetToStringSerializer.class)
    @JsonDeserialize(using = StringToSetDeserializer.class)
    Set<String> applicableGlossaries;

    /**
     * Qualified names of domains to which to restrict the attribute.
     * Only domains and data products within one of these domains will have this attribute available.
     * To further restrict the types of assets within the domains, see {@link #applicableDomainTypes}.
     */
    @Singular
    @JsonSerialize(using = SetToStringSerializer.class)
    @JsonDeserialize(using = StringToSetDeserializer.class)
    Set<String> applicableDomains;

    /**
     * Asset type names to which to restrict the attribute.
     * Only assets of one of these types will have this attribute available.
     * To further restrict the assets for this custom metadata by connection, see {@link #applicableConnections}.
     */
    @Singular
    @JsonSerialize(using = SetToStringSerializer.class)
    @JsonDeserialize(using = StringToSetDeserializer.class)
    @JsonProperty("assetTypesList")
    Set<String> applicableAssetTypes;

    /**
     * Glossary type names to which to restrict the attribute.
     * Only glossary assets of one of these types will have this attribute available.
     * To further restrict the glossary content for this custom metadata by glossary, see {@link #applicableGlossaries}.
     */
    @Singular
    @JsonSerialize(using = SetToStringSerializer.class)
    @JsonDeserialize(using = StringToSetDeserializer.class)
    @JsonProperty("glossaryTypeList")
    Set<String> applicableGlossaryTypes;

    /**
     * Data product type names to which to restrict the attribute.
     * These cover asset types in data products and data domains.
     * Only assets of one of these types will have this attribute available.
     */
    @Singular
    @JsonSerialize(using = SetToStringSerializer.class)
    @JsonDeserialize(using = StringToSetDeserializer.class)
    @JsonProperty("domainTypesList")
    Set<String> applicableDomainTypes;

    /**
     * Any other asset type names to which to restrict the attribute.
     * These cover any asset type that is not managed within a connection or a glossary.
     * Only assets of one of these types will have this attribute available.
     */
    @Singular
    @JsonSerialize(using = SetToStringSerializer.class)
    @JsonDeserialize(using = StringToSetDeserializer.class)
    @JsonProperty("otherAssetTypeList")
    Set<String> applicableOtherAssetTypes;

    /** Whether the attribute should be searchable (true) or not (false). */
    @Builder.Default
    Boolean allowSearch = false;

    /** Maximum length allowed for a string value. */
    @Builder.Default
    String maxStrLength = "100000000";

    /** Whether this attribute should appear in the filterable facets of discovery (true) or not (false). */
    @Builder.Default
    Boolean allowFiltering = true;

    /** Whether this attribute can have multiple values (true) or only a single value (false). */
    @Builder.Default
    Boolean multiValueSelect = false;

    /** Whether users will see this attribute in the overview tab of the sidebar (true) or not (false). */
    @Builder.Default
    Boolean showInOverview = false;

    /** Whether the attribute is deprecated ("true") or not (null or "false"). */
    String isDeprecated;

    /** Primitive type of the attribute. */
    AtlanCustomAttributePrimitiveType primitiveType;

    /** Whether the attribute is an enumeration (true) or not (false). */
    @Builder.Default
    Boolean isEnum = false;

    /** Name of the enumeration (options), when the attribute is an enumeration. */
    String enumType;

    /** Used for Atlan-specific types like {@code users}, {@code groups}, {@code url}, and {@code SQL}. */
    String customType;

    /** If true for a date attribute, then time-level precision is also available in the UI (otherwise only date-level) */
    Boolean hasTimePrecision;

    /** Whether the attribute has been deleted (true) or is still active (false). */
    Boolean isArchived;

    /** When the attribute was deleted. */
    Long archivedAt;

    /** User who deleted the attribute. */
    String archivedBy;

    /** TBC */
    String isSoftReference;

    /** TBC */
    String isAppendOnPartialUpdate;

    /**
     * Append the provided set of attribute definition options onto this attribute definition.
     * Note that certain options are ignored by the append, specifically: primitiveType, isEnum,
     * enumType, customType, multiValueSelect, isArchived, archivedAt, archivedBy. These should only be set
     * by the appropriate initial setup {@link #of(AtlanClient, AtlanCustomAttributePrimitiveType, String)} or archival
     * of an attribute definition.
     *
     * @param options to append to this set of attribute definition options
     * @return the combined set of attribute definition options
     */
    public AttributeDefOptions append(AttributeDefOptions options) {
        if (options == null) {
            return this;
        }
        if (options.description != null) {
            this.description = options.description;
        }
        if (options.applicableEntityTypes != null) {
            this.applicableEntityTypes = options.applicableEntityTypes;
        }
        if (options.customApplicableEntityTypes != null) {
            this.customApplicableEntityTypes = options.customApplicableEntityTypes;
        }
        if (options.applicableConnections != null) {
            this.applicableConnections = options.applicableConnections;
        }
        if (options.applicableGlossaries != null) {
            this.applicableGlossaries = options.applicableGlossaries;
        }
        if (options.applicableDomains != null) {
            this.applicableDomains = options.applicableDomains;
        }
        if (options.applicableAssetTypes != null) {
            this.applicableAssetTypes = options.applicableAssetTypes;
        }
        if (options.applicableGlossaryTypes != null) {
            this.applicableGlossaryTypes = options.applicableGlossaryTypes;
        }
        if (options.applicableDomainTypes != null) {
            this.applicableDomainTypes = options.applicableDomainTypes;
        }
        if (options.applicableOtherAssetTypes != null) {
            this.applicableOtherAssetTypes = options.applicableOtherAssetTypes;
        }
        if (options.allowSearch != null) {
            this.allowSearch = options.allowSearch;
        }
        if (options.maxStrLength != null) {
            this.maxStrLength = options.maxStrLength;
        }
        if (options.allowFiltering != null) {
            this.allowFiltering = options.allowFiltering;
        }
        if (options.showInOverview != null) {
            this.showInOverview = options.showInOverview;
        }
        if (options.isDeprecated != null) {
            this.isDeprecated = options.isDeprecated;
        }
        if (options.hasTimePrecision != null) {
            this.hasTimePrecision = options.hasTimePrecision;
        }
        return this;
    }

    public abstract static class AttributeDefOptionsBuilder<C extends AttributeDefOptions, B extends AttributeDefOptionsBuilder<C, B>>
        extends AtlanObject.AtlanObjectBuilder<C, B> {}
}
