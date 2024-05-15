package spiderman;
import java.util.*;

public class Graph {
    private ArrayList<Node> adjList;

    public ArrayList<Node> getAdjList(){
        return this.adjList;
    }      

    public Graph(ArrayList<Node> list) {
        this.adjList = list;
    }

    public void addEdge(int current, Node neighbor) {
        for (int i = 0; i < adjList.size(); i++){
            if(adjList.get(i).getDimensionNumber() == current){
                //comparing the dimension numbers of the nodes from the dimension file
                Node n = adjList.get(i);
                while (n.getNext() != null) {
                    n = n.getNext();
                }
                n.setNext(new Node(neighbor.getDimensionNumber(), neighbor.getnumOfCanonEvents(), neighbor.getDimensionWeight(), null, null));
                break;
            }
        }
    }
}
