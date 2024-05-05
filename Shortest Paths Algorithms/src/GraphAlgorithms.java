package org.example;


import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

public class GraphAlgorithms implements IGraphAlg{
    private String filePath;
    private int[][] graph;
    private int[][] edges ;
    public GraphAlgorithms(String filePath){
        this.filePath = filePath;
        this.readGraph();
    }

    private void readGraph(){
        // Read the graph from the file
        try {
            File file = new File(this.filePath);
            Scanner scanner = new Scanner(file);

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
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        }
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
    @Override
    public boolean floydWarshall(int[][] cost, int[][] predecessor) {
        for(int i=0 ; i<this.size() ; i++)
            System.arraycopy(this.graph[i] , 0 , cost[i] , 0 , this.size()) ;

//        for(int i=0 ; i<this.size() ; i++){
//            Arrays.fill(parent[i] , -1);
//            parent[i][i] = i ;
//        }


        for(int k=0 ; k<this.size() ; k++) {
            int[][] temp = cost.clone();
            for(int i=0 ; i<this.size() ; i++){
                for(int j=0 ; j<this.size() ; j++){
                    if(temp[i][k] != Integer.MAX_VALUE && temp[k][j] != Integer.MAX_VALUE) {
                        if (temp[i][j] > temp[i][k] + temp[k][j]) {
                            cost[i][j] = temp[i][k] + temp[k][j];
                            //parent[k][j] = k;
                            //parent[i][k] = i;
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

//    public static void main(String[] args) {
//        GraphAlgorithms graph_algorithms = new GraphAlgorithms("D:/KOLYA/Term4/Data Structure/Labs/Shortest-Paths-Algorithms/negative cycle.txt") ;
//        int [][] cost = new int [graph_algorithms.size()][graph_algorithms.size()];
//        int [][] parent = new int [graph_algorithms.size()][graph_algorithms.size()];
//        graph_algorithms.floydWarshall( cost , parent) ;
//        for(int i=0 ; i< graph_algorithms.size() ; i++){
//            for (int j=0 ; j<graph_algorithms.size() ; j++){
//                System.out.println("cost from " +i + " to "+j +" = "+cost[i][j]) ;
//                System.out.println("parent = " +parent[i][j]);
//            }
//        }
//
//    }
}
