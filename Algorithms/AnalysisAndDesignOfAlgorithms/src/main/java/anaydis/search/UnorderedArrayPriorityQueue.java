package anaydis.search;
import java.util.*;
import anaydis.sort.*;
public class UnorderedArrayPriorityQueue<K> implements PriorityQueue<K>{

    private K[] keys;
    private int size = 0;
    private Comparator<K> comparator;

    public UnorderedArrayPriorityQueue(Comparator<K> comparator) {
        this(50, comparator);
    }
    public UnorderedArrayPriorityQueue(int max, Comparator<K> comparator) {
        this.keys = (K[]) new Object[max];
        this.comparator = comparator;
    }
    @Override
    public void insert(K Ks) {
        if(size == keys.length){
            throw new NoSuchElementException("Queue full");
        }
        keys[size++] = Ks;
    }
    @Override
    public K pop() {
        if(isEmpty()){
            throw new NoSuchElementException("Empty Queue");
        }
        int max = max();
        exch(keys, max, size -1);
        return keys[--size];
    }
    @Override
    public K peek(){
        if(isEmpty()){
            throw new NoSuchElementException("Empty Queue");
        }
        return keys[max()];
    }
    @Override
    public int size() {
        return size;
    }
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    private int max(){
        int max = 0;
        for(int i = 1; i < size; i++){
            if(comparator.compare(keys[i], keys[max]) > 0){
                max = i;
            }
        }
        return max;
    }
    void exch(K[] datos, int i, int j) {
        K t = datos[i];
        datos[i] = datos[j];
        datos[j] = t;
    }
    @Override
    public Iterator<K> iterator(){
        return new KeysIterator();
    }
    private class KeysIterator implements Iterator<K>{

        K[] sorterkey;
        int size2 = 0;
        public KeysIterator(){
            sorterkey = Arrays.copyOf(keys, size);
            Arrays.sort(sorterkey, Collections.reverseOrder(comparator));
        }
        @Override
        public boolean hasNext() {
            return size2 < size;
        }

        @Override
        public K next(){
            if(!hasNext()){
                throw new NoSuchElementException("No next");
            }
            return sorterkey[size2++];
        }
    }
}
