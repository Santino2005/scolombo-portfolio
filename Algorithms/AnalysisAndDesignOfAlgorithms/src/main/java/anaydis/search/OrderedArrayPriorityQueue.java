package anaydis.search;

import java.util.*;

public class OrderedArrayPriorityQueue<K> implements PriorityQueue<K>{

    K[] keys;
    int size = 0;
    private Comparator<K> comparator;

    public OrderedArrayPriorityQueue(Comparator<K> comparator) {
        this(50, comparator);
    }
    public OrderedArrayPriorityQueue(int max, Comparator<K> comparator) {
        this.keys = (K[]) new Object[max];
        this.comparator = comparator;
    }
    @Override
    public void insert(K Ks) {
        if(size == keys.length){
            throw new NoSuchElementException("Nah ha");
        }
        keys[size++] = Ks;
        for(int i = size - 1; i > 0; i--){
            if(comparator.compare(keys[i], keys[i-1]) > 0){
                exch(keys, i, i - 1);
            }else{
                break;
            }
        }
    }

    @Override
    public K pop() {
        if(isEmpty()){
            throw new NoSuchElementException("Empty");
        }
        K max = keys[0];
        System.arraycopy(keys, 1, keys, 0, --size);
        return max;
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
    public K peek(){
        if(isEmpty()){
            throw new NoSuchElementException("No peek");
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

    @Override
    public Iterator<K> iterator(){
        return new KeysIterator();
    }
    private class KeysIterator implements Iterator<K>{
        K[] sorterkey;
        int size2 = 0;
        public KeysIterator(){
            sorterkey = Arrays.copyOf(keys, size + 1);
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
