package aalPackage;

import java.util.ArrayList;

public class Node {

    private ArrayList<Node> neighbors;
    private int positionX;
    private int positionY;

    public Node(int x, int y){
        this.positionX= x;
        this.positionY= y;
        this.neighbors= new ArrayList<Node>();
    }

    public void addNeighbor(Node node){
        neighbors.add(node);
    }
    public boolean checkIfNeighbors(Node node){
       return neighbors.contains(node);
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public ArrayList<Node> getNeighbors() {
        return neighbors;
    }

}
