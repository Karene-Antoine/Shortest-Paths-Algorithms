import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("Enter the file path: ");
        Scanner scanner = new Scanner(System.in);
        String filePath = scanner.nextLine();
        GraphAlgorithms graphAlgorithms = new GraphAlgorithms(filePath);
        graphAlgorithms.readGraph();
        int[] cost = new int[graphAlgorithms.size()];
        int[] parent = new int[graphAlgorithms.size()];
//        System.out.println(graphAlgorithms.bellmanFord(0, cost, parent));
        graphAlgorithms.dijkestra(1,cost,parent);

        for (int i = 0; i < graphAlgorithms.size(); i++) {
            if (cost[i] == Integer.MAX_VALUE) {
                System.out.println("path to " + i + " is unreachable");
            }
            else
                System.out.println("Cost to reach " + i + " is " + cost[i]);
        }

    }
}