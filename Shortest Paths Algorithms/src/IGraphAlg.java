public interface IGraphAlg {
    int size();
    int no_of_unique_edges();
    void dijkestra(int source, int[] cost, int[] parent);
    void cost_between_allpairs_with_dijkestra(int[][] cost , int [][] parent) ;
    boolean bellmanFord(int source, int[] cost, int[] parent);
    boolean cost_between_all_pairs_with_bellman (int[][] cost , int[][] parent) ;
    boolean contains_negative_cycle_with_bellman();
    boolean floydWarshall(int[][] cost, int[][] predecessor);
    boolean contains_negative_cycle_with_floyd() ;
    int cost(int [] cost , int destination) ;
    int cost_from_source(int [][] cost , int source ,  int destination) ;
    String get_path(int[] parent , int[] cost , int source , int destination);
    String get_path_from_source (int[][] parent , int[][] cost , int source , int destination) ;

}
