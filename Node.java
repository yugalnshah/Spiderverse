package spiderman;
import java.util.*;

public class Node {
    int dimensionNumber;
    int numOfCanonEvents;
    int dimensionWeight;
    ArrayList<Person> people;
    Node next;

    public Node(int dimensionNumber, int numOfCanonEvents, int dimensionWeight, ArrayList<Person> people, Node next) {
        this.dimensionNumber = dimensionNumber;
        this.numOfCanonEvents = numOfCanonEvents;
        this.dimensionWeight = dimensionWeight;
        this.people = new ArrayList<Person>();
        this.next = next;
    }

    public int getDimensionNumber() {
        return dimensionNumber;
    }

    public void setDimensionNumber(int dimensionNumber) {
        this.dimensionNumber = dimensionNumber;
    }

    public int getDimensionWeight() {
        return dimensionWeight;
    }

    public void setDimensionWeight(int dimensionWeight) {
        this.dimensionWeight = dimensionWeight;
    }

    public int getnumOfCanonEvents() {
        return numOfCanonEvents;
    }

    public void setnumOfCanonEvents(int numOfCanonEvents) {
        this.numOfCanonEvents = numOfCanonEvents;
    }

    public ArrayList<Person> getPeople(){
        return people;
    }

    public void addPerson(Person person){
        this.people.add(person);
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}