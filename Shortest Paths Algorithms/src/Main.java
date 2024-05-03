package org.example;
import java.util.*;

public class Main {

    public void menu (String[] args , GraphAlgorithms graph_algorithms) {
        Main m = new Main() ;
        Scanner input = new Scanner (System.in) ;
        System.out.println("choose the operation you would to perform on graph");
        System.out.println("--------------------------------------------------------") ;
        System.out.println("1.Finding the shortest paths from source node to all other nodes.");
        System.out.println("2.Finding the shortest paths between all pairs of nodes.");
        System.out.println("3.Check if the graph contains a negative cycle.");
        System.out.println("4.Back.");
        System.out.println("5.Exit");
        System.out.println("-----------") ;
        System.out.print("Your choice is: ");
        int choice = input.nextInt() ;


        switch (choice)
        {
            case 1:
                System.out.println("--------------------") ;
                System.out.print("The source node is: ") ;
                int src = input.nextInt() ;
                while(true){
                    System.out.println("The choices is : 1.Dijkstra Algorithm.");
                    System.out.println("                 2.Bellman-Ford Algorithm.");
                    System.out.println("                 3.Floyd-Warshall Algorithm.");
                    System.out.println("                 4.Back to main menu.");
                    System.out.print("Your choice is : ") ;
                    int algo = input.nextInt() ;
                    if(algo ==1 || algo ==2 || algo==3){
                    /*
                        create object and arrays
                    */
                        while(true){
                            System.out.println("The required operation is: 1.the path cost to a node.");
                            System.out.println("                           2.the path itself to a node.");
                            System.out.println("                           3.Back.");
                            System.out.print("Your choice is : ");
                            int x = input.nextInt() ;

                            if(x == 3)
                                break ;

                            else if(x==1){
                                System.out.print("The destination node is: ") ;
                                int dst = input.nextInt() ;
                                /*
                                display cost
                                 */
                                System.out.println("The cost is 0") ;
                                System.out.println("---------------------") ;
                            }

                            else if(x==2){
                                System.out.print("The destination node is: ") ;
                                int dst = input.nextInt() ;
                                /*
                                display path
                                 */
                                System.out.println("The path is 0123456") ;
                                System.out.println("-----------------------------") ;
                            }
                            else{
                                System.out.println("Un defined choice");
                                System.out.println("---------------------") ;
                            }
                        }
                    }
                    else if(algo == 4)
                        break ;
                    else {
                        System.out.println("Un defined choice");
                        System.out.println("---------------------") ;
                    }
                }
                menu(args , graph_algorithms);
                break;

            case 2:
                System.out.println("--------------------") ;
                while(true){
                    System.out.println("The choices is : 1.Dijkstra Algorithm.");
                    System.out.println("                 2.Bellman-Ford Algorithm.");
                    System.out.println("                 3.Floyd-Warshall Algorithm.");
                    System.out.println("                 4.Back to main menu.");
                    System.out.print("Your choice is : ") ;
                    int algo = input.nextInt() ;
                    if(algo ==1 || algo ==2 || algo==3){
                    /*
                        create object and arrays
                    */
                        while(true){
                            System.out.println("The required operation is: 1.the path cost to a node.");
                            System.out.println("                           2.the path itself to a node.");
                            System.out.println("                           3.Back.");
                            System.out.print("Your choice is : ");
                            int x = input.nextInt() ;

                            if(x == 3)
                                break ;

                            else if(x==1){
                                System.out.print("The source node is: ") ;
                                int source = input.nextInt() ;
                                System.out.print("The destination node is: ") ;
                                int destination = input.nextInt() ;
                                /*
                                display cost
                                 */
                                System.out.println("The cost is 0") ;
                                System.out.println("-------------------") ;
                            }

                            else if(x==2){
                                System.out.print("The source node is: ") ;
                                int source = input.nextInt() ;
                                System.out.print("The destination node is: ") ;
                                int destination = input.nextInt() ;
                                /*
                                display path
                                 */
                                System.out.println("The path is 0123456") ;
                                System.out.println("---------------------------") ;
                            }
                            else{
                                System.out.println("Un defined choice");
                                System.out.println("---------------------") ;
                            }
                        }
                    }
                    else if(algo == 4)
                        break ;
                    else{
                        System.out.println("Un defined choice");
                        System.out.println("---------------------") ;
                    }
                }
                menu(args , graph_algorithms);
                break;

            case 3:
                while(true) {
                    System.out.println("The choices is : 1.Bellman-Ford Algorithm.");
                    System.out.println("                 2.Floyd-Warshall Algorithm");
                    System.out.println("                 3.Back to main menu.");
                    System.out.print("Your choice is : ") ;
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
                        System.out.println("there is or there is not negative cycle") ;
                        System.out.println() ;
                        break ;
                    }
                    else if(algo == 3)
                        break ;
                    else {
                        System.out.println("Un defined choice");
                        System.out.println("---------------------") ;
                    }
                }
                menu(args , graph_algorithms) ;
                break;

            case 4:
                /*
                reread file
                 */
                System.out.println("-----------------------------------------------------------------------------------") ;
                m.main(args) ;
                break ;

            case 5:
                System.exit(0) ;
                break ;

            default:
                System.out.println("Un defined operation") ;
                System.out.println("-------------------------") ;
                menu(args , graph_algorithms) ;
        }
    }
    public static void main(String[] args) {
        Main m = new Main() ;
        Scanner input = new Scanner (System.in) ;
        System.out.print("Enter the path of the file : ");
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
        catch (RuntimeException e){
            m.main(args) ;
        }



//        System.out.println("Enter the file path: ");
//        Scanner scanner = new Scanner(System.in);
//        String filePath = scanner.nextLine();
//        GraphAlgorithms graphAlgorithms = new GraphAlgorithms(filePath);
//        graphAlgorithms.readGraph();
//        int[] cost = new int[graphAlgorithms.size()];
//        int[] parent = new int[graphAlgorithms.size()];
//        System.out.println(graphAlgorithms.bellmanFord(0, cost, parent));
//        for (int i = 0; i < graphAlgorithms.size(); i++) {
//            System.out.println("Cost to reach " + i + " is " + cost[i]);
//         }
    }
}
