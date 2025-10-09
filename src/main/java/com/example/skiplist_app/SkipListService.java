package com.example.skiplist_app;

import com.example.skiplist_app.dto.VisualizationData;
import lombok.Getter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Getter
public class SkipListService {

    private final SkipList<Integer, Player> leaderboard = new SkipList<>();
    private final List<Player> players = Collections.synchronizedList(new ArrayList<>());
    private final Random random = new Random();
    private VisualizationData.Path lastSearchPath = null;

    public SkipListService() {
        for (int i = 0; i < 100; i++) {
            int score = 100 + random.nextInt(900);
            Player player = new Player("Player-" + UUID.randomUUID().toString().substring(0, 5), score);
            players.add(player);
            leaderboard.insert(player.score, player);
        }
    }

    @Scheduled(fixedRate = 2000)
    public void simulateUpdates() {
        if (players.isEmpty()) return;

        int action = random.nextInt(100);

        if (action < 10) {
            int score = 100 + random.nextInt(900);
            Player newPlayer = new Player("Player-" + UUID.randomUUID().toString().substring(0, 5), score);
            players.add(newPlayer);
            leaderboard.insert(newPlayer.score, newPlayer);
        } else if (action < 20 && !players.isEmpty()) {
            Player playerToDelete = players.remove(random.nextInt(players.size()));
            leaderboard.delete(playerToDelete.score);
        } else if (!players.isEmpty()) {
            Player playerToUpdate = players.get(random.nextInt(players.size()));

            leaderboard.delete(playerToUpdate.score);

            int scoreChange = random.nextInt(101) - 50;
            playerToUpdate.score = Math.max(0, playerToUpdate.score + scoreChange);


            leaderboard.insert(playerToUpdate.score, playerToUpdate);
        }
    }

    public List<Player> getTopPlayers(int n) {
        List<Player> allPlayers = new ArrayList<>();
        SkipListNode<Integer, Player> current = leaderboard.head;
        while(current.down != null) {
            current = current.down;
        }
        current = current.forward;

        while (current != null) {
            allPlayers.add(current.value);
            current = current.forward;
        }
        allPlayers.sort(Comparator.comparingInt((Player p) -> p.score).reversed());
        return allPlayers.subList(0, Math.min(n, allPlayers.size()));
    }

    public void performSearch(Integer key) {
        if (key == null) {
            this.lastSearchPath = null;
            return;
        }
        List<SkipListNode<Integer, Player>> pathNodes = new ArrayList<>();
        leaderboard.search(key, pathNodes);

        List<Map<String, Object>> pathLinks = new ArrayList<>();
        for (int i = 0; i < pathNodes.size() - 1; i++) {
            pathLinks.add(Map.of(
                    "source", pathNodes.get(i).key == null ? "null" : pathNodes.get(i).key,
                    "target", pathNodes.get(i + 1).key,
                    "level", pathNodes.get(i).level
            ));
        }
        this.lastSearchPath = new VisualizationData.Path(key, pathLinks);
    }

    public VisualizationData getVisualizationData() {
        List<VisualizationData.Node> nodes = new ArrayList<>();
        List<VisualizationData.Link> links = new ArrayList<>();
        Map<Object, Integer> nodeMaxLevelMap = new HashMap<>();

        SkipListNode<Integer, Player> currentLevelHead = leaderboard.head;
        for (int i = leaderboard.head.level; i >= 0; i--) {
            SkipListNode<Integer, Player> current = currentLevelHead;
            while(current != null) {
                Object key = current.key == null ? "null" : current.key;

                nodeMaxLevelMap.putIfAbsent(key, i);

                if (current.forward != null) {
                    links.add(new VisualizationData.Link(key, current.forward.key, i));
                }
                current = current.forward;
            }
            currentLevelHead = currentLevelHead.down;
            if (currentLevelHead == null) break;
        }

        for (Map.Entry<Object, Integer> entry : nodeMaxLevelMap.entrySet()) {
            nodes.add(new VisualizationData.Node(entry.getKey(), entry.getValue()));
        }

        nodes.sort(Comparator.comparing(node -> {
            if (node.id().equals("null")) {
                return Integer.MIN_VALUE;
            }
            return (Integer) node.id();
        }));

        VisualizationData.Structure structure = new VisualizationData.Structure(leaderboard.currentMaxLevel, nodes, links);
        return new VisualizationData(structure, this.lastSearchPath);
    }
}