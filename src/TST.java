import java.util.*;

public class TST {

    private Node root;

    public TST() {
        root = null;
    }

    public void insert(String word) {
        Node tmp = insert(root, word);
        if (root == null)
            root = tmp;
    }

    private Node insert(Node current, String word) {
        if (current == null) {
            current = new Node(word.charAt(0));
        }
        if (word.charAt(0) < current.data) {
            current.left = insert(current.left, word);
        }
        else if (word.charAt(0) > current.data) {
            current.right = insert(current.right, word);
        }
        else {
            if (word.length() == 1) {
                current.end = true;
                return current;
            }
            current.middle = insert(current.middle, word.substring(1));
        }
        return current;

    }


    private Node findLastNode(String word) {
        if (word.length() == 0) {
            return root;
        }
        Node current = root;
        int i = 0;
        while (current != null) {
            if (word.charAt(i) == current.data) {
                if (i == word.length() - 1) {
                    return current;
                }
                ++i;
                current = current.middle;
            } else if (word.charAt(i) < current.data) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return null;
    }

    public ArrayList<String> autocomplete(String prefix) {
        ArrayList<String> words = new ArrayList<String>();
        if (prefix.length() == 0) {
            inOrder(root, words, prefix);
        }
        else {
            Node tmp = findLastNode(prefix);
            if (tmp == null)
                return null;
            if (tmp.end == true)
                words.add(prefix);
            inOrder(tmp.middle, words, prefix);
        }
        return words;
    }

    private void inOrder(Node current, ArrayList<String> words, String word) {
        if (current == null) {
            return;
        }
        inOrder(current.left, words, word);
        if (current.end == true) {
            words.add(word + current.data);
        }
        inOrder(current.middle, words, word + current.data);
        inOrder(current.right, words, word);
    }

    public class Node {
        public char data;
        public boolean end;
        public Node left, middle, right;

        public Node(char c) {
            this.data = c;
        }
    }

}