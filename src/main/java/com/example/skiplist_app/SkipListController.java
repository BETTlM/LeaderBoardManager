package com.example.skiplist_app;

import com.example.skiplist_app.dto.SearchRequest;
import com.example.skiplist_app.dto.VisualizationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SkipListController {

    @Autowired
    private SkipListService skipListService;

    @GetMapping("/leaderboard")
    public List<Player> getLeaderboard() {
        return skipListService.getTopPlayers(85);
    }

    @GetMapping("/visualization")
    public VisualizationData getVisualization() {
        return skipListService.getVisualizationData();
    }

    @PostMapping("/search")
    public ResponseEntity<Void> search(@RequestBody SearchRequest request) {
        skipListService.performSearch(request.key());
        return ResponseEntity.ok().build();
    }
}