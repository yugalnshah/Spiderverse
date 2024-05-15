package spiderman;
import java.util.*;

public class Collider {

    private Graph graph; // store the adjacency list
    private Node[] clusterArr; // array representing the hash table clusters
    private ArrayList<Node> adjList; // adjacency list representing connects between dimensions

    // constructor
    public Collider(Graph graph, Node[] clusterArr, ArrayList<Node> adjList){
        this.graph = graph; 
        this.clusterArr = clusterArr;
        this.adjList = adjList;
    }

    public ArrayList<Node> buildAdjList(Graph graph, int numOfDimensions, Node[] hash){
        for (int i = 0; i < clusterArr.length; i++){ // iterate over each cluster

            Node start = clusterArr[i];
            for (Node ptr = clusterArr[i].getNext(); ptr != null; ptr = ptr.getNext()){

                // add edges between dimensions in the cluster
                graph.addEdge(start.getDimensionNumber(), ptr);
                graph.addEdge(ptr.getDimensionNumber(), start);

            }

        }

        this.adjList = graph.getAdjList();
        return graph.getAdjList();
    }

    public void insertPeople(String spiderverseInputFile) {
        StdIn.setFile(spiderverseInputFile);
        int numOfPeople = StdIn.readInt();

        for (int i = 0; i < numOfPeople; i++) { // iterate over each dimension in the adjacency list

            int dimension = StdIn.readInt();
            String name = StdIn.readString();
            int signature = StdIn.readInt();
            Person person = new Person(name, signature, dimension);

            for (int j = 0; j<adjList.size(); j++){

                for(Node ptr = adjList.get(j); ptr!=null; ptr=ptr.getNext()) {

                    if(ptr.getDimensionNumber() == dimension){ // if dimension matches, add person
                        ptr.addPerson(person);
                    }

                }

            }

        }
    } 

    // getter for adjacency list
    public ArrayList<Node> getAdjList(){
        return adjList;
    }

    // print adjacency list
    public void printAdjList(String outputFile) {
        StdOut.setFile(outputFile);
        ArrayList<Node> adjList = graph.getAdjList();

        for(int i = 0; i < adjList.size(); i++) {

            for(Node ptr=adjList.get(i);ptr!=null;ptr=ptr.getNext()){

                StdOut.print(ptr.getDimensionNumber() + " ");

            }
            StdOut.println();

        }
    }
    
    public static void main(String[] args) {

        if (args.length < 3) {
            StdOut.println(
                "Execute: java -cp bin spiderman.Collider <dimension INput file> <spiderverse INput file> <collider OUTput file>");
                return;
        }

        String DiminputFile = args[0];
        String Spiderversefile = args[1];
        String outputFile = args[2];

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

        cluster.connectClusters(); // connect clusters

        Graph graph = new Graph(dims); // create graph and get cluster Array
        Node[] clusterArr = cluster.getClusterTab();

        Collider collider = new Collider(graph, clusterArr, new ArrayList<Node>());

        collider.buildAdjList(graph, numOfDimensions, clusterArr);

        collider.printAdjList(outputFile);

        collider.insertPeople(Spiderversefile);
    }
    
}