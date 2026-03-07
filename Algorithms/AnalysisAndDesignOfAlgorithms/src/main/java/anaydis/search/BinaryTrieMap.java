package anaydis.search;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BinaryTrieMap<Value> implements Map<String, Value>{

    private Node root;
    private int size = 0;

    private class Node{
        String Key;
        Value val;
        Node left;
        Node right;
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
        return x.val;
    }
    private Node get(Node x, String key, int d){
        if(x == null){
            return null;
        }
        if(x.left == null && x.right == null){
            if(key.equals(x.Key)){
                return x;
            }
            return null;
        }
        if(bitAt(key, d)){
            return get(x.right, key, d + 1);
        }else{
            return get(x.left, key, d + 1);
        }
    }

    private boolean bitAt(String pos, int n){
        int bit = n/8;
        return bit < pos.length() && ((pos.charAt(bit) >> (n % 8) & 1 ) != 0);
    }

    @Override
    public Value put(@NotNull String key, Value value) {
        Value old = get(key);
        System.out.println(sett());
        boolean existed = containsKey(key);
        if(!existed){
            size++;
        }
        root = put(root, key, value, 0);
        return old;
    }

    private Node put(Node x, String Key, Value val, int d){
        if(x == null){
            x = new Node();
            x.Key = Key;
            x.val = val;
            return x;
        }
        if(x.right == null && x.left == null) {
            if (x.Key.equals(Key)) {
                x.val = val;
                return x;
            } else {
                Node New = new Node();
                New.Key = Key;
                New.val = val;
                return split(New, x, d);
            }
        }
        if(bitAt(Key, d)){
            x.right = put(x.right, Key, val, d + 1);
        }else {
            x.left = put(x.left, Key, val, d + 1);
        }
        return x;
    }

    private Node split(Node N, Node x, int d){
        Node NN = new Node();
        int bitA = 0;
        int bitB = 0;


        if(bitAt(N.Key, d)){bitA = 1;}
        if(bitAt(x.Key, d)){bitB = 1;}

        /*switch (bitA*2 + bitB){
            case 0:
                NN.left = split(N,x,d+1);
                break;
            case 3:
                NN.right = split(N,x,d+1);
                break;
            case 1:
                NN.left = N;
                NN.right = x;
                size++;
                break;
            case 2:
                NN.left = x;
                NN.right = N;
                size++;
                break;
        }
        return NN;
         */
        if (bitA == 0 && bitB == 0) {
            NN.left = split(N, x, d + 1);
        } else if (bitA == 1 && bitB == 1) {
            NN.right = split(N, x, d + 1);
        } else if (bitA == 0 && bitB == 1) {
            NN.left = N ;
            NN.right = x;
        } else if (bitA == 1 && bitB == 0) {
            NN.left = x;
            NN.right = N;
        }
        return NN;
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
            collect(root, "", keys);
            return keys.iterator();
        }
        private void collect(Node x, String pre, List<String> keys) {
            if(x == null){
                return;
            }
            if(x.val != null){
                keys.add(x.Key);
            }
            if(x.left != null){
                collect(x.left, pre, keys);
            }
            if(x.right != null){
                collect(x.right, pre, keys);
            }
        }
    }
    String sett(){
        return "Hola churro";
    }
}

