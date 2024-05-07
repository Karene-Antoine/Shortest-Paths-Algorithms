import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
class GraphAlgorithmsTest {
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







}