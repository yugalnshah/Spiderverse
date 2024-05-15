package spiderman;

public class Person {
    private String name;
    private int sig;
    private int currDim;

    public Person(String name, int sig, int currDim){
        this.name = name;
        this.sig = sig;
        this.currDim = currDim;
    }

    public String getName() {
        return name;
    }

    public int getSig() {
        return sig;
    }

    public int getCurrDim() {
        return currDim;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSig(int sig) {
        this.sig = sig;
    }

    public void setCurrDim(int currDim) {
        this.currDim = currDim;
    }

    public String toString() {
        return name;
    }
}
