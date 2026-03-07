package algorithms.queue;
import org.junit.Test;
import org.junit.Assert;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;


public class ArrayQueueTest{
    ArrayQueue<String> Fila;

    ArrayQueue<String> Cola;

    @Test(expected = NoSuchElementException.class)
    public void test_constructor_underflow(){
        Fila = new ArrayQueue<>(-1);
    }

    @Test
    public void test_enqueue(){
        Cola = new ArrayQueue<>(1);
        Assert.assertEquals(0, Cola.size());
        Cola.enqueue("Pizza");
        Assert.assertEquals(1, Cola.size());
        Cola.enqueue("Papa");
        Assert.assertEquals(2, Cola.size());
    }

    @Test(expected = NoSuchElementException.class)
    public void test_dequeue_empty(){
        Fila = new ArrayQueue<>(1);
        Fila.dequeue();
    }

    @Test
    public void test_dequeue(){
        Fila = new ArrayQueue<>(1);
        Fila.enqueue("Pizza");
        Fila.enqueue("Papa");
        Assert.assertEquals("Pizza", Fila.dequeue());
        Assert.assertEquals("Papa", Fila.dequeue());
    }
    @Test
    public void test_is_Empty(){
        Cola = new ArrayQueue<>(1);
        Assert.assertEquals(true, Cola.isEmpty());
        Cola.enqueue("Pizza");
        Assert.assertEquals(false, Cola.isEmpty());
    }

    @Test
    public void test_size(){
        Fila = new ArrayQueue<>(1);
        Fila.enqueue("1");
        Fila.enqueue("2");
        Assert.assertEquals(2, Fila.size());
    }

    @Test
    public void test_resize(){
        Fila = new ArrayQueue<>(1);
        Fila.enqueue("AA");
        Fila.enqueue("BB");
        Assert.assertEquals("AA", Fila.dequeue());
        Fila.enqueue("CC");
        Assert.assertEquals("BB", Fila.dequeue());
        Assert.assertEquals("CC", Fila.dequeue());
    }

    @Test
    public void test_Next(){
        Fila = new ArrayQueue<>(1);
        Fila.enqueue("Pizza");
        Fila.enqueue("Papa");
        Fila.enqueue("Jabon");

        Iterator<String> iterator = Fila.iterator();
        Assert.assertEquals("Pizza", iterator.next());
        iterator.next();
        Assert.assertEquals("Jabon", iterator.next());
    }

    @Test(expected = NoSuchElementException.class)
    public void test_throw_exception_next() {
        Cola = new ArrayQueue<>(1);
        Cola.enqueue("Chocolate");
        Cola.enqueue("Anana");
        Iterator<String> iterator1 = Cola.iterator();
        iterator1.next();
        iterator1.next();
        iterator1.next();
    }
    @Test
    public void test_HasNext() {
        Fila = new ArrayQueue<>();
        Fila.enqueue("Banana1");
        Fila.enqueue("Simio1");

        Iterator<String> iterator2 = Fila.iterator();
        Assert.assertEquals(true, iterator2.hasNext());
        iterator2.next();
        Assert.assertEquals(true, iterator2.hasNext());
        iterator2.next();
        Assert.assertEquals(false, iterator2.hasNext());
    }
}
