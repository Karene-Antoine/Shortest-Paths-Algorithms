public class GraphAlgorithms implements IGraphAlg{
    private String filePath;
    private int[][] graph;
    public GraphAlgorithms(String filePath){
        this.filePath = filePath;
    }

    public void readGraph(){
        // Read the graph from the file
    }
    @Override
    public int size() {
        return 0;
    }

    @Override
    public void dijkestra(int source, int[] cost, int[] parent) {

    }

    @Override
    public boolean bellmanFord(int source, int[] cost, int[] parent) {
        return false;
    }

    @Override
    public boolean floydWarshall(int[][] cost, int[][] predecessor) {
        return false;
    }
}
