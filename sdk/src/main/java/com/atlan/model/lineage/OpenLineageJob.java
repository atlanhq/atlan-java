/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.lineage;

import com.atlan.AtlanClient;
import com.atlan.model.core.AtlanObject;
import io.openlineage.client.OpenLineage;
import io.openlineage.client.OpenLineageClientUtils;
import java.net.URI;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Atlan wrapper for abstracting OpenLineage jobs.
 * <br><br>
 * A job is a process that consumes or produces datasets.
 * <br><br>
 * This is abstract, and can map to different things in different operational contexts.
 * For example, a job could be a task in a workflow orchestration system. It could also be a model,
 * a query, or a checkpoint. Depending on the system under observation, a Job can represent a small
 * or large amount of work.
 * <br><br>
 * For more details, see <a href="https://openlineage.io/docs/spec/object-model#job">OpenLineage docs</a>.
 */
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class OpenLineageJob extends AtlanObject {
    private static final long serialVersionUID = 2L;

    private OpenLineage.JobBuilder _builder;

    /** Producer definition for OpenLineage. */
    @Getter
    OpenLineage openLineage;

    /** Internal definition of the OpenLineage job. */
    OpenLineage.Job _job;

    /**
     * Pass-through constructor for an Atlan wrapping of an existing OpenLineage job.
     *
     * @param openLineage definition of the OpenLineage producer
     * @param job an OpenLineage job object that already exists
     */
    public OpenLineageJob(OpenLineage openLineage, OpenLineage.Job job) {
        this.openLineage = openLineage;
        this._job = job;
    }

    /**
     * Builds the minimal object necessary to create an OpenLineage job.
     *
     * @param connectionName name of the Spark connection in which the OpenLineage job should be created
     * @param jobName unique name of the job - if it already exists the existing job will be updated
     * @param producer URI indicating the code or software that implements this job
     * @return the minimal request necessary to create the job, as a builder
     */
    public static OpenLineageJobBuilder<?, ?> creator(String connectionName, String jobName, String producer) {
        OpenLineage ol = new OpenLineage(URI.create(producer));
        return _internal()
                .openLineage(ol)
                ._builder(ol.newJobBuilder()
                        .namespace(connectionName)
                        .name(jobName)
                        .facets(ol.newJobFacetsBuilder().build()));
    }

    // TODO: provide some intuitive way to manage the facets of the job

    /**
     * Builds the minimal object necessary to create an OpenLineage dataset.
     * Note: before you can use the dataset, you will need to wire it as either an input or output
     * using either {@code toInput()} or {@code toOutput()}.
     *
     * @param namespace name of the source of the asset (see: https://github.com/OpenLineage/OpenLineage/blob/main/spec/Naming.md)
     * @param assetName name of the asset, by OpenLineage standard (for example, DB.SCHEMA.TABLE)
     * @return the minimal request necessary to create the dataset, as a builder
     */
    public OpenLineageDataset.OpenLineageDatasetBuilder<?, ?> createDataset(String namespace, String assetName) {
        return OpenLineageDataset.creator(namespace, assetName, openLineage);
    }

    /**
     * Builds the minimal object necessary to create an OpenLineage dataset, wired to use as an input (source)
     * for lineage.
     *
     * @param namespace name of the source of the asset (see: https://github.com/OpenLineage/OpenLineage/blob/main/spec/Naming.md)
     * @param assetName name of the asset, by OpenLineage standard (for example, DB.SCHEMA.TABLE)
     * @return the minimal request necessary to create the dataset, as a builder
     */
    public OpenLineageInputDataset.OpenLineageInputDatasetBuilder<?, ?> createInput(
            String namespace, String assetName) {
        return OpenLineageInputDataset.creator(namespace, assetName, openLineage);
    }

    /**
     * Builds the minimal object necessary to create an OpenLineage dataset, wired to use as an output (target)
     * for lineage.
     *
     * @param namespace name of the source of the asset (see: https://github.com/OpenLineage/OpenLineage/blob/main/spec/Naming.md)
     * @param assetName name of the asset, by OpenLineage standard (for example, DB.SCHEMA.TABLE)
     * @return the minimal request necessary to create the dataset, as a builder
     */
    public OpenLineageOutputDataset.OpenLineageOutputDatasetBuilder<?, ?> createOutput(
            String namespace, String assetName) {
        return OpenLineageOutputDataset.creator(namespace, assetName, openLineage);
    }

    /**
     * Retrieve the Job in OpenLineage standard form.
     *
     * @return the OpenLineage standard for the Job
     */
    OpenLineage.Job get_job() {
        if (_job == null) {
            _job = _builder.build();
        }
        return _job;
    }

    /** {@inheritDoc} */
    @Override
    public String toJson(AtlanClient client) {
        return OpenLineageClientUtils.toJson(get_job());
    }
}
