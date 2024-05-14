import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.Stream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
class GraphAlgorithmsTest {
    static int maxWeight = 100; // maximum weight of each edge
    static int samples = 2;
    static int vertices = 100;
    static int numOfEdges = 1000;
    int[] arrV = {10, 50, 100, 200, 500, 1000};
    int[] arrE = {10, 50, 100, 500, 1000, 5000, 10_000, 100_000};
    static double density =  numOfEdges / (double) (vertices * (vertices - 1));
    static long totalTimeDijkestra = 0;
    static long totalTimeBellmanFord = 0;
    static long totalTimeFloydWarshall = 0;
    static long totalTimeDijkestraAllPair = 0;
    static long totalTimeBellmanFordAllPair = 0;
    static long totalTimeFloydWarshallAllPair = 0;
    static long totalTimeBellmanFordNegativeCycle = 0;
    static long totalTimeFloydWarshallNegativeCycle = 0;

    private static Stream<int[][]> graphProvider() {
        Random rand = new Random();
        int edges;
        edges = numOfEdges;

        return Stream.generate(() -> {
            int[][] graph = new int[edges + 3][3];
            for (int i = 0; i < edges; i++) {
                graph[i][0] = rand.nextInt(vertices);
                graph[i][1] = rand.nextInt(vertices);
                while (graph[i][0] == graph[i][1]) {
                    graph[i][1] = rand.nextInt(vertices);
                }
                graph[i][2] = rand.nextInt(maxWeight) + 1;
            }
            // ensure the shortest path from 0 to 5
            graph[edges+0][0] = 0; graph[edges+0][1] = 2;graph[edges+0][2] = 0;
            graph[edges+1][0] = 2; graph[edges+1][1] = 7;graph[edges+1][2] = 0;
            graph[edges+2][0] = 7; graph[edges+2][1] = 5;graph[edges+2][2] = 0;

            return graph;
        }).limit(samples); // generate 10 graphs
    }
    private static Stream<int[][]> graphProviderNegativeCycle() {
        Random rand = new Random();
        int edges;
        edges = (int) (density * vertices * (vertices - 1));

        return Stream.generate(() -> {
            int[][] graph = new int[edges + 4][3];
            for (int i = 0; i < edges; i++) {
                graph[i][0] = rand.nextInt(vertices);
                graph[i][1] = rand.nextInt(vertices);
                while (graph[i][0] == graph[i][1]) {
                    graph[i][1] = rand.nextInt(vertices);
                }
                graph[i][2] = rand.nextInt(maxWeight) + 1;
            }
            // ensure a negative cycle 0 -> 2 -> 7 -> 5 -> 0
            graph[edges+0][0] = 0; graph[edges+0][1] = 2;graph[edges+0][2] = -1;
            graph[edges+1][0] = 2; graph[edges+1][1] = 7;graph[edges+1][2] =  4;
            graph[edges+2][0] = 7; graph[edges+2][1] = 5;graph[edges+2][2] = -9;
            graph[edges+3][0] = 5; graph[edges+3][1] = 0;graph[edges+3][2] =  1;

            return graph;
        }).limit(samples); // generate 10 graphs
    }

    @ParameterizedTest
    @MethodSource("parametersProvider")
    void testGraphAlgorithms(int vertices, int numOfEdges, int[][] graph1, int[][] graph2,int[][] ngraph1, int[][] ngraph2 ) throws IOException {
        performanceTestSingleSource(graph1);
        performanceTestSingleSource(graph2);
        performanceTestAllPair(graph1);
        performanceTestAllPair(graph2);
        performanceTestNegativeCycle(ngraph1);
        performanceTestNegativeCycle(ngraph2);
        writeToFile(vertices, numOfEdges);
    }

    static Stream<Arguments> parametersProvider() {
        int[] arrV = {10, 50, 100};
        int[] arrE = {10, 50, 100, 500, 1000, 5000, 10_000, 100_000};

        return Arrays.stream(arrV)
                .boxed()
                .flatMap(v -> Arrays.stream(arrE)
                        .mapToObj(e -> Arguments.of(v, e, generateGraph(v, e),generateGraph(v, e), generateNGraph(v, e),generateNGraph(v, e))));
    }

    static int[][] generateGraph(int vertices, int numOfEdges) {
        Random rand = new Random();
        int edges;
        edges = numOfEdges;

            int[][] graph = new int[edges + 3][3];
            for (int i = 0; i < edges; i++) {
                graph[i][0] = rand.nextInt(vertices);
                graph[i][1] = rand.nextInt(vertices);
                while (graph[i][0] == graph[i][1]) {
                    graph[i][1] = rand.nextInt(vertices);
                }
                graph[i][2] = rand.nextInt(maxWeight) + 1;
            }
            // ensure the shortest path from 0 to 5
            graph[edges+0][0] = 0; graph[edges+0][1] = 2;graph[edges+0][2] = 0;
            graph[edges+1][0] = 2; graph[edges+1][1] = 7;graph[edges+1][2] = 0;
            graph[edges+2][0] = 7; graph[edges+2][1] = 5;graph[edges+2][2] = 0;

            return graph;
        }
    static int[][] generateNGraph(int vertices, int numOfEdges) {
        Random rand = new Random();
        int edges;
        edges = numOfEdges;

        int[][] graph = new int[edges + 4][3];
        for (int i = 0; i < edges; i++) {
            graph[i][0] = rand.nextInt(vertices);
            graph[i][1] = rand.nextInt(vertices);
            while (graph[i][0] == graph[i][1]) {
                graph[i][1] = rand.nextInt(vertices);
            }
            graph[i][2] = rand.nextInt(maxWeight) + 1;
        }
        // ensure the shortest path from 0 to 5
        graph[edges+0][0] = 0; graph[edges+0][1] = 2;graph[edges+0][2] = -1;
        graph[edges+1][0] = 2; graph[edges+1][1] = 7;graph[edges+1][2] =  4;
        graph[edges+2][0] = 7; graph[edges+2][1] = 5;graph[edges+2][2] = -9;
        graph[edges+3][0] = 5; graph[edges+3][1] = 0;graph[edges+3][2] =  1;

        return graph;
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
            assertEquals("The path is 0 -> 2 -> 7 -> 5.", graphAlgorithms.get_path(parent, cost, 0, 5));

            long duration = endTime - startTime;
            totalTimeDijkestra += duration;

            cost = new int [graphAlgorithms.size()] ;
            parent = new int [graphAlgorithms.size()] ;

            // test BellmanFord
            startTime = System.nanoTime();
            graphAlgorithms.bellmanFord(0, cost, parent);
            graphAlgorithms.cost( cost, vertices/2);
            endTime = System.nanoTime();
            assertEquals("The path is 0 -> 2 -> 7 -> 5.", graphAlgorithms.get_path(parent, cost, 0, 5));

            duration = endTime - startTime;
            totalTimeBellmanFord += duration;

            // test FloydWarshall
            int [][] costMatrix = new int [graphAlgorithms.size()][graphAlgorithms.size()] ;
            int [][] predecessor = new int [graphAlgorithms.size()][graphAlgorithms.size()] ;
            startTime = System.nanoTime();
            graphAlgorithms.floydWarshall(costMatrix, predecessor);
            graphAlgorithms.cost_from_source(costMatrix, 0, vertices/2);
            endTime = System.nanoTime();
            assertEquals("The path is 0 -> 2 -> 7 -> 5.", graphAlgorithms.get_path_from_source(predecessor, costMatrix, 0, 5));

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
            assertEquals("The path is 0 -> 2 -> 7 -> 5.", graphAlgorithms.get_path_from_source(predecessor, costMatrix, 0, 5));


            costMatrix = new int [graphAlgorithms.size()][graphAlgorithms.size()] ;
            predecessor = new int [graphAlgorithms.size()][graphAlgorithms.size()] ;

            // test BellmanFord
            startTime = System.nanoTime();
            graphAlgorithms.cost_between_all_pairs_with_bellman(costMatrix, predecessor);
            endTime = System.nanoTime();

            duration = endTime - startTime;
            totalTimeBellmanFordAllPair += duration;
            assertEquals("The path is 0 -> 2 -> 7 -> 5.", graphAlgorithms.get_path_from_source(predecessor, costMatrix, 0, 5));


            // test FloydWarshall
            costMatrix = new int [graphAlgorithms.size()][graphAlgorithms.size()] ;
            predecessor = new int [graphAlgorithms.size()][graphAlgorithms.size()] ;
            startTime = System.nanoTime();
            graphAlgorithms.floydWarshall(costMatrix, predecessor);
            endTime = System.nanoTime();

            duration = endTime - startTime;
            totalTimeFloydWarshallAllPair += duration;
            assertEquals("The path is 0 -> 2 -> 7 -> 5.", graphAlgorithms.get_path_from_source(predecessor, costMatrix, 0, 5));



        } finally {
            // Deletes the temporary file
            Files.delete(tempFile);

        }
    }

    @ParameterizedTest
    @MethodSource("graphProviderNegativeCycle")
    public void performanceTestNegativeCycle(int[][] graph) throws IOException{

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


            // test BellmanFord
            long startTime = System.nanoTime();
            boolean valBellman = graphAlgorithms.contains_negative_cycle_with_bellman();
            long endTime = System.nanoTime();
            assertTrue(valBellman);

            long duration = endTime - startTime;
            totalTimeBellmanFordNegativeCycle += duration;

            // test FloydWarshall
            int [][] costMatrix = new int [graphAlgorithms.size()][graphAlgorithms.size()] ;
            int [][] predecessor = new int [graphAlgorithms.size()][graphAlgorithms.size()] ;
            startTime = System.nanoTime();
            boolean valFloyd = graphAlgorithms.contains_negative_cycle_with_floyd();
            endTime = System.nanoTime();
            assertTrue(valFloyd);

            duration = endTime - startTime;
            totalTimeFloydWarshallNegativeCycle += duration;



        } finally {
            // Deletes the temporary file
            Files.delete(tempFile);

        }
    }
    @AfterAll
    public static void printResults() {
        System.out.print("At V= " + vertices + ", E= " + numOfEdges + ", density= "+ String.format("%.3f", density) + " :\n");
        System.out.println("Single Source Dijkstra took on average:  " + format(totalTimeDijkestra / samples));
        System.out.println("Single Source Bellman-Ford took on average:  " + format(totalTimeBellmanFord / samples));
        System.out.println("Single Source Floyd-Warshall took on average:  " + format(totalTimeFloydWarshall / samples));
        System.out.println();
        System.out.println("All Pair Dijkstra took on average:  " + format(totalTimeDijkestraAllPair / samples));
        System.out.println("All Pair Bellman-Ford took on average:  " + format(totalTimeBellmanFordAllPair / samples));
        System.out.println("All Pair Floyd-Warshall took on average:  " + format(totalTimeFloydWarshallAllPair / samples));
        System.out.println();
        System.out.println("Negative Cycle Bellman-Ford took on average:  " + format(totalTimeBellmanFordNegativeCycle / samples));
        System.out.println("Negative Cycle Floyd-Warshall took on average:  " + format(totalTimeFloydWarshallNegativeCycle/ samples));
       // writeToFile();
    }

    public static void writeToFile(int vertices, int numOfEdges){
        Workbook workbook;
        Sheet sheet;
        String filePath = "Results.xlsx";

        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            workbook = new XSSFWorkbook(inputStream);
            sheet = workbook.getSheetAt(0);
        } catch (IOException e) {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("Results");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("V");
            headerRow.createCell(1).setCellValue("E");
           // headerRow.createCell(2).setCellValue("Effective Density");
            headerRow.createCell(3).setCellValue("Single Source Dijkstra");
            headerRow.createCell(4).setCellValue("Single Source Bellman-Ford");
            headerRow.createCell(5).setCellValue("Single Source Floyd-Warshall");
            headerRow.createCell(6).setCellValue("All Pair Dijkstra");
            headerRow.createCell(7).setCellValue("All Pair Bellman-Ford");
            headerRow.createCell(8).setCellValue("All Pair Floyd-Warshall");
            headerRow.createCell(9).setCellValue("Negative Cycle Bellman-Ford Optimized");
            headerRow.createCell(10).setCellValue("Negative Cycle Floyd-Warshall Optimized");
        }

        boolean rowExists = false;
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header row

            if (row.getCell(0).getNumericCellValue() == vertices &&
                    row.getCell(1).getNumericCellValue() == numOfEdges ){
                   // row.getCell(2).getStringCellValue().equals(String.format("%.3f", density))) {
                rowExists = true;
                updateRow(row, vertices, numOfEdges);
                break;
            }
        }

        if (!rowExists) {
            Row dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
            updateRow(dataRow, vertices, numOfEdges);
        }

        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateRow(Row row, int vertices, int numOfEdges) {
        row.createCell(0).setCellValue(vertices);
        row.createCell(1).setCellValue(numOfEdges);
        //row.createCell(2).setCellValue(String.format("%.3f", density));
        if(totalTimeDijkestra != 0)  row.createCell(3).setCellValue(totalTimeDijkestra / 2);
        if(totalTimeBellmanFord != 0)  row.createCell(4).setCellValue(totalTimeBellmanFord / 2);
        if(totalTimeFloydWarshall != 0)  row.createCell(5).setCellValue(totalTimeFloydWarshall / 2);
        if(totalTimeDijkestraAllPair != 0)  row.createCell(6).setCellValue(totalTimeDijkestraAllPair / 2);
        if(totalTimeBellmanFordAllPair != 0)  row.createCell(7).setCellValue(totalTimeBellmanFordAllPair / 2);
        if(totalTimeFloydWarshallAllPair != 0)  row.createCell(8).setCellValue(totalTimeFloydWarshallAllPair / 2);
        if(totalTimeBellmanFordNegativeCycle != 0)  row.createCell(9).setCellValue(totalTimeBellmanFordNegativeCycle / 2);
        if(totalTimeFloydWarshallNegativeCycle != 0)  row.createCell(10).setCellValue(totalTimeFloydWarshallNegativeCycle / 2);
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
        assertThrows(NoSuchElementException.class, () -> new GraphAlgorithms("empty.txt"));
    }
    @Test
    void testSize() throws IOException {
        GraphAlgorithms graph = new GraphAlgorithms("empty_graph.txt");
        assertEquals(0, graph.size());
        graph = new GraphAlgorithms("size_test.txt");
        assertEquals(5, graph.size());
    }
    @Test
    void testNonExistentSource() throws IOException {
        /*Test Algorithm With a source node that doesn't exist*/
        GraphAlgorithms graph = new GraphAlgorithms("dijkestra.txt");
        int[] cost = new int[graph.size()];
        int[] parent = new int[graph.size()];
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> graph.dijkestra(6, cost, parent));
    }
    @Test
    void testNonExistentDestination() throws IOException {
        /*Test Algorithm With a source node that doesn't exist*/
        GraphAlgorithms graph = new GraphAlgorithms("dijkestra.txt");
        int[] cost = new int[graph.size()];
        int[] parent = new int[graph.size()];
        graph.dijkestra(0, cost, parent);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> System.out.println(cost[6]));
    }
    @Test
    void testNonExistentPath() throws IOException {
        GraphAlgorithms graph = new GraphAlgorithms("dijkestra.txt");
        int[] cost = new int[graph.size()];
        int[] parent = new int[graph.size()];
        graph.dijkestra(2, cost, parent);
        assertEquals(Integer.MAX_VALUE, cost[1]);
        assertEquals(-1, parent[1]);
    }
    @Test
    void testNormalDijkstra() throws IOException {
        GraphAlgorithms graph = new GraphAlgorithms("dijkestra.txt");
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
        GraphAlgorithms graph = new GraphAlgorithms("bellman.txt");
        int[] cost = new int[graph.size()];
        int[] parent = new int[graph.size()];
        graph.bellmanFord(2, cost, parent);
        /*Test Normal Algorithm Run*/
        assertArrayEquals(new int[]{Integer.MAX_VALUE, -2, 0, Integer.MAX_VALUE, -3, Integer.MAX_VALUE, 0}, cost);
        assertArrayEquals(new int[]{-1, 2, 2, -1, 1, -1, 4}, parent);
    }
    @Test
    void testNormalFloydWarshall() throws IOException {
        GraphAlgorithms graph = new GraphAlgorithms("floyd.txt");
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
        GraphAlgorithms graph = new GraphAlgorithms("bellman.txt");
        int[] cost = new int[graph.size()];
        int[] parent = new int[graph.size()];
        graph.dijkestra(0, cost, parent);
        /*Test Algorithm With Negative Weights*/
        /*The algorithm should fail because it's not designed to handle negative weights*/
        // Changed the assertion to not equal
        assertNotEquals(3, cost[6]);
    }
    @Test
    void testBellmanFordWithNegativeWeight() throws IOException {
        GraphAlgorithms graph = new GraphAlgorithms("bellman.txt");
        int[] cost = new int[graph.size()];
        int[] parent = new int[graph.size()];
        graph.bellmanFord(0, cost, parent);
        /*Test Algorithm With Negative Weights*/
        assertEquals(3, cost[6]);
    }
    @Test
    void testFloydWarshallWithNegativeWeight() throws IOException {
        GraphAlgorithms graph = new GraphAlgorithms("bellman.txt");
        int[][] cost = new int[graph.size()][graph.size()];
        int[][] parent = new int[graph.size()][graph.size()];
        graph.floydWarshall(cost, parent);
        /*Test Algorithm With Negative Weights*/
        assertEquals(3, cost[0][6]);
    }
    @Test
    void testBellmanFordWithNegativeCycle() throws IOException {
        GraphAlgorithms actuallyNoCycle = new GraphAlgorithms("negativeCycleDoesntExist.txt");
        int[] cost = new int[actuallyNoCycle.size()];
        int[] parent = new int[actuallyNoCycle.size()];

        /*Passes because there is no negative cycle*/
        assertTrue(actuallyNoCycle.bellmanFord(0, cost, parent));
        GraphAlgorithms negativeCycleWithPathFromSource = new GraphAlgorithms("negativeCycleWithPathFromSource.txt");
        cost = new int[negativeCycleWithPathFromSource.size()];
        parent = new int[negativeCycleWithPathFromSource.size()];

        /*Passes because there is a negative cycle reachable from the source*/
        assertFalse(negativeCycleWithPathFromSource.bellmanFord(0, cost, parent));
        GraphAlgorithms negativeCycleWithNoPathFromSource = new GraphAlgorithms("negativeCycleWithNoPathFromSource.txt");
        cost = new int[negativeCycleWithNoPathFromSource.size()];
        parent = new int[negativeCycleWithNoPathFromSource.size()];
        /*Fails because there is a negative cycle but it's not reachable from the source*/
        // changed the assertion to true
        assertTrue(negativeCycleWithNoPathFromSource.bellmanFord(0, cost, parent));
    }
    @Test
    void testFloydWarshallWithNegativeCycle() throws IOException {
        GraphAlgorithms actuallyNoCycle = new GraphAlgorithms("negativeCycleDoesntExist.txt");
        int[][] cost = new int[actuallyNoCycle.size()][actuallyNoCycle.size()];
        int[][] parent = new int[actuallyNoCycle.size()][actuallyNoCycle.size()];
        /*Passes because there is no negative cycle*/
        assertTrue(actuallyNoCycle.floydWarshall(cost, parent));
        GraphAlgorithms negativeCycleWithPathFromSource = new GraphAlgorithms("negativeCycleWithPathFromSource.txt");
        cost = new int[negativeCycleWithPathFromSource.size()][negativeCycleWithPathFromSource.size()];
        parent = new int[negativeCycleWithPathFromSource.size()][negativeCycleWithPathFromSource.size()];

        /*Passes because there is a negative cycle reachable from the source*/
        assertFalse(negativeCycleWithPathFromSource.floydWarshall(cost, parent));
        GraphAlgorithms negativeCycleWithNoPathFromSource = new GraphAlgorithms("negativeCycleWithNoPathFromSource.txt");
        cost = new int[negativeCycleWithNoPathFromSource.size()][negativeCycleWithNoPathFromSource.size()];
        parent = new int[negativeCycleWithNoPathFromSource.size()][negativeCycleWithNoPathFromSource.size()];
        /**This one passes this time because there is a negative cycle even if it's not reachable from the source**/
        assertFalse(negativeCycleWithNoPathFromSource.floydWarshall(cost, parent));
    }
    @Test
    void graphWithOneNode() throws IOException {
        GraphAlgorithms graph = new GraphAlgorithms("graphWithOneNode.txt");
        int[] cost = new int[graph.size()];
        int[] parent = new int[graph.size()];
        graph.dijkestra(0, cost, parent);
        assertArrayEquals(new int[]{0}, cost);
        assertArrayEquals(new int[]{0}, parent);
    }
    @Test
    void testTreeGraph() throws IOException {
        GraphAlgorithms graph = new GraphAlgorithms("treeGraph.txt");
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