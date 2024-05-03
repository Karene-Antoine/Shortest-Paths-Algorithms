package org.example;

import javax.lang.model.type.NullType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.* ;

public class Dijkstra {
    private int[][]graph;
    public Dijkstra(int[][] graph) {
        this.graph = graph;
    }
    private class Node {
        int vertex;
        int cost;

        private Node(int vertex, int cost) {
            this.vertex = vertex;
            this.cost = cost;
        }
    }

    private void dijkstra(int source, int[] cost, int[] parent) {
        boolean[] visited = new boolean[graph.length];
        Arrays.fill(cost, Integer.MAX_VALUE);
        cost[source] = 0;

        Arrays.fill(parent , -1) ;
        parent[source] = source ;

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
                if (graph[u][v] != Integer.MAX_VALUE && !visited[v]) {
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

    public void cost_between_allpairs(int[][] cost , int [][] parent){
        for (int i = 0; i < cost.length; i++)
            dijkstra(i , cost[i] , parent[i]);
    }

    public int cost(int [] cost , int destination){
        return cost[destination] ;
    }

    public int cost_from_source(int [][] cost , int source ,  int destination){
        return cost[source][destination] ;
    }

    public String get_path(int[] parent , int[] cost , int source , int destination){
        if(cost[destination] == Integer.MAX_VALUE)
            return null;
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
                path = path + String.valueOf(stack.pop()) +" - " ;
            }
            path = path.substring(0 , path.length()-3) ;
            return path ;
        }
    }

    public String get_path_from_source (int[][] parent , int[][] cost , int source , int destination){
        return get_path(parent[source] , cost[source] , source , destination) ;
    }
//    public int[] cost_to_allnodes(int source) {
//        dijkstra(source, cost, parent);
//        return cost;
//    }
//    public ArrayList<ArrayList<Integer>> pathsFromSource(int source) {
//        dijkstra(source, cost, parent);
//        ArrayList<ArrayList<Integer>> paths = new ArrayList<>();
//        for (int i = 0; i < graph.length; i++) {
//            ArrayList<Integer> path = new ArrayList<>();
//            int current = i;
//            while (current != source) {
//                path.add(0, current);
//                current = parent[current];
//            }
//            path.add(0, source);
//            paths.add(path);
//        }
//        return paths;
//    }
//

//    public ArrayList<ArrayList<ArrayList<Integer>>> pathsBetweenAllPairs() {
//        ArrayList<ArrayList<ArrayList<Integer>>> allPaths = new ArrayList<>();
//        for (int i=0 ; i< graph.length;i++)
//        {
//            allPaths.add(pathsFromSource(i));
//        }
//        return allPaths;
//    }
//

    public static void main(String[] args) {

        int [][] graph = {{0,2,9,11} , {Integer.MAX_VALUE , 0 , 6 ,Integer.MAX_VALUE} , {Integer.MAX_VALUE , Integer.MAX_VALUE , 0 , 1} , {Integer.MAX_VALUE , Integer.MAX_VALUE , Integer.MAX_VALUE , 0}} ;
        Dijkstra dijkstra = new Dijkstra(graph) ;

        int [][] cost = new int[graph.length][graph.length];
        int [][] parent = new int [graph.length][graph.length];
        dijkstra.cost_between_allpairs(cost , parent);
        for(int i= 0 ; i<4 ; i++){
            for(int j=0 ; j<4 ; j++){
                System.out.println("cost from " +i +" to " + j + " = " + dijkstra.cost_from_source(cost , i , j)) ;
                System.out.println("path form " +j +" to " + j + " = " + dijkstra.get_path_from_source(parent , cost , i , j)) ;
            }
        }

    }
}
