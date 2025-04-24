/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.lineage;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanConnectorType;
import io.openlineage.client.OpenLineage;
import io.openlineage.client.OpenLineageClientUtils;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Atlan wrapper for abstracting OpenLineage events.
 * <br><br>
 * An event is a point-in-time representation of the state of a run.
 * <br><br>
 * You <em>must</em> have at least two states for any run for Atlan to process it for lineage:
 * <ul>
 *     <li>{@code START} to indicate that a run has begun</li>
 *     <li>One of the following to indicate that the run has finished:
 *     <ul>
 *         <li>{@code COMPLETE} to signify that execution of the run has concluded</li>
 *         <li>{@code ABORT} to signify the run has been stopped abnormally</li>
 *         <li>{@code FAIL} to signify the run has failed</li>
 *     </ul></li>
 * </ul>
 * In addition, for lineage to show any inputs and outputs to a process in Atlan, <em>at least one</em>
 * of the events must have {@code inputs} and {@code outputs} defined. It is not necessary to include these
 * on all events; they will be merged from across all events for the same run (by matching on {@code runId}).
 * <br><br>
 * For more details, see <a href="https://openlineage.io/docs/spec/run-cycle">OpenLineage docs</a>.
 */
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class OpenLineageEvent extends AtlanObject {
    private static final long serialVersionUID = 2L;

    private OpenLineage.RunEventBuilder _builder;

    /** Complete details about the OpenLineage run. */
    OpenLineageRun run;

    /** Inputs (sources) for the lineage. */
    @Singular
    List<OpenLineageInputDataset> inputs;

    /** Outputs (targets) for the lineage. */
    @Singular
    List<OpenLineageOutputDataset> outputs;

    /** Internal definition of the OpenLineage event. */
    OpenLineage.BaseEvent _event;

    public OpenLineageEvent(OpenLineage.BaseEvent event) {
        this._event = event;
    }

    /**
     * Builds the minimal object necessary to create an OpenLineage event.
     *
     * @param run the OpenLineage run for which to create a new event
     * @return the minimal request necessary to create the event, as a builder
     */
    public static OpenLineageEventBuilder<?, ?> creator(OpenLineageRun run, OpenLineage.RunEvent.EventType type) {
        OpenLineage ol = run.getJob().getOpenLineage();
        OpenLineage.RunEventBuilder b = ol.newRunEventBuilder()
                .eventType(type)
                .eventTime(ZonedDateTime.now(ZoneId.of("UTC")))
                .run(run.get_run())
                .job(run.getJob().get_job());
        return _internal().run(run)._builder(b);
    }

    /**
     * Send the OpenLineage event to Atlan to be processed.
     *
     * @param client connectivity to an Atlan tenant
     * @throws AtlanException on any API communication issues
     */
    public void emit(AtlanClient client) throws AtlanException {
        client.openLineage.send(this, AtlanConnectorType.SPARK);
    }

    /**
     * Retrieve the Event in OpenLineage standard form.
     *
     * @return the OpenLineage standard for the Event
     */
    OpenLineage.BaseEvent get_event() {
        if (_event == null) {
            _event = _builder.inputs(inputs.stream()
                            .map(OpenLineageInputDataset::get_dataset)
                            .collect(Collectors.toList()))
                    .outputs(outputs.stream()
                            .map(OpenLineageOutputDataset::get_dataset)
                            .collect(Collectors.toList()))
                    .build();
        }
        return _event;
    }

    /** {@inheritDoc} */
    @Override
    public String toJson(AtlanClient client) {
        return OpenLineageClientUtils.toJson(get_event());
    }
}
