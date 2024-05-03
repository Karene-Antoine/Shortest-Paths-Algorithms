import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

public class GraphAlgorithms implements IGraphAlg{
    private String filePath;




//    public int[][] getGraph() {
//        return graph;
//    }
//
//    public void setGraph(int[][] graph) {
//        this.graph = graph;
//    }

    public int[][] graph;
    public GraphAlgorithms(String filePath){
        this.filePath = filePath;
    }

    public void readGraph(){
        // Read the graph from the file
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            // Read number of vertices and edges
            int V = scanner.nextInt();
            int E = scanner.nextInt();

            // Initialize graph with IntegeR.max_value weight for all edges
            graph = new int[V][V];'
            for(int i=0 ; i<V ; i++)
                Arrays.fill(graph[i] , Integer.MAX_VALUE) ;

            // Read edges
            for (int i = 0; i < E; i++) {
                int source = scanner.nextInt();
                int destination = scanner.nextInt();
                int weight = scanner.nextInt();

                // Update graph with edge weight
                graph[source][destination] = weight;
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
    @Override
    public int size() {
        return graph.length;
    }

//    @Override
//    public void dijkestra(int source, int[] cost, int[] parent) {
//        boolean[] visited = new boolean[graph.length];
//        Arrays.fill(cost, Integer.MAX_VALUE);
//        cost[source] = 0;
//        PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> a.cost - b.cost);
//        pq.add(new Node(source, 0));
//        while (!pq.isEmpty()) {
//            Node node = pq.poll();
//            int u = node.vertex;
//            visited[u] = true;
//            if (cost[u] < node.cost) {
//                continue;
//            }
//            for (int v = 0; v < graph[u].length; v++) {
//                if (graph[u][v] != 0 && !visited[v]) {
//                    int newCost = cost[u] + graph[u][v];
//                    if (newCost < cost[v]) {
//                        cost[v] = newCost;
//                        parent[v] = u;
//                        pq.add(new Node(v, newCost));
//                    }
//                }
//            }
//        }
//
//    }

    @Override
    public boolean bellmanFord(int source, int[] cost, int[] parent) {
        //return true if there is no negative cycle
        Arrays.fill(cost, Integer.MAX_VALUE);
        cost[source] = 0;
        for (int i = 0; i < size() - 1; i++) {
            for (int u = 0; u < size(); u++) {
                for (int v = 0; v < size(); v++) {
                    if (graph[u][v] != 0) {
                        if (cost[v] > cost[u] + graph[u][v]) {
                            cost[v] = cost[u] + graph[u][v];
                            parent[v] = u;
                        }
                    }
                }
            }
        }
        for (int u = 0; u < size(); u++) {
            for (int v = 0; v < size(); v++) {
                if (graph[u][v] != 0) {
                    if (cost[v] > cost[u] + graph[u][v]) {
                        return false ;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean floydWarshall(int[][] cost, int[][] predecessor) {
        return false;
    }


}
