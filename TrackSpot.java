package spiderman;
import java.util.*;

public class TrackSpot {
    
    private ArrayList<Integer> path; // stores the path taken

    // constructor
    public TrackSpot(ArrayList<Integer> path){
        this.path=path;
    }

    // find the index of a dimension in the adjacency list
    int findIndex(ArrayList<Node> adjList, int dim) {
        for(int i = 0; i < adjList.size(); i++) {

            if (dim == adjList.get(i).getDimensionNumber()) return i;

        }
        System.out.println(dim);
        return -1;
    }

    // remove dimensions from the path list
    void remove_fromList(int startDim) {
       int i = 0;
       boolean check = false;
       while ( i < path.size()) {

            if (check) {
                path.remove(i);
                continue;
            }
            if (path.get(i) == startDim) {
                check = true;
            }
            i++;

       }
    }

    // find path taken by The Spot using DFS
    public void findPaths(int startDim, int targetDim, Graph graph, boolean[] visited) {
        path.add(startDim);
        ArrayList<Node> adjList = graph.getAdjList();
        visited[findIndex(adjList, startDim)] = true;

        if (visited[findIndex(adjList, targetDim)]) {
            return;
        }

        for(int i = 0; i < adjList.size(); i++) {

            if (adjList.get(i).getDimensionNumber() == startDim) {
                Node start = adjList.get(i).getNext();
                while (start != null) {

                    int dimNum = start.getDimensionNumber();
                    if (dimNum == targetDim) {
                        path.add(targetDim);
                        visited[findIndex(adjList, targetDim)] = true;
                        return;
                    }
                    //check if visited
                    int ind = findIndex(adjList, dimNum);
                    if (!visited[ind]) {
                        findPaths(dimNum, targetDim, graph, visited);
                        visited[ind] = true;
                    }
                    start = start.getNext();

                }
            }

        }
    }

    public int findindex(ArrayList<Node> adjList, int currentDim){
        int i=0;
        for(; i<adjList.size() && adjList.get(i).getDimensionNumber() != currentDim;i++);
        return i;
    }
    
    public static void main(String[] args) {

        if ( args.length < 4 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.TrackSpot <dimension INput file> <spiderverse INput file> <spot INput file> <trackspot OUTput file>");
                return;
        }

        String dimensionInputFile = args[0];
        String spotInputFile = args[2];
        String trackSpotOutputFile = args[3];

        StdIn.setFile(dimensionInputFile);
        int numOfDimensions = StdIn.readInt();
        int initialSize = StdIn.readInt();
        double threshold = StdIn.readDouble();
        StdIn.readLine();

        Clusters  cluster = new Clusters(numOfDimensions, initialSize, threshold);
        ArrayList<Node> dims = new ArrayList<>(numOfDimensions);
        
        StdIn.setFile(dimensionInputFile);
        StdIn.readLine();
        for (int i = 0; i < numOfDimensions; i++){

            int dimNum = StdIn.readInt();
            int numOfCanonEvents = StdIn.readInt();
            int dimWeight = StdIn.readInt();
            cluster.addDim(dimNum, numOfCanonEvents, dimWeight, dimensionInputFile);
            dims.add(new Node(dimNum, numOfCanonEvents, dimWeight, new ArrayList<Person>(), null));

        }

        cluster.connectClusters();

        Graph graph = new Graph(dims);
        Node[] clusterArr = cluster.getClusterTab();

        boolean[] visited = new boolean[graph.getAdjList().size()];

        Collider collider = new Collider(graph, clusterArr, new ArrayList<Node>());
        collider.buildAdjList(graph, numOfDimensions, clusterArr);
      
        StdIn.setFile(spotInputFile);
        int startDim = StdIn.readInt();
        int targetDim = StdIn.readInt();

        TrackSpot pathTrack = new TrackSpot(new ArrayList<Integer>());
        pathTrack.findPaths(startDim, targetDim, graph, visited);

        pathTrack.remove_fromList(targetDim);
        StdOut.setFile(trackSpotOutputFile);
        for (int i = 0; i <= pathTrack.path.size() - 1; i++) {

            StdOut.print(pathTrack.path.get(i) + " ");

        }
    }
}