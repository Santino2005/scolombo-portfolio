package algorithms.tree;
import algorithms.queue.LinkedListQueue;
import org.jetbrains.annotations.NotNull;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class RandomizedBinarySearchTree<Key, Value> implements TreeMap<Key, Value> {

    private Node<Key, Value> root;
    private Comparator<Key> Comparador;
    private Random random;


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
    public RandomizedBinarySearchTree(Comparator<Key> comparador) {
        Comparador = comparador;
        root = null;
        random = new Random();
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
    private Node<Key, Value> rotateright(Node<Key, Value> NewNode){
        Node<Key, Value> rotated;
        rotated = NewNode.left;
        NewNode.left = rotated.right;
        rotated.right = NewNode;
        rotated.size = NewNode.size;
        return rotated;
    }
    private Node<Key, Value> rotateleft(Node<Key, Value> NewNode){
        Node<Key, Value> rotated;
        rotated = NewNode.right;
        NewNode.right = rotated.left;
        rotated.left = NewNode;
        rotated.size = NewNode.size;
        return rotated;
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
        root = randomizeput(root, key, value);
    }
    private Node<Key, Value> randomizeput(Node<Key, Value> NewNode, Key key, Value value) {
        if(NewNode == null){
            return new Node<>(key,value,1);}
        boolean bolean = random.nextInt(NewNode.size + 1) == 0;
        if(bolean){
            return rootput(NewNode, key, value);
        }
        else{
            int Comparador_ = Comparador.compare(key, NewNode.key);
            if(Comparador_ < 0){
                NewNode.left = randomizeput(NewNode.left, key, value);
            }else if(Comparador_ > 0){
                NewNode.right = randomizeput(NewNode.right, key, value);
            }else{
                NewNode.value = value;
            }
        }
        NewNode.size = size(NewNode.left) + size(NewNode.right) + 1;
        return NewNode;
    }
    private Node<Key, Value> rootput(Node<Key, Value> NewNode, Key key, Value value){
        if(NewNode == null){
            return new Node<>(key,value,1);}
        else{
            int Comparator = Comparador.compare(key, NewNode.key);
            if (Comparator < 0) {
                NewNode.left = rootput(NewNode.left, key, value);
                NewNode = rotateright(NewNode);
                NewNode.size = size(NewNode.left) + size(NewNode.right) + 1;
                return NewNode;
            } else if (Comparator > 0) {
                NewNode.right = rootput(NewNode.right, key, value);
                NewNode = rotateleft(NewNode);
                NewNode.size = size(NewNode.left) + size(NewNode.right) + 1;
                return NewNode;
            } else if (NewNode.value != null) {
                NewNode.value = value;
                return NewNode;
            }
            NewNode.size = size(NewNode.left) + size(NewNode.right);
            return NewNode;
        }
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
            NewNode.size = size(NewNode.left) + size(NewNode.right) + 1;
            return NewNode;
        } else if (Comparador_ > 0) {
            NewNode.right = remove(NewNode.right, key);
            NewNode.size = size(NewNode.left) + size(NewNode.right) + 1;
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
                sucesor.left = randomizeput(sucesor2, sucesor2.key, sucesor2.value);
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
        if(NewNode == null){
            return null;
        }
        if (NewNode.left == null) {
            return NewNode.right;
        }
        NewNode.left = delMin(NewNode.left);
        NewNode.size = size(NewNode.left) + size(NewNode.right) + 1;
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
        if(NewNode == null){
            return null;
        }
        if (NewNode.right == null) {
            return NewNode.left;
        }
        NewNode.right = delMax(NewNode.right);
        NewNode.size = size(NewNode.left) + size(NewNode.right) + 1;
        return NewNode;
    }
}


