package anaydis.search;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import anaydis.sort.*;

import static org.assertj.core.api.Assertions.assertThat;

public class TestPractice06 {

    public UnorderedArrayPriorityQueue<Integer> queue;
    public Comparator<Integer> comparator;

    public OrderedArrayPriorityQueue<Integer> fakestack;
    public Comparator<Integer> comparator2;

    public HeapPriorityQueue<Integer> heap;
    public Comparator<Integer> comparator3;


    @Test
    public void testInsert() {

        comparator = Integer::compareTo;
        queue = new UnorderedArrayPriorityQueue<>(5, comparator);
        Assert.assertEquals(0, queue.size());
        queue.insert(10);
        Assert.assertEquals(Integer.valueOf(10), queue.peek());
        Assert.assertEquals(1, queue.size());
        queue.insert(20);
        Assert.assertEquals(2, queue.size());
        Assert.assertEquals(Integer.valueOf(20), queue.peek());
    }

    @Test
    public void testPop() {

        comparator = Integer::compareTo;
        queue = new UnorderedArrayPriorityQueue<>(5, comparator);
        queue.insert(5);
        queue.insert(15);
        queue.insert(10);

        queue.pop();
        Assert.assertEquals(2, queue.size());
        Assert.assertEquals(Integer.valueOf(10), queue.peek());
        queue.pop();
        Assert.assertEquals(1, queue.size());
        queue.pop();
    }

    @Test
    public void testPeek() {
        comparator = Integer::compareTo;
        queue = new UnorderedArrayPriorityQueue<>(5, comparator);
        queue.insert(30);
        queue.insert(50);
        queue.insert(40);

        Assert.assertEquals(Integer.valueOf(50), queue.peek());
        Assert.assertEquals(3, queue.size());
    }

    @Test(expected = NoSuchElementException.class)
    public void testUnderflow() {
        comparator = Integer::compareTo;
        queue = new UnorderedArrayPriorityQueue<>(1, comparator);
        queue.pop();
    }

    @Test(expected = NoSuchElementException.class)
    public void testOverflow() {
        comparator = Integer::compareTo;
        queue = new UnorderedArrayPriorityQueue<>(0, comparator);
        queue.insert(1);
    }

    @Test
    public void testIsEmpty() {
        comparator = Integer::compareTo;
        queue = new UnorderedArrayPriorityQueue<>(5, comparator);
        Assert.assertTrue(queue.isEmpty());
        queue.insert(1);
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testIterator() {

        comparator = Integer::compareTo;
        queue = new UnorderedArrayPriorityQueue<>(10, comparator);

        queue.insert(10);
        queue.insert(5);
        queue.insert(15);

        Iterator<Integer> iterator = queue.iterator();
        Assert.assertTrue(iterator.hasNext());

        Assert.assertEquals(Integer.valueOf(15), iterator.next());
        Assert.assertEquals(Integer.valueOf(10), iterator.next());
        Assert.assertEquals(Integer.valueOf(5), iterator.next());

        Assert.assertFalse(iterator.hasNext());
    }


    @Test
    public void OrdertestInsert() {
        comparator2 = Integer::compareTo;
        fakestack = new OrderedArrayPriorityQueue<>(5, comparator2);
        Assert.assertEquals(0, fakestack.size());
        fakestack.insert(10);
        Assert.assertEquals(Integer.valueOf(10), fakestack.peek());
        Assert.assertEquals(1, fakestack.size());
        fakestack.insert(20);
        Assert.assertEquals(2, fakestack.size());
        Assert.assertEquals(Integer.valueOf(20), fakestack.peek());
        fakestack.insert(5);
        Assert.assertEquals(3, fakestack.size());
        Assert.assertEquals(Integer.valueOf(20), fakestack.peek());
    }

    @Test
    public void OrdertestPop() {
        comparator2 = Integer::compareTo;
        fakestack = new OrderedArrayPriorityQueue<>(5, comparator2);
        fakestack.insert(5);
        fakestack.insert(15);
        fakestack.insert(10);
        Assert.assertEquals(Integer.valueOf(15), fakestack.pop());
        Assert.assertEquals(2, fakestack.size());
        Assert.assertEquals(Integer.valueOf(10), fakestack.peek());
        Assert.assertEquals(Integer.valueOf(10), fakestack.pop());
        Assert.assertEquals(1, fakestack.size());
        Assert.assertEquals(Integer.valueOf(5), fakestack.pop());
        Assert.assertTrue(fakestack.isEmpty());
    }

    @Test
    public void OrdertestPeek() {
        comparator2 = Integer::compareTo;
        fakestack = new OrderedArrayPriorityQueue<>(5, comparator2);
        fakestack.insert(30);
        fakestack.insert(50);
        fakestack.insert(40);

        Assert.assertEquals(Integer.valueOf(50), fakestack.peek());
        Assert.assertEquals(3, fakestack.size());
    }

    @Test(expected = NoSuchElementException.class)
    public void OrdertestUnderflow() {
        comparator2 = Integer::compareTo;
        fakestack = new OrderedArrayPriorityQueue<>(1, comparator2);
        fakestack.pop();
    }

    @Test(expected = NoSuchElementException.class)
    public void OrdertestOverflow() {
        comparator2 = Integer::compareTo;
        fakestack = new OrderedArrayPriorityQueue<>(0, comparator2);
        fakestack.insert(1);
    }

    @Test
    public void OrdertestIsEmpty() {
        comparator2 = Integer::compareTo;
        fakestack = new OrderedArrayPriorityQueue<>(5, comparator2);
        Assert.assertTrue(fakestack.isEmpty());

        fakestack.insert(1);
        Assert.assertFalse(fakestack.isEmpty());
    }

    @Test
    public void OrdertestIterator() {
        comparator2 = Integer::compareTo;
        fakestack = new OrderedArrayPriorityQueue<>(10, comparator2);
        fakestack.insert(10);
        fakestack.insert(5);
        fakestack.insert(15);
        Iterator<Integer> iterator = fakestack.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(15), iterator.next());
        Assert.assertEquals(Integer.valueOf(10), iterator.next());
        Assert.assertEquals(Integer.valueOf(5), iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void HeaptestInsert() {
        comparator3 = Integer::compareTo;
        heap = new HeapPriorityQueue<>(10, comparator3);

        heap.insert(10);
        heap.insert(15);
        heap.insert(5);
        heap.insert(20);
        Assert.assertEquals((Integer) 20, heap.peek());
    }

    @Test
    public void HeaptestPop() {
        comparator3 = Integer::compareTo;
        heap = new HeapPriorityQueue<>(10, comparator3);

        heap.insert(10);
        heap.insert(15);
        heap.insert(5);
        heap.insert(20);
        Assert.assertEquals((Integer) 20, heap.pop());
        Assert.assertEquals((Integer) 15, heap.peek());
    }

    @Test(expected = NoSuchElementException.class)
    public void HeaptestPopOnEmpty() {
        comparator3 = Integer::compareTo;
        heap = new HeapPriorityQueue<>(10, comparator3);

        heap.pop();
    }

    @Test
    public void HeaptestIsEmpty() {
        comparator3 = Integer::compareTo;
        heap = new HeapPriorityQueue<>(10, comparator3);

        Assert.assertTrue(heap.isEmpty());
        heap.insert(1);
        Assert.assertFalse(heap.isEmpty());
    }

    @Test
    public void HeaptestSize() {
        comparator3 = Integer::compareTo;
        heap = new HeapPriorityQueue<>(10, comparator3);

        Assert.assertEquals(0, heap.size());
        heap.insert(1);
        Assert.assertEquals(1, heap.size());
        heap.insert(2);
        Assert.assertEquals(2, heap.size());
    }

    @Test
    public void HeaptestIterator() {
        comparator3 = Integer::compareTo;
        heap = new HeapPriorityQueue<>(10, comparator3);

        heap.insert(10);
        heap.insert(15);
        heap.insert(5);
        heap.insert(20);

        Iterator<Integer> iterator = heap.iterator();

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(20), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(15), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(10), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(5), iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    //Informe 5 test
    @Test
    public void Unordetest() {
        Comparator<Integer> cmp = Integer::compareTo;
        UnorderedArrayPriorityQueue UQ = new UnorderedArrayPriorityQueue(5000, cmp);
        Random random = new Random();
        long InitialTime = System.nanoTime();
        for (int i = 0; i < 5000; i++) {
            UQ.insert(random.nextInt(5000));
        }
        long EndTime = System.nanoTime();
        long Final = EndTime - InitialTime;
        System.out.println(Final + " Nanosegundos");
        long InitialTime2 = System.nanoTime();
        while (UQ.isEmpty()) {
            UQ.pop();
        }
        long EndTime2 = System.nanoTime();
        long Final2 = EndTime2 - InitialTime2;
        System.out.println(Final2 + " Nanosegundos");
    }

    @Test
    public void OrderTest() {
        Comparator<Integer> cmp = Integer::compareTo;
        OrderedArrayPriorityQueue OQ = new OrderedArrayPriorityQueue(5000, cmp);
        Random random = new Random();
        long InitialTime2 = System.nanoTime();
        for (int i = 0; i < 5000; i++) {
            OQ.insert(random.nextInt(5000));
        }
        long EndTime2 = System.nanoTime();
        long Final = EndTime2 - InitialTime2;
        System.out.println(Final + " Nanosegundos");

        long InitialTime = System.nanoTime();
        while (OQ.isEmpty()) {
            OQ.pop();
        }
        long EndTime = System.nanoTime();
        long Final2 = EndTime - InitialTime;
        System.out.println(Final2 + " Nanosegundos");
    }

    @Test
    public void HeapTest() {
        Comparator<Integer> cmp = Integer::compareTo;
        OrderedArrayPriorityQueue HQ = new OrderedArrayPriorityQueue(5000, cmp);

        Random random = new Random();
        long InitialTime3 = System.nanoTime();
        for (int i = 0; i < 5000; i++) {
            HQ.insert(random.nextInt(5000));
        }
        long EndTime3 = System.nanoTime();
        long Final = EndTime3 - InitialTime3;
        System.out.println(Final + " Nanosegundos");
        long InitialTime = System.nanoTime();
        while (HQ.isEmpty()) {
            HQ.pop();
        }
        long EndTime = System.nanoTime();
        long Final3 = EndTime - InitialTime;
        System.out.println(Final3 + " Nanosegundos");
    }
}