package algorithms.tree;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RedBlackBinarySearchTreeTest {
    private RedBlackBinarySearchTree<Integer, String> tree;

    @Before
    public void setUp() {
        Comparator<Integer> comparator = Comparator.naturalOrder();
        tree = new RedBlackBinarySearchTree<>(comparator);
    }

    @Test
    public void testIsEmpty(){
        Assert.assertTrue(tree.isEmpty());
        tree.put(1, "one");
        Assert.assertFalse(tree.isEmpty());
    }

    @Test
    public void testSize() {
        Assert.assertEquals(0, tree.size());
        tree.put(1, "one");
        Assert.assertEquals(1, tree.size());
        tree.put(2, "two");
        Assert.assertEquals(2, tree.size());
        tree.put(3, "three");
        Assert.assertEquals(3, tree.size());
    }

    @Test
    public void testClear() {
        tree.put(1, "one");
        tree.put(2, "two");
        tree.put(3, "three");
        Assert.assertEquals(3, tree.size());
        tree.clear();
        Assert.assertTrue(tree.isEmpty());
        Assert.assertEquals(0, tree.size());
    }

    @Test
    public void testContains() {
        tree.put(1, "one");
        tree.put(2, "two");
        Assert.assertTrue(tree.contains(1));
        Assert.assertTrue(tree.contains(2));
        Assert.assertFalse(tree.contains(3));
    }

    @Test
    public void testGet() {
        tree.put(1, "one");
        tree.put(2, "two");
        Assert.assertEquals("one", tree.get(1));
        Assert.assertEquals("two", tree.get(2));
        Assert.assertNull(tree.get(3));
    }

    @Test
    public void testPut() {
        tree.put(1, "one");
        tree.put(2, "two");
        tree.put(3, "three");
        Assert.assertEquals(3, tree.size());
        Assert.assertEquals("one", tree.get(1));
        Assert.assertEquals("two", tree.get(2));
        Assert.assertEquals("three", tree.get(3));
    }

    @Test
    public void testMin() {
        tree.put(3, "three");
        tree.put(1, "one");
        tree.put(2, "two");
        Assert.assertEquals((Integer) 1, tree.min());
    }

    @Test(expected = NoSuchElementException.class)
    public void testMinEmptyTree() {
        tree.min();
    }

    @Test
    public void testMax() {
        tree.put(1, "one");
        tree.put(2, "two");
        tree.put(3, "three");
        Assert.assertEquals((Integer) 3, tree.max());
    }

    @Test(expected = NoSuchElementException.class)
    public void testMaxEmptyTree() {
        tree.max();
    }

    @Test
    public void testInOrder() {
        tree.put(3, "three");
        tree.put(1, "one");
        tree.put(2, "two");
        Iterator<Integer> iterator = tree.inOrder();
        Assert.assertEquals((Integer) 1, iterator.next());
        Assert.assertEquals((Integer) 2, iterator.next());
        Assert.assertEquals((Integer) 3, iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testPreOrder() {
        tree.put(3, "three");
        tree.put(1, "one");
        tree.put(2, "two");
        Iterator<Integer> iterator = tree.preOrder();
        Assert.assertEquals((Integer) 2, iterator.next());
        Assert.assertEquals((Integer) 1, iterator.next());
        Assert.assertEquals((Integer) 3, iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testPostOrder() {
        tree.put(3, "three");
        tree.put(1, "one");
        tree.put(2, "two");
        Iterator<Integer> iterator = tree.postOrder();
        Assert.assertEquals((Integer) 1, iterator.next());
        Assert.assertEquals((Integer) 3, iterator.next());
        Assert.assertEquals((Integer) 2, iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testLevelOrder() {
        tree.put(3, "three");
        tree.put(1, "one");
        tree.put(2, "two");
        Iterator<Integer> iterator = tree.levelOrder();
        Assert.assertEquals((Integer) 2, iterator.next());
        Assert.assertEquals((Integer) 1, iterator.next());
        Assert.assertEquals((Integer) 3, iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemove() {
        tree.put(1, "one");
        tree.remove(1);
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemoveMin() {
        tree.removeMin();
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemoveMax() {
        tree.removeMax();
    }
}
