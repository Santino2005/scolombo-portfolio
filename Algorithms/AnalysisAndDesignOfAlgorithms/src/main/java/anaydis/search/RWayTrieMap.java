package anaydis.search;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RWayTrieMap<Value> implements Map<String,Value>{

    Node root;
    int size = 0;

    private class Node {
        Value value;
        Node[] next = (Node[]) Array.newInstance(Node.class, 256);
    }
    @Override
    public int size() {
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
        return x.value;
    }

    private Node get(Node x, String key, int d){
        if(x == null) {
            return null;
        }
        if(d == key.length()){
            return x;
        }
        char next = key.charAt(d);
        return get(x.next[next], key, d+1);

    }

    @Override
    public Value put(@NotNull String key, Value value) {
        Value old = get(key);
        boolean existed = containsKey(key);
        if(!existed){
            size++;
        }
        root = put(root, key, value, 0);
        return old;
    }

    private Node put(Node x, String key, Value value, int d){
        if (x == null){
            x = new Node();
        }
        if (d == key.length()) {
            x.value = value;
            return x;
        }
        char c = key.charAt(d);
        x.next[c] = put(x.next[c], key, value, d + 1);
        return x;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Iterator<String> keys() {
        return new keysIterable().iterator();
    }

    private class keysIterable implements Iterable<String>{

        @Override
        public @NotNull Iterator<String> iterator() {
            List<String> keys = new ArrayList<>();
            collect(root,"", keys);
            return keys.iterator();
        }
        private void collect(Node x, String pre, List<String> keys) {
            if (x == null) {
                return;
            }if(x.value != null){
                keys.add(pre);
            }
            for(char c = 0;  c < 256; c++){
                if(x.next[c] != null){
                    collect(x.next[c],pre+c, keys);
                }
            }
        }
    }
}
