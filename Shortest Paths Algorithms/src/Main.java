package org.example;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static final String ANSI_RED = "\u001B[31m" ;
    public static final String ANSI_RESET = "\u001B[34m" ;
    public void menu (String[] args , GraphAlgorithms graph_algorithms) {
        Main m = new Main() ;
        Scanner input = new Scanner (System.in) ;
        System.out.println(ANSI_RESET + "choose the operation you would to perform on graph");
        System.out.println(ANSI_RESET + "--------------------------------------------------------") ;
        System.out.println(ANSI_RESET + "1.Finding the shortest paths from source node to all other nodes.");
        System.out.println(ANSI_RESET + "2.Finding the shortest paths between all pairs of nodes.");
        System.out.println(ANSI_RESET + "3.Check if the graph contains a negative cycle.");
        System.out.println(ANSI_RESET + "4.Back.");
        System.out.println(ANSI_RESET + "5.Exit");
        System.out.println(ANSI_RESET + "-----------") ;
        System.out.print(ANSI_RESET + "Your choice is: ");
        int choice = input.nextInt() ;


        switch (choice)
        {
            case 1:
                System.out.println(ANSI_RESET + "--------------------") ;
                System.out.print(ANSI_RESET + "The source node is: ") ;
                int src = input.nextInt() ;
                while(true){
                    System.out.println(ANSI_RESET + "The choices is : 1.Dijkstra Algorithm.");
                    System.out.println(ANSI_RESET + "                 2.Bellman-Ford Algorithm.");
                    System.out.println(ANSI_RESET + "                 3.Floyd-Warshall Algorithm.");
                    System.out.println(ANSI_RESET + "                 4.Back to main menu.");
                    System.out.print(ANSI_RESET + "Your choice is : ") ;
                    int algo = input.nextInt() ;
                    if(algo ==1 || algo ==2 || algo==3){
                    /*
                        create object and arrays
                    */
                        int [] cost = new int [graph_algorithms.size()] ;
                        int [] parent = new int [graph_algorithms.size()] ;

                        if(algo==1){
                            graph_algorithms.dijkestra(src , cost , parent);
                        }
                        else if(algo==2){
                            graph_algorithms.bellmanFord(src , cost , parent) ;
                        } else if (algo==3) {
                            int [][] costs = new int [graph_algorithms.size()][graph_algorithms.size()] ;
                            int [][] predecessor = new int [graph_algorithms.size()][graph_algorithms.size()] ;
                            graph_algorithms.floydWarshall(costs , predecessor) ;
                            cost = costs[src] ;
                            parent = predecessor[src] ;
                        }
                        while(true){
                            System.out.println(ANSI_RESET + "The required operation is: 1.the path cost to a node.");
                            System.out.println(ANSI_RESET + "                           2.the path itself to a node.");
                            System.out.println(ANSI_RESET + "                           3.Back.");
                            System.out.print(ANSI_RESET + "Your choice is : ");
                            int x = input.nextInt() ;

                            if(x == 3)
                                break ;

                            else if(x==1){
                                System.out.print(ANSI_RESET + "The destination node is: ") ;
                                int dst = input.nextInt() ;
                                /*
                                display cost
                                 */
                                int COST = graph_algorithms.cost(cost , dst) ;
                                if(COST == Integer.MAX_VALUE)
                                    System.out.println(ANSI_RED+ "The cost = INFINITY .") ;
                                else
                                    System.out.println(ANSI_RED + "The cost = "+COST + ".");
                                System.out.println(ANSI_RESET + "---------------------") ;
                            }

                            else if(x==2){
                                System.out.print(ANSI_RESET + "The destination node is: ") ;
                                int dst = input.nextInt() ;
                                /*
                                display path
                                 */
                                System.out.println(ANSI_RED+ graph_algorithms.get_path(parent , cost , src , dst)) ;
                                System.out.println(ANSI_RESET + "-----------------------------") ;
                            }
                            else{
                                System.out.println(ANSI_RESET + "Un defined choice");
                                System.out.println(ANSI_RESET + "---------------------") ;
                            }
                        }
                    }
                    else if(algo == 4)
                        break ;
                    else {
                        System.out.println(ANSI_RESET + "Un defined choice");
                        System.out.println(ANSI_RESET + "---------------------") ;
                    }
                }
                menu(args , graph_algorithms);
                break;

            case 2:
                System.out.println(ANSI_RESET + "--------------------") ;
                while(true){
                    System.out.println(ANSI_RESET + "The choices is : 1.Dijkstra Algorithm.");
                    System.out.println(ANSI_RESET + "                 2.Bellman-Ford Algorithm.");
                    System.out.println(ANSI_RESET + "                 3.Floyd-Warshall Algorithm.");
                    System.out.println(ANSI_RESET + "                 4.Back to main menu.");
                    System.out.print(ANSI_RESET + "Your choice is : ") ;
                    int algo = input.nextInt() ;
                    if(algo ==1 || algo ==2 || algo==3){
                    /*
                        create object and arrays
                    */
                        int[][] cost = new int [graph_algorithms.size()][graph_algorithms.size()] ;
                        int[][] parent = new int[graph_algorithms.size()][graph_algorithms.size()] ;
                        if(algo==1){
                            graph_algorithms.cost_between_allpairs_with_dijkestra(cost , parent);
                        }
                        else if(algo==2){
                            graph_algorithms.cost_between_all_pairs_with_bellman(cost , parent) ;
                        }
                        else if(algo==3){
                            graph_algorithms.floydWarshall(cost , parent) ;
                        }
                        while(true){
                            System.out.println(ANSI_RESET + "The required operation is: 1.the path cost to a node.");
                            System.out.println(ANSI_RESET + "                           2.the path itself to a node.");
                            System.out.println(ANSI_RESET + "                           3.Back.");
                            System.out.print(ANSI_RESET + "Your choice is : ");
                            int x = input.nextInt() ;

                            if(x == 3)
                                break ;

                            else if(x==1){
                                System.out.print(ANSI_RESET + "The source node is: ") ;
                                int source = input.nextInt() ;
                                System.out.print(ANSI_RESET + "The destination node is: ") ;
                                int destination = input.nextInt() ;
                                /*
                                display cost
                                 */
                                int COST = graph_algorithms.cost_from_source( cost , source , destination) ;
                                if(COST == Integer.MAX_VALUE)
                                    System.out.println(ANSI_RED + "The cost = INFINITY .") ;
                                else
                                    System.out.println(ANSI_RED + "The cost = "+COST + ".");
                                System.out.println(ANSI_RESET + "-------------------") ;
                            }

                            else if(x==2){
                                System.out.print(ANSI_RESET + "The source node is: ") ;
                                int source = input.nextInt() ;
                                System.out.print(ANSI_RESET + "The destination node is: ") ;
                                int destination = input.nextInt() ;
                                /*
                                display path
                                 */
                                System.out.println(ANSI_RED + graph_algorithms.get_path_from_source(parent,cost ,source,destination)) ;
                                System.out.println(ANSI_RESET + "---------------------------") ;
                            }
                            else{
                                System.out.println(ANSI_RESET + "Un defined choice");
                                System.out.println(ANSI_RESET + "---------------------") ;
                            }
                        }
                    }
                    else if(algo == 4)
                        break ;
                    else{
                        System.out.println(ANSI_RESET + "Un defined choice");
                        System.out.println(ANSI_RESET + "---------------------") ;
                    }
                }
                menu(args , graph_algorithms);
                break;

            case 3:
                while(true) {
                    System.out.println(ANSI_RESET + "The choices is : 1.Bellman-Ford Algorithm.");
                    System.out.println(ANSI_RESET + "                 2.Floyd-Warshall Algorithm");
                    System.out.println(ANSI_RESET + "                 3.Back to main menu.");
                    System.out.print(ANSI_RESET + "Your choice is : ") ;
                    int algo = input.nextInt() ;
                    /*
                    if(algo ==1 || algo ==2)
                    make output then break loop
                    if not continue in the loop
                     */
                    if (algo==1 || algo==2){
                        /*
                        perform operation
                         */
                        boolean negative_cycle ;
                        int [][] cost = new int[graph_algorithms.size()][graph_algorithms.size()];
                        int [][] parent = new int[graph_algorithms.size()][graph_algorithms.size()];
                        if(algo==1){
                            negative_cycle = graph_algorithms.cost_between_all_pairs_with_bellman(cost,parent) ;
                        }
                        else{
                            negative_cycle = graph_algorithms.floydWarshall(cost , parent) ;
                        }
                        if(negative_cycle)
                            System.out.println(ANSI_RED+ "there isn\'t a negative cycle in the graph") ;
                        else
                            System.out.println(ANSI_RED + "there is a negative cycle in the graph") ;
                        System.out.println() ;
                        break ;
                    }
                    else if(algo == 3)
                        break ;
                    else {
                        System.out.println(ANSI_RESET + "Un defined choice");
                        System.out.println(ANSI_RESET + "---------------------") ;
                    }
                }
                menu(args , graph_algorithms) ;
                break;

            case 4:
                /*
                reread file
                 */
                System.out.println(ANSI_RESET + "-----------------------------------------------------------------------------------") ;
                m.main(args) ;
                break ;

            case 5:
                System.exit(0) ;
                break ;

            default:
                System.out.println(ANSI_RESET + "Un defined operation") ;
                System.out.println(ANSI_RESET + "-------------------------") ;
                menu(args , graph_algorithms) ;
        }
    }
    public static void main(String[] args) {
        Main m = new Main() ;
        Scanner input = new Scanner (System.in) ;
        System.out.print(ANSI_RESET + "Enter the path of the file : ");
        String path = input.nextLine() ;
        /*
            create object of graph
            if created successfully go to menu
            if not return to main.
        */
        try{
            GraphAlgorithms graph_algorithms = new GraphAlgorithms(path) ;
            m.menu(args , graph_algorithms) ;
        }
        catch (FileNotFoundException e){
            m.main(args) ;
        }

    }
}
