/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.atlan.model.core.AtlanObject;
import com.atlan.serde.StructDeserializer;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javax.annotation.processing.Generated;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for all structs.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonDeserialize(using = StructDeserializer.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "typeName")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Action.class, name = Action.TYPE_NAME),
    @JsonSubTypes.Type(value = AuthPolicyCondition.class, name = AuthPolicyCondition.TYPE_NAME),
    @JsonSubTypes.Type(value = AuthPolicyValiditySchedule.class, name = AuthPolicyValiditySchedule.TYPE_NAME),
    @JsonSubTypes.Type(value = AppWorkflowRunStep.class, name = AppWorkflowRunStep.TYPE_NAME),
    @JsonSubTypes.Type(value = AssetExternalDQMetadata.class, name = AssetExternalDQMetadata.TYPE_NAME),
    @JsonSubTypes.Type(
            value = AssetExternalDQScoreBreakdownByDimension.class,
            name = AssetExternalDQScoreBreakdownByDimension.TYPE_NAME),
    @JsonSubTypes.Type(value = AssetExternalDQTestDetails.class, name = AssetExternalDQTestDetails.TYPE_NAME),
    @JsonSubTypes.Type(value = AssetExternalDQTestRunHistory.class, name = AssetExternalDQTestRunHistory.TYPE_NAME),
    @JsonSubTypes.Type(value = AssetHistogram.class, name = AssetHistogram.TYPE_NAME),
    @JsonSubTypes.Type(value = AwsCloudWatchMetric.class, name = AwsCloudWatchMetric.TYPE_NAME),
    @JsonSubTypes.Type(value = AwsTag.class, name = AwsTag.TYPE_NAME),
    @JsonSubTypes.Type(value = AzureTag.class, name = AzureTag.TYPE_NAME),
    @JsonSubTypes.Type(value = BadgeCondition.class, name = BadgeCondition.TYPE_NAME),
    @JsonSubTypes.Type(value = BusinessPolicyRule.class, name = BusinessPolicyRule.TYPE_NAME),
    @JsonSubTypes.Type(value = ColumnValueFrequencyMap.class, name = ColumnValueFrequencyMap.TYPE_NAME),
    @JsonSubTypes.Type(value = DatabricksAIModelVersionMetric.class, name = DatabricksAIModelVersionMetric.TYPE_NAME),
    @JsonSubTypes.Type(value = DbtJobRun.class, name = DbtJobRun.TYPE_NAME),
    @JsonSubTypes.Type(value = DbtMetricFilter.class, name = DbtMetricFilter.TYPE_NAME),
    @JsonSubTypes.Type(value = FormField.class, name = FormField.TYPE_NAME),
    @JsonSubTypes.Type(value = GoogleLabel.class, name = GoogleLabel.TYPE_NAME),
    @JsonSubTypes.Type(value = GoogleTag.class, name = GoogleTag.TYPE_NAME),
    @JsonSubTypes.Type(value = Histogram.class, name = Histogram.TYPE_NAME),
    @JsonSubTypes.Type(value = KafkaTopicConsumption.class, name = KafkaTopicConsumption.TYPE_NAME),
    @JsonSubTypes.Type(value = MCRuleComparison.class, name = MCRuleComparison.TYPE_NAME),
    @JsonSubTypes.Type(value = MCRuleSchedule.class, name = MCRuleSchedule.TYPE_NAME),
    @JsonSubTypes.Type(value = PopularityInsights.class, name = PopularityInsights.TYPE_NAME),
    @JsonSubTypes.Type(value = ResponseValue.class, name = ResponseValue.TYPE_NAME),
    @JsonSubTypes.Type(value = SourceTagAttachment.class, name = SourceTagAttachment.TYPE_NAME),
    @JsonSubTypes.Type(value = SourceTagAttachmentValue.class, name = SourceTagAttachmentValue.TYPE_NAME),
    @JsonSubTypes.Type(value = SourceTagAttribute.class, name = SourceTagAttribute.TYPE_NAME),
    @JsonSubTypes.Type(value = StarredDetails.class, name = StarredDetails.TYPE_NAME),
})
@Slf4j
public abstract class AtlanStruct extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Name of the type that defines the struct. */
    String typeName;

    public abstract static class AtlanStructBuilder<C extends AtlanStruct, B extends AtlanStructBuilder<C, B>>
            extends AtlanObject.AtlanObjectBuilder<C, B> {}
}
