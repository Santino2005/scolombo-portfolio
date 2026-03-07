package algorithms.stack;
import org.jetbrains.annotations.NotNull;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayStack<E> implements Stack<E> {
    private E[] Column;
    private int N;

    public ArrayStack(int initialCapacity) {
        if(initialCapacity < 0)
            throw new NoSuchElementException("Underflow");
        N = 0;
        Column = (E[]) new Object[initialCapacity];
    }
    public ArrayStack(){
        this(0);
    }
    @Override
    public void push(@NotNull E item) {
        if (N == Column.length) {
            resize(2 * Column.length);
            Column[N] = item;
        }
        Column[N++] = item;
    }


    @Override
    public E pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty");
        } else {
            E item = Column[--N];
            Column[N] = null;
            if (N > 0 && N == Column.length / 4) {
                resize(Column.length / 2);
            }
            return item;
        }
    }

    private void resize(int initialCapacity) {
        if (initialCapacity == 0) {
            initialCapacity++;
        }
        E[] Variable = (E[]) new Object[initialCapacity];

        for (int k = 0; k < N; k++) {
            Variable[k] = Column[k];
        }
        Column = Variable;
    }

    @Override
    public boolean isEmpty() {
        if (N == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int size() {
        return N;
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayStackIterator<>(Column);
    }


    class ArrayStackIterator<E> implements Iterator<E> {
        private E[] array;
        private int size1 = N;

        public ArrayStackIterator(E[] array) {
            this.array = array;
        }

        @Override
        public boolean hasNext() {
            return size1 > 0;
        }

        @Override
        public E next() {
            if (hasNext() == false)
                throw new NoSuchElementException("Empty");
            else
                return array[--size1];
        }
    }
}



