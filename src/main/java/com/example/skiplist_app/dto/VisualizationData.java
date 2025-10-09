package com.example.skiplist_app.dto;

import java.util.List;
import java.util.Map;

public record VisualizationData(
    Structure structure,
    Path path
) {
    public record Structure(int maxLevel, List<Node> nodes, List<Link> links) {}
    public record Node(Object id, int level) {}
    public record Link(Object source, Object target, int level) {}
    public record Path(Object searchKey, List<Map<String, Object>> path) {}
}