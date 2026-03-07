package algorithms.tree;
import org.junit.Assert;
import org.junit.Test;
import java.util.Iterator;

public class BinaryTreeVisitorTest {
    private Node<Character, Character> createTree() {
        Node<Character, Character> root = new Node<>('A', 'A');
        root.left = new Node<>('B', 'B');
        root.right = new Node<>('C', 'C');
        root.left.left = new Node<>('D','D');
        root.left.right = new Node<>('E','E');
        root.right.left = new Node<>('F','F');
        return root;
    }

    @Test
    public void inOrdertest() {
        BinaryTreeVisitor<Character> tree = new BinaryTreeVisitor<>();
        Node<Character, Character> root = createTree();
        Iterator<Character> iterator = tree.inOrder(root);
        Assert.assertEquals(Character.valueOf('D'), iterator.next());
        Assert.assertEquals(Character.valueOf('B'), iterator.next());
        Assert.assertEquals(Character.valueOf('E'), iterator.next());
        Assert.assertEquals(Character.valueOf('A'), iterator.next());
        Assert.assertEquals(Character.valueOf('F'), iterator.next());
        Assert.assertEquals(Character.valueOf('C'), iterator.next());
    }
    @Test
    public void postOrderTest(){
        BinaryTreeVisitor<Character> tree = new BinaryTreeVisitor<>();
        Node<Character, Character> root = createTree();
        Iterator<Character> iterator = tree.postOrder(root);
        Assert.assertEquals(Character.valueOf('D'), iterator.next());
        Assert.assertEquals(Character.valueOf('E'), iterator.next());
        Assert.assertEquals(Character.valueOf('B'), iterator.next());
        Assert.assertEquals(Character.valueOf('F'), iterator.next());
        Assert.assertEquals(Character.valueOf('C'), iterator.next());
        Assert.assertEquals(Character.valueOf('A'), iterator.next());
    }
    @Test
    public void preOrderTest(){
        BinaryTreeVisitor<Character> tree = new BinaryTreeVisitor<>();
        Node<Character, Character> root = createTree();
        Iterator<Character> iterator = tree.preOrder(root);
        Assert.assertEquals(Character.valueOf('A'), iterator.next());
        Assert.assertEquals(Character.valueOf('B'), iterator.next());
        Assert.assertEquals(Character.valueOf('D'), iterator.next());
        Assert.assertEquals(Character.valueOf('E'), iterator.next());
        Assert.assertEquals(Character.valueOf('C'), iterator.next());
        Assert.assertEquals(Character.valueOf('F'), iterator.next());
    }
    @Test
    public void levelOrderTest(){
        BinaryTreeVisitor<Character> tree = new BinaryTreeVisitor<>();
        Node<Character, Character> root = createTree();
        Iterator<Character> iterator = tree.levelOrder(root);
        Assert.assertEquals(Character.valueOf('A'), iterator.next());
        Assert.assertEquals(Character.valueOf('B'), iterator.next());
        Assert.assertEquals(Character.valueOf('C'), iterator.next());
        Assert.assertEquals(Character.valueOf('D'), iterator.next());
        Assert.assertEquals(Character.valueOf('E'), iterator.next());
        Assert.assertEquals(Character.valueOf('F'), iterator.next());
    }


}