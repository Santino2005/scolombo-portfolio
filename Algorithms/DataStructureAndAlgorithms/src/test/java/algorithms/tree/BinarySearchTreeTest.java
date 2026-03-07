package algorithms.tree;

import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.Iterator;

public class BinarySearchTreeTest {

    @Test
    public void test_size(){
        Comparator<Character> comparator = Comparator.naturalOrder();
        BinarySearchTree<Character, String> tree = new BinarySearchTree<>(comparator);
        Assert.assertEquals(0, tree.size());
        tree.put('C', "item1");
        tree.put('D', "item3");
        tree.put('J', "item2");
        tree.put('B', "item4");
        tree.put('A', "item5");
        Assert.assertEquals(5, tree.size());
    }

    @Test
    public void inOrder_test() {
        Comparator<Character> comparer = Comparator.naturalOrder();
        BinarySearchTree<Character, Character> tree = new BinarySearchTree<>(comparer);
        tree.put('D', 'D');
        tree.put('B', 'B');
        tree.put('E', 'E');
        tree.put('A', 'A');
        tree.put('F', 'F');
        tree.put('C', 'C');
        Iterator<Character> iterator1 = tree.inOrder();
        Assert.assertEquals(Character.valueOf('A'), iterator1.next());
        Assert.assertEquals(Character.valueOf('B'), iterator1.next());
        Assert.assertEquals(Character.valueOf('C'), iterator1.next());
        Assert.assertEquals(Character.valueOf('D'), iterator1.next());
        Assert.assertEquals(Character.valueOf('E'), iterator1.next());
        Assert.assertEquals(Character.valueOf('F'), iterator1.next());
    }
    @Test
    public void postOrder_test(){
        Comparator<Character> comparer = Comparator.naturalOrder();
        BinarySearchTree<Character, Character> tree = new BinarySearchTree<>(comparer);
        tree.put('D', 'D');
        tree.put('B', 'B');
        tree.put('E', 'E');
        tree.put('A', 'A');
        tree.put('F', 'F');
        tree.put('C', 'C');
        Iterator<Character> iterator1 = tree.postOrder();
        Assert.assertEquals(Character.valueOf('A'), iterator1.next());
        Assert.assertEquals(Character.valueOf('C'), iterator1.next());
        Assert.assertEquals(Character.valueOf('B'), iterator1.next());
        Assert.assertEquals(Character.valueOf('F'), iterator1.next());
        Assert.assertEquals(Character.valueOf('E'), iterator1.next());
        Assert.assertEquals(Character.valueOf('D'), iterator1.next());
    }
    @Test
    public void preOrder_test(){
        Comparator<Character> comparer = Comparator.naturalOrder();
        BinarySearchTree<Character, Character> tree = new BinarySearchTree<>(comparer);
        tree.put('D', 'D');
        tree.put('B', 'B');
        tree.put('E', 'E');
        tree.put('A', 'A');
        tree.put('F', 'F');
        tree.put('C', 'C');
        Iterator<Character> iterator1 = tree.preOrder();
        Assert.assertEquals(Character.valueOf('D'), iterator1.next());
        Assert.assertEquals(Character.valueOf('B'), iterator1.next());
        Assert.assertEquals(Character.valueOf('A'), iterator1.next());
        Assert.assertEquals(Character.valueOf('C'), iterator1.next());
        Assert.assertEquals(Character.valueOf('E'), iterator1.next());
        Assert.assertEquals(Character.valueOf('F'), iterator1.next());
    }
    @Test
    public void getTest(){
        Comparator<Character> comparer = Comparator.naturalOrder();
        BinarySearchTree<Character, String> tree = new BinarySearchTree<>(comparer);
        tree.put('5', "Five");
        tree.put('3', "Three");
        tree.put('7', "Seven");
        tree.put('2', "Two");
        tree.put('4', "Four");
        Assert.assertEquals("Five", tree.get('5'));
        Assert.assertEquals("Three", tree.get('3'));
        Assert.assertEquals("Seven", tree.get('7'));
    }
    @Test
    public void testDelete() {
        Comparator<Character> comparator = Comparator.naturalOrder();
        BinarySearchTree<Character, String> tree = new BinarySearchTree<>(comparator);
        tree.put('5', "Five");
        tree.put('3', "Three");
        tree.put('7', "Seven");
        tree.put('2', "Two");
        tree.put('4', "Four");
        tree.put('1', "One");
        tree.put('9',"Nine");
        tree.put('8', "Eight");
        tree.put('a', "AAA");
        tree.put('l', "LLL");
        tree.remove('7');
        tree.remove('1');
        Assert.assertNull(tree.get('1'));
        Assert.assertEquals("Five", tree.get('5'));
        tree.remove('3');
        Assert.assertNull(tree.get('3'));
        Assert.assertEquals("Two", tree.get('2'));
        Assert.assertEquals("Five", tree.get('5'));
        tree.remove('5');
        Assert.assertNull(tree.get('5'));
        Assert.assertEquals("Two", tree.get('2'));
        Assert.assertNull(tree.get('7'));
    }
    @Test
    public void isEmptyTest(){
        Comparator<Character> comparator = Comparator.naturalOrder();
        BinarySearchTree<Character, String> tree = new BinarySearchTree<>(comparator);
        Assert.assertTrue(tree.isEmpty());
        tree.put('1', "One");
        Assert.assertFalse(tree.isEmpty());
    }
    @Test
    public void containsTest(){
        Comparator<Character> comparator = Comparator.naturalOrder();
        BinarySearchTree<Character, String> tree = new BinarySearchTree<>(comparator);
        Assert.assertFalse(tree.contains('2'));
        tree.put('4', "Item");
        tree.put('1', "item1");
        tree.put('3', "Item3");
        tree.put('5', "Item4");
        Assert.assertTrue(tree.contains('3'));
        Assert.assertFalse(tree.contains('7'));
    }
    @Test
    public void TestClear(){
        Comparator<Character> comparator = Comparator.naturalOrder();
        BinarySearchTree<Character, String> tree = new BinarySearchTree<>(comparator);
        tree.put('2',"item2");
        tree.put('5',"item1");
        tree.put('9',"iteeeeeem");
        Assert.assertTrue(tree.contains('9'));
        tree.clear();
        Assert.assertFalse(tree.contains('2'));
    }
    @Test
    public void TestMin_and_Max(){
        Comparator<String> comparator = Comparator.naturalOrder();
        BinarySearchTree<String, String> tree = new BinarySearchTree<>(comparator);
        tree.put("5", "Five");
        tree.put("3", "Three");
        tree.put("7", "Seven");
        tree.put("2", "Two");
        tree.put("4", "Four");
        tree.put("1", "One");
        tree.put("8","Eight");
        Assert.assertEquals("1" , tree.min());
        Assert.assertEquals("8", tree.max());
    }
    @Test
    public void TestDelMin_and_MAx(){
        Comparator<Character> comparator = Comparator.naturalOrder();
        BinarySearchTree<Character, String> tree = new BinarySearchTree<>(comparator);
        tree.put('5', "Five");
        tree.put('3', "Three");
        tree.put('7', "Seven");
        tree.put('2', "Two");
        tree.put('4', "Four");
        tree.put('1', "One");
        Assert.assertTrue(tree.contains('1'));
        tree.removeMin();
        Assert.assertFalse(tree.contains('1'));
        Assert.assertTrue(tree.contains('7'));
        tree.removeMax();
        Assert.assertFalse(tree.contains('7'));
    }
}
