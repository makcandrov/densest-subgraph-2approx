import java.util.ArrayList;

public class Node {
    public int id;
    public String name;
    public ArrayList<Node> neighbours = new ArrayList<>();

    public Node(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
