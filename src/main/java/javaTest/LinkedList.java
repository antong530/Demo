package javaTest;

/**
 * Created by antong on 16/7/18.
 */
public class LinkedList<T> {
    private Node<T> firstNode;
    private Node<T> lastNode;
    private int mod;

    class Node<T> {
        T value;
        Node next;

        Node(T value) {
            this.value = value;
        }
    }

    public void add(Node node) {
        Node<T> l = lastNode;
        lastNode = node;
        if (l == null) {
            firstNode = node;
        } else {
            l.next = node;
        }
        mod++;
    }

    public void delete(Node node) {
        while (firstNode.next != null) {
            Node n = firstNode.next;
            if (n.next == node) {
                n.next = n.next.next;
                break;
            }
            firstNode = n;
        }
    }

    public void insert(Node fromNode, Node node) {
        while (firstNode.next != null) {
            Node n = firstNode.next;
            if (n == fromNode) {
                node.next = n.next;
                n.next = node;
            }
            firstNode = n;
        }
    }
}
