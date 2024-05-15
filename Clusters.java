package spiderman;

import java.util.ArrayList;

public class Clusters {
    private Node[] clusterTab; // array representing hash table
    private double threshold; // load factor threshold
    private int dimSoFar; // # of dimensions added so far

    public Clusters(int numDim, int initiSize, double thresh) {
        this.clusterTab = new Node[initiSize]; // initialize the hash table with the given initial size
        this.threshold = thresh; // set the rehashing threshold
        this.dimSoFar = 0; // initialize the counter of dimensions
    }

    public Node[] getClusterTab() {
        return clusterTab; // return the current state of the hash table
    }

    //addDimension method
    public void addDim(int dimensionNum, int numOfCanonEvents, int dimWeight, String inputFile){
        int ind = dimensionNum % clusterTab.length; // get the hash index

        if (clusterTab[ind] == null){ // create a new node if no list at the index
            clusterTab[ind] = new Node(dimensionNum, numOfCanonEvents, dimWeight, new ArrayList<>(), null);
        }
        else { // add to the front of the list if a list exists
            Node newDim = new Node(dimensionNum, numOfCanonEvents,dimWeight, new ArrayList<>(), null);
            newDim.setNext(clusterTab[ind]);
            clusterTab[ind] = newDim;
        }
        dimSoFar ++; // increment the number of added dimensions
        
        if ((dimSoFar/clusterTab.length) >= threshold){ // check if need to rehash
            rehash();
        }
    }

    private void rehash(){
        Node[] oldTable = clusterTab; // keep a reference to the old hash table
        for(int i = 0; i < oldTable.length; i++) { 

            Node start = oldTable[i];
            while (start != null) {

                start = start.getNext();

            }

        }
        clusterTab = new Node[oldTable.length*2]; // double the size of the hash table
        for(int i = 0; i < oldTable.length; i++) {

            Node start = oldTable[i];
            while (start != null) {

                int ind = start.getDimensionNumber() % clusterTab.length;
                if (clusterTab[ind] == null){
                    clusterTab[ind] = new Node(start.getDimensionNumber(), start.getnumOfCanonEvents(),start.getDimensionWeight(), new ArrayList<>(), null);
                } 
                else {
                    Node newDim = new Node(start.getDimensionNumber(), start.getnumOfCanonEvents(),start.getDimensionWeight(), new ArrayList<>(), null);
                    newDim.setNext(clusterTab[ind]);
                    clusterTab[ind] = newDim;
                }
                start = start.getNext();

            }

        }
    }

    public void connectClusters() {
        for(int i = 0; i < clusterTab.length; i++) {

            // determine the previous two clusters
            Node end1_cpy = clusterTab[ (i-1 < 0 ? clusterTab.length + i -  1: i-1) ];
            Node end2_cpy = clusterTab[ (i - 2 < 0 ? clusterTab.length + i - 2: i - 2)];
            // create copies 
            Node end1 = new Node(end1_cpy.getDimensionNumber(), end1_cpy.getnumOfCanonEvents(), end1_cpy.getDimensionWeight(), new ArrayList<>(), null);
            Node end2 = new Node(end2_cpy.getDimensionNumber(), end2_cpy.getnumOfCanonEvents(), end2_cpy.getDimensionWeight(), new ArrayList<>(), null);

            Node start_curr = clusterTab[i]; // find the end of the current cluster

            while (start_curr.getNext() != null) start_curr = start_curr.getNext();
            // connect the previous clusters to the current cluster
            end1.setNext(end2);
            start_curr.setNext(end1);

        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            StdOut.println(
                "Execute: java -cp bin spiderman.Clusters <dimension INput file> <collider OUTput file>");
                return;
        }
        
        String inputFile = args[0];
        String outputFile = args[1];
        StdIn.setFile(inputFile);
        int numOfDimensions = StdIn.readInt();
        int initialSize = StdIn.readInt();
        double threshold = StdIn.readDouble();
        StdIn.readLine();
        Clusters cluster = new Clusters(numOfDimensions, initialSize, threshold); // clusters object
        
        for (int i = 0; i < numOfDimensions; i++){ // add dimensions to the hash table

            int dimNum = StdIn.readInt();
            int numOfCanonEvents = StdIn.readInt();
            int dimWeight = StdIn.readInt();
            StdIn.readLine();
            cluster.addDim(dimNum,numOfCanonEvents,dimWeight,inputFile);

        }

        //method called connectClusters
        cluster.connectClusters();
        StdOut.setFile(outputFile);
        Node[] clusters = cluster.getClusterTab();

        for(int i = 0; i < clusters.length; i++) { // print the clusters

            Node start = clusters[i];
            while (start != null) {

                StdOut.print(start.getDimensionNumber() + " ");
                start = start.getNext();

            }
            StdOut.println();

        }
    }
}
