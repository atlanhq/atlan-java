/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.tasks;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanTaskStatus;
import com.atlan.model.enums.AtlanTaskType;
import com.atlan.model.fields.KeywordField;
import com.atlan.model.fields.NumericField;
import com.atlan.model.fields.TextField;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed entry in the task queue. These objects should be treated as immutable.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AtlanTask extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Type of the task. */
    public static final KeywordField TYPE = new KeywordField("type", "__task_type");

    /** Unique identifier of the task. */
    public static final TextField GUID = new TextField("guid", "__task_guid");

    /** User who created the task. */
    public static final KeywordField CREATED_BY = new KeywordField("createdBy", "__task_createdBy");

    /** Time (epoch) at which the task was created, in milliseconds. */
    public static final NumericField CREATED_TIME = new NumericField("createdTime", "__task_timestamp");

    /** Time (epoch) at which the task was last updated, in milliseconds. */
    public static final NumericField UPDATED_TIME = new NumericField("updatedTime", "__task_modificationTimestamp");

    /** Time (epoch) at which the task was started, in milliseconds. */
    public static final NumericField START_TIME = new NumericField("startTime", "__task_startTime");

    /** Time (epoch) at which the task was ended, in milliseconds. */
    public static final NumericField END_TIME = new NumericField("endTime", "__task_endTime");

    /** Total time taken to complete the task, in seconds. */
    public static final NumericField TIME_TAKEN_IN_SECONDS =
            new NumericField("timeTakenInSeconds", "__task_timeTakenInSeconds");

    /** Number of times the task has been attempted. */
    public static final NumericField ATTEMPT_COUNT = new NumericField("attemptCount", "__task_attemptCount");

    /** Status of the task. */
    public static final TextField STATUS = new TextField("status", "__task_status");

    /** TBC */
    public static final KeywordField CLASSIFICATION_ID =
            new KeywordField("classificationId", "__task_classificationId");

    /** Unique identifier of the asset the task originated from. */
    public static final KeywordField ENTITY_GUID = new KeywordField("entityGuid", "__task_entityGuid");

    /** Type of the task. */
    final AtlanTaskType type;

    /** Unique identifier of the task. */
    final String guid;

    /** User who created the task. */
    final String createdBy;

    /** Time (epoch) at which the task was created, in milliseconds. */
    final Long createdTime;

    /** Time (epoch) at which the task was last updated, in milliseconds. */
    final Long updatedTime;

    /** Time (epoch) at which the task was started, in milliseconds. */
    final Long startTime;

    /** Time (epoch) at which the task was ended, in milliseconds. */
    final Long endTime;

    /** Total time taken to complete the task, in seconds. */
    final Long timeTakenInSeconds;

    /** Parameters used for running the task. */
    final Map<String, Object> parameters;

    /** Number of times the task has been attempted. */
    final Long attemptCount;

    /** Status of the task. */
    final AtlanTaskStatus status;

    /** TBC */
    final String classificationId;

    /** Unique identifier of the asset the task originated from. */
    final String entityGuid;
}
