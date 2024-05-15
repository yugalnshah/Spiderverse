package spiderman;
import java.util.*;

public class GoHomeMachine {
    Graph graph;
    ArrayList<Person> people;
    List<List<Integer>> edgeTo;

    // constructor
    public GoHomeMachine(Graph graph, ArrayList<Person> people, List<List<Integer>> edgeTo){
        this.graph = graph;
        this.people = people;
        this.edgeTo = edgeTo;
    }
    // find a node in the graph by its dimension number
    public Node findDim(int dimNum){
        ArrayList<Node> adjList = graph.getAdjList();
        for (int i = 0; i < adjList.size(); i++){

            Node ptr = adjList.get(i);
            if (ptr.getDimensionNumber() == dimNum) 
            return ptr;

        }
        return null;
    }

    // find the index of a dimension in a list
    public int findInd(ArrayList<Integer> list, int dim) {
        for(int i = 0; i < list.size(); i++) {

            if (dim == list.get(i)) 
            return i;

        }
        return -1;
    }

    // find the index of a dimension in the adjacency list
    public int findIndex(ArrayList<Node> adjList, int dim) {
        for(int i = 0; i < adjList.size(); i++) {

            if (dim == adjList.get(i).getDimensionNumber()) 
            return i;

        }
        return -1;
    }

    // find the index of the dimension with minimum distance in a list
    public int findMinDistance(ArrayList<Integer> fringe) {
        int min = fringe.get(0);
        int index = 0;

        for(int i = 0; i < fringe.size();i++) {

            if (fringe.get(i) < min) {
                min = fringe.get(i);
                index = i;
            }

        }
        return index;
    }

    // implement Dijkstra's algorithm
    public void dAlgo(Graph graph, int startingHub, ArrayList<Person> anomalies, ArrayList<Integer> timePersons, String outFile) {

        StdOut.setFile(outFile);
        ArrayList<Integer> done = new ArrayList<>();
        Queue<Integer> fringe = new LinkedList<>();

        ArrayList<Integer> d = new ArrayList<>();
        ArrayList<Node> p = new ArrayList<>();

        ArrayList<Node> adjList = graph.getAdjList();

        // initialize distance and predecessor arrays
        for(int i = 0; i < adjList.size(); i++) {

            d.add(Integer.MAX_VALUE);
            p.add(null);

        }
        int ind = findIndex(adjList, startingHub);
        d.set(ind, 0);
        p.set(ind, null);
        fringe.add(startingHub);

        while(!fringe.isEmpty()) {

            int dim = fringe.poll();
            done.add(dim);
            Node neighborStart = findDim(dim).getNext();

            while (neighborStart != null) {

                if (!done.contains(neighborStart.getDimensionNumber())) {
                    int index_neighbor = findIndex(adjList, neighborStart.getDimensionNumber());
                    int res = d.get(findIndex(adjList, dim)) + neighborStart.getDimensionWeight();
                    if (d.get(index_neighbor) == Integer.MAX_VALUE) {
                        d.set(index_neighbor, res);
                        fringe.add(neighborStart.getDimensionNumber());
                        Node dimNode = findDim(dim);
                        p.set(index_neighbor, new Node(dimNode.getDimensionNumber(), dimNode.getnumOfCanonEvents(), dimNode.getDimensionWeight(), dimNode.getPeople(), null));
                    } 
                    else if (d.get(index_neighbor) > (d.get(findIndex(adjList, dim)) + neighborStart.getDimensionWeight())) {
                        d.set(index_neighbor, res);
                        Node dimNode = findDim(dim);
                        p.set(index_neighbor, new Node(dimNode.getDimensionNumber(), dimNode.getnumOfCanonEvents(), dimNode.getDimensionWeight(), dimNode.getPeople(), null));
                    }
                }
                neighborStart = neighborStart.getNext();

            }

        }

        // process anomalies and send them home
        for(int i = 0; i < anomalies.size(); i++) {

            Person anomaly  = anomalies.get(i);
            Node endNode = findDim(anomaly.getSig());
            int timeAllotted = 0;

            ArrayList<Node> result = new ArrayList<>();
            Node n = endNode;
            n.setNext(null);
            result.add(n);

            int index = findIndex(adjList, endNode.getDimensionNumber());
            Node c = p.get(index);
            c.setNext(null);
            result.add(c);
            timeAllotted += p.get(index).getDimensionWeight() + endNode.getDimensionWeight();
            
            Node prev = p.get(index);
            while (c.getDimensionNumber() != startingHub) {

                timeAllotted += prev.getDimensionWeight();
                int indexer = findIndex(adjList, c.getDimensionNumber());
                c = p.get(indexer);
                c.setNext(null);
                timeAllotted += p.get(indexer).getDimensionWeight();
                result.add(c);
                prev = p.get(indexer);

            }
            StdOut.print("" + 0 + " " + anomaly.getName() + " ");
            StdOut.print((timeAllotted < timePersons.get(i) ? "SUCCESS " : "FAILED "));
            for(int j = result.size() - 1; j >= 0; j--) {

                StdOut.print(result.get(j).getDimensionNumber() + " ");

            }
            StdOut.println();

        }
    }

    public static void main(String[] args) {

        if ( args.length < 5 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.GoHomeMachine <dimension INput file> <spiderverse INput file> <hub INput file> <anomalies INput file> <report OUTput file>");
                return;
        }

        String DiminputFile = args[0];
        String Spiderversefile = args[1];
        String hubFile = args[2];
        String anomaliesFile = args[3];
        String outputFile = args[4];

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
        ArrayList<Node> adjList = graph.getAdjList();
        Node[] clusterArr = cluster.getClusterTab();

        Collider collider = new Collider(graph, clusterArr, new ArrayList<Node>());
        collider.buildAdjList(graph, numOfDimensions, clusterArr);
        collider.insertPeople(Spiderversefile);

        StdIn.setFile(hubFile);
        int startingHub = StdIn.readInt();

        CollectAnomalies c = new CollectAnomalies(startingHub, graph, numOfDimensions);

        GoHomeMachine goHome = new GoHomeMachine(graph, new ArrayList<>(), new ArrayList<>());
        ArrayList<Person> anomalies = new ArrayList<Person>();
        ArrayList<Integer> timePersons = new ArrayList<Integer>();
        StdIn.setFile(anomaliesFile);
        int numOfAnomalies = StdIn.readInt();

        for (int i = 0; i < numOfAnomalies; i++){

            String nameOfAnomaly = StdIn.readString();
            int timeAlloted = StdIn.readInt();
            boolean found = false;

            for(int j = 0; j < adjList.size() ; j++) {

                Node start = adjList.get(j);
                while(start != null) {

                    ArrayList<Person> p = start.getPeople();
                    for(Person person : p) {

                        if (person.getName().equals(nameOfAnomaly)) {
                            anomalies.add(person);
                            timePersons.add(timeAlloted);
                            found = true;
                            break;
                        }

                    }
                    if(found) break;
                    start = start.getNext();

                }
                if (found) break;

            }
        }
        goHome.dAlgo(graph, startingHub, anomalies, timePersons, outputFile);
    }
}