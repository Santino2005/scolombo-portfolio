package anaydis.immutable;

import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

public class Node<T> implements List<T> {
    public static final Object NIL = new Node<>(null,null);
    private T head;
    private List<T> tail;
    private boolean empty = true;
   public Node(T head, List<T> tail){
        this.head = head;
        this.tail = tail;
        this.empty = false;
   }
    @Override
    public T head() {
        if(head == null){
            throw new NoSuchElementException("No head");
        }
        return head;
    }

    @Override
    public @NotNull List<T> tail(){
        if(head == null){
            throw new NoSuchElementException("No head");
        }
        return tail;
    }

    @Override
    public boolean isEmpty(){
        return empty;
    }

    @Override
    public @NotNull List<T> reverse() {
        List<T> reverse = List.nil();
        List<T> actual = this;
        while(!actual.isEmpty()){
            reverse = List.cons(actual.head(), reverse);
            actual = actual.tail();
        }
        return reverse;
    }
}
