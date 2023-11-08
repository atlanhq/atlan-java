/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.lineage;

import java.util.*;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.ToString;

/**
 * Class to represent a lineage graph using an adjacency list implementation.
 */
@ToString(callSuper = true)
class LineageGraph {

    /** Map from source GUID to target processes and asset GUIDs. */
    private final Map<String, Set<DirectedPair>> downstreamList;

    /** Map from target GUID to source processes and asset GUIDs. */
    private final Map<String, Set<DirectedPair>> upstreamList;

    public LineageGraph() {
        downstreamList = new LinkedHashMap<>();
        upstreamList = new LinkedHashMap<>();
    }

    private void addEdges(String sourceGuid, String processGuid, String targetGuid) {
        if (!downstreamList.containsKey(sourceGuid)) {
            downstreamList.put(sourceGuid, new LinkedHashSet<>());
        }
        if (!upstreamList.containsKey(targetGuid)) {
            upstreamList.put(targetGuid, new LinkedHashSet<>());
        }
        downstreamList.get(sourceGuid).add(new DirectedPair(processGuid, targetGuid));
        upstreamList.get(targetGuid).add(new DirectedPair(processGuid, sourceGuid));
    }

    /**
     * Add an edge to the lineage graph.
     *
     * @param relation a lineage relation, as returned from fetching lineage
     */
    public void addRelation(LineageRelation relation) {
        addEdges(relation.getFromEntityId(), relation.getProcessId(), relation.getToEntityId());
    }

    /**
     * Retrieve GUIDs of assets that are immediately downstream from the specified asset.
     *
     * @param guid unique ID (GUID) of the asset for which to fetch downstream assets
     * @return collection of GUIDs of the downstream assets
     */
    public Set<String> getDownstreamAssetGuids(String guid) {
        if (downstreamList.containsKey(guid)) {
            return downstreamList.get(guid).stream()
                    .map(DirectedPair::getTargetGuid)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    /**
     * Retrieve GUIDs of processes that run immediately downstream from the specified asset.
     *
     * @param guid unique ID (GUID) of the asset for which to fetch downstream processes
     * @return collection of GUIDs of the downstream processes
     */
    public Set<String> getDownstreamProcessGuids(String guid) {
        if (downstreamList.containsKey(guid)) {
            return downstreamList.get(guid).stream()
                    .map(DirectedPair::getProcessGuid)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    /**
     * Retrieve all GUIDs of assets that are downstream from the specified asset, across
     * multiple degrees of separation (hops), using a depth-first search traversal.
     *
     * @param guid unique ID (GUID) of the asset for which to fetch downstream assets
     * @return list of all downstream asset GUIDs
     */
    public List<String> getAllDownstreamAssetGuidsDFS(String guid) {
        Set<String> visited = new LinkedHashSet<>();
        ArrayDeque<String> stack = new ArrayDeque<>();
        stack.push(guid);
        while (!stack.isEmpty()) {
            String toTraverse = stack.pop();
            if (!visited.contains(toTraverse)) {
                visited.add(toTraverse);
                Set<String> downstreamGuids = getDownstreamAssetGuids(toTraverse);
                for (String downstream : downstreamGuids) {
                    if (!visited.contains(downstream)) {
                        stack.push(downstream);
                    }
                }
            }
        }
        return List.copyOf(visited);
    }

    /**
     * Retrieve GUIDs of assets that are immediately upstream from the specified asset.
     *
     * @param guid unique ID (GUID) of the asset for which to fetch upstream assets
     * @return collection of GUIDs of the upstream assets
     */
    public Set<String> getUpstreamAssetGuids(String guid) {
        if (upstreamList.containsKey(guid)) {
            return upstreamList.get(guid).stream()
                    .map(DirectedPair::getTargetGuid)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    /**
     * Retrieve GUIDs of processes that run immediately upstream to produce the specified asset.
     *
     * @param guid unique ID (GUID) of the asset for which to fetch upstream processes
     * @return collection of GUIDs of the upstream processes
     */
    public Set<String> getUpstreamProcessGuids(String guid) {
        if (upstreamList.containsKey(guid)) {
            return upstreamList.get(guid).stream()
                    .map(DirectedPair::getProcessGuid)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    /**
     * Retrieve all GUIDs of assets that are upstream from the specified asset, across
     * multiple degrees of separation (hops), using a depth-first search traversal.
     *
     * @param guid unique ID (GUID) of the asset for which to fetch upstream assets
     * @return list of all upstream asset GUIDs
     */
    public List<String> getAllUpstreamAssetGuidsDFS(String guid) {
        Set<String> visited = new LinkedHashSet<>();
        ArrayDeque<String> stack = new ArrayDeque<>();
        stack.push(guid);
        while (!stack.isEmpty()) {
            String toTraverse = stack.pop();
            if (!visited.contains(toTraverse)) {
                visited.add(toTraverse);
                Set<String> upstreamGuids = getUpstreamAssetGuids(toTraverse);
                for (String upstream : upstreamGuids) {
                    if (!visited.contains(upstream)) {
                        stack.push(upstream);
                    }
                }
            }
        }
        return List.copyOf(visited);
    }

    /** Class to internally represent the edges in the graph. */
    @Getter
    @ToString(callSuper = true)
    static class DirectedPair {
        /** Process in-between. */
        String processGuid;

        /** Target asset (either upstream or downstream via the process). */
        String targetGuid;

        DirectedPair(String processGuid, String targetGuid) {
            this.processGuid = processGuid;
            this.targetGuid = targetGuid;
        }
    }
}
