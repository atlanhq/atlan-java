/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.lineage;

import com.atlan.exception.InvalidRequestException;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanLineageDirection;
import com.atlan.net.ApiResource;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class LineageResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    /** Unique identifier of the root asset for which these lineage details exist. */
    String baseEntityGuid;

    /** Type of lineage covered by the response: upstream only, downstream only, or both. */
    AtlanLineageDirection lineageDirection;

    /** Number of degrees of separation (hops) across which the lineage was produced. */
    Integer lineageDepth;

    /** TBC */
    Long limit;

    /** TBC */
    Long offset;

    /** Whether there is more lineage upstream than returned (true) or not (false). */
    Boolean hasMoreUpstreamVertices;

    /** Whether there is more lineage downstream than returned (true) or not (false). */
    Boolean hasMoreDownstreamVertices;

    /**
     * Details of the assets that exist in the lineage graph.
     * The keys of the map are the unique identifiers (GUIDs) of each asset in lineage,
     * and their value is an object containing the requested details for each asset.
     * Note: this will always include both process and entity details.
     */
    @JsonProperty("guidEntityMap")
    Map<String, Entity> assetDetails;

    /** List of relationships describing the lineage between each asset. */
    @Getter(AccessLevel.PRIVATE)
    Set<LineageRelation> relations;

    /**
     * Mapping of assets to their upstream and downstream asset counts.
     * In theory, the keys of the map are the unique identifiers (GUIDs) of each asset in lineage,
     * and their value is an object containing a count of upstream {@code INPUT} and
     * downstream {@code OUTPUT} assets in lineage from that asset. In reality, the
     * {@code INPUT} count is missing for most assets.
     * Note: this should not be used, instead traverse the graph representation.
     */
    @Deprecated
    @Getter(AccessLevel.PRIVATE)
    Object childrenCounts;

    /** TBC */
    @Getter(AccessLevel.PRIVATE)
    Object vertexChildrenInfo;

    /** Traversable graph representation of the lineage results. */
    @JsonIgnore
    private transient LineageGraph graph = null;

    /**
     * Retrieve the graph representation of the lineage.
     *
     * @return graph representation
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    private LineageGraph getGraph() throws InvalidRequestException {
        if (graph == null) {
            graph = new LineageGraph();
            // Lazy-calculate the graph when first retrieved
            for (LineageRelation relation : relations) {
                if (relation.isFullLink()) {
                    graph.addRelation(relation);
                } else {
                    throw new InvalidRequestException(
                            "Lineage was retrieved using hideProcess = false. We do not provide a graph view in this case.",
                            "hideProcess",
                            "ATLAN-JAVA-CLIENT-400-050",
                            400,
                            null);
                }
            }
        }
        return graph;
    }

    /**
     * Retrieve GUIDs of entities that are immediately downstream from the originally-requested entity.
     *
     * @return collection of GUIDs of the downstream entities
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public Set<String> getDownstreamEntityGuids() throws InvalidRequestException {
        return getDownstreamEntityGuids(baseEntityGuid);
    }

    /**
     * Retrieve entities that are immediately downstream from the originally-requested entity.
     *
     * @return collection of the downstream entities
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public List<Entity> getDownstreamEntities() throws InvalidRequestException {
        return getDownstreamEntities(baseEntityGuid);
    }

    /**
     * Retrieve GUIDs of processes that run immediately downstream from the originally-requested entity.
     *
     * @return collection of GUIDs of the downstream processes
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public Set<String> getDownstreamProcessGuids() throws InvalidRequestException {
        return getDownstreamProcessGuids(baseEntityGuid);
    }

    /**
     * Retrieve GUIDs of entities that are immediately upstream from the originally-requested entity.
     *
     * @return collection of GUIDs of the upstream entities
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public Set<String> getUpstreamEntityGuids() throws InvalidRequestException {
        return getUpstreamEntityGuids(baseEntityGuid);
    }

    /**
     * Retrieve entities that are immediately upstream from the originally-requested entity.
     *
     * @return collection of the upstream entities
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public List<Entity> getUpstreamEntities() throws InvalidRequestException {
        return getUpstreamEntities(baseEntityGuid);
    }

    /**
     * Retrieve GUIDs of processes that run immediately upstream to produce the originally-requested entity.
     *
     * @return collection of GUIDs of the upstream processes
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public Set<String> getUpstreamProcessGuids() throws InvalidRequestException {
        return getUpstreamProcessGuids(baseEntityGuid);
    }

    /**
     * Retrieve GUIDs of entities that are immediately downstream from the specified entity.
     *
     * @param guid unique ID (GUID) of the entity for which to fetch downstream entities
     * @return collection of GUIDs of the downstream entities
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public Set<String> getDownstreamEntityGuids(String guid) throws InvalidRequestException {
        return getGraph().getDownstreamEntityGuids(guid);
    }

    /**
     * Retrieve GUIDs of processes that run immediately downstream from the specified entity.
     *
     * @param guid unique ID (GUID) of the entity for which to fetch downstream processes
     * @return collection of GUIDs of the downstream processes
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public Set<String> getDownstreamProcessGuids(String guid) throws InvalidRequestException {
        return getGraph().getDownstreamProcessGuids(guid);
    }

    /**
     * Retrieve GUIDs of entities that are immediately upstream from the specified entity.
     *
     * @param guid unique ID (GUID) of the entity for which to fetch upstream entities
     * @return collection of GUIDs of the upstream entities
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public Set<String> getUpstreamEntityGuids(String guid) throws InvalidRequestException {
        return getGraph().getUpstreamEntityGuids(guid);
    }

    /**
     * Retrieve GUIDs of processes that run immediately upstream to produce the specified entity.
     *
     * @param guid unique ID (GUID) of the entity for which to fetch upstream processes
     * @return collection of GUIDs of the upstream processes
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public Set<String> getUpstreamProcessGuids(String guid) throws InvalidRequestException {
        return getGraph().getUpstreamProcessGuids(guid);
    }

    /**
     * Retrieve entities that are immediately downstream from the specified entity.
     *
     * @param guid unique ID (GUID) of the entity for which to fetch downstream entities
     * @return collection of the downstream entities
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public List<Entity> getDownstreamEntities(String guid) throws InvalidRequestException {
        List<Entity> downstream = new ArrayList<>();
        Set<String> downstreamGuids = getDownstreamEntityGuids(guid);
        for (String downstreamGuid : downstreamGuids) {
            Entity one = assetDetails.get(downstreamGuid);
            if (one != null) {
                downstream.add(one);
            }
        }
        return Collections.unmodifiableList(downstream);
    }

    /**
     * Retrieve entities that are immediately upstream from the specified entity.
     *
     * @param guid unique ID (GUID) of the entity for which to fetch upstream entities
     * @return collection of the upstream entities
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public List<Entity> getUpstreamEntities(String guid) throws InvalidRequestException {
        List<Entity> upstream = new ArrayList<>();
        Set<String> upstreamGuids = getUpstreamEntityGuids(guid);
        for (String upstreamGuid : upstreamGuids) {
            Entity one = assetDetails.get(upstreamGuid);
            if (one != null) {
                upstream.add(one);
            }
        }
        return Collections.unmodifiableList(upstream);
    }

    /**
     * Retrieve all GUIDs of entities that are downstream from the originally-requested entity, across
     * multiple degrees of separation (hops), using a depth-first search traversal.
     *
     * @return list of all downstream entity GUIDs
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public List<String> getAllDownstreamEntityGuidsDFS() throws InvalidRequestException {
        return getAllDownstreamEntityGuidsDFS(baseEntityGuid);
    }

    /**
     * Retrieve all GUIDs of entities that are downstream from the specified entity, across
     * multiple degrees of separation (hops), using a depth-first search traversal.
     *
     * @param guid unique ID (GUID) of the entity for which to fetch downstream entities
     * @return list of all downstream entity GUIDs
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public List<String> getAllDownstreamEntityGuidsDFS(String guid) throws InvalidRequestException {
        return getGraph().getAllDownstreamEntityGuidsDFS(guid);
    }

    /**
     * Retrieve all entities that are downstream from the originally-requested entity, across
     * multiple degrees of separation (hops), using a depth-first search traversal.
     *
     * @return list of all downstream entities
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public List<Entity> getAllDownstreamEntitiesDFS() throws InvalidRequestException {
        return getAllDownstreamEntitiesDFS(baseEntityGuid);
    }

    /**
     * Retrieve all entities that are downstream from the specified entity, across
     * multiple degrees of separation (hops), using a depth-first search traversal.
     *
     * @param guid unique ID (GUID) of the entity for which to fetch downstream entities
     * @return list of all downstream entities
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public List<Entity> getAllDownstreamEntitiesDFS(String guid) throws InvalidRequestException {
        List<Entity> downstream = new ArrayList<>();
        List<String> downstreamGuids = getAllDownstreamEntityGuidsDFS(guid);
        for (String downstreamGuid : downstreamGuids) {
            Entity one = assetDetails.get(downstreamGuid);
            if (one != null) {
                downstream.add(one);
            }
        }
        return Collections.unmodifiableList(downstream);
    }

    /**
     * Retrieve all GUIDs of entities that are upstream from the originally-requested entity, across
     * multiple degrees of separation (hops), using a depth-first search traversal.
     *
     * @return list of all upstream entity GUIDs
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public List<String> getAllUpstreamEntityGuidsDFS() throws InvalidRequestException {
        return getAllUpstreamEntityGuidsDFS(baseEntityGuid);
    }

    /**
     * Retrieve all GUIDs of entities that are upstream from the specified entity, across
     * multiple degrees of separation (hops), using a depth-first search traversal.
     *
     * @param guid unique ID (GUID) of the entity for which to fetch upstream entities
     * @return list of all upstream entity GUIDs
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public List<String> getAllUpstreamEntityGuidsDFS(String guid) throws InvalidRequestException {
        return getGraph().getAllUpstreamEntityGuidsDFS(guid);
    }

    /**
     * Retrieve all entities that are upstream from the originally-requested entity, across
     * multiple degrees of separation (hops), using a depth-first search traversal.
     *
     * @return list of all upstream entities
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public List<Entity> getAllUpstreamEntitiesDFS() throws InvalidRequestException {
        return getAllUpstreamEntitiesDFS(baseEntityGuid);
    }

    /**
     * Retrieve all entities that are upstream from the specified entity, across
     * multiple degrees of separation (hops), using a depth-first search traversal.
     *
     * @param guid unique ID (GUID) of the entity for which to fetch upstream entities
     * @return list of all upstream entities
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public List<Entity> getAllUpstreamEntitiesDFS(String guid) throws InvalidRequestException {
        List<Entity> upstream = new ArrayList<>();
        List<String> upstreamGuids = getAllUpstreamEntityGuidsDFS(guid);
        for (String upstreamGuid : upstreamGuids) {
            Entity one = assetDetails.get(upstreamGuid);
            if (one != null) {
                upstream.add(one);
            }
        }
        return Collections.unmodifiableList(upstream);
    }
}
