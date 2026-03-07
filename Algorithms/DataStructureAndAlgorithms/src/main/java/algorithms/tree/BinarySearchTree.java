package algorithms.tree;
import algorithms.queue.LinkedListQueue;
import org.jetbrains.annotations.NotNull;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class BinarySearchTree<Key, Value> implements TreeMap<Key, Value> {

    private Node<Key, Value> root;
    private Comparator<Key> Comparador;

    private class Node<Key, Value> {
        private Key key;
        private Value value;
        private Node<Key, Value> left, right;
        private int size;

        public Node(Key key, Value value, int size) {
            this.key = key;
            this.value = value;
            this.size = size;
        }
    }
    public BinarySearchTree(Comparator<Key> comparador) {
        Comparador = comparador;
        root = null;
    }
    public int size() {
        return size(root);
    }

    private int size(Node<Key, Value> NewNode) {
        if (NewNode == null) {
            return 0;
        }
        return NewNode.size;
    }
    public boolean isEmpty(){
        return root == null || root.size == 0;
    }
    public void clear(){
        root.size = 0;
        root = null;
    }
    public boolean contains(@NotNull Key key){
        return find(root, key) != null;
    }
    public Value get(@NotNull Key key) {
        if (root == null) {
            return null;
        } else {
            Node<Key, Value> NewNode = find(root, key);
            if (NewNode == null) {
                return null;
            }
            return NewNode.value;
        }
    }
    private Node<Key, Value> find(Node<Key, Value> NewNode, Key key) {
        if (NewNode == null) {
            return null;
        }
        int Comparador_ = Comparador.compare(key, NewNode.key);
        if (Comparador_ < 0) {
            return find(NewNode.left, key);
        } else if (Comparador_ > 0) {
            return find(NewNode.right, key);
        } else {
            return NewNode;
        }
    }
    public void put(Key key, Value value) {
        root = put(root, key, value);
    }
    private Node<Key, Value> put(Node<Key, Value> NewNode, Key key, Value value) {
        if (NewNode == null) {
            return new Node<>(key, value, 1);
        }
        int Comparador_ = Comparador.compare(key, NewNode.key);
        if (Comparador_ < 0) {
            NewNode.left = put(NewNode.left, key, value);
        } else if (Comparador_ > 0) {
            NewNode.right = put(NewNode.right, key, value);
        } else if (NewNode.value != null) {
            NewNode.value = value;
        }
        NewNode.size = size(NewNode.left) + size(NewNode.right) + 1;
        return NewNode;
    }
    public void remove(Key key) {
        root = remove(root, key);
    }
    private Node<Key, Value> remove(Node<Key, Value> NewNode, Key key) {
        if (NewNode == null) {
            return null;
        }
        int Comparador_ = Comparador.compare(key, NewNode.key);
        if (Comparador_ < 0) {
            NewNode.left = remove(NewNode.left, key);
            return NewNode;
        } else if (Comparador_ > 0) {
            NewNode.right = remove(NewNode.right, key);
            return NewNode;
        } else {
            if (NewNode.left == null) {
                return NewNode.right;
            } else if (NewNode.right == null) {
                return NewNode.left;
            } else {
                Node<Key, Value> sucesor = NewNode.right;
                Node<Key, Value> sucesor2 = NewNode.left;
                NewNode = sucesor;
                sucesor.left = put(sucesor2, sucesor2.key, sucesor2.value);
            }
        }
        NewNode.size = size(NewNode.left) + size(NewNode.right) + 1;
        return NewNode;
    }
    public Iterator<Key> inOrder() {
        LinkedListQueue<Key> Queue = new LinkedListQueue<>();
        inOrder(root, Queue);
        return Queue.iterator();
    }

    private void inOrder(Node<Key, Value> NewNode, LinkedListQueue<Key> Queue) {
        if (NewNode == null) {
            return;
        }
        inOrder(NewNode.left, Queue);
        Queue.enqueue(NewNode.key);
        inOrder(NewNode.right, Queue);
    }

    public Iterator<Key> preOrder() {
        LinkedListQueue<Key> Queue = new LinkedListQueue<>();
        preOrder(root, Queue);
        return Queue.iterator();
    }

    private void preOrder(Node<Key, Value> NewNode, LinkedListQueue<Key> Queue) {
        if (NewNode == null) {
            return;
        }
        Queue.enqueue(NewNode.key);
        preOrder(NewNode.left, Queue);
        preOrder(NewNode.right, Queue);
    }
    public Iterator<Key> postOrder() {
        LinkedListQueue<Key> Queue = new LinkedListQueue<>();
        postOrder(root, Queue);
        return Queue.iterator();
    }

    private void postOrder(Node<Key, Value> NewNode, LinkedListQueue<Key> Queue) {
        if (NewNode == null) {
            return;
        }
        postOrder(NewNode.left, Queue);
        postOrder(NewNode.right, Queue);
        Queue.enqueue(NewNode.key);
    }

    @Override
    public Iterator<Key> levelOrder() {
        LinkedListQueue<Key> Queue_key = new LinkedListQueue<>();
        LinkedListQueue<Node<Key, Value>> node = new LinkedListQueue<>();
        node.enqueue(root);
        if (root == null) {
            return Queue_key.iterator();
        }
        while (!node.isEmpty()) {
            Node<Key, Value> NewNodo = node.dequeue();
            Queue_key.enqueue(NewNodo.key);
            if (NewNodo.left != null) {
                node.enqueue(NewNodo.left);
            }
            if (NewNodo.right != null) {
                node.enqueue(NewNodo.right);
            }
        }
        return Queue_key.iterator();
    }
    public Key min() {
        if(root == null){
            throw new NoSuchElementException("EmptyTree");
        }
        Node<Key, Value> NewNode = min(root);
        return NewNode.key;
    }
    private Node<Key, Value> min(Node<Key, Value> NewNode) {
        if (NewNode.left == null) {
            return NewNode;
        } else {
            return min(NewNode.left);
        }
    }
    public Key max() {
        if(root == null){
            throw new NoSuchElementException("EmptyTree");
        }
        Node<Key, Value> NewNode = max(root);
        return NewNode.key;
    }
    private Node<Key, Value> max(Node<Key, Value> NewNode) {
        if (NewNode.right == null) {
            return NewNode;
        } else {
            return max(NewNode.right);
        }
    }
    @Override
    public void removeMin() {
        if(root == null){
            throw new NoSuchElementException("EmptyTree");
        }
        root = delMin(root);
    }
    private Node<Key, Value> delMin(Node<Key, Value> NewNode) {
        if (NewNode.left == null) {
            return NewNode.right;
        }
        NewNode.left = delMin(NewNode.left);
        NewNode.size = size(NewNode.left) + size(NewNode.right);
        return NewNode;
    }
    @Override
    public void removeMax() {
        if(root == null){
            throw new NoSuchElementException("EmptyTree");
        }
        root = delMax(root);
    }
    private Node<Key, Value> delMax(Node<Key, Value> NewNode) {
        if (NewNode.right == null) {
            return NewNode.left;
        }
        NewNode.right = delMax(NewNode.right);
        NewNode.size = size(NewNode.left) + size(NewNode.right);
        return NewNode;
    }
}