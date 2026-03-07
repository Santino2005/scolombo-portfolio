package anaydis.immutable;
import org.jetbrains.annotations.NotNull;
import java.util.*;
public class BinaryTree<K,V> implements Map<K,V>{
    private final Comparator<K> comparator;
    private Node<K,V> BT;
    private int size;
    private class Node<K, V>{ private K key;private V value;private Node<K, V> left;private Node<K, V> right;
        public Node(K key, V value, Node<K,V> left, Node<K,V> right){
            this.key = key;this.value = value;this.left = left;this.right = right;}}
    public BinaryTree(@NotNull Comparator<K> comparator) {this.comparator = comparator;}
    private BinaryTree(@NotNull Comparator<K> comparator, Node<K,V> BT, int size) {
        this.comparator = comparator;this.BT = BT;this.size = size;}
    @Override public int size() {return size;}
    @Override public boolean containsKey(@NotNull K key) {
        return get(key) != null;}
    @Override public V get(@NotNull K key) {
        Node<K,V> value = get(BT, key);if(value != null){
            return value.value;}
        return null;}
    private Node<K,V> get(Node<K,V> x, K key){
        if(x == null){return null;}
        int cmp = comparator.compare(key, x.key);if(cmp < 0){
            return get(x.left, key);}
        else if(cmp > 0){return get(x.right, key);}
        else{return x;}}
    @Override public Map<K, V> put(@NotNull K key, V value) {
        if(get(key) == null){Node<K,V> NBT = put(BT, key, value);
            return new BinaryTree<>(comparator, NBT, size+1);}
        else{Node<K, V> NBT = put(BT, key, value);
            return new BinaryTree(comparator, NBT, size);}}
    private Node<K, V> put(Node<K,V> x, K key, V value){if(x == null){
        return new Node<>(key, value, null, null);}
        int cmp = comparator.compare(key, x.key);if(cmp < 0){
            return new Node<>(x.key, x.value, put(x.left, key, value), x.right);}
        else if(cmp > 0){return new Node<>(x.key, x.value, x.left, put(x.right, key, value));}
        else{return new Node<>(key, value, x.left, x.right);}}
    @Override public Iterator<K> keys() {
        Set<K> k = new HashSet<>();CollectKeys(BT, k);k = Collections.unmodifiableSet(k);
        return k.iterator();}private void CollectKeys(Node<K, V> x, Set<K> k) {
        if (x == null) return;CollectKeys(x.left, k);k.add(x.key);CollectKeys(x.right, k);}
    @Override public boolean isEmpty() {
        return size == 0;}}