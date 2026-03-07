package algorithms.queue;
import org.jetbrains.annotations.NotNull;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayQueue<E> implements Queue<E>{
    private E[] initial;
    private int size;
    private int head;
    private int tail;

    public ArrayQueue(){
        this(0);
    }
    public ArrayQueue(int initialCapacity){
        if(initialCapacity < 0)
            throw new NoSuchElementException("No negative capacity");
        else if(initialCapacity == 0)
            initialCapacity = 1;
        this.initial = (E[]) new Object[initialCapacity];
        this.size = 0;
        this.head = 0;
        this.tail = 0;
    }
    @Override
    public void enqueue(@NotNull E item) {
        if (initial.length == size)
            resize(2 * initial.length);
        initial[tail] = item;
        tail++;
        if (tail >= initial.length)
            tail = 0;
        size++;
    }
    @Override
    public @NotNull E dequeue(){
        if(isEmpty() || size == 0)
            throw new NoSuchElementException("EmptyEnqueu");
        E item_copy = initial[head];
        initial[head] = null;
        head = (head+1) % initial.length;
        size--;
        return item_copy;
    }
    public boolean isEmpty(){
        if(size == 0)
            return true;
        return false;
    }
    public int size(){
        return size;
    }
    public void resize(int initialCapacity){
        E[] newarray = (E[]) new Object[initialCapacity];
        for(int number = 0; number < initial.length; number++){
            newarray[number] = initial[(head + number) % initial.length];
        }
        initial = newarray;
        tail = size;
        head = 0;
    }
    @Override
    public Iterator<E> iterator() {
        return new ArrayQueueIterator();
    }

    class ArrayQueueIterator implements Iterator<E>{

        private int newhead;

        private int newsize;

        public ArrayQueueIterator(){
            newhead = head;
            newsize = size;
        }

        @Override
        public boolean hasNext() {
            return newsize > 0;
        }

        @Override
        public E next() {
            if (hasNext() == false) {
                throw new NoSuchElementException("Empty");
            }
            E item = initial[newhead];
            newhead = (newhead + 1) % initial.length;
            newsize--;
            return item;
        }
    }
}

