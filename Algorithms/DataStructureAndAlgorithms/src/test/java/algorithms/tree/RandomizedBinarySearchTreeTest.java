package algorithms.tree;

import static org.junit.Assert.*;

import algorithms.tree.RandomizedBinarySearchTree;
import org.junit.Before;
import org.junit.Test;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class RandomizedBinarySearchTreeTest {


    @Test
    public void testSize() {
        Comparator<Integer> comparator = Comparator.naturalOrder();
        RandomizedBinarySearchTree<Integer, String> tree = new RandomizedBinarySearchTree<>(comparator);
        assertEquals(0, tree.size());

        tree.put(5, "five");
        assertEquals(1, tree.size());

        tree.put(3, "three");
        tree.put(7, "seven");
        assertEquals(3, tree.size());
    }

    @Test
    public void testIsEmpty() {
        Comparator<Integer> comparator = Comparator.naturalOrder();
        RandomizedBinarySearchTree<Integer, String> tree = new RandomizedBinarySearchTree<>(comparator);
        assertTrue(tree.isEmpty());

        tree.put(5, "five");
        assertFalse(tree.isEmpty());
    }

    @Test
    public void testClear() {
        Comparator<Integer> comparator = Comparator.naturalOrder();
        RandomizedBinarySearchTree<Integer, String> tree = new RandomizedBinarySearchTree<>(comparator);
        tree.put(5, "five");
        tree.put(3, "three");
        tree.put(7, "seven");

        assertFalse(tree.isEmpty());

        tree.clear();
        assertTrue(tree.isEmpty());
    }

    @Test
    public void testContains() {
        Comparator<Integer> comparator = Comparator.naturalOrder();
        RandomizedBinarySearchTree<Integer, String> tree = new RandomizedBinarySearchTree<>(comparator);
        assertFalse(tree.contains(5));

        tree.put(5, "five");
        assertTrue(tree.contains(5));
    }

    @Test
    public void testGet() {
        Comparator<Integer> comparator = Comparator.naturalOrder();
        RandomizedBinarySearchTree<Integer, String> tree = new RandomizedBinarySearchTree<>(comparator);
        tree.put(5, "five");
        assertEquals("five", tree.get(5));

        assertNull(tree.get(10));
    }

    @Test
    public void testPut() {
        Comparator<Integer> comparator = Comparator.naturalOrder();
        RandomizedBinarySearchTree<Integer, String> tree = new RandomizedBinarySearchTree<>(comparator);
        tree.put(5, "five");
        assertEquals(1, tree.size());
        assertEquals("five", tree.get(5));

        tree.put(3, "three");
        tree.put(7, "seven");
        assertEquals(3, tree.size());
        assertEquals("three", tree.get(3));
        assertEquals("seven", tree.get(7));
    }

    @Test
    public void testRemove() {
        Comparator<Integer> comparator = Comparator.naturalOrder();
        RandomizedBinarySearchTree<Integer, String> tree = new RandomizedBinarySearchTree<>(comparator);
        tree.put(5, "five");
        tree.put(3, "three");
        tree.put(7, "seven");

        tree.remove(3);
        //assertFalse(tree.contains(3));
        //assertNull(tree.get(3));
        assertEquals(2, tree.size());

        tree.put(4, "four");
        tree.put(9, "nine");
        tree.remove(5);
       // assertFalse(tree.contains(5));
        //assertNull(tree.get(5));
        assertEquals(3, tree.size());

        tree.remove(7);
        //assertFalse(tree.contains(7));
        //assertNull(tree.get(7));
        assertEquals(2, tree.size());
    }
}
