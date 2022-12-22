/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.lineage;

import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.Asset;
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
     * Note: this will always include both process and asset details.
     */
    @JsonProperty("guidEntityMap")
    Map<String, Asset> assetDetails;

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
                    throw new InvalidRequestException(ErrorCode.NO_GRAPH_WITH_PROCESS);
                }
            }
        }
        return graph;
    }

    /**
     * Retrieve GUIDs of assets that are immediately downstream from the originally-requested asset.
     *
     * @return collection of GUIDs of the downstream assets
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public Set<String> getDownstreamAssetGuids() throws InvalidRequestException {
        return getDownstreamAssetGuids(baseEntityGuid);
    }

    /**
     * Retrieve assets that are immediately downstream from the originally-requested asset.
     *
     * @return collection of the downstream assets
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public List<Asset> getDownstreamAssets() throws InvalidRequestException {
        return getDownstreamAssets(baseEntityGuid);
    }

    /**
     * Retrieve GUIDs of processes that run immediately downstream from the originally-requested asset.
     *
     * @return collection of GUIDs of the downstream processes
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public Set<String> getDownstreamProcessGuids() throws InvalidRequestException {
        return getDownstreamProcessGuids(baseEntityGuid);
    }

    /**
     * Retrieve GUIDs of assets that are immediately upstream from the originally-requested asset.
     *
     * @return collection of GUIDs of the upstream assets
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public Set<String> getUpstreamAssetGuids() throws InvalidRequestException {
        return getUpstreamAssetGuids(baseEntityGuid);
    }

    /**
     * Retrieve assets that are immediately upstream from the originally-requested asset.
     *
     * @return collection of the upstream assets
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public List<Asset> getUpstreamAssets() throws InvalidRequestException {
        return getUpstreamAssets(baseEntityGuid);
    }

    /**
     * Retrieve GUIDs of processes that run immediately upstream to produce the originally-requested asset.
     *
     * @return collection of GUIDs of the upstream processes
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public Set<String> getUpstreamProcessGuids() throws InvalidRequestException {
        return getUpstreamProcessGuids(baseEntityGuid);
    }

    /**
     * Retrieve GUIDs of assets that are immediately downstream from the specified asset.
     *
     * @param guid unique ID (GUID) of the asset for which to fetch downstream assets
     * @return collection of GUIDs of the downstream assets
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public Set<String> getDownstreamAssetGuids(String guid) throws InvalidRequestException {
        return getGraph().getDownstreamAssetGuids(guid);
    }

    /**
     * Retrieve GUIDs of processes that run immediately downstream from the specified asset.
     *
     * @param guid unique ID (GUID) of the asset for which to fetch downstream processes
     * @return collection of GUIDs of the downstream processes
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public Set<String> getDownstreamProcessGuids(String guid) throws InvalidRequestException {
        return getGraph().getDownstreamProcessGuids(guid);
    }

    /**
     * Retrieve GUIDs of assets that are immediately upstream from the specified asset.
     *
     * @param guid unique ID (GUID) of the asset for which to fetch upstream assets
     * @return collection of GUIDs of the upstream assets
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public Set<String> getUpstreamAssetGuids(String guid) throws InvalidRequestException {
        return getGraph().getUpstreamAssetGuids(guid);
    }

    /**
     * Retrieve GUIDs of processes that run immediately upstream to produce the specified asset.
     *
     * @param guid unique ID (GUID) of the asset for which to fetch upstream processes
     * @return collection of GUIDs of the upstream processes
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public Set<String> getUpstreamProcessGuids(String guid) throws InvalidRequestException {
        return getGraph().getUpstreamProcessGuids(guid);
    }

    /**
     * Retrieve assets that are immediately downstream from the specified asset.
     *
     * @param guid unique ID (GUID) of the asset for which to fetch downstream assets
     * @return collection of the downstream assets
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public List<Asset> getDownstreamAssets(String guid) throws InvalidRequestException {
        List<Asset> downstream = new ArrayList<>();
        Set<String> downstreamGuids = getDownstreamAssetGuids(guid);
        for (String downstreamGuid : downstreamGuids) {
            Asset one = assetDetails.get(downstreamGuid);
            if (one != null) {
                downstream.add(one);
            }
        }
        return Collections.unmodifiableList(downstream);
    }

    /**
     * Retrieve assets that are immediately upstream from the specified asset.
     *
     * @param guid unique ID (GUID) of the asset for which to fetch upstream assets
     * @return collection of the upstream assets
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public List<Asset> getUpstreamAssets(String guid) throws InvalidRequestException {
        List<Asset> upstream = new ArrayList<>();
        Set<String> upstreamGuids = getUpstreamAssetGuids(guid);
        for (String upstreamGuid : upstreamGuids) {
            Asset one = assetDetails.get(upstreamGuid);
            if (one != null) {
                upstream.add(one);
            }
        }
        return Collections.unmodifiableList(upstream);
    }

    /**
     * Retrieve all GUIDs of assets that are downstream from the originally-requested asset, across
     * multiple degrees of separation (hops), using a depth-first search traversal.
     *
     * @return list of all downstream asset GUIDs
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public List<String> getAllDownstreamAssetGuidsDFS() throws InvalidRequestException {
        return getAllDownstreamAssetGuidsDFS(baseEntityGuid);
    }

    /**
     * Retrieve all GUIDs of assets that are downstream from the specified asset, across
     * multiple degrees of separation (hops), using a depth-first search traversal.
     *
     * @param guid unique ID (GUID) of the asset for which to fetch downstream assets
     * @return list of all downstream asset GUIDs
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public List<String> getAllDownstreamAssetGuidsDFS(String guid) throws InvalidRequestException {
        return getGraph().getAllDownstreamAssetGuidsDFS(guid);
    }

    /**
     * Retrieve all assets that are downstream from the originally-requested asset, across
     * multiple degrees of separation (hops), using a depth-first search traversal.
     *
     * @return list of all downstream assets
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public List<Asset> getAllDownstreamAssetsDFS() throws InvalidRequestException {
        return getAllDownstreamAssetsDFS(baseEntityGuid);
    }

    /**
     * Retrieve all assets that are downstream from the specified asset, across
     * multiple degrees of separation (hops), using a depth-first search traversal.
     *
     * @param guid unique ID (GUID) of the asset for which to fetch downstream assets
     * @return list of all downstream assets
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public List<Asset> getAllDownstreamAssetsDFS(String guid) throws InvalidRequestException {
        List<Asset> downstream = new ArrayList<>();
        List<String> downstreamGuids = getAllDownstreamAssetGuidsDFS(guid);
        for (String downstreamGuid : downstreamGuids) {
            Asset one = assetDetails.get(downstreamGuid);
            if (one != null) {
                downstream.add(one);
            }
        }
        return Collections.unmodifiableList(downstream);
    }

    /**
     * Retrieve all GUIDs of assets that are upstream from the originally-requested asset, across
     * multiple degrees of separation (hops), using a depth-first search traversal.
     *
     * @return list of all upstream asset GUIDs
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public List<String> getAllUpstreamAssetGuidsDFS() throws InvalidRequestException {
        return getAllUpstreamAssetGuidsDFS(baseEntityGuid);
    }

    /**
     * Retrieve all GUIDs of assets that are upstream from the specified asset, across
     * multiple degrees of separation (hops), using a depth-first search traversal.
     *
     * @param guid unique ID (GUID) of the asset for which to fetch upstream assets
     * @return list of all upstream asset GUIDs
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public List<String> getAllUpstreamAssetGuidsDFS(String guid) throws InvalidRequestException {
        return getGraph().getAllUpstreamAssetGuidsDFS(guid);
    }

    /**
     * Retrieve all assets that are upstream from the originally-requested asset, across
     * multiple degrees of separation (hops), using a depth-first search traversal.
     *
     * @return list of all upstream assets
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public List<Asset> getAllUpstreamAssetsDFS() throws InvalidRequestException {
        return getAllUpstreamAssetsDFS(baseEntityGuid);
    }

    /**
     * Retrieve all assets that are upstream from the specified asset, across
     * multiple degrees of separation (hops), using a depth-first search traversal.
     *
     * @param guid unique ID (GUID) of the asset for which to fetch upstream assets
     * @return list of all upstream assets
     * @throws InvalidRequestException if the lineage was fetched with {@code hideProcess} set to false
     */
    @JsonIgnore
    public List<Asset> getAllUpstreamAssetsDFS(String guid) throws InvalidRequestException {
        List<Asset> upstream = new ArrayList<>();
        List<String> upstreamGuids = getAllUpstreamAssetGuidsDFS(guid);
        for (String upstreamGuid : upstreamGuids) {
            Asset one = assetDetails.get(upstreamGuid);
            if (one != null) {
                upstream.add(one);
            }
        }
        return Collections.unmodifiableList(upstream);
    }
}
