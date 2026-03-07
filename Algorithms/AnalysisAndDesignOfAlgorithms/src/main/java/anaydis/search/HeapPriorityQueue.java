package anaydis.search;

import java.util.*;

public class HeapPriorityQueue<K> implements PriorityQueue<K>{

    int size = 0;
    K[] keys;
    private Comparator<K> comparator;
    public HeapPriorityQueue(Comparator<K> comparator) {
        this(50, comparator);
    }
    public HeapPriorityQueue(int max, Comparator<K> comparator) {
        this.keys = (K[]) new Object[max];
        this.comparator = comparator;
    }
    @Override
    public void insert(K ks) {
        if(size == keys.length){
            throw new NoSuchElementException("VACIO");
        }
        keys[++size] = ks;
        swim(size);
    }

    @Override
    public K pop(){
        if(isEmpty()){
            throw new NoSuchElementException("Antipopeado");
        }
        K result = keys[1];
        exch(keys, 1, size--);
        keys[size+1] = null;
        sink(1);
        return result;
    }

    private void sink(int k){
        int pos = k;
        while(2*pos <= size){
            int j = 2* pos;
            if(j < size && less(j, j+1)){j++;}
            if (!less(pos, j)) {break;}
            exch(keys, pos, j);
            pos = j;
        }
    }
    private void swim(int size){
        int pos = size;
        while (pos > 1 && less(pos/2, pos)){
            exch(keys, pos/2, pos);
            pos /= 2;
        }
    }
    private boolean less(int i, int j) {
        return comparator.compare(keys[i], keys[j]) < 0;
    }
    void exch(K[] datos, int i, int j) {
        K t = datos[i];
        datos[i] = datos[j];
        datos[j] = t;
    }

    @Override
    public K peek(){
        if(isEmpty()){
            throw new NoSuchElementException("EMPTY");
        }
        return keys[1];
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
            sorterkey = Arrays.copyOfRange(keys, 1, size + 1);
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
