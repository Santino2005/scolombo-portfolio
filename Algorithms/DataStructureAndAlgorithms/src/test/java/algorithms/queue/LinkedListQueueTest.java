package algorithms.queue;
import org.junit.Test;
import org.junit.Assert;
import java.util.Iterator;

import java.util.NoSuchElementException;
public class LinkedListQueueTest {

    LinkedListQueue Generic;

    @Test
    public void test_enqueue(){
        Generic = new LinkedListQueue();
        Generic.enqueue("Flour");
        Generic.enqueue("Tomatoe");
        Assert.assertEquals(2, Generic.size());
        Assert.assertEquals("Flour", Generic.dequeue());
        Generic.enqueue("Grass");
        Assert.assertEquals("Tomatoe", Generic.dequeue());
    }

    @Test(expected = NoSuchElementException.class)
    public void test_dequeue_empty(){
        Generic = new LinkedListQueue();
        Generic.dequeue();
    }
    @Test
    public void test_dequeue_not_empty(){
        Generic = new LinkedListQueue();
        Generic.enqueue("Pizza");
        Generic.enqueue("Barbacoa");
        Assert.assertEquals("Pizza", Generic.dequeue());
        Assert.assertEquals("Barbacoa", Generic.dequeue());
        Generic.enqueue("Pila");
        Generic.enqueue("Papa");
        Generic.dequeue();
        Assert.assertEquals("Papa", Generic.dequeue());
    }
    @Test
    public void test_is_Empty(){
        Generic = new LinkedListQueue();
        Assert.assertTrue(Generic.isEmpty());
        Generic.enqueue("Peperoni");
        Assert.assertFalse(Generic.isEmpty());
    }
    @Test
    public void test_size(){
        Generic = new LinkedListQueue();
        Generic.enqueue("A");
        Generic.enqueue("B");
        Generic.enqueue("C");
        Assert.assertEquals(3, Generic.size());
        Generic.dequeue();
        Generic.dequeue();
        Generic.enqueue("D");
        Assert.assertEquals(2, Generic.size());
    }
    @Test
    public void test_has_Next(){
        Generic = new LinkedListQueue<>();
        Generic.enqueue("A");
        Generic.enqueue("B");
        Iterator<String> iterator2 = Generic.iterator();
        Assert.assertTrue(iterator2.hasNext());
        iterator2.next();
        Assert.assertTrue(iterator2.hasNext());
    }
    @Test(expected = NoSuchElementException.class)
    public void test_next_No_Next(){
        Generic = new LinkedListQueue<>();
        Iterator<String> iterator3 = Generic.iterator();
        iterator3.next();
    }
    @Test
    public void test_next(){
        Generic = new LinkedListQueue<>();
        Generic.enqueue("Salame1");
        Generic.enqueue("Salame2");
        Generic.enqueue("Salame3");
        Generic.enqueue("Salame4");
        Iterator<String> iterator4 = Generic.iterator();
        iterator4.next();
        Assert.assertEquals("Salame2", iterator4.next());
        iterator4.next();
        Assert.assertTrue(iterator4.hasNext());
    }
}
