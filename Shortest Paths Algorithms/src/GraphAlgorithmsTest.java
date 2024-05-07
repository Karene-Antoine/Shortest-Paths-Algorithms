import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
class GraphAlgorithmsTest {
    static int maxWeight = 100; // maximum weight of each edge
    static int samples = 10;
    static int vertices = 100;
    static double density = 0.2;
    static long totalTimeDijkestra = 0;
    static long totalTimeBellmanFord = 0;
    static long totalTimeFloydWarshall = 0;
    static long totalTimeDijkestraAllPair = 0;
    static long totalTimeBellmanFordAllPair = 0;
    static long totalTimeFloydWarshallAllPair = 0;

    private static Stream<int[][]> graphProvider() {
        Random rand = new Random();
        int edges;
        edges = (int) (density * vertices * (vertices - 1));

        return Stream.generate(() -> {
            int[][] graph = new int[edges][3];
            for (int i = 0; i < edges; i++) {
                graph[i][0] = rand.nextInt(vertices);
                graph[i][1] = rand.nextInt(vertices);
                while (graph[i][0] == graph[i][1]) {
                    graph[i][1] = rand.nextInt(vertices);
                }
                graph[i][2] = rand.nextInt(maxWeight) + 1;
            }
            return graph;
        }).limit(samples); // generate 10 graphs
    }
    @ParameterizedTest
    @MethodSource("graphProvider")
    public void performanceTestSingleSource(int[][] graph) throws IOException {

        // Create a temporary file
        Path tempFile = Files.createTempFile("testcase", ".txt");

        try {
            // Writes the graph to the temporary file
            try (PrintWriter writer = new PrintWriter(tempFile.toFile())) {
                writer.println(vertices + " " + graph.length);
                for (int[] edge : graph) {
                    writer.println(edge[0] + " " + edge[1] + " " + edge[2]);
                }
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file.");
                e.printStackTrace();
            }

            GraphAlgorithms graphAlgorithms = new GraphAlgorithms(tempFile.toString());

            int [] cost = new int [graphAlgorithms.size()] ;
            int [] parent = new int [graphAlgorithms.size()] ;

            // test Dijkestra
            long startTime = System.nanoTime();
            graphAlgorithms.dijkestra(0, cost, parent);
            graphAlgorithms.cost( cost, vertices/2);
            long endTime = System.nanoTime();

            long duration = endTime - startTime;
            totalTimeDijkestra += duration;

            cost = new int [graphAlgorithms.size()] ;
            parent = new int [graphAlgorithms.size()] ;

            // test BellmanFord
            startTime = System.nanoTime();
            graphAlgorithms.bellmanFord(0, cost, parent);
            graphAlgorithms.cost( cost, vertices/2);
            endTime = System.nanoTime();

            duration = endTime - startTime;
            totalTimeBellmanFord += duration;

            // test FloydWarshall
            int [][] costMatrix = new int [graphAlgorithms.size()][graphAlgorithms.size()] ;
            int [][] predecessor = new int [graphAlgorithms.size()][graphAlgorithms.size()] ;
            startTime = System.nanoTime();
            graphAlgorithms.floydWarshall(costMatrix, predecessor);
            graphAlgorithms.cost_from_source(costMatrix, 0, vertices/2);
            endTime = System.nanoTime();

            duration = endTime - startTime;
            totalTimeFloydWarshall += duration;



        } finally {
            // Deletes the temporary file
            Files.delete(tempFile);

        }
    }

    @ParameterizedTest
    @MethodSource("graphProvider")
    public void performanceTestAllPair(int[][] graph) throws IOException {

        // Create a temporary file
        Path tempFile = Files.createTempFile("testcase", ".txt");

        try {
            // Writes the graph to the temporary file
            try (PrintWriter writer = new PrintWriter(tempFile.toFile())) {
                writer.println(vertices + " " + graph.length);
                for (int[] edge : graph) {
                    writer.println(edge[0] + " " + edge[1] + " " + edge[2]);
                }
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file.");
                e.printStackTrace();
            }

            GraphAlgorithms graphAlgorithms = new GraphAlgorithms(tempFile.toString());

            int [][] costMatrix = new int [graphAlgorithms.size()][graphAlgorithms.size()] ;
            int [][] predecessor = new int [graphAlgorithms.size()][graphAlgorithms.size()] ;

            // test Dijkestra
            long startTime = System.nanoTime();
            graphAlgorithms.cost_between_allpairs_with_dijkestra(costMatrix, predecessor);
            long endTime = System.nanoTime();

            long duration = endTime - startTime;
            totalTimeDijkestraAllPair += duration;

            costMatrix = new int [graphAlgorithms.size()][graphAlgorithms.size()] ;
            predecessor = new int [graphAlgorithms.size()][graphAlgorithms.size()] ;

            // test BellmanFord
            startTime = System.nanoTime();
            graphAlgorithms.cost_between_all_pairs_with_bellman(costMatrix, predecessor);
            endTime = System.nanoTime();

            duration = endTime - startTime;
            totalTimeBellmanFordAllPair += duration;

            // test FloydWarshall
            costMatrix = new int [graphAlgorithms.size()][graphAlgorithms.size()] ;
            predecessor = new int [graphAlgorithms.size()][graphAlgorithms.size()] ;
            startTime = System.nanoTime();
            graphAlgorithms.floydWarshall(costMatrix, predecessor);
            endTime = System.nanoTime();

            duration = endTime - startTime;
            totalTimeFloydWarshallAllPair += duration;



        } finally {
            // Deletes the temporary file
            Files.delete(tempFile);

        }
    }
    @AfterAll
    public static void printResults() {
        System.out.print("At V= " + vertices + ", density= "+ density + ", i.e " + format(density, vertices) + " :\n");
        System.out.println("Single Source Dijkstra took on average:  " + format(totalTimeDijkestra / samples));
        System.out.println("Single Source Bellman-Ford took on average:  " + format(totalTimeBellmanFord / samples));
        System.out.println("Single Source Floyd-Warshall took on average:  " + format(totalTimeFloydWarshall / samples));
        System.out.println();
        System.out.println("All Pair Dijkstra took on average:  " + format(totalTimeDijkestraAllPair / samples));
        System.out.println("All Pair Bellman-Ford took on average:  " + format(totalTimeBellmanFordAllPair / samples));
        System.out.println("All Pair Floyd-Warshall took on average:  " + format(totalTimeFloydWarshallAllPair / samples));
    }

    static String format(long nanoseconds){
        if (nanoseconds < 1000) return String.format("%d ns", nanoseconds);
        if (nanoseconds < 1000000) return String.format("%d us", nanoseconds / 1000);
        if (nanoseconds < 1000000000) return String.format("%d ms", nanoseconds / 1000000);
        return String.format("%.2f seconds", nanoseconds / 1000000000.0);
    }

    static String format(double density, int vertices){
        int val = (int) (density * vertices * (vertices - 1));
        if(val > vertices*vertices) return "E is O(V^2)";
        if(val > (int)vertices*Math.sqrt(vertices)) return "E is O(V^2)";
        return "E is O(V)";
    }

    /***Special Cases***/
    @Test
    void fileNotFound() {
        assertThrows(IOException.class, () -> new GraphAlgorithms("/dummy/path.txt"));
    }
    @Test
    void emptyFile() {
        assertThrows(IOException.class, () -> new GraphAlgorithms("D:\\Koleya\\CSED Year 2 (2023-2024)\\2nd Semester - Spring 2024\\DS II\\Labs\\Lab 3\\Shortest-Paths-Algorithms\\empty.txt"));
    }
    @Test
    void testSize() throws IOException {
        GraphAlgorithms graph = new GraphAlgorithms("D:\\Koleya\\CSED Year 2 (2023-2024)\\2nd Semester - Spring 2024\\DS II\\Labs\\Lab 3\\Shortest-Paths-Algorithms\\empty_graph.txt");
        assertEquals(0, graph.size());
        graph = new GraphAlgorithms("D:\\Koleya\\CSED Year 2 (2023-2024)\\2nd Semester - Spring 2024\\DS II\\Labs\\Lab 3\\Shortest-Paths-Algorithms\\size_test.txt");
        assertEquals(5, graph.size());
    }
    @Test
    void testNonExistentSource() throws IOException {
        /*Test Algorithm With a source node that doesn't exist*/
        GraphAlgorithms graph = new GraphAlgorithms("D:\\Koleya\\CSED Year 2 (2023-2024)\\2nd Semester - Spring 2024\\DS II\\Labs\\Lab 3\\Shortest-Paths-Algorithms\\dijkestra.txt");
        int[] cost = new int[graph.size()];
        int[] parent = new int[graph.size()];
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> graph.dijkestra(6, cost, parent));
    }
    @Test
    void testNonExistentDestination() throws IOException {
        /*Test Algorithm With a source node that doesn't exist*/
        GraphAlgorithms graph = new GraphAlgorithms("D:\\Koleya\\CSED Year 2 (2023-2024)\\2nd Semester - Spring 2024\\DS II\\Labs\\Lab 3\\Shortest-Paths-Algorithms\\dijkestra.txt");
        int[] cost = new int[graph.size()];
        int[] parent = new int[graph.size()];
        graph.dijkestra(0, cost, parent);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> System.out.println(cost[6]));
    }
    @Test
    void testNonExistentPath() throws IOException {
        GraphAlgorithms graph = new GraphAlgorithms("D:\\Koleya\\CSED Year 2 (2023-2024)\\2nd Semester - Spring 2024\\DS II\\Labs\\Lab 3\\Shortest-Paths-Algorithms\\dijkestra.txt");
        int[] cost = new int[graph.size()];
        int[] parent = new int[graph.size()];
        graph.dijkestra(2, cost, parent);
        assertEquals(Integer.MAX_VALUE, cost[1]);
        assertEquals(-1, parent[1]);
    }
    @Test
    void testNormalDijkstra() throws IOException {
        GraphAlgorithms graph = new GraphAlgorithms("D:\\Koleya\\CSED Year 2 (2023-2024)\\2nd Semester - Spring 2024\\DS II\\Labs\\Lab 3\\Shortest-Paths-Algorithms\\dijkestra.txt");
        int[][] cost = new int[graph.size()][graph.size()];
        int[][] parent = new int[graph.size()][graph.size()];
        graph.cost_between_allpairs_with_dijkestra(cost, parent);
        /*Test Normal Algorithm Run*/
        int[][] expectedCost = new int[][]{
                {0, 2, 3, 8, 6, 9},
                {Integer.MAX_VALUE, 0, 1, 6, 4, 7},
                {Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 5, 3, 6},
                {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, 1},
                {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 2, 0, 3},
                {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 0}
        };
        assertArrayEquals(expectedCost, cost);
        int[][] expectedParents = new int[][]{
                {0, 0, 1, 4, 2, 3},
                {-1, 1, 1, 4, 2, 3},
                {-1, -1, 2, 4, 2, 3},
                {-1, -1, -1, 3, -1, 3},
                {-1, -1, -1, 4, 4, 3},
                {-1, -1, -1, -1, -1, 5}
        };
        assertArrayEquals(expectedParents, parent);
    }
    @Test
    void testNormalBellmanFord() throws IOException {
        GraphAlgorithms graph = new GraphAlgorithms("D:\\Koleya\\CSED Year 2 (2023-2024)\\2nd Semester - Spring 2024\\DS II\\Labs\\Lab 3\\Shortest-Paths-Algorithms\\bellman.txt");
        int[] cost = new int[graph.size()];
        int[] parent = new int[graph.size()];
        graph.bellmanFord(2, cost, parent);
        /*Test Normal Algorithm Run*/
        assertArrayEquals(new int[]{Integer.MAX_VALUE, -2, 0, Integer.MAX_VALUE, -3, Integer.MAX_VALUE, 0}, cost);
        assertArrayEquals(new int[]{-1, 2, 2, -1, 1, -1, 4}, parent);
    }
    @Test
    void testNormalFloydWarshall() throws IOException {
        GraphAlgorithms graph = new GraphAlgorithms("D:\\Koleya\\CSED Year 2 (2023-2024)\\2nd Semester - Spring 2024\\DS II\\Labs\\Lab 3\\Shortest-Paths-Algorithms\\floyd.txt");
        int[][] cost = new int[graph.size()][graph.size()];
        int[][] parent = new int[graph.size()][graph.size()];
        graph.floydWarshall(cost, parent);
        /*Test Normal Algorithm Run*/
        int[][] expectedCost = new int[][]{
                {0, 3, 5, 6},
                {5, 0, 2, 3},
                {3, 6, 0, 1},
                {2, 5, 7, 0},
        };
        assertArrayEquals(expectedCost, cost);
    }
    @Test
    void testDijkstraWithNegativeWeights() throws IOException {
        GraphAlgorithms graph = new GraphAlgorithms("D:\\Koleya\\CSED Year 2 (2023-2024)\\2nd Semester - Spring 2024\\DS II\\Labs\\Lab 3\\Shortest-Paths-Algorithms\\bellman.txt");
        int[] cost = new int[graph.size()];
        int[] parent = new int[graph.size()];
        graph.dijkestra(0, cost, parent);
        /*Test Algorithm With Negative Weights*/
        /*The algorithm should fail because it's not designed to handle negative weights*/
        assertEquals(3, cost[6]);
    }
    @Test
    void testBellmanFordWithNegativeWeight() throws IOException {
        GraphAlgorithms graph = new GraphAlgorithms("D:\\Koleya\\CSED Year 2 (2023-2024)\\2nd Semester - Spring 2024\\DS II\\Labs\\Lab 3\\Shortest-Paths-Algorithms\\bellman.txt");
        int[] cost = new int[graph.size()];
        int[] parent = new int[graph.size()];
        graph.bellmanFord(0, cost, parent);
        /*Test Algorithm With Negative Weights*/
        assertEquals(3, cost[6]);
    }
    @Test
    void testFloydWarshallWithNegativeWeight() throws IOException {
        GraphAlgorithms graph = new GraphAlgorithms("D:\\Koleya\\CSED Year 2 (2023-2024)\\2nd Semester - Spring 2024\\DS II\\Labs\\Lab 3\\Shortest-Paths-Algorithms\\bellman.txt");
        int[][] cost = new int[graph.size()][graph.size()];
        int[][] parent = new int[graph.size()][graph.size()];
        graph.floydWarshall(cost, parent);
        /*Test Algorithm With Negative Weights*/
        assertEquals(3, cost[0][6]);
    }
    @Test
    void testBellmanFordWithNegativeCycle() throws IOException {
        GraphAlgorithms actuallyNoCycle = new GraphAlgorithms("D:\\Koleya\\CSED Year 2 (2023-2024)\\2nd Semester - Spring 2024\\DS II\\Labs\\Lab 3\\Shortest-Paths-Algorithms\\negativeCycleDoesntExist.txt");
        int[] cost = new int[actuallyNoCycle.size()];
        int[] parent = new int[actuallyNoCycle.size()];

        /*Passes because there is no negative cycle*/
        assertTrue(actuallyNoCycle.bellmanFord(0, cost, parent));
        GraphAlgorithms negativeCycleWithPathFromSource = new GraphAlgorithms("D:\\Koleya\\CSED Year 2 (2023-2024)\\2nd Semester - Spring 2024\\DS II\\Labs\\Lab 3\\Shortest-Paths-Algorithms\\negativeCycleWithPathFromSource.txt");
        cost = new int[negativeCycleWithPathFromSource.size()];
        parent = new int[negativeCycleWithPathFromSource.size()];

        /*Passes because there is a negative cycle reachable from the source*/
        assertFalse(negativeCycleWithPathFromSource.bellmanFord(0, cost, parent));
        GraphAlgorithms negativeCycleWithNoPathFromSource = new GraphAlgorithms("D:\\Koleya\\CSED Year 2 (2023-2024)\\2nd Semester - Spring 2024\\DS II\\Labs\\Lab 3\\Shortest-Paths-Algorithms\\negativeCycleWithNoPathFromSource.txt");
        cost = new int[negativeCycleWithNoPathFromSource.size()];
        parent = new int[negativeCycleWithNoPathFromSource.size()];
        /*Fails because there is a negative cycle but it's not reachable from the source*/
        assertFalse(negativeCycleWithNoPathFromSource.bellmanFord(0, cost, parent));
    }
    @Test
    void testFloydWarshallWithNegativeCycle() throws IOException {
        GraphAlgorithms actuallyNoCycle = new GraphAlgorithms("D:\\Koleya\\CSED Year 2 (2023-2024)\\2nd Semester - Spring 2024\\DS II\\Labs\\Lab 3\\Shortest-Paths-Algorithms\\negativeCycleDoesntExist.txt");
        int[][] cost = new int[actuallyNoCycle.size()][actuallyNoCycle.size()];
        int[][] parent = new int[actuallyNoCycle.size()][actuallyNoCycle.size()];
        /*Passes because there is no negative cycle*/
        assertTrue(actuallyNoCycle.floydWarshall(cost, parent));
        GraphAlgorithms negativeCycleWithPathFromSource = new GraphAlgorithms("D:\\Koleya\\CSED Year 2 (2023-2024)\\2nd Semester - Spring 2024\\DS II\\Labs\\Lab 3\\Shortest-Paths-Algorithms\\negativeCycleWithPathFromSource.txt");
        cost = new int[negativeCycleWithPathFromSource.size()][negativeCycleWithPathFromSource.size()];
        parent = new int[negativeCycleWithPathFromSource.size()][negativeCycleWithPathFromSource.size()];

        /*Passes because there is a negative cycle reachable from the source*/
        assertFalse(negativeCycleWithPathFromSource.floydWarshall(cost, parent));
        GraphAlgorithms negativeCycleWithNoPathFromSource = new GraphAlgorithms("D:\\Koleya\\CSED Year 2 (2023-2024)\\2nd Semester - Spring 2024\\DS II\\Labs\\Lab 3\\Shortest-Paths-Algorithms\\negativeCycleWithNoPathFromSource.txt");
        cost = new int[negativeCycleWithNoPathFromSource.size()][negativeCycleWithNoPathFromSource.size()];
        parent = new int[negativeCycleWithNoPathFromSource.size()][negativeCycleWithNoPathFromSource.size()];
        /**This one passes this time because there is a negative cycle even if it's not reachable from the source**/
        assertFalse(negativeCycleWithNoPathFromSource.floydWarshall(cost, parent));
    }
    @Test
    void graphWithOneNode() throws IOException {
        GraphAlgorithms graph = new GraphAlgorithms("D:\\Koleya\\CSED Year 2 (2023-2024)\\2nd Semester - Spring 2024\\DS II\\Labs\\Lab 3\\Shortest-Paths-Algorithms\\graphWithOneNode.txt");
        int[] cost = new int[graph.size()];
        int[] parent = new int[graph.size()];
        graph.dijkestra(0, cost, parent);
        assertArrayEquals(new int[]{0}, cost);
        assertArrayEquals(new int[]{0}, parent);
    }
    @Test
    void testTreeGraph() throws IOException {
        GraphAlgorithms graph = new GraphAlgorithms("D:\\Koleya\\CSED Year 2 (2023-2024)\\2nd Semester - Spring 2024\\DS II\\Labs\\Lab 3\\Shortest-Paths-Algorithms\\treeGraph.txt");
        int[][] cost = new int[graph.size()][graph.size()];
        int[][] parent = new int[graph.size()][graph.size()];
        graph.cost_between_allpairs_with_dijkestra(cost, parent);
        int[][] expectedCost = new int[][]{
                {0, 2, 3, 5, 8, 6},
                {Integer.MAX_VALUE, 0, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE},
                {Integer.MAX_VALUE, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE},
                {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 3, 1},
                {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 0, Integer.MAX_VALUE},
                {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 0}
        };
        assertArrayEquals(expectedCost, cost);
//        int[][] expectedParents = new int[][]{
//                {0, 0, 1, 4, 2, 3},
//                {-1, 1, 1, 4, 2, 3},
//                {-1, -1, 2, 4, 2, 3},
//                {-1, -1, -1, 3, -1, 3},
//                {-1, -1, -1, 4, 4, 3},
//                {-1, -1, -1, -1, -1, 5}
//        };
//        assertArrayEquals(expectedParents, parent);
    }
}