package algorithms.stack;
import org.junit.Test;
import org.junit.Assert;
import java.util.Iterator;

import java.util.NoSuchElementException;

public class LinkedListStackTest<E> {
    LinkedListStack Generic;

    @Test
    public void test_push(){
        Generic = new LinkedListStack();
        Generic.push("Pepe");
        Generic.push("Pepe2");
        Assert.assertEquals(2, Generic.size());
        Assert.assertEquals("Pepe2", Generic.pop());
        Assert.assertEquals("Pepe", Generic.pop());
    }
    @Test(expected = NoSuchElementException.class)
    public void test_pop_empty(){
        Generic = new LinkedListStack();
        Generic.pop();
    }

    @Test
    public void test_pop_Not_empty(){
        Generic = new LinkedListStack();
        Generic.push("Pizza");
        Generic.pop();
        Generic.push("Pizza2");
        Generic.pop();
        Generic.push("Pizza3");
        Generic.push("Pizza4");
        Assert.assertEquals("Pizza4", Generic.pop());
        Assert.assertEquals("Pizza3", Generic.pop());
    }
    @Test
    public void test_is_Empty(){
        Generic = new LinkedListStack();
        Assert.assertTrue(Generic.isEmpty());
        Generic.push("Push");
        Assert.assertFalse(Generic.isEmpty());
    }
    @Test
    public void test_hasNext(){
        Generic = new LinkedListStack();
        Generic.push("AAA");
        Generic.push("BBB");
        Iterator<String> iterador1 = Generic.iterator();
        Assert.assertTrue(iterador1.hasNext());
        iterador1.next();
        Assert.assertTrue(iterador1.hasNext());
    }
    @Test(expected = NoSuchElementException.class)
    public void test_No_Next(){
        Generic = new LinkedListStack();
        Iterator<String> iterator = Generic.iterator();
        iterator.next();
        iterator.next();
    }
    @Test
    public void test_Next(){
        Generic = new LinkedListStack();
        Generic.push("AAA");
        Generic.push("BBBB");
        Generic.push("CCCC");
        Iterator<String> iterator2 = Generic.iterator();
        iterator2.next();
        Assert.assertEquals("BBBB",iterator2.next());
        Assert.assertEquals("AAA", iterator2.next());
    }
}
