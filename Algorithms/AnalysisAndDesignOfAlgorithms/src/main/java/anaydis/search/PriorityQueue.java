package anaydis.search;

import java.util.Iterator;

//
public interface PriorityQueue<K> {
    void insert(K k);
    K pop();
    K peek();
    int size();
    boolean isEmpty();
    Iterator<K> iterator();
}
