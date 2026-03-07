package algorithms.tree;

import java.util.Iterator;

public interface TreeVisitor<Key> {
    Iterator<Key> inOrder(Node<Key, ?> root);

    Iterator<Key> postOrder(Node<Key, ?> root);

    Iterator<Key> preOrder(Node<Key, ?> root);

    Iterator<Key> levelOrder(Node<Key, ?> root);
}
