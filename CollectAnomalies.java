package spiderman;
import java.util.*;

public class CollectAnomalies {
    
    private ArrayList<Integer> path; // list to store path
    private Graph graph; // graph representing dimensions and connections
    private boolean[] visited; // array to mark visited during BFS
    private int[] edgeTo;
    private int numOfDimensions;
    private int[] prev; // array to store previous node during BFS
    private int startingHub; // starting hub dimension

    // constructor
    public CollectAnomalies(int startingHub, Graph graph, int numOfDimensions) {
        this.path = new ArrayList<>();
        this.graph = graph;
        this.numOfDimensions = numOfDimensions;
        this.visited = new boolean[numOfDimensions];
        this.edgeTo = new int[numOfDimensions];
        this.startingHub = startingHub;
    } 

    // getter for path list
    public ArrayList<Integer> getPath() {
        return path;
    }

    // find the dimension node by its dimension number
    public Node findDim(int dimNum){
        ArrayList<Node> adjList = graph.getAdjList();
        for (int i = 0; i < adjList.size(); i++){

            Node ptr = adjList.get(i);
            if (ptr.getDimensionNumber() == dimNum) { 
                return ptr;
            }
        }
        return null;
    }

    // find index of a dimension node in the adjacency list
    public int findIndex(ArrayList<Node> adjList, int dim) {
        for(int i = 0; i < adjList.size(); i++) {

            if (dim == adjList.get(i).getDimensionNumber()) 

            return i;

        }
        return -1; // if not found
    }

    // print anomalies and their routes
    public void printAnomalies(Graph g, ArrayList<Node> adjList, String outputFile){
        StdOut.setFile(outputFile);
        ArrayList<ArrayList<String>> anomaliesSpidersMatrix = new ArrayList<>();
        ArrayList<Integer> anomaliesKeys = new ArrayList<>();

        for(int i = 0; i < adjList.size(); i++) {

            for(int j = 0; j < adjList.get(i).getPeople().size(); j++) {

                Person p = adjList.get(i).getPeople().get(j);
                String anomaly = "", spider = "";

                if (p.getSig() != p.getCurrDim() && p.getCurrDim() != startingHub) {
                    ArrayList<String> hi = new ArrayList<>();
                    anomaly = p.getName();

                    for(int k = 0; k < adjList.get(i).getPeople().size(); k++) {

                        Person pg = adjList.get(i).getPeople().get(k);
                        if (pg.getSig() == pg.getCurrDim()) {
                            //spider
                            spider = pg.getName();
                        }

                    }
                    hi.add(anomaly);
                    hi.add(spider);

                    anomaliesSpidersMatrix.add(hi);
                    anomaliesKeys.add(p.getCurrDim());

                }

            }

        }

        for(int i = 0; i < anomaliesSpidersMatrix.size(); i++) {

            StdOut.print(anomaliesSpidersMatrix.get(i).get(0) + " " + anomaliesSpidersMatrix.get(i).get(1)); // print anomaly and spider


            if (anomaliesSpidersMatrix.get(i).get(1).length() > 0) StdOut.print(" ");

            ArrayList<Integer> path = bfs(g, findDim(startingHub), findDim(anomaliesKeys.get(i)));

            if (anomaliesSpidersMatrix.get(i).get(1).length() == 0) {
                for(int j = path.size() - 1; j > 0; j--) {

                    StdOut.print(path.get(j) + " "); // print path

                }
            }
            for(int j = 0; j < path.size(); j++) {

                StdOut.print(path.get(j) + " "); // print path

            }
            StdOut.println();

        }
    }

    // method to perform BFS
    public ArrayList<Integer> bfs(Graph graph, Node startNode, Node endNode) {
        prev = new int[graph.getAdjList().size()];
        visited = new boolean[graph.getAdjList().size()];
        Queue<Node> queueAnomaly = new LinkedList<>();
        queueAnomaly.add(startNode); // add start node to queue

        while (!queueAnomaly.isEmpty()){

            Node n = queueAnomaly.poll();
            int vNum = n.getDimensionNumber(); // get dimension
            int vIndex = findIndex(graph.getAdjList(), vNum);

            if (!visited[vIndex]) {
                visited[vIndex] = true;
                ArrayList<Node> graphInd = graph.getAdjList();

                Node ptr = graphInd.get(vIndex).getNext();
                while (ptr != null){

                        queueAnomaly.add(ptr); // add adjacent nodes to queue
                        if (prev[findIndex(graphInd, ptr.getDimensionNumber())] == 0) {
                            prev[findIndex(graphInd, ptr.getDimensionNumber())] = vNum; // find index of previous node
                        }
                    ptr = ptr.getNext();

                }
            } 

        }

        ArrayList<Node> graphInd = graph.getAdjList();

        ArrayList<Integer> result = new ArrayList<>();
        result.add(endNode.getDimensionNumber());

        int index = findIndex(graphInd, endNode.getDimensionNumber());
        int c = prev[index];
        result.add(c);
        while (c != startNode.getDimensionNumber()) {

            int ind = findIndex(graphInd, c);
            c = prev[ind];
            result.add(c);

        }
        return result; // print the path
    }

    public static void main(String[] args) {

        if ( args.length < 4 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.CollectAnomalies <dimension INput file> <spiderverse INput file> <hub INput file> <collected OUTput file>");
                return;
        }

        String DiminputFile = args[0];
        String Spiderversefile = args[1];
        String hubFile = args[2];
        String outputFile = args[3];

        StdIn.setFile(DiminputFile);
        int numOfDimensions = StdIn.readInt();
        int initialSize = StdIn.readInt();
        double threshold = StdIn.readDouble();
        StdIn.readLine();
        
        Clusters cluster = new Clusters(numOfDimensions, initialSize, threshold);
        ArrayList<Node> dims = new ArrayList<>(numOfDimensions);

        StdIn.setFile(DiminputFile);
        StdIn.readLine();
        for (int i = 0; i < numOfDimensions; i++){

            int dimNum = StdIn.readInt();
            int numOfCanonEvents = StdIn.readInt();
            int dimWeight = StdIn.readInt();
            cluster.addDim(dimNum, numOfCanonEvents, dimWeight, DiminputFile);
            dims.add(new Node(dimNum, numOfCanonEvents, dimWeight, new ArrayList<Person>(), null));
            
        }
        cluster.connectClusters();

        Graph graph = new Graph(dims);

        Node[] clusterArr = cluster.getClusterTab();

        Collider collider = new Collider(graph, clusterArr, new ArrayList<Node>());

        collider.buildAdjList(graph, numOfDimensions, clusterArr);

        StdIn.setFile(hubFile);
        int startingHub = StdIn.readInt();

        CollectAnomalies c = new CollectAnomalies(startingHub, graph, numOfDimensions);
        
        collider.insertPeople(Spiderversefile);

        c.printAnomalies(graph, graph.getAdjList(), outputFile);
    }
}