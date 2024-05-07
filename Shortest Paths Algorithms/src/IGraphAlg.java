public interface IGraphAlg {
    int size();
    void dijkestra(int source, int[] cost, int[] parent);
    boolean bellmanFord(int source, int[] cost, int[] parent);
    boolean floydWarshall(int[][] cost, int[][] predecessor);
}
