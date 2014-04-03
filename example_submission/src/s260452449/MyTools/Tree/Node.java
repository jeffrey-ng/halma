package s260452449.MyTools.Tree;

import halma.CCMove;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeffrey on 3/28/2014.
 */
public class Node {

    public Integer value;
    public Integer depth;

    public CCMove move;
    private ArrayList<Node> children;

    public Node(CCMove move, Integer depth) {
        this.depth = depth;
        children = new ArrayList<Node>();
        this.move = move;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public List<Node> getChildren() {
        return children;
    }

}
