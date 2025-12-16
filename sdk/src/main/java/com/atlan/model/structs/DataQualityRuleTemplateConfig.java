/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about the configurations settings for the rule template.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class DataQualityRuleTemplateConfig extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DataQualityRuleTemplateConfig";

    /** Fixed typeName for DataQualityRuleTemplateConfig. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Config for selecting base dataset for the rule. */
    String dqRuleTemplateConfigBaseDatasetQualifiedName;

    /** Config for selecting base column for the rule. */
    String dqRuleTemplateConfigBaseColumnQualifiedName;

    /** Config for selecting reference datasets for the rule. */
    String dqRuleTemplateConfigReferenceDatasetQualifiedNames;

    /** Config for selecting reference columns for the rule. */
    String dqRuleTemplateConfigReferenceColumnQualifiedNames;

    /** Config for selecting threshold object for the rule. */
    String dqRuleTemplateConfigThresholdObject;

    /** Config for selecting display name for the rule. */
    String dqRuleTemplateConfigDisplayName;

    /** Config for adding custom SQL. */
    String dqRuleTemplateConfigCustomSQL;

    /** Config for selecting rule dimension. */
    String dqRuleTemplateConfigDimension;

    /** Config for entering description of the rule by user. */
    String dqRuleTemplateConfigUserDescription;

    /** Advanced settings for the rule template. */
    String dqRuleTemplateConfigAdvancedSettings;

    /** Configuration for Rule conditions of the template. */
    String dqRuleTemplateConfigRuleConditions;

    /** Configuration for preflight check of the template. */
    String dqRuleTemplateConfigPreflightCheck;

    /**
     * Quickly create a new DataQualityRuleTemplateConfig.
     * @param dqRuleTemplateConfigBaseDatasetQualifiedName Config for selecting base dataset for the rule.
     * @param dqRuleTemplateConfigBaseColumnQualifiedName Config for selecting base column for the rule.
     * @param dqRuleTemplateConfigReferenceDatasetQualifiedNames Config for selecting reference datasets for the rule.
     * @param dqRuleTemplateConfigReferenceColumnQualifiedNames Config for selecting reference columns for the rule.
     * @param dqRuleTemplateConfigThresholdObject Config for selecting threshold object for the rule.
     * @param dqRuleTemplateConfigDisplayName Config for selecting display name for the rule.
     * @param dqRuleTemplateConfigCustomSQL Config for adding custom SQL.
     * @param dqRuleTemplateConfigDimension Config for selecting rule dimension.
     * @param dqRuleTemplateConfigUserDescription Config for entering description of the rule by user.
     * @param dqRuleTemplateConfigAdvancedSettings Advanced settings for the rule template.
     * @param dqRuleTemplateConfigRuleConditions Configuration for Rule conditions of the template.
     * @param dqRuleTemplateConfigPreflightCheck Configuration for preflight check of the template.
     * @return a DataQualityRuleTemplateConfig with the provided information
     */
    public static DataQualityRuleTemplateConfig of(
            String dqRuleTemplateConfigBaseDatasetQualifiedName,
            String dqRuleTemplateConfigBaseColumnQualifiedName,
            String dqRuleTemplateConfigReferenceDatasetQualifiedNames,
            String dqRuleTemplateConfigReferenceColumnQualifiedNames,
            String dqRuleTemplateConfigThresholdObject,
            String dqRuleTemplateConfigDisplayName,
            String dqRuleTemplateConfigCustomSQL,
            String dqRuleTemplateConfigDimension,
            String dqRuleTemplateConfigUserDescription,
            String dqRuleTemplateConfigAdvancedSettings,
            String dqRuleTemplateConfigRuleConditions,
            String dqRuleTemplateConfigPreflightCheck) {
        return DataQualityRuleTemplateConfig.builder()
                .dqRuleTemplateConfigBaseDatasetQualifiedName(dqRuleTemplateConfigBaseDatasetQualifiedName)
                .dqRuleTemplateConfigBaseColumnQualifiedName(dqRuleTemplateConfigBaseColumnQualifiedName)
                .dqRuleTemplateConfigReferenceDatasetQualifiedNames(dqRuleTemplateConfigReferenceDatasetQualifiedNames)
                .dqRuleTemplateConfigReferenceColumnQualifiedNames(dqRuleTemplateConfigReferenceColumnQualifiedNames)
                .dqRuleTemplateConfigThresholdObject(dqRuleTemplateConfigThresholdObject)
                .dqRuleTemplateConfigDisplayName(dqRuleTemplateConfigDisplayName)
                .dqRuleTemplateConfigCustomSQL(dqRuleTemplateConfigCustomSQL)
                .dqRuleTemplateConfigDimension(dqRuleTemplateConfigDimension)
                .dqRuleTemplateConfigUserDescription(dqRuleTemplateConfigUserDescription)
                .dqRuleTemplateConfigAdvancedSettings(dqRuleTemplateConfigAdvancedSettings)
                .dqRuleTemplateConfigRuleConditions(dqRuleTemplateConfigRuleConditions)
                .dqRuleTemplateConfigPreflightCheck(dqRuleTemplateConfigPreflightCheck)
                .build();
    }

    public abstract static class DataQualityRuleTemplateConfigBuilder<
                    C extends DataQualityRuleTemplateConfig, B extends DataQualityRuleTemplateConfigBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
