/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package ${packageRoot}.structs;

import com.atlan.model.structs.AtlanStruct;
<#if className == "BadgeCondition">
import com.atlan.model.enums.BadgeComparisonOperator;
import com.atlan.model.enums.BadgeConditionColor;
<#elseif className == "SourceTagAttachment">
import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.cache.SourceTagCache;
import com.atlan.exception.AtlanException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.assets.Connection;
import com.atlan.model.assets.ITag;
</#if>
<#list attributes as attribute>
<#if attribute.type.type == "ENUM">
import com.atlan.model.enums.${attribute.type.name};
<#elseif attribute.type.type == "STRUCT">
import com.atlan.model.structs.${attribute.type.name};
</#if>
</#list>
import java.util.Map;
import java.util.List;
import java.util.SortedSet;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import javax.annotation.processing.Generated;

/**
 * ${description}
 */
@Generated(value="${generatorName}")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
<#if mapContainers??>@SuppressWarnings({"cast", "serial"})<#else>@SuppressWarnings("serial")</#if>
public class ${className} extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "${className}";

    /** Fixed typeName for ${className}. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

<#list attributes as attribute>
    /** ${attribute.description} */
    <#if attribute.singular??>
    @Singular<#if attribute.singular?has_content>("${attribute.singular}")</#if>
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    </#if>
    <#if attribute.renamed != attribute.originalName>
    @JsonProperty("${attribute.originalName}")
    </#if>
    ${attribute.fullType} ${attribute.renamed};

</#list>
<#if className == "BadgeCondition">
    /**
     * Build a new condition for a badge on a string-based custom metadata property (including options (enumerations)).
     * Note that this will wrap the value itself in double-quotes, as this is needed to properly set the value for the
     * badge. So for example if you set the value as {@code abc123} and you retrieve this value back from the badge
     * condition, you will receive back {@code "abc123"}.
     *
     * @param operator the comparison operator for the condition
     * @param value the value to match against the comparison operator
     * @param color the color to use when a value matches (from the standard colors available in the UI)
     * @return the badge condition with these criteria
     */
    public static BadgeCondition of(BadgeComparisonOperator operator, String value, BadgeConditionColor color) {
        return BadgeCondition.builder()
                .badgeConditionOperator(operator)
                .badgeConditionValue("\"" + value + "\"")
                .badgeConditionColorhex(color.getValue())
                .build();
    }

    /**
     * Build a new condition for a badge on a string-based custom metadata property (including options (enumerations)).
     * Note that this will wrap the value itself in double-quotes, as this is needed to properly set the value for the
     * badge. So for example if you set the value as {@code abc123} and you retrieve this value back from the badge
     * condition, you will receive back {@code "abc123"}.
     *
     * @param operator the comparison operator for the condition
     * @param value the value to match against the comparison operator
     * @param color the color to use when a value matches (any valid RGB hex string of the form {@code #ffffff})
     * @return the badge condition with these criteria
     */
    public static BadgeCondition of(BadgeComparisonOperator operator, String value, String color) {
        return BadgeCondition.builder()
                .badgeConditionOperator(operator)
                .badgeConditionValue("\"" + value + "\"")
                .badgeConditionColorhex(color)
                .build();
    }

    /**
     * Build a new condition for a badge on a number-based custom metadata property.
     *
     * @param operator the comparison operator for the condition
     * @param value the value to match against the comparison operator
     * @param color the color to use when a value matches (from the standard colors available in the UI)
     * @return the badge condition with these criteria
     */
    public static BadgeCondition of(BadgeComparisonOperator operator, Number value, BadgeConditionColor color) {
        return BadgeCondition.builder()
                .badgeConditionOperator(operator)
                .badgeConditionValue(value.toString())
                .badgeConditionColorhex(color.getValue())
                .build();
    }

    /**
     * Build a new condition for a badge on a number-based custom metadata property.
     *
     * @param operator the comparison operator for the condition
     * @param value the value to match against the comparison operator
     * @param color the color to use when a value matches (any valid RGB hex string of the form {@code #ffffff})
     * @return the badge condition with these criteria
     */
    public static BadgeCondition of(BadgeComparisonOperator operator, Number value, String color) {
        return BadgeCondition.builder()
                .badgeConditionOperator(operator)
                .badgeConditionValue(value.toString())
                .badgeConditionColorhex(color)
                .build();
    }

    /**
     * Build a new condition for a badge on a boolean-based custom metadata property.
     *
     * @param operator the comparison operator for the condition
     * @param value the value to match against the comparison operator
     * @param color the color to use when a value matches (from the standard colors available in the UI)
     * @return the badge condition with these criteria
     */
    public static BadgeCondition of(BadgeComparisonOperator operator, boolean value, BadgeConditionColor color) {
        return BadgeCondition.builder()
                .badgeConditionOperator(operator)
                .badgeConditionValue(Boolean.toString(value))
                .badgeConditionColorhex(color.getValue())
                .build();
    }

    /**
     * Build a new condition for a badge on a boolean-based custom metadata property.
     *
     * @param operator the comparison operator for the condition
     * @param value the value to match against the comparison operator
     * @param color the color to use when a value matches (any valid RGB hex string of the form {@code #ffffff})
     * @return the badge condition with these criteria
     */
    public static BadgeCondition of(BadgeComparisonOperator operator, boolean value, String color) {
        return BadgeCondition.builder()
                .badgeConditionOperator(operator)
                .badgeConditionValue(Boolean.toString(value))
                .badgeConditionColorhex(color)
                .build();
    }
<#elseif className == "SourceTagAttachment">
    /**
     * Create a source-synced tag attachment with a particular value, when the attachment is
     * synced to the source.
     *
     * @param client connectivity to an Atlan tenant
     * @param name Unique name of the source tag, in Atlan.
     * @param sourceTagValues Value of the tag attachment, from the source.
     * @param sourceTagSyncTimestamp Time (epoch) when the tag attachment was synced at the source, in milliseconds.
     * @param sourceTagSyncError Error message if the tag attachment sync at the source failed.
     * @return a SourceTagAttachment with the provided information
     * @throws AtlanException on any error communicating via the underlying APIs
     * @throws NotFoundException if the source-synced tag cannot be resolved
     */
    public static SourceTagAttachment byName(
            AtlanClient client,
            SourceTagCache.SourceTagName name,
            List<SourceTagAttachmentValue> sourceTagValues,
            Long sourceTagSyncTimestamp,
            String sourceTagSyncError)
            throws AtlanException {
        return byName(client, name, sourceTagValues, true, sourceTagSyncTimestamp, sourceTagSyncError);
    }

    /**
     * Create a source-synced tag attachment with a particular value, when the attachment is
     * not synced to the source.
     *
     * @param client connectivity to an Atlan tenant
     * @param name Unique name of the source tag, in Atlan.
     * @param sourceTagValues Value of the tag attachment, from the source.
     * @return a SourceTagAttachment with the provided information
     * @throws AtlanException on any error communicating via the underlying APIs
     * @throws NotFoundException if the source-synced tag cannot be resolved
     */
    public static SourceTagAttachment byName(
            AtlanClient client, SourceTagCache.SourceTagName name, List<SourceTagAttachmentValue> sourceTagValues)
            throws AtlanException {
        return byName(client, name, sourceTagValues, false, null, null);
    }

    /**
     * Create a source-synced tag attachment with a particular value, when the attachment is
     * synced to the source.
     *
     * @param client connectivity to an Atlan tenant
     * @param name Unique name of the source tag, in Atlan.
     * @param sourceTagValues Value of the tag attachment, from the source.
     * @param isSourceTagSynced Whether the tag attachment has been synced at the source (true) or not (false).
     * @param sourceTagSyncTimestamp Time (epoch) when the tag attachment was synced at the source, in milliseconds.
     * @param sourceTagSyncError Error message if the tag attachment sync at the source failed.
     * @return a SourceTagAttachment with the provided information
     * @throws AtlanException on any error communicating via the underlying APIs
     * @throws NotFoundException if the source-synced tag cannot be resolved
     */
    private static SourceTagAttachment byName(
            AtlanClient client,
            SourceTagCache.SourceTagName name,
            List<SourceTagAttachmentValue> sourceTagValues,
            Boolean isSourceTagSynced,
            Long sourceTagSyncTimestamp,
            String sourceTagSyncError)
            throws AtlanException {
        ITag tag = (ITag) client.getSourceTagCache().getByName(name);
        String qualifiedName = tag.getQualifiedName();
        return of(
                tag.getName(),
                qualifiedName,
                tag.getGuid(),
                Connection.getConnectorTypeFromQualifiedName(qualifiedName).getValue(),
                sourceTagValues,
                isSourceTagSynced,
                sourceTagSyncTimestamp,
                sourceTagSyncError);
    }

    /**
     * Create a source-synced tag attachment with a particular value, when the attachment is
     * synced to the source.
     *
     * @param client connectivity to an Atlan tenant
     * @param sourceTagQualifiedName Unique name of the source tag, in Atlan.
     * @param sourceTagValues Value of the tag attachment, from the source.
     * @param sourceTagSyncTimestamp Time (epoch) when the tag attachment was synced at the source, in milliseconds.
     * @param sourceTagSyncError Error message if the tag attachment sync at the source failed.
     * @return a SourceTagAttachment with the provided information
     * @throws AtlanException on any error communicating via the underlying APIs
     * @throws NotFoundException if the source-synced tag cannot be resolved
     */
    public static SourceTagAttachment byQualifiedName(
            AtlanClient client,
            String sourceTagQualifiedName,
            List<SourceTagAttachmentValue> sourceTagValues,
            Long sourceTagSyncTimestamp,
            String sourceTagSyncError)
            throws AtlanException {
        return byQualifiedName(
                client, sourceTagQualifiedName, sourceTagValues, true, sourceTagSyncTimestamp, sourceTagSyncError);
    }

    /**
     * Create a source-synced tag attachment with a particular value, when the attachment is
     * not synced to the source.
     *
     * @param client connectivity to an Atlan tenant
     * @param sourceTagQualifiedName Unique name of the source tag, in Atlan.
     * @param sourceTagValues Value of the tag attachment, from the source.
     * @return a SourceTagAttachment with the provided information
     * @throws AtlanException on any error communicating via the underlying APIs
     * @throws NotFoundException if the source-synced tag cannot be resolved
     */
    public static SourceTagAttachment byQualifiedName(
            AtlanClient client, String sourceTagQualifiedName, List<SourceTagAttachmentValue> sourceTagValues) throws AtlanException {
        return byQualifiedName(client, sourceTagQualifiedName, sourceTagValues, false, null, null);
    }

    /**
     * Create a source-synced tag attachment with a particular value, when the attachment is
     * synced to the source.
     *
     * @param client connectivity to an Atlan tenant
     * @param sourceTagQualifiedName Unique name of the source tag, in Atlan.
     * @param sourceTagValues Value of the tag attachment, from the source.
     * @param isSourceTagSynced Whether the tag attachment has been synced at the source (true) or not (false).
     * @param sourceTagSyncTimestamp Time (epoch) when the tag attachment was synced at the source, in milliseconds.
     * @param sourceTagSyncError Error message if the tag attachment sync at the source failed.
     * @return a SourceTagAttachment with the provided information
     * @throws AtlanException on any error communicating via the underlying APIs
     * @throws NotFoundException if the source-synced tag cannot be resolved
     */
    private static SourceTagAttachment byQualifiedName(
            AtlanClient client,
            String sourceTagQualifiedName,
            List<SourceTagAttachmentValue> sourceTagValues,
            Boolean isSourceTagSynced,
            Long sourceTagSyncTimestamp,
            String sourceTagSyncError)
            throws AtlanException {
        ITag tag = (ITag) client.getSourceTagCache().getByQualifiedName(sourceTagQualifiedName);
        return of(
                tag.getName(),
                sourceTagQualifiedName,
                tag.getGuid(),
                Connection.getConnectorTypeFromQualifiedName(sourceTagQualifiedName).getValue(),
                sourceTagValues,
                isSourceTagSynced,
                sourceTagSyncTimestamp,
                sourceTagSyncError);
    }

    /**
     * Quickly create a new SourceTagAttachment.
     *
     * @param sourceTagName Simple name of the source tag.
     * @param sourceTagQualifiedName Unique name of the source tag, in Atlan.
     * @param sourceTagGuid Unique identifier (GUID) of the source tag, in Atlan.
     * @param sourceTagConnectorName Connector that is the source of the tag.
     * @param sourceTagValues Value of the tag attachment, from the source.
     * @param isSourceTagSynced Whether the tag attachment has been synced at the source (true) or not (false).
     * @param sourceTagSyncTimestamp Time (epoch) when the tag attachment was synced at the source, in milliseconds.
     * @param sourceTagSyncError Error message if the tag attachment sync at the source failed.
     * @return a SourceTagAttachment with the provided information
     */
    public static SourceTagAttachment of(
            String sourceTagName,
            String sourceTagQualifiedName,
            String sourceTagGuid,
            String sourceTagConnectorName,
            List<SourceTagAttachmentValue> sourceTagValues,
            Boolean isSourceTagSynced,
            Long sourceTagSyncTimestamp,
            String sourceTagSyncError) {
        return SourceTagAttachment.builder()
                .sourceTagName(sourceTagName)
                .sourceTagQualifiedName(sourceTagQualifiedName)
                .sourceTagGuid(sourceTagGuid)
                .sourceTagConnectorName(sourceTagConnectorName)
                .sourceTagValues(sourceTagValues)
                .isSourceTagSynced(isSourceTagSynced)
                .sourceTagSyncTimestamp(sourceTagSyncTimestamp)
                .sourceTagSyncError(sourceTagSyncError)
                .build();
    }
<#else>
    /**
     * Quickly create a new ${className}.
<#list attributes as attribute>
     * @param ${attribute.renamed} ${attribute.description}
</#list>
     * @return a ${className} with the provided information
     */
    public static ${className} of(<#list attributes as attribute>${attribute.fullType} ${attribute.renamed}<#sep>, </#sep></#list>) {
        return ${className}.builder()
        <#list attributes as attribute>
            .${attribute.renamed}(${attribute.renamed})
        </#list>
            .build();
    }
</#if>

    public abstract static class ${className}Builder<C extends ${className}, B extends ${className}Builder<C, B>>
        extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
