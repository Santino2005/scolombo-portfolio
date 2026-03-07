package algorithms.stack;
import org.junit.Test;
import org.junit.Assert;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ArrayStackTest<E> {

    Stack<String> Column;
    Stack<String> Bateria;

    @Test(expected = NoSuchElementException.class)
    public void test_constructor_underflow() {
        Bateria = new ArrayStack<>(-2);
    }

    @Test
    public void test_push() {
        Column = new ArrayStack<>();
        Column.push("Pizza");
        Column.push("Papa");
        Assert.assertEquals(2, Column.size());
    }

    @Test(expected = NoSuchElementException.class)
    public void test_pop_with_exception() {
        Column = new ArrayStack<>();
        Assert.assertEquals("Empty", Column.pop());

    }
    @Test
    public void test_pop_method() {
        Column = new ArrayStack<>();
        Column.push("Pizza");
        Column.push("Churro");
        Assert.assertEquals("Churro", Column.pop());
    }

    @Test
    public void test_size() {
        Column = new ArrayStack<>();
        Column.push("Pizza");
        Column.push("Cookie");
        Column.push("a");
        Column.push("b");
        Column.push("c");
        Column.push("d");
        Assert.assertEquals(6, Column.size());
    }

    @Test
    public void test_IsEmpty_Empty() {
        Column = new ArrayStack<>();
        Assert.assertEquals(true, Column.isEmpty());
    }


    @Test
    public void test_IsEmpty_with_element() {
        Column = new ArrayStack<>();
        Column.push("Pizza");
        Column.push("Apple");
        Assert.assertEquals(false, Column.isEmpty());
    }

    @Test
    public void test_Next() {
        ArrayStack<String> Pila = new ArrayStack<>();
        Pila.push("Pizza");
        Pila.push("Papa");
        Pila.push("Jabon");
        Iterator<String> iterator = Pila.iterator();
        assertThat(Pila.iterator().next()).isEqualTo("Jabon");
        iterator.next();
        Assert.assertEquals("Papa", iterator.next());
    }

    @Test(expected = NoSuchElementException.class)
    public void test_throw_exception_next() {
        ArrayStack<String> Pila2 = new ArrayStack<>();
        Pila2.push("Chocolate");
        Pila2.push("Anana");
        Iterator<String> iterator2 = Pila2.iterator();
        iterator2.next();
        iterator2.next();
        iterator2.next();
    }

    @Test
    public void test_HasNext() {
        ArrayStack<String> Pila2 = new ArrayStack<>();
        Pila2.push("Banana");
        Pila2.push("Simio");

        Iterator<String> iterator1 = Pila2.iterator();
        iterator1.next();
        Assert.assertEquals(true, iterator1.hasNext());
        iterator1.next();
        Assert.assertEquals(false, iterator1.hasNext());
    }
}
