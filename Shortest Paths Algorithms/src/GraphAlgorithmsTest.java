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
    @Test
    void fileNotFound() {
        assertThrows(IOException.class, () -> new GraphAlgorithms("/dummy/path.txt"));
    }
    @Test
    void emptyFile() {
        assertThrows(IOException.class, () -> new GraphAlgorithms("C:\\Users\\Lenovo\\Desktop\\Shortest-Paths-Algorithms\\empty.txt"));
    }
    @Test
    void testSize() throws IOException {
        GraphAlgorithms graph = new GraphAlgorithms("C:\\Users\\Lenovo\\Desktop\\Shortest-Paths-Algorithms\\empty_graph.txt");
        assertEquals(0, graph.size());
        graph = new GraphAlgorithms("C:\\Users\\Lenovo\\Desktop\\Shortest-Paths-Algorithms\\size_test.txt");
        assertEquals(5, graph.size());
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




}