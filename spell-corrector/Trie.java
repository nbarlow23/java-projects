package spell;

/**
 * 4 data types
 *      root node
 *      wordCount
 *      nodeCount
 *      hashCode
 *
 * 1 constant 'a' offset
 *
 * 1 constructor
 *
 * 8 functions
 *      add
 *      addHelper
 *      find
 *      findHelper
 *      toString
 *      toStringHelper
 *      equals
 *      equalsHelper
 */
public class Trie implements ITrie {

    private Node root;
    private int wordCount;
    private int nodeCount;
    private int hashCode;

    public static final int OFFSET = 'a';

    /**
     * initialize root to new node, node count to 1, word count 0, hash to 31
     */
    public Trie() {
        root = new Node();
        nodeCount = 1;
        wordCount = 0;
        hashCode = 31;
    }

    /**
     *  if empty string, return
     *  set word to lowercase
     *  call add helper on root, word, index: 0
     *  increment hashcode by word's hash code
     *
     * @param word The word being added to the trie
     */
    public void add(String word) {
        if (word.equals("")) {
            return;
        }
        word = word.toLowerCase();
        addHelper(root, word, 0);
        hashCode += word.hashCode();
    }

    /**
     * get letterIndex (in array of nodes) by subtracting 'a' from my current letter
     * set next node to cur nodes.nodes at that index
     * if next is null
     *      call addNode of curNode at that index
     *      increment nodecount
     * if index is word length - 1, final one
     *      if next.value is 0,
     *          increment wordcount
     *      call increment count of next
     *      return
     * increment index
     * call addHelper on next, word, index
     *
     * @param curNode Node i'm working on
     * @param word i'm adding
     * @param index letter of word i'm on
     */
    public void addHelper(Node curNode, String word, int index) {
        int letterIndex = word.charAt(index) - OFFSET;
        Node next = curNode.getNodes()[letterIndex];

        if (next == null) {
            next = curNode.addNode(letterIndex);
            nodeCount++;
        }

        if (index == word.length() - 1) {

            if (next.getValue() == 0) {
                wordCount++;
            }

            next.incrementCount();
            return;
        }

        addHelper(next, word, ++index);
    }

    /**
     *
     * if word is empty string, return null
     * return findHelper on (word.toLowercase, root, index: 0)
     *
     * @param word The word being searched for
     *
     * @return findHelper
     */
    public Node find(String word) {

        if (word.equals("")) {
            return null;
        }

        return findHelper(root, word.toLowerCase(), 0);
    }

    /**
     *  get array of letter index by substracting 'a' from currentLetter at index
     *  Next node is curNode.nodes at letterIndex
     *
     *  if next is null
     *      return
     *  if (index is wordLength - 1; at end of word)
     *      return next if next's value > 0, otherwise return null
     *
     *  increment index
     *  return findHelper on Next node, word, index
     *
     * @param curNode I'm working on
     * @param word i'm adding
     * @param index of word
     * @return findHelper
     */
    public Node findHelper(Node curNode, String word, int index) {
        int letterIndex = word.charAt(index) - OFFSET;
        Node next = curNode.getNodes()[letterIndex];

        if (next == null) {
            return null;
        }

        if (index == word.length() - 1) {
            return next.getValue() > 0 ? next : null;
        }

        return findHelper(next, word, ++index);
    }

    /**
     *  create total builder
     *  create curWord builder
     *  call toStringHelper on root, total, curWord
     *
     *
     * @return total.toString
     */
    public String toString() {
        StringBuilder total = new StringBuilder();
        StringBuilder curWord = new StringBuilder();
        toStringHelper(root, total, curWord);
        return total.toString();
    }

    /**
     * if my cur Node's value > 0
     *      if (total.length > 0
     *          add a new line to total
     *      add curWord to total
    *
    *    create a copy of curWord
     *   loop through numLetters
     *      create a nextNode based on curNode.node array
     *      if next != null
     *          char letter = index + offset
     *          add letter to curWord
     *          call toStringHelper on next, total, cur word
 *          reset curWord to be my old copy
     *
     *
     * @param curNode i'm working on
     * @param total string builder
     * @param curWord builder
     */
    public void toStringHelper(Node curNode, StringBuilder total,StringBuilder curWord) {

        if (curNode.getValue() != 0) {

            if (total.length() != 0) {
                total.append('\n');
            }

            total.append(curWord.toString());
        }

        String copy = curWord.toString();

        for (int i = 0; i < Node.NUM_LETTERS; i++) {
            Node next = curNode.getNodes()[i];

            if (next != null) {
                char letter = (char)(i + OFFSET);
                curWord.append(letter);
                toStringHelper(next, total, curWord);
            }

            curWord = new StringBuilder(copy);
        }

    }

    /**
     * if o is null or it's diff class, return false
     * cast to Trie, if has diff node, word, or hash count, return false
     * return equalsHelper on this root and object's root
     *
     * @param o object i'm comparing
     * @return equalshelper
     */
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }

        if (o.getClass() != this.getClass()) {
            return false;
        }

        Trie t = (Trie)o;

        if (t.getNodeCount() != this.getNodeCount() || t.getWordCount() != this.getWordCount()
        || t.hashCode() != this.hashCode()) {
            return false;
        }

        return equalsHelper(root, t.root);

    }

    /**
     *  if both null
     *      return true
     *  if neither null and node's have same count
     *      set are equal to true
     *      loop thru num letters;
     *          set areEqual to be areEqual & equalsHelper(next1, next2);
     *      return areEqual;
     *  else return false
     *
     * @param n1 this node
     * @param n2 t.node
     * @return
     */
    public boolean equalsHelper(Node n1, Node n2) {
        if (n1 == null && n2 == null) {
            return true;
        }
        else if (n1 != null && n2 != null && n1.getValue() == n2.getValue()) {
            boolean areEqual = true;

            for (int i = 0; i < Node.NUM_LETTERS; i++) {
                areEqual = areEqual && equalsHelper(n1.getNodes()[i], n2.getNodes()[i]);
            }

            return areEqual;
        }
        else {
            return false;
        }

    }

    public int hashCode() {
        return hashCode;
    }

    public int getWordCount() {
        return wordCount;
    }

    public int getNodeCount() {
        return nodeCount;
    }
}
