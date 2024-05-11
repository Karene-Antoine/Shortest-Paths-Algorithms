import java.io.File;
import java.io.FileNotFoundException;
import java.io.* ;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

public class GraphAlgorithms implements IGraphAlg{
    private String filePath;
    private int[][] graph;
    private int[][] edges ;
    public GraphAlgorithms(String filePath) throws IOException {
        this.filePath = filePath;
        this.readGraph();
    }

    private void readGraph() throws IOException {
        // Read the graph from the file
        File file = new File(this.filePath);
        Scanner scanner = new Scanner(file);
        // throws empty file exception
        if(!scanner.hasNextLine())
            throw new IOException() ;

        // Read number of vertices and edges
        int V = scanner.nextInt();
        int E = scanner.nextInt();

        // Initialize graph with Integer.MAX_VALUE weight for all edges except diagonal
        this.graph = new int[V][V];
        for(int i=0 ; i<V ; i++){
            for(int j=0 ; j<V ; j++){
                if(i!=j)
                    graph[i][j] = Integer.MAX_VALUE ;
            }
        }

        this.edges = new int[E][3] ;

        // Read edges
        for (int i = 0; i < E; i++) {
            int source = scanner.nextInt();
            int destination = scanner.nextInt();
            int weight = scanner.nextInt();
            // Update graph with edge weight
            this.graph[source][destination] = weight;
            this.edges[i][0] = source ;
            this.edges[i][1] = destination ;
            this.edges[i][2] = weight ;

        }
        scanner.close();
    }

    @Override
    public int size() {
        return this.graph.length;
    }



    private static class Node {
        int vertex;
        int cost;
        private Node(int vertex, int cost) {
            this.vertex = vertex;
            this.cost = cost;
        }
    }
    @Override
    public void dijkestra(int source, int[] cost, int[] parent) {
        boolean[] visited = new boolean[graph.length];
        Arrays.fill(cost, Integer.MAX_VALUE);
        cost[source] = 0;

        Arrays.fill(parent , -1) ;
        parent[source] = source ;

        PriorityQueue<GraphAlgorithms.Node> pq = new PriorityQueue<>((a, b) -> a.cost - b.cost);
        pq.add(new GraphAlgorithms.Node(source, 0));
        while (!pq.isEmpty()) {
            GraphAlgorithms.Node node = pq.poll();
            int u = node.vertex;
            visited[u] = true;
            if (cost[u] < node.cost) {
                continue;
            }
            for (int v = 0; v < this.graph[u].length; v++) {
                if (this.graph[u][v] != Integer.MAX_VALUE && !visited[v]) {
                    int newCost = cost[u] + this.graph[u][v];
                    if (newCost < cost[v]) {
                        cost[v] = newCost;
                        parent[v] = u;
                        pq.add(new GraphAlgorithms.Node(v, newCost));
                    }
                }
            }
        }
    }

    public void cost_between_allpairs_with_dijkestra(int[][] cost , int [][] parent){
        for (int i = 0; i < this.size(); i++)
            dijkestra(i , cost[i] , parent[i]);
    }

    @Override
    public boolean bellmanFord(int source, int[] cost, int[] parent) {
        //return true if there is no negative cycle
        Arrays.fill(cost, Integer.MAX_VALUE);
        cost[source] = 0;
        Arrays.fill(parent, -1);
        parent[source] = source;

        // relaxation edges with complexity O(V*E)
        for (int k = 0; k < this.size() - 1; k++) {
            int[] temp = cost.clone();
            for (int i = 0; i < this.edges.length; i++) {
                // check if cost of destination is larger than cost of source + weight of edge
                if(temp[this.edges[i][0]] != Integer.MAX_VALUE) {
                    if (temp[this.edges[i][1]] > temp[this.edges[i][0]] + this.edges[i][2]) {
                        cost[this.edges[i][1]] = temp[this.edges[i][0]] + this.edges[i][2];
                        parent[this.edges[i][1]] = this.edges[i][0];
                    }
                }
            }
        }

        // additional iteration
        for (int i = 0; i < this.edges.length; i++) {
            if(cost[this.edges[i][0]] != Integer.MAX_VALUE){
                if (cost[this.edges[i][1]] > cost[this.edges[i][0]] + this.edges[i][2]) {
                    return false;
                }
            }
        }
        return true ;
    }

    // return true if no negative cycle and false if there is negative cycle
    public boolean cost_between_all_pairs_with_bellman (int[][] cost , int[][] parent){
        boolean negative_cycle = true ;
        for (int i = 0; i < this.size(); i++)
            negative_cycle = negative_cycle && bellmanFord(i , cost[i] , parent[i]);
        return negative_cycle ;
    }

    public boolean contains_negative_cycle_with_bellman(int [] cost, int [] parent){
        boolean [] visited = new boolean[this.size()] ;
        Arrays.fill(visited , false) ;
        for(int i=0; i<this.size() ; i++){
            if(!visited[i]){
                if(!bellmanFord(i , cost , parent))
                    return true ;
                for(int j=0 ; j<this.size() ; j++){
                    if(parent[j] != -1)
                        visited[j] = true ;
                }
            }
        }
        return false ;
    }
    @Override
    public boolean floydWarshall(int[][] cost, int[][] predecessor) {
        //copy graph array into cost array
        for(int i=0 ; i<this.size() ; i++)
            System.arraycopy(this.graph[i] , 0 , cost[i] , 0 , this.size()) ;

        // initially all predecessor are -1(null) except diagonal and edges that is existed already
        for(int i=0 ; i<this.size() ; i++){
            Arrays.fill(predecessor[i] , -1);
            predecessor[i][i] = i ;
        }
        for(int i=0 ; i<this.edges.length ; i++){
            predecessor[this.edges[i][0]][this.edges[i][1]] = this.edges[i][0] ;
        }


        for(int k=0 ; k<this.size() ; k++) {
            int[][] temp = cost.clone();
            for(int i=0 ; i<this.size() ; i++){
                for(int j=0 ; j<this.size() ; j++){
                    if(temp[i][k] != Integer.MAX_VALUE && temp[k][j] != Integer.MAX_VALUE) {
                        if (temp[i][j] > temp[i][k] + temp[k][j]) {
                            cost[i][j] = temp[i][k] + temp[k][j];
                            predecessor[i][j] = predecessor[k][j] ;
                        }
                    }
                }
            }
        }

        for(int i=0 ; i<this.size() ; i++){
            if(cost[i][i]!=0)
                return false ;
        }
        return true;
    }


    public int cost(int [] cost , int destination){
        return cost[destination] ;
    }

    public int cost_from_source(int [][] cost , int source ,  int destination){
        return cost[source][destination] ;
    }

    public String get_path(int[] parent , int[] cost , int source , int destination){
        if(cost[destination] == Integer.MAX_VALUE) {
            String path = "No path from " + source + " to " + destination +"." ;
            return path ;
        }
        else {
            Stack<Integer> stack = new Stack<>() ;
            int node = destination ;
            while(node != source) {
                stack.push(node);
                node = parent[node];
            }
            stack.push(node) ;

            String path = "" ;
            while(!stack.empty()){
                path = path + stack.pop() +" -> " ;
            }
            path = path.substring(0 , path.length()-4) ;
            path = path + "." ;
            path = "The path is " + path ;
            return path ;
        }
    }

    public String get_path_from_source (int[][] parent , int[][] cost , int source , int destination){
        return get_path(parent[source] , cost[source] , source , destination) ;
    }

    public static void main(String[] args){
        //test negative cycle in bellman ford
        try {
            GraphAlgorithms graph = new GraphAlgorithms("src/test1.txt");
            int[] cost = new int[graph.size()];
            int[] parent = new int[graph.size()];
            if(graph.contains_negative_cycle_with_bellman(cost , parent))
                System.out.println("The graph contains negative cycle.");
            else
                System.out.println("The graph does not contain negative cycle.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
