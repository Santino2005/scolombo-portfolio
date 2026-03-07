package algorithms.stack;

import org.jetbrains.annotations.NotNull;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class LinkedListStack<E> implements algorithms.stack.Stack<E>{
    private int size;
    private int Accion;

    private class Node<E> {
        E item;
        Node<E> Next;
    }
        Node<E> initial = new Node();
        Node<E> last = initial;

    @Override
    public void push(@NotNull E item){
        if(initial.item == null) {
            initial.item = item;
        }
        else {
            last = initial;
            initial = new Node();
            initial.item = item;
            initial.Next = last;
        }
        size++;
        Accion++;
    }

    public E pop() {
        E end;
        if (isEmpty()){
            throw new NoSuchElementException("Empty");
        }else{
            end = initial.item;
            initial = last;
            if(size > 1)
                last = last.Next;
        }
        size--;
        Accion++;
        return end;
    }

    public boolean isEmpty() {
        if(size == 0)
            return true;
        else
            return false;
    }

    public int size(){
        return size;
    }
    @Override
    public Iterator<E> iterator(){
        return new LinkedListStackIterator();
    }
    private class LinkedListStackIterator implements Iterator<E>{
        private Node<E> current = initial;
        private Node<E> NewNode;
        int size = size();
        public boolean hasNext(){
            return current != null;
        }

        public E next(){
            if(!hasNext() || current == null){
                throw new NoSuchElementException("No Next");
            }else{
                NewNode = current;
                current = current.Next;
                size--;
                return NewNode.item;
            }
        }
    }
}
