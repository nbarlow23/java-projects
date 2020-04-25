package spell;

/**
 * 2 data types
 *      frequency
 *      Node array
 *  1 constant
 *      num letters = 26
 *
 *  1 constructor
 *      initialize
 *
 *  2 functions
 *      addNode
 *      incrementCount
 */
public class Node implements ITrie.INode {

    public static final int NUM_LETTERS = 26;
    private int frequency;
    private Node[] nodes;

    /**
     *
     */
    public Node() {
        frequency = 0;
        nodes = new Node[NUM_LETTERS];
    }

    /**
     * sets nodes at index to new node and returns it
     *
     * @param index node i'm setting
     * @return node
     */
    public Node addNode(int index) {
        nodes[index] = new Node();
        return nodes[index];
    }

    public void incrementCount() {
        frequency++;
    }

    public int getValue() {
        return frequency;
    }

    public Node[] getNodes() {
        return nodes;
    }

}
