import javax.lang.model.type.NullType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class Dijksta {
    int[]cost;
    int[]parent;
    int[][]graph;

    public Dijksta(int[] cost, int[] parent, int[][] graph) {
        this.cost = cost;
        this.parent = parent;
        this.graph = graph;
    }
    private class Node {
        int vertex;
        int cost;

        public Node(int vertex, int cost) {
            this.vertex = vertex;
            this.cost = cost;
        }
    }

    private void dijkstra(int source, int[] cost, int[] parent) {
        boolean[] visited = new boolean[graph.length];
        Arrays.fill(cost, Integer.MAX_VALUE);
        cost[source] = 0;
        PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> a.cost - b.cost);
        pq.add(new Node(source, 0));
        while (!pq.isEmpty()) {
            Node node = pq.poll();
            int u = node.vertex;
            visited[u] = true;
            if (cost[u] < node.cost) {
                continue;
            }
            for (int v = 0; v < graph[u].length; v++) {
                if (graph[u][v] != 0 && !visited[v]) {
                    int newCost = cost[u] + graph[u][v];
                    if (newCost < cost[v]) {
                        cost[v] = newCost;
                        parent[v] = u;
                        pq.add(new Node(v, newCost));
                    }
                }
            }
        }

    }

    public int[] cost_to_allnodes(int source) {
        dijkstra(source, cost, parent);
        return cost;
    }
    public ArrayList<ArrayList<Integer>> pathsFromSource(int source) {
        dijkstra(source, cost, parent);
        ArrayList<ArrayList<Integer>> paths = new ArrayList<>();
        for (int i = 0; i < graph.length; i++) {
            ArrayList<Integer> path = new ArrayList<>();
            int current = i;
            while (current != source) {
                path.add(0, current);
                current = parent[current];
            }
            path.add(0, source);
            paths.add(path);
        }
        return paths;
    }

    public int[][] cost_between_allpairs(){
        int n = cost.length;
        int[][] cost_between = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                cost_between[i][j] = cost_to_allnodes(i)[j];
            }
        }
        return cost_between;
    }
    public ArrayList<ArrayList<ArrayList<Integer>>> pathsBetweenAllPairs() {
        ArrayList<ArrayList<ArrayList<Integer>>> allPaths = new ArrayList<>();
        for (int i=0 ; i< graph.length;i++)
        {
            allPaths.add(pathsFromSource(i));
        }
        return allPaths;
    }


}
