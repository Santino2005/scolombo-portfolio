package anaydis.search;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TSTTrieMap<Value> implements Map<String, Value> {
    private Node root;
    private int size = 0;

    private class Node {
        char c;
        Node left;
        Node mid;
        Node right;
        Value val;
    }

    @Override
    public int size(){
        return size;
    }

    @Override
    public boolean containsKey(@NotNull String key) {
        return get(key) != null;
    }

    @Override
    public Value get(@NotNull String key) {
        Node x = get(root, key, 0);
        if(x == null){
            return null;
        }
        return x.val;
    }

    private Node get(Node x, String key, int d){
        if(x == null){
            return null;
        }
        char c = key.charAt(d);
        if(c < x.c){
            return get(x.left, key, d);
        }else if(c > x.c){
            return get(x.right, key, d);
        }else if(d + 1 < key.length()){
            return get(x.mid, key, d+1);
        }else{return x;}
    }
    @Override
    public Value put(@NotNull String key, Value value){
        Value old = get(key);
        boolean existed = containsKey(key);
        if(!existed){
            size++;
        }
        root = put(root, key, value, 0);
        return old;
    }
    private Node put(Node x, String key, Value val, int d){
        char c = key.charAt(d);
        if(x == null){
            x = new Node();
            x.c = c;
        }
        if(c < x.c){
            x.left = put(x.left, key, val, d);
        }else if(c > x.c){
            x.right = put(x.right, key, val, d);
        }else if(d + 1 < key.length()){
                x.mid = put(x.mid, key, val, d+1);
        }else{
            x.val = val;
        }
        return x;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Iterator<String> keys(){
        return new keysIterable().iterator();
    }

    private class keysIterable implements Iterable<String>{

        @Override
        public @NotNull Iterator<String> iterator() {
            List<String> keys = new ArrayList<>();
            collect(root, new StringBuilder(), keys);
            return keys.iterator();
        }
        private void collect(Node x, StringBuilder pre, List<String> keys) {
            if (x == null) {
                return;
            }
            collect(x.left, pre, keys);
            pre.append(x.c);
            if (x.val != null) {
                keys.add(pre.toString());
            }
            collect(x.mid, pre, keys);
            pre.deleteCharAt(pre.length()-1);
            collect(x.right, pre, keys);
        }
    }
}
