package algorithms.tree;
import algorithms.queue.LinkedListQueue;
import algorithms.tree.TreeVisitor;
import algorithms.tree.Node;
import java.util.Iterator;

public class BinaryTreeVisitor<Key> implements TreeVisitor<Key> {
    @Override
    public Iterator<Key> inOrder(Node<Key, ?> root) {
        LinkedListQueue<Key> Queue = new LinkedListQueue<Key>();
        inOrder(root, Queue);
        return Queue.iterator();
    }

    private void inOrder(Node<Key, ?> NewNode, LinkedListQueue<Key> Queue) {
        if (NewNode == null) {
            return;
        }
        inOrder(NewNode.left, Queue);
        Queue.enqueue(NewNode.key);
        inOrder(NewNode.right, Queue);
    }

    @Override
    public Iterator<Key> postOrder(Node<Key, ?> root) {
        LinkedListQueue<Key> Queue = new LinkedListQueue<>();
        postOrder(root, Queue);
        return Queue.iterator();
    }

    private void postOrder(Node<Key, ?> NewNode, LinkedListQueue<Key> Queue) {
        if (NewNode == null) {
            return;
        }
        postOrder(NewNode.left, Queue);
        postOrder(NewNode.right, Queue);
        Queue.enqueue(NewNode.key);
    }

    @Override
    public Iterator<Key> preOrder(Node<Key, ?> root) {
        LinkedListQueue<Key> Queue = new LinkedListQueue<>();
        preOrder(root, Queue);
        return Queue.iterator();
    }

    private void preOrder(Node<Key, ?> NewNode, LinkedListQueue<Key> Queue) {
        if (NewNode == null) {
            return;
        }
        Queue.enqueue(NewNode.key);
        preOrder(NewNode.left, Queue);
        preOrder(NewNode.right, Queue);
    }

    @Override
    public Iterator<Key> levelOrder(Node<Key, ?> root) {
        LinkedListQueue<Key> Queue_key = new LinkedListQueue<>();
        LinkedListQueue<Node<Key, ?>> node = new LinkedListQueue<>();
        node.enqueue(root);
        if(root == null){
            return Queue_key.iterator();
        }
        while(!node.isEmpty()){
            Node<Key, ?> NewNodo = node.dequeue();
            Queue_key.enqueue(NewNodo.key);
            if(NewNodo.left != null){
                node.enqueue(NewNodo.left);
            }
            if(NewNodo.right != null){
                node.enqueue(NewNodo.right);
            }
        }
        return Queue_key.iterator();
    }
}


